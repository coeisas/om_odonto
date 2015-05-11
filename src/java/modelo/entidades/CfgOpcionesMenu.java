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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "cfg_opciones_menu", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgOpcionesMenu.findAll", query = "SELECT c FROM CfgOpcionesMenu c"),
    @NamedQuery(name = "CfgOpcionesMenu.findByIdOpcionMenu", query = "SELECT c FROM CfgOpcionesMenu c WHERE c.idOpcionMenu = :idOpcionMenu"),
    @NamedQuery(name = "CfgOpcionesMenu.findByNombreOpcion", query = "SELECT c FROM CfgOpcionesMenu c WHERE c.nombreOpcion = :nombreOpcion"),
    @NamedQuery(name = "CfgOpcionesMenu.findByStyle", query = "SELECT c FROM CfgOpcionesMenu c WHERE c.style = :style"),
    @NamedQuery(name = "CfgOpcionesMenu.findByUrlOpcion", query = "SELECT c FROM CfgOpcionesMenu c WHERE c.urlOpcion = :urlOpcion"),
    @NamedQuery(name = "CfgOpcionesMenu.findByOrden", query = "SELECT c FROM CfgOpcionesMenu c WHERE c.orden = :orden")})
public class CfgOpcionesMenu implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_opcion_menu", nullable = false)
    private Integer idOpcionMenu;
    @Basic(optional = false)
    @Column(name = "nombre_opcion", nullable = false, length = 40)
    private String nombreOpcion;
    @Column(name = "style", length = 100)
    private String style;
    @Column(name = "url_opcion", length = 100)
    private String urlOpcion;
    @Column(name = "orden")
    private Integer orden;
    //@JoinTable(name = "cfg_opciones_menu_perfil_usuario", joinColumns = {
    //    @JoinColumn(name = "id_opcion_menu", referencedColumnName = "id_opcion_menu", nullable = false)}, inverseJoinColumns = {
    //    @JoinColumn(name = "id_perfil_usuario", referencedColumnName = "id_perfil", nullable = false)})
    //@ManyToMany      
    @ManyToMany(mappedBy = "cfgOpcionesMenuList")    
    private List<CfgPerfilesUsuario> cfgPerfilesUsuarioList;
    @OneToMany(mappedBy = "idOpcionPadre")
    private List<CfgOpcionesMenu> cfgOpcionesMenuList;
    @JoinColumn(name = "id_opcion_padre", referencedColumnName = "id_opcion_menu")
    @ManyToOne
    private CfgOpcionesMenu idOpcionPadre;

    public CfgOpcionesMenu() {
    }

    public CfgOpcionesMenu(Integer idOpcionMenu) {
        this.idOpcionMenu = idOpcionMenu;
    }

    public CfgOpcionesMenu(Integer idOpcionMenu, String nombreOpcion) {
        this.idOpcionMenu = idOpcionMenu;
        this.nombreOpcion = nombreOpcion;
    }

    public Integer getIdOpcionMenu() {
        return idOpcionMenu;
    }

    public void setIdOpcionMenu(Integer idOpcionMenu) {
        this.idOpcionMenu = idOpcionMenu;
    }

    public String getNombreOpcion() {
        return nombreOpcion;
    }

    public void setNombreOpcion(String nombreOpcion) {
        this.nombreOpcion = nombreOpcion;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getUrlOpcion() {
        return urlOpcion;
    }

    public void setUrlOpcion(String urlOpcion) {
        this.urlOpcion = urlOpcion;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    @XmlTransient
    public List<CfgPerfilesUsuario> getCfgPerfilesUsuarioList() {
        return cfgPerfilesUsuarioList;
    }

    public void setCfgPerfilesUsuarioList(List<CfgPerfilesUsuario> cfgPerfilesUsuarioList) {
        this.cfgPerfilesUsuarioList = cfgPerfilesUsuarioList;
    }

    @XmlTransient
    public List<CfgOpcionesMenu> getCfgOpcionesMenuList() {
        return cfgOpcionesMenuList;
    }

    public void setCfgOpcionesMenuList(List<CfgOpcionesMenu> cfgOpcionesMenuList) {
        this.cfgOpcionesMenuList = cfgOpcionesMenuList;
    }

    public CfgOpcionesMenu getIdOpcionPadre() {
        return idOpcionPadre;
    }

    public void setIdOpcionPadre(CfgOpcionesMenu idOpcionPadre) {
        this.idOpcionPadre = idOpcionPadre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOpcionMenu != null ? idOpcionMenu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgOpcionesMenu)) {
            return false;
        }
        CfgOpcionesMenu other = (CfgOpcionesMenu) object;
        if ((this.idOpcionMenu == null && other.idOpcionMenu != null) || (this.idOpcionMenu != null && !this.idOpcionMenu.equals(other.idOpcionMenu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.CfgOpcionesMenu[ idOpcionMenu=" + idOpcionMenu + " ]";
    }
    
}
