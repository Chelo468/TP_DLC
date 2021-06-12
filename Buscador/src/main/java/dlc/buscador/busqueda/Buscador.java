/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.buscador.busqueda;

import dlc.buscador.entidades.Resultado;
import dlc.buscador.resources.MyServletContextListener;
import dlc.indexador.bd.AccesoBD;
import dlc.indexador.bd.DBDocumento;
import dlc.indexador.bd.DBPosteo;
import dlc.indexador.bd.DBVocabulario;
import dlc.indexador.entidades.Documento;
import dlc.indexador.entidades.Posteo;
import dlc.indexador.entidades.Vocabulario;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Gabriel
 */
public class Buscador {
    
    private static Map<String, Float> pesosBusqueda;
    private static Map<String, Integer> frecuencias;    
    private static Map<String, Resultado> resultadosMap;
    
    public static ArrayList<Resultado> buscar(String busqueda) throws ClassNotFoundException, SQLException, Exception {       
        
        AccesoBD db = new AccesoBD();
        
        HashSet<String> stopwords = MyServletContextListener.getStopwords();
        db.obtenerConexion();
        db.getStatement();
            
        resultadosMap = new HashMap<String, Resultado>();
           
         
         int cantidadDeDocumentosEnBase = DBDocumento.contarDocumentos(db);
         
         System.out.println("La cantidad de documentos es: " + cantidadDeDocumentosEnBase);
         
         busqueda = busqueda.replace("+", " ");
         
         //Separamos los terminos de busqueda por espacio
         String[] words = busqueda.split("\\s+");
         
         Resultado resultado = new Resultado();
         
         //Para cada resultado mostrar la palabra buscada y su peso asociado         
         for (int i = 0; i < words.length; i++) {
             
            String word = words[i];            
            
            ArrayList<Posteo> posteos = DBPosteo.loadDB(db, word);
            Vocabulario vocabulario = DBVocabulario.loadDB(db, word);
            
             for (int j = 0; j < posteos.size(); j++) {
                 
                 String nombre = posteos.get(j).getIdDocumento();
                 Documento doc = DBDocumento.loadDB(db,nombre);
                 
                 resultado = resultadosMap.get(nombre);
                 
                 if(resultado == null)
                 {
                     resultado = new Resultado();
                     
                     resultado.setDocumento(doc);
                     resultado.agregarPalabra(vocabulario.getPalabra(), 0f);
                     
                     resultadosMap.put(nombre, resultado);
                 }
                 else
                 {
                     resultado.agregarPalabra(vocabulario.getPalabra(), 0f);
                     resultadosMap.put(nombre, resultado);
                 }
                 
                 double frecuenciaInversa = Math.log(cantidadDeDocumentosEnBase/vocabulario.getCantidadDocumentos());
                 
                 int frecuencia = posteos.get(j).getCantidadRepeticiones();
                 Float peso = ((float)frecuencia / (float) Math.sqrt(doc.getCantidadPalabras())) * (float) frecuenciaInversa;
                 if (stopwords.contains(word)){
                     peso = peso * (float) 0.0001;
                 }
                             
                 resultado = resultadosMap.get(nombre);
                                  
                 resultado.setFrecuenciaPalabra(vocabulario.getPalabra(), frecuencia);
                 resultado.sumarPesoAPalabra(vocabulario.getPalabra(), peso);
                 resultado.sumarPeso(peso);
                 
                 resultadosMap.put(nombre, resultado);
//                 if(resultado == null)
//                 {
//                     resultado = new Resultado();
//                     resultado.setDocumento(doc);
//                     resultado.setFrecuencia(frecuencia);
//                     resultado.setPeso(peso);
//                 }
                 
                 
                                  
                 
                 //Float valor = pesosBusqueda.get(nombre);
                 
                 
                 
//                 Resultado resultado = resultados.get(nombre);
//                 
//                 if (resultado != null){
//                     resultado.sumarPeso(peso);               
//                 }
//                 else{
//                     resultado = resultadoNuevo;
//                 }
//                 
//                 resultados.put(nombre, resultado);
                 
//                pesosBusqueda.put(nombre, valor);
//                frecuencias.put(nombre, frecuencia);
             }
        }
         
         resultadosMap = MapUtil.sortByValue(resultadosMap);
         
         ArrayList<Resultado> listaResultados = new ArrayList<>();
         
         int i = 0;
         for (Map.Entry<String, Resultado> entry : resultadosMap.entrySet()) {
            //Documento doc = DBDocumento.loadDB(db, entry.getKey());
            //Float peso = entry.getValue();
            //Resultado resultado = new Resultado(doc, peso);

            //resultado.setFrecuencia(frecuencias.get(entry.getKey()));

            listaResultados.add(entry.getValue());
            i++;
    //    if (i == 50){
    //        break;
    //    }
        } 
         
        return listaResultados;
    }

    
}
