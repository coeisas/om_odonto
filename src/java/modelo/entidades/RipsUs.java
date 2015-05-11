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
@Table(name = "rips_us", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RipsUs.findAll", query = "SELECT r FROM RipsUs r"),
    @NamedQuery(name = "RipsUs.findByIdRipAlmacenado", query = "SELECT r FROM RipsUs r WHERE r.ripsUsPK.idRipAlmacenado = :idRipAlmacenado"),
    @NamedQuery(name = "RipsUs.findByNumRegistro", query = "SELECT r FROM RipsUs r WHERE r.ripsUsPK.numRegistro = :numRegistro"),
    @NamedQuery(name = "RipsUs.findByTipIde", query = "SELECT r FROM RipsUs r WHERE r.tipIde = :tipIde"),
    @NamedQuery(name = "RipsUs.findByNumIde", query = "SELECT r FROM RipsUs r WHERE r.numIde = :numIde"),
    @NamedQuery(name = "RipsUs.findByCodEntAdm", query = "SELECT r FROM RipsUs r WHERE r.codEntAdm = :codEntAdm"),
    @NamedQuery(name = "RipsUs.findByTipUsu", query = "SELECT r FROM RipsUs r WHERE r.tipUsu = :tipUsu"),
    @NamedQuery(name = "RipsUs.findByPriApe", query = "SELECT r FROM RipsUs r WHERE r.priApe = :priApe"),
    @NamedQuery(name = "RipsUs.findBySegApe", query = "SELECT r FROM RipsUs r WHERE r.segApe = :segApe"),
    @NamedQuery(name = "RipsUs.findByPriNom", query = "SELECT r FROM RipsUs r WHERE r.priNom = :priNom"),
    @NamedQuery(name = "RipsUs.findBySegNom", query = "SELECT r FROM RipsUs r WHERE r.segNom = :segNom"),
    @NamedQuery(name = "RipsUs.findByEdad", query = "SELECT r FROM RipsUs r WHERE r.edad = :edad"),
    @NamedQuery(name = "RipsUs.findByTipoEdad", query = "SELECT r FROM RipsUs r WHERE r.tipoEdad = :tipoEdad"),
    @NamedQuery(name = "RipsUs.findBySexo", query = "SELECT r FROM RipsUs r WHERE r.sexo = :sexo"),
    @NamedQuery(name = "RipsUs.findByCodDepRes", query = "SELECT r FROM RipsUs r WHERE r.codDepRes = :codDepRes"),
    @NamedQuery(name = "RipsUs.findByCodMunRes", query = "SELECT r FROM RipsUs r WHERE r.codMunRes = :codMunRes"),
    @NamedQuery(name = "RipsUs.findByZonaRes", query = "SELECT r FROM RipsUs r WHERE r.zonaRes = :zonaRes")})
public class RipsUs implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RipsUsPK ripsUsPK;
    @Size(max = 2)
    @Column(name = "tip_ide", length = 2)
    private String tipIde;
    @Size(max = 20)
    @Column(name = "num_ide", length = 20)
    private String numIde;
    @Size(max = 6)
    @Column(name = "cod_ent_adm", length = 6)
    private String codEntAdm;
    @Size(max = 1)
    @Column(name = "tip_usu", length = 1)
    private String tipUsu;
    @Size(max = 10)
    @Column(name = "pri_ape", length = 10)
    private String priApe;
    @Size(max = 10)
    @Column(name = "seg_ape", length = 10)
    private String segApe;
    @Size(max = 6)
    @Column(name = "pri_nom", length = 6)
    private String priNom;
    @Size(max = 30)
    @Column(name = "seg_nom", length = 30)
    private String segNom;
    @Column(name = "edad")
    private Integer edad;
    @Size(max = 1)
    @Column(name = "tipo_edad", length = 1)
    private String tipoEdad;
    @Size(max = 1)
    @Column(name = "sexo", length = 1)
    private String sexo;
    @Size(max = 2)
    @Column(name = "cod_dep_res", length = 2)
    private String codDepRes;
    @Size(max = 3)
    @Column(name = "cod_mun_res", length = 3)
    private String codMunRes;
    @Size(max = 1)
    @Column(name = "zona_res", length = 1)
    private String zonaRes;
    @JoinColumn(name = "id_rip_almacenado", referencedColumnName = "id_rip_almacenado", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private RipsAlmacenados ripsAlmacenados;

    public RipsUs() {
    }

    public RipsUs(RipsUsPK ripsUsPK) {
        this.ripsUsPK = ripsUsPK;
    }

    public RipsUs(int idRipAlmacenado, int numRegistro) {
        this.ripsUsPK = new RipsUsPK(idRipAlmacenado, numRegistro);
    }

    public RipsUsPK getRipsUsPK() {
        return ripsUsPK;
    }

    public void setRipsUsPK(RipsUsPK ripsUsPK) {
        this.ripsUsPK = ripsUsPK;
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

    public String getCodEntAdm() {
        return codEntAdm;
    }

    public void setCodEntAdm(String codEntAdm) {
        this.codEntAdm = codEntAdm;
    }

    public String getTipUsu() {
        return tipUsu;
    }

    public void setTipUsu(String tipUsu) {
        this.tipUsu = tipUsu;
    }

    public String getPriApe() {
        return priApe;
    }

    public void setPriApe(String priApe) {
        this.priApe = priApe;
    }

    public String getSegApe() {
        return segApe;
    }

    public void setSegApe(String segApe) {
        this.segApe = segApe;
    }

    public String getPriNom() {
        return priNom;
    }

    public void setPriNom(String priNom) {
        this.priNom = priNom;
    }

    public String getSegNom() {
        return segNom;
    }

    public void setSegNom(String segNom) {
        this.segNom = segNom;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getTipoEdad() {
        return tipoEdad;
    }

    public void setTipoEdad(String tipoEdad) {
        this.tipoEdad = tipoEdad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getCodDepRes() {
        return codDepRes;
    }

    public void setCodDepRes(String codDepRes) {
        this.codDepRes = codDepRes;
    }

    public String getCodMunRes() {
        return codMunRes;
    }

    public void setCodMunRes(String codMunRes) {
        this.codMunRes = codMunRes;
    }

    public String getZonaRes() {
        return zonaRes;
    }

    public void setZonaRes(String zonaRes) {
        this.zonaRes = zonaRes;
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
        hash += (ripsUsPK != null ? ripsUsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RipsUs)) {
            return false;
        }
        RipsUs other = (RipsUs) object;
        if ((this.ripsUsPK == null && other.ripsUsPK != null) || (this.ripsUsPK != null && !this.ripsUsPK.equals(other.ripsUsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.RipsUs[ ripsUsPK=" + ripsUsPK + " ]";
    }
    
}
