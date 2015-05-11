/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import modelo.entidades.FacConsecutivo;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.FacConsecutivoFacade;
import org.primefaces.context.RequestContext;

/**
 *
 * @author santos
 */
@ManagedBean(name = "consecutivosMB")
@SessionScoped
public class ConsecutivosMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    FacConsecutivoFacade consecutivoFacade;
    @EJB
    CfgClasificacionesFacade clasificacionesFacade;
    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------
    private FacConsecutivo consecutivoSeleccionado;
    private FacConsecutivo consecutivoSeleccionadoTabla;
    private List<FacConsecutivo> listaConsecutivos;

    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------
    private String tituloTabConsecutivos = "Nuevo consecutivo";
    private String tipoDocumento = "";
    private String resolucion = "";
    private int inicio = 1;
    private int fin = 1;
    private int actual = 0;
    private String prefijo="";
    private String texto="";
    private boolean mostrarTabView = false;

    //---------------------------------------------------
    //------------- FUNCIONES INICIALES  ----------------
    //---------------------------------------------------     
    public ConsecutivosMB() {
    }

    //---------------------------------------------------
    //-----------------FUNCIONES CONSECUTIVOS -----------
    //--------------------------------------------------- 
    public void clickBtnNuevoConsecutivo(){
        limpiarFormularioConsecutivos();
        mostrarTabView=true;
    }
    
    public void limpiarFormularioConsecutivos() {
        tituloTabConsecutivos = "Nuevo consecutivo";
        consecutivoSeleccionado = null;
        tipoDocumento = "";
        resolucion = "";
        inicio = 1;
        fin = 1;
        actual = 0;
        prefijo="";
        texto="";
    }

    public void buscarConsecutivos() {
        listaConsecutivos = consecutivoFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().execute("PF('wvTablaConsecutivos').clearFilters(); PF('wvTablaConsecutivos').getPaginator().setPage(0); PF('dialogoBuscarConsecutivos').show();");
    }

    public void cargarConsecutivo() {//click cobre editar caja (carga los datos de la adminstradora)
        if (consecutivoSeleccionadoTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún consecutivo de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        limpiarFormularioConsecutivos();
        mostrarTabView=true;
        consecutivoSeleccionado = consecutivoFacade.find(consecutivoSeleccionadoTabla.getIdConsecutivo());
        tipoDocumento = consecutivoSeleccionado.getTipoDocumento().getId().toString();
        resolucion = consecutivoSeleccionado.getResolucionDian();
        inicio = consecutivoSeleccionado.getInicio();
        fin = consecutivoSeleccionado.getFin();
        actual = consecutivoSeleccionado.getActual();
        prefijo=consecutivoSeleccionado.getPrefijo();
        texto=consecutivoSeleccionado.getTexto();
        tituloTabConsecutivos = "Datos Consecutivo: " + consecutivoSeleccionado.getTipoDocumento().getDescripcion();
        RequestContext.getCurrentInstance().execute("PF('dialogoBuscarConsecutivos').hide();");
    }

    public void eliminarConsecutivo() {
        if (consecutivoSeleccionado == null) {
            imprimirMensaje("Error", "No se ha cargado ningún consecutivo", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if(consecutivoSeleccionado.getActual()!=0){
            imprimirMensaje("Error", "No se puede eliminar, por que se ha hecho uso de este consecutivo (Actual diferente de cero)", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoEliminarConsecutivo').show();");
    }

    public void confirmarEliminarConsecutivo() {
        if (consecutivoSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún consecutivo", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            consecutivoFacade.remove(consecutivoSeleccionado);
            limpiarFormularioConsecutivos();
            mostrarTabView=false;
            RequestContext.getCurrentInstance().update("IdFormConsecutivos");
            imprimirMensaje("Correcto", "El consecutivo ha sido eliminado", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El consecutivo que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void guardarConsecutivo() {
        if (validacionCampoVacio(tipoDocumento, "Tipo documento")) {
            return;
        }
        if (inicio > fin) {//validar que inicio sea menor o igual a fin
            imprimirMensaje("Error", "El Inicio debe ser: inferior o igual a Fin", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (consecutivoSeleccionado == null) {
            //validar que no exista un consecutivo sin terminar
            List<FacConsecutivo> listaCons = consecutivoFacade.buscarPorTipoDocumento(Integer.parseInt(tipoDocumento));
            if (listaCons != null) {
                for (FacConsecutivo c : listaCons) {
                    if (!Objects.equals(c.getFin(), c.getActual())) {
                        imprimirMensaje("Error", "Existe un consecutivo de este tipo que no se ha terminado aún.", FacesMessage.SEVERITY_ERROR);
                        return;
                    }
                }
            }
            guardarNuevoConsecutivo();
        } else {
            if (actual > fin) {//si se esta modificando se valida que 
                imprimirMensaje("Error", "Cuando se actualiza: Fin debe ser mayor o igual a Actual", FacesMessage.SEVERITY_ERROR);
                return;
            }
            actualizarConsecutivoExistente();
        }
    }

    private void guardarNuevoConsecutivo() {
        FacConsecutivo nuevoConsecutivo = new FacConsecutivo();
        nuevoConsecutivo.setTipoDocumento(clasificacionesFacade.find(Integer.parseInt(tipoDocumento)));
        nuevoConsecutivo.setResolucionDian(resolucion);
        nuevoConsecutivo.setInicio(inicio);
        nuevoConsecutivo.setFin(fin);
        nuevoConsecutivo.setFechaSistema(new Date());
        nuevoConsecutivo.setActual(actual);
        nuevoConsecutivo.setPrefijo(prefijo);
        nuevoConsecutivo.setTexto(texto);
        consecutivoFacade.create(nuevoConsecutivo);
        limpiarFormularioConsecutivos();
        mostrarTabView=false;
        RequestContext.getCurrentInstance().update("IdFormConsecutivos");
        imprimirMensaje("Correcto", "El consecutivo ha sido creado.", FacesMessage.SEVERITY_INFO);
    }

    private void actualizarConsecutivoExistente() {//realiza la actualizacion del consultorio
        consecutivoSeleccionado.setTipoDocumento(clasificacionesFacade.find(Integer.parseInt(tipoDocumento)));
        consecutivoSeleccionado.setResolucionDian(resolucion);
        consecutivoSeleccionado.setInicio(inicio);
        consecutivoSeleccionado.setFin(fin);
        consecutivoSeleccionado.setActual(actual);
        consecutivoSeleccionado.setPrefijo(prefijo);
        consecutivoSeleccionado.setTexto(texto);
        consecutivoFacade.edit(consecutivoSeleccionado);
        limpiarFormularioConsecutivos();
        RequestContext.getCurrentInstance().update("IdFormConsecutivos");
        mostrarTabView=false;
        imprimirMensaje("Correcto", "El consecutivo ha sido actualizado.", FacesMessage.SEVERITY_INFO);
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------
    public List<FacConsecutivo> getListaConsecutivos() {
        return listaConsecutivos;
    }

    public void setListaConsecutivos(List<FacConsecutivo> listaConsecutivos) {
        this.listaConsecutivos = listaConsecutivos;
    }

    public String getTituloTabConsecutivos() {
        return tituloTabConsecutivos;
    }

    public void setTituloTabConsecutivos(String tituloTabConsecutivos) {
        this.tituloTabConsecutivos = tituloTabConsecutivos;
    }

    public FacConsecutivo getConsecutivoSeleccionado() {
        return consecutivoSeleccionado;
    }

    public void setConsecutivoSeleccionado(FacConsecutivo consecutivoSeleccionado) {
        this.consecutivoSeleccionado = consecutivoSeleccionado;
    }

    public FacConsecutivo getConsecutivoSeleccionadoTabla() {
        return consecutivoSeleccionadoTabla;
    }

    public void setConsecutivoSeleccionadoTabla(FacConsecutivo consecutivoSeleccionadoTabla) {
        this.consecutivoSeleccionadoTabla = consecutivoSeleccionadoTabla;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getResolucion() {
        return resolucion;
    }

    public void setResolucion(String resolucion) {
        this.resolucion = resolucion;
    }

    public int getInicio() {
        return inicio;
    }

    public void setInicio(int inicio) {
        this.inicio = inicio;
    }

    public int getFin() {
        return fin;
    }

    public void setFin(int fin) {
        this.fin = fin;
    }

    public int getActual() {
        return actual;
    }

    public void setActual(int actual) {
        this.actual = actual;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public boolean isMostrarTabView() {
        return mostrarTabView;
    }

    public void setMostrarTabView(boolean mostrarTabView) {
        this.mostrarTabView = mostrarTabView;
    }    

}
