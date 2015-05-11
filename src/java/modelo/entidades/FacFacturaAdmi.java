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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "fac_factura_admi", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacFacturaAdmi.findAll", query = "SELECT f FROM FacFacturaAdmi f"),
    @NamedQuery(name = "FacFacturaAdmi.findByIdFacturaAdmi", query = "SELECT f FROM FacFacturaAdmi f WHERE f.idFacturaAdmi = :idFacturaAdmi"),
    @NamedQuery(name = "FacFacturaAdmi.findByCodigoDocumento", query = "SELECT f FROM FacFacturaAdmi f WHERE f.codigoDocumento = :codigoDocumento"),
    @NamedQuery(name = "FacFacturaAdmi.findByFechaSistema", query = "SELECT f FROM FacFacturaAdmi f WHERE f.fechaSistema = :fechaSistema"),
    @NamedQuery(name = "FacFacturaAdmi.findByFechaInicial", query = "SELECT f FROM FacFacturaAdmi f WHERE f.fechaInicial = :fechaInicial"),
    @NamedQuery(name = "FacFacturaAdmi.findByFechaFinal", query = "SELECT f FROM FacFacturaAdmi f WHERE f.fechaFinal = :fechaFinal"),
    @NamedQuery(name = "FacFacturaAdmi.findByFechaElaboracion", query = "SELECT f FROM FacFacturaAdmi f WHERE f.fechaElaboracion = :fechaElaboracion"),
    @NamedQuery(name = "FacFacturaAdmi.findByObservacion", query = "SELECT f FROM FacFacturaAdmi f WHERE f.observacion = :observacion"),
    @NamedQuery(name = "FacFacturaAdmi.findByResolucionDian", query = "SELECT f FROM FacFacturaAdmi f WHERE f.resolucionDian = :resolucionDian"),
    @NamedQuery(name = "FacFacturaAdmi.findByValoresIva", query = "SELECT f FROM FacFacturaAdmi f WHERE f.valoresIva = :valoresIva"),
    @NamedQuery(name = "FacFacturaAdmi.findByValoresCree", query = "SELECT f FROM FacFacturaAdmi f WHERE f.valoresCree = :valoresCree"),
    @NamedQuery(name = "FacFacturaAdmi.findByValoresCopago", query = "SELECT f FROM FacFacturaAdmi f WHERE f.valoresCopago = :valoresCopago"),
    @NamedQuery(name = "FacFacturaAdmi.findByValoresCuotaModeradora", query = "SELECT f FROM FacFacturaAdmi f WHERE f.valoresCuotaModeradora = :valoresCuotaModeradora"),
    @NamedQuery(name = "FacFacturaAdmi.findByAnulada", query = "SELECT f FROM FacFacturaAdmi f WHERE f.anulada = :anulada"),
    @NamedQuery(name = "FacFacturaAdmi.findByValorParcial", query = "SELECT f FROM FacFacturaAdmi f WHERE f.valorParcial = :valorParcial"),
    @NamedQuery(name = "FacFacturaAdmi.findByValorEmpresa", query = "SELECT f FROM FacFacturaAdmi f WHERE f.valorEmpresa = :valorEmpresa"),
    @NamedQuery(name = "FacFacturaAdmi.findByValorTotal", query = "SELECT f FROM FacFacturaAdmi f WHERE f.valorTotal = :valorTotal"),
    @NamedQuery(name = "FacFacturaAdmi.findByNumeroDocumento", query = "SELECT f FROM FacFacturaAdmi f WHERE f.numeroDocumento = :numeroDocumento"),
    @NamedQuery(name = "FacFacturaAdmi.findByObservacionAnulacion", query = "SELECT f FROM FacFacturaAdmi f WHERE f.observacionAnulacion = :observacionAnulacion"),
    @NamedQuery(name = "FacFacturaAdmi.findByValorUsuarios", query = "SELECT f FROM FacFacturaAdmi f WHERE f.valorUsuarios = :valorUsuarios")})
public class FacFacturaAdmi implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_factura_admi", nullable = false)
    private Integer idFacturaAdmi;
    @Column(name = "codigo_documento", length = 20)
    private String codigoDocumento;
    @Column(name = "fecha_sistema")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSistema;
    @Column(name = "fecha_inicial")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicial;
    @Column(name = "fecha_final")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFinal;
    @Column(name = "fecha_elaboracion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaElaboracion;
    @Column(name = "observacion", length = 2147483647)
    private String observacion;
    @Column(name = "resolucion_dian", length = 200)
    private String resolucionDian;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valores_iva", precision = 17, scale = 17)
    private Double valoresIva;
    @Column(name = "valores_cree", precision = 17, scale = 17)
    private Double valoresCree;
    @Column(name = "valores_copago", precision = 17, scale = 17)
    private Double valoresCopago;
    @Column(name = "valores_cuota_moderadora", precision = 17, scale = 17)
    private Double valoresCuotaModeradora;
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
    @Column(name = "observacion_anulacion", length = 2147483647)
    private String observacionAnulacion;
    @Column(name = "valor_usuarios", precision = 17, scale = 17)
    private Double valorUsuarios;
    @JoinTable(name = "fac_factura_admi_detalle", joinColumns = {
        @JoinColumn(name = "id_factura_admi", referencedColumnName = "id_factura_admi", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "id_factura_paciente", referencedColumnName = "id_factura_paciente", nullable = false)})
    @ManyToMany
    private List<FacFacturaPaciente> facFacturaPacienteList;
    @JoinColumn(name = "id_periodo", referencedColumnName = "id_periodo")
    @ManyToOne
    private FacPeriodo idPeriodo;
    @JoinColumn(name = "id_contrato", referencedColumnName = "id_contrato")
    @ManyToOne
    private FacContrato idContrato;
    @JoinColumn(name = "id_administradora", referencedColumnName = "id_administradora")
    @ManyToOne
    private FacAdministradora idAdministradora;
    @JoinColumn(name = "tipo_documento", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones tipoDocumento;

    public FacFacturaAdmi() {
    }

    public FacFacturaAdmi(Integer idFacturaAdmi) {
        this.idFacturaAdmi = idFacturaAdmi;
    }

    public Integer getIdFacturaAdmi() {
        return idFacturaAdmi;
    }

    public void setIdFacturaAdmi(Integer idFacturaAdmi) {
        this.idFacturaAdmi = idFacturaAdmi;
    }

    public String getCodigoDocumento() {
        return codigoDocumento;
    }

    public void setCodigoDocumento(String codigoDocumento) {
        this.codigoDocumento = codigoDocumento;
    }

    public Date getFechaSistema() {
        return fechaSistema;
    }

    public void setFechaSistema(Date fechaSistema) {
        this.fechaSistema = fechaSistema;
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

    public Date getFechaElaboracion() {
        return fechaElaboracion;
    }

    public void setFechaElaboracion(Date fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
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

    public Double getValoresIva() {
        return valoresIva;
    }

    public void setValoresIva(Double valoresIva) {
        this.valoresIva = valoresIva;
    }

    public Double getValoresCree() {
        return valoresCree;
    }

    public void setValoresCree(Double valoresCree) {
        this.valoresCree = valoresCree;
    }

    public Double getValoresCopago() {
        return valoresCopago;
    }

    public void setValoresCopago(Double valoresCopago) {
        this.valoresCopago = valoresCopago;
    }

    public Double getValoresCuotaModeradora() {
        return valoresCuotaModeradora;
    }

    public void setValoresCuotaModeradora(Double valoresCuotaModeradora) {
        this.valoresCuotaModeradora = valoresCuotaModeradora;
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

    public String getObservacionAnulacion() {
        return observacionAnulacion;
    }

    public void setObservacionAnulacion(String observacionAnulacion) {
        this.observacionAnulacion = observacionAnulacion;
    }

    public Double getValorUsuarios() {
        return valorUsuarios;
    }

    public void setValorUsuarios(Double valorUsuarios) {
        this.valorUsuarios = valorUsuarios;
    }

    @XmlTransient
    public List<FacFacturaPaciente> getFacFacturaPacienteList() {
        return facFacturaPacienteList;
    }

    public void setFacFacturaPacienteList(List<FacFacturaPaciente> facFacturaPacienteList) {
        this.facFacturaPacienteList = facFacturaPacienteList;
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

    public FacAdministradora getIdAdministradora() {
        return idAdministradora;
    }

    public void setIdAdministradora(FacAdministradora idAdministradora) {
        this.idAdministradora = idAdministradora;
    }

    public CfgClasificaciones getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(CfgClasificaciones tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFacturaAdmi != null ? idFacturaAdmi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacFacturaAdmi)) {
            return false;
        }
        FacFacturaAdmi other = (FacFacturaAdmi) object;
        if ((this.idFacturaAdmi == null && other.idFacturaAdmi != null) || (this.idFacturaAdmi != null && !this.idFacturaAdmi.equals(other.idFacturaAdmi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.FacFacturaAdmi[ idFactura=" + idFacturaAdmi + " ]";
    }
    
}
