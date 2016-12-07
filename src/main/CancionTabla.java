package main;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Eduardo on 12/7/2016.
 * Clase Canción que puede ser mostrada en el TableView
 */
public class CancionTabla {

    private final SimpleStringProperty nombreArtista;
    private final SimpleStringProperty nombreCancion;
    private final SimpleStringProperty enlace;

    // Constructor con letra
    public CancionTabla(String nombreCancion, String nombreArtista, String letra) {
        this.nombreArtista = new SimpleStringProperty(nombreArtista);
        this.nombreCancion = new SimpleStringProperty(nombreCancion);
        this.enlace = new SimpleStringProperty(letra);
    }

    // Constructor sin letra
    private CancionTabla(String nombreCancion, String nombreArtista) {
        this.nombreArtista = new SimpleStringProperty(nombreArtista);
        this.nombreCancion = new SimpleStringProperty(nombreCancion);
        this.enlace = new SimpleStringProperty("");
    }


}
