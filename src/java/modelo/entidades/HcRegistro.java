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
@Table(name = "hc_registro", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HcRegistro.findAll", query = "SELECT h FROM HcRegistro h"),
    @NamedQuery(name = "HcRegistro.findByIdRegistro", query = "SELECT h FROM HcRegistro h WHERE h.idRegistro = :idRegistro"),
    @NamedQuery(name = "HcRegistro.findByFechaReg", query = "SELECT h FROM HcRegistro h WHERE h.fechaReg = :fechaReg"),
    @NamedQuery(name = "HcRegistro.findByFechaSis", query = "SELECT h FROM HcRegistro h WHERE h.fechaSis = :fechaSis"),
    @NamedQuery(name = "HcRegistro.findByFolio", query = "SELECT h FROM HcRegistro h WHERE h.folio = :folio")})
public class HcRegistro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_registro", nullable = false)
    private Integer idRegistro;
    @Column(name = "fecha_reg")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaReg;
    @Column(name = "fecha_sis")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSis;
    @Column(name = "folio")
    private Integer folio;
    @JoinColumn(name = "id_tipo_reg", referencedColumnName = "id_tipo_reg")
    @ManyToOne
    private HcTipoReg idTipoReg;
    @JoinColumn(name = "id_cita", referencedColumnName = "id_cita")
    @ManyToOne
    private CitCitas idCita;
    @JoinColumn(name = "id_medico", referencedColumnName = "id_usuario")
    @ManyToOne
    private CfgUsuarios idMedico;
    @JoinColumn(name = "id_paciente", referencedColumnName = "id_paciente")
    @ManyToOne
    private CfgPacientes idPaciente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hcRegistro")
    private List<HcDetalle> hcDetalleList;
    @OneToMany(mappedBy = "idRegistro")
    private List<HcItems> hcItemsList;

    public HcRegistro() {
    }

    public HcRegistro(Integer idRegistro) {
        this.idRegistro = idRegistro;
    }

    public Integer getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(Integer idRegistro) {
        this.idRegistro = idRegistro;
    }

    public Date getFechaReg() {
        return fechaReg;
    }

    public void setFechaReg(Date fechaReg) {
        this.fechaReg = fechaReg;
    }

    public Date getFechaSis() {
        return fechaSis;
    }

    public void setFechaSis(Date fechaSis) {
        this.fechaSis = fechaSis;
    }

    public Integer getFolio() {
        return folio;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    public HcTipoReg getIdTipoReg() {
        return idTipoReg;
    }

    public void setIdTipoReg(HcTipoReg idTipoReg) {
        this.idTipoReg = idTipoReg;
    }

    public CitCitas getIdCita() {
        return idCita;
    }

    public void setIdCita(CitCitas idCita) {
        this.idCita = idCita;
    }

    public CfgUsuarios getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(CfgUsuarios idMedico) {
        this.idMedico = idMedico;
    }

    public CfgPacientes getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(CfgPacientes idPaciente) {
        this.idPaciente = idPaciente;
    }

    @XmlTransient
    public List<HcDetalle> getHcDetalleList() {
        return hcDetalleList;
    }

    public void setHcDetalleList(List<HcDetalle> hcDetalleList) {
        this.hcDetalleList = hcDetalleList;
    }

    @XmlTransient
    public List<HcItems> getHcItemsList() {
        return hcItemsList;
    }

    public void setHcItemsList(List<HcItems> hcItemsList) {
        this.hcItemsList = hcItemsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRegistro != null ? idRegistro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HcRegistro)) {
            return false;
        }
        HcRegistro other = (HcRegistro) object;
        if ((this.idRegistro == null && other.idRegistro != null) || (this.idRegistro != null && !this.idRegistro.equals(other.idRegistro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades2.HcRegistro[ idRegistro=" + idRegistro + " ]";
    }

}
