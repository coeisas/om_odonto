/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import beans.utilidades.FilaDataTable;
import beans.utilidades.LazyPacienteDataModel;
import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import modelo.entidades.CfgPacientes;
import modelo.entidades.FacAdministradora;
import modelo.entidades.FacConsumoInsumo;
import modelo.entidades.FacConsumoMedicamento;
import modelo.entidades.FacConsumoPaquete;
import modelo.entidades.FacConsumoServicio;
import modelo.entidades.FacContrato;
import modelo.entidades.FacManualTarifario;
import modelo.entidades.FacManualTarifarioInsumo;
import modelo.entidades.FacManualTarifarioMedicamento;
import modelo.entidades.FacManualTarifarioPaquete;
import modelo.entidades.FacManualTarifarioServicio;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgDiagnosticoFacade;
import modelo.fachadas.CfgInsumoFacade;
import modelo.fachadas.CfgMedicamentoFacade;
import modelo.fachadas.CfgPacientesFacade;
import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.FacConsumoInsumoFacade;
import modelo.fachadas.FacConsumoMedicamentoFacade;
import modelo.fachadas.FacConsumoPaqueteFacade;
import modelo.fachadas.FacConsumoServicioFacade;
import modelo.fachadas.FacManualTarifarioInsumoFacade;
import modelo.fachadas.FacManualTarifarioMedicamentoFacade;
import modelo.fachadas.FacManualTarifarioPaqueteFacade;
import modelo.fachadas.FacManualTarifarioServicioFacade;
import modelo.fachadas.FacPaqueteFacade;
import modelo.fachadas.FacServicioFacade;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author santos
 */
@ManagedBean(name = "consumosMB")
@SessionScoped
public class ConsumosMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    FacConsumoServicioFacade consumoServicioFacade;
    @EJB
    FacConsumoInsumoFacade consumoInsumoFacade;
    @EJB
    FacConsumoPaqueteFacade consumoPaqueteFacade;
    @EJB
    FacConsumoMedicamentoFacade consumoMedicamentoFacade;
    @EJB
    CfgInsumoFacade insumoFacade;
    @EJB
    FacManualTarifarioInsumoFacade manualTarifarioInsumoFacade;
    @EJB
    FacPaqueteFacade paqueteFacade;
    @EJB
    FacManualTarifarioPaqueteFacade manualTarifarioPaqueteFacade;
    @EJB
    FacServicioFacade servicioFacade;
    @EJB
    FacManualTarifarioServicioFacade manualTarifarioServicioFacade;
    @EJB
    CfgMedicamentoFacade medicamentoFacade;
    @EJB
    FacManualTarifarioMedicamentoFacade manualTarifarioMedicamentoFacade;
    @EJB
    CfgClasificacionesFacade clasificacionesFacade;
    @EJB
    CfgUsuariosFacade usuariosFacade;
    @EJB
    CfgPacientesFacade pacientesFacade;
    @EJB
    CfgDiagnosticoFacade diagnosticoFacade;
    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------
    private LazyDataModel<CfgPacientes> listaPacientes;
    private CfgPacientes pacienteTmp;
    private CfgPacientes pacienteSeleccionadoTabla;
    private CfgPacientes pacienteSeleccionado;
    private FacManualTarifario manualTarifarioPaciente;

    private List<FacManualTarifarioServicio> listaServiciosManual;
    private List<FacManualTarifarioInsumo> listaInsumosManual;
    private List<FacManualTarifarioMedicamento> listaMedicamentosManual;
    private List<FacManualTarifarioPaquete> listaPaquetesManual;

    //---------------------------------------------------
    //----------------- VARIABLES -----------------------
    //---------------------------------------------------
    private List<FilaDataTable> listaServiciosConsumo = new ArrayList<>();
    private List<FilaDataTable> listaInsumosConsumo = new ArrayList<>();
    private List<FilaDataTable> listaMedicamentosConsumo = new ArrayList<>();
    private List<FilaDataTable> listaPaquetesConsumo = new ArrayList<>();
    private List<FilaDataTable> listaServiciosConsumoFiltro = new ArrayList<>();
    private List<FilaDataTable> listaInsumosConsumoFiltro = new ArrayList<>();
    private List<FilaDataTable> listaMedicamentosConsumoFiltro = new ArrayList<>();
    private List<FilaDataTable> listaPaquetesConsumoFiltro = new ArrayList<>();
    private FilaDataTable servicioConsumoSeleccionado;
    private FilaDataTable insumoConsumoSeleccionado;
    private FilaDataTable medicamentoConsumoSeleccionado;
    private FilaDataTable paqueteConsumoSeleccionado;

    //------------- OTRAS VARIABLES ------
    private String mensajeConfiguracion = null;//permite informar problemas al querer agregar consumos 
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    //-------------PACIENTES--------------
    private String identificacionPaciente = "";
    private String tipoIdentificacion = "";
    private String nombrePaciente = "Paciente";
    private String generoPaciente = "";
    private String edadPaciente = "";
    private String administradoraPaciente = "";

    //-------------SERVICICOS--------------
    private String tituloTabServicios = "Servicios (0)";
    private String idPrestadorServicio = "";
    private String idServicioManual = "";
    private Date fechaServicio;
    private int cantidadServicio = 1;
    private String tipoTarifaServicio;
    private double valorUnitarioServicio = 0;
    private double valorFinalServicio = 0;
    private String diagnosticoPrincipal = "";
    private String diagnosticoRelacionado = "";
    //-------------INSUMOS--------------
    private String tituloTabInsumos = "Insumos (0)";
    private String idPrestadorInsumo = "";
    private String idInsumoManual = "";
    private Date fechaInsumo;
    private int cantidadInsumo = 1;
    //private String tipoTarifaInsumo;
    private double valorUnitarioInsumo = 0;
    private double valorFinalInsumo = 0;
    //-------------MEDICAMENTOS--------------
    private String tituloTabMedicamentos = "Medicamentos (0)";
    private String idPrestadorMedicamento = "";
    private String idMedicamentoManual = "";
    private Date fechaMedicamento;
    private int cantidadMedicamento = 1;
    //private String tipoTarifaMedicamento;
    private double valorUnitarioMedicamento = 0;
    private double valorFinalMedicamento = 0;
    //-------------PAQUETES--------------
    private String tituloTabPaquetes = "Paquetes (0)";
    private String idPrestadorPaquete = "";
    private String idPaqueteManual = "";
    private Date fechaPaquete;
    private int cantidadPaquete = 1;
    //private String tipoTarifaPaquete;
    private double valorUnitarioPaquete = 0;
    private double valorFinalPaquete = 0;

    //---------------------------------------------------
    //------------- FUNCIONES INICIALES  ----------------
    //---------------------------------------------------      
    @PostConstruct
    public void inicializar() {
        listaPacientes = new LazyPacienteDataModel(pacientesFacade);
        //recargarListas();
    }

    public ConsumosMB() {
    }

    //---------------------------------------------------
    //-----------------FUNCIONES CONSUMOS ---------------
    //---------------------------------------------------    
    private void recargarListas() {
        servicioConsumoSeleccionado = null;
        insumoConsumoSeleccionado = null;
        medicamentoConsumoSeleccionado = null;
        paqueteConsumoSeleccionado = null;

        listaServiciosConsumo = new ArrayList<>();
        listaInsumosConsumo = new ArrayList<>();
        listaMedicamentosConsumo = new ArrayList<>();
        listaPaquetesConsumo = new ArrayList<>();

        listaServiciosConsumoFiltro = new ArrayList<>();
        listaInsumosConsumoFiltro = new ArrayList<>();
        listaMedicamentosConsumoFiltro = new ArrayList<>();
        listaPaquetesConsumoFiltro = new ArrayList<>();

        FilaDataTable nuevaFila;
        if (pacienteSeleccionado != null) {
            List<FacConsumoServicio> listaServiciosCon = pacienteSeleccionado.getFacConsumoServicioList();
            for (FacConsumoServicio servicioConsumo : listaServiciosCon) {//lista servicios
                nuevaFila = new FilaDataTable();
                nuevaFila.setColumna1(servicioConsumo.getIdConsumoServicio().toString());
                nuevaFila.setColumna2(dateFormat.format(servicioConsumo.getFecha()));
                nuevaFila.setColumna3(servicioConsumo.getIdServicio().getNombreServicio());
                nuevaFila.setColumna4(servicioConsumo.getTipoTarifa());
                nuevaFila.setColumna5(servicioConsumo.getValorUnitario().toString());
                nuevaFila.setColumna6(servicioConsumo.getCantidad().toString());
                nuevaFila.setColumna7(servicioConsumo.getValorFinal().toString());
                nuevaFila.setColumna8(servicioConsumo.getIdPrestador().nombreCompleto());
                listaServiciosConsumo.add(nuevaFila);
                listaServiciosConsumoFiltro.add(nuevaFila);
            }
            List<FacConsumoInsumo> listaInsumosCon = pacienteSeleccionado.getFacConsumoInsumoList();
            for (FacConsumoInsumo insumoConsumo : listaInsumosCon) {//lista servicios
                nuevaFila = new FilaDataTable();
                nuevaFila.setColumna1(insumoConsumo.getIdConsumoInsumo().toString());
                nuevaFila.setColumna2(dateFormat.format(insumoConsumo.getFecha()));
                nuevaFila.setColumna3(insumoConsumo.getIdInsumo().getNombreInsumo());
                nuevaFila.setColumna4(insumoConsumo.getValorUnitario().toString());
                nuevaFila.setColumna5(insumoConsumo.getCantidad().toString());
                nuevaFila.setColumna6(insumoConsumo.getValorFinal().toString());
                nuevaFila.setColumna7(insumoConsumo.getIdPrestador().nombreCompleto());
                listaInsumosConsumo.add(nuevaFila);
                listaInsumosConsumoFiltro.add(nuevaFila);
            }
            List<FacConsumoMedicamento> listaMedicamentosCon = pacienteSeleccionado.getFacConsumoMedicamentoList();
            for (FacConsumoMedicamento medicamentoConsumo : listaMedicamentosCon) {//lista servicios
                nuevaFila = new FilaDataTable();
                nuevaFila.setColumna1(medicamentoConsumo.getIdConsumoMedicamento().toString());
                nuevaFila.setColumna2(dateFormat.format(medicamentoConsumo.getFecha()));
                nuevaFila.setColumna3(medicamentoConsumo.getIdMedicamento().getNombreMedicamento());
                nuevaFila.setColumna4(medicamentoConsumo.getIdMedicamento().getFormaMedicamento());
                nuevaFila.setColumna5(medicamentoConsumo.getValorUnitario().toString());
                nuevaFila.setColumna6(medicamentoConsumo.getCantidad().toString());
                nuevaFila.setColumna7(medicamentoConsumo.getValorFinal().toString());
                nuevaFila.setColumna8(medicamentoConsumo.getIdPrestador().nombreCompleto());
                listaMedicamentosConsumo.add(nuevaFila);
                listaMedicamentosConsumoFiltro.add(nuevaFila);
            }
            List<FacConsumoPaquete> listaPaquetesCon = pacienteSeleccionado.getFacConsumoPaqueteList();
            for (FacConsumoPaquete paqueteConsumo : listaPaquetesCon) {//lista servicios
                nuevaFila = new FilaDataTable();
                nuevaFila.setColumna1(paqueteConsumo.getIdConsumoPaquete().toString());
                nuevaFila.setColumna2(dateFormat.format(paqueteConsumo.getFecha()));
                nuevaFila.setColumna3(paqueteConsumo.getIdPaquete().getNombrePaquete());
                nuevaFila.setColumna4(paqueteConsumo.getValorUnitario().toString());
                nuevaFila.setColumna5(paqueteConsumo.getCantidad().toString());
                nuevaFila.setColumna6(paqueteConsumo.getValorFinal().toString());
                nuevaFila.setColumna7(paqueteConsumo.getIdPrestador().nombreCompleto());
                listaPaquetesConsumo.add(nuevaFila);
                listaPaquetesConsumoFiltro.add(nuevaFila);
            }
        }
        tituloTabInsumos = "Insumos (" + listaInsumosConsumo.size() + ")";
        tituloTabMedicamentos = "Medicamentos (" + listaMedicamentosConsumo.size() + ")";
        tituloTabServicios = "Servicios (" + listaServiciosConsumo.size() + ")";
        tituloTabPaquetes = "Paquetes (" + listaPaquetesConsumo.size() + ")";
    }

    //---------------------------------------------------
    //-----------------FUNCIONES PACIENTES --------------
    //---------------------------------------------------
    public void validarIdentificacion() {//verifica si existe la identificacion de lo contrario abre un dialogo para seleccionar el paciente de una tabla
        pacienteTmp = pacientesFacade.buscarPorIdentificacion(identificacionPaciente);
        if (pacienteTmp != null) {
            pacienteSeleccionadoTabla = pacienteTmp;
            cargarPaciente();

        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgSeleccionarPaciente').show();");
        }
    }

    public void recargarPaciente() {//funcion que se llama cada vez que se carga completamente la pagina consumos.xhtml
        if (pacienteSeleccionadoTabla != null) {
            cargarPaciente();
        }
    }

    public void cargarPaciente() {//cargar un paciente desde del dialogo de buscar paciente o al digitar una identificacion valida(esta en pacientes)
        if (pacienteSeleccionadoTabla != null) {

            pacienteSeleccionado = pacientesFacade.find(pacienteSeleccionadoTabla.getIdPaciente());
            identificacionPaciente = "";
            mensajeConfiguracion = null;
            FacContrato contratoSeleccionado = null;

            if (pacienteSeleccionado.getIdAdministradora() != null) {//SE VALIDA QUE SE PUEDA OBTENER EL MANUAL TARIFARIO
                if (pacienteSeleccionado.getRegimen().getId() != null) {
                    FacAdministradora ad = pacienteSeleccionado.getIdAdministradora();
                    if (ad.getFacContratoList() != null && !ad.getFacContratoList().isEmpty()) {
                        for (FacContrato contrato : ad.getFacContratoList()) {//BUSCO UN MANUAL QUE CORRESPONDA AL MISMO REGIMEN DEL PACIENTE
                            if (Objects.equals(pacienteSeleccionado.getRegimen().getId(), contrato.getTipoContrato().getId())) {
                                contratoSeleccionado = contrato;
                            }
                        }
                        if (contratoSeleccionado == null) {
                            mensajeConfiguracion = "No se puede gestionar Consumos para : \n"
                                    + "Paciente: " + pacienteSeleccionado.nombreCompleto() + " \n"
                                    + "Administradora: " + ad.getRazonSocial() + " \n"
                                    + "Razón: Ningún contrato es del tipo: " + pacienteSeleccionado.getRegimen();
                        } else {
                            if (contratoSeleccionado.getIdManualTarifario() != null) {//DETERMINAR SI CONTRATO SELECCIONADO TIENE MANUAL TARIFARIO
                                manualTarifarioPaciente = contratoSeleccionado.getIdManualTarifario();
                            } else {
                                mensajeConfiguracion = "No se puede gestionar Consumos para : \n"
                                        + "Paciente: " + pacienteSeleccionado.nombreCompleto() + " \n"
                                        + "Administradora: " + ad.getRazonSocial() + " \n"
                                        + "Contrato: " + contratoSeleccionado.getDescripcion() + " \n"
                                        + "Razón: Contrato no tiene manual tarifario";
                            }
                        }
                    } else {
                        mensajeConfiguracion = "No se puede gestionar Consumos para: \n"
                                + "Paciente: " + pacienteSeleccionado.nombreCompleto() + "\n"
                                + "Administradora: " + ad.getRazonSocial() + "\n"
                                + "Razón: la adminstradora no tiene ningún contrato";
                    }
                } else {
                    mensajeConfiguracion = "No se puede gestionar Consumos para: \n"
                            + "Paciente: " + pacienteSeleccionado.nombreCompleto() + "\n"
                            + "Razón: El paciente no tiene régimen.";
                }
            } else {
                mensajeConfiguracion = "No se puede gestionar Consumos para : \n"
                        + "Paciente " + pacienteSeleccionado.nombreCompleto() + "\n"
                        + "Razón: El paciente no tiene administradora.";
            }
            if (mensajeConfiguracion != null) {//APARECIO ERROR AL CARGAR MANUA TARIFARIO
                pacienteSeleccionado = null;
                identificacionPaciente = "";
                RequestContext.getCurrentInstance().execute("PF('dlgSeleccionarPaciente').hide();");
                RequestContext.getCurrentInstance().update("IdFormConsumos");
                return;
            }
            recargarListas();
            nombrePaciente = "Paciente";
            identificacionPaciente = pacienteSeleccionado.getIdentificacion();
            if (pacienteSeleccionado.getTipoIdentificacion() != null) {
                tipoIdentificacion = pacienteSeleccionado.getTipoIdentificacion().getDescripcion();
            } else {
                tipoIdentificacion = "";
            }
            nombrePaciente = pacienteSeleccionado.nombreCompleto();
            if (pacienteSeleccionado.getGenero() != null) {
                generoPaciente = pacienteSeleccionado.getGenero().getObservacion();
            } else {
                generoPaciente = "";
            }
            if (pacienteSeleccionado.getFechaNacimiento() != null) {
                edadPaciente = calcularEdad(pacienteSeleccionado.getFechaNacimiento());
            } else {
                edadPaciente = "";
            }
            if (pacienteSeleccionado.getIdAdministradora() != null) {
                administradoraPaciente = pacienteSeleccionado.getIdAdministradora().getRazonSocial();
            } else {
                administradoraPaciente = "";
            }

            RequestContext.getCurrentInstance().execute("PF('dlgSeleccionarPaciente').hide();");
            RequestContext.getCurrentInstance().update("IdFormConsumos");
        } else {
            imprimirMensaje("Error", "Se debe seleccionar un paciente de la tabla", FacesMessage.SEVERITY_ERROR);
        }
    }

    //---------------------------------------------------
    //-----------------FUNCIONES SERVICIOS ---------------
    //--------------------------------------------------- 
    public List<String> autocompletarDiagnostico(String txt) {//retorna una lista con los diagnosticos que contengan el parametro txt
        if (txt != null && txt.length() > 2) {
            return diagnosticoFacade.autocompletarDiagnostico(txt);
        } else {
            return null;
        }
    }

    public void cambiaServicio() {//se debe determinar que tipos de tarifas contiene el servicio
        idPrestadorServicio = "";
        fechaServicio = new Date();
        tipoTarifaServicio = "";
        diagnosticoPrincipal = "";
        diagnosticoRelacionado = "";
        cantidadServicio = 1;
        valorUnitarioServicio = 0;
        valorFinalServicio = 0;
        calcularValoresServicio();
    }

    private FacManualTarifarioServicio buscarEnListaServiciosManual(int idServicio) {
        if (listaServiciosManual != null && !listaServiciosManual.isEmpty()) {
            for (FacManualTarifarioServicio servicioManual : listaServiciosManual) {
                if (servicioManual.getFacServicio().getIdServicio() == idServicio) {
                    return servicioManual;
                }
            }
        }
        return null;
    }

    public void calcularValoresServicio() {
        if (validarNoVacio(idServicioManual)) {
            FacManualTarifarioServicio s = buscarEnListaServiciosManual(Integer.parseInt(idServicioManual));
            tipoTarifaServicio = s.getTipoTarifa();
            valorUnitarioServicio = s.getValorInicial();
            valorFinalServicio = valorUnitarioServicio * cantidadServicio;
        }
    }

    public void cargarDialogoAgregarServicio() {
        listaServiciosManual = manualTarifarioPaciente.getFacManualTarifarioServicioList();
        if (listaServiciosManual != null && !listaServiciosManual.isEmpty()) {
            idServicioManual = listaServiciosManual.get(0).getFacServicio().getIdServicio().toString();
        }
        cambiaServicio();
        RequestContext.getCurrentInstance().update("IdFormDialogs:IdPanelAgregarServicio");
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarServicio').show();");
    }

    public void agregarServicio() {
        if (validacionCampoVacio(idServicioManual, "Servicio")) {
            return;
        }
        if (validacionCampoVacio(idPrestadorServicio, "Prestador")) {
            return;
        }
        if (fechaServicio == null) {
            imprimirMensaje("Error", "Debe ingresar una fecha", FacesMessage.SEVERITY_ERROR);
            return;
        }
        FacConsumoServicio nuevoConsumoServicio = new FacConsumoServicio();
        nuevoConsumoServicio.setIdPaciente(pacienteSeleccionado);
        nuevoConsumoServicio.setIdPrestador(usuariosFacade.find(Integer.parseInt(idPrestadorServicio)));
        nuevoConsumoServicio.setIdServicio(servicioFacade.find(Integer.parseInt(idServicioManual)));
        nuevoConsumoServicio.setFecha(fechaServicio);
        nuevoConsumoServicio.setCantidad(cantidadServicio);
        nuevoConsumoServicio.setDiagnosticoPrincipal(diagnosticoPrincipal);
        nuevoConsumoServicio.setDiagnosticoRelacionado(diagnosticoRelacionado);
        nuevoConsumoServicio.setTipoTarifa(tipoTarifaServicio);
        nuevoConsumoServicio.setValorUnitario(valorUnitarioServicio);
        nuevoConsumoServicio.setValorFinal(valorFinalServicio);
        consumoServicioFacade.create(nuevoConsumoServicio);
        pacienteSeleccionado = pacientesFacade.find(pacienteSeleccionado.getIdPaciente());//recargar el manual        
        recargarListas();
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarServicio').hide(); PF('wvTablaServiciosConsumo').clearFilters(); PF('wvTablaServiciosConsumo').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("IdFormConsumos:IdTabView");
    }

    public void quitarServicio() {
        if (servicioConsumoSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún servicio de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoQuitarServicio').show();");
    }

    public void confirmarQuitarServicio() {
        if (servicioConsumoSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún servicio", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            FacConsumoServicio consumoServicioBuscado = consumoServicioFacade.find(Integer.parseInt(servicioConsumoSeleccionado.getColumna1()));
            consumoServicioFacade.remove(consumoServicioBuscado);
            pacienteSeleccionado = pacientesFacade.find(pacienteSeleccionado.getIdPaciente());
            recargarListas();
            RequestContext.getCurrentInstance().update("IdFormConsumos:IdTabView");
            RequestContext.getCurrentInstance().execute("PF('dialogoQuitarServicio').hide(); PF('wvTablaServiciosConsumo').clearFilters(); PF('wvTablaServiciosConsumo').getPaginator().setPage(0);");
        } catch (Exception e) {
            imprimirMensaje("Error", "El servicio que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    //---------------------------------------------------
    //-----------------FUNCIONES PAQUETES ---------------
    //---------------------------------------------------    
    public void cambiaPaquete() {//se debe determinar que tipos de tarifas contiene el servicio
        idPrestadorPaquete = "";
        fechaPaquete = new Date();
        //tipoTarifaPaquete = "";
        cantidadPaquete = 1;
        valorUnitarioPaquete = 0;
        valorFinalPaquete = 0;
        calcularValoresPaquete();
    }

    private FacManualTarifarioPaquete buscarEnListaPaquetesManual(int idPaquete) {
        if (listaPaquetesManual != null && !listaPaquetesManual.isEmpty()) {
            for (FacManualTarifarioPaquete servicioManual : listaPaquetesManual) {
                if (servicioManual.getFacPaquete().getIdPaquete() == idPaquete) {
                    return servicioManual;
                }
            }
        }
        return null;
    }

    public void calcularValoresPaquete() {
        if (validarNoVacio(idPaqueteManual)) {
            FacManualTarifarioPaquete s = buscarEnListaPaquetesManual(Integer.parseInt(idPaqueteManual));
            //tipoTarifaPaquete = s.getTipoTarifa();
            valorUnitarioPaquete = s.getValorInicial();
            valorFinalPaquete = valorUnitarioPaquete * cantidadPaquete;
        }
    }

    public void cargarDialogoAgregarPaquete() {
        listaPaquetesManual = manualTarifarioPaciente.getFacManualTarifarioPaqueteList();
        if (listaPaquetesManual != null && !listaPaquetesManual.isEmpty()) {
            idPaqueteManual = listaPaquetesManual.get(0).getFacPaquete().getIdPaquete().toString();
        }
        cambiaPaquete();
        RequestContext.getCurrentInstance().update("IdFormDialogs:IdPanelAgregarPaquete");
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarPaquete').show();");
    }

    public void agregarPaquete() {
        if (validacionCampoVacio(idPaqueteManual, "Paquete")) {
            return;
        }
        if (validacionCampoVacio(idPrestadorPaquete, "Prestador")) {
            return;
        }
        if (fechaPaquete == null) {
            imprimirMensaje("Error", "Debe ingresar una fecha", FacesMessage.SEVERITY_ERROR);
            return;
        }
        FacConsumoPaquete nuevoConsumoPaquete = new FacConsumoPaquete();
        nuevoConsumoPaquete.setIdPaciente(pacienteSeleccionado);
        nuevoConsumoPaquete.setIdPrestador(usuariosFacade.find(Integer.parseInt(idPrestadorPaquete)));
        nuevoConsumoPaquete.setIdPaquete(paqueteFacade.find(Integer.parseInt(idPaqueteManual)));
        nuevoConsumoPaquete.setFecha(fechaPaquete);
        nuevoConsumoPaquete.setCantidad(cantidadPaquete);
        //nuevoConsumoPaquete.setTipoTarifa(clasificacionesFacade.find(Integer.parseInt(tipoTarifaPaquete)));
        nuevoConsumoPaquete.setValorUnitario(valorUnitarioPaquete);
        nuevoConsumoPaquete.setValorFinal(valorFinalPaquete);
        consumoPaqueteFacade.create(nuevoConsumoPaquete);
        pacienteSeleccionado = pacientesFacade.find(pacienteSeleccionado.getIdPaciente());//recargar el manual        
        recargarListas();
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarPaquete').hide(); PF('wvTablaPaquetesConsumo').clearFilters(); PF('wvTablaPaquetesConsumo').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("IdFormConsumos:IdTabView");
    }

    public void quitarPaquete() {
        if (paqueteConsumoSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún paquete de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoQuitarPaquete').show();");
    }

    public void confirmarQuitarPaquete() {
        if (paqueteConsumoSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún paquete", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            FacConsumoPaquete consumoPaqueteBuscado = consumoPaqueteFacade.find(Integer.parseInt(paqueteConsumoSeleccionado.getColumna1()));
            consumoPaqueteFacade.remove(consumoPaqueteBuscado);
            pacienteSeleccionado = pacientesFacade.find(pacienteSeleccionado.getIdPaciente());
            recargarListas();
            RequestContext.getCurrentInstance().update("IdFormConsumos:IdTabView");
            RequestContext.getCurrentInstance().execute("PF('dialogoQuitarPaquete').hide(); PF('wvTablaPaquetesConsumo').clearFilters(); PF('wvTablaPaquetesConsumo').getPaginator().setPage(0);");
        } catch (Exception e) {
            imprimirMensaje("Error", "El servicio que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    //---------------------------------------------------
    //-----------------FUNCIONES INSUMOS ---------------
    //---------------------------------------------------    
    public void cambiaInsumo() {//se debe determinar que tipos de tarifas contiene el servicio
        idPrestadorInsumo = "";
        fechaInsumo = new Date();
        //tipoTarifaInsumo = "";
        cantidadInsumo = 1;
        valorUnitarioInsumo = 0;
        valorFinalInsumo = 0;
        calcularValoresInsumo();
    }

    private FacManualTarifarioInsumo buscarEnListaInsumosManual(int idInsumo) {
        if (listaInsumosManual != null && !listaInsumosManual.isEmpty()) {
            for (FacManualTarifarioInsumo servicioManual : listaInsumosManual) {
                if (servicioManual.getCfgInsumo().getIdInsumo() == idInsumo) {
                    return servicioManual;
                }
            }
        }
        return null;
    }

    public void calcularValoresInsumo() {
        if (validarNoVacio(idInsumoManual)) {
            FacManualTarifarioInsumo s = buscarEnListaInsumosManual(Integer.parseInt(idInsumoManual));
            //tipoTarifaInsumo = s.getTipoTarifa();
            valorUnitarioInsumo = s.getValorInicial();
            valorFinalInsumo = valorUnitarioInsumo * cantidadInsumo;
        }
    }

    public void cargarDialogoAgregarInsumo() {
        listaInsumosManual = manualTarifarioPaciente.getFacManualTarifarioInsumoList();
        if (listaInsumosManual != null && !listaInsumosManual.isEmpty()) {
            idInsumoManual = listaInsumosManual.get(0).getCfgInsumo().getIdInsumo().toString();
        }
        cambiaInsumo();
        RequestContext.getCurrentInstance().update("IdFormDialogs:IdPanelAgregarInsumo");
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarInsumo').show();");
    }

    public void agregarInsumo() {
        if (validacionCampoVacio(idInsumoManual, "Insumo")) {
            return;
        }
        if (validacionCampoVacio(idPrestadorInsumo, "Prestador")) {
            return;
        }
        if (fechaInsumo == null) {
            imprimirMensaje("Error", "Debe ingresar una fecha", FacesMessage.SEVERITY_ERROR);
            return;
        }
        FacConsumoInsumo nuevoConsumoInsumo = new FacConsumoInsumo();
        nuevoConsumoInsumo.setIdPaciente(pacienteSeleccionado);
        nuevoConsumoInsumo.setIdPrestador(usuariosFacade.find(Integer.parseInt(idPrestadorInsumo)));
        nuevoConsumoInsumo.setIdInsumo(insumoFacade.find(Integer.parseInt(idInsumoManual)));
        nuevoConsumoInsumo.setFecha(fechaInsumo);
        nuevoConsumoInsumo.setCantidad(cantidadInsumo);
        //nuevoConsumoInsumo.setTipoTarifa(clasificacionesFacade.find(Integer.parseInt(tipoTarifaInsumo)));
        nuevoConsumoInsumo.setValorUnitario(valorUnitarioInsumo);
        nuevoConsumoInsumo.setValorFinal(valorFinalInsumo);
        consumoInsumoFacade.create(nuevoConsumoInsumo);
        pacienteSeleccionado = pacientesFacade.find(pacienteSeleccionado.getIdPaciente());//recargar el manual        
        recargarListas();
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarInsumo').hide(); PF('wvTablaInsumosConsumo').clearFilters(); PF('wvTablaInsumosConsumo').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("IdFormConsumos:IdTabView");
    }

    public void quitarInsumo() {
        if (insumoConsumoSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún inusmo de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoQuitarInsumo').show();");
    }

    public void confirmarQuitarInsumo() {
        if (insumoConsumoSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún insumo", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            FacConsumoInsumo consumoInsumoBuscado = consumoInsumoFacade.find(Integer.parseInt(insumoConsumoSeleccionado.getColumna1()));
            consumoInsumoFacade.remove(consumoInsumoBuscado);
            pacienteSeleccionado = pacientesFacade.find(pacienteSeleccionado.getIdPaciente());
            recargarListas();
            RequestContext.getCurrentInstance().update("IdFormConsumos:IdTabView");
            RequestContext.getCurrentInstance().execute("PF('dialogoQuitarInsumo').hide(); PF('wvTablaInsumosConsumo').clearFilters(); PF('wvTablaInsumosConsumo').getPaginator().setPage(0);");
        } catch (Exception e) {
            imprimirMensaje("Error", "El servicio que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    //---------------------------------------------------
    //-----------------FUNCIONES MEDICAMENTOS -----------
    //---------------------------------------------------    
    public void cambiaMedicamento() {//se debe determinar que tipos de tarifas contiene el servicio
        idPrestadorMedicamento = "";
        fechaMedicamento = new Date();
        //tipoTarifaMedicamento = "";
        cantidadMedicamento = 1;
        valorUnitarioMedicamento = 0;
        valorFinalMedicamento = 0;
        calcularValoresMedicamento();
    }

    private FacManualTarifarioMedicamento buscarEnListaMedicamentosManual(int idMedicamento) {
        if (listaMedicamentosManual != null && !listaMedicamentosManual.isEmpty()) {
            for (FacManualTarifarioMedicamento servicioManual : listaMedicamentosManual) {
                if (servicioManual.getCfgMedicamento().getIdMedicamento() == idMedicamento) {
                    return servicioManual;
                }
            }
        }
        return null;
    }

    public void calcularValoresMedicamento() {
        if (validarNoVacio(idMedicamentoManual)) {
            FacManualTarifarioMedicamento s = buscarEnListaMedicamentosManual(Integer.parseInt(idMedicamentoManual));
            //tipoTarifaMedicamento = s.getTipoTarifa();
            valorUnitarioMedicamento = s.getValorInicial();
            valorFinalMedicamento = valorUnitarioMedicamento * cantidadMedicamento;
        }
    }

    public void cargarDialogoAgregarMedicamento() {
        listaMedicamentosManual = manualTarifarioPaciente.getFacManualTarifarioMedicamentoList();
        if (listaMedicamentosManual != null && !listaMedicamentosManual.isEmpty()) {
            idMedicamentoManual = listaMedicamentosManual.get(0).getCfgMedicamento().getIdMedicamento().toString();
        }
        cambiaMedicamento();
        RequestContext.getCurrentInstance().update("IdFormDialogs:IdPanelAgregarMedicamento");
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarMedicamento').show();");
    }

    public void agregarMedicamento() {
        if (validacionCampoVacio(idMedicamentoManual, "Medicamento")) {
            return;
        }
        if (validacionCampoVacio(idPrestadorMedicamento, "Prestador")) {
            return;
        }
        if (fechaMedicamento == null) {
            imprimirMensaje("Error", "Debe ingresar una fecha", FacesMessage.SEVERITY_ERROR);
            return;
        }
        FacConsumoMedicamento nuevoConsumoMedicamento = new FacConsumoMedicamento();
        nuevoConsumoMedicamento.setIdPaciente(pacienteSeleccionado);
        nuevoConsumoMedicamento.setIdPrestador(usuariosFacade.find(Integer.parseInt(idPrestadorMedicamento)));
        nuevoConsumoMedicamento.setIdMedicamento(medicamentoFacade.find(Integer.parseInt(idMedicamentoManual)));
        nuevoConsumoMedicamento.setFecha(fechaMedicamento);
        nuevoConsumoMedicamento.setCantidad(cantidadMedicamento);
        //nuevoConsumoMedicamento.setTipoTarifa(clasificacionesFacade.find(Integer.parseInt(tipoTarifaMedicamento)));
        nuevoConsumoMedicamento.setValorUnitario(valorUnitarioMedicamento);
        nuevoConsumoMedicamento.setValorFinal(valorFinalMedicamento);
        consumoMedicamentoFacade.create(nuevoConsumoMedicamento);
        pacienteSeleccionado = pacientesFacade.find(pacienteSeleccionado.getIdPaciente());//recargar el manual        
        recargarListas();
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarMedicamento').hide(); PF('wvTablaMedicamentosConsumo').clearFilters(); PF('wvTablaMedicamentosConsumo').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("IdFormConsumos:IdTabView");
    }

    public void quitarMedicamento() {
        if (medicamentoConsumoSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún medicamento de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoQuitarMedicamento').show();");
    }

    public void confirmarQuitarMedicamento() {
        if (medicamentoConsumoSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún medicamento", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            FacConsumoMedicamento consumoMedicamentoBuscado = consumoMedicamentoFacade.find(Integer.parseInt(medicamentoConsumoSeleccionado.getColumna1()));
            consumoMedicamentoFacade.remove(consumoMedicamentoBuscado);
            pacienteSeleccionado = pacientesFacade.find(pacienteSeleccionado.getIdPaciente());
            recargarListas();
            RequestContext.getCurrentInstance().update("IdFormConsumos:IdTabView");
            RequestContext.getCurrentInstance().execute("PF('dialogoQuitarMedicamento').hide(); PF('wvTablaMedicamentosConsumo').clearFilters(); PF('wvTablaMedicamentosConsumo').getPaginator().setPage(0);");
        } catch (Exception e) {
            imprimirMensaje("Error", "El servicio que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------    
    public LazyDataModel<CfgPacientes> getListaPacientes() {
        return listaPacientes;
    }

    public void setListaPacientes(LazyDataModel<CfgPacientes> listaPacientes) {
        this.listaPacientes = listaPacientes;
    }

    public CfgPacientes getPacienteTmp() {
        return pacienteTmp;
    }

    public void setPacienteTmp(CfgPacientes pacienteTmp) {
        this.pacienteTmp = pacienteTmp;
    }

    public CfgPacientes getPacienteSeleccionadoTabla() {
        return pacienteSeleccionadoTabla;
    }

    public void setPacienteSeleccionadoTabla(CfgPacientes pacienteSeleccionadoTabla) {
        this.pacienteSeleccionadoTabla = pacienteSeleccionadoTabla;
    }

    public CfgPacientes getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }

    public void setPacienteSeleccionado(CfgPacientes pacienteSeleccionado) {
        this.pacienteSeleccionado = pacienteSeleccionado;
    }

    public String getIdentificacionPaciente() {
        return identificacionPaciente;
    }

    public void setIdentificacionPaciente(String identificacionPaciente) {
        this.identificacionPaciente = identificacionPaciente;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getGeneroPaciente() {
        return generoPaciente;
    }

    public void setGeneroPaciente(String generoPaciente) {
        this.generoPaciente = generoPaciente;
    }

    public String getEdadPaciente() {
        return edadPaciente;
    }

    public void setEdadPaciente(String edadPaciente) {
        this.edadPaciente = edadPaciente;
    }

    public String getAdministradoraPaciente() {
        return administradoraPaciente;
    }

    public void setAdministradoraPaciente(String administradoraPaciente) {
        this.administradoraPaciente = administradoraPaciente;
    }

    public FilaDataTable getInsumoConsumoSeleccionado() {
        return insumoConsumoSeleccionado;
    }

    public void setInsumoConsumoSeleccionado(FilaDataTable insumoConsumoSeleccionado) {
        this.insumoConsumoSeleccionado = insumoConsumoSeleccionado;
    }

    public String getIdPrestadorServicio() {
        return idPrestadorServicio;
    }

    public void setIdPrestadorServicio(String idPrestadorServicio) {
        this.idPrestadorServicio = idPrestadorServicio;
    }

    public Date getFechaServicio() {
        return fechaServicio;
    }

    public void setFechaServicio(Date fechaServicio) {
        this.fechaServicio = fechaServicio;
    }

    public int getCantidadServicio() {
        return cantidadServicio;
    }

    public void setCantidadServicio(int cantidadServicio) {
        this.cantidadServicio = cantidadServicio;
    }

    public String getIdPrestadorInsumo() {
        return idPrestadorInsumo;
    }

    public void setIdPrestadorInsumo(String idPrestadorInsumo) {
        this.idPrestadorInsumo = idPrestadorInsumo;
    }

    public Date getFechaInsumo() {
        return fechaInsumo;
    }

    public void setFechaInsumo(Date fechaInsumo) {
        this.fechaInsumo = fechaInsumo;
    }

    public int getCantidadInsumo() {
        return cantidadInsumo;
    }

    public void setCantidadInsumo(int cantidadInsumo) {
        this.cantidadInsumo = cantidadInsumo;
    }

    public String getIdPrestadorMedicamento() {
        return idPrestadorMedicamento;
    }

    public void setIdPrestadorMedicamento(String idPrestadorMedicamento) {
        this.idPrestadorMedicamento = idPrestadorMedicamento;
    }

    public Date getFechaMedicamento() {
        return fechaMedicamento;
    }

    public void setFechaMedicamento(Date fechaMedicamento) {
        this.fechaMedicamento = fechaMedicamento;
    }

    public int getCantidadMedicamento() {
        return cantidadMedicamento;
    }

    public void setCantidadMedicamento(int cantidadMedicamento) {
        this.cantidadMedicamento = cantidadMedicamento;
    }

    public String getIdPrestadorPaquete() {
        return idPrestadorPaquete;
    }

    public void setIdPrestadorPaquete(String idPrestadorPaquete) {
        this.idPrestadorPaquete = idPrestadorPaquete;
    }

    public Date getFechaPaquete() {
        return fechaPaquete;
    }

    public void setFechaPaquete(Date fechaPaquete) {
        this.fechaPaquete = fechaPaquete;
    }

    public int getCantidadPaquete() {
        return cantidadPaquete;
    }

    public void setCantidadPaquete(int cantidadPaquete) {
        this.cantidadPaquete = cantidadPaquete;
    }

    public List<FilaDataTable> getListaServiciosConsumo() {
        return listaServiciosConsumo;
    }

    public void setListaServiciosConsumo(List<FilaDataTable> listaServiciosConsumo) {
        this.listaServiciosConsumo = listaServiciosConsumo;
    }

    public List<FilaDataTable> getListaInsumosConsumo() {
        return listaInsumosConsumo;
    }

    public void setListaInsumosConsumo(List<FilaDataTable> listaInsumosConsumo) {
        this.listaInsumosConsumo = listaInsumosConsumo;
    }

    public List<FilaDataTable> getListaMedicamentosConsumo() {
        return listaMedicamentosConsumo;
    }

    public void setListaMedicamentosConsumo(List<FilaDataTable> listaMedicamentosConsumo) {
        this.listaMedicamentosConsumo = listaMedicamentosConsumo;
    }

    public List<FilaDataTable> getListaPaquetesConsumo() {
        return listaPaquetesConsumo;
    }

    public void setListaPaquetesConsumo(List<FilaDataTable> listaPaquetesConsumo) {
        this.listaPaquetesConsumo = listaPaquetesConsumo;
    }

    public List<FilaDataTable> getListaServiciosConsumoFiltro() {
        return listaServiciosConsumoFiltro;
    }

    public void setListaServiciosConsumoFiltro(List<FilaDataTable> listaServiciosConsumoFiltro) {
        this.listaServiciosConsumoFiltro = listaServiciosConsumoFiltro;
    }

    public List<FilaDataTable> getListaInsumosConsumoFiltro() {
        return listaInsumosConsumoFiltro;
    }

    public void setListaInsumosConsumoFiltro(List<FilaDataTable> listaInsumosConsumoFiltro) {
        this.listaInsumosConsumoFiltro = listaInsumosConsumoFiltro;
    }

    public List<FilaDataTable> getListaMedicamentosConsumoFiltro() {
        return listaMedicamentosConsumoFiltro;
    }

    public void setListaMedicamentosConsumoFiltro(List<FilaDataTable> listaMedicamentosConsumoFiltro) {
        this.listaMedicamentosConsumoFiltro = listaMedicamentosConsumoFiltro;
    }

    public List<FilaDataTable> getListaPaquetesConsumoFiltro() {
        return listaPaquetesConsumoFiltro;
    }

    public void setListaPaquetesConsumoFiltro(List<FilaDataTable> listaPaquetesConsumoFiltro) {
        this.listaPaquetesConsumoFiltro = listaPaquetesConsumoFiltro;
    }

    public FilaDataTable getServicioConsumoSeleccionado() {
        return servicioConsumoSeleccionado;
    }

    public void setServicioConsumoSeleccionado(FilaDataTable servicioConsumoSeleccionado) {
        this.servicioConsumoSeleccionado = servicioConsumoSeleccionado;
    }

    public FilaDataTable getMedicamentoConsumoSeleccionado() {
        return medicamentoConsumoSeleccionado;
    }

    public void setMedicamentoConsumoSeleccionado(FilaDataTable medicamentoConsumoSeleccionado) {
        this.medicamentoConsumoSeleccionado = medicamentoConsumoSeleccionado;
    }

    public FilaDataTable getPaqueteConsumoSeleccionado() {
        return paqueteConsumoSeleccionado;
    }

    public void setPaqueteConsumoSeleccionado(FilaDataTable paqueteConsumoSeleccionado) {
        this.paqueteConsumoSeleccionado = paqueteConsumoSeleccionado;
    }

    public String getTituloTabServicios() {
        return tituloTabServicios;
    }

    public void setTituloTabServicios(String tituloTabServicios) {
        this.tituloTabServicios = tituloTabServicios;
    }

    public String getTipoTarifaServicio() {
        return tipoTarifaServicio;
    }

    public void setTipoTarifaServicio(String tipoTarifaServicio) {
        this.tipoTarifaServicio = tipoTarifaServicio;
    }

    public double getValorUnitarioServicio() {
        return valorUnitarioServicio;
    }

    public void setValorUnitarioServicio(double valorUnitarioServicio) {
        this.valorUnitarioServicio = valorUnitarioServicio;
    }

    public double getValorFinalServicio() {
        return valorFinalServicio;
    }

    public void setValorFinalServicio(double valorFinalServicio) {
        this.valorFinalServicio = valorFinalServicio;
    }

    public String getTituloTabInsumos() {
        return tituloTabInsumos;
    }

    public void setTituloTabInsumos(String tituloTabInsumos) {
        this.tituloTabInsumos = tituloTabInsumos;
    }

//    public String getTipoTarifaInsumo() {
//        return tipoTarifaInsumo;
//    }
//
//    public void setTipoTarifaInsumo(String tipoTarifaInsumo) {
//        this.tipoTarifaInsumo = tipoTarifaInsumo;
//    }
    public double getValorUnitarioInsumo() {
        return valorUnitarioInsumo;
    }

    public void setValorUnitarioInsumo(double valorUnitarioInsumo) {
        this.valorUnitarioInsumo = valorUnitarioInsumo;
    }

    public double getValorFinalInsumo() {
        return valorFinalInsumo;
    }

    public void setValorFinalInsumo(double valorFinalInsumo) {
        this.valorFinalInsumo = valorFinalInsumo;
    }

    public String getTituloTabMedicamentos() {
        return tituloTabMedicamentos;
    }

    public void setTituloTabMedicamentos(String tituloTabMedicamentos) {
        this.tituloTabMedicamentos = tituloTabMedicamentos;
    }

//    public String getTipoTarifaMedicamento() {
//        return tipoTarifaMedicamento;
//    }
//
//    public void setTipoTarifaMedicamento(String tipoTarifaMedicamento) {
//        this.tipoTarifaMedicamento = tipoTarifaMedicamento;
//    }
    public double getValorUnitarioMedicamento() {
        return valorUnitarioMedicamento;
    }

    public void setValorUnitarioMedicamento(double valorUnitarioMedicamento) {
        this.valorUnitarioMedicamento = valorUnitarioMedicamento;
    }

    public double getValorFinalMedicamento() {
        return valorFinalMedicamento;
    }

    public void setValorFinalMedicamento(double valorFinalMedicamento) {
        this.valorFinalMedicamento = valorFinalMedicamento;
    }

    public String getTituloTabPaquetes() {
        return tituloTabPaquetes;
    }

    public void setTituloTabPaquetes(String tituloTabPaquetes) {
        this.tituloTabPaquetes = tituloTabPaquetes;
    }

//    public String getTipoTarifaPaquete() {
//        return tipoTarifaPaquete;
//    }
//
//    public void setTipoTarifaPaquete(String tipoTarifaPaquete) {
//        this.tipoTarifaPaquete = tipoTarifaPaquete;
//    }
    public double getValorUnitarioPaquete() {
        return valorUnitarioPaquete;
    }

    public void setValorUnitarioPaquete(double valorUnitarioPaquete) {
        this.valorUnitarioPaquete = valorUnitarioPaquete;
    }

    public double getValorFinalPaquete() {
        return valorFinalPaquete;
    }

    public void setValorFinalPaquete(double valorFinalPaquete) {
        this.valorFinalPaquete = valorFinalPaquete;
    }

    public String getMensajeConfiguracion() {
        return mensajeConfiguracion;
    }

    public void setMensajeConfiguracion(String mensajeConfiguracion) {
        this.mensajeConfiguracion = mensajeConfiguracion;
    }

    public String getIdServicioManual() {
        return idServicioManual;
    }

    public void setIdServicioManual(String idServicioManual) {
        this.idServicioManual = idServicioManual;
    }

    public String getIdInsumoManual() {
        return idInsumoManual;
    }

    public void setIdInsumoManual(String idInsumoManual) {
        this.idInsumoManual = idInsumoManual;
    }

    public String getIdMedicamentoManual() {
        return idMedicamentoManual;
    }

    public void setIdMedicamentoManual(String idMedicamentoManual) {
        this.idMedicamentoManual = idMedicamentoManual;
    }

    public String getIdPaqueteManual() {
        return idPaqueteManual;
    }

    public void setIdPaqueteManual(String idPaqueteManual) {
        this.idPaqueteManual = idPaqueteManual;
    }

    public List<FacManualTarifarioServicio> getListaServiciosManual() {
        return listaServiciosManual;
    }

    public void setListaServiciosManual(List<FacManualTarifarioServicio> listaServiciosManual) {
        this.listaServiciosManual = listaServiciosManual;
    }

    public List<FacManualTarifarioInsumo> getListaInsumosManual() {
        return listaInsumosManual;
    }

    public void setListaInsumosManual(List<FacManualTarifarioInsumo> listaInsumosManual) {
        this.listaInsumosManual = listaInsumosManual;
    }

    public List<FacManualTarifarioMedicamento> getListaMedicamentosManual() {
        return listaMedicamentosManual;
    }

    public void setListaMedicamentosManual(List<FacManualTarifarioMedicamento> listaMedicamentosManual) {
        this.listaMedicamentosManual = listaMedicamentosManual;
    }

    public List<FacManualTarifarioPaquete> getListaPaquetesManual() {
        return listaPaquetesManual;
    }

    public void setListaPaquetesManual(List<FacManualTarifarioPaquete> listaPaquetesManual) {
        this.listaPaquetesManual = listaPaquetesManual;
    }

    public String getDiagnosticoPrincipal() {
        return diagnosticoPrincipal;
    }

    public void setDiagnosticoPrincipal(String diagnosticoPrincipal) {
        this.diagnosticoPrincipal = diagnosticoPrincipal;
    }

    public String getDiagnosticoRelacionado() {
        return diagnosticoRelacionado;
    }

    public void setDiagnosticoRelacionado(String diagnosticoRelacionado) {
        this.diagnosticoRelacionado = diagnosticoRelacionado;
    }

}
