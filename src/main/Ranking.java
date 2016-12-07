package main;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

/*Clase encargada de rankear los documentos con base a la similitud de estos con cada consulta*/

public class Ranking
{
    static JSONObject indicePosicional = new JSONObject();
    static ArrayList<ParScoreId> ranking = new  ArrayList<ParScoreId>();

    public static void rankearConsulta(String consulta, String nombreDoc)
    {
        obtenerIndice();
        int numDocs = obtenerNumdocs(nombreDoc);
        ArrayList<String> consultaTokens = new ArrayList<String>(Arrays.asList(consulta.split(" ")));
        ArrayList<Double> pesosConsulta = construirVectorConsulta(consultaTokens, numDocs);
        for(int id = 1; id <= numDocs; id++)
        {
            double score = 0;
            ArrayList<Double> pesosDoc = construirVectorDocumentos(id, numDocs);
            score = Vector.normCoseno(pesosConsulta, pesosDoc);
            ranking.add(new ParScoreId(score, id));
        }
        //Una vez que se tiene el ranking por similitud, es hora de agregar puntos en caso de que la consulta
        //se encuentre tal como se digitó en alguno de los documentos, o sea una búsqueda por frase
        ordenarRanking();
        agregarPuntajePorFrase(consultaTokens);
    }

    //Se llama a la clase PuntajePorFrase para obtener la lista con los documentos que obtienen puntos extra por clase
    //junto con la cantidad de puntos que se le asignará
    //Una vez con la lista se buscan los ids dentro de ranking y se agregan los puntos extra

    public static  void agregarPuntajePorFrase(ArrayList<String> consultaTokens)
    {
        PuntajePorFrase.llenarListaDocumentosConConsulta(indicePosicional, consultaTokens);
        ArrayList<Integer> interseccion = PuntajePorFrase.obtenerListaConFraseCompleta();
        PuntajePorFrase.obtenerFrecuenciasDocumentosConFraseCompleta(indicePosicional, consultaTokens, interseccion);
        PuntajePorFrase.imprimirLista();
        ArrayList<ParScoreId> extra = PuntajePorFrase.revisarSiTieneFrase(consultaTokens);
        for(ParScoreId parExtra: extra)
        {
            for(ParScoreId par: ranking)
            {
                if(parExtra.getDocId() == par.getDocId())
                {
                    par.aumentarScore(parExtra.getScore());
                    break;
                }
            }
        }
    }

    //Se recorre todo el diccionario posible y a partir de ahí se construye un vector de pesos  para la consulta
    //usando el pesaje tf-idf

    public static ArrayList<Double> construirVectorConsulta(ArrayList<String> consultaTokens, int numDocs)
    {
        ArrayList<Double> pesos = new ArrayList<Double>();
        for(Iterator iterador = indicePosicional.keySet().iterator(); iterador.hasNext();)
        {
            String termino = (String) iterador.next();
            pesos.add(tfConsulta(termino, consultaTokens) * idf(termino, numDocs ));

        }
        return pesos;
    }

    //Se recorre todo el diccionario posible y a partir de ahí se construye un vector de pesos  para cada documento

    public static ArrayList<Double> construirVectorDocumentos(int idDocumento, int numDocs)
    {
        ArrayList<Double> pesos = new ArrayList<Double>();
        for(Iterator iterador = indicePosicional.keySet().iterator(); iterador.hasNext();)
        {
            String termino = (String) iterador.next();
            pesos.add(tf(termino, idDocumento) * idf(termino, numDocs));
        }
        return pesos;
    }

    public static double tf(String termino, int docId)
    {
        double pesoTf = 0;
        JSONArray postings = (JSONArray) indicePosicional.get(termino);
        for (int i = 0 ; i < postings.size(); i++)
        {
            JSONObject obj = (JSONObject) postings.get(i);
            int idDocJson = (int) (long) obj.get("idDoc");
            if(idDocJson == docId)
            {
                pesoTf = 1 + Math.log10((long) obj.get("frecuencia"));
                break;
            }
        }
        return pesoTf;
    }

    public static double tfConsulta( String termino, ArrayList<String> tokensConsulta)
    {
        double pesoTf = 0;
        double tf = Collections.frequency(tokensConsulta, termino);
        if(tf > 0)
            pesoTf = 1 + Math.log10(tf);
        return pesoTf;
    }


    public static double idf(String termino, int numDocs)
    {
        double pesoIdf = 0;
        JSONArray postings = (JSONArray) indicePosicional.get(termino);
        double df = postings.size();
        pesoIdf = Math.log10(numDocs / df);
        return pesoIdf;
    }

    //Obtiene el número de canciones en base
    // Se ingresa ahora el nombre del documento en los parámetros

    public static int obtenerNumdocs(String nombreDoc)
    {
        Object objeto = LectorJSON.leerDatosJSON(nombreDoc);
        JSONObject base = (JSONObject) objeto;
        int numDocs = base.keySet().size();
        return numDocs;
    }

    //Se obtiene el índice posicional a partir del documento json creado anteriormente

    public static void obtenerIndice()
    {
        Object objeto = LectorJSON.leerDatosJSON("IndicePosicional.json");
        indicePosicional = (JSONObject) objeto;
    }

    public static void ordenarRanking()
    {
        ranking.sort(new compParScoreId().reversed());
    }


    // Se modifica este método para que devuelva el ArrayList con los docIds
    public static ArrayList<Integer> imprimirRanking(String nombreRanking)
    {
        ArrayList<Integer> listaDeDocIds = new ArrayList<Integer>();
        StringBuilder sb = new StringBuilder();
        sb.append("Score - DocId" + System.lineSeparator());
        for(ParScoreId par : ranking)
        {
            listaDeDocIds.add(par.getDocId()); // Se añade el respectivo docId a la lista
            sb.append(par.getScore() + " - " + par.getDocId() + System.lineSeparator());
        }

        try (PrintStream out = new PrintStream(new FileOutputStream(nombreRanking)))
        {
            out.print(sb.toString());
        }
        catch(IOException ex) {}
        return listaDeDocIds;
    }
}
