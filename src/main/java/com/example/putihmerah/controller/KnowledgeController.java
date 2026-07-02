package com.example.putihmerah.controller;

import com.example.putihmerah.model.KnowledgeRepository;
import com.example.putihmerah.model.Putusan;
import com.example.putihmerah.model.StatistikPutusan;
import com.example.putihmerah.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class KnowledgeController {

    private final KnowledgeRepository repository;

    public KnowledgeController() {
        this.repository = new KnowledgeRepository();
        com.example.putihmerah.util.DataDummy.muatData(repository);
    }

    // ── tambahPutusan(String[] data) ──────────────────────────
    public boolean tambahPutusan(String[] data) {
        try {
            if (data == null || data.length != 12) {
                throw new IllegalArgumentException("Data tidak lengkap.");
            }

            ValidationUtil.validasiFormPutusan(
                    data[0], data[1], data[2], data[3], data[4], data[5],
                    data[6], data[7], data[8], data[9], data[10], data[11]
            );

            String nomorPerkara = data[0].trim();
            if (repository.isNomorDuplikat(nomorPerkara)) {
                throw new IllegalArgumentException("Nomor perkara \"" + nomorPerkara + "\" sudah terdaftar.");
            }

            Putusan p = new Putusan(
                    nomorPerkara, data[1].trim(), data[2].trim(), data[3].trim(),
                    ValidationUtil.parseInt(data[4], 10, 100),
                    data[5].trim(),
                    ValidationUtil.parseDouble(data[6], 0.0),
                    data[7].trim(), data[8].trim(),
                    ValidationUtil.parseInt(data[9], 1, 1200),
                    ValidationUtil.parseDouble(data[10], 0.0),
                    data[11].trim()
            );

            repository.simpan(p);
            return true;

        } catch (IllegalArgumentException e) {
            throw e; // dilempar ke View untuk ditampilkan sebagai pesan error
        } catch (Exception e) {
            throw new IllegalArgumentException("Terjadi kesalahan tidak terduga: " + e.getMessage());
        }
    }

    // ── cariPutusan(keyword, mode) ─────────────────────────────
    public ArrayList<Putusan> cariPutusan(String keyword, String mode) {
        ArrayList<Putusan> hasil = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) return hasil;

        switch (mode.toLowerCase()) {
            case "nomor" -> {
                Putusan p = repository.cariByNomor(keyword);
                if (p != null) hasil.add(p);
            }
            case "nama" -> hasil = repository.cariByNama(keyword);
        }
        return hasil;
    }

    // ── filterPutusan(kriteria, nilai) ─────────────────────────
    public ArrayList<Putusan> filterPutusan(String kriteria, String nilai) {
        ArrayList<Putusan> hasil = new ArrayList<>();
        if (nilai == null || nilai.trim().isEmpty()) return hasil;

        switch (kriteria.toLowerCase()) {
            case "jenis"      -> hasil = repository.filterByJenis(nilai);
            case "pengadilan" -> hasil = repository.filterByPengadilan(nilai);
        }
        return hasil;
    }

    // ── hapusPutusan(nomor) ─────────────────────────────────────
    public boolean hapusPutusan(String nomor) {
        if (nomor == null || nomor.trim().isEmpty()) return false;
        return repository.hapus(nomor);
    }

    // ── getStatistik() ───────────────────────────────────────
    public StatistikPutusan getStatistik() {
        return new StatistikPutusan(repository.getDaftarSemua());
    }

    // ── tampilkanSemua() — return data untuk View ─────────────
    public ArrayList<Putusan> tampilkanSemua() {
        return repository.getDaftarSemua();
    }

    public Putusan cariByNomor(String nomor) {
        return repository.cariByNomor(nomor);
    }

    public List<Putusan> cariByNama(String nama) {
        return repository.cariByNama(nama);
    }

    public List<Putusan> filterByJenis(String jenis) {
        return repository.filterByJenis(jenis);
    }

    public List<Putusan> filterByPengadilan(String pengadilan) {
        return repository.filterByPengadilan(pengadilan);
    }

    public int getTotalPutusan()             { return getStatistik().getTotalPutusan(); }
    public double getRataRataVonis()         { return getStatistik().getRataRataVonis(); }
    public double getRataRataDenda()         { return getStatistik().getRataRataDenda(); }
    public String getNarkotikaTerbanyak()    { return getStatistik().getJenisNarkotikaTerbanyak(); }
    public String[] getDistribusiPeran()     { return getStatistik().getDistribusiPeran(); }
    public Map<String,Integer> getDistribusiNarkotika()  { return getStatistik().getDistribusiJenisNarkotika(); }
    public Map<String,Integer> getDistribusiPengadilan() { return getStatistik().getDistribusiPengadilan(); }
    public Putusan getVonisTertinggi()       { return getStatistik().getVonisTertinggi(); }
    public Putusan getVonisTerendah()        { return getStatistik().getVonisTerendah(); }
}
