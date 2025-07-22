/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.utp.factory;

import pe.edu.utp.modelo.Habitacion;
import pe.edu.utp.state.EstadoDisponible;

/**
 *
 * @author USUARIO
 */
public class TipoHabitacionFactory {

    public static Habitacion crearHabitacion(String tipo) {
        Habitacion h = new Habitacion();
        h.setTipo(tipo);
        h.setEstadoActual(new EstadoDisponible());

        switch (tipo.trim().toLowerCase()) {
            case "habitación twin" -> h.setPrecio(200.00);
            case "habitación twin superior" -> h.setPrecio(240.00);
            case "junior suite twin bed", "junior suite king" -> h.setPrecio(300.00);
            case "junior twin superior", "junior king superior" -> h.setPrecio(335.00);
            case "grand suite" -> h.setPrecio(580.00);
            default -> {
                h.setPrecio(0.00);
                h.setTipo("desconocido");
            }
        }

        return h;
    }
}

