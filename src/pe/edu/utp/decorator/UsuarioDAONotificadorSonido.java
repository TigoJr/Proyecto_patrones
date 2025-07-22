/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.utp.decorator;

import java.awt.Toolkit;
import pe.edu.utp.dao.UsuarioDAO;
import pe.edu.utp.modelo.Usuario;

/**
 *
 * @author USUARIO
 */
public class UsuarioDAONotificadorSonido extends UsuarioDAODecorator {

    public UsuarioDAONotificadorSonido(UsuarioDAO decorado) {
        super(decorado);
    }

    @Override
    public boolean agregar(Usuario usuario) {
        boolean registrado = super.agregar(usuario);
        if (registrado) {
            Toolkit.getDefaultToolkit().beep();
            System.out.println("[Alerta sonora] Usuario registrado con éxito: " + usuario.getNombreUsuario());
        }
        return registrado;
    }
}
