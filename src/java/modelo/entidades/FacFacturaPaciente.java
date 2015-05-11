/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author santos
 */
@Entity
@Table(name = "fac_factura_paciente", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacFacturaPaciente.findAll", query = "SELECT f FROM FacFacturaPaciente f"),
    @NamedQuery(name = "FacFacturaPaciente.findByIdFacturaPaciente", query = "SELECT f FROM FacFacturaPaciente f WHERE f.idFacturaPaciente = :idFacturaPaciente"),
    @NamedQuery(name = "FacFacturaPaciente.findByCodigoDocumento", query = "SELECT f FROM FacFacturaPaciente f WHERE f.codigoDocumento = :codigoDocumento"),
    @NamedQuery(name = "FacFacturaPaciente.findByFechaElaboracion", query = "SELECT f FROM FacFacturaPaciente f WHERE f.fechaElaboracion = :fechaElaboracion"),
    @NamedQuery(name = "FacFacturaPaciente.findByNumeroAutorizacion", query = "SELECT f FROM FacFacturaPaciente f WHERE f.numeroAutorizacion = :numeroAutorizacion"),
    @NamedQuery(name = "FacFacturaPaciente.findByFechaAutorizacion", query = "SELECT f FROM FacFacturaPaciente f WHERE f.fechaAutorizacion = :fechaAutorizacion"),
    @NamedQuery(name = "FacFacturaPaciente.findByObservacion", query = "SELECT f FROM FacFacturaPaciente f WHERE f.observacion = :observacion"),
    @NamedQuery(name = "FacFacturaPaciente.findByResolucionDian", query = "SELECT f FROM FacFacturaPaciente f WHERE f.resolucionDian = :resolucionDian"),
    @NamedQuery(name = "FacFacturaPaciente.findByIva", query = "SELECT f FROM FacFacturaPaciente f WHERE f.iva = :iva"),
    @NamedQuery(name = "FacFacturaPaciente.findByCree", query = "SELECT f FROM FacFacturaPaciente f WHERE f.cree = :cree"),
    @NamedQuery(name = "FacFacturaPaciente.findByCopago", query = "SELECT f FROM FacFacturaPaciente f WHERE f.copago = :copago"),
    @NamedQuery(name = "FacFacturaPaciente.findByCuotaModeradora", query = "SELECT f FROM FacFacturaPaciente f WHERE f.cuotaModeradora = :cuotaModeradora"),
    @NamedQuery(name = "FacFacturaPaciente.findByAnulada", query = "SELECT f FROM FacFacturaPaciente f WHERE f.anulada = :anulada"),
    @NamedQuery(name = "FacFacturaPaciente.findByValorParcial", query = "SELECT f FROM FacFacturaPaciente f WHERE f.valorParcial = :valorParcial"),
    @NamedQuery(name = "FacFacturaPaciente.findByValorEmpresa", query = "SELECT f FROM FacFacturaPaciente f WHERE f.valorEmpresa = :valorEmpresa"),
    @NamedQuery(name = "FacFacturaPaciente.findByValorTotal", query = "SELECT f FROM FacFacturaPaciente f WHERE f.valorTotal = :valorTotal"),
    @NamedQuery(name = "FacFacturaPaciente.findByNumeroDocumento", query = "SELECT f FROM FacFacturaPaciente f WHERE f.numeroDocumento = :numeroDocumento"),
    @NamedQuery(name = "FacFacturaPaciente.findByFacturarComoParticular", query = "SELECT f FROM FacFacturaPaciente f WHERE f.facturarComoParticular = :facturarComoParticular"),
    @NamedQuery(name = "FacFacturaPaciente.findByObservacionAnulacion", query = "SELECT f FROM FacFacturaPaciente f WHERE f.observacionAnulacion = :observacionAnulacion"),
    @NamedQuery(name = "FacFacturaPaciente.findByFechaSistema", query = "SELECT f FROM FacFacturaPaciente f WHERE f.fechaSistema = :fechaSistema"),
    @NamedQuery(name = "FacFacturaPaciente.findByValorUsuario", query = "SELECT f FROM FacFacturaPaciente f WHERE f.valorUsuario = :valorUsuario")})
public class FacFacturaPaciente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_factura_paciente", nullable = false)
    private Integer idFacturaPaciente;
    @Column(name = "codigo_documento", length = 20)
    private String codigoDocumento;
    @Column(name = "fecha_elaboracion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaElaboracion;
    @Column(name = "numero_autorizacion", length = 20)
    private String numeroAutorizacion;
    @Column(name = "fecha_autorizacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAutorizacion;
    @Column(name = "observacion", length = 2147483647)
    private String observacion;
    @Column(name = "resolucion_dian", length = 200)
    private String resolucionDian;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "iva", precision = 17, scale = 17)
    private Double iva;
    @Column(name = "cree", precision = 17, scale = 17)
    private Double cree;
    @Column(name = "copago", precision = 17, scale = 17)
    private Double copago;
    @Column(name = "cuota_moderadora", precision = 17, scale = 17)
    private Double cuotaModeradora;
    @Column(name = "anulada")
    private Boolean anulada;
    @Column(name = "valor_parcial", precision = 17, scale = 17)
    private Double valorParcial;
    @Column(name = "valor_empresa", precision = 17, scale = 17)
    private Double valorEmpresa;
    @Column(name = "valor_total", precision = 17, scale = 17)
    private Double valorTotal;
    @Column(name = "numero_documento")
    private Integer numeroDocumento;
    @Column(name = "facturar_como_particular")
    private Boolean facturarComoParticular;
    @Column(name = "observacion_anulacion", length = 2147483647)
    private String observacionAnulacion;
    @Column(name = "fecha_sistema")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSistema;
    @Column(name = "valor_usuario", precision = 17, scale = 17)
    private Double valorUsuario;
    @ManyToMany(mappedBy = "facFacturaPacienteList")
    private List<FacFacturaAdmi> facFacturaAdmiList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facFacturaPaciente")
    private List<FacFacturaPaquete> facFacturaPaqueteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facFacturaPaciente")
    private List<FacFacturaMedicamento> facFacturaMedicamentoList;
    @JoinColumn(name = "id_periodo", referencedColumnName = "id_periodo")
    @ManyToOne
    private FacPeriodo idPeriodo;
    @JoinColumn(name = "id_contrato", referencedColumnName = "id_contrato")
    @ManyToOne
    private FacContrato idContrato;
    @JoinColumn(name = "id_caja", referencedColumnName = "id_caja")
    @ManyToOne
    private FacCaja idCaja;
    @JoinColumn(name = "id_administradora", referencedColumnName = "id_administradora")
    @ManyToOne
    private FacAdministradora idAdministradora;
    @JoinColumn(name = "id_cita", referencedColumnName = "id_cita")
    @ManyToOne
    private CitCitas idCita;
    @JoinColumn(name = "id_paciente", referencedColumnName = "id_paciente")
    @ManyToOne
    private CfgPacientes idPaciente;
    @JoinColumn(name = "tipo_ingreso", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones tipoIngreso;
    @JoinColumn(name = "tipo_documento", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones tipoDocumento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facFacturaPaciente")
    private List<FacFacturaInsumo> facFacturaInsumoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facFacturaPaciente")
    private List<FacFacturaServicio> facFacturaServicioList;
    @Column(name = "facturada_en_admi")
    private Boolean facturadaEnAdmi;

    public FacFacturaPaciente() {
    }

    public FacFacturaPaciente(Integer idFacturaPaciente) {
        this.idFacturaPaciente = idFacturaPaciente;
    }

    public Integer getIdFacturaPaciente() {
        return idFacturaPaciente;
    }

    public void setIdFacturaPaciente(Integer idFacturaPaciente) {
        this.idFacturaPaciente = idFacturaPaciente;
    }

    public String getCodigoDocumento() {
        return codigoDocumento;
    }

    public void setCodigoDocumento(String codigoDocumento) {
        this.codigoDocumento = codigoDocumento;
    }

    public Date getFechaElaboracion() {
        return fechaElaboracion;
    }

    public void setFechaElaboracion(Date fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public Date getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(Date fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getResolucionDian() {
        return resolucionDian;
    }

    public void setResolucionDian(String resolucionDian) {
        this.resolucionDian = resolucionDian;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public Double getCree() {
        return cree;
    }

    public void setCree(Double cree) {
        this.cree = cree;
    }

    public Double getCopago() {
        return copago;
    }

    public void setCopago(Double copago) {
        this.copago = copago;
    }

    public Double getCuotaModeradora() {
        return cuotaModeradora;
    }

    public void setCuotaModeradora(Double cuotaModeradora) {
        this.cuotaModeradora = cuotaModeradora;
    }

    public Boolean getAnulada() {
        return anulada;
    }

    public void setAnulada(Boolean anulada) {
        this.anulada = anulada;
    }

    public Double getValorParcial() {
        return valorParcial;
    }

    public void setValorParcial(Double valorParcial) {
        this.valorParcial = valorParcial;
    }

    public Double getValorEmpresa() {
        return valorEmpresa;
    }

    public void setValorEmpresa(Double valorEmpresa) {
        this.valorEmpresa = valorEmpresa;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Integer getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(Integer numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Boolean getFacturarComoParticular() {
        return facturarComoParticular;
    }

    public void setFacturarComoParticular(Boolean facturarComoParticular) {
        this.facturarComoParticular = facturarComoParticular;
    }

    public String getObservacionAnulacion() {
        return observacionAnulacion;
    }

    public void setObservacionAnulacion(String observacionAnulacion) {
        this.observacionAnulacion = observacionAnulacion;
    }

    public Date getFechaSistema() {
        return fechaSistema;
    }

    public void setFechaSistema(Date fechaSistema) {
        this.fechaSistema = fechaSistema;
    }

    public Double getValorUsuario() {
        return valorUsuario;
    }

    public void setValorUsuario(Double valorUsuario) {
        this.valorUsuario = valorUsuario;
    }

    @XmlTransient
    public List<FacFacturaAdmi> getFacFacturaAdmiList() {
        return facFacturaAdmiList;
    }

    public void setFacFacturaAdmiList(List<FacFacturaAdmi> facFacturaAdmiList) {
        this.facFacturaAdmiList = facFacturaAdmiList;
    }

    @XmlTransient
    public List<FacFacturaPaquete> getFacFacturaPaqueteList() {
        return facFacturaPaqueteList;
    }

    public void setFacFacturaPaqueteList(List<FacFacturaPaquete> facFacturaPaqueteList) {
        this.facFacturaPaqueteList = facFacturaPaqueteList;
    }

    @XmlTransient
    public List<FacFacturaMedicamento> getFacFacturaMedicamentoList() {
        return facFacturaMedicamentoList;
    }

    public void setFacFacturaMedicamentoList(List<FacFacturaMedicamento> facFacturaMedicamentoList) {
        this.facFacturaMedicamentoList = facFacturaMedicamentoList;
    }

    public FacPeriodo getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(FacPeriodo idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public FacContrato getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(FacContrato idContrato) {
        this.idContrato = idContrato;
    }

    public FacCaja getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(FacCaja idCaja) {
        this.idCaja = idCaja;
    }

    public FacAdministradora getIdAdministradora() {
        return idAdministradora;
    }

    public void setIdAdministradora(FacAdministradora idAdministradora) {
        this.idAdministradora = idAdministradora;
    }

    public CitCitas getIdCita() {
        return idCita;
    }

    public void setIdCita(CitCitas idCita) {
        this.idCita = idCita;
    }

    public CfgPacientes getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(CfgPacientes idPaciente) {
        this.idPaciente = idPaciente;
    }

    public CfgClasificaciones getTipoIngreso() {
        return tipoIngreso;
    }

    public void setTipoIngreso(CfgClasificaciones tipoIngreso) {
        this.tipoIngreso = tipoIngreso;
    }

    public CfgClasificaciones getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(CfgClasificaciones tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
    
    public Boolean getFacturadaEnAdmi() {
        return facturadaEnAdmi;
    }

    public void setFacturadaEnAdmi(Boolean facturadaEnAdmi) {
        this.facturadaEnAdmi = facturadaEnAdmi;
    }

    @XmlTransient
    public List<FacFacturaInsumo> getFacFacturaInsumoList() {
        return facFacturaInsumoList;
    }

    public void setFacFacturaInsumoList(List<FacFacturaInsumo> facFacturaInsumoList) {
        this.facFacturaInsumoList = facFacturaInsumoList;
    }

    @XmlTransient
    public List<FacFacturaServicio> getFacFacturaServicioList() {
        return facFacturaServicioList;
    }

    public void setFacFacturaServicioList(List<FacFacturaServicio> facFacturaServicioList) {
        this.facFacturaServicioList = facFacturaServicioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFacturaPaciente != null ? idFacturaPaciente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacFacturaPaciente)) {
            return false;
        }
        FacFacturaPaciente other = (FacFacturaPaciente) object;
        if ((this.idFacturaPaciente == null && other.idFacturaPaciente != null) || (this.idFacturaPaciente != null && !this.idFacturaPaciente.equals(other.idFacturaPaciente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.FacFacturaPaciente[ idFacturaPaciente=" + idFacturaPaciente + " ]";
    }
    
}
