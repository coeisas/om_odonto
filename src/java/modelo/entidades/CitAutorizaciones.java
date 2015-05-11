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
@Table(name = "cit_autorizaciones", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CitAutorizaciones.findAll", query = "SELECT c FROM CitAutorizaciones c"),
    @NamedQuery(name = "CitAutorizaciones.findByNumAutorizacion", query = "SELECT c FROM CitAutorizaciones c WHERE c.numAutorizacion = :numAutorizacion"),
    @NamedQuery(name = "CitAutorizaciones.findByIdAutorizacion", query = "SELECT c FROM CitAutorizaciones c WHERE c.idAutorizacion = :idAutorizacion"),
    @NamedQuery(name = "CitAutorizaciones.findByCerrada", query = "SELECT c FROM CitAutorizaciones c WHERE c.cerrada = :cerrada"),
    @NamedQuery(name = "CitAutorizaciones.findByFacturada", query = "SELECT c FROM CitAutorizaciones c WHERE c.facturada = :facturada"),
    @NamedQuery(name = "CitAutorizaciones.findByFechaSistema", query = "SELECT c FROM CitAutorizaciones c WHERE c.fechaSistema = :fechaSistema"),
    @NamedQuery(name = "CitAutorizaciones.findByFechaAutorizacion", query = "SELECT c FROM CitAutorizaciones c WHERE c.fechaAutorizacion = :fechaAutorizacion")})

public class CitAutorizaciones implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "num_autorizacion", length = 10)
    private String numAutorizacion;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_autorizacion", nullable = false)
    private Integer idAutorizacion;
    @Column(name = "cerrada")
    private Boolean cerrada;
    @Column(name = "facturada")
    private Boolean facturada;
    @Column(name = "fecha_sistema")
    @Temporal(TemporalType.DATE)
    private Date fechaSistema;
    @Column(name = "fecha_autorizacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAutorizacion;    
    @OneToMany(mappedBy = "idAutorizacion")
    private List<CitCitas> citCitasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "citAutorizaciones")
    private List<CitAutorizacionesServicios> citAutorizacionesServiciosList;
    @JoinColumn(name = "administradora", referencedColumnName = "id_administradora")
    @ManyToOne
    private FacAdministradora administradora;
    @JoinColumn(name = "id_usuario_creador", referencedColumnName = "id_usuario")
    @ManyToOne
    private CfgUsuarios idUsuarioCreador;
    @JoinColumn(name = "paciente", referencedColumnName = "id_paciente")
    @ManyToOne
    private CfgPacientes paciente;

    public CitAutorizaciones() {
    }

    public CitAutorizaciones(Integer idAutorizacion) {
        this.idAutorizacion = idAutorizacion;
    }

    public String getNumAutorizacion() {
        return numAutorizacion;
    }

    public void setNumAutorizacion(String numAutorizacion) {
        this.numAutorizacion = numAutorizacion;
    }

    public Integer getIdAutorizacion() {
        return idAutorizacion;
    }

    public void setIdAutorizacion(Integer idAutorizacion) {
        this.idAutorizacion = idAutorizacion;
    }

    public Boolean getCerrada() {
        return cerrada;
    }

    public void setCerrada(Boolean cerrada) {
        this.cerrada = cerrada;
    }

    public Boolean getFacturada() {
        return facturada;
    }

    public void setFacturada(Boolean facturada) {
        this.facturada = facturada;
    }
    
    public Date getFechaSistema() {
        return fechaSistema;
    }

    public void setFechaSistema(Date fechaSistema) {
        this.fechaSistema = fechaSistema;
    }

    public Date getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(Date fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }    

    @XmlTransient
    public List<CitCitas> getCitCitasList() {
        return citCitasList;
    }

    public void setCitCitasList(List<CitCitas> citCitasList) {
        this.citCitasList = citCitasList;
    }

    @XmlTransient
    public List<CitAutorizacionesServicios> getCitAutorizacionesServiciosList() {
        return citAutorizacionesServiciosList;
    }

    public void setCitAutorizacionesServiciosList(List<CitAutorizacionesServicios> citAutorizacionesServiciosList) {
        this.citAutorizacionesServiciosList = citAutorizacionesServiciosList;
    }

    public FacAdministradora getAdministradora() {
        return administradora;
    }

    public void setAdministradora(FacAdministradora administradora) {
        this.administradora = administradora;
    }

    public CfgUsuarios getIdUsuarioCreador() {
        return idUsuarioCreador;
    }

    public void setIdUsuarioCreador(CfgUsuarios idUsuarioCreador) {
        this.idUsuarioCreador = idUsuarioCreador;
    }

    public CfgPacientes getPaciente() {
        return paciente;
    }

    public void setPaciente(CfgPacientes paciente) {
        this.paciente = paciente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAutorizacion != null ? idAutorizacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CitAutorizaciones)) {
            return false;
        }
        CitAutorizaciones other = (CitAutorizaciones) object;
        if ((this.idAutorizacion == null && other.idAutorizacion != null) || (this.idAutorizacion != null && !this.idAutorizacion.equals(other.idAutorizacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.CitAutorizaciones[ idAutorizacion=" + idAutorizacion + " ]";
    }
    
}
