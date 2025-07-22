/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.utp.facade;

import pe.edu.utp.daoimpl.HabitacionDAOImpl;
import pe.edu.utp.daoimpl.ReservaDAOImpl;
import pe.edu.utp.factory.EstadoHabitacionFactory;
import pe.edu.utp.modelo.Habitacion;
import pe.edu.utp.modelo.Reserva;
import pe.edu.utp.state.EstadoHabitacion;
import pe.edu.utp.state.EstadoOcupada;

public class ReservaFacade {

    private final ReservaDAOImpl reservaDAO;
    private final HabitacionDAOImpl habitacionDAO;

    public ReservaFacade(ReservaDAOImpl reservaDAO, HabitacionDAOImpl habitacionDAO) {
        this.reservaDAO = reservaDAO;
        this.habitacionDAO = habitacionDAO;
    }

    public boolean hacerReserva(Reserva reserva) {
        try {
            boolean insertado = reservaDAO.agregar(reserva);
            if (!insertado) {
                return false;
            }

            Habitacion habitacion = habitacionDAO.buscarPorId(reserva.getIdHabitacion());
            if (habitacion == null) {
                return false;
            }

            habitacion.setEstadoActual(new EstadoOcupada());
            String estadoTexto = "Ocupada";
            EstadoHabitacion estado = EstadoHabitacionFactory.crearEstado(estadoTexto);
            habitacion.setEstadoActual(estado);
            
            boolean actualizada = habitacionDAO.actualizar(habitacion);

            if (!actualizada) {
                System.out.println("Reserva creada, pero no se actualizó la habitación.");
            }
            
            return true;

        } catch (Exception e) {
            System.err.println("Error en hacerReserva(): " + e.getMessage());
            return false;
        }

    }
}
