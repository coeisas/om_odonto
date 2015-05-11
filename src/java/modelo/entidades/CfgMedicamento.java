/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "cfg_medicamento", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgMedicamento.findAll", query = "SELECT c FROM CfgMedicamento c"),
    @NamedQuery(name = "CfgMedicamento.findByIdMedicamento", query = "SELECT c FROM CfgMedicamento c WHERE c.idMedicamento = :idMedicamento"),
    @NamedQuery(name = "CfgMedicamento.findByCodigoMedicamento", query = "SELECT c FROM CfgMedicamento c WHERE c.codigoMedicamento = :codigoMedicamento"),
    @NamedQuery(name = "CfgMedicamento.findByCodigoCums", query = "SELECT c FROM CfgMedicamento c WHERE c.codigoCums = :codigoCums"),
    @NamedQuery(name = "CfgMedicamento.findByCodigoCups", query = "SELECT c FROM CfgMedicamento c WHERE c.codigoCups = :codigoCups"),    
    @NamedQuery(name = "CfgMedicamento.findByNombreMedicamento", query = "SELECT c FROM CfgMedicamento c WHERE c.nombreMedicamento = :nombreMedicamento"),
    @NamedQuery(name = "CfgMedicamento.findByNombreGenerico", query = "SELECT c FROM CfgMedicamento c WHERE c.nombreGenerico = :nombreGenerico"),
    @NamedQuery(name = "CfgMedicamento.findByNombreComercial", query = "SELECT c FROM CfgMedicamento c WHERE c.nombreComercial = :nombreComercial"),
    @NamedQuery(name = "CfgMedicamento.findByFormaMedicamento", query = "SELECT c FROM CfgMedicamento c WHERE c.formaMedicamento = :formaMedicamento"),
    @NamedQuery(name = "CfgMedicamento.findByPos", query = "SELECT c FROM CfgMedicamento c WHERE c.pos = :pos"),
    @NamedQuery(name = "CfgMedicamento.findByConcentracion", query = "SELECT c FROM CfgMedicamento c WHERE c.concentracion = :concentracion"),
    @NamedQuery(name = "CfgMedicamento.findByUnidadMedida", query = "SELECT c FROM CfgMedicamento c WHERE c.unidadMedida = :unidadMedida"),
    @NamedQuery(name = "CfgMedicamento.findByControlMedico", query = "SELECT c FROM CfgMedicamento c WHERE c.controlMedico = :controlMedico"),
    @NamedQuery(name = "CfgMedicamento.findByRegistroSanitario", query = "SELECT c FROM CfgMedicamento c WHERE c.registroSanitario = :registroSanitario"),
    @NamedQuery(name = "CfgMedicamento.findByModAdmin", query = "SELECT c FROM CfgMedicamento c WHERE c.modAdmin = :modAdmin"),
    @NamedQuery(name = "CfgMedicamento.findByValor", query = "SELECT c FROM CfgMedicamento c WHERE c.valor = :valor"),
    @NamedQuery(name = "CfgMedicamento.findByAplicaIva", query = "SELECT c FROM CfgMedicamento c WHERE c.aplicaIva = :aplicaIva"),
    @NamedQuery(name = "CfgMedicamento.findByAplicaCree", query = "SELECT c FROM CfgMedicamento c WHERE c.aplicaCree = :aplicaCree")})
public class CfgMedicamento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_medicamento", nullable = false)
    private Integer idMedicamento;
    @Column(name = "codigo_medicamento", length = 20)
    private String codigoMedicamento;
    @Column(name = "codigo_cums", length = 20)
    private String codigoCums;
    @Column(name = "codigo_cups", length = 20)
    private String codigoCups;    
    @Column(name = "nombre_medicamento", length = 150)
    private String nombreMedicamento;
    @Column(name = "nombre_generico", length = 150)
    private String nombreGenerico;
    @Column(name = "nombre_comercial", length = 150)
    private String nombreComercial;
    @Column(name = "forma_medicamento", length = 150)
    private String formaMedicamento;
    @Column(name = "pos")
    private Boolean pos;
    @Column(name = "concentracion", length = 100)
    private String concentracion;
    @Column(name = "unidad_medida", length = 20)
    private String unidadMedida;
    @Column(name = "control_medico")
    private Boolean controlMedico;
    @Column(name = "registro_sanitario", length = 50)
    private String registroSanitario;
    @Column(name = "mod_admin", length = 50)
    private String modAdmin;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor", precision = 17, scale = 17)
    private Double valor;
    @Column(name = "aplica_iva")
    private Boolean aplicaIva;
    @Column(name = "aplica_cree")
    private Boolean aplicaCree;
    @OneToMany(mappedBy = "idMedicamento")
    private List<FacConsumoMedicamento> facConsumoMedicamentoList;
    @OneToMany(mappedBy = "idMedicamento")
    private List<FacFacturaMedicamento> facFacturaMedicamentoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgMedicamento")
    private List<FacManualTarifarioMedicamento> facManualTarifarioMedicamentoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfgMedicamento")
    private List<FacPaqueteMedicamento> facPaqueteMedicamentoList;

    public CfgMedicamento() {
    }

    public CfgMedicamento(Integer idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public Integer getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(Integer idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getCodigoMedicamento() {
        return codigoMedicamento;
    }

    public void setCodigoMedicamento(String codigoMedicamento) {
        this.codigoMedicamento = codigoMedicamento;
    }

    public String getCodigoCums() {
        return codigoCums;
    }

    public void setCodigoCums(String codigoCums) {
        this.codigoCums = codigoCums;
    }

    public String getCodigoCups() {
        return codigoCups;
    }

    public void setCodigoCups(String codigoCups) {
        this.codigoCups = codigoCups;
    }

    public String getNombreMedicamento() {
        return nombreMedicamento;
    }

    public void setNombreMedicamento(String nombreMedicamento) {
        this.nombreMedicamento = nombreMedicamento;
    }

    public String getNombreGenerico() {
        return nombreGenerico;
    }

    public void setNombreGenerico(String nombreGenerico) {
        this.nombreGenerico = nombreGenerico;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getFormaMedicamento() {
        return formaMedicamento;
    }

    public void setFormaMedicamento(String formaMedicamento) {
        this.formaMedicamento = formaMedicamento;
    }

    public Boolean getPos() {
        return pos;
    }

    public void setPos(Boolean pos) {
        this.pos = pos;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(String concentracion) {
        this.concentracion = concentracion;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Boolean getControlMedico() {
        return controlMedico;
    }

    public void setControlMedico(Boolean controlMedico) {
        this.controlMedico = controlMedico;
    }

    public String getRegistroSanitario() {
        return registroSanitario;
    }

    public void setRegistroSanitario(String registroSanitario) {
        this.registroSanitario = registroSanitario;
    }

    public String getModAdmin() {
        return modAdmin;
    }

    public void setModAdmin(String modAdmin) {
        this.modAdmin = modAdmin;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Boolean getAplicaIva() {
        return aplicaIva;
    }

    public void setAplicaIva(Boolean aplicaIva) {
        this.aplicaIva = aplicaIva;
    }

    public Boolean getAplicaCree() {
        return aplicaCree;
    }

    public void setAplicaCree(Boolean aplicaCree) {
        this.aplicaCree = aplicaCree;
    }

    @XmlTransient
    public List<FacConsumoMedicamento> getFacConsumoMedicamentoList() {
        return facConsumoMedicamentoList;
    }

    public void setFacConsumoMedicamentoList(List<FacConsumoMedicamento> facConsumoMedicamentoList) {
        this.facConsumoMedicamentoList = facConsumoMedicamentoList;
    }

    @XmlTransient
    public List<FacFacturaMedicamento> getFacFacturaMedicamentoList() {
        return facFacturaMedicamentoList;
    }

    public void setFacFacturaMedicamentoList(List<FacFacturaMedicamento> facFacturaMedicamentoList) {
        this.facFacturaMedicamentoList = facFacturaMedicamentoList;
    }

    @XmlTransient
    public List<FacManualTarifarioMedicamento> getFacManualTarifarioMedicamentoList() {
        return facManualTarifarioMedicamentoList;
    }

    public void setFacManualTarifarioMedicamentoList(List<FacManualTarifarioMedicamento> facManualTarifarioMedicamentoList) {
        this.facManualTarifarioMedicamentoList = facManualTarifarioMedicamentoList;
    }

    @XmlTransient
    public List<FacPaqueteMedicamento> getFacPaqueteMedicamentoList() {
        return facPaqueteMedicamentoList;
    }

    public void setFacPaqueteMedicamentoList(List<FacPaqueteMedicamento> facPaqueteMedicamentoList) {
        this.facPaqueteMedicamentoList = facPaqueteMedicamentoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMedicamento != null ? idMedicamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgMedicamento)) {
            return false;
        }
        CfgMedicamento other = (CfgMedicamento) object;
        if ((this.idMedicamento == null && other.idMedicamento != null) || (this.idMedicamento != null && !this.idMedicamento.equals(other.idMedicamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades2.CfgMedicamento[ idMedicamento=" + idMedicamento + " ]";
    }
    
}
