/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author santos
 */
@Embeddable
public class FacManualTarifarioServicioPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_manual_tarifario", nullable = false)
    private int idManualTarifario;
    @Basic(optional = false)
    @Column(name = "id_servicio", nullable = false)
    private int idServicio;

    public FacManualTarifarioServicioPK() {
    }

    public FacManualTarifarioServicioPK(int idManualTarifario, int idServicio) {
        this.idManualTarifario = idManualTarifario;
        this.idServicio = idServicio;
    }

    public int getIdManualTarifario() {
        return idManualTarifario;
    }

    public void setIdManualTarifario(int idManualTarifario) {
        this.idManualTarifario = idManualTarifario;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idManualTarifario;
        hash += (int) idServicio;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacManualTarifarioServicioPK)) {
            return false;
        }
        FacManualTarifarioServicioPK other = (FacManualTarifarioServicioPK) object;
        if (this.idManualTarifario != other.idManualTarifario) {
            return false;
        }
        if (this.idServicio != other.idServicio) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.FacManualTarifarioServicioPK[ idManualTarifario=" + idManualTarifario + ", idServicio=" + idServicio + " ]";
    }
    
}
