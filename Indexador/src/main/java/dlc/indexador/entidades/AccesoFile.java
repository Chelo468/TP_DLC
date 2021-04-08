/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.indexador.entidades;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 *
 * @author Chelo
 */
public class AccesoFile {
    private BufferedReader  reader = null;
    
    //Lee Archivo y devuelve el BufferedReader
    public BufferedReader leerArchivo(String file) throws IOException{
           
     //Encoding windows-1252 para que lea alfabeto español
           return  reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), "windows-1252"));
       
    }
    
    public void cerrarConexion() throws IOException{
                
            reader.close();
          
    }
    
    public int lineasDeTexto(String file){
        int cnt = 0;
        LineNumberReader reader  = null;
        try {
            reader = new LineNumberReader(new FileReader(file));
            String lineRead = "";
            while ((lineRead = reader.readLine()) != null) {}
            cnt = reader.getLineNumber();
            reader.close();
            System.out.print(+cnt);
        } catch (FileNotFoundException ex) {
            System.out.println("Error al contar lineas de texto");
        } catch (IOException ex) {
            System.out.println("Error al contar lineas de texto");
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
               System.out.println("Error al contar lineas de texto");
            }
        }
    
    
    
    return cnt;
    
    }
}
