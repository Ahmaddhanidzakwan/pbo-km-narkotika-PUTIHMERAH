# putihmerah — KMS Putusan Pengadilan Narkotika

Knowledge Management System (KMS) untuk mengelola data putusan pengadilan tindak pidana narkotika, dibangun dengan **Java + JavaFX** menggunakan arsitektur **MVC (Model–View–Controller)**.

Tugas Besar Mata Kuliah **Pemrograman Berorientasi Object (PBO)** — Semester Genap 2025/2026.

**Repository:** https://github.com/Ahmaddhanidzakwan/pbo-km-narkotika-PUTIHMERAH

---

## 👥 Anggota Kelompok

| Nama | NIM | Kelas | Peran | Branch |
|------|-----|-------|-------|--------|
| Muh Rizal Julfikar | 202510370110095 | C | Knowledge / Database Engineer (Model) | `feature/model` |
| Ahmad Dhani Dzakwan | 202510370110072 | C | GUI Designer / View Developer (View) | `feature/view` |
| Rasyid Abdillah | 202510370110080 | C | Backend Developer / Controller Engineer (Controller) | `feature/controller` |

---

## 📋 Deskripsi Proyek

Aplikasi ini mengolah dataset putusan pengadilan pidana narkotika untuk:

- Mengelola data putusan (tambah, tampilkan, cari, hapus) lewat antarmuka **GUI JavaFX**
- Melakukan pencarian dan filter berdasarkan nomor perkara, nama terdakwa, jenis narkotika, dan pengadilan
- Menghitung statistik: total putusan, rata-rata vonis, rata-rata denda, jenis narkotika terbanyak, distribusi peran terdakwa, distribusi jenis narkotika, distribusi pengadilan, serta vonis tertinggi/terendah

Aplikasi dijalankan sebagai **JavaFX Desktop Application** dengan navigasi **sidebar** (Dashboard, Daftar Putusan, Tambah Putusan, Cari & Filter, Statistik), dan menerapkan konsep OOP: encapsulation, inheritance, interface, method overloading, method overriding, static field/method, exception handling, dan collection (ArrayList).

**Sumber Data:** Dataset berisi 51 putusan pidana narkotika (Pid.Sus) dari berbagai Pengadilan Negeri di Indonesia(Sesuai Data yang di sediakan di modul tugas besar PBO), tahun 2024–2025, dimuat langsung ke aplikasi melalui `util/DataDummy.java`.

---

## 🏗️ Arsitektur

```
Project: putihmerah
package: com.example.trypbo

com/example/trypbo/
├── model/
│   ├── Putusan.java                  ← entity utama (private fields + getter/setter)
│   ├── KnowledgeRepository.java      ← ArrayList, operasi CRUD
│   ├── StatistikPutusan.java         ← kalkulasi statistik (implements IPelaporan)
│   ├── StatistikPutusanTahunan.java  ← extends StatistikPutusan, filter per tahun
│   └── IPelaporan.java               ← interface laporan
├── view/
│   ├── MainView.java                 ← Application entry, header + sidebar + content area
│   ├── DaftarPutusanView.java        ← tabel semua putusan + detail panel + hapus
│   ├── TambahPutusanView.java        ← form tambah putusan
│   ├── CariFilterView.java           ← panel pencarian & filter
│   └── StatistikView.java            ← dashboard statistik (kartu, bar chart, list)
├── controller/
│   └── KnowledgeController.java      ← orkestrasi Model ↔ View
├── util/
│   ├── ValidationUtil.java           ← validasi input + exception handling
│   └── DataDummy.java                ← data sampel putusan (dimuat ke repository)
└── app/
    └── Main.java                      ← entry point (launch JavaFX Application)
```

**Aturan MVC yang diterapkan:**
- `View` tidak mengakses `Model` secara langsung — semua melalui `KnowledgeController`
- Logika bisnis (validasi, CRUD, statistik) berada di `Controller`, bukan di `Main` atau `View`
- `Main` hanya sebagai entry point yang memanggil `Application.launch(MainView.class, args)`

---

## 📄 .gitignore

Repository ini menggunakan `.gitignore` gabungan untuk Java/IntelliJ/Maven/Gradle (mengabaikan `out/`, `.idea/`, `*.class`, `*.iml`, dll) agar file build & konfigurasi IDE tidak ikut ter-commit.

---

## ⚙️ Cara Kompilasi & Menjalankan

### Menggunakan IntelliJ IDEA (disarankan)

1. Buka project ini sebagai **Java Project** dengan JavaFX SDK terpasang (via library/module atau Maven/Gradle jika dikonfigurasi)
2. Tandai folder `src` sebagai **Sources Root**
3. Pastikan VM options mengarah ke JavaFX SDK jika tidak pakai build tool, contoh:
   ```
   --module-path "PATH_TO_JAVAFX_SDK/lib" --add-modules javafx.controls,javafx.fxml
   ```
4. Klik kanan `Main.java` → **Run 'Main.main()'**

### Menggunakan terminal (javac manual)

```bash
javac -d out --module-path PATH_TO_JAVAFX_SDK/lib --add-modules javafx.controls $(find src -name "*.java")

java --module-path PATH_TO_JAVAFX_SDK/lib --add-modules javafx.controls -cp out com.example.trypbo.app.Main
```

---

## 🎥 Video Demo

Link video demo (durasi ±5 menit): https://youtu.be/vGMitp5Ko8I

Video mencakup:
- Penjelasan arsitektur MVC dan pembagian peran tim
- Demo alur Dashboard, Daftar Putusan, Tambah Putusan (validasi/Exception Handling)
- Demo pencarian & filter
- Code walkthrough Model (encapsulation, constructor, overloading, static)
- Demo Statistik (kartu, chart, distribusi)
- Bukti aktivitas Git (commit & branch tiap anggota)

---

## 🔧 Fitur

### Fitur Wajib
- [x] Tambah data putusan (form GUI + validasi)
- [x] Tampilkan semua putusan (tabel + panel detail)
- [x] Cari berdasarkan nomor perkara
- [x] Cari berdasarkan nama terdakwa
- [x] Filter berdasarkan jenis narkotika
- [x] Filter berdasarkan pengadilan
- [x] Hapus putusan (dengan konfirmasi dialog)
- [x] Statistik (total, rata-rata vonis, rata-rata denda, narkotika terbanyak, distribusi peran, distribusi narkotika, distribusi pengadilan, vonis tertinggi & terendah)
- [x] Exception handling pada seluruh input (`ValidationUtil` + `IllegalArgumentException`)
- [x] Data putusan sampel (hard-coded via `DataDummy`)

### Fitur Bonus
- [x] Antarmuka GUI JavaFX (sidebar navigation, dashboard statistik bergaya kartu & chart)
- [ ] Parsing PDF otomatis (Apache PDFBox)
- [ ] Sorting putusan (Comparable/Comparator)
- [ ] Ekspor statistik ke file .txt
- [ ] GitHub Actions CI

---

## 📚 Konsep OOP yang Diterapkan

| Konsep | Implementasi |
|--------|---------------|
| Encapsulation | Seluruh field `Putusan` bersifat `private`, diakses via getter/setter |
| Constructor | `Putusan()` (no-arg) dan `Putusan(12 parameter)` |
| Method Overloading | `tampilkan()` dan `tampilkan(boolean detail)`; `parseInt`/`parseDouble` dengan & tanpa batas min-max |
| Static Field & Method | `jumlahDibuat` (static field), `getJumlahDibuat()` (static method) |
| Inheritance | `StatistikPutusanTahunan extends StatistikPutusan` |
| Interface | `IPelaporan` diimplementasikan oleh `StatistikPutusan` |
| Method Overriding | `hitungSemua()` dan `tampilkanLaporan()` (override dari interface, dan override lagi di subclass) |
| Exception Handling | `try-catch` di `ValidationUtil` dan `KnowledgeController` |
| Collection | `ArrayList<Putusan>` sebagai struktur data utama; `Map<String,Integer>` unx`tuk distribusi |
| Array Primitif | `String[] distribusiPeran` di `StatistikPutusan` |

---

## 🌳 Strategi Branching

| Branch | Tujuan | Pengelola |
|--------|--------|-----------|
| `main` | Branch produksi (final, stabil) | Semua anggota (via PR) |
| `develop` | Branch integrasi sebelum ke `main` | Backend Developer |
| `feature/model` | Layer Model | Knowledge/DB Engineer |
| `feature/view` | Layer View | GUI Designer |
| `feature/controller` | Layer Controller & Validation | Backend Developer |

---

## 📝 Lisensi & Atribusi

Proyek ini dibuat untuk keperluan akademik (Tugas Besar PBO, Universitas Muhammadiyah Malang). Dataset bersumber dari putusan pengadilan publik (Pid.Sus) berbagai Pengadilan Negeri di Indonesia(Sesuai dengan data yang di berikan di modul Tugas Besar PBO), tahun 2024–2025.
