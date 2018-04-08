/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ksiazkakucharska;


import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class KsiazkaKucharska extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Pane root = (Pane) FXMLLoader.load(getClass().getResource("KsiazkaKucharska.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("Książka kucharska");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
