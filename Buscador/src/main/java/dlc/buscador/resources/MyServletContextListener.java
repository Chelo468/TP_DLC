/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.buscador.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import javax.servlet.*;
/**
 *
 * @author Gabriel
 */


public class MyServletContextListener implements ServletContextListener {
private static HashSet<String> stopwords = new HashSet(); 
private static final String DIRECTORIO_STOPWORDS = "C:\\StopWords\\";
 public void contextInitialized(ServletContextEvent e) {
   try{
       File dir = new File(DIRECTORIO_STOPWORDS);
            
            String[] ficheros = dir.list();
         for (int i = 0; i < ficheros.length; i++) {
        String pathArchivo = DIRECTORIO_STOPWORDS + ficheros[i];
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                                    new FileInputStream(pathArchivo), "windows-1252"));

            String inputLine = null;

            while ((inputLine = reader.readLine()) != null) {
                stopwords.add(inputLine);
            }
            }
   }
   catch(Exception ex){
       
   }
     
 }
 
 public static HashSet getStopwords(){
     return stopwords;
 }

 public void contextDestroyed(ServletContextEvent e) {

 }
}
