package main;


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
