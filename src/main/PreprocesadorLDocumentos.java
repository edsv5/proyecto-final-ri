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
        texto = texto.replaceAll(" lyrics","");
        texto = texto.replaceAll("\\'", ""); //En el caso de la comilla se elimina, no se sustituye por espacio
        texto = texto.replaceAll("\\[(A-Za-z0-9)+\\]", " ");
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


}
