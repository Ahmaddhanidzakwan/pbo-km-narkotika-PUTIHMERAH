package com.example.putihmerah.view;

import com.example.putihmerah.controller.KnowledgeController;
import com.example.putihmerah.model.Putusan;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;


public class DaftarPutusanView {

    private final KnowledgeController controller;
    private final BorderPane root;
    private TableView<PutusanRow> table;
    private Label labelTotal;


    private final String BG_MAIN = "#0B1120";
    private final String BG_CARD = "#111827";
    private final String BORDER_COLOR = "#1F2937";
    private final String TEXT_MUTED = "#94A3B8";
    private final String TEXT_WHITE = "#F8FAFC";

    public DaftarPutusanView(KnowledgeController controller) {
        this.controller = controller;
        this.root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_MAIN + ";");
        build();
        refresh();
    }

    public BorderPane getRoot() { return root; }

    private void build() {
        // ── Header / Toolbar ──────────────────────────────────────
        HBox toolbar = new HBox(16);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(32, 40, 20, 40));
        toolbar.setStyle("-fx-background-color: transparent;");

        VBox titleBox = new VBox(4);
        Label judul = new Label("Daftar Putusan Perkara");
        judul.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_WHITE + ";");

        labelTotal = new Label("Total: 0 putusan");
        labelTotal.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-text-fill: #38BDF8; " +
                "-fx-background-color: rgba(56, 189, 248, 0.1); -fx-padding: 4 10 4 10; -fx-background-radius: 12;");
        titleBox.getChildren().addAll(judul, labelTotal);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);


        Button btnRefresh = buatButton("Segarkan Data", "rgba(56, 189, 248, 0.1)", "rgba(56, 189, 248, 0.2)", "#38BDF8", "#38BDF8");
        Button btnHapus   = buatButton("Hapus Terpilih", "#7F1D1D", "#991B1B", "#FCA5A5", "transparent");

        toolbar.getChildren().addAll(titleBox, spacer, btnRefresh, btnHapus);


        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Styling dasar tabel inline
        table.setStyle("-fx-background-color: " + BG_CARD + "; " +
                "-fx-control-inner-background: " + BG_CARD + "; " +
                "-fx-table-cell-border-color: transparent; " +
                "-fx-table-header-border-color: #1F2937; " +
                "-fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 16; -fx-background-radius: 16; -fx-padding: 4;");

        Label placeholder = new Label("Belum ada data putusan yang tercatat.");
        placeholder.setStyle("-fx-text-fill: " + TEXT_MUTED + "; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");
        table.setPlaceholder(placeholder);

        table.getColumns().addAll(
                kolom("No. Perkara",   "nomorPerkara",  150),
                kolom("Terdakwa",      "namaTerdakwa",  180),
                kolom("Pengadilan",    "pengadilan",    140),
                kolom("Narkotika",     "jenisNarkotika", 120),
                kolom("Peran",         "peranTerdakwa",  110),
                kolom("Vonis (bln)",   "vonisHukuman",   80),
                kolom("Denda",         "vonisDenda",     120)
        );


        table.setRowFactory(tv -> {
            TableRow<PutusanRow> row = new TableRow<>();
            row.styleProperty().bind(Bindings.when(row.selectedProperty())
                    .then("-fx-background-color: #1E293B; -fx-text-background-color: #F8FAFC;")
                    .otherwise(Bindings.when(row.hoverProperty())
                            .then("-fx-background-color: rgba(30, 41, 59, 0.4); -fx-text-background-color: #E2E8F0;")
                            .otherwise("-fx-background-color: transparent; -fx-text-background-color: #CBD5E1;")));
            return row;
        });


        VBox detailPanel = buildDetailPanel();

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, row) -> {
            if (row != null) tampilkanDetail(row.getPutusan(), detailPanel);
        });


        btnRefresh.setOnAction(e -> refresh());
        btnHapus.setOnAction(e -> hapusAksi());


        BorderPane.setMargin(table, new Insets(0, 0, 0, 0));


        SplitPane split = new SplitPane(table, detailPanel);
        split.setDividerPositions(0.70); // 70% Tabel, 30% Detail
        split.setStyle("-fx-background-color: " + BG_MAIN + "; -fx-padding: 0 40 40 40;");


        SplitPane.setResizableWithParent(detailPanel, false);

        root.setTop(toolbar);
        root.setCenter(split);
    }

    public void refresh() {
        List<Putusan> list = controller.tampilkanSemua();
        table.setItems(FXCollections.observableArrayList(
                list.stream().map(PutusanRow::new).toList()
        ));
        labelTotal.setText("Total: " + list.size() + " putusan");
    }

    private void hapusAksi() {
        PutusanRow selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih baris putusan yang akan dihapus terlebih dahulu.");
            return;
        }
        Alert konfirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Apakah Anda yakin ingin menghapus perkara " + selected.getNomorPerkara() + " (" + selected.getNamaTerdakwa() + ")?",
                ButtonType.YES, ButtonType.NO);
        konfirm.setTitle("Konfirmasi Hapus");
        konfirm.setHeaderText("Menghapus Putusan");
        DialogPane dialogPane = konfirm.getDialogPane();
        dialogPane.setStyle("-fx-font-family: 'Segoe UI';");

        konfirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                controller.hapusPutusan(selected.getNomorPerkara());
                refresh();
                // Kosongkan panel detail
                table.getSelectionModel().clearSelection();
            }
        });
    }


    private VBox buildDetailPanel() {
        VBox panel = new VBox(16);
        panel.setPadding(new Insets(24));

        panel.setStyle("-fx-background-color: " + BG_CARD + "; -fx-border-color: " + BORDER_COLOR + "; " +
                "-fx-border-width: 1; -fx-background-radius: 16; -fx-border-radius: 16; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 4);");

        tampilkanKosong(panel);
        return panel;
    }

    private void tampilkanKosong(VBox panel) {
        panel.getChildren().clear();
        panel.setAlignment(Pos.CENTER);

        Label icon = new Label("📄");
        icon.setStyle("-fx-font-size: 32px; -fx-text-fill: #334155;");

        Label placeholder = new Label("Pilih putusan di tabel untuk melihat rincian detailnya.");
        placeholder.setStyle("-fx-text-fill: " + TEXT_MUTED + "; -fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-text-alignment: center;");
        placeholder.setWrapText(true);

        panel.getChildren().addAll(icon, placeholder);
    }

    private void tampilkanDetail(Putusan p, VBox panel) {
        panel.getChildren().clear();
        panel.setAlignment(Pos.TOP_LEFT);

        VBox header = new VBox(4);
        Label lblTerdakwa = new Label(p.getNamaTerdakwa());
        lblTerdakwa.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_WHITE + ";");
        lblTerdakwa.setWrapText(true);

        Label lblPerkara = new Label(p.getNomorPerkara() + "  •  " + p.getPengadilan());
        lblPerkara.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-text-fill: #38BDF8; -fx-font-weight: bold;");

        header.getChildren().addAll(lblTerdakwa, lblPerkara);


        Region divider = new Region();
        divider.setMinHeight(1);
        divider.setStyle("-fx-background-color: " + BORDER_COLOR + ";");
        VBox.setMargin(divider, new Insets(8, 0, 8, 0));


        VBox konten = new VBox(12);
        String[][] fields = {
                {"Tanggal Putusan", p.getTanggalPutusan()},
                {"Umur Terdakwa",   p.getUmurTerdakwa() + " tahun"},
                {"Jenis Narkotika", p.getJenisNarkotika() + " (" + String.format("%.2f gram", p.getBeratBarangBukti()) + ")"},
                {"Pasal Dilanggar", p.getPasalDilanggar()},
                {"Peran",           p.getPeranTerdakwa()},
                {"Vonis Hukuman",   p.getVonisHukuman() + " bulan (" + p.getKategoriHukuman() + ")"},
                {"Vonis Denda",     String.format("Rp %,.2f", p.getVonisDenda())},
                {"Hakim Ketua",     p.getNamaHakim()}
        };

        for (String[] field : fields) {
            VBox row = new VBox(2);
            Label lblLabel = new Label(field[0].toUpperCase());
            lblLabel.setStyle("-fx-text-fill: " + TEXT_MUTED + "; -fx-font-family: 'Segoe UI'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-letter-spacing: 1px;");

            Label lblValue = new Label(field[1]);
            lblValue.setStyle("-fx-text-fill: #E2E8F0; -fx-font-family: 'Segoe UI'; -fx-font-size: 13px;");
            lblValue.setWrapText(true);

            row.getChildren().addAll(lblLabel, lblValue);
            konten.getChildren().add(row);
        }

        ScrollPane scroll = new ScrollPane(konten);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: " + BG_CARD + "; -fx-background-color: " + BG_CARD + "; -fx-border-color: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        panel.getChildren().addAll(header, divider, scroll);
    }

    private TableColumn<PutusanRow, String> kolom(String judul, String prop, double minW) {
        TableColumn<PutusanRow, String> col = new TableColumn<>(judul);
        col.setCellValueFactory(new PropertyValueFactory<>(prop));
        col.setMinWidth(minW);
        col.setStyle("-fx-alignment: CENTER-LEFT; -fx-font-family: 'Segoe UI'; -fx-font-size: 13px;");
        return col;
    }


    private Button buatButton(String teks, String bgNormal, String bgHover, String textCol, String borderCol) {
        Button btn = new Button(teks);
        String baseStyle = "-fx-background-color: " + bgNormal + "; -fx-text-fill: " + textCol + "; " +
                "-fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-font-weight: bold; " +
                "-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: " + borderCol + "; -fx-cursor: hand; -fx-padding: 8 16;";
        String hoverStyle = baseStyle.replace(bgNormal, bgHover);

        btn.setStyle(baseStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
        return btn;
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type, msg);
        a.setTitle(title);
        a.setHeaderText(null);
        DialogPane dialogPane = a.getDialogPane();
        dialogPane.setStyle("-fx-font-family: 'Segoe UI';");
        a.showAndWait();
    }


    public static class PutusanRow {
        private final Putusan putusan;
        private final SimpleStringProperty nomorPerkara, namaTerdakwa, pengadilan,
                jenisNarkotika, peranTerdakwa, pasalDilanggar, tanggalPutusan;
        private final SimpleStringProperty vonisHukuman, vonisDenda;

        public PutusanRow(Putusan p) {
            this.putusan        = p;
            this.nomorPerkara   = new SimpleStringProperty(p.getNomorPerkara());
            this.namaTerdakwa   = new SimpleStringProperty(p.getNamaTerdakwa());
            this.pengadilan     = new SimpleStringProperty(p.getPengadilan());
            this.jenisNarkotika = new SimpleStringProperty(p.getJenisNarkotika());
            this.peranTerdakwa  = new SimpleStringProperty(p.getPeranTerdakwa());
            this.pasalDilanggar = new SimpleStringProperty(p.getPasalDilanggar());
            this.vonisHukuman   = new SimpleStringProperty(String.valueOf(p.getVonisHukuman()));
            this.vonisDenda     = new SimpleStringProperty(String.format("Rp %,.2f", p.getVonisDenda()));
            this.tanggalPutusan = new SimpleStringProperty(p.getTanggalPutusan());
        }

        public Putusan getPutusan() { return putusan; }
        public String getNomorPerkara()   { return nomorPerkara.get(); }
        public String getNamaTerdakwa()   { return namaTerdakwa.get(); }
        public String getPengadilan()     { return pengadilan.get(); }
        public String getJenisNarkotika() { return jenisNarkotika.get(); }
        public String getPeranTerdakwa()  { return peranTerdakwa.get(); }
        public String getPasalDilanggar() { return pasalDilanggar.get(); }
        public String getVonisHukuman()   { return vonisHukuman.get(); }
        public String getVonisDenda()     { return vonisDenda.get(); }
        public String getTanggalPutusan() { return tanggalPutusan.get(); }
    }
}