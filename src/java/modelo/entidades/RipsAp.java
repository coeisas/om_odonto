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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author santos
 */
@Entity
@Table(name = "rips_ap", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RipsAp.findAll", query = "SELECT r FROM RipsAp r"),
    @NamedQuery(name = "RipsAp.findByIdRipAlmacenado", query = "SELECT r FROM RipsAp r WHERE r.ripsApPK.idRipAlmacenado = :idRipAlmacenado"),
    @NamedQuery(name = "RipsAp.findByNumRegistro", query = "SELECT r FROM RipsAp r WHERE r.ripsApPK.numRegistro = :numRegistro"),
    @NamedQuery(name = "RipsAp.findByNumFac", query = "SELECT r FROM RipsAp r WHERE r.numFac = :numFac"),
    @NamedQuery(name = "RipsAp.findByCodPre", query = "SELECT r FROM RipsAp r WHERE r.codPre = :codPre"),
    @NamedQuery(name = "RipsAp.findByTipIde", query = "SELECT r FROM RipsAp r WHERE r.tipIde = :tipIde"),
    @NamedQuery(name = "RipsAp.findByNumIde", query = "SELECT r FROM RipsAp r WHERE r.numIde = :numIde"),
    @NamedQuery(name = "RipsAp.findByFecProc", query = "SELECT r FROM RipsAp r WHERE r.fecProc = :fecProc"),
    @NamedQuery(name = "RipsAp.findByNumAut", query = "SELECT r FROM RipsAp r WHERE r.numAut = :numAut"),
    @NamedQuery(name = "RipsAp.findByCodPro", query = "SELECT r FROM RipsAp r WHERE r.codPro = :codPro"),
    @NamedQuery(name = "RipsAp.findByAmbPro", query = "SELECT r FROM RipsAp r WHERE r.ambPro = :ambPro"),
    @NamedQuery(name = "RipsAp.findByFinPro", query = "SELECT r FROM RipsAp r WHERE r.finPro = :finPro"),
    @NamedQuery(name = "RipsAp.findByPersAti", query = "SELECT r FROM RipsAp r WHERE r.persAti = :persAti"),
    @NamedQuery(name = "RipsAp.findByDxPpal", query = "SELECT r FROM RipsAp r WHERE r.dxPpal = :dxPpal"),
    @NamedQuery(name = "RipsAp.findByDxRel", query = "SELECT r FROM RipsAp r WHERE r.dxRel = :dxRel"),
    @NamedQuery(name = "RipsAp.findByComplicacion", query = "SELECT r FROM RipsAp r WHERE r.complicacion = :complicacion"),
    @NamedQuery(name = "RipsAp.findByActQuirur", query = "SELECT r FROM RipsAp r WHERE r.actQuirur = :actQuirur"),
    @NamedQuery(name = "RipsAp.findByValor", query = "SELECT r FROM RipsAp r WHERE r.valor = :valor")})
public class RipsAp implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RipsApPK ripsApPK;
    @Size(max = 20)
    @Column(name = "num_fac", length = 20)
    private String numFac;
    @Size(max = 12)
    @Column(name = "cod_pre", length = 12)
    private String codPre;
    @Size(max = 2)
    @Column(name = "tip_ide", length = 2)
    private String tipIde;
    @Size(max = 20)
    @Column(name = "num_ide", length = 20)
    private String numIde;
    @Size(max = 10)
    @Column(name = "fec_proc", length = 10)
    private String fecProc;
    @Size(max = 15)
    @Column(name = "num_aut", length = 15)
    private String numAut;
    @Size(max = 20)
    @Column(name = "cod_pro", length = 20)
    private String codPro;
    @Size(max = 1)
    @Column(name = "amb_pro", length = 1)
    private String ambPro;
    @Size(max = 1)
    @Column(name = "fin_pro", length = 1)
    private String finPro;
    @Size(max = 1)
    @Column(name = "pers_ati", length = 1)
    private String persAti;
    @Size(max = 4)
    @Column(name = "dx_ppal", length = 4)
    private String dxPpal;
    @Size(max = 4)
    @Column(name = "dx_rel", length = 4)
    private String dxRel;
    @Size(max = 4)
    @Column(name = "complicacion", length = 4)
    private String complicacion;
    @Size(max = 1)
    @Column(name = "act_quirur", length = 1)
    private String actQuirur;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor", precision = 17, scale = 17)
    private Double valor;
    @JoinColumn(name = "id_rip_almacenado", referencedColumnName = "id_rip_almacenado", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private RipsAlmacenados ripsAlmacenados;

    public RipsAp() {
    }

    public RipsAp(RipsApPK ripsApPK) {
        this.ripsApPK = ripsApPK;
    }

    public RipsAp(int idRipAlmacenado, int numRegistro) {
        this.ripsApPK = new RipsApPK(idRipAlmacenado, numRegistro);
    }

    public RipsApPK getRipsApPK() {
        return ripsApPK;
    }

    public void setRipsApPK(RipsApPK ripsApPK) {
        this.ripsApPK = ripsApPK;
    }

    public String getNumFac() {
        return numFac;
    }

    public void setNumFac(String numFac) {
        this.numFac = numFac;
    }

    public String getCodPre() {
        return codPre;
    }

    public void setCodPre(String codPre) {
        this.codPre = codPre;
    }

    public String getTipIde() {
        return tipIde;
    }

    public void setTipIde(String tipIde) {
        this.tipIde = tipIde;
    }

    public String getNumIde() {
        return numIde;
    }

    public void setNumIde(String numIde) {
        this.numIde = numIde;
    }

    public String getFecProc() {
        return fecProc;
    }

    public void setFecProc(String fecProc) {
        this.fecProc = fecProc;
    }

    public String getNumAut() {
        return numAut;
    }

    public void setNumAut(String numAut) {
        this.numAut = numAut;
    }

    public String getCodPro() {
        return codPro;
    }

    public void setCodPro(String codPro) {
        this.codPro = codPro;
    }

    public String getAmbPro() {
        return ambPro;
    }

    public void setAmbPro(String ambPro) {
        this.ambPro = ambPro;
    }

    public String getFinPro() {
        return finPro;
    }

    public void setFinPro(String finPro) {
        this.finPro = finPro;
    }

    public String getPersAti() {
        return persAti;
    }

    public void setPersAti(String persAti) {
        this.persAti = persAti;
    }

    public String getDxPpal() {
        return dxPpal;
    }

    public void setDxPpal(String dxPpal) {
        this.dxPpal = dxPpal;
    }

    public String getDxRel() {
        return dxRel;
    }

    public void setDxRel(String dxRel) {
        this.dxRel = dxRel;
    }

    public String getComplicacion() {
        return complicacion;
    }

    public void setComplicacion(String complicacion) {
        this.complicacion = complicacion;
    }

    public String getActQuirur() {
        return actQuirur;
    }

    public void setActQuirur(String actQuirur) {
        this.actQuirur = actQuirur;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public RipsAlmacenados getRipsAlmacenados() {
        return ripsAlmacenados;
    }

    public void setRipsAlmacenados(RipsAlmacenados ripsAlmacenados) {
        this.ripsAlmacenados = ripsAlmacenados;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ripsApPK != null ? ripsApPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RipsAp)) {
            return false;
        }
        RipsAp other = (RipsAp) object;
        if ((this.ripsApPK == null && other.ripsApPK != null) || (this.ripsApPK != null && !this.ripsApPK.equals(other.ripsApPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.RipsAp[ ripsApPK=" + ripsApPK + " ]";
    }
    
}
