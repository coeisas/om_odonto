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
@Table(name = "hc_campos_reg", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HcCamposReg.findAll", query = "SELECT h FROM HcCamposReg h"),
    @NamedQuery(name = "HcCamposReg.findByIdCampo", query = "SELECT h FROM HcCamposReg h WHERE h.idCampo = :idCampo"),
    @NamedQuery(name = "HcCamposReg.findByNombre", query = "SELECT h FROM HcCamposReg h WHERE h.nombre = :nombre"),
    @NamedQuery(name = "HcCamposReg.findByTabla", query = "SELECT h FROM HcCamposReg h WHERE h.tabla = :tabla"),
    @NamedQuery(name = "HcCamposReg.findByPosicion", query = "SELECT h FROM HcCamposReg h WHERE h.posicion = :posicion"),
    @NamedQuery(name = "HcCamposReg.findByNombrePdf", query = "SELECT h FROM HcCamposReg h WHERE h.nombrePdf = :nombrePdf")})
public class HcCamposReg implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_campo", nullable = false)
    private Integer idCampo;
    @Column(name = "nombre", length = 100)
    private String nombre;
    @Column(name = "tabla", length = 50)
    private String tabla;
    @Column(name = "posicion")
    private Integer posicion;
    @Column(name = "nombre_pdf", length = 2147483647)
    private String nombrePdf;
    @JoinColumn(name = "id_tipo_reg", referencedColumnName = "id_tipo_reg")
    @ManyToOne
    private HcTipoReg idTipoReg;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hcCamposReg")
    private List<HcDetalle> hcDetalleList;

    public HcCamposReg() {
    }

    public HcCamposReg(Integer idCampo) {
        this.idCampo = idCampo;
    }

    public Integer getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(Integer idCampo) {
        this.idCampo = idCampo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }
    
    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }

    public String getNombrePdf() {
        return nombrePdf;
    }

    public void setNombrePdf(String nombrePdf) {
        this.nombrePdf = nombrePdf;
    }

    public HcTipoReg getIdTipoReg() {
        return idTipoReg;
    }

    public void setIdTipoReg(HcTipoReg idTipoReg) {
        this.idTipoReg = idTipoReg;
    }

    @XmlTransient
    public List<HcDetalle> getHcDetalleList() {
        return hcDetalleList;
    }

    public void setHcDetalleList(List<HcDetalle> hcDetalleList) {
        this.hcDetalleList = hcDetalleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCampo != null ? idCampo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HcCamposReg)) {
            return false;
        }
        HcCamposReg other = (HcCamposReg) object;
        if ((this.idCampo == null && other.idCampo != null) || (this.idCampo != null && !this.idCampo.equals(other.idCampo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades2.HcCamposReg[ idCampo=" + idCampo + " ]";
    }

}
