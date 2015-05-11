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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "fac_movimiento_caja", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacMovimientoCaja.findAll", query = "SELECT f FROM FacMovimientoCaja f"),
    @NamedQuery(name = "FacMovimientoCaja.findByIdMovimiento", query = "SELECT f FROM FacMovimientoCaja f WHERE f.idMovimiento = :idMovimiento"),
    @NamedQuery(name = "FacMovimientoCaja.findByAbrirCaja", query = "SELECT f FROM FacMovimientoCaja f WHERE f.abrirCaja = :abrirCaja"),
    @NamedQuery(name = "FacMovimientoCaja.findByValor", query = "SELECT f FROM FacMovimientoCaja f WHERE f.valor = :valor"),
    @NamedQuery(name = "FacMovimientoCaja.findByFecha", query = "SELECT f FROM FacMovimientoCaja f WHERE f.fecha = :fecha")})
public class FacMovimientoCaja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_movimiento", nullable = false)
    private Integer idMovimiento;
    @Column(name = "abrir_caja")
    private Boolean abrirCaja;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor", precision = 17, scale = 17)
    private Double valor;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIME)
    private Date fecha;
    @JoinColumn(name = "id_caja", referencedColumnName = "id_caja")
    @ManyToOne
    private FacCaja idCaja;

    public FacMovimientoCaja() {
    }

    public FacMovimientoCaja(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public Integer getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public Boolean getAbrirCaja() {
        return abrirCaja;
    }

    public void setAbrirCaja(Boolean abrirCaja) {
        this.abrirCaja = abrirCaja;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public FacCaja getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(FacCaja idCaja) {
        this.idCaja = idCaja;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMovimiento != null ? idMovimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacMovimientoCaja)) {
            return false;
        }
        FacMovimientoCaja other = (FacMovimientoCaja) object;
        if ((this.idMovimiento == null && other.idMovimiento != null) || (this.idMovimiento != null && !this.idMovimiento.equals(other.idMovimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.FacMovimientoCaja[ idMovimiento=" + idMovimiento + " ]";
    }
    
}
