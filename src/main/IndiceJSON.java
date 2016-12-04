package main;

import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class IndiceJSON
{
    public static void guardarIndicePosicional(TreeMap<String, ArrayList<Posting>> indice)
    {
        JSONObject objetoTermino = procesarTerminos(indice);
        EscritorJSON.escribir(objetoTermino, "IndicePosicional.json");
    }

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

    public static JSONArray obtenerArregloPostings(ArrayList<Posting> postings)
    {
        JSONArray arregloPostings = new JSONArray();
        for(Posting posting: postings)
        {
            arregloPostings.add(obtenerDatosTermino(posting));
        }
        return arregloPostings;

    }

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
