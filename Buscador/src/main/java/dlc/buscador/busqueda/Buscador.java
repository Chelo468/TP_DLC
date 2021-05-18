/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.buscador.busqueda;

import dlc.indexador.bd.AccesoBD;
import dlc.indexador.bd.DBDocumento;
import dlc.indexador.bd.DBPosteo;
import dlc.indexador.bd.DBVocabulario;
import dlc.indexador.entidades.Documento;
import dlc.indexador.entidades.Posteo;
import dlc.indexador.entidades.Resultado;
import dlc.indexador.entidades.Vocabulario;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gabriel
 */
public class Buscador {
    private static Map<String, Float> pesosBusqueda;
    public static ArrayList<Resultado> buscar(String busqueda) throws ClassNotFoundException, SQLException, Exception {
        pesosBusqueda = new HashMap<String, Float>();
           
         AccesoBD db = new AccesoBD();
         db.obtenerConexion();
         db.getStatement();
         int documentos = DBDocumento.contarDocumentos(db);
         System.out.println("La cantidad de documentos es: " + documentos);
         String[] words = busqueda.split("\\s+");
         for (int i = 0; i < words.length; i++) {
            String word = words[i];
            ArrayList<Posteo> posteos = DBPosteo.loadDB(db, word);
            Vocabulario vocabulario = DBVocabulario.loadDB(db, word);

             int cantidadDocumentos = vocabulario.getCantidadDocumentos();
             for (int j = 0; j < posteos.size(); j++) {
                 String nombre = posteos.get(j).getIdDocumento();
                 Documento doc = DBDocumento.loadDB(db,nombre);
                 double cantidadPalabras = Math.sqrt(doc.getCantidadPalabras());
                 //System.out.println(cantidadPalabras);
                 int frecuencia = posteos.get(j).getCantidadRepeticiones();
                 double frecuenciaInversa = Math.log(documentos/cantidadDocumentos);
                 Float peso =  ((float)frecuencia / (float) cantidadPalabras) * (float) frecuenciaInversa;
                 Float valor = pesosBusqueda.get(nombre);
                 if (valor != null){
                     valor = valor + peso;
               
                 }
                 else{
                     valor = peso;
                 }
                       pesosBusqueda.put(nombre, valor);
             }
        }
         Map<String, Float> pesosOrdenados = MapUtil.sortByValue(pesosBusqueda);
         ArrayList<Resultado> listaResultados = new ArrayList<>();
         int i = 0;
         for (Map.Entry<String, Float> entry : pesosOrdenados.entrySet()) {
             Documento doc = DBDocumento.loadDB(db, entry.getKey());
             Float peso = entry.getValue();
             Resultado resultado = new Resultado(doc, peso);
             listaResultados.add(resultado);
  //  System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
    i++;
    if (i == 50){
        break;
    }
} 
         return listaResultados;
    }
    
}
