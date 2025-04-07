//package org.example.functioncalculator;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class FunctionCalculatorApplication extends Application {
//
//    public static void main(String[] args) {
//        new Thread(() -> SpringApplication.run(FunctionCalculatorApplication.class, args)).start();
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/calculator_layout.fxml"));
//        Scene scene = new Scene(loader.load(), 800, 600);
//        stage.setTitle("Function Calculator");
//        stage.setScene(scene);
//        stage.show();
//    }
//}

package org.example.functioncalculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FunctionCalculatorApplication extends Application {

    public static void main(String[] args) {
        new Thread(() -> SpringApplication.run(FunctionCalculatorApplication.class, args)).start();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/calculator_layout.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);
        stage.setTitle("Function Calculator");
        stage.setScene(scene);
        stage.show();
    }
}
