package main;

import org.json.JSONException;
import org.json.JSONObject;

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
        //System.out.println(1);

        Object objeto = LectorJSON.leerDatosJSON("BaseCanciones.json"); // Lee la base de datos json
        //System.out.println(1.5);
        // SE ESTÁ PEGANDO ACÁ
        JSONObject base = (JSONObject) objeto;
        //System.out.println(2);
        // Identifica la canción por docId
        JSONObject objetoCancion = (JSONObject) base.get(String.valueOf(docIdPorAsociar));
        //System.out.println(3);
        // Se extrae el artista
        informacionExtraida[0] = (String) objetoCancion.get("artista");
        //System.out.println(4);
        // Se extrae el título
        informacionExtraida[1] = (String) objetoCancion.get("titulo");
        //System.out.println(5);
        // Se extrae la letra
        informacionExtraida[2] = (String) objetoCancion.get("letra");
        //System.out.println(6);

        return informacionExtraida;
    }


}
