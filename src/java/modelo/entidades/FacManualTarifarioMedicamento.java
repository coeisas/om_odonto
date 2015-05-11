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
@Table(name = "fac_manual_tarifario_medicamento", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacManualTarifarioMedicamento.findAll", query = "SELECT f FROM FacManualTarifarioMedicamento f"),
    @NamedQuery(name = "FacManualTarifarioMedicamento.findByIdManualTarifario", query = "SELECT f FROM FacManualTarifarioMedicamento f WHERE f.facManualTarifarioMedicamentoPK.idManualTarifario = :idManualTarifario"),
    @NamedQuery(name = "FacManualTarifarioMedicamento.findByIdMedicamento", query = "SELECT f FROM FacManualTarifarioMedicamento f WHERE f.facManualTarifarioMedicamentoPK.idMedicamento = :idMedicamento"),
    @NamedQuery(name = "FacManualTarifarioMedicamento.findByDescuento", query = "SELECT f FROM FacManualTarifarioMedicamento f WHERE f.descuento = :descuento"),
    @NamedQuery(name = "FacManualTarifarioMedicamento.findByObservacion", query = "SELECT f FROM FacManualTarifarioMedicamento f WHERE f.observacion = :observacion"),
    @NamedQuery(name = "FacManualTarifarioMedicamento.findByActivo", query = "SELECT f FROM FacManualTarifarioMedicamento f WHERE f.activo = :activo"),
    @NamedQuery(name = "FacManualTarifarioMedicamento.findByValorInicial", query = "SELECT f FROM FacManualTarifarioMedicamento f WHERE f.valorInicial = :valorInicial"),
    @NamedQuery(name = "FacManualTarifarioMedicamento.findByValorFinal", query = "SELECT f FROM FacManualTarifarioMedicamento f WHERE f.valorFinal = :valorFinal")})
public class FacManualTarifarioMedicamento implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FacManualTarifarioMedicamentoPK facManualTarifarioMedicamentoPK;
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
    @JoinColumn(name = "id_medicamento", referencedColumnName = "id_medicamento", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CfgMedicamento cfgMedicamento;

    public FacManualTarifarioMedicamento() {
    }

    public FacManualTarifarioMedicamento(FacManualTarifarioMedicamentoPK facManualTarifarioMedicamentoPK) {
        this.facManualTarifarioMedicamentoPK = facManualTarifarioMedicamentoPK;
    }

    public FacManualTarifarioMedicamento(int idManualTarifario, int idMedicamento) {
        this.facManualTarifarioMedicamentoPK = new FacManualTarifarioMedicamentoPK(idManualTarifario, idMedicamento);
    }

    public FacManualTarifarioMedicamentoPK getFacManualTarifarioMedicamentoPK() {
        return facManualTarifarioMedicamentoPK;
    }

    public void setFacManualTarifarioMedicamentoPK(FacManualTarifarioMedicamentoPK facManualTarifarioMedicamentoPK) {
        this.facManualTarifarioMedicamentoPK = facManualTarifarioMedicamentoPK;
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

    public CfgMedicamento getCfgMedicamento() {
        return cfgMedicamento;
    }

    public void setCfgMedicamento(CfgMedicamento cfgMedicamento) {
        this.cfgMedicamento = cfgMedicamento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facManualTarifarioMedicamentoPK != null ? facManualTarifarioMedicamentoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacManualTarifarioMedicamento)) {
            return false;
        }
        FacManualTarifarioMedicamento other = (FacManualTarifarioMedicamento) object;
        if ((this.facManualTarifarioMedicamentoPK == null && other.facManualTarifarioMedicamentoPK != null) || (this.facManualTarifarioMedicamentoPK != null && !this.facManualTarifarioMedicamentoPK.equals(other.facManualTarifarioMedicamentoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.FacManualTarifarioMedicamento[ facManualTarifarioMedicamentoPK=" + facManualTarifarioMedicamentoPK + " ]";
    }
    
}
