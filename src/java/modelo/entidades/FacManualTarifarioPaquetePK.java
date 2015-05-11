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
public class FacManualTarifarioPaquetePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_manual_tarifario", nullable = false)
    private int idManualTarifario;
    @Basic(optional = false)
    @Column(name = "id_paquete", nullable = false)
    private int idPaquete;

    public FacManualTarifarioPaquetePK() {
    }

    public FacManualTarifarioPaquetePK(int idManualTarifario, int idPaquete) {
        this.idManualTarifario = idManualTarifario;
        this.idPaquete = idPaquete;
    }

    public int getIdManualTarifario() {
        return idManualTarifario;
    }

    public void setIdManualTarifario(int idManualTarifario) {
        this.idManualTarifario = idManualTarifario;
    }

    public int getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(int idPaquete) {
        this.idPaquete = idPaquete;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idManualTarifario;
        hash += (int) idPaquete;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacManualTarifarioPaquetePK)) {
            return false;
        }
        FacManualTarifarioPaquetePK other = (FacManualTarifarioPaquetePK) object;
        if (this.idManualTarifario != other.idManualTarifario) {
            return false;
        }
        if (this.idPaquete != other.idPaquete) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.FacManualTarifarioPaquetePK[ idManualTarifario=" + idManualTarifario + ", idPaquete=" + idPaquete + " ]";
    }
    
}
