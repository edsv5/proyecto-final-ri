package main;

/**
 * Id de documento
 * Frecuencia
 * Posicion
 */
public class ContenedorTerminoIdPosicion implements Comparable<ContenedorTerminoIdPosicion>
{
    String termino;
    int idDocumento;
    int posicion;

    public ContenedorTerminoIdPosicion(String termino, int idDocumento, int posicion)
    {
        this.idDocumento = idDocumento;
        this.termino  = termino;
        this.posicion    = posicion;
    }

    public int getIdDocumento()
    {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento)
    {
        this.idDocumento = idDocumento;
    }

    public String getTermino()
    {
        return termino;
    }

    public void setTermino(String termino)
    {
        this.termino = termino;
    }

    public int getPosicion()
    {
        return posicion;
    }

    public void setPosicion(int posicion)
    {
        this.posicion = posicion;
    }

    @Override
    public int compareTo(ContenedorTerminoIdPosicion par1)
    {
        return this.termino.compareTo(par1.getTermino());
    }
}
