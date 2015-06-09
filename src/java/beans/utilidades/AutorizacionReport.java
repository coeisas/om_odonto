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
public class AutorizacionReport {
    private int idAutorizacion;
    private String administradora;
    private String paciente;
    private String numAutorizacion;
    private boolean cerrada;
    private int idServicio;
    private boolean requiereAutorizacion;
    private String servicio;
    private int sesionesAutorizadas;
    private int sesionesRealizadas;
    private int sesionesPendientes;

    public AutorizacionReport() {
    }
    
    public AutorizacionReport(int idAutorizacion, String administradora, String paciente, String numAutorizacion, boolean cerrada, String servicio, int sesionesAutorizadas, int sesionesRealizadas, int sesionesPendientes) {
        this.idAutorizacion = idAutorizacion;
        this.administradora = administradora;
        this.paciente = paciente;
        this.numAutorizacion = numAutorizacion;
        this.cerrada = cerrada;
        this.servicio = servicio;
        this.sesionesAutorizadas = sesionesAutorizadas;
        this.sesionesRealizadas = sesionesRealizadas;
        this.sesionesPendientes = sesionesPendientes;
    }

    public int getIdAutorizacion() {
        return idAutorizacion;
    }

    public void setIdAutorizacion(int idAutorizacion) {
        this.idAutorizacion = idAutorizacion;
    }

    public String getAdministradora() {
        return administradora;
    }

    public void setAdministradora(String administradora) {
        this.administradora = administradora;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getNumAutorizacion() {
        return numAutorizacion;
    }

    public void setNumAutorizacion(String numAutorizacion) {
        this.numAutorizacion = numAutorizacion;
    }

    public boolean isCerrada() {
        return cerrada;
    }

    public void setCerrada(boolean cerrada) {
        this.cerrada = cerrada;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public int getSesionesAutorizadas() {
        return sesionesAutorizadas;
    }

    public void setSesionesAutorizadas(int sesionesAutorizadas) {
        this.sesionesAutorizadas = sesionesAutorizadas;
    }

    public int getSesionesRealizadas() {
        return sesionesRealizadas;
    }

    public void setSesionesRealizadas(int sesionesRealizadas) {
        this.sesionesRealizadas = sesionesRealizadas;
    }

    public int getSesionesPendientes() {
        return sesionesPendientes;
    }

    public void setSesionesPendientes(int sesionesPendientes) {
        this.sesionesPendientes = sesionesPendientes;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public boolean isRequiereAutorizacion() {
        return requiereAutorizacion;
    }

    public void setRequiereAutorizacion(boolean requiereAutorizacion) {
        this.requiereAutorizacion = requiereAutorizacion;
    }
    
}
