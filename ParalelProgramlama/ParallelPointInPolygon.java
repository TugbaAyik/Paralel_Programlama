import java.util.concurrent.atomic.AtomicInteger;

public class ParallelPointInPolygon {

    static class Point {
        double x, y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Worker extends Thread {

        Point[] polygon;
        Point testPoint;

        int start;
        int end;

        AtomicInteger intersections;

        Worker(Point[] polygon, Point testPoint,
               int start, int end,
               AtomicInteger intersections) {

            this.polygon = polygon;
            this.testPoint = testPoint;
            this.start = start;
            this.end = end;
            this.intersections = intersections;
        }

        @Override
        public void run() {

            int count = 0;

            int n = polygon.length;

            for (int i = start; i < end; i++) {

                int j = (i + 1) % n;

                if (((polygon[i].y > testPoint.y) !=
                     (polygon[j].y > testPoint.y))
                        &&
                    (testPoint.x <
                     (polygon[j].x - polygon[i].x)
                     * (testPoint.y - polygon[i].y)
                     / (polygon[j].y - polygon[i].y)
                     + polygon[i].x)) {

                    count++;
                }
            }

            intersections.addAndGet(count);
        }
    }

    public static void main(String[] args)
            throws InterruptedException {

        int vertexCount = 50000000;

        Point[] polygon = new Point[vertexCount];

        for (int i = 0; i < vertexCount; i++) {

            double angle = 2 * Math.PI * i / vertexCount;

            polygon[i] =
                    new Point(
                            100 * Math.cos(angle),
                            100 * Math.sin(angle));
        }

        Point testPoint = new Point(10, 10);

        long startSequential = System.nanoTime();

        int seqCount = 0;

        for (int i = 0; i < vertexCount; i++) {

            int j = (i + 1) % vertexCount;

            if (((polygon[i].y > testPoint.y)
                    !=
                    (polygon[j].y > testPoint.y))
                    &&
                    (testPoint.x <
                            (polygon[j].x - polygon[i].x)
                            * (testPoint.y - polygon[i].y)
                            / (polygon[j].y - polygon[i].y)
                            + polygon[i].x)) {

                seqCount++;
            }
        }

        boolean seqInside = seqCount % 2 == 1;

        long endSequential = System.nanoTime();

        long seqTime =
                endSequential - startSequential;

        AtomicInteger intersections =
                new AtomicInteger(0);

        int threadCount = 4;

        Worker[] workers =
                new Worker[threadCount];

        int chunk =
                vertexCount / threadCount;

        long startParallel =
                System.nanoTime();

        for (int i = 0; i < threadCount; i++) {

            int start =
                    i * chunk;

            int end =
                    (i == threadCount - 1)
                            ? vertexCount
                            : start + chunk;

            workers[i] =
                    new Worker(
                            polygon,
                            testPoint,
                            start,
                            end,
                            intersections);

            workers[i].start();
        }

        for (Worker w : workers)
            w.join();

        boolean parallelInside =
                intersections.get() % 2 == 1;

        long endParallel =
                System.nanoTime();

        long parallelTime =
                endParallel - startParallel;

        double speedup =
                (double) seqTime /
                        parallelTime;

        System.out.println("Sequential: "
                + seqInside);

        System.out.println("Parallel: "
                + parallelInside);

        System.out.println("Sequential Time(ns): "
                + seqTime);

        System.out.println("Parallel Time(ns): "
                + parallelTime);

        System.out.println("Speedup: "
                + speedup);
    }
}