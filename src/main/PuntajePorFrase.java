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
    //Lista que guardará los ids de los documentos que contengan cada término de la consulta
    static ArrayList< ArrayList<Integer>> listasDocumentosConConsulta = new ArrayList< ArrayList<Integer>>();
    static ArrayList<ArrayList<JSONObject>> listaObjetosConFrecuencia = new ArrayList< ArrayList<JSONObject>>();

    // Por cada término se obtiene una lista de los ids de documentos que lo contienen
    //cada lista se agrega a la listaDocumentosConConsulta

    public static void llenarListaDocumentosConConsulta(JSONObject indicePosicional, ArrayList<String> consultaTokens)
    {
        for(String termino: consultaTokens)
        {

            listasDocumentosConConsulta.add(obtenerListaDocumentosPorTermino(indicePosicional, termino));
        }
    }

    //Agarrando la lista de listas con los docIds en los que aparece cada término
    //se crea una solo lista de la intersección de todas las listas en listasDocumentosConConsultas
    //Así al final se tendrá una lista con los documentos que tienen TODOS los términos de la consulta
    //sin embargo esto no asegura que aparezca la frase seguida en estos documentos

    public static ArrayList<Integer> obtenerListaConFraseCompleta()
    {
        ArrayList<Integer> interseccion = listasDocumentosConConsulta.get(0);
        for(ArrayList<Integer> lista: listasDocumentosConConsulta)
        {
            interseccion.retainAll(lista);
        }
        return interseccion;
    }

    //A partir de la intersección se agregan elementos a la lista de listas de objetos JSON
    //Esta lista de listas tendrá la siguiente estructura:
    //Habrá una lista para cada documento que se encuentre en la intersección
    //Cada lista será una lista de objetos
    //Se recuperará el objeto con el id de documento para cada uno de los términos de la consulta
    //Así una lista de estas se vería así por ejemplo:
    //{ docId: 1, frecuencia: 1, posiciones: [ 1, 2, 3]} - { docId: 1, frecuencia: 1, posiciones: [ 6, 7, 8]} - ...
    //La idea es que cada elemento de esta lista sea el posting correspondiente al docId en cada uno de los términos de la consulta
    //Por ello esta lista será tan larga como el número de términos de la consulta
    //Esta lista facilita la búsqueda de terminos en posiciones consecutivas dentro de un mismo documento

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

    //Es el método más importante, encargado de agregar puntos extra proporcionales a la porción de palabras de la frase
    //que se encuentren consecutivamente en cada documento que contenga todas las palabras de la frase
    //Por cada lista de objetos en listaObjetosConPosiciones se hace lo siguiente:
    //Por ejemplo con la frase: soy Carlos, si se encuentra a "soy" en la posicion 3 del doc 1 y "Carlos" en la posicion 4 del doc 1
    //Entonces si se da un match con la frase
    //Por lo tanto la idea es buscar una palabra en una posicion x y asegurarse que la palabra siguiente en la consulta se encuentre en la posicon
    //x + 1 en el mismo documento y que la palabra que sigue esté en la posicion x + 2, etc.
    //Cada vez que se cumpla esta condicion se suma un porcentaje de AUMENTOPORFRASE
    //Si se encontrara la frase entera entonces el aumento a ese documento será igual a AUMENTOFRASE.

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

    //Se fija si la posicion ingresada se encuentra en el JSONArray indicado,
    //Esto sirve para ver si en el siguiente término de la búsqueda se encuentra en cierto documento en la posición deseada

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

    //Método encargado de obtener la lista de docIds que contengan al término indicado

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
