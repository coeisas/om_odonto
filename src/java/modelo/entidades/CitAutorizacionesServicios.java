/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "cit_autorizaciones_servicios", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CitAutorizacionesServicios.findAll", query = "SELECT c FROM CitAutorizacionesServicios c"),
    @NamedQuery(name = "CitAutorizacionesServicios.findByIdAutorizacion", query = "SELECT c FROM CitAutorizacionesServicios c WHERE c.citAutorizacionesServiciosPK.idAutorizacion = :idAutorizacion"),
    @NamedQuery(name = "CitAutorizacionesServicios.findByIdServicio", query = "SELECT c FROM CitAutorizacionesServicios c WHERE c.citAutorizacionesServiciosPK.idServicio = :idServicio"),
    @NamedQuery(name = "CitAutorizacionesServicios.findBySesionesAutorizadas", query = "SELECT c FROM CitAutorizacionesServicios c WHERE c.sesionesAutorizadas = :sesionesAutorizadas"),
    @NamedQuery(name = "CitAutorizacionesServicios.findBySesionesPendientes", query = "SELECT c FROM CitAutorizacionesServicios c WHERE c.sesionesPendientes = :sesionesPendientes"),
    @NamedQuery(name = "CitAutorizacionesServicios.findBySesionesRealizadas", query = "SELECT c FROM CitAutorizacionesServicios c WHERE c.sesionesRealizadas = :sesionesRealizadas")})
public class CitAutorizacionesServicios implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CitAutorizacionesServiciosPK citAutorizacionesServiciosPK;
    @Column(name = "sesiones_autorizadas")
    private Integer sesionesAutorizadas;
    @Column(name = "sesiones_pendientes")
    private Integer sesionesPendientes;
    @Column(name = "sesiones_realizadas")
    private Integer sesionesRealizadas;
    @JoinColumn(name = "id_servicio", referencedColumnName = "id_servicio", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private FacServicio facServicio;
    @JoinColumn(name = "id_autorizacion", referencedColumnName = "id_autorizacion", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CitAutorizaciones citAutorizaciones;

    public CitAutorizacionesServicios() {
    }

    public CitAutorizacionesServicios(CitAutorizacionesServiciosPK citAutorizacionesServiciosPK) {
        this.citAutorizacionesServiciosPK = citAutorizacionesServiciosPK;
    }

    public CitAutorizacionesServicios(int idAutorizacion, int idServicio) {
        this.citAutorizacionesServiciosPK = new CitAutorizacionesServiciosPK(idAutorizacion, idServicio);
    }

    public CitAutorizacionesServiciosPK getCitAutorizacionesServiciosPK() {
        return citAutorizacionesServiciosPK;
    }

    public void setCitAutorizacionesServiciosPK(CitAutorizacionesServiciosPK citAutorizacionesServiciosPK) {
        this.citAutorizacionesServiciosPK = citAutorizacionesServiciosPK;
    }

    public Integer getSesionesAutorizadas() {
        return sesionesAutorizadas;
    }

    public void setSesionesAutorizadas(Integer sesionesAutorizadas) {
        this.sesionesAutorizadas = sesionesAutorizadas;
    }

    public Integer getSesionesPendientes() {
        return sesionesPendientes;
    }

    public void setSesionesPendientes(Integer sesionesPendientes) {
        this.sesionesPendientes = sesionesPendientes;
    }

    public Integer getSesionesRealizadas() {
        return sesionesRealizadas;
    }

    public void setSesionesRealizadas(Integer sesionesRealizadas) {
        this.sesionesRealizadas = sesionesRealizadas;
    }

    public FacServicio getFacServicio() {
        return facServicio;
    }

    public void setFacServicio(FacServicio facServicio) {
        this.facServicio = facServicio;
    }

    public CitAutorizaciones getCitAutorizaciones() {
        return citAutorizaciones;
    }

    public void setCitAutorizaciones(CitAutorizaciones citAutorizaciones) {
        this.citAutorizaciones = citAutorizaciones;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (citAutorizacionesServiciosPK != null ? citAutorizacionesServiciosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CitAutorizacionesServicios)) {
            return false;
        }
        CitAutorizacionesServicios other = (CitAutorizacionesServicios) object;
        if ((this.citAutorizacionesServiciosPK == null && other.citAutorizacionesServiciosPK != null) || (this.citAutorizacionesServiciosPK != null && !this.citAutorizacionesServiciosPK.equals(other.citAutorizacionesServiciosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.CitAutorizacionesServicios[ citAutorizacionesServiciosPK=" + citAutorizacionesServiciosPK + " ]";
    }
    
}
