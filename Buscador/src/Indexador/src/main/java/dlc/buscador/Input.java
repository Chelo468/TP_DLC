/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.buscador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

/**
 *
 * @author Gabriel
 */
public class Input {
    public static void main(String[] args) throws Exception {
         BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));
         String busqueda = reader.readLine();
          Buscador.buscar(busqueda);
    }
}
