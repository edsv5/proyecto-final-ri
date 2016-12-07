package main;

import org.json.JSONException;
import org.json.simple.JSONObject;

/**
 * Created by Eduardo on 12/6/2016.
 * Asocia docId's a su respectiva información
 */

// Recibe una lista con los números de los docIds y devuelve la lista con los enlaces en el mismo orden
public class Asociador {

    // Recibe un docId por asociar y devuelve un arreglo de tres strings con artista, nombre de la canción y letra

    public static String[] extraerInformacionDeDocId(int docIdPorAsociar) throws JSONException {
        //System.out.println("Extrayendo info");
        String[] informacionExtraida = new String[3]; // Crea el arreglo para contener los 3 elementos

        Object objeto = LectorJSON.leerDatosJSON("BaseCanciones.json"); // Lee la base de datos json
        JSONObject base = (JSONObject) objeto;
        // Identifica la canción por docId
        JSONObject objetoCancion = (JSONObject) base.get(String.valueOf(docIdPorAsociar));
        // Se extrae el artista
        informacionExtraida[0] = (String) objetoCancion.get("artista");
        // Se extrae el título
        informacionExtraida[1] = (String) objetoCancion.get("titulo");
        // Se extrae la letra
        informacionExtraida[2] = (String) objetoCancion.get("letra");
        return informacionExtraida;
    }


}
