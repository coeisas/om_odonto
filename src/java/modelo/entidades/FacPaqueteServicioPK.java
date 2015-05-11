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
public class FacPaqueteServicioPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_paquete", nullable = false)
    private int idPaquete;
    @Basic(optional = false)
    @Column(name = "id_servicio", nullable = false)
    private int idServicio;

    public FacPaqueteServicioPK() {
    }

    public FacPaqueteServicioPK(int idPaquete, int idServicio) {
        this.idPaquete = idPaquete;
        this.idServicio = idServicio;
    }

    public int getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(int idPaquete) {
        this.idPaquete = idPaquete;
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
        hash += (int) idPaquete;
        hash += (int) idServicio;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacPaqueteServicioPK)) {
            return false;
        }
        FacPaqueteServicioPK other = (FacPaqueteServicioPK) object;
        if (this.idPaquete != other.idPaquete) {
            return false;
        }
        if (this.idServicio != other.idServicio) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.FacPaqueteServicioPK[ idPaquete=" + idPaquete + ", idServicio=" + idServicio + " ]";
    }
    
}
