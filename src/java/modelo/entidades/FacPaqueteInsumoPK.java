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
public class FacPaqueteInsumoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_paquete", nullable = false)
    private int idPaquete;
    @Basic(optional = false)
    @Column(name = "id_insumo", nullable = false)
    private int idInsumo;

    public FacPaqueteInsumoPK() {
    }

    public FacPaqueteInsumoPK(int idPaquete, int idInsumo) {
        this.idPaquete = idPaquete;
        this.idInsumo = idInsumo;
    }

    public int getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(int idPaquete) {
        this.idPaquete = idPaquete;
    }

    public int getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(int idInsumo) {
        this.idInsumo = idInsumo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idPaquete;
        hash += (int) idInsumo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacPaqueteInsumoPK)) {
            return false;
        }
        FacPaqueteInsumoPK other = (FacPaqueteInsumoPK) object;
        if (this.idPaquete != other.idPaquete) {
            return false;
        }
        if (this.idInsumo != other.idInsumo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.FacPaqueteInsumoPK[ idPaquete=" + idPaquete + ", idInsumo=" + idInsumo + " ]";
    }
    
}
