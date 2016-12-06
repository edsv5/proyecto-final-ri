package main;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class PuntajeTFIDF
{
    public static double tf(JSONObject indicePosicional, String termino, int docId)
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

    public static double tfConsulta( JSONObject indicePosicional, String termino, ArrayList<String> tokensConsulta)
    {
        double pesoTf = 0;
        double tf = Collections.frequency(tokensConsulta, termino);
        if(tf > 0)
            pesoTf = 1 + Math.log10(tf);
        return pesoTf;
    }


    public static double idf(JSONObject indicePosicional, String termino, int numDocs)
    {
        double pesoIdf = 0;
        JSONArray postings = (JSONArray) indicePosicional.get(termino);
        double df = postings.size();
        pesoIdf = Math.log10(numDocs / df);
        return pesoIdf;
    }
}
