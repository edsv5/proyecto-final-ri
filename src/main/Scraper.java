package main;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;


public class Scraper
{
    static ArrayList<Cancion> listaCanciones = new ArrayList<Cancion>();

    public static ArrayList<Cancion> extraerYAgregarALista(ArrayList<String> enlacesCanciones)
    {
        int idContador = 1;
        for(String enlace: enlacesCanciones)
        {
            Document doc = Recuperador.recuperarDocumento(enlace);
            String artista = extraerArtista(doc);
            String titulo  = extraerTitulo(doc);
            String letra   = extraerLetra(doc);
            listaCanciones.add(new Cancion(idContador, titulo, artista, letra));
            idContador ++;
        }
        return listaCanciones;
    }

    public static String extraerArtista(Document doc)
    {
        Element linkArtista = doc.select(".header-band-name").first();
        String  artista = linkArtista.text();
        return artista;
    }

    public static String extraerLetra(Document doc)
    {
        Element parrafoLetra = doc.select("#lyrics_text").first();
        String letra = parrafoLetra.html();
        letra = PreprocesadorLDocumentos.normalizar(letra);
        return letra;
    }

    public static String extraerTitulo(Document doc)
    {
        Element nombre = doc.select(".header-song-name").first();
        String  titulo = nombre.text();
        titulo = titulo.replaceAll(" lyrics", "");
        return titulo;
    }
}
