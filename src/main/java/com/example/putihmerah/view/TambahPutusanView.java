package com.example.putihmerah.view;

import com.example.putihmerah.controller.KnowledgeController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class TambahPutusanView {

    private final KnowledgeController controller;
    private final DaftarPutusanView daftarView;
    private final ScrollPane root;

    private TextField tfNomorPerkara, tfPengadilan, tfTanggal, tfNamaTerdakwa,
            tfUmur, tfJenisNarkotika, tfBeratBB, tfPasalDilanggar,
            tfPeranTerdakwa, tfVonisHukuman, tfVonisDenda, tfNamaHakim;
    private Label lblStatus;

    public TambahPutusanView(KnowledgeController controller, DaftarPutusanView daftarView) {
        this.controller  = controller;
        this.daftarView  = daftarView;
        this.root        = new ScrollPane(buildForm());
        root.setFitToWidth(true);
        root.setStyle("-fx-background-color: #0B1622; -fx-background: #0B1622;");
    }

    public ScrollPane getRoot() { return root; }

    private VBox buildForm() {
        VBox container = new VBox(0);
        container.setStyle("-fx-background-color: #0B1622;");
        container.setPadding(new Insets(24, 30, 30, 30));

        Label judul = new Label("Tambah Data Putusan Baru");
        judul.setFont(Font.font("System", FontWeight.BOLD, 18));
        judul.setStyle("-fx-text-fill: #E8F4FD; -fx-padding: 0 0 16 0;");

        GridPane grid = new GridPane();
        grid.setHgap(16); grid.setVgap(14);
        grid.setStyle("-fx-background-color: #0F1D2B; -fx-background-radius: 12; -fx-padding: 24; " +
                "-fx-border-color: #16273A; -fx-border-width: 1; -fx-border-radius: 12;");

        tfNomorPerkara  = tf("Contoh: 1051/Pid.Sus/2025/PN Sby");
        tfPengadilan    = tf("Contoh: PN Surabaya");
        tfTanggal       = tf("Format: DD-MM-YYYY");
        tfNamaTerdakwa  = tf("Nama lengkap terdakwa");
        tfUmur          = tf("Usia dalam tahun (10-100)");
        tfJenisNarkotika= tf("Contoh: Sabu-Sabu, Ganja, Ekstasi");
        tfBeratBB       = tf("Berat dalam gram (gunakan titik desimal)");
        tfPasalDilanggar= tf("Contoh: Pasal 114 UU 35/2009");
        tfPeranTerdakwa = tf("Contoh: Pengedar, Penyimpan, Kurir, Pengguna");
        tfVonisHukuman  = tf("Dalam bulan (1-1200)");
        tfVonisDenda    = tf("Dalam Rupiah (gunakan titik desimal)");
        tfNamaHakim     = tf("Nama hakim ketua");

        String[] label = {
                "Nomor Perkara *", "Pengadilan *", "Tanggal Putusan *", "Nama Terdakwa *",
                "Umur Terdakwa *", "Jenis Narkotika *", "Berat BB (gram) *", "Pasal Dilanggar *",
                "Peran Terdakwa *", "Vonis Hukuman (bln) *", "Vonis Denda (Rp) *", "Nama Hakim *"
        };

        TextField[] fields = {
                tfNomorPerkara, tfPengadilan, tfTanggal, tfNamaTerdakwa,
                tfUmur, tfJenisNarkotika, tfBeratBB, tfPasalDilanggar,
                tfPeranTerdakwa, tfVonisHukuman, tfVonisDenda, tfNamaHakim
        };

        for (int i = 0; i < fields.length; i++) {
            int col = (i % 2) * 2;
            int row = i / 2;
            Label lbl = new Label(label[i]);
            lbl.setStyle("-fx-text-fill: #8FA8BD; -fx-font-size: 12px;");
            grid.add(lbl,       col,     row);
            grid.add(fields[i], col + 1, row);
            GridPane.setHgrow(fields[i], Priority.ALWAYS);
        }

        lblStatus = new Label("");
        lblStatus.setStyle("-fx-font-size: 12px; -fx-padding: 6 12; -fx-background-radius: 6; -fx-min-height: 30;");
        lblStatus.setWrapText(true);

        HBox btnBox = new HBox(12);
        btnBox.setAlignment(Pos.CENTER_LEFT);
        btnBox.setPadding(new Insets(16, 0, 0, 0));

        Button btnSimpan = buatButton("Simpan Putusan", "#103A35", "#4FD1C5");
        Button btnReset  = buatButton("Reset Form",     "#16273A", "#8FA8BD");

        btnSimpan.setOnAction(e -> prosesSimpan());
        btnReset.setOnAction(e  -> resetForm());

        btnBox.getChildren().addAll(btnSimpan, btnReset, lblStatus);

        container.getChildren().addAll(judul, grid, btnBox);
        return container;
    }

    private void prosesSimpan() {
        try {
            String[] data = {
                    tfNomorPerkara.getText(),  tfPengadilan.getText(),
                    tfTanggal.getText(),       tfNamaTerdakwa.getText(),
                    tfUmur.getText(),          tfJenisNarkotika.getText(),
                    tfBeratBB.getText(),       tfPasalDilanggar.getText(),
                    tfPeranTerdakwa.getText(), tfVonisHukuman.getText(),
                    tfVonisDenda.getText(),    tfNamaHakim.getText()
            };
            controller.tambahPutusan(data);

            lblStatus.setText("Putusan berhasil disimpan.");
            lblStatus.setStyle("-fx-font-size: 12px; -fx-text-fill: #4FD1C5; " +
                    "-fx-background-color: #103A35; -fx-padding: 6 12; -fx-background-radius: 6;");
            daftarView.refresh();
            resetForm();
        } catch (IllegalArgumentException ex) {
            // Exception Handling — pesan error dari controller/validator
            lblStatus.setText(ex.getMessage());
            lblStatus.setStyle("-fx-font-size: 12px; -fx-text-fill: #E05A5A; " +
                    "-fx-background-color: #2A1418; -fx-padding: 6 12; -fx-background-radius: 6;");
        }
    }

    private void resetForm() {
        tfNomorPerkara.clear(); tfPengadilan.clear(); tfTanggal.clear();
        tfNamaTerdakwa.clear(); tfUmur.clear(); tfJenisNarkotika.clear();
        tfBeratBB.clear(); tfPasalDilanggar.clear(); tfPeranTerdakwa.clear();
        tfVonisHukuman.clear(); tfVonisDenda.clear(); tfNamaHakim.clear();
    }

    private TextField tf(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-color: #16273A; -fx-text-fill: #D0E8F8; " +
                "-fx-prompt-text-fill: #5B7B95; -fx-background-radius: 6; -fx-padding: 7 10;");
        tf.setMinWidth(220);
        return tf;
    }

    private Button buatButton(String teks, String bgColor, String textColor) {
        Button btn = new Button(teks);
        btn.setStyle("-fx-background-color: " + bgColor + "; -fx-text-fill: " + textColor + "; " +
                "-fx-font-size: 13px; -fx-background-radius: 7; -fx-cursor: hand; -fx-padding: 9 22;");
        return btn;
    }
}