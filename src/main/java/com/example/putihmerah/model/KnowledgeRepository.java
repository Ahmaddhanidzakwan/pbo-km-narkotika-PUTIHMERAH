package com.example.putihmerah.model;

import java.util.ArrayList;

public class KnowledgeRepository {

    private ArrayList<Putusan> daftarPutusan;

    public KnowledgeRepository() {
        this.daftarPutusan = new ArrayList<>();
    }

    public void simpan(Putusan p) {
        daftarPutusan.add(p);
    }

    public Putusan cariByNomor(String nomor) {
        for (Putusan p : daftarPutusan) {
            if (p.getNomorPerkara().equalsIgnoreCase(nomor.trim())) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<Putusan> cariByNama(String nama) {
        ArrayList<Putusan> hasil = new ArrayList<>();
        for (Putusan p : daftarPutusan) {
            if (p.getNamaTerdakwa().toLowerCase().contains(nama.toLowerCase().trim())) {
                hasil.add(p);
            }
        }
        return hasil;
    }

    public ArrayList<Putusan> filterByJenis(String jenis) {
        ArrayList<Putusan> hasil = new ArrayList<>();
        for (Putusan p : daftarPutusan) {
            if (p.getJenisNarkotika().equalsIgnoreCase(jenis.trim())) {
                hasil.add(p);
            }
        }
        return hasil;
    }

    public ArrayList<Putusan> filterByPengadilan(String pengadilan) {
        ArrayList<Putusan> hasil = new ArrayList<>();
        for (Putusan p : daftarPutusan) {
            if (p.getPengadilan().toLowerCase().contains(pengadilan.toLowerCase().trim())) {
                hasil.add(p);
            }
        }
        return hasil;
    }

    public boolean hapus(String nomor) {
        return daftarPutusan.removeIf(
                p -> p.getNomorPerkara().equalsIgnoreCase(nomor.trim())
        );
    }

    public ArrayList<Putusan> getDaftarSemua() {
        return daftarPutusan;
    }

    public int getTotalData() {
        return daftarPutusan.size();
    }

    public boolean isNomorDuplikat(String nomor) {
        return cariByNomor(nomor) != null;
    }
}
