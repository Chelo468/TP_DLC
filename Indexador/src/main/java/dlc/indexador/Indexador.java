/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.indexador;

import dlc.indexador.configuracion.Configuracion;
import dlc.indexador.entidades.AccesoFile;
import dlc.indexador.entidades.Vocabulario;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Chelo
 */
public class Indexador {
    
    private static int cantidadDocumentosIndexados;
    private static Map<String, Vocabulario> palabrasIndexadas;
    
    public static Map<String, Vocabulario> getPalabrasIndexadas()
    {
        return palabrasIndexadas;
    }
    
    public static void Indexar() throws FileNotFoundException, IOException
    {
        if(palabrasIndexadas == null)
        {
            palabrasIndexadas = new HashMap<String, Vocabulario>();
        }
        if (Configuracion.DIRECTORIO_ORIGEN != null) {
            InputStream is = null;
            OutputStream os = null;
            
            //Agregado
            File dir = new File(Configuracion.DIRECTORIO_ORIGEN);
            String[] ficheros = dir.list();
            String nombreFichero;
            String pathArchivo;
            File source;
            File dest;
            for (int i = 0; i < ficheros.length; i++) {
                nombreFichero = ficheros[i];
                pathArchivo = Configuracion.DIRECTORIO_ORIGEN + nombreFichero;
                
                source = new File(pathArchivo);
                dest = new File(Configuracion.DIRECTORIO_DESTINO + source.getName());

                try {
                    is = new FileInputStream(source);
                    os = new FileOutputStream(dest);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                    }
                } finally {
                    is.close();
                    os.close();
                }

                indexarNuevoDocumento(dest.getAbsolutePath());
                
                if(i % 50 == 0)
                {
                    System.out.println("Documentos procesados: " + i);    
                }
                
            }
            
            
        }
    }
    
    private static void indexarNuevoDocumento(String name) throws IOException {
        if (name != null) {
            
            Path fileToIndex = Paths.get(name);
            
            //El siguiente método permite obtener la ruta completa del archivo
            //String toString = fileToIndex.toString();
            
            //El siguiente método permite obtener el nombre del archivo sin la ruta
            //String fileName = fileToIndex.getFileName().toString();
            
            

            indexarPalabras(fileToIndex);
        }
    }
    
    private static void indexarPalabras(Path archivoProcesar) throws IOException {

        AccesoFile File = new AccesoFile();

        BufferedReader reader = File.leerArchivo(archivoProcesar.toString());
        String nombreDocumento = archivoProcesar.getFileName().toString();

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
                
                if (palabra == null) {

                    //No está agregada en el vocabulario que estamos indexando
                    palabra = new Vocabulario(word);

                }                 
                
                palabra.agregarCantidadEnDocumento(nombreDocumento);
                
                palabrasIndexadas.put(word, palabra);

            }

        }

    }
    
}
