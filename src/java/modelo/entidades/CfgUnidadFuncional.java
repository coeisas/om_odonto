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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author santos
 */
@Entity
@Table(name = "cfg_unidad_funcional", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgUnidadFuncional.findAll", query = "SELECT c FROM CfgUnidadFuncional c"),
    @NamedQuery(name = "CfgUnidadFuncional.findByCodUndFuncional", query = "SELECT c FROM CfgUnidadFuncional c WHERE c.codUndFuncional = :codUndFuncional"),
    @NamedQuery(name = "CfgUnidadFuncional.findByNomUndFuncional", query = "SELECT c FROM CfgUnidadFuncional c WHERE c.nomUndFuncional = :nomUndFuncional"),
    @NamedQuery(name = "CfgUnidadFuncional.findByCodCtoCosto", query = "SELECT c FROM CfgUnidadFuncional c WHERE c.codCtoCosto = :codCtoCosto")})
public class CfgUnidadFuncional implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cod_und_funcional", nullable = false, length = 2)
    private String codUndFuncional;
    @Column(name = "nom_und_funcional", length = 100)
    private String nomUndFuncional;
    @Column(name = "cod_cto_costo", length = 3)
    private String codCtoCosto;

    public CfgUnidadFuncional() {
    }

    public CfgUnidadFuncional(String codUndFuncional) {
        this.codUndFuncional = codUndFuncional;
    }

    public String getCodUndFuncional() {
        return codUndFuncional;
    }

    public void setCodUndFuncional(String codUndFuncional) {
        this.codUndFuncional = codUndFuncional;
    }

    public String getNomUndFuncional() {
        return nomUndFuncional;
    }

    public void setNomUndFuncional(String nomUndFuncional) {
        this.nomUndFuncional = nomUndFuncional;
    }

    public String getCodCtoCosto() {
        return codCtoCosto;
    }

    public void setCodCtoCosto(String codCtoCosto) {
        this.codCtoCosto = codCtoCosto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codUndFuncional != null ? codUndFuncional.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgUnidadFuncional)) {
            return false;
        }
        CfgUnidadFuncional other = (CfgUnidadFuncional) object;
        if ((this.codUndFuncional == null && other.codUndFuncional != null) || (this.codUndFuncional != null && !this.codUndFuncional.equals(other.codUndFuncional))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.CfgUnidadFuncional[ codUndFuncional=" + codUndFuncional + " ]";
    }
    
}
