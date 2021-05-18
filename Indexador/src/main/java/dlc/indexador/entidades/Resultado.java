/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.indexador.entidades;

/**
 *
 * @author Gabriel
 */
public class Resultado {
    private Documento documento;
    private Float peso;
    
    public Resultado(Documento doc, Float peso) {
        this.documento = doc;
        this.peso = peso;
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
    
}
