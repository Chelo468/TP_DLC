/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.indexador.entidades;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Chelo
 */
public class Vocabulario {
    private int idPalabra;
    private String palabra;
    private int cantidadDocumentos;
    private int maximaFrecuencia;
    
    private Map<String, Integer> repeticionesEnDocumentos;
    
    public Vocabulario(String palabra)
    {
        this.palabra = palabra;
        this.cantidadDocumentos = 0;
        this.maximaFrecuencia = 0;
        repeticionesEnDocumentos = new HashMap<String, Integer>();
    }
    
    public void agregarCantidadEnDocumento(String documento)
    {
        Integer cantidad = repeticionesEnDocumentos.get(documento);
        
        if(cantidad != null)
        {
            cantidad++;
            
            //Si supera la frecuencia máxima, actualizamos el valor
            if(cantidad > maximaFrecuencia)
            {
                maximaFrecuencia = cantidad;
            }
        }
        else
        {
            cantidadDocumentos++;
            cantidad = 1;
            maximaFrecuencia = 1;
        }
        
        repeticionesEnDocumentos.put(documento, cantidad);
        
    }
    
    public void indexarDocumento(String documento) {
        
        
        
    }
    
}
