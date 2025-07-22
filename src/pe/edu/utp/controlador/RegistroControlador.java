/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.utp.controlador;

import java.sql.Connection;
import pe.edu.utp.daoimpl.UsuarioDAOImpl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.mindrot.jbcrypt.BCrypt;
import pe.edu.utp.dao.UsuarioDAO;
import pe.edu.utp.decorator.UsuarioDAOLogger;
import pe.edu.utp.decorator.UsuarioDAONotificadorCorreo;
import pe.edu.utp.decorator.UsuarioDAONotificadorSonido;
import pe.edu.utp.decorator.UsuarioDAOValidador;
import pe.edu.utp.modelo.Usuario;
import pe.edu.utp.singleton.ConexionBD;
import pe.edu.utp.vista.LoginVista;

/**
 *
 * @author USUARIO
 */
public class RegistroControlador implements ActionListener {

    private final LoginVista vista;
    private final UsuarioDAO usuarioDAO;

    public RegistroControlador(LoginVista vista) {
        this.vista = vista;

        Connection conexion = ConexionBD.getInstancia().getConexion();

        UsuarioDAO base = new UsuarioDAOImpl(conexion);
        UsuarioDAO conLogger = new UsuarioDAOLogger(base);
        UsuarioDAO conValidador = new UsuarioDAOValidador(conLogger);
        UsuarioDAO conNotificadorCorreo = new UsuarioDAONotificadorCorreo(conValidador);
        UsuarioDAO conNotificadorSonido = new UsuarioDAONotificadorSonido(conNotificadorCorreo);

        this.usuarioDAO = conNotificadorSonido;

        vista.getBtnRegistrar().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnRegistrar()) {
            String nombre = vista.getTxtNombre().getText().trim();
            String passPlano = new String(vista.getTxtPassword().getPassword()).trim();
            String confirm = new String(vista.getTxtConfirmPassword().getPassword()).trim();

            if (!passPlano.equals(confirm)) {
                JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden.");
                return;
            }

            if (!passPlano.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{6,30}$")) {
                JOptionPane.showMessageDialog(null, "La contraseña debe tener letras y números, mínimo 6 caracteres.");
                return;
            }

            String passHash = BCrypt.hashpw(passPlano, BCrypt.gensalt());
            Usuario usuario = new Usuario(nombre, passHash);

            boolean registrado = usuarioDAO.agregar(usuario);

            if (registrado) {
                limpiarCampos();
                vista.getTabbedFormularios().setSelectedIndex(
                        vista.getTabbedFormularios().indexOfComponent(vista.getPanelLogin())
                );
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo registrar el usuario.");
            }
        }
    }

    public void limpiarCampos() {
        vista.getTxtNombre().setText("");
        vista.getTxtPassword().setText("");
        vista.getTxtConfirmPassword().setText("");
    }
}
