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
    private String palabra;
    private int cantidadDocumentos;
    private int maximaFrecuencia;
    private boolean actualizado;

    public Vocabulario() {
        this.palabra = null;
        this.cantidadDocumentos = 0;
        this.maximaFrecuencia = 0;
        this.actualizado = false;
        repeticionesEnDocumentos = new HashMap<String, Integer>();
    }

    public void setActualizado() {
        this.actualizado = true;
    }
    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public void setCantidadDocumentos(int cantidadDocumentos) {
        this.cantidadDocumentos = cantidadDocumentos;
    }

    public void setMaximaFrecuencia(int maximaFrecuencia) {
        this.maximaFrecuencia = maximaFrecuencia;
    }

    public void setRepeticionesEnDocumentos(Map<String, Integer> repeticionesEnDocumentos) {
        this.repeticionesEnDocumentos = repeticionesEnDocumentos;
    }

    public String getPalabra() {
        return palabra;
    }

    public int getCantidadDocumentos() {
        return cantidadDocumentos;
    }

    public int getMaximaFrecuencia() {
        return maximaFrecuencia;
    }

    public Map<String, Integer> getRepeticionesEnDocumentos() {
        return repeticionesEnDocumentos;
    }
    
    private Map<String, Integer> repeticionesEnDocumentos;
    
    public Vocabulario(String palabra)
    {
        this.palabra = palabra;
        this.cantidadDocumentos = 0;
        this.maximaFrecuencia = 0;
        this.actualizado = false;
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
            if(maximaFrecuencia == 0){
                maximaFrecuencia = 1;
            }
        }
        
        repeticionesEnDocumentos.put(documento, cantidad);
        this.actualizado = false;
    }
    
    public void indexarDocumento(String documento) {
        
        
        
    }

    public boolean getActualizado() {
        return this.actualizado;
    }
    
}
