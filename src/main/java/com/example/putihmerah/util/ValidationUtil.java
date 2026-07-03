package com.example.putihmerah.util;

public class ValidationUtil {

    private ValidationUtil() {

    }

    public static boolean isNotEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static int parseInt(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Harus berupa angka bulat: \"" + s + "\"");
        }
    }

    public static int parseInt(String s, int min, int max) {
        int nilai = parseInt(s);

        if (nilai < min || nilai > max) {
            throw new IllegalArgumentException(
                    "Angka harus antara " + min + " dan " + max +
                            " (diberikan: " + nilai + ")");
        }

        return nilai;
    }

    public static double parseDouble(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Harus berupa angka desimal: \"" + s + "\"");
        }
    }

    public static double parseDouble(String s, double min) {
        double nilai = parseDouble(s);

        if (nilai < min) {
            throw new IllegalArgumentException(
                    "Nilai minimal adalah " + min +
                            " (diberikan: " + nilai + ")");
        }

        return nilai;
    }

    public static void validasiFormPutusan(
            String nomorPerkara,
            String pengadilan,
            String tanggal,
            String namaTerdakwa,
            String umur,
            String jenisNarkotika,
            String berat,
            String pasal,
            String peran,
            String vonis,
            String denda,
            String hakim) {

        if (!isNotEmpty(nomorPerkara))
            throw new IllegalArgumentException("Nomor Perkara wajib diisi.");

        if (!isNotEmpty(pengadilan))
            throw new IllegalArgumentException("Pengadilan wajib diisi.");

        if (!isNotEmpty(tanggal))
            throw new IllegalArgumentException("Tanggal Putusan wajib diisi.");

        if (!isNotEmpty(namaTerdakwa))
            throw new IllegalArgumentException("Nama Terdakwa wajib diisi.");

        parseInt(umur, 10, 100);

        if (!isNotEmpty(jenisNarkotika))
            throw new IllegalArgumentException("Jenis Narkotika wajib diisi.");

        parseDouble(berat, 0.0);

        if (!isNotEmpty(pasal))
            throw new IllegalArgumentException("Pasal Dilanggar wajib diisi.");

        if (!isNotEmpty(peran))
            throw new IllegalArgumentException("Peran Terdakwa wajib diisi.");

        parseInt(vonis, 1, 1200);

        parseDouble(denda, 0.0);

        if (!isNotEmpty(hakim))
            throw new IllegalArgumentException("Nama Hakim wajib diisi.");
    }
}