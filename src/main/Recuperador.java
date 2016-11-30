package main;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Recuperador
{
    public static Document recuperarDocumento(String url)
    {
        Document doc = null;
        try
        {
            Connection con  = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .userAgent("Mozilla/5.0")
                    .timeout(100000);
            Connection.Response resp = con.execute();
            if (resp.statusCode() == 200)
            {
                doc = con.get();
            }

        }
        catch (IOException ex)
        {
            System.out.println("Excepción al obtener el HTML de la página " + url + " " + ex.getMessage());
        }
        return doc;
    }
}
