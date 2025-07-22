/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.utp.controlador;

import java.sql.Connection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import pe.edu.utp.dao.UsuarioDAO;
import pe.edu.utp.daoimpl.UsuarioDAOImpl;
import pe.edu.utp.decorator.UsuarioDAOLogger;
import pe.edu.utp.estilo.Estilos;
import pe.edu.utp.estilo.PaletaColores;
import pe.edu.utp.servicio.ServicioAutenticacion;
import pe.edu.utp.singleton.ConexionBD;
import pe.edu.utp.vista.LoginVista;
import pe.edu.utp.vista.PrincipalVista;

/**
 *
 * @author USUARIO
 */
public class LoginControlador implements ActionListener {

    private final LoginVista vista;
    private final ServicioAutenticacion servicio;

    public LoginControlador(LoginVista vista) {
        this.vista = vista;

        Connection conexion = ConexionBD.getInstancia().getConexion();
        UsuarioDAO daoBase = new UsuarioDAOImpl(conexion);
        UsuarioDAO usuarioDAO = new UsuarioDAOLogger(daoBase);

        this.servicio = new ServicioAutenticacion(usuarioDAO);

        aplicarEstilos();
        agregarAcciones();

        new RegistroControlador(vista);
    }

    private void agregarAcciones() {
        vista.getBtnCerrar().addActionListener(this);
        vista.getBtnIngresar().addActionListener(this);
        vista.getBtnRegistro().addActionListener(this);
        vista.getBtnSalir().addActionListener(this);
        vista.getBtnVolver().addActionListener(this);
    }

    private void aplicarEstilos() {
        Estilos.panelPrincipal(vista.getPanelPrincipal());
        Estilos.panelContenido(vista.getPanelInfo());
        Estilos.panelContenido(vista.getPanelLogin());
        Estilos.panelContenido(vista.getPanelRegistro());
        Estilos.panelSeccion(vista.getPanelImagen());
        Estilos.panelTitulo(vista.getPanelLogo());

        aplicarEstiloBotones();
        aplicarEstiloCamposTexto();
        aplicarEstiloLabels();
        aplicarEstiloTabbedPaneYScroll();
    }

    private void aplicarEstiloBotones() {
        Estilos.botonFormulario(vista.getBtnIngresar(), PaletaColores.PRIMARIO_CLARO);
        Estilos.botonFormulario(vista.getBtnRegistrar(), PaletaColores.PRIMARIO_OSCURO);
        Estilos.botonFormulario(vista.getBtnCerrar(), PaletaColores.BOTON_ROJO);
        Estilos.botonFormulario(vista.getBtnSalir(), PaletaColores.BOTON_ROJO);
        Estilos.botonFormulario(vista.getBtnVolver(), PaletaColores.BOTON_GRIS);
        Estilos.botonFormulario(vista.getBtnRegistro(), PaletaColores.BOTON_GRIS);
    }

    private void aplicarEstiloCamposTexto() {
        Estilos.personalizarCampoTexto(vista.getTxtNombre());
        Estilos.personalizarCampoTexto(vista.getTxtNombreLogin());
        Estilos.personalizarCampoTexto(vista.getTxtPassword());
        Estilos.personalizarCampoTexto(vista.getTxtPasswordLogin());
        Estilos.personalizarCampoTexto(vista.getTxtConfirmPassword());
        Estilos.personalizarTextArea(vista.getTxtDescripcion());
    }

    private void aplicarEstiloLabels() {
        Estilos.labelFormulario(vista.getLblNombre());
        Estilos.labelFormulario(vista.getLblPassword());
        Estilos.labelFormulario(vista.getLblNombreLogin());
        Estilos.labelFormulario(vista.getLblPasswordLogin());
        Estilos.labelFormulario(vista.getLblConfirmPassword());

        vista.getLblImgUser().setOpaque(true);
        vista.getLblImgUser().setBackground(PaletaColores.FONDO_CLARO);
    }

    private void aplicarEstiloTabbedPaneYScroll() {
        Estilos.estilizarTabbedPane(vista.getTabbedFormularios());

        JScrollPane scroll = vista.getScrollTxtDescripcion();

        scroll.setBorder(BorderFactory.createLineBorder(PaletaColores.CAMPO_BORDE));
        scroll.getViewport().setBackground(PaletaColores.FONDO_CLARO);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object fuente = e.getSource();

        if (fuente == vista.getBtnIngresar()) {
            procesarLogin();
        } else if (fuente == vista.getBtnRegistro()) {
            int index = vista.getTabbedFormularios().indexOfComponent(vista.getPanelRegistro());
            vista.getTabbedFormularios().setSelectedIndex(index);

            vista.getTxtNombre().setText("");
            vista.getTxtPassword().setText("");
            vista.getTxtConfirmPassword().setText("");

        } else if (fuente == vista.getBtnVolver()) {
            int index = vista.getTabbedFormularios().indexOfComponent(vista.getPanelLogin());
            vista.getTabbedFormularios().setSelectedIndex(index);

        } else if (fuente == vista.getBtnSalir() || fuente == vista.getBtnCerrar()) {
            System.exit(0);
        }
    }

    private void procesarLogin() {
        String nombreUsuario = vista.getTxtNombreLogin().getText().trim();
        String password = new String(vista.getTxtPasswordLogin().getPassword()).trim();

        if (nombreUsuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar usuario y contraseña.");
            return;
        }

        boolean acceso = servicio.autenticar(nombreUsuario, password);
        if (acceso) {
            JOptionPane.showMessageDialog(null, "Acceso correcto. Bienvenido " + nombreUsuario);
            vista.dispose();

            PrincipalVista vista = new PrincipalVista();
            new PrincipalControlador(vista);
            vista.getLblUsuarioActivo().setText("Usuario : " + nombreUsuario);
            vista.setLocationRelativeTo(null);
            vista.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos.");
        }
    }
}
