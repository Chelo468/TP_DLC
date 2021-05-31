/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.indexador.bd;

import dlc.indexador.entidades.Documento;
import dlc.indexador.entidades.Vocabulario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Gabriel
 */
public interface DBVocabulario {
    static final String PALABRA = "palabra";
    static final String CANTIDAD_DOCUMENTOS = "cantidadDocumentos";
    static final String MAXIMA_FRECUENCIA = "maximaFrecuencia";
    
    public static Vocabulario buildVocabulario(ResultSet rs) throws Exception {
        Vocabulario vocabulario = null;
        if (rs.next()) {
            vocabulario = new Vocabulario();
            vocabulario.setCantidadDocumentos(rs.getInt(CANTIDAD_DOCUMENTOS));
            vocabulario.setPalabra(rs.getString(PALABRA));
            vocabulario.setMaximaFrecuencia(rs.getInt(MAXIMA_FRECUENCIA));
    }
            return vocabulario;
    
}
    public static Vocabulario loadDB(AccesoBD db, String palabra) throws Exception {
        String query = "SELECT * FROM VOCABULARIO v WHERE v.palabra = '" + palabra + "'";
       // db.prepareStatement(query);
        ResultSet rs = db.executeQuery(query);
        Vocabulario vocabulario = buildVocabulario(rs);
        rs.close();
        return vocabulario;
    }
    public static int contarVocabularios(AccesoBD db) throws SQLException{
        int contarVocabularios;
        
        String sql = "SELECT COUNT(*) FROM VOCABULARIO";
 
        ResultSet rs = db.stmt.executeQuery(sql);
        if (rs.next()){
           contarVocabularios = rs.getInt(1);
        }
        else{
            contarVocabularios = 0;
        }
        return contarVocabularios;
        }
     public static ArrayList<Vocabulario> loadAllDB(AccesoBD db) throws Exception{
         String query = "SELECT * FROM VOCABULARIO";
         ArrayList<Vocabulario> vocabularios = new ArrayList<>();
         ResultSet rs = db.executeQuery(query);
         Vocabulario vocabulario;
        //int i = 0;
        do{
          vocabulario = buildVocabulario(rs);
          if (vocabulario != null){
               vocabularios.add(vocabulario);
          }
          //i++;
        }
        while(vocabulario != null);
        rs.close();
        return vocabularios;
    }
    public static ArrayList<Vocabulario> loadDB(AccesoBD db, String[] palabras) throws Exception {
        ArrayList<Vocabulario> vocabularios = new ArrayList<>();
        String busqueda = "";
        for (int i = 0; i < palabras.length; i++) {
            busqueda = busqueda + "'" + palabras[i] + "', ";
        }
        busqueda = busqueda.substring(0, busqueda.length() - 2);
      //  String query = "SELECT * FROM VOCABULARIO v WHERE v.palabra IN " + palabras;
        String query = "SELECT * FROM VOCABULARIO v WHERE v.palabra IN (" + busqueda + ")";
        ResultSet rs = db.executeQuery(query);
        Vocabulario vocabulario;
        int i = 0;
        do{
          vocabulario = buildVocabulario(rs);
          if (vocabulario != null){
               vocabularios.add(vocabulario);
          }
          i++;
        }
        while(vocabulario != null);
        rs.close();
        return vocabularios;
    }
    
    
    public static void agregarVocabulario(AccesoBD db, Vocabulario vocabulario) throws ClassNotFoundException, SQLException, Exception{
        String palabra = vocabulario.getPalabra();
        Integer cantidadDocumentos = vocabulario.getCantidadDocumentos();
        Integer maximaFrecuencia = vocabulario.getMaximaFrecuencia();
        String sql = "INSERT INTO VOCABULARIO VALUES(?,?,?)";
        db.prepareStatement(sql);
        db.setString(1, palabra);
        db.setInt(2, cantidadDocumentos);
        db.setInt(3, maximaFrecuencia);
        db.executeUpdate();
        }
    public static void prepararVocabulario(AccesoBD db) throws SQLException {
        String sql = "INSERT INTO VOCABULARIO VALUES(?,?,?)";
        db.prepareStatement(sql);
        }
    public static void agregarVocabularioPreparado(AccesoBD db, Vocabulario vocabulario) throws Exception{
        db.setString(1, vocabulario.getPalabra());
        db.setInt(2, vocabulario.getCantidadDocumentos());
        db.setInt(3, vocabulario.getMaximaFrecuencia());
        db.executeUpdate();
        }
public static void prepararVocabularioMerge(AccesoBD db) throws SQLException {
        String sql = "MERGE INTO VOCABULARIO WITH (HOLDLOCK) AS target"
                + " USING (SELECT ? AS word) AS source(word) "
                + "ON (target.palabra = source.word) "
                + "WHEN MATCHED THEN "
                + "UPDATE SET maximaFrecuencia = ? "
                + "WHEN NOT MATCHED THEN "
                + "INSERT VALUES(?,?,?);";
        db.prepareStatement(sql);
        }

    public static void agregarVocabularioPreparadoConUpdate(AccesoBD db, Vocabulario vocabulario) throws Exception{
        db.setString(1, vocabulario.getPalabra());
        db.setInt(3, vocabulario.getMaximaFrecuencia());
        db.setInt(2, vocabulario.getCantidadDocumentos());
        db.setInt(4, vocabulario.getMaximaFrecuencia());
        db.executeUpdate();
        }
    public static void mergeVocabulario(AccesoBD db, Vocabulario vocabulario) throws ClassNotFoundException, SQLException, Exception{
        String palabra = vocabulario.getPalabra();
        Integer cantidadDocumentos = vocabulario.getCantidadDocumentos();
        Integer maximaFrecuencia = vocabulario.getMaximaFrecuencia();
        String sql = "MERGE INTO VOCABULARIO WITH (HOLDLOCK) AS target"
                + " USING (SELECT ? AS word) AS source(word) "
                + "ON (target.palabra = source.word) "
                + "WHEN MATCHED THEN "
                + "UPDATE SET maximaFrecuencia = ? "
                + "WHEN NOT MATCHED THEN "
                + "INSERT VALUES(?,?,?);";
        db.prepareStatement(sql);
        db.setString(1, palabra);
        db.setInt(2, maximaFrecuencia);
        db.setString(3, palabra);
        db.setInt(4, cantidadDocumentos);
        db.setInt(5, maximaFrecuencia);
        db.executeUpdate();
        }
     public static void mergeVocabularioPreparado(AccesoBD db, Vocabulario vocabulario) throws ClassNotFoundException, SQLException, Exception{
        String palabra = vocabulario.getPalabra();
        Integer cantidadDocumentos = vocabulario.getCantidadDocumentos();
        Integer maximaFrecuencia = vocabulario.getMaximaFrecuencia();
        db.setString(1, palabra);
        db.setInt(2, maximaFrecuencia);
        db.setString(3, palabra);
        db.setInt(4, cantidadDocumentos);
        db.setInt(5, maximaFrecuencia);
        db.executeUpdate();
        }
     
     public static void prepararActualizarVocabulario(AccesoBD db) throws SQLException
     {
         String sql = "UPDATE VOCABULARIO SET cantidadDocumentos=?, maximaFrecuencia=? "
                + "WHERE palabra=?";
         
        db.prepareStatement(sql);
     }

    public static void actualizarVocabulario(AccesoBD db, Vocabulario vocabulario) throws SQLException, Exception{
    String palabra = vocabulario.getPalabra();
        Integer cantidadDocumentos = vocabulario.getCantidadDocumentos();
        Integer maximaFrecuencia = vocabulario.getMaximaFrecuencia();
        
        db.setString(3, palabra);
        db.setInt(1, cantidadDocumentos);
        db.setInt(2, maximaFrecuencia);
        db.executeUpdate();
        }
    
    public static void actualizarCantidadDeDocumentos(AccesoBD db) throws Exception
    {
        String sqlQuery = "update VOCABULARIO\n" +
                        "set VOCABULARIO.cantidadDocumentos = (SELECT COUNT(pos.idVocabulario)\n" +
                        "FROM Posteo pos\n" +
                        "where VOCABULARIO.palabra = pos.idVocabulario)\n" +
                        "from Posteo pos2\n" +
                        "where VOCABULARIO.palabra = pos2.idVocabulario";
        
        db.executeQuery(sqlQuery);
    }
}
