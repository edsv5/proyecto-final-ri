package main;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeMap;

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
    private TextArea enlacesTextArea; // Donde se imprimen los enlaces
    @FXML
    private TextField txtFldCantidadLinks; // Texto que ingresa el usuario para decidir la cantidad de links
    @FXML
    private TextField txtFldProfundidadCrawl; // Texto que ingresa el usuario para decidir la profundidad

    private Stage resultadosStage;
    // Esto es lo que va a suceder cuando apretemos el botón
    public void iniciarBusqueda(ActionEvent event) throws IOException {

        // Inicia el crawl
        startTaskBusqueda();

        // Crea y abre la interfaz que muestra resultados
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Buscador.fxml"));
        Parent root = (Parent)loader.load();
        Scene secondScene = new Scene(root, 600, 400); // Se crea la scene
        Stage secondStage = new Stage();
        secondStage.setTitle("Buscador");
        secondStage.setScene(secondScene);
        secondStage.show();
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
                        int limite = Integer.parseInt(txtFldCantidadLinks.getText());
                        int profundidad = Integer.parseInt(txtFldProfundidadCrawl.getText());

                        // Y corre el crawl
                        runTaskBusqueda(profundidad, limite); // Ejecuta el método que hace la búsqueda
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
    public void runTaskBusqueda(int profundidad, int limite) throws JSONException
    {
        enlacesTextArea.appendText("Crawleando..." + System.lineSeparator());
        // Si la entrada es un número, ejecuta el método
        System.out.println("INT");
        ArrayList<String> lista = crawlear(profundidad, limite); // Se almacena la lista de enlaces
        ArrayList<Cancion> listaCanciones = Scraper.extraerYAgregarALista(lista);
        BaseCancionesJSON.guardarInfoCanciones(listaCanciones);
        ArrayList<ContenedorTerminoIdPosicion> indiceIntermedio = IndiceIntermedio.recorrerJSONLeido();
        IndiceIntermedio.imprimirLista();
        IndicePosicional.construirIndicePosicional(indiceIntermedio);
        IndicePosicional.imprimirIndicePosicional();
        TreeMap<String, ArrayList<Posting>> indice = IndicePosicional.obtenerIndice();
        IndiceJSON.guardarIndicePosicional(indice);
        Ranking.rankearConsulta("aint burn chill chrome");
        Ranking.imprimirRanking();
        indexar(); // Se indexan
        imprimirEnlaces(lista);
    }

    // Para que la araña crawlee, devuelve la lista de los enlaces
    public ArrayList<String> crawlear(int profundidad, int limite)
    {
        System.out.println("Crawling iniciado");
        try
        {
            List<String> arregloEnlaces = busqueda(profundidad,limite);
            enlacesTextArea.appendText("Imprimiendo enlaces 1..." + System.lineSeparator());
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
        System.out.println("Imprimiendo enlaces 2 " + listaEnlaces.size());
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