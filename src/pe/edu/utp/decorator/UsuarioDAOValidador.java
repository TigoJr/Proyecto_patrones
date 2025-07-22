/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.utp.decorator;

import pe.edu.utp.dao.UsuarioDAO;
import pe.edu.utp.modelo.Usuario;

/**
 *
 * @author USUARIO
 */
public class UsuarioDAOValidador extends UsuarioDAODecorator {

    public UsuarioDAOValidador(UsuarioDAO decorado) {
        super(decorado);
    }

    @Override
    public boolean agregar(Usuario usuario) {
        if (usuario.getNombreUsuario() == null || usuario.getNombreUsuario().isBlank()) {
            System.out.println("[VALIDACIÓN] El nombre de usuario está vacío.");
            return false;
        }

        if (usuario.getPasswordHash() == null || usuario.getPasswordHash().length() < 6) {
            System.out.println("[VALIDACIÓN] La contraseña encriptada es inválida.");
            return false;
        }

        return super.agregar(usuario);
    }
}

