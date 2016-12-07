package main;

import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/*
* Clase encargada de guardar el índice construido en IndicePosicional y guardarlo en un .json
* desde el cual se podrán efectuar las búsquedas de ahora en adelante, sin necesidad de
* mantener el TreeMap en memoria todo el tiempo
*
* */

public class IndiceJSON
{
    public static void guardarIndicePosicional(TreeMap<String, ArrayList<Posting>> indice)
    {
        JSONObject objetoTermino = procesarTerminos(indice);
        EscritorJSON.escribir(objetoTermino, "IndicePosicional.json");
    }

    //Se encarga de recorrer el índice posicional término por término creando objetos json
    //que tendran como llave el término y como valor un array de objetos
    //Cada objeto representa un posting del índice entonces contiene: Id de documento, frecuencia de término en documento
    //y posiciones del término en el documento

    public static JSONObject procesarTerminos(TreeMap<String, ArrayList<Posting>> indice)
    {
        JSONObject objetoTermino = new JSONObject();
        try
        {
            for(Map.Entry<String, ArrayList<Posting>> entry : indice.entrySet())
            {
                String termino = entry.getKey();
                ArrayList<Posting> postings = entry.getValue();
                objetoTermino.put(termino, obtenerArregloPostings(postings));
            }
        }
        catch(JSONException ex)
        {
            System.out.println(ex);
        }
        return objetoTermino;
    }

    //Crea el arreglo de objetos que es el valor de la llave  dada por cada término

    public static JSONArray obtenerArregloPostings(ArrayList<Posting> postings)
    {
        JSONArray arregloPostings = new JSONArray();
        for(Posting posting: postings)
        {
            arregloPostings.add(obtenerDatosTermino(posting));
        }
        return arregloPostings;

    }

    //Ingresa el posting a un objeto Json

    public static JSONObject obtenerDatosTermino(Posting posting)
    {
        JSONObject objetoDatosTermino = new JSONObject();
        try
        {
            objetoDatosTermino.put("idDoc", posting.getIdDocumento());
            objetoDatosTermino.put("frecuencia", posting.getFrecuencia());
            objetoDatosTermino.put("posiciones", obtenerArregloPosiciones(posting.getListaPosicionesTermino()));
        }
        catch(JSONException ex)
        {
            System.out.println(ex);
        }
        return objetoDatosTermino;
    }

    //Procesa el array de posiciones dentro del posting y lo pasa a un JSONArray que estará en el objeto JSON

    public static JSONArray obtenerArregloPosiciones(ArrayList<Integer> posiciones)
    {
        org.json.simple.JSONArray arregloPosiciones = new org.json.simple.JSONArray();
        for(int posicion: posiciones)
        {
            arregloPosiciones.add(posicion);
        }
        return arregloPosiciones;
    }
}
