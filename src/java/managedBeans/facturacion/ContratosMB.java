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
import modelo.entidades.FacAdministradora;
import modelo.entidades.FacContrato;
import modelo.entidades.FacManualTarifario;
import modelo.entidades.FacManualTarifarioInsumo;
import modelo.entidades.FacManualTarifarioInsumoPK;
import modelo.entidades.FacManualTarifarioMedicamentoPK;
import modelo.entidades.FacManualTarifarioPaquetePK;
import modelo.entidades.FacManualTarifarioServicioPK;
import modelo.entidades.FacManualTarifarioMedicamento;
import modelo.entidades.FacManualTarifarioPaquete;
import modelo.entidades.FacManualTarifarioServicio;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.FacAdministradoraFacade;
import modelo.fachadas.FacContratoFacade;
import modelo.fachadas.FacManualTarifarioFacade;
import modelo.fachadas.FacManualTarifarioInsumoFacade;
import modelo.fachadas.FacManualTarifarioMedicamentoFacade;
import modelo.fachadas.FacManualTarifarioPaqueteFacade;
import modelo.fachadas.FacManualTarifarioServicioFacade;
import org.primefaces.context.RequestContext;

/**
 *
 * @author santos
 */
@ManagedBean(name = "contratosMB")
@SessionScoped
public class ContratosMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    FacContratoFacade contratoFacade;
    @EJB
    CfgClasificacionesFacade clasificacionesFachada;
    @EJB
    FacAdministradoraFacade administradoraFacade;
    @EJB
    FacManualTarifarioFacade manualTarifarioFacade;
    @EJB
    FacManualTarifarioServicioFacade manualTarifarioServicioFacade;
    @EJB
    FacManualTarifarioInsumoFacade manualTarifarioInsumoFacade;
    @EJB
    FacManualTarifarioMedicamentoFacade manualTarifarioMedicamentoFacade;
    @EJB
    FacManualTarifarioPaqueteFacade manualTarifarioPaqueteFacade;
    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------
    private FacContrato contratoSeleccionado;
    private FacContrato contratoSeleccionadoTabla;
    private List<FacContrato> listaContratos;

    //private FacPrograma programaSeleccionado; //programa seleccionado de los asociados a un contrato
    private List<FacContrato> listaContratosCopia;//lista de programas asociados a un contrato cargado
    private FacContrato contratoCopiaSeleccionado = null;//contrato seleccionado cuando se quiere copiar
    private FacAdministradora administradoraCopiaSeleccionada = null;//programa seleccionado cuando se quiere copiar programas    
    private FacManualTarifario manualCopiaSeleccionado = null;//lista de programas de una contrato cuando se quiere copiar programas
    private List<FacManualTarifario> listaManuales;
    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------

    private String idAdministradoraACopiar = "";//identificador de la administradora de donde se copiara el manual
    private String idContratoACopiar = "";//identificador del contrato del cual se copiara el manual
    private String nombreManualContratoACopiar = "";//nombre del manual tarifario del contrato seleccionado
    private String nombreCopiaManual = "";//nombre del manual tarifario
    private String tituloTabContratos = "Nuevo Contrato";

    private boolean cargandoDesdeTab = false;
    private List<SelectItem> listaTipoFacturacion = new ArrayList<>();

    private String codigo = "";
    private String descripcionContrato = "";
    private String administradora = "";
    private Date fechaInicio = null;
    private Date fechaVencimiento = null;
    private String numeroRipContrato = "";
    private String numeroPoliza = "";
    private int numeroAfiliados = 0;
    private String tipoContrato = "";
    private String tipoPago = "";
    private double valorMensual = 0;
    private double valorTotal = 0;
    private double valorValidacionMensual = 0;
    private double valorAlarma = 0;
    private String tarifaPos = "";
    private String tarifaNoPos = "";
    private String tipoFacturacion = "";
    private boolean exigirRipsFacturar = false;
    private String observacionesContrato = "";
    private String observacionesFacturacion = "";
    private String cuentaPorCobrar = "";
    private String cuentaCopago = "";
    private String codigoConcepto = "";
    private String cuentaConceptoDescuento = "";
    //----------SACADO DE PROGRAMAS----------------
    private double cm1 = 0;
    private double cm2 = 0;
    private double cm3 = 0;
    private double cm4 = 0;
    private double cm5 = 0;
    private boolean cmc = false;
    private boolean cmb = false;
    private double cp1 = 0;
    private double cp2 = 0;
    private double cp3 = 0;
    private double cp4 = 0;
    private double cp5 = 0;
    private boolean cpc = false;
    private boolean cpb = false;
    private double medicamentoValor1 = 0;
    private double medicamentoValor2 = 0;
    private double medicamentoValor3 = 0;
    private double insumosPorcentaje1 = 0;
    private double insumosPorcentaje2 = 0;
    private double insumosPorcentaje3 = 0;
    private String idManualTarifario = "";
    private boolean iva = false;
    private boolean cree = false;

    //---------------------------------------------------
    //------------- FUNCIONES INICIALES  ----------------
    //---------------------------------------------------      
    public void inicializar() {
        if (contratoSeleccionado != null) {
            contratoSeleccionadoTabla = contratoSeleccionado;
            cargarContrato();
        } else {
            limpiarFormularioContratos();
        }
    }

    public ContratosMB() {
    }

    //---------------------------------------------------
    //-----------------FUNCIONES CONTRATOS --------------
    //--------------------------------------------------- 
    public void btnNuevoContrato() {
        contratoSeleccionado = null;
        limpiarFormularioContratos();
    }

    public void limpiarFormularioContratos() {
        //listaProgramas = new ArrayList<>();
        //programaSeleccionado = null;
        idContratoACopiar = "";
        listaContratos = contratoFacade.buscarOrdenado();
        listaManuales = manualTarifarioFacade.buscarNoAsociados();
        if (listaManuales == null) {//agregar manual 
            listaManuales = new ArrayList<>();
        }
        if (contratoSeleccionado != null && contratoSeleccionado.getIdManualTarifario() != null) {
            listaManuales.add(contratoSeleccionado.getIdManualTarifario());
        }
        tituloTabContratos = "Nuevo Contrato";
        //tituloTabProgramas = "Programas (0)";
        contratoSeleccionado = null;
        codigo = "";
        descripcionContrato = "";
        administradora = "";
        fechaInicio = null;
        fechaVencimiento = null;
        numeroRipContrato = "";
        numeroPoliza = "";
        numeroAfiliados = 0;
        tipoContrato = "";
        tipoPago = "";
        valorMensual = 0;
        valorTotal = 0;
        valorValidacionMensual = 0;
        valorAlarma = 0;
        tarifaPos = "";
        tarifaNoPos = "";
        tipoFacturacion = "";
        listaTipoFacturacion = new ArrayList<>();
        exigirRipsFacturar = false;
        observacionesContrato = "";
        observacionesFacturacion = "";
        cuentaPorCobrar = "";
        cuentaCopago = "";
        codigoConcepto = "";
        cuentaConceptoDescuento = "";
        //----------SACADO DE PROGRAMAS----------------
        cm1 = 0;
        cm2 = 0;
        cm3 = 0;
        cm4 = 0;
        cm5 = 0;
        cmc = false;
        cmb = false;
        cp1 = 0;
        cp2 = 0;
        cp3 = 0;
        cp4 = 0;
        cp5 = 0;
        cpc = false;
        cpb = false;
        medicamentoValor1 = 0;
        medicamentoValor2 = 0;
        medicamentoValor3 = 0;
        insumosPorcentaje1 = 0;
        insumosPorcentaje2 = 0;
        insumosPorcentaje3 = 0;
        idManualTarifario = "";

        iva = false;
        cree = false;
    }

    public void recargarSiHaySeleccionado() {
        //se usa esta funcion cuando se crea un manua tarifario y al volver a contratos esten cargados los datos
        //if (contratoSeleccionado != null) {
        listaManuales = manualTarifarioFacade.buscarNoAsociados();
        //}
    }

    public void buscarContratos() {
        listaContratos = contratoFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().execute("PF('wvTablaContratos').clearFilters(); PF('wvTablaContratos').getPaginator().setPage(0); PF('dialogoBuscarContratos').show();");
    }

    public void cargarDesdeTab(String id) {//abrir tab contratos desde tab externo (Administradoras)
        cargandoDesdeTab = true;
        String[] splitId = id.split(";");
        if (splitId[0].compareTo("idAdministradora") == 0) {//nuevo contrato por que se recibe idAdministradora
            limpiarFormularioContratos();
            administradora = administradoraFacade.find(Integer.parseInt(splitId[1])).getIdAdministradora().toString();
        } else {//editar contrato existente por que se recibe IdContrato
            contratoSeleccionadoTabla = contratoFacade.find(Integer.parseInt(splitId[1]));
            cargarContrato();
        }
        RequestContext.getCurrentInstance().update("IdFormContratos");
        cargandoDesdeTab = false;
    }

    public void cargarContrato() {//click cobre editar caja (carga los datos de la adminstradora)
        if (contratoSeleccionadoTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún contrato de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        contratoSeleccionado = contratoSeleccionadoTabla;
        limpiarFormularioContratos();
        contratoSeleccionado = contratoFacade.find(contratoSeleccionadoTabla.getIdContrato());
        codigo = contratoSeleccionado.getCodigoContrato();
        descripcionContrato = contratoSeleccionado.getDescripcion();
        if (contratoSeleccionado.getIdAdministradora() != null) {
            administradora = contratoSeleccionado.getIdAdministradora().getIdAdministradora().toString();
        }
        if (contratoSeleccionado.getFechaInicio() != null) {
            fechaInicio = contratoSeleccionado.getFechaInicio();
        }
        if (contratoSeleccionado.getFechaFinal() != null) {
            fechaVencimiento = contratoSeleccionado.getFechaFinal();
        }
        numeroRipContrato = contratoSeleccionado.getNumeroRipContrato();
        numeroPoliza = contratoSeleccionado.getNumeroPoliza();
        numeroAfiliados = contratoSeleccionado.getNumeroAfiliados();
        if (contratoSeleccionado.getTipoContrato() != null) {
            tipoContrato = contratoSeleccionado.getTipoContrato().getId().toString();
        }
        if (contratoSeleccionado.getTipoPago() != null) {
            tipoPago = contratoSeleccionado.getTipoPago().getId().toString();
            cambiaTipoPago();
        }
        valorMensual = contratoSeleccionado.getValorMensual();
        valorTotal = contratoSeleccionado.getValorContrato();
        valorValidacionMensual = contratoSeleccionado.getValorValidacionMensual();
        valorAlarma = contratoSeleccionado.getValorAlarma();
        tarifaPos = contratoSeleccionado.getCodigoTarifaPos();
        tarifaNoPos = contratoSeleccionado.getCodigoTarifaNopos();
        if (contratoSeleccionado.getTipoFacturacion() != null) {
            tipoFacturacion = contratoSeleccionado.getTipoFacturacion().getId().toString();
        }
        exigirRipsFacturar = contratoSeleccionado.getRip();
        observacionesContrato = contratoSeleccionado.getObservacionContrato();
        observacionesFacturacion = contratoSeleccionado.getObservacionFacturacion();
        cuentaPorCobrar = contratoSeleccionado.getCuentaCobrar();
        cuentaCopago = contratoSeleccionado.getCuentaCopago();
        codigoConcepto = contratoSeleccionado.getCodigoConcepto();
        cuentaConceptoDescuento = contratoSeleccionado.getCodigoConceptoDescuento();

        //----------SACADO DE PROGRAMAS----------------
        cm1 = contratoSeleccionado.getCm1();
        cm2 = contratoSeleccionado.getCm2();
        cm3 = contratoSeleccionado.getCm3();
        cm4 = contratoSeleccionado.getCm4();
        cm5 = contratoSeleccionado.getCm5();
        cmc = contratoSeleccionado.getCmc();
        cmb = contratoSeleccionado.getCmb();
        cp1 = contratoSeleccionado.getCp1();
        cp2 = contratoSeleccionado.getCp2();
        cp3 = contratoSeleccionado.getCp3();
        cp4 = contratoSeleccionado.getCp4();
        cp5 = contratoSeleccionado.getCp5();
        cpc = contratoSeleccionado.getCpc();
        cpb = contratoSeleccionado.getCpb();
        medicamentoValor1 = contratoSeleccionado.getMedicamentoValor1();
        medicamentoValor2 = contratoSeleccionado.getMedicamentoValor2();
        medicamentoValor3 = contratoSeleccionado.getMedicamentoValor3();
        insumosPorcentaje1 = contratoSeleccionado.getInsumosPorcentaje1();
        insumosPorcentaje2 = contratoSeleccionado.getInsumosPorcentaje2();
        insumosPorcentaje3 = contratoSeleccionado.getInsumosPorcentaje3();
        if (contratoSeleccionado.getIdManualTarifario() != null) {
            idManualTarifario = contratoSeleccionado.getIdManualTarifario().getIdManualTarifario().toString();
        }

        iva = contratoSeleccionado.getAplicarIva();
        cree = contratoSeleccionado.getAplicarCree();

        tituloTabContratos = "Datos Contrato: " + descripcionContrato;
        //listaProgramas = contratoSeleccionado.getFacProgramaList();
        //tituloTabProgramas = "Programas (" + listaProgramas.size() + ")";
        if (!cargandoDesdeTab) {//si se esta cargando desde tab esta funcion no aplica (provocaria error javaScript)
            RequestContext.getCurrentInstance().execute("PF('dialogoBuscarContratos').hide(); PF('wvTablaContratos').clearFilters();");
        }
    }

    public void eliminarContrato() {
        if (contratoSeleccionado == null) {
            imprimirMensaje("Error", "No se ha cargado ningún contrato", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoEliminarContrato').show();");
    }

    public void confirmarEliminarContrato() {
        if (contratoSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún contrato", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            contratoFacade.remove(contratoSeleccionado);
            listaContratos = contratoFacade.buscarOrdenado();
            limpiarFormularioContratos();
            RequestContext.getCurrentInstance().update("IdFormContratos");
            imprimirMensaje("Correcto", "El contrato ha sido eliminado", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El contrato que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void clickBtnCopiarManual() {
        if (contratoSeleccionado == null) {
            imprimirMensaje("Error", "No se ha cargado ninguna contrado", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoCopiarManual').show();");
    }

    public void guardarContrato() {
        if (validacionCampoVacio(codigo, "Código")) {
            return;
        }
        //buscar si codigo ya existe        
        List<FacContrato> listaContratosBuscados;
        if (contratoSeleccionado == null) {//es nuevo
            listaContratosBuscados = contratoFacade.buscarPorCodigo(codigo);
            if (listaContratosBuscados != null && !listaContratosBuscados.isEmpty()) {
                imprimirMensaje("Error", "El código ingresado ya está siendo usado por otro contrato", FacesMessage.SEVERITY_ERROR);
                return;
            }
        } else {//se esta actualizando
            if (codigo.compareToIgnoreCase(contratoSeleccionado.getCodigoContrato()) != 0) {//se modifico el codigo
                listaContratosBuscados = contratoFacade.buscarPorCodigo(codigo);
                if (listaContratosBuscados != null && !listaContratosBuscados.isEmpty()) {
                    imprimirMensaje("Error", "El código ingresado ya está siendo usado por otro contrato", FacesMessage.SEVERITY_ERROR);
                    return;
                }
            }
        }

        if (validacionCampoVacio(descripcionContrato, "Descripción Contrato")) {
            return;
        }
        if (validacionCampoVacio(administradora, "Administradora")) {
            return;
        }
        if (validacionCampoVacio(tipoContrato, "Tipo Contrato")) {
            return;
        }
        if (validacionCampoVacio(tipoPago, "Tipo Pago")) {
            return;
        }
        if (validacionFechaVacia(fechaInicio, "Fecha inicio")) {
            return;
        }
        if (validacionFechaVacia(fechaVencimiento, "Fecha vencimiento")) {
            return;
        }
        if (contratoSeleccionado == null) {
            guardarNuevoContrato();
        } else {
            actualizarContratoExistente();
        }
    }

    private void guardarNuevoContrato() {
        FacContrato nuevoContrato = new FacContrato();
        nuevoContrato.setCodigoContrato(codigo);
        nuevoContrato.setDescripcion(descripcionContrato);
        if (validarNoVacio(administradora)) {
            nuevoContrato.setIdAdministradora(administradoraFacade.find(Integer.parseInt(administradora)));
        }
        nuevoContrato.setFechaInicio(fechaInicio);
        nuevoContrato.setFechaFinal(fechaVencimiento);
        nuevoContrato.setNumeroRipContrato(numeroRipContrato);
        nuevoContrato.setNumeroPoliza(numeroPoliza);
        nuevoContrato.setNumeroAfiliados(numeroAfiliados);

        if (validarNoVacio(tipoContrato)) {
            nuevoContrato.setTipoContrato(clasificacionesFachada.find(Integer.parseInt(tipoContrato)));
        }
        if (validarNoVacio(tipoPago)) {
            nuevoContrato.setTipoPago(clasificacionesFachada.find(Integer.parseInt(tipoPago)));
        }
        nuevoContrato.setValorMensual(valorMensual);
        nuevoContrato.setValorContrato(valorTotal);
        nuevoContrato.setValorValidacionMensual(valorValidacionMensual);
        nuevoContrato.setValorAlarma(valorAlarma);
        nuevoContrato.setCodigoTarifaPos(tarifaPos);
        nuevoContrato.setCodigoTarifaNopos(tarifaNoPos);
        if (validarNoVacio(tipoFacturacion)) {
            nuevoContrato.setTipoFacturacion(clasificacionesFachada.find(Integer.parseInt(tipoFacturacion)));
        }
        nuevoContrato.setRip(exigirRipsFacturar);
        nuevoContrato.setObservacionContrato(observacionesContrato);
        nuevoContrato.setObservacionFacturacion(observacionesFacturacion);
        nuevoContrato.setCuentaCobrar(cuentaPorCobrar);
        nuevoContrato.setCuentaCopago(cuentaCopago);
        nuevoContrato.setCodigoConcepto(codigoConcepto);
        nuevoContrato.setCodigoConceptoDescuento(cuentaConceptoDescuento);

        nuevoContrato.setCm1(cm1);
        nuevoContrato.setCm2(cm2);
        nuevoContrato.setCm3(cm3);
        nuevoContrato.setCm4(cm4);
        nuevoContrato.setCm5(cm5);
        nuevoContrato.setCmc(cmc);
        nuevoContrato.setCmb(cmb);
        nuevoContrato.setCp1(cp1);
        nuevoContrato.setCp2(cp2);
        nuevoContrato.setCp3(cp3);
        nuevoContrato.setCp4(cp4);
        nuevoContrato.setCp5(cp5);
        nuevoContrato.setCpc(cpc);
        nuevoContrato.setCpb(cpb);
        nuevoContrato.setMedicamentoValor1(medicamentoValor1);
        nuevoContrato.setMedicamentoValor2(medicamentoValor2);
        nuevoContrato.setMedicamentoValor3(medicamentoValor3);
        nuevoContrato.setInsumosPorcentaje1(insumosPorcentaje1);
        nuevoContrato.setInsumosPorcentaje2(insumosPorcentaje2);
        nuevoContrato.setInsumosPorcentaje3(insumosPorcentaje3);
        if (validarNoVacio(idManualTarifario)) {
            nuevoContrato.setIdManualTarifario(manualTarifarioFacade.find(Integer.parseInt(idManualTarifario)));
        }

        nuevoContrato.setAplicarIva(iva);
        nuevoContrato.setAplicarCree(cree);

        contratoFacade.create(nuevoContrato);
        limpiarFormularioContratos();
        listaContratos = contratoFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().update("IdFormContratos");
        imprimirMensaje("Correcto", "El contrato ha sido creado.", FacesMessage.SEVERITY_INFO);
    }

    private void actualizarContratoExistente() {//realiza la actualizacion del consultorio        

        contratoSeleccionado.setCodigoContrato(codigo);
        contratoSeleccionado.setDescripcion(descripcionContrato);
        if (validarNoVacio(administradora)) {
            contratoSeleccionado.setIdAdministradora(administradoraFacade.find(Integer.parseInt(administradora)));
        } else {
            contratoSeleccionado.setIdAdministradora(null);
        }
        contratoSeleccionado.setFechaInicio(fechaInicio);
        contratoSeleccionado.setFechaFinal(fechaVencimiento);
        contratoSeleccionado.setNumeroRipContrato(numeroRipContrato);
        contratoSeleccionado.setNumeroPoliza(numeroPoliza);
        contratoSeleccionado.setNumeroAfiliados(numeroAfiliados);

        if (validarNoVacio(tipoContrato)) {
            contratoSeleccionado.setTipoContrato(clasificacionesFachada.find(Integer.parseInt(tipoContrato)));
        } else {
            contratoSeleccionado.setTipoContrato(null);
        }
        if (validarNoVacio(tipoPago)) {
            contratoSeleccionado.setTipoPago(clasificacionesFachada.find(Integer.parseInt(tipoPago)));
        } else {
            contratoSeleccionado.setTipoPago(null);
        }
        contratoSeleccionado.setValorMensual(valorMensual);
        contratoSeleccionado.setValorContrato(valorTotal);
        contratoSeleccionado.setValorValidacionMensual(valorValidacionMensual);
        contratoSeleccionado.setValorAlarma(valorAlarma);
        contratoSeleccionado.setCodigoTarifaPos(tarifaPos);
        contratoSeleccionado.setCodigoTarifaNopos(tarifaNoPos);
        if (validarNoVacio(tipoFacturacion)) {
            contratoSeleccionado.setTipoFacturacion(clasificacionesFachada.find(Integer.parseInt(tipoFacturacion)));
        } else {
            contratoSeleccionado.setTipoFacturacion(null);
        }
        contratoSeleccionado.setRip(exigirRipsFacturar);
        contratoSeleccionado.setObservacionContrato(observacionesContrato);
        contratoSeleccionado.setObservacionFacturacion(observacionesFacturacion);
        contratoSeleccionado.setCuentaCobrar(cuentaPorCobrar);
        contratoSeleccionado.setCuentaCopago(cuentaCopago);
        contratoSeleccionado.setCodigoConcepto(codigoConcepto);
        contratoSeleccionado.setCodigoConceptoDescuento(cuentaConceptoDescuento);

        contratoSeleccionado.setCm1(cm1);
        contratoSeleccionado.setCm2(cm2);
        contratoSeleccionado.setCm3(cm3);
        contratoSeleccionado.setCm4(cm4);
        contratoSeleccionado.setCm5(cm5);
        contratoSeleccionado.setCmc(cmc);
        contratoSeleccionado.setCmb(cmb);
        contratoSeleccionado.setCp1(cp1);
        contratoSeleccionado.setCp2(cp2);
        contratoSeleccionado.setCp3(cp3);
        contratoSeleccionado.setCp4(cp4);
        contratoSeleccionado.setCp5(cp5);
        contratoSeleccionado.setCpc(cpc);
        contratoSeleccionado.setCpb(cpb);
        contratoSeleccionado.setMedicamentoValor1(medicamentoValor1);
        contratoSeleccionado.setMedicamentoValor2(medicamentoValor2);
        contratoSeleccionado.setMedicamentoValor3(medicamentoValor3);
        contratoSeleccionado.setInsumosPorcentaje1(insumosPorcentaje1);
        contratoSeleccionado.setInsumosPorcentaje2(insumosPorcentaje2);
        contratoSeleccionado.setInsumosPorcentaje3(insumosPorcentaje3);

        if (validarNoVacio(idManualTarifario)) {
            contratoSeleccionado.setIdManualTarifario(manualTarifarioFacade.find(Integer.parseInt(idManualTarifario)));
        } else {
            contratoSeleccionado.setIdManualTarifario(null);
        }

        contratoSeleccionado.setAplicarIva(iva);
        contratoSeleccionado.setAplicarCree(cree);

        contratoFacade.edit(contratoSeleccionado);
        limpiarFormularioContratos();
        listaContratos = contratoFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().update("IdFormContratos");
        imprimirMensaje("Correcto", "El contrato ha sido actualizado.", FacesMessage.SEVERITY_INFO);
    }

    public void cambiaTipoPago() {
        listaTipoFacturacion = new ArrayList<>();
        if (validarNoVacio(tipoPago)) {
            listaTipoFacturacion = cargarClasificacion("TipoFacturacion");//carga todos los tipo de factuaracion
            for (int i = 0; i < listaTipoFacturacion.size(); i++) {
                if (clasificacionesFachada.find(Integer.parseInt(tipoPago)).getDescripcion().compareTo("Capitado") == 0) {//tipo pago es capitado
                    if (listaTipoFacturacion.get(i).getLabel().compareTo("Orden de Servicio") == 0
                            || listaTipoFacturacion.get(i).getLabel().compareTo("Orden de Servicio Extramural") == 0
                            || listaTipoFacturacion.get(i).getLabel().compareTo("Recibo de Caja") == 0
                            || listaTipoFacturacion.get(i).getLabel().compareTo("Todos") == 0) {
                    } else {
                        listaTipoFacturacion.remove(i);
                        i--;
                    }
                } else {//tipo pago es Evento
                    if (listaTipoFacturacion.get(i).getLabel().compareTo("Factura de Venta") == 0
                            || listaTipoFacturacion.get(i).getLabel().compareTo("Factura de Venta Extramural") == 0
                            || listaTipoFacturacion.get(i).getLabel().compareTo("Recibo de Caja") == 0
                            || listaTipoFacturacion.get(i).getLabel().compareTo("Todos") == 0) {
                    } else {
                        listaTipoFacturacion.remove(i);
                        i--;
                    }
                }
            }
        }

    }

    
    public void cambiaAdministradoraCopia() {//cambia la administradora de la cual se va a copiar el manual
        listaContratosCopia = new ArrayList<>();
        nombreCopiaManual="";
        idContratoACopiar="";        
        if (validarNoVacio(idAdministradoraACopiar)) {
            administradoraCopiaSeleccionada = administradoraFacade.find(Integer.parseInt(idAdministradoraACopiar));
            if (administradoraCopiaSeleccionada != null) {
                listaContratosCopia = administradoraCopiaSeleccionada.getFacContratoList();
            }
        }
        cambiaContratoCopia();
    }
    
    public void cambiaContratoCopia() {//cambia el comtrato de la cual se van a copiar el manual
        nombreManualContratoACopiar="";
        if (validarNoVacio(idContratoACopiar)) {
            contratoCopiaSeleccionado = contratoFacade.find(Integer.parseInt(idContratoACopiar));
            if (contratoCopiaSeleccionado != null) {
                if (contratoCopiaSeleccionado.getIdManualTarifario() != null) {
                    manualCopiaSeleccionado=contratoCopiaSeleccionado.getIdManualTarifario();
                    nombreManualContratoACopiar=manualCopiaSeleccionado.getNombreManualTarifario();
                }
            } 
        }
    }

    

    public void copiarManual() {
        if (administradoraCopiaSeleccionada==null) {
            imprimirMensaje("Error", "Se debe seleccionar una administradora", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (contratoCopiaSeleccionado==null) {
            imprimirMensaje("Error", "Se debe seleccionar un contrato", FacesMessage.SEVERITY_ERROR);
            return;
        }

        if (manualCopiaSeleccionado==null) {
            imprimirMensaje("Error", "El contrato seleccionado no tiene manual tarifario", FacesMessage.SEVERITY_ERROR);
            return;
        }

        if (validacionCampoVacio(nombreCopiaManual, "Nombre de copia")) {
            return;
        }
        
        FacManualTarifario manualAux;
        FacManualTarifarioInsumo nuevoManualTarifarioInsumo;
        FacManualTarifarioMedicamento nuevoManualTarifarioMedicamento;
        FacManualTarifarioPaquete nuevoManualTarifarioPaquete;
        FacManualTarifarioServicio nuevoManualTarifarioServicio;

        //VALIDAR QUE NO EXISTA MANUAL CON IGUAL NOMBRE
        manualAux = manualTarifarioFacade.buscarPorNombre(nombreCopiaManual);
        if (manualAux != null) {
            imprimirMensaje("Error", "Ya existe un manual tarifario con el igual nombre, debe digitar otro nombre", FacesMessage.SEVERITY_ERROR);
            return;
        }

        if (manualCopiaSeleccionado != null) {
            manualAux = new FacManualTarifario();
            manualAux.setCodigoManualTarifario(manualCopiaSeleccionado.getCodigoManualTarifario());
            manualAux.setNombreManualTarifario(nombreCopiaManual);
            manualTarifarioFacade.create(manualAux);
            manualAux = manualTarifarioFacade.find(manualAux.getIdManualTarifario());

            for (FacManualTarifarioInsumo manualTarifarioInsumo : manualCopiaSeleccionado.getFacManualTarifarioInsumoList()) {
                nuevoManualTarifarioInsumo = new FacManualTarifarioInsumo();
                FacManualTarifarioInsumoPK llavePK = new FacManualTarifarioInsumoPK();
                llavePK.setIdManualTarifario(manualAux.getIdManualTarifario());
                llavePK.setIdInsumo(manualTarifarioInsumo.getFacManualTarifarioInsumoPK().getIdInsumo());
                nuevoManualTarifarioInsumo.setFacManualTarifarioInsumoPK(llavePK);
                nuevoManualTarifarioInsumo.setActivo(manualTarifarioInsumo.getActivo());
                nuevoManualTarifarioInsumo.setDescuento(manualTarifarioInsumo.getDescuento());
                nuevoManualTarifarioInsumo.setObservacion(manualTarifarioInsumo.getObservacion());
                nuevoManualTarifarioInsumo.setValorInicial(manualTarifarioInsumo.getValorInicial());
                nuevoManualTarifarioInsumo.setValorFinal(manualTarifarioInsumo.getValorFinal());
                manualTarifarioInsumoFacade.create(nuevoManualTarifarioInsumo);
            }

            for (FacManualTarifarioMedicamento manualTarifarioMedicamento : manualCopiaSeleccionado.getFacManualTarifarioMedicamentoList()) {
                nuevoManualTarifarioMedicamento = new FacManualTarifarioMedicamento();
                FacManualTarifarioMedicamentoPK llavePK = new FacManualTarifarioMedicamentoPK();
                llavePK.setIdManualTarifario(manualAux.getIdManualTarifario());
                llavePK.setIdMedicamento(manualTarifarioMedicamento.getFacManualTarifarioMedicamentoPK().getIdMedicamento());
                nuevoManualTarifarioMedicamento.setFacManualTarifarioMedicamentoPK(llavePK);
                nuevoManualTarifarioMedicamento.setActivo(manualTarifarioMedicamento.getActivo());
                nuevoManualTarifarioMedicamento.setDescuento(manualTarifarioMedicamento.getDescuento());
                nuevoManualTarifarioMedicamento.setObservacion(manualTarifarioMedicamento.getObservacion());
                nuevoManualTarifarioMedicamento.setValorInicial(manualTarifarioMedicamento.getValorInicial());
                nuevoManualTarifarioMedicamento.setValorFinal(manualTarifarioMedicamento.getValorFinal());
                manualTarifarioMedicamentoFacade.create(nuevoManualTarifarioMedicamento);
            }

            for (FacManualTarifarioPaquete manualTarifarioPaquete : manualCopiaSeleccionado.getFacManualTarifarioPaqueteList()) {
                nuevoManualTarifarioPaquete = new FacManualTarifarioPaquete();
                FacManualTarifarioPaquetePK llavePK = new FacManualTarifarioPaquetePK();
                llavePK.setIdManualTarifario(manualAux.getIdManualTarifario());
                llavePK.setIdPaquete(manualTarifarioPaquete.getFacManualTarifarioPaquetePK().getIdPaquete());
                nuevoManualTarifarioPaquete.setFacManualTarifarioPaquetePK(llavePK);
                nuevoManualTarifarioPaquete.setActivo(manualTarifarioPaquete.getActivo());
                nuevoManualTarifarioPaquete.setDescuento(manualTarifarioPaquete.getDescuento());
                nuevoManualTarifarioPaquete.setObservacion(manualTarifarioPaquete.getObservacion());
                nuevoManualTarifarioPaquete.setValorInicial(manualTarifarioPaquete.getValorInicial());
                nuevoManualTarifarioPaquete.setValorFinal(manualTarifarioPaquete.getValorFinal());
                manualTarifarioPaqueteFacade.create(nuevoManualTarifarioPaquete);
            }

            for (FacManualTarifarioServicio manualTarifarioServicio : manualCopiaSeleccionado.getFacManualTarifarioServicioList()) {
                nuevoManualTarifarioServicio = new FacManualTarifarioServicio();
                FacManualTarifarioServicioPK llavePK = new FacManualTarifarioServicioPK();
                llavePK.setIdManualTarifario(manualAux.getIdManualTarifario());
                llavePK.setIdServicio(manualTarifarioServicio.getFacManualTarifarioServicioPK().getIdServicio());
                nuevoManualTarifarioServicio.setFacManualTarifarioServicioPK(llavePK);
                nuevoManualTarifarioServicio.setActivo(manualTarifarioServicio.getActivo());
                nuevoManualTarifarioServicio.setDescuento(manualTarifarioServicio.getDescuento());
                nuevoManualTarifarioServicio.setObservacion(manualTarifarioServicio.getObservacion());
                nuevoManualTarifarioServicio.setValorInicial(manualTarifarioServicio.getValorInicial());
                nuevoManualTarifarioServicio.setValorFinal(manualTarifarioServicio.getValorFinal());
                nuevoManualTarifarioServicio.setMetaCumplimiento(manualTarifarioServicio.getMetaCumplimiento());
                nuevoManualTarifarioServicio.setPeriodicidad(manualTarifarioServicio.getPeriodicidad());
                nuevoManualTarifarioServicio.setHonorarioMedico(manualTarifarioServicio.getHonorarioMedico());
                nuevoManualTarifarioServicio.setTipoTarifa(manualTarifarioServicio.getTipoTarifa());
                nuevoManualTarifarioServicio.setAnioUnidadValor(manualTarifarioServicio.getAnioUnidadValor());
                manualTarifarioServicioFacade.create(nuevoManualTarifarioServicio);
            }
        }
        contratoSeleccionado.setIdManualTarifario(manualAux);
        contratoFacade.edit(contratoSeleccionado);
        imprimirMensaje("Correcto", "copia de manual tarifario realizada.", FacesMessage.SEVERITY_INFO);
        RequestContext.getCurrentInstance().update("IdFormContratos");
        RequestContext.getCurrentInstance().update("IdMensajeContratos");
        RequestContext.getCurrentInstance().execute("PF('dialogoCopiarManual').hide()");
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcionContrato() {
        return descripcionContrato;
    }

    public void setDescripcionContrato(String descripcionContrato) {
        this.descripcionContrato = descripcionContrato;
    }

    public String getAdministradora() {
        return administradora;
    }

    public void setAdministradora(String administradora) {
        this.administradora = administradora;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getNumeroRipContrato() {
        return numeroRipContrato;
    }

    public void setNumeroRipContrato(String numeroRipContrato) {
        this.numeroRipContrato = numeroRipContrato;
    }

    public String getNumeroPoliza() {
        return numeroPoliza;
    }

    public void setNumeroPoliza(String numeroPoliza) {
        this.numeroPoliza = numeroPoliza;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public double getValorMensual() {
        return valorMensual;
    }

    public void setValorMensual(double valorMensual) {
        this.valorMensual = valorMensual;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public double getValorValidacionMensual() {
        return valorValidacionMensual;
    }

    public void setValorValidacionMensual(double valorValidacionMensual) {
        this.valorValidacionMensual = valorValidacionMensual;
    }

    public String getTarifaPos() {
        return tarifaPos;
    }

    public void setTarifaPos(String tarifaPos) {
        this.tarifaPos = tarifaPos;
    }

    public String getTarifaNoPos() {
        return tarifaNoPos;
    }

    public void setTarifaNoPos(String tarifaNoPos) {
        this.tarifaNoPos = tarifaNoPos;
    }

    public String getTipoFacturacion() {
        return tipoFacturacion;
    }

    public void setTipoFacturacion(String tipoFacturacion) {
        this.tipoFacturacion = tipoFacturacion;
    }

    public boolean isExigirRipsFacturar() {
        return exigirRipsFacturar;
    }

    public void setExigirRipsFacturar(boolean exigirRipsFacturar) {
        this.exigirRipsFacturar = exigirRipsFacturar;
    }

    public String getObservacionesContrato() {
        return observacionesContrato;
    }

    public void setObservacionesContrato(String observacionesContrato) {
        this.observacionesContrato = observacionesContrato;
    }

    public String getObservacionesFacturacion() {
        return observacionesFacturacion;
    }

    public void setObservacionesFacturacion(String observacionesFacturacion) {
        this.observacionesFacturacion = observacionesFacturacion;
    }

    public String getCodigoConcepto() {
        return codigoConcepto;
    }

    public void setCodigoConcepto(String codigoConcepto) {
        this.codigoConcepto = codigoConcepto;
    }

    public String getTituloTabContratos() {
        return tituloTabContratos;
    }

    public void setTituloTabContratos(String tituloTabContratos) {
        this.tituloTabContratos = tituloTabContratos;
    }

//    public String getTituloTabProgramas() {
//        return tituloTabProgramas;
//    }
//
//    public void setTituloTabProgramas(String tituloTabProgramas) {
//        this.tituloTabProgramas = tituloTabProgramas;
//    }
    public int getNumeroAfiliados() {
        return numeroAfiliados;
    }

    public void setNumeroAfiliados(int numeroAfiliados) {
        this.numeroAfiliados = numeroAfiliados;
    }

    public double getValorAlarma() {
        return valorAlarma;
    }

    public void setValorAlarma(double valorAlarma) {
        this.valorAlarma = valorAlarma;
    }

    public String getCuentaPorCobrar() {
        return cuentaPorCobrar;
    }

    public void setCuentaPorCobrar(String cuentaPorCobrar) {
        this.cuentaPorCobrar = cuentaPorCobrar;
    }

    public String getCuentaCopago() {
        return cuentaCopago;
    }

    public void setCuentaCopago(String cuentaCopago) {
        this.cuentaCopago = cuentaCopago;
    }

    public String getCuentaConceptoDescuento() {
        return cuentaConceptoDescuento;
    }

    public void setCuentaConceptoDescuento(String cuentaConceptoDescuento) {
        this.cuentaConceptoDescuento = cuentaConceptoDescuento;
    }

    public FacContrato getContratoSeleccionadoTabla() {
        return contratoSeleccionadoTabla;
    }

    public void setContratoSeleccionadoTabla(FacContrato contratoSeleccionadoTabla) {
        this.contratoSeleccionadoTabla = contratoSeleccionadoTabla;
    }

    public List<FacContrato> getListaContratos() {
        return listaContratos;
    }

    public void setListaContratos(List<FacContrato> listaContratos) {
        this.listaContratos = listaContratos;
    }

    public FacContrato getContratoSeleccionado() {
        return contratoSeleccionado;
    }

    public void setContratoSeleccionado(FacContrato contratoSeleccionado) {
        this.contratoSeleccionado = contratoSeleccionado;
    }

//    public FacPrograma getProgramaSeleccionado() {
//        return programaSeleccionado;
//    }
//
//    public void setProgramaSeleccionado(FacPrograma programaSelecccionado) {
//        this.programaSeleccionado = programaSelecccionado;
//    }
//
//    public List<FacPrograma> getListaProgramas() {
//        return listaProgramas;
//    }
//
//    public void setListaProgramas(List<FacPrograma> listaProgramas) {
//        this.listaProgramas = listaProgramas;
//    }
    public FacContrato getContratoCopiaSeleccionado() {
        return contratoCopiaSeleccionado;
    }

    public void setContratoCopiaSeleccionado(FacContrato contratoCopiaSeleccionado) {
        this.contratoCopiaSeleccionado = contratoCopiaSeleccionado;
    }
//
//    public List<FacPrograma> getListaProgramasCopia() {
//        return listaProgramasCopia;
//    }
//
//    public void setListaProgramasCopia(List<FacPrograma> listaProgramasCopia) {
//        this.listaProgramasCopia = listaProgramasCopia;
//    }

    public String getIdContratoACopiar() {
        return idContratoACopiar;
    }

    public void setIdContratoACopiar(String idContratoACopiar) {
        this.idContratoACopiar = idContratoACopiar;
    }

//    public List<FacPrograma> getProgramasCopiaSeleccionados() {
//        return programasCopiaSeleccionados;
//    }
//
//    public void setProgramasCopiaSeleccionados(List<FacPrograma> programasCopiaSeleccionados) {
//        this.programasCopiaSeleccionados = programasCopiaSeleccionados;
//    }
    public List<SelectItem> getListaTipoFacturacion() {
        return listaTipoFacturacion;
    }

    public void setListaTipoFacturacion(List<SelectItem> listaTipoFacturacion) {
        this.listaTipoFacturacion = listaTipoFacturacion;
    }

    public double getCm1() {
        return cm1;
    }

    public void setCm1(double cm1) {
        this.cm1 = cm1;
    }

    public double getCm2() {
        return cm2;
    }

    public void setCm2(double cm2) {
        this.cm2 = cm2;
    }

    public double getCm3() {
        return cm3;
    }

    public void setCm3(double cm3) {
        this.cm3 = cm3;
    }

    public double getCm4() {
        return cm4;
    }

    public void setCm4(double cm4) {
        this.cm4 = cm4;
    }

    public double getCm5() {
        return cm5;
    }

    public void setCm5(double cm5) {
        this.cm5 = cm5;
    }

    public boolean isCmc() {
        return cmc;
    }

    public void setCmc(boolean cmc) {
        this.cmc = cmc;
    }

    public boolean isCmb() {
        return cmb;
    }

    public void setCmb(boolean cmb) {
        this.cmb = cmb;
    }

    public double getCp1() {
        return cp1;
    }

    public void setCp1(double cp1) {
        this.cp1 = cp1;
    }

    public double getCp2() {
        return cp2;
    }

    public void setCp2(double cp2) {
        this.cp2 = cp2;
    }

    public double getCp3() {
        return cp3;
    }

    public void setCp3(double cp3) {
        this.cp3 = cp3;
    }

    public double getCp4() {
        return cp4;
    }

    public void setCp4(double cp4) {
        this.cp4 = cp4;
    }

    public double getCp5() {
        return cp5;
    }

    public void setCp5(double cp5) {
        this.cp5 = cp5;
    }

    public boolean isCpc() {
        return cpc;
    }

    public void setCpc(boolean cpc) {
        this.cpc = cpc;
    }

    public boolean isCpb() {
        return cpb;
    }

    public void setCpb(boolean cpb) {
        this.cpb = cpb;
    }

    public double getMedicamentoValor1() {
        return medicamentoValor1;
    }

    public void setMedicamentoValor1(double medicamentoValor1) {
        this.medicamentoValor1 = medicamentoValor1;
    }

    public double getMedicamentoValor2() {
        return medicamentoValor2;
    }

    public void setMedicamentoValor2(double medicamentoValor2) {
        this.medicamentoValor2 = medicamentoValor2;
    }

    public double getMedicamentoValor3() {
        return medicamentoValor3;
    }

    public void setMedicamentoValor3(double medicamentoValor3) {
        this.medicamentoValor3 = medicamentoValor3;
    }

    public double getInsumosPorcentaje1() {
        return insumosPorcentaje1;
    }

    public void setInsumosPorcentaje1(double insumosPorcentaje1) {
        this.insumosPorcentaje1 = insumosPorcentaje1;
    }

    public double getInsumosPorcentaje2() {
        return insumosPorcentaje2;
    }

    public void setInsumosPorcentaje2(double insumosPorcentaje2) {
        this.insumosPorcentaje2 = insumosPorcentaje2;
    }

    public double getInsumosPorcentaje3() {
        return insumosPorcentaje3;
    }

    public void setInsumosPorcentaje3(double insumosPorcentaje3) {
        this.insumosPorcentaje3 = insumosPorcentaje3;
    }

    public String getIdManualTarifario() {
        return idManualTarifario;
    }

    public void setIdManualTarifario(String idManualTarifario) {
        this.idManualTarifario = idManualTarifario;
    }

    public List<FacManualTarifario> getListaManuales() {
        return listaManuales;
    }

    public void setListaManuales(List<FacManualTarifario> listaManuales) {
        this.listaManuales = listaManuales;
    }

    public boolean isIva() {
        return iva;
    }

    public void setIva(boolean iva) {
        this.iva = iva;
    }

    public boolean isCree() {
        return cree;
    }

    public void setCree(boolean cree) {
        this.cree = cree;
    }

    public String getIdAdministradoraACopiar() {
        return idAdministradoraACopiar;
    }

    public void setIdAdministradoraACopiar(String idAdministradoraACopiar) {
        this.idAdministradoraACopiar = idAdministradoraACopiar;
    }

    public String getNombreManualContratoACopiar() {
        return nombreManualContratoACopiar;
    }

    public void setNombreManualContratoACopiar(String nombreManualContratoACopiar) {
        this.nombreManualContratoACopiar = nombreManualContratoACopiar;
    }

    public String getNombreCopiaManual() {
        return nombreCopiaManual;
    }

    public void setNombreCopiaManual(String nombreCopiaManual) {
        this.nombreCopiaManual = nombreCopiaManual;
    }

    public List<FacContrato> getListaContratosCopia() {
        return listaContratosCopia;
    }

    public void setListaContratosCopia(List<FacContrato> listaContratosCopia) {
        this.listaContratosCopia = listaContratosCopia;
    }

}
