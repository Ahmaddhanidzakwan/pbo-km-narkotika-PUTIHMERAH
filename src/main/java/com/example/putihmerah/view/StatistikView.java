package com.example.putihmerah.view;

import com.example.putihmerah.controller.KnowledgeController;
import com.example.putihmerah.model.Putusan;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Map;


public class StatistikView {

    private final KnowledgeController controller;
    private final ScrollPane root;
    private final VBox contentBox;

    public StatistikView(KnowledgeController controller) {
        this.controller = controller;
        contentBox = new VBox(18);
        contentBox.setPadding(new Insets(24, 28, 28, 28));
        contentBox.setStyle("-fx-background-color: #0B1622;");

        root = new ScrollPane(contentBox);
        root.setFitToWidth(true);
        root.setStyle("-fx-background-color: #0B1622; -fx-background: #0B1622;");
        refresh();
    }

    public ScrollPane getRoot() { return root; }

    public void refresh() {
        contentBox.getChildren().clear();


        HBox kartuRow = new HBox(16);
        kartuRow.getChildren().addAll(
                kartuStatistik("Total Putusan", String.valueOf(controller.getTotalPutusan()),
                        "🔨", "#3B82C4", "#16273A"),
                kartuStatistik("Rata-rata Vonis", String.format("%.1f bulan", controller.getRataRataVonis()),
                        "🕐", "#2DD4BF", "#102A28"),
                kartuStatistik("Rata-rata Denda", String.format("Rp %,.2f", controller.getRataRataDenda()),
                        "🪙", "#D9A441", "#2A2113"),
                kartuStatistik("Narkotika Terbanyak", controller.getNarkotikaTerbanyak(),
                        "🌿", "#A78BFA", "#221A33")
        );
        for (var node : kartuRow.getChildren()) HBox.setHgrow(node, Priority.ALWAYS);


        Putusan pMax = controller.getVonisTertinggi();
        Putusan pMin = controller.getVonisTerendah();
        HBox vonisRow = new HBox(16);
        if (pMax != null) vonisRow.getChildren().add(kartuVonis("Vonis Tertinggi", pMax, "#3B82C4"));
        if (pMin != null) vonisRow.getChildren().add(kartuVonis("Vonis Terendah",  pMin, "#D9714A"));
        for (var node : vonisRow.getChildren()) HBox.setHgrow(node, Priority.ALWAYS);


        HBox bottomRow = new HBox(18);
        VBox chartNarkotika = buildBarChartPanel(
                "Distribusi Jenis Narkotika", controller.getDistribusiNarkotika(), "#2DD4BF");
        VBox listPengadilan = buildListPanel(
                "Distribusi Per Pengadilan", controller.getDistribusiPengadilan());
        VBox chartPeran = buildBarChartPanel(
                "Distribusi Peran Terdakwa", mapFromArray(controller.getDistribusiPeran()), "#F2994A");

        bottomRow.getChildren().addAll(chartNarkotika, listPengadilan, chartPeran);
        for (var node : bottomRow.getChildren()) HBox.setHgrow(node, Priority.ALWAYS);

        contentBox.getChildren().addAll(kartuRow, vonisRow, bottomRow);
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

    private VBox kartuStatistik(String label, String nilai, String icon, String borderColor, String bgColor) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(16, 18, 16, 18));
        card.setStyle("-fx-background-color: " + bgColor + "; " +
                "-fx-border-color: " + borderColor + "; -fx-border-width: 1.5; " +
                "-fx-background-radius: 12; -fx-border-radius: 12;");

        HBox topRow = new HBox(8);
        topRow.setAlignment(Pos.CENTER_LEFT);
        Label lbl = new Label(label);
        lbl.setStyle("-fx-text-fill: #B8CCDA; -fx-font-size: 12px;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label iconLbl = new Label(icon);
        iconLbl.setStyle("-fx-font-size: 18px;");
        topRow.getChildren().addAll(lbl, spacer, iconLbl);

        Label val = new Label(nilai);
        val.setFont(Font.font("System", FontWeight.BOLD, 22));
        val.setStyle("-fx-text-fill: " + borderColor + ";");
        val.setWrapText(true);

        card.getChildren().addAll(topRow, val);
        return card;
    }


    private HBox kartuVonis(String label, Putusan p, String accentColor) {
        HBox card = new HBox(14);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(16, 20, 16, 20));
        card.setStyle("-fx-background-color: #0F1D2B; -fx-background-radius: 12; " +
                "-fx-border-color: #16273A; -fx-border-width: 1; -fx-border-radius: 12;");


        String inisial = p.getNamaTerdakwa().length() >= 2
                ? p.getNamaTerdakwa().substring(0, 2).toUpperCase()
                : p.getNamaTerdakwa().toUpperCase();
        StackPane avatar = new StackPane();
        javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle(26);
        circle.setFill(Color.web("#0F1D2B"));
        circle.setStroke(Color.web(accentColor));
        circle.setStrokeWidth(2);
        Label avatarText = new Label(inisial);
        avatarText.setStyle("-fx-text-fill: " + accentColor + "; -fx-font-weight: bold;");
        avatar.getChildren().addAll(circle, avatarText);

        VBox info = new VBox(2);
        Label lbl = new Label(label);
        lbl.setStyle("-fx-text-fill: #8FA8BD; -fx-font-size: 11px;");
        Label nama = new Label(p.getNamaTerdakwa() + " (" + p.getVonisHukuman() + " bulan)");
        nama.setStyle("-fx-text-fill: #E8F4FD; -fx-font-size: 14px; -fx-font-weight: bold;");
        Label nomor = new Label(p.getNomorPerkara() + " — " + p.getPengadilan());
        nomor.setStyle("-fx-text-fill: #5B7B95; -fx-font-size: 11px;");
        info.getChildren().addAll(lbl, nama, nomor);

        card.getChildren().addAll(avatar, info);
        return card;
    }


    private VBox buildBarChartPanel(String judul, Map<String, Integer> data, String barColor) {
        VBox box = new VBox(12);
        box.setPadding(new Insets(16));
        box.setStyle("-fx-background-color: #0F1D2B; -fx-background-radius: 12; " +
                "-fx-border-color: #16273A; -fx-border-width: 1; -fx-border-radius: 12;");

        Label lbl = new Label(judul);
        lbl.setFont(Font.font("System", FontWeight.BOLD, 13));
        lbl.setStyle("-fx-text-fill: #E8F4FD;");

        if (data.isEmpty()) {
            box.getChildren().addAll(lbl, new Label("Tidak ada data."));
            return box;
        }

        int maxVal = data.values().stream().max(Integer::compareTo).orElse(1);
        double chartW = 280;
        double barH = 22, gap = 14;
        double canvasH = data.size() * (barH + gap);

        Canvas canvas = new Canvas(chartW, canvasH);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        int idx = 0;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            double y = idx * (barH + gap);
            double maxBarW = chartW - 90;
            double barW = Math.max(18, (double) entry.getValue() / maxVal * maxBarW);


            gc.setFill(Color.web("#B8CCDA"));
            gc.fillText(entry.getKey(), 0, y + 10);


            gc.setFill(Color.web(barColor));
            gc.fillRoundRect(0, y + 14, barW, 10, 10, 10);

            idx++;
        }

        box.getChildren().addAll(lbl, canvas);
        return box;
    }


    private VBox buildListPanel(String judul, Map<String, Integer> data) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(16));
        box.setStyle("-fx-background-color: #0F1D2B; -fx-background-radius: 12; " +
                "-fx-border-color: #16273A; -fx-border-width: 1; -fx-border-radius: 12;");

        Label lbl = new Label(judul);
        lbl.setFont(Font.font("System", FontWeight.BOLD, 13));
        lbl.setStyle("-fx-text-fill: #E8F4FD;");
        box.getChildren().add(lbl);

        if (data.isEmpty()) {
            box.getChildren().add(new Label("Tidak ada data."));
            return box;
        }

        int maxVal = data.values().stream().max(Integer::compareTo).orElse(1);

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(8, 10, 8, 10));
            row.setStyle("-fx-background-color: #16273A; -fx-background-radius: 8;");

            Label icon = new Label("🏛");
            icon.setStyle("-fx-font-size: 13px;");

            Label nama = new Label(entry.getKey());
            nama.setStyle("-fx-text-fill: #D0E8F8; -fx-font-size: 12px;");
            nama.setMinWidth(140);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label jumlah = new Label(entry.getValue() + " kasus");
            jumlah.setStyle("-fx-text-fill: #5B7B95; -fx-font-size: 11px;");


            double barW = (double) entry.getValue() / maxVal * 60;
            Region miniBar = new Region();
            miniBar.setStyle("-fx-background-color: #2DD4BF; -fx-background-radius: 4;");
            miniBar.setMinWidth(Math.max(4, barW));
            miniBar.setMinHeight(4);
            miniBar.setMaxHeight(4);

            VBox jumlahBox = new VBox(4, jumlah, miniBar);
            jumlahBox.setAlignment(Pos.CENTER_RIGHT);

            row.getChildren().addAll(icon, nama, spacer, jumlahBox);
            box.getChildren().add(row);
        }

        return box;
    }
}
