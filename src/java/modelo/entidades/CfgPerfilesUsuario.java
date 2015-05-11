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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "cfg_perfiles_usuario", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgPerfilesUsuario.findAll", query = "SELECT c FROM CfgPerfilesUsuario c"),
    @NamedQuery(name = "CfgPerfilesUsuario.findByIdPerfil", query = "SELECT c FROM CfgPerfilesUsuario c WHERE c.idPerfil = :idPerfil"),
    @NamedQuery(name = "CfgPerfilesUsuario.findByNombrePerfil", query = "SELECT c FROM CfgPerfilesUsuario c WHERE c.nombrePerfil = :nombrePerfil")})
public class CfgPerfilesUsuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_perfil", nullable = false)
    private Integer idPerfil;
    @Basic(optional = false)
    @Column(name = "nombre_perfil", nullable = false, length = 50)
    private String nombrePerfil;
    //@ManyToMany(mappedBy = "cfgPerfilesUsuarioList")    
    @JoinTable(name = "cfg_opciones_menu_perfil_usuario", joinColumns = {
        @JoinColumn(name = "id_perfil_usuario", referencedColumnName = "id_perfil", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "id_opcion_menu", referencedColumnName = "id_opcion_menu", nullable = false)})
    @ManyToMany
    private List<CfgOpcionesMenu> cfgOpcionesMenuList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPerfil")
    private List<CfgUsuarios> cfgUsuariosList;

    public CfgPerfilesUsuario() {
    }

    public CfgPerfilesUsuario(Integer idPerfil) {
        this.idPerfil = idPerfil;
    }

    public CfgPerfilesUsuario(Integer idPerfil, String nombrePerfil) {
        this.idPerfil = idPerfil;
        this.nombrePerfil = nombrePerfil;
    }

    public Integer getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Integer idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getNombrePerfil() {
        return nombrePerfil;
    }

    public void setNombrePerfil(String nombrePerfil) {
        this.nombrePerfil = nombrePerfil;
    }

    @XmlTransient
    public List<CfgOpcionesMenu> getCfgOpcionesMenuList() {
        return cfgOpcionesMenuList;
    }

    public void setCfgOpcionesMenuList(List<CfgOpcionesMenu> cfgOpcionesMenuList) {
        this.cfgOpcionesMenuList = cfgOpcionesMenuList;
    }

    @XmlTransient
    public List<CfgUsuarios> getCfgUsuariosList() {
        return cfgUsuariosList;
    }

    public void setCfgUsuariosList(List<CfgUsuarios> cfgUsuariosList) {
        this.cfgUsuariosList = cfgUsuariosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPerfil != null ? idPerfil.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgPerfilesUsuario)) {
            return false;
        }
        CfgPerfilesUsuario other = (CfgPerfilesUsuario) object;
        if ((this.idPerfil == null && other.idPerfil != null) || (this.idPerfil != null && !this.idPerfil.equals(other.idPerfil))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.CfgPerfilesUsuario[ idPerfil=" + idPerfil + " ]";
    }
    
}
