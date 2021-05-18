/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.buscador.api;

import dlc.buscador.busqueda.Buscador;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.MediaType;
import dlc.indexador.Indexador;
import dlc.indexador.IndexadorDrive;
import dlc.indexador.entidades.Resultado;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Gabriel
 */
@Path("/buscador")
@RequestScoped
public class BuscadorResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of BuscadorResource
     */
    public BuscadorResource() {
    }

    /**
     * Retrieves representation of an instance of dlc.buscador.BuscadorResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response error() throws SQLException, Exception {
        
        return Response.status(Response.Status.BAD_REQUEST).build();
      
    }
    @GET
    @Path("/{texto}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscar(@PathParam("texto") String texto) throws SQLException, Exception {
        //TODO return proper representation object
        ArrayList<Resultado> salida = Buscador.buscar(texto);
        if (salida != null){
              return Response.ok(salida).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
      
    }

    /**
     * PUT method for updating or creating an instance of BuscadorResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putJson(String content) throws ClassNotFoundException, SQLException, Exception {
        JSONObject obj;
         obj = new JSONObject(content);
         String link = (String) obj.get("link");
         try{
             if (link != null){
             IndexadorDrive.Indexar(link);
         }
         else{
             Indexador.Indexar();
         }
         }
         catch (Exception ex) {
             return Response.status(Response.Status.NOT_FOUND).build();
         }
         
        return Response.ok("Indexado OK").build();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postJson(String content) throws ClassNotFoundException, SQLException, Exception {
        JSONObject obj;
         obj = new JSONObject(content);
         String link = (String) obj.get("link");
         try{
             if (link != null){
             IndexadorDrive.Indexar(link);
         }
         else{
             Indexador.Indexar();
         }
         }
         catch (Exception ex) {
             return Response.status(Response.Status.NOT_FOUND).build();
         }
         
        return Response.ok("Indexado OK").build();
    }
}