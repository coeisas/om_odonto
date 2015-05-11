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
@Table(name = "fac_manual_tarifario_servicio", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacManualTarifarioServicio.findAll", query = "SELECT f FROM FacManualTarifarioServicio f"),
    @NamedQuery(name = "FacManualTarifarioServicio.findByIdManualTarifario", query = "SELECT f FROM FacManualTarifarioServicio f WHERE f.facManualTarifarioServicioPK.idManualTarifario = :idManualTarifario"),
    @NamedQuery(name = "FacManualTarifarioServicio.findByIdServicio", query = "SELECT f FROM FacManualTarifarioServicio f WHERE f.facManualTarifarioServicioPK.idServicio = :idServicio"),
    @NamedQuery(name = "FacManualTarifarioServicio.findByMetaCumplimiento", query = "SELECT f FROM FacManualTarifarioServicio f WHERE f.metaCumplimiento = :metaCumplimiento"),
    @NamedQuery(name = "FacManualTarifarioServicio.findByPeriodicidad", query = "SELECT f FROM FacManualTarifarioServicio f WHERE f.periodicidad = :periodicidad"),
    @NamedQuery(name = "FacManualTarifarioServicio.findByDescuento", query = "SELECT f FROM FacManualTarifarioServicio f WHERE f.descuento = :descuento"),
    @NamedQuery(name = "FacManualTarifarioServicio.findByHonorarioMedico", query = "SELECT f FROM FacManualTarifarioServicio f WHERE f.honorarioMedico = :honorarioMedico"),
    @NamedQuery(name = "FacManualTarifarioServicio.findByObservacion", query = "SELECT f FROM FacManualTarifarioServicio f WHERE f.observacion = :observacion"),
    @NamedQuery(name = "FacManualTarifarioServicio.findByActivo", query = "SELECT f FROM FacManualTarifarioServicio f WHERE f.activo = :activo"),
    @NamedQuery(name = "FacManualTarifarioServicio.findByTipoTarifa", query = "SELECT f FROM FacManualTarifarioServicio f WHERE f.tipoTarifa = :tipoTarifa"),
    @NamedQuery(name = "FacManualTarifarioServicio.findByValorInicial", query = "SELECT f FROM FacManualTarifarioServicio f WHERE f.valorInicial = :valorInicial"),
    @NamedQuery(name = "FacManualTarifarioServicio.findByValorFinal", query = "SELECT f FROM FacManualTarifarioServicio f WHERE f.valorFinal = :valorFinal")})
public class FacManualTarifarioServicio implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FacManualTarifarioServicioPK facManualTarifarioServicioPK;
    @Column(name = "meta_cumplimiento")
    private Integer metaCumplimiento;
    @Column(name = "periodicidad")
    private Integer periodicidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "descuento", precision = 17, scale = 17)
    private Double descuento;
    @Column(name = "honorario_medico", precision = 17, scale = 17)
    private Double honorarioMedico;
    @Column(name = "observacion", length = 2147483647)
    private String observacion;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "tipo_tarifa", length = 11)
    private String tipoTarifa;
    @Column(name = "valor_inicial", precision = 17, scale = 17)
    private Double valorInicial;
    @Column(name = "valor_final", precision = 17, scale = 17)
    private Double valorFinal;
    @JoinColumn(name = "anio_unidad_valor", referencedColumnName = "anio")
    @ManyToOne
    private FacUnidadValor anioUnidadValor;
    @JoinColumn(name = "id_servicio", referencedColumnName = "id_servicio", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private FacServicio facServicio;
    @JoinColumn(name = "id_manual_tarifario", referencedColumnName = "id_manual_tarifario", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private FacManualTarifario facManualTarifario;

    public FacManualTarifarioServicio() {
    }

    public FacManualTarifarioServicio(FacManualTarifarioServicioPK facManualTarifarioServicioPK) {
        this.facManualTarifarioServicioPK = facManualTarifarioServicioPK;
    }

    public FacManualTarifarioServicio(int idManualTarifario, int idServicio) {
        this.facManualTarifarioServicioPK = new FacManualTarifarioServicioPK(idManualTarifario, idServicio);
    }

    public FacManualTarifarioServicioPK getFacManualTarifarioServicioPK() {
        return facManualTarifarioServicioPK;
    }

    public void setFacManualTarifarioServicioPK(FacManualTarifarioServicioPK facManualTarifarioServicioPK) {
        this.facManualTarifarioServicioPK = facManualTarifarioServicioPK;
    }

    public Integer getMetaCumplimiento() {
        return metaCumplimiento;
    }

    public void setMetaCumplimiento(Integer metaCumplimiento) {
        this.metaCumplimiento = metaCumplimiento;
    }

    public Integer getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(Integer periodicidad) {
        this.periodicidad = periodicidad;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getHonorarioMedico() {
        return honorarioMedico;
    }

    public void setHonorarioMedico(Double honorarioMedico) {
        this.honorarioMedico = honorarioMedico;
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

    public String getTipoTarifa() {
        return tipoTarifa;
    }

    public void setTipoTarifa(String tipoTarifa) {
        this.tipoTarifa = tipoTarifa;
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

    public FacUnidadValor getAnioUnidadValor() {
        return anioUnidadValor;
    }

    public void setAnioUnidadValor(FacUnidadValor anioUnidadValor) {
        this.anioUnidadValor = anioUnidadValor;
    }

    public FacServicio getFacServicio() {
        return facServicio;
    }

    public void setFacServicio(FacServicio facServicio) {
        this.facServicio = facServicio;
    }

    public FacManualTarifario getFacManualTarifario() {
        return facManualTarifario;
    }

    public void setFacManualTarifario(FacManualTarifario facManualTarifario) {
        this.facManualTarifario = facManualTarifario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facManualTarifarioServicioPK != null ? facManualTarifarioServicioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacManualTarifarioServicio)) {
            return false;
        }
        FacManualTarifarioServicio other = (FacManualTarifarioServicio) object;
        if ((this.facManualTarifarioServicioPK == null && other.facManualTarifarioServicioPK != null) || (this.facManualTarifarioServicioPK != null && !this.facManualTarifarioServicioPK.equals(other.facManualTarifarioServicioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.FacManualTarifarioServicio[ facManualTarifarioServicioPK=" + facManualTarifarioServicioPK + " ]";
    }
    
}
