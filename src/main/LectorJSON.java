package main;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class LectorJSON
{
    public static Object leerDatosCanciones()
    {
        Object objeto = null;

        try
        {
            JSONParser parser = new JSONParser();
            objeto = parser.parse(new FileReader("BaseCancionesJSON.json"));
        }
        catch( ParseException | IOException ex)
        {
            System.out.println("Error de lectura " + ex);
        }
        return objeto;
    }
}
