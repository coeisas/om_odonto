/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author santos
 */
@Entity
@Table(name = "fac_servicio", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacServicio.findAll", query = "SELECT f FROM FacServicio f"),
    @NamedQuery(name = "FacServicio.findByIdServicio", query = "SELECT f FROM FacServicio f WHERE f.idServicio = :idServicio"),
    @NamedQuery(name = "FacServicio.findByCodigoServicio", query = "SELECT f FROM FacServicio f WHERE f.codigoServicio = :codigoServicio"),
    @NamedQuery(name = "FacServicio.findByCodigoCup", query = "SELECT f FROM FacServicio f WHERE f.codigoCup = :codigoCup"),
    @NamedQuery(name = "FacServicio.findByCodigoCums", query = "SELECT f FROM FacServicio f WHERE f.codigoCums = :codigoCums"),
    @NamedQuery(name = "FacServicio.findByCodigoSoat", query = "SELECT f FROM FacServicio f WHERE f.codigoSoat = :codigoSoat"),
    @NamedQuery(name = "FacServicio.findByCodigoIss", query = "SELECT f FROM FacServicio f WHERE f.codigoIss = :codigoIss"),
    @NamedQuery(name = "FacServicio.findByNombreServicio", query = "SELECT f FROM FacServicio f WHERE f.nombreServicio = :nombreServicio"),
    @NamedQuery(name = "FacServicio.findByEdadInicial", query = "SELECT f FROM FacServicio f WHERE f.edadInicial = :edadInicial"),
    @NamedQuery(name = "FacServicio.findByEdadFinal", query = "SELECT f FROM FacServicio f WHERE f.edadFinal = :edadFinal"),
    @NamedQuery(name = "FacServicio.findByTipoPos", query = "SELECT f FROM FacServicio f WHERE f.tipoPos = :tipoPos"),
    @NamedQuery(name = "FacServicio.findByPorcentajeHonorarios", query = "SELECT f FROM FacServicio f WHERE f.porcentajeHonorarios = :porcentajeHonorarios"),
    @NamedQuery(name = "FacServicio.findByTipoServicioFurip", query = "SELECT f FROM FacServicio f WHERE f.tipoServicioFurip = :tipoServicioFurip"),
    @NamedQuery(name = "FacServicio.findByRipAplica", query = "SELECT f FROM FacServicio f WHERE f.ripAplica = :ripAplica"),
    @NamedQuery(name = "FacServicio.findByAutorizacion", query = "SELECT f FROM FacServicio f WHERE f.autorizacion = :autorizacion"),
    @NamedQuery(name = "FacServicio.findByVisible", query = "SELECT f FROM FacServicio f WHERE f.visible = :visible"),
    @NamedQuery(name = "FacServicio.findByValorParticular", query = "SELECT f FROM FacServicio f WHERE f.valorParticular = :valorParticular"),
    @NamedQuery(name = "FacServicio.findByFactorSoat", query = "SELECT f FROM FacServicio f WHERE f.factorSoat = :factorSoat"),
    @NamedQuery(name = "FacServicio.findByFactorIss", query = "SELECT f FROM FacServicio f WHERE f.factorIss = :factorIss"),
    @NamedQuery(name = "FacServicio.findByAplicaIva", query = "SELECT f FROM FacServicio f WHERE f.aplicaIva = :aplicaIva"),
    @NamedQuery(name = "FacServicio.findByAplicaCree", query = "SELECT f FROM FacServicio f WHERE f.aplicaCree = :aplicaCree")})
public class FacServicio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_servicio", nullable = false)
    private Integer idServicio;
    @Column(name = "codigo_servicio", length = 20)
    private String codigoServicio;
    @Column(name = "codigo_cup", length = 10)
    private String codigoCup;
    @Column(name = "codigo_cums", length = 10)
    private String codigoCums;
    @Column(name = "codigo_soat", length = 10)
    private String codigoSoat;
    @Column(name = "codigo_iss", length = 10)
    private String codigoIss;
    @Column(name = "nombre_servicio", length = 150)
    private String nombreServicio;
    @Column(name = "edad_inicial")
    private Integer edadInicial;
    @Column(name = "edad_final")
    private Integer edadFinal;
    @Column(name = "tipo_pos")
    private Boolean tipoPos;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "porcentaje_honorarios", precision = 17, scale = 17)
    private Double porcentajeHonorarios;
    @Column(name = "tipo_servicio_furip", length = 2)
    private String tipoServicioFurip;
    @Column(name = "rip_aplica", length = 1)
    private String ripAplica;
    @Column(name = "autorizacion")
    private Boolean autorizacion;
    @Column(name = "visible")
    private Boolean visible;
    @Column(name = "valor_particular", precision = 17, scale = 17)
    private Double valorParticular;
    @Column(name = "factor_soat", precision = 17, scale = 17)
    private Double factorSoat;
    @Column(name = "factor_iss", precision = 17, scale = 17)
    private Double factorIss;
    @Column(name = "aplica_iva")
    private Boolean aplicaIva;
    @Column(name = "aplica_cree")
    private Boolean aplicaCree;
    @OneToMany(mappedBy = "idServicio")
    private List<CitCitas> citCitasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idServicio")
    private List<CitPaqDetalle> citPaqDetalleList;
    @JoinColumn(name = "codigo_diagnostico", referencedColumnName = "codigo_diagnostico")
    @ManyToOne
    private CfgDiagnostico codigoDiagnostico;
    @JoinColumn(name = "unidad_edad_final", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones unidadEdadFinal;
    @JoinColumn(name = "unidad_edad_inicial", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones unidadEdadInicial;
    @JoinColumn(name = "sexo", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones sexo;
    @JoinColumn(name = "finalidad", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones finalidad;
    @JoinColumn(name = "tipo_sevicio", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones tipoSevicio;
    @JoinColumn(name = "grupo_servicio", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones grupoServicio;
    @JoinColumn(name = "especialidad", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones especialidad;
    @JoinColumn(name = "acto_quirurgico", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones actoQuirurgico;
    @JoinColumn(name = "zona", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones zona;
    @JoinColumn(name = "ambito", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones ambito;
    @JoinColumn(name = "centro_costo", referencedColumnName = "id_centro_costo")
    @ManyToOne
    private CfgCentroCosto centroCosto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facServicio")
    private List<CitAutorizacionesServicios> citAutorizacionesServiciosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facServicio")
    private List<FacManualTarifarioServicio> facManualTarifarioServicioList;
    @OneToMany(mappedBy = "idServicio")
    private List<FacFacturaServicio> facFacturaServicioList;
    @OneToMany(mappedBy = "idServicio")
    private List<FacConsumoServicio> facConsumoServicioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facServicio")
    private List<FacPaqueteServicio> facPaqueteServicioList;

    public FacServicio() {
    }

    public FacServicio(Integer idServicio) {
        this.idServicio = idServicio;
    }

    public Integer getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Integer idServicio) {
        this.idServicio = idServicio;
    }

    public String getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(String codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public String getCodigoCup() {
        return codigoCup;
    }

    public void setCodigoCup(String codigoCup) {
        this.codigoCup = codigoCup;
    }

    public String getCodigoCums() {
        return codigoCums;
    }

    public void setCodigoCums(String codigoCums) {
        this.codigoCums = codigoCums;
    }

    public String getCodigoSoat() {
        return codigoSoat;
    }

    public void setCodigoSoat(String codigoSoat) {
        this.codigoSoat = codigoSoat;
    }

    public String getCodigoIss() {
        return codigoIss;
    }

    public void setCodigoIss(String codigoIss) {
        this.codigoIss = codigoIss;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public Integer getEdadInicial() {
        return edadInicial;
    }

    public void setEdadInicial(Integer edadInicial) {
        this.edadInicial = edadInicial;
    }

    public Integer getEdadFinal() {
        return edadFinal;
    }

    public void setEdadFinal(Integer edadFinal) {
        this.edadFinal = edadFinal;
    }

    public Boolean getTipoPos() {
        return tipoPos;
    }

    public void setTipoPos(Boolean tipoPos) {
        this.tipoPos = tipoPos;
    }

    public Double getPorcentajeHonorarios() {
        return porcentajeHonorarios;
    }

    public void setPorcentajeHonorarios(Double porcentajeHonorarios) {
        this.porcentajeHonorarios = porcentajeHonorarios;
    }

    public String getTipoServicioFurip() {
        return tipoServicioFurip;
    }

    public void setTipoServicioFurip(String tipoServicioFurip) {
        this.tipoServicioFurip = tipoServicioFurip;
    }

    public String getRipAplica() {
        return ripAplica;
    }

    public void setRipAplica(String ripAplica) {
        this.ripAplica = ripAplica;
    }

    public Boolean getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(Boolean autorizacion) {
        this.autorizacion = autorizacion;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Double getValorParticular() {
        return valorParticular;
    }

    public void setValorParticular(Double valorParticular) {
        this.valorParticular = valorParticular;
    }

    public Double getFactorSoat() {
        return factorSoat;
    }

    public void setFactorSoat(Double factorSoat) {
        this.factorSoat = factorSoat;
    }

    public Double getFactorIss() {
        return factorIss;
    }

    public void setFactorIss(Double factorIss) {
        this.factorIss = factorIss;
    }

    public Boolean getAplicaIva() {
        return aplicaIva;
    }

    public void setAplicaIva(Boolean aplicaIva) {
        this.aplicaIva = aplicaIva;
    }

    public Boolean getAplicaCree() {
        return aplicaCree;
    }

    public void setAplicaCree(Boolean aplicaCree) {
        this.aplicaCree = aplicaCree;
    }

    @XmlTransient
    public List<CitCitas> getCitCitasList() {
        return citCitasList;
    }

    public void setCitCitasList(List<CitCitas> citCitasList) {
        this.citCitasList = citCitasList;
    }

    @XmlTransient
    public List<CitPaqDetalle> getCitPaqDetalleList() {
        return citPaqDetalleList;
    }

    public void setCitPaqDetalleList(List<CitPaqDetalle> citPaqDetalleList) {
        this.citPaqDetalleList = citPaqDetalleList;
    }

    public CfgDiagnostico getCodigoDiagnostico() {
        return codigoDiagnostico;
    }

    public void setCodigoDiagnostico(CfgDiagnostico codigoDiagnostico) {
        this.codigoDiagnostico = codigoDiagnostico;
    }

    public CfgClasificaciones getUnidadEdadFinal() {
        return unidadEdadFinal;
    }

    public void setUnidadEdadFinal(CfgClasificaciones unidadEdadFinal) {
        this.unidadEdadFinal = unidadEdadFinal;
    }

    public CfgClasificaciones getUnidadEdadInicial() {
        return unidadEdadInicial;
    }

    public void setUnidadEdadInicial(CfgClasificaciones unidadEdadInicial) {
        this.unidadEdadInicial = unidadEdadInicial;
    }

    public CfgClasificaciones getSexo() {
        return sexo;
    }

    public void setSexo(CfgClasificaciones sexo) {
        this.sexo = sexo;
    }

    public CfgClasificaciones getFinalidad() {
        return finalidad;
    }

    public void setFinalidad(CfgClasificaciones finalidad) {
        this.finalidad = finalidad;
    }

    public CfgClasificaciones getTipoSevicio() {
        return tipoSevicio;
    }

    public void setTipoSevicio(CfgClasificaciones tipoSevicio) {
        this.tipoSevicio = tipoSevicio;
    }

    public CfgClasificaciones getGrupoServicio() {
        return grupoServicio;
    }

    public void setGrupoServicio(CfgClasificaciones grupoServicio) {
        this.grupoServicio = grupoServicio;
    }

    public CfgClasificaciones getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(CfgClasificaciones especialidad) {
        this.especialidad = especialidad;
    }

    public CfgClasificaciones getActoQuirurgico() {
        return actoQuirurgico;
    }

    public void setActoQuirurgico(CfgClasificaciones actoQuirurgico) {
        this.actoQuirurgico = actoQuirurgico;
    }

    public CfgClasificaciones getZona() {
        return zona;
    }

    public void setZona(CfgClasificaciones zona) {
        this.zona = zona;
    }

    public CfgClasificaciones getAmbito() {
        return ambito;
    }

    public void setAmbito(CfgClasificaciones ambito) {
        this.ambito = ambito;
    }

    public CfgCentroCosto getCentroCosto() {
        return centroCosto;
    }

    public void setCentroCosto(CfgCentroCosto centroCosto) {
        this.centroCosto = centroCosto;
    }

    @XmlTransient
    public List<CitAutorizacionesServicios> getCitAutorizacionesServiciosList() {
        return citAutorizacionesServiciosList;
    }

    public void setCitAutorizacionesServiciosList(List<CitAutorizacionesServicios> citAutorizacionesServiciosList) {
        this.citAutorizacionesServiciosList = citAutorizacionesServiciosList;
    }

    @XmlTransient
    public List<FacManualTarifarioServicio> getFacManualTarifarioServicioList() {
        return facManualTarifarioServicioList;
    }

    public void setFacManualTarifarioServicioList(List<FacManualTarifarioServicio> facManualTarifarioServicioList) {
        this.facManualTarifarioServicioList = facManualTarifarioServicioList;
    }

    @XmlTransient
    public List<FacFacturaServicio> getFacFacturaServicioList() {
        return facFacturaServicioList;
    }

    public void setFacFacturaServicioList(List<FacFacturaServicio> facFacturaServicioList) {
        this.facFacturaServicioList = facFacturaServicioList;
    }

    @XmlTransient
    public List<FacConsumoServicio> getFacConsumoServicioList() {
        return facConsumoServicioList;
    }

    public void setFacConsumoServicioList(List<FacConsumoServicio> facConsumoServicioList) {
        this.facConsumoServicioList = facConsumoServicioList;
    }

    @XmlTransient
    public List<FacPaqueteServicio> getFacPaqueteServicioList() {
        return facPaqueteServicioList;
    }

    public void setFacPaqueteServicioList(List<FacPaqueteServicio> facPaqueteServicioList) {
        this.facPaqueteServicioList = facPaqueteServicioList;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idServicio != null ? idServicio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacServicio)) {
            return false;
        }
        FacServicio other = (FacServicio) object;
        if ((this.idServicio == null && other.idServicio != null) || (this.idServicio != null && !this.idServicio.equals(other.idServicio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.FacServicio[ idServicio=" + idServicio + " ]";
    }

}
