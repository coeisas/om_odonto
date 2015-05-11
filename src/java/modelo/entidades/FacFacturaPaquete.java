/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author santos
 */
@Entity
@Table(name = "fac_factura_paquete", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacFacturaPaquete.findAll", query = "SELECT f FROM FacFacturaPaquete f"),
    @NamedQuery(name = "FacFacturaPaquete.findByIdDetalle", query = "SELECT f FROM FacFacturaPaquete f WHERE f.facFacturaPaquetePK.idDetalle = :idDetalle"),
    @NamedQuery(name = "FacFacturaPaquete.findByIdFactura", query = "SELECT f FROM FacFacturaPaquete f WHERE f.facFacturaPaquetePK.idFactura = :idFactura"),
    @NamedQuery(name = "FacFacturaPaquete.findByFechaPaquete", query = "SELECT f FROM FacFacturaPaquete f WHERE f.fechaPaquete = :fechaPaquete"),
    @NamedQuery(name = "FacFacturaPaquete.findByCantidadPaquete", query = "SELECT f FROM FacFacturaPaquete f WHERE f.cantidadPaquete = :cantidadPaquete"),
    @NamedQuery(name = "FacFacturaPaquete.findByValorPaquete", query = "SELECT f FROM FacFacturaPaquete f WHERE f.valorPaquete = :valorPaquete"),
    @NamedQuery(name = "FacFacturaPaquete.findByValorParcial", query = "SELECT f FROM FacFacturaPaquete f WHERE f.valorParcial = :valorParcial"),
    @NamedQuery(name = "FacFacturaPaquete.findByValorUsuario", query = "SELECT f FROM FacFacturaPaquete f WHERE f.valorUsuario = :valorUsuario"),
    @NamedQuery(name = "FacFacturaPaquete.findByValorEmpresa", query = "SELECT f FROM FacFacturaPaquete f WHERE f.valorEmpresa = :valorEmpresa"),
    @NamedQuery(name = "FacFacturaPaquete.findByValorIva", query = "SELECT f FROM FacFacturaPaquete f WHERE f.valorIva = :valorIva"),
    @NamedQuery(name = "FacFacturaPaquete.findByValorCree", query = "SELECT f FROM FacFacturaPaquete f WHERE f.valorCree = :valorCree"),
    @NamedQuery(name = "FacFacturaPaquete.findByNumAutorizacion", query = "SELECT f FROM FacFacturaPaquete f WHERE f.numAutorizacion = :numAutorizacion"),
    @NamedQuery(name = "FacFacturaPaquete.findByFechaAutorizacion", query = "SELECT f FROM FacFacturaPaquete f WHERE f.fechaAutorizacion = :fechaAutorizacion")})
public class FacFacturaPaquete implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FacFacturaPaquetePK facFacturaPaquetePK;
    @Column(name = "fecha_paquete")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPaquete;
    @Column(name = "cantidad_paquete")
    private Short cantidadPaquete;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_paquete", precision = 17, scale = 17)
    private Double valorPaquete;
    @Column(name = "valor_parcial", precision = 17, scale = 17)
    private Double valorParcial;
    @Column(name = "valor_usuario", precision = 17, scale = 17)
    private Double valorUsuario;
    @Column(name = "valor_empresa", precision = 17, scale = 17)
    private Double valorEmpresa;
    @Column(name = "valor_iva", precision = 17, scale = 17)
    private Double valorIva;
    @Column(name = "valor_cree", precision = 17, scale = 17)
    private Double valorCree;
    @Column(name = "num_autorizacion", length = 20)
    private String numAutorizacion;
    @Column(name = "fecha_autorizacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAutorizacion;
    @JoinColumn(name = "id_paquete", referencedColumnName = "id_paquete")
    @ManyToOne
    private FacPaquete idPaquete;
    @JoinColumn(name = "id_factura", referencedColumnName = "id_factura_paciente", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private FacFacturaPaciente facFacturaPaciente;
    @JoinColumn(name = "id_medico", referencedColumnName = "id_usuario")
    @ManyToOne
    private CfgUsuarios idMedico;
    @JoinColumn(name = "id_consultorio", referencedColumnName = "id_consultorio")
    @ManyToOne
    private CfgConsultorios idConsultorio;

    public FacFacturaPaquete() {
    }

    public FacFacturaPaquete(FacFacturaPaquetePK facFacturaPaquetePK) {
        this.facFacturaPaquetePK = facFacturaPaquetePK;
    }

    public FacFacturaPaquete(int idDetalle, int idFactura) {
        this.facFacturaPaquetePK = new FacFacturaPaquetePK(idDetalle, idFactura);
    }

    public FacFacturaPaquetePK getFacFacturaPaquetePK() {
        return facFacturaPaquetePK;
    }

    public void setFacFacturaPaquetePK(FacFacturaPaquetePK facFacturaPaquetePK) {
        this.facFacturaPaquetePK = facFacturaPaquetePK;
    }

    public Date getFechaPaquete() {
        return fechaPaquete;
    }

    public void setFechaPaquete(Date fechaPaquete) {
        this.fechaPaquete = fechaPaquete;
    }

    public Short getCantidadPaquete() {
        return cantidadPaquete;
    }

    public void setCantidadPaquete(Short cantidadPaquete) {
        this.cantidadPaquete = cantidadPaquete;
    }

    public Double getValorPaquete() {
        return valorPaquete;
    }

    public void setValorPaquete(Double valorPaquete) {
        this.valorPaquete = valorPaquete;
    }

    public Double getValorParcial() {
        return valorParcial;
    }

    public void setValorParcial(Double valorParcial) {
        this.valorParcial = valorParcial;
    }

    public Double getValorUsuario() {
        return valorUsuario;
    }

    public void setValorUsuario(Double valorUsuario) {
        this.valorUsuario = valorUsuario;
    }

    public Double getValorEmpresa() {
        return valorEmpresa;
    }

    public void setValorEmpresa(Double valorEmpresa) {
        this.valorEmpresa = valorEmpresa;
    }

    public Double getValorIva() {
        return valorIva;
    }

    public void setValorIva(Double valorIva) {
        this.valorIva = valorIva;
    }

    public Double getValorCree() {
        return valorCree;
    }

    public void setValorCree(Double valorCree) {
        this.valorCree = valorCree;
    }

    public String getNumAutorizacion() {
        return numAutorizacion;
    }

    public void setNumAutorizacion(String numAutorizacion) {
        this.numAutorizacion = numAutorizacion;
    }

    public Date getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(Date fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public FacPaquete getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(FacPaquete idPaquete) {
        this.idPaquete = idPaquete;
    }

    public FacFacturaPaciente getFacFacturaPaciente() {
        return facFacturaPaciente;
    }

    public void setFacFacturaPaciente(FacFacturaPaciente facFacturaPaciente) {
        this.facFacturaPaciente = facFacturaPaciente;
    }

    public CfgUsuarios getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(CfgUsuarios idMedico) {
        this.idMedico = idMedico;
    }

    public CfgConsultorios getIdConsultorio() {
        return idConsultorio;
    }

    public void setIdConsultorio(CfgConsultorios idConsultorio) {
        this.idConsultorio = idConsultorio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facFacturaPaquetePK != null ? facFacturaPaquetePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacFacturaPaquete)) {
            return false;
        }
        FacFacturaPaquete other = (FacFacturaPaquete) object;
        if ((this.facFacturaPaquetePK == null && other.facFacturaPaquetePK != null) || (this.facFacturaPaquetePK != null && !this.facFacturaPaquetePK.equals(other.facFacturaPaquetePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.FacFacturaPaquete[ facFacturaPaquetePK=" + facFacturaPaquetePK + " ]";
    }
    
}
