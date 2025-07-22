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
public class UsuarioDAOLogger extends UsuarioDAODecorator {

    public UsuarioDAOLogger(UsuarioDAO decorado) {
        super(decorado);
    }

    @Override
    public boolean agregar(Usuario usuario) {
        System.out.println("[LOG] Intentando registrar usuario: " + usuario.getNombreUsuario());
        boolean resultado = super.agregar(usuario);
        System.out.println("[LOG] Resultado: " + (resultado ? "Éxito" : "Fallo"));
        return resultado;
    }

    @Override
    public boolean existePorNombreUsuario(String nombreUsuario) {
        System.out.println("[LOG] Verificando si existe el usuario: " + nombreUsuario);
        boolean existe = super.existePorNombreUsuario(nombreUsuario);
        System.out.println("[LOG] ¿Existe? " + (existe ? "Sí" : "No"));
        return existe;
    }

    @Override
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        System.out.println("[LOG] Buscando usuario: " + nombreUsuario);
        Usuario usuario = super.buscarPorNombreUsuario(nombreUsuario);
        System.out.println("[LOG] Resultado: " + (usuario != null ? "Encontrado" : "No encontrado"));
        return usuario;
    }
}
