/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.indexador;

import dlc.indexador.entidades.Vocabulario;
import java.util.Calendar;
import java.util.Map;
import dlc.indexador.bd.AccesoBD;
import dlc.indexador.entidades.Posteo;

/**
 *
 * @author Chelo
 */
public class Main {
    
    public static void main(String[] args)
    {        
        try
        {
            long milisInicial = Calendar.getInstance().getTimeInMillis();
            
            AccesoBD db = new AccesoBD();

            //Inicializa el conector
            db.obtenerConexion();

            //Crea el objeto Statement en la conexion
            //db.getStatement();

          //  AccesoBD.borrarDatos(db);
           // AccesoBD.crearDatos(db);
            IndexadorDrive.Indexar("asd");
      //      IndexadorDrive.Indexar("0B_R7SeoAotsmUUtYendIX04zRjA");

            Map<String, Vocabulario> palabrasIndexadas = Indexador.getPalabrasIndexadas();
            System.out.println(palabrasIndexadas.size());
          
            Map<String, Posteo> posteosIndexados = Indexador.getPosteosIndexadas();
            System.out.println(posteosIndexados.size());

            
            
            long milisFinal = Calendar.getInstance().getTimeInMillis();
            long demoraProceso = milisFinal - milisInicial;
            
            System.out.println("Tiempo de proceso: " + (demoraProceso / 1000) + " segundos.");
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        
    }
    
}
