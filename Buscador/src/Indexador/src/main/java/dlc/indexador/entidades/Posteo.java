/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.indexador.entidades;

/**
 *
 * @author Chelo
 */
public class Posteo {
    private String idPalabra;
    private String idDocumento;
    private int cantidadRepeticiones;

    public Posteo(String palabra, String documento) {
        this.idPalabra = palabra;
        this.idDocumento = documento;
        this.cantidadRepeticiones = 0;
    }

    public Posteo() {
        this.idPalabra = null;
        this.idDocumento = null;
        this.cantidadRepeticiones = 0;
    }

    public void setIdPalabra(String idPalabra) {
        this.idPalabra = idPalabra;
    }

    public void setIdDocumento(String idDocumento) {
        this.idDocumento = idDocumento;
    }

    public void setCantidadRepeticiones(int cantidadRepeticiones) {
        this.cantidadRepeticiones = cantidadRepeticiones;
    }

    public String getIdPalabra() {
        return idPalabra;
    }

    public String getIdDocumento() {
        return idDocumento;
    }

    public int getCantidadRepeticiones() {
        return cantidadRepeticiones;
    }

    public Posteo(String idPalabra, String idDocumento, int cantidadRepeticiones) {
        this.idPalabra = idPalabra;
        this.idDocumento = idDocumento;
        this.cantidadRepeticiones = cantidadRepeticiones;
    }

    public void sumarRepeticion() {
        this.cantidadRepeticiones ++;
    }
}
