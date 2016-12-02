package main;


import java.util.ArrayList;

public class ListaPostings
{
    ArrayList<Posting> lista;

    public ListaPostings(Posting posting)
    {
        lista = new ArrayList<Posting>();
        lista.add(posting);
    }

    public ListaPostings(ArrayList<Posting> lista)
    {
        this.lista = lista;
    }

    public int encontrarPosicionEnLista(Posting posting)
    {
        boolean esta = false;
        int posicion = 0;//valor default
        for(Posting post: lista)
        {
            if(posting.getIdDocumento() == post.getIdDocumento())
            {
                esta = true;
                break;
            }
            posicion ++;
        }
        if(!esta)
            posicion = -1;
        return posicion;
    }

    public Posting acumularValoresPostings(Posting postingEnLista, Posting postingNuevo)
    {
        ArrayList<Integer> nuevaPos = postingNuevo.getListaPosicionesTermino();
        postingEnLista.aumentarFrecuencia();
        postingEnLista.agregarPosicionALista(nuevaPos);
        return postingEnLista;
    }

    public void agregarListasPostings(ListaPostings listaPosting)
    {
//        System.out.println("Lista vieja: "+ this.imprimirListaPostings());
//        System.out.println("Lista nueva: "+ listaPosting.imprimirListaPostings());
        lista.addAll(listaPosting.getListaPostings());
//        System.out.println("Lista combinada: "+ this.imprimirListaPostings());


    }


    public void acumularPostings(int posicion, Posting postingNuevo)
    {
        Posting postingViejo = lista.get(posicion);
        postingViejo.combinarPosting(postingNuevo);
        System.out.println("Lista " + postingViejo.getListaPosicionesTermino());
        lista.set(posicion, postingViejo);
    }

    public ArrayList<Posting> getListaPostings()
    {
        return lista;
    }

    public String imprimirListaPostings()
    {
        StringBuilder impresion = new StringBuilder();
        boolean primer = true;
        for(Posting posting: lista)
        {
            if(!primer)
                impresion.append(" -> ");
            impresion.append("{ ");
            impresion.append(posting.getIdDocumento() + " ,");
            impresion.append(posting.getFrecuencia() + " ,");
            impresion.append("[");
            for(Integer pos: posting.getListaPosicionesTermino())
            {
                impresion.append(pos + " ");
            }
            impresion.append("]");
            impresion.append(" }");
        }

        return impresion.toString();
    }


}
