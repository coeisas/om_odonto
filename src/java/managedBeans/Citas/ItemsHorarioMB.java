/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.Citas;

import beans.utilidades.ItemsHorario;
import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import modelo.entidades.CfgHorario;
import modelo.entidades.CfgItemsHorario;
import modelo.fachadas.CfgHorarioFacade;
import modelo.fachadas.CfgItemsHorarioFacade;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author mario
 */
@ManagedBean(name = "itemsHorarioMB")
@SessionScoped
public class ItemsHorarioMB extends MetodosGenerales implements Serializable {

    private List<ItemsHorario> horario;
    private Date horaIni;
    private Date horaFin;
    private String nomDia;
    private Short[] selectedDays;
    private String display = "none";
    private List<CfgItemsHorario> listaItemsHorario;
    private CfgHorario horarioseleccionado;
    private int index;
    private boolean renderizar = false;//boton guardar items horario
    private boolean renderizarAddItems = true;//boton guardar items horario
    private boolean tablaEditable;

    @EJB
    private CfgItemsHorarioFacade cfgItemsHorarioFacade;

    /**
     * Creates a new instance of HorarioMB
     */
    public ItemsHorarioMB() {
    }

    @PostConstruct
    public void initial() {
        listaItemsHorario = new ArrayList();
        setIndex(0);
        //System.out.println("HorarioMB");
    }

    public void addItemHorario() {
//        if (horarioseleccionado != null) {

            if (selectedDays.length != 0) {

                if (horaIni != null && horaFin != null && horaFin.after(horaIni)) {
                    for (Short selectedDay : selectedDays) {
                        CfgItemsHorario h = new CfgItemsHorario();
                        h.setIdItemHorario(getIndex());
                        h.setIdHorario(horarioseleccionado);
                        h.setDia(selectedDay);
                        h.setNombredia(asignarNomDia(selectedDay));
                        h.setHoraInicio(horaIni);
                        h.setHoraFinal(horaFin);
                        listaItemsHorario.add(h);
                        setIndex(getIndex() + 1);
                    }
                    //persistirHorario
                    //setListaCfgHorario(cfgHorarioFacade.findCitasByCodCfgTurno(cfgTurnoMB.getCod()));
                    setListaItemsHorario(listaItemsHorario);
                    setRenderizar(true);
                    //cargarListadoHorario();
                }else{
                    imprimirMensaje("Error", "Ingrese la hora final e inicial", FacesMessage.SEVERITY_ERROR);
                }
            } else {
                imprimirMensaje("Error", "Eliga al menos un dia laboral", FacesMessage.SEVERITY_ERROR);
                if (listaItemsHorario.size() > 0) {
                    setRenderizar(true);
                } else {
                    setRenderizar(false);
                }
            }
//        } else {
//            imprimirMensaje("Información", "Es necesario que un horario este creado", FacesMessage.SEVERITY_ERROR);
//            setRenderizar(false);
//        }
    }
    
    public void removeItem(ActionEvent actionEvent){
        int id_item = (int) actionEvent.getComponent().getAttributes().get("id_item");
        listaItemsHorario.remove(id_item);
        
    }

    public void guardarItems() {
        //System.out.println("guardando items");
        if (horarioseleccionado != null) {
            if (listaItemsHorario.size() > 0) {
                for (CfgItemsHorario cfgItemsHorario : listaItemsHorario) {
                    cfgItemsHorario.setIdItemHorario(null);
                    cfgItemsHorario.setIdHorario(horarioseleccionado);
                    cfgItemsHorarioFacade.create(cfgItemsHorario);
                }
                imprimirMensaje("Correcto", "Se ha creado el horario", FacesMessage.SEVERITY_INFO);
                setRenderizarAddItems(false);
                setTablaEditable(false);
                setRenderizar(false);

            } else {
                imprimirMensaje("Error", "Es necesario que especifique el horario de trabajo de almenos un dia", FacesMessage.SEVERITY_ERROR);
            }
        } else {
            imprimirMensaje("Error", "Es necesario que un horario este creado, o cree uno ingresadno su codigo y descripcion", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        System.out.println(listaItemsHorario);

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Car Edited");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void cargarListadoHorario() {
        setListaItemsHorario(cfgItemsHorarioFacade.findHorarioByIdCfgTurno(horarioseleccionado.getIdHorario()));
    }

    private String asignarNomDia(Short dia) {
        String nombreDia = "";
        switch (dia) {
            case 0:
                nombreDia = "Domingo";
                break;
            case 1:
                nombreDia = "Lunes";
                break;
            case 2:
                nombreDia = "Martes";
                break;
            case 3:
                nombreDia = "Miercoles";
                break;
            case 4:
                nombreDia = "Jueves";
                break;
            case 5:
                nombreDia = "Viernes";
                break;
            case 6:
                nombreDia = "Sabado";
                break;
        }
        return nombreDia;
    }

    /**
     * @return the horario
     */
    public List<ItemsHorario> getHorario() {
        return horario;
    }

    /**
     * @param horario the horario to set
     */
    public void setHorario(List<ItemsHorario> horario) {
        this.horario = horario;
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
     * @return the horaIni
     */
    public Date getHoraIni() {
        return horaIni;
    }

    /**
     * @param horaIni the horaIni to set
     */
    public void setHoraIni(Date horaIni) {
        this.horaIni = horaIni;
    }

    /**
     * @return the horaFin
     */
    public Date getHoraFin() {
        return horaFin;
    }

    /**
     * @param horaFin the horaFin to set
     */
    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }

    /**
     * @return the selectedDays
     */
    public Short[] getSelectedDays() {
        return selectedDays;
    }

    /**
     * @param selectedDays the selectedDays to set
     */
    public void setSelectedDays(Short[] selectedDays) {
        this.selectedDays = selectedDays;
    }

    /**
     * @return the listaCfgHorario
     */
    public List<CfgItemsHorario> getListaItemsHorario() {
        return listaItemsHorario;
    }

    /**
     * @param listaCfgHorario the listaCfgHorario to set
     */
    public void setListaItemsHorario(List<CfgItemsHorario> listaCfgHorario) {
        this.listaItemsHorario = listaCfgHorario;
    }

    /**
     * @return the nomDia
     */
    public String getNomDia() {
        return nomDia;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public boolean isRenderizar() {
        return renderizar;
    }

    public void setRenderizar(boolean renderizar) {
        this.renderizar = renderizar;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isRenderizarAddItems() {
        return renderizarAddItems;
    }

    public void setRenderizarAddItems(boolean renderizarAddItems) {
        this.renderizarAddItems = renderizarAddItems;
    }

    public boolean isTablaEditable() {
        return tablaEditable;
    }

    public void setTablaEditable(boolean tablaEditable) {
        this.tablaEditable = tablaEditable;
    }

}
