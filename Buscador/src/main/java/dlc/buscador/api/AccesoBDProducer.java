/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.buscador.api;

import dlc.indexador.bd.AccesoBD;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Produces;

/**
 *
 * @author Chelo
 */
public class AccesoBDProducer {
    
    @Produces
    @RequestScoped
    public AccesoBD create(){
        
        AccesoBD db = new AccesoBD();
        
        try
        {            
            db.obtenerConexion();
            db.getStatement();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return null;
        }
        
        return db;
        
    }
    
//    public void destroy(@Disposes AccesoBD db)
//    {
//        //db.close();
//    }
    
}
