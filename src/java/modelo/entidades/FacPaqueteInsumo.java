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
@Table(name = "fac_paquete_insumo", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacPaqueteInsumo.findAll", query = "SELECT f FROM FacPaqueteInsumo f"),
    @NamedQuery(name = "FacPaqueteInsumo.findByIdPaquete", query = "SELECT f FROM FacPaqueteInsumo f WHERE f.facPaqueteInsumoPK.idPaquete = :idPaquete"),
    @NamedQuery(name = "FacPaqueteInsumo.findByIdInsumo", query = "SELECT f FROM FacPaqueteInsumo f WHERE f.facPaqueteInsumoPK.idInsumo = :idInsumo"),
    @NamedQuery(name = "FacPaqueteInsumo.findByValorFinal", query = "SELECT f FROM FacPaqueteInsumo f WHERE f.valorFinal = :valorFinal"),
    @NamedQuery(name = "FacPaqueteInsumo.findByDescuento", query = "SELECT f FROM FacPaqueteInsumo f WHERE f.descuento = :descuento"),
    @NamedQuery(name = "FacPaqueteInsumo.findByObservacion", query = "SELECT f FROM FacPaqueteInsumo f WHERE f.observacion = :observacion"),
    @NamedQuery(name = "FacPaqueteInsumo.findByActivo", query = "SELECT f FROM FacPaqueteInsumo f WHERE f.activo = :activo"),
    @NamedQuery(name = "FacPaqueteInsumo.findByCantidad", query = "SELECT f FROM FacPaqueteInsumo f WHERE f.cantidad = :cantidad"),
    @NamedQuery(name = "FacPaqueteInsumo.findByValorUnitario", query = "SELECT f FROM FacPaqueteInsumo f WHERE f.valorUnitario = :valorUnitario")})
public class FacPaqueteInsumo implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FacPaqueteInsumoPK facPaqueteInsumoPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_final", precision = 17, scale = 17)
    private Double valorFinal;
    @Column(name = "descuento", precision = 17, scale = 17)
    private Double descuento;
    @Column(name = "observacion", length = 2147483647)
    private String observacion;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Column(name = "valor_unitario", precision = 17, scale = 17)
    private Double valorUnitario;
    @JoinColumn(name = "id_paquete", referencedColumnName = "id_paquete", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private FacPaquete facPaquete;
    @JoinColumn(name = "id_insumo", referencedColumnName = "id_insumo", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CfgInsumo cfgInsumo;

    public FacPaqueteInsumo() {
    }

    public FacPaqueteInsumo(FacPaqueteInsumoPK facPaqueteInsumoPK) {
        this.facPaqueteInsumoPK = facPaqueteInsumoPK;
    }

    public FacPaqueteInsumo(int idPaquete, int idInsumo) {
        this.facPaqueteInsumoPK = new FacPaqueteInsumoPK(idPaquete, idInsumo);
    }

    public FacPaqueteInsumoPK getFacPaqueteInsumoPK() {
        return facPaqueteInsumoPK;
    }

    public void setFacPaqueteInsumoPK(FacPaqueteInsumoPK facPaqueteInsumoPK) {
        this.facPaqueteInsumoPK = facPaqueteInsumoPK;
    }

    public Double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(Double valorFinal) {
        this.valorFinal = valorFinal;
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

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public FacPaquete getFacPaquete() {
        return facPaquete;
    }

    public void setFacPaquete(FacPaquete facPaquete) {
        this.facPaquete = facPaquete;
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
        hash += (facPaqueteInsumoPK != null ? facPaqueteInsumoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacPaqueteInsumo)) {
            return false;
        }
        FacPaqueteInsumo other = (FacPaqueteInsumo) object;
        if ((this.facPaqueteInsumoPK == null && other.facPaqueteInsumoPK != null) || (this.facPaqueteInsumoPK != null && !this.facPaqueteInsumoPK.equals(other.facPaqueteInsumoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.FacPaqueteInsumo[ facPaqueteInsumoPK=" + facPaqueteInsumoPK + " ]";
    }
    
}
