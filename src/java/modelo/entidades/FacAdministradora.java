/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author santos
 */
@Entity
@Table(name = "fac_administradora", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacAdministradora.findAll", query = "SELECT f FROM FacAdministradora f"),
    @NamedQuery(name = "FacAdministradora.findByIdAdministradora", query = "SELECT f FROM FacAdministradora f WHERE f.idAdministradora = :idAdministradora"),
    @NamedQuery(name = "FacAdministradora.findByCodigoAdministradora", query = "SELECT f FROM FacAdministradora f WHERE f.codigoAdministradora = :codigoAdministradora"),
    @NamedQuery(name = "FacAdministradora.findByRazonSocial", query = "SELECT f FROM FacAdministradora f WHERE f.razonSocial = :razonSocial"),
    @NamedQuery(name = "FacAdministradora.findByNumeroIdentificacion", query = "SELECT f FROM FacAdministradora f WHERE f.numeroIdentificacion = :numeroIdentificacion"),
    @NamedQuery(name = "FacAdministradora.findByDigitoVerificacion", query = "SELECT f FROM FacAdministradora f WHERE f.digitoVerificacion = :digitoVerificacion"),
    @NamedQuery(name = "FacAdministradora.findByNumeroDocumentoRepresentante", query = "SELECT f FROM FacAdministradora f WHERE f.numeroDocumentoRepresentante = :numeroDocumentoRepresentante"),
    @NamedQuery(name = "FacAdministradora.findByNombreRepresentante", query = "SELECT f FROM FacAdministradora f WHERE f.nombreRepresentante = :nombreRepresentante"),
    @NamedQuery(name = "FacAdministradora.findByCodigoRip", query = "SELECT f FROM FacAdministradora f WHERE f.codigoRip = :codigoRip"),
    @NamedQuery(name = "FacAdministradora.findByDireccion", query = "SELECT f FROM FacAdministradora f WHERE f.direccion = :direccion"),
    @NamedQuery(name = "FacAdministradora.findByTelefono1", query = "SELECT f FROM FacAdministradora f WHERE f.telefono1 = :telefono1"),
    @NamedQuery(name = "FacAdministradora.findByTelefono2", query = "SELECT f FROM FacAdministradora f WHERE f.telefono2 = :telefono2"),
    @NamedQuery(name = "FacAdministradora.findByWebsite", query = "SELECT f FROM FacAdministradora f WHERE f.website = :website"),
    @NamedQuery(name = "FacAdministradora.findByCodigoIngreso", query = "SELECT f FROM FacAdministradora f WHERE f.codigoIngreso = :codigoIngreso"),
    @NamedQuery(name = "FacAdministradora.findByCodigoRubro", query = "SELECT f FROM FacAdministradora f WHERE f.codigoRubro = :codigoRubro"),
    @NamedQuery(name = "FacAdministradora.findByCodigoConc", query = "SELECT f FROM FacAdministradora f WHERE f.codigoConc = :codigoConc")})
public class FacAdministradora implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_administradora", nullable = false)
    private Integer idAdministradora;
    @Column(name = "codigo_administradora", length = 20)
    private String codigoAdministradora;
    @Column(name = "razon_social", length = 200)
    private String razonSocial;
    @Column(name = "numero_identificacion", length = 20)
    private String numeroIdentificacion;
    @Column(name = "digito_verificacion", length = 1)
    private String digitoVerificacion;
    @Column(name = "numero_documento_representante", length = 20)
    private String numeroDocumentoRepresentante;
    @Column(name = "nombre_representante", length = 200)
    private String nombreRepresentante;
    @Column(name = "codigo_rip", length = 20)
    private String codigoRip;
    @Column(name = "direccion", length = 200)
    private String direccion;
    @Column(name = "telefono_1", length = 10)
    private String telefono1;
    @Column(name = "telefono_2", length = 10)
    private String telefono2;
    @Column(name = "website", length = 60)
    private String website;
    @Column(name = "codigo_ingreso", length = 5)
    private String codigoIngreso;
    @Column(name = "codigo_rubro", length = 20)
    private String codigoRubro;
    @Column(name = "codigo_conc", length = 5)
    private String codigoConc;
    @OneToMany(mappedBy = "idAdministradora")
    private List<FacFacturaAdmi> facFacturaAdmiList;
    @OneToMany(mappedBy = "idAdministradora")
    private List<CitCitas> citCitasList;
    @OneToMany(mappedBy = "idAdministradora")
    private List<RipsAlmacenados> ripsAlmacenadosList;
    @OneToMany(mappedBy = "idAdministradora")
    private List<CfgPacientes> cfgPacientesList;
    @JoinColumn(name = "tipo_documento_representante", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones tipoDocumentoRepresentante;
    @JoinColumn(name = "tipo_documento", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones tipoDocumento;
    @JoinColumn(name = "tipo_administradora", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones tipoAdministradora;
    @JoinColumn(name = "codigo_municipio", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones codigoMunicipio;
    @JoinColumn(name = "codigo_departamento", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones codigoDepartamento;
    @OneToMany(mappedBy = "idAdministradora")
    private List<FacFacturaPaciente> facFacturaPacienteList;
    @OneToMany(mappedBy = "idAdministradora")
    private List<FacContrato> facContratoList;
    @OneToMany(mappedBy = "administradora")
    private List<CitAutorizaciones> citAutorizacionesList;

    public FacAdministradora() {
    }

    public FacAdministradora(Integer idAdministradora) {
        this.idAdministradora = idAdministradora;
    }

    public Integer getIdAdministradora() {
        return idAdministradora;
    }

    public void setIdAdministradora(Integer idAdministradora) {
        this.idAdministradora = idAdministradora;
    }

    public String getCodigoAdministradora() {
        return codigoAdministradora;
    }

    public void setCodigoAdministradora(String codigoAdministradora) {
        this.codigoAdministradora = codigoAdministradora;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getDigitoVerificacion() {
        return digitoVerificacion;
    }

    public void setDigitoVerificacion(String digitoVerificacion) {
        this.digitoVerificacion = digitoVerificacion;
    }

    public String getNumeroDocumentoRepresentante() {
        return numeroDocumentoRepresentante;
    }

    public void setNumeroDocumentoRepresentante(String numeroDocumentoRepresentante) {
        this.numeroDocumentoRepresentante = numeroDocumentoRepresentante;
    }

    public String getNombreRepresentante() {
        return nombreRepresentante;
    }

    public void setNombreRepresentante(String nombreRepresentante) {
        this.nombreRepresentante = nombreRepresentante;
    }

    public String getCodigoRip() {
        return codigoRip;
    }

    public void setCodigoRip(String codigoRip) {
        this.codigoRip = codigoRip;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCodigoIngreso() {
        return codigoIngreso;
    }

    public void setCodigoIngreso(String codigoIngreso) {
        this.codigoIngreso = codigoIngreso;
    }

    public String getCodigoRubro() {
        return codigoRubro;
    }

    public void setCodigoRubro(String codigoRubro) {
        this.codigoRubro = codigoRubro;
    }

    public String getCodigoConc() {
        return codigoConc;
    }

    public void setCodigoConc(String codigoConc) {
        this.codigoConc = codigoConc;
    }

    @XmlTransient
    public List<FacFacturaAdmi> getFacFacturaAdmiList() {
        return facFacturaAdmiList;
    }

    public void setFacFacturaAdmiList(List<FacFacturaAdmi> facFacturaAdmiList) {
        this.facFacturaAdmiList = facFacturaAdmiList;
    }

    @XmlTransient
    public List<CitCitas> getCitCitasList() {
        return citCitasList;
    }

    public void setCitCitasList(List<CitCitas> citCitasList) {
        this.citCitasList = citCitasList;
    }

    @XmlTransient
    public List<RipsAlmacenados> getRipsAlmacenadosList() {
        return ripsAlmacenadosList;
    }

    public void setRipsAlmacenadosList(List<RipsAlmacenados> ripsAlmacenadosList) {
        this.ripsAlmacenadosList = ripsAlmacenadosList;
    }

    @XmlTransient
    public List<CfgPacientes> getCfgPacientesList() {
        return cfgPacientesList;
    }

    public void setCfgPacientesList(List<CfgPacientes> cfgPacientesList) {
        this.cfgPacientesList = cfgPacientesList;
    }

    public CfgClasificaciones getTipoDocumentoRepresentante() {
        return tipoDocumentoRepresentante;
    }

    public void setTipoDocumentoRepresentante(CfgClasificaciones tipoDocumentoRepresentante) {
        this.tipoDocumentoRepresentante = tipoDocumentoRepresentante;
    }

    public CfgClasificaciones getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(CfgClasificaciones tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public CfgClasificaciones getTipoAdministradora() {
        return tipoAdministradora;
    }

    public void setTipoAdministradora(CfgClasificaciones tipoAdministradora) {
        this.tipoAdministradora = tipoAdministradora;
    }

    public CfgClasificaciones getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(CfgClasificaciones codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public CfgClasificaciones getCodigoDepartamento() {
        return codigoDepartamento;
    }

    public void setCodigoDepartamento(CfgClasificaciones codigoDepartamento) {
        this.codigoDepartamento = codigoDepartamento;
    }    

    @XmlTransient
    public List<FacFacturaPaciente> getFacFacturaPacienteList() {
        return facFacturaPacienteList;
    }

    public void setFacFacturaPacienteList(List<FacFacturaPaciente> facFacturaPacienteList) {
        this.facFacturaPacienteList = facFacturaPacienteList;
    }

    @XmlTransient
    public List<FacContrato> getFacContratoList() {
        return facContratoList;
    }

    public void setFacContratoList(List<FacContrato> facContratoList) {
        this.facContratoList = facContratoList;
    }

    @XmlTransient
    public List<CitAutorizaciones> getCitAutorizacionesList() {
        return citAutorizacionesList;
    }

    public void setCitAutorizacionesList(List<CitAutorizaciones> citAutorizacionesList) {
        this.citAutorizacionesList = citAutorizacionesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAdministradora != null ? idAdministradora.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacAdministradora)) {
            return false;
        }
        FacAdministradora other = (FacAdministradora) object;
        if ((this.idAdministradora == null && other.idAdministradora != null) || (this.idAdministradora != null && !this.idAdministradora.equals(other.idAdministradora))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.FacAdministradora[ idAdministradora=" + idAdministradora + " ]";
    }
    
}
