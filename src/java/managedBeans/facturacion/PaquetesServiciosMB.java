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
import modelo.entidades.FacPaqueteInsumo;
import modelo.entidades.FacPaqueteMedicamento;
import modelo.entidades.FacPaqueteServicio;
import modelo.entidades.FacPaqueteServicioPK;
import modelo.entidades.FacPaquete;
import modelo.entidades.FacPaqueteInsumoPK;
import modelo.entidades.FacPaqueteMedicamentoPK;
import modelo.entidades.FacServicio;
import modelo.entidades.FacUnidadValor;
import modelo.fachadas.CfgInsumoFacade;
import modelo.fachadas.CfgMedicamentoFacade;
import modelo.fachadas.FacPaqueteFacade;
import modelo.fachadas.FacPaqueteInsumoFacade;
import modelo.fachadas.FacPaqueteMedicamentoFacade;
import modelo.fachadas.FacPaqueteServicioFacade;
import modelo.fachadas.FacServicioFacade;
import modelo.fachadas.FacUnidadValorFacade;
import org.primefaces.context.RequestContext;

/**
 *
 * @author santos
 */
@ManagedBean(name = "paquetesServiciosMB")
@SessionScoped
public class PaquetesServiciosMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    FacPaqueteFacade paqueteFacade;
    @EJB
    FacPaqueteServicioFacade paqueteServicioFacade;
    @EJB
    FacPaqueteInsumoFacade paqueteInsumoFacade;
    @EJB
    FacPaqueteMedicamentoFacade paqueteMedicamentoFacade;
    @EJB
    FacServicioFacade servicioFacade;
    @EJB
    CfgMedicamentoFacade medicamentoFacade;
    @EJB
    CfgInsumoFacade insumoFacade;
    @EJB
    FacUnidadValorFacade unidadValorFacade;

    //---------------------------------------------------
    //-----------------ENTIDADES ------------------------
    //---------------------------------------------------
    private List<FacUnidadValor> listaUnidadesValor;
    private FacPaquete paqueteSeleccionado;
    private FacPaquete paqueteSeleccionadoTabla;
    private List<FacPaquete> listaPaquetes;

    private List<FacServicio> listaServicios;
    private List<CfgInsumo> listaInsumos;
    private List<CfgMedicamento> listaMedicamentos;

    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------
    //private boolean hayPaqueteSeleccionado = false;//se usa por que hay componentes que no se renderizan desde el principio
    private List<FilaDataTable> listaServiciosPaquete = new ArrayList<>();
    private List<FilaDataTable> listaInsumosPaquete = new ArrayList<>();
    private List<FilaDataTable> listaMedicamentosPaquete = new ArrayList<>();
    private List<FilaDataTable> listaServiciosPaqueteFiltro = new ArrayList<>();
    private List<FilaDataTable> listaInsumosPaqueteFiltro = new ArrayList<>();
    private List<FilaDataTable> listaMedicamentosPaqueteFiltro = new ArrayList<>();

    private FilaDataTable servicioPaqueteSeleccionado;
    private FilaDataTable insumoPaqueteSeleccionado;
    private FilaDataTable medicamentoPaqueteSeleccionado;

    private String tituloTabPaquete = "Nuevo Paquete";
    private String tituloTabInsumos = "Insumos (0)";
    private String tituloTabMedicamentos = "Medicamentos (0)";
    private String tituloTabServicios = "Servicios (0)";
    private String codigoPaquete = "";
    private String nombrePaquete = "";
    private double valorPaquete = 0;//valor ingresado por el usuario
    private double valorCalculadoPaquete = 0;//valor calculado dependiendo de los items del 
    //nuevo servicio a adicionar
    private String idServicio = "";
    private int metaCumplimiento = 0;
    private int periodicidad = 0;
    private double descuentoServicio = 0;
    private double honorarioMedico = 0;
    private String observacionServicio = "";
    private boolean activoServicio = true;
    //private String tipoValor = "";
    private int cantidadServicios = 1;
    private double valorFinalServicio = 0;
    private double valorUnitarioServicio = 0;
    private List<SelectItem> listaTipoTarifa;
    private String tipoTarifa = "";
    private String unidadValor = "";

    //nuevo insumo
    private String idInsumo = "";
    private double descuentoInsumo = 0;
    private String observacionInsumo = "";
    private boolean activoInsumo = true;
    private int cantidadInsumos = 1;
    private double valorFinalInsumo = 0;
    private double valorUnitarioInsumo = 0;
    //nuevo medicamento
    private String idMedicamento = "";
    private double valorFinalMedicamento = 0;
    private double descuentoMedicamento = 0;
    private String observacionMedicamento = "";
    private boolean activoMedicamento = true;
    private int cantidadMedicamentos = 1;
    private double valorUnitarioMedicamento = 0;

    //---------------------------------------------------
    //------------- FUNCIONES INICIALES  ----------------
    //---------------------------------------------------      
    public PaquetesServiciosMB() {
    }

    //---------------------------------------------------
    //-------------FUNCIONES PAQUETES -------------------
    //--------------------------------------------------- 
    public void cargarPaqueteDesdeTab(String idAdministradora) {//cargar contrato desde el tab externo de Administradoras
    }

    public void limpiarFormularioPaquetes() {
        listaPaquetes = paqueteFacade.buscarOrdenado();
        paqueteSeleccionado = null;
        tituloTabPaquete = "Nuevo Paquete Tarifario";
        tituloTabInsumos = "Insumos (0)";
        tituloTabMedicamentos = "Medicamentos (0)";
        tituloTabServicios = "Servicios (0)";
        codigoPaquete = "";
        nombrePaquete = "";
        valorPaquete = 0;
        valorCalculadoPaquete = 0;
    }

    public void buscarPaquete() {
        listaPaquetes = paqueteFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().execute("PF('wvTablaPaquetes').clearFilters(); PF('wvTablaPaquetes').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().execute("PF('dialogoBuscarPaquetes').show();");
    }

    public void cargarPaquete() {//click cobre editar paquete
        if (paqueteSeleccionadoTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún paquete de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        limpiarFormularioPaquetes();
        paqueteSeleccionado = paqueteFacade.find(paqueteSeleccionadoTabla.getIdPaquete());
        recargarFilasTablas();
        codigoPaquete = paqueteSeleccionado.getCodigoPaquete();
        nombrePaquete = paqueteSeleccionado.getNombrePaquete();
        valorPaquete = paqueteSeleccionado.getValorPaquete();
        valorCalculadoPaquete = paqueteSeleccionado.getValorCalculado();
        tituloTabPaquete = "Datos Paquete: " + nombrePaquete;
        RequestContext.getCurrentInstance().execute("PF('dialogoBuscarPaquetes').hide();");
        RequestContext.getCurrentInstance().execute("PF('wvTabView').select(0);");//seleccionar primer tab                                
        RequestContext.getCurrentInstance().update("IdFormPaquetes:IdTabView");
    }

    public void eliminarPaquete() {
        if (paqueteSeleccionado == null) {
            imprimirMensaje("Error", "No se ha cargado ningún paquete de Servicios", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoEliminarPaquete').show();");
    }

    public void confirmarEliminarPaquete() {
        if (paqueteSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún paquete de servicios", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            paqueteFacade.remove(paqueteSeleccionado);
            listaPaquetes = paqueteFacade.buscarOrdenado();
            limpiarFormularioPaquetes();
            recargarFilasTablas();
            RequestContext.getCurrentInstance().update("IdFormPaquetes");
            imprimirMensaje("Correcto", "El Paquete de Servicios ha sido eliminado", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El Paquete de Servicios que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void guardarPaquete() {
        if (validacionCampoVacio(codigoPaquete, "Código paquete")) {
            return;
        }
        if (validacionCampoVacio(nombrePaquete, "Nombre paquete")) {
            return;
        }
        if (paqueteSeleccionado == null) {
            guardarNuevoPaquete();
        } else {
            actualizarPaqueteExistente();
        }
    }

    private void guardarNuevoPaquete() {
        FacPaquete nuevoPaquete = new FacPaquete();
        nuevoPaquete.setCodigoPaquete(codigoPaquete);
        nuevoPaquete.setNombrePaquete(nombrePaquete);
        nuevoPaquete.setValorPaquete(valorPaquete);
        nuevoPaquete.setValorCalculado(valorCalculadoPaquete);
        paqueteFacade.create(nuevoPaquete);
        limpiarFormularioPaquetes();
        recargarFilasTablas();
        listaPaquetes = paqueteFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().update("IdFormPaquetes");
        imprimirMensaje("Correcto", "El Paquete de servicios ha sido creado.", FacesMessage.SEVERITY_INFO);
    }

    private void actualizarPaqueteExistente() {//realiza la actualizacion del consultorio        
        paqueteSeleccionado.setCodigoPaquete(codigoPaquete);
        paqueteSeleccionado.setNombrePaquete(nombrePaquete);
        paqueteSeleccionado.setValorPaquete(valorPaquete);
        paqueteSeleccionado.setValorCalculado(valorCalculadoPaquete);
        paqueteFacade.edit(paqueteSeleccionado);
        limpiarFormularioPaquetes();
        recargarFilasTablas();
        listaPaquetes = paqueteFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().update("IdFormPaquetes");
        imprimirMensaje("Correcto", "El Paquete de Servicios  ha sido actualizado.", FacesMessage.SEVERITY_INFO);
    }

    private void recalcularValorPaquete() {
        valorCalculadoPaquete = 0;
        if (paqueteSeleccionado != null) {
            List<FacPaqueteServicio> listaServiciosPaq = paqueteSeleccionado.getFacPaqueteServicioList();
            for (FacPaqueteServicio servicioPaquete : listaServiciosPaq) {//lista servicios
                valorCalculadoPaquete = valorCalculadoPaquete + (servicioPaquete.getValorFinal());
            }
            List<FacPaqueteInsumo> listaInsumosPaq = paqueteSeleccionado.getFacPaqueteInsumoList();
            for (FacPaqueteInsumo insumoPaquete : listaInsumosPaq) {//lista insumos
                valorCalculadoPaquete = valorCalculadoPaquete + (insumoPaquete.getValorFinal());
            }
            List<FacPaqueteMedicamento> listaMedicamentosPaq = paqueteSeleccionado.getFacPaqueteMedicamentoList();
            for (FacPaqueteMedicamento medicamentoPaquete : listaMedicamentosPaq) {//lista medicamentos
                valorCalculadoPaquete = valorCalculadoPaquete + (medicamentoPaquete.getValorFinal());
            }
            paqueteSeleccionado.setValorCalculado(valorCalculadoPaquete);
            paqueteFacade.edit(paqueteSeleccionado);
        }
    }

    public void recargarFilasTablas() {
        servicioPaqueteSeleccionado = null;
        insumoPaqueteSeleccionado = null;
        medicamentoPaqueteSeleccionado = null;

        listaServiciosPaquete = new ArrayList<>();
        listaInsumosPaquete = new ArrayList<>();
        listaMedicamentosPaquete = new ArrayList<>();

        listaServiciosPaqueteFiltro = new ArrayList<>();
        listaInsumosPaqueteFiltro = new ArrayList<>();
        listaMedicamentosPaqueteFiltro = new ArrayList<>();

        FilaDataTable nuevaFila;
        if (paqueteSeleccionado != null) {
            List<FacPaqueteServicio> listaServiciosPaq = paqueteSeleccionado.getFacPaqueteServicioList();
            for (FacPaqueteServicio servicioPaquete : listaServiciosPaq) {//lista servicios
                nuevaFila = new FilaDataTable();
                nuevaFila.setColumna1(servicioPaquete.getFacPaquete().getIdPaquete().toString());
                nuevaFila.setColumna2(servicioPaquete.getFacServicio().getIdServicio().toString());
                nuevaFila.setColumna3(servicioPaquete.getFacServicio().getCodigoServicio());
                nuevaFila.setColumna4(servicioPaquete.getFacServicio().getNombreServicio());
                nuevaFila.setColumna5(servicioPaquete.getTipoTarifa());
                nuevaFila.setColumna6(servicioPaquete.getValorUnitario().toString());
                nuevaFila.setColumna7(servicioPaquete.getDescuento().toString());
                nuevaFila.setColumna8(servicioPaquete.getHonorarioMedico().toString());
                nuevaFila.setColumna9(servicioPaquete.getCantidad().toString());
                nuevaFila.setColumna10(servicioPaquete.getValorFinal().toString());
                nuevaFila.setColumna11(servicioPaquete.getMetaCumplimiento().toString());
                nuevaFila.setColumna12(servicioPaquete.getPeriodicidad().toString());
                if (servicioPaquete.getActivo()) {
                    nuevaFila.setColumna13("ACTIVO");
                } else {
                    nuevaFila.setColumna13("INACTIVO");
                }
                nuevaFila.setColumna14(servicioPaquete.getObservacion());
                listaServiciosPaquete.add(nuevaFila);
                listaServiciosPaqueteFiltro.add(nuevaFila);
            }
            List<FacPaqueteInsumo> listaInsumosPaq = paqueteSeleccionado.getFacPaqueteInsumoList();
            for (FacPaqueteInsumo insumoPaquete : listaInsumosPaq) {//lista insumos
                nuevaFila = new FilaDataTable();
                nuevaFila.setColumna1(insumoPaquete.getFacPaquete().getIdPaquete().toString());
                nuevaFila.setColumna2(insumoPaquete.getCfgInsumo().getIdInsumo().toString());
                nuevaFila.setColumna3(insumoPaquete.getCfgInsumo().getCodigoInsumo());
                nuevaFila.setColumna4(insumoPaquete.getCfgInsumo().getNombreInsumo());
                nuevaFila.setColumna5(insumoPaquete.getValorUnitario().toString());
                nuevaFila.setColumna6(insumoPaquete.getDescuento().toString());
                nuevaFila.setColumna7(insumoPaquete.getCantidad().toString());
                nuevaFila.setColumna8(insumoPaquete.getValorFinal().toString());
                if (insumoPaquete.getActivo()) {
                    nuevaFila.setColumna9("ACTIVO");
                } else {
                    nuevaFila.setColumna9("INACTIVO");
                }
                nuevaFila.setColumna10(insumoPaquete.getObservacion());
                listaInsumosPaquete.add(nuevaFila);
                listaInsumosPaqueteFiltro.add(nuevaFila);
            }
            List<FacPaqueteMedicamento> listaMedicamentosPaq = paqueteSeleccionado.getFacPaqueteMedicamentoList();
            for (FacPaqueteMedicamento medicamentoPaquete : listaMedicamentosPaq) {//lista medicamentos
                nuevaFila = new FilaDataTable();
                nuevaFila.setColumna1(medicamentoPaquete.getFacPaquete().getIdPaquete().toString());
                nuevaFila.setColumna2(medicamentoPaquete.getCfgMedicamento().getIdMedicamento().toString());
                nuevaFila.setColumna3(medicamentoPaquete.getCfgMedicamento().getCodigoMedicamento());
                nuevaFila.setColumna4(medicamentoPaquete.getCfgMedicamento().getNombreMedicamento());
                nuevaFila.setColumna5(medicamentoPaquete.getCfgMedicamento().getFormaMedicamento());
                nuevaFila.setColumna6(medicamentoPaquete.getValorUnitario().toString());
                nuevaFila.setColumna7(medicamentoPaquete.getCantidad().toString());
                nuevaFila.setColumna8(medicamentoPaquete.getDescuento().toString());
                nuevaFila.setColumna9(medicamentoPaquete.getValorFinal().toString());
                if (medicamentoPaquete.getActivo()) {
                    nuevaFila.setColumna10("ACTIVO");
                } else {
                    nuevaFila.setColumna10("INACTIVO");
                }
                nuevaFila.setColumna11(medicamentoPaquete.getObservacion());

                listaMedicamentosPaquete.add(nuevaFila);
                listaMedicamentosPaqueteFiltro.add(nuevaFila);
            }
        }
        tituloTabInsumos = "Insumos (" + listaInsumosPaquete.size() + ")";
        tituloTabMedicamentos = "Medicamentos (" + listaMedicamentosPaquete.size() + ")";
        tituloTabServicios = "Servicios (" + listaServiciosPaquete.size() + ")";
    }

    //---------------------------------------------------
    //----------------- SERVICIOS -----------------------
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
        valorUnitarioServicio = 0;
        valorFinalServicio = 0;
        descuentoServicio = 0;
        honorarioMedico = 0;
        observacionServicio = "";
        tipoTarifa = "";
        cantidadServicios=1;
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
        if (validarNoVacio(idServicio)) {
            FacServicio s = servicioFacade.find(Integer.parseInt(idServicio));
            switch (tipoTarifa) {
                case "Especifica":
                    valorUnitarioServicio = s.getValorParticular();
                    valorFinalServicio = cantidadServicios * (valorUnitarioServicio - (valorUnitarioServicio * (descuentoServicio / 100)) + (valorUnitarioServicio * (honorarioMedico / 100)));
                    break;
                case "SOAT":
                    if (validarNoVacio(unidadValor)) {
                        FacUnidadValor f = unidadValorFacade.find(Integer.parseInt(unidadValor));
                        valorUnitarioServicio = s.getFactorIss() * f.getSmlvd();
                        valorFinalServicio = cantidadServicios * (valorUnitarioServicio - (valorUnitarioServicio * (descuentoServicio / 100)) + (valorUnitarioServicio * (honorarioMedico / 100)));
                    } else {
                        imprimirMensaje("Error", "No hay una unidad de valor para calcular el valor final", FacesMessage.SEVERITY_ERROR);
                    }
                    break;
                case "ISS":
                    if (validarNoVacio(unidadValor)) {
                        FacUnidadValor f = unidadValorFacade.find(Integer.parseInt(unidadValor));
                        valorUnitarioServicio = s.getFactorSoat() * f.getUvr();
                        valorFinalServicio = cantidadServicios * (valorUnitarioServicio - (valorUnitarioServicio * (descuentoServicio / 100)) + (valorUnitarioServicio * (honorarioMedico / 100)));
                    } else {
                        imprimirMensaje("Alerta", "No hay una unidad de valor para calcular el valor final", FacesMessage.SEVERITY_WARN);
                    }
                    break;
            }
        }
    }

    public void cargarDialogoAgregarServicio() {
        listaServicios = servicioFacade.buscarNoEstanEnPaquete(paqueteSeleccionado.getIdPaquete());
        if (listaServicios != null && !listaServicios.isEmpty()) {
            idServicio = listaServicios.get(0).getIdServicio().toString();
        }
        cambiaServicio();
        RequestContext.getCurrentInstance().update("IdFormDialogs:IdPanelAgregarServicio");
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarServicio').show();");

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
        FacPaqueteServicioPK llavePK = new FacPaqueteServicioPK();
        llavePK.setIdPaquete(paqueteSeleccionado.getIdPaquete());
        llavePK.setIdServicio(Integer.parseInt(idServicio));
        FacPaqueteServicio buscado = paqueteServicioFacade.find(llavePK);
        if (buscado != null) {//validar que no exista
            imprimirMensaje("Error", "Este paquete ya tiene adicionado este servicio.", FacesMessage.SEVERITY_ERROR);
            return;
        }
        FacPaqueteServicio nuevoPaqueteServicio = new FacPaqueteServicio();
        nuevoPaqueteServicio.setFacPaqueteServicioPK(llavePK);
        nuevoPaqueteServicio.setActivo(activoServicio);
        nuevoPaqueteServicio.setDescuento(descuentoServicio);
        nuevoPaqueteServicio.setHonorarioMedico(honorarioMedico);
        nuevoPaqueteServicio.setMetaCumplimiento(metaCumplimiento);
        nuevoPaqueteServicio.setObservacion(observacionServicio);
        nuevoPaqueteServicio.setPeriodicidad(periodicidad);
        nuevoPaqueteServicio.setTipoTarifa(tipoTarifa);
        nuevoPaqueteServicio.setValorUnitario(valorUnitarioServicio);
        nuevoPaqueteServicio.setValorFinal(valorFinalServicio);
        nuevoPaqueteServicio.setCantidad(cantidadServicios);
        paqueteServicioFacade.create(nuevoPaqueteServicio);
        paqueteSeleccionado = paqueteFacade.find(paqueteSeleccionado.getIdPaquete());//recargar el manual
        recalcularValorPaquete();
        recargarFilasTablas();
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarServicio').hide(); PF('wvTablaServiciosPaquete').clearFilters(); PF('wvTablaServiciosPaquete').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("IdFormPaquetes:IdTabView");
        //imprimirMensaje("Correcto", "El servicio has sido adicionado al tarifario.", FacesMessage.SEVERITY_INFO);
    }

    public void quitarServicio() {
        if (servicioPaqueteSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún servicio de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoQuitarServicio').show();");
    }

    public void confirmarQuitarServicio() {
        if (servicioPaqueteSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún servicio", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            FacPaqueteServicioPK llavePK = new FacPaqueteServicioPK();
            llavePK.setIdPaquete(paqueteFacade.find(Integer.parseInt(servicioPaqueteSeleccionado.getColumna1())).getIdPaquete());
            llavePK.setIdServicio(servicioFacade.find(Integer.parseInt(servicioPaqueteSeleccionado.getColumna2())).getIdServicio());
            paqueteServicioFacade.remove(paqueteServicioFacade.find(llavePK));
            paqueteSeleccionado = paqueteFacade.find(paqueteSeleccionado.getIdPaquete());
            recalcularValorPaquete();
            recargarFilasTablas();
            RequestContext.getCurrentInstance().update("IdFormPaquetes:IdTabView");
            RequestContext.getCurrentInstance().execute("PF('dialogoQuitarServicio').hide(); PF('wvTablaServiciosPaquete').clearFilters(); PF('wvTablaServiciosPaquete').getPaginator().setPage(0);");
            //imprimirMensaje("Correcto", "El servicio ha sido eliminado del tarifario.", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El servicio que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    //---------------------------------------------------
    //----------------- INSUMOS -------------------------
    //---------------------------------------------------
    public void cambiaInsumo() {
        activoInsumo = true;
        valorUnitarioInsumo = 0;
        valorFinalInsumo = 0;
        descuentoInsumo = 0;
        cantidadInsumos=1;
        observacionInsumo = "";
        calcularValoresInsumo();
    }

    public void calcularValoresInsumo() {
        if (validarNoVacio(idInsumo)) {
            CfgInsumo p = insumoFacade.find(Integer.parseInt(idInsumo));
            valorUnitarioInsumo = p.getValor();
            valorFinalInsumo = cantidadInsumos * (valorUnitarioInsumo - (valorUnitarioInsumo * (descuentoInsumo / 100)));
        }
    }

    public void cargarDialogoAgregarInsumo() {
        idInsumo = "";
        listaInsumos = insumoFacade.buscarNoEstanEnPaquete(paqueteSeleccionado.getIdPaquete());
        if (listaInsumos != null && !listaInsumos.isEmpty()) {
            idInsumo = listaInsumos.get(0).getIdInsumo().toString();
        }
        cambiaInsumo();
        RequestContext.getCurrentInstance().update("IdFormDialogs:IdPanelAgregarInsumo");
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarInsumo').show();");

    }

    public void agregarInsumo() {
        if (validacionCampoVacio(idInsumo, "Insumo")) {
            return;
        }
        FacPaqueteInsumoPK llavePK = new FacPaqueteInsumoPK();
        llavePK.setIdPaquete(paqueteSeleccionado.getIdPaquete());
        llavePK.setIdInsumo(Integer.parseInt(idInsumo));
        FacPaqueteInsumo buscado = paqueteInsumoFacade.find(llavePK);
        if (buscado != null) {//validar que no exista
            imprimirMensaje("Error", "Este paquete ya tiene adicionado este insumo.", FacesMessage.SEVERITY_ERROR);
            return;
        }
        FacPaqueteInsumo nuevoPaqueteInsumo = new FacPaqueteInsumo();
        nuevoPaqueteInsumo.setFacPaqueteInsumoPK(llavePK);
        nuevoPaqueteInsumo.setActivo(activoInsumo);
        nuevoPaqueteInsumo.setDescuento(descuentoInsumo);
        nuevoPaqueteInsumo.setObservacion(observacionInsumo);
        nuevoPaqueteInsumo.setValorFinal(valorFinalInsumo);
        nuevoPaqueteInsumo.setValorUnitario(valorUnitarioInsumo);
        nuevoPaqueteInsumo.setCantidad(cantidadInsumos);
        paqueteInsumoFacade.create(nuevoPaqueteInsumo);
        paqueteSeleccionado = paqueteFacade.find(paqueteSeleccionado.getIdPaquete());//recargar el manual
        recalcularValorPaquete();
        recargarFilasTablas();
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarInsumo').hide(); PF('wvTablaInsumosPaquete').clearFilters(); PF('wvTablaInsumosPaquete').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("IdFormPaquetes:IdTabView");
    }

    public void quitarInsumo() {
        if (insumoPaqueteSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún insumo de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoQuitarInsumo').show();");
    }

    public void confirmarQuitarInsumo() {
        if (insumoPaqueteSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún insumo", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            FacPaqueteInsumoPK llavePK = new FacPaqueteInsumoPK();
            llavePK.setIdPaquete(paqueteFacade.find(Integer.parseInt(insumoPaqueteSeleccionado.getColumna1())).getIdPaquete());
            llavePK.setIdInsumo(insumoFacade.find(Integer.parseInt(insumoPaqueteSeleccionado.getColumna2())).getIdInsumo());
            paqueteInsumoFacade.remove(paqueteInsumoFacade.find(llavePK));
            paqueteSeleccionado = paqueteFacade.find(paqueteSeleccionado.getIdPaquete());
            recalcularValorPaquete();
            recargarFilasTablas();
            RequestContext.getCurrentInstance().update("IdFormPaquetes:IdTabView");
            RequestContext.getCurrentInstance().execute("PF('dialogoQuitarInsumo').hide(); PF('wvTablaInsumosPaquete').clearFilters(); PF('wvTablaInsumosPaquete').getPaginator().setPage(0);");
            //imprimirMensaje("Correcto", "El insumo ha sido eliminado del tarifario.", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El insumo que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    //---------------------------------------------------
    //----------------- MEDICAMENTOS --------------------
    //---------------------------------------------------
    public void cambiaMedicamento() {
        activoMedicamento = true;
        cantidadMedicamentos=1;
        valorUnitarioMedicamento = 0;
        valorFinalMedicamento = 0;
        descuentoMedicamento = 0;
        observacionMedicamento = "";
        calcularValoresMedicamento();
    }

    public void calcularValoresMedicamento() {
        if (validarNoVacio(idMedicamento)) {
            CfgMedicamento p = medicamentoFacade.find(Integer.parseInt(idMedicamento));
            valorUnitarioMedicamento = p.getValor();
            valorFinalMedicamento = cantidadMedicamentos * (valorUnitarioMedicamento - (valorUnitarioMedicamento * (descuentoMedicamento / 100)));
        }
    }

    public void cargarDialogoAgregarMedicamento() {
        idMedicamento = "";
        listaMedicamentos = medicamentoFacade.buscarNoEstanEnPaquete(paqueteSeleccionado.getIdPaquete());
        if (listaMedicamentos != null && !listaMedicamentos.isEmpty()) {
            idMedicamento = listaMedicamentos.get(0).getIdMedicamento().toString();
        }
        cambiaMedicamento();
        RequestContext.getCurrentInstance().update("IdFormDialogs:IdPanelAgregarMedicamento");
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarMedicamento').show();");

    }

    public void agregarMedicamento() {
        if (validacionCampoVacio(idMedicamento, "Medicamento")) {
            return;
        }
        FacPaqueteMedicamentoPK llavePK = new FacPaqueteMedicamentoPK();
        llavePK.setIdPaquete(paqueteSeleccionado.getIdPaquete());
        llavePK.setIdMedicamento(Integer.parseInt(idMedicamento));
        FacPaqueteMedicamento buscado = paqueteMedicamentoFacade.find(llavePK);
        if (buscado != null) {//validar que no exista
            imprimirMensaje("Error", "Este paquete ya tiene adicionado este medicamento.", FacesMessage.SEVERITY_ERROR);
            return;
        }
        FacPaqueteMedicamento nuevoPaqueteMedicamento = new FacPaqueteMedicamento();
        nuevoPaqueteMedicamento.setFacPaqueteMedicamentoPK(llavePK);
        nuevoPaqueteMedicamento.setActivo(activoMedicamento);
        nuevoPaqueteMedicamento.setDescuento(descuentoMedicamento);
        nuevoPaqueteMedicamento.setObservacion(observacionMedicamento);
        nuevoPaqueteMedicamento.setValorFinal(valorFinalMedicamento);
        nuevoPaqueteMedicamento.setCantidad(cantidadMedicamentos);
        nuevoPaqueteMedicamento.setValorUnitario(valorUnitarioMedicamento);
        paqueteMedicamentoFacade.create(nuevoPaqueteMedicamento);
        paqueteSeleccionado = paqueteFacade.find(paqueteSeleccionado.getIdPaquete());//recargar el manual
        recalcularValorPaquete();
        recargarFilasTablas();
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarMedicamento').hide(); PF('wvTablaMedicamentosPaquete').clearFilters(); PF('wvTablaMedicamentosPaquete').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("IdFormPaquetes:IdTabView");
    }

    public void quitarMedicamento() {
        if (medicamentoPaqueteSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún medicamento de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoQuitarMedicamento').show();");
    }

    public void confirmarQuitarMedicamento() {
        if (medicamentoPaqueteSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún medicamento", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            FacPaqueteMedicamentoPK llavePK = new FacPaqueteMedicamentoPK();
            llavePK.setIdPaquete(paqueteFacade.find(Integer.parseInt(medicamentoPaqueteSeleccionado.getColumna1())).getIdPaquete());
            llavePK.setIdMedicamento(medicamentoFacade.find(Integer.parseInt(medicamentoPaqueteSeleccionado.getColumna2())).getIdMedicamento());
            paqueteMedicamentoFacade.remove(paqueteMedicamentoFacade.find(llavePK));
            paqueteSeleccionado = paqueteFacade.find(paqueteSeleccionado.getIdPaquete());
            recalcularValorPaquete();
            recargarFilasTablas();
            RequestContext.getCurrentInstance().update("IdFormPaquetes:IdTabView");
            RequestContext.getCurrentInstance().execute("PF('dialogoQuitarMedicamento').hide(); PF('wvTablaMedicamentosPaquete').clearFilters(); PF('wvTablaMedicamentosPaquete').getPaginator().setPage(0);");
            //imprimirMensaje("Correcto", "El medicamento ha sido eliminado del tarifario.", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El medicamento que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------
    public FacPaquete getPaqueteSeleccionado() {
        return paqueteSeleccionado;
    }

    public void setPaqueteSeleccionado(FacPaquete paqueteSeleccionado) {
        this.paqueteSeleccionado = paqueteSeleccionado;
    }

    public FacPaquete getPaqueteSeleccionadoTabla() {
        return paqueteSeleccionadoTabla;
    }

    public void setPaqueteSeleccionadoTabla(FacPaquete paqueteSeleccionadoTabla) {
        this.paqueteSeleccionadoTabla = paqueteSeleccionadoTabla;
    }

    public List<FacPaquete> getListaPaquetes() {
        return listaPaquetes;
    }

    public void setListaPaquetes(List<FacPaquete> listaPaquetes) {
        this.listaPaquetes = listaPaquetes;
    }

    public List<FilaDataTable> getListaServiciosPaquete() {
        return listaServiciosPaquete;
    }

    public void setListaServiciosPaquete(List<FilaDataTable> listaServiciosPaquete) {
        this.listaServiciosPaquete = listaServiciosPaquete;
    }

    public List<FilaDataTable> getListaInsumosPaquete() {
        return listaInsumosPaquete;
    }

    public void setListaInsumosPaquete(List<FilaDataTable> listaInsumosPaquete) {
        this.listaInsumosPaquete = listaInsumosPaquete;
    }

    public List<FilaDataTable> getListaServiciosPaqueteFiltro() {
        return listaServiciosPaqueteFiltro;
    }

    public void setListaServiciosPaqueteFiltro(List<FilaDataTable> listaServiciosPaqueteFiltro) {
        this.listaServiciosPaqueteFiltro = listaServiciosPaqueteFiltro;
    }

    public List<FilaDataTable> getListaInsumosPaqueteFiltro() {
        return listaInsumosPaqueteFiltro;
    }

    public void setListaInsumosPaqueteFiltro(List<FilaDataTable> listaInsumosPaqueteFiltro) {
        this.listaInsumosPaqueteFiltro = listaInsumosPaqueteFiltro;
    }

    public List<FilaDataTable> getListaMedicamentosPaqueteFiltro() {
        return listaMedicamentosPaqueteFiltro;
    }

    public void setListaMedicamentosPaqueteFiltro(List<FilaDataTable> listaMedicamentosPaqueteFiltro) {
        this.listaMedicamentosPaqueteFiltro = listaMedicamentosPaqueteFiltro;
    }

    public FilaDataTable getServicioPaqueteSeleccionado() {
        return servicioPaqueteSeleccionado;
    }

    public void setServicioPaqueteSeleccionado(FilaDataTable servicioPaqueteSeleccionado) {
        this.servicioPaqueteSeleccionado = servicioPaqueteSeleccionado;
    }

    public FilaDataTable getInsumoPaqueteSeleccionado() {
        return insumoPaqueteSeleccionado;
    }

    public void setInsumoPaqueteSeleccionado(FilaDataTable insumoPaqueteSeleccionado) {
        this.insumoPaqueteSeleccionado = insumoPaqueteSeleccionado;
    }

    public FilaDataTable getMedicamentoPaqueteSeleccionado() {
        return medicamentoPaqueteSeleccionado;
    }

    public void setMedicamentoPaqueteSeleccionado(FilaDataTable medicamentoPaqueteSeleccionado) {
        this.medicamentoPaqueteSeleccionado = medicamentoPaqueteSeleccionado;
    }

    public String getTituloTabPaquete() {
        return tituloTabPaquete;
    }

    public void setTituloTabPaquete(String tituloTabPaquete) {
        this.tituloTabPaquete = tituloTabPaquete;
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

    public String getCodigoPaquete() {
        return codigoPaquete;
    }

    public void setCodigoPaquete(String codigoPaquete) {
        this.codigoPaquete = codigoPaquete;
    }

    public String getNombrePaquete() {
        return nombrePaquete;
    }

    public void setNombrePaquete(String nombrePaquete) {
        this.nombrePaquete = nombrePaquete;
    }

    public double getValorPaquete() {
        return valorPaquete;
    }

    public void setValorPaquete(double valorPaquete) {
        this.valorPaquete = valorPaquete;
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

    public double getDescuentoServicio() {
        return descuentoServicio;
    }

    public void setDescuentoServicio(double descuentoServicio) {
        this.descuentoServicio = descuentoServicio;
    }

    public double getHonorarioMedico() {
        return honorarioMedico;
    }

    public void setHonorarioMedico(double honorarioMedico) {
        this.honorarioMedico = honorarioMedico;
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

    public String getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(String idInsumo) {
        this.idInsumo = idInsumo;
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

    public String getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(String idMedicamento) {
        this.idMedicamento = idMedicamento;
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

    public List<FilaDataTable> getListaMedicamentosPaquete() {
        return listaMedicamentosPaquete;
    }

    public void setListaMedicamentosPaquete(List<FilaDataTable> listaMedicamentosPaquete) {
        this.listaMedicamentosPaquete = listaMedicamentosPaquete;
    }

    public int getCantidadServicios() {
        return cantidadServicios;
    }

    public void setCantidadServicios(int cantidadServicios) {
        this.cantidadServicios = cantidadServicios;
    }

    public double getValorUnitarioServicio() {
        return valorUnitarioServicio;
    }

    public void setValorUnitarioServicio(double valorUnitarioServicio) {
        this.valorUnitarioServicio = valorUnitarioServicio;
    }

    public int getCantidadInsumos() {
        return cantidadInsumos;
    }

    public void setCantidadInsumos(int cantidadInsumos) {
        this.cantidadInsumos = cantidadInsumos;
    }

    public double getValorUnitarioInsumo() {
        return valorUnitarioInsumo;
    }

    public void setValorUnitarioInsumo(double valorUnitarioInsumo) {
        this.valorUnitarioInsumo = valorUnitarioInsumo;
    }

    public int getCantidadMedicamentos() {
        return cantidadMedicamentos;
    }

    public void setCantidadMedicamentos(int cantidadMedicamentos) {
        this.cantidadMedicamentos = cantidadMedicamentos;
    }

    public double getValorUnitarioMedicamento() {
        return valorUnitarioMedicamento;
    }

    public void setValorUnitarioMedicamento(double valorUnitarioMedicamento) {
        this.valorUnitarioMedicamento = valorUnitarioMedicamento;
    }

    public double getValorCalculadoPaquete() {
        return valorCalculadoPaquete;
    }

    public void setValorCalculadoPaquete(double valorCalculadoPaquete) {
        this.valorCalculadoPaquete = valorCalculadoPaquete;
    }

    public double getValorFinalServicio() {
        return valorFinalServicio;
    }

    public void setValorFinalServicio(double valorFinalServicio) {
        this.valorFinalServicio = valorFinalServicio;
    }

    public double getValorFinalInsumo() {
        return valorFinalInsumo;
    }

    public void setValorFinalInsumo(double valorFinalInsumo) {
        this.valorFinalInsumo = valorFinalInsumo;
    }

    public double getValorFinalMedicamento() {
        return valorFinalMedicamento;
    }

    public void setValorFinalMedicamento(double valorFinalMedicamento) {
        this.valorFinalMedicamento = valorFinalMedicamento;
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

    public List<SelectItem> getListaTipoTarifa() {
        return listaTipoTarifa;
    }

    public void setListaTipoTarifa(List<SelectItem> listaTipoTarifa) {
        this.listaTipoTarifa = listaTipoTarifa;
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

}
