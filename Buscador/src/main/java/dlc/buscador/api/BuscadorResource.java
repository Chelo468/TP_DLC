/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.buscador.api;

import dlc.buscador.busqueda.Buscador;
import dlc.buscador.entidades.Resultado;
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
import dlc.indexador.bd.AccesoBD;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.inject.Inject;
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
    
    //@Inject private AccesoBD db;

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
        
        try
        {
            ArrayList<Resultado> salida = Buscador.buscar(texto);

            if (salida != null){
                  return Response.ok(salida).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        catch(Exception ex)
        {
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            return Response.accepted(ex.toString()).build();
        }
        
      
    }

    /**
     * PUT method for updating or creating an instance of BuscadorResource
     * @param content representation for the resource
     */
    @PUT
  //  @Consumes(MediaType.APPLICATION_JSON)
    public Response putJson(String content) throws ClassNotFoundException, SQLException, Exception {
        try{
                JSONObject obj;
                obj = new JSONObject(content);
                String metodo = (String) obj.get("metodo");
                boolean resultado;
                switch(metodo){
                    case "local":
                        resultado = Indexador.Indexar();
                        if (resultado == true){
                            return Response.ok("Indexado manual OK").build();
                            }
                        else{
                            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                        }
                    case "drive":
                        String link = (String) obj.get("link");
                        resultado = IndexadorDrive.Indexar(link);
                        if (resultado == true){
                            return Response.ok("Indexado Drive OK").build();
                        }
                        else{
                            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                        }
                    default:
                        return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity("Error en el método pasado").build();
                }
        }
                catch (Exception ex) {
             return Response.status(Response.Status.BAD_REQUEST).build();
         }
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postJson(String content) throws ClassNotFoundException, SQLException, Exception {
         try{
                JSONObject obj;
                obj = new JSONObject(content);
                String metodo = (String) obj.get("metodo");
                boolean resultado;
                switch(metodo){
                    case "local":
                        resultado = Indexador.Indexar();
                        if (resultado == true){
                            return Response.ok("Indexado manual OK").build();
                            }
                        else{
                            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                        }
                    case "drive":
                        String link = (String) obj.get("link");
                        resultado = IndexadorDrive.Indexar(link);
                        if (resultado == true){
                            return Response.ok("Indexado Drive OK").build();
                        }
                        else{
                            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                        }
                    default:
                        return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity("Error en el método pasado").build();
                }
        }
                catch (Exception ex) {
             return Response.status(Response.Status.BAD_REQUEST).build();
         }
}
}
