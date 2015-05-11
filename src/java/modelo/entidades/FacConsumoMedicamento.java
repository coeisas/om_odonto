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
@Table(name = "fac_consumo_medicamento", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacConsumoMedicamento.findAll", query = "SELECT f FROM FacConsumoMedicamento f"),
    @NamedQuery(name = "FacConsumoMedicamento.findByIdConsumoMedicamento", query = "SELECT f FROM FacConsumoMedicamento f WHERE f.idConsumoMedicamento = :idConsumoMedicamento"),
    @NamedQuery(name = "FacConsumoMedicamento.findByFecha", query = "SELECT f FROM FacConsumoMedicamento f WHERE f.fecha = :fecha"),
    @NamedQuery(name = "FacConsumoMedicamento.findByCantidad", query = "SELECT f FROM FacConsumoMedicamento f WHERE f.cantidad = :cantidad"),
    @NamedQuery(name = "FacConsumoMedicamento.findByValorUnitario", query = "SELECT f FROM FacConsumoMedicamento f WHERE f.valorUnitario = :valorUnitario"),
    @NamedQuery(name = "FacConsumoMedicamento.findByValorFinal", query = "SELECT f FROM FacConsumoMedicamento f WHERE f.valorFinal = :valorFinal")})
public class FacConsumoMedicamento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_consumo_medicamento", nullable = false)
    private Integer idConsumoMedicamento;
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
    @JoinColumn(name = "id_medicamento", referencedColumnName = "id_medicamento")
    @ManyToOne
    private CfgMedicamento idMedicamento;

    public FacConsumoMedicamento() {
    }

    public FacConsumoMedicamento(Integer idConsumoMedicamento) {
        this.idConsumoMedicamento = idConsumoMedicamento;
    }

    public FacConsumoMedicamento(Integer idConsumoMedicamento, Date fecha) {
        this.idConsumoMedicamento = idConsumoMedicamento;
        this.fecha = fecha;
    }

    public Integer getIdConsumoMedicamento() {
        return idConsumoMedicamento;
    }

    public void setIdConsumoMedicamento(Integer idConsumoMedicamento) {
        this.idConsumoMedicamento = idConsumoMedicamento;
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

    public CfgMedicamento getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(CfgMedicamento idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConsumoMedicamento != null ? idConsumoMedicamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacConsumoMedicamento)) {
            return false;
        }
        FacConsumoMedicamento other = (FacConsumoMedicamento) object;
        if ((this.idConsumoMedicamento == null && other.idConsumoMedicamento != null) || (this.idConsumoMedicamento != null && !this.idConsumoMedicamento.equals(other.idConsumoMedicamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.FacConsumoMedicamento[ idConsumoMedicamento=" + idConsumoMedicamento + " ]";
    }
    
}
