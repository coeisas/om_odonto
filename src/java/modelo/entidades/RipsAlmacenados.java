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
@Table(name = "rips_almacenados", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RipsAlmacenados.findAll", query = "SELECT r FROM RipsAlmacenados r"),
    @NamedQuery(name = "RipsAlmacenados.findByIdRipAlmacenado", query = "SELECT r FROM RipsAlmacenados r WHERE r.idRipAlmacenado = :idRipAlmacenado"),
    @NamedQuery(name = "RipsAlmacenados.findByNombre", query = "SELECT r FROM RipsAlmacenados r WHERE r.nombre = :nombre"),
    @NamedQuery(name = "RipsAlmacenados.findByFecha", query = "SELECT r FROM RipsAlmacenados r WHERE r.fecha = :fecha"),
    @NamedQuery(name = "RipsAlmacenados.findByArchivos", query = "SELECT r FROM RipsAlmacenados r WHERE r.archivos = :archivos"),
    @NamedQuery(name = "RipsAlmacenados.findByFechaInicial", query = "SELECT r FROM RipsAlmacenados r WHERE r.fechaInicial = :fechaInicial"),
    @NamedQuery(name = "RipsAlmacenados.findByFechaFinal", query = "SELECT r FROM RipsAlmacenados r WHERE r.fechaFinal = :fechaFinal")})
public class RipsAlmacenados implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_rip_almacenado", nullable = false)
    private Integer idRipAlmacenado;
    @Column(name = "nombre", length = 300)
    private String nombre;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "archivos", length = 100)
    private String archivos;
    @Column(name = "fecha_inicial")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicial;
    @Column(name = "fecha_final")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFinal;
    @JoinColumn(name = "id_contrato", referencedColumnName = "id_contrato")
    @ManyToOne
    private FacContrato idContrato;
    @JoinColumn(name = "id_administradora", referencedColumnName = "id_administradora")
    @ManyToOne
    private FacAdministradora idAdministradora;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ripsAlmacenados")
    private List<RipsCt> ripsCtList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ripsAlmacenados")
    private List<RipsAp> ripsApList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ripsAlmacenados")
    private List<RipsAf> ripsAfList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ripsAlmacenados")
    private List<RipsAc> ripsAcList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ripsAlmacenados")
    private List<RipsUs> ripsUsList;

    public RipsAlmacenados() {
    }

    public RipsAlmacenados(Integer idRipAlmacenado) {
        this.idRipAlmacenado = idRipAlmacenado;
    }

    public Integer getIdRipAlmacenado() {
        return idRipAlmacenado;
    }

    public void setIdRipAlmacenado(Integer idRipAlmacenado) {
        this.idRipAlmacenado = idRipAlmacenado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getArchivos() {
        return archivos;
    }

    public void setArchivos(String archivos) {
        this.archivos = archivos;
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

    @XmlTransient
    public List<RipsCt> getRipsCtList() {
        return ripsCtList;
    }

    public void setRipsCtList(List<RipsCt> ripsCtList) {
        this.ripsCtList = ripsCtList;
    }

    @XmlTransient
    public List<RipsAp> getRipsApList() {
        return ripsApList;
    }

    public void setRipsApList(List<RipsAp> ripsApList) {
        this.ripsApList = ripsApList;
    }

    @XmlTransient
    public List<RipsAf> getRipsAfList() {
        return ripsAfList;
    }

    public void setRipsAfList(List<RipsAf> ripsAfList) {
        this.ripsAfList = ripsAfList;
    }

    @XmlTransient
    public List<RipsAc> getRipsAcList() {
        return ripsAcList;
    }

    public void setRipsAcList(List<RipsAc> ripsAcList) {
        this.ripsAcList = ripsAcList;
    }

    @XmlTransient
    public List<RipsUs> getRipsUsList() {
        return ripsUsList;
    }

    public void setRipsUsList(List<RipsUs> ripsUsList) {
        this.ripsUsList = ripsUsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRipAlmacenado != null ? idRipAlmacenado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RipsAlmacenados)) {
            return false;
        }
        RipsAlmacenados other = (RipsAlmacenados) object;
        if ((this.idRipAlmacenado == null && other.idRipAlmacenado != null) || (this.idRipAlmacenado != null && !this.idRipAlmacenado.equals(other.idRipAlmacenado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.RipsAlmacenados[ idRipAlmacenado=" + idRipAlmacenado + " ]";
    }
    
}
