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
import java.io.FileNotFoundException;
import java.io.IOException;
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
    private static AccesoBD db;
    private static Map<String, Posteo> posteosIndexados;
    private static long CANTIDAD_POSTEOS_MEMORIA = 200000;
    private static int contadorActualizaciones = 0;
    private static int contadorDocumentos = 0;
    private static int cantidadPalabras = 0;
    
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
    
    public static boolean Indexar(String idDrive) throws FileNotFoundException, IOException, ClassNotFoundException, SQLException, Exception
    {
      // String idDrive = "0B_R7SeoAotsmUUtYendIX04zRjA";
       System.out.println("Comienzo de indexado...");
       db = generarAccesoBD();
       obtenerVocabularios(db);       
       Drive driveService = GoogleDriveUtils.getDriveService();
       
        if(posteosIndexados == null)
        {
            posteosIndexados = new HashMap<String, Posteo>();
        }
         try{
        Map<String, Posteo> posteosAInsertar = new HashMap<>();
        
        long contPosteos = 0;
        
        if (Configuracion.DIRECTORIO_ORIGEN != null) {
            
            Map<String, String> archivosDrive = obtenerDrive(idDrive, driveService);
            Documento doc;
            String pathArchivo;
            DBDocumento.prepararDocumento(db);
            
            int i = 0;
            for (Map.Entry<String, String> entry : archivosDrive.entrySet()) {
                String link = entry.getValue();
                String nombreFichero = entry.getKey();
                doc = DBDocumento.loadDB(db, nombreFichero);
                if(doc == null){
                     contadorDocumentos++;
                pathArchivo = Configuracion.DIRECTORIO_ORIGEN + nombreFichero;
                descargarArchivo(pathArchivo, link,driveService);
                doc = new Documento(nombreFichero, pathArchivo, link);
                Map<String, Posteo> posteosDocumento = indexarNuevoDocumento(pathArchivo);
                    
                    if(posteosDocumento != null)
                    {
                        doc.setCantidadPalabras(cantidadPalabras);
                        cantidadPalabras = 0;
                                        
                        if(contadorDocumentos == 1 || (contadorDocumentos) % 10 == 0)
                        {
                            System.out.println("Documentos procesados: " + (contadorDocumentos));    
                        }

                        DBDocumento.agregarDocumento(db, doc);

                        posteosAInsertar.putAll(posteosDocumento);
                        
                        if(posteosAInsertar.size() > CANTIDAD_POSTEOS_MEMORIA)
                        {
                            contPosteos += posteosAInsertar.size();
                            
                            System.out.println("Cargando " + posteosAInsertar.size() + " posteos para llegar a " + contPosteos);
                            
                            insertarPosteos(posteosAInsertar);
                            
                            System.out.println("Posteos insertados exitosamente...");
                            
                            posteosAInsertar.clear();
                            
                        }
                    }            
            }
            }
            
       if(contadorDocumentos > 0)
            {
                System.out.println("Actualizando cantidades de documentos en vocabulario...");
                ActualizarPalabrasBD(db);
                
                System.out.println("Vocabulario actualizado...");
            }
            
            if(posteosAInsertar.size() > 0)
            {
                contPosteos += posteosAInsertar.size();
                System.out.println("Cargando " + posteosAInsertar.size() + " posteos para llegar a " + contPosteos);
                insertarPosteos(posteosAInsertar);
                
                System.out.println("Posteos insertados exitosamente...");
                
                posteosAInsertar.clear();
            }
            
            System.out.println("Documentos indexados: " + contadorDocumentos);
        }
        
         System.out.println("Palabras Indexadas: " + palabrasIndexadas.size());
         System.out.println("Posteos indexados: " + contPosteos);
         return true; 
         }
          catch (Exception ex){
                 Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                 return false;
         }
         
    }
        
    
     private static Map<String, Posteo> indexarNuevoDocumento(String name) throws IOException, SQLException, Exception {
        //int cantidadPalabras = 0;
        if (name != null) {
            
            Path fileToIndex = Paths.get(name);
                      
            return indexarPalabras(fileToIndex);
            
        }
        
        return null;
    }
    
    private static Map<String, Posteo> indexarPalabras(Path archivoProcesar) throws IOException, SQLException, Exception {

        Map<String, Vocabulario> palabrasNuevas = new HashMap<>();
        Map<String, Posteo> posteosNuevos = new HashMap<>();
        
        AccesoFile File = new AccesoFile();

        BufferedReader reader = File.leerArchivo(archivoProcesar.toString());
        String nombreDocumento = archivoProcesar.getFileName().toString();
        
        //Si el documento se leyo aumentamos el contador
        cantidadDocumentosIndexados++;

        //inputLine es la linea ha ser leida 
        String inputLine = null;
        
        boolean agregarPalabraNueva = false;

        while ((inputLine = reader.readLine()) != null) {
            //Expresión regular para parsear la linea  
            //inputLine = inputLine.replaceAll("[^a-zA-ZÁÉÍÓÚáéíóúÑñÜü]", " ").toLowerCase();
            inputLine = filtrarPalabras(inputLine);
            
            //Separamos todas las palabras de la linea
            String[] words = inputLine.split("\\s+");

            // Ignoramos si quedan espacios entre filas .
            if (inputLine.equals("")) {
                continue;
            }

            //Recorremos palabra por palabra 
            for (String word : words) {

                agregarPalabraNueva = false;
                
                //Ignoramos texto vacio despues del  parse
                if (esPalabraAIgnorar(word)) {
                    continue;
                }

                //Traemos la palabra del indexador
                Vocabulario palabra = palabrasIndexadas.get(word);
                
                cantidadPalabras++;
                
                if (palabra == null) {

                    //No está agregada en el vocabulario que estamos indexando
                    palabra = new Vocabulario(word);
                    
                    agregarPalabraNueva = true;

                }
               
                palabra.agregarCantidadEnDocumento(nombreDocumento);
                
                palabrasIndexadas.put(word, palabra);
                
                if(agregarPalabraNueva)
                {
                    palabrasNuevas.put(word, palabra);
                }
                
                String key = nombreDocumento + word;
                Posteo posteo = posteosNuevos.get(key);
                if (posteo == null) {

                    posteo = new Posteo(word, nombreDocumento);

                }
                
                posteo.sumarRepeticion();
                posteosNuevos.put(key, posteo);
        }           
    }
        
    //System.out.println("Palabras a agregar: " + palabrasNuevas.size() + " ; Archivo: " + archivoProcesar.getFileName().toString());
    
    boolean statementPreparado = false;
    
    for (Map.Entry<String, Vocabulario> entry : palabrasNuevas.entrySet()) {
        
        if(!statementPreparado)
        {
            DBVocabulario.prepararVocabulario(db);
            
            statementPreparado = true;
        }
        
        Vocabulario vocabulario = entry.getValue();
        DBVocabulario.agregarVocabularioPreparado(db, vocabulario);
        vocabulario.setActualizado();
        
          
    };//Fin foreach
    
    return posteosNuevos;
}
    public static Map<String, String> obtenerDrive(String idDrive, Drive driveService) throws IOException {
        Map<String, String> archivosDrive = obtenerArchivos(idDrive, driveService);
                  return archivosDrive;
    }
    
    private static void obtenerVocabularios(AccesoBD db) throws Exception {
        ArrayList<Vocabulario> vocabularios = DBVocabulario.loadAllDB(db);
        
        palabrasIndexadas = new HashMap<String, Vocabulario>();
        if (vocabularios != null){
         
            
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

   private static void ActualizarPalabrasBD(AccesoBD db) throws Exception {
        
        boolean statementPreparado = false;
        
        for (Map.Entry<String, Vocabulario> entry : palabrasIndexadas.entrySet()) {
            
            if (entry.getValue().getActualizado() == false){
                    if(!statementPreparado)
                    {
                        DBVocabulario.prepararActualizarVocabulario(db);
                        statementPreparado = true;
                    }
                     
                   if (entry.getValue().getActualizado() == false){
                        
                    
                    DBVocabulario.actualizarVocabulario(db, entry.getValue());
                    contadorActualizaciones++;
                    if (contadorActualizaciones % 25000 == 0){
                        System.out.println("Se han actualizado " + contadorActualizaciones + " vocabularios");
                    }
}}
            };
    }
    
    private static void ActualizarPalabrasBD(AccesoBD db, Map<String, Vocabulario> palabras) {
       palabras.forEach((key, value) -> {
            if (value.getActualizado() == false){
                Vocabulario voc;
                try {
                    voc = DBVocabulario.loadDB(db, value.getPalabra());
                    if (voc == null){
                        DBVocabulario.prepararVocabulario(db);
                        DBVocabulario.agregarVocabularioPreparado(db, value);
                    }    
                    else{
                            DBVocabulario.actualizarVocabulario(db, value);

                        }
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
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
     private static boolean esPalabraAIgnorar(String word){
        return word.equals("") || word.contains("@**@");
    }
    
    private static String filtrarPalabras(String inputLine)
    {        
        //Expresión regular para parsear la linea  
        inputLine = inputLine.replaceAll("[^a-zA-Z0-9ÁÉÍÓÚáéíóúÑñÜü'\\s]", "").toLowerCase();

        //folio/35 => folio@**@35        
        inputLine = inputLine.replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u").replace("ü", "u").toLowerCase();
        
        inputLine = inputLine.replace("'", "");
                

        return inputLine;
    }   

    private static void insertarPosteos(Map<String, Posteo> posteosAInsertar) throws Exception {
        
        boolean statementPreparado = false;
    
        for (Map.Entry<String, Posteo> entry : posteosAInsertar.entrySet()) {

            if(!statementPreparado)
            {
                DBPosteo.prepararPosteo(db);

                statementPreparado = true;
            }

            DBPosteo.agregarPosteoPreparado(db, entry.getValue());


        };//Fin foreach
}
}