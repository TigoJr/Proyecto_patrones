/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.utp.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import pe.edu.utp.daoimpl.HabitacionDAOImpl;
import pe.edu.utp.daoimpl.ReservaDAOImpl;
import pe.edu.utp.factory.EstadoHabitacionFactory;
import pe.edu.utp.modelo.Habitacion;
import pe.edu.utp.modelo.Reserva;
import pe.edu.utp.state.EstadoDisponible;
import pe.edu.utp.state.EstadoHabitacion;
import pe.edu.utp.state.EstadoMantenimiento;
import pe.edu.utp.state.EstadoOcupada;
import pe.edu.utp.vista.PrincipalVista;

/**
 *
 * @author USUARIO
 */
public class CheckInOutControlador implements ActionListener {

    private final HabitacionDAOImpl habitacionDao;
    private final PrincipalVista vista;

    public CheckInOutControlador(HabitacionDAOImpl habitacionDao, PrincipalVista vista) {
        this.habitacionDao = habitacionDao;
        this.vista = vista;

        agregarEventos();
        cargarHabitaciones();
    }

    private void agregarEventos() {
        vista.getBtnCheckIn().addActionListener(this);
        vista.getBtnCheckOut().addActionListener(this);
        vista.getBtnRefrescarPC().addActionListener(this);
        vista.getCbxHabitacionPCK().addActionListener(this);
    }

    private void cargarHabitaciones() {
        vista.getCbxHabitacionPCK().removeAllItems();
        List<Reserva> reservasPagadas = new ReservaDAOImpl().listarReservasPagadas();

        for (Reserva r : reservasPagadas) {
            Habitacion h = habitacionDao.buscarPorId(r.getIdHabitacion());
            if (!h.getEstadoActual().getNombreEstado().equalsIgnoreCase("Disponible")) {
                String texto = h.getIdHabitacion() + " - Hab. " + h.getNumero() + " (" + h.getEstadoActual().getNombreEstado() + ")";
                vista.getCbxHabitacionPCK().addItem(texto);
            }
        }

        vista.getTxtEstadoPCK().setText("");
        actualizarEstadoActual();
    }

    private void actualizarEstadoActual() {
        if (vista.getCbxHabitacionPCK().getSelectedItem() != null) {
            int id = Integer.parseInt(
                    vista.getCbxHabitacionPCK().getSelectedItem().toString().split(" - ")[0]
            );
            Habitacion h = habitacionDao.buscarPorId(id);
            vista.getTxtEstadoPCK().setText(h.getEstadoActual().getNombreEstado());

            switch (h.getEstadoActual().getNombreEstado().toLowerCase()) {
                case "ocupada" -> {
                    vista.getBtnCheckIn().setEnabled(true);
                    vista.getBtnCheckOut().setEnabled(false);
                }
                case "mantenimiento" -> {
                    vista.getBtnCheckIn().setEnabled(false);
                    vista.getBtnCheckOut().setEnabled(true);
                }
                default -> {
                    vista.getBtnCheckIn().setEnabled(false);
                    vista.getBtnCheckOut().setEnabled(false);
                }
            }
        }
    }

    private void hacerCheckIn() {
        int id = getHabitacionSeleccionada();
        if (id == -1) {
            return;
        }

        Habitacion h = habitacionDao.buscarPorId(id);
        
        if (h.getEstadoActual().getNombreEstado().equalsIgnoreCase("Ocupada")) {
            h.setEstadoActual(new EstadoMantenimiento());
            h.mostrarEstado();

            String estadoTexto = "Mantenimiento";
            EstadoHabitacion estado = EstadoHabitacionFactory.crearEstado(estadoTexto);
            h.setEstadoActual(estado);

            habitacionDao.actualizar(h);

            JOptionPane.showMessageDialog(null, "Check-In realizado. Habitación ahora está en MANTENIMIENTO.");
            cargarHabitaciones();
        } else {
            JOptionPane.showMessageDialog(null, "Esta habitación no está ocupada.");
        }
    }

    private void hacerCheckOut() {
        int id = getHabitacionSeleccionada();
        if (id == -1) {
            return;
        }

        Habitacion h = habitacionDao.buscarPorId(id);
        
        if (h.getEstadoActual().getNombreEstado().equalsIgnoreCase("Mantenimiento")) {
            h.setEstadoActual(new EstadoDisponible());
            h.mostrarEstado();

            String estadoTexto = "Disponible";
            EstadoHabitacion estado = EstadoHabitacionFactory.crearEstado(estadoTexto);
            h.setEstadoActual(estado);
            habitacionDao.actualizar(h);

            JOptionPane.showMessageDialog(null, "Check-Out realizado. Habitación ahora está DISPONIBLE.");
            cargarHabitaciones();
        } else {
            JOptionPane.showMessageDialog(null, "Esta habitación no está en mantenimiento.");
        }
    }

    private int getHabitacionSeleccionada() {
        if (vista.getCbxHabitacionPCK().getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Selecciona una habitación.");
            return -1;
        }

        return Integer.parseInt(
                vista.getCbxHabitacionPCK().getSelectedItem().toString().split(" - ")[0]
        );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object fuente = e.getSource();

        if (fuente == vista.getBtnRefrescarPC() || fuente == vista.getCbxHabitacionPCK()) {
            actualizarEstadoActual();
            cargarHabitaciones();
        } else if (fuente == vista.getBtnCheckIn()) {
            hacerCheckIn();
        } else if (fuente == vista.getBtnCheckOut()) {
            hacerCheckOut();
        }
    }
}
