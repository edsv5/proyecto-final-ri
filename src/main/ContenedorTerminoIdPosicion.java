package main;

/**
 * Id de documento
 * Frecuencia
 * Posicion
 */

//Clase utilizada como contenedor
//Guarda el id del documento al cual pertenece el término, su posición dentro del documento y el término en si.
//Ejemplo: [ "casa" ][ 1 ][ 17 ]
//Nótese que solo se cuenta con la posicion de la palabra en específico, no con la recopilación de todas las posiciones
//correspondiente a la repetición de la palabra en toda la canción

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
