/*
 * ROSCOSMOS CORP. PROPERTY. 
 * Don't use without permission
 */
package ru.npopm.dep715.searchdocs.gui;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Главное окно приложения
 *
 * @author ayrat
 */
public class ApplicationLaunch extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Поиск в файлах - Lucene");

        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            launch(args);
    }

}
