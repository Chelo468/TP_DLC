/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.indexador.entidades;

import com.google.api.client.util.DateTime;
import java.sql.Time;
import java.sql.Timestamp;

/**
 *
 * @author Chelo
 */
public class Documento {
    private String titulo;
    private String url;

    private int cantidadPalabras;
    private Timestamp ultimaActualizacion;

    public Documento() {
        this.url = null;
        this.titulo = null;
        this.cantidadPalabras = 0;
        this.ultimaActualizacion = null;
    }

    public Documento(String nombreFichero, String pathArchivo, int cantidadPalabras) {
       this.url = url;
        this.titulo = titulo;
        this.cantidadPalabras = cantidadPalabras;
        this.ultimaActualizacion = new Timestamp(System.currentTimeMillis());
    }

    public String getUrl() {
        return url;
    }

    public String getTitulo() {
        return titulo;
    }
    public int getCantidadPalabras() {
        return cantidadPalabras;
    }
    public Timestamp getFechaACtualizacion() {
        return ultimaActualizacion;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setCantidadPalabras(int cantidad) {
        this.cantidadPalabras = cantidad;
    }
    public void setFechaActualizacion(Timestamp fechaHora) {
        this.ultimaActualizacion = fechaHora;
    }
    public Documento( String url, String titulo) {
        this.url = url;
        this.titulo = titulo;
        this.cantidadPalabras = 0;
        this.ultimaActualizacion = new Timestamp(System.currentTimeMillis());
    }

}
