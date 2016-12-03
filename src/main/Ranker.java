package main;

import java.util.*;

public class Ranker
{

    //Lista que guarda los documentos en orden de similitud con la consulta
    ArrayList<ParScoreId> ranking;

    public Ranker(TreeMap<String, ArrayList<ParIdFrec>> indice, String consulta, int numDocs)
    {
        ranking = new ArrayList<ParScoreId>();
        rank(indice, consulta, numDocs);
        ranking.sort(new compParScoreId().reversed());
    }

    public void rank(TreeMap<String, ArrayList<ParIdFrec>> indice, String consulta, int numDocs)
    {

        ArrayList<String> consultaTokens = new ArrayList<String>(Arrays.asList(consulta.split(" ")));
        ArrayList<Double> pesosConsulta = construirVectorConsulta(indice, consultaTokens, numDocs );


        for(int id = 1; id <= numDocs; id++)
        {
            double score = 0;
            ArrayList<Double> pesosDoc = construirVectorDocumentos(indice, numDocs, id);
            score = normCoseno(pesosConsulta, pesosDoc);
            ranking.add(new ParScoreId(score, id));
        }

    }

    public double idf(TreeMap<String, ArrayList<ParIdFrec>> indice, String termino, int numDocs)
    {
        double pesoIdf = 0;
        double df = indice.get(termino).size();
        pesoIdf = Math.log10(numDocs / df);
        return pesoIdf;
    }


    public double tf(TreeMap<String, ArrayList<ParIdFrec>> indice, String termino, int docId)
    {
        double pesoTf = 0;
        ArrayList<ParIdFrec> postings = indice.get(termino);
        for(ParIdFrec par : postings)
        {
            if(par.getDocId() == docId)
            {
                pesoTf = 1 + Math.log10(par.getFrecuencia());
                break;
            }
        }

        return pesoTf;

    }

    public double tfConsulta(TreeMap<String, ArrayList<ParIdFrec>> indice, String termino, ArrayList<String> tokensConsulta)
    {
        double pesoTf = 0;
        double tf = Collections.frequency(tokensConsulta, termino);

        if(tf > 0)
            pesoTf = 1 + Math.log10(tf);

        return pesoTf;
    }


    public ArrayList<Double> normalizarVector(ArrayList<Double> vec)
    {
        double modulo = moduloVector(vec);
        for(Double d : vec)
        {
            d = d / modulo;
        }
        return vec;
    }

    public double moduloVector(ArrayList<Double> vec)
    {
        double modulo = 0;
        for(Double d : vec)
        {
            modulo += Math.pow(d, 2);
        }
        modulo = Math.sqrt(modulo);
        return modulo;
    }


    public ArrayList<Double> construirVectorDocumentos(TreeMap<String, ArrayList<ParIdFrec>> indice, int numDocs, int docId)
    {
        ArrayList<Double> pesos = new ArrayList<Double>();

        for(Map.Entry<String, ArrayList<ParIdFrec>> entrada : indice.entrySet())
        {
            String termino = entrada.getKey();
            pesos.add(tf(indice, termino, docId) * idf(indice, termino, numDocs));

        }

        return pesos;
    }

    public ArrayList<Double> construirVectorConsulta(TreeMap<String, ArrayList<ParIdFrec>> indice, ArrayList<String> tokens, int numDocs)
    {
        ArrayList<Double> pesos = new ArrayList<Double>();
        for(Map.Entry<String, ArrayList<ParIdFrec>> entrada : indice.entrySet())
        {
            String termino = entrada.getKey();
            pesos.add(tfConsulta(indice, termino, tokens) * idf(indice, termino, numDocs ));

        }

        //pesos = normalizarVector(pesos);
        return pesos;
    }

    public double productoPunto(ArrayList<Double> vec1, ArrayList<Double> vec2)
    {
        double producto = 0;

        for(int i = 0; i < vec1.size(); i++)
        {
            producto += vec1.get(i) * vec2.get(i);
        }
        return producto;
    }

    public double normCoseno(ArrayList<Double> vec1, ArrayList<Double> vec2)
    {
        double sim = 0;
        ArrayList<Double> norm1 = normalizarVector(vec1);
        ArrayList<Double> norm2 = normalizarVector(vec2);
        sim = productoPunto(norm1, norm2);
        return sim;
    }

    public String imprimirRanking()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Score DocId\n");
        for(ParScoreId par : ranking)
        {
            sb.append(par.getScore() + " " + par.getDocId() + "\n");
        }
        sb.append(ranking.size());
        return sb.toString();
    }

    public ArrayList<Integer> getTop(int cantidad)
    {
        ArrayList<Integer> top = new ArrayList<>();
        for(int i = 0; i < cantidad; i++)
        {
           top.add(ranking.get(i).getDocId());
        }
        return top;
    }
}
