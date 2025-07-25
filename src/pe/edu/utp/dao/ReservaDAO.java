/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pe.edu.utp.dao;

import java.util.List;
import pe.edu.utp.modelo.Reserva;

/**
 *
 * @author USUARIO
 */
public interface ReservaDAO extends CrudDAO<Reserva, Integer> {

    List<Reserva> listarReservasPagadas();
    String obtenerNombreClientePorId(int idCliente);
    int obtenerNumeroHabitacion(int idHabitacion);
}