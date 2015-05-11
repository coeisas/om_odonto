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
public class FacPaqueteMedicamentoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_paquete", nullable = false)
    private int idPaquete;
    @Basic(optional = false)
    @Column(name = "id_medicamento", nullable = false)
    private int idMedicamento;

    public FacPaqueteMedicamentoPK() {
    }

    public FacPaqueteMedicamentoPK(int idPaquete, int idMedicamento) {
        this.idPaquete = idPaquete;
        this.idMedicamento = idMedicamento;
    }

    public int getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(int idPaquete) {
        this.idPaquete = idPaquete;
    }

    public int getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(int idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idPaquete;
        hash += (int) idMedicamento;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacPaqueteMedicamentoPK)) {
            return false;
        }
        FacPaqueteMedicamentoPK other = (FacPaqueteMedicamentoPK) object;
        if (this.idPaquete != other.idPaquete) {
            return false;
        }
        if (this.idMedicamento != other.idMedicamento) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.FacPaqueteMedicamentoPK[ idPaquete=" + idPaquete + ", idMedicamento=" + idMedicamento + " ]";
    }
    
}
