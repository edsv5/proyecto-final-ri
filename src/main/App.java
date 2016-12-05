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
    public void start(Stage stageInicio) throws Exception{

        FXMLLoader loaderInicio = new FXMLLoader(getClass().getResource("../fxml/Inicio.fxml"));
        Parent rootInicio = (Parent)loaderInicio.load();
        Scene sceneInicio = new Scene(rootInicio, 444, 400); // Se crea la scene
        stageInicio.setTitle("Menú principal");
        stageInicio.setResizable(false);
        stageInicio.setScene(sceneInicio);
        stageInicio.show();


    }

    // Aquí solo se llama al launch
    public static void main(String[] args) {
        launch(args);
    }




}