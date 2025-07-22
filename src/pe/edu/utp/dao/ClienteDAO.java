/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pe.edu.utp.dao;

import pe.edu.utp.modelo.Cliente;

/**
 *
 * @author USUARIO
 */
public interface ClienteDAO extends CrudDAO<Cliente, Integer> {

    boolean existePorDni(String dni);
    boolean existePorEmail(String email);
    boolean existePorTelefono(String telefono);

    boolean existePorDniEnOtro(String dni, int idActual);
    boolean existePorEmailEnOtro(String email, int idActual);
    boolean existePorTelefonoEnOtro(String telefono, int idActual);
}