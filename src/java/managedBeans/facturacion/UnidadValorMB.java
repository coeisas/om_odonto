/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import modelo.entidades.FacUnidadValor;
import modelo.fachadas.FacUnidadValorFacade;
import org.primefaces.context.RequestContext;

/**
 *
 * @author santos
 */
@ManagedBean(name = "unidadValorMB")
@SessionScoped
public class UnidadValorMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    FacUnidadValorFacade unidadValorFacade;
    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------
    private FacUnidadValor unidadValorSeleccionada;
    private FacUnidadValor unidadValorSeleccionadaTabla;
    private List<FacUnidadValor> listaUnidadValor;
    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------
    private String tituloTabUnidadValor = "Nueva unidad de valor";
    private String anio = "0";
    private double smlv = 0;
    private double uvr = 0;
    private final Date fechaActual = new Date();
    private List<SelectItem> listaAnios;

    //---------------------------------------------------
    //------------- FUNCIONES INICIALES  ----------------
    //---------------------------------------------------      

    public UnidadValorMB() {
        anio = String.valueOf(fechaActual.getYear() + 1900);
        listaAnios = new ArrayList<>();
        int a = Integer.parseInt(anio) + 2;//aumenar doa años mas al actual
        for (int i = a; i > a - 5; i--) {
            listaAnios.add(new SelectItem(String.valueOf(i), String.valueOf(i)));
        }
    }

    //---------------------------------------------------
    //-----------------FUNCIONES PERIODO --------
    //--------------------------------------------------- 
    public void limpiarFormularioUnidadValor() {
        tituloTabUnidadValor = "Nueva unidad de valor";
        unidadValorSeleccionada = null;
        anio = String.valueOf(fechaActual.getYear() + 1900);
        uvr = 0;
        smlv = 0;
    }

    public void buscarUnidadValor() {
        listaUnidadValor = unidadValorFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().execute("PF('wvTablaUnidadValor').clearFilters(); PF('wvTablaUnidadValor').getPaginator().setPage(0); PF('dialogoBuscarUnidadValor').show();");
    }

    public void cargarUnidadValor() {//click cobre editar caja (carga los datos de la adminstradora)
        if (unidadValorSeleccionadaTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ninguna unidad de valor de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        limpiarFormularioUnidadValor();
        unidadValorSeleccionada = unidadValorFacade.find(unidadValorSeleccionadaTabla.getAnio());
        anio = unidadValorSeleccionada.getAnio().toString();
        smlv = unidadValorSeleccionada.getSmlvd();
        uvr = unidadValorSeleccionada.getUvr();
        tituloTabUnidadValor = "Datos Unidad de valor: " + anio;
        RequestContext.getCurrentInstance().execute("PF('dialogoBuscarUnidadValor').hide();");
    }

    public void eliminarUnidadValor() {
        if (unidadValorSeleccionada == null) {
            imprimirMensaje("Error", "No se ha cargado ninguna unidad de valor", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoEliminarUnidadValor').show();");
    }

    public void confirmarEliminarUnidadValor() {
        if (unidadValorSeleccionada == null) {
            imprimirMensaje("Error", "No se ha seleccionado ninguna unidad de valor", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            unidadValorFacade.remove(unidadValorSeleccionada);
            limpiarFormularioUnidadValor();
            RequestContext.getCurrentInstance().update("IdFormUnidadValor");
            imprimirMensaje("Correcto", "La unidad de valor ha sido eliminada", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "La unidad de valor que intenta eliminar esta siendo usada por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void guardarUnidadValor() {
        if (unidadValorSeleccionada == null) {
            if (unidadValorFacade.buscarPorAnio(Integer.parseInt(anio)) != null) {
                imprimirMensaje("Error", "Ya existe una unidad de valor para ese año", FacesMessage.SEVERITY_ERROR);
                return;
            }
            guardarNuevoUnidadValor();
        } else {
            actualizarUnidadValorExistente();
        }
    }

    private void guardarNuevoUnidadValor() {
        FacUnidadValor nuevaUnidadValor = new FacUnidadValor();
        nuevaUnidadValor.setAnio(Integer.parseInt(anio));
        nuevaUnidadValor.setUvr(uvr);
        nuevaUnidadValor.setSmlvd(smlv);
        unidadValorFacade.create(nuevaUnidadValor);
        limpiarFormularioUnidadValor();
        RequestContext.getCurrentInstance().update("IdFormUnidadValor");
        imprimirMensaje("Correcto", "El unidadValor ha sido creado.", FacesMessage.SEVERITY_INFO);
    }

    private void actualizarUnidadValorExistente() {//realiza la actualizacion del consultorio        
        unidadValorSeleccionada.setAnio(Integer.parseInt(anio));
        unidadValorSeleccionada.setSmlvd(smlv);
        unidadValorSeleccionada.setUvr(uvr);
        unidadValorFacade.edit(unidadValorSeleccionada);
        limpiarFormularioUnidadValor();
        RequestContext.getCurrentInstance().update("IdFormUnidadValor");
        imprimirMensaje("Correcto", "El unidadValor ha sido actualizado.", FacesMessage.SEVERITY_INFO);
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------
    public FacUnidadValor getUnidadValorSeleccionada() {
        return unidadValorSeleccionada;
    }

    public void setUnidadValorSeleccionada(FacUnidadValor unidadValorSeleccionada) {
        this.unidadValorSeleccionada = unidadValorSeleccionada;
    }

    

    public List<FacUnidadValor> getListaUnidadValor() {
        return listaUnidadValor;
    }

    public void setListaUnidadValor(List<FacUnidadValor> listaUnidadValor) {
        this.listaUnidadValor = listaUnidadValor;
    }

    public String getTituloTabUnidadValor() {
        return tituloTabUnidadValor;
    }

    public void setTituloTabUnidadValor(String tituloTabUnidadValor) {
        this.tituloTabUnidadValor = tituloTabUnidadValor;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public List<SelectItem> getListaAnios() {
        return listaAnios;
    }

    public void setListaAnios(List<SelectItem> listaAnios) {
        this.listaAnios = listaAnios;
    }

    public double getSmlv() {
        return smlv;
    }

    public void setSmlv(double smlv) {
        this.smlv = smlv;
    }

    public double getUvr() {
        return uvr;
    }

    public void setUvr(double uvr) {
        this.uvr = uvr;
    }

    public FacUnidadValor getUnidadValorSeleccionadaTabla() {
        return unidadValorSeleccionadaTabla;
    }

    public void setUnidadValorSeleccionadaTabla(FacUnidadValor unidadValorSeleccionadaTabla) {
        this.unidadValorSeleccionadaTabla = unidadValorSeleccionadaTabla;
    }
    
    

}
