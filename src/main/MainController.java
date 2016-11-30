package main;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static main.Spider.arregloEnlaces;
import static main.Spider.busqueda;

public class MainController implements Initializable {

    @FXML
    Parent root;

    // Este método no lo vamos a usar aquí
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    // Para hacer referencia al stage principal


    // Este es el texto que se va a mostrar cuando apretamos el botón
    @FXML
    private TextArea enlacesTextArea;

    // Esto es lo que va a suceder cuando apretemos el botón
    public void iniciarBusqueda(ActionEvent event){
        //System.out.println("BLA BLA BLA");
        //System.out.println(greetingLabel);
        //Stage stage = (Stage) root.getScene().getWindow();
        //stage.getScene().setCursor(Cursor.WAIT); // Se pone el cursor en wait mientras completa la operación
        startTaskBusqueda();
        //stagePrincipal.getScene().setCursor(Cursor.DEFAULT); // Se pone el cursor en wait mientras completa la operación

    }

    private Service<Void> taskBuscar;

    // Se hace la task afuera para que todos la puedan accesar
    /*
    Task taskCrawl = new Task<Void>()
    {
        @Override
        protected Void call() throws Exception {
            runTaskBusqueda();
            return null;
        }
    };
    */

    // Crear el hilo afuera para poder accesarlo desde los botones
    //Thread th = new Thread(taskCrawl);

    public void startTaskBusqueda()
    {
        taskBuscar = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        // Toma lo que hay en los campos de texto
                        runTaskBusqueda(5, 10); // Ejecuta el método que hace la búsqueda
                        return null;
                    }
                };
            }
        };

        taskBuscar.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Crawling completado");
            }
        });

        taskBuscar.restart();

        // Hacerlo subtarea del hilo principal
        //th.setDaemon(true);
        // Correrse
        //th.start();
    }

    // Cancela el hilo de la búsqueda, si está activo
    public void cancelarTaskBusqueda(){
        taskBuscar.cancel();
        if(taskBuscar.isRunning()){
            System.out.println("Task corriendo");
        }
    }

    //Tarea de hacer el crawling de enlaces
    public void runTaskBusqueda(int profundidad, int limite){
        enlacesTextArea.appendText("Crawleando..." + System.lineSeparator());
        List<String> lista = crawlear(profundidad, limite); // Se almacena la lista de enlaces
        indexar(); // Se indexan
        imprimirEnlaces(lista);

    }

    // Para que la araña crawlee, devuelve la lista de los enlaces
    public List<String> crawlear(int profundidad, int limite)
    {
        System.out.println("Crawling iniciado");
        try
        {
            List<String> arregloEnlaces = busqueda(profundidad,limite);
            enlacesTextArea.appendText("Imprimiendo enlaces..." + System.lineSeparator());
        }
        catch(URISyntaxException e){}
        return arregloEnlaces();
    }

    // Indexación de la araña
    public void indexar()
    {
        System.out.println("indexar iniciado");
        BSBI_Indexer.creacionIndices(10);
    }

    // Imprimir los enlaces en la cajita de abajo, recibe una lista de enlacs

    public void imprimirEnlaces(List<String> listaEnlaces){
        System.out.println("Imprimiendo enlaces");
        for(String en: listaEnlaces) // Por cada string que devuelve el método crawlear
        {
            //System.out.println(en);
            enlacesTextArea.appendText(en + System.lineSeparator());
        }
    }

    // Para que busque los enlaces en el background


/*
    public void stopTaskBusqueda(){

        backgroundThread.stop();
    }*/



    // Para el botón de salir del menú

    public void doExit(){
        Platform.exit();
    }

}