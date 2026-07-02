package com.example.putihmerah.view;

import com.example.putihmerah.controller.KnowledgeController;
import com.example.putihmerah.model.Putusan;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;


public class DaftarPutusanView {

    private final KnowledgeController controller;
    private final BorderPane root;
    private TableView<PutusanRow> table;
    private Label labelTotal;

    public DaftarPutusanView(KnowledgeController controller) {
        this.controller = controller;
        this.root = new BorderPane();
        root.setStyle("-fx-background-color: #0B1622;");
        build();
        refresh();
    }

    public BorderPane getRoot() { return root; }

    private void build() {
        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(20, 24, 16, 24));
        toolbar.setStyle("-fx-background-color: #0B1622;");

        Label judul = new Label("Daftar Putusan");
        judul.setFont(Font.font("System", FontWeight.BOLD, 18));
        judul.setStyle("-fx-text-fill: #E8F4FD;");

        labelTotal = new Label("Total: 0 putusan");
        labelTotal.setStyle("-fx-text-fill: #5B7B95; -fx-font-size: 12px; -fx-padding: 0 0 0 12;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnRefresh = buatButton("Refresh", "#16273A", "#4FD1C5");
        Button btnHapus   = buatButton("Hapus Terpilih", "#2A1418", "#E05A5A");

        toolbar.getChildren().addAll(judul, labelTotal, spacer, btnRefresh, btnHapus);

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-background-color: #0F1D2B; -fx-control-inner-background: #0F1D2B;");
        table.setPlaceholder(new Label("Belum ada data putusan."));

        table.getColumns().addAll(
                kolom("No. Perkara",   "nomorPerkara",  170),
                kolom("Terdakwa",      "namaTerdakwa",  170),
                kolom("Pengadilan",    "pengadilan",    150),
                kolom("Narkotika",     "jenisNarkotika", 100),
                kolom("Peran",         "peranTerdakwa",  95),
                kolom("Pasal",         "pasalDilanggar", 170),
                kolom("Vonis (bln)",   "vonisHukuman",   80),
                kolom("Denda",         "vonisDenda",     110),
                kolom("Tanggal",       "tanggalPutusan", 100)
        );

        VBox detailPanel = buildDetailPanel();

        table.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, row) -> {
                    if (row != null) tampilkanDetail(row.getPutusan(), detailPanel);
                }
        );

        btnRefresh.setOnAction(e -> refresh());
        btnHapus.setOnAction(e -> {
            PutusanRow selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert(Alert.AlertType.WARNING, "Pilih dulu baris yang akan dihapus.");
                return;
            }
            Alert konfirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Hapus putusan: " + selected.getNomorPerkara() + "?",
                    ButtonType.YES, ButtonType.NO);
            konfirm.setTitle("Konfirmasi Hapus");
            konfirm.showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.YES) {
                    controller.hapusPutusan(selected.getNomorPerkara());
                    refresh();
                    showAlert(Alert.AlertType.INFORMATION, "Putusan berhasil dihapus.");
                }
            });
        });

        BorderPane.setMargin(table, new Insets(0, 24, 16, 24));

        SplitPane split = new SplitPane(table, detailPanel);
        split.setDividerPositions(0.65);
        split.setStyle("-fx-background-color: #0B1622;");

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

    private VBox buildDetailPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(16));
        panel.setStyle("-fx-background-color: #0F1D2B;");
        Label placeholder = new Label("Klik baris untuk melihat detail putusan");
        placeholder.setStyle("-fx-text-fill: #5B7B95; -fx-font-size: 13px;");
        placeholder.setWrapText(true);
        panel.getChildren().add(placeholder);
        return panel;
    }

    private void tampilkanDetail(Putusan p, VBox panel) {
        panel.getChildren().clear();

        Label judul = new Label("Detail Putusan");
        judul.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #4FD1C5;");
        panel.getChildren().add(judul);

        String[][] fields = {
                {"No. Perkara",     p.getNomorPerkara()},
                {"Pengadilan",      p.getPengadilan()},
                {"Tanggal Putusan", p.getTanggalPutusan()},
                {"Nama Terdakwa",   p.getNamaTerdakwa()},
                {"Umur Terdakwa",   p.getUmurTerdakwa() + " tahun"},
                {"Jenis Narkotika", p.getJenisNarkotika()},
                {"Berat BB",        String.format("%.2f gram", p.getBeratBarangBukti())},
                {"Pasal Dilanggar", p.getPasalDilanggar()},
                {"Peran Terdakwa",  p.getPeranTerdakwa()},
                {"Vonis Hukuman",   p.getVonisHukuman() + " bulan (" + p.getKategoriHukuman() + ")"},
                {"Vonis Denda",     String.format("Rp %,.2f", p.getVonisDenda())},
                {"Nama Hakim",      p.getNamaHakim()},
        };

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(8);
        for (int i = 0; i < fields.length; i++) {
            Label lbl = new Label(fields[i][0]);
            lbl.setStyle("-fx-text-fill: #5B7B95; -fx-font-size: 11px; -fx-min-width: 110;");
            Label val = new Label(fields[i][1]);
            val.setStyle("-fx-text-fill: #E0F0FF; -fx-font-size: 11px;");
            val.setWrapText(true);
            grid.add(lbl, 0, i); grid.add(val, 1, i);
        }
        panel.getChildren().add(grid);
    }

    private TableColumn<PutusanRow, String> kolom(String judul, String prop, double minW) {
        TableColumn<PutusanRow, String> col = new TableColumn<>(judul);
        col.setCellValueFactory(new PropertyValueFactory<>(prop));
        col.setMinWidth(minW);
        return col;
    }

    private Button buatButton(String teks, String bgColor, String textColor) {
        Button btn = new Button(teks);
        btn.setStyle("-fx-background-color: " + bgColor + "; -fx-text-fill: " + textColor + "; " +
                "-fx-font-size: 12px; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 7 16;");
        return btn;
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert a = new Alert(type, msg);
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
            this.vonisDenda     = new SimpleStringProperty(String.format("%,.2f", p.getVonisDenda()));
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

        public SimpleStringProperty nomorPerkaraProperty()   { return nomorPerkara; }
        public SimpleStringProperty namaTerdakwaProperty()   { return namaTerdakwa; }
        public SimpleStringProperty pengadilanProperty()     { return pengadilan; }
        public SimpleStringProperty jenisNarkotikaProperty() { return jenisNarkotika; }
        public SimpleStringProperty peranTerdakwaProperty()  { return peranTerdakwa; }
        public SimpleStringProperty pasalDilanggarProperty() { return pasalDilanggar; }
        public SimpleStringProperty vonisHukumanProperty()   { return vonisHukuman; }
        public SimpleStringProperty vonisDendaProperty()     { return vonisDenda; }
        public SimpleStringProperty tanggalPutusanProperty() { return tanggalPutusan; }
    }
}