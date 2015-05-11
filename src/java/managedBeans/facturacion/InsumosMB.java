/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import modelo.entidades.CfgInsumo;
import modelo.fachadas.CfgInsumoFacade;
import org.primefaces.context.RequestContext;

/**
 *
 * @author santos
 */
@ManagedBean(name = "insumosMB")
@SessionScoped
public class InsumosMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    CfgInsumoFacade insumoFacade;
    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------

    private CfgInsumo insumoSeleccionado;
    private CfgInsumo insumoSeleccionadoTabla;
    private List<CfgInsumo> listaInsumos;

    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------
    private String tituloTabInsumos = "Nuevo Insumo";
    private String codigoInsumo = "";
    private String nombreInsumo = "";
    private String observacion = "";
    private double valor = 0;

    //---------------------------------------------------
    //------------- FUNCIONES INICIALES  ----------------
    //---------------------------------------------------      
    public InsumosMB() {
    }

    //---------------------------------------------------
    //-----------------FUNCIONES PROGRAMAS --------------
    //--------------------------------------------------- 
    public void limpiarFormularioInsumos() {
        tituloTabInsumos = "Nuevo insumo";
        insumoSeleccionado=null;
        codigoInsumo = "";
        nombreInsumo = "";
        observacion = "";
        valor = 0;
    }

    public void buscarInsumos() {
        listaInsumos = insumoFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().execute("PF('wvTablaInsumos').clearFilters(); PF('wvTablaInsumos').getPaginator().setPage(0); PF('dialogoBuscarInsumos').show();");
    }

    public void cargarInsumo() {//click cobre editar caja (carga los datos de la adminstradora)
        if (insumoSeleccionadoTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún insumo de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        limpiarFormularioInsumos();
        insumoSeleccionado = insumoFacade.find(insumoSeleccionadoTabla.getIdInsumo());
        codigoInsumo = insumoSeleccionado.getCodigoInsumo();
        nombreInsumo = insumoSeleccionado.getNombreInsumo();
        observacion = insumoSeleccionado.getObservacion();
        valor = insumoSeleccionado.getValor();
        tituloTabInsumos = "Datos Insumo: " + nombreInsumo;
        RequestContext.getCurrentInstance().execute("PF('dialogoBuscarInsumos').hide();");

    }

    public void eliminarInsumo() {
        if (insumoSeleccionado == null) {
            imprimirMensaje("Error", "No se ha cargado ningún insumo", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoEliminarInsumo').show();");
    }

    public void confirmarEliminarInsumo() {
        if (insumoSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún insumo", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            insumoFacade.remove(insumoSeleccionado);
            limpiarFormularioInsumos();
            RequestContext.getCurrentInstance().update("IdFormInsumos");
            imprimirMensaje("Correcto", "El insumo ha sido eliminado", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El insumo que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void guardarInsumo() {
        if (validacionCampoVacio(codigoInsumo, "Código insumo")) {
            return;
        }
        if (validacionCampoVacio(nombreInsumo, "Nombre insumo")) {
            return;
        }
        if (insumoSeleccionado == null) {
            guardarNuevoInsumo();
        } else {
            actualizarInsumoExistente();
        }
    }

    private void guardarNuevoInsumo() {
        CfgInsumo nuevoInsumo = new CfgInsumo();
        nuevoInsumo.setCodigoInsumo(codigoInsumo);
        nuevoInsumo.setNombreInsumo(nombreInsumo);
        nuevoInsumo.setObservacion(observacion);
        nuevoInsumo.setValor(valor);
        insumoFacade.create(nuevoInsumo);
        limpiarFormularioInsumos();
        RequestContext.getCurrentInstance().update("IdFormInsumos");
        imprimirMensaje("Correcto", "El insumo ha sido creado.", FacesMessage.SEVERITY_INFO);
    }

    private void actualizarInsumoExistente() {//realiza la actualizacion del consultorio        
        insumoSeleccionado.setCodigoInsumo(codigoInsumo);
        insumoSeleccionado.setNombreInsumo(nombreInsumo);
        insumoSeleccionado.setObservacion(observacion);
        insumoSeleccionado.setValor(valor);
        insumoFacade.edit(insumoSeleccionado);
        limpiarFormularioInsumos();
        RequestContext.getCurrentInstance().update("IdFormInsumos");
        imprimirMensaje("Correcto", "El insumo ha sido actualizado.", FacesMessage.SEVERITY_INFO);
    }    

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------

    public CfgInsumo getInsumoSeleccionado() {
        return insumoSeleccionado;
    }

    public void setInsumoSeleccionado(CfgInsumo insumoSeleccionado) {
        this.insumoSeleccionado = insumoSeleccionado;
    }

    public CfgInsumo getInsumoSeleccionadoTabla() {
        return insumoSeleccionadoTabla;
    }

    public void setInsumoSeleccionadoTabla(CfgInsumo insumoSeleccionadoTabla) {
        this.insumoSeleccionadoTabla = insumoSeleccionadoTabla;
    }

    public List<CfgInsumo> getListaInsumos() {
        return listaInsumos;
    }

    public void setListaInsumos(List<CfgInsumo> listaInsumos) {
        this.listaInsumos = listaInsumos;
    }

    public String getTituloTabInsumos() {
        return tituloTabInsumos;
    }

    public void setTituloTabInsumos(String tituloTabInsumos) {
        this.tituloTabInsumos = tituloTabInsumos;
    }

    public String getCodigoInsumo() {
        return codigoInsumo;
    }

    public void setCodigoInsumo(String codigoInsumo) {
        this.codigoInsumo = codigoInsumo;
    }

    public String getNombreInsumo() {
        return nombreInsumo;
    }

    public void setNombreInsumo(String nombreInsumo) {
        this.nombreInsumo = nombreInsumo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
    
}
