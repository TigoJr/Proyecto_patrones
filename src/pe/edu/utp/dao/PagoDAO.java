/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pe.edu.utp.dao;

import java.util.List;
import pe.edu.utp.modelo.Pago;

/**
 *
 * @author USUARIO
 */
public interface PagoDAO {

    boolean agregar(Pago pago);
    boolean existePorIdReserva(int idReserva);
    List<Pago> listarTodos();
}
