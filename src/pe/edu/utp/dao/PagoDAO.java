/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.utp.dao;

import pe.edu.utp.singleton.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import pe.edu.utp.modelo.Pago;

/**
 *
 * @author USUARIO
 */
public class PagoDAO {

    private final Connection conexion;

    public PagoDAO() {
        conexion = ConexionBD.getInstancia().getConexion();
    }

    public boolean agregar(Pago pago) {
        String sql = "INSERT INTO Pago (idReserva, monto, fecha, metodoPago) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, pago.getIdReserva());
            ps.setDouble(2, pago.getMonto());
            ps.setDate(3, new java.sql.Date(pago.getFecha().getTime()));
            ps.setString(4, pago.getMetodoPago());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar pago: " + e.getMessage());
            return false;
        }
    }

    public boolean existePagoParaReserva(int idReserva) {
        String sql = "SELECT 1 FROM Pago WHERE idReserva = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error al verificar pago existente: " + e.getMessage());
            return false;
        }
    }

    public List<Pago> listarTodos() {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM Pago";
        try (Statement st = conexion.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Pago p = new Pago();
                p.setIdPago(rs.getInt("idPago"));
                p.setIdReserva(rs.getInt("idReserva"));
                p.setMonto(rs.getDouble("monto"));
                p.setFecha(rs.getDate("fecha"));
                p.setMetodoPago(rs.getString("metodoPago"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pagos: " + e.getMessage());
        }
        return lista;
    }
}
