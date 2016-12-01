package main;


import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class EscritorJSON
{
    public static void escribir(JSONObject objeto)
    {
        try (FileWriter file = new FileWriter("BaseCanciones.json"))
        {
            file.write(objeto.toString());

        }
        catch(IOException ex)
        {
            System.out.println("Error de escritura: " + ex);
        }
    }
}
