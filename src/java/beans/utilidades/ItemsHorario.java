/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.utilidades;

import java.util.Date;

/**
 *
 * @author mario
 */
public class ItemsHorario {

    private Short codDia;
    private String nomDia;
    private Date horaIni;
    private Date horaFin;

    /**
     * @return the codDia
     */
    public int getCodDia() {
        return codDia;
    }

    /**
     * @param codDia the codDia to set
     */
    public void setCodDia(Short codDia) {
        this.codDia = codDia;
        asignarNomDia(this.codDia);
    }

    private void asignarNomDia(Short dia) {
        switch (dia) {
            case 0: this.nomDia = "Domingo";break;
            case 1: this.nomDia = "Lunes";break;
            case 2: this.nomDia = "Martes";break;
            case 3: this.nomDia = "Miercoles";break;
            case 4: this.nomDia = "Jueves";break;
            case 5: this.nomDia = "Viernes";break;
            case 6: this.nomDia = "Sabado";break;
        }
    }

    /**
     * @return the horaIni
     */
    public Date getHoraIni() {
        return horaIni;
    }

    /**
     * @param horaIni the horaIni to set
     */
    public void setHoraIni(Date horaIni) {
        this.horaIni = horaIni;
    }

    /**
     * @return the horaFin
     */
    public Date getHoraFin() {
        return horaFin;
    }

    /**
     * @param horaFin the horaFin to set
     */
    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }

    /**
     * @return the nomDia
     */
    public String getNomDia() {
        return nomDia;
    }

}
