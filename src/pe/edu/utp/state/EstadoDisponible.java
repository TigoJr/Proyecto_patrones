/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.utp.state;

/**
 *
 * @author USUARIO
 */
public class EstadoDisponible implements EstadoHabitacion {

    @Override
    public void mostrarEstado() {
        System.out.println("La habitación está disponible.");
    }

    @Override
    public String getNombreEstado() {
        return "Disponible";
    }
}
