package main;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.lang.reflect.Array;
import java.util.*;


public class BSBI_Indexer
{
    static ArrayList<ArrayList<String>> bloques;
    static ArrayList<ArrayList<ParTermId>> listaPares;
    static TreeMap<String, ArrayList<Integer>> indice;
    static TreeMap<String, ArrayList<ParIdFrec>> indiceFrecuencial;
    static int numDocs;




    public static void creacionIndices(int tamBloque)
    {
        bloques = PrepLing.documentos;
        numDocs = PrepLing.documentos.size();
        listaPares = new ArrayList<ArrayList<ParTermId>>();
        indice =  new TreeMap<String, ArrayList<Integer>>();
        indiceFrecuencial = new TreeMap<String, ArrayList<ParIdFrec>>();
        indexar(tamBloque);
        invertir();
        crearIndice();
        crearIndiceFrecuencial();
    }

    /*
    * Establece el número de bloques en los que se dividirán las listas de tokens. Se efectúa la división.
    * */
    public static void indexar(int tambBloque)
    {
        int salto = bloques.size() / tambBloque;

        for(int i = 0; i <= bloques.size(); i+= salto)
        {
            partirBloques(i, salto);
        }

    }


    public static String imprimirListaPares()
    {

        StringBuilder sb = new StringBuilder();
        int c = 1;
        for(ArrayList<ParTermId> bloque : listaPares )
        {
            sb.append("\n Bloque "+ c + "\n");
            for(ParTermId par : bloque)
            {
                sb.append(par.getTermino());
                sb.append(" ");
                sb.append(par.getDocId());
                sb.append("\n");

            }
            c++;
        }
        return sb.toString();
    }

    /*
    * Se encarga de dividir las palabras y sus docIds por bloques, guardándose en "listaPares".
    * */
    public static void partirBloques(int docActual, int tambloque)
    {
        int numDocs = bloques.size();

        int docLimite = tambloque + docActual;

        if(docLimite > numDocs)
        {
            docLimite -= tambloque;
            docLimite += numDocs - docActual;
        }



        ArrayList<ParTermId> tuplas = new ArrayList<ParTermId>();
        for(int i = docActual; i < docLimite; i++)
        {
            //System.out.println("Documento " + i);
            for(String tok : bloques.get(i))
            {
                ParTermId par = new ParTermId(tok, i + 1);
                // System.out.println(tok);
                tuplas.add(par);
            }

        }
        listaPares.add(tuplas);

    }

    /*
    * Ordena las listas de ParTermId por término.
    * */
    public static void invertir()
    {
        for(ArrayList<ParTermId> bloque : listaPares )
        {
            Collections.sort(bloque);
        }
    }


    /*
    * Recorre todo lista pares y se encarga de agrupar por términos y acumular los postings de cada uno.
    * Esto se hace representando al diccionario como un TreeMap <String, ArrayList<Integer>>
    * */
    public static void crearIndice()
    {
        for(ArrayList<ParTermId> bloque : listaPares)
        {
            for(ParTermId par: bloque)
            {
                ArrayList<Integer> postings = new ArrayList<Integer>();

                String termino = par.getTermino();
                int docId = par.getDocId();
                postings.add(docId);
                ArrayList<Integer> reemplazo = indice.put(termino, postings);

                if(reemplazo != null)
                {
                    reemplazo.addAll(postings);
                    //reemplazo = new ArrayList<Integer>(new LinkedHashSet<Integer>(reemplazo));
                    indice.put(termino, reemplazo);
                }


            }
        }
    }

    public static ArrayList<ParIdFrec> obtenerFrecuencias(ArrayList<Integer> repPostings)
    {
        ArrayList<ParIdFrec>  postingsFrecuencias = new ArrayList<ParIdFrec>();

        //Primero obtenemos una lista con todos los valores distintos
        ArrayList<Integer> unicos = new ArrayList<Integer>(new LinkedHashSet<Integer>(repPostings));
        for(Integer docId : unicos)
        {
            int frecuencia = Collections.frequency(repPostings,docId);
            ParIdFrec par = new ParIdFrec(docId, frecuencia);
            postingsFrecuencias.add(par);
        }
        return postingsFrecuencias;
    }

    public static void crearIndiceFrecuencial()
    {
        for(Map.Entry<String, ArrayList<Integer>> entrada : indice.entrySet())
        {
            ArrayList<ParIdFrec> postingsFrec = new ArrayList<ParIdFrec>();
            String termino = entrada.getKey();
            ArrayList<Integer> valor = entrada.getValue();
            postingsFrec = obtenerFrecuencias(valor);
            indiceFrecuencial.put(termino, postingsFrec);
        }
    }

    public static String imprimirIndice()
    {
        StringBuilder sb = new StringBuilder();

        for(Map.Entry<String,ArrayList<Integer>> entry : indice.entrySet())
        {
            String key = entry.getKey();
            ArrayList<Integer> value = entry.getValue();

            int contador = 0;
            sb.append(key);
            sb.append(" ===> ");
            for(Integer i : value)
            {
                if (contador != 0)
                    sb.append(" -> ");
                sb.append("[");
                sb.append(i);
                sb.append("]");
                contador ++;
            }
            sb.append("\n");

        }
        return sb.toString();
    }

    public static String imprimirIndiceFrecuencial()
    {
        StringBuilder sb = new StringBuilder();

        for(Map.Entry<String,ArrayList<ParIdFrec>> entry : indiceFrecuencial.entrySet())
        {
            String key = entry.getKey();
            ArrayList<ParIdFrec> value = entry.getValue();

            int contador = 0;
            sb.append(key);
            sb.append(" ===> ");
            for(ParIdFrec i : value)
            {
                if (contador != 0)
                    sb.append(" -> ");
                sb.append("[");
                sb.append(i.getDocId());
                sb.append(", ");
                sb.append(i.getFrecuencia());
                sb.append("]");
                contador ++;
            }
            sb.append("\n");

        }
        return sb.toString();
    }


    public static TreeMap<String, ArrayList<ParIdFrec>> obtenerIndice()
    {
        return indiceFrecuencial;
    }

}
