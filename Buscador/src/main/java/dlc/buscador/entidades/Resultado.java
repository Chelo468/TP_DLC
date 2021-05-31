/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.buscador.entidades;

import dlc.indexador.entidades.Documento;
import dlc.indexador.entidades.Vocabulario;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Chelo
 */
public class Resultado implements Comparable {
    private Documento documento;
    private Float peso;
//    private int frecuencia;
    private ArrayList<ResultadoVocabulario> palabras;
    
    public Resultado()
    {
        this.documento = new Documento();
        this.peso = 0f;
        palabras = new ArrayList<>();
    }
    
    public Resultado(Documento doc, Float peso) {
        this.documento = doc;
        this.peso = peso;
        palabras = new ArrayList<>();
    }
    
    public Documento getDocumento() {
        return documento;
    }

    public Float getPeso() {
        return peso;
    }
    public void setDocumento(Documento doc) {
        this.documento = doc;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }   

//    public int getFrecuencia() {
//        return frecuencia;
//    }
    
    public void sumarPeso(Float peso)
    {
        this.peso += peso;
    }

//    public void setFrecuencia(int frecuencia) {
//        this.frecuencia = frecuencia;
//    }
    
    public ArrayList<ResultadoVocabulario> getPalabras()
    {
        return this.palabras;
    }
    
    public Float getPesoDePalabra(String palabra)
    {
        Float peso = 0f;
        
        for (int i = 0; i < palabras.size(); i++) {
            if(palabras.get(i).getPalabra().toLowerCase() == palabra.toLowerCase())
            {
                peso = palabras.get(i).getPeso();
                break;
            }
        }
        
        
        return peso;
    }
    
    public void sumarPesoAPalabra(String palabra, Float peso)
    {        
        Float pesoActual = 0f;
        
        for (int i = 0; i < palabras.size(); i++) {
            if(palabras.get(i).getPalabra().toLowerCase() == palabra.toLowerCase())
            {
                pesoActual = palabras.get(i).getPeso();
                
                pesoActual += peso;
                
                palabras.get(i).setPeso(pesoActual);
                break;
            }
        }
    }
    
    public void setFrecuenciaPalabra(String palabra, int frecuencia)
    {
         for (int i = 0; i < palabras.size(); i++) {
            if(palabras.get(i).getPalabra().toLowerCase() == palabra.toLowerCase())
            {
                palabras.get(i).setFrecuencia(frecuencia);
                break;
            }
        }
    }
    
    public void agregarPalabra(String palabra, Float peso)
    {
        ResultadoVocabulario vocab = new ResultadoVocabulario();
        
        vocab.setPalabra(palabra);
        vocab.setPeso(peso);
        
        this.palabras.add(vocab);
    }

    @Override
    public int compareTo(Object objeto2) {
        
        float diferencia = this.peso - ((Resultado)objeto2).getPeso();
        
        if(diferencia > 0)
        {
            return 1;
        }
        
        if(diferencia < 0)
        {
            return -1;
        }
        
        return 0;
        
    }
    
    public class ResultadoVocabulario
    {
        private String palabra;
        private int frecuencia;
        private Float ponderancia;
        
        public String getPalabra()
        {
            return this.palabra;
        }
        
        public void setPalabra(String palabra)
        {
            this.palabra = palabra;
        }

        public int getFrecuencia() {
            return frecuencia;
        }

        public void setFrecuencia(int frecuencia) {
            this.frecuencia = frecuencia;
        }

        public Float getPeso() {
            return ponderancia;
        }

        public void setPeso(Float peso) {
            this.ponderancia = peso;
        }
        
        
    }
}


