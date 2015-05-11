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
@Table(name = "fac_consecutivo", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacConsecutivo.findAll", query = "SELECT f FROM FacConsecutivo f"),
    @NamedQuery(name = "FacConsecutivo.findByIdConsecutivo", query = "SELECT f FROM FacConsecutivo f WHERE f.idConsecutivo = :idConsecutivo"),
    @NamedQuery(name = "FacConsecutivo.findByResolucionDian", query = "SELECT f FROM FacConsecutivo f WHERE f.resolucionDian = :resolucionDian"),
    @NamedQuery(name = "FacConsecutivo.findByInicio", query = "SELECT f FROM FacConsecutivo f WHERE f.inicio = :inicio"),
    @NamedQuery(name = "FacConsecutivo.findByFin", query = "SELECT f FROM FacConsecutivo f WHERE f.fin = :fin"),
    @NamedQuery(name = "FacConsecutivo.findByActual", query = "SELECT f FROM FacConsecutivo f WHERE f.actual = :actual"),
    @NamedQuery(name = "FacConsecutivo.findByTexto", query = "SELECT f FROM FacConsecutivo f WHERE f.texto = :texto"),
    @NamedQuery(name = "FacConsecutivo.findByPrefijo", query = "SELECT f FROM FacConsecutivo f WHERE f.prefijo = :prefijo"),
    @NamedQuery(name = "FacConsecutivo.findByFechaSistema", query = "SELECT f FROM FacConsecutivo f WHERE f.fechaSistema = :fechaSistema")})
public class FacConsecutivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_consecutivo", nullable = false)
    private Integer idConsecutivo;
    @Column(name = "resolucion_dian", length = 200)
    private String resolucionDian;
    @Column(name = "inicio")
    private Integer inicio;
    @Column(name = "fin")
    private Integer fin;
    @Column(name = "actual")
    private Integer actual;
    @Column(name = "texto", length = 2147483647)
    private String texto;
    @Column(name = "prefijo", length = 5)
    private String prefijo;
    @Column(name = "fecha_sistema")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSistema;
    @JoinColumn(name = "tipo_documento", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones tipoDocumento;

    public FacConsecutivo() {
    }

    public FacConsecutivo(Integer idConsecutivo) {
        this.idConsecutivo = idConsecutivo;
    }

    public Integer getIdConsecutivo() {
        return idConsecutivo;
    }

    public void setIdConsecutivo(Integer idConsecutivo) {
        this.idConsecutivo = idConsecutivo;
    }

    public String getResolucionDian() {
        return resolucionDian;
    }

    public void setResolucionDian(String resolucionDian) {
        this.resolucionDian = resolucionDian;
    }

    public Integer getInicio() {
        return inicio;
    }

    public void setInicio(Integer inicio) {
        this.inicio = inicio;
    }

    public Integer getFin() {
        return fin;
    }

    public void setFin(Integer fin) {
        this.fin = fin;
    }

    public Integer getActual() {
        return actual;
    }

    public void setActual(Integer actual) {
        this.actual = actual;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    public Date getFechaSistema() {
        return fechaSistema;
    }

    public void setFechaSistema(Date fechaSistema) {
        this.fechaSistema = fechaSistema;
    }

    public CfgClasificaciones getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(CfgClasificaciones tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConsecutivo != null ? idConsecutivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacConsecutivo)) {
            return false;
        }
        FacConsecutivo other = (FacConsecutivo) object;
        if ((this.idConsecutivo == null && other.idConsecutivo != null) || (this.idConsecutivo != null && !this.idConsecutivo.equals(other.idConsecutivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.FacConsecutivo[ idConsecutivo=" + idConsecutivo + " ]";
    }
    
}
