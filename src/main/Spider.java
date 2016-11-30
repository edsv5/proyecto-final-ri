package main;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Para filtrar con regex

public class Spider
{

    static ArrayList<String> enlacesPorLetra = new ArrayList<String>();
    static ArrayList<String> enlacesArtistasGlobal = new ArrayList<String>();
    static ArrayList<String> enlacesCancionesGlobal = new ArrayList<String>();
    static ArrayList<String> enlaces = new ArrayList<String>();
    static int nivel = 0;
    static int cantidad = 0;
    static String dominio = "";
    // Se va a usar esto para llevar registro del progreso del crawl
    static int contadorLinks = 0;


    public static Document recuperarDocumento(String url)
    {
        Document doc = null;
        try
        {
            Connection con  = Jsoup.connect(url)
                                   .ignoreContentType(true)
                                   .userAgent("Mozilla/5.0")
                                   .timeout(100000);
            Connection.Response resp = con.execute();

            URI uri = new URI(url);
            dominio = uri.getHost();

            if (resp.statusCode() == 200)
            {
                doc = con.get();
            }

        }
        catch (IOException | URISyntaxException ex)
        {
            System.out.println("Excepción al obtener el HTML de la página " + url + " " + ex.getMessage());
        }
        return doc;
    }


    public static ArrayList<String> obtenerEnlaces(Document doc)  throws java.net.URISyntaxException
    {
        ArrayList<String> enlacesDoc = new ArrayList<String>();
        if(doc != null)
        {
            Elements links = doc.select("a[href]");

            String titulo = doc.select("title").text();
            boolean filtrarTitulo = filtrarPorTitulo(titulo);

            if(!filtrarTitulo)
            {
                for(Element link : links)
                {

                    String enlace = link.attr("href");
                    boolean filtrarEnlace = filtrarPorUrl(enlace);
                    if(!filtrarEnlace)
                    {
                        enlace = link.absUrl("href");
                        if(enlace.contains(dominio))
                        {
                            if(!enlaces.contains(enlace.trim()))
                            {
                                enlacesDoc.add(enlace);
                            }
                            else {}
                        }
                    }
                }
            }
        }

        return enlacesDoc;
    }

    public static void obtenerEnlacesInicialDesdePrincipal(String link)
    {
        Document doc = recuperarDocumento(link);
        Elements enlaces = doc.select(".clearfix.fs13.char_nav a");
        for(Element enlace: enlaces)
        {
            enlacesPorLetra.add(enlace.absUrl("href"));
        }

    }

    public static void obtenerEnlacesArtistaPorLetra()
    {

        for(String enlaceLetra: enlacesPorLetra)
        {
            Document doc = recuperarDocumento(enlaceLetra);
            Elements artistas = doc.select("a[href^=/lyrics/][title]");
            for(Element enlaceArtista: artistas)
            {
                enlacesArtistasGlobal.add(enlaceArtista.absUrl("href"));
            }
        }
    }

    public static void obtenerEnlacesCancionPorArtista()
    {
        int contador = 0;
        for(String enlaceArtista: enlacesArtistasGlobal)
        {
            System.out.println("Artista actual " + enlaceArtista);
            Document doc = recuperarDocumento(enlaceArtista);
            Elements canciones = doc.select(".ui-song-title");
            for(Element enlaceCancion: canciones)
            {
                if(contador <= 100)
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
    }

    public static boolean filtrarPorTitulo(String titulo)
    {
        boolean filtrar = false;

        if(titulo.contains("User") )
            filtrar = true;

        return filtrar;
    }


    // Todas las letras de lyricsmode son del formato http://www.lyricsmode.com/lyrics/<letra>/<artista>/<nombre_de_la_canción>.html
    public static boolean filtrarPorUrl(String enlace){
        //System.out.println("Link: " + enlace);
        boolean filtrar = true;
        String patron = "/lyrics/[a-z]/\\w+/\\w+.html";
        Pattern p = Pattern.compile(patron);
        Matcher m = p.matcher(enlace);
        // Si encuentra matches en el patrón del link, no filtrar
        if (m.find() ) {
            filtrar = false;
            //System.out.println("Link valido: " + enlace);
        }else{
            //System.out.println("El link es caca: " + enlace);
        }

        return filtrar;
    }


    public static void crawl(ArrayList<String> links, int profundidad, int limite)  throws java.net.URISyntaxException
    {

        if(!links.isEmpty())
        {
            nivel ++;

            for(String link : links)
            {
                if(!enlaces.contains(link))
                {
                    enlaces.add(link);
                    cantidad ++;
                    System.out.println("Documento #" + cantidad + " Link: " + link); // Aquí se hace la impresión de cada documento

                    Document doc = recuperarDocumento(link);

                    PreprocesadorLDocumentos.preprocesar(doc);

                    ArrayList<String> res = new ArrayList<String>();
                    res.add(doc.title());
                    res.add(link);
                    Resultados.agregarResultado(cantidad, res);

                    // Aumentar el contador de progreso

                    contadorLinks++;

                    ArrayList<String> nuevosEnlaces = obtenerEnlaces(doc);

                    try
                    {
                        if(nivel <= profundidad && cantidad <= limite)
                        {
                            crawl(nuevosEnlaces, profundidad, limite);
                            nivel --;
                        }
                        else
                            return;

                    }
                    catch(NullPointerException e)
                    {

                    }
                }

            }
        }

    }

    // Hace la búsqueda y devuelve la lista de enlaces
    public static List<String> busqueda(int profundidad, int limite) throws java.net.URISyntaxException
    {
        //pruebitaBorrarDespues();
        Document docInicial = recuperarDocumento("http://www.lyricsmode.com");
        ArrayList<String> listaInicial = new ArrayList<String>();
        listaInicial = Spider.obtenerEnlaces(docInicial);
        crawl(listaInicial, profundidad, limite);
        //imprimirEnlaces();

        return arregloEnlaces(); // Devuelve la lista de enlaces
    }

    public static void pruebitaBorrarDespues()
    {
        obtenerEnlacesInicialDesdePrincipal("http://www.lyricsmode.com");
        obtenerEnlacesArtistaPorLetra();
        obtenerEnlacesCancionPorArtista();
        System.out.println("Terminó: " + enlacesCancionesGlobal.size());
    }
    public static void imprimirEnlaces()
    {
        System.out.println("Impresión de araña: ");
        for(String en: enlaces)
        {
            System.out.println(en);
        }
    }

    // Devuelve un arreglo con el string que se debe imprimir
    public static List<String> arregloEnlaces(){
        System.out.println("arregloEnlaces");
        // Se crea el arreglo
        List<String> arregloEnlaces = new ArrayList<String>();
        // Se le pegan todos los enlaces en orden
        for(String en: enlaces)
        {
            arregloEnlaces.add(en); // Se agrega el string al arreglo
        }
        return arregloEnlaces;
    }

}
