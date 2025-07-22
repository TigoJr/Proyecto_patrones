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
import pe.edu.utp.dao.ClienteDAO;
import pe.edu.utp.modelo.Cliente;

/**
 *
 * @author USUARIO
 */
public class ClienteDAOImpl implements ClienteDAO {

    private final Connection conexion;

    public ClienteDAOImpl() {
        this.conexion = ConexionBD.getInstancia().getConexion();
    }

    @Override
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

    @Override
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

    @Override
    public boolean eliminar(Integer idCliente) {
        String sql = "DELETE FROM Cliente WHERE idCliente=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Cliente buscarPorId(Integer idCliente) {
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
            System.err.println("Error al buscar cliente por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
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

    @Override
    public boolean existePorDni(String dni) {
        return existe("dni", dni);
    }

    @Override
    public boolean existePorEmail(String email) {
        return existe("email", email);
    }

    @Override
    public boolean existePorTelefono(String telefono) {
        return existe("telefono", telefono);
    }

    private boolean existe(String campo, String valor) {
        String sql = "SELECT COUNT(*) FROM Cliente WHERE " + campo + " = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, valor);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia por " + campo + ": " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean existePorDniEnOtro(String dni, int idActual) {
        return existeEnOtro("dni", dni, idActual);
    }

    @Override
    public boolean existePorEmailEnOtro(String email, int idActual) {
        return existeEnOtro("email", email, idActual);
    }

    @Override
    public boolean existePorTelefonoEnOtro(String telefono, int idActual) {
        return existeEnOtro("telefono", telefono, idActual);
    }

    private boolean existeEnOtro(String campo, String valor, int idActual) {
        String sql = "SELECT COUNT(*) FROM Cliente WHERE " + campo + " = ? AND idCliente <> ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, valor);
            ps.setInt(2, idActual);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error al verificar duplicado en otro cliente (" + campo + "): " + e.getMessage());
        }
        return false;
    }
}