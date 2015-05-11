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
import modelo.entidades.CfgMedicamento;
import modelo.fachadas.CfgMedicamentoFacade;
import org.primefaces.context.RequestContext;

/**
 *
 * @author santos
 */
@ManagedBean(name = "medicamentosMB")
@SessionScoped
public class MedicamentosMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    CfgMedicamentoFacade medicamentoFacade;
    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------

    private CfgMedicamento medicamentoSeleccionado;
    private CfgMedicamento medicamentoSeleccionadoTabla;
    private List<CfgMedicamento> listaMedicamentos;

    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------
    private String tituloTabMedicamentos = "Nuevo Medicamento";
    private String codigoMedicamento = "";   //character varying(20)
    private String codigoCums = "";          //character varying(20), -- CODIGO CUMS
    private String codigoCups = "";          //character varying(20), -- CODIGO CUPS    
    private String nombreMedicamento = "";   //character varying(150),
    private String nombreGenerico = "";      //character varying(150),
    private String nombreComercial = "";     //character varying(150),
    private String formaMedicamento = "";    //character varying(150),
    private boolean pos = true;              //boolean, pos, no pos
    private String concentracion = "";       //character varying(100),
    private String unidadMedida = "";        //character varying(20),
    private boolean controlMedico = true;    //boolean,
    private String registroSanitario = "";   // character varying(50),
    private String modAdmin = "";            //character varying(50),
    private double valor = 0;

    //---------------------------------------------------
    //------------- FUNCIONES INICIALES  ----------------
    //---------------------------------------------------      
    public MedicamentosMB() {
    }

    //---------------------------------------------------
    //-----------------FUNCIONES PROGRAMAS --------
    //--------------------------------------------------- 
    public void limpiarFormularioMedicamentos() {
        tituloTabMedicamentos = "Nuevo medicamento";
        medicamentoSeleccionado=null;
        codigoMedicamento = "";
        codigoCums = "";          //character varying(20), -- CODIGO CUMS
        codigoCups = "";          //character varying(20), -- CODIGO CUPS
        nombreMedicamento = "";   //character varying(150),
        nombreGenerico = "";      //character varying(150),
        nombreComercial = "";     //character varying(150),
        formaMedicamento = "";    //character varying(150),
        pos = true;                 //boolean,
        concentracion = "";       //character varying(100),
        unidadMedida = "";        //character varying(20),
        controlMedico = true;       //boolean,
        registroSanitario = "";   // character varying(50),
        modAdmin = "";            //character varying(50),
        valor = 0;
    }

    public void buscarMedicamentos() {
        listaMedicamentos = medicamentoFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().execute("PF('wvTablaMedicamentos').clearFilters(); PF('wvTablaMedicamentos').getPaginator().setPage(0); PF('dialogoBuscarMedicamentos').show();");
    }

    public void cargarMedicamento() {//click cobre editar caja (carga los datos de la adminstradora)
        if (medicamentoSeleccionadoTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún medicamento de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        limpiarFormularioMedicamentos();
        medicamentoSeleccionado = medicamentoFacade.find(medicamentoSeleccionadoTabla.getIdMedicamento());
        codigoMedicamento = medicamentoSeleccionado.getCodigoMedicamento();
        codigoCums = medicamentoSeleccionado.getCodigoCums();
        codigoCups = medicamentoSeleccionado.getCodigoCups();
        nombreMedicamento = medicamentoSeleccionado.getNombreMedicamento();
        nombreGenerico = medicamentoSeleccionado.getNombreGenerico();
        nombreComercial = medicamentoSeleccionado.getNombreComercial();
        formaMedicamento = medicamentoSeleccionado.getFormaMedicamento();
        pos = medicamentoSeleccionado.getPos();
        concentracion = medicamentoSeleccionado.getConcentracion();
        unidadMedida = medicamentoSeleccionado.getUnidadMedida();
        controlMedico = medicamentoSeleccionado.getControlMedico();
        registroSanitario = medicamentoSeleccionado.getRegistroSanitario();
        modAdmin = medicamentoSeleccionado.getModAdmin();
        if (medicamentoSeleccionado.getValor() != null) {
            valor = medicamentoSeleccionado.getValor();
        }
        tituloTabMedicamentos = "Datos Medicamento: " + nombreMedicamento;
        RequestContext.getCurrentInstance().execute("PF('dialogoBuscarMedicamentos').hide();");
    }

    public void eliminarMedicamento() {
        if (medicamentoSeleccionado == null) {
            imprimirMensaje("Error", "No se ha cargado ningún medicamento", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoEliminarMedicamento').show();");
    }

    public void confirmarEliminarMedicamento() {
        if (medicamentoSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún medicamento", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            medicamentoFacade.remove(medicamentoSeleccionado);
            limpiarFormularioMedicamentos();
            RequestContext.getCurrentInstance().update("IdFormMedicamentos");
            imprimirMensaje("Correcto", "El medicamento ha sido eliminado", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El medicamento que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void guardarMedicamento() {
        if (validacionCampoVacio(codigoMedicamento, "Código medicamento")) {
            return;
        }
        if (validacionCampoVacio(nombreMedicamento, "Nombre medicamento")) {
            return;
        }
        if (medicamentoSeleccionado == null) {
            guardarNuevoMedicamento();
        } else {
            actualizarMedicamentoExistente();
        }
    }

    private void guardarNuevoMedicamento() {
        CfgMedicamento nuevoMedicamento = new CfgMedicamento();
        nuevoMedicamento.setCodigoMedicamento(codigoMedicamento);
        nuevoMedicamento.setCodigoCums(codigoCums);
        nuevoMedicamento.setCodigoCups(codigoCups);
        nuevoMedicamento.setNombreMedicamento(nombreMedicamento);
        nuevoMedicamento.setNombreGenerico(nombreGenerico);
        nuevoMedicamento.setNombreComercial(nombreComercial);
        nuevoMedicamento.setFormaMedicamento(formaMedicamento);
        nuevoMedicamento.setPos(pos);
        nuevoMedicamento.setConcentracion(concentracion);
        nuevoMedicamento.setUnidadMedida(unidadMedida);
        nuevoMedicamento.setControlMedico(controlMedico);
        nuevoMedicamento.setRegistroSanitario(registroSanitario);
        nuevoMedicamento.setModAdmin(modAdmin);
        nuevoMedicamento.setValor(valor);
        medicamentoFacade.create(nuevoMedicamento);
        limpiarFormularioMedicamentos();
        RequestContext.getCurrentInstance().update("IdFormMedicamentos");
        imprimirMensaje("Correcto", "El medicamento ha sido creado.", FacesMessage.SEVERITY_INFO);
    }

    private void actualizarMedicamentoExistente() {//realiza la actualizacion del consultorio        
        medicamentoSeleccionado.setCodigoMedicamento(codigoMedicamento);
        medicamentoSeleccionado.setCodigoCums(codigoCums);
        medicamentoSeleccionado.setCodigoCups(codigoCups);
        medicamentoSeleccionado.setNombreMedicamento(nombreMedicamento);
        medicamentoSeleccionado.setNombreGenerico(nombreGenerico);
        medicamentoSeleccionado.setNombreComercial(nombreComercial);
        medicamentoSeleccionado.setFormaMedicamento(formaMedicamento);
        medicamentoSeleccionado.setPos(pos);
        medicamentoSeleccionado.setConcentracion(concentracion);
        medicamentoSeleccionado.setUnidadMedida(unidadMedida);
        medicamentoSeleccionado.setControlMedico(controlMedico);
        medicamentoSeleccionado.setRegistroSanitario(registroSanitario);
        medicamentoSeleccionado.setModAdmin(modAdmin);
        medicamentoSeleccionado.setValor(valor);
        medicamentoFacade.edit(medicamentoSeleccionado);
        limpiarFormularioMedicamentos();
        RequestContext.getCurrentInstance().update("IdFormMedicamentos");
        imprimirMensaje("Correcto", "El medicamento ha sido actualizado.", FacesMessage.SEVERITY_INFO);
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------
    public CfgMedicamento getMedicamentoSeleccionado() {
        return medicamentoSeleccionado;
    }

    public void setMedicamentoSeleccionado(CfgMedicamento medicamentoSeleccionado) {
        this.medicamentoSeleccionado = medicamentoSeleccionado;
    }

    public CfgMedicamento getMedicamentoSeleccionadoTabla() {
        return medicamentoSeleccionadoTabla;
    }

    public void setMedicamentoSeleccionadoTabla(CfgMedicamento medicamentoSeleccionadoTabla) {
        this.medicamentoSeleccionadoTabla = medicamentoSeleccionadoTabla;
    }

    public List<CfgMedicamento> getListaMedicamentos() {
        return listaMedicamentos;
    }

    public void setListaMedicamentos(List<CfgMedicamento> listaMedicamentos) {
        this.listaMedicamentos = listaMedicamentos;
    }

    public String getTituloTabMedicamentos() {
        return tituloTabMedicamentos;
    }

    public void setTituloTabMedicamentos(String tituloTabMedicamentos) {
        this.tituloTabMedicamentos = tituloTabMedicamentos;
    }

    public String getCodigoMedicamento() {
        return codigoMedicamento;
    }

    public void setCodigoMedicamento(String codigoMedicamento) {
        this.codigoMedicamento = codigoMedicamento;
    }

    public String getCodigoCums() {
        return codigoCums;
    }

    public void setCodigoCums(String codigoCums) {
        this.codigoCums = codigoCums;
    }

    public String getCodigoCups() {
        return codigoCups;
    }

    public void setCodigoCups(String codigoCups) {
        this.codigoCups = codigoCups;
    }

    public String getNombreMedicamento() {
        return nombreMedicamento;
    }

    public void setNombreMedicamento(String nombreMedicamento) {
        this.nombreMedicamento = nombreMedicamento;
    }

    public String getNombreGenerico() {
        return nombreGenerico;
    }

    public void setNombreGenerico(String nombreGenerico) {
        this.nombreGenerico = nombreGenerico;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getFormaMedicamento() {
        return formaMedicamento;
    }

    public void setFormaMedicamento(String formaMedicamento) {
        this.formaMedicamento = formaMedicamento;
    }

    public boolean isPos() {
        return pos;
    }

    public void setPos(boolean pos) {
        this.pos = pos;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(String concentracion) {
        this.concentracion = concentracion;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public boolean isControlMedico() {
        return controlMedico;
    }

    public void setControlMedico(boolean controlMedico) {
        this.controlMedico = controlMedico;
    }

    public String getRegistroSanitario() {
        return registroSanitario;
    }

    public void setRegistroSanitario(String registroSanitario) {
        this.registroSanitario = registroSanitario;
    }

    public String getModAdmin() {
        return modAdmin;
    }

    public void setModAdmin(String modAdmin) {
        this.modAdmin = modAdmin;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

}
