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
@Table(name = "hc_items", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HcItems.findAll", query = "SELECT h FROM HcItems h"),
    @NamedQuery(name = "HcItems.findByIdItem", query = "SELECT h FROM HcItems h WHERE h.idItem = :idItem"),
    @NamedQuery(name = "HcItems.findByTabla", query = "SELECT h FROM HcItems h WHERE h.tabla = :tabla"),
    @NamedQuery(name = "HcItems.findByIdTabla", query = "SELECT h FROM HcItems h WHERE h.idTabla = :idTabla"),
    @NamedQuery(name = "HcItems.findByCantidad", query = "SELECT h FROM HcItems h WHERE h.cantidad = :cantidad"),
    @NamedQuery(name = "HcItems.findByObservacion", query = "SELECT h FROM HcItems h WHERE h.observacion = :observacion")})
public class HcItems implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_item", nullable = false)
    private Integer idItem;
    @Column(name = "tabla", length = 50)
    private String tabla;
    @Column(name = "id_tabla", length = 50)
    private String idTabla;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Column(name = "observacion", length = 2147483647)
    private String observacion;
    @JoinColumn(name = "id_registro", referencedColumnName = "id_registro")
    @ManyToOne
    private HcRegistro idRegistro;

    public HcItems() {
    }

    public HcItems(Integer idItem) {
        this.idItem = idItem;
    }

    public Integer getIdItem() {
        return idItem;
    }

    public void setIdItem(Integer idItem) {
        this.idItem = idItem;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getIdTabla() {
        return idTabla;
    }

    public void setIdTabla(String idTabla) {
        this.idTabla = idTabla;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public HcRegistro getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(HcRegistro idRegistro) {
        this.idRegistro = idRegistro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idItem != null ? idItem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HcItems)) {
            return false;
        }
        HcItems other = (HcItems) object;
        if ((this.idItem == null && other.idItem != null) || (this.idItem != null && !this.idItem.equals(other.idItem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades2.HcItems[ idItem=" + idItem + " ]";
    }
    
}
