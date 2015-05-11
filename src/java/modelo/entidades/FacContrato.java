/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author santos
 */
@Entity
@Table(name = "fac_contrato", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacContrato.findAll", query = "SELECT f FROM FacContrato f"),
    @NamedQuery(name = "FacContrato.findByIdContrato", query = "SELECT f FROM FacContrato f WHERE f.idContrato = :idContrato"),
    @NamedQuery(name = "FacContrato.findByCodigoContrato", query = "SELECT f FROM FacContrato f WHERE f.codigoContrato = :codigoContrato"),
    @NamedQuery(name = "FacContrato.findByFechaInicio", query = "SELECT f FROM FacContrato f WHERE f.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "FacContrato.findByFechaFinal", query = "SELECT f FROM FacContrato f WHERE f.fechaFinal = :fechaFinal"),
    @NamedQuery(name = "FacContrato.findByNumeroRipContrato", query = "SELECT f FROM FacContrato f WHERE f.numeroRipContrato = :numeroRipContrato"),
    @NamedQuery(name = "FacContrato.findByDescripcion", query = "SELECT f FROM FacContrato f WHERE f.descripcion = :descripcion"),
    @NamedQuery(name = "FacContrato.findByNumeroPoliza", query = "SELECT f FROM FacContrato f WHERE f.numeroPoliza = :numeroPoliza"),
    @NamedQuery(name = "FacContrato.findByUpc", query = "SELECT f FROM FacContrato f WHERE f.upc = :upc"),
    @NamedQuery(name = "FacContrato.findByNumeroAfiliados", query = "SELECT f FROM FacContrato f WHERE f.numeroAfiliados = :numeroAfiliados"),
    @NamedQuery(name = "FacContrato.findByValorContrato", query = "SELECT f FROM FacContrato f WHERE f.valorContrato = :valorContrato"),
    @NamedQuery(name = "FacContrato.findByValorMensual", query = "SELECT f FROM FacContrato f WHERE f.valorMensual = :valorMensual"),
    @NamedQuery(name = "FacContrato.findByValorValidacionMensual", query = "SELECT f FROM FacContrato f WHERE f.valorValidacionMensual = :valorValidacionMensual"),
    @NamedQuery(name = "FacContrato.findByValorAlarma", query = "SELECT f FROM FacContrato f WHERE f.valorAlarma = :valorAlarma"),
    @NamedQuery(name = "FacContrato.findByPorcentajeDescuentoServicios", query = "SELECT f FROM FacContrato f WHERE f.porcentajeDescuentoServicios = :porcentajeDescuentoServicios"),
    @NamedQuery(name = "FacContrato.findByPorcentejeDescuentoEntidad", query = "SELECT f FROM FacContrato f WHERE f.porcentejeDescuentoEntidad = :porcentejeDescuentoEntidad"),
    @NamedQuery(name = "FacContrato.findByCodigoTarifaPos", query = "SELECT f FROM FacContrato f WHERE f.codigoTarifaPos = :codigoTarifaPos"),
    @NamedQuery(name = "FacContrato.findByCodigoTarifaNopos", query = "SELECT f FROM FacContrato f WHERE f.codigoTarifaNopos = :codigoTarifaNopos"),
    @NamedQuery(name = "FacContrato.findByRip", query = "SELECT f FROM FacContrato f WHERE f.rip = :rip"),
    @NamedQuery(name = "FacContrato.findByObservacionContrato", query = "SELECT f FROM FacContrato f WHERE f.observacionContrato = :observacionContrato"),
    @NamedQuery(name = "FacContrato.findByObservacionFacturacion", query = "SELECT f FROM FacContrato f WHERE f.observacionFacturacion = :observacionFacturacion"),
    @NamedQuery(name = "FacContrato.findByCuentaCobrar", query = "SELECT f FROM FacContrato f WHERE f.cuentaCobrar = :cuentaCobrar"),
    @NamedQuery(name = "FacContrato.findByCuentaCopago", query = "SELECT f FROM FacContrato f WHERE f.cuentaCopago = :cuentaCopago"),
    @NamedQuery(name = "FacContrato.findByCodigoConcepto", query = "SELECT f FROM FacContrato f WHERE f.codigoConcepto = :codigoConcepto"),
    @NamedQuery(name = "FacContrato.findByCodigoConceptoDescuento", query = "SELECT f FROM FacContrato f WHERE f.codigoConceptoDescuento = :codigoConceptoDescuento"),
    @NamedQuery(name = "FacContrato.findByC1", query = "SELECT f FROM FacContrato f WHERE f.c1 = :c1"),
    @NamedQuery(name = "FacContrato.findByCm1", query = "SELECT f FROM FacContrato f WHERE f.cm1 = :cm1"),
    @NamedQuery(name = "FacContrato.findByCm2", query = "SELECT f FROM FacContrato f WHERE f.cm2 = :cm2"),
    @NamedQuery(name = "FacContrato.findByCm3", query = "SELECT f FROM FacContrato f WHERE f.cm3 = :cm3"),
    @NamedQuery(name = "FacContrato.findByCm4", query = "SELECT f FROM FacContrato f WHERE f.cm4 = :cm4"),
    @NamedQuery(name = "FacContrato.findByCm5", query = "SELECT f FROM FacContrato f WHERE f.cm5 = :cm5"),
    @NamedQuery(name = "FacContrato.findByCmc", query = "SELECT f FROM FacContrato f WHERE f.cmc = :cmc"),
    @NamedQuery(name = "FacContrato.findByCmb", query = "SELECT f FROM FacContrato f WHERE f.cmb = :cmb"),
    @NamedQuery(name = "FacContrato.findByCp1", query = "SELECT f FROM FacContrato f WHERE f.cp1 = :cp1"),
    @NamedQuery(name = "FacContrato.findByCp2", query = "SELECT f FROM FacContrato f WHERE f.cp2 = :cp2"),
    @NamedQuery(name = "FacContrato.findByCp3", query = "SELECT f FROM FacContrato f WHERE f.cp3 = :cp3"),
    @NamedQuery(name = "FacContrato.findByCp4", query = "SELECT f FROM FacContrato f WHERE f.cp4 = :cp4"),
    @NamedQuery(name = "FacContrato.findByCp5", query = "SELECT f FROM FacContrato f WHERE f.cp5 = :cp5"),
    @NamedQuery(name = "FacContrato.findByCpc", query = "SELECT f FROM FacContrato f WHERE f.cpc = :cpc"),
    @NamedQuery(name = "FacContrato.findByCpb", query = "SELECT f FROM FacContrato f WHERE f.cpb = :cpb"),
    @NamedQuery(name = "FacContrato.findByMedicamentoValor1", query = "SELECT f FROM FacContrato f WHERE f.medicamentoValor1 = :medicamentoValor1"),
    @NamedQuery(name = "FacContrato.findByMedicamentoValor2", query = "SELECT f FROM FacContrato f WHERE f.medicamentoValor2 = :medicamentoValor2"),
    @NamedQuery(name = "FacContrato.findByMedicamentoValor3", query = "SELECT f FROM FacContrato f WHERE f.medicamentoValor3 = :medicamentoValor3"),
    @NamedQuery(name = "FacContrato.findByInsumosPorcentaje1", query = "SELECT f FROM FacContrato f WHERE f.insumosPorcentaje1 = :insumosPorcentaje1"),
    @NamedQuery(name = "FacContrato.findByInsumosPorcentaje2", query = "SELECT f FROM FacContrato f WHERE f.insumosPorcentaje2 = :insumosPorcentaje2"),
    @NamedQuery(name = "FacContrato.findByInsumosPorcentaje3", query = "SELECT f FROM FacContrato f WHERE f.insumosPorcentaje3 = :insumosPorcentaje3"),
    @NamedQuery(name = "FacContrato.findByAplicarIva", query = "SELECT f FROM FacContrato f WHERE f.aplicarIva = :aplicarIva"),
    @NamedQuery(name = "FacContrato.findByAplicarCree", query = "SELECT f FROM FacContrato f WHERE f.aplicarCree = :aplicarCree")})
public class FacContrato implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_contrato", nullable = false)
    private Integer idContrato;
    @Column(name = "codigo_contrato", length = 10)
    private String codigoContrato;
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Column(name = "fecha_final")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFinal;
    @Column(name = "numero_rip_contrato", length = 10)
    private String numeroRipContrato;
    @Column(name = "descripcion", length = 2147483647)
    private String descripcion;
    @Column(name = "numero_poliza", length = 10)
    private String numeroPoliza;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "upc", precision = 17, scale = 17)
    private Double upc;
    @Column(name = "numero_afiliados")
    private Integer numeroAfiliados;
    @Column(name = "valor_contrato", precision = 17, scale = 17)
    private Double valorContrato;
    @Column(name = "valor_mensual", precision = 17, scale = 17)
    private Double valorMensual;
    @Column(name = "valor_validacion_mensual", precision = 17, scale = 17)
    private Double valorValidacionMensual;
    @Column(name = "valor_alarma", precision = 17, scale = 17)
    private Double valorAlarma;
    @Column(name = "porcentaje_descuento_servicios", precision = 17, scale = 17)
    private Double porcentajeDescuentoServicios;
    @Column(name = "porcenteje_descuento_entidad", precision = 17, scale = 17)
    private Double porcentejeDescuentoEntidad;
    @Column(name = "codigo_tarifa_pos", length = 3)
    private String codigoTarifaPos;
    @Column(name = "codigo_tarifa_nopos", length = 3)
    private String codigoTarifaNopos;
    @Column(name = "rip")
    private Boolean rip;
    @Column(name = "observacion_contrato", length = 2147483647)
    private String observacionContrato;
    @Column(name = "observacion_facturacion", length = 2147483647)
    private String observacionFacturacion;
    @Column(name = "cuenta_cobrar", length = 20)
    private String cuentaCobrar;
    @Column(name = "cuenta_copago", length = 20)
    private String cuentaCopago;
    @Column(name = "codigo_concepto", length = 5)
    private String codigoConcepto;
    @Column(name = "codigo_concepto_descuento", length = 5)
    private String codigoConceptoDescuento;
    @Column(name = "c1", precision = 17, scale = 17)
    private Double c1;
    @Column(name = "cm1", precision = 17, scale = 17)
    private Double cm1;
    @Column(name = "cm2", precision = 17, scale = 17)
    private Double cm2;
    @Column(name = "cm3", precision = 17, scale = 17)
    private Double cm3;
    @Column(name = "cm4", precision = 17, scale = 17)
    private Double cm4;
    @Column(name = "cm5", precision = 17, scale = 17)
    private Double cm5;
    @Column(name = "cmc")
    private Boolean cmc;
    @Column(name = "cmb")
    private Boolean cmb;
    @Column(name = "cp1", precision = 17, scale = 17)
    private Double cp1;
    @Column(name = "cp2", precision = 17, scale = 17)
    private Double cp2;
    @Column(name = "cp3", precision = 17, scale = 17)
    private Double cp3;
    @Column(name = "cp4", precision = 17, scale = 17)
    private Double cp4;
    @Column(name = "cp5", precision = 17, scale = 17)
    private Double cp5;
    @Column(name = "cpc")
    private Boolean cpc;
    @Column(name = "cpb")
    private Boolean cpb;
    @Column(name = "medicamento_valor_1", precision = 17, scale = 17)
    private Double medicamentoValor1;
    @Column(name = "medicamento_valor_2", precision = 17, scale = 17)
    private Double medicamentoValor2;
    @Column(name = "medicamento_valor_3", precision = 17, scale = 17)
    private Double medicamentoValor3;
    @Column(name = "insumos_porcentaje_1", precision = 17, scale = 17)
    private Double insumosPorcentaje1;
    @Column(name = "insumos_porcentaje_2", precision = 17, scale = 17)
    private Double insumosPorcentaje2;
    @Column(name = "insumos_porcentaje_3", precision = 17, scale = 17)
    private Double insumosPorcentaje3;
    @Column(name = "aplicar_iva")
    private Boolean aplicarIva;
    @Column(name = "aplicar_cree")
    private Boolean aplicarCree;
    @OneToMany(mappedBy = "idContrato")
    private List<FacFacturaAdmi> facFacturaAdmiList;
    @OneToMany(mappedBy = "idContrato")
    private List<RipsAlmacenados> ripsAlmacenadosList;
    @OneToMany(mappedBy = "idContrato")
    private List<FacFacturaPaciente> facFacturaPacienteList;
    @JoinColumn(name = "id_manual_tarifario", referencedColumnName = "id_manual_tarifario")
    @ManyToOne
    private FacManualTarifario idManualTarifario;
    @JoinColumn(name = "id_administradora", referencedColumnName = "id_administradora")
    @ManyToOne
    private FacAdministradora idAdministradora;
    @JoinColumn(name = "tipo_pago", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones tipoPago;
    @JoinColumn(name = "tipo_facturacion", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones tipoFacturacion;
    @JoinColumn(name = "tipo_contrato", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones tipoContrato;
    @OneToMany(mappedBy = "idContrato")
    private List<FacPrograma> facProgramaList;
    @Transient
    private String vigencia;

    public FacContrato() {
    }

    public String getVigencia() {
        if (fechaInicio != null && fechaFinal != null) {
            Calendar calendarHoy = new GregorianCalendar();
            Calendar calendarInicio = new GregorianCalendar();
            Calendar calendarFin = new GregorianCalendar();
            calendarHoy.setTime(new Date());
            calendarInicio.setTime(fechaInicio);
            calendarFin.setTime(fechaFinal);
            if (calendarHoy.after(calendarInicio) && calendarHoy.before(calendarFin)) {
                return "VIGENTE";
            } else {
                return "VENCIDO";
            }
        }
        return "FALTA FECHA INICIAL O FINAL";
    }

    public void setVigencia(String vigencia) {
        this.vigencia = vigencia;
    }

    public FacContrato(Integer idContrato) {
        this.idContrato = idContrato;
    }

    public Integer getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Integer idContrato) {
        this.idContrato = idContrato;
    }

    public String getCodigoContrato() {
        return codigoContrato;
    }

    public void setCodigoContrato(String codigoContrato) {
        this.codigoContrato = codigoContrato;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getNumeroRipContrato() {
        return numeroRipContrato;
    }

    public void setNumeroRipContrato(String numeroRipContrato) {
        this.numeroRipContrato = numeroRipContrato;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNumeroPoliza() {
        return numeroPoliza;
    }

    public void setNumeroPoliza(String numeroPoliza) {
        this.numeroPoliza = numeroPoliza;
    }

    public Double getUpc() {
        return upc;
    }

    public void setUpc(Double upc) {
        this.upc = upc;
    }

    public Integer getNumeroAfiliados() {
        return numeroAfiliados;
    }

    public void setNumeroAfiliados(Integer numeroAfiliados) {
        this.numeroAfiliados = numeroAfiliados;
    }

    public Double getValorContrato() {
        return valorContrato;
    }

    public void setValorContrato(Double valorContrato) {
        this.valorContrato = valorContrato;
    }

    public Double getValorMensual() {
        return valorMensual;
    }

    public void setValorMensual(Double valorMensual) {
        this.valorMensual = valorMensual;
    }

    public Double getValorValidacionMensual() {
        return valorValidacionMensual;
    }

    public void setValorValidacionMensual(Double valorValidacionMensual) {
        this.valorValidacionMensual = valorValidacionMensual;
    }

    public Double getValorAlarma() {
        return valorAlarma;
    }

    public void setValorAlarma(Double valorAlarma) {
        this.valorAlarma = valorAlarma;
    }

    public Double getPorcentajeDescuentoServicios() {
        return porcentajeDescuentoServicios;
    }

    public void setPorcentajeDescuentoServicios(Double porcentajeDescuentoServicios) {
        this.porcentajeDescuentoServicios = porcentajeDescuentoServicios;
    }

    public Double getPorcentejeDescuentoEntidad() {
        return porcentejeDescuentoEntidad;
    }

    public void setPorcentejeDescuentoEntidad(Double porcentejeDescuentoEntidad) {
        this.porcentejeDescuentoEntidad = porcentejeDescuentoEntidad;
    }

    public String getCodigoTarifaPos() {
        return codigoTarifaPos;
    }

    public void setCodigoTarifaPos(String codigoTarifaPos) {
        this.codigoTarifaPos = codigoTarifaPos;
    }

    public String getCodigoTarifaNopos() {
        return codigoTarifaNopos;
    }

    public void setCodigoTarifaNopos(String codigoTarifaNopos) {
        this.codigoTarifaNopos = codigoTarifaNopos;
    }

    public Boolean getRip() {
        return rip;
    }

    public void setRip(Boolean rip) {
        this.rip = rip;
    }

    public String getObservacionContrato() {
        return observacionContrato;
    }

    public void setObservacionContrato(String observacionContrato) {
        this.observacionContrato = observacionContrato;
    }

    public String getObservacionFacturacion() {
        return observacionFacturacion;
    }

    public void setObservacionFacturacion(String observacionFacturacion) {
        this.observacionFacturacion = observacionFacturacion;
    }

    public String getCuentaCobrar() {
        return cuentaCobrar;
    }

    public void setCuentaCobrar(String cuentaCobrar) {
        this.cuentaCobrar = cuentaCobrar;
    }

    public String getCuentaCopago() {
        return cuentaCopago;
    }

    public void setCuentaCopago(String cuentaCopago) {
        this.cuentaCopago = cuentaCopago;
    }

    public String getCodigoConcepto() {
        return codigoConcepto;
    }

    public void setCodigoConcepto(String codigoConcepto) {
        this.codigoConcepto = codigoConcepto;
    }

    public String getCodigoConceptoDescuento() {
        return codigoConceptoDescuento;
    }

    public void setCodigoConceptoDescuento(String codigoConceptoDescuento) {
        this.codigoConceptoDescuento = codigoConceptoDescuento;
    }

    public Double getC1() {
        return c1;
    }

    public void setC1(Double c1) {
        this.c1 = c1;
    }

    public Double getCm1() {
        return cm1;
    }

    public void setCm1(Double cm1) {
        this.cm1 = cm1;
    }

    public Double getCm2() {
        return cm2;
    }

    public void setCm2(Double cm2) {
        this.cm2 = cm2;
    }

    public Double getCm3() {
        return cm3;
    }

    public void setCm3(Double cm3) {
        this.cm3 = cm3;
    }

    public Double getCm4() {
        return cm4;
    }

    public void setCm4(Double cm4) {
        this.cm4 = cm4;
    }

    public Double getCm5() {
        return cm5;
    }

    public void setCm5(Double cm5) {
        this.cm5 = cm5;
    }

    public Boolean getCmc() {
        return cmc;
    }

    public void setCmc(Boolean cmc) {
        this.cmc = cmc;
    }

    public Boolean getCmb() {
        return cmb;
    }

    public void setCmb(Boolean cmb) {
        this.cmb = cmb;
    }

    public Double getCp1() {
        return cp1;
    }

    public void setCp1(Double cp1) {
        this.cp1 = cp1;
    }

    public Double getCp2() {
        return cp2;
    }

    public void setCp2(Double cp2) {
        this.cp2 = cp2;
    }

    public Double getCp3() {
        return cp3;
    }

    public void setCp3(Double cp3) {
        this.cp3 = cp3;
    }

    public Double getCp4() {
        return cp4;
    }

    public void setCp4(Double cp4) {
        this.cp4 = cp4;
    }

    public Double getCp5() {
        return cp5;
    }

    public void setCp5(Double cp5) {
        this.cp5 = cp5;
    }

    public Boolean getCpc() {
        return cpc;
    }

    public void setCpc(Boolean cpc) {
        this.cpc = cpc;
    }

    public Boolean getCpb() {
        return cpb;
    }

    public void setCpb(Boolean cpb) {
        this.cpb = cpb;
    }

    public Double getMedicamentoValor1() {
        return medicamentoValor1;
    }

    public void setMedicamentoValor1(Double medicamentoValor1) {
        this.medicamentoValor1 = medicamentoValor1;
    }

    public Double getMedicamentoValor2() {
        return medicamentoValor2;
    }

    public void setMedicamentoValor2(Double medicamentoValor2) {
        this.medicamentoValor2 = medicamentoValor2;
    }

    public Double getMedicamentoValor3() {
        return medicamentoValor3;
    }

    public void setMedicamentoValor3(Double medicamentoValor3) {
        this.medicamentoValor3 = medicamentoValor3;
    }

    public Double getInsumosPorcentaje1() {
        return insumosPorcentaje1;
    }

    public void setInsumosPorcentaje1(Double insumosPorcentaje1) {
        this.insumosPorcentaje1 = insumosPorcentaje1;
    }

    public Double getInsumosPorcentaje2() {
        return insumosPorcentaje2;
    }

    public void setInsumosPorcentaje2(Double insumosPorcentaje2) {
        this.insumosPorcentaje2 = insumosPorcentaje2;
    }

    public Double getInsumosPorcentaje3() {
        return insumosPorcentaje3;
    }

    public void setInsumosPorcentaje3(Double insumosPorcentaje3) {
        this.insumosPorcentaje3 = insumosPorcentaje3;
    }

    public Boolean getAplicarIva() {
        return aplicarIva;
    }

    public void setAplicarIva(Boolean aplicarIva) {
        this.aplicarIva = aplicarIva;
    }

    public Boolean getAplicarCree() {
        return aplicarCree;
    }

    public void setAplicarCree(Boolean aplicarCree) {
        this.aplicarCree = aplicarCree;
    }

    @XmlTransient
    public List<FacFacturaAdmi> getFacFacturaAdmiList() {
        return facFacturaAdmiList;
    }

    public void setFacFacturaAdmiList(List<FacFacturaAdmi> facFacturaAdmiList) {
        this.facFacturaAdmiList = facFacturaAdmiList;
    }

    @XmlTransient
    public List<RipsAlmacenados> getRipsAlmacenadosList() {
        return ripsAlmacenadosList;
    }

    public void setRipsAlmacenadosList(List<RipsAlmacenados> ripsAlmacenadosList) {
        this.ripsAlmacenadosList = ripsAlmacenadosList;
    }

    @XmlTransient
    public List<FacFacturaPaciente> getFacFacturaPacienteList() {
        return facFacturaPacienteList;
    }

    public void setFacFacturaPacienteList(List<FacFacturaPaciente> facFacturaPacienteList) {
        this.facFacturaPacienteList = facFacturaPacienteList;
    }

    public FacManualTarifario getIdManualTarifario() {
        return idManualTarifario;
    }

    public void setIdManualTarifario(FacManualTarifario idManualTarifario) {
        this.idManualTarifario = idManualTarifario;
    }

    public FacAdministradora getIdAdministradora() {
        return idAdministradora;
    }

    public void setIdAdministradora(FacAdministradora idAdministradora) {
        this.idAdministradora = idAdministradora;
    }

    public CfgClasificaciones getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(CfgClasificaciones tipoPago) {
        this.tipoPago = tipoPago;
    }

    public CfgClasificaciones getTipoFacturacion() {
        return tipoFacturacion;
    }

    public void setTipoFacturacion(CfgClasificaciones tipoFacturacion) {
        this.tipoFacturacion = tipoFacturacion;
    }

    public CfgClasificaciones getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(CfgClasificaciones tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    @XmlTransient
    public List<FacPrograma> getFacProgramaList() {
        return facProgramaList;
    }

    public void setFacProgramaList(List<FacPrograma> facProgramaList) {
        this.facProgramaList = facProgramaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idContrato != null ? idContrato.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacContrato)) {
            return false;
        }
        FacContrato other = (FacContrato) object;
        if ((this.idContrato == null && other.idContrato != null) || (this.idContrato != null && !this.idContrato.equals(other.idContrato))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.FacContrato[ idContrato=" + idContrato + " ]";
    }

}
