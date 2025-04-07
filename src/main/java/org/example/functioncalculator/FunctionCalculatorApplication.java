package org.example.functioncalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

@SpringBootApplication
public class FunctionCalculatorApplication {
    public static void main(String[] args) {
        // Set headless mode explicitly
        System.setProperty("java.awt.headless", "true");

        SpringApplication.run(FunctionCalculatorApplication.class, args);
    }

    @RestController
    @RequestMapping("/api/calculator")
    public static class CalculatorController {

        @GetMapping("/functions")
        public Map<String, String> getAvailableFunctions() {
            Map<String, String> functions = new HashMap<>();
            functions.put("sin", "Sine Function");
            functions.put("cos", "Cosine Function");
            functions.put("tan", "Tangent Function");
            functions.put("log", "Logarithm Function");
            functions.put("sqrt", "Square Root Function");
            return functions;
        }

        @GetMapping("/calculate/{function}")
        public Map<String, Object> calculateFunction(
                @PathVariable String function,
                @RequestParam(required = true) Double value) {

            Map<String, Object> result = new HashMap<>();
            double calculatedValue;

            try {
                switch (function.toLowerCase()) {
                    case "sin":
                        calculatedValue = Math.sin(Math.toRadians(value));
                        break;
                    case "cos":
                        calculatedValue = Math.cos(Math.toRadians(value));
                        break;
                    case "tan":
                        calculatedValue = Math.tan(Math.toRadians(value));
                        break;
                    case "log":
                        calculatedValue = Math.log10(value);
                        break;
                    case "sqrt":
                        calculatedValue = Math.sqrt(value);
                        break;
                    default:
                        result.put("error", "Unknown function: " + function);
                        return result;
                }

                result.put("function", function);
                result.put("input", value);
                result.put("result", calculatedValue);

            } catch (Exception e) {
                result.put("error", e.getMessage());
            }

            return result;
        }

        @GetMapping(value = "/pdf/{function}", produces = MediaType.APPLICATION_PDF_VALUE)
        public ResponseEntity<byte[]> getPdfDocument(@PathVariable String function) {
            try {
                // Look for PDF in resources
                String pdfPath = "com/example/docs/" + function + ".pdf";
                Resource resource = new ClassPathResource(pdfPath);

                if (!resource.exists()) {
                    // Try alternate location
                    resource = new ClassPathResource("docs/" + function + ".pdf");

                    if (!resource.exists()) {
                        return ResponseEntity.notFound().build();
                    }
                }

                byte[] pdfBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDisposition(ContentDisposition.builder("inline")
                        .filename(function + ".pdf").build());

                return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        @GetMapping(value = "/image/{function}", produces = MediaType.IMAGE_PNG_VALUE)
        public ResponseEntity<byte[]> getPdfAsImage(@PathVariable String function) {
            try {
                String pdfPath = "com/example/docs/" + function + ".pdf";
                Resource resource = new ClassPathResource(pdfPath);
                File pdfFile;

                if (!resource.exists()) {
                    resource = new ClassPathResource("docs/" + function + ".pdf");

                    if (!resource.exists()) {
                        return ResponseEntity.notFound().build();
                    }
                }

                pdfFile = resource.getFile();

                PDDocument document = PDDocument.load(pdfFile);
                PDFRenderer renderer = new PDFRenderer(document);
                BufferedImage image = renderer.renderImageWithDPI(0, 150);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "png", baos);
                byte[] imageBytes = baos.toByteArray();

                document.close();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_PNG);

                return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        @GetMapping("/image/{function}")
        public ResponseEntity<byte[]> getFunctionImage(@PathVariable String function) {
            try {
                Resource imageResource = new ClassPathResource("static/images/" + function + ".png");

                if (!imageResource.exists()) {
                    return ResponseEntity.notFound().build();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BufferedImage bufferedImage = ImageIO.read(imageResource.getInputStream());
                ImageIO.write(bufferedImage, "png", baos);

                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(baos.toByteArray());

            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null);
            }
        }

    }
}