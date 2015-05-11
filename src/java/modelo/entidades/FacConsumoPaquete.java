/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author santos
 */
@Entity
@Table(name = "fac_consumo_paquete", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacConsumoPaquete.findAll", query = "SELECT f FROM FacConsumoPaquete f"),
    @NamedQuery(name = "FacConsumoPaquete.findByIdConsumoPaquete", query = "SELECT f FROM FacConsumoPaquete f WHERE f.idConsumoPaquete = :idConsumoPaquete"),
    @NamedQuery(name = "FacConsumoPaquete.findByFecha", query = "SELECT f FROM FacConsumoPaquete f WHERE f.fecha = :fecha"),
    @NamedQuery(name = "FacConsumoPaquete.findByCantidad", query = "SELECT f FROM FacConsumoPaquete f WHERE f.cantidad = :cantidad"),
    @NamedQuery(name = "FacConsumoPaquete.findByValorUnitario", query = "SELECT f FROM FacConsumoPaquete f WHERE f.valorUnitario = :valorUnitario"),
    @NamedQuery(name = "FacConsumoPaquete.findByValorFinal", query = "SELECT f FROM FacConsumoPaquete f WHERE f.valorFinal = :valorFinal")})
public class FacConsumoPaquete implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_consumo_paquete", nullable = false)
    private Integer idConsumoPaquete;
    @Basic(optional = false)
    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "cantidad")
    private Integer cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_unitario", precision = 17, scale = 17)
    private Double valorUnitario;
    @Column(name = "valor_final", precision = 17, scale = 17)
    private Double valorFinal;
    @JoinColumn(name = "id_paquete", referencedColumnName = "id_paquete")
    @ManyToOne
    private FacPaquete idPaquete;
    @JoinColumn(name = "id_prestador", referencedColumnName = "id_usuario")
    @ManyToOne
    private CfgUsuarios idPrestador;
    @JoinColumn(name = "id_paciente", referencedColumnName = "id_paciente", nullable = false)
    @ManyToOne(optional = false)
    private CfgPacientes idPaciente;

    public FacConsumoPaquete() {
    }

    public FacConsumoPaquete(Integer idConsumoPaquete) {
        this.idConsumoPaquete = idConsumoPaquete;
    }

    public FacConsumoPaquete(Integer idConsumoPaquete, Date fecha) {
        this.idConsumoPaquete = idConsumoPaquete;
        this.fecha = fecha;
    }

    public Integer getIdConsumoPaquete() {
        return idConsumoPaquete;
    }

    public void setIdConsumoPaquete(Integer idConsumoPaquete) {
        this.idConsumoPaquete = idConsumoPaquete;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public Double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(Double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public FacPaquete getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(FacPaquete idPaquete) {
        this.idPaquete = idPaquete;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConsumoPaquete != null ? idConsumoPaquete.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacConsumoPaquete)) {
            return false;
        }
        FacConsumoPaquete other = (FacConsumoPaquete) object;
        if ((this.idConsumoPaquete == null && other.idConsumoPaquete != null) || (this.idConsumoPaquete != null && !this.idConsumoPaquete.equals(other.idConsumoPaquete))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.FacConsumoPaquete[ idConsumoPaquete=" + idConsumoPaquete + " ]";
    }
    
}
