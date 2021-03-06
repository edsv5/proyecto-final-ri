package main;

/*Clase encargada de obtener un objeto JSON a partir del documento indicado por nombre*/

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class LectorJSON
{
    public static Object leerDatosJSON(String nombre)
    {
        Object objeto = null;
        try
        {
            JSONParser parser = new JSONParser();
            objeto = parser.parse(new FileReader(nombre));
        }
        catch( ParseException | IOException ex)
        {
            System.out.println("Error de lectura " + ex);
        }
        return objeto;
    }
}
