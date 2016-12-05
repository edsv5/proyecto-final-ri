package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class PuntajePorFrase
{
    static final double AUMENTOPORFRASE = 5;
    static ArrayList< ArrayList<Integer>> listasDocumentosConConsulta = new ArrayList< ArrayList<Integer>>();
    static ArrayList<ArrayList<JSONObject>> listaObjetosConFrecuencia = new ArrayList< ArrayList<JSONObject>>();


    public static void llenarListaDocumentosConConsulta(JSONObject indicePosicional, ArrayList<String> consultaTokens)
    {
        for(String termino: consultaTokens)
        {

            listasDocumentosConConsulta.add(obtenerListaDocumentosPorTermino(indicePosicional, termino));
        }
    }

    public static ArrayList<Integer> obtenerListaConFraseCompleta()
    {
        ArrayList<Integer> interseccion = listasDocumentosConConsulta.get(0);
        for(ArrayList<Integer> lista: listasDocumentosConConsulta)
        {
            interseccion.retainAll(lista);
        }
        return interseccion;
    }

    public static void obtenerFrecuenciasDocumentosConFraseCompleta(JSONObject indicePosicional,
                                                                    ArrayList<String> consultaTokens,
                                                                    ArrayList<Integer> interseccion)
    {
        for(int doc: interseccion)
        {
            ArrayList<JSONObject> listaFrec = new ArrayList<JSONObject>();
            for(String termino: consultaTokens)
            {
                JSONArray postings = (JSONArray) indicePosicional.get(termino);
                for (int i = 0 ; i < postings.size(); i++)
                {
                    JSONObject obj = (JSONObject) postings.get(i);
                    int idDocJson = (int) (long) obj.get("idDoc");
                    if(idDocJson == doc)
                    {
                        listaFrec.add(obj);
                        break;
                    }
                }
            }
            listaObjetosConFrecuencia.add(listaFrec);
        }

    }

    public static ArrayList<ParScoreId> revisarSiTieneFrase(ArrayList<String> consultaTokens)
    {
        ArrayList<ParScoreId> listaPuntajesExtra = new ArrayList<ParScoreId>();
        int contadorPalabra = 0;
        for(ArrayList<JSONObject> listaDoc: listaObjetosConFrecuencia)
        {
            double puntajeExtra = 0;
            JSONObject obj = listaDoc.get(contadorPalabra);
            JSONArray posiciones = (JSONArray) obj.get("posiciones");
            int idDoc = (int) (long) obj.get("idDoc");
            contadorPalabra++;
            JSONObject objSiguiente = listaDoc.get(contadorPalabra);
            JSONArray  posicionesSiguiente = (JSONArray) objSiguiente.get("posiciones");
            for (int i = 0 ; i < posiciones.size(); i++)
            {
                int posicion = (int) (long) posiciones.get(i);
                puntajeExtra += AUMENTOPORFRASE / (double) consultaTokens.size();
                while(estaPosicionEnArray(posicion + 1, posicionesSiguiente) && contadorPalabra < consultaTokens.size() - 1 )
                {
                    puntajeExtra += AUMENTOPORFRASE / (double) consultaTokens.size();
                    posicion ++;
                    contadorPalabra++;
                    objSiguiente = listaDoc.get(contadorPalabra);
                    posicionesSiguiente = (JSONArray) objSiguiente.get("posiciones");
                }

            }
            listaPuntajesExtra.add(new ParScoreId(puntajeExtra, idDoc));
            contadorPalabra = 0;
        }
        return listaPuntajesExtra;
    }

    public static boolean estaPosicionEnArray(int posicion, JSONArray arrayPosiciones)
    {
        boolean encontrado = false;
        for (int i = 0 ; i < arrayPosiciones.size(); i++)
        {
            int pos = (int) (long) arrayPosiciones.get(i);
            if (pos == posicion)
            {
                encontrado = true;
                break;
            }
        }
        return encontrado;
    }

    public static ArrayList<Integer> obtenerListaDocumentosPorTermino(JSONObject indicePosicional, String termino)
    {
        ArrayList<Integer> listaDocumentos = new ArrayList<>();
        JSONArray postings = (JSONArray) indicePosicional.get(termino);
        if(postings != null)
        {
            for (int i = 0 ; i < postings.size(); i++)
            {
                JSONObject obj = (JSONObject) postings.get(i);
                int idDocJson = (int) (long) obj.get("idDoc");
                listaDocumentos.add(idDocJson);
            }
        }
        return listaDocumentos;
    }


    public static void imprimirLista()
    {
        StringBuilder sb = new StringBuilder();
        for(ArrayList<JSONObject> listaDoc: listaObjetosConFrecuencia)
        {
            for(JSONObject lob: listaDoc)
            {
                sb.append(lob);
            }
            sb.append("\n");
        }
        try (PrintStream out = new PrintStream(new FileOutputStream("lolalo.txt")))
        {
            out.print(sb.toString());
        }
        catch(IOException ex)
        {

        }
    }
}
