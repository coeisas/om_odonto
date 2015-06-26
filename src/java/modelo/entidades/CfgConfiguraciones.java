/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author santos
 */
@Entity
@Table(name = "cfg_configuraciones", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgConfiguraciones.findAll", query = "SELECT c FROM CfgConfiguraciones c"),
    @NamedQuery(name = "CfgConfiguraciones.findByIdConfiguracion", query = "SELECT c FROM CfgConfiguraciones c WHERE c.idConfiguracion = :idConfiguracion"),
    @NamedQuery(name = "CfgConfiguraciones.findByUsuario", query = "SELECT c FROM CfgConfiguraciones c WHERE c.usuario = :usuario"),
    @NamedQuery(name = "CfgConfiguraciones.findByClave", query = "SELECT c FROM CfgConfiguraciones c WHERE c.clave = :clave"),
    @NamedQuery(name = "CfgConfiguraciones.findByNombreDb", query = "SELECT c FROM CfgConfiguraciones c WHERE c.nombreDb = :nombreDb"),
    @NamedQuery(name = "CfgConfiguraciones.findByServidor", query = "SELECT c FROM CfgConfiguraciones c WHERE c.servidor = :servidor"),
    @NamedQuery(name = "CfgConfiguraciones.findByRutaImagenes", query = "SELECT c FROM CfgConfiguraciones c WHERE c.rutaImagenes = :rutaImagenes"),
    @NamedQuery(name = "CfgConfiguraciones.findByRutaCopiasSeguridad", query = "SELECT c FROM CfgConfiguraciones c WHERE c.rutaCopiasSeguridad = :rutaCopiasSeguridad"),
    @NamedQuery(name = "CfgConfiguraciones.findByRutaBinPostgres", query = "SELECT c FROM CfgConfiguraciones c WHERE c.rutaBinPostgres = :rutaBinPostgres")})
public class CfgConfiguraciones implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_configuracion", nullable = false)
    private Integer idConfiguracion;
    @Size(max = 100)
    @Column(name = "usuario", length = 100)
    private String usuario;
    @Size(max = 100)
    @Column(name = "clave", length = 100)
    private String clave;
    @Size(max = 100)
    @Column(name = "nombre_db", length = 100)
    private String nombreDb;
    @Size(max = 100)
    @Column(name = "servidor", length = 100)
    private String servidor;
    @Size(max = 100)
    @Column(name = "ruta_imagenes", length = 100)
    private String rutaImagenes;
    @Size(max = 100)
    @Column(name = "ruta_copias_seguridad", length = 100)
    private String rutaCopiasSeguridad;
    @Size(max = 2147483647)
    @Column(name = "ruta_bin_postgres", length = 2147483647)
    private String rutaBinPostgres;

    public CfgConfiguraciones() {
    }

    public CfgConfiguraciones(Integer idConfiguracion) {
        this.idConfiguracion = idConfiguracion;
    }

    public Integer getIdConfiguracion() {
        return idConfiguracion;
    }

    public void setIdConfiguracion(Integer idConfiguracion) {
        this.idConfiguracion = idConfiguracion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombreDb() {
        return nombreDb;
    }

    public void setNombreDb(String nombreDb) {
        this.nombreDb = nombreDb;
    }

    public String getServidor() {
        return servidor;
    }

    public void setServidor(String servidor) {
        this.servidor = servidor;
    }

    public String getRutaImagenes() {
        return rutaImagenes;
    }

    public void setRutaImagenes(String rutaImagenes) {
        this.rutaImagenes = rutaImagenes;
    }

    public String getRutaCopiasSeguridad() {
        return rutaCopiasSeguridad;
    }

    public void setRutaCopiasSeguridad(String rutaCopiasSeguridad) {
        this.rutaCopiasSeguridad = rutaCopiasSeguridad;
    }

    public String getRutaBinPostgres() {
        return rutaBinPostgres;
    }

    public void setRutaBinPostgres(String rutaBinPostgres) {
        this.rutaBinPostgres = rutaBinPostgres;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConfiguracion != null ? idConfiguracion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgConfiguraciones)) {
            return false;
        }
        CfgConfiguraciones other = (CfgConfiguraciones) object;
        if ((this.idConfiguracion == null && other.idConfiguracion != null) || (this.idConfiguracion != null && !this.idConfiguracion.equals(other.idConfiguracion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.CfgConfiguraciones[ idConfiguracion=" + idConfiguracion + " ]";
    }
    
}
