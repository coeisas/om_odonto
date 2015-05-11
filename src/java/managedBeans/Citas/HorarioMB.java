/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.Citas;

import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import modelo.entidades.CfgHorario;
import modelo.fachadas.CfgHorarioFacade;
import org.primefaces.context.RequestContext;

/**
 *
 * @author mario
 */
@ManagedBean(name = "horarioMB")
@SessionScoped
public class HorarioMB extends MetodosGenerales implements Serializable {

    //private Short dia;
    private int id;
    private String cod;
    private String desc;
    private List<CfgHorario> cfgHorarios;
    private List<SelectItem> listahorarios;
    private CfgHorario horarioseleccionado;
    private boolean renderizar = true;//boton guardar horario

    private boolean sololectura = false;

    @EJB
    CfgHorarioFacade cfgHorarioFacade;

    @ManagedProperty(value = "#{itemsHorarioMB}")
    private ItemsHorarioMB itemsHorarioMB;

    /**
     * Creates a new instance of CfgTurno
     */
    public HorarioMB() {
    }

    @PostConstruct
    public void initial() {

        cargarItems();

    }

    private void cargarItems() {
        setListaCfgHorario(cfgHorarioFacade.findAll());
        listahorarios = new ArrayList<>();
        for (CfgHorario cfgHorario : cfgHorarios) {
            listahorarios.add(new SelectItem(cfgHorario.getIdHorario(), cfgHorario.getDescripcion()));
        }
        setListaHorarios(listahorarios);
    }

    private boolean exiteTurno() {//EXISTE TURNO
        boolean ban = false;
        setHorarioseleccionado(cfgHorarioFacade.buscarPorDescripcion(desc));
        if (getHorarioseleccionado() != null) {
            ban = true;
        }
        return ban;
    }

    public void validarNombreHorario(ActionEvent actionEvent) {
        if (!desc.isEmpty()) {
            if (exiteTurno()) {
                imprimirMensaje("Información", "ya existe un horario con ese nombre", FacesMessage.SEVERITY_ERROR);
                RequestContext.getCurrentInstance().update("formcfghorario");
            } else {
                setRenderizar(false);
                itemsHorarioMB.setListaItemsHorario(new ArrayList());
                itemsHorarioMB.setRenderizarAddItems(true);
                itemsHorarioMB.setTablaEditable(true);
                itemsHorarioMB.setDisplay("block");
                itemsHorarioMB.setIndex(0);
                RequestContext.getCurrentInstance().update("formcfghorario");                
            }
        } else {
            imprimirMensaje("Información", "Es necesario Ingresar el nombre del horario", FacesMessage.SEVERITY_ERROR);
            itemsHorarioMB.setDisplay("none");
        }
    }

    //crea un horario de trabajo
    public void guardarHorario() {
            CfgHorario cfgHorario = new CfgHorario();
            cfgHorario.setDescripcion(desc);
            cfgHorarioFacade.create(cfgHorario);
            horarioseleccionado = cfgHorario;
            itemsHorarioMB.setHorarioseleccionado(horarioseleccionado);
    }

    //para eliminar
    public void validarCodigo() {
        //System.out.println("VALIDANDO " + cod);
        if (exiteTurno()) {
            setDesc(getHorarioseleccionado().getDescripcion());
            itemsHorarioMB.setHorarioseleccionado(horarioseleccionado);
            itemsHorarioMB.cargarListadoHorario();
        } else {
            //imprimirMensaje("Error", "No se encontro el horario con  codigo " + cod, FacesMessage.SEVERITY_ERROR);
            setDesc(null);
            itemsHorarioMB.setListaItemsHorario(null);
        }

    }

    public void validarHorario(CfgHorario horario) {
        if (horario != null) {
            setDesc(horario.getDescripcion());
            itemsHorarioMB.setHorarioseleccionado(horario);
            itemsHorarioMB.cargarListadoHorario();
            itemsHorarioMB.setDisplay("none");
            setRenderizar(false);
        } else {
            itemsHorarioMB.setDisplay("none");
            setDesc(null);
            setRenderizar(true);
            itemsHorarioMB.setListaItemsHorario(null);
        }
    }

    /**
     * @return the cod
     */
    public String getCod() {
        return cod;
    }

    /**
     * @param cod the cod to set
     */
    public void setCod(String cod) {
        this.cod = cod;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return the sololectura
     */
    public boolean isSololectura() {
        return sololectura;
    }

    /**
     * @param sololectura the sololectura to set
     */
    public void setSololectura(boolean sololectura) {
        this.sololectura = sololectura;
    }

    /**
     * @return the listaCfgHorario
     */
    public List<CfgHorario> getListaCfgHorario() {
        return cfgHorarios;
    }

    /**
     * @param listaCfgHorario the listaCfgHorario to set
     */
    public void setListaCfgHorario(List<CfgHorario> listaCfgHorario) {
        this.cfgHorarios = listaCfgHorario;
    }

    /**
     * @return the listahorarios
     */
    public List<SelectItem> getListaHorarios() {
        return listahorarios;
    }

    /**
     * @param listaHorario the listahorarios to set
     */
    public void setListaHorarios(List<SelectItem> listaHorario) {
        this.listahorarios = listaHorario;
    }

    /**
     * @return the horarioseleccionado
     */
    public CfgHorario getHorarioseleccionado() {
        return horarioseleccionado;
    }

    /**
     * @param horarioseleccionado the horarioseleccionado to set
     */
    public void setHorarioseleccionado(CfgHorario horarioseleccionado) {
        this.horarioseleccionado = horarioseleccionado;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the itemsHorarioMB
     */
    public ItemsHorarioMB getItemsHorarioMB() {
        return itemsHorarioMB;
    }

    /**
     * @param itemsHorarioMB the itemsHorarioMB to set
     */
    public void setItemsHorarioMB(ItemsHorarioMB itemsHorarioMB) {
        this.itemsHorarioMB = itemsHorarioMB;
    }

    public boolean isRenderizar() {
        return renderizar;
    }

    public void setRenderizar(boolean renderizar) {
        this.renderizar = renderizar;
    }

}
