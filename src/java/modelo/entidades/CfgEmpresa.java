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
@Table(name = "cfg_empresa", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgEmpresa.findAll", query = "SELECT c FROM CfgEmpresa c"),
    @NamedQuery(name = "CfgEmpresa.findByCodEmpresa", query = "SELECT c FROM CfgEmpresa c WHERE c.codEmpresa = :codEmpresa"),
    @NamedQuery(name = "CfgEmpresa.findByNumIdentificacion", query = "SELECT c FROM CfgEmpresa c WHERE c.numIdentificacion = :numIdentificacion"),
    @NamedQuery(name = "CfgEmpresa.findByDv", query = "SELECT c FROM CfgEmpresa c WHERE c.dv = :dv"),
    @NamedQuery(name = "CfgEmpresa.findByRazonSocial", query = "SELECT c FROM CfgEmpresa c WHERE c.razonSocial = :razonSocial"),
    @NamedQuery(name = "CfgEmpresa.findByNumDocRepLegal", query = "SELECT c FROM CfgEmpresa c WHERE c.numDocRepLegal = :numDocRepLegal"),
    @NamedQuery(name = "CfgEmpresa.findByNomRepLegal", query = "SELECT c FROM CfgEmpresa c WHERE c.nomRepLegal = :nomRepLegal"),
    @NamedQuery(name = "CfgEmpresa.findByDireccion", query = "SELECT c FROM CfgEmpresa c WHERE c.direccion = :direccion"),
    @NamedQuery(name = "CfgEmpresa.findByTelefono1", query = "SELECT c FROM CfgEmpresa c WHERE c.telefono1 = :telefono1"),
    @NamedQuery(name = "CfgEmpresa.findByTelefono2", query = "SELECT c FROM CfgEmpresa c WHERE c.telefono2 = :telefono2"),
    @NamedQuery(name = "CfgEmpresa.findByWebsite", query = "SELECT c FROM CfgEmpresa c WHERE c.website = :website"),
    @NamedQuery(name = "CfgEmpresa.findByObservaciones", query = "SELECT c FROM CfgEmpresa c WHERE c.observaciones = :observaciones"),
    @NamedQuery(name = "CfgEmpresa.findByCodigoEmpresa", query = "SELECT c FROM CfgEmpresa c WHERE c.codigoEmpresa = :codigoEmpresa"),
    @NamedQuery(name = "CfgEmpresa.findByRegimen", query = "SELECT c FROM CfgEmpresa c WHERE c.regimen = :regimen")})
public class CfgEmpresa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cod_empresa", nullable = false)
    private Integer codEmpresa;
    @Column(name = "num_identificacion", length = 20)
    private String numIdentificacion;
    @Column(name = "dv", length = 1)
    private String dv;
    @Column(name = "razon_social", length = 200)
    private String razonSocial;
    @Column(name = "num_doc_rep_legal", length = 20)
    private String numDocRepLegal;
    @Column(name = "nom_rep_legal", length = 200)
    private String nomRepLegal;
    @Column(name = "direccion", length = 200)
    private String direccion;
    @Column(name = "telefono_1", length = 10)
    private String telefono1;
    @Column(name = "telefono_2", length = 10)
    private String telefono2;
    @Column(name = "website", length = 100)
    private String website;
    @Column(name = "observaciones", length = 2147483647)
    private String observaciones;
    @Column(name = "codigo_empresa", length = 50)
    private String codigoEmpresa;
    @Column(name = "regimen", length = 50)
    private String regimen;
    @JoinColumn(name = "logo", referencedColumnName = "id")
    @ManyToOne
    private CfgImagenes logo;
    @JoinColumn(name = "tipo_doc_rep_legal", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones tipoDocRepLegal;
    @JoinColumn(name = "tipo_doc", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones tipoDoc;
    @JoinColumn(name = "cod_municipio", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones codMunicipio;
    @JoinColumn(name = "cod_departamento", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones codDepartamento;

    public CfgEmpresa() {
    }

    public CfgEmpresa(Integer codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public Integer getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(Integer codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public String getNumIdentificacion() {
        return numIdentificacion;
    }

    public void setNumIdentificacion(String numIdentificacion) {
        this.numIdentificacion = numIdentificacion;
    }

    public String getDv() {
        return dv;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNumDocRepLegal() {
        return numDocRepLegal;
    }

    public void setNumDocRepLegal(String numDocRepLegal) {
        this.numDocRepLegal = numDocRepLegal;
    }

    public String getNomRepLegal() {
        return nomRepLegal;
    }

    public void setNomRepLegal(String nomRepLegal) {
        this.nomRepLegal = nomRepLegal;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(String codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public String getRegimen() {
        return regimen;
    }

    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }

    public CfgImagenes getLogo() {
        return logo;
    }

    public void setLogo(CfgImagenes logo) {
        this.logo = logo;
    }

    public CfgClasificaciones getTipoDocRepLegal() {
        return tipoDocRepLegal;
    }

    public void setTipoDocRepLegal(CfgClasificaciones tipoDocRepLegal) {
        this.tipoDocRepLegal = tipoDocRepLegal;
    }

    public CfgClasificaciones getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(CfgClasificaciones tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public CfgClasificaciones getCodMunicipio() {
        return codMunicipio;
    }

    public void setCodMunicipio(CfgClasificaciones codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    public CfgClasificaciones getCodDepartamento() {
        return codDepartamento;
    }

    public void setCodDepartamento(CfgClasificaciones codDepartamento) {
        this.codDepartamento = codDepartamento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codEmpresa != null ? codEmpresa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgEmpresa)) {
            return false;
        }
        CfgEmpresa other = (CfgEmpresa) object;
        if ((this.codEmpresa == null && other.codEmpresa != null) || (this.codEmpresa != null && !this.codEmpresa.equals(other.codEmpresa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.CfgEmpresa[ codEmpresa=" + codEmpresa + " ]";
    }
    
}
