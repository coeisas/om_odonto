/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "fac_paquete", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacPaquete.findAll", query = "SELECT f FROM FacPaquete f"),
    @NamedQuery(name = "FacPaquete.findByIdPaquete", query = "SELECT f FROM FacPaquete f WHERE f.idPaquete = :idPaquete"),
    @NamedQuery(name = "FacPaquete.findByCodigoPaquete", query = "SELECT f FROM FacPaquete f WHERE f.codigoPaquete = :codigoPaquete"),
    @NamedQuery(name = "FacPaquete.findByNombrePaquete", query = "SELECT f FROM FacPaquete f WHERE f.nombrePaquete = :nombrePaquete"),
    @NamedQuery(name = "FacPaquete.findByValorPaquete", query = "SELECT f FROM FacPaquete f WHERE f.valorPaquete = :valorPaquete"),
    @NamedQuery(name = "FacPaquete.findByValorCalculado", query = "SELECT f FROM FacPaquete f WHERE f.valorCalculado = :valorCalculado"),
    @NamedQuery(name = "FacPaquete.findByAplicaIva", query = "SELECT f FROM FacPaquete f WHERE f.aplicaIva = :aplicaIva"),
    @NamedQuery(name = "FacPaquete.findByAplicaCree", query = "SELECT f FROM FacPaquete f WHERE f.aplicaCree = :aplicaCree")})
public class FacPaquete implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_paquete", nullable = false)
    private Integer idPaquete;
    @Column(name = "codigo_paquete", length = 50)
    private String codigoPaquete;
    @Column(name = "nombre_paquete", length = 150)
    private String nombrePaquete;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_paquete", precision = 17, scale = 17)
    private Double valorPaquete;
    @Column(name = "valor_calculado", precision = 17, scale = 17)
    private Double valorCalculado;
    @Column(name = "aplica_iva")
    private Boolean aplicaIva;
    @Column(name = "aplica_cree")
    private Boolean aplicaCree;
    @OneToMany(mappedBy = "idPaquete")
    private List<FacFacturaPaquete> facFacturaPaqueteList;
    @OneToMany(mappedBy = "idPaquete")
    private List<FacConsumoPaquete> facConsumoPaqueteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facPaquete")
    private List<FacPaqueteMedicamento> facPaqueteMedicamentoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facPaquete")
    private List<FacManualTarifarioPaquete> facManualTarifarioPaqueteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facPaquete")
    private List<FacPaqueteInsumo> facPaqueteInsumoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facPaquete")
    private List<FacPaqueteServicio> facPaqueteServicioList;

    public FacPaquete() {
    }

    public FacPaquete(Integer idPaquete) {
        this.idPaquete = idPaquete;
    }

    public Integer getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(Integer idPaquete) {
        this.idPaquete = idPaquete;
    }

    public String getCodigoPaquete() {
        return codigoPaquete;
    }

    public void setCodigoPaquete(String codigoPaquete) {
        this.codigoPaquete = codigoPaquete;
    }

    public String getNombrePaquete() {
        return nombrePaquete;
    }

    public void setNombrePaquete(String nombrePaquete) {
        this.nombrePaquete = nombrePaquete;
    }

    public Double getValorPaquete() {
        return valorPaquete;
    }

    public void setValorPaquete(Double valorPaquete) {
        this.valorPaquete = valorPaquete;
    }

    public Double getValorCalculado() {
        return valorCalculado;
    }

    public void setValorCalculado(Double valorCalculado) {
        this.valorCalculado = valorCalculado;
    }

    public Boolean getAplicaIva() {
        return aplicaIva;
    }

    public void setAplicaIva(Boolean aplicaIva) {
        this.aplicaIva = aplicaIva;
    }

    public Boolean getAplicaCree() {
        return aplicaCree;
    }

    public void setAplicaCree(Boolean aplicaCree) {
        this.aplicaCree = aplicaCree;
    }

    @XmlTransient
    public List<FacFacturaPaquete> getFacFacturaPaqueteList() {
        return facFacturaPaqueteList;
    }

    public void setFacFacturaPaqueteList(List<FacFacturaPaquete> facFacturaPaqueteList) {
        this.facFacturaPaqueteList = facFacturaPaqueteList;
    }

    @XmlTransient
    public List<FacConsumoPaquete> getFacConsumoPaqueteList() {
        return facConsumoPaqueteList;
    }

    public void setFacConsumoPaqueteList(List<FacConsumoPaquete> facConsumoPaqueteList) {
        this.facConsumoPaqueteList = facConsumoPaqueteList;
    }

    @XmlTransient
    public List<FacPaqueteMedicamento> getFacPaqueteMedicamentoList() {
        return facPaqueteMedicamentoList;
    }

    public void setFacPaqueteMedicamentoList(List<FacPaqueteMedicamento> facPaqueteMedicamentoList) {
        this.facPaqueteMedicamentoList = facPaqueteMedicamentoList;
    }

    @XmlTransient
    public List<FacManualTarifarioPaquete> getFacManualTarifarioPaqueteList() {
        return facManualTarifarioPaqueteList;
    }

    public void setFacManualTarifarioPaqueteList(List<FacManualTarifarioPaquete> facManualTarifarioPaqueteList) {
        this.facManualTarifarioPaqueteList = facManualTarifarioPaqueteList;
    }

    @XmlTransient
    public List<FacPaqueteInsumo> getFacPaqueteInsumoList() {
        return facPaqueteInsumoList;
    }

    public void setFacPaqueteInsumoList(List<FacPaqueteInsumo> facPaqueteInsumoList) {
        this.facPaqueteInsumoList = facPaqueteInsumoList;
    }

    @XmlTransient
    public List<FacPaqueteServicio> getFacPaqueteServicioList() {
        return facPaqueteServicioList;
    }

    public void setFacPaqueteServicioList(List<FacPaqueteServicio> facPaqueteServicioList) {
        this.facPaqueteServicioList = facPaqueteServicioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPaquete != null ? idPaquete.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacPaquete)) {
            return false;
        }
        FacPaquete other = (FacPaquete) object;
        if ((this.idPaquete == null && other.idPaquete != null) || (this.idPaquete != null && !this.idPaquete.equals(other.idPaquete))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.FacPaquete[ idPaquete=" + idPaquete + " ]";
    }
    
}
