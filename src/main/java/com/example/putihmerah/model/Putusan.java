package com.example.putihmerah.model;

public class Putusan {

    private static int jumlahDibuat = 0;

    private String nomorPerkara;
    private String pengadilan;
    private String tanggalPutusan;
    private String namaTerdakwa;
    private int    umurTerdakwa;
    private String jenisNarkotika;
    private double beratBarangBukti;
    private String pasalDilanggar;
    private String peranTerdakwa;
    private int    vonisHukuman;
    private double vonisDenda;
    private String namaHakim;

    public Putusan() {
        jumlahDibuat++;
    }

    public Putusan(String nomorPerkara, String pengadilan, String tanggalPutusan,
                   String namaTerdakwa, int umurTerdakwa, String jenisNarkotika,
                   double beratBarangBukti, String pasalDilanggar, String peranTerdakwa,
                   int vonisHukuman, double vonisDenda, String namaHakim) {
        this.nomorPerkara     = nomorPerkara;
        this.pengadilan       = pengadilan;
        this.tanggalPutusan   = tanggalPutusan;
        this.namaTerdakwa     = namaTerdakwa;
        this.umurTerdakwa     = umurTerdakwa;
        this.jenisNarkotika   = jenisNarkotika;
        this.beratBarangBukti = beratBarangBukti;
        this.pasalDilanggar   = pasalDilanggar;
        this.peranTerdakwa    = peranTerdakwa;
        this.vonisHukuman     = vonisHukuman;
        this.vonisDenda       = vonisDenda;
        this.namaHakim        = namaHakim;
        jumlahDibuat++;
    }

    public static int getJumlahDibuat() {
        return jumlahDibuat;
    }

    public String getNomorPerkara()     { return nomorPerkara; }
    public String getPengadilan()       { return pengadilan; }
    public String getTanggalPutusan()   { return tanggalPutusan; }
    public String getNamaTerdakwa()     { return namaTerdakwa; }
    public int    getUmurTerdakwa()     { return umurTerdakwa; }
    public String getJenisNarkotika()   { return jenisNarkotika; }
    public double getBeratBarangBukti() { return beratBarangBukti; }
    public String getPasalDilanggar()   { return pasalDilanggar; }
    public String getPeranTerdakwa()    { return peranTerdakwa; }
    public int    getVonisHukuman()     { return vonisHukuman; }
    public double getVonisDenda()       { return vonisDenda; }
    public String getNamaHakim()        { return namaHakim; }

    public void setNomorPerkara(String nomorPerkara)         { this.nomorPerkara = nomorPerkara; }
    public void setPengadilan(String pengadilan)             { this.pengadilan = pengadilan; }
    public void setTanggalPutusan(String tanggalPutusan)     { this.tanggalPutusan = tanggalPutusan; }
    public void setNamaTerdakwa(String namaTerdakwa)         { this.namaTerdakwa = namaTerdakwa; }

    public void setUmurTerdakwa(int umurTerdakwa) {
        if (umurTerdakwa < 0) throw new IllegalArgumentException("Umur tidak boleh negatif.");
        this.umurTerdakwa = umurTerdakwa;
    }

    public void setJenisNarkotika(String jenisNarkotika)     { this.jenisNarkotika = jenisNarkotika; }

    public void setBeratBarangBukti(double beratBarangBukti) {
        if (beratBarangBukti < 0) throw new IllegalArgumentException("Berat barang bukti tidak boleh negatif.");
        this.beratBarangBukti = beratBarangBukti;
    }

    public void setPasalDilanggar(String pasalDilanggar)     { this.pasalDilanggar = pasalDilanggar; }
    public void setPeranTerdakwa(String peranTerdakwa)       { this.peranTerdakwa = peranTerdakwa; }

    public void setVonisHukuman(int vonisHukuman) {
        if (vonisHukuman < 0) throw new IllegalArgumentException("Vonis hukuman tidak boleh negatif.");
        this.vonisHukuman = vonisHukuman;
    }

    public void setVonisDenda(double vonisDenda) {
        if (vonisDenda < 0) throw new IllegalArgumentException("Vonis denda tidak boleh negatif.");
        this.vonisDenda = vonisDenda;
    }

    public void setNamaHakim(String namaHakim)               { this.namaHakim = namaHakim; }

    public void tampilkan() {
        System.out.printf("%-22s | %-22s | %-15s | %-12s | %4d bln%n",
                nomorPerkara, namaTerdakwa, pengadilan, jenisNarkotika, vonisHukuman);
    }

    public void tampilkan(boolean detail) {
        if (!detail) { tampilkan(); return; }
        System.out.println(toString());
    }

    public String getKategoriHukuman() {
        if (vonisHukuman < 24) return "Ringan";
        else if (vonisHukuman < 72) return "Sedang";
        else return "Berat";
    }

    @Override
    public String toString() {
        return String.format(
                "Nomor Perkara   : %s%n" +
                        "Pengadilan      : %s%n" +
                        "Tanggal Putusan : %s%n" +
                        "Terdakwa        : %s (%d tahun)%n" +
                        "Jenis Narkotika : %s%n" +
                        "Berat BB        : %.2f gram%n" +
                        "Pasal Dilanggar : %s%n" +
                        "Peran           : %s%n" +
                        "Vonis           : %d bulan (Kategori: %s)%n" +
                        "Denda           : Rp %,.2f%n" +
                        "Hakim           : %s",
                nomorPerkara, pengadilan, tanggalPutusan,
                namaTerdakwa, umurTerdakwa, jenisNarkotika,
                beratBarangBukti, pasalDilanggar, peranTerdakwa,
                vonisHukuman, getKategoriHukuman(), vonisDenda, namaHakim
        );
    }
}
