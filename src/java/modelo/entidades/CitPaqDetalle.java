/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author santos
 */
@Entity
@Table(name = "cit_paq_detalle", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CitPaqDetalle.findAll", query = "SELECT c FROM CitPaqDetalle c"),
    @NamedQuery(name = "CitPaqDetalle.findByIdPaqDetalle", query = "SELECT c FROM CitPaqDetalle c WHERE c.idPaqDetalle = :idPaqDetalle")})
public class CitPaqDetalle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_paq_detalle", nullable = false)
    private Integer idPaqDetalle;
    @JoinColumn(name = "id_servicio", referencedColumnName = "id_servicio", nullable = false)
    @ManyToOne(optional = false)
    private FacServicio idServicio;
    @JoinColumn(name = "id_paq_maestro", referencedColumnName = "id_paq_maestro", nullable = false)
    @ManyToOne(optional = false)
    private CitPaqMaestro idPaqMaestro;
    @JoinColumn(name = "id_prestador", referencedColumnName = "id_usuario", nullable = false)
    @ManyToOne(optional = false)
    private CfgUsuarios idPrestador;

    public CitPaqDetalle() {
    }

    public CitPaqDetalle(Integer idPaqDetalle) {
        this.idPaqDetalle = idPaqDetalle;
    }

    public Integer getIdPaqDetalle() {
        return idPaqDetalle;
    }

    public void setIdPaqDetalle(Integer idPaqDetalle) {
        this.idPaqDetalle = idPaqDetalle;
    }

    public FacServicio getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(FacServicio idServicio) {
        this.idServicio = idServicio;
    }

    public CitPaqMaestro getIdPaqMaestro() {
        return idPaqMaestro;
    }

    public void setIdPaqMaestro(CitPaqMaestro idPaqMaestro) {
        this.idPaqMaestro = idPaqMaestro;
    }

    public CfgUsuarios getIdPrestador() {
        return idPrestador;
    }

    public void setIdPrestador(CfgUsuarios idPrestador) {
        this.idPrestador = idPrestador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPaqDetalle != null ? idPaqDetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CitPaqDetalle)) {
            return false;
        }
        CitPaqDetalle other = (CitPaqDetalle) object;
        if ((this.idPaqDetalle == null && other.idPaqDetalle != null) || (this.idPaqDetalle != null && !this.idPaqDetalle.equals(other.idPaqDetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.CitPaqDetalle[ idPaqDetalle=" + idPaqDetalle + " ]";
    }
    
}
