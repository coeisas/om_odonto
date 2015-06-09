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
import modelo.entidades.FacPeriodo;
import modelo.fachadas.FacPeriodoFacade;
import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;

/**
 *
 * @author santos
 */
@ManagedBean(name = "periodosMB")
@SessionScoped
public class PeriodosMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    FacPeriodoFacade periodoFacade;
    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------

    private FacPeriodo periodoSeleccionado;
    private FacPeriodo periodoSeleccionadoTabla;
    private List<FacPeriodo> listaPeriodos;

    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------
    private String tituloTabPeriodos = "Nuevo Periodo";
    private String anio = "0";
    private String mes = "0";
    private Date fechaInicial = new Date();
    private Date fechaFinal = new Date();
    private Date fechaLimite = new Date();
    private String nombre = "";
    private boolean cerrado = true;
    private List<SelectItem> listaAnios;
    private boolean mostrarTabView=false;

    //---------------------------------------------------
    //------------- FUNCIONES INICIALES  ----------------
    //---------------------------------------------------      
    public PeriodosMB() {
        anio = String.valueOf(fechaInicial.getYear() + 1900);
        mes = String.valueOf(fechaInicial.getMonth());
        listaAnios = new ArrayList<>();
        int a = Integer.parseInt(anio);
        for (int i = a; i > a - 10; i--) {
            listaAnios.add(new SelectItem(String.valueOf(i), String.valueOf(i)));
        }
        cambiaPeriodo();
    }

    //---------------------------------------------------
    //-----------------FUNCIONES PERIODO --------
    //--------------------------------------------------- 
    private String determinarNombre(String a, String m) {//agrega un cero de requerirse al mes
        m = String.valueOf(Integer.parseInt(m) + 1);
        if (m.length() == 1) {
            return a + "0" + m;
        } else {
            return a + m;
        }

    }

    public final void cambiaPeriodo() {//recalcular la fecha inicial, final y limite del periodo
        Date dp = new Date();
        dp.setDate(1);//dia
        dp.setMonth(Integer.parseInt(mes));
        dp.setYear(Integer.parseInt(anio) - 1900);
        DateTime dt = new DateTime(dp);
        fechaInicial = dt.dayOfMonth().withMinimumValue().toDate();
        fechaFinal = dt.dayOfMonth().withMaximumValue().toDate();
        fechaLimite = dt.dayOfMonth().withMaximumValue().toDate();
        nombre = determinarNombre(anio, mes);
    }
    
    public void clickBtnNuevoPeriodo(){
        limpiarFormularioPeriodos();
        mostrarTabView=true;
    }

    public void limpiarFormularioPeriodos() {
        tituloTabPeriodos = "Nuevo periodo";
        periodoSeleccionado = null;
        fechaInicial = new Date();
        fechaFinal = new Date();
        fechaLimite = new Date();
        anio = String.valueOf(fechaInicial.getYear() + 1900);
        mes = String.valueOf(fechaInicial.getMonth());
        nombre = determinarNombre(anio, mes);
        cambiaPeriodo();
        cerrado = true;
    }

    public void buscarPeriodos() {
        listaPeriodos = periodoFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().execute("PF('wvTablaPeriodos').clearFilters(); PF('wvTablaPeriodos').getPaginator().setPage(0); PF('dialogoBuscarPeriodos').show();");
    }

    public void cargarPeriodo() {//click cobre editar caja (carga los datos de la adminstradora)
        if (periodoSeleccionadoTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún periodo de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        mostrarTabView=true;
        limpiarFormularioPeriodos();
        periodoSeleccionado = periodoFacade.find(periodoSeleccionadoTabla.getIdPeriodo());
        anio = periodoSeleccionado.getAnio().toString();
        mes = periodoSeleccionado.getMes().toString();
        nombre = determinarNombre(anio, mes);
        fechaInicial = periodoSeleccionado.getFechaInicial();
        fechaFinal = periodoSeleccionado.getFechaFinal();
        fechaLimite = periodoSeleccionado.getFechaLimite();
        cerrado = periodoSeleccionado.getCerrado();
        tituloTabPeriodos = "Datos Periodo: " + nombre;
        RequestContext.getCurrentInstance().execute("PF('dialogoBuscarPeriodos').hide();");
    }

    public void eliminarPeriodo() {
        if (periodoSeleccionado == null) {
            imprimirMensaje("Error", "No se ha cargado ningún periodo", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoEliminarPeriodo').show();");
    }

    public void confirmarEliminarPeriodo() {
        if (periodoSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún periodo", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            periodoFacade.remove(periodoSeleccionado);
            limpiarFormularioPeriodos();
            mostrarTabView=false;
            RequestContext.getCurrentInstance().update("IdFormPeriodos");
            imprimirMensaje("Correcto", "El periodo ha sido eliminado", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El periodo que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void guardarPeriodo() {
        if (periodoSeleccionado == null) {
            if (periodoFacade.buscarPorNombre(determinarNombre(anio, mes)) != null) {
                imprimirMensaje("Error", "Ya existe un periodo con igual año y mes", FacesMessage.SEVERITY_ERROR);
                return;
            }
            guardarNuevoPeriodo();
        } else {
            actualizarPeriodoExistente();
        }
    }

    private void guardarNuevoPeriodo() {
        FacPeriodo nuevoPeriodo = new FacPeriodo();
        nuevoPeriodo.setAnio(Integer.parseInt(anio));
        nuevoPeriodo.setMes(Integer.parseInt(mes));
        nuevoPeriodo.setNombre(determinarNombre(anio, mes));
        nuevoPeriodo.setFechaFinal(fechaFinal);
        nuevoPeriodo.setFechaInicial(fechaInicial);
        nuevoPeriodo.setFechaLimite(fechaLimite);
        nuevoPeriodo.setCerrado(cerrado);
        periodoFacade.create(nuevoPeriodo);
        limpiarFormularioPeriodos();
        mostrarTabView=false;
        RequestContext.getCurrentInstance().update("IdFormPeriodos");
        imprimirMensaje("Correcto", "El periodo ha sido creado.", FacesMessage.SEVERITY_INFO);
    }

    private void actualizarPeriodoExistente() {//realiza la actualizacion del consultorio        
        periodoSeleccionado.setAnio(Integer.parseInt(anio));
        periodoSeleccionado.setMes(Integer.parseInt(mes));
        periodoSeleccionado.setNombre(determinarNombre(anio, mes));
        periodoSeleccionado.setFechaFinal(fechaFinal);
        periodoSeleccionado.setFechaInicial(fechaInicial);
        periodoSeleccionado.setFechaLimite(fechaLimite);
        periodoSeleccionado.setCerrado(cerrado);
        periodoFacade.edit(periodoSeleccionado);
        limpiarFormularioPeriodos();
        mostrarTabView=false;
        RequestContext.getCurrentInstance().update("IdFormPeriodos");
        imprimirMensaje("Correcto", "El periodo ha sido actualizado.", FacesMessage.SEVERITY_INFO);
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------
    public FacPeriodo getPeriodoSeleccionado() {
        return periodoSeleccionado;
    }

    public void setPeriodoSeleccionado(FacPeriodo periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
    }

    public FacPeriodo getPeriodoSeleccionadoTabla() {
        return periodoSeleccionadoTabla;
    }

    public void setPeriodoSeleccionadoTabla(FacPeriodo periodoSeleccionadoTabla) {
        this.periodoSeleccionadoTabla = periodoSeleccionadoTabla;
    }

    public List<FacPeriodo> getListaPeriodos() {
        return listaPeriodos;
    }

    public void setListaPeriodos(List<FacPeriodo> listaPeriodos) {
        this.listaPeriodos = listaPeriodos;
    }

    public String getTituloTabPeriodos() {
        return tituloTabPeriodos;
    }

    public void setTituloTabPeriodos(String tituloTabPeriodos) {
        this.tituloTabPeriodos = tituloTabPeriodos;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public boolean isCerrado() {
        return cerrado;
    }

    public void setCerrado(boolean cerrado) {
        this.cerrado = cerrado;
    }

    public List<SelectItem> getListaAnios() {
        return listaAnios;
    }

    public void setListaAnios(List<SelectItem> listaAnios) {
        this.listaAnios = listaAnios;
    }

    public boolean isMostrarTabView() {
        return mostrarTabView;
    }

    public void setMostrarTabView(boolean mostrarTabView) {
        this.mostrarTabView = mostrarTabView;
    }

}
