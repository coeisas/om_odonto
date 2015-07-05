/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import beans.utilidades.ConversorDeNumerosALetras;
import beans.utilidades.FilaDataTable;
import beans.utilidades.LazyPacienteDataModel;
import beans.utilidades.MetodosGenerales;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.CfgPacientes;
import modelo.entidades.CitAutorizaciones;
import modelo.entidades.CitCitas;
import modelo.entidades.CitTurnos;
import modelo.entidades.FacAdministradora;
import modelo.entidades.FacCaja;
import modelo.entidades.FacConsecutivo;
import modelo.entidades.FacConsumoInsumo;
import modelo.entidades.FacConsumoMedicamento;
import modelo.entidades.FacConsumoPaquete;
import modelo.entidades.FacConsumoServicio;
import modelo.entidades.FacContrato;
import modelo.entidades.FacFacturaInsumo;
import modelo.entidades.FacFacturaInsumoPK;
import modelo.entidades.FacFacturaMedicamento;
import modelo.entidades.FacFacturaMedicamentoPK;
import modelo.entidades.FacFacturaPaciente;
import modelo.entidades.FacFacturaPaquete;
import modelo.entidades.FacFacturaPaquetePK;
import modelo.entidades.FacFacturaServicio;
import modelo.entidades.FacFacturaServicioPK;
import modelo.entidades.FacImpuestos;
import modelo.entidades.FacManualTarifario;
import modelo.entidades.FacManualTarifarioInsumo;
import modelo.entidades.FacManualTarifarioMedicamento;
import modelo.entidades.FacManualTarifarioPaquete;
import modelo.entidades.FacManualTarifarioServicio;
import modelo.entidades.FacPeriodo;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgDiagnosticoFacade;
import modelo.fachadas.CfgInsumoFacade;
import modelo.fachadas.CfgMedicamentoFacade;
import modelo.fachadas.CfgPacientesFacade;
import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.CitAutorizacionesFacade;
import modelo.fachadas.CitCitasFacade;
import modelo.fachadas.CitTurnosFacade;
import modelo.fachadas.FacAdministradoraFacade;
import modelo.fachadas.FacCajaFacade;
import modelo.fachadas.FacConsecutivoFacade;
import modelo.fachadas.FacConsumoInsumoFacade;
import modelo.fachadas.FacConsumoMedicamentoFacade;
import modelo.fachadas.FacConsumoPaqueteFacade;
import modelo.fachadas.FacConsumoServicioFacade;
import modelo.fachadas.FacContratoFacade;
import modelo.fachadas.FacFacturaInsumoFacade;
import modelo.fachadas.FacFacturaMedicamentoFacade;
import modelo.fachadas.FacFacturaPacienteFacade;
import modelo.fachadas.FacFacturaPaqueteFacade;
import modelo.fachadas.FacFacturaServicioFacade;
import modelo.fachadas.FacImpuestosFacade;
import modelo.fachadas.FacManualTarifarioFacade;
import modelo.fachadas.FacPaqueteFacade;
import modelo.fachadas.FacPeriodoFacade;
import modelo.fachadas.FacServicioFacade;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author santos
 */
@ManagedBean(name = "facturarPacienteMB")
@SessionScoped
public class FacturarPacienteMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    FacFacturaPacienteFacade facturaPacienteFacade;
    @EJB
    CfgPacientesFacade pacientesFacade;
    @EJB
    CitCitasFacade citasFacade;
    @EJB
    CitAutorizacionesFacade autorizacionesFacade;
    @EJB
    CitTurnosFacade turnosFacade;
    @EJB
    CfgUsuariosFacade usuariosFacade;
    @EJB
    FacCajaFacade cajaFacade;
    @EJB
    FacPeriodoFacade periodoFacade;
    @EJB
    FacServicioFacade servicioFacade;
    @EJB
    CfgMedicamentoFacade medicamentoFacade;
    @EJB
    CfgInsumoFacade insumoFacade;
    @EJB
    FacPaqueteFacade paqueteFacade;
    @EJB
    FacImpuestosFacade impuestosFacade;
    @EJB
    CfgClasificacionesFacade clasificacionesFachada;
    @EJB
    FacConsecutivoFacade consecutivoFacade;
    @EJB
    FacManualTarifarioFacade manualTarifarioFacade;
    @EJB
    FacAdministradoraFacade administradoraFacade;
    @EJB
    FacContratoFacade contratoFacade;
    @EJB
    CfgDiagnosticoFacade diagnosticoFacade;

    @EJB
    FacFacturaServicioFacade facturaServicioFacade;
    @EJB
    FacFacturaInsumoFacade facturaInsumoFacade;
    @EJB
    FacFacturaMedicamentoFacade facturaMedicamentoFacade;
    @EJB
    FacFacturaPaqueteFacade facturaPaqueteFacade;

    @EJB
    FacConsumoServicioFacade consumoServicioFacade;
    @EJB
    FacConsumoMedicamentoFacade consumoMedicamentoFacade;
    @EJB
    FacConsumoInsumoFacade consumoInsumoFacade;
    @EJB
    FacConsumoPaqueteFacade consumoPaqueteFacade;

    //---------------------------------------------------
    //-----------------ENTIDADES ------------------------
    //---------------------------------------------------
    private FacContrato contratoActual;
    private FacAdministradora administradoraActual;
    private LazyDataModel<FilaDataTable> listaFacturas;
    private FacFacturaPaciente facturaSeleccionada;
    private FilaDataTable facturaSeleccionadaTabla;
    private LazyDataModel<CfgPacientes> listaPacientes;

    private CfgPacientes pacienteTmp;
    private CfgPacientes pacienteSeleccionadoTabla;
    private CfgPacientes pacienteSeleccionado;
    private FacManualTarifario manualTarifarioPaciente;
    private FacConsecutivo consecutivoSeleccionado;
    private FacCaja cajaSeleccionada;
    private FacPeriodo periodoSeleccionado;

    private List<FacManualTarifarioServicio> listaServiciosManual;
    private List<FacManualTarifarioInsumo> listaInsumosManual;
    private List<FacManualTarifarioMedicamento> listaMedicamentosManual;
    private List<FacManualTarifarioPaquete> listaPaquetesManual;
    private List<FacCaja> listaCajas;
    private List<CfgClasificaciones> listaDocumentos;

    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------
    private LoginMB loginMB;
    private String tipoDocumentoFacturaSeleccionada = "";//determinar si se imprime pagina completa o media pagina
    private List<EstructuraFacturaPaciente> listaRegistrosFactura;
    private List<EstructuraReciboCaja> listaRegistrosReciboCaja;
    private final SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    private final SimpleDateFormat formateadorFechaSinHora = new SimpleDateFormat("dd-MM-yyyy");
    private final DecimalFormat formateadorDecimal = new DecimalFormat("0.00");
    private boolean disableControlesBuscarFactura = true;
    private boolean facturarComoParticular = false;//saber si se debe cargar el manual tarifario propio o el de la administradora particular
    private boolean facturarComoParticularDisabled = false;//si administradora no es particular se puede facturar como administradora
    private String msjBtnFacturarParticular = "Facturar como particular: NO";
    private boolean cargandoDesdeTab = false;

    //------- CONSUMOS --------------
    private String tituloTabServiciosConsumo = "Servicios (0)";
    private String tituloTabInsumosConsumo = "Insumos (0)";
    private String tituloTabMedicamentosConsumo = "Medicamentos (0)";
    private String tituloTabPaquetesConsumo = "Paquetes (0)";

    private List<FilaDataTable> listaServiciosConsumo = new ArrayList<>();
    private List<FilaDataTable> listaInsumosConsumo = new ArrayList<>();
    private List<FilaDataTable> listaMedicamentosConsumo = new ArrayList<>();
    private List<FilaDataTable> listaPaquetesConsumo = new ArrayList<>();
    private List<FilaDataTable> listaServiciosConsumoFiltro = new ArrayList<>();
    private List<FilaDataTable> listaInsumosConsumoFiltro = new ArrayList<>();
    private List<FilaDataTable> listaMedicamentosConsumoFiltro = new ArrayList<>();
    private List<FilaDataTable> listaPaquetesConsumoFiltro = new ArrayList<>();
    private List<FilaDataTable> serviciosConsumoSeleccionados = new ArrayList<>();
    private List<FilaDataTable> insumosConsumoSeleccionados = new ArrayList<>();
    private List<FilaDataTable> medicamentosConsumoSeleccionados = new ArrayList<>();
    private List<FilaDataTable> paquetesConsumoSeleccionados = new ArrayList<>();

    //------- FACTURA --------------    
    private List<FilaDataTable> listaServiciosFactura = new ArrayList<>();
    private List<FilaDataTable> listaInsumosFactura = new ArrayList<>();
    private List<FilaDataTable> listaMedicamentosFactura = new ArrayList<>();
    private List<FilaDataTable> listaPaquetesFactura = new ArrayList<>();
    private List<FilaDataTable> listaServiciosFacturaFiltro = new ArrayList<>();
    private List<FilaDataTable> listaInsumosFacturaFiltro = new ArrayList<>();
    private List<FilaDataTable> listaMedicamentosFacturaFiltro = new ArrayList<>();
    private List<FilaDataTable> listaPaquetesFacturaFiltro = new ArrayList<>();
    private FilaDataTable servicioFacturaSeleccionado = null;
    private FilaDataTable insumoFacturaSeleccionado = null;
    private FilaDataTable medicamentoFacturaSeleccionado = null;
    private FilaDataTable paqueteFacturaSeleccionado = null;

    //-------------PACIENTES--------------
    private String identificacionPaciente = "";
    private String tipoIdentificacion = "";
    private String nombrePaciente = "Paciente";
    private String generoPaciente = "";
    private String edadPaciente = "";

    private String administradoraPaciente = "";
    private String msjHtmlAdministradoraPaciente = "";//mensaje informativo sobre estado de la caja
    private String estiloAdministradoraPaciente = "";

    private String msjHtmlBtnConsumos = "";//mensaje informativo sobre estado de la caja
    private String estiloBtnConsumos = "";

    //------- FACTURA --------------
    private String turnoCita = "";//turno al cual corresponde la cita
    private String msjHtmlTurnoCita = "";//mensaje informativo sobre estado de la caja
    private String estiloTurnoCita = "";

    private CitCitas citaActual = null;
    private CitTurnos turnoActual = null;

    private String caja = "";//caja asociada al usuario actual       
    private String msjHtmlCaja = "";//mensaje informativo sobre estado de la caja
    private String estiloCaja = "";

    private Date fecha = new Date();
    private String msjHtmlPeriodo = ""; //mensaje informativo soble estado del periodo: Abierto, Cerrado, Sin Crear
    private String estiloPeriodo = "";

    private String documento = "";//factura, cuenta cobro, recibo.
    private String msjHtmlDocumento = ""; //mensaje informativo sobre el tipo Documento 
    private String estiloDocumento = "";

    private double valorIva = 0;//porcentage correspondiente al iva
    private String valorIvaStr = "--% (--)";//sigue el formato 16%(3500)
    private String msjHtmlIva = "";
    private String estiloIva = "";

    private double valorCree = 0;//porcentage correspondiente al cree
    private String valorCreeStr = "--% (--)";//sigue el formato 16%(3500)
    private String msjHtmlCree = "";
    private String estiloCree = "";

    private String mensajeConfiguracion = null;//permite informar problemas al querer facturar

    private String idFactura;
    private String idSede = "";//sale de principal pero puede cambiarla    
    private String consecutivo = "";//numero de consecutivo        
    private String observaciones = "";//observaciones de la factura
    private String observacionAnulacion = "";

    private double valorEmpresa = 0;
    private double valorUsuario = 0;
    private double valorParcial = 0;
    private double valorTotal = 0;

    private double valorCopago = 0;
    private String msjHtmlCopago = "";

    private double valorCuotaModeradora = 0;
    private String msjHtmlCuotaModeradora = "";

    //-------------SERVICICOS--------------
    private String tituloTabServiciosFactura = "Servicios (0)";
    private String idPrestadorServicio = "";
    private String idServicioManual = "";
    private Date fechaServicio;
    private String diagnosticoPrincipal = "";
    private String diagnosticoRelacionado = "";
    private int cantidadServicio = 1;
    private String tipoTarifaServicio;
    private double valorUnitarioServicio = 0;
    private double valorFinalServicio = 0;
    //-------------INSUMOS--------------
    private String tituloTabInsumosFactura = "Insumos (0)";
    private String idPrestadorInsumo = "";
    private String idInsumoManual = "";
    private Date fechaInsumo;
    private int cantidadInsumo = 1;
    private double valorUnitarioInsumo = 0;
    private double valorFinalInsumo = 0;
    //-------------MEDICAMENTOS--------------
    private String tituloTabMedicamentosFactura = "Medicamentos (0)";
    private String idPrestadorMedicamento = "";
    private String idMedicamentoManual = "";
    private Date fechaMedicamento;
    private int cantidadMedicamento = 1;
    private double valorUnitarioMedicamento = 0;
    private double valorFinalMedicamento = 0;
    //-------------PAQUETES--------------
    private String tituloTabPaquetesFactura = "Paquetes (0)";
    private String idPrestadorPaquete = "";
    private String idPaqueteManual = "";
    private Date fechaPaquete;
    private int cantidadPaquete = 1;
    private double valorUnitarioPaquete = 0;
    private double valorFinalPaquete = 0;

    private List<FacContrato> listaContratosAplican;//contratos que se pueden aplicar para la facturacion
    private String idContratoActual;

    //---------------------------------------------------
    //------------- FUNCIONES INICIALES  ----------------
    //---------------------------------------------------  
    @PostConstruct
    public void inicializar() {
        listaPacientes = new LazyPacienteDataModel(pacientesFacade);
        listaFacturas = new LazyFacturasDataModel(facturaPacienteFacade);//toma solo facturas de pacientes
    }

    public FacturarPacienteMB() {
    }

    public void cargarDesdeTab(String id) {//cargar paciente y servicios desde tab recepcionCitas
        //System.out.println("Cargar desde tab");
        cargandoDesdeTab = true;
        turnoCita = "";
        citaActual = null;
        turnoActual = null;
        limpiarFormularioFacturacion();
        String[] splitId = id.split(";");
        if (splitId[0].compareTo("idCita") == 0) {
            citaActual = citasFacade.find(Integer.parseInt(splitId[1]));
            turnoActual = citaActual.getIdTurno();
            turnoCita = citaActual.getIdTurno().getIdTurno().toString();
            //BUSCAR PACIENTE
            pacienteTmp = pacientesFacade.find(citaActual.getIdPaciente().getIdPaciente());
            if (pacienteTmp == null) {
                imprimirMensaje("Error", "No se encontró el paciente correspondiente a la cita", FacesMessage.SEVERITY_ERROR);
                cargandoDesdeTab = false;
                RequestContext.getCurrentInstance().update("IdFormFacturacion");
                RequestContext.getCurrentInstance().update("IdMensajeFacturacion");
                return;
            }
            pacienteSeleccionadoTabla = pacienteTmp;
            facturarComoParticular = citaActual.getIdAdministradora().getIdAdministradora() == 1; //determinar si facturara como particular(puede ocurrir si desde recepcionCItas se cambio a facuturar como particular)
            facturarComoParticular = !facturarComoParticular;//se invierte por que la confirmarCambioManualTarifario() cambia el estado de facturarComoParticular
            confirmarCambioManualTarifario();
            //se vuelven a reasignar las tres variables siguentes por que se llamo a limpiar formulario y quedan nuevamente en null
            citaActual = citasFacade.find(Integer.parseInt(splitId[1]));
            turnoActual = citaActual.getIdTurno();
            turnoCita = citaActual.getIdTurno().getIdTurno().toString();
            //BUSCAR SERVICIO EN EL MANUAL
            if (manualTarifarioPaciente == null || pacienteSeleccionado == null) {
                return;
            }
            FacManualTarifarioServicio servicioEnManualTarifario = null;
            for (FacManualTarifarioServicio servicioManual : manualTarifarioPaciente.getFacManualTarifarioServicioList()) {
                if (Objects.equals(servicioManual.getFacServicio().getIdServicio(), citaActual.getIdServicio().getIdServicio())) {
                    servicioEnManualTarifario = servicioManual;
                    break;
                }
            }
            if (servicioEnManualTarifario == null) {//servicio no se encuentra en el manual tarifario                
                imprimirMensaje("Alerta", "El servicio " + citaActual.getIdServicio().getNombreServicio() + " no se encuentra en el manual tarifario " + manualTarifarioPaciente.getNombreManualTarifario(), FacesMessage.SEVERITY_WARN);
            } else {//servicio si esta en el manual trifario
                fechaServicio = new Date();
                idServicioManual = String.valueOf(servicioEnManualTarifario.getFacManualTarifarioServicioPK().getIdServicio());
                tipoTarifaServicio = servicioEnManualTarifario.getTipoTarifa();
                valorUnitarioServicio = servicioEnManualTarifario.getValorFinal();
                cantidadServicio = 1;
                valorFinalServicio = servicioEnManualTarifario.getValorFinal();
                idPrestadorServicio = citaActual.getIdPrestador().getIdUsuario().toString();
                diagnosticoPrincipal = "";
                diagnosticoRelacionado = "";
                agregarServicioAFactura();
            }
        }
        RequestContext.getCurrentInstance().update("IdFormFacturacion");
        RequestContext.getCurrentInstance().update("IdMensajeFacturacion");
        cargandoDesdeTab = false;
        //System.out.println("FIN Cargar desde tab---------------");
    }

    //---------------------------------------------------
    //-----------------FUNCIONES PACIENTES --------------
    //---------------------------------------------------  
    public List<String> autocompletarDiagnostico(String txt) {//retorna una lista con los diagnosticos que contengan el parametro txt
        if (txt != null && txt.length() > 2) {
            return diagnosticoFacade.autocompletarDiagnostico(txt);
        } else {
            return null;
        }
    }

    public void validarIdentificacion() {//verifica si existe la identificacion de lo contrario abre un dialogo para seleccionar el paciente de una tabla
        //System.out.println("validarIdentificacion");
        pacienteTmp = pacientesFacade.buscarPorIdentificacion(identificacionPaciente);
        if (pacienteTmp != null) {

            pacienteSeleccionadoTabla = pacienteTmp;
            cargarPaciente();
        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgSeleccionarPaciente').show();");
        }
    }

    private boolean validarCaja() {
        //System.out.println("validarCaja");
        listaCajas = new ArrayList<>();
        msjHtmlCaja = "No hay cajas <br/>en el sistema";
        if (loginMB.getUsuarioActual().getIdPerfil().getNombrePerfil().compareTo("SuperUsuario") == 0) {
            listaCajas = cajaFacade.buscarOrdenado();
            if (listaCajas == null || listaCajas.isEmpty()) {
                mensajeConfiguracion = "No se puede facturar: \n"
                        + "Paciente: " + pacienteSeleccionado.nombreCompleto() + " \n"
                        + "Razón: No existen cajas ";
                return false;
            } else {
                if (listaCajas.get(0).getCerrada()) {
                    msjHtmlCaja = "La caja <br/>está cerrada";
                    estiloCaja = "border-color: orange; border-width: 2px; border-style: solid; border-radius: 7px 7px 7px 7px;";
                } else {
                    msjHtmlCaja = "Correcto: <br/>Caja abierta";
                    estiloCaja = "";
                }
                cajaSeleccionada = listaCajas.get(0);
                return true;
            }
        } else {
            if (loginMB.getUsuarioActual().getFacCajaList() == null || loginMB.getUsuarioActual().getFacCajaList().isEmpty()) {
                mensajeConfiguracion = "No se puede facturar: \n"
                        + "Paciente: " + pacienteSeleccionado.nombreCompleto() + " \n"
                        + "Razón: El usuario actual no tiene una caja asignada";
                return false;
            } else {
                listaCajas.add(loginMB.getUsuarioActual().getFacCajaList().get(0));
                caja = listaCajas.get(0).getIdCaja().toString();
                if (listaCajas.get(0).getCerrada()) {
                    msjHtmlCaja = "La caja <br/>esta cerrada";
                    estiloCaja = "border-color: orange; border-width: 2px; border-style: solid; border-radius: 7px 7px 7px 7px;";
                } else {
                    msjHtmlCaja = "Correcto: <br/>Caja abierta";
                    estiloCaja = "";
                }
                cajaSeleccionada = listaCajas.get(0);
                return true;
            }
        }
    }

    public void cambiaContrato() {//funcion cuando se selecciona otro contrato del listado
        contratoActual = contratoFacade.find(Integer.parseInt(idContratoActual));
        determinarCopagoCuotaModeradora();
        determinarImpuestos();
        cargarListaTiposDocumento();
    }

    public void cambiaCaja() {//funcion cuando se selecciona otra caja del listado
        //System.out.println("validarCambiaCaja");
        if (validarNoVacio(caja)) {
            for (FacCaja c : listaCajas) {
                if (c.getIdCaja() == Integer.parseInt(caja)) {
                    if (c.getCerrada()) {
                        msjHtmlCaja = "La caja <br/>está cerrada";
                        estiloCaja = "border-color: orange; border-width: 2px; border-style: solid; border-radius: 7px 7px 7px 7px;";
                    } else {
                        msjHtmlCaja = "Correcto: <br/>Caja abierta";
                        estiloCaja = "";
                    }
                    cajaSeleccionada = c;
                    break;
                }
            }
        } else {
            imprimirMensaje("Error", "No se ha seleccionado una caja", FacesMessage.SEVERITY_ERROR);
            msjHtmlCaja = "Se debe seleccionar <br/>una caja";
            estiloCaja = "border-color: orange; border-width: 2px; border-style: solid; border-radius: 7px 7px 7px 7px;";
        }
    }

    public void cambiaDocumento() {//determinar si existe un consecutivo
        //System.out.println("Cambia documento");
        msjHtmlDocumento = "Se debe configurar <br/>consecutivos para el <br/>tipo de documento";
        estiloDocumento = "border-color: orange; border-width: 2px; border-style: solid; border-radius: 7px 7px 7px 7px;";
        if (validarNoVacio(documento)) {
            List<FacConsecutivo> listaConsecutivos = consecutivoFacade.buscarPorTipoDocumento(Integer.parseInt(documento));
            if (listaConsecutivos != null && !listaConsecutivos.isEmpty()) {
                for (FacConsecutivo c : listaConsecutivos) {
                    if (!Objects.equals(c.getActual(), c.getFin())) {
                        msjHtmlDocumento = "Correcto: <br/>tiene consecutivos";
                        estiloDocumento = "";
                        consecutivoSeleccionado = c;
                        observaciones = c.getTexto();
                    }
                }
            }
        } else {
            msjHtmlDocumento = "Debe seleccionar <br/> un documento";
            estiloDocumento = "border-color: orange; border-width: 2px; border-style: solid; border-radius: 7px 7px 7px 7px;";
            observaciones = "";
        }
    }

    public void cargarListaTiposDocumento() {//validar si que tipos de documeto se pueden generar asi como si hay consecutivos disponibles        
        //System.out.println("cargarListaTiposDocumento");
        listaDocumentos = clasificacionesFachada.buscarPorMaestro("TipoFacturacion");
        //System.out.println("Aqui1");
        if (contratoActual.getTipoPago().getCodigo().compareTo("01") == 0) {//capitado
            //System.out.println("Aqui2");
            if (contratoActual.getTipoFacturacion().getCodigo().compareTo("06") == 0) {//todos
                //System.out.println("Aqui3");
                for (int i = 0; i < listaDocumentos.size(); i++) {
                    //System.out.println("Aqui4");
                    if (listaDocumentos.get(i).getDescripcion().compareTo("Factura de Venta") == 0
                            || listaDocumentos.get(i).getDescripcion().compareTo("Factura de Venta Extramural") == 0
                            || listaDocumentos.get(i).getDescripcion().compareTo("Recibo de Caja") == 0) {
                        //System.out.println("Aqui5");
                    } else {
                        //System.out.println("Aqui6");
                        listaDocumentos.remove(i);
                        i--;
                    }
                }
            } else {
                //System.out.println("Aqui7");
                listaDocumentos = new ArrayList<>();
                listaDocumentos.add(contratoActual.getTipoFacturacion());
            }
            //System.out.println("Aqui8");
        } else {//evento
            //System.out.println("Aqui9");
            if (contratoActual.getTipoFacturacion().getCodigo().compareTo("06") == 0) {//todos
                //System.out.println("Aqui10");
                for (int i = 0; i < listaDocumentos.size(); i++) {
                    //System.out.println("Aqui11");
                    if (listaDocumentos.get(i).getDescripcion().compareTo("Orden de Servicio") == 0
                            || listaDocumentos.get(i).getDescripcion().compareTo("Orden de Servicio Extramural") == 0
                            || listaDocumentos.get(i).getDescripcion().compareTo("Recibo de Caja") == 0) {
                        //System.out.println("Aqui12");
                    } else {
                        //System.out.println("Aqui13");
                        listaDocumentos.remove(i);
                        i--;
                    }
                }
            } else {
                //System.out.println("Aqui14");
                listaDocumentos = new ArrayList<>();
                listaDocumentos.add(contratoActual.getTipoFacturacion());
            }
        }
        //System.out.println("Aqui15  " + listaDocumentos.size());
        if (!listaDocumentos.isEmpty()) {
            //documento = listaDocumentos.get(0).getId().toString();
            if (facturarComoParticular) {//facturando paciente como particular
                for (CfgClasificaciones d : listaDocumentos) {//se intenta escoger por defecto: 'Factura de Venta'
                    if (d.getDescripcion().compareTo("Factura de Venta") == 0) {
                        documento = d.getId().toString();
                        break;
                    }
                }
            } else {//facturando paciente por administradora
                for (CfgClasificaciones d : listaDocumentos) {//se intenta escoger por defecto 'Recibo de caja'
                    if (d.getDescripcion().compareTo("Recibo de Caja") == 0) {
                        documento = d.getId().toString();
                        break;
                    }
                }
            }
            if (!validarNoVacio(documento)) {//no se encontro el por defecto, toca asignar el primero
                documento = listaDocumentos.get(0).getId().toString();
            }
        } else {//no hay tipos de documentos para facturar
            documento = "";
        }
        cambiaDocumento();
    }

    private void determinarCopagoCuotaModeradora() {
        //System.out.println("determinarCopagoCuotaModeradora");
        if (facturarComoParticular) {//si es particular copago y cuotamoderadora son cero
            valorCopago = 0;
            msjHtmlCopago = "Se esta facturando como particular <br/> no aplica copago";
            valorCuotaModeradora = 0;
            msjHtmlCuotaModeradora = "Se esta facturando como particular <br/> no aplica cuota moderadora";
            return;
        }
        //DETERMINAR SI ES COTIZANTE
        boolean esCotizante = true;
        if (pacienteSeleccionado.getTipoAfiliado() != null) {
            if (pacienteSeleccionado.getTipoAfiliado().getDescripcion().compareTo("Cotizante") != 0) {
                esCotizante = false;
            }
        }
        //DETERMINAR SI SE HA FACTURADO ANTERIORMENTE(SEGUN LA AUTORIZACION)
        boolean facturadoAnteriormente = false;
        if (citaActual != null) {
            if (citaActual.getCancelada() == false) {//no esta cancelada
                if (citaActual.getIdAutorizacion() != null) {//tiene autorizacion
                    if (citaActual.getIdAutorizacion().getFacturada() != null && citaActual.getIdAutorizacion().getFacturada() == true) {//tiene autorizacion
                        facturadoAnteriormente = true;

                    }
                }
            }
        }

        //------COPAGO----------------
        if (esCotizante) {//es cotizante
            if (contratoActual.getCpc()) {//aplica copago a cotizante
                if (facturadoAnteriormente) {
                    valorCopago = 0;//aqui toma el valor de copago nivel_1 pero se debe escoger dependiendo del nivel de usuario
                    msjHtmlCopago = "Aplica copago a cotizante<br/> pero ya se cobro anteriormente";
                } else {
                    valorCopago = contratoActual.getCp1();//aqui toma el valor de copago nivel_1 pero se debe escoger dependiendo del nivel de usuario
                    msjHtmlCopago = "Aplica copago<br/> a cotizante";
                }
            } else {//no aplica copago a cotizante
                valorCopago = 0;
                msjHtmlCopago = "No aplica copago<br/> a cotizante";
            }
        } else {//no es cotizante
            if (contratoActual.getCpc()) {//aplica copago a no cotizante
                if (facturadoAnteriormente) {
                    valorCopago = 0;//aqui toma el valor de copago nivel_1 pero se debe escoger dependiendo del nivel de usuario
                    msjHtmlCopago = "Aplica copago a beneficiario<br/> pero ya se cobro anteriormente ";
                } else {
                    valorCopago = contratoActual.getCp1();//aqui toma el valor de copago nivel_1 pero se debe escoger dependiendo del nivel de usuario
                    msjHtmlCopago = "Aplica copago<br/> a beneficiario";
                }
            } else {//no aplica copago a no cotizante
                valorCopago = 0;
                msjHtmlCopago = "No aplica copago<br/> a beneficiario";
            }
        }
        //------CUOTA MODERADORA----------------
        if (esCotizante) {//es cotizante
            if (contratoActual.getCmc()) {//aplica cuota moderadora a cotizante
                if (facturadoAnteriormente) {
                    valorCuotaModeradora = 0;//aqui toma el valor de copago nivel_1 pero se debe escoger dependiendo del nivel de usuario
                    msjHtmlCuotaModeradora = "Aplica cuota moderadora a cotizante<br/> pero ya se cobro anteriormente";
                } else {
                    valorCuotaModeradora = contratoActual.getCm1();//aqui toma el valor de copago nivel_1 pero se debe escoger dependiendo del nivel de usuario
                    msjHtmlCuotaModeradora = "Aplica cuota moderadora<br/> a cotizante";
                }
            } else {//no aplica cuota moderadora a cotizante
                valorCuotaModeradora = 0;
                msjHtmlCuotaModeradora = "No aplica cuota moderadora<br/> a cotizante";
            }
        } else {//no es cotizante
            if (contratoActual.getCmc()) {//aplica cuota moderadora a no cotizante
                if (facturadoAnteriormente) {
                    valorCuotaModeradora = 0;//aqui toma el valor de copago nivel_1 pero se debe escoger dependiendo del nivel de usuario
                    msjHtmlCuotaModeradora = "Aplica cuota moderadora a beneficiario<br/> pero ya se cobro anteriormente";
                } else {
                    valorCuotaModeradora = contratoActual.getCm1();//aqui toma el valor de copago nivel_1 pero se debe escoger dependiendo del nivel de usuario
                    msjHtmlCuotaModeradora = "Aplica cuota moderadora<br/> a beneficiario";
                }
            } else {//no aplica cuota moderadora a no cotizante
                valorCuotaModeradora = 0;
                msjHtmlCuotaModeradora = "No aplica cuota moderadora<br/> a beneficiario";
            }
        }
    }

    private void determinarImpuestos() {
        //System.out.println("determinarImpuestos");
        //IVA        
        msjHtmlIva = "";
        if (contratoActual.getAplicarIva()) {//buscar si en la tabla impuestos hay un valor para el año de la fecha
            List<FacImpuestos> listaImpuestos = impuestosFacade.buscarPorNombre("IVA");
            if (listaImpuestos == null || listaImpuestos.isEmpty()) {
                valorIva = 0;
                valorIvaStr = "--% (--)";
                msjHtmlIva = "Se debe configurar <br/>el impuesto IVA <br/>para la fecha escogida";
                estiloIva = "border-color: orange; border-width: 2px; border-style: solid; border-radius: 7px 7px 7px 7px;";
            } else {
                for (FacImpuestos impuesto : listaImpuestos) {
                    if (fechaDentroDeRangoMas1Dia(impuesto.getFechaInicial(), impuesto.getFechaFinal(), fecha)) {
                        valorIva = impuesto.getValor();
                        valorIvaStr = String.valueOf(impuesto.getValor()) + "% (0)";
                        msjHtmlIva = "Correcto: <br/>IVA: " + String.valueOf(impuesto.getValor()) + "%";
                        estiloIva = "";
                        break;
                    }
                }
                if (msjHtmlIva.length() == 0) {
                    valorIva = 0;
                    valorIvaStr = "--% (--)";
                    msjHtmlIva = "Se debe configurar <br/>el impuesto IVA <br/>para la fecha escogida";
                    estiloIva = "border-color: orange; border-width: 2px; border-style: solid; border-radius: 7px 7px 7px 7px;";
                }
            }

        } else {
            valorIva = 0;
            valorIvaStr = "0% (0)";
            msjHtmlIva = "Correcto: <br/>contrato no aplica IVA";
            estiloIva = "";
        }
        //CREE
        msjHtmlCree = "";
        if (contratoActual.getAplicarCree()) {//buscar si en la tabla impuestos hay un valor para el año de la fecha
            List<FacImpuestos> listaImpuestos = impuestosFacade.buscarPorNombre("CREE");
            if (listaImpuestos == null || listaImpuestos.isEmpty()) {
                valorCree = 0;
                valorCreeStr = "--% (--)";
                msjHtmlCree = "Se debe configurar <br/>el impuesto CREE <br/>para la fecha escogida";
                estiloCree = "border-color: orange; border-width: 2px; border-style: solid; border-radius: 7px 7px 7px 7px;";
            } else {
                for (FacImpuestos impuesto : listaImpuestos) {
                    if (fechaDentroDeRangoMas1Dia(impuesto.getFechaInicial(), impuesto.getFechaFinal(), fecha)) {
                        valorCree = impuesto.getValor();
                        valorCreeStr = String.valueOf(impuesto.getValor()) + "% (0)";
                        msjHtmlCree = "Correcto: <br/>CREE: " + String.valueOf(impuesto.getValor()) + "%";
                        estiloCree = "";
                        break;
                    }
                }
                if (msjHtmlCree.length() == 0) {
                    valorCree = 0;
                    valorCreeStr = "--% (--)";
                    msjHtmlCree = "Se debe configurar <br/>el impuesto CREE <br/>para la fecha escogida";
                    estiloCree = "border-color: orange; border-width: 2px; border-style: solid; border-radius: 7px 7px 7px 7px;";
                }
            }
        } else {
            valorCree = 0;
            valorCreeStr = "0% (0)";
            msjHtmlCree = "Correcto: <br/>contrato no aplica CREE";
            estiloCree = "";
        }
    }

    public void validarPeriodo() {//determina existencia y estado de un periodo para la fecha de la factura
        //System.out.println("validarPeriodo");
        msjHtmlPeriodo = "";
        if (fecha != null) {
            List<FacPeriodo> listaPeriodos = periodoFacade.buscarOrdenado();
            for (FacPeriodo p : listaPeriodos) {
                if (fechaDentroDeRangoMas1Dia(p.getFechaInicial(), p.getFechaFinal(), fecha)) {
                    if (p.getCerrado()) {
                        msjHtmlPeriodo = "El periodo esta cerrado";
                        estiloPeriodo = "border-color: orange; border-width: 2px; border-style: solid; border-radius: 7px 7px 7px 7px;";
                    } else {

                        msjHtmlPeriodo = "Correcto: <br/>Periodo abierto";
                        estiloPeriodo = "";
                    }
                    periodoSeleccionado = p;
                    break;
                }
            }
            if (msjHtmlPeriodo.length() == 0) {
                msjHtmlPeriodo = "Se debe configurar <br/>un periodo para usar <br/>la fecha seleccionada";
                estiloPeriodo = "border-color: orange; border-width: 2px; border-style: solid; border-radius: 7px 7px 7px 7px;";
            }
        } else {
            msjHtmlPeriodo = "La fecha <br/>es erronea";
            estiloPeriodo = "border-color: orange; border-width: 2px; border-style: solid; border-radius: 7px 7px 7px 7px;";
        }
    }

    public void validaPeriodoImpuestos() {
        //System.out.println("validarImpuestos");
        //usado cuando se cambia la fecha determinar estado de periodo e impuestos    
        validarPeriodo();
        validarContrato();
        determinarImpuestos();
        recalcularValorFactura();
    }

    private boolean validarManualTarifario() {
        contratoActual = null;
        FacAdministradora administradoraParticular = administradoraFacade.find(1);
        FacContrato contratoParticular = null;
        //PACIENTE TENGA REGIMEN Y ADMINISTRADORA------------------------------
        if (pacienteSeleccionado.getRegimen() == null || pacienteSeleccionado.getIdAdministradora() == null) {
            mensajeConfiguracion = "No se puede facturar: \n"
                    + "Paciente: " + pacienteSeleccionado.nombreCompleto() + " \n"
                    + "Razón: El paciente no tiene régimen ó administradora";
            return false;
        }

        //EXISTENCIA DE ADMINISTRADORA PARTICULAR------------------------------
        if (pacienteSeleccionado.getIdAdministradora().getIdAdministradora() != 1) {//ADMINISTRADORA PACIENTE NO ES PARTICULAR, SE DEBEN REALIZAR LAS CORESPONDIENTES VALIDACIONES
            //EXISTENCIA DE ADMINISTRADORA PARTICULAR------------------------------
            if (administradoraParticular == null) {//validar que exista la administradora particular y tenga manual tarifario
                mensajeConfiguracion = "No se puede facturar: \n"
                        + "Paciente: " + pacienteSeleccionado.nombreCompleto() + " \n"
                        + "Razón: Es obligatorio que exista una administradora PARTICULAR";
                return false;
            }
            //PRIMER ADMINISTRADORA SEA PARTICULAR----------------------------------
            if (administradoraParticular.getRazonSocial().compareTo("PARTICULAR") != 0) {//la primer administradora debe ser PARTICULAR (no puede ser otra)
                mensajeConfiguracion = "No se puede facturar: \n"
                        + "Paciente: " + pacienteSeleccionado.nombreCompleto() + " \n"
                        + "Razón: La primer administradora ( id = 1 ) debe llamarse PARTICULAR obligatoriamente";
                return false;
            }
            //ADMINISTRADORA PARTICULAR TENGA CONTRATOS ----------------------------
            if (administradoraParticular.getFacContratoList() == null || administradoraParticular.getFacContratoList().isEmpty()) {//debe tener un contrato
                mensajeConfiguracion = "No se puede facturar: \n"
                        + "Paciente: " + pacienteSeleccionado.nombreCompleto() + " \n"
                        + "Administradora: " + administradoraParticular.getRazonSocial() + " \n"
                        + "Razón: La administradora no tiene contratos";
                return false;
            }
            //CONTRATOS DE ADMINISTRADORA PARTICULAR TENGAN MANUALES TARIFARIOS-----        
            for (FacContrato contrato : administradoraParticular.getFacContratoList()) {
                if (contrato.getIdManualTarifario() == null) {//debe contar con el manual tarifario                    
                    mensajeConfiguracion = "No se puede facturar: \n"
                            + "Paciente: " + pacienteSeleccionado.nombreCompleto() + " \n"
                            + "Administradora: " + administradoraParticular.getRazonSocial() + " \n"
                            + "Contrato: " + contrato.getDescripcion() + " \n"
                            + "Razón: El contrato no tiene manual tarifario";
                    return false;
                }
                contratoParticular = contrato;//se usa el primer contrato cuando es administradora particular
                break;
            }
        }

        //ADMINISTRADORA PACIENTE TENGA CONTRATOS ----------------------------
        if (pacienteSeleccionado.getIdAdministradora().getFacContratoList() == null || pacienteSeleccionado.getIdAdministradora().getFacContratoList().isEmpty()) {//debe tener un contrato
            mensajeConfiguracion = "No se puede facturar: \n"
                    + "Paciente: " + pacienteSeleccionado.nombreCompleto() + " \n"
                    + "Administradora: " + pacienteSeleccionado.getIdAdministradora().getRazonSocial() + " \n"
                    + "Razón: La administradora no tiene contratos";
            return false;
        }
        //CONTRATOS DE ADMINISTRADORA PACIENTE TENGAN MANUAL TARIFARIO:        
        listaContratosAplican = new ArrayList<>();
        if (pacienteSeleccionado.getIdAdministradora().getIdAdministradora() == 1) {//ADMINISTRADORA PACIENTE ES PARTICULAR (NO IMPORTA REGIMEN PACIENTE)
            contratoParticular = null;
            for (FacContrato contrato : pacienteSeleccionado.getIdAdministradora().getFacContratoList()) {
                if (contrato.getIdManualTarifario() != null) {
                    listaContratosAplican.add(contrato);
                    if (contratoParticular == null) {//SE ASIGNA EL PRIMER CONTRATO QUE TENGA LA ADMINISTRADORA
                        contratoParticular = contrato;
                    }
                }
            }

        } else {//ADMINISTRADORA DE PACIENTE NO ES PARTICULAR(SI IMPORTA EL REGIMEN)
            for (FacContrato contrato : pacienteSeleccionado.getIdAdministradora().getFacContratoList()) {//System.err.println("TIPO CONTRATO: " + contrato.getTipoContrato().getDescripcion() + " - ID REGIMEN PACIENTE " + pacienteSeleccionado.getRegimen().getId() + " - ID TIPO CONTRATO " + contrato.getTipoContrato().getId());
                if (Objects.equals(pacienteSeleccionado.getRegimen().getId(), contrato.getTipoContrato().getId())) {
                    if (contrato.getIdManualTarifario() != null) {
                        listaContratosAplican.add(contrato);
                    }
                }
            }
        }

        if (listaContratosAplican.isEmpty()) {
            mensajeConfiguracion = "No se puede facturar: \n"
                    + "Paciente: " + pacienteSeleccionado.nombreCompleto() + " \n"
                    + "Administradora: " + pacienteSeleccionado.getIdAdministradora().getRazonSocial() + " \n"
                    + "Razón: La administradora no tiene contratos de tipo " + pacienteSeleccionado.getRegimen().getDescripcion() + "\n"
                    + "ó de tenerlo no tiene asociado un manual tarifario.";
            return false;
        }

        //--------DETERMINAR SI USAR MANUAL DE PACIENTE O PARTICULAR------------------------------
        administradoraActual = pacienteSeleccionado.getIdAdministradora();
        contratoActual = listaContratosAplican.get(0);//ESCOGER PRIMERO QUE APAREZCA EN EL COMBO

        if (contratoActual.getIdManualTarifario().getIdManualTarifario() == 1) {//si manual tarifario particular, no se necesita la opcion facturar como particular
            facturarComoParticularDisabled = true;
            msjBtnFacturarParticular = "Facturar como particular: SI";
            facturarComoParticular = true;
        }
        if (facturarComoParticular) {//se facturara como particular
            contratoActual = contratoParticular;
            manualTarifarioPaciente = contratoParticular.getIdManualTarifario();//se carga el manual tarifario particular                        
        } else {//se facturara segun el manual tarifario que tenga el contrato al que corresponde(no es el particular)
            manualTarifarioPaciente = contratoActual.getIdManualTarifario();
        }
        mensajeConfiguracion = null;
        determinarCopagoCuotaModeradora();
        determinarImpuestos();
        cargarListaTiposDocumento();
        return true;

    }

    public void validarContrato() {
        //validar si el contrato actual esta vigente
        if (fechaDentroDeRangoMas1Dia(contratoActual.getFechaInicio(), contratoActual.getFechaFinal(), fecha)) {
            msjHtmlAdministradoraPaciente = "Correcto <br/>contrato vigente";
            estiloAdministradoraPaciente = "";
        } else {
            msjHtmlAdministradoraPaciente
                    = "Contrato no vigente: "
                    + "<br/>Inicia: " + formateadorFechaSinHora.format(contratoActual.getFechaInicio())
                    + "<br/>Finaliza: " + formateadorFechaSinHora.format(contratoActual.getFechaFinal());
            estiloAdministradoraPaciente = "border-color: orange; border-width: 2px; border-style: solid; border-radius: 7px 7px 7px 7px;";
        }
    }

    public void validarTurno() {
        //si no hay turno informar que al generar rips pueden haber datos faltantes
        if (turnoActual != null) {
            msjHtmlTurnoCita = "Correcto se <br/>asociará este turno";
            estiloTurnoCita = "";
        } else {
            msjHtmlTurnoCita = "Se puede facturar <br/>pero tener en cuenta <br/>que al no asociar un turno <br/>la generacion de RIPS <br/>podría salir incompleta.";
            estiloTurnoCita = "border-color: gray; border-width: 2px; border-style: solid; border-radius: 7px 7px 7px 7px;";
        }
    }

    public void validarConsumos() {
        //informar si existen o no consumos para el paciente
        int consumos = 0;
        consumos = consumos + pacienteSeleccionado.getFacConsumoServicioList().size();
        consumos = consumos + pacienteSeleccionado.getFacConsumoMedicamentoList().size();
        consumos = consumos + pacienteSeleccionado.getFacConsumoPaqueteList().size();
        consumos = consumos + pacienteSeleccionado.getFacConsumoInsumoList().size();
        if (consumos == 0) {
            msjHtmlBtnConsumos = "No tiene <br/>consumos pendientes";
            estiloBtnConsumos = "";
        } else {
            msjHtmlBtnConsumos = "Se puede facturar <br/>pero tener en cuenta <br/>que el paciente<br/> tiene " + consumos + " consumos <br/>pendientes por facturar.";
            estiloBtnConsumos = "border-color: gray; border-width: 2px; border-style: solid; border-radius: 7px 7px 7px 7px;";
        }

    }

    public void cargarDatosPaciente() {//System.out.println("cargarDatosPaciente");
        //se separa esta funcion de cargar paciente por que 'Facturar como particular' no de debe modificar ese boton(value,render,disable)                
        if (pacienteSeleccionadoTabla == null) {//esta instruccion se usa cuando se regresa a Tab 'Facturar Paciente', y en otra tab se produjeron cambios que afectan facturarPacienteMB (y no se tiene que limpiar las listas de items(serv,medicam,insum,paq) agregados a la factura)
            return;
        }
        pacienteSeleccionado = pacientesFacade.find(pacienteSeleccionadoTabla.getIdPaciente());
        identificacionPaciente = "";
        mensajeConfiguracion = null;
        if (!validarCaja() || !validarManualTarifario()) {
            pacienteSeleccionado = null;
            identificacionPaciente = "";
            if (!cargandoDesdeTab) {//cuando se esta cargando desde tab no se ejecuta esta funcion por que probocaria error
                RequestContext.getCurrentInstance().execute("PF('dlgSeleccionarPaciente').hide();");
                RequestContext.getCurrentInstance().update("IdFormFacturacion");
            }
            return;
        }
        validarPeriodo();

        validarContrato();
        validarTurno();
        validarConsumos();

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
        recargarFilasTablasConsumo();
        recalcularValorFactura();
        if (!cargandoDesdeTab) {//cuando se esta cargando desde tab no se ejecuta esta funcion por que probocaria error
            RequestContext.getCurrentInstance().execute("PF('dlgSeleccionarPaciente').hide();");
        }
        RequestContext.getCurrentInstance().update("IdFormFacturacion");
        RequestContext.getCurrentInstance().update("IdFormFacturacion:IdDialogSeleccionConsumos:IdTabViewDialog");
    }

    public void cargarPaciente() {//cargar un paciente desde del dialogo de buscar paciente o al digitar una identificacion valida(esta en pacientes)        
        //System.out.println("cargarPaciente");
        if (pacienteSeleccionadoTabla == null) {
            imprimirMensaje("Error", "Se debe seleccionar un paciente de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        limpiarFormularioFacturacion();
        facturarComoParticular = false;
        facturarComoParticularDisabled = false;
        msjBtnFacturarParticular = "Facturar como particular: NO";
        cargarDatosPaciente();
        RequestContext.getCurrentInstance().update("IdFormFacturacion:IdpanelDatosPaciente");
        RequestContext.getCurrentInstance().update("IdFormFacturacion:IdTabView");
        RequestContext.getCurrentInstance().update("IdFormFacturacion:IdPanelResultados");
    }

    //---------------------------------------------------
    //-----------------FUNCIONES FACTURAS ---------------
    //--------------------------------------------------- 
    public void confirmarCambioManualTarifario() {
        //cuando se desea facturar como particular se debe manejar otro manual tarifario
        if (pacienteSeleccionadoTabla == null) {
            imprimirMensaje("Error", "Se debe seleccionar un paciente de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        limpiarFormularioFacturacion();
        if (facturarComoParticular) {
            facturarComoParticular = false;
            msjBtnFacturarParticular = "Facturar como particular: NO";
        } else {
            facturarComoParticular = true;
            msjBtnFacturarParticular = "Facturar como particular: SI";
        }
        //System.out.println("CargarDatosPaciente desde confirmarCambioManualTarifario");
        cargarDatosPaciente();
    }

    public void limpiarFormularioFacturacion() {
        //System.out.println("limpiarFormularioFacturacion");
        turnoCita = "";
        turnoActual = null;
        citaActual = null;

        pacienteSeleccionado = null;
        //------- TABVIEW FACTURA --------------    
        tituloTabInsumosFactura = "Insumos (0)";
        tituloTabServiciosFactura = "Servicios (0)";
        tituloTabMedicamentosFactura = "Medicamentos (0)";
        tituloTabPaquetesFactura = "Paquetes (0)";
        listaServiciosFactura = new ArrayList<>();
        listaInsumosFactura = new ArrayList<>();
        listaMedicamentosFactura = new ArrayList<>();
        listaPaquetesFactura = new ArrayList<>();
        listaServiciosFacturaFiltro = new ArrayList<>();
        listaInsumosFacturaFiltro = new ArrayList<>();
        listaMedicamentosFacturaFiltro = new ArrayList<>();
        listaPaquetesFacturaFiltro = new ArrayList<>();
        servicioFacturaSeleccionado = null;
        insumoFacturaSeleccionado = null;
        medicamentoFacturaSeleccionado = null;
        paqueteFacturaSeleccionado = null;
        //-------------PACIENTES--------------
        identificacionPaciente = "";
        tipoIdentificacion = "";
        nombrePaciente = "Paciente";
        generoPaciente = "";
        edadPaciente = "";
        administradoraPaciente = "";
        //------- FACTURA --------------
        cajaSeleccionada = null;
        periodoSeleccionado = null;
        fecha = new Date();
        caja = "";//caja asociada al usuario actual   
        msjHtmlCaja = "La caja se encuentra cerrada";
        estiloCaja = "";
        msjHtmlPeriodo = "No exite periodo <br/>para esta fecha";
        estiloPeriodo = "";
        msjHtmlDocumento = "No hay documento"; //me dice si el periodo esta: Abierto, Cerrado, Sin Crear
        estiloDocumento = "";
        valorIva = 0;
        valorIvaStr = "--% (--)";
        msjHtmlIva = "Se debe configurar <br/>el impuesto IVA <br/>para la fecha seleccionada";
        estiloIva = "";
        valorCree = 0;
        valorCreeStr = "--% (--)";
        msjHtmlCree = "Se debe configurar <br/>el impuesto CREE <br/>para la fecha seleccionada ";
        estiloCree = "";

        msjHtmlAdministradoraPaciente = "";
        msjHtmlBtnConsumos = "";
        msjHtmlTurnoCita = "";

        estiloAdministradoraPaciente = "";
        estiloBtnConsumos = "";
        estiloTurnoCita = "";

        mensajeConfiguracion = null;//permite informar problemas al querer facturar
        documento = "";//factura, cuenta cobro, recibo.
        idFactura = "";
        idSede = "";//sale de principal pero puede cambiarla    
        consecutivo = "";//numero de consecutivo        
        observaciones = "";//observaciones de la factura
        observacionAnulacion = "";
        valorEmpresa = 0;
        valorUsuario = 0;
        valorParcial = 0;
        valorTotal = 0;
        valorCuotaModeradora = 0;
//        if(pacienteSeleccionado==null){
//            //System.out.println("1. Aqui va null");
//        }else{
//            //System.out.println("2. NO es null");
//        }
    }

    public void anularFactura() {
        if (facturaSeleccionadaTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ninguna factura", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoAnularFactura').show();");
    }

    public void seleccionFilaBuscarFactura() {
        disableControlesBuscarFactura = facturaSeleccionadaTabla == null;
    }

    public void generarPdf() throws JRException, IOException {//genera un pdf de una historia seleccionada en el historial        
        if (facturaSeleccionadaTabla != null) {
            facturaSeleccionada = facturaPacienteFacade.find(Integer.parseInt(facturaSeleccionadaTabla.getColumna1()));
        } else {
            return;
        }
        tipoDocumentoFacturaSeleccionada = facturaSeleccionada.getTipoDocumento().getDescripcion();//determinar si se imprime media o pagina completa        
        JRBeanCollectionDataSource beanCollectionDataSource;
        switch (tipoDocumentoFacturaSeleccionada) {
            case "Recibo de Caja"://se imprime a media hoja
                if (!cargarFuenteDeDatosReciboCaja()) {
                    return;
                }
                beanCollectionDataSource = new JRBeanCollectionDataSource(listaRegistrosReciboCaja);
                break;
            default://se imprime a pagina completa
                if (!cargarFuenteDeDatosFactura()) {
                    return;
                }
                beanCollectionDataSource = new JRBeanCollectionDataSource(listaRegistrosFactura);
                break;
        }

        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            JasperPrint jasperPrint;
            switch (tipoDocumentoFacturaSeleccionada) {
                case "Recibo de Caja":
                    jasperPrint = JasperFillManager.fillReport(servletContext.getRealPath("facturacion/reportes/reciboCaja.jasper"), new HashMap(), beanCollectionDataSource);
                    break;
                default:
                    jasperPrint = JasperFillManager.fillReport(servletContext.getRealPath("facturacion/reportes/facturaPaciente.jasper"), new HashMap(), beanCollectionDataSource);
                    break;
            }
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    private boolean cargarFuenteDeDatosFactura() {

        List<EstructuraItemsPaciente> listaItemsFactura = new ArrayList<>();
        listaRegistrosFactura = new ArrayList<>();
        EstructuraFacturaPaciente nuevaFactura = new EstructuraFacturaPaciente();
        nuevaFactura.setTituloFactura(loginMB.getEmpresaActual().getRazonSocial());
        nuevaFactura.setRegimenEmpresa(loginMB.getEmpresaActual().getRegimen());
        nuevaFactura.setNitEmpresa("NIT. " + loginMB.getEmpresaActual().getNumIdentificacion());
        nuevaFactura.setTipoDocumento(facturaSeleccionada.getTipoDocumento().getDescripcion() + " No.");
        nuevaFactura.setCodigoDocumento("" + facturaSeleccionada.getCodigoDocumento());
        nuevaFactura.setSubtituloFactura(loginMB.getEmpresaActual().getDireccion() + " - Tel1: " + loginMB.getEmpresaActual().getTelefono1() + " - Tel2: " + loginMB.getEmpresaActual().getTelefono2() + " - " + loginMB.getEmpresaActual().getWebsite());
        nuevaFactura.setClienteNombre("<b>NOMBRE: </b>" + facturaSeleccionada.getIdPaciente().nombreCompleto());
        nuevaFactura.setClienteDireccion("<b>DIRECCION: </b>" + facturaSeleccionada.getIdPaciente().getDireccion());
        nuevaFactura.setClienteIdentificacion("<b>IDENTIFICACION: </b>" + facturaSeleccionada.getIdPaciente().getIdentificacion());
        nuevaFactura.setClienteTelefono("<b>TELEFONO: </b>" + facturaSeleccionada.getIdPaciente().getTelefonoResidencia() + " <b>CELULAR: </b>" + facturaSeleccionada.getIdPaciente().getCelular());
        nuevaFactura.setClienteCiudad("<b>CIUDAD: </b>" + "-");
        nuevaFactura.setFechaFactura("<b>FECHA FACTURA: </b>" + formateadorFecha.format(facturaSeleccionada.getFechaElaboracion()));
        nuevaFactura.setFechaVencimiento("<b>FECHA VENCIMIENTO: </b>" + "-");
        nuevaFactura.setGravadas("GRAVADA");
        nuevaFactura.setNoGravadas("NO GRAVADA");
        nuevaFactura.setObservaciones(facturaSeleccionada.getObservacion());
        nuevaFactura.setSubtotal(formateadorDecimal.format(facturaSeleccionada.getValorParcial()));
        nuevaFactura.setIva(formateadorDecimal.format(facturaSeleccionada.getIva() * facturaSeleccionada.getValorParcial() / 100));
        nuevaFactura.setTituloCree("CREE: (" + formateadorDecimal.format(facturaSeleccionada.getCree()) + "%)");
        nuevaFactura.setTituloIva("IVA: (" + formateadorDecimal.format(facturaSeleccionada.getIva()) + "%)");
        nuevaFactura.setCree(formateadorDecimal.format(facturaSeleccionada.getCree() * facturaSeleccionada.getValorParcial() / 100));

        nuevaFactura.setMenosCuotaModeradora(formateadorDecimal.format(facturaSeleccionada.getCuotaModeradora()));
        nuevaFactura.setMenosCopago(formateadorDecimal.format(facturaSeleccionada.getCopago()));
        String t = formateadorDecimal.format(facturaSeleccionada.getValorTotal());
        t = t.replace(".", ",");
        if (t.contains(",")) {
            t = t.split(",")[0];
        }
        nuevaFactura.setTotal(formateadorDecimal.format(facturaSeleccionada.getValorTotal()));
        nuevaFactura.setSon("<b>SON: </b>" + ConversorDeNumerosALetras.convertNumberToLetter(t));
        nuevaFactura.setFirmaAutoriza(loginMB.getRutaCarpetaImagenes() + loginMB.getEmpresaActual().getLogo().getUrlImagen());
        nuevaFactura.setLogoEmpresa(loginMB.getRutaCarpetaImagenes() + loginMB.getEmpresaActual().getLogo().getUrlImagen());
        for (FacFacturaServicio servicioFactura : facturaSeleccionada.getFacFacturaServicioList()) {
            EstructuraItemsPaciente nuevoItem = new EstructuraItemsPaciente();
            nuevoItem.setCantidad(servicioFactura.getCantidadServicio().toString());
            nuevoItem.setDescripcion(servicioFactura.getIdServicio().getNombreServicio());
            nuevoItem.setValorUnitario(formateadorDecimal.format(servicioFactura.getValorServicio()));
            nuevoItem.setValorTotal(formateadorDecimal.format(servicioFactura.getValorParcial()));
            listaItemsFactura.add(nuevoItem);
        }
        for (FacFacturaMedicamento medicamentoFactura : facturaSeleccionada.getFacFacturaMedicamentoList()) {
            EstructuraItemsPaciente nuevoItem = new EstructuraItemsPaciente();
            nuevoItem.setCantidad(medicamentoFactura.getCantidadMedicamento().toString());
            nuevoItem.setDescripcion(medicamentoFactura.getIdMedicamento().getNombreMedicamento());
            nuevoItem.setValorUnitario(formateadorDecimal.format(medicamentoFactura.getValorMedicamento()));
            nuevoItem.setValorTotal(formateadorDecimal.format(medicamentoFactura.getValorParcial()));
            listaItemsFactura.add(nuevoItem);
        }
        for (FacFacturaInsumo insumoFactura : facturaSeleccionada.getFacFacturaInsumoList()) {
            EstructuraItemsPaciente nuevoItem = new EstructuraItemsPaciente();
            nuevoItem.setCantidad(insumoFactura.getCantidadInsumo().toString());
            nuevoItem.setDescripcion(insumoFactura.getIdInsumo().getNombreInsumo());
            nuevoItem.setValorUnitario(formateadorDecimal.format(insumoFactura.getValorInsumo()));
            nuevoItem.setValorTotal(formateadorDecimal.format(insumoFactura.getValorParcial()));
            listaItemsFactura.add(nuevoItem);
        }
        for (FacFacturaPaquete paqueteFactura : facturaSeleccionada.getFacFacturaPaqueteList()) {
            EstructuraItemsPaciente nuevoItem = new EstructuraItemsPaciente();
            nuevoItem.setCantidad(paqueteFactura.getCantidadPaquete().toString());
            nuevoItem.setDescripcion(paqueteFactura.getIdPaquete().getNombrePaquete());
            nuevoItem.setValorUnitario(formateadorDecimal.format(paqueteFactura.getValorPaquete()));
            nuevoItem.setValorTotal(formateadorDecimal.format(paqueteFactura.getValorParcial()));
            listaItemsFactura.add(nuevoItem);
        }
        nuevaFactura.setListaItemsFactura(listaItemsFactura);
        listaRegistrosFactura.add(nuevaFactura);
        return true;
    }

    private boolean cargarFuenteDeDatosReciboCaja() {
        listaRegistrosReciboCaja = new ArrayList<>();
        EstructuraReciboCaja nuevoReciboCaja = new EstructuraReciboCaja();
        nuevoReciboCaja.setTituloFactura(loginMB.getEmpresaActual().getRazonSocial());
        nuevoReciboCaja.setRegimenEmpresa(loginMB.getEmpresaActual().getRegimen());
        nuevoReciboCaja.setNitEmpresa("NIT. " + loginMB.getEmpresaActual().getNumIdentificacion());
        nuevoReciboCaja.setCodigoDocumento("" + facturaSeleccionada.getCodigoDocumento());
        nuevoReciboCaja.setSubtituloFactura(loginMB.getEmpresaActual().getDireccion() + " - Tel1: " + loginMB.getEmpresaActual().getTelefono1() + " - Tel2: " + loginMB.getEmpresaActual().getTelefono2() + " - " + loginMB.getEmpresaActual().getWebsite());
        nuevoReciboCaja.setClienteCiudad("<b>CIUDAD: </b>" + "-");
        nuevoReciboCaja.setFechaFactura("<b>FECHA : </b>" + formateadorFecha.format(facturaSeleccionada.getFechaElaboracion()));
        nuevoReciboCaja.setClienteAdministradora("<b>ADMINISTRADORA: </b>" + facturaSeleccionada.getIdPaciente().getIdAdministradora().getRazonSocial());
        nuevoReciboCaja.setClienteNombre("<b>NOMBRE: </b>" + facturaSeleccionada.getIdPaciente().nombreCompleto());
        nuevoReciboCaja.setClienteIdentificacion("<b>IDENTIFICACION: </b>" + facturaSeleccionada.getIdPaciente().getIdentificacion());
        nuevoReciboCaja.setCuotaModeradora(formateadorDecimal.format(facturaSeleccionada.getCuotaModeradora()));
        nuevoReciboCaja.setCopago(formateadorDecimal.format(facturaSeleccionada.getCopago()));
        nuevoReciboCaja.setBono("");
        nuevoReciboCaja.setParticular("");
        nuevoReciboCaja.setObservaciones("");
        String t = formateadorDecimal.format(facturaSeleccionada.getValorTotal());
        t = t.replace(".", ",");
        if (t.contains(",")) {
            t = t.split(",")[0];
        }
        nuevoReciboCaja.setTotal(formateadorDecimal.format(facturaSeleccionada.getValorTotal()));
        nuevoReciboCaja.setSon("<b>SON: </b>" + ConversorDeNumerosALetras.convertNumberToLetter(t));
        nuevoReciboCaja.setFirmaAutoriza(loginMB.getRutaCarpetaImagenes() + loginMB.getEmpresaActual().getLogo().getUrlImagen());
        nuevoReciboCaja.setLogoEmpresa(loginMB.getRutaCarpetaImagenes() + loginMB.getEmpresaActual().getLogo().getUrlImagen());
        listaRegistrosReciboCaja.add(nuevoReciboCaja);
        return true;
    }

    public void confirmarAnularFactura() {
        if (facturaSeleccionadaTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún factura", FacesMessage.SEVERITY_ERROR);
            return;
        }
        facturaSeleccionada = facturaPacienteFacade.find(Integer.parseInt(facturaSeleccionadaTabla.getColumna1()));
        facturaSeleccionada.setAnulada(true);
        facturaSeleccionada.setObservacionAnulacion(observacionAnulacion);
        facturaPacienteFacade.edit(facturaSeleccionada);
        imprimirMensaje("Correcto", "La facrura ha sido anulada", FacesMessage.SEVERITY_INFO);
    }

    public void guardarFactura() {

        if (pacienteSeleccionado == null) {
            imprimirMensaje("Error", "Se debe seleccionar un paciente", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (estiloCaja.length() != 0) {
            imprimirMensaje("Error", msjHtmlCaja.replace("<br/>", " "), FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (estiloDocumento.length() != 0) {
            imprimirMensaje("Error", msjHtmlDocumento.replace("<br/>", " "), FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (estiloPeriodo.length() != 0) {
            imprimirMensaje("Error", msjHtmlPeriodo.replace("<br/>", " "), FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (estiloIva.length() != 0) {
            imprimirMensaje("Error", msjHtmlIva.replace("<br/>", " "), FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (estiloCree.length() != 0) {
            imprimirMensaje("Error", msjHtmlCree.replace("<br/>", " "), FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (listaServiciosFactura.isEmpty() && listaInsumosFactura.isEmpty() && listaMedicamentosFactura.isEmpty() && listaPaquetesFactura.isEmpty()) {
            imprimirMensaje("Error", "La factura se encuentra vacia", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoConfirmarGuardarFactura').show();");
    }

    public void confirmarGuardarFactura() {
        String numDocStr;
        int numDocInt;
        consecutivoSeleccionado = consecutivoFacade.find(consecutivoSeleccionado.getIdConsecutivo());
        if (Objects.equals(consecutivoSeleccionado.getActual(), consecutivoSeleccionado.getFin())) {//se vuelve a comprobar el consecutivo 
            imprimirMensaje("Error", "Otro usuario utilizó el último consecutivo para el tipo de documento seleccionado", FacesMessage.SEVERITY_ERROR);
            cambiaDocumento();
            RequestContext.getCurrentInstance().update("IdFormFacturacion");
            return;
        } else {
            numDocInt = consecutivoSeleccionado.getActual() + 1;
            numDocStr = cerosIzquierda(numDocInt, 5);//el documento se completa con ceros a 5 cifras
            if (validarNoVacio(consecutivoSeleccionado.getPrefijo())) {
                numDocStr = consecutivoSeleccionado.getPrefijo() + numDocStr;
            }
            consecutivoSeleccionado.setActual(consecutivoSeleccionado.getActual() + 1);
            consecutivoFacade.edit(consecutivoSeleccionado);
        }
        FacFacturaPaciente nuevaFactura = new FacFacturaPaciente();
        nuevaFactura.setIdCaja(cajaSeleccionada);
        nuevaFactura.setTipoDocumento(clasificacionesFachada.find(Integer.parseInt(documento)));
        nuevaFactura.setNumeroDocumento(numDocInt);
        nuevaFactura.setCodigoDocumento(numDocStr);
        nuevaFactura.setFechaElaboracion(fecha);
        nuevaFactura.setIdPeriodo(periodoSeleccionado);
        //nuevaFactura.setTipoIngreso(clasificacionesFachada.buscarPorMaestroObservacion("TipoIngreso", "Ambulatorio").get(0));
        nuevaFactura.setIdPaciente(pacienteSeleccionado);
        nuevaFactura.setIdContrato(contratoActual);
        if (citaActual != null) {
            if (citaActual.getIdAutorizacion() != null) {//numero y fecha autorizacion                
                nuevaFactura.setNumeroAutorizacion(citaActual.getIdAutorizacion().getNumAutorizacion());
                nuevaFactura.setFechaAutorizacion(citaActual.getIdAutorizacion().getFechaAutorizacion());
            }
        }

        nuevaFactura.setObservacion(observaciones);
        nuevaFactura.setResolucionDian(consecutivoSeleccionado.getResolucionDian());
        nuevaFactura.setIdAdministradora(administradoraActual);
        nuevaFactura.setIva(valorIva);
        nuevaFactura.setCree(valorCree);
        nuevaFactura.setCopago(valorCopago);
        nuevaFactura.setCuotaModeradora(valorCuotaModeradora);
        nuevaFactura.setAnulada(false);
        nuevaFactura.setFacturadaEnAdmi(false);
        nuevaFactura.setValorParcial(valorParcial);
        nuevaFactura.setValorEmpresa(valorEmpresa);
        nuevaFactura.setValorUsuario(valorUsuario);
        nuevaFactura.setValorTotal(valorTotal);
        nuevaFactura.setFacturarComoParticular(facturarComoParticular);
        nuevaFactura.setFechaSistema(new Date());
        nuevaFactura.setIdCita(citaActual);

        facturaPacienteFacade.create(nuevaFactura);
        if (citaActual != null) {//ya se facturo la cita
            citaActual = citasFacade.find(citaActual.getIdCita());
            citaActual.setFacturada(true);
            citasFacade.edit(citaActual);
            if (citaActual.getIdAutorizacion() != null) {//ya se facturo la auorizacion(solo cobrar una cuota moderadora y copago)
                CitAutorizaciones autorizacionActual = citaActual.getIdAutorizacion();
                autorizacionActual.setFacturada(true);
                autorizacionesFacade.edit(autorizacionActual);
            }
            citaActual = null;
        }
        if (turnoActual != null) {
            turnoActual = turnosFacade.find(turnoActual.getIdTurno());
            turnoActual.setEstado("en_espera");
            turnosFacade.edit(turnoActual);
            turnoActual = null;
        }
        turnoCita = "";
        //ingresar detalles        
        for (FilaDataTable servicioFactura : listaServiciosFactura) {
            try {
                FacFacturaServicioPK llave = new FacFacturaServicioPK(Integer.parseInt(servicioFactura.getColumna1()), nuevaFactura.getIdFacturaPaciente());
                FacFacturaServicio nuevoServicioFactura = new FacFacturaServicio();
                nuevoServicioFactura.setFacFacturaServicioPK(llave);//nuevaFila.setColumna1(String.valueOf(listaServiciosFactura.size() + 1));
                nuevoServicioFactura.setFechaServicio(formateadorFecha.parse(servicioFactura.getColumna2()));//nuevaFila.setColumna2(dateFormat.format(fechaServicio));
                //nuevaFila.setColumna4(tipoTarifaServicio);
                nuevoServicioFactura.setValorServicio(Double.parseDouble(servicioFactura.getColumna5()));//nuevaFila.setColumna5(String.valueOf(valorUnitarioServicio));
                nuevoServicioFactura.setCantidadServicio(Short.parseShort(servicioFactura.getColumna6()));//nuevaFila.setColumna6(String.valueOf(cantidadServicio));
                nuevoServicioFactura.setValorParcial(Double.parseDouble(servicioFactura.getColumna7()));//nuevaFila.setColumna7(String.valueOf(valorFinalServicio));

                //nuevaFila.setColumna8(usuariosFacade.find(Integer.parseInt(idPrestadorServicio)).nombreCompleto());
                nuevoServicioFactura.setDiagnosticoPrincipal(servicioFactura.getColumna9());
                nuevoServicioFactura.setDiagnosticoRelacionado(servicioFactura.getColumna10());

                //nuevaFila.setColumna20(pacienteSeleccionado.getIdPaciente().toString());
                nuevoServicioFactura.setIdMedico(usuariosFacade.find(Integer.parseInt(servicioFactura.getColumna21())));//nuevaFila.setColumna21(idPrestadorServicio);
                nuevoServicioFactura.setIdServicio(servicioFacade.find(Integer.parseInt(servicioFactura.getColumna22())));//nuevaFila.setColumna22(idServicioManual);

                nuevoServicioFactura.setValorIva(nuevoServicioFactura.getValorParcial() * (valorIva / 100));
                nuevoServicioFactura.setValorCree(nuevoServicioFactura.getValorParcial() * (valorCree / 100));
                if (facturarComoParticular) {
                    nuevoServicioFactura.setValorEmpresa(Double.parseDouble("0"));
                    nuevoServicioFactura.setValorUsuario(nuevoServicioFactura.getValorParcial() + nuevoServicioFactura.getValorIva() + nuevoServicioFactura.getValorCree());
                } else {
                    nuevoServicioFactura.setValorEmpresa(nuevoServicioFactura.getValorParcial() + nuevoServicioFactura.getValorIva() + nuevoServicioFactura.getValorCree());
                    nuevoServicioFactura.setValorUsuario(Double.parseDouble("0"));
                }

                if (servicioFactura.getColumna30().compareTo("-1") != 0) {//ELIMINAR SI FORMABA PARTE DE CONSUMOS
                    consumoServicioFacade.remove(consumoServicioFacade.find(Integer.parseInt(servicioFactura.getColumna30())));//            nuevaFila.setColumna30("-1");//COLUMNA 30 CONTIENE EL IDENTIFICADOR EN TABLA fac_consumo_servicio //como fue agreado desde la misma fatura no refiere a la tabla fac_consumo_servicio                    
                }
                facturaServicioFacade.create(nuevoServicioFactura);
            } catch (ParseException ex) {
                //System.out.println("Error -- " + ex.getMessage());
            }
        }
        for (FilaDataTable medicamentoFactura : listaMedicamentosFactura) {
            try {
                FacFacturaMedicamentoPK llave = new FacFacturaMedicamentoPK(Integer.parseInt(medicamentoFactura.getColumna1()), nuevaFactura.getIdFacturaPaciente());
                FacFacturaMedicamento nuevoMedicamentoFactura = new FacFacturaMedicamento();
                nuevoMedicamentoFactura.setFacFacturaMedicamentoPK(llave); //nuevaFila.setColumna1(String.valueOf(listaMedicamentosFactura.size() + 1));
                nuevoMedicamentoFactura.setFechaMedicamento(formateadorFecha.parse(medicamentoFactura.getColumna2()));//nuevaFila.setColumna2(dateFormat.format(fechaMedicamento));
                //nuevaFila.setColumna3(medicamentoFacade.find(Integer.parseInt(idMedicamentoManual)).getNombreMedicamento());
                //nuevaFila.setColumna4(medicamentoFacade.find(Integer.parseInt(idMedicamentoManual)).getFormaMedicamento());

                nuevoMedicamentoFactura.setValorMedicamento(Double.parseDouble(medicamentoFactura.getColumna5()));//nuevaFila.setColumna5(String.valueOf(valorUnitarioMedicamento));
                nuevoMedicamentoFactura.setCantidadMedicamento(Short.parseShort(medicamentoFactura.getColumna6()));//nuevaFila.setColumna6(String.valueOf(cantidadMedicamento));
                nuevoMedicamentoFactura.setValorParcial(Double.parseDouble(medicamentoFactura.getColumna7()));//nuevaFila.setColumna7(String.valueOf(valorFinalMedicamento));
                //nuevaFila.setColumna8(usuariosFacade.find(Integer.parseInt(idPrestadorMedicamento)).nombreCompleto());
                //nuevaFila.setColumna20(pacienteSeleccionado.getIdPaciente().toString());
                nuevoMedicamentoFactura.setIdMedico(usuariosFacade.find(Integer.parseInt(medicamentoFactura.getColumna21())));//nuevaFila.setColumna21(idPrestadorMedicamento);
                nuevoMedicamentoFactura.setIdMedicamento(medicamentoFacade.find(Integer.parseInt(medicamentoFactura.getColumna22())));//nuevaFila.setColumna22(idMedicamentoManual);

                nuevoMedicamentoFactura.setValorIva(valorIva * nuevoMedicamentoFactura.getValorParcial());
                nuevoMedicamentoFactura.setValorCree(valorCree * nuevoMedicamentoFactura.getValorParcial());
                if (facturarComoParticular) {
                    nuevoMedicamentoFactura.setValorEmpresa(Double.parseDouble("0"));
                    nuevoMedicamentoFactura.setValorUsuario(nuevoMedicamentoFactura.getValorParcial() + nuevoMedicamentoFactura.getValorIva() + nuevoMedicamentoFactura.getValorCree());
                } else {
                    nuevoMedicamentoFactura.setValorEmpresa(nuevoMedicamentoFactura.getValorParcial() + nuevoMedicamentoFactura.getValorIva() + nuevoMedicamentoFactura.getValorCree());
                    nuevoMedicamentoFactura.setValorUsuario(Double.parseDouble("0"));
                }

                if (medicamentoFactura.getColumna30().compareTo("-1") != 0) {//ELIMINAR SI FORMABA PARTE DE CONSUMOS
                    consumoMedicamentoFacade.remove(consumoMedicamentoFacade.find(Integer.parseInt(medicamentoFactura.getColumna30())));//            nuevaFila.setColumna30("-1");//COLUMNA 30 CONTIENE EL IDENTIFICADOR EN TABLA fac_consumo_servicio //como fue agreado desde la misma fatura no refiere a la tabla fac_consumo_servicio                    
                }
                facturaMedicamentoFacade.create(nuevoMedicamentoFactura);
            } catch (ParseException ex) {
                //System.out.println("Error -- " + ex.getMessage());
            }
        }
        for (FilaDataTable paqueteFactura : listaPaquetesFactura) {
            try {
                FacFacturaPaquetePK llave = new FacFacturaPaquetePK(Integer.parseInt(paqueteFactura.getColumna1()), nuevaFactura.getIdFacturaPaciente());
                FacFacturaPaquete nuevoPaqueteFactura = new FacFacturaPaquete();
                nuevoPaqueteFactura.setFacFacturaPaquetePK(llave); //nuevaFila.setColumna1(String.valueOf(listaPaquetesFactura.size() + 1));
                nuevoPaqueteFactura.setFechaPaquete(formateadorFecha.parse(paqueteFactura.getColumna2()));//nuevaFila.setColumna2(dateFormat.format(fechaPaquete));
                //nuevaFila.setColumna3(medicamentoFacade.find(Integer.parseInt(idPaqueteManual)).getNombrePaquete());
                //nuevaFila.setColumna4(medicamentoFacade.find(Integer.parseInt(idPaqueteManual)).getFormaPaquete());
                nuevoPaqueteFactura.setValorPaquete(Double.parseDouble(paqueteFactura.getColumna5()));//nuevaFila.setColumna5(String.valueOf(valorUnitarioPaquete));
                nuevoPaqueteFactura.setCantidadPaquete(Short.parseShort(paqueteFactura.getColumna6()));//nuevaFila.setColumna6(String.valueOf(cantidadPaquete));
                nuevoPaqueteFactura.setValorParcial(Double.parseDouble(paqueteFactura.getColumna7()));//nuevaFila.setColumna7(String.valueOf(valorFinalPaquete));
                //nuevaFila.setColumna8(usuariosFacade.find(Integer.parseInt(idPrestadorPaquete)).nombreCompleto());
                //nuevaFila.setColumna20(pacienteSeleccionado.getIdPaciente().toString());
                nuevoPaqueteFactura.setIdMedico(usuariosFacade.find(Integer.parseInt(paqueteFactura.getColumna21())));//nuevaFila.setColumna21(idPrestadorPaquete);
                nuevoPaqueteFactura.setIdPaquete(paqueteFacade.find(Integer.parseInt(paqueteFactura.getColumna22())));//nuevaFila.setColumna22(idPaqueteManual);

                nuevoPaqueteFactura.setValorIva(valorIva * nuevoPaqueteFactura.getValorParcial());
                nuevoPaqueteFactura.setValorCree(valorCree * nuevoPaqueteFactura.getValorParcial());
                if (facturarComoParticular) {
                    nuevoPaqueteFactura.setValorEmpresa(Double.parseDouble("0"));
                    nuevoPaqueteFactura.setValorUsuario(nuevoPaqueteFactura.getValorParcial() + nuevoPaqueteFactura.getValorIva() + nuevoPaqueteFactura.getValorCree());
                } else {
                    nuevoPaqueteFactura.setValorEmpresa(nuevoPaqueteFactura.getValorParcial() + nuevoPaqueteFactura.getValorIva() + nuevoPaqueteFactura.getValorCree());
                    nuevoPaqueteFactura.setValorUsuario(Double.parseDouble("0"));
                }

                if (paqueteFactura.getColumna30().compareTo("-1") != 0) {//ELIMINAR SI FORMABA PARTE DE CONSUMOS
                    consumoPaqueteFacade.remove(consumoPaqueteFacade.find(Integer.parseInt(paqueteFactura.getColumna30())));//            nuevaFila.setColumna30("-1");//COLUMNA 30 CONTIENE EL IDENTIFICADOR EN TABLA fac_consumo_servicio //como fue agreado desde la misma fatura no refiere a la tabla fac_consumo_servicio                    
                }
                facturaPaqueteFacade.create(nuevoPaqueteFactura);
            } catch (ParseException ex) {
                //System.out.println("Error -- " + ex.getMessage());
            }
        }
        for (FilaDataTable insumoFactura : listaInsumosFactura) {
            try {
                FacFacturaInsumoPK llave = new FacFacturaInsumoPK(Integer.parseInt(insumoFactura.getColumna1()), nuevaFactura.getIdFacturaPaciente());
                FacFacturaInsumo nuevoInsumoFactura = new FacFacturaInsumo();
                nuevoInsumoFactura.setFacFacturaInsumoPK(llave); //nuevaFila.setColumna1(String.valueOf(listaInsumosFactura.size() + 1));
                nuevoInsumoFactura.setFechaInsumo(formateadorFecha.parse(insumoFactura.getColumna2()));//nuevaFila.setColumna2(dateFormat.format(fechaInsumo));
                //nuevaFila.setColumna3(medicamentoFacade.find(Integer.parseInt(idInsumoManual)).getNombreInsumo());
                nuevoInsumoFactura.setValorInsumo(Double.parseDouble(insumoFactura.getColumna4()));//nuevaFila.setColumna4(String.valueOf(valorUnitarioInsumo));
                nuevoInsumoFactura.setCantidadInsumo(Short.parseShort(insumoFactura.getColumna5()));//nuevaFila.setColumna5(String.valueOf(cantidadInsumo));
                nuevoInsumoFactura.setValorParcial(Double.parseDouble(insumoFactura.getColumna6()));//nuevaFila.setColumna6(String.valueOf(valorFinalInsumo));
                //nuevaFila.setColumna7(usuariosFacade.find(Integer.parseInt(idPrestadorInsumo)).nombreCompleto());
                //nuevaFila.setColumna20(pacienteSeleccionado.getIdPaciente().toString());
                nuevoInsumoFactura.setIdMedico(usuariosFacade.find(Integer.parseInt(insumoFactura.getColumna21())));//nuevaFila.setColumna21(idPrestadorInsumo);
                nuevoInsumoFactura.setIdInsumo(insumoFacade.find(Integer.parseInt(insumoFactura.getColumna22())));//nuevaFila.setColumna22(idInsumoManual);

                nuevoInsumoFactura.setValorIva(valorIva * nuevoInsumoFactura.getValorParcial());
                nuevoInsumoFactura.setValorCree(valorCree * nuevoInsumoFactura.getValorParcial());
                if (facturarComoParticular) {
                    nuevoInsumoFactura.setValorEmpresa(Double.parseDouble("0"));
                    nuevoInsumoFactura.setValorUsuario(nuevoInsumoFactura.getValorParcial() + nuevoInsumoFactura.getValorIva() + nuevoInsumoFactura.getValorCree());
                } else {
                    nuevoInsumoFactura.setValorEmpresa(nuevoInsumoFactura.getValorParcial() + nuevoInsumoFactura.getValorIva() + nuevoInsumoFactura.getValorCree());
                    nuevoInsumoFactura.setValorUsuario(Double.parseDouble("0"));
                }

                if (insumoFactura.getColumna30().length() != 0 && insumoFactura.getColumna30().compareTo("-1") != 0) {//ELIMINAR SI FORMABA PARTE DE CONSUMOS
                    consumoInsumoFacade.remove(consumoInsumoFacade.find(Integer.parseInt(insumoFactura.getColumna30())));//            nuevaFila.setColumna30("-1");//COLUMNA 30 CONTIENE EL IDENTIFICADOR EN TABLA fac_consumo_servicio //como fue agreado desde la misma fatura no refiere a la tabla fac_consumo_servicio                    
                }
                facturaInsumoFacade.create(nuevoInsumoFactura);
            } catch (ParseException ex) {
                //System.out.println("Error -- " + ex.getMessage());
            }
        }
        pacienteSeleccionadoTabla = null;
        limpiarFormularioFacturacion();
        RequestContext.getCurrentInstance().update("IdFormFacturacion");
        imprimirMensaje("Correcto", nuevaFactura.getTipoDocumento().getDescripcion() + " " + numDocStr + " se ha creado.", FacesMessage.SEVERITY_INFO);
    }

    public void mostrarValoresPrueba() {
        //System.out.println("valorParcial " + valorParcial);
        //System.out.println("valorEmpresa " + valorEmpresa);
        //System.out.println("valorUsuario " + valorUsuario);
        //System.out.println("valorIvaStr " + valorIvaStr);
        //System.out.println("valorCree " + valorCreeStr);
        //System.out.println("valorTotal " + valorTotal);
    }

    private void recalcularValorFactura() {
        //System.out.println("Entra a recalcular valor factura");
        double vlr = 0;
        for (FilaDataTable servicioFactura : listaServiciosFactura) {
            vlr = vlr + Double.parseDouble(servicioFactura.getColumna7());
        }
        for (FilaDataTable medicamentoFactura : listaMedicamentosFactura) {
            vlr = vlr + Double.parseDouble(medicamentoFactura.getColumna7());
        }
        for (FilaDataTable paqueteFactura : listaPaquetesFactura) {
            vlr = vlr + Double.parseDouble(paqueteFactura.getColumna7());
        }
        for (FilaDataTable insumoFactura : listaInsumosFactura) {
            vlr = vlr + Double.parseDouble(insumoFactura.getColumna6());
        }
        valorParcial = vlr;
        if (facturarComoParticular) {//se debe cobrar todo al paciente(SIN CUOTA NI COPAGO)
            valorEmpresa = 0;
            valorUsuario = valorParcial + (valorParcial * (valorIva / 100)) + (valorParcial * (valorCree / 100));
            valorIvaStr = String.valueOf(valorIva) + "% (" + String.valueOf((valorParcial * (valorIva / 100))) + ")";
            valorCreeStr = String.valueOf(valorCree) + "% (" + String.valueOf((valorParcial * (valorCree / 100))) + ")";
        } else {//se le debe cobrar solo cuota moderadora y/o copago si aplica            
            valorUsuario = valorCopago + valorCuotaModeradora;
            valorIvaStr = String.valueOf(valorIva) + "% (" + String.valueOf((valorParcial * (valorIva / 100))) + ")";
            valorCreeStr = String.valueOf(valorCree) + "% (" + String.valueOf((valorParcial * (valorCree / 100))) + ")";
            valorEmpresa = valorParcial - valorUsuario + (valorParcial * (valorIva / 100)) + (valorParcial * (valorCree / 100));//vlr - valorCopago - valorCuotaModeradora;            
        }
        valorTotal = valorUsuario + valorEmpresa;
    }

    private void renumerarIdLista(List<FilaDataTable> lista) {
        //reasignar identificador de la fila<=>columna1 a una lista (Al eliminar y crear al final puede quedar el mismo)
        int id = 0;
        for (FilaDataTable fila : lista) {
            id++;
            fila.setColumna1(String.valueOf(id));

        }
    }

    //---------------------------------------------------
    //-----------------FUNCIONES CONSUMOS ---------------
    //---------------------------------------------------    
    private void recargarFilasTablaConsumoServicio() {
        serviciosConsumoSeleccionados = new ArrayList<>();
        listaServiciosConsumo = new ArrayList<>();
        listaServiciosConsumoFiltro = new ArrayList<>();
        FilaDataTable nuevaFila;
        if (pacienteSeleccionado != null) {
            List<FacConsumoServicio> listaServiciosCon = pacienteSeleccionado.getFacConsumoServicioList();
            for (FacConsumoServicio servicioConsumo : listaServiciosCon) {//lista servicios
                if (!buscarConsumoServicioEnFactura(servicioConsumo.getIdConsumoServicio().toString())) {//buscar si no esta en la factura
                    nuevaFila = new FilaDataTable();
                    nuevaFila.setColumna1(servicioConsumo.getIdConsumoServicio().toString());
                    nuevaFila.setColumna2(formateadorFecha.format(servicioConsumo.getFecha()));
                    nuevaFila.setColumna3(servicioConsumo.getIdServicio().getNombreServicio());
                    nuevaFila.setColumna4(servicioConsumo.getTipoTarifa());
                    nuevaFila.setColumna5(servicioConsumo.getValorUnitario().toString());
                    nuevaFila.setColumna6(servicioConsumo.getCantidad().toString());
                    nuevaFila.setColumna7(servicioConsumo.getValorFinal().toString());
                    nuevaFila.setColumna8(servicioConsumo.getIdPrestador().nombreCompleto());
                    listaServiciosConsumo.add(nuevaFila);
                }
            }
            listaServiciosConsumoFiltro.addAll(listaServiciosConsumo);
        }
        tituloTabServiciosConsumo = "Servicios (" + listaServiciosConsumo.size() + ")";
    }

    private void recargarFilasTablaConsumoMedicamentos() {
        medicamentosConsumoSeleccionados = new ArrayList<>();
        listaMedicamentosConsumo = new ArrayList<>();
        listaMedicamentosConsumoFiltro = new ArrayList<>();
        FilaDataTable nuevaFila;
        if (pacienteSeleccionado != null) {
            List<FacConsumoMedicamento> listaMedicamentosCon = pacienteSeleccionado.getFacConsumoMedicamentoList();
            for (FacConsumoMedicamento medicamentoConsumo : listaMedicamentosCon) {//lista servicios
                if (!buscarConsumoMedicamentoEnFactura(medicamentoConsumo.getIdConsumoMedicamento().toString())) {//buscar si no esta en la factura
                    nuevaFila = new FilaDataTable();
                    nuevaFila.setColumna1(medicamentoConsumo.getIdConsumoMedicamento().toString());
                    nuevaFila.setColumna2(formateadorFecha.format(medicamentoConsumo.getFecha()));
                    nuevaFila.setColumna3(medicamentoConsumo.getIdMedicamento().getNombreMedicamento());
                    nuevaFila.setColumna4(medicamentoConsumo.getIdMedicamento().getFormaMedicamento());
                    nuevaFila.setColumna5(medicamentoConsumo.getValorUnitario().toString());
                    nuevaFila.setColumna6(medicamentoConsumo.getCantidad().toString());
                    nuevaFila.setColumna7(medicamentoConsumo.getValorFinal().toString());
                    nuevaFila.setColumna8(medicamentoConsumo.getIdPrestador().nombreCompleto());
                    listaMedicamentosConsumo.add(nuevaFila);
                }
            }
            listaMedicamentosConsumoFiltro.addAll(listaMedicamentosConsumo);
        }
        tituloTabMedicamentosConsumo = "Medicamentos (" + listaMedicamentosConsumo.size() + ")";
    }

    private void recargarFilasTablaConsumoPaquetes() {
        paquetesConsumoSeleccionados = new ArrayList<>();
        listaPaquetesConsumo = new ArrayList<>();
        listaPaquetesConsumoFiltro = new ArrayList<>();
        FilaDataTable nuevaFila;
        if (pacienteSeleccionado != null) {
            List<FacConsumoPaquete> listaPaquetesCon = pacienteSeleccionado.getFacConsumoPaqueteList();
            for (FacConsumoPaquete paqueteConsumo : listaPaquetesCon) {//lista servicios
                if (!buscarConsumoPaqueteEnFactura(paqueteConsumo.getIdConsumoPaquete().toString())) {//buscar si no esta en la factura
                    nuevaFila = new FilaDataTable();
                    nuevaFila.setColumna1(paqueteConsumo.getIdConsumoPaquete().toString());
                    nuevaFila.setColumna2(formateadorFecha.format(paqueteConsumo.getFecha()));
                    nuevaFila.setColumna3(paqueteConsumo.getIdPaquete().getNombrePaquete());
                    nuevaFila.setColumna4(paqueteConsumo.getValorUnitario().toString());
                    nuevaFila.setColumna5(paqueteConsumo.getCantidad().toString());
                    nuevaFila.setColumna6(paqueteConsumo.getValorFinal().toString());
                    nuevaFila.setColumna7(paqueteConsumo.getIdPrestador().nombreCompleto());
                    listaPaquetesConsumo.add(nuevaFila);
                }
            }
            listaPaquetesConsumoFiltro.addAll(listaPaquetesConsumo);
        }
        tituloTabPaquetesConsumo = "Paquetes (" + listaPaquetesConsumo.size() + ")";
    }

    private void recargarFilasTablaConsumoInsumos() {
        insumosConsumoSeleccionados = new ArrayList<>();
        listaInsumosConsumo = new ArrayList<>();
        listaInsumosConsumoFiltro = new ArrayList<>();
        FilaDataTable nuevaFila;
        if (pacienteSeleccionado != null) {
            List<FacConsumoInsumo> listaInsumosCon = pacienteSeleccionado.getFacConsumoInsumoList();
            for (FacConsumoInsumo insumoConsumo : listaInsumosCon) {//lista servicios
                if (!buscarConsumoInsumoEnFactura(insumoConsumo.getIdConsumoInsumo().toString())) {//buscar si no esta en la factura
                    nuevaFila = new FilaDataTable();
                    nuevaFila.setColumna1(insumoConsumo.getIdConsumoInsumo().toString());
                    nuevaFila.setColumna2(formateadorFecha.format(insumoConsumo.getFecha()));
                    nuevaFila.setColumna3(insumoConsumo.getIdInsumo().getNombreInsumo());
                    nuevaFila.setColumna4(insumoConsumo.getValorUnitario().toString());
                    nuevaFila.setColumna5(insumoConsumo.getCantidad().toString());
                    nuevaFila.setColumna6(insumoConsumo.getValorFinal().toString());
                    nuevaFila.setColumna7(insumoConsumo.getIdPrestador().nombreCompleto());
                    listaInsumosConsumo.add(nuevaFila);
                }
            }
            listaInsumosConsumoFiltro.addAll(listaInsumosConsumo);
        }
        tituloTabInsumosConsumo = "Insumos (" + listaInsumosConsumo.size() + ")";
    }

    public void recargarFilasTablasConsumo() {//cargar los consumos cuando se carga un paciente
        //System.out.println("recargarFilasTablasConsumo");
        recargarFilasTablaConsumoServicio();
        recargarFilasTablaConsumoMedicamentos();
        recargarFilasTablaConsumoPaquetes();
        recargarFilasTablaConsumoInsumos();

    }

    public void cargarDialogoConsumos() {
        pacienteSeleccionado = pacientesFacade.find(pacienteSeleccionado.getIdPaciente());
        recargarFilasTablasConsumo();
        RequestContext.getCurrentInstance().update("IdFormFacturacion:IdTabViewDialog");
        RequestContext.getCurrentInstance().execute("PF('dlgSeleccionConsumos').show();");
    }

    private boolean buscarConsumoServicioEnFactura(String idConsumoServicio) {//DETERMINA SI UN COSUMO DE TIPO SERVICIO YA FUE AGRAGADO A LA FACTURA        
        for (FilaDataTable servicioFactura : listaServiciosFactura) {
            if (servicioFactura.getColumna30().compareTo(idConsumoServicio) == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean buscarConsumoMedicamentoEnFactura(String idConsumoMedicamento) {//DETERMINA SI UN COSUMO DE TIPO MEDICAMENTO YA FUE AGRAGADO A LA FACTURA        
        for (FilaDataTable medicamentoFactura : listaMedicamentosFactura) {
            if (medicamentoFactura.getColumna30().compareTo(idConsumoMedicamento) == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean buscarConsumoPaqueteEnFactura(String idConsumoPaquete) {//DETERMINA SI UN COSUMO DE TIPO PAQUETE YA FUE AGRAGADO A LA FACTURA        
        for (FilaDataTable serviciosFactura : listaPaquetesFactura) {
            if (serviciosFactura.getColumna30().compareTo(idConsumoPaquete) == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean buscarConsumoInsumoEnFactura(String idConsumoInsumo) {//DETERMINA SI UN COSUMO DE TIPO INSUMO YA FUE AGRAGADO A LA FACTURA        
        for (FilaDataTable insumoFactura : listaInsumosFactura) {
            if (insumoFactura.getColumna30().compareTo(idConsumoInsumo) == 0) {
                return true;
            }
        }
        return false;
    }

    //---------------------------------------------------
    //-----------------FUNCIONES SERVICIOS ---------------
    //---------------------------------------------------    
    public void cambiaServicio() {//se limpia el formulario
        idPrestadorServicio = "";
        fechaServicio = new Date();
        tipoTarifaServicio = "";
        cantidadServicio = 1;
        diagnosticoPrincipal = "";
        diagnosticoRelacionado = "";
        valorUnitarioServicio = 0;
        valorFinalServicio = 0;
        calcularValoresServicio();
    }

    private FacManualTarifarioServicio buscarEnListaServiciosManual(int idServicio) {
        //determinar si un servicio 
        if (listaServiciosManual != null && !listaServiciosManual.isEmpty()) {
            for (FacManualTarifarioServicio servicioManual : listaServiciosManual) {
                if (servicioManual.getFacServicio().getIdServicio() == idServicio) {
                    return servicioManual;
                }
            }
        }
        return null;
    }

    public void calcularValoresServicio() {//calculo del valor final de un servicio
        if (validarNoVacio(idServicioManual)) {
            FacManualTarifarioServicio s = buscarEnListaServiciosManual(Integer.parseInt(idServicioManual));
            tipoTarifaServicio = s.getTipoTarifa();
            valorUnitarioServicio = s.getValorFinal();
            valorFinalServicio = valorUnitarioServicio * cantidadServicio;
        }
    }

    public void cargarDialogoAgregarServicio() {//se abre dialogo de registro de un nuevo servicio en la factura
        if (manualTarifarioPaciente == null) {
        }
        listaServiciosManual = manualTarifarioPaciente.getFacManualTarifarioServicioList();
        if (listaServiciosManual != null && !listaServiciosManual.isEmpty()) {
            idServicioManual = listaServiciosManual.get(0).getFacServicio().getIdServicio().toString();
        }
        cambiaServicio();
        RequestContext.getCurrentInstance().update("IdFormFacturacion:IdPanelAgregarServicio");
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarServicio').show();");
    }

    public void agregarServicioAFactura() {//agregar desde la seccion de items de factura
        //System.out.println("Agregar servicio a factura");
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
        FilaDataTable nuevaFila = new FilaDataTable();
        nuevaFila.setColumna1(String.valueOf(listaServiciosFactura.size() + 1));
        nuevaFila.setColumna2(formateadorFecha.format(fechaServicio));
        nuevaFila.setColumna3(servicioFacade.find(Integer.parseInt(idServicioManual)).getNombreServicio());
        nuevaFila.setColumna4(tipoTarifaServicio);
        nuevaFila.setColumna5(String.valueOf(valorUnitarioServicio));
        nuevaFila.setColumna6(String.valueOf(cantidadServicio));
        nuevaFila.setColumna7(String.valueOf(valorFinalServicio));
        nuevaFila.setColumna8(usuariosFacade.find(Integer.parseInt(idPrestadorServicio)).nombreCompleto());
        nuevaFila.setColumna9(diagnosticoPrincipal);
        nuevaFila.setColumna10(diagnosticoRelacionado);
        nuevaFila.setColumna20(pacienteSeleccionado.getIdPaciente().toString());
        nuevaFila.setColumna21(idPrestadorServicio);
        nuevaFila.setColumna22(idServicioManual);
        nuevaFila.setColumna30("-1");//COLUMNA 30 CONTIENE EL IDENTIFICADOR EN TABLA fac_consumo_servicio //como fue agreado desde la misma fatura no refiere a la tabla fac_consumo_servicio
        listaServiciosFactura.add(nuevaFila);
        renumerarIdLista(listaServiciosFactura);
        listaServiciosFacturaFiltro = new ArrayList<>();
        listaServiciosFacturaFiltro.addAll(listaServiciosFactura);
        recalcularValorFactura();
        tituloTabServiciosFactura = "Servicios (" + listaServiciosFactura.size() + ")";
        if (!cargandoDesdeTab) {
            RequestContext.getCurrentInstance().execute("PF('wvTablaServiciosFactura').clearFilters(); PF('wvTablaServiciosFactura').getPaginator().setPage(0);");
            RequestContext.getCurrentInstance().execute("PF('dialogoAgregarServicio').hide();");
            RequestContext.getCurrentInstance().update("IdFormFacturacion");
        }
    }

    public void agregarServiciosDesdeConsumos() {//agregar desde el dialogo de consumos
        if (serviciosConsumoSeleccionados == null || serviciosConsumoSeleccionados.isEmpty()) {
            imprimirMensaje("Error", "Ne se ha seleccionado ningún servicio", FacesMessage.SEVERITY_ERROR);
        } else {
            FilaDataTable nuevaFila;
            for (FilaDataTable servicio : serviciosConsumoSeleccionados) {
                nuevaFila = new FilaDataTable();
                FacConsumoServicio consumoServicioBuscado = consumoServicioFacade.find(Integer.parseInt(servicio.getColumna1()));

                nuevaFila.setColumna1(String.valueOf(listaServiciosFactura.size() + 1));
                nuevaFila.setColumna2(formateadorFecha.format(consumoServicioBuscado.getFecha()));
                nuevaFila.setColumna3(consumoServicioBuscado.getIdServicio().getNombreServicio());
                nuevaFila.setColumna4(consumoServicioBuscado.getTipoTarifa());
                nuevaFila.setColumna5(String.valueOf(consumoServicioBuscado.getValorUnitario()));
                nuevaFila.setColumna6(String.valueOf(consumoServicioBuscado.getCantidad()));
                nuevaFila.setColumna7(String.valueOf(consumoServicioBuscado.getValorFinal()));
                nuevaFila.setColumna8(consumoServicioBuscado.getIdPrestador().nombreCompleto());
                nuevaFila.setColumna9(consumoServicioBuscado.getDiagnosticoPrincipal());
                nuevaFila.setColumna10(consumoServicioBuscado.getDiagnosticoRelacionado());
                nuevaFila.setColumna20(pacienteSeleccionado.getIdPaciente().toString());
                nuevaFila.setColumna21(consumoServicioBuscado.getIdPrestador().getIdUsuario().toString());
                nuevaFila.setColumna22(consumoServicioBuscado.getIdServicio().getIdServicio().toString());

                nuevaFila.setColumna30(consumoServicioBuscado.getIdConsumoServicio().toString());//COLUMNA 30 CONTIENE EL IDENTIFICADOR EN TABLA fac_consumo_servicio
                listaServiciosFactura.add(nuevaFila);
            }
            renumerarIdLista(listaServiciosFactura);
            listaServiciosFacturaFiltro = new ArrayList<>();
            listaServiciosFacturaFiltro.addAll(listaServiciosFactura);
            tituloTabServiciosFactura = "Servicios (" + listaServiciosFactura.size() + ")";
            recargarFilasTablaConsumoServicio();
            recalcularValorFactura();
        }
    }

    public void seleccionarTodoNingunServicioDeConsumos() {//seleccionarlos todos o ninguno
        if (serviciosConsumoSeleccionados != null) {
            if (serviciosConsumoSeleccionados.isEmpty()) {
                serviciosConsumoSeleccionados = listaServiciosConsumo;
            } else {
                serviciosConsumoSeleccionados = new ArrayList<>();
            }
        } else {
            serviciosConsumoSeleccionados = new ArrayList<>();
            serviciosConsumoSeleccionados = listaServiciosConsumo;
        }
        //RequestContext.getCurrentInstance().update("IdFormDialogs:IdTabViewDialog");
        //RequestContext.getCurrentInstance().update("IdTabViewDialog");
    }

    public void quitarServicioFactura() {
        if (servicioFacturaSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún servicio", FacesMessage.SEVERITY_ERROR);
            return;
        }

        for (int i = 0; i < listaServiciosFactura.size(); i++) {
            if (listaServiciosFactura.get(i).getColumna1().compareTo(servicioFacturaSeleccionado.getColumna1()) == 0) {
                listaServiciosFactura.remove(i);
                break;
            }
        }
        servicioFacturaSeleccionado = null;
        renumerarIdLista(listaServiciosFactura);
        listaServiciosFacturaFiltro = new ArrayList<>();
        listaServiciosFacturaFiltro.addAll(listaServiciosFactura);
        tituloTabServiciosFactura = "Servicios (" + listaServiciosFactura.size() + ")";
        recargarFilasTablaConsumoServicio();
        recalcularValorFactura();
    }

    //---------------------------------------------------
    //-----------------FUNCIONES MEDICAMENTOS ---------------
    //---------------------------------------------------    
    public void cambiaMedicamento() {//se limpia el formulario
        idPrestadorMedicamento = "";
        fechaMedicamento = new Date();
        cantidadMedicamento = 1;
        valorUnitarioMedicamento = 0;
        valorFinalMedicamento = 0;
        calcularValoresMedicamento();
    }

    private FacManualTarifarioMedicamento buscarEnListaMedicamentosManual(int idMedicamento) {
        //determinar si un Medicamento 
        if (listaMedicamentosManual != null && !listaMedicamentosManual.isEmpty()) {
            for (FacManualTarifarioMedicamento medicamentoManual : listaMedicamentosManual) {
                if (medicamentoManual.getCfgMedicamento().getIdMedicamento() == idMedicamento) {
                    return medicamentoManual;
                }
            }
        }
        return null;
    }

    public void calcularValoresMedicamento() {//calculo del valor final de un medicamento
        if (validarNoVacio(idMedicamentoManual)) {
            FacManualTarifarioMedicamento s = buscarEnListaMedicamentosManual(Integer.parseInt(idMedicamentoManual));
            valorUnitarioMedicamento = s.getValorFinal();
            valorFinalMedicamento = valorUnitarioMedicamento * cantidadMedicamento;
        }
    }

    public void cargarDialogoAgregarMedicamento() {//se abre dialogo de registro de un nuevo medicamento en la factura
        if (manualTarifarioPaciente == null) {
        }
        listaMedicamentosManual = manualTarifarioPaciente.getFacManualTarifarioMedicamentoList();
        if (listaMedicamentosManual != null && !listaMedicamentosManual.isEmpty()) {
            idMedicamentoManual = listaMedicamentosManual.get(0).getCfgMedicamento().getIdMedicamento().toString();
        }
        cambiaMedicamento();
        RequestContext.getCurrentInstance().update("IdFormFacturacion:IdPanelAgregarMedicamento");
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarMedicamento').show();");
    }

    public void agregarMedicamentoAFactura() {//agregar desde la seccion de items de factura
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
        FilaDataTable nuevaFila = new FilaDataTable();

        nuevaFila.setColumna1(String.valueOf(listaMedicamentosFactura.size() + 1));
        nuevaFila.setColumna2(formateadorFecha.format(fechaMedicamento));
        nuevaFila.setColumna3(medicamentoFacade.find(Integer.parseInt(idMedicamentoManual)).getNombreMedicamento());
        nuevaFila.setColumna4(medicamentoFacade.find(Integer.parseInt(idMedicamentoManual)).getFormaMedicamento());
        nuevaFila.setColumna5(String.valueOf(valorUnitarioMedicamento));
        nuevaFila.setColumna6(String.valueOf(cantidadMedicamento));
        nuevaFila.setColumna7(String.valueOf(valorFinalMedicamento));
        nuevaFila.setColumna8(usuariosFacade.find(Integer.parseInt(idPrestadorMedicamento)).nombreCompleto());

        nuevaFila.setColumna20(pacienteSeleccionado.getIdPaciente().toString());
        nuevaFila.setColumna21(idPrestadorMedicamento);
        nuevaFila.setColumna22(idMedicamentoManual);
        nuevaFila.setColumna30("-1");//COLUMNA 30 CONTIENE EL IDENTIFICADOR EN TABLA fac_consumo_medicamento //como fue agreado desde la misma fatura no refiere a la tabla fac_consumo_medicamento
        listaMedicamentosFactura.add(nuevaFila);
        renumerarIdLista(listaMedicamentosFactura);
        listaMedicamentosFacturaFiltro = new ArrayList<>();
        listaMedicamentosFacturaFiltro.addAll(listaMedicamentosFactura);
        recalcularValorFactura();
        tituloTabMedicamentosFactura = "Medicamentos (" + listaMedicamentosFactura.size() + ")";
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarMedicamento').hide(); PF('wvTablaMedicamentosFactura').clearFilters(); PF('wvTablaMedicamentosFactura').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("IdFormFacturacion");
    }

    public void agregarMedicamentosDesdeConsumos() {//agregar desde el dialogo de consumos
        if (medicamentosConsumoSeleccionados == null || medicamentosConsumoSeleccionados.isEmpty()) {
            imprimirMensaje("Error", "Ne se ha seleccionado ningún medicamento", FacesMessage.SEVERITY_ERROR);
        } else {
            FilaDataTable nuevaFila;
            for (FilaDataTable medicamento : medicamentosConsumoSeleccionados) {
                nuevaFila = new FilaDataTable();
                FacConsumoMedicamento consumoMedicamentoBuscado = consumoMedicamentoFacade.find(Integer.parseInt(medicamento.getColumna1()));

                nuevaFila.setColumna1(String.valueOf(listaMedicamentosFactura.size() + 1));
                nuevaFila.setColumna2(formateadorFecha.format(consumoMedicamentoBuscado.getFecha()));
                nuevaFila.setColumna3(consumoMedicamentoBuscado.getIdMedicamento().getNombreMedicamento());
                nuevaFila.setColumna4(consumoMedicamentoBuscado.getIdMedicamento().getFormaMedicamento());
                nuevaFila.setColumna5(String.valueOf(consumoMedicamentoBuscado.getValorUnitario()));
                nuevaFila.setColumna6(String.valueOf(consumoMedicamentoBuscado.getCantidad()));
                nuevaFila.setColumna7(String.valueOf(consumoMedicamentoBuscado.getValorFinal()));
                nuevaFila.setColumna8(consumoMedicamentoBuscado.getIdPrestador().nombreCompleto());

                nuevaFila.setColumna20(pacienteSeleccionado.getIdPaciente().toString());
                nuevaFila.setColumna21(consumoMedicamentoBuscado.getIdPrestador().getIdUsuario().toString());
                nuevaFila.setColumna22(consumoMedicamentoBuscado.getIdMedicamento().getIdMedicamento().toString());

                nuevaFila.setColumna30(consumoMedicamentoBuscado.getIdConsumoMedicamento().toString());//COLUMNA 30 CONTIENE EL IDENTIFICADOR EN TABLA fac_consumo_medicamento
                listaMedicamentosFactura.add(nuevaFila);
            }
            renumerarIdLista(listaMedicamentosFactura);
            listaMedicamentosFacturaFiltro = new ArrayList<>();
            listaMedicamentosFacturaFiltro.addAll(listaMedicamentosFactura);
            tituloTabMedicamentosFactura = "Medicamentos (" + listaMedicamentosFactura.size() + ")";
            recargarFilasTablaConsumoMedicamentos();
            recalcularValorFactura();
        }
    }

    public void seleccionarTodoNingunMedicamentoDeConsumos() {//seleccionarlos todos o ninguno
        if (medicamentosConsumoSeleccionados != null) {
            if (medicamentosConsumoSeleccionados.isEmpty()) {
                medicamentosConsumoSeleccionados = listaMedicamentosConsumo;
            } else {
                medicamentosConsumoSeleccionados = new ArrayList<>();
            }
        } else {
            medicamentosConsumoSeleccionados = new ArrayList<>();
            medicamentosConsumoSeleccionados = listaMedicamentosConsumo;
        }
    }

    public void quitarMedicamentoFactura() {
        if (medicamentoFacturaSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún medicamento", FacesMessage.SEVERITY_ERROR);
            return;
        }
        for (int i = 0; i < listaMedicamentosFactura.size(); i++) {
            if (listaMedicamentosFactura.get(i).getColumna1().compareTo(medicamentoFacturaSeleccionado.getColumna1()) == 0) {
                listaMedicamentosFactura.remove(i);
                break;
            }
        }
        renumerarIdLista(listaMedicamentosFactura);
        listaMedicamentosFacturaFiltro = new ArrayList<>();
        listaMedicamentosFacturaFiltro.addAll(listaMedicamentosFactura);
        medicamentoFacturaSeleccionado = null;
        tituloTabMedicamentosFactura = "Medicamentos (" + listaMedicamentosFactura.size() + ")";
        recargarFilasTablaConsumoMedicamentos();
        recalcularValorFactura();
        RequestContext.getCurrentInstance().execute("PF('wvTablaMedicamentosFactura').clearFilters(); PF('wvTablaMedicamentosFactura').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("IdFormFacturacion");

    }

    //---------------------------------------------------
    //-----------------FUNCIONES INSUMOS ---------------
    //---------------------------------------------------    
    public void cambiaInsumo() {//se limpia el formulario
        idPrestadorInsumo = "";
        fechaInsumo = new Date();
        cantidadInsumo = 1;
        valorUnitarioInsumo = 0;
        valorFinalInsumo = 0;
        calcularValoresInsumo();
    }

    private FacManualTarifarioInsumo buscarEnListaInsumosManual(int idInsumo) {
        //determinar si un Insumo 
        if (listaInsumosManual != null && !listaInsumosManual.isEmpty()) {
            for (FacManualTarifarioInsumo insumoManual : listaInsumosManual) {
                if (insumoManual.getCfgInsumo().getIdInsumo() == idInsumo) {
                    return insumoManual;
                }
            }
        }
        return null;
    }

    public void calcularValoresInsumo() {//calculo del valor final de un insumo
        if (validarNoVacio(idInsumoManual)) {
            FacManualTarifarioInsumo s = buscarEnListaInsumosManual(Integer.parseInt(idInsumoManual));
            valorUnitarioInsumo = s.getValorFinal();
            valorFinalInsumo = valorUnitarioInsumo * cantidadInsumo;
        }
    }

    public void cargarDialogoAgregarInsumo() {//se abre dialogo de registro de un nuevo insumo en la factura
        if (manualTarifarioPaciente == null) {
        }
        listaInsumosManual = manualTarifarioPaciente.getFacManualTarifarioInsumoList();
        if (listaInsumosManual != null && !listaInsumosManual.isEmpty()) {
            idInsumoManual = listaInsumosManual.get(0).getCfgInsumo().getIdInsumo().toString();
        }
        cambiaInsumo();
        RequestContext.getCurrentInstance().update("IdFormFacturacion:IdPanelAgregarInsumo");
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarInsumo').show();");
    }

    public void agregarInsumoAFactura() {//agregar desde la seccion de items de factura
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

        FilaDataTable nuevaFila = new FilaDataTable();

        nuevaFila.setColumna1(String.valueOf(listaInsumosFactura.size() + 1));
        nuevaFila.setColumna2(formateadorFecha.format(fechaInsumo));
        nuevaFila.setColumna3(insumoFacade.find(Integer.parseInt(idInsumoManual)).getNombreInsumo());
        nuevaFila.setColumna4(String.valueOf(valorUnitarioInsumo));
        nuevaFila.setColumna5(String.valueOf(cantidadInsumo));
        nuevaFila.setColumna6(String.valueOf(valorFinalInsumo));
        nuevaFila.setColumna7(usuariosFacade.find(Integer.parseInt(idPrestadorInsumo)).nombreCompleto());

        nuevaFila.setColumna20(pacienteSeleccionado.getIdPaciente().toString());
        nuevaFila.setColumna21(idPrestadorInsumo);
        nuevaFila.setColumna22(idInsumoManual);
        nuevaFila.setColumna30("-1");//COLUMNA 30 CONTIENE EL IDENTIFICADOR EN TABLA fac_consumo_insumo //como fue agreado desde la misma fatura no refiere a la tabla fac_consumo_insumo
        listaInsumosFactura.add(nuevaFila);
        renumerarIdLista(listaInsumosFactura);
        listaInsumosFacturaFiltro = new ArrayList<>();
        listaInsumosFacturaFiltro.addAll(listaInsumosFactura);
        recalcularValorFactura();
        tituloTabInsumosFactura = "Insumos (" + listaInsumosFactura.size() + ")";
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarInsumo').hide(); PF('wvTablaInsumosFactura').clearFilters(); PF('wvTablaInsumosFactura').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("IdFormFacturacion");
    }

    public void agregarInsumosDesdeConsumos() {//agregar desde el dialogo de consumos
        if (insumosConsumoSeleccionados == null || insumosConsumoSeleccionados.isEmpty()) {
            imprimirMensaje("Error", "Ne se ha seleccionado ningún insumo", FacesMessage.SEVERITY_ERROR);
        } else {
            FilaDataTable nuevaFila;
            for (FilaDataTable insumo : insumosConsumoSeleccionados) {
                nuevaFila = new FilaDataTable();
                FacConsumoInsumo consumoInsumoBuscado = consumoInsumoFacade.find(Integer.parseInt(insumo.getColumna1()));

                nuevaFila.setColumna1(String.valueOf(listaInsumosFactura.size() + 1));
                nuevaFila.setColumna2(formateadorFecha.format(consumoInsumoBuscado.getFecha()));
                nuevaFila.setColumna3(consumoInsumoBuscado.getIdInsumo().getNombreInsumo());
                nuevaFila.setColumna4(String.valueOf(consumoInsumoBuscado.getValorUnitario()));
                nuevaFila.setColumna5(String.valueOf(consumoInsumoBuscado.getCantidad()));
                nuevaFila.setColumna6(String.valueOf(consumoInsumoBuscado.getValorFinal()));
                nuevaFila.setColumna7(consumoInsumoBuscado.getIdPrestador().nombreCompleto());

                nuevaFila.setColumna20(pacienteSeleccionado.getIdPaciente().toString());
                nuevaFila.setColumna21(consumoInsumoBuscado.getIdPrestador().getIdUsuario().toString());
                nuevaFila.setColumna22(consumoInsumoBuscado.getIdInsumo().getIdInsumo().toString());

                nuevaFila.setColumna30(consumoInsumoBuscado.getIdConsumoInsumo().toString());//COLUMNA 30 CONTIENE EL IDENTIFICADOR EN TABLA fac_consumo_insumo
                listaInsumosFactura.add(nuevaFila);
            }
            renumerarIdLista(listaInsumosFactura);
            listaInsumosFacturaFiltro = new ArrayList<>();
            listaInsumosFacturaFiltro.addAll(listaInsumosFactura);
            tituloTabInsumosFactura = "Insumos (" + listaInsumosFactura.size() + ")";
            recargarFilasTablaConsumoInsumos();
            recalcularValorFactura();
        }
    }

    public void seleccionarTodoNingunInsumoDeConsumos() {//seleccionarlos todos o ninguno
        if (insumosConsumoSeleccionados != null) {
            if (insumosConsumoSeleccionados.isEmpty()) {
                insumosConsumoSeleccionados = listaInsumosConsumo;
            } else {
                insumosConsumoSeleccionados = new ArrayList<>();
            }
        } else {
            insumosConsumoSeleccionados = new ArrayList<>();
            insumosConsumoSeleccionados = listaInsumosConsumo;
        }
    }

    public void quitarInsumoFactura() {
        if (insumoFacturaSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún insumo", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            for (int i = 0; i < listaInsumosFactura.size(); i++) {
                if (listaInsumosFactura.get(i).getColumna1().compareTo(insumoFacturaSeleccionado.getColumna1()) == 0) {
                    listaInsumosFactura.remove(i);
                    break;
                }
            }
            insumoFacturaSeleccionado = null;
            renumerarIdLista(listaInsumosFactura);
            listaInsumosFacturaFiltro = new ArrayList<>();
            listaInsumosFacturaFiltro.addAll(listaInsumosFactura);
            tituloTabInsumosFactura = "Insumos (" + listaInsumosFactura.size() + ")";
            recargarFilasTablaConsumoInsumos();
            recalcularValorFactura();
            RequestContext.getCurrentInstance().execute("PF('wvTablaInsumosFactura').clearFilters(); PF('wvTablaInsumosFactura').getPaginator().setPage(0);");
            RequestContext.getCurrentInstance().update("IdFormFacturacion");
        } catch (Exception e) {
            imprimirMensaje("Error", "El Insumo que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    //---------------------------------------------------
    //-----------------FUNCIONES PAQUETES ---------------
    //---------------------------------------------------    
    public void cambiaPaquete() {//se limpia el formulario
        idPrestadorPaquete = "";
        fechaPaquete = new Date();
        cantidadPaquete = 1;
        valorUnitarioPaquete = 0;
        valorFinalPaquete = 0;
        calcularValoresPaquete();
    }

    private FacManualTarifarioPaquete buscarEnListaPaquetesManual(int idPaquete) {
        //determinar si un Paquete 
        if (listaPaquetesManual != null && !listaPaquetesManual.isEmpty()) {
            for (FacManualTarifarioPaquete paqueteManual : listaPaquetesManual) {
                if (paqueteManual.getFacPaquete().getIdPaquete() == idPaquete) {
                    return paqueteManual;
                }
            }
        }
        return null;
    }

    public void calcularValoresPaquete() {//calculo del valor final de un paquete
        if (validarNoVacio(idPaqueteManual)) {
            FacManualTarifarioPaquete s = buscarEnListaPaquetesManual(Integer.parseInt(idPaqueteManual));
            valorUnitarioPaquete = s.getValorFinal();
            valorFinalPaquete = valorUnitarioPaquete * cantidadPaquete;
        }
    }

    public void cargarDialogoAgregarPaquete() {//se abre dialogo de registro de un nuevo paquete en la factura
        if (manualTarifarioPaciente == null) {
        }
        listaPaquetesManual = manualTarifarioPaciente.getFacManualTarifarioPaqueteList();
        if (listaPaquetesManual != null && !listaPaquetesManual.isEmpty()) {
            idPaqueteManual = listaPaquetesManual.get(0).getFacPaquete().getIdPaquete().toString();
        }
        cambiaPaquete();
        RequestContext.getCurrentInstance().update("IdFormFacturacion:IdPanelAgregarPaquete");
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarPaquete').show();");
    }

    public void agregarPaqueteAFactura() {//agregar desde la seccion de items de factura
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

        FilaDataTable nuevaFila = new FilaDataTable();

        nuevaFila.setColumna1(String.valueOf(listaPaquetesFactura.size() + 1));
        nuevaFila.setColumna2(formateadorFecha.format(fechaPaquete));
        nuevaFila.setColumna3(paqueteFacade.find(Integer.parseInt(idPaqueteManual)).getNombrePaquete());
        nuevaFila.setColumna5(String.valueOf(valorUnitarioPaquete));
        nuevaFila.setColumna6(String.valueOf(cantidadPaquete));
        nuevaFila.setColumna7(String.valueOf(valorFinalPaquete));
        nuevaFila.setColumna8(usuariosFacade.find(Integer.parseInt(idPrestadorPaquete)).nombreCompleto());

        nuevaFila.setColumna20(pacienteSeleccionado.getIdPaciente().toString());
        nuevaFila.setColumna21(idPrestadorPaquete);
        nuevaFila.setColumna22(idPaqueteManual);
        nuevaFila.setColumna30("-1");//COLUMNA 30 CONTIENE EL IDENTIFICADOR EN TABLA fac_consumo_paquete //como fue agreado desde la misma fatura no refiere a la tabla fac_consumo_paquete
        listaPaquetesFactura.add(nuevaFila);
        renumerarIdLista(listaPaquetesFactura);
        listaPaquetesFacturaFiltro = new ArrayList<>();
        listaPaquetesFacturaFiltro.addAll(listaPaquetesFactura);
        recalcularValorFactura();
        tituloTabPaquetesFactura = "Paquetes (" + listaPaquetesFactura.size() + ")";
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarPaquete').hide(); PF('wvTablaPaquetesFactura').clearFilters(); PF('wvTablaPaquetesFactura').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("IdFormFacturacion");
    }

    public void agregarPaquetesDesdeConsumos() {//agregar desde el dialogo de consumos
        if (paquetesConsumoSeleccionados == null || paquetesConsumoSeleccionados.isEmpty()) {
            imprimirMensaje("Error", "Ne se ha seleccionado ningún paquete", FacesMessage.SEVERITY_ERROR);
        } else {
            FilaDataTable nuevaFila;
            for (FilaDataTable paquete : paquetesConsumoSeleccionados) {
                nuevaFila = new FilaDataTable();
                FacConsumoPaquete consumoPaqueteBuscado = consumoPaqueteFacade.find(Integer.parseInt(paquete.getColumna1()));

                nuevaFila.setColumna1(String.valueOf(listaPaquetesFactura.size() + 1));
                nuevaFila.setColumna2(formateadorFecha.format(consumoPaqueteBuscado.getFecha()));
                nuevaFila.setColumna3(consumoPaqueteBuscado.getIdPaquete().getNombrePaquete());
                nuevaFila.setColumna5(String.valueOf(consumoPaqueteBuscado.getValorUnitario()));
                nuevaFila.setColumna6(String.valueOf(consumoPaqueteBuscado.getCantidad()));
                nuevaFila.setColumna7(String.valueOf(consumoPaqueteBuscado.getValorFinal()));
                nuevaFila.setColumna8(consumoPaqueteBuscado.getIdPrestador().nombreCompleto());

                nuevaFila.setColumna20(pacienteSeleccionado.getIdPaciente().toString());
                nuevaFila.setColumna21(consumoPaqueteBuscado.getIdPrestador().getIdUsuario().toString());
                nuevaFila.setColumna22(consumoPaqueteBuscado.getIdPaquete().getIdPaquete().toString());

                nuevaFila.setColumna30(consumoPaqueteBuscado.getIdConsumoPaquete().toString());//COLUMNA 30 CONTIENE EL IDENTIFICADOR EN TABLA fac_consumo_paquete
                listaPaquetesFactura.add(nuevaFila);
            }
            renumerarIdLista(listaPaquetesFactura);
            listaPaquetesFacturaFiltro = new ArrayList<>();
            listaPaquetesFacturaFiltro.addAll(listaPaquetesFactura);
            tituloTabPaquetesFactura = "Paquetes (" + listaPaquetesFactura.size() + ")";
            recargarFilasTablaConsumoPaquetes();
            recalcularValorFactura();
        }
    }

    public void seleccionarTodoNingunPaqueteDeConsumos() {//seleccionarlos todos o ninguno
        if (paquetesConsumoSeleccionados != null) {
            if (paquetesConsumoSeleccionados.isEmpty()) {
                paquetesConsumoSeleccionados = listaPaquetesConsumo;
            } else {
                paquetesConsumoSeleccionados = new ArrayList<>();
            }
        } else {
            paquetesConsumoSeleccionados = new ArrayList<>();
            paquetesConsumoSeleccionados = listaPaquetesConsumo;
        }
    }

    public void quitarPaqueteFactura() {
        if (paqueteFacturaSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún paquete", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            for (int i = 0; i < listaPaquetesFactura.size(); i++) {
                if (listaPaquetesFactura.get(i).getColumna1().compareTo(paqueteFacturaSeleccionado.getColumna1()) == 0) {
                    listaPaquetesFactura.remove(i);
                    break;
                }
            }
            renumerarIdLista(listaPaquetesFactura);
            listaPaquetesFacturaFiltro = new ArrayList<>();
            listaPaquetesFacturaFiltro.addAll(listaPaquetesFactura);
            paqueteFacturaSeleccionado = null;
            tituloTabPaquetesFactura = "Paquetes (" + listaPaquetesFactura.size() + ")";
            recargarFilasTablaConsumoPaquetes();
            recalcularValorFactura();
            RequestContext.getCurrentInstance().execute("PF('wvTablaPaquetesFactura').clearFilters(); PF('wvTablaPaquetesFactura').getPaginator().setPage(0);");
            RequestContext.getCurrentInstance().update("IdFormFacturacion");
        } catch (Exception e) {
            imprimirMensaje("Error", "El Paquete que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------
    public CfgPacientes getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }

    public void setPacienteSeleccionado(CfgPacientes pacienteSeleccionado) {
        this.pacienteSeleccionado = pacienteSeleccionado;
    }

    public CfgPacientes getPacienteSeleccionadoTabla() {
        return pacienteSeleccionadoTabla;
    }

    public void setPacienteSeleccionadoTabla(CfgPacientes pacienteSeleccionadoTabla) {
        this.pacienteSeleccionadoTabla = pacienteSeleccionadoTabla;
    }

    public FilaDataTable getFacturaSeleccionadaTabla() {
        return facturaSeleccionadaTabla;
    }

    public void setFacturaSeleccionadaTabla(FilaDataTable facturaSeleccionadaTabla) {
        this.facturaSeleccionadaTabla = facturaSeleccionadaTabla;
    }

    public LazyDataModel<FilaDataTable> getListaFacturas() {
        return listaFacturas;
    }

    public void setListaFacturas(LazyDataModel<FilaDataTable> listaFacturas) {
        this.listaFacturas = listaFacturas;
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

    public List<FilaDataTable> getServiciosConsumoSeleccionados() {
        return serviciosConsumoSeleccionados;
    }

    public void setServiciosConsumoSeleccionados(List<FilaDataTable> serviciosConsumoSeleccionados) {
        this.serviciosConsumoSeleccionados = serviciosConsumoSeleccionados;
    }

    public List<FilaDataTable> getInsumosConsumoSeleccionados() {
        return insumosConsumoSeleccionados;
    }

    public void setInsumosConsumoSeleccionados(List<FilaDataTable> insumosConsumoSeleccionados) {
        this.insumosConsumoSeleccionados = insumosConsumoSeleccionados;
    }

    public List<FilaDataTable> getMedicamentosConsumoSeleccionados() {
        return medicamentosConsumoSeleccionados;
    }

    public void setMedicamentosConsumoSeleccionados(List<FilaDataTable> medicamentosConsumoSeleccionados) {
        this.medicamentosConsumoSeleccionados = medicamentosConsumoSeleccionados;
    }

    public List<FilaDataTable> getPaquetesConsumoSeleccionados() {
        return paquetesConsumoSeleccionados;
    }

    public void setPaquetesConsumoSeleccionados(List<FilaDataTable> paquetesConsumoSeleccionados) {
        this.paquetesConsumoSeleccionados = paquetesConsumoSeleccionados;
    }

    public List<FilaDataTable> getListaServiciosFactura() {
        return listaServiciosFactura;
    }

    public void setListaServiciosFactura(List<FilaDataTable> listaServiciosFactura) {
        this.listaServiciosFactura = listaServiciosFactura;
    }

    public List<FilaDataTable> getListaInsumosFactura() {
        return listaInsumosFactura;
    }

    public void setListaInsumosFactura(List<FilaDataTable> listaInsumosFactura) {
        this.listaInsumosFactura = listaInsumosFactura;
    }

    public List<FilaDataTable> getListaMedicamentosFactura() {
        return listaMedicamentosFactura;
    }

    public void setListaMedicamentosFactura(List<FilaDataTable> listaMedicamentosFactura) {
        this.listaMedicamentosFactura = listaMedicamentosFactura;
    }

    public List<FilaDataTable> getListaPaquetesFactura() {
        return listaPaquetesFactura;
    }

    public void setListaPaquetesFactura(List<FilaDataTable> listaPaquetesFactura) {
        this.listaPaquetesFactura = listaPaquetesFactura;
    }

    public List<FilaDataTable> getListaServiciosFacturaFiltro() {
        return listaServiciosFacturaFiltro;
    }

    public void setListaServiciosFacturaFiltro(List<FilaDataTable> listaServiciosFacturaFiltro) {
        this.listaServiciosFacturaFiltro = listaServiciosFacturaFiltro;
    }

    public List<FilaDataTable> getListaInsumosFacturaFiltro() {
        return listaInsumosFacturaFiltro;
    }

    public void setListaInsumosFacturaFiltro(List<FilaDataTable> listaInsumosFacturaFiltro) {
        this.listaInsumosFacturaFiltro = listaInsumosFacturaFiltro;
    }

    public List<FilaDataTable> getListaMedicamentosFacturaFiltro() {
        return listaMedicamentosFacturaFiltro;
    }

    public void setListaMedicamentosFacturaFiltro(List<FilaDataTable> listaMedicamentosFacturaFiltro) {
        this.listaMedicamentosFacturaFiltro = listaMedicamentosFacturaFiltro;
    }

    public List<FilaDataTable> getListaPaquetesFacturaFiltro() {
        return listaPaquetesFacturaFiltro;
    }

    public void setListaPaquetesFacturaFiltro(List<FilaDataTable> listaPaquetesFacturaFiltro) {
        this.listaPaquetesFacturaFiltro = listaPaquetesFacturaFiltro;
    }

    public FilaDataTable getServicioFacturaSeleccionado() {
        return servicioFacturaSeleccionado;
    }

    public void setServicioFacturaSeleccionado(FilaDataTable servicioFacturaSeleccionado) {
        this.servicioFacturaSeleccionado = servicioFacturaSeleccionado;
    }

    public FilaDataTable getInsumoFacturaSeleccionado() {
        return insumoFacturaSeleccionado;
    }

    public void setInsumoFacturaSeleccionado(FilaDataTable insumoFacturaSeleccionado) {
        this.insumoFacturaSeleccionado = insumoFacturaSeleccionado;
    }

    public FilaDataTable getMedicamentoFacturaSeleccionado() {
        return medicamentoFacturaSeleccionado;
    }

    public void setMedicamentoFacturaSeleccionado(FilaDataTable medicamentoFacturaSeleccionado) {
        this.medicamentoFacturaSeleccionado = medicamentoFacturaSeleccionado;
    }

    public FilaDataTable getPaqueteFacturaSeleccionado() {
        return paqueteFacturaSeleccionado;
    }

    public void setPaqueteFacturaSeleccionado(FilaDataTable paqueteFacturaSeleccionado) {
        this.paqueteFacturaSeleccionado = paqueteFacturaSeleccionado;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public String getMsjHtmlCaja() {
        return msjHtmlCaja;
    }

    public void setMsjHtmlCaja(String msjHtmlCaja) {
        this.msjHtmlCaja = msjHtmlCaja;
    }

    public String getMsjHtmlPeriodo() {
        return msjHtmlPeriodo;
    }

    public void setMsjHtmlPeriodo(String msjHtmlPeriodo) {
        this.msjHtmlPeriodo = msjHtmlPeriodo;
    }

    public String getMsjHtmlDocumento() {
        return msjHtmlDocumento;
    }

    public void setMsjHtmlDocumento(String msjHtmlDocumento) {
        this.msjHtmlDocumento = msjHtmlDocumento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(String idFactura) {
        this.idFactura = idFactura;
    }

    public String getIdSede() {
        return idSede;
    }

    public void setIdSede(String idSede) {
        this.idSede = idSede;
    }

    public String getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(String consecutivo) {
        this.consecutivo = consecutivo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

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

    public FacManualTarifario getManualTarifarioPaciente() {
        return manualTarifarioPaciente;
    }

    public void setManualTarifarioPaciente(FacManualTarifario manualTarifarioPaciente) {
        this.manualTarifarioPaciente = manualTarifarioPaciente;
    }

    public String getMensajeConfiguracion() {
        return mensajeConfiguracion;
    }

    public void setMensajeConfiguracion(String mensajeConfiguracion) {
        this.mensajeConfiguracion = mensajeConfiguracion;
    }

    public String getTituloTabServiciosConsumo() {
        return tituloTabServiciosConsumo;
    }

    public void setTituloTabServiciosConsumo(String tituloTabServiciosConsumo) {
        this.tituloTabServiciosConsumo = tituloTabServiciosConsumo;
    }

    public String getTituloTabInsumosConsumo() {
        return tituloTabInsumosConsumo;
    }

    public void setTituloTabInsumosConsumo(String tituloTabInsumosConsumo) {
        this.tituloTabInsumosConsumo = tituloTabInsumosConsumo;
    }

    public String getTituloTabMedicamentosConsumo() {
        return tituloTabMedicamentosConsumo;
    }

    public void setTituloTabMedicamentosConsumo(String tituloTabMedicamentosConsumo) {
        this.tituloTabMedicamentosConsumo = tituloTabMedicamentosConsumo;
    }

    public String getTituloTabPaquetesConsumo() {
        return tituloTabPaquetesConsumo;
    }

    public void setTituloTabPaquetesConsumo(String tituloTabPaquetesConsumo) {
        this.tituloTabPaquetesConsumo = tituloTabPaquetesConsumo;
    }

    public String getTituloTabServiciosFactura() {
        return tituloTabServiciosFactura;
    }

    public void setTituloTabServiciosFactura(String tituloTabServiciosFactura) {
        this.tituloTabServiciosFactura = tituloTabServiciosFactura;
    }

    public String getTituloTabInsumosFactura() {
        return tituloTabInsumosFactura;
    }

    public void setTituloTabInsumosFactura(String tituloTabInsumosFactura) {
        this.tituloTabInsumosFactura = tituloTabInsumosFactura;
    }

    public String getTituloTabMedicamentosFactura() {
        return tituloTabMedicamentosFactura;
    }

    public void setTituloTabMedicamentosFactura(String tituloTabMedicamentosFactura) {
        this.tituloTabMedicamentosFactura = tituloTabMedicamentosFactura;
    }

    public String getTituloTabPaquetesFactura() {
        return tituloTabPaquetesFactura;
    }

    public void setTituloTabPaquetesFactura(String tituloTabPaquetesFactura) {
        this.tituloTabPaquetesFactura = tituloTabPaquetesFactura;
    }

    public String getIdPrestadorServicio() {
        return idPrestadorServicio;
    }

    public void setIdPrestadorServicio(String idPrestadorServicio) {
        this.idPrestadorServicio = idPrestadorServicio;
    }

    public String getIdServicioManual() {
        return idServicioManual;
    }

    public void setIdServicioManual(String idServicioManual) {
        this.idServicioManual = idServicioManual;
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

    public String getIdPrestadorInsumo() {
        return idPrestadorInsumo;
    }

    public void setIdPrestadorInsumo(String idPrestadorInsumo) {
        this.idPrestadorInsumo = idPrestadorInsumo;
    }

    public String getIdInsumoManual() {
        return idInsumoManual;
    }

    public void setIdInsumoManual(String idInsumoManual) {
        this.idInsumoManual = idInsumoManual;
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

    public String getIdPrestadorMedicamento() {
        return idPrestadorMedicamento;
    }

    public void setIdPrestadorMedicamento(String idPrestadorMedicamento) {
        this.idPrestadorMedicamento = idPrestadorMedicamento;
    }

    public String getIdMedicamentoManual() {
        return idMedicamentoManual;
    }

    public void setIdMedicamentoManual(String idMedicamentoManual) {
        this.idMedicamentoManual = idMedicamentoManual;
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

    public String getIdPrestadorPaquete() {
        return idPrestadorPaquete;
    }

    public void setIdPrestadorPaquete(String idPrestadorPaquete) {
        this.idPrestadorPaquete = idPrestadorPaquete;
    }

    public String getIdPaqueteManual() {
        return idPaqueteManual;
    }

    public void setIdPaqueteManual(String idPaqueteManual) {
        this.idPaqueteManual = idPaqueteManual;
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

    public LoginMB getLoginMB() {
        return loginMB;
    }

    public void setLoginMB(LoginMB loginMB) {
        this.loginMB = loginMB;
    }

    public List<FacCaja> getListaCajas() {
        return listaCajas;
    }

    public void setListaCajas(List<FacCaja> listaCajas) {
        this.listaCajas = listaCajas;
    }

    public String getMsjHtmlIva() {
        return msjHtmlIva;
    }

    public void setMsjHtmlIva(String msjHtmlIva) {
        this.msjHtmlIva = msjHtmlIva;
    }

    public String getMsjHtmlCree() {
        return msjHtmlCree;
    }

    public void setMsjHtmlCree(String msjHtmlCree) {
        this.msjHtmlCree = msjHtmlCree;
    }

    public List<CfgClasificaciones> getListaDocumentos() {
        return listaDocumentos;
    }

    public void setListaDocumentos(List<CfgClasificaciones> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }

    public String getEstiloCaja() {
        return estiloCaja;
    }

    public void setEstiloCaja(String estiloCaja) {
        this.estiloCaja = estiloCaja;
    }

    public String getEstiloPeriodo() {
        return estiloPeriodo;
    }

    public void setEstiloPeriodo(String estiloPeriodo) {
        this.estiloPeriodo = estiloPeriodo;
    }

    public String getEstiloDocumento() {
        return estiloDocumento;
    }

    public void setEstiloDocumento(String estiloDocumento) {
        this.estiloDocumento = estiloDocumento;
    }

    public String getEstiloIva() {
        return estiloIva;
    }

    public void setEstiloIva(String estiloIva) {
        this.estiloIva = estiloIva;
    }

    public String getEstiloCree() {
        return estiloCree;
    }

    public void setEstiloCree(String estiloCree) {
        this.estiloCree = estiloCree;
    }

    public double getValorIva() {
        return valorIva;
    }

    public void setValorIva(double valorIva) {
        this.valorIva = valorIva;
    }

    public double getValorCree() {
        return valorCree;
    }

    public void setValorCree(double valorCree) {
        this.valorCree = valorCree;
    }

    public String getValorIvaStr() {
        return valorIvaStr;
    }

    public void setValorIvaStr(String valorIvaStr) {
        this.valorIvaStr = valorIvaStr;
    }

    public String getValorCreeStr() {
        return valorCreeStr;
    }

    public void setValorCreeStr(String valorCreeStr) {
        this.valorCreeStr = valorCreeStr;
    }

    public double getValorEmpresa() {
        return valorEmpresa;
    }

    public void setValorEmpresa(double valorEmpresa) {
        this.valorEmpresa = valorEmpresa;
    }

    public double getValorParcial() {
        return valorParcial;
    }

    public void setValorParcial(double valorParcial) {
        this.valorParcial = valorParcial;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public double getValorCopago() {
        return valorCopago;
    }

    public void setValorCopago(double valorCopago) {
        this.valorCopago = valorCopago;
    }

    public double getValorCuotaModeradora() {
        return valorCuotaModeradora;
    }

    public void setValorCuotaModeradora(double valorCuotaModeradora) {
        this.valorCuotaModeradora = valorCuotaModeradora;
    }

    public boolean isDisableControlesBuscarFactura() {
        return disableControlesBuscarFactura;
    }

    public void setDisableControlesBuscarFactura(boolean disableControlesBuscarFactura) {
        this.disableControlesBuscarFactura = disableControlesBuscarFactura;
    }

    public boolean isFacturarComoParticular() {
        return facturarComoParticular;
    }

    public void setFacturarComoParticular(boolean facturarComoParticular) {
        this.facturarComoParticular = facturarComoParticular;
    }

    public boolean isFacturarComoParticularDisabled() {
        return facturarComoParticularDisabled;
    }

    public void setFacturarComoParticularDisabled(boolean facturarComoParticularDisabled) {
        this.facturarComoParticularDisabled = facturarComoParticularDisabled;
    }

    public String getMsjBtnFacturarParticular() {
        return msjBtnFacturarParticular;
    }

    public void setMsjBtnFacturarParticular(String msjBtnFacturarParticular) {
        this.msjBtnFacturarParticular = msjBtnFacturarParticular;
    }

    public String getObservacionAnulacion() {
        return observacionAnulacion;
    }

    public void setObservacionAnulacion(String observacionAnulacion) {
        this.observacionAnulacion = observacionAnulacion;
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

    public double getValorUsuario() {
        return valorUsuario;
    }

    public void setValorUsuario(double valorUsuario) {
        this.valorUsuario = valorUsuario;
    }

    public String getTurnoCita() {
        return turnoCita;
    }

    public void setTurnoCita(String turnoCita) {
        this.turnoCita = turnoCita;
    }

    public String getMsjHtmlCopago() {
        return msjHtmlCopago;
    }

    public void setMsjHtmlCopago(String msjHtmlCopago) {
        this.msjHtmlCopago = msjHtmlCopago;
    }

    public String getMsjHtmlCuotaModeradora() {
        return msjHtmlCuotaModeradora;
    }

    public void setMsjHtmlCuotaModeradora(String msjHtmlCuotaModeradora) {
        this.msjHtmlCuotaModeradora = msjHtmlCuotaModeradora;
    }

    public String getMsjHtmlAdministradoraPaciente() {
        return msjHtmlAdministradoraPaciente;
    }

    public void setMsjHtmlAdministradoraPaciente(String msjHtmlAdministradoraPaciente) {
        this.msjHtmlAdministradoraPaciente = msjHtmlAdministradoraPaciente;
    }

    public String getEstiloAdministradoraPaciente() {
        return estiloAdministradoraPaciente;
    }

    public void setEstiloAdministradoraPaciente(String estiloAdministradoraPaciente) {
        this.estiloAdministradoraPaciente = estiloAdministradoraPaciente;
    }

    public String getMsjHtmlBtnConsumos() {
        return msjHtmlBtnConsumos;
    }

    public void setMsjHtmlBtnConsumos(String msjHtmlBtnConsumos) {
        this.msjHtmlBtnConsumos = msjHtmlBtnConsumos;
    }

    public String getEstiloBtnConsumos() {
        return estiloBtnConsumos;
    }

    public void setEstiloBtnConsumos(String estiloBtnConsumos) {
        this.estiloBtnConsumos = estiloBtnConsumos;
    }

    public String getMsjHtmlTurnoCita() {
        return msjHtmlTurnoCita;
    }

    public void setMsjHtmlTurnoCita(String msjHtmlTurnoCita) {
        this.msjHtmlTurnoCita = msjHtmlTurnoCita;
    }

    public String getEstiloTurnoCita() {
        return estiloTurnoCita;
    }

    public void setEstiloTurnoCita(String estiloTurnoCita) {
        this.estiloTurnoCita = estiloTurnoCita;
    }

    public List<FacContrato> getListaContratosAplican() {
        return listaContratosAplican;
    }

    public void setListaContratosAplican(List<FacContrato> listaContratosAplican) {
        this.listaContratosAplican = listaContratosAplican;
    }

    public String getIdContratoActual() {
        return idContratoActual;
    }

    public void setIdContratoActual(String idContratoActual) {
        this.idContratoActual = idContratoActual;
    }

}
