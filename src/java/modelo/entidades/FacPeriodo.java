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
@Table(name = "fac_periodo", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacPeriodo.findAll", query = "SELECT f FROM FacPeriodo f"),
    @NamedQuery(name = "FacPeriodo.findByIdPeriodo", query = "SELECT f FROM FacPeriodo f WHERE f.idPeriodo = :idPeriodo"),
    @NamedQuery(name = "FacPeriodo.findByAnio", query = "SELECT f FROM FacPeriodo f WHERE f.anio = :anio"),
    @NamedQuery(name = "FacPeriodo.findByMes", query = "SELECT f FROM FacPeriodo f WHERE f.mes = :mes"),
    @NamedQuery(name = "FacPeriodo.findByNombre", query = "SELECT f FROM FacPeriodo f WHERE f.nombre = :nombre"),
    @NamedQuery(name = "FacPeriodo.findByFechaInicial", query = "SELECT f FROM FacPeriodo f WHERE f.fechaInicial = :fechaInicial"),
    @NamedQuery(name = "FacPeriodo.findByFechaFinal", query = "SELECT f FROM FacPeriodo f WHERE f.fechaFinal = :fechaFinal"),
    @NamedQuery(name = "FacPeriodo.findByFechaLimite", query = "SELECT f FROM FacPeriodo f WHERE f.fechaLimite = :fechaLimite"),
    @NamedQuery(name = "FacPeriodo.findByCerrado", query = "SELECT f FROM FacPeriodo f WHERE f.cerrado = :cerrado")})
public class FacPeriodo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_periodo", nullable = false)
    private Integer idPeriodo;
    @Column(name = "anio")
    private Integer anio;
    @Column(name = "mes")
    private Integer mes;
    @Column(name = "nombre", length = 6)
    private String nombre;
    @Column(name = "fecha_inicial")
    @Temporal(TemporalType.DATE)
    private Date fechaInicial;
    @Column(name = "fecha_final")
    @Temporal(TemporalType.DATE)
    private Date fechaFinal;
    @Column(name = "fecha_limite")
    @Temporal(TemporalType.DATE)
    private Date fechaLimite;
    @Column(name = "cerrado")
    private Boolean cerrado;
    @OneToMany(mappedBy = "idPeriodo")
    private List<FacFacturaAdmi> facFacturaAdmiList;
    @OneToMany(mappedBy = "idPeriodo")
    private List<FacFacturaPaciente> facFacturaPacienteList;

    public FacPeriodo() {
    }

    public FacPeriodo(Integer idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public Integer getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Integer idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public Date getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public Boolean getCerrado() {
        return cerrado;
    }

    public void setCerrado(Boolean cerrado) {
        this.cerrado = cerrado;
    }

    @XmlTransient
    public List<FacFacturaAdmi> getFacFacturaAdmiList() {
        return facFacturaAdmiList;
    }

    public void setFacFacturaAdmiList(List<FacFacturaAdmi> facFacturaAdmiList) {
        this.facFacturaAdmiList = facFacturaAdmiList;
    }

    @XmlTransient
    public List<FacFacturaPaciente> getFacFacturaPacienteList() {
        return facFacturaPacienteList;
    }

    public void setFacFacturaPacienteList(List<FacFacturaPaciente> facFacturaPacienteList) {
        this.facFacturaPacienteList = facFacturaPacienteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPeriodo != null ? idPeriodo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacPeriodo)) {
            return false;
        }
        FacPeriodo other = (FacPeriodo) object;
        if ((this.idPeriodo == null && other.idPeriodo != null) || (this.idPeriodo != null && !this.idPeriodo.equals(other.idPeriodo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.FacPeriodo[ idPeriodo=" + idPeriodo + " ]";
    }
    
}
