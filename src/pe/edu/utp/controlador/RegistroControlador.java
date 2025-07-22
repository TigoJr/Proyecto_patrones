/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.utp.controlador;


import java.sql.Connection;
import pe.edu.utp.daoimpl.UsuarioDAOImpl;
import pe.edu.utp.servicio.ServicioAutenticacion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import pe.edu.utp.singleton.ConexionBD;
import pe.edu.utp.vista.LoginVista;

/**
 *
 * @author USUARIO
 */

public class RegistroControlador implements ActionListener {

    private final LoginVista vista;
    private final ServicioAutenticacion servicio;

    public RegistroControlador(LoginVista vista) {
        this.vista = vista;

        Connection conexion = ConexionBD.getInstancia().getConexion();
        UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl(conexion);
        this.servicio = new ServicioAutenticacion(usuarioDAO);
        
        vista.getBtnRegistrar().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String nombreUsuario = vista.getTxtNombre().getText().trim();
        String password = new String(vista.getTxtPassword().getPassword()).trim();
        String confirmarPassword = new String(vista.getTxtConfirmPassword().getPassword()).trim();

        if (nombreUsuario.isEmpty() || password.isEmpty() || confirmarPassword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.");
            return;
        }

        if (!password.equals(confirmarPassword)) {
            JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden.");
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos 6 caracteres.");
            return;
        }

        boolean registrado = servicio.registrar(nombreUsuario, password);
        if (registrado) {
            JOptionPane.showMessageDialog(null, "Usuario registrado correctamente.");
            limpiarCampos();
            vista.getTabbedFormularios().setSelectedIndex(
                vista.getTabbedFormularios().indexOfComponent(vista.getPanelLogin())
            );
        } else {
            JOptionPane.showMessageDialog(null, "El nombre de usuario ya está en uso.");
        }
    }

    public void limpiarCampos() {
        vista.getTxtNombre().setText("");
        vista.getTxtPassword().setText("");
        vista.getTxtConfirmPassword().setText("");
    }
}
