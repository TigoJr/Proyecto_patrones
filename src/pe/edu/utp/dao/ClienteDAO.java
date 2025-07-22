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
import pe.edu.utp.modelo.Cliente;

/**
 *
 * @author USUARIO
 */
public class ClienteDAO {

    private final Connection conexion;

    public ClienteDAO() {
        conexion = ConexionBD.getInstancia().getConexion();
    }

    public boolean agregar(Cliente cliente) {
        String sql = "INSERT INTO Cliente (nombre, dni, telefono, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDni());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar cliente: " + e.getMessage());
            return false;
        }
    }

    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";
        try (Statement st = conexion.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("idCliente"));
                c.setNombre(rs.getString("nombre"));
                c.setDni(rs.getString("dni"));
                c.setTelefono(rs.getString("telefono"));
                c.setEmail(rs.getString("email"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Cliente cliente) {
        String sql = "UPDATE Cliente SET nombre=?, dni=?, telefono=?, email=? WHERE idCliente=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDni());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getEmail());
            ps.setInt(5, cliente.getIdCliente());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idCliente) {
        String sql = "DELETE FROM Cliente WHERE idCliente=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }

    public Cliente buscarPorId(int idCliente) {
        String sql = "SELECT * FROM Cliente WHERE idCliente=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Cliente(
                        rs.getInt("idCliente"),
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente: " + e.getMessage());
        }
        return null;
    }

    public boolean dniExiste(String dni) {
        return existe("dni", dni);
    }

    public boolean telefonoExiste(String telefono) {
        return existe("telefono", telefono);
    }

    public boolean emailExiste(String email) {
        return existe("email", email);
    }

    private boolean existe(String campo, String valor) {
        String sql = "SELECT COUNT(*) FROM cliente WHERE " + campo + " = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, valor);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean dniExisteEnOtroCliente(String dni, int idActual) {
        return existeEnOtro("dni", dni, idActual);
    }

    public boolean telefonoExisteEnOtroCliente(String telefono, int idActual) {
        return existeEnOtro("telefono", telefono, idActual);
    }

    public boolean emailExisteEnOtroCliente(String email, int idActual) {
        return existeEnOtro("email", email, idActual);
    }

    private boolean existeEnOtro(String campo, String valor, int idActual) {
        String sql = "SELECT COUNT(*) FROM cliente WHERE " + campo + " = ? AND idCliente <> ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, valor);
            ps.setInt(2, idActual);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
