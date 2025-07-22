/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pe.edu.utp.dao;

import java.util.List;

/**
 *
 * @author USUARIO
 */
public interface CrudDAO<T, ID> {
    boolean agregar(T entidad);
    boolean actualizar(T entidad);
    boolean eliminar(ID id);
    T buscarPorId(ID id);
    List<T> listarTodos();
}
