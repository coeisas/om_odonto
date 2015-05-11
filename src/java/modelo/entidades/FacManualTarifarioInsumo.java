/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "fac_manual_tarifario_insumo", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacManualTarifarioInsumo.findAll", query = "SELECT f FROM FacManualTarifarioInsumo f"),
    @NamedQuery(name = "FacManualTarifarioInsumo.findByIdManualTarifario", query = "SELECT f FROM FacManualTarifarioInsumo f WHERE f.facManualTarifarioInsumoPK.idManualTarifario = :idManualTarifario"),
    @NamedQuery(name = "FacManualTarifarioInsumo.findByIdInsumo", query = "SELECT f FROM FacManualTarifarioInsumo f WHERE f.facManualTarifarioInsumoPK.idInsumo = :idInsumo"),
    @NamedQuery(name = "FacManualTarifarioInsumo.findByDescuento", query = "SELECT f FROM FacManualTarifarioInsumo f WHERE f.descuento = :descuento"),
    @NamedQuery(name = "FacManualTarifarioInsumo.findByObservacion", query = "SELECT f FROM FacManualTarifarioInsumo f WHERE f.observacion = :observacion"),
    @NamedQuery(name = "FacManualTarifarioInsumo.findByActivo", query = "SELECT f FROM FacManualTarifarioInsumo f WHERE f.activo = :activo"),
    @NamedQuery(name = "FacManualTarifarioInsumo.findByValorInicial", query = "SELECT f FROM FacManualTarifarioInsumo f WHERE f.valorInicial = :valorInicial"),
    @NamedQuery(name = "FacManualTarifarioInsumo.findByValorFinal", query = "SELECT f FROM FacManualTarifarioInsumo f WHERE f.valorFinal = :valorFinal")})
public class FacManualTarifarioInsumo implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FacManualTarifarioInsumoPK facManualTarifarioInsumoPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "descuento", precision = 17, scale = 17)
    private Double descuento;
    @Column(name = "observacion", length = 2147483647)
    private String observacion;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "valor_inicial", precision = 17, scale = 17)
    private Double valorInicial;
    @Column(name = "valor_final", precision = 17, scale = 17)
    private Double valorFinal;
    @JoinColumn(name = "id_manual_tarifario", referencedColumnName = "id_manual_tarifario", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private FacManualTarifario facManualTarifario;
    @JoinColumn(name = "id_insumo", referencedColumnName = "id_insumo", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CfgInsumo cfgInsumo;

    public FacManualTarifarioInsumo() {
    }

    public FacManualTarifarioInsumo(FacManualTarifarioInsumoPK facManualTarifarioInsumoPK) {
        this.facManualTarifarioInsumoPK = facManualTarifarioInsumoPK;
    }

    public FacManualTarifarioInsumo(int idManualTarifario, int idInsumo) {
        this.facManualTarifarioInsumoPK = new FacManualTarifarioInsumoPK(idManualTarifario, idInsumo);
    }

    public FacManualTarifarioInsumoPK getFacManualTarifarioInsumoPK() {
        return facManualTarifarioInsumoPK;
    }

    public void setFacManualTarifarioInsumoPK(FacManualTarifarioInsumoPK facManualTarifarioInsumoPK) {
        this.facManualTarifarioInsumoPK = facManualTarifarioInsumoPK;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Double getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(Double valorInicial) {
        this.valorInicial = valorInicial;
    }

    public Double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(Double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public FacManualTarifario getFacManualTarifario() {
        return facManualTarifario;
    }

    public void setFacManualTarifario(FacManualTarifario facManualTarifario) {
        this.facManualTarifario = facManualTarifario;
    }

    public CfgInsumo getCfgInsumo() {
        return cfgInsumo;
    }

    public void setCfgInsumo(CfgInsumo cfgInsumo) {
        this.cfgInsumo = cfgInsumo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facManualTarifarioInsumoPK != null ? facManualTarifarioInsumoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacManualTarifarioInsumo)) {
            return false;
        }
        FacManualTarifarioInsumo other = (FacManualTarifarioInsumo) object;
        if ((this.facManualTarifarioInsumoPK == null && other.facManualTarifarioInsumoPK != null) || (this.facManualTarifarioInsumoPK != null && !this.facManualTarifarioInsumoPK.equals(other.facManualTarifarioInsumoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.FacManualTarifarioInsumo[ facManualTarifarioInsumoPK=" + facManualTarifarioInsumoPK + " ]";
    }
    
}
