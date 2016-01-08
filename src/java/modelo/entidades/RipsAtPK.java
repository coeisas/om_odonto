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
 * @author Mario
 */
@Embeddable
public class RipsAtPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_rip_almacenado", nullable = false)
    private int idRipAlmacenado;
    @Basic(optional = false)
    @Column(name = "num_registro", nullable = false)
    private int numRegistro;

    public RipsAtPK() {
    }

    public RipsAtPK(int idRipAlmacenado, int numRegistro) {
        this.idRipAlmacenado = idRipAlmacenado;
        this.numRegistro = numRegistro;
    }

    public int getIdRipAlmacenado() {
        return idRipAlmacenado;
    }

    public void setIdRipAlmacenado(int idRipAlmacenado) {
        this.idRipAlmacenado = idRipAlmacenado;
    }

    public int getNumRegistro() {
        return numRegistro;
    }

    public void setNumRegistro(int numRegistro) {
        this.numRegistro = numRegistro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idRipAlmacenado;
        hash += (int) numRegistro;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RipsAtPK)) {
            return false;
        }
        RipsAtPK other = (RipsAtPK) object;
        if (this.idRipAlmacenado != other.idRipAlmacenado) {
            return false;
        }
        if (this.numRegistro != other.numRegistro) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.RipsAtPK[ idRipAlmacenado=" + idRipAlmacenado + ", numRegistro=" + numRegistro + " ]";
    }
    
}
