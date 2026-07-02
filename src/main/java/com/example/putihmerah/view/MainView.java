package com.example.putihmerah.view;

import com.example.putihmerah.controller.KnowledgeController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class MainView extends Application {

    private KnowledgeController controller;
    private BorderPane contentArea;
    private DaftarPutusanView daftarView;
    private TambahPutusanView tambahView;
    private CariFilterView    cariView;
    private StatistikView     statView;


    private Button btnDashboard, btnDaftar, btnTambah, btnCari, btnStatistik;

    @Override
    public void start(Stage primaryStage) {
        controller = new KnowledgeController();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0B1622;");

        root.setTop(buildHeader());
        root.setLeft(buildSidebar());


        daftarView = new DaftarPutusanView(controller);
        tambahView = new TambahPutusanView(controller, daftarView);
        cariView   = new CariFilterView(controller);
        statView   = new StatistikView(controller);

        contentArea = new BorderPane();
        contentArea.setStyle("-fx-background-color: #0B1622;");
        tampilkanStatistik();

        root.setCenter(contentArea);

        Scene scene = new Scene(root, 1300, 800);
        primaryStage.setTitle("KMS Putusan Pengadilan Narkotika");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }


    private HBox buildHeader() {
        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(14, 24, 14, 24));
        header.setStyle("-fx-background-color: #0F1D2B; -fx-border-color: #16273A; -fx-border-width: 0 0 1 0;");

        Label logo = new Label("⚖");
        logo.setStyle("-fx-font-size: 26px; -fx-text-fill: #4FD1C5;");

        VBox titleBox = new VBox(0);
        Label title = new Label("Knowledge Management System");
        title.setFont(Font.font("System", FontWeight.BOLD, 17));
        title.setStyle("-fx-text-fill: #E8F4FD;");
        Label subtitle = new Label("Sistem Informasi Hukum Putusan Narkotika");
        subtitle.setStyle("-fx-font-size: 11px; -fx-text-fill: #5B7B95;");
        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        TextField search = new TextField();
        search.setPromptText("Cari putusan...");
        search.setPrefWidth(260);
        search.setStyle("-fx-background-color: #16273A; -fx-text-fill: #D0E8F8; " +
                "-fx-prompt-text-fill: #5B7B95; -fx-background-radius: 18; -fx-padding: 8 16;");

        Circle avatarCircle = new Circle(16, Color.web("#2DD4BF"));
        StackPane avatar = new StackPane(avatarCircle, new Label("K"));
        avatar.setAlignment(Pos.CENTER);

        header.getChildren().addAll(logo, titleBox, spacer, search, avatar);
        return header;
    }


    private VBox buildSidebar() {
        VBox sidebar = new VBox(4);
        sidebar.setPrefWidth(210);
        sidebar.setPadding(new Insets(18, 12, 18, 12));
        sidebar.setStyle("-fx-background-color: #0F1D2B; -fx-border-color: #16273A; -fx-border-width: 0 1 0 0;");

        btnDashboard = sidebarButton("📊  Main Dashboard");
        btnDaftar    = sidebarButton("📋  Daftar Putusan");
        btnTambah    = sidebarButton("➕  Tambah Putusan");
        btnCari      = sidebarButton("🔍  Cari & Filter");
        btnStatistik = sidebarButton("📈  Statistik");

        btnDashboard.setOnAction(e -> tampilkanStatistik());
        btnStatistik.setOnAction(e -> tampilkanStatistik());
        btnDaftar.setOnAction(e -> tampilkanDaftar());
        btnTambah.setOnAction(e -> tampilkanTambah());
        btnCari.setOnAction(e -> tampilkanCari());

        sidebar.getChildren().addAll(btnDashboard, btnDaftar, btnTambah, btnCari, btnStatistik);
        return sidebar;
    }

    private Button sidebarButton(String teks) {
        Button btn = new Button(teks);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(10, 14, 10, 14));
        btn.setStyle(sidebarStyleInactive());
        return btn;
    }

    private String sidebarStyleInactive() {
        return "-fx-background-color: transparent; -fx-text-fill: #8FA8BD; " +
                "-fx-font-size: 13px; -fx-background-radius: 8; -fx-cursor: hand;";
    }

    private String sidebarStyleActive() {
        return "-fx-background-color: #1B3A4B; -fx-text-fill: #4FD1C5; " +
                "-fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;";
    }

    private void setActiveSidebar(Button aktif) {
        for (Button b : new Button[]{btnDashboard, btnDaftar, btnTambah, btnCari, btnStatistik}) {
            b.setStyle(sidebarStyleInactive());
        }
        aktif.setStyle(sidebarStyleActive());
    }


    private void tampilkanStatistik() {
        statView.refresh();
        contentArea.setCenter(statView.getRoot());
        setActiveSidebar(btnStatistik);
    }

    private void tampilkanDaftar() {
        daftarView.refresh();
        contentArea.setCenter(daftarView.getRoot());
        setActiveSidebar(btnDaftar);
    }

    private void tampilkanTambah() {
        contentArea.setCenter(tambahView.getRoot());
        setActiveSidebar(btnTambah);
    }

    private void tampilkanCari() {
        contentArea.setCenter(cariView.getRoot());
        setActiveSidebar(btnCari);
    }
}
