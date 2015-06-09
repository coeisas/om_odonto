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
@Table(name = "rips_af", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RipsAf.findAll", query = "SELECT r FROM RipsAf r"),
    @NamedQuery(name = "RipsAf.findByIdRipAlmacenado", query = "SELECT r FROM RipsAf r WHERE r.ripsAfPK.idRipAlmacenado = :idRipAlmacenado"),
    @NamedQuery(name = "RipsAf.findByNumRegistro", query = "SELECT r FROM RipsAf r WHERE r.ripsAfPK.numRegistro = :numRegistro"),
    @NamedQuery(name = "RipsAf.findByCodPre", query = "SELECT r FROM RipsAf r WHERE r.codPre = :codPre"),
    @NamedQuery(name = "RipsAf.findByRazSoc", query = "SELECT r FROM RipsAf r WHERE r.razSoc = :razSoc"),
    @NamedQuery(name = "RipsAf.findByTipIde", query = "SELECT r FROM RipsAf r WHERE r.tipIde = :tipIde"),
    @NamedQuery(name = "RipsAf.findByNumIde", query = "SELECT r FROM RipsAf r WHERE r.numIde = :numIde"),
    @NamedQuery(name = "RipsAf.findByNumFac", query = "SELECT r FROM RipsAf r WHERE r.numFac = :numFac"),
    @NamedQuery(name = "RipsAf.findByFecExp", query = "SELECT r FROM RipsAf r WHERE r.fecExp = :fecExp"),
    @NamedQuery(name = "RipsAf.findByFecInc", query = "SELECT r FROM RipsAf r WHERE r.fecInc = :fecInc"),
    @NamedQuery(name = "RipsAf.findByFecFin", query = "SELECT r FROM RipsAf r WHERE r.fecFin = :fecFin"),
    @NamedQuery(name = "RipsAf.findByCodEnt", query = "SELECT r FROM RipsAf r WHERE r.codEnt = :codEnt"),
    @NamedQuery(name = "RipsAf.findByNomEnt", query = "SELECT r FROM RipsAf r WHERE r.nomEnt = :nomEnt"),
    @NamedQuery(name = "RipsAf.findByNumCon", query = "SELECT r FROM RipsAf r WHERE r.numCon = :numCon"),
    @NamedQuery(name = "RipsAf.findByPlanBen", query = "SELECT r FROM RipsAf r WHERE r.planBen = :planBen"),
    @NamedQuery(name = "RipsAf.findByNumPoli", query = "SELECT r FROM RipsAf r WHERE r.numPoli = :numPoli"),
    @NamedQuery(name = "RipsAf.findByValCopa", query = "SELECT r FROM RipsAf r WHERE r.valCopa = :valCopa"),
    @NamedQuery(name = "RipsAf.findByValCom", query = "SELECT r FROM RipsAf r WHERE r.valCom = :valCom"),
    @NamedQuery(name = "RipsAf.findByValDesc", query = "SELECT r FROM RipsAf r WHERE r.valDesc = :valDesc"),
    @NamedQuery(name = "RipsAf.findByValNet", query = "SELECT r FROM RipsAf r WHERE r.valNet = :valNet")})
public class RipsAf implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RipsAfPK ripsAfPK;
    @Size(max = 12)
    @Column(name = "cod_pre", length = 12)
    private String codPre;
    @Size(max = 60)
    @Column(name = "raz_soc", length = 60)
    private String razSoc;
    @Size(max = 2)
    @Column(name = "tip_ide", length = 2)
    private String tipIde;
    @Size(max = 20)
    @Column(name = "num_ide", length = 20)
    private String numIde;
    @Size(max = 20)
    @Column(name = "num_fac", length = 20)
    private String numFac;
    @Size(max = 10)
    @Column(name = "fec_exp", length = 10)
    private String fecExp;
    @Size(max = 10)
    @Column(name = "fec_inc", length = 10)
    private String fecInc;
    @Size(max = 10)
    @Column(name = "fec_fin", length = 10)
    private String fecFin;
    @Size(max = 20)
    @Column(name = "cod_ent", length = 20)
    private String codEnt;
    @Size(max = 30)
    @Column(name = "nom_ent", length = 30)
    private String nomEnt;
    @Size(max = 15)
    @Column(name = "num_con", length = 15)
    private String numCon;
    @Size(max = 30)
    @Column(name = "plan_ben", length = 30)
    private String planBen;
    @Size(max = 10)
    @Column(name = "num_poli", length = 10)
    private String numPoli;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "val_copa", precision = 17, scale = 17)
    private Double valCopa;
    @Column(name = "val_com", precision = 17, scale = 17)
    private Double valCom;
    @Column(name = "val_desc", precision = 17, scale = 17)
    private Double valDesc;
    @Column(name = "val_net", precision = 17, scale = 17)
    private Double valNet;
    @JoinColumn(name = "id_rip_almacenado", referencedColumnName = "id_rip_almacenado", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private RipsAlmacenados ripsAlmacenados;

    public RipsAf() {
    }

    public RipsAf(RipsAfPK ripsAfPK) {
        this.ripsAfPK = ripsAfPK;
    }

    public RipsAf(int idRipAlmacenado, int numRegistro) {
        this.ripsAfPK = new RipsAfPK(idRipAlmacenado, numRegistro);
    }

    public RipsAfPK getRipsAfPK() {
        return ripsAfPK;
    }

    public void setRipsAfPK(RipsAfPK ripsAfPK) {
        this.ripsAfPK = ripsAfPK;
    }

    public String getCodPre() {
        return codPre;
    }

    public void setCodPre(String codPre) {
        this.codPre = codPre;
    }

    public String getRazSoc() {
        return razSoc;
    }

    public void setRazSoc(String razSoc) {
        this.razSoc = razSoc;
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

    public String getNumFac() {
        return numFac;
    }

    public void setNumFac(String numFac) {
        this.numFac = numFac;
    }

    public String getFecExp() {
        return fecExp;
    }

    public void setFecExp(String fecExp) {
        this.fecExp = fecExp;
    }

    public String getFecInc() {
        return fecInc;
    }

    public void setFecInc(String fecInc) {
        this.fecInc = fecInc;
    }

    public String getFecFin() {
        return fecFin;
    }

    public void setFecFin(String fecFin) {
        this.fecFin = fecFin;
    }

    public String getCodEnt() {
        return codEnt;
    }

    public void setCodEnt(String codEnt) {
        this.codEnt = codEnt;
    }

    public String getNomEnt() {
        return nomEnt;
    }

    public void setNomEnt(String nomEnt) {
        this.nomEnt = nomEnt;
    }

    public String getNumCon() {
        return numCon;
    }

    public void setNumCon(String numCon) {
        this.numCon = numCon;
    }

    public String getPlanBen() {
        return planBen;
    }

    public void setPlanBen(String planBen) {
        this.planBen = planBen;
    }

    public String getNumPoli() {
        return numPoli;
    }

    public void setNumPoli(String numPoli) {
        this.numPoli = numPoli;
    }

    public Double getValCopa() {
        return valCopa;
    }

    public void setValCopa(Double valCopa) {
        this.valCopa = valCopa;
    }

    public Double getValCom() {
        return valCom;
    }

    public void setValCom(Double valCom) {
        this.valCom = valCom;
    }

    public Double getValDesc() {
        return valDesc;
    }

    public void setValDesc(Double valDesc) {
        this.valDesc = valDesc;
    }

    public Double getValNet() {
        return valNet;
    }

    public void setValNet(Double valNet) {
        this.valNet = valNet;
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
        hash += (ripsAfPK != null ? ripsAfPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RipsAf)) {
            return false;
        }
        RipsAf other = (RipsAf) object;
        if ((this.ripsAfPK == null && other.ripsAfPK != null) || (this.ripsAfPK != null && !this.ripsAfPK.equals(other.ripsAfPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.RipsAf[ ripsAfPK=" + ripsAfPK + " ]";
    }
    
}
