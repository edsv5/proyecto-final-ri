package main;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;

public class PreprocesadorLDocumentos
{

    static ArrayList<ArrayList<String>> documentos = new ArrayList<ArrayList<String>>();

    /*
    * Extrae texto proveniente de párrafos, títulos, listas y tablas de cada documento.
    * */
    public static String extraerTexto(Document doc)
    {
        String texto = "";

        StringBuilder concatenacion = new StringBuilder();

        Elements parrafos = doc.select("p, li, h1, h2, h3, h4, h5, h6, th, tr");

        for(Element par : parrafos)
        {
            concatenacion.append(par.text());
            concatenacion.append(" ");
        }

        texto = concatenacion.toString();

        return texto;
    }


    /*
    * Elimina tokens que corresponden a numerales
    * */
    public static String quitarNumeros(String texto)
    {

        texto = texto.replaceAll("\\b([a-zA-Z]*[0-9]+[a-zA-Z]*)+\\b", " ");
        return  texto;
    }

    /*
    * Se encarga de eliminar carácteres no alfanuméricos, dobles espacios y pasar todas las mayúsculas a minúscula
    * */
    public static String normalizar(String texto)
    {
        texto = removerCaracteresEspeciales(texto);
        texto = quitarMayusculas(texto);
        return texto;
    }

    public static String removerCaracteresEspeciales(String texto)
    {
        texto = texto.replaceAll("'", ""); //En el caso de la comilla se elimina, no se sustituye por espacio
        texto = texto.replaceAll("[^A-Za-z0-9 ]", " ");
        texto = texto.replaceAll("[ ]+", " ");
        return texto;
    }

    public static String quitarMayusculas(String texto)
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < texto.length(); i++)
        {
            int ascii = (int) texto.charAt(i);
            if(ascii >= 65 && ascii <= 90 )
            {
                char c = (char) (ascii + 32);
                sb.append( c );
            }
            else
            {
                sb.append(texto.charAt(i));
            }
        }

        return sb.toString();
    }


    /*
    * Divide el texto en tokens usando el espacio como delimitador
    * */
    public static ArrayList<String> tokenizar(String texto)
    {
        ArrayList<String> listaTokens = new ArrayList<String>(Arrays.asList(texto.split(" ")));

        return listaTokens;
    }

    /*
    * Llama a todos los métodos anteriores para preprocesar el texto del documento
    * */
    public static void preprocesar(Document doc)
    {
        ArrayList<String> docPrep = new ArrayList<String>();

        String texto = extraerTexto(doc);
        texto = normalizar(texto);
        docPrep = tokenizar(texto);

        documentos.add(docPrep);
    }
}
