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
    private String path;
    private String driveId;

    private int cantidadPalabras;

    public Documento() {
        this.path = null;
        this.titulo = null;
        this.driveId = null;
        this.cantidadPalabras = 0;
    }

    public Documento(String nombreFichero, String pathArchivo, int cantidadPalabras) {
       this.path = path;
        this.titulo = titulo;
        this.cantidadPalabras = cantidadPalabras;
        this.driveId = null;
    }
     public Documento(String nombreFichero, String pathArchivo, int cantidadPalabras, String driveId) {
       this.path = path;
        this.titulo = titulo;
        this.cantidadPalabras = cantidadPalabras;
        this.driveId = driveId;
    }
     public String getDriveId() {
        return driveId;
    }
    public String getPath() {
        return path;
    }

    public String getTitulo() {
        return titulo;
    }
    public int getCantidadPalabras() {
        return cantidadPalabras;
    }
    public void setDriveId(String driveId) {
        this.driveId = driveId;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setCantidadPalabras(int cantidad) {
        this.cantidadPalabras = cantidad;
    }
    public Documento( String url, String titulo) {
        this.path = url;
        this.titulo = titulo;
        this.cantidadPalabras = 0;
    }
    public Documento( String url, String titulo, String driveId) {
        this.path = url;
        this.titulo = titulo;
        this.driveId = driveId;
        this.cantidadPalabras = 0;
    }

}
