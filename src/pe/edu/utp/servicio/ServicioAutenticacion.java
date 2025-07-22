/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.utp.servicio;

import org.mindrot.jbcrypt.BCrypt;
import pe.edu.utp.dao.UsuarioDAO;
import pe.edu.utp.modelo.Usuario;

/**
 *
 * @author USUARIO
 */
public class ServicioAutenticacion {

    private final UsuarioDAO usuarioDAO;

    public ServicioAutenticacion(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public boolean registrar(String nombreUsuario, String passwordPlano) {
        if (usuarioDAO.existePorNombreUsuario(nombreUsuario)) {
            return false;
        }

        String passwordEncriptado = BCrypt.hashpw(passwordPlano, BCrypt.gensalt());
        Usuario usuario = new Usuario(nombreUsuario, passwordEncriptado);
        return usuarioDAO.agregar(usuario);
    }

    public boolean autenticar(String nombreUsuario, String passwordPlano) {
        Usuario usuario = usuarioDAO.buscarPorNombreUsuario(nombreUsuario);
        if (usuario == null) {
            return false;
        }

        return BCrypt.checkpw(passwordPlano, usuario.getPasswordHash());
    }
}
