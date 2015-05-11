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
@Table(name = "cfg_imagenes", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgImagenes.findAll", query = "SELECT c FROM CfgImagenes c"),
    @NamedQuery(name = "CfgImagenes.findById", query = "SELECT c FROM CfgImagenes c WHERE c.id = :id"),
    @NamedQuery(name = "CfgImagenes.findByNombre", query = "SELECT c FROM CfgImagenes c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "CfgImagenes.findByNombreEnServidor", query = "SELECT c FROM CfgImagenes c WHERE c.nombreEnServidor = :nombreEnServidor"),
    @NamedQuery(name = "CfgImagenes.findByUrlImagen", query = "SELECT c FROM CfgImagenes c WHERE c.urlImagen = :urlImagen")})
public class CfgImagenes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "nombre", length = 2147483647)
    private String nombre;
    @Column(name = "nombre_en_servidor", length = 2147483647)
    private String nombreEnServidor;
    @Column(name = "url_imagen", length = 2147483647)
    private String urlImagen;
    @OneToMany(mappedBy = "firma")
    private List<CfgPacientes> cfgPacientesList;
    @OneToMany(mappedBy = "foto")
    private List<CfgPacientes> cfgPacientesList1;
    @OneToMany(mappedBy = "logo")
    private List<CfgEmpresa> cfgEmpresaList;
    @OneToMany(mappedBy = "foto")
    private List<CfgUsuarios> cfgUsuariosList;
    @OneToMany(mappedBy = "firma")
    private List<CfgUsuarios> cfgUsuariosList1;

    public CfgImagenes() {
    }

    public CfgImagenes(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreEnServidor() {
        return nombreEnServidor;
    }

    public void setNombreEnServidor(String nombreEnServidor) {
        this.nombreEnServidor = nombreEnServidor;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    @XmlTransient
    public List<CfgPacientes> getCfgPacientesList() {
        return cfgPacientesList;
    }

    public void setCfgPacientesList(List<CfgPacientes> cfgPacientesList) {
        this.cfgPacientesList = cfgPacientesList;
    }

    @XmlTransient
    public List<CfgPacientes> getCfgPacientesList1() {
        return cfgPacientesList1;
    }

    public void setCfgPacientesList1(List<CfgPacientes> cfgPacientesList1) {
        this.cfgPacientesList1 = cfgPacientesList1;
    }

    @XmlTransient
    public List<CfgEmpresa> getCfgEmpresaList() {
        return cfgEmpresaList;
    }

    public void setCfgEmpresaList(List<CfgEmpresa> cfgEmpresaList) {
        this.cfgEmpresaList = cfgEmpresaList;
    }

    @XmlTransient
    public List<CfgUsuarios> getCfgUsuariosList() {
        return cfgUsuariosList;
    }

    public void setCfgUsuariosList(List<CfgUsuarios> cfgUsuariosList) {
        this.cfgUsuariosList = cfgUsuariosList;
    }

    @XmlTransient
    public List<CfgUsuarios> getCfgUsuariosList1() {
        return cfgUsuariosList1;
    }

    public void setCfgUsuariosList1(List<CfgUsuarios> cfgUsuariosList1) {
        this.cfgUsuariosList1 = cfgUsuariosList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgImagenes)) {
            return false;
        }
        CfgImagenes other = (CfgImagenes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.CfgImagenes[ id=" + id + " ]";
    }
    
}
