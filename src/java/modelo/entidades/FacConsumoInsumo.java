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
@Table(name = "fac_consumo_insumo", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacConsumoInsumo.findAll", query = "SELECT f FROM FacConsumoInsumo f"),
    @NamedQuery(name = "FacConsumoInsumo.findByIdConsumoInsumo", query = "SELECT f FROM FacConsumoInsumo f WHERE f.idConsumoInsumo = :idConsumoInsumo"),
    @NamedQuery(name = "FacConsumoInsumo.findByFecha", query = "SELECT f FROM FacConsumoInsumo f WHERE f.fecha = :fecha"),
    @NamedQuery(name = "FacConsumoInsumo.findByCantidad", query = "SELECT f FROM FacConsumoInsumo f WHERE f.cantidad = :cantidad"),
    @NamedQuery(name = "FacConsumoInsumo.findByValorUnitario", query = "SELECT f FROM FacConsumoInsumo f WHERE f.valorUnitario = :valorUnitario"),
    @NamedQuery(name = "FacConsumoInsumo.findByValorFinal", query = "SELECT f FROM FacConsumoInsumo f WHERE f.valorFinal = :valorFinal")})
public class FacConsumoInsumo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_consumo_insumo", nullable = false)
    private Integer idConsumoInsumo;
    @Basic(optional = false)
    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "cantidad")
    private Integer cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_unitario", precision = 17, scale = 17)
    private Double valorUnitario;
    @Column(name = "valor_final", precision = 17, scale = 17)
    private Double valorFinal;
    @JoinColumn(name = "id_prestador", referencedColumnName = "id_usuario")
    @ManyToOne
    private CfgUsuarios idPrestador;
    @JoinColumn(name = "id_paciente", referencedColumnName = "id_paciente", nullable = false)
    @ManyToOne(optional = false)
    private CfgPacientes idPaciente;
    @JoinColumn(name = "id_insumo", referencedColumnName = "id_insumo")
    @ManyToOne
    private CfgInsumo idInsumo;

    public FacConsumoInsumo() {
    }

    public FacConsumoInsumo(Integer idConsumoInsumo) {
        this.idConsumoInsumo = idConsumoInsumo;
    }

    public FacConsumoInsumo(Integer idConsumoInsumo, Date fecha) {
        this.idConsumoInsumo = idConsumoInsumo;
        this.fecha = fecha;
    }

    public Integer getIdConsumoInsumo() {
        return idConsumoInsumo;
    }

    public void setIdConsumoInsumo(Integer idConsumoInsumo) {
        this.idConsumoInsumo = idConsumoInsumo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public Double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(Double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public CfgUsuarios getIdPrestador() {
        return idPrestador;
    }

    public void setIdPrestador(CfgUsuarios idPrestador) {
        this.idPrestador = idPrestador;
    }

    public CfgPacientes getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(CfgPacientes idPaciente) {
        this.idPaciente = idPaciente;
    }

    public CfgInsumo getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(CfgInsumo idInsumo) {
        this.idInsumo = idInsumo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConsumoInsumo != null ? idConsumoInsumo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacConsumoInsumo)) {
            return false;
        }
        FacConsumoInsumo other = (FacConsumoInsumo) object;
        if ((this.idConsumoInsumo == null && other.idConsumoInsumo != null) || (this.idConsumoInsumo != null && !this.idConsumoInsumo.equals(other.idConsumoInsumo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.FacConsumoInsumo[ idConsumoInsumo=" + idConsumoInsumo + " ]";
    }
    
}
