/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author santos
 */
@Entity
@Table(name = "fac_impuestos", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacImpuestos.findAll", query = "SELECT f FROM FacImpuestos f"),
    @NamedQuery(name = "FacImpuestos.findByIdImpuesto", query = "SELECT f FROM FacImpuestos f WHERE f.idImpuesto = :idImpuesto"),
    @NamedQuery(name = "FacImpuestos.findByNombre", query = "SELECT f FROM FacImpuestos f WHERE f.nombre = :nombre"),
    @NamedQuery(name = "FacImpuestos.findByValor", query = "SELECT f FROM FacImpuestos f WHERE f.valor = :valor"),
    @NamedQuery(name = "FacImpuestos.findByFechaInicial", query = "SELECT f FROM FacImpuestos f WHERE f.fechaInicial = :fechaInicial"),
    @NamedQuery(name = "FacImpuestos.findByFechaFinal", query = "SELECT f FROM FacImpuestos f WHERE f.fechaFinal = :fechaFinal")})
public class FacImpuestos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_impuesto", nullable = false)
    private Integer idImpuesto;
    @Column(name = "nombre", length = 50)
    private String nombre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor", precision = 17, scale = 17)
    private Double valor;
    @Column(name = "fecha_inicial")
    @Temporal(TemporalType.DATE)
    private Date fechaInicial;
    @Column(name = "fecha_final")
    @Temporal(TemporalType.DATE)
    private Date fechaFinal;

    public FacImpuestos() {
    }

    public FacImpuestos(Integer idImpuesto) {
        this.idImpuesto = idImpuesto;
    }

    public Integer getIdImpuesto() {
        return idImpuesto;
    }

    public void setIdImpuesto(Integer idImpuesto) {
        this.idImpuesto = idImpuesto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idImpuesto != null ? idImpuesto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacImpuestos)) {
            return false;
        }
        FacImpuestos other = (FacImpuestos) object;
        if ((this.idImpuesto == null && other.idImpuesto != null) || (this.idImpuesto != null && !this.idImpuesto.equals(other.idImpuesto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.FacImpuestos[ idImpuesto=" + idImpuesto + " ]";
    }
    
}
