package main;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class IndiceIntermedio
{
    static ArrayList<ContenedorTerminoIdPosicion> listaTerminoIdPosicion = new ArrayList<ContenedorTerminoIdPosicion>();

    public static ArrayList<ContenedorTerminoIdPosicion> recorrerJSONLeido()
    {
        Object objeto = LectorJSON.leerDatosCanciones();
        JSONObject base = (JSONObject) objeto;
        for(Iterator iterator = base.keySet().iterator(); iterator.hasNext();)
        {
            String  idDoc = (String) iterator.next();
            JSONObject objetoCancion = (JSONObject) base.get(idDoc);
            String letras = (String) objetoCancion.get("letra");
            ArrayList<String> listaTerminos = tokenizarLetras(letras);
            agregarTuplaALista(Integer.parseInt(idDoc), listaTerminos);
        }
        ordenarListaPorTermino();
        return listaTerminoIdPosicion;
    }

    public static  ArrayList<String> tokenizarLetras(String letras)
    {
        ArrayList<String> listaTerminosPorCancion = new ArrayList<String>(Arrays.asList(letras.split(" ")));
        return listaTerminosPorCancion;
    }

    public static void agregarTuplaALista(int idDocumento, ArrayList<String> listaTerminosPorCancion)
    {
        int posicion = 1;
        for(String termino: listaTerminosPorCancion)
        {
            if(!termino.equals("br"))
            {
                listaTerminoIdPosicion.add(new ContenedorTerminoIdPosicion(termino.trim(), idDocumento, posicion));
                posicion ++;
            }
        }
    }

    public static void ordenarListaPorTermino()
    {
        Collections.sort(listaTerminoIdPosicion);
    }

    public static void imprimirLista()
    {
        String imp = "";
        for(ContenedorTerminoIdPosicion contenedor: listaTerminoIdPosicion)
        {
            imp += contenedor.getTermino() + " " + contenedor.getIdDocumento() + " " + contenedor.getPosicion() + "\n";
        }

        try (PrintStream out = new PrintStream(new FileOutputStream("indiceInt.txt")))
        {
            out.print(imp);
        }
        catch(IOException ex)
        {

        }
    }
}

