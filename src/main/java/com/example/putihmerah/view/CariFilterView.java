package com.example.putihmerah.view;

import com.example.putihmerah.controller.KnowledgeController;
import com.example.putihmerah.model.Putusan;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;


public class CariFilterView {

    private final KnowledgeController controller;
    private final VBox root;
    private TableView<DaftarPutusanView.PutusanRow> tableHasil;
    private Label lblHasil;

    public CariFilterView(KnowledgeController controller) {
        this.controller = controller;
        this.root = new VBox(0);
        root.setStyle("-fx-background-color: #0B1622;");
        build();
    }

    public VBox getRoot() { return root; }

    private void build() {
        Label judul = new Label("Pencarian & Filter Data Putusan");
        judul.setFont(Font.font("System", FontWeight.BOLD, 18));
        judul.setStyle("-fx-text-fill: #E8F4FD; -fx-padding: 20 24 12 24;");

        GridPane searchGrid = new GridPane();
        searchGrid.setHgap(14); searchGrid.setVgap(12);
        searchGrid.setPadding(new Insets(16, 24, 16, 24));
        searchGrid.setStyle("-fx-background-color: #0F1D2B; -fx-background-radius: 12; " +
                "-fx-border-color: #16273A; -fx-border-width: 1; -fx-border-radius: 12;");
        BorderPane.setMargin(searchGrid, new Insets(0, 24, 0, 24));

        Label lNomor = lbl("Nomor Perkara");
        TextField tfNomor = tf("Masukkan nomor perkara");
        Button btnCariNomor = btn("Cari", "#103A35", "#4FD1C5");
        searchGrid.add(lNomor, 0, 0); searchGrid.add(tfNomor, 1, 0);
        searchGrid.add(btnCariNomor, 2, 0);

        Label lNama = lbl("Nama Terdakwa");
        TextField tfNama = tf("Nama atau sebagian nama");
        Button btnCariNama = btn("Cari", "#103A35", "#4FD1C5");
        searchGrid.add(lNama, 0, 1); searchGrid.add(tfNama, 1, 1);
        searchGrid.add(btnCariNama, 2, 1);

        Label lNarko = lbl("Jenis Narkotika");
        ComboBox<String> cbNarko = new ComboBox<>();
        cbNarko.getItems().addAll("Sabu-Sabu","Ganja","Ekstasi","Heroin","Kokain","Tramadol");
        cbNarko.setPromptText("Pilih jenis...");
        cbNarko.setStyle("-fx-background-color: #16273A; -fx-text-fill: #D0E8F8;");
        cbNarko.setMinWidth(220);
        Button btnFilterNarko = btn("Filter", "#1E2A12", "#9BC53D");
        searchGrid.add(lNarko, 0, 2); searchGrid.add(cbNarko, 1, 2);
        searchGrid.add(btnFilterNarko, 2, 2);

        Label lPeng = lbl("Pengadilan");
        TextField tfPeng = tf("Nama kota pengadilan");
        Button btnFilterPeng = btn("Filter", "#1E2A12", "#9BC53D");
        Button btnTampilSemua = btn("Tampil Semua", "#16273A", "#8FA8BD");
        searchGrid.add(lPeng, 0, 3); searchGrid.add(tfPeng, 1, 3);
        HBox pengBtn = new HBox(8, btnFilterPeng, btnTampilSemua);
        searchGrid.add(pengBtn, 2, 3);

        GridPane.setHgrow(tfNomor, Priority.ALWAYS);
        GridPane.setHgrow(tfNama,  Priority.ALWAYS);
        GridPane.setHgrow(cbNarko, Priority.ALWAYS);
        GridPane.setHgrow(tfPeng,  Priority.ALWAYS);

        lblHasil = new Label("Gunakan form di atas untuk mencari atau filter data.");
        lblHasil.setStyle("-fx-text-fill: #5B7B95; -fx-font-size: 12px; -fx-padding: 12 24 8 24;");

        tableHasil = new TableView<>();
        tableHasil.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableHasil.setStyle("-fx-background-color: #0F1D2B;");
        tableHasil.setPlaceholder(new Label("Tidak ada hasil."));

        tableHasil.getColumns().addAll(
                kolom("No. Perkara",   "nomorPerkara",  160),
                kolom("Terdakwa",      "namaTerdakwa",  160),
                kolom("Pengadilan",    "pengadilan",    130),
                kolom("Narkotika",     "jenisNarkotika", 100),
                kolom("Peran",         "peranTerdakwa",  90),
                kolom("Pasal",         "pasalDilanggar", 160),
                kolom("Vonis (bln)",   "vonisHukuman",   80),
                kolom("Denda",         "vonisDenda",     110)
        );

        btnCariNomor.setOnAction(e -> {
            String q = tfNomor.getText().trim();
            if (q.isEmpty()) { lblHasil.setText("Masukkan nomor perkara."); return; }
            Putusan p = controller.cariByNomor(q);
            if (p == null) {
                tampilkanHasil(List.of(), "Nomor perkara \"" + q + "\" tidak ditemukan.");
            } else {
                tampilkanHasil(List.of(p), "Ditemukan 1 putusan.");
            }
        });

        btnCariNama.setOnAction(e -> {
            String q = tfNama.getText().trim();
            if (q.isEmpty()) { lblHasil.setText("Masukkan nama terdakwa."); return; }
            List<Putusan> hasil = controller.cariByNama(q);
            tampilkanHasil(hasil, "Ditemukan " + hasil.size() + " putusan untuk nama: \"" + q + "\"");
        });

        btnFilterNarko.setOnAction(e -> {
            String q = cbNarko.getValue();
            if (q == null) { lblHasil.setText("Pilih jenis narkotika."); return; }
            List<Putusan> hasil = controller.filterByJenis(q);
            tampilkanHasil(hasil, "Filter \"" + q + "\": " + hasil.size() + " putusan ditemukan.");
        });

        btnFilterPeng.setOnAction(e -> {
            String q = tfPeng.getText().trim();
            if (q.isEmpty()) { lblHasil.setText("Masukkan nama pengadilan."); return; }
            List<Putusan> hasil = controller.filterByPengadilan(q);
            tampilkanHasil(hasil, "Filter pengadilan \"" + q + "\": " + hasil.size() + " putusan.");
        });

        btnTampilSemua.setOnAction(e -> {
            List<Putusan> semua = controller.tampilkanSemua();
            tampilkanHasil(semua, "Menampilkan semua " + semua.size() + " putusan.");
        });

        VBox.setVgrow(tableHasil, Priority.ALWAYS);
        BorderPane.setMargin(tableHasil, new Insets(0, 24, 16, 24));

        VBox tableWrap = new VBox(tableHasil);
        tableWrap.setPadding(new Insets(0, 24, 16, 24));
        VBox.setVgrow(tableWrap, Priority.ALWAYS);

        root.getChildren().addAll(judul, searchGrid, lblHasil, tableWrap);
    }

    private void tampilkanHasil(List<Putusan> list, String pesan) {
        lblHasil.setText(pesan);
        lblHasil.setStyle("-fx-text-fill: #4FD1C5; -fx-font-size: 12px; -fx-padding: 12 24 8 24;");
        tableHasil.setItems(FXCollections.observableArrayList(
                list.stream().map(DaftarPutusanView.PutusanRow::new).toList()
        ));
    }

    private TableColumn<DaftarPutusanView.PutusanRow, String> kolom(String judul, String prop, double minW) {
        TableColumn<DaftarPutusanView.PutusanRow, String> col = new TableColumn<>(judul);
        col.setCellValueFactory(new PropertyValueFactory<>(prop));
        col.setMinWidth(minW);
        return col;
    }

    private Label lbl(String t) {
        Label l = new Label(t);
        l.setStyle("-fx-text-fill: #8FA8BD; -fx-font-size: 12px; -fx-min-width: 120;");
        return l;
    }

    private TextField tf(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-color: #16273A; -fx-text-fill: #D0E8F8; " +
                "-fx-prompt-text-fill: #5B7B95; -fx-background-radius: 6; -fx-padding: 7 10;");
        tf.setMinWidth(220);
        return tf;
    }

    private Button btn(String t, String bg, String fg) {
        Button b = new Button(t);
        b.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: " + fg + "; " +
                "-fx-font-size: 12px; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 7 16;");
        return b;
    }
}
