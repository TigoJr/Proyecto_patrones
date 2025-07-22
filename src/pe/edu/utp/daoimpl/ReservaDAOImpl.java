/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.utp.daoimpl;

import pe.edu.utp.singleton.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import pe.edu.utp.dao.ReservaDAO;
import pe.edu.utp.factory.EstadoReservaFactory;
import pe.edu.utp.modelo.Reserva;
import pe.edu.utp.state.EstadoReserva;

/**
 *
 * @author USUARIO
 */
public class ReservaDAOImpl implements ReservaDAO {

    private final Connection conexion;

    public ReservaDAOImpl() {
        this.conexion = ConexionBD.getInstancia().getConexion();
    }

    @Override
    public boolean agregar(Reserva reserva) {
        String sql = "INSERT INTO Reserva (idCliente, idHabitacion, fechaInicio, fechaFin, estado, fechaReserva) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, reserva.getIdCliente());
            ps.setInt(2, reserva.getIdHabitacion());
            ps.setDate(3, new java.sql.Date(reserva.getFechaInicio().getTime()));
            ps.setDate(4, new java.sql.Date(reserva.getFechaFin().getTime()));
            ps.setString(5, reserva.getNombreEstado());
            ps.setDate(6, new java.sql.Date(reserva.getFechaReserva().getTime()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar reserva: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Reserva buscarPorId(Integer idReserva) {
        String sql = "SELECT * FROM Reserva WHERE idReserva = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Reserva r = new Reserva();
                r.setIdReserva(rs.getInt("idReserva"));
                r.setIdCliente(rs.getInt("idCliente"));
                r.setIdHabitacion(rs.getInt("idHabitacion"));
                r.setFechaInicio(rs.getDate("fechaInicio"));
                r.setFechaFin(rs.getDate("fechaFin"));
                r.setFechaReserva(rs.getDate("fechaReserva"));

                EstadoReserva estado = EstadoReservaFactory.crearEstado(rs.getString("estado"));
                r.setEstadoActual(estado);

                return r;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar reserva por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Reserva> listarTodos() {
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
                r.setFechaReserva(rs.getDate("fechaReserva"));

                EstadoReserva estado = EstadoReservaFactory.crearEstado(rs.getString("estado"));
                r.setEstadoActual(estado);

                lista.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar reservas: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Reserva> listarReservasPagadas() {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT r.* FROM Reserva r INNER JOIN Pago p ON r.idReserva = p.idReserva";
        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Reserva r = new Reserva();
                r.setIdReserva(rs.getInt("idReserva"));
                r.setIdCliente(rs.getInt("idCliente"));
                r.setIdHabitacion(rs.getInt("idHabitacion"));
                r.setFechaInicio(rs.getDate("fechaInicio"));
                r.setFechaFin(rs.getDate("fechaFin"));
                r.setFechaReserva(rs.getDate("fechaReserva"));

                EstadoReserva estado = EstadoReservaFactory.crearEstado(rs.getString("estado"));
                r.setEstadoActual(estado);

                lista.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar reservas pagadas: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Reserva reserva) {
        String sql = "UPDATE Reserva SET idCliente=?, idHabitacion=?, fechaInicio=?, fechaFin=?, estado=?, fechaReserva=? WHERE idReserva=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, reserva.getIdCliente());
            ps.setInt(2, reserva.getIdHabitacion());
            ps.setDate(3, new java.sql.Date(reserva.getFechaInicio().getTime()));
            ps.setDate(4, new java.sql.Date(reserva.getFechaFin().getTime()));
            ps.setString(5, reserva.getNombreEstado());
            ps.setDate(6, new java.sql.Date(reserva.getFechaReserva().getTime()));
            ps.setInt(7, reserva.getIdReserva());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar reserva: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(Integer idReserva) {
        String sql = "DELETE FROM Reserva WHERE idReserva=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar reserva: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String obtenerNombreClientePorId(int idCliente) {
        String nombre = "";
        String sql = "SELECT nombre FROM Cliente WHERE idCliente = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nombre = rs.getString("nombre");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener nombre de cliente: " + e.getMessage());
        }
        return nombre;
    }

    @Override
    public int obtenerNumeroHabitacion(int idHabitacion) {
        int numero = 0;
        String sql = "SELECT numero FROM Habitacion WHERE idHabitacion = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idHabitacion);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                numero = rs.getInt("numero");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener número de habitación: " + e.getMessage());
        }
        return numero;
    }
}