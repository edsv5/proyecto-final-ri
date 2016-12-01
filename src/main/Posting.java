package main;

import java.util.ArrayList;

public class Posting
{
    private int idDocumento;
    private int frecuencia;
    private ArrayList<Integer> listaPosicionesTermino;

    public Posting(int idDocumento, int frecuencia)
    {
        this.idDocumento = idDocumento;
        this.frecuencia  = frecuencia;
        listaPosicionesTermino = new ArrayList<Integer>();
    }
}
