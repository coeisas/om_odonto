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
@Table(name = "cfg_consultorios", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgConsultorios.findAll", query = "SELECT c FROM CfgConsultorios c"),
    @NamedQuery(name = "CfgConsultorios.findByIdConsultorio", query = "SELECT c FROM CfgConsultorios c WHERE c.idConsultorio = :idConsultorio"),
    @NamedQuery(name = "CfgConsultorios.findByCodConsultorio", query = "SELECT c FROM CfgConsultorios c WHERE c.codConsultorio = :codConsultorio"),
    @NamedQuery(name = "CfgConsultorios.findByNomConsultorio", query = "SELECT c FROM CfgConsultorios c WHERE c.nomConsultorio = :nomConsultorio"),
    @NamedQuery(name = "CfgConsultorios.findByPisoConsultorio", query = "SELECT c FROM CfgConsultorios c WHERE c.pisoConsultorio = :pisoConsultorio")})
public class CfgConsultorios implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_consultorio", nullable = false)
    private Integer idConsultorio;
    @Basic(optional = false)
    @Column(name = "cod_consultorio", nullable = false, length = 5)
    private String codConsultorio;
    @Column(name = "nom_consultorio", length = 2147483647)
    private String nomConsultorio;
    @Column(name = "piso_consultorio")
    private Integer pisoConsultorio;
    @OneToMany(mappedBy = "idConsultorio")
    private List<FacFacturaPaquete> facFacturaPaqueteList;
    @OneToMany(mappedBy = "idConsultorio")
    private List<FacFacturaMedicamento> facFacturaMedicamentoList;
    @OneToMany(mappedBy = "idConsultorio")
    private List<FacFacturaInsumo> facFacturaInsumoList;
    @OneToMany(mappedBy = "idConsultorio")
    private List<CitTurnos> citTurnosList;
    @OneToMany(mappedBy = "idConsultorio")
    private List<FacFacturaServicio> facFacturaServicioList;
    @JoinColumn(name = "id_sede", referencedColumnName = "id_sede")
    @ManyToOne
    private CfgSede idSede;
    @JoinColumn(name = "cod_especialidad", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones codEspecialidad;

    public CfgConsultorios() {
    }

    public CfgConsultorios(Integer idConsultorio) {
        this.idConsultorio = idConsultorio;
    }

    public CfgConsultorios(Integer idConsultorio, String codConsultorio) {
        this.idConsultorio = idConsultorio;
        this.codConsultorio = codConsultorio;
    }

    public Integer getIdConsultorio() {
        return idConsultorio;
    }

    public void setIdConsultorio(Integer idConsultorio) {
        this.idConsultorio = idConsultorio;
    }

    public String getCodConsultorio() {
        return codConsultorio;
    }

    public void setCodConsultorio(String codConsultorio) {
        this.codConsultorio = codConsultorio;
    }

    public String getNomConsultorio() {
        return nomConsultorio;
    }

    public void setNomConsultorio(String nomConsultorio) {
        this.nomConsultorio = nomConsultorio;
    }

    public Integer getPisoConsultorio() {
        return pisoConsultorio;
    }

    public void setPisoConsultorio(Integer pisoConsultorio) {
        this.pisoConsultorio = pisoConsultorio;
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

    @XmlTransient
    public List<FacFacturaInsumo> getFacFacturaInsumoList() {
        return facFacturaInsumoList;
    }

    public void setFacFacturaInsumoList(List<FacFacturaInsumo> facFacturaInsumoList) {
        this.facFacturaInsumoList = facFacturaInsumoList;
    }

    @XmlTransient
    public List<CitTurnos> getCitTurnosList() {
        return citTurnosList;
    }

    public void setCitTurnosList(List<CitTurnos> citTurnosList) {
        this.citTurnosList = citTurnosList;
    }

    @XmlTransient
    public List<FacFacturaServicio> getFacFacturaServicioList() {
        return facFacturaServicioList;
    }

    public void setFacFacturaServicioList(List<FacFacturaServicio> facFacturaServicioList) {
        this.facFacturaServicioList = facFacturaServicioList;
    }

    public CfgSede getIdSede() {
        return idSede;
    }

    public void setIdSede(CfgSede idSede) {
        this.idSede = idSede;
    }

    public CfgClasificaciones getCodEspecialidad() {
        return codEspecialidad;
    }

    public void setCodEspecialidad(CfgClasificaciones codEspecialidad) {
        this.codEspecialidad = codEspecialidad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConsultorio != null ? idConsultorio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgConsultorios)) {
            return false;
        }
        CfgConsultorios other = (CfgConsultorios) object;
        if ((this.idConsultorio == null && other.idConsultorio != null) || (this.idConsultorio != null && !this.idConsultorio.equals(other.idConsultorio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.CfgConsultorios[ idConsultorio=" + idConsultorio + " ]";
    }
    
}
