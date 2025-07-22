/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pe.edu.utp.app;

import pe.edu.utp.controlador.LoginControlador;
import pe.edu.utp.vista.LoginVista;

/**
 *
 * @author USUARIO
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LoginVista vista = new LoginVista();
        new LoginControlador(vista);

        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

}
