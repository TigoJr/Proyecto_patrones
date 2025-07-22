/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pe.edu.utp.dao;

import pe.edu.utp.modelo.Usuario;

/**
 *
 * @author USUARIO
 */
public interface UsuarioDAO {

    boolean agregar(Usuario usuario);
    boolean existePorNombreUsuario(String nombreUsuario);
    Usuario buscarPorNombreUsuario(String nombreUsuario);
}