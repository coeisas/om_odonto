/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import beans.utilidades.FilaDataTable;
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
import modelo.entidades.CfgInsumo;
import modelo.entidades.CfgMedicamento;
import modelo.entidades.FacManualTarifario;
import modelo.entidades.FacManualTarifarioInsumo;
import modelo.entidades.FacManualTarifarioInsumoPK;
import modelo.entidades.FacManualTarifarioMedicamento;
import modelo.entidades.FacManualTarifarioMedicamentoPK;
import modelo.entidades.FacManualTarifarioPaquete;
import modelo.entidades.FacManualTarifarioPaquetePK;
import modelo.entidades.FacManualTarifarioServicio;
import modelo.entidades.FacManualTarifarioServicioPK;
import modelo.entidades.FacPaquete;
import modelo.entidades.FacServicio;
import modelo.entidades.FacUnidadValor;
import modelo.fachadas.CfgInsumoFacade;
import modelo.fachadas.CfgMedicamentoFacade;
import modelo.fachadas.FacManualTarifarioFacade;
import modelo.fachadas.FacManualTarifarioInsumoFacade;
import modelo.fachadas.FacManualTarifarioMedicamentoFacade;
import modelo.fachadas.FacManualTarifarioPaqueteFacade;
import modelo.fachadas.FacManualTarifarioServicioFacade;
import modelo.fachadas.FacPaqueteFacade;
import modelo.fachadas.FacServicioFacade;
import modelo.fachadas.FacUnidadValorFacade;
import org.primefaces.context.RequestContext;

/**
 *
 * @author santos
 */
@ManagedBean(name = "manualesTarifariosMB")
@SessionScoped
public class ManualesTarifariosMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
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
    @EJB
    FacServicioFacade servicioFacade;
    @EJB
    CfgMedicamentoFacade medicamentoFacade;
    @EJB
    CfgInsumoFacade insumoFacade;
    @EJB
    FacPaqueteFacade paqueteFacade;
    @EJB
    FacUnidadValorFacade unidadValorFacade;

    //---------------------------------------------------
    //-----------------ENTIDADES ------------------------
    //---------------------------------------------------
    private List<FacUnidadValor> listaUnidadesValor;
    private FacManualTarifario manualSeleccionado;
    private FacManualTarifario manualSeleccionadoTabla;
    private List<FacManualTarifario> listaManuales;

    private List<FacServicio> listaServicios;
    private List<CfgInsumo> listaInsumos;
    private List<CfgMedicamento> listaMedicamentos;
    private List<FacPaquete> listaPaquetes;

    private boolean mostrarTabView = false;//ocultar controles si no se tiene escogido un manual tarifario

    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------  
    private List<FilaDataTable> listaServiciosManual = new ArrayList<>();
    private List<FilaDataTable> listaInsumosManual = new ArrayList<>();
    private List<FilaDataTable> listaMedicamentosManual = new ArrayList<>();
    private List<FilaDataTable> listaPaquetesManual = new ArrayList<>();
    private List<FilaDataTable> listaServiciosManualFiltro = new ArrayList<>();
    private List<FilaDataTable> listaInsumosManualFiltro = new ArrayList<>();
    private List<FilaDataTable> listaMedicamentosManualFiltro = new ArrayList<>();
    private List<FilaDataTable> listaPaquetesManualFiltro = new ArrayList<>();
    private FilaDataTable servicioManualSeleccionado;
    private FilaDataTable insumoManualSeleccionado;
    private FilaDataTable medicamentoManualSeleccionado;
    private FilaDataTable paqueteManualSeleccionado;

    private String tituloTabManual = "Nuevo Manual Tarifario";
    private String tituloTabPaquetes = "Paquetes (0)";
    private String tituloTabInsumos = "Insumos (0)";
    private String tituloTabMedicamentos = "Medicamentos (0)";
    private String tituloTabServicios = "Servicios (0)";
    //datos paquete
    private String codigoManual = "";
    private String nombreManual = "";
    //nuevo servicio a adicionar
    private String idServicio = "";
    private double valorFinalServicio = 0;
    private double valorInicialServicio = 0;
    private int metaCumplimiento = 0;
    private int periodicidad = 0;
    private double descuentoServicio = 0;
    private double honorarioMedico = 0;
    private String observacionServicio = "";
    private boolean activoServicio = true;
    private boolean disabledUnidadValor = false;
    private List<SelectItem> listaTipoTarifa;
    private String tipoTarifa = "";
    private String unidadValor = "";
    //editando servicio
    private String idServicioEditando = "";
    private String nombreServicioEditando = "";
    private double valorFinalServicioEditando = 0;
    private double valorInicialServicioEditando = 0;
    private int metaCumplimientoEditando = 0;
    private int periodicidadEditando = 0;
    private double descuentoServicioEditando = 0;
    private double honorarioMedicoEditando = 0;
    private String observacionServicioEditando = "";
    private boolean activoServicioEditando = true;
    private String tipoTarifaEditando = "";
    private String unidadValorEditando = "";
    //nuevo insumo
    private String idInsumo = "";
    private double valorFinalInsumo = 0;
    private double valorInicialInsumo = 0;
    private double descuentoInsumo = 0;
    private String observacionInsumo = "";
    private boolean activoInsumo = true;
    //Editar insumo
    private String idInsumoEditando = "";
    private String nombreInsumoEditando = "";
    private double valorFinalInsumoEditando = 0;
    private double valorInicialInsumoEditando = 0;
    private double descuentoInsumoEditando = 0;
    private String observacionInsumoEditando = "";
    private boolean activoInsumoEditando = true;
    //nuevo medicamento
    private String idMedicamento = "";
    private double valorFinalMedicamento = 0;
    private double valorInicialMedicamento = 0;
    private double descuentoMedicamento = 0;
    private String observacionMedicamento = "";
    private boolean activoMedicamento = true;
    //nuevo medicamento
    private String idMedicamentoEditando = "";
    private String nombreMedicamentoEditando = "";
    private double valorFinalMedicamentoEditando = 0;
    private double valorInicialMedicamentoEditando = 0;
    private double descuentoMedicamentoEditando = 0;
    private String observacionMedicamentoEditando = "";
    private boolean activoMedicamentoEditando = true;
    //nuevo paquete
    private String idPaquete = "";
    private double valorFinalPaquete = 0;
    private double valorInicialPaquete = 0;
    private double descuentoPaquete = 0;
    private String observacionPaquete = "";
    private boolean activoPaquete = true;
    //nuevo paquete
    private String idPaqueteEditando = "";
    private String nombrePaqueteEditando = "";
    private double valorFinalPaqueteEditando = 0;
    private double valorInicialPaqueteEditando = 0;
    private double descuentoPaqueteEditando = 0;
    private String observacionPaqueteEditando = "";
    private boolean activoPaqueteEditando = true;
    //---------------------------------------------------
    //------------- FUNCIONES INICIALES  ----------------
    //---------------------------------------------------      

    public ManualesTarifariosMB() {
    }

    //---------------------------------------------------
    //-------------FUNCIONES MANUALES TARIFARIOS --------
    //--------------------------------------------------- 
    public void cargarManualTarifarioDesdeTab(String idAdministradora) {//cargar contrato desde el tab externo de Administradoras
    }

    public void clickBtnNuevoManual() {
        limpiarFormularioManualesTarifarios();
        mostrarTabView = true;
    }

    public void limpiarFormularioManualesTarifarios() {
        listaUnidadesValor = unidadValorFacade.buscarOrdenado();
        listaManuales = manualTarifarioFacade.buscarOrdenado();
        manualSeleccionado = null;
        tituloTabManual = "Nuevo Manual Tarifario";
        tituloTabPaquetes = "Paquetes (0)";
        tituloTabInsumos = "Insumos (0)";
        tituloTabMedicamentos = "Medicamentos (0)";
        tituloTabServicios = "Servicios (0)";
        codigoManual = "";
        nombreManual = "";
    }

    public void buscarManualTarifario() {
        listaManuales = manualTarifarioFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().execute("PF('wvTablaManualesTarifarios').clearFilters(); PF('wvTablaManualesTarifarios').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().execute("PF('dialogoBuscarManualesTarifarios').show();");
    }

    public void cargarManualTarifario() {//click cobre editar caja (carga los datos de la adminstradora)
        if (manualSeleccionadoTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún manual tarifario de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        limpiarFormularioManualesTarifarios();
        manualSeleccionado = manualTarifarioFacade.find(manualSeleccionadoTabla.getIdManualTarifario());
        recargarFilasTablas();
        codigoManual = manualSeleccionado.getCodigoManualTarifario();
        nombreManual = manualSeleccionado.getNombreManualTarifario();
        tituloTabManual = "Datos Manual: " + nombreManual;
        mostrarTabView = true;
        RequestContext.getCurrentInstance().execute("PF('dialogoBuscarManualesTarifarios').hide();");
        //RequestContext.getCurrentInstance().execute("PF('wvTabView').select(0);");//seleccionar primer tab                                
        RequestContext.getCurrentInstance().update("IdFormManuales:IdTabView");

    }

    public void eliminarManualTarifario() {
        if (manualSeleccionado == null) {
            imprimirMensaje("Error", "No se ha cargado ningún manual tarifario", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoEliminarManualTarifario').show();");
    }

    public void confirmarEliminarManualTarifario() {
        if (manualSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún manual tarifario", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            manualTarifarioFacade.remove(manualSeleccionado);
            listaManuales = manualTarifarioFacade.buscarOrdenado();
            limpiarFormularioManualesTarifarios();
            recargarFilasTablas();
            mostrarTabView = false;
            RequestContext.getCurrentInstance().update("IdFormManuales");
            imprimirMensaje("Correcto", "El Manual Tarifario ha sido eliminado", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El manual tarifario que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void guardarManualTarifario() {
        if (validacionCampoVacio(codigoManual, "Código manual")) {
            return;
        }
        if (validacionCampoVacio(nombreManual, "Nombre manual")) {
            return;
        }
        if (manualSeleccionado == null) {
            guardarNuevoManualTarifario();
        } else {
            actualizarManualTarifarioExistente();
        }
        mostrarTabView = false;
    }

    private void guardarNuevoManualTarifario() {
        FacManualTarifario nuevoManual = new FacManualTarifario();
        nuevoManual.setCodigoManualTarifario(codigoManual);
        nuevoManual.setNombreManualTarifario(nombreManual);
        manualTarifarioFacade.create(nuevoManual);
        limpiarFormularioManualesTarifarios();
        recargarFilasTablas();
        listaManuales = manualTarifarioFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().update("IdFormManuales");
        imprimirMensaje("Correcto", "El manual tarifario ha sido creado.", FacesMessage.SEVERITY_INFO);
    }

    private void actualizarManualTarifarioExistente() {//realiza la actualizacion del consultorio        
        manualSeleccionado.setCodigoManualTarifario(codigoManual);
        manualSeleccionado.setNombreManualTarifario(nombreManual);
        manualTarifarioFacade.edit(manualSeleccionado);
        limpiarFormularioManualesTarifarios();
        recargarFilasTablas();
        listaManuales = manualTarifarioFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().update("IdFormManuales");
        imprimirMensaje("Correcto", "El manual Tarifario ha sido actualizado.", FacesMessage.SEVERITY_INFO);
    }

    public void recargarFilasTablas() {
        servicioManualSeleccionado = null;
        insumoManualSeleccionado = null;
        medicamentoManualSeleccionado = null;
        paqueteManualSeleccionado = null;

        listaServiciosManual = new ArrayList<>();
        listaPaquetesManual = new ArrayList<>();
        listaInsumosManual = new ArrayList<>();
        listaMedicamentosManual = new ArrayList<>();

        listaServiciosManualFiltro = new ArrayList<>();
        listaPaquetesManualFiltro = new ArrayList<>();
        listaInsumosManualFiltro = new ArrayList<>();
        listaMedicamentosManualFiltro = new ArrayList<>();

        FilaDataTable nuevaFila;
        if (manualSeleccionado != null) {
            List<FacManualTarifarioServicio> listaServiciosManualTarifario = manualTarifarioServicioFacade.buscarPorManualTarifario(manualSeleccionado.getIdManualTarifario());
            for (FacManualTarifarioServicio servicioManual : listaServiciosManualTarifario) {//lista servicios
                nuevaFila = new FilaDataTable();
                nuevaFila.setColumna1(servicioManual.getFacManualTarifario().getIdManualTarifario().toString());
                nuevaFila.setColumna2(servicioManual.getFacServicio().getIdServicio().toString());
                nuevaFila.setColumna3(servicioManual.getFacServicio().getCodigoServicio());
                nuevaFila.setColumna4(servicioManual.getFacServicio().getNombreServicio());
                nuevaFila.setColumna5(servicioManual.getTipoTarifa());
                nuevaFila.setColumna6(servicioManual.getValorInicial().toString());
                nuevaFila.setColumna7(servicioManual.getDescuento().toString());
                nuevaFila.setColumna8(servicioManual.getHonorarioMedico().toString());
                nuevaFila.setColumna9(servicioManual.getValorFinal().toString());
                nuevaFila.setColumna10(servicioManual.getMetaCumplimiento().toString());
                nuevaFila.setColumna11(servicioManual.getPeriodicidad().toString());
                if (servicioManual.getActivo()) {
                    nuevaFila.setColumna12("ACTIVO");
                } else {
                    nuevaFila.setColumna12("INACTIVO");
                }
                nuevaFila.setColumna13(servicioManual.getObservacion());
                listaServiciosManual.add(nuevaFila);
                listaServiciosManualFiltro.add(nuevaFila);
            }

            List<FacManualTarifarioPaquete> listaPaquetesManualTarifario = manualTarifarioPaqueteFacade.buscarPorManualTarifario(manualSeleccionado.getIdManualTarifario());
            for (FacManualTarifarioPaquete paqueteManual : listaPaquetesManualTarifario) {//lista paquetes
                nuevaFila = new FilaDataTable();
                nuevaFila.setColumna1(paqueteManual.getFacManualTarifario().getIdManualTarifario().toString());
                nuevaFila.setColumna2(paqueteManual.getFacPaquete().getIdPaquete().toString());
                nuevaFila.setColumna3(paqueteManual.getFacPaquete().getCodigoPaquete());
                nuevaFila.setColumna4(paqueteManual.getFacPaquete().getNombrePaquete());
                nuevaFila.setColumna5(paqueteManual.getValorInicial().toString());
                nuevaFila.setColumna6(paqueteManual.getDescuento().toString());
                nuevaFila.setColumna7(paqueteManual.getValorFinal().toString());
                if (paqueteManual.getActivo()) {
                    nuevaFila.setColumna8("ACTIVO");
                } else {
                    nuevaFila.setColumna8("INACTIVO");
                }
                nuevaFila.setColumna9(paqueteManual.getObservacion());
                listaPaquetesManual.add(nuevaFila);
                listaPaquetesManualFiltro.add(nuevaFila);
            }

            List<FacManualTarifarioInsumo> listaInsumosManualTarifario = manualTarifarioInsumoFacade.buscarPorManualTarifario(manualSeleccionado.getIdManualTarifario());
            for (FacManualTarifarioInsumo insumoManual : listaInsumosManualTarifario) {//lista insumos
                nuevaFila = new FilaDataTable();
                nuevaFila.setColumna1(insumoManual.getFacManualTarifario().getIdManualTarifario().toString());
                nuevaFila.setColumna2(insumoManual.getCfgInsumo().getIdInsumo().toString());
                nuevaFila.setColumna3(insumoManual.getCfgInsumo().getCodigoInsumo());
                nuevaFila.setColumna4(insumoManual.getCfgInsumo().getNombreInsumo());
                nuevaFila.setColumna5(insumoManual.getValorInicial().toString());
                nuevaFila.setColumna6(insumoManual.getDescuento().toString());
                nuevaFila.setColumna7(insumoManual.getValorFinal().toString());
                if (insumoManual.getActivo()) {
                    nuevaFila.setColumna8("ACTIVO");
                } else {
                    nuevaFila.setColumna8("INACTIVO");
                }
                nuevaFila.setColumna9(insumoManual.getObservacion());
                listaInsumosManual.add(nuevaFila);
                listaInsumosManualFiltro.add(nuevaFila);
            }

            List<FacManualTarifarioMedicamento> listaMedicamentosManualTarifario = manualTarifarioMedicamentoFacade.buscarPorManualTarifario(manualSeleccionado.getIdManualTarifario());
            for (FacManualTarifarioMedicamento medicamentoManual : listaMedicamentosManualTarifario) {//lista medicamentos
                nuevaFila = new FilaDataTable();
                nuevaFila.setColumna1(medicamentoManual.getFacManualTarifario().getIdManualTarifario().toString());
                nuevaFila.setColumna2(medicamentoManual.getCfgMedicamento().getIdMedicamento().toString());
                nuevaFila.setColumna3(medicamentoManual.getCfgMedicamento().getCodigoMedicamento());
                nuevaFila.setColumna4(medicamentoManual.getCfgMedicamento().getNombreMedicamento());
                nuevaFila.setColumna5(medicamentoManual.getCfgMedicamento().getFormaMedicamento());
                nuevaFila.setColumna6(medicamentoManual.getValorInicial().toString());
                nuevaFila.setColumna7(medicamentoManual.getDescuento().toString());
                nuevaFila.setColumna8(medicamentoManual.getValorFinal().toString());
                if (medicamentoManual.getActivo()) {
                    nuevaFila.setColumna9("ACTIVO");
                } else {
                    nuevaFila.setColumna9("INACTIVO");
                }
                nuevaFila.setColumna10(medicamentoManual.getObservacion());
                listaMedicamentosManual.add(nuevaFila);
                listaMedicamentosManualFiltro.add(nuevaFila);
            }
        }

        tituloTabPaquetes = "Paquetes (" + listaPaquetesManual.size() + ")";
        tituloTabInsumos = "Insumos (" + listaInsumosManual.size() + ")";
        tituloTabMedicamentos = "Medicamentos (" + listaMedicamentosManual.size() + ")";
        tituloTabServicios = "Servicios (" + listaServiciosManual.size() + ")";

    }

    //---------------------------------------------------
    //-------------FUNCIONES SERVICIOS ------------------
    //--------------------------------------------------- 
    public void cambiaServicio() {//se debe determinar que tipos de tarifas contiene el servicio
        unidadValor = "";
        listaUnidadesValor = unidadValorFacade.buscarOrdenado();
        if (listaUnidadesValor != null && !listaUnidadesValor.isEmpty()) {
            int anioActual = (new Date()).getYear() + 1900;
            for (FacUnidadValor unidad : listaUnidadesValor) {
                if (unidad.getAnio() == anioActual) {
                    unidadValor = String.valueOf(anioActual);
                    break;
                }
            }
            if (unidadValor.length() == 0) {
                unidadValor = listaUnidadesValor.get(0).getAnio().toString();
            }
        }

        activoServicio = true;
        metaCumplimiento = 0;
        periodicidad = 0;
        valorInicialServicio = 0;
        valorFinalServicio = 0;
        descuentoServicio = 0;
        honorarioMedico = 0;
        observacionServicio = "";
        tipoTarifa = "";
        listaTipoTarifa = new ArrayList<>();
        if (validarNoVacio(idServicio)) {
            FacServicio s = servicioFacade.find(Integer.parseInt(idServicio));
            if (s.getFactorSoat() != 0) {
                listaTipoTarifa.add(new SelectItem("SOAT", "SOAT"));
            }
            if (s.getFactorIss() != 0) {
                listaTipoTarifa.add(new SelectItem("ISS", "ISS"));
            }
        }
        listaTipoTarifa.add(new SelectItem("Especifica", "Especifica"));
        tipoTarifa = listaTipoTarifa.get(0).getValue().toString();

        calcularValoresServicio();
    }

    public void calcularValoresServicio() {
        disabledUnidadValor = tipoTarifa.compareTo("Especifica") == 0;
        if (validarNoVacio(idServicio)) {
            FacServicio s = servicioFacade.find(Integer.parseInt(idServicio));
            switch (tipoTarifa) {
                case "Especifica":
                    valorInicialServicio = s.getValorParticular();
                    valorFinalServicio = valorInicialServicio - (valorInicialServicio * (descuentoServicio / 100)) + honorarioMedico;
                    break;
                case "SOAT":
                    if (validarNoVacio(unidadValor)) {
                        FacUnidadValor f = unidadValorFacade.find(Integer.parseInt(unidadValor));
                        valorInicialServicio = s.getFactorIss() * f.getSmlvd();
                        valorFinalServicio = valorInicialServicio - (valorInicialServicio * (descuentoServicio / 100)) + honorarioMedico;
                    } else {
                        imprimirMensaje("Alerta", "No hay una unidad de valor para calcular el valor final", FacesMessage.SEVERITY_WARN);
                    }
                    break;
                case "ISS":
                    if (validarNoVacio(unidadValor)) {
                        FacUnidadValor f = unidadValorFacade.find(Integer.parseInt(unidadValor));
                        valorInicialServicio = s.getFactorSoat() * f.getUvr();
                        valorFinalServicio = valorInicialServicio - (valorInicialServicio * (descuentoServicio / 100)) + honorarioMedico;
                    } else {
                        imprimirMensaje("Alerta", "No hay una unidad de valor para calcular el valor final", FacesMessage.SEVERITY_WARN);
                    }
                    break;
            }
        }
    }

//    public void calcularValoresServicioEditando() {
//        if (validarNoVacio(idServicioEditando)) {
//            FacServicio s = servicioFacade.find(Integer.parseInt(idServicioEditando));
//            switch (tipoTarifa) {
//                case "Especifica":
//                    valorInicialServicioEditando = s.getValorParticular();
//                    valorFinalServicioEditando = valorInicialServicioEditando - (valorInicialServicioEditando * (descuentoServicioEditando / 100)) + honorarioMedicoEditando;
//                    break;
//                case "SOAT":
//                    if (validarNoVacio(unidadValorEditando)) {
//                        FacUnidadValor f = unidadValorFacade.buscarPorAnio(Integer.parseInt(unidadValorEditando));
//                        valorInicialServicioEditando = s.getFactorIss() * f.getSmlvd();
//                        valorFinalServicioEditando = valorInicialServicioEditando - (valorInicialServicioEditando * (descuentoServicioEditando / 100)) + honorarioMedicoEditando;
//                    } else {
//                        imprimirMensaje("Alerta", "No hay una unidad de valor para calcular el valor final", FacesMessage.SEVERITY_WARN);
//                    }
//                    break;
//                case "ISS":
//                    if (validarNoVacio(unidadValorEditando)) {
//                        FacUnidadValor f = unidadValorFacade.buscarPorAnio(Integer.parseInt(unidadValorEditando));
//                        valorInicialServicioEditando = s.getFactorSoat() * f.getUvr();
//                        valorFinalServicioEditando = valorInicialServicioEditando - (valorInicialServicioEditando * (descuentoServicioEditando / 100)) + honorarioMedicoEditando;
//                    } else {
//                        imprimirMensaje("Alerta", "No hay una unidad de valor para calcular el valor final", FacesMessage.SEVERITY_WARN);
//                    }
//                    break;
//            }
//        }
//    }
//
    public void cargarDialogoAgregarServicio() {
        listaServicios = servicioFacade.buscarNoEstanEnManual(manualSeleccionado.getIdManualTarifario());
        if (listaServicios != null && !listaServicios.isEmpty()) {
            idServicio = listaServicios.get(0).getIdServicio().toString();
        }
        cambiaServicio();
        RequestContext.getCurrentInstance().update("IdFormDialogs:IdPanelAgregarServicio");
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarServicio').show();");

    }

    public void cargarDialogoEditarServicio() {
        if (servicioManualSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún servicio de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }

        FacManualTarifarioServicioPK llavePK = new FacManualTarifarioServicioPK();
        llavePK.setIdManualTarifario(manualTarifarioFacade.find(Integer.parseInt(servicioManualSeleccionado.getColumna1())).getIdManualTarifario());
        llavePK.setIdServicio(servicioFacade.find(Integer.parseInt(servicioManualSeleccionado.getColumna2())).getIdServicio());
        FacManualTarifarioServicio buscado = manualTarifarioServicioFacade.find(llavePK);

        nombreServicioEditando = buscado.getFacServicio().getNombreServicio();
        idServicioEditando = buscado.getFacServicio().getIdServicio().toString();
        activoServicioEditando = buscado.getActivo();
        descuentoServicioEditando = buscado.getDescuento();
        honorarioMedicoEditando = buscado.getHonorarioMedico();
        metaCumplimientoEditando = buscado.getMetaCumplimiento();
        observacionServicioEditando = buscado.getObservacion();
        periodicidadEditando = buscado.getPeriodicidad();
        tipoTarifaEditando = buscado.getTipoTarifa();
        if (buscado.getAnioUnidadValor() != null) {
            unidadValorEditando = buscado.getAnioUnidadValor().getAnio().toString();
        } else {
            unidadValorEditando = "";
        }
        valorInicialServicioEditando = buscado.getValorInicial();
        valorFinalServicioEditando = buscado.getValorFinal();

        RequestContext.getCurrentInstance().update("IdFormDialogs:IdPanelEditandoServicio");
        RequestContext.getCurrentInstance().execute("PF('dialogoEditandoServicio').show();");
    }

    public void agregarServicio() {
        if (validacionCampoVacio(idServicio, "Servicio")) {
            return;
        }
        if (validacionCampoVacio(tipoTarifa, "Tipo tarifa")) {
            return;
        }
        if (tipoTarifa.compareTo("Especifica") != 0) {//si tarifa no es especifica se necesita unidad de valor
            if (validacionCampoVacio(unidadValor, "Unidad de valor")) {
                return;
            }
        }
        FacManualTarifarioServicioPK llavePK = new FacManualTarifarioServicioPK();
        llavePK.setIdManualTarifario(manualSeleccionado.getIdManualTarifario());
        llavePK.setIdServicio(Integer.parseInt(idServicio));
        FacManualTarifarioServicio buscado = manualTarifarioServicioFacade.find(llavePK);
        if (buscado != null) {//validar que no exista
            imprimirMensaje("Error", "Este manual tarifario ya tiene adicionado este servicio.", FacesMessage.SEVERITY_ERROR);
            return;
        }
        FacManualTarifarioServicio nuevoManualTarifarioServicio = new FacManualTarifarioServicio();
        nuevoManualTarifarioServicio.setFacManualTarifarioServicioPK(llavePK);
        nuevoManualTarifarioServicio.setActivo(activoServicio);
        nuevoManualTarifarioServicio.setDescuento(descuentoServicio);
        nuevoManualTarifarioServicio.setHonorarioMedico(honorarioMedico);
        nuevoManualTarifarioServicio.setMetaCumplimiento(metaCumplimiento);
        nuevoManualTarifarioServicio.setObservacion(observacionServicio);
        nuevoManualTarifarioServicio.setPeriodicidad(periodicidad);
        nuevoManualTarifarioServicio.setTipoTarifa(tipoTarifa);
        if (validarNoVacio(unidadValor)) {
            nuevoManualTarifarioServicio.setAnioUnidadValor(unidadValorFacade.find(Integer.parseInt(unidadValor)));
        }
        nuevoManualTarifarioServicio.setValorInicial(valorInicialServicio);
        nuevoManualTarifarioServicio.setValorFinal(valorFinalServicio);
        manualTarifarioServicioFacade.create(nuevoManualTarifarioServicio);
        manualSeleccionado = manualTarifarioFacade.find(manualSeleccionado.getIdManualTarifario());//recargar el manual
        recargarFilasTablas();
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarServicio').hide(); PF('wvTablaServiciosManual').clearFilters(); PF('wvTablaServiciosManual').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("IdFormManuales:IdTabView");
        //imprimirMensaje("Correcto", "El servicio has sido adicionado al manual tarifario.", FacesMessage.SEVERITY_INFO);
    }

    public void actualizarServicio() {
        if (validacionCampoVacio(idServicioEditando, "Servicio")) {
            return;
        }
        if (validacionCampoVacio(tipoTarifaEditando, "Tipo tarifa")) {
            return;
        }
        if (tipoTarifaEditando.compareTo("Especifica") != 0) {//si tarifa no es especifica se necesita unidad de valor
            if (validacionCampoVacio(unidadValorEditando, "Unidad de valor")) {
                return;
            }
        }

        FacManualTarifarioServicioPK llavePK = new FacManualTarifarioServicioPK();
        llavePK.setIdManualTarifario(manualTarifarioFacade.find(Integer.parseInt(servicioManualSeleccionado.getColumna1())).getIdManualTarifario());
        llavePK.setIdServicio(servicioFacade.find(Integer.parseInt(servicioManualSeleccionado.getColumna2())).getIdServicio());
        FacManualTarifarioServicio buscado = manualTarifarioServicioFacade.find(llavePK);
        buscado.setActivo(activoServicioEditando);
        buscado.setDescuento(descuentoServicioEditando);
        buscado.setHonorarioMedico(honorarioMedicoEditando);
        buscado.setMetaCumplimiento(metaCumplimientoEditando);
        buscado.setObservacion(observacionServicioEditando);
        buscado.setPeriodicidad(periodicidadEditando);
        buscado.setValorInicial(valorInicialServicioEditando);
        buscado.setValorFinal(valorFinalServicioEditando);
        manualTarifarioServicioFacade.edit(buscado);
        manualSeleccionado = manualTarifarioFacade.find(manualSeleccionado.getIdManualTarifario());//recargar el manual
        recargarFilasTablas();
        RequestContext.getCurrentInstance().execute("PF('dialogoEditandoServicio').hide(); PF('wvTablaServiciosManual').clearFilters(); PF('wvTablaServiciosManual').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("IdFormManuales:IdTabView");
        //imprimirMensaje("Correcto", "El servicio has sido adicionado al manual tarifario.", FacesMessage.SEVERITY_INFO);
    }

    public void quitarServicio() {
        if (servicioManualSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún servicio de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoQuitarServicio').show();");
    }

    public void confirmarQuitarServicio() {
        if (servicioManualSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún servicio tarifario", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            FacManualTarifarioServicioPK llavePK = new FacManualTarifarioServicioPK();
            llavePK.setIdManualTarifario(manualTarifarioFacade.find(Integer.parseInt(servicioManualSeleccionado.getColumna1())).getIdManualTarifario());
            llavePK.setIdServicio(servicioFacade.find(Integer.parseInt(servicioManualSeleccionado.getColumna2())).getIdServicio());
            manualTarifarioServicioFacade.remove(manualTarifarioServicioFacade.find(llavePK));
            manualSeleccionado = manualTarifarioFacade.find(manualSeleccionado.getIdManualTarifario());
            recargarFilasTablas();
            RequestContext.getCurrentInstance().update("IdFormManuales:IdTabView");
            RequestContext.getCurrentInstance().execute("PF('dialogoQuitarServicio').hide(); PF('wvTablaServiciosManual').clearFilters(); PF('wvTablaServiciosManual').getPaginator().setPage(0);");
            //imprimirMensaje("Correcto", "El servicio ha sido eliminado del manual tarifario.", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El manual servicio que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    //---------------------------------------------------
    //-------------FUNCIONES INSUMOS --------------------
    //--------------------------------------------------- 
    public void cambiaInsumo() {
        activoInsumo = true;
        valorInicialInsumo = 0;
        valorFinalInsumo = 0;
        descuentoInsumo = 0;
        observacionInsumo = "";
        calcularValoresInsumo();
    }

    public void calcularValoresInsumo() {
        if (validarNoVacio(idInsumo)) {
            CfgInsumo p = insumoFacade.find(Integer.parseInt(idInsumo));
            valorInicialInsumo = p.getValor();
            valorFinalInsumo = valorInicialInsumo - (valorInicialInsumo * (descuentoInsumo / 100));
        }
    }

//    public void calcularValoresInsumoEditando() {
//        if (validarNoVacio(idInsumoEditando)) {
//            CfgInsumo p = insumoFacade.find(Integer.parseInt(idInsumoEditando));
//            valorInicialInsumoEditando = p.getValor();
//            valorFinalInsumoEditando = valorInicialInsumoEditando - (valorInicialInsumoEditando * (descuentoInsumoEditando / 100));
//        }
//    }
//    
    public void cargarDialogoAgregarInsumo() {
        idInsumo = "";
        listaInsumos = insumoFacade.buscarNoEstanEnManual(manualSeleccionado.getIdManualTarifario());
        if (listaInsumos != null && !listaInsumos.isEmpty()) {
            idInsumo = listaInsumos.get(0).getIdInsumo().toString();
        }
        cambiaInsumo();
        RequestContext.getCurrentInstance().update("IdFormDialogs:IdPanelAgregarInsumo");
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarInsumo').show();");

    }

    public void cargarDialogoEditarInsumo() {
        if (insumoManualSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún insumo de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }

        FacManualTarifarioInsumoPK llavePK = new FacManualTarifarioInsumoPK();
        llavePK.setIdManualTarifario(manualTarifarioFacade.find(Integer.parseInt(insumoManualSeleccionado.getColumna1())).getIdManualTarifario());
        llavePK.setIdInsumo(insumoFacade.find(Integer.parseInt(insumoManualSeleccionado.getColumna2())).getIdInsumo());
        FacManualTarifarioInsumo buscado = manualTarifarioInsumoFacade.find(llavePK);

        nombreInsumoEditando = buscado.getCfgInsumo().getNombreInsumo();
        idInsumoEditando = buscado.getCfgInsumo().getIdInsumo().toString();
        activoInsumoEditando = buscado.getActivo();
        descuentoInsumoEditando = buscado.getDescuento();
        observacionInsumoEditando = buscado.getObservacion();
        valorInicialInsumoEditando = buscado.getValorInicial();
        valorFinalInsumoEditando = buscado.getValorFinal();

        RequestContext.getCurrentInstance().update("IdFormDialogs:IdPanelEditandoInsumo");
        RequestContext.getCurrentInstance().execute("PF('dialogoEditandoInsumo').show();");
    }

    public void agregarInsumo() {
        if (validacionCampoVacio(idInsumo, "Insumo")) {
            return;
        }
        FacManualTarifarioInsumoPK llavePK = new FacManualTarifarioInsumoPK();
        llavePK.setIdManualTarifario(manualSeleccionado.getIdManualTarifario());
        llavePK.setIdInsumo(Integer.parseInt(idInsumo));
        FacManualTarifarioInsumo buscado = manualTarifarioInsumoFacade.find(llavePK);
        if (buscado != null) {//validar que no exista
            imprimirMensaje("Error", "Este Manual tarifario ya tiene adicionado este insumo.", FacesMessage.SEVERITY_ERROR);
            return;
        }
        FacManualTarifarioInsumo nuevoManualTarifarioInsumo = new FacManualTarifarioInsumo();
        nuevoManualTarifarioInsumo.setFacManualTarifarioInsumoPK(llavePK);
        nuevoManualTarifarioInsumo.setActivo(activoInsumo);
        nuevoManualTarifarioInsumo.setDescuento(descuentoInsumo);
        nuevoManualTarifarioInsumo.setObservacion(observacionInsumo);
        nuevoManualTarifarioInsumo.setValorInicial(valorInicialInsumo);
        nuevoManualTarifarioInsumo.setValorFinal(valorFinalInsumo);
        manualTarifarioInsumoFacade.create(nuevoManualTarifarioInsumo);
        manualSeleccionado = manualTarifarioFacade.find(manualSeleccionado.getIdManualTarifario());//recargar el manual
        recargarFilasTablas();
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarInsumo').hide(); PF('wvTablaInsumosManual').clearFilters(); PF('wvTablaInsumosManual').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("IdFormManuales:IdTabView");
    }

    public void actualizarInsumo() {
        if (validacionCampoVacio(idInsumoEditando, "Insumo")) {
            return;
        }

        FacManualTarifarioInsumoPK llavePK = new FacManualTarifarioInsumoPK();
        llavePK.setIdManualTarifario(manualTarifarioFacade.find(Integer.parseInt(insumoManualSeleccionado.getColumna1())).getIdManualTarifario());
        llavePK.setIdInsumo(insumoFacade.find(Integer.parseInt(insumoManualSeleccionado.getColumna2())).getIdInsumo());
        FacManualTarifarioInsumo buscado = manualTarifarioInsumoFacade.find(llavePK);
        buscado.setActivo(activoInsumoEditando);
        buscado.setDescuento(descuentoInsumoEditando);
        buscado.setObservacion(observacionInsumoEditando);
        buscado.setValorInicial(valorInicialInsumoEditando);
        buscado.setValorFinal(valorFinalInsumoEditando);
        manualTarifarioInsumoFacade.edit(buscado);
        manualSeleccionado = manualTarifarioFacade.find(manualSeleccionado.getIdManualTarifario());//recargar el manual
        recargarFilasTablas();
        RequestContext.getCurrentInstance().execute("PF('dialogoEditandoInsumo').hide(); PF('wvTablaInsumosManual').clearFilters(); PF('wvTablaInsumosManual').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("IdFormManuales:IdTabView");
        //imprimirMensaje("Correcto", "El Insumo has sido adicionado al manual tarifario.", FacesMessage.SEVERITY_INFO);
    }

    public void quitarInsumo() {
        if (insumoManualSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún insumo de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoQuitarInsumo').show();");
    }

    public void confirmarQuitarInsumo() {
        if (insumoManualSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún insumo", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            FacManualTarifarioInsumoPK llavePK = new FacManualTarifarioInsumoPK();
            llavePK.setIdManualTarifario(manualTarifarioFacade.find(Integer.parseInt(insumoManualSeleccionado.getColumna1())).getIdManualTarifario());
            llavePK.setIdInsumo(insumoFacade.find(Integer.parseInt(insumoManualSeleccionado.getColumna2())).getIdInsumo());
            manualTarifarioInsumoFacade.remove(manualTarifarioInsumoFacade.find(llavePK));
            manualSeleccionado = manualTarifarioFacade.find(manualSeleccionado.getIdManualTarifario());
            recargarFilasTablas();
            RequestContext.getCurrentInstance().update("IdFormManuales:IdTabView");
            RequestContext.getCurrentInstance().execute("PF('dialogoQuitarInsumo').hide(); PF('wvTablaInsumosManual').clearFilters(); PF('wvTablaInsumosManual').getPaginator().setPage(0);");
            //imprimirMensaje("Correcto", "El insumo ha sido eliminado del manual tarifario.", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El insumo que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    //---------------------------------------------------
    //-------------FUNCIONES MEDICAMENTOS ---------------
    //--------------------------------------------------- 
    public void cambiaMedicamento() {
        activoMedicamento = true;
        valorInicialMedicamento = 0;
        valorFinalMedicamento = 0;
        descuentoMedicamento = 0;
        observacionMedicamento = "";
        calcularValoresMedicamento();
    }

    public void calcularValoresMedicamento() {
        if (validarNoVacio(idMedicamento)) {
            CfgMedicamento p = medicamentoFacade.find(Integer.parseInt(idMedicamento));
            valorInicialMedicamento = p.getValor();
            valorFinalMedicamento = valorInicialMedicamento - (valorInicialMedicamento * (descuentoMedicamento / 100));
        }
    }

//    public void calcularValoresMedicamentoEditando() {
//        if (validarNoVacio(idMedicamentoEditando)) {
//            CfgMedicamento p = medicamentoFacade.find(Integer.parseInt(idMedicamentoEditando));
//            valorInicialMedicamentoEditando = p.getValor();
//            valorFinalMedicamentoEditando = valorInicialMedicamentoEditando - (valorInicialMedicamentoEditando * (descuentoMedicamentoEditando / 100));
//        }
//    }
//
    public void cargarDialogoAgregarMedicamento() {
        idMedicamento = "";
        listaMedicamentos = medicamentoFacade.buscarNoEstanEnManual(manualSeleccionado.getIdManualTarifario());
        if (listaMedicamentos != null && !listaMedicamentos.isEmpty()) {
            idMedicamento = listaMedicamentos.get(0).getIdMedicamento().toString();
        }
        cambiaMedicamento();
        RequestContext.getCurrentInstance().update("IdFormDialogs:IdPanelAgregarMedicamento");
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarMedicamento').show();");

    }

    public void cargarDialogoEditarMedicamento() {
        if (medicamentoManualSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún medicamento de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }

        FacManualTarifarioMedicamentoPK llavePK = new FacManualTarifarioMedicamentoPK();
        llavePK.setIdManualTarifario(manualTarifarioFacade.find(Integer.parseInt(medicamentoManualSeleccionado.getColumna1())).getIdManualTarifario());
        llavePK.setIdMedicamento(medicamentoFacade.find(Integer.parseInt(medicamentoManualSeleccionado.getColumna2())).getIdMedicamento());
        FacManualTarifarioMedicamento buscado = manualTarifarioMedicamentoFacade.find(llavePK);

        nombreMedicamentoEditando = buscado.getCfgMedicamento().getNombreMedicamento();
        idMedicamentoEditando = buscado.getCfgMedicamento().getIdMedicamento().toString();
        activoMedicamentoEditando = buscado.getActivo();
        descuentoMedicamentoEditando = buscado.getDescuento();
        observacionMedicamentoEditando = buscado.getObservacion();
        valorInicialMedicamentoEditando = buscado.getValorInicial();
        valorFinalMedicamentoEditando = buscado.getValorFinal();

        RequestContext.getCurrentInstance().update("IdFormDialogs:IdPanelEditandoMedicamento");
        RequestContext.getCurrentInstance().execute("PF('dialogoEditandoMedicamento').show();");
    }

    public void agregarMedicamento() {
        if (validacionCampoVacio(idMedicamento, "Medicamento")) {
            return;
        }
        FacManualTarifarioMedicamentoPK llavePK = new FacManualTarifarioMedicamentoPK();
        llavePK.setIdManualTarifario(manualSeleccionado.getIdManualTarifario());
        llavePK.setIdMedicamento(Integer.parseInt(idMedicamento));
        FacManualTarifarioMedicamento buscado = manualTarifarioMedicamentoFacade.find(llavePK);
        if (buscado != null) {//validar que no exista
            imprimirMensaje("Error", "Este Manual tarifario ya tiene adicionado este medicamento.", FacesMessage.SEVERITY_ERROR);
            return;
        }
        FacManualTarifarioMedicamento nuevoManualTarifarioMedicamento = new FacManualTarifarioMedicamento();
        nuevoManualTarifarioMedicamento.setFacManualTarifarioMedicamentoPK(llavePK);
        nuevoManualTarifarioMedicamento.setActivo(activoMedicamento);
        nuevoManualTarifarioMedicamento.setDescuento(descuentoMedicamento);
        nuevoManualTarifarioMedicamento.setObservacion(observacionMedicamento);
        nuevoManualTarifarioMedicamento.setValorInicial(valorInicialMedicamento);
        nuevoManualTarifarioMedicamento.setValorFinal(valorFinalMedicamento);
        manualTarifarioMedicamentoFacade.create(nuevoManualTarifarioMedicamento);
        manualSeleccionado = manualTarifarioFacade.find(manualSeleccionado.getIdManualTarifario());//recargar el manual
        recargarFilasTablas();
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarMedicamento').hide(); PF('wvTablaMedicamentosManual').clearFilters(); PF('wvTablaMedicamentosManual').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("IdFormManuales:IdTabView");
    }

    public void actualizarMedicamento() {
        if (validacionCampoVacio(idMedicamentoEditando, "Medicamento")) {
            return;
        }

        FacManualTarifarioMedicamentoPK llavePK = new FacManualTarifarioMedicamentoPK();
        llavePK.setIdManualTarifario(manualTarifarioFacade.find(Integer.parseInt(medicamentoManualSeleccionado.getColumna1())).getIdManualTarifario());
        llavePK.setIdMedicamento(medicamentoFacade.find(Integer.parseInt(medicamentoManualSeleccionado.getColumna2())).getIdMedicamento());
        FacManualTarifarioMedicamento buscado = manualTarifarioMedicamentoFacade.find(llavePK);
        buscado.setActivo(activoMedicamentoEditando);
        buscado.setDescuento(descuentoMedicamentoEditando);
        buscado.setObservacion(observacionMedicamentoEditando);
        buscado.setValorInicial(valorInicialMedicamentoEditando);
        buscado.setValorFinal(valorFinalMedicamentoEditando);
        manualTarifarioMedicamentoFacade.edit(buscado);
        manualSeleccionado = manualTarifarioFacade.find(manualSeleccionado.getIdManualTarifario());//recargar el manual
        recargarFilasTablas();
        RequestContext.getCurrentInstance().execute("PF('dialogoEditandoMedicamento').hide(); PF('wvTablaMedicamentosManual').clearFilters(); PF('wvTablaMedicamentosManual').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("IdFormManuales:IdTabView");
        //imprimirMensaje("Correcto", "El Medicamento has sido adicionado al manual tarifario.", FacesMessage.SEVERITY_INFO);
    }

    public void quitarMedicamento() {
        if (medicamentoManualSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún medicamento de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoQuitarMedicamento').show();");
    }

    public void confirmarQuitarMedicamento() {
        if (medicamentoManualSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún medicamento", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            FacManualTarifarioMedicamentoPK llavePK = new FacManualTarifarioMedicamentoPK();
            llavePK.setIdManualTarifario(manualTarifarioFacade.find(Integer.parseInt(medicamentoManualSeleccionado.getColumna1())).getIdManualTarifario());
            llavePK.setIdMedicamento(medicamentoFacade.find(Integer.parseInt(medicamentoManualSeleccionado.getColumna2())).getIdMedicamento());
            manualTarifarioMedicamentoFacade.remove(manualTarifarioMedicamentoFacade.find(llavePK));
            manualSeleccionado = manualTarifarioFacade.find(manualSeleccionado.getIdManualTarifario());
            recargarFilasTablas();
            RequestContext.getCurrentInstance().update("IdFormManuales:IdTabView");
            RequestContext.getCurrentInstance().execute("PF('dialogoQuitarMedicamento').hide(); PF('wvTablaMedicamentosManual').clearFilters(); PF('wvTablaMedicamentosManual').getPaginator().setPage(0);");
            //imprimirMensaje("Correcto", "El medicamento ha sido eliminado del manual tarifario.", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El medicamento que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    //---------------------------------------------------
    //-------------FUNCIONES PAQUETES -------------------
    //--------------------------------------------------- 
    public void cambiaPaquete() {
        activoPaquete = true;
        valorInicialPaquete = 0;
        valorFinalPaquete = 0;
        descuentoPaquete = 0;
        observacionPaquete = "";
        calcularValoresPaquete();
    }

    public void calcularValoresPaquete() {
        if (validarNoVacio(idPaquete)) {
            FacPaquete p = paqueteFacade.find(Integer.parseInt(idPaquete));
            valorInicialPaquete = p.getValorPaquete();
            valorFinalPaquete = valorInicialPaquete - (valorInicialPaquete * (descuentoPaquete / 100));
        }
    }

//    public void calcularValoresPaqueteEditando() {
//        if (validarNoVacio(idPaqueteEditando)) {
//            FacPaquete p = paqueteFacade.find(Integer.parseInt(idPaqueteEditando));
//            valorInicialPaqueteEditando = p.getValorPaquete();
//            valorFinalPaqueteEditando = valorInicialPaqueteEditando - (valorInicialPaqueteEditando * (descuentoPaqueteEditando / 100));            
//        }
//    }
//    
    public void cargarDialogoAgregarPaquete() {
        idPaquete = "";
        listaPaquetes = paqueteFacade.buscarNoEstanEnManual(manualSeleccionado.getIdManualTarifario());
        if (listaPaquetes != null && !listaPaquetes.isEmpty()) {
            idPaquete = listaPaquetes.get(0).getIdPaquete().toString();
        }
        cambiaPaquete();
        RequestContext.getCurrentInstance().update("IdFormDialogs:IdPanelAgregarPaquete");
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarPaquete').show();");
    }

    public void cargarDialogoEditarPaquete() {
        if (paqueteManualSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún paquete de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }

        FacManualTarifarioPaquetePK llavePK = new FacManualTarifarioPaquetePK();
        llavePK.setIdManualTarifario(manualTarifarioFacade.find(Integer.parseInt(paqueteManualSeleccionado.getColumna1())).getIdManualTarifario());
        llavePK.setIdPaquete(paqueteFacade.find(Integer.parseInt(paqueteManualSeleccionado.getColumna2())).getIdPaquete());
        FacManualTarifarioPaquete buscado = manualTarifarioPaqueteFacade.find(llavePK);

        nombrePaqueteEditando = buscado.getFacPaquete().getNombrePaquete();
        idPaqueteEditando = buscado.getFacPaquete().getIdPaquete().toString();
        activoPaqueteEditando = buscado.getActivo();
        descuentoPaqueteEditando = buscado.getDescuento();
        observacionPaqueteEditando = buscado.getObservacion();
        valorInicialPaqueteEditando = buscado.getValorInicial();
        valorFinalPaqueteEditando = buscado.getValorFinal();
        //System.out.println("valorFinalPaqueteEditando " + valorFinalPaqueteEditando);
        RequestContext.getCurrentInstance().update("IdFormDialogs:IdPanelEditandoPaquete");
        RequestContext.getCurrentInstance().execute("PF('dialogoEditandoPaquete').show();");
    }

    public void agregarPaquete() {
        if (validacionCampoVacio(idPaquete, "Paquete")) {
            return;
        }
        FacManualTarifarioPaquetePK llavePK = new FacManualTarifarioPaquetePK();
        llavePK.setIdManualTarifario(manualSeleccionado.getIdManualTarifario());
        llavePK.setIdPaquete(Integer.parseInt(idPaquete));
        FacManualTarifarioPaquete buscado = manualTarifarioPaqueteFacade.find(llavePK);
        if (buscado != null) {//validar que no exista
            imprimirMensaje("Error", "Este Manual tarifario ya tiene adicionado este paquete.", FacesMessage.SEVERITY_ERROR);
            return;
        }
        FacManualTarifarioPaquete nuevoManualTarifarioPaquete = new FacManualTarifarioPaquete();
        nuevoManualTarifarioPaquete.setFacManualTarifarioPaquetePK(llavePK);
        nuevoManualTarifarioPaquete.setActivo(activoPaquete);
        nuevoManualTarifarioPaquete.setDescuento(descuentoPaquete);
        nuevoManualTarifarioPaquete.setObservacion(observacionPaquete);
        nuevoManualTarifarioPaquete.setValorInicial(valorInicialPaquete);
        nuevoManualTarifarioPaquete.setValorFinal(valorFinalPaquete);
        manualTarifarioPaqueteFacade.create(nuevoManualTarifarioPaquete);
        manualSeleccionado = manualTarifarioFacade.find(manualSeleccionado.getIdManualTarifario());//recargar el manual
        recargarFilasTablas();
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarPaquete').hide(); PF('wvTablaPaquetesManual').clearFilters(); PF('wvTablaPaquetesManual').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("IdFormManuales:IdTabView");
    }

    public void actualizarPaquete() {
        if (validacionCampoVacio(idPaqueteEditando, "Paquete")) {
            return;
        }

        FacManualTarifarioPaquetePK llavePK = new FacManualTarifarioPaquetePK();
        llavePK.setIdManualTarifario(manualTarifarioFacade.find(Integer.parseInt(paqueteManualSeleccionado.getColumna1())).getIdManualTarifario());
        llavePK.setIdPaquete(paqueteFacade.find(Integer.parseInt(paqueteManualSeleccionado.getColumna2())).getIdPaquete());
        FacManualTarifarioPaquete buscado = manualTarifarioPaqueteFacade.find(llavePK);
        buscado.setActivo(activoPaqueteEditando);
        buscado.setDescuento(descuentoPaqueteEditando);
        buscado.setObservacion(observacionPaqueteEditando);
        buscado.setValorInicial(valorInicialPaqueteEditando);
        buscado.setValorFinal(valorFinalPaqueteEditando);
        manualTarifarioPaqueteFacade.edit(buscado);
        manualSeleccionado = manualTarifarioFacade.find(manualSeleccionado.getIdManualTarifario());//recargar el manual
        recargarFilasTablas();
        RequestContext.getCurrentInstance().execute("PF('dialogoEditandoPaquete').hide(); PF('wvTablaPaquetesManual').clearFilters(); PF('wvTablaPaquetesManual').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("IdFormManuales:IdTabView");
        //imprimirMensaje("Correcto", "El Paquete has sido adicionado al manual tarifario.", FacesMessage.SEVERITY_INFO);
    }

    public void quitarPaquete() {
        if (paqueteManualSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún paquete de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoQuitarPaquete').show();");
    }

    public void confirmarQuitarPaquete() {
        if (paqueteManualSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún paquete", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            FacManualTarifarioPaquetePK llavePK = new FacManualTarifarioPaquetePK();
            llavePK.setIdManualTarifario(manualTarifarioFacade.find(Integer.parseInt(paqueteManualSeleccionado.getColumna1())).getIdManualTarifario());
            llavePK.setIdPaquete(paqueteFacade.find(Integer.parseInt(paqueteManualSeleccionado.getColumna2())).getIdPaquete());
            manualTarifarioPaqueteFacade.remove(manualTarifarioPaqueteFacade.find(llavePK));
            manualSeleccionado = manualTarifarioFacade.find(manualSeleccionado.getIdManualTarifario());
            recargarFilasTablas();
            RequestContext.getCurrentInstance().update("IdFormManuales:IdTabView");
            RequestContext.getCurrentInstance().execute("PF('dialogoQuitarPaquete').hide(); PF('wvTablaPaquetesManual').clearFilters(); PF('wvTablaPaquetesManual').getPaginator().setPage(0);");
            //imprimirMensaje("Correcto", "El medicamento ha sido eliminado del manual tarifario.", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El medicamento que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------
    public FacManualTarifario getManualSeleccionado() {
        return manualSeleccionado;
    }

    public void setManualSeleccionado(FacManualTarifario manualSeleccionado) {
        this.manualSeleccionado = manualSeleccionado;
    }

    public FacManualTarifario getManualSeleccionadoTabla() {
        return manualSeleccionadoTabla;
    }

    public void setManualSeleccionadoTabla(FacManualTarifario manualSeleccionadoTabla) {
        this.manualSeleccionadoTabla = manualSeleccionadoTabla;
    }

    public List<FacManualTarifario> getListaManuales() {
        return listaManuales;
    }

    public void setListaManuales(List<FacManualTarifario> listaManuales) {
        this.listaManuales = listaManuales;
    }

    public String getTituloTabManual() {
        return tituloTabManual;
    }

    public void setTituloTabManual(String tituloTabManual) {
        this.tituloTabManual = tituloTabManual;
    }

    public String getCodigoManual() {
        return codigoManual;
    }

    public void setCodigoManual(String codigoManual) {
        this.codigoManual = codigoManual;
    }

    public String getNombreManual() {
        return nombreManual;
    }

    public void setNombreManual(String nombreManual) {
        this.nombreManual = nombreManual;
    }

    public List<FilaDataTable> getListaServiciosManual() {
        return listaServiciosManual;
    }

    public void setListaServiciosManual(List<FilaDataTable> listaServiciosManual) {
        this.listaServiciosManual = listaServiciosManual;
    }

    public List<FilaDataTable> getListaInsumosManual() {
        return listaInsumosManual;
    }

    public void setListaInsumosManual(List<FilaDataTable> listaInsumosManual) {
        this.listaInsumosManual = listaInsumosManual;
    }

    public List<FilaDataTable> getListaMedicamentosManual() {
        return listaMedicamentosManual;
    }

    public void setListaMedicamentosManual(List<FilaDataTable> listaMedicamentosManual) {
        this.listaMedicamentosManual = listaMedicamentosManual;
    }

    public List<FilaDataTable> getListaPaquetesManual() {
        return listaPaquetesManual;
    }

    public void setListaPaquetesManual(List<FilaDataTable> listaPaquetesManual) {
        this.listaPaquetesManual = listaPaquetesManual;
    }

    public FilaDataTable getServicioManualSeleccionado() {
        return servicioManualSeleccionado;
    }

    public void setServicioManualSeleccionado(FilaDataTable servicioManualSeleccionado) {
        this.servicioManualSeleccionado = servicioManualSeleccionado;
    }

    public FilaDataTable getInsumoManualSeleccionado() {
        return insumoManualSeleccionado;
    }

    public void setInsumoManualSeleccionado(FilaDataTable insumoManualSeleccionado) {
        this.insumoManualSeleccionado = insumoManualSeleccionado;
    }

    public FilaDataTable getMedicamentoManualSeleccionado() {
        return medicamentoManualSeleccionado;
    }

    public void setMedicamentoManualSeleccionado(FilaDataTable medicamentoManualSeleccionado) {
        this.medicamentoManualSeleccionado = medicamentoManualSeleccionado;
    }

    public FilaDataTable getPaqueteManualSeleccionado() {
        return paqueteManualSeleccionado;
    }

    public void setPaqueteManualSeleccionado(FilaDataTable paqueteManualSeleccionado) {
        this.paqueteManualSeleccionado = paqueteManualSeleccionado;
    }

    public String getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(String idServicio) {
        this.idServicio = idServicio;
    }

    public int getMetaCumplimiento() {
        return metaCumplimiento;
    }

    public void setMetaCumplimiento(int metaCumplimiento) {
        this.metaCumplimiento = metaCumplimiento;
    }

    public int getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(int periodicidad) {
        this.periodicidad = periodicidad;
    }

    public double getHonorarioMedico() {
        return honorarioMedico;
    }

    public void setHonorarioMedico(double honorarioMedico) {
        this.honorarioMedico = honorarioMedico;
    }

    public String getTipoTarifa() {
        return tipoTarifa;
    }

    public void setTipoTarifa(String tipoTarifa) {
        this.tipoTarifa = tipoTarifa;
    }

    public String getUnidadValor() {
        return unidadValor;
    }

    public void setUnidadValor(String unidadValor) {
        this.unidadValor = unidadValor;
    }

    public double getDescuentoServicio() {
        return descuentoServicio;
    }

    public void setDescuentoServicio(double descuentoServicio) {
        this.descuentoServicio = descuentoServicio;
    }

    public String getObservacionServicio() {
        return observacionServicio;
    }

    public void setObservacionServicio(String observacionServicio) {
        this.observacionServicio = observacionServicio;
    }

    public boolean isActivoServicio() {
        return activoServicio;
    }

    public void setActivoServicio(boolean activoServicio) {
        this.activoServicio = activoServicio;
    }

    public double getDescuentoInsumo() {
        return descuentoInsumo;
    }

    public void setDescuentoInsumo(double descuentoInsumo) {
        this.descuentoInsumo = descuentoInsumo;
    }

    public String getObservacionInsumo() {
        return observacionInsumo;
    }

    public void setObservacionInsumo(String observacionInsumo) {
        this.observacionInsumo = observacionInsumo;
    }

    public boolean isActivoInsumo() {
        return activoInsumo;
    }

    public void setActivoInsumo(boolean activoInsumo) {
        this.activoInsumo = activoInsumo;
    }

    public double getDescuentoMedicamento() {
        return descuentoMedicamento;
    }

    public void setDescuentoMedicamento(double descuentoMedicamento) {
        this.descuentoMedicamento = descuentoMedicamento;
    }

    public String getObservacionMedicamento() {
        return observacionMedicamento;
    }

    public void setObservacionMedicamento(String observacionMedicamento) {
        this.observacionMedicamento = observacionMedicamento;
    }

    public boolean isActivoMedicamento() {
        return activoMedicamento;
    }

    public void setActivoMedicamento(boolean activoMedicamento) {
        this.activoMedicamento = activoMedicamento;
    }

    public double getDescuentoPaquete() {
        return descuentoPaquete;
    }

    public void setDescuentoPaquete(double descuentoPaquete) {
        this.descuentoPaquete = descuentoPaquete;
    }

    public String getObservacionPaquete() {
        return observacionPaquete;
    }

    public void setObservacionPaquete(String observacionPaquete) {
        this.observacionPaquete = observacionPaquete;
    }

    public boolean isActivoPaquete() {
        return activoPaquete;
    }

    public void setActivoPaquete(boolean activoPaquete) {
        this.activoPaquete = activoPaquete;
    }

    public String getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(String idInsumo) {
        this.idInsumo = idInsumo;
    }

    public String getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(String idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(String idPaquete) {
        this.idPaquete = idPaquete;
    }

    public String getTituloTabPaquetes() {
        return tituloTabPaquetes;
    }

    public void setTituloTabPaquetes(String tituloTabPaquetes) {
        this.tituloTabPaquetes = tituloTabPaquetes;
    }

    public String getTituloTabInsumos() {
        return tituloTabInsumos;
    }

    public void setTituloTabInsumos(String tituloTabInsumos) {
        this.tituloTabInsumos = tituloTabInsumos;
    }

    public String getTituloTabMedicamentos() {
        return tituloTabMedicamentos;
    }

    public void setTituloTabMedicamentos(String tituloTabMedicamentos) {
        this.tituloTabMedicamentos = tituloTabMedicamentos;
    }

    public String getTituloTabServicios() {
        return tituloTabServicios;
    }

    public void setTituloTabServicios(String tituloTabServicios) {
        this.tituloTabServicios = tituloTabServicios;
    }

    public List<FilaDataTable> getListaServiciosManualFiltro() {
        return listaServiciosManualFiltro;
    }

    public void setListaServiciosManualFiltro(List<FilaDataTable> listaServiciosManualFiltro) {
        this.listaServiciosManualFiltro = listaServiciosManualFiltro;
    }

    public List<FilaDataTable> getListaInsumosManualFiltro() {
        return listaInsumosManualFiltro;
    }

    public void setListaInsumosManualFiltro(List<FilaDataTable> listaInsumosManualFiltro) {
        this.listaInsumosManualFiltro = listaInsumosManualFiltro;
    }

    public List<FilaDataTable> getListaMedicamentosManualFiltro() {
        return listaMedicamentosManualFiltro;
    }

    public void setListaMedicamentosManualFiltro(List<FilaDataTable> listaMedicamentosManualFiltro) {
        this.listaMedicamentosManualFiltro = listaMedicamentosManualFiltro;
    }

    public List<FilaDataTable> getListaPaquetesManualFiltro() {
        return listaPaquetesManualFiltro;
    }

    public void setListaPaquetesManualFiltro(List<FilaDataTable> listaPaquetesManualFiltro) {
        this.listaPaquetesManualFiltro = listaPaquetesManualFiltro;
    }

    public double getValorFinalServicio() {
        return valorFinalServicio;
    }

    public void setValorFinalServicio(double valorFinalServicio) {
        this.valorFinalServicio = valorFinalServicio;
    }

    public double getValorInicialServicio() {
        return valorInicialServicio;
    }

    public void setValorInicialServicio(double valorInicialServicio) {
        this.valorInicialServicio = valorInicialServicio;
    }

    public double getValorFinalInsumo() {
        return valorFinalInsumo;
    }

    public void setValorFinalInsumo(double valorFinalInsumo) {
        this.valorFinalInsumo = valorFinalInsumo;
    }

    public double getValorInicialInsumo() {
        return valorInicialInsumo;
    }

    public void setValorInicialInsumo(double valorInicialInsumo) {
        this.valorInicialInsumo = valorInicialInsumo;
    }

    public double getValorFinalMedicamento() {
        return valorFinalMedicamento;
    }

    public void setValorFinalMedicamento(double valorFinalMedicamento) {
        this.valorFinalMedicamento = valorFinalMedicamento;
    }

    public double getValorInicialMedicamento() {
        return valorInicialMedicamento;
    }

    public void setValorInicialMedicamento(double valorInicialMedicamento) {
        this.valorInicialMedicamento = valorInicialMedicamento;
    }

    public double getValorFinalPaquete() {
        return valorFinalPaquete;
    }

    public void setValorFinalPaquete(double valorFinalPaquete) {
        this.valorFinalPaquete = valorFinalPaquete;
    }

    public double getValorInicialPaquete() {
        return valorInicialPaquete;
    }

    public void setValorInicialPaquete(double valorInicialPaquete) {
        this.valorInicialPaquete = valorInicialPaquete;
    }

    public List<FacUnidadValor> getListaUnidadesValor() {
        return listaUnidadesValor;
    }

    public void setListaUnidadesValor(List<FacUnidadValor> listaUnidadesValor) {
        this.listaUnidadesValor = listaUnidadesValor;
    }

    public List<FacServicio> getListaServicios() {
        return listaServicios;
    }

    public void setListaServicios(List<FacServicio> listaServicios) {
        this.listaServicios = listaServicios;
    }

    public List<CfgInsumo> getListaInsumos() {
        return listaInsumos;
    }

    public void setListaInsumos(List<CfgInsumo> listaInsumos) {
        this.listaInsumos = listaInsumos;
    }

    public List<CfgMedicamento> getListaMedicamentos() {
        return listaMedicamentos;
    }

    public void setListaMedicamentos(List<CfgMedicamento> listaMedicamentos) {
        this.listaMedicamentos = listaMedicamentos;
    }

    public List<FacPaquete> getListaPaquetes() {
        return listaPaquetes;
    }

    public void setListaPaquetes(List<FacPaquete> listaPaquetes) {
        this.listaPaquetes = listaPaquetes;
    }

    public List<SelectItem> getListaTipoTarifa() {
        return listaTipoTarifa;
    }

    public void setListaTipoTarifa(List<SelectItem> listaTipoTarifa) {
        this.listaTipoTarifa = listaTipoTarifa;
    }

    public boolean isMostrarTabView() {
        return mostrarTabView;
    }

    public void setMostrarTabView(boolean mostrarTabView) {
        this.mostrarTabView = mostrarTabView;
    }

    public boolean isDisabledUnidadValor() {
        return disabledUnidadValor;
    }

    public void setDisabledUnidadValor(boolean disabledUnidadValor) {
        this.disabledUnidadValor = disabledUnidadValor;
    }

    public String getNombreServicioEditando() {
        return nombreServicioEditando;
    }

    public void setNombreServicioEditando(String nombreServicioEditando) {
        this.nombreServicioEditando = nombreServicioEditando;
    }

    public double getValorFinalServicioEditando() {
        return valorFinalServicioEditando;
    }

    public void setValorFinalServicioEditando(double valorFinalServicioEditando) {
        this.valorFinalServicioEditando = valorFinalServicioEditando;
    }

    public double getValorInicialServicioEditando() {
        return valorInicialServicioEditando;
    }

    public void setValorInicialServicioEditando(double valorInicialServicioEditando) {
        this.valorInicialServicioEditando = valorInicialServicioEditando;
    }

    public int getMetaCumplimientoEditando() {
        return metaCumplimientoEditando;
    }

    public void setMetaCumplimientoEditando(int metaCumplimientoEditando) {
        this.metaCumplimientoEditando = metaCumplimientoEditando;
    }

    public int getPeriodicidadEditando() {
        return periodicidadEditando;
    }

    public void setPeriodicidadEditando(int periodicidadEditando) {
        this.periodicidadEditando = periodicidadEditando;
    }

    public double getDescuentoServicioEditando() {
        return descuentoServicioEditando;
    }

    public void setDescuentoServicioEditando(double descuentoServicioEditando) {
        this.descuentoServicioEditando = descuentoServicioEditando;
    }

    public double getHonorarioMedicoEditando() {
        return honorarioMedicoEditando;
    }

    public void setHonorarioMedicoEditando(double honorarioMedicoEditando) {
        this.honorarioMedicoEditando = honorarioMedicoEditando;
    }

    public String getObservacionServicioEditando() {
        return observacionServicioEditando;
    }

    public void setObservacionServicioEditando(String observacionServicioEditando) {
        this.observacionServicioEditando = observacionServicioEditando;
    }

    public boolean isActivoServicioEditando() {
        return activoServicioEditando;
    }

    public void setActivoServicioEditando(boolean activoServicioEditando) {
        this.activoServicioEditando = activoServicioEditando;
    }

    public String getTipoTarifaEditando() {
        return tipoTarifaEditando;
    }

    public void setTipoTarifaEditando(String tipoTarifaEditando) {
        this.tipoTarifaEditando = tipoTarifaEditando;
    }

    public String getUnidadValorEditando() {
        return unidadValorEditando;
    }

    public void setUnidadValorEditando(String unidadValorEditando) {
        this.unidadValorEditando = unidadValorEditando;
    }

    public String getIdServicioEditando() {
        return idServicioEditando;
    }

    public void setIdServicioEditando(String idServicioEditando) {
        this.idServicioEditando = idServicioEditando;
    }

    public String getIdInsumoEditando() {
        return idInsumoEditando;
    }

    public void setIdInsumoEditando(String idInsumoEditando) {
        this.idInsumoEditando = idInsumoEditando;
    }

    public String getNombreInsumoEditando() {
        return nombreInsumoEditando;
    }

    public void setNombreInsumoEditando(String nombreInsumoEditando) {
        this.nombreInsumoEditando = nombreInsumoEditando;
    }

    public double getValorFinalInsumoEditando() {
        return valorFinalInsumoEditando;
    }

    public void setValorFinalInsumoEditando(double valorFinalInsumoEditando) {
        this.valorFinalInsumoEditando = valorFinalInsumoEditando;
    }

    public double getValorInicialInsumoEditando() {
        return valorInicialInsumoEditando;
    }

    public void setValorInicialInsumoEditando(double valorInicialInsumoEditando) {
        this.valorInicialInsumoEditando = valorInicialInsumoEditando;
    }

    public double getDescuentoInsumoEditando() {
        return descuentoInsumoEditando;
    }

    public void setDescuentoInsumoEditando(double descuentoInsumoEditando) {
        this.descuentoInsumoEditando = descuentoInsumoEditando;
    }

    public String getObservacionInsumoEditando() {
        return observacionInsumoEditando;
    }

    public void setObservacionInsumoEditando(String observacionInsumoEditando) {
        this.observacionInsumoEditando = observacionInsumoEditando;
    }

    public boolean isActivoInsumoEditando() {
        return activoInsumoEditando;
    }

    public void setActivoInsumoEditando(boolean activoInsumoEditando) {
        this.activoInsumoEditando = activoInsumoEditando;
    }

    public String getIdMedicamentoEditando() {
        return idMedicamentoEditando;
    }

    public void setIdMedicamentoEditando(String idMedicamentoEditando) {
        this.idMedicamentoEditando = idMedicamentoEditando;
    }

    public String getNombreMedicamentoEditando() {
        return nombreMedicamentoEditando;
    }

    public void setNombreMedicamentoEditando(String nombreMedicamentoEditando) {
        this.nombreMedicamentoEditando = nombreMedicamentoEditando;
    }

    public double getValorFinalMedicamentoEditando() {
        return valorFinalMedicamentoEditando;
    }

    public void setValorFinalMedicamentoEditando(double valorFinalMedicamentoEditando) {
        this.valorFinalMedicamentoEditando = valorFinalMedicamentoEditando;
    }

    public double getValorInicialMedicamentoEditando() {
        return valorInicialMedicamentoEditando;
    }

    public void setValorInicialMedicamentoEditando(double valorInicialMedicamentoEditando) {
        this.valorInicialMedicamentoEditando = valorInicialMedicamentoEditando;
    }

    public double getDescuentoMedicamentoEditando() {
        return descuentoMedicamentoEditando;
    }

    public void setDescuentoMedicamentoEditando(double descuentoMedicamentoEditando) {
        this.descuentoMedicamentoEditando = descuentoMedicamentoEditando;
    }

    public String getObservacionMedicamentoEditando() {
        return observacionMedicamentoEditando;
    }

    public void setObservacionMedicamentoEditando(String observacionMedicamentoEditando) {
        this.observacionMedicamentoEditando = observacionMedicamentoEditando;
    }

    public boolean isActivoMedicamentoEditando() {
        return activoMedicamentoEditando;
    }

    public void setActivoMedicamentoEditando(boolean activoMedicamentoEditando) {
        this.activoMedicamentoEditando = activoMedicamentoEditando;
    }

    public String getIdPaqueteEditando() {
        return idPaqueteEditando;
    }

    public void setIdPaqueteEditando(String idPaqueteEditando) {
        this.idPaqueteEditando = idPaqueteEditando;
    }

    public String getNombrePaqueteEditando() {
        return nombrePaqueteEditando;
    }

    public void setNombrePaqueteEditando(String nombrePaqueteEditando) {
        this.nombrePaqueteEditando = nombrePaqueteEditando;
    }

    public double getValorFinalPaqueteEditando() {
        return valorFinalPaqueteEditando;
    }

    public void setValorFinalPaqueteEditando(double valorFinalPaqueteEditando) {
        this.valorFinalPaqueteEditando = valorFinalPaqueteEditando;
    }

    public double getValorInicialPaqueteEditando() {
        return valorInicialPaqueteEditando;
    }

    public void setValorInicialPaqueteEditando(double valorInicialPaqueteEditando) {
        this.valorInicialPaqueteEditando = valorInicialPaqueteEditando;
    }

    public double getDescuentoPaqueteEditando() {
        return descuentoPaqueteEditando;
    }

    public void setDescuentoPaqueteEditando(double descuentoPaqueteEditando) {
        this.descuentoPaqueteEditando = descuentoPaqueteEditando;
    }

    public String getObservacionPaqueteEditando() {
        return observacionPaqueteEditando;
    }

    public void setObservacionPaqueteEditando(String observacionPaqueteEditando) {
        this.observacionPaqueteEditando = observacionPaqueteEditando;
    }

    public boolean isActivoPaqueteEditando() {
        return activoPaqueteEditando;
    }

    public void setActivoPaqueteEditando(boolean activoPaqueteEditando) {
        this.activoPaqueteEditando = activoPaqueteEditando;
    }

}
