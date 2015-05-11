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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "cfg_txt_predefinidos", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgTxtPredefinidos.findAll", query = "SELECT c FROM CfgTxtPredefinidos c"),
    @NamedQuery(name = "CfgTxtPredefinidos.findByIdTxtPredefinido", query = "SELECT c FROM CfgTxtPredefinidos c WHERE c.idTxtPredefinido = :idTxtPredefinido"),
    @NamedQuery(name = "CfgTxtPredefinidos.findByNombre", query = "SELECT c FROM CfgTxtPredefinidos c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "CfgTxtPredefinidos.findByDetalle", query = "SELECT c FROM CfgTxtPredefinidos c WHERE c.detalle = :detalle")})
public class CfgTxtPredefinidos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_txt_predefinido", nullable = false)
    private Integer idTxtPredefinido;
    @Column(name = "nombre", length = 100)
    private String nombre;
    @Column(name = "detalle", length = 2147483647)
    private String detalle;
    @JoinColumn(name = "id_maestro", referencedColumnName = "id_maestro")
    @ManyToOne
    private CfgMaestrosTxtPredefinidos idMaestro;

    public CfgTxtPredefinidos() {
    }

    public CfgTxtPredefinidos(Integer idTxtPredefinido) {
        this.idTxtPredefinido = idTxtPredefinido;
    }

    public Integer getIdTxtPredefinido() {
        return idTxtPredefinido;
    }

    public void setIdTxtPredefinido(Integer idTxtPredefinido) {
        this.idTxtPredefinido = idTxtPredefinido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public CfgMaestrosTxtPredefinidos getIdMaestro() {
        return idMaestro;
    }

    public void setIdMaestro(CfgMaestrosTxtPredefinidos idMaestro) {
        this.idMaestro = idMaestro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTxtPredefinido != null ? idTxtPredefinido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgTxtPredefinidos)) {
            return false;
        }
        CfgTxtPredefinidos other = (CfgTxtPredefinidos) object;
        if ((this.idTxtPredefinido == null && other.idTxtPredefinido != null) || (this.idTxtPredefinido != null && !this.idTxtPredefinido.equals(other.idTxtPredefinido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.CfgTxtPredefinidos[ idTxtPredefinido=" + idTxtPredefinido + " ]";
    }
    
}
