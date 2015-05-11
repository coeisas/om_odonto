/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mario
 */
@Embeddable
public class CfgDiasNoLaboralesPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_sede", nullable = false)
    private int idSede;
    @Basic(optional = false)
    @Column(name = "fecha_no_laboral", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaNoLaboral;

    public CfgDiasNoLaboralesPK() {
    }

    public CfgDiasNoLaboralesPK(int idSede, Date fechaNoLaboral) {
        this.idSede = idSede;
        this.fechaNoLaboral = fechaNoLaboral;
    }

    public int getIdSede() {
        return idSede;
    }

    public void setIdSede(int idSede) {
        this.idSede = idSede;
    }

    public Date getFechaNoLaboral() {
        return fechaNoLaboral;
    }

    public void setFechaNoLaboral(Date fechaNoLaboral) {
        this.fechaNoLaboral = fechaNoLaboral;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idSede;
        hash += (fechaNoLaboral != null ? fechaNoLaboral.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgDiasNoLaboralesPK)) {
            return false;
        }
        CfgDiasNoLaboralesPK other = (CfgDiasNoLaboralesPK) object;
        if (this.idSede != other.idSede) {
            return false;
        }
        if ((this.fechaNoLaboral == null && other.fechaNoLaboral != null) || (this.fechaNoLaboral != null && !this.fechaNoLaboral.equals(other.fechaNoLaboral))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.CfgDiasNoLaboralesPK[ idSede=" + idSede + ", fechaNoLaboral=" + fechaNoLaboral + " ]";
    }
    
}
