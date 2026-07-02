package com.example.putihmerah.view;

import com.example.putihmerah.controller.KnowledgeController;
import com.example.putihmerah.model.Putusan;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Map;

public class StatistikView {

    private final KnowledgeController controller;
    private final ScrollPane root;
    private final VBox contentBox;


    private final String BG_MAIN = "#0B1120";
    private final String BG_CARD = "#111827";
    private final String BORDER_COLOR = "#1F2937";
    private final String TEXT_MUTED = "#94A3B8";
    private final String TEXT_WHITE = "#F8FAFC";

    public StatistikView(KnowledgeController controller) {
        this.controller = controller;
        contentBox = new VBox(24);
        contentBox.setPadding(new Insets(32, 40, 40, 40));
        contentBox.setStyle("-fx-background-color: " + BG_MAIN + ";");

        root = new ScrollPane(contentBox);
        root.setFitToWidth(true);
        root.setStyle("-fx-background: " + BG_MAIN + "; -fx-background-color: " + BG_MAIN + "; " +
                "-fx-viewport-background: " + BG_MAIN + "; -fx-border-color: transparent;");
        refresh();
    }

    public ScrollPane getRoot() { return root; }

    public void refresh() {
        contentBox.getChildren().clear();


        VBox headerBox = new VBox(6);
        Label judulStat = new Label("Dashboard Analitik Putusan");
        judulStat.setStyle("-fx-font-family: 'Segoe UI', sans-serif; -fx-font-size: 24px; " +
                "-fx-font-weight: bold; -fx-text-fill: " + TEXT_WHITE + ";");
        Label subJudulStat = new Label("Tinjauan komprehensif data perkara narkotika dan statistik pengadilan.");
        subJudulStat.setStyle("-fx-font-family: 'Segoe UI', sans-serif; -fx-font-size: 13px; -fx-text-fill: " + TEXT_MUTED + ";");
        headerBox.getChildren().addAll(judulStat, subJudulStat);


        HBox kartuRow = new HBox(20);

        kartuRow.getChildren().addAll(
                kartuStatistik("Total Putusan", String.valueOf(controller.getTotalPutusan()), "TP",
                        "#38BDF8", "#0EA5E9", "rgba(56, 189, 248, 0.1)"),
                kartuStatistik("Rata-rata Vonis", String.format("%.1f bulan", controller.getRataRataVonis()), "RV",
                        "#34D399", "#10B981", "rgba(52, 211, 153, 0.1)"),
                kartuStatistik("Rata-rata Denda", String.format("Rp %,.2f", controller.getRataRataDenda()), "RD",
                        "#FBBF24", "#F59E0B", "rgba(251, 191, 36, 0.1)"),
                kartuStatistik("Narkotika Terbanyak", controller.getNarkotikaTerbanyak(), "NT",
                        "#C084FC", "#A855F7", "rgba(192, 132, 252, 0.1)")
        );
        for (var node : kartuRow.getChildren()) {
            HBox.setHgrow(node, Priority.ALWAYS);
            ((VBox) node).setPrefWidth(200); // Memaksa pembagian lebar rata
        }


        Putusan pMax = controller.getVonisTertinggi();
        Putusan pMin = controller.getVonisTerendah();
        HBox vonisRow = new HBox(20);
        if (pMax != null) {
            HBox cardMax = kartuVonis("Puncak Vonis Tertinggi", pMax, "#EF4444", "rgba(239, 68, 68, 0.1)");
            HBox.setHgrow(cardMax, Priority.ALWAYS);
            cardMax.setMaxWidth(Double.MAX_VALUE);
            vonisRow.getChildren().add(cardMax);
        }
        if (pMin != null) {
            HBox cardMin = kartuVonis("Batas Vonis Terendah",  pMin, "#3B82F6", "rgba(59, 130, 246, 0.1)");
            HBox.setHgrow(cardMin, Priority.ALWAYS);
            cardMin.setMaxWidth(Double.MAX_VALUE);
            vonisRow.getChildren().add(cardMin);
        }


        HBox bottomRow = new HBox(20);
        VBox chartNarkotika = buildBarChartPanel("Distribusi Narkotika", controller.getDistribusiNarkotika(), "#34D399", "#10B981");
        VBox listPengadilan = buildListPanel("Distribusi Pengadilan", controller.getDistribusiPengadilan());
        VBox chartPeran = buildBarChartPanel("Peran Terdakwa", mapFromArray(controller.getDistribusiPeran()), "#FBBF24", "#F59E0B");


        chartNarkotika.setPrefWidth(300); chartNarkotika.setMaxWidth(Double.MAX_VALUE); HBox.setHgrow(chartNarkotika, Priority.ALWAYS);
        listPengadilan.setPrefWidth(300); listPengadilan.setMaxWidth(Double.MAX_VALUE); HBox.setHgrow(listPengadilan, Priority.ALWAYS);
        chartPeran.setPrefWidth(300);     chartPeran.setMaxWidth(Double.MAX_VALUE);     HBox.setHgrow(chartPeran, Priority.ALWAYS);

        bottomRow.getChildren().addAll(chartNarkotika, listPengadilan, chartPeran);

        contentBox.getChildren().addAll(headerBox, kartuRow, vonisRow, bottomRow);
    }

    private Map<String, Integer> mapFromArray(String[] arr) {
        Map<String, Integer> map = new java.util.LinkedHashMap<>();
        for (String s : arr) {
            String[] parts = s.split(":");
            if (parts.length == 2) {
                String key = parts[0].trim();
                String valStr = parts[1].replace("kasus", "").trim();
                try {
                    map.put(key, Integer.parseInt(valStr));
                } catch (NumberFormatException ignored) {}
            }
        }
        return map;
    }


    private VBox kartuStatistik(String label, String nilai, String inisial, String colorLight, String colorDark, String iconBgColor) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: " + BG_CARD + "; -fx-border-color: " + BORDER_COLOR + "; " +
                "-fx-border-width: 1; -fx-background-radius: 12; -fx-border-radius: 12;");

        HBox topRow = new HBox(12);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: " + TEXT_MUTED + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label iconLbl = new Label(inisial); // Menggunakan teks inisial aman
        iconLbl.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + colorLight + ";");
        StackPane iconBox = new StackPane(iconLbl);
        iconBox.setPrefSize(36, 36);
        iconBox.setStyle("-fx-background-color: " + iconBgColor + "; -fx-background-radius: 10;");

        topRow.getChildren().addAll(lbl, spacer, iconBox);

        Label val = new Label(nilai);
        val.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_WHITE + ";");
        val.setWrapText(true);

        card.getChildren().addAll(topRow, val);
        return card;
    }


    private HBox kartuVonis(String label, Putusan p, String accentColor, String bgAccent) {
        HBox card = new HBox(16);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: " + BG_CARD + "; -fx-background-radius: 12; " +
                "-fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 1; -fx-border-radius: 12;");

        String inisial = p.getNamaTerdakwa().length() >= 2
                ? p.getNamaTerdakwa().substring(0, 2).toUpperCase()
                : p.getNamaTerdakwa().toUpperCase();

        StackPane avatar = new StackPane();
        Circle circle = new Circle(24);
        circle.setFill(Color.web(bgAccent));

        Label avatarText = new Label(inisial);
        avatarText.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + accentColor + ";");
        avatar.getChildren().addAll(circle, avatarText);

        VBox info = new VBox(4);
        Label lbl = new Label(label.toUpperCase());
        lbl.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: " + accentColor + "; -fx-letter-spacing: 1px;");

        Label nama = new Label(p.getNamaTerdakwa() + " — " + p.getVonisHukuman() + " Bulan");
        nama.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_WHITE + ";");
        nama.setWrapText(false);

        Label nomor = new Label(p.getNomorPerkara() + "  •  " + p.getPengadilan());
        nomor.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-text-fill: " + TEXT_MUTED + ";");

        info.getChildren().addAll(lbl, nama, nomor);
        card.getChildren().addAll(avatar, info);
        return card;
    }


    private VBox buildBarChartPanel(String judul, Map<String, Integer> data, String colorLight, String colorDark) {
        VBox box = new VBox(16);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: " + BG_CARD + "; -fx-background-radius: 12; " +
                "-fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 1; -fx-border-radius: 12;");

        Label lbl = new Label(judul);
        lbl.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_WHITE + ";");
        box.getChildren().add(lbl);

        if (data.isEmpty()) {
            Label emptyLbl = new Label("Belum ada data tersedia.");
            emptyLbl.setStyle("-fx-font-family: 'Segoe UI'; -fx-text-fill: #475569; -fx-font-style: italic;");
            box.getChildren().add(emptyLbl);
            return box;
        }

        int maxVal = data.values().stream().max(Integer::compareTo).orElse(1);

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            VBox itemRow = new VBox(8);

            HBox textRow = new HBox(8);
            textRow.setAlignment(Pos.CENTER_LEFT);

            Label nameLbl = new Label(entry.getKey());
            nameLbl.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-text-fill: #CBD5E1;");

            nameLbl.setWrapText(false);
            nameLbl.setMaxWidth(160); // Batas aman lebar teks sebelum dipotong
            Tooltip tooltip = new Tooltip(entry.getKey()); // Munculkan teks asli jika kursor ditaruh di atasnya
            tooltip.setStyle("-fx-font-size: 12px;");
            Tooltip.install(nameLbl, tooltip);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label valueLbl = new Label(entry.getValue() + " Perkara");
            valueLbl.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + colorLight + ";");

            valueLbl.setMinWidth(Region.USE_PREF_SIZE);

            textRow.getChildren().addAll(nameLbl, spacer, valueLbl);

            double persentase = (double) entry.getValue() / maxVal;
            HBox barContainer = new HBox();
            barContainer.setStyle("-fx-background-color: #1E293B; -fx-background-radius: 6;");
            barContainer.setMinHeight(8);
            barContainer.setMaxHeight(8);

            Region pillBar = new Region();
            pillBar.setStyle("-fx-background-color: linear-gradient(to right, " + colorDark + ", " + colorLight + "); -fx-background-radius: 6;");
            pillBar.setMinHeight(8);
            pillBar.setMaxHeight(8);
            pillBar.prefWidthProperty().bind(barContainer.widthProperty().multiply(persentase));

            barContainer.getChildren().add(pillBar);
            itemRow.getChildren().addAll(textRow, barContainer);
            box.getChildren().add(itemRow);
        }

        return box;
    }


    private VBox buildListPanel(String judul, Map<String, Integer> data) {
        VBox box = new VBox(12);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: " + BG_CARD + "; -fx-background-radius: 12; " +
                "-fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 1; -fx-border-radius: 12;");

        Label lbl = new Label(judul);
        lbl.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_WHITE + ";");
        box.getChildren().add(lbl);

        if (data.isEmpty()) {
            Label emptyLbl = new Label("Belum ada data tersedia.");
            emptyLbl.setStyle("-fx-font-family: 'Segoe UI'; -fx-text-fill: #475569; -fx-font-style: italic;");
            box.getChildren().add(emptyLbl);
            return box;
        }

        int maxVal = data.values().stream().max(Integer::compareTo).orElse(1);

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(10, 12, 10, 12));
            row.setStyle("-fx-background-color: rgba(30, 41, 59, 0.4); -fx-background-radius: 8; " +
                    "-fx-border-color: rgba(255,255,255,0.03); -fx-border-width: 1; -fx-border-radius: 8;");

            StackPane iconBox = new StackPane(new Label("PN")); // Inisial aman sebagai ganti emoji
            iconBox.getChildren().get(0).setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #94A3B8;");
            iconBox.setPrefSize(28, 28);
            iconBox.setMinSize(28, 28);
            iconBox.setStyle("-fx-background-color: rgba(255,255,255,0.05); -fx-background-radius: 6;");

            Label nama = new Label(entry.getKey());
            nama.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #E2E8F0;");
            nama.setWrapText(false);

            HBox.setHgrow(nama, Priority.ALWAYS);
            nama.setMaxWidth(Double.MAX_VALUE);

            Tooltip tooltip = new Tooltip(entry.getKey());
            Tooltip.install(nama, tooltip);

            Label jumlah = new Label(entry.getValue() + " kss");
            jumlah.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 11px; -fx-text-fill: " + TEXT_MUTED + ";");
            jumlah.setMinWidth(Region.USE_PREF_SIZE);

            double barW = ((double) entry.getValue() / maxVal) * 45;
            Region miniBar = new Region();
            miniBar.setStyle("-fx-background-color: linear-gradient(to right, #3B82F6, #60A5FA); -fx-background-radius: 4;");
            miniBar.setMinWidth(Math.max(6, barW));
            miniBar.setMinHeight(4);
            miniBar.setMaxHeight(4);

            VBox jumlahBox = new VBox(4, jumlah, miniBar);
            jumlahBox.setAlignment(Pos.CENTER_RIGHT);

            row.getChildren().addAll(iconBox, nama, jumlahBox);
            box.getChildren().add(row);
        }

        return box;
    }
}