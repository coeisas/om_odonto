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
@Table(name = "cit_citas", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CitCitas.findAll", query = "SELECT c FROM CitCitas c"),
    @NamedQuery(name = "CitCitas.findByIdCita", query = "SELECT c FROM CitCitas c WHERE c.idCita = :idCita"),
    @NamedQuery(name = "CitCitas.findByAtendida", query = "SELECT c FROM CitCitas c WHERE c.atendida = :atendida"),
    @NamedQuery(name = "CitCitas.findByCancelada", query = "SELECT c FROM CitCitas c WHERE c.cancelada = :cancelada"),
    @NamedQuery(name = "CitCitas.findByMultado", query = "SELECT c FROM CitCitas c WHERE c.multado = :multado"),
    @NamedQuery(name = "CitCitas.findByDescCancelacion", query = "SELECT c FROM CitCitas c WHERE c.descCancelacion = :descCancelacion"),
    @NamedQuery(name = "CitCitas.findByFechaRegistro", query = "SELECT c FROM CitCitas c WHERE c.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "CitCitas.findByFechaCancelacion", query = "SELECT c FROM CitCitas c WHERE c.fechaCancelacion = :fechaCancelacion"),
    @NamedQuery(name = "CitCitas.findByFacturada", query = "SELECT c FROM CitCitas c WHERE c.facturada = :facturada"),
    @NamedQuery(name = "CitCitas.findByNoPaqAplicado", query = "SELECT c FROM CitCitas c WHERE c.noPaqAplicado = :noPaqAplicado")})
public class CitCitas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_cita", nullable = false)
    private Integer idCita;
    @Column(name = "atendida")
    private Boolean atendida;
    @Column(name = "cancelada")
    private Boolean cancelada;
    @Column(name = "multado")
    private Boolean multado;
    @Column(name = "desc_cancelacion", length = 2147483647)
    private String descCancelacion;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;
    @Column(name = "fecha_cancelacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCancelacion;
    @Column(name = "facturada")
    private Boolean facturada;
    @Column(name = "no_paq_aplicado")
    private Integer noPaqAplicado;
    @JoinColumn(name = "id_servicio", referencedColumnName = "id_servicio")
    @ManyToOne
    private FacServicio idServicio;
    @JoinColumn(name = "id_administradora", referencedColumnName = "id_administradora")
    @ManyToOne
    private FacAdministradora idAdministradora;
    @JoinColumn(name = "id_turno", referencedColumnName = "id_turno", nullable = false)
    @ManyToOne(optional = false)
    private CitTurnos idTurno;
    @JoinColumn(name = "id_paquete", referencedColumnName = "id_paq_maestro")
    @ManyToOne
    private CitPaqMaestro idPaquete;
    @JoinColumn(name = "id_autorizacion", referencedColumnName = "id_autorizacion")
    @ManyToOne
    private CitAutorizaciones idAutorizacion;
    @JoinColumn(name = "id_prestador", referencedColumnName = "id_usuario", nullable = false)
    @ManyToOne(optional = false)
    private CfgUsuarios idPrestador;
    @JoinColumn(name = "id_paciente", referencedColumnName = "id_paciente", nullable = false)
    @ManyToOne(optional = false)
    private CfgPacientes idPaciente;
    @JoinColumn(name = "motivo_cancelacion", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones motivoCancelacion;
    @JoinColumn(name = "tipo_cita", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones tipoCita;
    @OneToMany(mappedBy = "idCita")
    private List<HcRegistro> hcRegistroList;
    @OneToMany(mappedBy = "idCita")
    private List<FacFacturaPaciente> facFacturaPacienteList;
    @Column(name = "tiene_reg_asociado")
    private Boolean tieneRegAsociado;
    
    public CitCitas() {
    }

    public CitCitas(Integer idCita) {
        this.idCita = idCita;
    }

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    public Boolean getAtendida() {
        return atendida;
    }

    public void setAtendida(Boolean atendida) {
        this.atendida = atendida;
    }

    public Boolean getCancelada() {
        return cancelada;
    }

    public void setCancelada(Boolean cancelada) {
        this.cancelada = cancelada;
    }

    public Boolean getMultado() {
        return multado;
    }

    public void setMultado(Boolean multado) {
        this.multado = multado;
    }

    public String getDescCancelacion() {
        return descCancelacion;
    }

    public void setDescCancelacion(String descCancelacion) {
        this.descCancelacion = descCancelacion;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(Date fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }

    public Boolean getFacturada() {
        return facturada;
    }

    public void setFacturada(Boolean facturada) {
        this.facturada = facturada;
    }

    public Integer getNoPaqAplicado() {
        return noPaqAplicado;
    }

    public void setNoPaqAplicado(Integer noPaqAplicado) {
        this.noPaqAplicado = noPaqAplicado;
    }

    public FacServicio getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(FacServicio idServicio) {
        this.idServicio = idServicio;
    }

    public FacAdministradora getIdAdministradora() {
        return idAdministradora;
    }

    public void setIdAdministradora(FacAdministradora idAdministradora) {
        this.idAdministradora = idAdministradora;
    }

    public CitTurnos getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(CitTurnos idTurno) {
        this.idTurno = idTurno;
    }

    public CitPaqMaestro getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(CitPaqMaestro idPaquete) {
        this.idPaquete = idPaquete;
    }

    public CitAutorizaciones getIdAutorizacion() {
        return idAutorizacion;
    }

    public void setIdAutorizacion(CitAutorizaciones idAutorizacion) {
        this.idAutorizacion = idAutorizacion;
    }

    public CfgUsuarios getIdPrestador() {
        return idPrestador;
    }

    public void setIdPrestador(CfgUsuarios idPrestador) {
        this.idPrestador = idPrestador;
    }

    public CfgPacientes getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(CfgPacientes idPaciente) {
        this.idPaciente = idPaciente;
    }

    public CfgClasificaciones getMotivoCancelacion() {
        return motivoCancelacion;
    }

    public void setMotivoCancelacion(CfgClasificaciones motivoCancelacion) {
        this.motivoCancelacion = motivoCancelacion;
    }

    public CfgClasificaciones getTipoCita() {
        return tipoCita;
    }

    public void setTipoCita(CfgClasificaciones tipoCita) {
        this.tipoCita = tipoCita;
    }
    
    public Boolean getTieneRegAsociado() {
        return tieneRegAsociado;
    }

    public void setTieneRegAsociado(Boolean tieneRegAsociado) {
        this.tieneRegAsociado = tieneRegAsociado;
    }

    @XmlTransient
    public List<HcRegistro> getHcRegistroList() {
        return hcRegistroList;
    }

    public void setHcRegistroList(List<HcRegistro> hcRegistroList) {
        this.hcRegistroList = hcRegistroList;
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
        hash += (idCita != null ? idCita.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CitCitas)) {
            return false;
        }
        CitCitas other = (CitCitas) object;
        if ((this.idCita == null && other.idCita != null) || (this.idCita != null && !this.idCita.equals(other.idCita))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.CitCitas[ idCita=" + idCita + " ]";
    }
    
}
