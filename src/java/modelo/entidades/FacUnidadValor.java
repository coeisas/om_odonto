/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author santos
 */
@Entity
@Table(name = "fac_unidad_valor", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacUnidadValor.findAll", query = "SELECT f FROM FacUnidadValor f"),
    @NamedQuery(name = "FacUnidadValor.findByAnio", query = "SELECT f FROM FacUnidadValor f WHERE f.anio = :anio"),
    @NamedQuery(name = "FacUnidadValor.findBySmlvd", query = "SELECT f FROM FacUnidadValor f WHERE f.smlvd = :smlvd"),
    @NamedQuery(name = "FacUnidadValor.findByUvr", query = "SELECT f FROM FacUnidadValor f WHERE f.uvr = :uvr")})
public class FacUnidadValor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "anio", nullable = false)
    private Integer anio;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "smlvd", precision = 17, scale = 17)
    private Double smlvd;
    @Column(name = "uvr", precision = 17, scale = 17)
    private Double uvr;
    @OneToMany(mappedBy = "anioUnidadValor")
    private List<FacManualTarifarioServicio> facManualTarifarioServicioList;

    public FacUnidadValor() {
    }

    public FacUnidadValor(Integer anio) {
        this.anio = anio;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Double getSmlvd() {
        return smlvd;
    }

    public void setSmlvd(Double smlvd) {
        this.smlvd = smlvd;
    }

    public Double getUvr() {
        return uvr;
    }

    public void setUvr(Double uvr) {
        this.uvr = uvr;
    }

    @XmlTransient
    public List<FacManualTarifarioServicio> getFacManualTarifarioServicioList() {
        return facManualTarifarioServicioList;
    }

    public void setFacManualTarifarioServicioList(List<FacManualTarifarioServicio> facManualTarifarioServicioList) {
        this.facManualTarifarioServicioList = facManualTarifarioServicioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (anio != null ? anio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacUnidadValor)) {
            return false;
        }
        FacUnidadValor other = (FacUnidadValor) object;
        if ((this.anio == null && other.anio != null) || (this.anio != null && !this.anio.equals(other.anio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.FacUnidadValor[ anio=" + anio + " ]";
    }
    
}
