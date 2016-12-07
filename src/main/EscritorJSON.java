package main;

//Clase general que solo se encarga de escribir un archivo .json con el nombre especificado

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class EscritorJSON
{
    public static void escribir(JSONObject objeto, String nombre)
    {
        try (FileWriter file = new FileWriter(nombre))
        {
            file.write(objeto.toString());

        }
        catch(IOException ex)
        {
            System.out.println("Error de escritura: " + ex);
        }
    }
}
