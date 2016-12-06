package main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class IndicePosicional
{
    static TreeMap<String, ArrayList<Posting>> indice = new TreeMap<String, ArrayList<Posting>>();

    public static TreeMap<String, ArrayList<Posting>> obtenerIndice()
    {
        return indice;
    }

    public static void construirIndicePosicional(ArrayList<ContenedorTerminoIdPosicion> indiceIntermedio)
    {
        for(ContenedorTerminoIdPosicion contenedor: indiceIntermedio)
        {
            String termino = contenedor.getTermino();
            Posting postingNuevo = new Posting(contenedor);
            ingresoPostingsSinAcumular(termino, postingNuevo);

        }
        acumularPostingsPorTermino();

    }

    public static void ingresoPostingsSinAcumular(String termino, Posting postingNuevo)
    {
        ArrayList<Posting> listaPostingsNueva = new ArrayList<Posting>();
        listaPostingsNueva.add(postingNuevo);
        ArrayList<Posting> reemplazo = indice.put(termino.trim(), listaPostingsNueva);
        if(reemplazo != null)
        {
            reemplazo.addAll(listaPostingsNueva);
            indice.put(termino, reemplazo);
        }
    }

    public static void acumularPostingsPorTermino()
    {
        for(Map.Entry<String, ArrayList<Posting>> entry : indice.entrySet())
        {
            String termino = entry.getKey();
            ArrayList<Posting> listaPostings = entry.getValue();
            listaPostings = acumularLista(listaPostings);
            indice.put(termino, listaPostings);
        }
    }

    public static ArrayList<Posting> acumularLista(ArrayList<Posting> listaPostings)
    {
        ArrayList<Posting> listaAcumulada = new ArrayList<Posting>();

        if(listaAcumulada.isEmpty())
        {
            listaAcumulada.add(listaPostings.get(0));
        }
        listaPostings.remove(0);
        listaAcumulada = combinarOAgregarNuevo(listaPostings, listaAcumulada);
        return listaAcumulada;
    }

    public static ArrayList<Posting> combinarOAgregarNuevo( ArrayList<Posting> listaPostings, ArrayList<Posting> listaAcumulada )
    {
        for(Posting posting: listaPostings)
        {
            int ultimaPosicion = listaAcumulada.size() - 1;
            Posting acumulado = listaAcumulada.get(ultimaPosicion);
            if(posting.getIdDocumento() == acumulado.getIdDocumento())
            {
                acumulado.combinarPosting(posting);
                listaAcumulada.set(ultimaPosicion, acumulado);
            }
            else
            {
                listaAcumulada.add(posting);
            }
        }
        return listaAcumulada;
    }


    public static void imprimirIndicePosicional()
    {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, ArrayList<Posting>> entry : indice.entrySet())
        {
            String key = entry.getKey();
            ArrayList<Posting> value = entry.getValue();
            sb.append(key);
            sb.append(" ===> ");
            int contador = 0;
            for(Posting posting : value)
            {
                if (contador != 0)
                    sb.append(" -> ");
                sb.append("{");
                sb.append(posting.getIdDocumento());
                sb.append(", ");
                sb.append(posting.getFrecuencia());
                sb.append(", ");
                sb.append("[");
                int contador2 = 0;
                for(Integer i: posting.getListaPosicionesTermino())
                {
                    if(contador2 != 0)
                    {
                       sb.append(" , ");
                    }
                    sb.append(i);
                    contador2++;
                }
                sb.append("]");
                sb.append("}");
                contador ++;
            }
            sb.append("\n");
        }

        try (PrintStream out = new PrintStream(new FileOutputStream("indicePos.txt")))
        {
            out.print(sb.toString());
        }
        catch(IOException ex)
        {

        }
    }

}
