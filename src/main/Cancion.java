package main;

//Clase usada para almacenar la información deseada del link de cada cancion, en la clase Scraper

public class Cancion
{
    private int id;
    private String titulo;
    private String artista;
    private String letra;

    public Cancion(int id, String titulo, String artista, String letra)
    {
        this.id = id;
        this.titulo = titulo;
        this.artista = artista;
        this.letra= letra;
    }

    public int getId()
    {
        return id;
    }

    public String getTitulo()
    {
        return titulo;
    }

    public String getArtista()
    {
        return artista;
    }

    public String getLetra()
    {
        return letra;
    }
}
