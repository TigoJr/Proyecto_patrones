/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pe.edu.utp.dao;

/**
 *
 * @author USUARIO
 */
public interface ValidacionDuplicadosDAO {
    boolean existePorEnOtro(String campo, String valor, int idActual);
}