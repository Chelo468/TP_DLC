/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.buscador.busqueda;

import dlc.buscador.entidades.Resultado;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Gabriel
 */
public class Input {
    public static void main(String[] args) throws Exception {
         BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));
        String busqueda = reader.readLine();
          ArrayList<Resultado> listaResultados = Buscador.buscar(busqueda);
          for (int i = 0; i < listaResultados.size(); i++) {
            String documento = listaResultados.get(i).getDocumento().getTitulo();
            Float peso = listaResultados.get(i).getPeso();
              System.out.println("Documento: " + documento + ", Peso: " + peso);
            
        }
    }
    
}

