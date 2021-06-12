/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.buscador.api;

import dlc.indexador.bd.AccesoBD;
import dlc.indexador.bd.DBDocumento;
import dlc.indexador.entidades.Documento;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Chelo
 */
@Path("/documento")
@RequestScoped
public class DocumentoResource {
    
    @Context
    private UriInfo context;
    
    @GET
    @Path("/{documento}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDocumento(@PathParam("documento") String documento) throws SQLException, Exception {
        
        try
        {
            AccesoBD db = new AccesoBD();
            
            db.obtenerConexion();
            db.getStatement();
        
            Documento doc = DBDocumento.loadDB(db, documento);

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                                    new FileInputStream(doc.getUrl()), "windows-1252"));

            String inputLine = null;
            StringBuilder contenidoDocumento = new StringBuilder();


            while ((inputLine = reader.readLine()) != null) {
                contenidoDocumento.append(inputLine + "\n");
            }

            return Response.ok(contenidoDocumento.toString()).build();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            
            return Response.accepted(ex).build();
        }
        
    }
}
