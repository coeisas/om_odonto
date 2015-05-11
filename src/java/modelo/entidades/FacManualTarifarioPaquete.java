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
@Table(name = "fac_manual_tarifario_paquete", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacManualTarifarioPaquete.findAll", query = "SELECT f FROM FacManualTarifarioPaquete f"),
    @NamedQuery(name = "FacManualTarifarioPaquete.findByIdManualTarifario", query = "SELECT f FROM FacManualTarifarioPaquete f WHERE f.facManualTarifarioPaquetePK.idManualTarifario = :idManualTarifario"),
    @NamedQuery(name = "FacManualTarifarioPaquete.findByIdPaquete", query = "SELECT f FROM FacManualTarifarioPaquete f WHERE f.facManualTarifarioPaquetePK.idPaquete = :idPaquete"),
    @NamedQuery(name = "FacManualTarifarioPaquete.findByDescuento", query = "SELECT f FROM FacManualTarifarioPaquete f WHERE f.descuento = :descuento"),
    @NamedQuery(name = "FacManualTarifarioPaquete.findByObservacion", query = "SELECT f FROM FacManualTarifarioPaquete f WHERE f.observacion = :observacion"),
    @NamedQuery(name = "FacManualTarifarioPaquete.findByActivo", query = "SELECT f FROM FacManualTarifarioPaquete f WHERE f.activo = :activo"),
    @NamedQuery(name = "FacManualTarifarioPaquete.findByValorInicial", query = "SELECT f FROM FacManualTarifarioPaquete f WHERE f.valorInicial = :valorInicial"),
    @NamedQuery(name = "FacManualTarifarioPaquete.findByValorFinal", query = "SELECT f FROM FacManualTarifarioPaquete f WHERE f.valorFinal = :valorFinal")})
public class FacManualTarifarioPaquete implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FacManualTarifarioPaquetePK facManualTarifarioPaquetePK;
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
    @JoinColumn(name = "id_paquete", referencedColumnName = "id_paquete", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private FacPaquete facPaquete;
    @JoinColumn(name = "id_manual_tarifario", referencedColumnName = "id_manual_tarifario", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private FacManualTarifario facManualTarifario;

    public FacManualTarifarioPaquete() {
    }

    public FacManualTarifarioPaquete(FacManualTarifarioPaquetePK facManualTarifarioPaquetePK) {
        this.facManualTarifarioPaquetePK = facManualTarifarioPaquetePK;
    }

    public FacManualTarifarioPaquete(int idManualTarifario, int idPaquete) {
        this.facManualTarifarioPaquetePK = new FacManualTarifarioPaquetePK(idManualTarifario, idPaquete);
    }

    public FacManualTarifarioPaquetePK getFacManualTarifarioPaquetePK() {
        return facManualTarifarioPaquetePK;
    }

    public void setFacManualTarifarioPaquetePK(FacManualTarifarioPaquetePK facManualTarifarioPaquetePK) {
        this.facManualTarifarioPaquetePK = facManualTarifarioPaquetePK;
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

    public FacPaquete getFacPaquete() {
        return facPaquete;
    }

    public void setFacPaquete(FacPaquete facPaquete) {
        this.facPaquete = facPaquete;
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
        hash += (facManualTarifarioPaquetePK != null ? facManualTarifarioPaquetePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacManualTarifarioPaquete)) {
            return false;
        }
        FacManualTarifarioPaquete other = (FacManualTarifarioPaquete) object;
        if ((this.facManualTarifarioPaquetePK == null && other.facManualTarifarioPaquetePK != null) || (this.facManualTarifarioPaquetePK != null && !this.facManualTarifarioPaquetePK.equals(other.facManualTarifarioPaquetePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.FacManualTarifarioPaquete[ facManualTarifarioPaquetePK=" + facManualTarifarioPaquetePK + " ]";
    }
    
}
