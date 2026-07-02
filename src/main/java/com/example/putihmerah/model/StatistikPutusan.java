package com.example.putihmerah.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class StatistikPutusan implements IPelaporan {

    private int    totalPutusan;
    private double rataRataVonis;
    private double rataRataDenda;
    private String jenisNarkotikaTerbanyak;
    private String[] distribusiPeran;

    private final ArrayList<Putusan> daftar;

    public StatistikPutusan(ArrayList<Putusan> daftar) {
        this.daftar = daftar;
        hitungSemua();
    }

    @Override
    public void hitungSemua() {
        totalPutusan = daftar.size();

        if (daftar.isEmpty()) {
            rataRataVonis = 0;
            rataRataDenda = 0;
            jenisNarkotikaTerbanyak = "Tidak ada data";
            distribusiPeran = new String[0];
            return;
        }

        double totalVonis = 0, totalDenda = 0;
        Map<String, Integer> freqNarkotika = new HashMap<>();
        Map<String, Integer> freqPeran     = new HashMap<>();

        for (Putusan p : daftar) {
            totalVonis += p.getVonisHukuman();
            totalDenda += p.getVonisDenda();
            freqNarkotika.merge(p.getJenisNarkotika(), 1, Integer::sum);
            freqPeran.merge(p.getPeranTerdakwa(), 1, Integer::sum);
        }

        rataRataVonis = totalVonis / daftar.size();
        rataRataDenda = totalDenda / daftar.size();

        jenisNarkotikaTerbanyak = freqNarkotika.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> e.getKey() + " (" + e.getValue() + " kasus)")
                .orElse("Tidak ada data");

        distribusiPeran = new String[freqPeran.size()];
        int idx = 0;
        for (Map.Entry<String, Integer> entry : freqPeran.entrySet()) {
            distribusiPeran[idx++] = entry.getKey() + ": " + entry.getValue() + " kasus";
        }
    }

    @Override
    public void tampilkanLaporan() {
        System.out.println("==================================================");
        System.out.println("        LAPORAN STATISTIK PUTUSAN PERKARA NARKOTIKA        ");
        System.out.println("==================================================");
        System.out.println();

        System.out.printf("Jumlah Total Putusan                : %d%n", totalPutusan);
        System.out.printf("Rata-rata Masa Hukuman              : %.2f Bulan%n", rataRataVonis);
        System.out.printf("Rata-rata Nilai Barang Bukti        : Rp %,2f%n", rataDataDenda);
        System.out.printf("Jenis Narkotika Paling Sering Ditemukan : %s%n", jenisNarkotikaTerbanyak);
        System.out.println();

        System.out.println("--- RINCIAN PERSENTASE BERDASARKAN JENIS ---");
        for (String baris : distribusiPerJenis) {
            System.out.println("  - " + baris);
        }
    }

    public int getTotalPutusan()                 { return totalPutusan; }
    public double getRataRataVonis()              { return rataRataVonis; }
    public double getRataRataDenda()              { return rataRataDenda; }
    public String getJenisNarkotikaTerbanyak()    { return jenisNarkotikaTerbanyak; }
    public String[] getDistribusiPeran()          { return distribusiPeran; }

    public Putusan getVonisTertinggi() {
        return daftar.stream()
                .max((a, b) -> Integer.compare(a.getVonisHukuman(), b.getVonisHukuman()))
                .orElse(null);
    }

    public Putusan getVonisTerendah() {
        return daftar.stream()
                .min((a, b) -> Integer.compare(a.getVonisHukuman(), b.getVonisHukuman()))
                .orElse(null);
    }

    public Map<String, Integer> getDistribusiJenisNarkotika() {
        Map<String, Integer> map = new HashMap<>();
        for (Putusan p : daftar) map.merge(p.getJenisNarkotika(), 1, Integer::sum);
        return map;
    }

    public Map<String, Integer> getDistribusiPengadilan() {
        Map<String, Integer> map = new HashMap<>();
        for (Putusan p : daftar) map.merge(p.getPengadilan(), 1, Integer::sum);
        return map;
    }
}

