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
@Table(name = "cfg_maestros_txt_predefinidos", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgMaestrosTxtPredefinidos.findAll", query = "SELECT c FROM CfgMaestrosTxtPredefinidos c"),
    @NamedQuery(name = "CfgMaestrosTxtPredefinidos.findByIdMaestro", query = "SELECT c FROM CfgMaestrosTxtPredefinidos c WHERE c.idMaestro = :idMaestro"),
    @NamedQuery(name = "CfgMaestrosTxtPredefinidos.findByNombre", query = "SELECT c FROM CfgMaestrosTxtPredefinidos c WHERE c.nombre = :nombre")})
public class CfgMaestrosTxtPredefinidos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_maestro", nullable = false)
    private Integer idMaestro;
    @Column(name = "nombre", length = 100)
    private String nombre;
    @OneToMany(mappedBy = "idMaestro")
    private List<CfgTxtPredefinidos> cfgTxtPredefinidosList;

    public CfgMaestrosTxtPredefinidos() {
    }

    public CfgMaestrosTxtPredefinidos(Integer idMaestro) {
        this.idMaestro = idMaestro;
    }

    public Integer getIdMaestro() {
        return idMaestro;
    }

    public void setIdMaestro(Integer idMaestro) {
        this.idMaestro = idMaestro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<CfgTxtPredefinidos> getCfgTxtPredefinidosList() {
        return cfgTxtPredefinidosList;
    }

    public void setCfgTxtPredefinidosList(List<CfgTxtPredefinidos> cfgTxtPredefinidosList) {
        this.cfgTxtPredefinidosList = cfgTxtPredefinidosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMaestro != null ? idMaestro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgMaestrosTxtPredefinidos)) {
            return false;
        }
        CfgMaestrosTxtPredefinidos other = (CfgMaestrosTxtPredefinidos) object;
        if ((this.idMaestro == null && other.idMaestro != null) || (this.idMaestro != null && !this.idMaestro.equals(other.idMaestro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.CfgMaestrosTxtPredefinidos[ idMaestro=" + idMaestro + " ]";
    }
    
}
