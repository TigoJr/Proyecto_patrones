/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.utp.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import pe.edu.utp.daoimpl.ClienteDAOImpl;
import pe.edu.utp.daoimpl.HabitacionDAOImpl;
import pe.edu.utp.daoimpl.ReservaDAOImpl;
import pe.edu.utp.facade.ReservaFacade;
import pe.edu.utp.factory.EstadoReservaFactory;
import pe.edu.utp.modelo.Cliente;
import pe.edu.utp.modelo.Habitacion;
import pe.edu.utp.modelo.Reserva;
import pe.edu.utp.state.EstadoReserva;
import pe.edu.utp.vista.PrincipalVista;

/**
 *
 * @author USUARIO
 */
public class ReservaControlador implements ActionListener {

    private final PrincipalVista vista;
    private final ClienteDAOImpl clienteDao;
    private final HabitacionDAOImpl habitacionDao;
    private final ReservaDAOImpl reservaDao;

    public ReservaControlador(PrincipalVista vista, ClienteDAOImpl clienteDao, HabitacionDAOImpl habitacionDao, ReservaDAOImpl reservaDao) {
        this.vista = vista;
        this.clienteDao = clienteDao;
        this.habitacionDao = habitacionDao;
        this.reservaDao = reservaDao;

        agregarEventos();

        cargarClientes();
        cargarHabitacionesDisponibles();
        cargarReservasEnTabla();
        vista.getTxtEstadoPR().setText("Confirmada");
    }

    private void agregarEventos() {
        vista.getBtnReservarPR().addActionListener(this);
        vista.getBtnLimpiarPR().addActionListener(this);
        vista.getBtnRefrescarPR().addActionListener(this);
    }

    private void cargarClientes() {
        List<Cliente> clientes = clienteDao.listarTodos();
        vista.getCbxClientePR().removeAllItems();
        for (Cliente c : clientes) {
            vista.getCbxClientePR().addItem(c.getIdCliente() + " - " + c.getNombre());
        }
    }

    private void cargarHabitacionesDisponibles() {
        List<Habitacion> habitaciones = habitacionDao.listarTodos();
        vista.getCbxHabitacionPR().removeAllItems();

        for (Habitacion h : habitaciones) {
            if (h.getEstadoActual().getNombreEstado().equalsIgnoreCase("Disponible")) {
                String item = h.getIdHabitacion() + " - N° Hab. " + h.getNumero() + " (" + h.getTipo() + ")";
                vista.getCbxHabitacionPR().addItem(item);
            }
        }
    }

    private void cargarReservasEnTabla() {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaReservas().getModel();
        modelo.setRowCount(0);

        List<Reserva> reservas = reservaDao.listarTodos();
        for (Reserva r : reservas) {
            modelo.addRow(new Object[]{
                r.getIdReserva(),
                r.getIdCliente(),
                r.getIdHabitacion(),
                r.getFechaInicio(),
                r.getFechaFin(),
                r.getEstadoActual().getNombreEstado()
            });
        }
    }

    private void limpiarCampos() {
        vista.getTxtEstadoPR().setText("Confirmada");
        vista.getTxtFechaFinPR().setText("");
        vista.getTxtFechaInicioPR().setText("");
        cargarHabitacionesDisponibles();
        cargarClientes();
        cargarHabitacionesDisponibles();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnReservarPR()) {
            registrarReserva();
        } else if (e.getSource() == vista.getBtnLimpiarPR()) {
            limpiarCampos();
        } else if (e.getSource() == vista.getBtnRefrescarPR()) {
            cargarClientes();
            cargarHabitacionesDisponibles();
            cargarReservasEnTabla();
        }
    }

    private void registrarReserva() {
        try {
            int idCliente = Integer.parseInt(vista.getCbxClientePR().getSelectedItem().toString().split(" - ")[0]);
            int idHabitacion = Integer.parseInt(vista.getCbxHabitacionPR().getSelectedItem().toString().split(" - ")[0]);

            Date fechaInicio = java.sql.Date.valueOf(vista.getTxtFechaInicioPR().getText().trim());
            Date fechaFin = java.sql.Date.valueOf(vista.getTxtFechaFinPR().getText().trim());
            java.sql.Date fechaReservaHoy = java.sql.Date.valueOf(LocalDate.now());

            if (fechaInicio.before(fechaReservaHoy)) {
                JOptionPane.showMessageDialog(null, "La fecha de inicio no puede ser anterior a hoy.");
                return;
            }

            if (!fechaFin.after(fechaInicio)) {
                JOptionPane.showMessageDialog(null, "La fecha de fin debe ser posterior a la fecha de inicio.");
                return;
            }

            String estadoReserva = vista.getTxtEstadoPR().getText().trim();

            Reserva r = new Reserva();
            r.setIdCliente(idCliente);
            r.setIdHabitacion(idHabitacion);
            r.setFechaInicio(fechaInicio);
            r.setFechaFin(fechaFin);

            EstadoReserva estado = EstadoReservaFactory.crearEstado(estadoReserva);
            r.setEstadoActual(estado);
            r.setFechaReserva(fechaReservaHoy);
            System.out.println("[Controlador] Fecha reserva seteada: " + fechaReservaHoy);

            ReservaFacade facade = new ReservaFacade(reservaDao, habitacionDao);
            boolean exito = facade.hacerReserva(r);

            if (exito) {
                JOptionPane.showMessageDialog(null, "Reserva procesada correctamente.");
                cargarReservasEnTabla();
                limpiarCampos();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Verifica los datos ingresados: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
