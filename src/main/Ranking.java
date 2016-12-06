package main;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.*;

public class Ranking
{
    static JSONObject indicePosicional = new JSONObject();
    static ArrayList<ParScoreId> ranking = new  ArrayList<ParScoreId>();

    public static void rankearConsulta(String consulta)
    {
        obtenerIndice();
        int numDocs = obtenerNumdocs();
        ArrayList<String> consultaTokens = new ArrayList<String>(Arrays.asList(consulta.split(" ")));
        ArrayList<Double> pesosConsulta = construirVectorConsulta(consultaTokens, numDocs);
        for(int id = 1; id <= numDocs; id++)
        {
            double score = 0;
            ArrayList<Double> pesosDoc = construirVectorDocumentos(id, numDocs);
            score = Vector.normCoseno(pesosConsulta, pesosDoc);
            ranking.add(new ParScoreId(score, id));
        }
        agregarPuntajePorFrase(consultaTokens);
        ordenarRanking();
    }

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

    public static ArrayList<Double> construirVectorConsulta(ArrayList<String> consultaTokens, int numDocs)
    {
        ArrayList<Double> pesos = new ArrayList<Double>();
        for(Iterator iterador = indicePosicional.keySet().iterator(); iterador.hasNext();)
        {
            String termino = (String) iterador.next();
            double tf  = PuntajeTFIDF.tfConsulta(indicePosicional, termino, consultaTokens);
            double idf = PuntajeTFIDF.idf(indicePosicional, termino, numDocs );
            pesos.add(tf * idf );

        }
        return pesos;
    }

    public static ArrayList<Double> construirVectorDocumentos(int idDocumento, int numDocs)
    {
        ArrayList<Double> pesos = new ArrayList<Double>();
        for(Iterator iterador = indicePosicional.keySet().iterator(); iterador.hasNext();)
        {
            String termino = (String) iterador.next();
            double tf  = PuntajeTFIDF.tf(indicePosicional, termino, idDocumento);
            double idf = PuntajeTFIDF.idf(indicePosicional, termino, numDocs);
            pesos.add(tf * idf);
        }
        return pesos;
    }



    public static int obtenerNumdocs()
    {
        Object objeto = LectorJSON.leerDatosJSON("BaseCanciones.json");
        JSONObject base = (JSONObject) objeto;
        int numDocs = base.keySet().size();
        return numDocs;
    }

    public static void obtenerIndice()
    {
        Object objeto = LectorJSON.leerDatosJSON("IndicePosicional.json");
        indicePosicional = (JSONObject) objeto;
    }

    public static void ordenarRanking()
    {
        ranking.sort(new compParScoreId().reversed());
    }


    public static void imprimirRanking()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Score DocId\n");
        for(ParScoreId par : ranking)
        {
            sb.append(par.getScore() + " " + par.getDocId() + "\n");
        }

        try (PrintStream out = new PrintStream(new FileOutputStream("Ranking.txt")))
        {
            out.print(sb.toString());
        }
        catch(IOException ex)
        {

        }
    }
}
