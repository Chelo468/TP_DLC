/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.indexador;

import dlc.indexador.entidades.Vocabulario;
import java.util.Calendar;
import java.util.Map;
import dlc.indexador.bd.AccesoBD;
import dlc.indexador.entidades.Posteo;

/**
 *
 * @author Chelo
 */
public class Main {
    
    public static void main(String[] args)
    {        
        try
        {
            long milisInicial = Calendar.getInstance().getTimeInMillis();
            
          AccesoBD db = new AccesoBD();
          db.obtenerConexion();
          db.getStatement();
          
        //  AccesoBD.borrarDatos(db);
         // AccesoBD.crearDatos(db);
          Indexador.Indexar();
          //IndexadorDrive.Indexar();
          //Map<String, Vocabulario> resultado = IndexadorDrive.Indexar(); 
          
            
         //   System.out.println(resultado);
                        
            Map<String, Vocabulario> palabrasIndexadas = Indexador.getPalabrasIndexadas();
            System.out.println(palabrasIndexadas.size());
           //DBVocabulario.prepararVocabularioMerge(db);
         //   DBVocabulario.prepararVocabulario(db);
//            palabrasIndexadas.forEach((key, value) -> {
//            Vocabulario voc;    
//                try {
//                    voc = DBVocabulario.loadDB(db, value.getPalabra());
//                    if (voc == null){
//                        DBVocabulario.prepararVocabulario(db);
//                        DBVocabulario.agregarVocabularioPreparado(db, value);
//                    }    
//                    else{
//                        if (value.getActualizado() == false){
//                            DBVocabulario.actualizarVocabulario(db, value);
//                            System.out.println("ok");
//                        }
//                       
//                    }
//                 //   System.out.println(value.getPalabra());
                  //  System.out.println(value.getMaximaFrecuencia());
                //    DBVocabulario.agregarVocabularioPreparadoConUpdate(db, value);
                //    DBVocabulario.mergeVocabularioPreparado(db, value);
//                 //   DBVocabulario.agregarVocabularioPreparado(db, value);
//                } catch (Exception ex) {
//                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            });
//            String[] palabras = {"admiration", "quixote", "panza"};
//            ArrayList<Vocabulario> test = DBVocabulario.loadDB(db, palabras);
//            for (int i = 0; i < test.size(); i++) {
//                System.out.println(test.get(i).getPalabra());
//                System.out.println(test.get(i).getCantidadDocumentos());
//                System.out.println(test.get(i).getMaximaFrecuencia());
//            }
//                Vocabulario test = DBVocabulario.loadDB(db, "admiration");
//                System.out.println(test.getPalabra());
//                System.out.println(test.getCantidadDocumentos());
//                System.out.println(test.getMaximaFrecuencia());
            Map<String, Posteo> posteosIndexados = Indexador.getPosteosIndexadas();
            System.out.println(posteosIndexados.size());
//            DBPosteo.prepararPosteo(db);
//            posteosIndexados.forEach((key, value) -> {
//                try {
//                    DBPosteo.agregarPosteoPreparado(db, value);
//                } catch (Exception ex) {
//                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            });
//            String[] documentos = {"wwrld10.txt", "ylwlp10.txt", "zambs10.txt"};
//            String[] palabras2 = {"take", "electronic", "donations"};
//            ArrayList<Posteo> test2 = DBPosteo.loadDB(db, documentos, palabras2);
//            System.out.println(test2.size());
//            for (int i = 0; i < test2.size(); i++) {
//                System.out.println(test2.get(i).getIdPalabra());
//                System.out.println(test2.get(i).getIdDocumento());
//                System.out.println(test2.get(i).getCantidadRepeticiones());
//            }
                
            
            
            long milisFinal = Calendar.getInstance().getTimeInMillis();
            long demoraProceso = milisFinal - milisInicial;
            
            System.out.println("Cantidad de palabras indexadas: " + palabrasIndexadas.size());
            System.out.println("Tiempo de proceso: " + (demoraProceso / 1000) + " segundos.");
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        
    }
    
}
