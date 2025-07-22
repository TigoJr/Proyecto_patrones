/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.utp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import pe.edu.utp.factory.EstadoReservaFactory;
import pe.edu.utp.modelo.Reserva;
import pe.edu.utp.state.EstadoReserva;

/**
 *
 * @author USUARIO
 */
public class ReservaDAO {

    private final Connection conexion;

    public ReservaDAO() {
        conexion = ConexionBD.getInstancia().getConexion();
    }

    public boolean agregar(Reserva reserva) {
        String sql = "INSERT INTO Reserva (idCliente, idHabitacion, fechaInicio, fechaFin, estado, fechaReserva) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, reserva.getIdCliente());
            ps.setInt(2, reserva.getIdHabitacion());
            ps.setDate(3, new java.sql.Date(reserva.getFechaInicio().getTime()));
            ps.setDate(4, new java.sql.Date(reserva.getFechaFin().getTime()));
            ps.setString(5, reserva.getNombreEstado()); // <- Estado como texto
            ps.setDate(6, new java.sql.Date(reserva.getFechaReserva().getTime()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar reserva: " + e.getMessage());
            return false;
        }
    }

    public Reserva buscarPorId(int idReserva) {
        Reserva r = null;
        String sql = "SELECT * FROM Reserva WHERE idReserva = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                r = new Reserva();
                r.setIdReserva(rs.getInt("idReserva"));
                r.setIdCliente(rs.getInt("idCliente"));
                r.setIdHabitacion(rs.getInt("idHabitacion"));
                r.setFechaInicio(rs.getDate("fechaInicio"));
                r.setFechaFin(rs.getDate("fechaFin"));
                r.setFechaReserva(rs.getDate("fechaReserva"));

                String estadoTexto = rs.getString("estado");
                EstadoReserva estado = EstadoReservaFactory.crearEstado(estadoTexto);
                r.setEstadoActual(estado);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar reserva por ID: " + e.getMessage());
        }
        return r;
    }

    public String obtenerNombreClientePorId(int idCliente) {
        String nombre = "";
        try (PreparedStatement ps = conexion.prepareStatement("Select nombre FROM cliente WHERE idCliente = ?")) {

            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nombre = rs.getString("nombre");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nombre;
    }

    public int obtenerNumeroHabitacion(int idHabitacion) {
        int numero = 0;
        try (PreparedStatement ps = conexion.prepareStatement("SELECT numero FROM habitacion WHERE idHabitacion = ?")) {

            ps.setInt(1, idHabitacion);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                numero = rs.getInt("numero");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return numero;
    }

    public List<Reserva> listarTodas() {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM Reserva";
        try (Statement st = conexion.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Reserva r = new Reserva();
                r.setIdReserva(rs.getInt("idReserva"));
                r.setIdCliente(rs.getInt("idCliente"));
                r.setIdHabitacion(rs.getInt("idHabitacion"));
                r.setFechaInicio(rs.getDate("fechaInicio"));
                r.setFechaFin(rs.getDate("fechaFin"));
                
                String estadoTexto = rs.getString("estado");
                EstadoReserva estado = EstadoReservaFactory.crearEstado(estadoTexto);
                
                r.setEstadoActual(estado);
                r.setFechaReserva(rs.getDate("fechaReserva"));
                lista.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar reservas: " + e.getMessage());
        }
        return lista;
    }

    public List<Reserva> listarReservasPagadas() {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT r.* FROM Reserva r "
                + "INNER JOIN Pago p ON r.idReserva = p.idReserva";

        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Reserva r = new Reserva();
                r.setIdReserva(rs.getInt("idReserva"));
                r.setIdCliente(rs.getInt("idCliente"));
                r.setIdHabitacion(rs.getInt("idHabitacion"));
                r.setFechaInicio(rs.getDate("fechaInicio"));
                r.setFechaFin(rs.getDate("fechaFin"));
                
                String estadoTexto = rs.getString("estado");
                EstadoReserva estado = EstadoReservaFactory.crearEstado(estadoTexto);
                
                r.setEstadoActual(estado);
                r.setFechaReserva(rs.getDate("fechaReserva"));
                lista.add(r);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar reservas pagadas: " + e.getMessage());
        }

        return lista;
    }
}
