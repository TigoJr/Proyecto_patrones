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
import java.util.ArrayList;
import java.util.List;
import pe.edu.utp.dao.HabitacionDAO;
import pe.edu.utp.factory.EstadoHabitacionFactory;
import pe.edu.utp.modelo.Habitacion;
import pe.edu.utp.state.EstadoHabitacion;

/**
 *
 * @author USUARIO
 */
public class HabitacionDAOImpl implements HabitacionDAO {

    private final Connection conexion;

    public HabitacionDAOImpl() {
        this.conexion = ConexionBD.getInstancia().getConexion();
    }

    @Override
    public boolean agregar(Habitacion habitacion) {
        if (habitacion.getNumero() <= 0) {
            System.err.println("Número de habitación inválido.");
            return false;
        }

        if (existePorNumero(habitacion.getNumero())) {
            System.err.println("Ya existe una habitación con ese número.");
            return false;
        }

        String sql = "INSERT INTO Habitacion (numero, tipo, estado, precio) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, habitacion.getNumero());
            ps.setString(2, habitacion.getTipo());
            ps.setString(3, habitacion.getEstadoActual().getNombreEstado());
            ps.setDouble(4, habitacion.getPrecio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar habitación: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Habitacion habitacion) {
        if (habitacion.getNumero() <= 0) {
            System.err.println("Número de habitación inválido.");
            return false;
        }

        if (existePorNumeroEnOtro(habitacion.getNumero(), habitacion.getIdHabitacion())) {
            System.err.println("Ya existe otra habitación con ese número.");
            return false;
        }

        String sql = "UPDATE Habitacion SET numero=?, tipo=?, estado=?, precio=? WHERE idHabitacion=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, habitacion.getNumero());
            ps.setString(2, habitacion.getTipo());
            ps.setString(3, habitacion.getEstadoActual().getNombreEstado());
            ps.setDouble(4, habitacion.getPrecio());
            ps.setInt(5, habitacion.getIdHabitacion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar habitación: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(Integer idHabitacion) {
        String sql = "DELETE FROM Habitacion WHERE idHabitacion=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idHabitacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar habitación: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Habitacion buscarPorId(Integer idHabitacion) {
        String sql = "SELECT * FROM Habitacion WHERE idHabitacion = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idHabitacion);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Habitacion h = new Habitacion();
                h.setIdHabitacion(rs.getInt("idHabitacion"));
                h.setNumero(rs.getInt("numero"));
                h.setTipo(rs.getString("tipo"));
                h.setPrecio(rs.getDouble("precio"));

                String estadoTexto = rs.getString("estado");
                EstadoHabitacion estado = EstadoHabitacionFactory.crearEstado(estadoTexto);
                h.setEstadoActual(estado);

                return h;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar habitación por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Habitacion> listarTodos() {
        List<Habitacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM Habitacion";
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Habitacion h = new Habitacion();
                h.setIdHabitacion(rs.getInt("idHabitacion"));
                h.setNumero(rs.getInt("numero"));
                h.setTipo(rs.getString("tipo"));
                h.setPrecio(rs.getDouble("precio"));

                String estadoTexto = rs.getString("estado");
                EstadoHabitacion estado = EstadoHabitacionFactory.crearEstado(estadoTexto);
                h.setEstadoActual(estado);

                lista.add(h);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar habitaciones: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean existePorNumero(int numero) {
        String sql = "SELECT 1 FROM Habitacion WHERE numero = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, numero);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error al verificar número de habitación: " + e.getMessage());
            return true;
        }
    }

    @Override
    public boolean existePorNumeroEnOtro(int numero, int idHabitacion) {
        String sql = "SELECT COUNT(*) FROM Habitacion WHERE numero = ? AND idHabitacion <> ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, numero);
            ps.setInt(2, idHabitacion);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error al verificar duplicado en habitación: " + e.getMessage());
        }
        return false;
    }
}