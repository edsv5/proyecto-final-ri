package main;

public class PrepCon
{
    public static String normalizar(String consulta)
    {
        StringBuilder sb = new StringBuilder();

        consulta = consulta.replaceAll("[^A-Za-z0-9 ]", " ");
        consulta = consulta.replaceAll("[ ]+", " ");

        for(int i = 0; i < consulta.length(); i++)
        {
            int ascii = (int) consulta.charAt(i);

            if(ascii >= 65 && ascii <= 90 )
            {
                char c = (char) (ascii + 32);
                sb.append( c );
            }
            else
            {
                sb.append(consulta.charAt(i));
            }
        }
        return sb.toString();
    }
}
