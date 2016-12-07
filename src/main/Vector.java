package main;

/*Clase con métodos de vectores adaptados a listas de doubles
* Son necesarios para la elaboración del ranking, para comparar vectores de documentos con los de la consulta*/

import java.util.ArrayList;

public class Vector
{
    public static double productoPunto(ArrayList<Double> vec1, ArrayList<Double> vec2)
    {
        double producto = 0;

        for(int i = 0; i < vec1.size(); i++)
        {
            producto += vec1.get(i) * vec2.get(i);
        }
        return producto;
    }

    public static double normCoseno(ArrayList<Double> vec1, ArrayList<Double> vec2)
    {
        double sim = 0;
        ArrayList<Double> norm1 = normalizarVector(vec1);
        ArrayList<Double> norm2 = normalizarVector(vec2);
        sim = productoPunto(norm1, norm2);
        return sim;
    }

    public static ArrayList<Double> normalizarVector(ArrayList<Double> vec)
    {
        double modulo = moduloVector(vec);
        for(Double d : vec)
        {
            d = d / modulo;
        }
        return vec;
    }

    public static double moduloVector(ArrayList<Double> vec)
    {
        double modulo = 0;
        for(Double d : vec)
        {
            modulo += Math.pow(d, 2);
        }
        modulo = Math.sqrt(modulo);
        return modulo;
    }
}
