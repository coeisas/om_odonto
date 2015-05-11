/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.utilidades;

/**
 *
 * @author mario
 */
public class CtrlSesionesAutorizadas {
    private int contadorSesiones;
    private int sesionesAutorizadas;
    private int idServicio;
    private boolean autorizacionRequerida;

    public CtrlSesionesAutorizadas() {
    }

    public CtrlSesionesAutorizadas(int idServicio, boolean autorizacionRequerida, int sesionesAutorizadas) {
        this.sesionesAutorizadas = sesionesAutorizadas;
        this.idServicio = idServicio;
        this.contadorSesiones = 0;
        this.autorizacionRequerida = autorizacionRequerida;
    }        

    public int getContadorSesiones() {
        return contadorSesiones;
    }

    public void setContadorSesiones(int contadorSesiones) {
        this.contadorSesiones = contadorSesiones;
    }

    public int getSesionesAutorizadas() {
        return sesionesAutorizadas;
    }

    public void setSesionesAutorizadas(int sesionesAutorizadas) {
        this.sesionesAutorizadas = sesionesAutorizadas;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public boolean isAutorizacionRequerida() {
        return autorizacionRequerida;
    }

    public void setAutorizacionRequerida(boolean autorizacionRequerida) {
        this.autorizacionRequerida = autorizacionRequerida;
    }
    
    
    
    
}
