package main;

/*
* Clase que representa los pares término docId
* */

public class ParTermId implements Comparable<ParTermId>
{
    String termino;
    int docId;

    public ParTermId(String termino, int docId)
    {
        this.termino = termino;
        this.docId   = docId;
    }

    public String getTermino() { return termino; }
    public int getDocId()      { return docId;}

    @Override
    public int compareTo(ParTermId par1)
    {
        return this.termino.compareTo(par1.getTermino());
    }


}


