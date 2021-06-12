/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.indexador.bd;

import static dlc.indexador.drive.ReadFromDrive.descargarArchivo;
import dlc.indexador.configuracion.Configuracion;
import dlc.indexador.entidades.Vocabulario;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 *
 * @author Gabriel
 */
public class AccesoBD {
    public static final String SQLDRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String CONNECTIONURL = "jdbc:sqlserver://localhost;databaseName=DLCDB";
    public static final String USER = "sa";
    public static final String PASSWORD = "admin";
    Connection cn = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    
     public Connection getConnection(){
            return this.cn;
        }
    public Statement getStatement() throws SQLException{
        
            if(this.cn != null)
            {
                this.stmt = this.cn.createStatement();                
            }
        
            return this.stmt;
        }
    public static void crearDatos(AccesoBD db) throws ClassNotFoundException, SQLException{
           Connection con = db.getConnection();
        Statement st = con.createStatement();
        String sql = "CREATE TABLE DOCUMENTO " +
                   " (titulo VARCHAR(100)not NULL UNIQUE, " + 
                   " path VARCHAR(100), " + 
                   " cantidadPalabras INTEGER, " + 
                   " driveId VARCHAR(100), " + 
                   " PRIMARY KEY ( titulo ));" + 
                    "CREATE TABLE VOCABULARIO " +
                   " (palabra VARCHAR(100) not NULL UNIQUE, " + 
                   " cantidadDocumentos INTEGER, " +  
                   " maximaFrecuencia INTEGER, " +  
                   "PRIMARY KEY ( palabra ));" +
                   "CREATE TABLE POSTEO " +
               // "( idVocabulario VARCHAR(255) not NULL, " + 
                   "( idVocabulario VARCHAR(100) not NULL FOREIGN KEY REFERENCES VOCABULARIO (palabra), " + 
                   " idDocumento VARCHAR(100) not NULL FOREIGN KEY REFERENCES DOCUMENTO (titulo), " +  
                   " cantidadRepeticiones INTEGER, " +  
                   " PRIMARY KEY ( idVocabulario, idDocumento));";
         //       "CREATE INDEX indice on VOCABULARIO(palabra);";
        //FOREIGN KEY REFERENCES VOCABULARIO (palabra)
        st.executeUpdate(sql);
        }
    public static void borrarDatos(AccesoBD db) throws ClassNotFoundException, SQLException{
           Connection con = db.getConnection();
        Statement st = con.createStatement();
        String sql = "DROP TABLE POSTEO; " +
                     "DROP TABLE VOCABULARIO; " +
                     "DROP TABLE DOCUMENTO; ";
        st.executeUpdate(sql);
        }
     
     public static void agregarVocabularios(AccesoBD db, java.util.Map vocabularios) throws ClassNotFoundException, SQLException{
           Connection con = db.getConnection();
         Statement st = con.createStatement();
        int i = 1;
        for (Object key: vocabularios.keySet()){
            Vocabulario vocal = (Vocabulario) vocabularios.get(key);
            String palabra = (String) key;
            int cantidadDocumentos = vocal.getCantidadDocumentos();
            int maximaFrecuencia = vocal.getMaximaFrecuencia();
            String sql = "INSERT INTO VOCABULARIO " +
                    "VALUES("+ i + ", '" + palabra + "', '" + cantidadDocumentos + "', '" + maximaFrecuencia + "'); ";
        st.executeUpdate(sql);
        i++;    
        }
               
       
        }
     public ResultSet executeQuery(String query) throws Exception{
       return this.stmt.executeQuery(query);
    }  
     public boolean executeUpdate() throws Exception {
        if (this.pstmt == null) {
            throw new Exception("DBManager Error: se intenta ejecutar una instrucción NO preparada/precompilada.");
        }
        return this.pstmt.execute();
    }
    
    public void obtenerConexion() throws ClassNotFoundException, SQLException{
        Class.forName(SQLDRIVER);
        if (this.cn == null){
           this.cn = DriverManager.getConnection(CONNECTIONURL, USER, PASSWORD); 
        }
    
    }
    public void prepareStatement(String query) throws SQLException{
        this.pstmt = this.cn.prepareStatement(query);
    }
    public void setInt(int indice, Integer entero) throws Exception {
        this.pstmt.setInt(indice, entero);
    }
    public void setString(int indice, String string) throws Exception {
        this.pstmt.setString(indice, string);
    }

      
}
