package net.emirozturk;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
enum AracTuru {Sedan, HatchBack, Kamyon}

interface Arac {
    char getModel();

    void parkSuresiBelirle();

    void beklemeSuresiBelirle();

    void setPlaka(String plaka);

    int getBeklemeSuresi();

    String getPlaka();

    void parkSuresiAzalt();

    void beklemeSuresiAzalt();

    int getParkSuresi();
}

class MotorluTasit implements Arac {
    int beklemeSuresi;
    int parkSuresi;
    char model;
    String plaka = null;

    public char getModel() {
        return model;
    }

    public void parkSuresiBelirle() {
        parkSuresi = new Random().nextInt(5) + 1;
    }

    public void beklemeSuresiBelirle() {
        beklemeSuresi = new Random().nextInt(2) + 1;
    }

    public void setPlaka(String plaka) {
        this.plaka = plaka;
    }

    public int getBeklemeSuresi() {
        return beklemeSuresi;
    }

    public int getParkSuresi(){
        return parkSuresi;
    }
    public String getPlaka() {
        return plaka;
    }

    public void parkSuresiAzalt() {
        parkSuresi--;
    }
    public void beklemeSuresiAzalt(){
        beklemeSuresi--;
    }
}

class Bireysel extends MotorluTasit {
}

class Sedan extends Bireysel {
    public Sedan(String plaka) {
        this.plaka = plaka;
        model = 'S';
        parkSuresiBelirle();
        beklemeSuresiBelirle();
    }
}

class HatchBack extends Bireysel {
    public HatchBack(String plaka) {
        this.plaka = plaka;
        model = 'H';
        parkSuresiBelirle();
        beklemeSuresiBelirle();
    }
}

class Kamyon extends MotorluTasit {
    public Kamyon(String plaka) {
        this.plaka = plaka;
        model = 'K';
        parkSuresiBelirle();
        beklemeSuresiBelirle();
    }

    public void parkSuresiBelirle() {
        parkSuresi = new Random().nextInt(5) + 5;
    }

    public void beklemeSuresiBelirle() {
        beklemeSuresi = new Random().nextInt(5) + 5;
    }
}

class Otopark {
    Arac[] araclar;

    public Otopark(int otoparkBuyuklugu) {
        araclar = new Arac[otoparkBuyuklugu];
    }

    public Arac[] getAraclar() {
        return araclar;
    }

    public void gidenleriHesapla() {
        for (int i = 0; i < araclar.length; i++)
            if(araclar[i]!=null)
                if (araclar[i].getParkSuresi() == 0)
                {
                    System.out.println(araclar[i].getPlaka()+" plakalı "+araclar[i].getModel()+" araç gitti.");
                    araclar[i] = null;
                }
    }

    public int bosYerSayisiAl() {
        int sayi = 0;
        for (Arac arac : araclar)
            if (arac == null)
                sayi++;
        return sayi;
    }

    public void aracEkle(ArrayList<Arac> otoparkaGecenler) {
        for (Arac arac : otoparkaGecenler)
            System.out.println(ekle(arac));
    }

    private int bosYerBul() {
        for (int i = 0; i < araclar.length; i++)
            if (araclar[i] == null)
                return i;
        return -1;
    }

    public String ekle(Arac arac) {
        int yer = bosYerBul();
        araclar[yer]=arac;
        return arac.getPlaka()+" plakalı "+arac.getModel()+" araç "+yer+" yerine yerleşti.";
    }

    public void sureDus() {
        for(Arac arac:araclar)
            if(arac!=null)
                arac.parkSuresiAzalt();
    }
}

class Kuyruk {
    private ArrayList<Arac> kuyruk;
    private int kuyrukBuyuklugu;

    public Kuyruk(int kuyrukBuyuklugu) {
        this.kuyrukBuyuklugu = kuyrukBuyuklugu;
        kuyruk = new ArrayList<Arac>();
    }

    public void gidenleriHesapla() {
        for(Arac a:kuyruk)
            if(a.getBeklemeSuresi()==0)
                System.out.println(a.getPlaka() + " plakalı "+a.getModel()+" araç kuyrukta beklemekten sıkıldı.");
        kuyruk.removeIf(x->x.getBeklemeSuresi()==0);
    }

    public boolean yerVarMi() {
        return kuyruk.size() < kuyrukBuyuklugu;
    }

    public String ekle(Arac arac) {
        if (yerVarMi()) {
            kuyruk.add(arac);
            return arac.getPlaka() + " plakalı "+arac.getModel()+" araç kuyruğa girdi.";
        } else
            return arac.getPlaka() + " plakalı "+arac.getModel()+" araç beklemeden gitti.";
    }

    public ArrayList<Arac> al(int bosYerSayisi) {
        ArrayList<Arac> siradakiler = new ArrayList<Arac>();
        int min = Math.min(kuyruk.size(), bosYerSayisi);
        for (int i = 0; i < min; i++) {
            siradakiler.add(kuyruk.get(0));
            kuyruk.remove(0);
        }
        return siradakiler;
    }

    public void sureDus() {
        for(Arac a:kuyruk)
            a.beklemeSuresiAzalt();
    }

    public ArrayList<Arac> getAraclar() {
        return kuyruk;
    }
}

class Simulator {
    private Otopark otopark;
    private Kuyruk kuyruk;
    private int maxAracSayisi;

    public Simulator(int otoparkBuyuklugu, int kuyrukBuyuklugu, int maxAracSayisi) {
        otopark = new Otopark(otoparkBuyuklugu);
        kuyruk = new Kuyruk(kuyrukBuyuklugu);
        this.maxAracSayisi = maxAracSayisi;
    }

    public void baslat(int adimSayisi, int interval) throws InterruptedException {
        for (int i = 0; i < adimSayisi; i++) {
            konsoluTemizle();
            ArrayList<Arac> aracListesi = rastgeleAracTut(maxAracSayisi);
            durumGuncelle(aracListesi);
            cizdir();
            Thread.sleep(interval);
        }
    }

    private void durumGuncelle(ArrayList<Arac> aracListesi) {
        otopark.sureDus();
        kuyruk.sureDus();
        otopark.gidenleriHesapla();
        kuyruk.gidenleriHesapla();
        for (Arac arac : aracListesi){
            int bosYerSayisi = otopark.bosYerSayisiAl();
            if(bosYerSayisi>0)
                System.out.println(otopark.ekle(arac));
            else
                System.out.println(kuyruk.ekle(arac));
        }
        int bosYerSayisi = otopark.bosYerSayisiAl();
        if(bosYerSayisi>0)
        {
            ArrayList<Arac> otoparkaGecenler = kuyruk.al(bosYerSayisi);
            otopark.aracEkle(otoparkaGecenler);
        }
    }

    public static void konsoluTemizle() {
        System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    private ArrayList<Arac> rastgeleAracTut(int maxAracSayisi) {
        ArrayList<Arac> aracListesi = new ArrayList<Arac>();
        int aracSayisi = new Random().nextInt(maxAracSayisi);
        for (int i = 0; i < aracSayisi; i++) {
            Arac yeniArac;
            AracTuru tur = AracTuru.values()[new Random().nextInt(AracTuru.values().length)];
            if (tur == AracTuru.HatchBack)
                yeniArac = new HatchBack(PlakaOlustur());
            else if (tur == AracTuru.Sedan)
                yeniArac = new Sedan(PlakaOlustur());
            else if (tur == AracTuru.Kamyon)
                yeniArac = new Kamyon(PlakaOlustur());
            else
                yeniArac = new MotorluTasit();
            aracListesi.add(yeniArac);
        }
        return aracListesi;
    }

    private String PlakaOlustur() {
        Random r = new Random();
        int ilkKisim = r.nextInt(80) + 1;
        int sonKisim = r.nextInt(899) + 100;
        byte[] dizi = new byte[2];
        dizi[0] = (byte) (r.nextInt(25)+65);
        dizi[1] = (byte) (r.nextInt(25)+65);
        String araKisim = new String(dizi, StandardCharsets.UTF_8);
        return ilkKisim + araKisim + sonKisim;
    }

    private void cizdir() {
        Arac[] araclar = otopark.getAraclar();
        ArrayList<Arac> kuyruktakiler = kuyruk.getAraclar();
        int sayac = 0;
        for (Arac arac : araclar) {
            if (arac == null)
                System.out.print("| |");
            else
                System.out.print("|" + arac.getModel() + "|");
            if (++sayac % 10 == 0)
                System.out.println("\n");
        }
        System.out.print("\n==============================\n");
        for(Arac arac: kuyruktakiler)
            System.out.print(arac.getModel()+" ");
        System.out.print("\n==============================\n");
    }
}

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Simulator yeniSimulator = new Simulator(15, 5, 8);
        yeniSimulator.baslat(30, 100);
    }
}
