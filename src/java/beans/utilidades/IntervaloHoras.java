/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.utilidades;

import java.util.Date;

/**
 *
 * @author Mario
 */
public class IntervaloHoras {
    private Date horaInicial;
    private Date horaFinal;

    public IntervaloHoras(Date horaInicial, Date horaFinal) {
        this.horaInicial = horaInicial;
        this.horaFinal = horaFinal;
    }

    public Date getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(Date horaInicial) {
        this.horaInicial = horaInicial;
    }

    public Date getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(Date horaFinal) {
        this.horaFinal = horaFinal;
    }
    
    
}
