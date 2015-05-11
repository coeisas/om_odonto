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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author santos
 */
@Entity
@Table(name = "fac_programa", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacPrograma.findAll", query = "SELECT f FROM FacPrograma f"),
    @NamedQuery(name = "FacPrograma.findByIdPrograma", query = "SELECT f FROM FacPrograma f WHERE f.idPrograma = :idPrograma"),
    @NamedQuery(name = "FacPrograma.findByCodigoPrograma", query = "SELECT f FROM FacPrograma f WHERE f.codigoPrograma = :codigoPrograma"),
    @NamedQuery(name = "FacPrograma.findByNombrePrograma", query = "SELECT f FROM FacPrograma f WHERE f.nombrePrograma = :nombrePrograma"),
    @NamedQuery(name = "FacPrograma.findByActivo", query = "SELECT f FROM FacPrograma f WHERE f.activo = :activo"),
    @NamedQuery(name = "FacPrograma.findByCm1", query = "SELECT f FROM FacPrograma f WHERE f.cm1 = :cm1"),
    @NamedQuery(name = "FacPrograma.findByCm2", query = "SELECT f FROM FacPrograma f WHERE f.cm2 = :cm2"),
    @NamedQuery(name = "FacPrograma.findByCm3", query = "SELECT f FROM FacPrograma f WHERE f.cm3 = :cm3"),
    @NamedQuery(name = "FacPrograma.findByCm4", query = "SELECT f FROM FacPrograma f WHERE f.cm4 = :cm4"),
    @NamedQuery(name = "FacPrograma.findByCm5", query = "SELECT f FROM FacPrograma f WHERE f.cm5 = :cm5"),
    @NamedQuery(name = "FacPrograma.findByCmc", query = "SELECT f FROM FacPrograma f WHERE f.cmc = :cmc"),
    @NamedQuery(name = "FacPrograma.findByCmb", query = "SELECT f FROM FacPrograma f WHERE f.cmb = :cmb"),
    @NamedQuery(name = "FacPrograma.findByCp1", query = "SELECT f FROM FacPrograma f WHERE f.cp1 = :cp1"),
    @NamedQuery(name = "FacPrograma.findByCp2", query = "SELECT f FROM FacPrograma f WHERE f.cp2 = :cp2"),
    @NamedQuery(name = "FacPrograma.findByCp3", query = "SELECT f FROM FacPrograma f WHERE f.cp3 = :cp3"),
    @NamedQuery(name = "FacPrograma.findByCp4", query = "SELECT f FROM FacPrograma f WHERE f.cp4 = :cp4"),
    @NamedQuery(name = "FacPrograma.findByCp5", query = "SELECT f FROM FacPrograma f WHERE f.cp5 = :cp5"),
    @NamedQuery(name = "FacPrograma.findByCpc", query = "SELECT f FROM FacPrograma f WHERE f.cpc = :cpc"),
    @NamedQuery(name = "FacPrograma.findByCpb", query = "SELECT f FROM FacPrograma f WHERE f.cpb = :cpb"),
    @NamedQuery(name = "FacPrograma.findByMedicamentoValor1", query = "SELECT f FROM FacPrograma f WHERE f.medicamentoValor1 = :medicamentoValor1"),
    @NamedQuery(name = "FacPrograma.findByMedicamentoValor2", query = "SELECT f FROM FacPrograma f WHERE f.medicamentoValor2 = :medicamentoValor2"),
    @NamedQuery(name = "FacPrograma.findByMedicamentoValor3", query = "SELECT f FROM FacPrograma f WHERE f.medicamentoValor3 = :medicamentoValor3"),
    @NamedQuery(name = "FacPrograma.findByInsumosPorcentaje1", query = "SELECT f FROM FacPrograma f WHERE f.insumosPorcentaje1 = :insumosPorcentaje1"),
    @NamedQuery(name = "FacPrograma.findByInsumosPorcentaje2", query = "SELECT f FROM FacPrograma f WHERE f.insumosPorcentaje2 = :insumosPorcentaje2"),
    @NamedQuery(name = "FacPrograma.findByInsumosPorcentaje3", query = "SELECT f FROM FacPrograma f WHERE f.insumosPorcentaje3 = :insumosPorcentaje3")})
public class FacPrograma implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_programa", nullable = false)
    private Integer idPrograma;
    @Basic(optional = false)
    @Column(name = "codigo_programa", nullable = false, length = 10)
    private String codigoPrograma;
    @Column(name = "nombre_programa", length = 150)
    private String nombrePrograma;
    @Column(name = "activo")
    private Boolean activo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cm1", precision = 17, scale = 17)
    private Double cm1;
    @Column(name = "cm2", precision = 17, scale = 17)
    private Double cm2;
    @Column(name = "cm3", precision = 17, scale = 17)
    private Double cm3;
    @Column(name = "cm4", precision = 17, scale = 17)
    private Double cm4;
    @Column(name = "cm5", precision = 17, scale = 17)
    private Double cm5;
    @Column(name = "cmc")
    private Boolean cmc;
    @Column(name = "cmb")
    private Boolean cmb;
    @Column(name = "cp1", precision = 17, scale = 17)
    private Double cp1;
    @Column(name = "cp2", precision = 17, scale = 17)
    private Double cp2;
    @Column(name = "cp3", precision = 17, scale = 17)
    private Double cp3;
    @Column(name = "cp4", precision = 17, scale = 17)
    private Double cp4;
    @Column(name = "cp5", precision = 17, scale = 17)
    private Double cp5;
    @Column(name = "cpc")
    private Boolean cpc;
    @Column(name = "cpb")
    private Boolean cpb;
    @Column(name = "medicamento_valor_1", precision = 17, scale = 17)
    private Double medicamentoValor1;
    @Column(name = "medicamento_valor_2", precision = 17, scale = 17)
    private Double medicamentoValor2;
    @Column(name = "medicamento_valor_3", precision = 17, scale = 17)
    private Double medicamentoValor3;
    @Column(name = "insumos_porcentaje_1", precision = 17, scale = 17)
    private Double insumosPorcentaje1;
    @Column(name = "insumos_porcentaje_2", precision = 17, scale = 17)
    private Double insumosPorcentaje2;
    @Column(name = "insumos_porcentaje_3", precision = 17, scale = 17)
    private Double insumosPorcentaje3;
    @JoinColumn(name = "id_manual_tarifario", referencedColumnName = "id_manual_tarifario")
    @ManyToOne
    private FacManualTarifario idManualTarifario;
    @JoinColumn(name = "id_contrato", referencedColumnName = "id_contrato")
    @ManyToOne
    private FacContrato idContrato;
    @JoinColumn(name = "codigo_diagnostico", referencedColumnName = "codigo_diagnostico")
    @ManyToOne
    private CfgDiagnostico codigoDiagnostico;
    @JoinColumn(name = "finalidad_procedimineto", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones finalidadProcedimineto;
    @JoinColumn(name = "finalidad_consulta", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones finalidadConsulta;
    @JoinColumn(name = "causa_externa", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones causaExterna;

    public FacPrograma() {
    }

    public FacPrograma(Integer idPrograma) {
        this.idPrograma = idPrograma;
    }

    public FacPrograma(Integer idPrograma, String codigoPrograma) {
        this.idPrograma = idPrograma;
        this.codigoPrograma = codigoPrograma;
    }

    public Integer getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(Integer idPrograma) {
        this.idPrograma = idPrograma;
    }

    public String getCodigoPrograma() {
        return codigoPrograma;
    }

    public void setCodigoPrograma(String codigoPrograma) {
        this.codigoPrograma = codigoPrograma;
    }

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Double getCm1() {
        return cm1;
    }

    public void setCm1(Double cm1) {
        this.cm1 = cm1;
    }

    public Double getCm2() {
        return cm2;
    }

    public void setCm2(Double cm2) {
        this.cm2 = cm2;
    }

    public Double getCm3() {
        return cm3;
    }

    public void setCm3(Double cm3) {
        this.cm3 = cm3;
    }

    public Double getCm4() {
        return cm4;
    }

    public void setCm4(Double cm4) {
        this.cm4 = cm4;
    }

    public Double getCm5() {
        return cm5;
    }

    public void setCm5(Double cm5) {
        this.cm5 = cm5;
    }

    public Boolean getCmc() {
        return cmc;
    }

    public void setCmc(Boolean cmc) {
        this.cmc = cmc;
    }

    public Boolean getCmb() {
        return cmb;
    }

    public void setCmb(Boolean cmb) {
        this.cmb = cmb;
    }

    public Double getCp1() {
        return cp1;
    }

    public void setCp1(Double cp1) {
        this.cp1 = cp1;
    }

    public Double getCp2() {
        return cp2;
    }

    public void setCp2(Double cp2) {
        this.cp2 = cp2;
    }

    public Double getCp3() {
        return cp3;
    }

    public void setCp3(Double cp3) {
        this.cp3 = cp3;
    }

    public Double getCp4() {
        return cp4;
    }

    public void setCp4(Double cp4) {
        this.cp4 = cp4;
    }

    public Double getCp5() {
        return cp5;
    }

    public void setCp5(Double cp5) {
        this.cp5 = cp5;
    }

    public Boolean getCpc() {
        return cpc;
    }

    public void setCpc(Boolean cpc) {
        this.cpc = cpc;
    }

    public Boolean getCpb() {
        return cpb;
    }

    public void setCpb(Boolean cpb) {
        this.cpb = cpb;
    }

    public Double getMedicamentoValor1() {
        return medicamentoValor1;
    }

    public void setMedicamentoValor1(Double medicamentoValor1) {
        this.medicamentoValor1 = medicamentoValor1;
    }

    public Double getMedicamentoValor2() {
        return medicamentoValor2;
    }

    public void setMedicamentoValor2(Double medicamentoValor2) {
        this.medicamentoValor2 = medicamentoValor2;
    }

    public Double getMedicamentoValor3() {
        return medicamentoValor3;
    }

    public void setMedicamentoValor3(Double medicamentoValor3) {
        this.medicamentoValor3 = medicamentoValor3;
    }

    public Double getInsumosPorcentaje1() {
        return insumosPorcentaje1;
    }

    public void setInsumosPorcentaje1(Double insumosPorcentaje1) {
        this.insumosPorcentaje1 = insumosPorcentaje1;
    }

    public Double getInsumosPorcentaje2() {
        return insumosPorcentaje2;
    }

    public void setInsumosPorcentaje2(Double insumosPorcentaje2) {
        this.insumosPorcentaje2 = insumosPorcentaje2;
    }

    public Double getInsumosPorcentaje3() {
        return insumosPorcentaje3;
    }

    public void setInsumosPorcentaje3(Double insumosPorcentaje3) {
        this.insumosPorcentaje3 = insumosPorcentaje3;
    }

    public FacManualTarifario getIdManualTarifario() {
        return idManualTarifario;
    }

    public void setIdManualTarifario(FacManualTarifario idManualTarifario) {
        this.idManualTarifario = idManualTarifario;
    }

    public FacContrato getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(FacContrato idContrato) {
        this.idContrato = idContrato;
    }

    public CfgDiagnostico getCodigoDiagnostico() {
        return codigoDiagnostico;
    }

    public void setCodigoDiagnostico(CfgDiagnostico codigoDiagnostico) {
        this.codigoDiagnostico = codigoDiagnostico;
    }

    public CfgClasificaciones getFinalidadProcedimineto() {
        return finalidadProcedimineto;
    }

    public void setFinalidadProcedimineto(CfgClasificaciones finalidadProcedimineto) {
        this.finalidadProcedimineto = finalidadProcedimineto;
    }

    public CfgClasificaciones getFinalidadConsulta() {
        return finalidadConsulta;
    }

    public void setFinalidadConsulta(CfgClasificaciones finalidadConsulta) {
        this.finalidadConsulta = finalidadConsulta;
    }

    public CfgClasificaciones getCausaExterna() {
        return causaExterna;
    }

    public void setCausaExterna(CfgClasificaciones causaExterna) {
        this.causaExterna = causaExterna;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPrograma != null ? idPrograma.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacPrograma)) {
            return false;
        }
        FacPrograma other = (FacPrograma) object;
        if ((this.idPrograma == null && other.idPrograma != null) || (this.idPrograma != null && !this.idPrograma.equals(other.idPrograma))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.FacPrograma[ idPrograma=" + idPrograma + " ]";
    }
    
}
