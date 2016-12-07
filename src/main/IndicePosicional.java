package main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/*Se encarga de agarrar el índice intermedio creado anteriormente y acumularlo en un índice posicional verdadero
* Esto requiere eliminar las repeticiones de términos y acumularlos en sus apariciones por documento, indicando
* la frecuencia del término en cada documento y agregando las posiciones en una lista que muestre el lugar de aparicion
* del término en cada documento
* Los resultados se pueden ver en el indicePos.txt*/

public class IndicePosicional
{
    static TreeMap<String, ArrayList<Posting>> indice = new TreeMap<String, ArrayList<Posting>>();

    public static TreeMap<String, ArrayList<Posting>> obtenerIndice()
    {
        return indice;
    }

    //recorre cada documento y crea un posting a partir de cada contenedor, sin embargo aún no se están acumulando los postings

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

    //Se ingresan multiples veces el mismo término, pero cada vez con una lista de postings nueva
    //por ello se usa el valor retornado por el put para obtener la lista de postings vieja y encadenarla con la nueva
    //La lista sin acumular se puede ver así:
    // [1, 1, [1]] - [1, 1, [5]]

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

    //Como los postings tienen ids repetidos entonces se deben de acumular,
    //para ello se recorre todo el diccionario y se invoca a acumularLista

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

    //Método que guarda como primer elemento en la lista acumulada al primer elemento de la lista sin acumular, y se remueve este elemento
    //de la lista sin acumular
    //ahora se procede a acumular los postings de listaPostings en listaAcumulada

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

    //Método clave para la finalización del índice:
    //Se referencia a la ultima posicion de la lista acumulada, al principio de este método solo hay un posting, el extraído en acumularLista
    //Recorre la lista de postings sin acumular y toma la siguiente decisión:
    //Si coinciden en id de documento entonces se llama al método combinarPosting de postings y se establece este posting como el último de la
    //lista acumulada
    //Si no coinciden en id, simplemente se agrega a la lista acumulada
    //Al final se tendrán listas de postings en las cuales no se repetirá el docId por lista, estará la frecuencia por docId y la lista de posiciones
    //del término en cada documento

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
