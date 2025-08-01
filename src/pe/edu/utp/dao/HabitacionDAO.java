/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pe.edu.utp.dao;

import pe.edu.utp.modelo.Habitacion;

/**
 *
 * @author USUARIO
 */
public interface HabitacionDAO extends CrudDAO<Habitacion, Integer> {

    boolean existePorNumero(int numero);
    boolean existePorNumeroEnOtro(int numero, int idHabitacion);
}