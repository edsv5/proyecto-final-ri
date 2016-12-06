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
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeMap;

import static main.Spider.crawlCanciones;

public class MainController implements Initializable {

    @FXML
    Parent root;

    // Este método no lo vamos a usar aquí
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Iniciando...");
    }

    // Para hacer referencia al stage principal

    // Este es el texto que se va a mostrar cuando apretamos el botón
    @FXML
    private TextArea enlacesTextArea; // Donde se imprimen los enlaces
    @FXML
    private TextField txtFldCantidadLinks; // Texto que ingresa el usuario para decidir la cantidad de links
    @FXML
    private TextField txtFldProfundidadCrawl; // Texto que ingresa el usuario para decidir la profundidad
    @FXML
    private ProgressBar progressBarCrawl;
    @FXML
    private Text txtProgreso; // Label que indica el estado del crawling
    @FXML
    private Button iniciarCrawlingButton; // Botón de iniciar crawling
    @FXML
    private Button abrirBusquedaCrawlButton; // Botón de iniciar búsqueda
    @FXML
    private Button menuButton; // Menú que va desde la búsqueda con crawl hacia el menú principal

    // Botones del inicio

    @FXML
    private Button bSinCrawl;
    @FXML
    private Button bConCrawl;
    @FXML
    private Button bCreditos;
    @FXML
    private Button bSalir;

    private Stage resultadosStage;
    // Esto sucede cuando se presiona el botón de búsqueda
    public void iniciarBusqueda(ActionEvent event) throws IOException {

        // Inicia el crawl
        startTaskBusqueda();
    }

    // Abre la ventana con los créditos
    public void mostrarCreditos() throws IOException {
        FXMLLoader loaderCreditos = new FXMLLoader(getClass().getResource("../fxml/Creditos.fxml"));
        Parent rootCreditos = (Parent)loaderCreditos.load();
        Scene sceneCreditos = new Scene(rootCreditos, 461, 317); // Se crea la scene
        Stage stageCreditos = new Stage();
        stageCreditos.setTitle("Créditos");
        stageCreditos.setScene(sceneCreditos);
        stageCreditos.setResizable(false);
        stageCreditos.show();
    }

    // Cierra la ventana de búsqueda con crawl y abre la de menú principal
    public void mostrarMenuPrincipal() throws IOException {
        Stage stagePorCerrar = (Stage) menuButton.getScene().getWindow();
        stagePorCerrar.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Inicio.fxml"));
        Parent root = (Parent)loader.load();
        Scene scene = new Scene(root, 444, 400); // Se crea la scene
        Stage stage = new Stage();
        stage.setTitle("Menú principal");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    // Abre la ventana para hacer una búsqueda con crawl

    public void opcionBusquedaConCrawl() throws IOException {
        // Cierra la ventana actual
        Stage stage = (Stage) bConCrawl.getScene().getWindow();
        stage.close();
        // Invoca la ventana de búsqueda
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Crawling.fxml"));
        Parent root = (Parent)loader.load();
        Scene secondScene = new Scene(root, 600, 400); // Se crea la scene
        Stage secondStage = new Stage();
        secondStage.setTitle("Búsqueda con crawl");
        secondStage.setScene(secondScene);
        secondStage.setResizable(false);
        secondStage.show();
    }

    // Cierra la ventana de menú principal  abre la ventana de búsqueda sin crawl
    public void abrirVentanaBusqueda(ActionEvent event) throws IOException{
        // Cierra la ventana actual
        Stage stagePorCerrar = (Stage) bSinCrawl.getScene().getWindow();
        stagePorCerrar.close();
        // Crea y abre la interfaz que muestra resultados
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Buscador.fxml"));
        Parent root = (Parent)loader.load();
        Scene secondScene = new Scene(root, 600, 400); // Se crea la scene
        Stage secondStage = new Stage();
        secondStage.setTitle("Buscador");
        secondStage.setScene(secondScene);
        secondStage.show();
    }
    // Cierra la ventana de crawl y hace la búsqueda
    public void abrirVentanaBusquedaConCrawl(ActionEvent event) throws IOException{
        // Cierra la ventana actual
        Stage stagePorCerrar = (Stage) abrirBusquedaCrawlButton.getScene().getWindow();
        stagePorCerrar.close();
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

                        // Corre el crawl
                        runTaskBusqueda(limite); // Ejecuta el método que hace la búsqueda
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
    }

    // Cancela el hilo de la búsqueda, si está activo
    public void cancelarTaskBusqueda(){
        taskBuscar.cancel();
        if(taskBuscar.isRunning()){
            System.out.println("Task corriendo");
        }
    }

    //Tarea de hacer el crawling de enlaces
    public void runTaskBusqueda(int limite) throws JSONException, URISyntaxException {
        // Deshabilita los botones
        iniciarCrawlingButton.setDisable(true);
        abrirBusquedaCrawlButton.setDisable(true);

        // Crawlea los enlaces

        progressBarCrawl.setProgress(0.1); //ACTUALIZA
        txtProgreso.setText("Crawling...");
        ArrayList<String> lista = crawlCanciones(limite); // Se almacena la lista de enlaces

        // Extrae los objetos de tipo Cancion (que incluyen id, título, artista y letra)

        progressBarCrawl.setProgress(0.2); //ACTUALIZA
        txtProgreso.setText("Extrayendo...");
        ArrayList<Cancion> listaCanciones = Scraper.extraerYAgregarALista(lista); // Este método toma como parámetro la lista de enlaces y extrae la información necesaria

        // Aquí extrae la información de las canciones y las guarda en BaseCanciones.json

        progressBarCrawl.setProgress(0.3); //ACTUALIZA
        txtProgreso.setText("Guardando información en archivo JSON...");
        BaseCancionesJSON.guardarInfoCanciones(listaCanciones);

        progressBarCrawl.setProgress(0.4); //ACTUALIZA
        txtProgreso.setText("Creando índice intermedio");
        ArrayList<ContenedorTerminoIdPosicion> indiceIntermedio = IndiceIntermedio.recorrerJSONLeido();

        // Aquí crea indiceInt.txt

        progressBarCrawl.setProgress(0.5); //ACTUALIZA
        txtProgreso.setText("Imprimiendo lista...");
        IndiceIntermedio.imprimirLista();

        // Construye el índice posicional a partir del índice intermedio

        progressBarCrawl.setProgress(0.6); //ACTUALIZA
        txtProgreso.setText("Construyendo índice posicional...");
        IndicePosicional.construirIndicePosicional(indiceIntermedio);

        // Crea indicePos.txt

        progressBarCrawl.setProgress(0.7); //ACTUALIZA
        txtProgreso.setText("Imprimiendo índice posicional...");
        IndicePosicional.imprimirIndicePosicional();

        // Crea IndicePosicional.json

        progressBarCrawl.setProgress(0.8); //ACTUALIZA
        txtProgreso.setText("Guardando índice...");
        TreeMap<String, ArrayList<Posting>> indice = IndicePosicional.obtenerIndice();
        IndiceJSON.guardarIndicePosicional(indice);

        // Crea Ranking.txt, aquí es donde se evalúa una consulta y se crea el archivo con los resultados más relevantes

        progressBarCrawl.setProgress(0.9); //ACTUALIZA
        txtProgreso.setText("Rankeando consulta...");
        Ranking.rankearConsulta("My momma said to stay away from guys like you");
        Ranking.imprimirRanking();

        progressBarCrawl.setProgress(1); //ACTUALIZA
        txtProgreso.setText("Completado");
        imprimirEnlaces(lista); // Imprime los enlaces en la interfaz
        iniciarCrawlingButton.setDisable(false); // Reactiva los botones
        abrirBusquedaCrawlButton.setDisable(false);

    }


    // Imprimir los enlaces en el textArea de abajo, recibe una lista de enlaces
    public void imprimirEnlaces(List<String> listaEnlaces){
        //System.out.println("Imprimiendo enlaces " + listaEnlaces.size());
        enlacesTextArea.appendText("Enlaces crawleados" + System.lineSeparator());
        for(String en: listaEnlaces) // Por cada string que devuelve el método crawlear
        {
            //System.out.println(en);
            enlacesTextArea.appendText(en + System.lineSeparator());
        }
    }

    // Para el botón de salir del menú

    public void doExit(){
        Platform.exit();
    }

}