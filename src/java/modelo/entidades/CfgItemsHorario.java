/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author santos
 */
@Entity
@Table(name = "cfg_items_horario", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgItemsHorario.findAll", query = "SELECT c FROM CfgItemsHorario c"),
    @NamedQuery(name = "CfgItemsHorario.findByIdItemHorario", query = "SELECT c FROM CfgItemsHorario c WHERE c.idItemHorario = :idItemHorario"),
    @NamedQuery(name = "CfgItemsHorario.findByDia", query = "SELECT c FROM CfgItemsHorario c WHERE c.dia = :dia"),
    @NamedQuery(name = "CfgItemsHorario.findByHoraInicio", query = "SELECT c FROM CfgItemsHorario c WHERE c.horaInicio = :horaInicio"),
    @NamedQuery(name = "CfgItemsHorario.findByHoraFinal", query = "SELECT c FROM CfgItemsHorario c WHERE c.horaFinal = :horaFinal"),
    @NamedQuery(name = "CfgItemsHorario.findByNombredia", query = "SELECT c FROM CfgItemsHorario c WHERE c.nombredia = :nombredia")})
public class CfgItemsHorario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_item_horario", nullable = false)
    private Integer idItemHorario;
    @Column(name = "dia")
    private Short dia;
    @Column(name = "hora_inicio")
    @Temporal(TemporalType.TIME)
    private Date horaInicio;
    @Column(name = "hora_final")
    @Temporal(TemporalType.TIME)
    private Date horaFinal;
    @Column(name = "nombredia", length = 20)
    private String nombredia;
    @JoinColumn(name = "id_horario", referencedColumnName = "id_horario")
    @ManyToOne
    private CfgHorario idHorario;

    public CfgItemsHorario() {
    }

    public CfgItemsHorario(Integer idItemHorario) {
        this.idItemHorario = idItemHorario;
    }

    public Integer getIdItemHorario() {
        return idItemHorario;
    }

    public void setIdItemHorario(Integer idItemHorario) {
        this.idItemHorario = idItemHorario;
    }

    public Short getDia() {
        return dia;
    }

    public void setDia(Short dia) {
        this.dia = dia;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(Date horaFinal) {
        this.horaFinal = horaFinal;
    }

    public String getNombredia() {
        return nombredia;
    }

    public void setNombredia(String nombredia) {
        this.nombredia = nombredia;
    }

    public CfgHorario getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(CfgHorario idHorario) {
        this.idHorario = idHorario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idItemHorario != null ? idItemHorario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgItemsHorario)) {
            return false;
        }
        CfgItemsHorario other = (CfgItemsHorario) object;
        if ((this.idItemHorario == null && other.idItemHorario != null) || (this.idItemHorario != null && !this.idItemHorario.equals(other.idItemHorario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.CfgItemsHorario[ idItemHorario=" + idItemHorario + " ]";
    }
    
}
