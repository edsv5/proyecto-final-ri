package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Resultados
{
    static HashMap <Integer, ArrayList<String>> resultados = new HashMap<Integer, ArrayList<String>>();

    public static void agregarResultado(int docId, ArrayList<String> datos)
    {
        resultados.put(docId, datos);
    }

    public static ArrayList<ArrayList<String> > retornarTop(ArrayList<Integer> mejores)
    {
        ArrayList<ArrayList<String> > top = new ArrayList<ArrayList<String> >();
        for(int i : mejores)
        {
            ArrayList<String> resultado = new ArrayList<String>();
            ArrayList<String> datos = resultados.get(i);
            resultado.add(datos.get(0));//Se agrega el título
            resultado.add(datos.get(1));//Se agrega el link
            top.add(resultado);
        }
        return top;
    }

    public static String imprimirResultados()
    {
        StringBuilder sb = new StringBuilder();

        for(Map.Entry<Integer,ArrayList<String>> entry : resultados.entrySet())
        {
            int key = entry.getKey();
            ArrayList<String> value = entry.getValue();

            sb.append("# doc " + key + " ");
            sb.append("Titulo: ");
            sb.append(value.get(0));
            sb.append(", ");
            sb.append("Enlace: ");
            sb.append(value.get(1));

            sb.append("\n");

        }

        return sb.toString();
    }

}
