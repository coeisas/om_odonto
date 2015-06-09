/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author santos
 */
@Entity
@Table(name = "rips_ct", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RipsCt.findAll", query = "SELECT r FROM RipsCt r"),
    @NamedQuery(name = "RipsCt.findByIdRipAlmacenado", query = "SELECT r FROM RipsCt r WHERE r.ripsCtPK.idRipAlmacenado = :idRipAlmacenado"),
    @NamedQuery(name = "RipsCt.findByNumRegistro", query = "SELECT r FROM RipsCt r WHERE r.ripsCtPK.numRegistro = :numRegistro"),
    @NamedQuery(name = "RipsCt.findByCodPres", query = "SELECT r FROM RipsCt r WHERE r.codPres = :codPres"),
    @NamedQuery(name = "RipsCt.findByFecRem", query = "SELECT r FROM RipsCt r WHERE r.fecRem = :fecRem"),
    @NamedQuery(name = "RipsCt.findByCodArc", query = "SELECT r FROM RipsCt r WHERE r.codArc = :codArc"),
    @NamedQuery(name = "RipsCt.findByTotalReg", query = "SELECT r FROM RipsCt r WHERE r.totalReg = :totalReg")})
public class RipsCt implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RipsCtPK ripsCtPK;
    @Size(max = 12)
    @Column(name = "cod_pres", length = 12)
    private String codPres;
    @Column(name = "fec_rem")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecRem;
    @Size(max = 20)
    @Column(name = "cod_arc", length = 20)
    private String codArc;
    @Column(name = "total_reg")
    private Integer totalReg;
    @JoinColumn(name = "id_rip_almacenado", referencedColumnName = "id_rip_almacenado", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private RipsAlmacenados ripsAlmacenados;

    public RipsCt() {
    }

    public RipsCt(RipsCtPK ripsCtPK) {
        this.ripsCtPK = ripsCtPK;
    }

    public RipsCt(int idRipAlmacenado, int numRegistro) {
        this.ripsCtPK = new RipsCtPK(idRipAlmacenado, numRegistro);
    }

    public RipsCtPK getRipsCtPK() {
        return ripsCtPK;
    }

    public void setRipsCtPK(RipsCtPK ripsCtPK) {
        this.ripsCtPK = ripsCtPK;
    }

    public String getCodPres() {
        return codPres;
    }

    public void setCodPres(String codPres) {
        this.codPres = codPres;
    }

    public Date getFecRem() {
        return fecRem;
    }

    public void setFecRem(Date fecRem) {
        this.fecRem = fecRem;
    }

    public String getCodArc() {
        return codArc;
    }

    public void setCodArc(String codArc) {
        this.codArc = codArc;
    }

    public Integer getTotalReg() {
        return totalReg;
    }

    public void setTotalReg(Integer totalReg) {
        this.totalReg = totalReg;
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
        hash += (ripsCtPK != null ? ripsCtPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RipsCt)) {
            return false;
        }
        RipsCt other = (RipsCt) object;
        if ((this.ripsCtPK == null && other.ripsCtPK != null) || (this.ripsCtPK != null && !this.ripsCtPK.equals(other.ripsCtPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.RipsCt[ ripsCtPK=" + ripsCtPK + " ]";
    }
    
}
