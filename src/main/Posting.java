package main;

import java.util.ArrayList;

public class Posting
{
    private int idDocumento;
    private int frecuencia;
    private ArrayList<Integer> listaPosicionesTermino;



    public Posting(int idDocumento, int posicion)
    {
        this.idDocumento = idDocumento;
        this.frecuencia  = 1;
        listaPosicionesTermino = new ArrayList<Integer>();
        listaPosicionesTermino.add(posicion);

    }

    public Posting(ContenedorTerminoIdPosicion contenedor)
    {
        this.idDocumento = contenedor.getIdDocumento();
        this.frecuencia = 1;
        listaPosicionesTermino = new ArrayList<Integer>();
        listaPosicionesTermino.add(contenedor.getPosicion());
    }

    public void agregarPosicionALista(ArrayList<Integer> nuevaPosicion)
    {
        listaPosicionesTermino.addAll(nuevaPosicion);
    }

    public void aumentarFrecuencia()
    {
        this.frecuencia ++;
    }

    public int getIdDocumento()
    {
        return idDocumento;
    }

    public ArrayList<Integer> getListaPosicionesTermino()
    {
        return listaPosicionesTermino;
    }

    public int getFrecuencia()
    {
        return frecuencia;
    }

    public void combinarPosting(Posting nuevoPosting)
    {
        this.frecuencia += nuevoPosting.getFrecuencia();
        this.listaPosicionesTermino.addAll(nuevoPosting.getListaPosicionesTermino());
    }
}
