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
@Table(name = "rips_ac", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RipsAc.findAll", query = "SELECT r FROM RipsAc r"),
    @NamedQuery(name = "RipsAc.findByIdRipAlmacenado", query = "SELECT r FROM RipsAc r WHERE r.ripsAcPK.idRipAlmacenado = :idRipAlmacenado"),
    @NamedQuery(name = "RipsAc.findByNumRegistro", query = "SELECT r FROM RipsAc r WHERE r.ripsAcPK.numRegistro = :numRegistro"),
    @NamedQuery(name = "RipsAc.findByNumFac", query = "SELECT r FROM RipsAc r WHERE r.numFac = :numFac"),
    @NamedQuery(name = "RipsAc.findByCodPre", query = "SELECT r FROM RipsAc r WHERE r.codPre = :codPre"),
    @NamedQuery(name = "RipsAc.findByTipIde", query = "SELECT r FROM RipsAc r WHERE r.tipIde = :tipIde"),
    @NamedQuery(name = "RipsAc.findByNumIde", query = "SELECT r FROM RipsAc r WHERE r.numIde = :numIde"),
    @NamedQuery(name = "RipsAc.findByFecCons", query = "SELECT r FROM RipsAc r WHERE r.fecCons = :fecCons"),
    @NamedQuery(name = "RipsAc.findByNumAut", query = "SELECT r FROM RipsAc r WHERE r.numAut = :numAut"),
    @NamedQuery(name = "RipsAc.findByCodCon", query = "SELECT r FROM RipsAc r WHERE r.codCon = :codCon"),
    @NamedQuery(name = "RipsAc.findByFinCon", query = "SELECT r FROM RipsAc r WHERE r.finCon = :finCon"),
    @NamedQuery(name = "RipsAc.findByCauExt", query = "SELECT r FROM RipsAc r WHERE r.cauExt = :cauExt"),
    @NamedQuery(name = "RipsAc.findByDxPpal", query = "SELECT r FROM RipsAc r WHERE r.dxPpal = :dxPpal"),
    @NamedQuery(name = "RipsAc.findByDxRel1", query = "SELECT r FROM RipsAc r WHERE r.dxRel1 = :dxRel1"),
    @NamedQuery(name = "RipsAc.findByDxRel2", query = "SELECT r FROM RipsAc r WHERE r.dxRel2 = :dxRel2"),
    @NamedQuery(name = "RipsAc.findByDxRel3", query = "SELECT r FROM RipsAc r WHERE r.dxRel3 = :dxRel3"),
    @NamedQuery(name = "RipsAc.findByDxRel4", query = "SELECT r FROM RipsAc r WHERE r.dxRel4 = :dxRel4"),
    @NamedQuery(name = "RipsAc.findByTipoDxPpal", query = "SELECT r FROM RipsAc r WHERE r.tipoDxPpal = :tipoDxPpal"),
    @NamedQuery(name = "RipsAc.findByVlrCons", query = "SELECT r FROM RipsAc r WHERE r.vlrCons = :vlrCons"),
    @NamedQuery(name = "RipsAc.findByVlrCuoMod", query = "SELECT r FROM RipsAc r WHERE r.vlrCuoMod = :vlrCuoMod"),
    @NamedQuery(name = "RipsAc.findByVlrNeto", query = "SELECT r FROM RipsAc r WHERE r.vlrNeto = :vlrNeto")})
public class RipsAc implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RipsAcPK ripsAcPK;
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
    @Column(name = "fec_cons", length = 10)
    private String fecCons;
    @Size(max = 15)
    @Column(name = "num_aut", length = 15)
    private String numAut;
    @Size(max = 20)
    @Column(name = "cod_con", length = 20)
    private String codCon;
    @Size(max = 2)
    @Column(name = "fin_con", length = 2)
    private String finCon;
    @Size(max = 2)
    @Column(name = "cau_ext", length = 2)
    private String cauExt;
    @Size(max = 4)
    @Column(name = "dx_ppal", length = 4)
    private String dxPpal;
    @Size(max = 4)
    @Column(name = "dx_rel1", length = 4)
    private String dxRel1;
    @Size(max = 4)
    @Column(name = "dx_rel2", length = 4)
    private String dxRel2;
    @Size(max = 4)
    @Column(name = "dx_rel3", length = 4)
    private String dxRel3;
    @Size(max = 4)
    @Column(name = "dx_rel4", length = 4)
    private String dxRel4;
    @Column(name = "tipo_dx_ppal", length = 1)
    private String tipoDxPpal;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "vlr_cons", precision = 17, scale = 17)
    private Double vlrCons;
    @Column(name = "vlr_cuo_mod", precision = 17, scale = 17)
    private Double vlrCuoMod;
    @Column(name = "vlr_neto", precision = 17, scale = 17)
    private Double vlrNeto;
    @JoinColumn(name = "id_rip_almacenado", referencedColumnName = "id_rip_almacenado", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private RipsAlmacenados ripsAlmacenados;

    public RipsAc() {
    }

    public RipsAc(RipsAcPK ripsAcPK) {
        this.ripsAcPK = ripsAcPK;
    }

    public RipsAc(int idRipAlmacenado, int numRegistro) {
        this.ripsAcPK = new RipsAcPK(idRipAlmacenado, numRegistro);
    }

    public RipsAcPK getRipsAcPK() {
        return ripsAcPK;
    }

    public void setRipsAcPK(RipsAcPK ripsAcPK) {
        this.ripsAcPK = ripsAcPK;
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

    public String getFecCons() {
        return fecCons;
    }

    public void setFecCons(String fecCons) {
        this.fecCons = fecCons;
    }

    public String getNumAut() {
        return numAut;
    }

    public void setNumAut(String numAut) {
        this.numAut = numAut;
    }

    public String getCodCon() {
        return codCon;
    }

    public void setCodCon(String codCon) {
        this.codCon = codCon;
    }

    public String getFinCon() {
        return finCon;
    }

    public void setFinCon(String finCon) {
        this.finCon = finCon;
    }

    public String getCauExt() {
        return cauExt;
    }

    public void setCauExt(String cauExt) {
        this.cauExt = cauExt;
    }

    public String getDxPpal() {
        return dxPpal;
    }

    public void setDxPpal(String dxPpal) {
        this.dxPpal = dxPpal;
    }

    public String getDxRel1() {
        return dxRel1;
    }

    public void setDxRel1(String dxRel1) {
        this.dxRel1 = dxRel1;
    }

    public String getDxRel2() {
        return dxRel2;
    }

    public void setDxRel2(String dxRel2) {
        this.dxRel2 = dxRel2;
    }

    public String getDxRel3() {
        return dxRel3;
    }

    public void setDxRel3(String dxRel3) {
        this.dxRel3 = dxRel3;
    }

    public String getDxRel4() {
        return dxRel4;
    }

    public void setDxRel4(String dxRel4) {
        this.dxRel4 = dxRel4;
    }

    public String getTipoDxPpal() {
        return tipoDxPpal;
    }

    public void setTipoDxPpal(String tipoDxPpal) {
        this.tipoDxPpal = tipoDxPpal;
    }

    public Double getVlrCons() {
        return vlrCons;
    }

    public void setVlrCons(Double vlrCons) {
        this.vlrCons = vlrCons;
    }

    public Double getVlrCuoMod() {
        return vlrCuoMod;
    }

    public void setVlrCuoMod(Double vlrCuoMod) {
        this.vlrCuoMod = vlrCuoMod;
    }

    public Double getVlrNeto() {
        return vlrNeto;
    }

    public void setVlrNeto(Double vlrNeto) {
        this.vlrNeto = vlrNeto;
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
        hash += (ripsAcPK != null ? ripsAcPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RipsAc)) {
            return false;
        }
        RipsAc other = (RipsAc) object;
        if ((this.ripsAcPK == null && other.ripsAcPK != null) || (this.ripsAcPK != null && !this.ripsAcPK.equals(other.ripsAcPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.RipsAc[ ripsAcPK=" + ripsAcPK + " ]";
    }
    
}
