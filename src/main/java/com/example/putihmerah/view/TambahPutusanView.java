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
        // Desain ScrollPane modern tanpa border kasar
        root.setStyle("-fx-background-color: #0B1622; -fx-background: #0B1622; -fx-viewport-background: #0B1622; -fx-border-color: transparent;");
    }

    public ScrollPane getRoot() { return root; }

    private VBox buildForm() {
        VBox container = new VBox(0);
        container.setStyle("-fx-background-color: #0B1622;");
        container.setPadding(new Insets(32, 40, 40, 40)); // Padding lebih lega ala dashboard modern

        // Header Section
        VBox headerBox = new VBox(4);
        headerBox.setPadding(new Insets(0, 0, 24, 0));

        Label judul = new Label("Tambah Data Putusan Baru");
        judul.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22)); // Font modern & lebih tegas
        judul.setStyle("-fx-text-fill: #FFFFFF;");

        Label subJudul = new Label("Silakan lengkapi seluruh form di bawah ini dengan data yang valid.");
        subJudul.setFont(Font.font("Segoe UI", 13));
        subJudul.setStyle("-fx-text-fill: #6F8597;");

        headerBox.getChildren().addAll(judul, subJudul);

        // Form Grid
        GridPane grid = new GridPane();
        grid.setHgap(24); // Jarak horizontal antar kolom diperlebar
        grid.setVgap(18); // Jarak vertikal antar baris diperlebar
        grid.setStyle("-fx-background-color: #122031; -fx-background-radius: 16; -fx-padding: 32; " +
                "-fx-border-color: #1E3146; -fx-border-width: 1; -fx-border-radius: 16;");

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

        // Menyusun Label di atas TextField agar form terlihat lebih bersih dan mudah dibaca
        for (int i = 0; i < fields.length; i++) {
            int col = i % 2; // Berubah dari skema kolom inline lama menjadi tata letak grid seimbang
            int row = i / 2;

            VBox fieldGroup = new VBox(6); // Jarak label ke textfield
            Label lbl = new Label(label[i]);
            lbl.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));
            lbl.setStyle("-fx-text-fill: #9BB1C4;");

            fieldGroup.getChildren().addAll(lbl, fields[i]);
            GridPane.setHgrow(fieldGroup, Priority.ALWAYS);

            grid.add(fieldGroup, col, row);
        }

        // Status Label (Akan muncul rapi di atas tombol saat ada trigger)
        lblStatus = new Label("");
        lblStatus.setFont(Font.font("Segoe UI", 13));
        lblStatus.setWrapText(true);
        lblStatus.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(lblStatus, new Insets(16, 0, 0, 0));

        // Action Buttons
        HBox btnBox = new HBox(14);
        btnBox.setAlignment(Pos.CENTER_RIGHT); // Menggeser tombol ke kanan agar terlihat profesional
        btnBox.setPadding(new Insets(24, 0, 0, 0));

        Button btnSimpan = buatButton("Simpan Putusan", "#4FD1C5", "#0B1622", "#38B2AC");
        Button btnReset  = buatButton("Reset Form",     "#25374B", "#A0AEC0", "#2D435C");

        btnSimpan.setOnAction(e -> prosesSimpan());
        btnReset.setOnAction(e  -> resetForm());

        btnBox.getChildren().addAll(btnReset, btnSimpan);

        container.getChildren().addAll(headerBox, grid, lblStatus, btnBox);
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

            lblStatus.setText("✓  Putusan berhasil disimpan dengan aman.");
            lblStatus.setStyle("-fx-text-fill: #4FD1C5; -fx-background-color: rgba(79, 209, 197, 0.1); " +
                    "-fx-padding: 10 16; -fx-background-radius: 8; -fx-border-color: rgba(79, 209, 197, 0.2); -fx-border-radius: 8;");
            daftarView.refresh();
            resetForm();
        } catch (IllegalArgumentException ex) {
            lblStatus.setText("⚠  " + ex.getMessage());
            lblStatus.setStyle("-fx-text-fill: #FC8181; -fx-background-color: rgba(252, 129, 129, 0.1); " +
                    "-fx-padding: 10 16; -fx-background-radius: 8; -fx-border-color: rgba(252, 129, 129, 0.2); -fx-border-radius: 8;");
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
        tf.setFont(Font.font("Segoe UI", 13));

        // CSS Kompleks untuk menangani state Idle dan Focus (saat diklik mengetik)
        String styleIdle = "-fx-background-color: #1A2B3D; -fx-text-fill: #E2E8F0; -fx-prompt-text-fill: #4A5D6E; " +
                "-fx-background-radius: 8; -fx-padding: 10 12; -fx-border-color: #2D3F52; -fx-border-radius: 8; -fx-border-width: 1;";
        String styleFocus = "-fx-background-color: #1A2B3D; -fx-text-fill: #E2E8F0; -fx-prompt-text-fill: #4A5D6E; " +
                "-fx-background-radius: 8; -fx-padding: 10 12; -fx-border-color: #4FD1C5; -fx-border-radius: 8; -fx-border-width: 1;";

        tf.setStyle(styleIdle);
        tf.focusedProperty().addListener((obs, oldVal, newVal) -> tf.setStyle(newVal ? styleFocus : styleIdle));
        tf.setMinWidth(220);
        return tf;
    }

    private Button buatButton(String teks, String bgColor, String textColor, String hoverColor) {
        Button btn = new Button(teks);
        btn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));

        String styleIdle = "-fx-background-color: " + bgColor + "; -fx-text-fill: " + textColor + "; " +
                "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 24; -fx-transition: 0.2s;";
        String styleHover = "-fx-background-color: " + hoverColor + "; -fx-text-fill: " + textColor + "; " +
                "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 24;";

        btn.setStyle(styleIdle);
        btn.setOnMouseEntered(e -> btn.setStyle(styleHover));
        btn.setOnMouseExited(e -> btn.setStyle(styleIdle));
        return btn;
    }
}