# HEDY - Akıllı Gardırop ve Yaşam Asistanı

HEDY, gardırop yönetimini dijitalleştiren, bağlamsal verilere dayalı stil kararlarını otomatize eden ve sürdürülebilirlik analizleri sunan Java tabanlı bir arka uç (backend) motorudur. Geleneksel envanter uygulamalarının aksine HEDY; **Nesne Yönelimli Programlama (OOP)** prensipleri ve **Tasarım Desenleri (Design Patterns)** kullanılarak geliştirilmiş, ölçeklenebilir ve sürdürülebilir bir karar destek sistemi olarak işlev görür.

## Proje Özeti

Bu projenin temel amacı, bir kıyafetin satın alınmasından elden çıkarılmasına kadar olan yaşam döngüsünü simüle eden güçlü bir yazılım mimarisi oluşturmaktır. Sistem; çevresel faktörleri (hava durumu), sosyal bağlamı (etkinlik türü) ve kullanıcı psikolojisini (ruh hali) entegre ederek akıllı kıyafet önerileri üretir.

Bu depo (repository); temel iş mantığını, veri modellerini ve servis katmanlarını içermekte olup, herhangi bir ön yüz istemcisi (Mobil veya Web) ve kalıcı veri katmanı (Veritabanı) ile entegre edilmeye hazırdır.

## Fonksiyonel Özellikler

Sistem, aşağıdaki temel fonksiyonları yerine getirmek üzere tasarlanmıştır:

### 1. Bağlamsal Öneri Motoru
Uygulama, gardırop envanterini dış kısıtlamalara göre filtreleyerek analiz eder:
- **Hava Durumu Analizi:** Malzeme hasarını önler (örneğin, yağmurlu havalarda süet ürünlerin elenmesi) ve termal konfor sağlar.
- **Etkinlik Uyumluluğu:** Kıyafetleri sosyal ortama göre filtreler (örneğin, iş toplantıları için uygun olmayan parçaların elenmesi).

### 2. Ruh Hali Tabanlı Stil Stratejisi
**Strategy Tasarım Deseni** kullanılarak, sistemin öneri algoritmaları çalışma zamanında dinamik olarak değiştirilebilir. "Ruh Hali Stratejisi", kullanıcının o anki psikolojik durumuna (örneğin; Güçlü, Rahat, Minimal) göre envanteri filtreler.

### 3. Alışveriş Uyumluluk Analizi
Dürtüsel satın almayı önlemek için tasarlanmış bir algoritmadır. Potansiyel bir satın alma işlemini mevcut envanter ile karşılaştırarak renk ve stil uyumluluğunu analiz eder ve fayda odaklı bir "satın al" veya "alma" önerisi sunar.

### 4. Dijital Organizasyon ve QR Takibi (Phygital)
Fiziksel depolama ile dijital takibi birleştirir. Sistem, saklama kutuları için benzersiz tanımlayıcılar (simüle edilmiş QR kodları) üreterek, kullanıcıların fiziksel erişim sağlamadan arşivlenmiş mevsimsel ürünlerin içeriğini görüntülemesine olanak tanır.

### 5. Sürdürülebilirlik ve Gardırop Sağlığı
- **Hareketsizlik Uyarıları:** Mevsime uygun olduğu halde 30 günden uzun süredir giyilmeyen ürünleri tespit eder.
- **Kullanım Başına Maliyet (CPW) Analizi:** Her bir ürünün finansal verimliliğini, satın alma fiyatı ve kullanım sıklığına göre hesaplar.

---

## Teknik Mimari

Proje, veri, mantık ve arayüz tanımları arasında endişelerin ayrılmasını (separation of concerns) sağlayan **Servis Odaklı Mimari (SOA)** yaklaşımını takip eder.

### Sistem Gereksinimleri
- **Dil:** Java (JDK 17 veya üzeri)
- **Paradigma:** Nesne Yönelimli Programlama (OOP)

### Uygulanan Tasarım Desenleri

Mimari, yaygın yazılım tasarımı problemlerini çözmek için endüstri standardı tasarım desenlerine dayanır:

#### 1. Builder Pattern (Kurucu Deseni)
*   **Bağlam:** `ClothingItem` sınıfı çok sayıda niteliğe (renk, beden, kumaş, marka, mevsim vb.) sahiptir, bu da yapıcı metodun (constructor) karmaşıklaşmasına neden olur.
*   **Çözüm:** Nesneleri adım adım oluşturmak, kod okunabilirliğini artırmak ve gerektiğinde değişmezliği (immutability) sağlamak için statik bir `Builder` iç sınıfı kullanılmıştır.

#### 2. Strategy Pattern (Strateji Deseni)
*   **Bağlam:** Kombin önerme mantığı, kullanıcı tercihine göre (Hava Durumuna göre veya Ruh Haline göre) değişiklik gösterir.
*   **Çözüm:** Bir `SuggestionStrategy` arayüzü tanımlanmıştır. `MoodStrategy` ve `WeatherStrategy` sınıfları bu arayüzü uygulayarak, çekirdek kodu değiştirmeden çalışma zamanında algoritmaların değiştirilmesine olanak tanır (Açık/Kapalı Prensibi'ne uygunluk).

### Modüler Yapı

- **Model Paketi:** Veri alanını temsil eden varlık sınıflarını (`ClothingItem`, `Outfit`, `StorageBox`) ve Enum yapılarını (`Category`, `Material`, `Season`) içerir.
- **Service Paketi:** Modelleri işleyen ve iş kurallarını yürüten mantık sınıflarını (`StylistService`, `VisionService`, `NotificationService`) içerir.
- **Interface Paketi:** Uygulamayı tanımdan ayırmak için soyutlamaları (`SuggestionStrategy`) içerir.

---


## Kurulum ve Çalıştırma

Bu proje konsol tabanlı bir Java uygulamasıdır. Simülasyonu çalıştırmak için aşağıdaki adımları izleyin.

1.  **Depoyu Klonlayın:**
    ```bash
    git clone https://github.com/KULLANICI_ADINIZ/Hedy-Smart-Life-Assistant.git
    ```

2.  **IDE ile Açın:**
    Proje klasörünü IntelliJ IDEA, Eclipse veya VS Code içerisine aktarın.

3.  **Derleyin ve Çalıştırın:**
    `src/HedyMasterApp.java` dosyasını bulun. Önceden tanımlanmış simülasyon senaryolarını yürütmek için `main` metodunu çalıştırın.

---

## Simülasyon Çıktısı

Çalıştırıldığında sistem, servislerin yeteneklerini göstermek için bir dizi otomatik test gerçekleştirir. Aşağıda konsol çıktısının bir örneği verilmiştir:

```text
==========================================
          HEDY     
==========================================

[AI Vision] Tarama: raf_resmi.jpg
   Analiz: 2 parça tespit edildi.
   
[Alışveriş Asistanı] 'Neon Yeşil Etek' almalı mısın?
   Hayır! Dolabındaki hiçbir şeyle uymuyor.

[Vibe Strategy] Modun: RAHAT
   Öneri: Mavi Jeans

[Weather Strategy] Hava: YAGMURLU
   Güvenli Seçim: Deri Bot

[QR Kutu] Oluşturuldu: Yazlıklar 2026 | Kod: QR-A7B2C9
