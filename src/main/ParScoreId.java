package main;

/*
* Clase que representa los pares término docId
* */

import java.util.Comparator;

public class ParScoreId implements Comparable<ParScoreId>
{
    Double score;
    int docId;

    public ParScoreId(double score, int docId)
    {
        this.score  = score;
        this.docId  = docId;
    }

    public Double getScore() { return score; }
    public int getDocId()      { return docId;}
    public void aumentarScore(double aumento)
    {
        score += aumento;
    }

    @Override
    public int compareTo(ParScoreId par1)
    {
        return this.score.compareTo(par1.getScore());
    }


}

class compParScoreId implements Comparator<ParScoreId>
{
    @Override
    public int compare(ParScoreId p1, ParScoreId p2)
    {
        return p1.getScore().compareTo(p2.getScore());
    }
}

