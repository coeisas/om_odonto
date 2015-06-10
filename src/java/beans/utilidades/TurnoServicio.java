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
public class TurnoServicio {
    private int servicio;
    private int turno;

    public TurnoServicio(int servicio, int turno) {
        this.servicio = servicio;
        this.turno = turno;
    }

    public int getServicio() {
        return servicio;
    }

    public void setServicio(int servicio) {
        this.servicio = servicio;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }          
}
