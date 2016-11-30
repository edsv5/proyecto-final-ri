package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    // El método start debe ser implementado en la aplicación
    // En el start se crea la interfaz de usuario
    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Inicio.fxml"));
        Parent root = (Parent)loader.load();
        Scene scene = new Scene(root, 600, 400); // Se crea la scene

        primaryStage.setTitle("Mi Canal, penes");
        primaryStage.setResizable(false); // Se pone esto para que no pueda ser resizable
        primaryStage.setScene(scene);
        primaryStage.show();

        /*
        Scene secondScene = new Scene(root, 800, 400);
        Stage secondStage = new Stage();
        secondStage.setTitle("Your to-do.....");
        secondStage.setScene(secondScene);
        //secondStage.initStyle(StageStyle.DECORATED);
        //secondStage.initModality(Modality.NONE);
        secondStage.initOwner(primaryStage);
        primaryStage.toFront();
        secondStage.show();
*/

    }

    // Aquí solo se llama al launch
    public static void main(String[] args) {
        launch(args);
    }




}