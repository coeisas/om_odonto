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
@Table(name = "cfg_maestros_clasificaciones", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgMaestrosClasificaciones.findAll", query = "SELECT c FROM CfgMaestrosClasificaciones c"),
    @NamedQuery(name = "CfgMaestrosClasificaciones.findByMaestro", query = "SELECT c FROM CfgMaestrosClasificaciones c WHERE c.maestro = :maestro"),
    @NamedQuery(name = "CfgMaestrosClasificaciones.findByDescripcion", query = "SELECT c FROM CfgMaestrosClasificaciones c WHERE c.descripcion = :descripcion")})
public class CfgMaestrosClasificaciones implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "maestro", nullable = false, length = 50)
    private String maestro;
    @Column(name = "descripcion", length = 2147483647)
    private String descripcion;
    @OneToMany(mappedBy = "maestro")
    private List<CfgClasificaciones> cfgClasificacionesList;

    public CfgMaestrosClasificaciones() {
    }

    public CfgMaestrosClasificaciones(String maestro) {
        this.maestro = maestro;
    }

    public String getMaestro() {
        return maestro;
    }

    public void setMaestro(String maestro) {
        this.maestro = maestro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<CfgClasificaciones> getCfgClasificacionesList() {
        return cfgClasificacionesList;
    }

    public void setCfgClasificacionesList(List<CfgClasificaciones> cfgClasificacionesList) {
        this.cfgClasificacionesList = cfgClasificacionesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (maestro != null ? maestro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgMaestrosClasificaciones)) {
            return false;
        }
        CfgMaestrosClasificaciones other = (CfgMaestrosClasificaciones) object;
        if ((this.maestro == null && other.maestro != null) || (this.maestro != null && !this.maestro.equals(other.maestro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.CfgMaestrosClasificaciones[ maestro=" + maestro + " ]";
    }
    
}
