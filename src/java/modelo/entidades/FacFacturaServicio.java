/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "fac_factura_servicio", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacFacturaServicio.findAll", query = "SELECT f FROM FacFacturaServicio f"),
    @NamedQuery(name = "FacFacturaServicio.findByIdDetalle", query = "SELECT f FROM FacFacturaServicio f WHERE f.facFacturaServicioPK.idDetalle = :idDetalle"),
    @NamedQuery(name = "FacFacturaServicio.findByIdFactura", query = "SELECT f FROM FacFacturaServicio f WHERE f.facFacturaServicioPK.idFactura = :idFactura"),
    @NamedQuery(name = "FacFacturaServicio.findByFechaServicio", query = "SELECT f FROM FacFacturaServicio f WHERE f.fechaServicio = :fechaServicio"),
    @NamedQuery(name = "FacFacturaServicio.findByCantidadServicio", query = "SELECT f FROM FacFacturaServicio f WHERE f.cantidadServicio = :cantidadServicio"),
    @NamedQuery(name = "FacFacturaServicio.findByValorServicio", query = "SELECT f FROM FacFacturaServicio f WHERE f.valorServicio = :valorServicio"),
    @NamedQuery(name = "FacFacturaServicio.findByValorParcial", query = "SELECT f FROM FacFacturaServicio f WHERE f.valorParcial = :valorParcial"),
    @NamedQuery(name = "FacFacturaServicio.findByValorUsuario", query = "SELECT f FROM FacFacturaServicio f WHERE f.valorUsuario = :valorUsuario"),
    @NamedQuery(name = "FacFacturaServicio.findByValorEmpresa", query = "SELECT f FROM FacFacturaServicio f WHERE f.valorEmpresa = :valorEmpresa"),
    @NamedQuery(name = "FacFacturaServicio.findByValorIva", query = "SELECT f FROM FacFacturaServicio f WHERE f.valorIva = :valorIva"),
    @NamedQuery(name = "FacFacturaServicio.findByValorCree", query = "SELECT f FROM FacFacturaServicio f WHERE f.valorCree = :valorCree"),
    @NamedQuery(name = "FacFacturaServicio.findByDiagnosticoPrincipal", query = "SELECT f FROM FacFacturaServicio f WHERE f.diagnosticoPrincipal = :diagnosticoPrincipal"),
    @NamedQuery(name = "FacFacturaServicio.findByDiagnosticoRelacionado", query = "SELECT f FROM FacFacturaServicio f WHERE f.diagnosticoRelacionado = :diagnosticoRelacionado")})
public class FacFacturaServicio implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FacFacturaServicioPK facFacturaServicioPK;
    @Column(name = "fecha_servicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaServicio;
    @Column(name = "cantidad_servicio")
    private Short cantidadServicio;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_servicio", precision = 17, scale = 17)
    private Double valorServicio;
    @Column(name = "valor_parcial", precision = 17, scale = 17)
    private Double valorParcial;
    @Column(name = "valor_usuario", precision = 17, scale = 17)
    private Double valorUsuario;
    @Column(name = "valor_empresa", precision = 17, scale = 17)
    private Double valorEmpresa;
    @Column(name = "valor_iva", precision = 17, scale = 17)
    private Double valorIva;
    @Column(name = "valor_cree", precision = 17, scale = 17)
    private Double valorCree;
    @Column(name = "diagnostico_principal", length = 2147483647)
    private String diagnosticoPrincipal;
    @Column(name = "diagnostico_relacionado", length = 2147483647)
    private String diagnosticoRelacionado;
    @JoinColumn(name = "id_servicio", referencedColumnName = "id_servicio")
    @ManyToOne
    private FacServicio idServicio;
    @JoinColumn(name = "id_factura", referencedColumnName = "id_factura_paciente", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private FacFacturaPaciente facFacturaPaciente;
    @JoinColumn(name = "id_medico", referencedColumnName = "id_usuario")
    @ManyToOne
    private CfgUsuarios idMedico;
    @JoinColumn(name = "id_consultorio", referencedColumnName = "id_consultorio")
    @ManyToOne
    private CfgConsultorios idConsultorio;

    public FacFacturaServicio() {
    }

    public FacFacturaServicio(FacFacturaServicioPK facFacturaServicioPK) {
        this.facFacturaServicioPK = facFacturaServicioPK;
    }

    public FacFacturaServicio(int idDetalle, int idFactura) {
        this.facFacturaServicioPK = new FacFacturaServicioPK(idDetalle, idFactura);
    }

    public FacFacturaServicioPK getFacFacturaServicioPK() {
        return facFacturaServicioPK;
    }

    public void setFacFacturaServicioPK(FacFacturaServicioPK facFacturaServicioPK) {
        this.facFacturaServicioPK = facFacturaServicioPK;
    }

    public Date getFechaServicio() {
        return fechaServicio;
    }

    public void setFechaServicio(Date fechaServicio) {
        this.fechaServicio = fechaServicio;
    }

    public Short getCantidadServicio() {
        return cantidadServicio;
    }

    public void setCantidadServicio(Short cantidadServicio) {
        this.cantidadServicio = cantidadServicio;
    }

    public Double getValorServicio() {
        return valorServicio;
    }

    public void setValorServicio(Double valorServicio) {
        this.valorServicio = valorServicio;
    }

    public Double getValorParcial() {
        return valorParcial;
    }

    public void setValorParcial(Double valorParcial) {
        this.valorParcial = valorParcial;
    }

    public Double getValorUsuario() {
        return valorUsuario;
    }

    public void setValorUsuario(Double valorUsuario) {
        this.valorUsuario = valorUsuario;
    }

    public Double getValorEmpresa() {
        return valorEmpresa;
    }

    public void setValorEmpresa(Double valorEmpresa) {
        this.valorEmpresa = valorEmpresa;
    }

    public Double getValorIva() {
        return valorIva;
    }

    public void setValorIva(Double valorIva) {
        this.valorIva = valorIva;
    }

    public Double getValorCree() {
        return valorCree;
    }

    public void setValorCree(Double valorCree) {
        this.valorCree = valorCree;
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

    public FacFacturaPaciente getFacFacturaPaciente() {
        return facFacturaPaciente;
    }

    public void setFacFacturaPaciente(FacFacturaPaciente facFacturaPaciente) {
        this.facFacturaPaciente = facFacturaPaciente;
    }

    public CfgUsuarios getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(CfgUsuarios idMedico) {
        this.idMedico = idMedico;
    }

    public CfgConsultorios getIdConsultorio() {
        return idConsultorio;
    }

    public void setIdConsultorio(CfgConsultorios idConsultorio) {
        this.idConsultorio = idConsultorio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facFacturaServicioPK != null ? facFacturaServicioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacFacturaServicio)) {
            return false;
        }
        FacFacturaServicio other = (FacFacturaServicio) object;
        if ((this.facFacturaServicioPK == null && other.facFacturaServicioPK != null) || (this.facFacturaServicioPK != null && !this.facFacturaServicioPK.equals(other.facFacturaServicioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.FacFacturaServicio[ facFacturaServicioPK=" + facFacturaServicioPK + " ]";
    }
    
}
