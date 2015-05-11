/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "cfg_horario", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgHorario.findAll", query = "SELECT c FROM CfgHorario c"),
    @NamedQuery(name = "CfgHorario.findByIdHorario", query = "SELECT c FROM CfgHorario c WHERE c.idHorario = :idHorario"),
    @NamedQuery(name = "CfgHorario.findByCodigo", query = "SELECT c FROM CfgHorario c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "CfgHorario.findByDescripcion", query = "SELECT c FROM CfgHorario c WHERE c.descripcion = :descripcion")})
public class CfgHorario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_horario", nullable = false)
    private Integer idHorario;
    @Column(name = "codigo", length = 5)
    private String codigo;
    @Column(name = "descripcion", length = 150)
    private String descripcion;
    @OneToMany(mappedBy = "idHorario")
    private List<CitTurnos> citTurnosList;
    @OneToMany(mappedBy = "idHorario")
    private List<CfgItemsHorario> cfgItemsHorarioList;

    public CfgHorario() {
    }

    public CfgHorario(Integer idHorario) {
        this.idHorario = idHorario;
    }

    public Integer getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Integer idHorario) {
        this.idHorario = idHorario;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<CitTurnos> getCitTurnosList() {
        return citTurnosList;
    }

    public void setCitTurnosList(List<CitTurnos> citTurnosList) {
        this.citTurnosList = citTurnosList;
    }

    @XmlTransient
    public List<CfgItemsHorario> getCfgItemsHorarioList() {
        return cfgItemsHorarioList;
    }

    public void setCfgItemsHorarioList(List<CfgItemsHorario> cfgItemsHorarioList) {
        this.cfgItemsHorarioList = cfgItemsHorarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHorario != null ? idHorario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgHorario)) {
            return false;
        }
        CfgHorario other = (CfgHorario) object;
        if ((this.idHorario == null && other.idHorario != null) || (this.idHorario != null && !this.idHorario.equals(other.idHorario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.CfgHorario[ idHorario=" + idHorario + " ]";
    }
    
}
