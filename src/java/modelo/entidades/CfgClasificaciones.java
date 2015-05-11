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
@Table(name = "cfg_clasificaciones", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgClasificaciones.findAll", query = "SELECT c FROM CfgClasificaciones c"),
    @NamedQuery(name = "CfgClasificaciones.findById", query = "SELECT c FROM CfgClasificaciones c WHERE c.id = :id"),
    @NamedQuery(name = "CfgClasificaciones.findByCodigo", query = "SELECT c FROM CfgClasificaciones c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "CfgClasificaciones.findByDescripcion", query = "SELECT c FROM CfgClasificaciones c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "CfgClasificaciones.findByObservacion", query = "SELECT c FROM CfgClasificaciones c WHERE c.observacion = :observacion")})
public class CfgClasificaciones implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "codigo", length = 20)
    private String codigo;
    @Column(name = "descripcion", length = 2147483647)
    private String descripcion;
    @Column(name = "observacion", length = 2147483647)
    private String observacion;
    @OneToMany(mappedBy = "tipoDocumento")
    private List<FacFacturaAdmi> facFacturaAdmiList;
    @OneToMany(mappedBy = "motivoCancelacion")
    private List<CitCitas> citCitasList;
    @OneToMany(mappedBy = "tipoCita")
    private List<CitCitas> citCitasList1;
    @OneToMany(mappedBy = "unidadEdadFinal")
    private List<FacServicio> facServicioList;
    @OneToMany(mappedBy = "unidadEdadInicial")
    private List<FacServicio> facServicioList1;
    @OneToMany(mappedBy = "sexo")
    private List<FacServicio> facServicioList2;
    @OneToMany(mappedBy = "finalidad")
    private List<FacServicio> facServicioList3;
    @OneToMany(mappedBy = "tipoSevicio")
    private List<FacServicio> facServicioList4;
    @OneToMany(mappedBy = "grupoServicio")
    private List<FacServicio> facServicioList5;
    @OneToMany(mappedBy = "especialidad")
    private List<FacServicio> facServicioList6;
    @OneToMany(mappedBy = "actoQuirurgico")
    private List<FacServicio> facServicioList7;
    @OneToMany(mappedBy = "zona")
    private List<FacServicio> facServicioList8;
    @OneToMany(mappedBy = "ambito")
    private List<FacServicio> facServicioList9;
    @OneToMany(mappedBy = "regimen")
    private List<CfgPacientes> cfgPacientesList;
    @OneToMany(mappedBy = "ocupacion")
    private List<CfgPacientes> cfgPacientesList1;
    @OneToMany(mappedBy = "nivel")
    private List<CfgPacientes> cfgPacientesList2;
    @OneToMany(mappedBy = "grupoSanguineo")
    private List<CfgPacientes> cfgPacientesList3;
    @OneToMany(mappedBy = "genero")
    private List<CfgPacientes> cfgPacientesList4;
    @OneToMany(mappedBy = "categoriaPaciente")
    private List<CfgPacientes> cfgPacientesList5;
    @OneToMany(mappedBy = "depNacimiento")
    private List<CfgPacientes> cfgPacientesList6;
    @OneToMany(mappedBy = "tipoAfiliado")
    private List<CfgPacientes> cfgPacientesList7;
    @OneToMany(mappedBy = "tipoIdentificacion")
    private List<CfgPacientes> cfgPacientesList8;
    @OneToMany(mappedBy = "zona")
    private List<CfgPacientes> cfgPacientesList9;
    @OneToMany(mappedBy = "munNacimiento")
    private List<CfgPacientes> cfgPacientesList10;
    @OneToMany(mappedBy = "municipio")
    private List<CfgPacientes> cfgPacientesList11;
    @OneToMany(mappedBy = "parentesco")
    private List<CfgPacientes> cfgPacientesList12;
    @OneToMany(mappedBy = "departamento")
    private List<CfgPacientes> cfgPacientesList13;
    @OneToMany(mappedBy = "escolaridad")
    private List<CfgPacientes> cfgPacientesList14;
    @OneToMany(mappedBy = "estadoCivil")
    private List<CfgPacientes> cfgPacientesList15;
    @OneToMany(mappedBy = "etnia")
    private List<CfgPacientes> cfgPacientesList16;
    @OneToMany(mappedBy = "tipoDocumentoRepresentante")
    private List<FacAdministradora> facAdministradoraList;
    @OneToMany(mappedBy = "tipoDocumento")
    private List<FacAdministradora> facAdministradoraList1;
    @OneToMany(mappedBy = "tipoAdministradora")
    private List<FacAdministradora> facAdministradoraList2;
    @OneToMany(mappedBy = "codigoMunicipio")
    private List<FacAdministradora> facAdministradoraList3;
    @OneToMany(mappedBy = "codigoDepartamento")
    private List<FacAdministradora> facAdministradoraList4;
    @JoinColumn(name = "maestro", referencedColumnName = "maestro")
    @ManyToOne
    private CfgMaestrosClasificaciones maestro;
    @OneToMany(mappedBy = "tipoIngreso")
    private List<FacFacturaPaciente> facFacturaPacienteList;
    @OneToMany(mappedBy = "tipoDocumento")
    private List<FacFacturaPaciente> facFacturaPacienteList1;
    @OneToMany(mappedBy = "tipoDocumento")
    private List<FacConsecutivo> facConsecutivoList;
    @OneToMany(mappedBy = "tipoPago")
    private List<FacContrato> facContratoList;
    @OneToMany(mappedBy = "tipoFacturacion")
    private List<FacContrato> facContratoList1;
    @OneToMany(mappedBy = "tipoContrato")
    private List<FacContrato> facContratoList2;
    @OneToMany(mappedBy = "tipoDocRepLegal")
    private List<CfgEmpresa> cfgEmpresaList;
    @OneToMany(mappedBy = "tipoDoc")
    private List<CfgEmpresa> cfgEmpresaList1;
    @OneToMany(mappedBy = "codMunicipio")
    private List<CfgEmpresa> cfgEmpresaList2;
    @OneToMany(mappedBy = "codDepartamento")
    private List<CfgEmpresa> cfgEmpresaList3;
    @OneToMany(mappedBy = "tipoIdentificacion")
    private List<CfgUsuarios> cfgUsuariosList;
    @OneToMany(mappedBy = "municipio")
    private List<CfgUsuarios> cfgUsuariosList1;
    @OneToMany(mappedBy = "departamento")
    private List<CfgUsuarios> cfgUsuariosList2;
    @OneToMany(mappedBy = "tipoUsuario")
    private List<CfgUsuarios> cfgUsuariosList3;
    @OneToMany(mappedBy = "personalAtiende")
    private List<CfgUsuarios> cfgUsuariosList4;
    @OneToMany(mappedBy = "genero")
    private List<CfgUsuarios> cfgUsuariosList5;
    @OneToMany(mappedBy = "especialidad")
    private List<CfgUsuarios> cfgUsuariosList6;
    @OneToMany(mappedBy = "codEspecialidad")
    private List<CfgConsultorios> cfgConsultoriosList;
    @OneToMany(mappedBy = "municipio")
    private List<CfgSede> cfgSedeList;
    @OneToMany(mappedBy = "departamento")
    private List<CfgSede> cfgSedeList1;
    @OneToMany(mappedBy = "finalidadProcedimineto")
    private List<FacPrograma> facProgramaList;
    @OneToMany(mappedBy = "finalidadConsulta")
    private List<FacPrograma> facProgramaList1;
    @OneToMany(mappedBy = "causaExterna")
    private List<FacPrograma> facProgramaList2;

    public CfgClasificaciones() {
    }

    public CfgClasificaciones(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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
    public List<CitCitas> getCitCitasList1() {
        return citCitasList1;
    }

    public void setCitCitasList1(List<CitCitas> citCitasList1) {
        this.citCitasList1 = citCitasList1;
    }

    @XmlTransient
    public List<FacServicio> getFacServicioList() {
        return facServicioList;
    }

    public void setFacServicioList(List<FacServicio> facServicioList) {
        this.facServicioList = facServicioList;
    }

    @XmlTransient
    public List<FacServicio> getFacServicioList1() {
        return facServicioList1;
    }

    public void setFacServicioList1(List<FacServicio> facServicioList1) {
        this.facServicioList1 = facServicioList1;
    }

    @XmlTransient
    public List<FacServicio> getFacServicioList2() {
        return facServicioList2;
    }

    public void setFacServicioList2(List<FacServicio> facServicioList2) {
        this.facServicioList2 = facServicioList2;
    }

    @XmlTransient
    public List<FacServicio> getFacServicioList3() {
        return facServicioList3;
    }

    public void setFacServicioList3(List<FacServicio> facServicioList3) {
        this.facServicioList3 = facServicioList3;
    }

    @XmlTransient
    public List<FacServicio> getFacServicioList4() {
        return facServicioList4;
    }

    public void setFacServicioList4(List<FacServicio> facServicioList4) {
        this.facServicioList4 = facServicioList4;
    }

    @XmlTransient
    public List<FacServicio> getFacServicioList5() {
        return facServicioList5;
    }

    public void setFacServicioList5(List<FacServicio> facServicioList5) {
        this.facServicioList5 = facServicioList5;
    }

    @XmlTransient
    public List<FacServicio> getFacServicioList6() {
        return facServicioList6;
    }

    public void setFacServicioList6(List<FacServicio> facServicioList6) {
        this.facServicioList6 = facServicioList6;
    }

    @XmlTransient
    public List<FacServicio> getFacServicioList7() {
        return facServicioList7;
    }

    public void setFacServicioList7(List<FacServicio> facServicioList7) {
        this.facServicioList7 = facServicioList7;
    }

    @XmlTransient
    public List<FacServicio> getFacServicioList8() {
        return facServicioList8;
    }

    public void setFacServicioList8(List<FacServicio> facServicioList8) {
        this.facServicioList8 = facServicioList8;
    }

    @XmlTransient
    public List<FacServicio> getFacServicioList9() {
        return facServicioList9;
    }

    public void setFacServicioList9(List<FacServicio> facServicioList9) {
        this.facServicioList9 = facServicioList9;
    }

    @XmlTransient
    public List<CfgPacientes> getCfgPacientesList() {
        return cfgPacientesList;
    }

    public void setCfgPacientesList(List<CfgPacientes> cfgPacientesList) {
        this.cfgPacientesList = cfgPacientesList;
    }

    @XmlTransient
    public List<CfgPacientes> getCfgPacientesList1() {
        return cfgPacientesList1;
    }

    public void setCfgPacientesList1(List<CfgPacientes> cfgPacientesList1) {
        this.cfgPacientesList1 = cfgPacientesList1;
    }

    @XmlTransient
    public List<CfgPacientes> getCfgPacientesList2() {
        return cfgPacientesList2;
    }

    public void setCfgPacientesList2(List<CfgPacientes> cfgPacientesList2) {
        this.cfgPacientesList2 = cfgPacientesList2;
    }

    @XmlTransient
    public List<CfgPacientes> getCfgPacientesList3() {
        return cfgPacientesList3;
    }

    public void setCfgPacientesList3(List<CfgPacientes> cfgPacientesList3) {
        this.cfgPacientesList3 = cfgPacientesList3;
    }

    @XmlTransient
    public List<CfgPacientes> getCfgPacientesList4() {
        return cfgPacientesList4;
    }

    public void setCfgPacientesList4(List<CfgPacientes> cfgPacientesList4) {
        this.cfgPacientesList4 = cfgPacientesList4;
    }

    @XmlTransient
    public List<CfgPacientes> getCfgPacientesList5() {
        return cfgPacientesList5;
    }

    public void setCfgPacientesList5(List<CfgPacientes> cfgPacientesList5) {
        this.cfgPacientesList5 = cfgPacientesList5;
    }

    @XmlTransient
    public List<CfgPacientes> getCfgPacientesList6() {
        return cfgPacientesList6;
    }

    public void setCfgPacientesList6(List<CfgPacientes> cfgPacientesList6) {
        this.cfgPacientesList6 = cfgPacientesList6;
    }

    @XmlTransient
    public List<CfgPacientes> getCfgPacientesList7() {
        return cfgPacientesList7;
    }

    public void setCfgPacientesList7(List<CfgPacientes> cfgPacientesList7) {
        this.cfgPacientesList7 = cfgPacientesList7;
    }

    @XmlTransient
    public List<CfgPacientes> getCfgPacientesList8() {
        return cfgPacientesList8;
    }

    public void setCfgPacientesList8(List<CfgPacientes> cfgPacientesList8) {
        this.cfgPacientesList8 = cfgPacientesList8;
    }

    @XmlTransient
    public List<CfgPacientes> getCfgPacientesList9() {
        return cfgPacientesList9;
    }

    public void setCfgPacientesList9(List<CfgPacientes> cfgPacientesList9) {
        this.cfgPacientesList9 = cfgPacientesList9;
    }

    @XmlTransient
    public List<CfgPacientes> getCfgPacientesList10() {
        return cfgPacientesList10;
    }

    public void setCfgPacientesList10(List<CfgPacientes> cfgPacientesList10) {
        this.cfgPacientesList10 = cfgPacientesList10;
    }

    @XmlTransient
    public List<CfgPacientes> getCfgPacientesList11() {
        return cfgPacientesList11;
    }

    public void setCfgPacientesList11(List<CfgPacientes> cfgPacientesList11) {
        this.cfgPacientesList11 = cfgPacientesList11;
    }

    @XmlTransient
    public List<CfgPacientes> getCfgPacientesList12() {
        return cfgPacientesList12;
    }

    public void setCfgPacientesList12(List<CfgPacientes> cfgPacientesList12) {
        this.cfgPacientesList12 = cfgPacientesList12;
    }

    @XmlTransient
    public List<CfgPacientes> getCfgPacientesList13() {
        return cfgPacientesList13;
    }

    public void setCfgPacientesList13(List<CfgPacientes> cfgPacientesList13) {
        this.cfgPacientesList13 = cfgPacientesList13;
    }

    @XmlTransient
    public List<CfgPacientes> getCfgPacientesList14() {
        return cfgPacientesList14;
    }

    public void setCfgPacientesList14(List<CfgPacientes> cfgPacientesList14) {
        this.cfgPacientesList14 = cfgPacientesList14;
    }

    @XmlTransient
    public List<CfgPacientes> getCfgPacientesList15() {
        return cfgPacientesList15;
    }

    public void setCfgPacientesList15(List<CfgPacientes> cfgPacientesList15) {
        this.cfgPacientesList15 = cfgPacientesList15;
    }

    @XmlTransient
    public List<CfgPacientes> getCfgPacientesList16() {
        return cfgPacientesList16;
    }

    public void setCfgPacientesList16(List<CfgPacientes> cfgPacientesList16) {
        this.cfgPacientesList16 = cfgPacientesList16;
    }

    @XmlTransient
    public List<FacAdministradora> getFacAdministradoraList() {
        return facAdministradoraList;
    }

    public void setFacAdministradoraList(List<FacAdministradora> facAdministradoraList) {
        this.facAdministradoraList = facAdministradoraList;
    }

    @XmlTransient
    public List<FacAdministradora> getFacAdministradoraList1() {
        return facAdministradoraList1;
    }

    public void setFacAdministradoraList1(List<FacAdministradora> facAdministradoraList1) {
        this.facAdministradoraList1 = facAdministradoraList1;
    }

    @XmlTransient
    public List<FacAdministradora> getFacAdministradoraList2() {
        return facAdministradoraList2;
    }

    public void setFacAdministradoraList2(List<FacAdministradora> facAdministradoraList2) {
        this.facAdministradoraList2 = facAdministradoraList2;
    }

    @XmlTransient
    public List<FacAdministradora> getFacAdministradoraList3() {
        return facAdministradoraList3;
    }

    public void setFacAdministradoraList3(List<FacAdministradora> facAdministradoraList3) {
        this.facAdministradoraList3 = facAdministradoraList3;
    }

    @XmlTransient
    public List<FacAdministradora> getFacAdministradoraList4() {
        return facAdministradoraList4;
    }

    public void setFacAdministradoraList4(List<FacAdministradora> facAdministradoraList4) {
        this.facAdministradoraList4 = facAdministradoraList4;
    }

    public CfgMaestrosClasificaciones getMaestro() {
        return maestro;
    }

    public void setMaestro(CfgMaestrosClasificaciones maestro) {
        this.maestro = maestro;
    }

    @XmlTransient
    public List<FacFacturaPaciente> getFacFacturaPacienteList() {
        return facFacturaPacienteList;
    }

    public void setFacFacturaPacienteList(List<FacFacturaPaciente> facFacturaPacienteList) {
        this.facFacturaPacienteList = facFacturaPacienteList;
    }

    @XmlTransient
    public List<FacFacturaPaciente> getFacFacturaPacienteList1() {
        return facFacturaPacienteList1;
    }

    public void setFacFacturaPacienteList1(List<FacFacturaPaciente> facFacturaPacienteList1) {
        this.facFacturaPacienteList1 = facFacturaPacienteList1;
    }

    @XmlTransient
    public List<FacConsecutivo> getFacConsecutivoList() {
        return facConsecutivoList;
    }

    public void setFacConsecutivoList(List<FacConsecutivo> facConsecutivoList) {
        this.facConsecutivoList = facConsecutivoList;
    }

    @XmlTransient
    public List<FacContrato> getFacContratoList() {
        return facContratoList;
    }

    public void setFacContratoList(List<FacContrato> facContratoList) {
        this.facContratoList = facContratoList;
    }

    @XmlTransient
    public List<FacContrato> getFacContratoList1() {
        return facContratoList1;
    }

    public void setFacContratoList1(List<FacContrato> facContratoList1) {
        this.facContratoList1 = facContratoList1;
    }

    @XmlTransient
    public List<FacContrato> getFacContratoList2() {
        return facContratoList2;
    }

    public void setFacContratoList2(List<FacContrato> facContratoList2) {
        this.facContratoList2 = facContratoList2;
    }

    @XmlTransient
    public List<CfgEmpresa> getCfgEmpresaList() {
        return cfgEmpresaList;
    }

    public void setCfgEmpresaList(List<CfgEmpresa> cfgEmpresaList) {
        this.cfgEmpresaList = cfgEmpresaList;
    }

    @XmlTransient
    public List<CfgEmpresa> getCfgEmpresaList1() {
        return cfgEmpresaList1;
    }

    public void setCfgEmpresaList1(List<CfgEmpresa> cfgEmpresaList1) {
        this.cfgEmpresaList1 = cfgEmpresaList1;
    }

    @XmlTransient
    public List<CfgEmpresa> getCfgEmpresaList2() {
        return cfgEmpresaList2;
    }

    public void setCfgEmpresaList2(List<CfgEmpresa> cfgEmpresaList2) {
        this.cfgEmpresaList2 = cfgEmpresaList2;
    }

    @XmlTransient
    public List<CfgEmpresa> getCfgEmpresaList3() {
        return cfgEmpresaList3;
    }

    public void setCfgEmpresaList3(List<CfgEmpresa> cfgEmpresaList3) {
        this.cfgEmpresaList3 = cfgEmpresaList3;
    }

    @XmlTransient
    public List<CfgUsuarios> getCfgUsuariosList() {
        return cfgUsuariosList;
    }

    public void setCfgUsuariosList(List<CfgUsuarios> cfgUsuariosList) {
        this.cfgUsuariosList = cfgUsuariosList;
    }

    @XmlTransient
    public List<CfgUsuarios> getCfgUsuariosList1() {
        return cfgUsuariosList1;
    }

    public void setCfgUsuariosList1(List<CfgUsuarios> cfgUsuariosList1) {
        this.cfgUsuariosList1 = cfgUsuariosList1;
    }

    @XmlTransient
    public List<CfgUsuarios> getCfgUsuariosList2() {
        return cfgUsuariosList2;
    }

    public void setCfgUsuariosList2(List<CfgUsuarios> cfgUsuariosList2) {
        this.cfgUsuariosList2 = cfgUsuariosList2;
    }

    @XmlTransient
    public List<CfgUsuarios> getCfgUsuariosList3() {
        return cfgUsuariosList3;
    }

    public void setCfgUsuariosList3(List<CfgUsuarios> cfgUsuariosList3) {
        this.cfgUsuariosList3 = cfgUsuariosList3;
    }

    @XmlTransient
    public List<CfgUsuarios> getCfgUsuariosList4() {
        return cfgUsuariosList4;
    }

    public void setCfgUsuariosList4(List<CfgUsuarios> cfgUsuariosList4) {
        this.cfgUsuariosList4 = cfgUsuariosList4;
    }
    
    @XmlTransient
    public List<CfgUsuarios> getCfgUsuariosList5() {
        return cfgUsuariosList5;
    }

    public void setCfgUsuariosList5(List<CfgUsuarios> cfgUsuariosList5) {
        this.cfgUsuariosList5 = cfgUsuariosList5;
    }

    @XmlTransient
    public List<CfgUsuarios> getCfgUsuariosList6() {
        return cfgUsuariosList6;
    }

    public void setCfgUsuariosList6(List<CfgUsuarios> cfgUsuariosList6) {
        this.cfgUsuariosList6 = cfgUsuariosList6;
    }

    @XmlTransient
    public List<CfgConsultorios> getCfgConsultoriosList() {
        return cfgConsultoriosList;
    }

    public void setCfgConsultoriosList(List<CfgConsultorios> cfgConsultoriosList) {
        this.cfgConsultoriosList = cfgConsultoriosList;
    }

    @XmlTransient
    public List<CfgSede> getCfgSedeList() {
        return cfgSedeList;
    }

    public void setCfgSedeList(List<CfgSede> cfgSedeList) {
        this.cfgSedeList = cfgSedeList;
    }

    @XmlTransient
    public List<CfgSede> getCfgSedeList1() {
        return cfgSedeList1;
    }

    public void setCfgSedeList1(List<CfgSede> cfgSedeList1) {
        this.cfgSedeList1 = cfgSedeList1;
    }

    @XmlTransient
    public List<FacPrograma> getFacProgramaList() {
        return facProgramaList;
    }

    public void setFacProgramaList(List<FacPrograma> facProgramaList) {
        this.facProgramaList = facProgramaList;
    }

    @XmlTransient
    public List<FacPrograma> getFacProgramaList1() {
        return facProgramaList1;
    }

    public void setFacProgramaList1(List<FacPrograma> facProgramaList1) {
        this.facProgramaList1 = facProgramaList1;
    }

    @XmlTransient
    public List<FacPrograma> getFacProgramaList2() {
        return facProgramaList2;
    }

    public void setFacProgramaList2(List<FacPrograma> facProgramaList2) {
        this.facProgramaList2 = facProgramaList2;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgClasificaciones)) {
            return false;
        }
        CfgClasificaciones other = (CfgClasificaciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.CfgClasificaciones[ id=" + id + " ]";
    }
    
}
