import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

// ============================================================================
// 1. ENUM KATMANI (VERI STANDARTLARI)
// ============================================================================

enum Category { PANTOLON, GOMLEK, CEKET, ETEK, AYAKKABI, AKSESUAR, DIS_GIYIM }
enum Season { YAZ, KIS, ILKBAHAR, SONBAHAR, TUM_MEVSIMLER }
enum WeatherType { GUNESLI, YAGMURLU, KARLI, RUZGARLI, BULUTLU }
enum EventType { IS_TOPLANTISI, DATE_NIGHT, BALO, GUNLUK, SPOR, SEYAHAT }

// Detayli Nitelikler
enum FitStyle { MASKULEN, FEMINEN, UNISEX, OVERSIZE, SLIM }
enum PatternType { BASIC, MOTIFLI, CIZGILI, KARELI }
enum Vibe { IDDIALI, OTURAKLI, RAHAT, ASI, MINIMAL }
enum Material { PAMUK, YUN, KETEN, DENIM, DERI, SUET, SENTETIK }
enum SeasonalPalette { KIS_KADINI, YAZ_KADINI, SONBAHAR_KADINI, ILKBAHAR_KADINI }

// ============================================================================
// 2. MODEL KATMANI (VARLIKLAR)
// ============================================================================

// --- ANA KIYAFET SINIFI (BUILDER PATTERN) ---
class ClothingItem {
    private final String id;
    
    // Zorunlu Ana Bilesenler
    private String color;
    private String size;
    private String fabricRatio; 
    private Category category;
    private String brand;

    // Opsiyonel / Isaretlenebilir Ozellikler
    private Material material;
    private PatternType pattern;
    private FitStyle fit;       
    private Vibe vibe;          
    private Set<Season> suitableSeasons;
    private String personalNote;
    private String photoUrl;

    // Sistem Verileri
    private LocalDate dateAdded;
    private LocalDate lastWornDate;
    private int wearCount;
    private double purchasePrice; 
    
    // Sosyal Durum
    private boolean isBorrowed;
    private String borrowedBy;

    private ClothingItem(Builder builder) {
        this.id = UUID.randomUUID().toString();
        this.color = builder.color;
        this.size = builder.size;
        this.fabricRatio = builder.fabricRatio;
        this.category = builder.category;
        this.brand = builder.brand;
        this.material = builder.material;
        this.pattern = builder.pattern;
        this.fit = builder.fit;
        this.vibe = builder.vibe;
        this.suitableSeasons = builder.suitableSeasons;
        this.personalNote = builder.personalNote;
        this.photoUrl = builder.photoUrl;
        this.purchasePrice = builder.price;
        
        this.dateAdded = LocalDate.now();
        this.lastWornDate = builder.lastWornDate != null ? builder.lastWornDate : LocalDate.now();
        this.wearCount = 0;
        this.isBorrowed = false;
    }

    // Islemler
    public void wear() {
        this.lastWornDate = LocalDate.now();
        this.wearCount++;
    }

    public void markAsBorrowed(String friendName) {
        this.isBorrowed = true;
        this.borrowedBy = friendName;
    }

    public double getCostPerWear() {
        return wearCount == 0 ? purchasePrice : purchasePrice / wearCount;
    }

    // Getterlar
    public String getName() { return brand + " " + color + " " + category; }
    public Category getCategory() { return category; }
    public String getColor() { return color; }
    public Material getMaterial() { return material; }
    public Vibe getVibe() { return vibe; }
    public Set<Season> getSuitableSeasons() { return suitableSeasons; }
    public LocalDate getLastWornDate() { return lastWornDate; }
    public boolean isBorrowed() { return isBorrowed; }
    public PatternType getPattern() { return pattern; }
    public FitStyle getFit() { return fit; }

    @Override
    public String toString() {
        return String.format("%s [%s, %s] (Not: %s)", getName(), fit, vibe, personalNote);
    }

    // --- BUILDER CLASS ---
    public static class Builder {
        // Zorunlu
        private final Category category;
        private final String color;
        private final String size;
        private final String fabricRatio;
        private final String brand;

        // Opsiyonel (Varsayilanli)
        private Material material = Material.PAMUK;
        private PatternType pattern = PatternType.BASIC;
        private FitStyle fit = FitStyle.UNISEX;
        private Vibe vibe = Vibe.OTURAKLI;
        private Set<Season> suitableSeasons = new HashSet<>();
        private String personalNote = "";
        private String photoUrl = "";
        private double price = 0.0;
        private LocalDate lastWornDate;

        public Builder(Category category, String color, String size, String fabricRatio, String brand) {
            this.category = category; this.color = color; this.size = size; 
            this.fabricRatio = fabricRatio; this.brand = brand;
        }

        public Builder setMaterial(Material m) { this.material = m; return this; }
        public Builder setPattern(PatternType p) { this.pattern = p; return this; }
        public Builder setFit(FitStyle f) { this.fit = f; return this; }
        public Builder setVibe(Vibe v) { this.vibe = v; return this; }
        public Builder addSeason(Season s) { this.suitableSeasons.add(s); return this; }
        public Builder setNote(String n) { this.personalNote = n; return this; }
        public Builder setPhoto(String url) { this.photoUrl = url; return this; }
        public Builder setPrice(double p) { this.price = p; return this; }
        public Builder setLastWorn(LocalDate d) { this.lastWornDate = d; return this; }

        public ClothingItem build() { return new ClothingItem(this); }
    }
}

// --- CANVAS & KOMBIN YAPILARI ---
class CanvasItem {
    private ClothingItem item;
    private double x, y; // Tuval koordinatlari
    private int layer;   // Katman sirasi
    
    public CanvasItem(ClothingItem item, double x, double y, int layer) {
        this.item = item; this.x = x; this.y = y; this.layer = layer;
    }
    public ClothingItem getItem() { return item; }
    @Override public String toString() { return item.getName() + " (X:" + x + ", Y:" + y + ")"; }
}

class Outfit {
    private String name;
    private EventType eventType;
    private LocalDate plannedDate;
    private List<CanvasItem> items;

    public Outfit(String name, EventType eventType, List<CanvasItem> items) {
        this.name = name; this.eventType = eventType; this.items = items;
    }
    public void setPlannedDate(LocalDate date) { this.plannedDate = date; }
    public List<CanvasItem> getItems() { return items; }
    public EventType getEventType() { return eventType; }
    
    @Override public String toString() { return "Kombin: " + name + " (" + eventType + ") Tarih: " + plannedDate; }
}

class Lookbook {
    private String title;
    private List<Outfit> outfits = new ArrayList<>();
    public Lookbook(String title) { this.title = title; }
    public void addOutfit(Outfit o) { outfits.add(o); }
    public void show() { System.out.println("Koleksiyon: " + title + " (" + outfits.size() + " Parca)"); }
}

// --- PHYGITAL KUTU ---
class StorageBox {
    private String qrCode;
    private String label;
    private List<ClothingItem> contents = new ArrayList<>();
    
    public StorageBox(String label) {
        this.label = label;
        this.qrCode = "QR-" + UUID.randomUUID().toString().substring(0,6);
    }
    public void addItem(ClothingItem item) { contents.add(item); }
    public String getQrCode() { return qrCode; }
    public String getLabel() { return label; }
    public List<ClothingItem> getContents() { return contents; }
}

// ============================================================================
// 3. INTERFACE & STRATEGY PATTERN (ESNEKLIK VE MIMARI)
// ============================================================================

interface SuggestionStrategy {
    void suggest(List<ClothingItem> wardrobe);
}

// Strateji 1: Ruh Haline Gore Oneri
class MoodStrategy implements SuggestionStrategy {
    private Vibe targetVibe;
    public MoodStrategy(Vibe vibe) { this.targetVibe = vibe; }
    
    @Override
    public void suggest(List<ClothingItem> wardrobe) {
        System.out.println("\n[Vibe Strategy] Modun: " + targetVibe);
        wardrobe.stream()
            .filter(i -> i.getVibe() == targetVibe && !i.isBorrowed())
            .forEach(i -> System.out.println("   -> Oneri: " + i.getName()));
    }
}

// Strateji 2: Hava Durumuna Gore Oneri
class WeatherStrategy implements SuggestionStrategy {
    private WeatherType weather;
    public WeatherStrategy(WeatherType w) { this.weather = w; }

    @Override
    public void suggest(List<ClothingItem> wardrobe) {
        System.out.println("\n[Weather Strategy] Hava: " + weather);
        wardrobe.stream()
            .filter(i -> {
                if(weather == WeatherType.YAGMURLU && i.getMaterial() == Material.SUET) return false;
                return true;
            })
            .limit(3)
            .forEach(i -> System.out.println("   -> Guvenli Secim: " + i.getName()));
    }
}

// ============================================================================
// 4. SERVICE KATMANI (IS MANTIGI)
// ============================================================================

// 1. Goruntu Isleme & Toplu Tarama
class VisionService {
    public List<ClothingItem> scanBatch(String imagePath) {
        System.out.println("\n[AI Vision] Tarama: " + imagePath);
        // Simulasyon
        System.out.println("   -> Analiz: 2 parca tespit edildi.");
        System.out.println("   -> [Soru]: Parlayan cisim bir 'Siyah Kazak' mi? (Guven: %65)");
        System.out.println("   -> [Kullanici]: Evet.");
        
        List<ClothingItem> items = new ArrayList<>();
        items.add(new ClothingItem.Builder(Category.DIS_GIYIM, "Siyah", "M", "%100 Yun", "Zara").build());
        items.add(new ClothingItem.Builder(Category.PANTOLON, "Mavi", "32", "Denim", "Levis").build());
        return items;
    }
}

// 2. Stilist & Baglamsal Oneri (AI)
class StylistService {
    public void suggestOutfit(List<ClothingItem> wardrobe, WeatherType weather, EventType event) {
        System.out.println("\n[AI Stylist] Analiz: " + weather + " + " + event);
        
        wardrobe.stream()
            // Baglamsal Filtreleme
            .filter(i -> {
                // Yagmurda Suet Olmaz
                if(weather == WeatherType.YAGMURLU && i.getMaterial() == Material.SUET) return false;
                // Resmi ortamda Yirtik/Asi olmaz
                if(event == EventType.IS_TOPLANTISI && i.getVibe() == Vibe.ASI) return false;
                return true;
            })
            // Renk Uyumu (Simulasyon)
            .limit(2)
            .forEach(i -> System.out.println("   -> Secim: " + i.getName()));
    }
}

// 3. Bildirim & Surdurulebilirlik (30 Gun Kurali)
class NotificationService {
    public void checkHealth(List<ClothingItem> wardrobe, Season currentSeason) {
        System.out.println("\n[Hatirlatici] Detoks Analizi (" + currentSeason + ")");
        LocalDate today = LocalDate.now();
        
        wardrobe.stream()
            .filter(i -> i.getSuitableSeasons().contains(currentSeason)) // Mevsim Uygun
            .filter(i -> ChronoUnit.DAYS.between(i.getLastWornDate(), today) > 30) // 30 Gundur Giyilmedi
            .forEach(i -> {
                System.out.println("   [UYARI]: '" + i.getName() + "' parcasini uzun suredir giymedin.");
                System.out.println("            (Son Giyilme: " + i.getLastWornDate() + ")");
            });
    }
}

// 4. Akilli Alisveris (Compatibility)
class ShoppingAssistantService {
    public void checkCompatibility(ClothingItem targetItem, List<ClothingItem> wardrobe) {
        System.out.println("\n[Alisveris Asistani] '" + targetItem.getName() + "' almali misin?");
        long compatibleCount = wardrobe.stream()
            .filter(i -> isColorMatch(i.getColor(), targetItem.getColor()))
            .count();
        
        if(compatibleCount > 0) System.out.println("   [+] Evet! Dolabinda " + compatibleCount + " parca ile uyar.");
        else System.out.println("   [-] Hayir! Dolabindaki hicbir seyle uymuyor.");
    }
    
    private boolean isColorMatch(String c1, String c2) {
        // Basit mantik: Siyah her seye uyar
        return c1.equals("Siyah") || c2.equals("Siyah") || !c1.equals(c2);
    }
}

// 5. Organizasyon (QR)
class OrganizationService {
    private List<StorageBox> boxes = new ArrayList<>();
    public StorageBox createBox(String label, List<ClothingItem> items) {
        StorageBox box = new StorageBox(label);
        items.forEach(box::addItem);
        boxes.add(box);
        System.out.println("\n[QR Kutu] Olusturuldu: " + label + " | Kod: " + box.getQrCode());
        return box;
    }
}

// 6. Sosyal & Renk Analizi
class SocialService {
    public void lendItem(ClothingItem item, String friend) {
        item.markAsBorrowed(friend);
        System.out.println("\n[Sosyal] '" + item.getName() + "' -> " + friend + " kisisine verildi.");
    }
}

class ColorAnalysisService {
    public void analyze(List<ClothingItem> wardrobe, SeasonalPalette palette) {
        System.out.println("\n[Renk Analizi] Tipin: " + palette);
        // Ornek: Kis kadinina Siyah yakisir
        if(palette == SeasonalPalette.KIS_KADINI) {
            wardrobe.stream().filter(i -> i.getColor().equals("Siyah"))
                    .forEach(i -> System.out.println("   [*] Yildiz Parca: " + i.getName()));
        }
    }
}

class TravelService {
    public void packLuggage(String city, int days, WeatherType weather) {
        System.out.println("\n[Bavul] " + city + " (" + days + " Gun) - Hava: " + weather);
        System.out.println("   [+] Kapsul liste hazirlaniyor...");
    }
}

// ============================================================================
// 5. MAIN (UYGULAMA SENARYOSU)
// ============================================================================

public class HedyMasterApp {
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("          HEDY - ULTRA YASAM ASISTANI     ");
        System.out.println("==========================================\n");

        // 1. HAZIRLIK: Manuel Veri Girisi (Builder Pattern)
        List<ClothingItem> wardrobe = new ArrayList<>();
        
        ClothingItem blazer = new ClothingItem.Builder(Category.CEKET, "Siyah", "M", "%100 Yun", "Massimo Dutti")
                .setFit(FitStyle.MASKULEN).setVibe(Vibe.IDDIALI).setMaterial(Material.YUN)
                .addSeason(Season.KIS).addSeason(Season.SONBAHAR)
                .setPrice(5000)
                .setLastWorn(LocalDate.now().minusDays(45)) // 45 gun once giyildi
                .build();

        ClothingItem jeans = new ClothingItem.Builder(Category.PANTOLON, "Mavi", "32", "Denim", "Mavi Jeans")
                .setFit(FitStyle.UNISEX).setPattern(PatternType.BASIC).setVibe(Vibe.RAHAT)
                .addSeason(Season.TUM_MEVSIMLER)
                .setLastWorn(LocalDate.now().minusDays(2))
                .build();
        
        ClothingItem suedeShoes = new ClothingItem.Builder(Category.AYAKKABI, "Kahve", "42", "Suet", "Divarese")
                .setMaterial(Material.SUET).setVibe(Vibe.OTURAKLI)
                .addSeason(Season.SONBAHAR)
                .build();

        wardrobe.add(blazer);
        wardrobe.add(jeans);
        wardrobe.add(suedeShoes);

        // 2. TOPLU TARAMA (AI Simulasyonu)
        VisionService vision = new VisionService();
        wardrobe.addAll(vision.scanBatch("raf_resmi.jpg"));

        // 3. CANVAS & KOMBIN PLANLAMA (Ileri Tarihli)
        System.out.println("\n[Canvas] Kombin Yapiliyor...");
        List<CanvasItem> canvasItems = new ArrayList<>();
        canvasItems.add(new CanvasItem(blazer, 50, 100, 2)); // Ceket Ustte
        canvasItems.add(new CanvasItem(jeans, 50, 300, 1));  // Jean Altta
        
        Outfit meetingLook = new Outfit("Buyuk Sunum", EventType.IS_TOPLANTISI, canvasItems);
        meetingLook.setPlannedDate(LocalDate.now().plusDays(5)); // 5 Gun sonraya planla
        
        Lookbook winterFavs = new Lookbook("Kis Favorileri");
        winterFavs.addOutfit(meetingLook);
        winterFavs.show();

        // 4. BAGLAMSAL ONERI (Hava & Etkinlik)
        StylistService stylist = new StylistService();
        // Senaryo: Yagmurlu havada Is Toplantisi
        stylist.suggestOutfit(wardrobe, WeatherType.YAGMURLU, EventType.IS_TOPLANTISI);
        // Beklenti: Suet Ayakkabi onerilmemeli!

        // 5. STRATEJI DESENI KULLANIMI (Strategy Pattern)
        // Ruh haline gore:
        SuggestionStrategy myStrategy = new MoodStrategy(Vibe.RAHAT);
        myStrategy.suggest(wardrobe);
        
        // Hava durumuna gore (Polymorphism):
        myStrategy = new WeatherStrategy(WeatherType.YAGMURLU);
        myStrategy.suggest(wardrobe);

        // 6. BILDIRIM SISTEMI (Mevsim & Sure)
        NotificationService notifier = new NotificationService();
        notifier.checkHealth(wardrobe, Season.KIS);
        // Beklenti: Blazer (Kislik ve 45 gun giyilmedi) -> UYARI VERMELI.

        // 7. PHYGITAL & SOSYAL & ANALIZ (Ekstralar)
        OrganizationService org = new OrganizationService();
        org.createBox("Yazliklar 2026", Arrays.asList(jeans));
        
        SocialService social = new SocialService();
        social.lendItem(jeans, "Ali"); // Jean odunc verildi
        
        ColorAnalysisService color = new ColorAnalysisService();
        color.analyze(wardrobe, SeasonalPalette.KIS_KADINI); // Siyah blazer onerilir

        ShoppingAssistantService shop = new ShoppingAssistantService();
        ClothingItem badItem = new ClothingItem.Builder(Category.ETEK, "Neon Pembe", "S", "Poly", "Zara").build();
        shop.checkCompatibility(badItem, wardrobe);

        // 8. BAVUL HAZIRLAMA 
        TravelService travel = new TravelService();
        travel.packLuggage("Paris", 3, WeatherType.KARLI);
    }
}
