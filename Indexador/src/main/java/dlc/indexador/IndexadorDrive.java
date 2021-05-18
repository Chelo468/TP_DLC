/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.indexador;

import dlc.indexador.drive.GoogleDriveUtils;
import com.google.api.services.drive.Drive;
import static dlc.indexador.drive.ReadFromDrive.obtenerArchivos;
import static dlc.indexador.drive.ReadFromDrive.descargarArchivo;
import static dlc.indexador.Indexador.generarAccesoBD;
import dlc.indexador.bd.AccesoBD;
import dlc.indexador.bd.DBDocumento;
import dlc.indexador.bd.DBPosteo;
import dlc.indexador.bd.DBVocabulario;
import dlc.indexador.configuracion.Configuracion;
import dlc.indexador.entidades.AccesoFile;
import dlc.indexador.entidades.Documento;
import dlc.indexador.entidades.Posteo;
import dlc.indexador.entidades.Vocabulario;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gabriel
 */
public class IndexadorDrive {
    
    private static int cantidadDocumentosIndexados;
    private static Map<String, Vocabulario> palabrasIndexadas;
    private static Map<String, Posteo> posteosIndexados;
    
    
    public static AccesoBD generarAccesoBD() throws ClassNotFoundException, SQLException{
        AccesoBD db = new AccesoBD();
        db.obtenerConexion();
        db.getStatement();
        return db;
    }   
    
    public static Map<String, Vocabulario> getPalabrasIndexadas()
    {
        return palabrasIndexadas;
    }
    
    public static Map<String, Posteo> getPosteosIndexadas()
    {
        return posteosIndexados;
    }
    
    public static void Indexar() throws FileNotFoundException, IOException, ClassNotFoundException, SQLException, Exception
    {
       String idDrive = "0B_R7SeoAotsmUUtYendIX04zRjA";

       AccesoBD db = generarAccesoBD();
       obtenerVocabularios(db);       
       Drive driveService = GoogleDriveUtils.getDriveService();
        if(palabrasIndexadas == null)
        {
            palabrasIndexadas = new HashMap<String, Vocabulario>();
        }
         if(posteosIndexados == null)
        {
            posteosIndexados = new HashMap<String, Posteo>();
        }
        if (Configuracion.DIRECTORIO_ORIGEN != null) {
            
            Map<String, String> archivosDrive = obtenerDrive(idDrive, driveService);
            Documento doc;
            DBDocumento.prepararDocumento(db);
            int i = 0;
            for (Map.Entry<String, String> entry : archivosDrive.entrySet()) {
                String link = entry.getValue();
                String nombreFichero = entry.getKey();
                doc = DBDocumento.loadDB(db, link);
                if(doc == null){
                String pathArchivo = Configuracion.DIRECTORIO_ORIGEN + nombreFichero;
                descargarArchivo(pathArchivo, link,driveService);
                doc = new Documento(nombreFichero, pathArchivo);
                int cantidadPalabras = indexarNuevoDocumento(pathArchivo);
                doc.setCantidadPalabras(cantidadPalabras);
                if(i % 50 == 0)
                  {
                      System.out.println("Documentos procesados: " + i);    
                  }
                     DBDocumento.agregarDocumentoPreparado(db, doc);
                     i++; 
                  }            
            }
        }
         System.out.println(palabrasIndexadas.size());
         int vocabulariosEnBD = DBVocabulario.contarVocabularios(db);
         if (vocabulariosEnBD == 0){
             InsertarPalabrasBDInicial(db);
         }
         else{
             ActualizarPalabrasBD(db);
         }
          System.out.println(posteosIndexados.size());
          int posteosEnBD = DBPosteo.contarPosteos(db);
          if (posteosEnBD == 0){
             InsertarPosteosBDInicial(db);
         }
         else{
             ActualizarPosteosBD(db);
         }
    }
        
    
    private static int indexarNuevoDocumento(String name) throws IOException {
        int cantidadPalabras = 0;
        if (name != null) {
            
            Path fileToIndex = Paths.get(name);
            
            //El siguiente método permite obtener la ruta completa del archivo
            //String toString = fileToIndex.toString();
            
            //El siguiente método permite obtener el nombre del archivo sin la ruta
            //String fileName = fileToIndex.getFileName().toString();
            
            

            cantidadPalabras = indexarPalabras(fileToIndex);
            
        }
        return cantidadPalabras;
    }
    
    private static int indexarPalabras(Path archivoProcesar) throws IOException {

        AccesoFile File = new AccesoFile();

        BufferedReader reader = File.leerArchivo(archivoProcesar.toString());
        String nombreDocumento = archivoProcesar.getFileName().toString();
        int cantidadPalabras = 0;
        
        //Si el documento se leyo aumentamos el contador
        cantidadDocumentosIndexados++;

        //inputLine es la linea ha ser leida 
        String inputLine = null;

        while ((inputLine = reader.readLine()) != null) {
            //Expresión regular para parsear la linea  
            inputLine = inputLine.replaceAll("[^a-zA-ZÁÉÍÓÚáéíóúÑñÜü]", " ").toLowerCase();
            
            //Separamos todas las palabras de la linea
            String[] words = inputLine.split("\\s+");

            // Ignoramos si quedan espacios entre filas .
            if (inputLine.equals("")) {
                continue;
            }

            //Recorremos palabra por palabra 
            for (String word : words) {

                //Ignoramos texto vacio despues del  parse
                if (word.equals("")) {
                    continue;
                }

                //Traemos la palabra del indexador
                Vocabulario palabra = palabrasIndexadas.get(word);
                cantidadPalabras++;
                if (palabra == null) {

                    //No está agregada en el vocabulario que estamos indexando
                    palabra = new Vocabulario(word);

                }
               
                palabra.agregarCantidadEnDocumento(nombreDocumento);
                
                palabrasIndexadas.put(word, palabra);
                
                String key = word + nombreDocumento;
                Posteo posteo = posteosIndexados.get(key);
                if (posteo == null) {

                    posteo = new Posteo(word, nombreDocumento);

            }
                    posteo.sumarRepeticion();
                    posteosIndexados.put(key, posteo);
        }

    }
    return cantidadPalabras;
}
    public static Map<String, String> obtenerDrive(String idDrive, Drive driveService) throws IOException {
        Map<String, String> archivosDrive = obtenerArchivos(idDrive, driveService);
                  return archivosDrive;
    }
    
    private static void obtenerVocabularios(AccesoBD db) throws Exception {
        ArrayList<Vocabulario> vocabularios = DBVocabulario.loadAllDB(db);
        if (vocabularios != null){
            if(palabrasIndexadas == null)
        {
            palabrasIndexadas = new HashMap<String, Vocabulario>();
            }
            for (int i = 0; i < vocabularios.size(); i++) {
                Vocabulario vocabulario = vocabularios.get(i);
                String palabra = vocabulario.getPalabra();
                vocabulario.setActualizado();
                palabrasIndexadas.put(palabra, vocabulario);
            }
        }
       
    }

    private static void InsertarPalabrasBDInicial(AccesoBD db) throws SQLException {
        DBVocabulario.prepararVocabulario(db);  
        palabrasIndexadas.forEach((key, value) -> {   
                try {
                    DBVocabulario.agregarVocabularioPreparado(db, value);
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
    }

    private static void ActualizarPalabrasBD(AccesoBD db) {
       palabrasIndexadas.forEach((key, value) -> {
            Vocabulario voc;    
                try {
                    voc = DBVocabulario.loadDB(db, value.getPalabra());
                    if (voc == null){
                        DBVocabulario.prepararVocabulario(db);
                        DBVocabulario.agregarVocabularioPreparado(db, value);
                    }    
                    else{
                        if (value.getActualizado() == false){
                            DBVocabulario.actualizarVocabulario(db, value);
                            System.out.println("actualizado");
                        }
                       
                    }
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
    }

    private static void InsertarPosteosBDInicial(AccesoBD db) throws Exception {
        DBPosteo.prepararPosteo(db);
            posteosIndexados.forEach((key, value) -> {
                try {
                    DBPosteo.agregarPosteoPreparado(db, value);
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
    }

    private static void ActualizarPosteosBD(AccesoBD db) {
         posteosIndexados.forEach((key, value) -> {
             Posteo posteo;
                try {
                    posteo = DBPosteo.loadDB(db, value.getIdPalabra(), value.getIdDocumento());
                    if (posteo == null){
                         DBPosteo.prepararPosteo(db);
                         DBPosteo.agregarPosteoPreparado(db, value);
                    }
                    else{
                        DBPosteo.actualizarPosteo(db, value);
                            System.out.println("actualizado");
                    }
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
    }
}