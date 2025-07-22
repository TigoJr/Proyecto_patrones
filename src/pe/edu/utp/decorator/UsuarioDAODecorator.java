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
public abstract class UsuarioDAODecorator implements UsuarioDAO {
    protected UsuarioDAO decorado;

    public UsuarioDAODecorator(UsuarioDAO decorado) {
        this.decorado = decorado;
    }
    
    @Override
    public boolean agregar(Usuario usuario) {
        return decorado.agregar(usuario);
    }
    
    @Override
    public boolean existePorNombreUsuario(String nombreUsuario) {
        return decorado.existePorNombreUsuario(nombreUsuario);
    }
    
    @Override
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        return decorado.buscarPorNombreUsuario(nombreUsuario);
    }
}