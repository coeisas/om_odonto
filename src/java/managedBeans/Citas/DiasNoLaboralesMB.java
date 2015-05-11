/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.Citas;

import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import modelo.entidades.CfgDiasNoLaborales;
import modelo.entidades.CfgSede;
import modelo.fachadas.CfgDiasNoLaboralesFacade;
import modelo.fachadas.CfgSedeFacade;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Mario
 */
@ManagedBean(name = "diasNoLaboralesMB")
@SessionScoped
public class DiasNoLaboralesMB extends MetodosGenerales implements Serializable {

    /**
     * Creates a new instance of DiasNoLaboralesMB
     */
    private String display = "none";
    private Date fechaNoLaboral;
    private int idSede;
    private final int todas = 0;//id para todas las sedes
    private List<CfgDiasNoLaborales> listaDiasNoLaborales;

    @EJB
    CfgDiasNoLaboralesFacade cfgDiasNoLaboralesFacade;

    @EJB
    CfgSedeFacade cfgSedeFacade;

    public DiasNoLaboralesMB() {
    }

    public Date getFechaNoLaboral() {
        return fechaNoLaboral;
    }

    //---------------------------------------------------------
    //---------------------METHODS-----------------------------
    //---------------------------------------------------------
    public void validarDiaSeleccionado(SelectEvent selectEvent) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        //imprimirMensaje("Informaciom", "Entro a validar " + format.format(fechaNoLaboral), FacesMessage.SEVERITY_INFO);
        List<Integer> sedes = new ArrayList();
        sedes.add(todas);
        if (idSede == 0) {
            for (CfgSede cfgSede : cfgSedeFacade.findAll()) {
                sedes.add(cfgSede.getIdSede());
            }
        } else {
            sedes.add(idSede);
        }
        if (cfgDiasNoLaboralesFacade.FindDiaNoLaboral(sedes, fechaNoLaboral) != null) {
            setDisplay("none");
            Date aux = fechaNoLaboral;
            setFechaNoLaboral(null);
            RequestContext.getCurrentInstance().update("idFormDiasNoLaborales");
            if (idSede == 0) {
                imprimirMensaje("Error", format.format(aux) + " ya se encuentra adicionada en alguna de las sedes", FacesMessage.SEVERITY_ERROR);
            } else {
                imprimirMensaje("Error", format.format(aux) + " ya se encuentra adicionada, en esta sede o en todas", FacesMessage.SEVERITY_ERROR);
            }
        } else {
            setDisplay("block");
            RequestContext.getCurrentInstance().update("idFormDiasNoLaborales:idButton");
        }

    }

    public void insertarDia() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        setDisplay("none");
        CfgDiasNoLaborales diasNoLaborales = new CfgDiasNoLaborales(idSede, fechaNoLaboral);
        cfgDiasNoLaboralesFacade.create(diasNoLaborales);
        Date aux = fechaNoLaboral;
        cargarDiasNoLaborales();
        imprimirMensaje("Correcto", format.format(aux) + " se ha adicionado", FacesMessage.SEVERITY_INFO);
    }

    public void eliminarDia(ActionEvent actionEvent) {
        int sede = (int) actionEvent.getComponent().getAttributes().get("sede");
        Date fecha = (Date) actionEvent.getComponent().getAttributes().get("fecha");
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        CfgDiasNoLaborales diaNoLaboral = cfgDiasNoLaboralesFacade.FindDiaNoLaboraleBySede(sede, fecha);
        cfgDiasNoLaboralesFacade.remove(diaNoLaboral);
        cargarDiasNoLaborales();
        imprimirMensaje("Correcto", format.format(fecha) + " se ha eliminado", FacesMessage.SEVERITY_INFO);
    }

    public void cargarDiasNoLaborales() {
        setFechaNoLaboral(null);
        setListaDiasNoLaborales(cfgDiasNoLaboralesFacade.diasNoLaboralesBySede(idSede));
        //setFechaNoLaboral(null);
        setDisplay("none");
        RequestContext.getCurrentInstance().update("idFormDiasNoLaborales");
    }
    //---------------------------------------------------------
    //----------------GETTERS AND SETTERS----------------------
    //---------------------------------------------------------

    public void setFechaNoLaboral(Date fechaNoLaboral) {
        this.fechaNoLaboral = fechaNoLaboral;
    }

    public int getIdSede() {
        return idSede;
    }

    public void setIdSede(int idSede) {
        this.idSede = idSede;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public List<CfgDiasNoLaborales> getListaDiasNoLaborales() {
        return listaDiasNoLaborales;
    }

    public void setListaDiasNoLaborales(List<CfgDiasNoLaborales> listaDiasNoLaborales) {
        this.listaDiasNoLaborales = listaDiasNoLaborales;
    }

    public int getTodas() {
        return todas;
    }

}
