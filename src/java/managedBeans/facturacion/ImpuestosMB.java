/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import modelo.entidades.FacImpuestos;
import modelo.fachadas.FacImpuestosFacade;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.primefaces.context.RequestContext;

/**
 *
 * @author santos
 */
@ManagedBean(name = "impuestosMB")
@SessionScoped
public class ImpuestosMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    FacImpuestosFacade impuestosFacade;
    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------

    private FacImpuestos impuestoSeleccionado;
    private FacImpuestos impuestoSeleccionadoTabla;
    private List<FacImpuestos> listaImpuestos;

    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------
    private String tituloTabImpuestos = "Nuevo Impuesto";
    private Date fechaInicial = new Date();
    private Date fechaFinal = new Date();
    private double valor = 0;
    private String nombre = "";
    Calendar c = Calendar.getInstance();

    //---------------------------------------------------
    //------------- FUNCIONES INICIALES  ----------------
    //---------------------------------------------------      
    @PostConstruct
    public void inicializar() {
        limpiarFormularioImpuestos();
    }

    public ImpuestosMB() {
    }

    //---------------------------------------------------
    //-----------------FUNCIONES IMPUESTOS --------------
    //---------------------------------------------------     
    public void limpiarFormularioImpuestos() {
        tituloTabImpuestos = "Nuevo impuesto";
        impuestoSeleccionado = null;
        fechaInicial.setDate(1);
        fechaInicial.setMonth(0);
        fechaInicial.setYear(c.get(Calendar.YEAR) - 1900);
        fechaFinal.setDate(31);
        fechaFinal.setMonth(11);
        fechaFinal.setYear(c.get(Calendar.YEAR) - 1900);
        valor = Double.parseDouble("0");
        nombre = "IVA";
    }

    public void buscarImpuestos() {
        listaImpuestos = impuestosFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().execute("PF('wvTablaImpuestos').clearFilters(); PF('wvTablaImpuestos').getPaginator().setPage(0); PF('dialogoBuscarImpuestos').show();");
    }

    public void cargarImpuesto() {//click cobre editar caja (carga los datos de la adminstradora)
        if (impuestoSeleccionadoTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún impuesto de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        limpiarFormularioImpuestos();
        impuestoSeleccionado = impuestosFacade.find(impuestoSeleccionadoTabla.getIdImpuesto());
        nombre = impuestoSeleccionado.getNombre();
        fechaInicial = impuestoSeleccionado.getFechaInicial();
        fechaFinal = impuestoSeleccionado.getFechaFinal();
        valor = impuestoSeleccionado.getValor();
        tituloTabImpuestos = "Datos Impuesto: " + nombre;
        RequestContext.getCurrentInstance().execute("PF('dialogoBuscarImpuestos').hide();");
    }

    public void eliminarImpuesto() {
        if (impuestoSeleccionado == null) {
            imprimirMensaje("Error", "No se ha cargado ningún impuesto", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoEliminarImpuesto').show();");
    }

    public void confirmarEliminarImpuesto() {
        if (impuestoSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún impuesto", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            impuestosFacade.remove(impuestoSeleccionado);
            limpiarFormularioImpuestos();
            RequestContext.getCurrentInstance().update("IdFormImpuestos");
            imprimirMensaje("Correcto", "El impuesto ha sido eliminado", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El impuesto que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    private boolean validarRangoImpuestos() {
        //determina si el rango especificado ya se encuentra dentro de otro
        List<FacImpuestos> listaImp = impuestosFacade.buscarPorNombre(nombre);
        boolean estaEnRango = false;
        for (FacImpuestos impuesto : listaImp) {
            if (impuesto.getFechaFinal() != null) {
                if (rangoDentroDeRango(impuesto.getFechaInicial(), impuesto.getFechaFinal(), fechaInicial, fechaFinal)) {
                    return true;
                }
            }
        }
        return estaEnRango;
    }

    public void guardarImpuesto() {
        if (validacionCampoVacio(nombre, "Nombre")) {
            return;
        }
        if (fechaInicial == null || fechaFinal == null) {
            imprimirMensaje("Error", "Fecha Inicial y Final son obigatorias", FacesMessage.SEVERITY_ERROR);
            return;
        }

        if (!fechaFinal.after(fechaInicial)) {
            imprimirMensaje("Error", "Fecha Inicial debe ser inferior a Fecha Final", FacesMessage.SEVERITY_ERROR);
            return;
        }

        if (impuestoSeleccionado == null) {
            if (validarRangoImpuestos()) {
                imprimirMensaje("Error", "El rango de fechas se encuentra contenido en un registro existente", FacesMessage.SEVERITY_ERROR);
                return;
            }
            guardarNuevoImpuesto();
        } else {
            actualizarImpuestoExistente();
        }
    }

    private void guardarNuevoImpuesto() {
        FacImpuestos nuevoImpuesto = new FacImpuestos();
        nuevoImpuesto.setNombre(nombre);
        nuevoImpuesto.setFechaFinal(fechaFinal);
        nuevoImpuesto.setFechaInicial(fechaInicial);
        nuevoImpuesto.setValor(valor);
        impuestosFacade.create(nuevoImpuesto);
        limpiarFormularioImpuestos();
        RequestContext.getCurrentInstance().update("IdFormImpuestos");
        imprimirMensaje("Correcto", "El impuesto ha sido creado.", FacesMessage.SEVERITY_INFO);
    }

    private void actualizarImpuestoExistente() {//realiza la actualizacion del consultorio        

        impuestoSeleccionado.setNombre(nombre);
        impuestoSeleccionado.setFechaFinal(fechaFinal);
        impuestoSeleccionado.setFechaInicial(fechaInicial);
        impuestoSeleccionado.setValor(valor);
        impuestosFacade.edit(impuestoSeleccionado);
        limpiarFormularioImpuestos();
        RequestContext.getCurrentInstance().update("IdFormImpuestos");
        imprimirMensaje("Correcto", "El impuesto ha sido actualizado.", FacesMessage.SEVERITY_INFO);
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------
    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public FacImpuestos getImpuestoSeleccionado() {
        return impuestoSeleccionado;
    }

    public void setImpuestoSeleccionado(FacImpuestos impuestoSeleccionado) {
        this.impuestoSeleccionado = impuestoSeleccionado;
    }

    public FacImpuestos getImpuestoSeleccionadoTabla() {
        return impuestoSeleccionadoTabla;
    }

    public void setImpuestoSeleccionadoTabla(FacImpuestos impuestoSeleccionadoTabla) {
        this.impuestoSeleccionadoTabla = impuestoSeleccionadoTabla;
    }

    public List<FacImpuestos> getListaImpuestos() {
        return listaImpuestos;
    }

    public void setListaImpuestos(List<FacImpuestos> listaImpuestos) {
        this.listaImpuestos = listaImpuestos;
    }

    public String getTituloTabImpuestos() {
        return tituloTabImpuestos;
    }

    public void setTituloTabImpuestos(String tituloTabImpuestos) {
        this.tituloTabImpuestos = tituloTabImpuestos;
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

}
