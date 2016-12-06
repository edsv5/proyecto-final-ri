package main;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

// Para filtrar con regex

public class Spider
{

    static ArrayList<String> enlacesPorLetra = new ArrayList<String>();
    static ArrayList<String> enlacesArtistasGlobal = new ArrayList<String>();
    static ArrayList<String> enlacesCancionesGlobal = new ArrayList<String>();
    static ArrayList<String> enlaces = new ArrayList<String>();

    // Recupera el documento inicial
    public static void obtenerEnlacesInicialDesdePrincipal(String link)
    {
        int contadorEnlaces = 0;
        Document doc = Recuperador.recuperarDocumento(link);
        // Selecciona todos los enlaces que encuentre en el documento
        Elements enlaces = doc.select(".clearfix.fs13.char_nav a");
        for(Element enlace: enlaces)
        {
            enlacesPorLetra.add(enlace.absUrl("href"));
            contadorEnlaces++; // Para ver cuántos enlaces encuentra
        }
        System.out.println("Cantidad de enlaces encontrados en documento inicial: " + contadorEnlaces);
    }

    public static void obtenerEnlacesArtistaPorLetra()
    {
        int contadorEnlacesArtistas = 0;
        for(String enlaceLetra: enlacesPorLetra)
        {
            // Recupera el documento del enlace de la letra
            Document doc = Recuperador.recuperarDocumento(enlaceLetra);
            Elements artistas = doc.select("a[href^=/lyrics/][title]");
            for(Element enlaceArtista: artistas)
            {
                enlacesArtistasGlobal.add(enlaceArtista.absUrl("href"));
                contadorEnlacesArtistas++;
            }
        }
        System.out.println("Enlaces de artistas encontrados en total: " + contadorEnlacesArtistas);
    }

    public static void obtenerEnlacesCancionPorArtista(int cantidad)
    {
        System.out.println("HPÑA");
        int contador = 1; // Empieza en 1 para que crawlee la cantidad ingresada
        for(String enlaceArtista: enlacesArtistasGlobal)
        {
            System.out.println("Artista actual " + enlaceArtista);
            Document doc = Recuperador.recuperarDocumento(enlaceArtista);
            Elements canciones = doc.select(".ui-song-title");
            for(Element enlaceCancion: canciones)
            {
                if(contador <= cantidad)
                {
                    enlacesCancionesGlobal.add(enlaceCancion.absUrl("href"));
                    contador++;
                }
                else
                {
                    return;
                }
            }
        }
        System.out.println("Contador enlaces canción por artista: " + contador);
    }

    // Se cambió de void a ArrayList<String> para que devuelva el arreglo global de enlaces

    public static ArrayList<String> crawlCanciones(int cantidad)
    {
        obtenerEnlacesInicialDesdePrincipal("http://www.lyricsmode.com");
        obtenerEnlacesArtistaPorLetra();
        obtenerEnlacesCancionPorArtista(cantidad);
        System.out.println("Tamaño de arreglo de enlacesCancionesGlobal: " + enlacesCancionesGlobal.size());
        return enlacesCancionesGlobal;
    }

}
