/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author santos
 */
@Entity
@Table(name = "cfg_diagnostico", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgDiagnostico.findAll", query = "SELECT c FROM CfgDiagnostico c"),
    @NamedQuery(name = "CfgDiagnostico.findByCodigoDiagnostico", query = "SELECT c FROM CfgDiagnostico c WHERE c.codigoDiagnostico = :codigoDiagnostico"),
    @NamedQuery(name = "CfgDiagnostico.findByNombreDiagnostico", query = "SELECT c FROM CfgDiagnostico c WHERE c.nombreDiagnostico = :nombreDiagnostico"),
    @NamedQuery(name = "CfgDiagnostico.findByCodigoEspecialidad", query = "SELECT c FROM CfgDiagnostico c WHERE c.codigoEspecialidad = :codigoEspecialidad"),
    @NamedQuery(name = "CfgDiagnostico.findByCodigoEvento", query = "SELECT c FROM CfgDiagnostico c WHERE c.codigoEvento = :codigoEvento"),
    @NamedQuery(name = "CfgDiagnostico.findBySimbolo", query = "SELECT c FROM CfgDiagnostico c WHERE c.simbolo = :simbolo"),
    @NamedQuery(name = "CfgDiagnostico.findBySexo", query = "SELECT c FROM CfgDiagnostico c WHERE c.sexo = :sexo"),
    @NamedQuery(name = "CfgDiagnostico.findByEdad", query = "SELECT c FROM CfgDiagnostico c WHERE c.edad = :edad"),
    @NamedQuery(name = "CfgDiagnostico.findByUnidadEdad", query = "SELECT c FROM CfgDiagnostico c WHERE c.unidadEdad = :unidadEdad"),
    @NamedQuery(name = "CfgDiagnostico.findByEdadFinal", query = "SELECT c FROM CfgDiagnostico c WHERE c.edadFinal = :edadFinal"),
    @NamedQuery(name = "CfgDiagnostico.findByUnidadEdadFinal", query = "SELECT c FROM CfgDiagnostico c WHERE c.unidadEdadFinal = :unidadEdadFinal"),
    @NamedQuery(name = "CfgDiagnostico.findByAtencionObligatoria", query = "SELECT c FROM CfgDiagnostico c WHERE c.atencionObligatoria = :atencionObligatoria"),
    @NamedQuery(name = "CfgDiagnostico.findByVigilanciaEpidemiologica", query = "SELECT c FROM CfgDiagnostico c WHERE c.vigilanciaEpidemiologica = :vigilanciaEpidemiologica"),
    @NamedQuery(name = "CfgDiagnostico.findByCent", query = "SELECT c FROM CfgDiagnostico c WHERE c.cent = :cent"),
    @NamedQuery(name = "CfgDiagnostico.findByPatCron", query = "SELECT c FROM CfgDiagnostico c WHERE c.patCron = :patCron"),
    @NamedQuery(name = "CfgDiagnostico.findByDisMental", query = "SELECT c FROM CfgDiagnostico c WHERE c.disMental = :disMental"),
    @NamedQuery(name = "CfgDiagnostico.findByDisSensorial", query = "SELECT c FROM CfgDiagnostico c WHERE c.disSensorial = :disSensorial"),
    @NamedQuery(name = "CfgDiagnostico.findByDisMotriz", query = "SELECT c FROM CfgDiagnostico c WHERE c.disMotriz = :disMotriz")})
public class CfgDiagnostico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codigo_diagnostico", nullable = false, length = 10)
    private String codigoDiagnostico;
    @Column(name = "nombre_diagnostico", length = 2147483647)
    private String nombreDiagnostico;
    @Column(name = "codigo_especialidad", length = 5)
    private String codigoEspecialidad;
    @Column(name = "codigo_evento", length = 5)
    private String codigoEvento;
    @Column(name = "simbolo", length = 1)
    private String simbolo;
    @Column(name = "sexo", length = 1)
    private String sexo;
    @Column(name = "edad")
    private Short edad;
    @Column(name = "unidad_edad", length = 1)
    private String unidadEdad;
    @Column(name = "edad_final")
    private Short edadFinal;
    @Column(name = "unidad_edad_final", length = 1)
    private String unidadEdadFinal;
    @Column(name = "atencion_obligatoria")
    private Boolean atencionObligatoria;
    @Column(name = "vigilancia_epidemiologica")
    private Boolean vigilanciaEpidemiologica;
    @Column(name = "cent")
    private Boolean cent;
    @Column(name = "pat_cron")
    private Boolean patCron;
    @Column(name = "dis_mental")
    private Boolean disMental;
    @Column(name = "dis_sensorial")
    private Boolean disSensorial;
    @Column(name = "dis_motriz")
    private Boolean disMotriz;
    @OneToMany(mappedBy = "codigoDiagnostico")
    private List<FacServicio> facServicioList;
    @OneToMany(mappedBy = "codigoDiagnostico")
    private List<FacPrograma> facProgramaList;

    public CfgDiagnostico() {
    }

    public CfgDiagnostico(String codigoDiagnostico) {
        this.codigoDiagnostico = codigoDiagnostico;
    }

    public String getCodigoDiagnostico() {
        return codigoDiagnostico;
    }

    public void setCodigoDiagnostico(String codigoDiagnostico) {
        this.codigoDiagnostico = codigoDiagnostico;
    }

    public String getNombreDiagnostico() {
        return nombreDiagnostico;
    }

    public void setNombreDiagnostico(String nombreDiagnostico) {
        this.nombreDiagnostico = nombreDiagnostico;
    }

    public String getCodigoEspecialidad() {
        return codigoEspecialidad;
    }

    public void setCodigoEspecialidad(String codigoEspecialidad) {
        this.codigoEspecialidad = codigoEspecialidad;
    }

    public String getCodigoEvento() {
        return codigoEvento;
    }

    public void setCodigoEvento(String codigoEvento) {
        this.codigoEvento = codigoEvento;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Short getEdad() {
        return edad;
    }

    public void setEdad(Short edad) {
        this.edad = edad;
    }

    public String getUnidadEdad() {
        return unidadEdad;
    }

    public void setUnidadEdad(String unidadEdad) {
        this.unidadEdad = unidadEdad;
    }

    public Short getEdadFinal() {
        return edadFinal;
    }

    public void setEdadFinal(Short edadFinal) {
        this.edadFinal = edadFinal;
    }

    public String getUnidadEdadFinal() {
        return unidadEdadFinal;
    }

    public void setUnidadEdadFinal(String unidadEdadFinal) {
        this.unidadEdadFinal = unidadEdadFinal;
    }

    public Boolean getAtencionObligatoria() {
        return atencionObligatoria;
    }

    public void setAtencionObligatoria(Boolean atencionObligatoria) {
        this.atencionObligatoria = atencionObligatoria;
    }

    public Boolean getVigilanciaEpidemiologica() {
        return vigilanciaEpidemiologica;
    }

    public void setVigilanciaEpidemiologica(Boolean vigilanciaEpidemiologica) {
        this.vigilanciaEpidemiologica = vigilanciaEpidemiologica;
    }

    public Boolean getCent() {
        return cent;
    }

    public void setCent(Boolean cent) {
        this.cent = cent;
    }

    public Boolean getPatCron() {
        return patCron;
    }

    public void setPatCron(Boolean patCron) {
        this.patCron = patCron;
    }

    public Boolean getDisMental() {
        return disMental;
    }

    public void setDisMental(Boolean disMental) {
        this.disMental = disMental;
    }

    public Boolean getDisSensorial() {
        return disSensorial;
    }

    public void setDisSensorial(Boolean disSensorial) {
        this.disSensorial = disSensorial;
    }

    public Boolean getDisMotriz() {
        return disMotriz;
    }

    public void setDisMotriz(Boolean disMotriz) {
        this.disMotriz = disMotriz;
    }

    @XmlTransient
    public List<FacServicio> getFacServicioList() {
        return facServicioList;
    }

    public void setFacServicioList(List<FacServicio> facServicioList) {
        this.facServicioList = facServicioList;
    }

    @XmlTransient
    public List<FacPrograma> getFacProgramaList() {
        return facProgramaList;
    }

    public void setFacProgramaList(List<FacPrograma> facProgramaList) {
        this.facProgramaList = facProgramaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoDiagnostico != null ? codigoDiagnostico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgDiagnostico)) {
            return false;
        }
        CfgDiagnostico other = (CfgDiagnostico) object;
        if ((this.codigoDiagnostico == null && other.codigoDiagnostico != null) || (this.codigoDiagnostico != null && !this.codigoDiagnostico.equals(other.codigoDiagnostico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.CfgDiagnostico[ codigoDiagnostico=" + codigoDiagnostico + " ]";
    }
    
}
