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
 * @author Mario
 */
@Entity
@Table(name = "rips_at", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RipsAt.findAll", query = "SELECT r FROM RipsAt r"),
    @NamedQuery(name = "RipsAt.findByIdRipAlmacenado", query = "SELECT r FROM RipsAt r WHERE r.ripsAtPK.idRipAlmacenado = :idRipAlmacenado"),
    @NamedQuery(name = "RipsAt.findByNumRegistro", query = "SELECT r FROM RipsAt r WHERE r.ripsAtPK.numRegistro = :numRegistro"),
    @NamedQuery(name = "RipsAt.findByNumFac", query = "SELECT r FROM RipsAt r WHERE r.numFac = :numFac"),
    @NamedQuery(name = "RipsAt.findByCodPre", query = "SELECT r FROM RipsAt r WHERE r.codPre = :codPre"),
    @NamedQuery(name = "RipsAt.findByTipIde", query = "SELECT r FROM RipsAt r WHERE r.tipIde = :tipIde"),
    @NamedQuery(name = "RipsAt.findByNumIde", query = "SELECT r FROM RipsAt r WHERE r.numIde = :numIde"),
    @NamedQuery(name = "RipsAt.findByNumAut", query = "SELECT r FROM RipsAt r WHERE r.numAut = :numAut"),
    @NamedQuery(name = "RipsAt.findByTipSer", query = "SELECT r FROM RipsAt r WHERE r.tipSer = :tipSer"),
    @NamedQuery(name = "RipsAt.findByCodSer", query = "SELECT r FROM RipsAt r WHERE r.codSer = :codSer"),
    @NamedQuery(name = "RipsAt.findByNomSer", query = "SELECT r FROM RipsAt r WHERE r.nomSer = :nomSer"),
    @NamedQuery(name = "RipsAt.findByCantidad", query = "SELECT r FROM RipsAt r WHERE r.cantidad = :cantidad"),
    @NamedQuery(name = "RipsAt.findByVrUni", query = "SELECT r FROM RipsAt r WHERE r.vrUni = :vrUni"),
    @NamedQuery(name = "RipsAt.findByVrTot", query = "SELECT r FROM RipsAt r WHERE r.vrTot = :vrTot")})
public class RipsAt implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RipsAtPK ripsAtPK;
    @Column(name = "num_fac", length = 20)
    private String numFac;
    @Column(name = "cod_pre", length = 10)
    private String codPre;
    @Column(name = "tip_ide", length = 2)
    private String tipIde;
    @Column(name = "num_ide", length = 20)
    private String numIde;
    @Column(name = "num_aut", length = 15)
    private String numAut;
    @Column(name = "tip_ser", length = 1)
    private String tipSer;
    @Column(name = "cod_ser", length = 20)
    private String codSer;
    @Column(name = "nom_ser", length = 60)
    private String nomSer;    
    @Column(name = "cantidad", length = 5)
    private String cantidad;
    @Column(name = "vr_uni", length = 15)
    private String vrUni;
    @Column(name = "vr_tot", length = 15)
    private String vrTot;
    @JoinColumn(name = "id_rip_almacenado", referencedColumnName = "id_rip_almacenado", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private RipsAlmacenados ripsAlmacenados;

    public RipsAt() {
    }

    public RipsAt(RipsAtPK ripsAtPK) {
        this.ripsAtPK = ripsAtPK;
    }

    public RipsAt(int idRipAlmacenado, int numRegistro) {
        this.ripsAtPK = new RipsAtPK(idRipAlmacenado, numRegistro);
    }

    public RipsAtPK getRipsAtPK() {
        return ripsAtPK;
    }

    public void setRipsAtPK(RipsAtPK ripsAtPK) {
        this.ripsAtPK = ripsAtPK;
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

    public String getNumAut() {
        return numAut;
    }

    public void setNumAut(String numAut) {
        this.numAut = numAut;
    }

    public String getTipSer() {
        return tipSer;
    }

    public void setTipSer(String tipSer) {
        this.tipSer = tipSer;
    }

    public String getCodSer() {
        return codSer;
    }

    public void setCodSer(String codSer) {
        this.codSer = codSer;
    }
    
    public String getNomSer() {
        return nomSer;
    }

    public void setNomSer(String nomSer) {
        this.nomSer = nomSer;
    }    

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getVrUni() {
        return vrUni;
    }

    public void setVrUni(String vrUni) {
        this.vrUni = vrUni;
    }

    public String getVrTot() {
        return vrTot;
    }

    public void setVrTot(String vrTot) {
        this.vrTot = vrTot;
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
        hash += (ripsAtPK != null ? ripsAtPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RipsAt)) {
            return false;
        }
        RipsAt other = (RipsAt) object;
        if ((this.ripsAtPK == null && other.ripsAtPK != null) || (this.ripsAtPK != null && !this.ripsAtPK.equals(other.ripsAtPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.RipsAt[ ripsAtPK=" + ripsAtPK + " ]";
    }
    
}
