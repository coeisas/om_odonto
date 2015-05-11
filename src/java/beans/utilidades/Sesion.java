/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.utilidades;

/**
 *
 * @author santos
 */
public class Sesion {
    /*
    Cuando un usuario accede al sistema se registra el id de la sesion
    y el id del usuario en DB, esto se administra desde AplicacionGeneralMB
    */
    
    private String idSesion;
    private int idUsuario;

    public Sesion(String idSesion, int idUsuario) {
        this.idSesion = idSesion;
        this.idUsuario = idUsuario;
    }

    public String getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(String idSesion) {
        this.idSesion = idSesion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
}
