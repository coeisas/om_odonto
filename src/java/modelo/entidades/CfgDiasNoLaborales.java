/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mario
 */
@Entity
@Table(name = "cfg_dias_no_laborales", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgDiasNoLaborales.findAll", query = "SELECT c FROM CfgDiasNoLaborales c"),
    @NamedQuery(name = "CfgDiasNoLaborales.findByIdSede", query = "SELECT c FROM CfgDiasNoLaborales c WHERE c.cfgDiasNoLaboralesPK.idSede = :idSede"),
    @NamedQuery(name = "CfgDiasNoLaborales.findByFechaNoLaboral", query = "SELECT c FROM CfgDiasNoLaborales c WHERE c.cfgDiasNoLaboralesPK.fechaNoLaboral = :fechaNoLaboral")})
public class CfgDiasNoLaborales implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CfgDiasNoLaboralesPK cfgDiasNoLaboralesPK;

    public CfgDiasNoLaborales() {
    }

    public CfgDiasNoLaborales(CfgDiasNoLaboralesPK cfgDiasNoLaboralesPK) {
        this.cfgDiasNoLaboralesPK = cfgDiasNoLaboralesPK;
    }

    public CfgDiasNoLaborales(int idSede, Date fechaNoLaboral) {
        this.cfgDiasNoLaboralesPK = new CfgDiasNoLaboralesPK(idSede, fechaNoLaboral);
    }

    public CfgDiasNoLaboralesPK getCfgDiasNoLaboralesPK() {
        return cfgDiasNoLaboralesPK;
    }

    public void setCfgDiasNoLaboralesPK(CfgDiasNoLaboralesPK cfgDiasNoLaboralesPK) {
        this.cfgDiasNoLaboralesPK = cfgDiasNoLaboralesPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cfgDiasNoLaboralesPK != null ? cfgDiasNoLaboralesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgDiasNoLaborales)) {
            return false;
        }
        CfgDiasNoLaborales other = (CfgDiasNoLaborales) object;
        if ((this.cfgDiasNoLaboralesPK == null && other.cfgDiasNoLaboralesPK != null) || (this.cfgDiasNoLaboralesPK != null && !this.cfgDiasNoLaboralesPK.equals(other.cfgDiasNoLaboralesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.CfgDiasNoLaborales[ cfgDiasNoLaboralesPK=" + cfgDiasNoLaboralesPK + " ]";
    }
    
}
