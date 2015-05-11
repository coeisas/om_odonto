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
@Table(name = "fac_consumo_servicio", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacConsumoServicio.findAll", query = "SELECT f FROM FacConsumoServicio f"),
    @NamedQuery(name = "FacConsumoServicio.findByIdConsumoServicio", query = "SELECT f FROM FacConsumoServicio f WHERE f.idConsumoServicio = :idConsumoServicio"),
    @NamedQuery(name = "FacConsumoServicio.findByFecha", query = "SELECT f FROM FacConsumoServicio f WHERE f.fecha = :fecha"),
    @NamedQuery(name = "FacConsumoServicio.findByCantidad", query = "SELECT f FROM FacConsumoServicio f WHERE f.cantidad = :cantidad"),
    @NamedQuery(name = "FacConsumoServicio.findByValorUnitario", query = "SELECT f FROM FacConsumoServicio f WHERE f.valorUnitario = :valorUnitario"),
    @NamedQuery(name = "FacConsumoServicio.findByValorFinal", query = "SELECT f FROM FacConsumoServicio f WHERE f.valorFinal = :valorFinal"),
    @NamedQuery(name = "FacConsumoServicio.findByTipoTarifa", query = "SELECT f FROM FacConsumoServicio f WHERE f.tipoTarifa = :tipoTarifa"),
    @NamedQuery(name = "FacConsumoServicio.findByDiagnosticoPrincipal", query = "SELECT f FROM FacConsumoServicio f WHERE f.diagnosticoPrincipal = :diagnosticoPrincipal"),
    @NamedQuery(name = "FacConsumoServicio.findByDiagnosticoRelacionado", query = "SELECT f FROM FacConsumoServicio f WHERE f.diagnosticoRelacionado = :diagnosticoRelacionado")})
public class FacConsumoServicio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_consumo_servicio", nullable = false)
    private Integer idConsumoServicio;
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
    @Column(name = "tipo_tarifa", length = 11)
    private String tipoTarifa;
    @Column(name = "diagnostico_principal", length = 2147483647)
    private String diagnosticoPrincipal;
    @Column(name = "diagnostico_relacionado", length = 2147483647)
    private String diagnosticoRelacionado;
    @JoinColumn(name = "id_servicio", referencedColumnName = "id_servicio")
    @ManyToOne
    private FacServicio idServicio;
    @JoinColumn(name = "id_prestador", referencedColumnName = "id_usuario")
    @ManyToOne
    private CfgUsuarios idPrestador;
    @JoinColumn(name = "id_paciente", referencedColumnName = "id_paciente", nullable = false)
    @ManyToOne(optional = false)
    private CfgPacientes idPaciente;

    public FacConsumoServicio() {
    }

    public FacConsumoServicio(Integer idConsumoServicio) {
        this.idConsumoServicio = idConsumoServicio;
    }

    public FacConsumoServicio(Integer idConsumoServicio, Date fecha) {
        this.idConsumoServicio = idConsumoServicio;
        this.fecha = fecha;
    }

    public Integer getIdConsumoServicio() {
        return idConsumoServicio;
    }

    public void setIdConsumoServicio(Integer idConsumoServicio) {
        this.idConsumoServicio = idConsumoServicio;
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

    public String getTipoTarifa() {
        return tipoTarifa;
    }

    public void setTipoTarifa(String tipoTarifa) {
        this.tipoTarifa = tipoTarifa;
    }

    public String getDiagnosticoPrincipal() {
        return diagnosticoPrincipal;
    }

    public void setDiagnosticoPrincipal(String diagnosticoPrincipal) {
        this.diagnosticoPrincipal = diagnosticoPrincipal;
    }

    public String getDiagnosticoRelacionado() {
        return diagnosticoRelacionado;
    }

    public void setDiagnosticoRelacionado(String diagnosticoRelacionado) {
        this.diagnosticoRelacionado = diagnosticoRelacionado;
    }

    public FacServicio getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(FacServicio idServicio) {
        this.idServicio = idServicio;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConsumoServicio != null ? idConsumoServicio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacConsumoServicio)) {
            return false;
        }
        FacConsumoServicio other = (FacConsumoServicio) object;
        if ((this.idConsumoServicio == null && other.idConsumoServicio != null) || (this.idConsumoServicio != null && !this.idConsumoServicio.equals(other.idConsumoServicio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.FacConsumoServicio[ idConsumoServicio=" + idConsumoServicio + " ]";
    }
    
}
