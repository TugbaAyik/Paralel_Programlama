# Paralel Point-In-Polygon (Poligon İçerisinde Nokta Bulma)

## Proje Hakkında

Bu projede, verilen bir noktanın belirli bir poligonun içerisinde bulunup bulunmadığı **paralel programlama** kullanılarak belirlenmiştir.

Projenin amacı, klasik Point-In-Polygon (PIP) problemini Java Thread yapısını kullanarak paralel hale getirmek ve sıralı (sequential) çözüm ile paralel çözümün performanslarını karşılaştırmaktır.

Bu çalışma, Paralel Programlama dersi kapsamında gerçekleştirilmiştir.

---

# Problem Tanımı

Verilenler:

* x ve y koordinatlarından oluşan bir poligon
* Test edilmek istenen bir nokta

Amaç:

Bu noktanın poligonun içerisinde mi yoksa dışında mı olduğunu belirlemektir.

Program hem **konveks (convex)** hem de **konkav (concave)** poligonlar üzerinde çalışabilmektedir.

---

# Kullanılan Algoritma

Bu projede **Ray Casting (Işın İzleme)** algoritması kullanılmıştır.

## Çalışma Mantığı

Test edilmek istenen noktadan sağa doğru sonsuz uzunlukta hayali bir ışın gönderilir.

Bu ışının poligon kenarları ile yaptığı kesişim sayısı hesaplanır.

Kurallar:

* Kesişim sayısı tek ise nokta poligonun içindedir.
* Kesişim sayısı çift ise nokta poligonun dışındadır.

Algoritmanın zaman karmaşıklığı:

```text
O(n)
```

Burada n, poligonun kenar sayısını ifade etmektedir.

---

# Paralel Çözüm

Poligonun kenarları dört eşit parçaya bölünmüştür.

Her parça farklı bir iş parçacığı (Thread) tarafından işlenmiştir.

```text
Thread-1 → İlk %25 kenar
Thread-2 → İkinci %25 kenar
Thread-3 → Üçüncü %25 kenar
Thread-4 → Son %25 kenar
```

Her iş parçacığı kendi sorumluluğundaki kenarlar üzerinde kesişim hesabını yapmıştır.

Bulunan sonuçlar Java'nın **AtomicInteger** sınıfı kullanılarak güvenli şekilde birleştirilmiştir.

Bu sayede aynı işlem birden fazla işlemci çekirdeği üzerinde paralel olarak çalıştırılmıştır.

---

# Kullanılan Teknolojiler

* Java
* Multithreading (Çok İş Parçacıklı Programlama)
* AtomicInteger
* Ray Casting Algoritması

---

# Proje Yapısı

```text
Proje/
│
├── ParallelPointInPolygon.java
├── README.md
└── Rapor.pdf
```

---

# Derleme ve Çalıştırma

Projeyi derlemek için:

```bash
javac ParallelPointInPolygon.java
```

Projeyi çalıştırmak için:

```bash
java ParallelPointInPolygon
```

---

# Örnek Çıktı

```text
Sequential: true
Parallel: true

Sequential Time(ns): 1246000
Parallel Time(ns): 1842400

Speedup: 0.67
```

Büyük veri kümelerinde örnek çıktı:

```text
Sequential: true
Parallel: true

Sequential Time(ns): 45300000
Parallel Time(ns): 18400000

Speedup: 2.46
```

---

# Performans Analizi

Hızlanma katsayısı (Speedup) aşağıdaki formül ile hesaplanmaktadır:

```text
Speedup = Sequential Time / Parallel Time
```

Yorum:

* Speedup > 1 ise paralel çözüm daha hızlıdır.
* Speedup = 1 ise performanslar eşittir.
* Speedup < 1 ise Thread oluşturma maliyetleri nedeniyle paralel çözüm avantaj sağlayamamıştır.

Küçük veri kümelerinde paralelleştirmenin maliyeti yüksek olabilir.

Veri boyutu büyüdükçe paralel çözümün avantajı daha belirgin hale gelmektedir.

---

# Bellek Kısıtları

Çok büyük veri kümeleri kullanıldığında aşağıdaki hata ile karşılaşılabilir:

```text
java.lang.OutOfMemoryError: Java heap space
```

Bu hata, poligon noktalarının bellekte tutulması için yeterli RAM bulunmadığında ortaya çıkmaktadır.

Çözüm olarak:

* JVM heap boyutu artırılabilir.
* Nokta sayısı azaltılabilir.
* Noktalar bellekte tutulmak yerine dinamik olarak üretilebilir.

Örnek:

```bash
java -Xmx8G ParallelPointInPolygon
```

---

# Sonuç

Bu projede Point-In-Polygon problemi paralel programlama yaklaşımı ile çözülmüştür.

Ray Casting algoritması kullanılarak sıralı ve paralel çözümler karşılaştırılmıştır.

Yapılan deneyler sonucunda, veri boyutu arttıkça paralel programlamanın performans açısından önemli avantajlar sağladığı gözlemlenmiştir.

Bu çalışma, geometrik problemlerin çözümünde paralel programlama tekniklerinin etkili bir şekilde kullanılabileceğini göstermektedir.


