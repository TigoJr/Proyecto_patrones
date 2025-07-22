/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.utp.decorator;

import javax.swing.JOptionPane;
import pe.edu.utp.dao.UsuarioDAO;
import pe.edu.utp.modelo.Usuario;

/**
 *
 * @author USUARIO
 */
public class UsuarioDAONotificador extends UsuarioDAODecorator {

    public UsuarioDAONotificador(UsuarioDAO decorado) {
        super(decorado);
    }

    @Override
    public boolean agregar(Usuario usuario) {
        boolean registrado = super.agregar(usuario);
        if (registrado) {
            JOptionPane.showMessageDialog(null, ">> Usuario '" + usuario.getNombreUsuario() + "' registrado correctamente. :D");
        }
        return registrado;
    }
}
