/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.indexador.bd;

import static dlc.indexador.bd.DBDocumento.buildDocumento;
import dlc.indexador.entidades.Documento;
import dlc.indexador.entidades.Posteo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Gabriel
 */




public interface DBPosteo {
    static final String IDPALABRA = "idVocabulario";
    static final String IDDOCUMENTO = "idDocumento";
    static final String CANTIDAD_REPETICIONES = "cantidadRepeticiones";

       public static Posteo buildPosteo(ResultSet rs) throws Exception {
        Posteo posteo = null;
        if (rs.next()) {
            posteo = new Posteo();
            posteo.setIdPalabra(rs.getString(IDPALABRA));
            posteo.setIdDocumento(rs.getString(IDDOCUMENTO));
            posteo.setCantidadRepeticiones(rs.getInt(CANTIDAD_REPETICIONES));
    }
            return posteo;
    }
       public static int contarPosteos(AccesoBD db) throws SQLException{
        int contarPosteos;
        
        String sql = "SELECT COUNT(*) FROM POSTEO";
 
        ResultSet rs = db.stmt.executeQuery(sql);
        if (rs.next()){
           contarPosteos = rs.getInt(1);
        }
        else{
            contarPosteos = 0;
        }
        return contarPosteos;
        }
        public static Posteo loadDB(AccesoBD db, String palabra, String documento) throws Exception {
        String query = "SELECT * FROM POSTEO p WHERE p.idVocabulario = '" + palabra + "' and p.idDocumento = '" + documento + "'";
        ResultSet rs = db.executeQuery(query);
        Posteo posteo = buildPosteo(rs);
        rs.close();
        return posteo;
    }
    public static ArrayList<Posteo> loadDB(AccesoBD db, String palabra) throws Exception {
        ArrayList<Posteo> posteos = new ArrayList<>();
        String query = "SELECT * FROM POSTEO p WHERE p.idVocabulario = '" + palabra + "'";
       // db.prepareStatement(query);
        ResultSet rs = db.executeQuery(query);
        Posteo posteo;
        int i = 0;
        do{
          posteo = buildPosteo(rs);
          if (posteo != null){
               posteos.add(posteo);
          }
          i++;
        }
        while(posteo != null);
        rs.close();
        return posteos;
    }
      public static ArrayList<Posteo> loadDB(AccesoBD db, String[] palabras) throws Exception{
        ArrayList<Posteo> posteos = new ArrayList<>();
        String busqueda = "";
        for (int i = 0; i < palabras.length; i++) {
            busqueda = busqueda + "'" + palabras[i] + "', ";
        }
        busqueda = busqueda.substring(0, busqueda.length() - 2);
      //  String query = "SELECT * FROM VOCABULARIO v WHERE v.palabra IN " + palabras;
        String query = "SELECT * FROM POSTEO p WHERE p.idVocabulario IN (" + busqueda + ")";
        ResultSet rs = db.executeQuery(query);
        Posteo posteo;
        int i = 0;
        do{
          posteo = buildPosteo(rs);
          if (posteo != null){
               posteos.add(posteo);
          }
          i++;
        }
        while(posteo != null);
        rs.close();
        return posteos;
      }
    public static ArrayList<Posteo> loadDB(AccesoBD db, String[] idDocumentos, String[] idPalabras) throws Exception {
        if (idDocumentos.length != idPalabras.length){
            throw new Exception("Error, los arrays de idDocumentos y de idPalabras deben tener el mismo largo");
        }
        ArrayList<Posteo> posteos = new ArrayList<>();
        String busqueda = "";
        for (int i = 0; i < idDocumentos.length; i++) {
            busqueda = busqueda + "'" + idDocumentos[i] + idPalabras[i] + "', ";
        }
        busqueda = busqueda.substring(0, busqueda.length() - 2);
        String query = "SELECT * FROM POSTEO p WHERE concat(p.idDocumento, p.idVocabulario) IN (" + busqueda + ")";
        ResultSet rs = db.executeQuery(query);
        Posteo posteo;
        int i = 0;
        do{
          posteo = buildPosteo(rs);
          if (posteo != null){
               posteos.add(posteo);
          }
          i++;
        }
        while(posteo != null);
        rs.close();
        return posteos;
    }
    public static void agregarPosteo(AccesoBD db, Posteo posteo) throws Exception{
          String idPalabra = posteo.getIdPalabra(); 
          String idDocumento = posteo.getIdDocumento();
          Integer cantidadRepeticiones = posteo.getCantidadRepeticiones();
          String sql = "INSERT INTO POSTEO VALUES(?,?,?)";
          db.prepareStatement(sql);
          db.setString(1, idPalabra);
          db.setString(2, idDocumento);
          db.setInt(3, cantidadRepeticiones);
       db.executeUpdate();
    }
   
public static void prepararPosteo(AccesoBD db) throws Exception{
          String sql = "INSERT INTO POSTEO VALUES(?,?,?)";
          db.prepareStatement(sql);
    }
  public static void agregarPosteoPreparado(AccesoBD db, Posteo posteo) throws Exception{
          db.setString(1, posteo.getIdPalabra());
          db.setString(2, posteo.getIdDocumento());
          db.setInt(3, posteo.getCantidadRepeticiones());
       db.executeUpdate();
    } 

    public static void actualizarPosteo(AccesoBD db, Posteo posteo) throws SQLException, Exception{
     String palabra = posteo.getIdPalabra();
     String documento = posteo.getIdDocumento();
     int cantidadRepeticiones = posteo.getCantidadRepeticiones();
        String sql = "UPDATE POSTEO SET cantidadRepeticiones=? "
                + "WHERE palabra=? and documento=?";
        db.prepareStatement(sql);
        db.setInt(1, cantidadRepeticiones);
        db.setString(2, palabra);
        db.setString(3, documento);
        db.executeUpdate();
   
        }
}
    

