package main;


import org.json.simple.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

//lista que contiene cada término de cada canción sin acumular, es decir, hay un campo para cada palabra aunque esté repetida ya en la lista
//cada palabra está con su id de documento y posicion
//El objetivo es que esta estructura sea un auxiliar para luego acumular los valores en el indice posicional

public class IndiceIntermedio
{
    static ArrayList<ContenedorTerminoIdPosicion> listaTerminoIdPosicion = new ArrayList<ContenedorTerminoIdPosicion>();

    //El índice intermedio se creará a partir de la información guardada en el JSON baseCanciones

    public static ArrayList<ContenedorTerminoIdPosicion> recorrerJSONLeido()
    {
        Object objeto = LectorJSON.leerDatosJSON("BaseCanciones.json");
        JSONObject base = (JSONObject) objeto;
        for(Iterator iterador = base.keySet().iterator(); iterador.hasNext();)
        {
            String  idDoc = (String) iterador.next();
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

    //Con cada termino individual de cada cancion se crea un objeto contenedor, se guarda la posición de este término en
    //la canción, por ejemplo: "la casa es vieja" del documento 1 crearía los siguientes contenedores:
    //[ la ][ 1 ][ 1] por ejemplo

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

    //Se realiza ordenamiento por término

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

