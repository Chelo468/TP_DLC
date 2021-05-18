/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.indexador.bd;

import com.google.api.client.util.DateTime;
import static dlc.indexador.bd.DBVocabulario.CANTIDAD_DOCUMENTOS;
import static dlc.indexador.bd.DBVocabulario.MAXIMA_FRECUENCIA;
import static dlc.indexador.bd.DBVocabulario.PALABRA;
import static dlc.indexador.bd.DBVocabulario.buildVocabulario;
import dlc.indexador.entidades.Documento;
import dlc.indexador.entidades.Vocabulario;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author Gabriel
 */
public abstract class DBDocumento {
    private static final String TITULO = "titulo";
    private static final String URL = "url";
    private static final String CANTIDAD_PALABRAS = "cantidadPalabras";
    private static final String ULTIMA_ACTUALIZACION = "ultimaActualizacion";
 
//    public static ResultSet consultarDocumento(AccesoBD db) throws SQLException{
//        String sql = "SELECT * FROM DOCUMENTO;";
//        ResultSet rs = db.executeQuery(sql);
//        return rs;
//    }
//    public static ResultSet consultarDocumento(AccesoBD db, String titulo) throws SQLException{
//        String sql = "SELECT * FROM DOCUMENTO WHERE titulo = '" + titulo + "';";
//        ResultSet rs = db.executeQuery(sql);
//        return rs;
//    }
//    
    public static Documento buildDocumento(ResultSet rs) throws Exception {
        Documento documento = null;
        if (rs.next()) {
            documento = new Documento();
            documento.setTitulo(rs.getString(TITULO));
            documento.setUrl(rs.getString(URL));
            documento.setCantidadPalabras(rs.getInt(CANTIDAD_PALABRAS));
            documento.setFechaActualizacion(rs.getTimestamp(ULTIMA_ACTUALIZACION));
            
    }
            return documento;
    }      
    public static int contarDocumentos(AccesoBD db) throws SQLException{
        int cantidadDocumentos;
        
        String sql = "SELECT COUNT(*) FROM DOCUMENTO";
 
        ResultSet rs = db.stmt.executeQuery(sql);
        if (rs.next()){
           cantidadDocumentos = rs.getInt(1);
        }
        else{
            cantidadDocumentos = 0;
        }
        return cantidadDocumentos;
    }
    public static Documento loadDB(AccesoBD db, String palabra) throws Exception {
        String query = "SELECT * FROM DOCUMENTO d WHERE d.titulo = '" + palabra + "'";
       // db.prepareStatement(query);
        ResultSet rs = db.executeQuery(query);
        Documento documento = buildDocumento(rs);
        rs.close();
        return documento;
    }
    public static Documento loadDB(AccesoBD db, String link, boolean bool) throws Exception {
        String query = "SELECT * FROM DOCUMENTO d WHERE d.url = '" + link + "'";
       // db.prepareStatement(query);
        ResultSet rs = db.executeQuery(query);
        Documento documento = buildDocumento(rs);
        rs.close();
        return documento;
    }
    public static ArrayList<Documento> loadDB(AccesoBD db, String[] palabras) throws Exception {
        ArrayList<Documento> documentos = new ArrayList<>();
        String busqueda = "";
        for (int i = 0; i < palabras.length; i++) {
            busqueda = busqueda + "'" + palabras[i] + "', ";
        }
        busqueda = busqueda.substring(0, busqueda.length() - 2);
      //  String query = "SELECT * FROM VOCABULARIO v WHERE v.palabra IN " + palabras;
        String query = "SELECT * FROM DOCUMENTO d WHERE d.titulo IN (" + busqueda + ")";
        ResultSet rs = db.executeQuery(query);
        Documento documento;
        int i = 0;
        do{
          documento = buildDocumento(rs);
          if (documento != null){
               documentos.add(documento);
          }
          i++;
        }
        while(documento != null);
        rs.close();
        return documentos;
    }
    
    public static Documento cargarDocumento(ResultSet rs) throws SQLException {
        Documento documento = null;
        if (rs.next()) {
            documento = new Documento();
            documento.setTitulo(rs.getString(TITULO));
            documento.setUrl(rs.getString(URL));
        }
        return documento;
    }
    public static void agregarDocumento(AccesoBD db, Documento doc) throws ClassNotFoundException, SQLException, Exception{
        String sql = "INSERT INTO DOCUMENTO VALUES(?,?,?,?)";
               //      "VALUES('"+ path + "', '" + nombre + "'); ";
        db.prepareStatement(sql);
        db.setString(1, doc.getUrl());
        db.setString(2, doc.getTitulo());
        db.setInt(3, doc.getCantidadPalabras());
        db.setString(4, doc.getFechaACtualizacion().toString());
        db.executeUpdate();
        }
    public static void prepararDocumento(AccesoBD db) throws SQLException{
        String sql = "INSERT INTO DOCUMENTO VALUES(?,?,?,?)";
        db.prepareStatement(sql);
    }
    public static void agregarDocumentoPreparado(AccesoBD db, Documento doc) throws SQLException, Exception{
        db.setString(1, doc.getUrl());
        db.setString(2, doc.getTitulo());
        db.setInt(3, doc.getCantidadPalabras());
        db.setString(4, doc.getFechaACtualizacion().toString());
        db.executeUpdate();
    }
    
}
