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
@Table(name = "fac_factura_medicamento", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacFacturaMedicamento.findAll", query = "SELECT f FROM FacFacturaMedicamento f"),
    @NamedQuery(name = "FacFacturaMedicamento.findByIdDetalle", query = "SELECT f FROM FacFacturaMedicamento f WHERE f.facFacturaMedicamentoPK.idDetalle = :idDetalle"),
    @NamedQuery(name = "FacFacturaMedicamento.findByIdFactura", query = "SELECT f FROM FacFacturaMedicamento f WHERE f.facFacturaMedicamentoPK.idFactura = :idFactura"),
    @NamedQuery(name = "FacFacturaMedicamento.findByFechaMedicamento", query = "SELECT f FROM FacFacturaMedicamento f WHERE f.fechaMedicamento = :fechaMedicamento"),
    @NamedQuery(name = "FacFacturaMedicamento.findByCantidadMedicamento", query = "SELECT f FROM FacFacturaMedicamento f WHERE f.cantidadMedicamento = :cantidadMedicamento"),
    @NamedQuery(name = "FacFacturaMedicamento.findByValorMedicamento", query = "SELECT f FROM FacFacturaMedicamento f WHERE f.valorMedicamento = :valorMedicamento"),
    @NamedQuery(name = "FacFacturaMedicamento.findByValorParcial", query = "SELECT f FROM FacFacturaMedicamento f WHERE f.valorParcial = :valorParcial"),
    @NamedQuery(name = "FacFacturaMedicamento.findByValorUsuario", query = "SELECT f FROM FacFacturaMedicamento f WHERE f.valorUsuario = :valorUsuario"),
    @NamedQuery(name = "FacFacturaMedicamento.findByValorEmpresa", query = "SELECT f FROM FacFacturaMedicamento f WHERE f.valorEmpresa = :valorEmpresa"),
    @NamedQuery(name = "FacFacturaMedicamento.findByValorIva", query = "SELECT f FROM FacFacturaMedicamento f WHERE f.valorIva = :valorIva"),
    @NamedQuery(name = "FacFacturaMedicamento.findByValorCree", query = "SELECT f FROM FacFacturaMedicamento f WHERE f.valorCree = :valorCree"),
    @NamedQuery(name = "FacFacturaMedicamento.findByNumAutorizacion", query = "SELECT f FROM FacFacturaMedicamento f WHERE f.numAutorizacion = :numAutorizacion"),
    @NamedQuery(name = "FacFacturaMedicamento.findByFechaAutorizacion", query = "SELECT f FROM FacFacturaMedicamento f WHERE f.fechaAutorizacion = :fechaAutorizacion")})
public class FacFacturaMedicamento implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FacFacturaMedicamentoPK facFacturaMedicamentoPK;
    @Column(name = "fecha_medicamento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMedicamento;
    @Column(name = "cantidad_medicamento")
    private Short cantidadMedicamento;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_medicamento", precision = 17, scale = 17)
    private Double valorMedicamento;
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
    @Column(name = "num_autorizacion", length = 20)
    private String numAutorizacion;
    @Column(name = "fecha_autorizacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAutorizacion;
    @JoinColumn(name = "id_factura", referencedColumnName = "id_factura_paciente", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private FacFacturaPaciente facFacturaPaciente;
    @JoinColumn(name = "id_medico", referencedColumnName = "id_usuario")
    @ManyToOne
    private CfgUsuarios idMedico;
    @JoinColumn(name = "id_medicamento", referencedColumnName = "id_medicamento")
    @ManyToOne
    private CfgMedicamento idMedicamento;
    @JoinColumn(name = "id_consultorio", referencedColumnName = "id_consultorio")
    @ManyToOne
    private CfgConsultorios idConsultorio;

    public FacFacturaMedicamento() {
    }

    public FacFacturaMedicamento(FacFacturaMedicamentoPK facFacturaMedicamentoPK) {
        this.facFacturaMedicamentoPK = facFacturaMedicamentoPK;
    }

    public FacFacturaMedicamento(int idDetalle, int idFactura) {
        this.facFacturaMedicamentoPK = new FacFacturaMedicamentoPK(idDetalle, idFactura);
    }

    public FacFacturaMedicamentoPK getFacFacturaMedicamentoPK() {
        return facFacturaMedicamentoPK;
    }

    public void setFacFacturaMedicamentoPK(FacFacturaMedicamentoPK facFacturaMedicamentoPK) {
        this.facFacturaMedicamentoPK = facFacturaMedicamentoPK;
    }

    public Date getFechaMedicamento() {
        return fechaMedicamento;
    }

    public void setFechaMedicamento(Date fechaMedicamento) {
        this.fechaMedicamento = fechaMedicamento;
    }

    public Short getCantidadMedicamento() {
        return cantidadMedicamento;
    }

    public void setCantidadMedicamento(Short cantidadMedicamento) {
        this.cantidadMedicamento = cantidadMedicamento;
    }

    public Double getValorMedicamento() {
        return valorMedicamento;
    }

    public void setValorMedicamento(Double valorMedicamento) {
        this.valorMedicamento = valorMedicamento;
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

    public String getNumAutorizacion() {
        return numAutorizacion;
    }

    public void setNumAutorizacion(String numAutorizacion) {
        this.numAutorizacion = numAutorizacion;
    }

    public Date getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(Date fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
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

    public CfgMedicamento getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(CfgMedicamento idMedicamento) {
        this.idMedicamento = idMedicamento;
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
        hash += (facFacturaMedicamentoPK != null ? facFacturaMedicamentoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacFacturaMedicamento)) {
            return false;
        }
        FacFacturaMedicamento other = (FacFacturaMedicamento) object;
        if ((this.facFacturaMedicamentoPK == null && other.facFacturaMedicamentoPK != null) || (this.facFacturaMedicamentoPK != null && !this.facFacturaMedicamentoPK.equals(other.facFacturaMedicamentoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.FacFacturaMedicamento[ facFacturaMedicamentoPK=" + facFacturaMedicamentoPK + " ]";
    }
    
}
