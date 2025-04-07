package org.example.functioncalculator.controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;

public class CalculatorController {

    @FXML
    private Pane pdfPane;

    @FXML
    private void onFunctionClick(javafx.event.ActionEvent event) {
        Button clicked = (Button) event.getSource();
        String function = clicked.getText();
        showPDF("src/main/resources/com/example/docs/" + function + ".pdf");
    }

    private void showPDF(String pathToPDF) {
        try {
            PDDocument document = PDDocument.load(new File(pathToPDF));
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage img = renderer.renderImageWithDPI(0, 150);
            Image fxImage = SwingFXUtils.toFXImage(img, null);

            ImageView imageView = new ImageView(fxImage);
            imageView.setFitWidth(pdfPane.getWidth());
            imageView.setPreserveRatio(true);

            pdfPane.getChildren().setAll(imageView);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
