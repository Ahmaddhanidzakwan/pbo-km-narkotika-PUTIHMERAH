package com.example.putihmerah.model;

import java.util.ArrayList;


public class StatistikPutusanTahunan extends StatistikPutusan {

    private final String tahunFilter;

    public StatistikPutusanTahunan(ArrayList<Putusan> daftar, String tahunFilter) {
        super(filterByTahun(daftar, tahunFilter));
        this.tahunFilter = tahunFilter;
    }

    private static ArrayList<Putusan> filterByTahun(ArrayList<Putusan> daftar, String tahun) {
        ArrayList<Putusan> hasil = new ArrayList<>();
        for (Putusan p : daftar) {
            if (p.getTanggalPutusan() != null && p.getTanggalPutusan().endsWith(tahun)) {
                hasil.add(p);
            }
        }
        return hasil;
    }

    @Override
    public void tampilkanLaporan() {
        System.out.println("Laporan Statistik Tahun: " + tahunFilter);
        super.tampilkanLaporan();
    }

    public String getTahunFilter() {
        return tahunFilter;
    }
}
