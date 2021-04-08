/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.indexador;

import dlc.indexador.entidades.Vocabulario;
import java.util.Calendar;
import java.util.Map;

/**
 *
 * @author Chelo
 */
public class Main {
    
    public static void main(String[] args)
    {
        System.out.println("Hola Mundo!!");
        
        try
        {
            long milisInicial = Calendar.getInstance().getTimeInMillis();
            
            Indexador.Indexar();    
            
            long milisFinal = Calendar.getInstance().getTimeInMillis();
            
            Map<String, Vocabulario> palabrasIndexadas = Indexador.getPalabrasIndexadas();
            
            long demoraProceso = milisFinal - milisInicial;
            
            System.out.println("Cantidad de palabras indexadas: " + palabrasIndexadas.size());
            System.out.println("Tiempo de proceso: " + (demoraProceso / 1000) + " segundos.");
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        
    }
    
}
