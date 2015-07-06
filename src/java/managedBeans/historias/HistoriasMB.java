package managedBeans.historias;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import beans.utilidades.MetodosGenerales;
import beans.utilidades.NodoArbolHistorial;
import beans.utilidades.TipoNodoEnum;
import beans.utilidades.LazyPacienteDataModel;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import managedBeans.facturacion.ConsecutivosMB;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.CfgDiagnostico;
import modelo.entidades.CfgEmpresa;
import modelo.entidades.CfgMaestrosTxtPredefinidos;
import modelo.entidades.CfgPacientes;
import modelo.entidades.CfgTxtPredefinidos;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.CitCitas;
import modelo.entidades.FacConsecutivo;
import modelo.entidades.FacServicio;
import modelo.entidades.HcCamposReg;
import modelo.entidades.HcDetalle;
import modelo.entidades.HcRegistro;
import modelo.entidades.HcTipoReg;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgDiagnosticoFacade;
import modelo.fachadas.CfgEmpresaFacade;
import modelo.fachadas.CfgMaestrosTxtPredefinidosFacade;
import modelo.fachadas.CfgPacientesFacade;
import modelo.fachadas.CfgTxtPredefinidosFacade;
import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.CitCitasFacade;
import modelo.fachadas.CitTurnosFacade;
import modelo.fachadas.FacConsecutivoFacade;
import modelo.fachadas.FacServicioFacade;
import modelo.fachadas.HcCamposRegFacade;
import modelo.fachadas.HcRegistroFacade;
import modelo.fachadas.HcTipoRegFacade;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.TreeNode;

@ManagedBean(name = "historiasMB")
@SessionScoped
public class HistoriasMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    HcRegistroFacade registroFacade;
    @EJB
    CitTurnosFacade turnosFacade;
    @EJB
    CitCitasFacade citasFacade;
    @EJB
    HcTipoRegFacade tipoRegCliFacade;
    @EJB
    HcCamposRegFacade camposRegFacade;
    @EJB
    CfgPacientesFacade pacientesFacade;
    @EJB
    CfgMaestrosTxtPredefinidosFacade maestrosTxtPredefinidosFacade;
    @EJB
    CfgTxtPredefinidosFacade txtPredefinidosFacade;
    @EJB
    CfgDiagnosticoFacade diagnosticoFacade;
    @EJB
    CfgUsuariosFacade usuariosFacade;
    @EJB
    CfgClasificacionesFacade clasificacionesFacade;
    @EJB
    FacServicioFacade servicioFacade;
    @EJB
    CfgEmpresaFacade empresaFacade;

    //---------------------------------------------------
    //-----------------ENTIDADES ------------------------
    //---------------------------------------------------
    private HcTipoReg tipoRegistroClinicoActual;
    private LazyDataModel<CfgPacientes> listaPacientes;
    //private List<CfgPacientes> listaPacientes;
    private List<CfgPacientes> listaPacientesFiltro;
    private CfgPacientes pacienteSeleccionadoTabla;
    private CfgPacientes pacienteSeleccionado;
    private CfgPacientes pacienteTmp;
    private List<CfgMaestrosTxtPredefinidos> listaMaestrosTxtPredefinidos;
    private List<CfgTxtPredefinidos> listaTxtPredefinidos;
    private List<CfgUsuarios> listaPrestadores;
    private CfgTxtPredefinidos txtPredefinidoActual = null;
    private CfgClasificaciones clasificacionBuscada;
    private CfgDiagnostico diagnosticoBuscado;
    private FacServicio servicioBuscado;
    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------        

    private String filtroAutorizacion = "";
    private Date filtroFechaInicial = null;//new Date();
    private Date filtroFechaFinal = null;//new Date();

    private String detalleTextoPredef = "";
    private String idTextoPredef = "";
    private String idMaestroTextoPredef = "";
    private String nombreTextoPredefinido = "";
    private String urlFoto = "../recursos/img/img_usuario.png";
    private String tipoRegistroClinico = "";//tipo de registro clinico usado para cargar el fomulario correspondiente
    private String[] regCliSelHistorial;//registros clinicos seleccionados para el historial
    private String urlPagina = "";
    private String nombreTipoRegistroClinico = "";
    private String identificacionPaciente = "";
    private String tipoIdentificacion = "";
    private String nombrePaciente = "Paciente";
    private String generoPaciente = "";
    private String edadPaciente = "";
    private String administradoraPaciente = "";
    private String idEditorSeleccionado = "";//determinar cual de los editores de texto se les asignara un texto predefinido
    private int posListaTxtPredef = 0;//determinar la posicion en la estructura 'estructuraCampos' al usar un texto predefinido
    private String idPrestador = "";
    private String especialidadPrestador = "";
    private DatosFormularioHistoria datosFormulario = new DatosFormularioHistoria();//valores de cada uno de los campos de cualquier registro clinico
    private Date fechaReg;
    private Date fechaSis;
    private TreeNode treeNodeRaiz;//nodos raiz del historico
    //private TreeNode treeNodeSeleccionado;//nodo seleccionado del historico
    private TreeNode[] treeNodesSeleccionados;
    private List<HcTipoReg> listaTipoRegistroClinico;
    private boolean hayPacienteSeleccionado = false;//se ha seleccionado un paciente
    private boolean modificandoRegCli = false;//se esta modificando un registro clinco existente
    private boolean btnHistorialDisabled = true;
    private boolean btnPdfAgrupadoDisabled = true;
    private HcRegistro registroEncontrado = null;//variable que almacena el registro clinico cargado desde el historial
    private List<DatosFormularioHistoria> listaRegistrosParaPdf;// maneja como lista por si fueran varias historias al tiempo    

    //private final SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm:ss");
    private CfgEmpresa empresa;
    private LoginMB loginMB;
    private boolean btnEditarRendered = false;

    private String turnoCita = "";//identificador del turno de la cita
    private boolean cargandoDesdeTab = false;
    private CitCitas citaActual = null;

    //private String comoBolean = "TRUE";
    private List<SelectItem> listaMunicipios;
    private String departamento = "";
    private String municipio = "";

    private final SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm");
    private final SimpleDateFormat sdfDateHour = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final SimpleDateFormat sdfFechaString = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);//Thu Apr 30 19:00:00 COT 2015
    private final DecimalFormat formateadorDecimal = new DecimalFormat("#.##");

    //---------------------------------------------------
    //----------------- FUNCIONES INICIALES -----------------------
    //---------------------------------------------------
    public HistoriasMB() {
    }

    public void cargarMunicipios() {
        listaMunicipios = new ArrayList<>();
        if (datosFormulario != null) {
            departamento = datosFormulario.getDato1().toString();
            if (departamento != null && departamento.length() != 0 && esNumero(departamento)) {
                List<CfgClasificaciones> listaM = clasificacionesFacade.buscarMunicipioPorDepartamento(clasificacionesFacade.find(Integer.parseInt(departamento)).getCodigo());
                for (CfgClasificaciones mun : listaM) {
                    listaMunicipios.add(new SelectItem(mun.getId(), mun.getDescripcion()));
                }
            }
        }
    }

    @PostConstruct
    public void inicializar() {
        recargarMaestrosTxtPredefinidos();
        listaPacientes = new LazyPacienteDataModel(pacientesFacade);
        //listaPacientes = pacientesFacade.buscarOrdenado();
        //listaPacientesFiltro = new ArrayList<>();
        //listaPacientesFiltro.addAll(listaPacientes);
        listaTipoRegistroClinico = tipoRegCliFacade.buscarTiposRegstroActivos();
        listaPrestadores = usuariosFacade.buscarUsuariosParaHistorias();
        seleccionaTodosRegCliHis();
        empresa = empresaFacade.findAll().get(0);
    }

    private void valoresPorDefecto() {
        //Asignacion de valores por defecto cuando se muestra un formulario
        if (tipoRegistroClinicoActual != null) {//validacion particular para asignar valores por defecto             
            if (tipoRegistroClinicoActual.getIdTipoReg() == 8) {//SOLICITUD DE AUTORIZACION DE SERVICIOS
                datosFormulario.setDato4("Prioritaria");//prioridad de la atencion                
                datosFormulario.setDato0(pacienteSeleccionado.getIdAdministradora().getRazonSocial());//pagador 
                datosFormulario.setDato1(pacienteSeleccionado.getIdAdministradora().getCodigoAdministradora());//codigo
            }
//            if (tipoRegistroClinicoActual.getIdTipoReg() == 2) {
//                datosFormulario.setDato1("475");//en este combo quede seleccionado ENFERMEDAD GENERAL                
//            }
        }
    }

    public void cargarDesdeTab(String id) {//cargar paciente y desde Tab agenda prestador                
        cargandoDesdeTab = true;
        turnoCita = "";
        citaActual = null;
        limpiarFormulario();
        String[] splitId = id.split(";");
        if (splitId[0].compareTo("idCita") == 0) {
            citaActual = citasFacade.find(Integer.parseInt(splitId[1]));
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
            cargarPaciente();
            turnoCita = citaActual.getIdTurno().getIdTurno().toString();
        }
        RequestContext.getCurrentInstance().update("IdMensajeFacturacion");
        cargandoDesdeTab = false;
    }

    //---------------------------------------------------
    //----------------- HISTORICO -----------------------
    //---------------------------------------------------
    private void seleccionaTodosRegCliHis() {//seleccionar todos los tipos de registros clinicos para el historial
        regCliSelHistorial = new String[listaTipoRegistroClinico.size()];
        for (int i = 0; i < listaTipoRegistroClinico.size(); i++) {
            regCliSelHistorial[i] = listaTipoRegistroClinico.get(i).getIdTipoReg().toString();
        }
    }

    public void cargarHistorialCompleto() {
        btnHistorialDisabled = true;
        btnPdfAgrupadoDisabled = true;
        seleccionaTodosRegCliHis();//selecciona todos los tipos de registros clinicos
        cargarHistorial();//realiza la creacion del arbol historial
    }

    public void cargarHistorial() {
        //genera un arbol con los registros clinicos de un paciente(depende de los seleccionados en 'IdComboTipReg')

        treeNodeRaiz = new DefaultTreeNode(new NodoArbolHistorial(TipoNodoEnum.RAIZ_HISTORIAL, null, -1, -1, null, null), null);
        NodoArbolHistorial nodoSeleccionTodoNada = new NodoArbolHistorial(TipoNodoEnum.NOVALUE, null, -1, -1, null, "Selección Todo/Ninguno");

        TreeNode nodoInicial = new DefaultTreeNode(nodoSeleccionTodoNada, treeNodeRaiz);
        nodoInicial.setExpanded(true);

        treeNodesSeleccionados = null;
        btnPdfAgrupadoDisabled = true;
        btnHistorialDisabled = true;
        boolean exitenRegistros = false;
        boolean usarFiltroFechas = false;
        boolean usarFiltroAutorizacion = false;

        if (pacienteSeleccionado == null) {
            imprimirMensaje("Error", "No hay un paciente seleccionado", FacesMessage.SEVERITY_ERROR);
            RequestContext.getCurrentInstance().update("IdFormHistorias:IdPanelHistorico");
            return;
        }
        if ((filtroFechaInicial == null && filtroFechaFinal != null) || (filtroFechaInicial != null && filtroFechaFinal == null)) {
            imprimirMensaje("Error", "Debe especificar fecha inicial y fecha final al tiempo ", FacesMessage.SEVERITY_ERROR);
            RequestContext.getCurrentInstance().update("IdFormHistorias:IdPanelHistorico");
            return;
        }
        if (filtroFechaInicial != null && filtroFechaFinal != null) {
            usarFiltroFechas = true;
        }

        if (validarNoVacio(filtroAutorizacion)) {
            usarFiltroAutorizacion = true;
        }
        List<HcRegistro> listaRegistrosPaciente;
        if (usarFiltroFechas && usarFiltroAutorizacion) {//usar dos filtros
            listaRegistrosPaciente = registroFacade.buscarFiltradoPorNumeroAutorizacionYFecha(pacienteSeleccionado.getIdPaciente(), filtroFechaInicial, filtroFechaFinal, filtroAutorizacion);
        } else if (usarFiltroFechas) {// usar filtro de fecha
            listaRegistrosPaciente = registroFacade.buscarOrdenado(pacienteSeleccionado.getIdPaciente(), filtroFechaInicial, filtroFechaFinal);
        } else if (usarFiltroAutorizacion) {//usar filtro de autorizacion
            listaRegistrosPaciente = registroFacade.buscarFiltradoPorNumeroAutorizacion(pacienteSeleccionado.getIdPaciente(), filtroAutorizacion);
        } else {//no usar filtro, se busca todos
            listaRegistrosPaciente = registroFacade.buscarOrdenadoPorFecha(pacienteSeleccionado.getIdPaciente());// pacienteSeleccionado.getHcRegistroList();//buscar por orden de fecha decendente            
        }

        NodoArbolHistorial nodHisNuevo;//esta dentro de un TreeNode
        NodoArbolHistorial nodHisFecha;//
        NodoArbolHistorial nodHisComparar;//
        TreeNode treeNodeFecha = null;//son realmente nodos del arbol(contiene a un NodoArbolIstoria)
        TreeNode treeNodeCreado;
        for (HcRegistro registro : listaRegistrosPaciente) {
            if (estaEnListaTipRegHis(registro.getIdTipoReg().getIdTipoReg().toString())) {//verificar que este en la lista de tipos de registro que se desea listar
                exitenRegistros = true;
                nodHisNuevo = new NodoArbolHistorial(
                        TipoNodoEnum.REGISTRO_HISTORIAL,
                        registro.getFechaReg(),
                        registro.getIdRegistro(),
                        registro.getIdTipoReg().getIdTipoReg(),
                        registro.getIdTipoReg().getNombre(),
                        registro.getIdMedico().nombreCompleto());
                if (treeNodeFecha == null) {//nose han agregado nodos
                    nodHisFecha = new NodoArbolHistorial(TipoNodoEnum.FECHA_HISTORIAL, registro.getFechaReg(), -1, -1, null, null);
                    treeNodeFecha = new DefaultTreeNode(nodHisFecha, nodoInicial);
                    treeNodeFecha.setExpanded(true);
                    treeNodeCreado = new DefaultTreeNode(nodHisNuevo, treeNodeFecha);
                } else {//ya existe un nodo de tipo fecha creado con anterioridad                    
                    nodHisComparar = (NodoArbolHistorial) treeNodeFecha.getData();
                    if (nodHisNuevo.getStrFecha().compareTo(nodHisComparar.getStrFecha()) == 0) {//la es la misma se crea un nodo REGISTRO_HISTORIAL al ultimo nodo fecha
                        treeNodeCreado = new DefaultTreeNode(nodHisNuevo, treeNodeFecha);
                    } else {//la fecha ha cambiado, se crea un nodo 'FECHA_HISTORIAL' y se le agrega el nodo REGISTRO_HISTORIAL
                        nodHisFecha = new NodoArbolHistorial(TipoNodoEnum.FECHA_HISTORIAL, registro.getFechaReg(), -1, -1, null, null);
                        treeNodeFecha = new DefaultTreeNode(nodHisFecha, nodoInicial);
                        treeNodeFecha.setExpanded(true);
                        treeNodeCreado = new DefaultTreeNode(nodHisNuevo, treeNodeFecha);
                    }
                }
            }
        }
        if (!exitenRegistros) {//no se encontraron registros
            nodHisNuevo = new NodoArbolHistorial(TipoNodoEnum.NOVALUE, null, -1, -1, "", "NO SE ENCONTRARON REGISTROS");
            treeNodeCreado = new DefaultTreeNode(nodHisNuevo, treeNodeRaiz);
        }

        //RequestContext.getCurrentInstance().update("IdFormHistorias:IdPanelHistorico");
    }

    private boolean estaEnListaTipRegHis(String idBuscado) {
        //buscar si un el tipo de registro se debe o no listar el el arbol del historial
        for (String idTipRegCliSelHis : regCliSelHistorial) {
            if (idBuscado.compareTo(idTipRegCliSelHis) == 0) {
                return true;
            }
        }
        return false;
    }

    public void seleccionaNodoArbol() {//determinar si se activan los botenes de 'editar' y 'PDF' del historial
        btnHistorialDisabled = true;
        btnPdfAgrupadoDisabled = true;
        boolean igualTipoRegistro = true;//determinar si los registros seleccionados son del mismo tipo para activar o no btnPdfAgrupadoDisabled
        int tipoRegistroEncontrado = -1;

        if (treeNodesSeleccionados != null) {
            int contadorRegistrosSeleccionadosHistorial = 0;
            for (TreeNode nodo : treeNodesSeleccionados) {
                NodoArbolHistorial nodArbolHisSeleccionado = (NodoArbolHistorial) nodo.getData();
                if (nodArbolHisSeleccionado.getTipoDeNodo() == TipoNodoEnum.REGISTRO_HISTORIAL) {
                    if (tipoRegistroEncontrado == -1) {
                        tipoRegistroEncontrado = nodArbolHisSeleccionado.getIdTipoRegistro();
                    } else {
                        if (tipoRegistroEncontrado != nodArbolHisSeleccionado.getIdTipoRegistro()) {
                            igualTipoRegistro = false;
                        }
                    }
                    contadorRegistrosSeleccionadosHistorial++;
                }
            }
            if (contadorRegistrosSeleccionadosHistorial == 0) {//no hay ninguno
                btnHistorialDisabled = true;
                btnPdfAgrupadoDisabled = true;
            } else if (contadorRegistrosSeleccionadosHistorial == 1) {//solo es uno
                btnHistorialDisabled = false;
                btnPdfAgrupadoDisabled = true;
            } else {//hay mas de uno
                if (igualTipoRegistro) {//son del mismo tipo
                    btnHistorialDisabled = true;
                    btnPdfAgrupadoDisabled = false;
                } else {//no son del mismo tipo
                    btnHistorialDisabled = true;
                    btnPdfAgrupadoDisabled = true;
                }
            }
        }
    }

    public void cargarRegistroClinico() {//cargar un registro clinico seleccionado en el arbol de historial de paciente
        //System.out.println("Se INgreso a cargar registro");
        if (treeNodesSeleccionados == null) {
            imprimirMensaje("Error", "Se debe seleccionar un registro del histórico", FacesMessage.SEVERITY_ERROR);
            return;
        }
        NodoArbolHistorial nodArbolHisSeleccionado = null;
        for (TreeNode nodo : treeNodesSeleccionados) {
            nodArbolHisSeleccionado = (NodoArbolHistorial) nodo.getData();
            if (nodArbolHisSeleccionado.getTipoDeNodo() == TipoNodoEnum.REGISTRO_HISTORIAL) {
                break;
            }
        }
        if (nodArbolHisSeleccionado == null) {
            imprimirMensaje("Error", "Se debe seleccionar un registro del histórico", FacesMessage.SEVERITY_ERROR);
            return;
        }

        tipoRegistroClinico = "";
        tipoRegistroClinico = String.valueOf(nodArbolHisSeleccionado.getIdTipoRegistro());//se posicione el combo
        limpiarFormulario();
        //cargo los datos principales a estructuraCampos
        registroEncontrado = registroFacade.find(nodArbolHisSeleccionado.getIdRegistro());
        if (registroEncontrado.getIdMedico() != null) {
            idPrestador = registroEncontrado.getIdMedico().getIdUsuario().toString();
            especialidadPrestador = registroEncontrado.getIdMedico().getEspecialidad().getDescripcion();
        } else {
            idPrestador = "";
            especialidadPrestador = "";
        }
        fechaReg = registroEncontrado.getFechaReg();
        fechaSis = registroEncontrado.getFechaSis();

        //cargar los datos adicionales a estructuraCampos
        List<HcDetalle> listaDetalles = registroEncontrado.getHcDetalleList();
        for (HcDetalle detalle : listaDetalles) {
            HcCamposReg campo = camposRegFacade.find(detalle.getHcDetallePK().getIdCampo());
            if (campo != null) {
                if (campo.getTabla() != null && campo.getTabla().length() != 0) {//tiene tabala o tipo de dato
                    switch (campo.getTabla()) {
                        case "date":
                            try {
                                Date f = sdfDateHour.parse(detalle.getValor());
                                datosFormulario.setValor(campo.getPosicion(), f);
                            } catch (ParseException ex) {
                                datosFormulario.setValor(campo.getPosicion(), "");
                            }
                            break;
                        default://es una categoria
                            datosFormulario.setValor(campo.getPosicion(), detalle.getValor());
                            break;
                    }
                } else {//simplemente es texto
                    datosFormulario.setValor(campo.getPosicion(), detalle.getValor());
                }//System.out.println("Se coloco en datosFormulario." + campo.getPosicion() + " el valor de " + detalle.getValor());
            } else {
                System.out.println("Error: no se encontro hc_campos_reg.id_campo= " + detalle.getHcDetallePK().getIdCampo());
            }
            cargarMunicipios();//intento cargar municipios sea o no necesario

        }
        modificandoRegCli = true;
        mostrarFormularioRegistroClinico();
        RequestContext.getCurrentInstance().execute("PF('dlgHistorico').hide();");
        RequestContext.getCurrentInstance().update("IdFormHistorias");
    }

    //---------------------------------------------
    //---------   ------------
    //---------------------------------------------  
    private void cargarFuenteDatos(HcRegistro regEncontrado) {
        listaRegistrosParaPdf = new ArrayList<>();
        DatosFormularioHistoria datosReporte = new DatosFormularioHistoria();
        List<HcDetalle> listaCamposDeRegistroEncontrado = regEncontrado.getHcDetalleList();
        //----------------------------------------------------------------------
        //CARGO TITULOS TODOS LOS CAMPOS (hc_campos_registro)-------------------
        //----------------------------------------------------------------------
        List<HcCamposReg> listaCamposPorTipoDeRegistro = camposRegFacade.buscarPorTipoRegistro(regEncontrado.getIdTipoReg().getIdTipoReg());
        for (HcCamposReg campoPorTipoRegistro : listaCamposPorTipoDeRegistro) {
            datosReporte.setValor(campoPorTipoRegistro.getPosicion(), "<b>" + campoPorTipoRegistro.getNombrePdf() + " </b>");
        }
        //nota!!!!!! algunos de los siguientes campos tambien incluirlos en la tabla hc_campos_reg(para que se coloquen titulos en el ciclo anterior)        
        //----------------------------------------------------------------------
        //CARGO DATOS DESDE (hc_registro)(cfg_empresa) -------------------------
        //----------------------------------------------------------------------
        datosReporte.setValor(44, "<b>PRIMER NOMBRE: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getPrimerNombre()));//
        datosReporte.setValor(45, "<b>SEGUNDO NOMBRE: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getSegundoNombre()));//
        datosReporte.setValor(46, "<b>PRIMER APELLIDO: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getPrimerApellido()));//
        datosReporte.setValor(47, "<b>SEGUNDO APELLIDO: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getSegundoApellido()));//
        datosReporte.setValor(48, "<b>CELULAR: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getSegundoApellido()));//
        datosReporte.setValor(49, "<b>CORREO: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getEmail()));//
        datosReporte.setValor(50, "<b>FOLIO: </b>" + regEncontrado.getFolio());//folio
        datosReporte.setValor(51, "<b>HISTORIA No: </b>" + regEncontrado.getIdPaciente().getIdentificacion());//numero de historia
        datosReporte.setValor(53, "<b>NOMBRE: </b>" + pacienteSeleccionado.nombreCompleto());//NOMBRES PACIENTE        

        datosReporte.setValor(54, "<b>FECHA: </b> " + sdfDateHour.format(regEncontrado.getFechaReg()));
        datosReporte.setValor(88, "<b>FECHA: </b> " + sdfDate.format(regEncontrado.getFechaReg()) + "<b> HORA: </b> " + sdfHour.format(regEncontrado.getFechaReg()));
        if (pacienteSeleccionado.getIdAdministradora() != null) {
            datosReporte.setValor(55, "<b>ENTIDAD: </b> " + pacienteSeleccionado.getIdAdministradora().getRazonSocial());
        } else {
            datosReporte.setValor(55, "<b>ENTIDAD: </b> ");
        }
        if (pacienteSeleccionado.getFechaNacimiento() != null) {
            datosReporte.setValor(56, "<b>EDAD: </b> " + calcularEdad(pacienteSeleccionado.getFechaNacimiento()));
            datosReporte.setValor(43, "<b>FECHA NACIMIENTO: </b>" + sdfDate.format(pacienteSeleccionado.getFechaNacimiento()));
        } else {
            datosReporte.setValor(56, "<b>EDAD: </b> ");
            datosReporte.setValor(43, "<b>FECHA NACIMIENTO: </b>");
        }
        if (pacienteSeleccionado.getGenero() != null) {
            datosReporte.setValor(57, "<b>SEXO: </b> " + pacienteSeleccionado.getGenero().getDescripcion());
        } else {
            datosReporte.setValor(57, "<b>SEXO: </b> ");
        }
        if (pacienteSeleccionado.getOcupacion() != null) {
            datosReporte.setValor(58, "<b>OCUPACION: </b> " + pacienteSeleccionado.getOcupacion().getDescripcion());
        } else {
            datosReporte.setValor(58, "<b>OCUPACION: </b> ");
        }
        datosReporte.setValor(59, "<b>DIRECCION: </b> " + obtenerCadenaNoNula(pacienteSeleccionado.getDireccion()));
        datosReporte.setValor(60, "<b>TELEFONO: </b> " + obtenerCadenaNoNula(pacienteSeleccionado.getTelefonoResidencia()));

        if (pacienteSeleccionado.getTipoIdentificacion() != null) {
            datosReporte.setValor(69, pacienteSeleccionado.getTipoIdentificacion().getDescripcion() + " " + pacienteSeleccionado.getIdentificacion());
            datosReporte.setValor(61, "<b>IDENTIFICACION: </b> " + pacienteSeleccionado.getTipoIdentificacion().getDescripcion() + " " + pacienteSeleccionado.getIdentificacion());
        } else {
            datosReporte.setValor(69, pacienteSeleccionado.getIdentificacion());
            datosReporte.setValor(61, "<b>IDENTIFICACION: </b> " + pacienteSeleccionado.getIdentificacion());
        }
        if (pacienteSeleccionado.getRegimen() != null) {
            datosReporte.setValor(62, "<b>TIPO AFILIACION: </b> " + pacienteSeleccionado.getRegimen().getDescripcion());
            datosReporte.setValor(42, "<b>COBERTURA EN SALUD: </b>" + pacienteSeleccionado.getRegimen().getDescripcion());
        } else {
            datosReporte.setValor(62, "<b>TIPO AFILIACION: </b> ");
            datosReporte.setValor(42, "<b>COBERTURA EN SALUD: </b>");
        }
        datosReporte.setValor(63, "<b>RESPONSABLE: </b> " + obtenerCadenaNoNula(pacienteSeleccionado.getResponsable()));
        datosReporte.setValor(64, "<b>TELEFONO: </b> " + obtenerCadenaNoNula(pacienteSeleccionado.getTelefonoResponsable()));
        if (pacienteSeleccionado.getEstadoCivil() != null) {
            datosReporte.setValor(65, "<b>ESTADO CIVIL: </b> " + pacienteSeleccionado.getEstadoCivil().getDescripcion());
        } else {
            datosReporte.setValor(65, "<b>ESTADO CIVIL: </b> ");
        }
        if (pacienteSeleccionado.getDepartamento() != null) {
            datosReporte.setValor(66, "<b>DEPARTAMENTO: </b>" + pacienteSeleccionado.getDepartamento().getDescripcion() + " " + pacienteSeleccionado.getDepartamento().getCodigo());
        } else {
            datosReporte.setValor(66, "<b>DEPARTAMENTO: </b>");
        }
        if (pacienteSeleccionado.getMunicipio() != null) {
            datosReporte.setValor(67, "<b>MUNICIPIO: </b>" + pacienteSeleccionado.getMunicipio().getDescripcion() + " " + pacienteSeleccionado.getMunicipio().getCodigo());//TELEFONO EMPRESA                
        } else {
            datosReporte.setValor(67, "<b>MUNICIPIO: </b>");//TELEFONO EMPRESA                
        }
        if (pacienteSeleccionado.getFirma() != null) {//firma paciente
            datosReporte.setValor(68, loginMB.getRutaCarpetaImagenes() + pacienteSeleccionado.getFirma().getUrlImagen());//FIRMA MEDICO
        } else {
            datosReporte.setValor(68, null);
        }
        datosReporte.setValor(69, pacienteSeleccionado.nombreCompleto() + "<br/>" + datosReporte.getValor(69));//NOMBRE EN FIRMA PACIENTE        

        //empresa
        if (loginMB.getEmpresaActual().getLogo() != null) {
            datosReporte.setValor(70, loginMB.getRutaCarpetaImagenes() + loginMB.getEmpresaActual().getLogo().getUrlImagen());//IMAGEN LOGO
        } else {
            datosReporte.setValor(70, null);//IMAGEN LOGO
        }
        if (regEncontrado.getIdMedico() != null) {
            datosReporte.setValor(71, regEncontrado.getIdMedico().nombreCompleto());//NOMBRE MEDICO
            datosReporte.setValor(86, regEncontrado.getIdMedico().getTelefonoResidencia());//TELEFONO MEDICO
            datosReporte.setValor(87, regEncontrado.getIdMedico().getTelefonoOficina());//CELULAR MEDICO
            datosReporte.setValor(84, regEncontrado.getIdMedico().nombreCompleto());//PARA FIRMA NOMBRE MEDICO
            if (regEncontrado.getIdMedico().getEspecialidad() != null) {
                datosReporte.setValor(72, regEncontrado.getIdMedico().getEspecialidad().getDescripcion());//ESPECIALIDAD MEDICO
                datosReporte.setValor(84, datosReporte.getValor(84) + " <br/> " + regEncontrado.getIdMedico().getEspecialidad().getDescripcion());//PARA FIRMA  NOMBRE MEDICO
            }
            datosReporte.setValor(73, obtenerCadenaNoNula(regEncontrado.getIdMedico().getRegistroProfesional()));//REGISTRO PROFESIONAL MEDICO
            datosReporte.setValor(84, datosReporte.getValor(84) + " <br/> Reg. prof. " + regEncontrado.getIdMedico().getRegistroProfesional());//NOMBRE MEDICO

            if (regEncontrado.getIdMedico().getFirma() != null) {
                datosReporte.setValor(74, loginMB.getRutaCarpetaImagenes() + regEncontrado.getIdMedico().getFirma().getUrlImagen());//FIRMA MEDICO            
            } else {
                datosReporte.setValor(74, null);//FIRMA MEDICO
            }
        }
        datosReporte.setValor(75, "<b>Dirección: </b> " + empresa.getDireccion() + "      " + empresa.getWebsite() + "      <b>Teléfono: </b> " + empresa.getTelefono1());//DIR TEL EMPRESA        
        datosReporte.setValor(76, "<b>NOMBRE: </b>" + empresa.getRazonSocial());//NOMBRE EMPRESA                
        datosReporte.setValor(77, "<b>CODIGO: </b>" + empresa.getCodigoEmpresa());//CODIGO EMPRESA                
        datosReporte.setValor(78, "<b>DIRECCION: </b>" + empresa.getDireccion());//DIRECCION EMPRESA                
        datosReporte.setValor(79, "<b>TELEFONO: </b>" + empresa.getTelefono1());//TELEFONO EMPRESA                
        datosReporte.setValor(80, "<b>DEPARTAMENTO: </b>" + empresa.getCodDepartamento().getCodigo() + " " + empresa.getCodDepartamento().getDescripcion());//TELEFONO EMPRESA                
        datosReporte.setValor(81, "<b>MUNICIPIO: </b>" + empresa.getCodMunicipio().getCodigo() + " " + empresa.getCodMunicipio().getDescripcion());//TELEFONO EMPRESA                
        datosReporte.setValor(82, "<b>" + empresa.getTipoDoc().getDescripcion() + ": </b>  " + empresa.getNumIdentificacion());//NIT        
        datosReporte.setValor(83, empresa.getWebsite());//sitio web       

        
        datosReporte.setValor(97, empresa.getNomRepLegal());//CONSTANSA PORTILLA BENAVIDES
        datosReporte.setValor(98, empresa.getTipoDoc().getDescripcion() + ":" + empresa.getNumIdentificacion() + " " + empresa.getObservaciones());//OPTOMETRA U.L SALLE-BOGOTA        
        datosReporte.setValor(100, empresa.getRazonSocial());//
        datosReporte.setValor(99, "CONSULTORIO " + empresa.getDireccion() + " " + empresa.getCodMunicipio().getDescripcion() + "  TELEFONO: " + empresa.getTelefono1());//CONSULTRIO
        //datosReporte.setValor(85, "<b>ASEGURADORA RESPONSABLE DE LA ATENCION, NUMERO DE POLIZA SI ES SOAT Y VIGENCIA: </b> ");

        //----------------------------------------------------------------------
        //CARGO DATOS QUE SE LLENARON EN EL REGISTRO (hc_detalle)---------------
        //----------------------------------------------------------------------
        for (HcDetalle campoDeRegistroEncontrado : listaCamposDeRegistroEncontrado) {
            if (campoDeRegistroEncontrado.getHcCamposReg().getTabla() != null) {//ES CATEGORIA (realizar busqueda)
                switch (campoDeRegistroEncontrado.getHcCamposReg().getTabla()) {
                    case "cfg_clasificaciones":
                        clasificacionBuscada = clasificacionesFacade.find(Integer.parseInt(campoDeRegistroEncontrado.getValor()));
                        if (clasificacionBuscada != null) {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + clasificacionBuscada.getDescripcion());
                        }
                        break;
                    case "cfg_clasificaciones_2"://el mismo anterior pero imprimiendo tambien el codigo de la clasificacion
                        clasificacionBuscada = clasificacionesFacade.find(Integer.parseInt(campoDeRegistroEncontrado.getValor()));
                        if (clasificacionBuscada != null) {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + clasificacionBuscada.getCodigo() + " - " + clasificacionBuscada.getDescripcion());
                        }
                        break;
                    case "fac_servicio":
                        servicioBuscado = servicioFacade.find(Integer.parseInt(campoDeRegistroEncontrado.getValor()));
                        if (servicioBuscado != null) {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + servicioBuscado.getNombreServicio());
                        }
                        break;
                    case "boolean":
                        if (campoDeRegistroEncontrado.getValor().compareTo("true") == 0) {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b> SI");
                        } else {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b> NO");
                        }
                        break;
                    case "date":
                    case "html":
                        datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + campoDeRegistroEncontrado.getValor());
                        break;
                }
            } else {//NO ES CATEGORIA (sacar valor)
                datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + campoDeRegistroEncontrado.getValor());
            }
        }
        //datosReporte.setListaDatosAdicionales(listadoDatosAdicionales);//CUANDO SE USE SUBRREPORTES USAR ESTA LINEA
        listaRegistrosParaPdf.add(datosReporte);
    }

    public void generarPdf() throws JRException, IOException {//genera un pdf de una historia seleccionada en el historial        
        //----------------------------------------------------------------------
        //VALIDACIONES NECESARIAS-----------------------------------------------
        //----------------------------------------------------------------------
        if (treeNodesSeleccionados == null) {//hay nodos seleccionados en el arbol
            imprimirMensaje("Error", "Se debe seleccionar un registro del histórico", FacesMessage.SEVERITY_ERROR);
            return;
        }
        NodoArbolHistorial nodArbolHisSeleccionado = null;
        for (TreeNode nodo : treeNodesSeleccionados) {
            nodArbolHisSeleccionado = (NodoArbolHistorial) nodo.getData();
            if (nodArbolHisSeleccionado.getTipoDeNodo() == TipoNodoEnum.REGISTRO_HISTORIAL) {
                break;
            }
        }
        if (nodArbolHisSeleccionado == null) {//de los nodos seleccionados ninguno es regitro clinico
            imprimirMensaje("Error", "Se debe seleccionar un registro del histórico", FacesMessage.SEVERITY_ERROR);
            return;
        }
        HcRegistro regEncontrado = registroFacade.find(nodArbolHisSeleccionado.getIdRegistro());

        //----------------------------------------------------------------------
        //SE CARGA LA FUENTE DE DATOS PARA EL PDF ------------------------------
        //----------------------------------------------------------------------
        cargarFuenteDatos(regEncontrado);

        //----------------------------------------------------------------------
        //CREACION DEL PDF -----------------------------------------------------
        //----------------------------------------------------------------------
        JRBeanCollectionDataSource beanCollectionDataSource;
        HttpServletResponse httpServletResponse;
        String rutaReporte = "";
        rutaReporte = rutaReporte + "historias/reportes/";
        rutaReporte = rutaReporte + regEncontrado.getIdTipoReg().getUrlPagina();
        rutaReporte = rutaReporte.substring(0, rutaReporte.length() - 6);//quito .xhtml
        rutaReporte = rutaReporte + ".jasper";

        beanCollectionDataSource = new JRBeanCollectionDataSource(listaRegistrosParaPdf);
        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            JasperPrint jasperPrint = JasperFillManager.fillReport(servletContext.getRealPath(rutaReporte), new HashMap(), beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

//    public void generarPdfAgrupado() throws JRException, IOException {//genera un pdf de un serie de historias seleccionadas en el historial        
//        if (cargarFuenteDeDatosAgrupada()) {//si se puede cargar datos continuo
//            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listaRegistrosParaPdf);
//            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//            try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
//                httpServletResponse.setContentType("application/pdf");
//                ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
//                JasperPrint jasperPrint = JasperFillManager.fillReport(servletContext.getRealPath("historias/reportes/reporteRegCliAgrupado.jasper"), new HashMap(), beanCollectionDataSource);
//                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
//                FacesContext.getCurrentInstance().responseComplete();
//            }
//        }
//    }
    //---------------------------------------------------
    //--------- FUNCIONES REGISTROS CLINICOS ------------
    //---------------------------------------------------
    public void limpiarFormulario() {//reinicia todos los controles de un registro clinico

        modificandoRegCli = false;
        registroEncontrado = null;
        datosFormulario.limpiar();
        listaMunicipios = new ArrayList<>();
        idPrestador = null;
        especialidadPrestador = null;
        fechaReg = new Date();
        fechaSis = new Date();
        //seleccionar medico de ser posible
        if (loginMB.getUsuarioActual().getTipoUsuario().getCodigo().equals("2")) {//es un prestador
            for (CfgUsuarios prestador : listaPrestadores) {
                if (Objects.equals(prestador.getIdUsuario(), loginMB.getUsuarioActual().getIdUsuario())) {
                    idPrestador = loginMB.getUsuarioActual().getIdUsuario().toString();
                    if (prestador.getEspecialidad() != null) {
                        especialidadPrestador = prestador.getEspecialidad().getDescripcion();
                    }
                    break;
                }
            }
        }

    }

    public void btnLimpiarFormulario() {//no se carga ultimo registro
        limpiarFormulario();
        valoresPorDefecto();
        modificandoRegCli = false;
    }

    public void cambiaTipoRegistroClinico() {//cambia el combo 'tipo de registro clinico'
        limpiarFormulario();
        cargarUltimoRegistro();
        valoresPorDefecto();
        modificandoRegCli = false;
    }

    private void cargarUltimoRegistro() {
        mostrarFormularioRegistroClinico();
        if (tipoRegistroClinicoActual != null) {
            registroEncontrado = registroFacade.buscarUltimo(pacienteSeleccionado.getIdPaciente(), tipoRegistroClinicoActual.getIdTipoReg());
            if (registroEncontrado != null) {
                //cargar los datos adicionales a estructuraCampos
                List<HcDetalle> listaDetalles = registroEncontrado.getHcDetalleList();
                for (HcDetalle detalle : listaDetalles) {
                    HcCamposReg campo = camposRegFacade.find(detalle.getHcDetallePK().getIdCampo());
                    if (campo != null) {
                        if (campo.getTabla() != null && campo.getTabla().length() != 0) {//tiene tabala o tipo de dato
                            switch (campo.getTabla()) {
                                case "date":
                                    try {
                                        Date f = sdfDateHour.parse(detalle.getValor());
                                        datosFormulario.setValor(campo.getPosicion(), f);
                                    } catch (ParseException ex) {
                                        datosFormulario.setValor(campo.getPosicion(), "");
                                    }
                                    break;
                                default://es una categoria
                                    datosFormulario.setValor(campo.getPosicion(), detalle.getValor());
                                    break;
                            }
                        } else {//simplemente es texto
                            datosFormulario.setValor(campo.getPosicion(), detalle.getValor());
                        }//System.out.println("Se coloco en datosFormulario." + campo.getPosicion() + " el valor de " + detalle.getValor());
                    } else {
                        System.out.println("Error: no se encontro hc_campos_reg.id_campo= " + detalle.getHcDetallePK().getIdCampo());
                    }
                    cargarMunicipios();//intento cargar municipios sea o no necesario
                }
                imprimirMensaje("Informacion", "Para su facilidad se cargo los datos de la última historia de este tipo de registro", FacesMessage.SEVERITY_INFO);
            } else {
                valoresPorDefecto();
                imprimirMensaje("Informacion", "El paciente no tiene registros anteriores de este tipo", FacesMessage.SEVERITY_INFO);
            }
        }
        //RequestContext.getCurrentInstance().update("IdFormHistorias");

    }

    private void mostrarFormularioRegistroClinico() {//permite visualizar un formulario dependiendo de la seleccion en el combo 'tipo de registro clinico'
        if (tipoRegistroClinico != null && tipoRegistroClinico.length() != 0) {
            tipoRegistroClinicoActual = tipoRegCliFacade.find(Integer.parseInt(tipoRegistroClinico));
            urlPagina = tipoRegistroClinicoActual.getUrlPagina();
            nombreTipoRegistroClinico = tipoRegistroClinicoActual.getNombre();
        } else {
            tipoRegistroClinicoActual = null;
            urlPagina = "";
            nombreTipoRegistroClinico = "";
        }
    }

    public void cambiaMedico() {//cambia el combo 'medico'(actualiza el campo especialidad)
        if (idPrestador != null && idPrestador.length() != 0) {
            especialidadPrestador = usuariosFacade.find(Integer.parseInt(idPrestador)).getEspecialidad().getDescripcion();
        } else {
            especialidadPrestador = "";
        }
    }

    public void actualizarRegistro() {//actualizacion de un registro clinico existente
        List<HcDetalle> listaDetalle = new ArrayList<>();
        HcDetalle nuevoDetalle;
        HcCamposReg campoResgistro;

        if (!modificandoRegCli) {
            imprimirMensaje("Error", "No se ha cargado un registro para poder modificarlo", FacesMessage.SEVERITY_ERROR);
            return;
        }
        //nuevoRegistro=
        if (idPrestador != null && idPrestador.length() != 0) {//validacion de campos obligatorios
            registroEncontrado.setIdMedico(usuariosFacade.find(Integer.parseInt(idPrestador)));
        } else {
            imprimirMensaje("Error", "Debe seleccionar un médico", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (tipoRegistroClinicoActual != null) {
            registroEncontrado.setIdTipoReg(tipoRegistroClinicoActual);
        } else {
            imprimirMensaje("Error", "No se puede determinar el tipo de registro clinico", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (fechaReg == null) {
            fechaReg = fechaSis;
        }
        registroEncontrado.setFechaReg(fechaReg);
        registroEncontrado.setFechaSis(fechaSis);
        registroEncontrado.setIdPaciente(pacienteSeleccionado);
        registroFacade.edit(registroEncontrado);
        for (int i = 0; i < 100; i++) {
            if (datosFormulario.getValor(i) != null && datosFormulario.getValor(i).toString().length() != 0) {
                campoResgistro = camposRegFacade.buscarPorTipoRegistroYPosicion(tipoRegistroClinicoActual.getIdTipoReg(), i);
                if (campoResgistro != null) {
                    nuevoDetalle = new HcDetalle(registroEncontrado.getIdRegistro(), campoResgistro.getIdCampo());
                    if (campoResgistro.getTabla() == null || campoResgistro.getTabla().length() == 0) {
                        nuevoDetalle.setValor(datosFormulario.getValor(i).toString());
                    } else {
                        switch (campoResgistro.getTabla()) {
                            case "html":
                                nuevoDetalle.setValor(corregirHtml(datosFormulario.getValor(i).toString()));
                                break;
                            case "date":
                                try {
                                    Date f = sdfFechaString.parse(datosFormulario.getValor(i).toString());
                                    nuevoDetalle.setValor(sdfDateHour.format(f));
                                } catch (ParseException ex) {
                                    nuevoDetalle.setValor("Error: " + datosFormulario.getValor(i).toString());
                                }
                                break;
                            default://casos: cfg_clasificaciones,boolean, double, u otros
                                nuevoDetalle.setValor(datosFormulario.getValor(i).toString());
                                break;
                        }
                    }
                    listaDetalle.add(nuevoDetalle);
                } else {
                    System.out.println("No encontro en tabla hc_campos_registro el valor: id_tipo_reg = " + tipoRegistroClinicoActual.getIdTipoReg());
                }
            }
        }
        registroEncontrado.setHcDetalleList(listaDetalle);
        registroFacade.edit(registroEncontrado);
        imprimirMensaje("Correcto", "Registro actualizado.", FacesMessage.SEVERITY_INFO);
        limpiarFormulario();
        valoresPorDefecto();
        RequestContext.getCurrentInstance().update("IdFormRegistroClinico");
    }

    public void guardarRegistro() {//guardar un nuevo registro clinico        

        HcRegistro nuevoRegistro = new HcRegistro();
        List<HcDetalle> listaDetalle = new ArrayList<>();
        HcDetalle nuevoDetalle;
        HcCamposReg campoResgistro;
        if (idPrestador != null && idPrestador.length() != 0) {//validacion de campos obligatorios
            nuevoRegistro.setIdMedico(usuariosFacade.find(Integer.parseInt(idPrestador)));
        } else {
            imprimirMensaje("Error", "Debe seleccionar un médico", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (tipoRegistroClinicoActual != null) {
            nuevoRegistro.setIdTipoReg(tipoRegistroClinicoActual);
        } else {
            imprimirMensaje("Error", "No se puede determinar el tipo de registro clinico", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (fechaReg == null) {
            fechaReg = fechaSis;
        }
        nuevoRegistro.setFechaReg(fechaReg);
        nuevoRegistro.setFechaSis(fechaSis);
        nuevoRegistro.setIdPaciente(pacienteSeleccionado);

        if (validarNoVacio(turnoCita)) {
            List<CitCitas> listaCitas = turnosFacade.find(Integer.parseInt(turnoCita)).getCitCitasList();
            CitCitas citaAtendida = null;
            for (CitCitas cita : listaCitas) {
                if (cita.getCancelada() == false) {
                    nuevoRegistro.setIdCita(cita);
                    citaAtendida = cita;
                    break;
                }
            }
            if (citaAtendida != null) {
                citaAtendida.setAtendida(true);
                citasFacade.edit(citaAtendida);
            }
        }
        registroFacade.create(nuevoRegistro);

        for (int i = 0; i < 100; i++) {
            if (datosFormulario.getValor(i) != null && datosFormulario.getValor(i).toString().length() != 0) {
                campoResgistro = camposRegFacade.buscarPorTipoRegistroYPosicion(tipoRegistroClinicoActual.getIdTipoReg(), i);
                if (campoResgistro != null) {
                    nuevoDetalle = new HcDetalle(nuevoRegistro.getIdRegistro(), campoResgistro.getIdCampo());
                    if (campoResgistro.getTabla() == null || campoResgistro.getTabla().length() == 0) {
                        nuevoDetalle.setValor(datosFormulario.getValor(i).toString());
                    } else {
                        switch (campoResgistro.getTabla()) {
                            case "html":
                                nuevoDetalle.setValor(corregirHtml(datosFormulario.getValor(i).toString()));
                                break;
                            case "date":
                                try {
                                    Date f = sdfFechaString.parse(datosFormulario.getValor(i).toString());
                                    nuevoDetalle.setValor(sdfDateHour.format(f));
                                } catch (ParseException ex) {
                                    nuevoDetalle.setValor("Error: " + datosFormulario.getValor(i).toString());
                                }
                                break;
                            default://casos: cfg_clasificaciones,boolean, double, u otros
                                nuevoDetalle.setValor(datosFormulario.getValor(i).toString());
                                break;
                        }
                    }
                    listaDetalle.add(nuevoDetalle);
                } else {
                    System.out.println("No encontro en tabla hc_campos_registro el valor: id_tipo_reg=" + tipoRegistroClinicoActual.getIdTipoReg() + " posicion " + i);
                }
            }
        }

        if (tipoRegistroClinicoActual.getIdTipoReg() == 8) {//SOLICITUD DE AUTORIZACION DE SERVICIOS            
            tipoRegistroClinicoActual.setConsecutivo(tipoRegistroClinicoActual.getConsecutivo() + 1);
            tipoRegCliFacade.edit(tipoRegistroClinicoActual);//incrementar consecutivo a tipo de registro clinico actual           
            for (int i = 0; i < listaDetalle.size(); i++) {//se quita el valor por se autocalculara
                if (listaDetalle.get(i).getHcDetallePK().getIdCampo() == 182) {
                    listaDetalle.remove(i);
                    break;
                }
            }
            nuevoDetalle = new HcDetalle(nuevoRegistro.getIdRegistro(), 182);//numero de solicitud
            nuevoDetalle.setValor(String.valueOf(tipoRegistroClinicoActual.getConsecutivo()));
            listaDetalle.add(nuevoDetalle);
        }

        nuevoRegistro.setHcDetalleList(listaDetalle);
        nuevoRegistro.setFolio(registroFacade.buscarMaximoFolio(nuevoRegistro.getIdPaciente().getIdPaciente()) + 1);
        registroFacade.edit(nuevoRegistro);

        imprimirMensaje("Correcto", "Nuevo registro almacenado.", FacesMessage.SEVERITY_INFO);
        limpiarFormulario();
        valoresPorDefecto();
        RequestContext.getCurrentInstance().update("IdFormRegistroClinico");
    }

    public List<String> autocompletarDiagnostico(String txt) {//retorna una lista con los diagnosticos que contengan el parametro txt
        if (txt != null && txt.length() > 2) {
            return diagnosticoFacade.autocompletarDiagnostico(txt);
        } else {
            return null;
        }
    }

    //---------------------------------------------------
    //------ FUNCIONES CARGAR/BUSCAR PACIENTES ----------
    //---------------------------------------------------
    public void validarIdentificacion() {//verifica si existe la identificacion de lo contrario abre un dialogo para seleccionar el paciente de una tabla
        pacienteTmp = pacientesFacade.buscarPorIdentificacion(identificacionPaciente);

        if (pacienteTmp != null) {
            tipoRegistroClinico = "";
            urlPagina = "";
            nombreTipoRegistroClinico = "";
            pacienteSeleccionadoTabla = pacienteTmp;
            hayPacienteSeleccionado = true;//se pueden mostrar opciones
            cargarPaciente();

        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgSeleccionarPaciente').show();");
        }
    }

    public void editarPaciente() {//se invoca funcion javaScript(cargarPaciente -> home.xhtml) que carga la pestaña de pacientes y los datos del paciende seleccionado
        if (pacienteSeleccionado != null) {
            RequestContext.getCurrentInstance().execute("window.parent.cargarPaciente('Pacientes','configuraciones/pacientes.xhtml','" + pacienteSeleccionado.getIdPaciente() + "')");
        } else {
            hayPacienteSeleccionado = false;
            imprimirMensaje("Error", "Se debe seleccionar un paciente de la tabla", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void cargarPaciente() {//cargar un paciente desde del dialogo de buscar paciente o al digitar una identificacion valida(esta en pacientes)        
        if (pacienteSeleccionadoTabla != null) {
            turnoCita = "";
            pacienteSeleccionado = pacientesFacade.find(pacienteSeleccionadoTabla.getIdPaciente());
            urlPagina = "";
            identificacionPaciente = "";
            nombrePaciente = "Paciente";
            nombreTipoRegistroClinico = "";
            hayPacienteSeleccionado = true;
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
            tipoRegistroClinico = "";
            cambiaTipoRegistroClinico();
            if (!cargandoDesdeTab) {
                RequestContext.getCurrentInstance().execute("PF('dlgSeleccionarPaciente').hide();");
            }
        } else {
            hayPacienteSeleccionado = false;
            imprimirMensaje("Error", "Se debe seleccionar un paciente de la tabla", FacesMessage.SEVERITY_ERROR);
        }
    }

    //---------------------------------------------------
    //-------- FUNCIONES TEXTOS PREDEFINIDOS ------------
    //---------------------------------------------------
    public void seleccionEditor(String ed, String posVec) {//determinar cual 'p:editor' y la posicion en 'estructuraCampos' donde se usara el texto predefinido
        idEditorSeleccionado = ed;
        posListaTxtPredef = Integer.parseInt(posVec);//determinar la posicion en el vector 'estructuraCampos' al usar un texto predefinido

    }

    public void usarTextoPredefinido() {//asignar el texto predefinido a un 'p:editor'
        if (detalleTextoPredef != null && detalleTextoPredef.length() != 0) {
            datosFormulario.setValor(posListaTxtPredef, detalleTextoPredef);
            RequestContext.getCurrentInstance().update("IdFormRegistroClinico:" + idEditorSeleccionado);//se actualiza el editor
            RequestContext.getCurrentInstance().execute("PF('dlgTextosPredefinidos').hide()");//se cierra el dialogo            
        } else {
            imprimirMensaje("Error", "El texto predefinido está vacio", FacesMessage.SEVERITY_ERROR);
        }
    }

    private void recargarMaestrosTxtPredefinidos() {//carga la lista de textos predefinidos (se usa en cualquier p:editor)
        listaMaestrosTxtPredefinidos = maestrosTxtPredefinidosFacade.findAll();
        idMaestroTextoPredef = "";
        nombreTextoPredefinido = "";
        cambiaMaestro();
    }

    public void cambiaMaestro() {//cambia la clasificacion cuando se esta en el dialogo de textos predefinidos
        idTextoPredef = "";
        if (idMaestroTextoPredef != null && idMaestroTextoPredef.length() != 0) {
            listaTxtPredefinidos = maestrosTxtPredefinidosFacade.find(Integer.parseInt(idMaestroTextoPredef)).getCfgTxtPredefinidosList();
            if (listaTxtPredefinidos != null && !listaTxtPredefinidos.isEmpty()) {
                idTextoPredef = listaTxtPredefinidos.get(0).getIdTxtPredefinido().toString();
            }
        } else {
            listaTxtPredefinidos = null;
        }
        cambiaTextoPredefinido();
    }

    public void cambiaTextoPredefinido() {//usado en dialogo 'textosPredefinidos' cuando cambia el combo de 'textos predefinidos', permite cargar el texto predefinido en el p:ditor 
        if (idTextoPredef != null && idTextoPredef.length() != 0) {
            txtPredefinidoActual = txtPredefinidosFacade.find(Integer.parseInt(idTextoPredef));
            detalleTextoPredef = txtPredefinidoActual.getDetalle();
            nombreTextoPredefinido = txtPredefinidoActual.getNombre();
        } else {
            txtPredefinidoActual = null;
            detalleTextoPredef = "";
            nombreTextoPredefinido = "";
        }
    }

    public void guardarTextoPredefinido() {//almacenar en la base de datos el texto predefinido
        if (txtPredefinidoActual != null) {//se cargo uno
            if (txtPredefinidoActual.getNombre().compareTo(nombreTextoPredefinido) == 0) {//son los mismos se actualiza
                txtPredefinidoActual.setDetalle(detalleTextoPredef);
                txtPredefinidosFacade.edit(txtPredefinidoActual);
                imprimirMensaje("Correcto", "El texto predefinido se ha actualizado", FacesMessage.SEVERITY_INFO);
            } else {
                if (nombreTextoPredefinido.trim().length() == 0) {
                    imprimirMensaje("Error", "Debe escribir un nombre para el texto predefinido", FacesMessage.SEVERITY_ERROR);
                } else {//es nuevo se debe crear
                    if (txtPredefinidosFacade.buscarPorNombre(nombreTextoPredefinido) != null) {
                        imprimirMensaje("Error", "Ya existe un texto predefinido con este nombre", FacesMessage.SEVERITY_ERROR);
                    } else {
                        CfgTxtPredefinidos nuevoPredefinido = new CfgTxtPredefinidos();
                        nuevoPredefinido.setNombre(nombreTextoPredefinido);
                        nuevoPredefinido.setIdMaestro(txtPredefinidoActual.getIdMaestro());
                        nuevoPredefinido.setDetalle(detalleTextoPredef);
                        txtPredefinidosFacade.create(nuevoPredefinido);
                        recargarMaestrosTxtPredefinidos();
                        RequestContext.getCurrentInstance().update("IdFormRegistroClinico:IdPanelTextosPredefinidos");//se actualiza el editor
                        imprimirMensaje("Correcto", "El nuevo texto predefinido ha sido creado", FacesMessage.SEVERITY_INFO);
                    }
                }
            }
        } else {//se debe agregar a la categoria
            if (idMaestroTextoPredef != null && idMaestroTextoPredef.length() != 0) {
                if (nombreTextoPredefinido.trim().length() == 0) {
                    imprimirMensaje("Error", "Debe escribir un nombre para el texto predefinido", FacesMessage.SEVERITY_ERROR);
                } else {//es nuevo se debe crear
                    if (txtPredefinidosFacade.buscarPorNombre(nombreTextoPredefinido) != null) {
                        imprimirMensaje("Error", "Ya existe un texto predefinido con este nombre", FacesMessage.SEVERITY_ERROR);
                    } else {
                        CfgTxtPredefinidos nuevoPredefinido = new CfgTxtPredefinidos();
                        nuevoPredefinido.setNombre(nombreTextoPredefinido);
                        nuevoPredefinido.setIdMaestro(maestrosTxtPredefinidosFacade.find(Integer.parseInt(idMaestroTextoPredef)));
                        nuevoPredefinido.setDetalle(detalleTextoPredef);
                        txtPredefinidosFacade.create(nuevoPredefinido);
                        recargarMaestrosTxtPredefinidos();
                        RequestContext.getCurrentInstance().update("IdFormRegistroClinico:IdPanelTextosPredefinidos");//se actualiza el editor
                        imprimirMensaje("Correcto", "El nuevo texto predefinido ha sido creado", FacesMessage.SEVERITY_INFO);
                    }
                }
            } else {
                imprimirMensaje("Error", "No se ha seleccionado ninguna categoría", FacesMessage.SEVERITY_ERROR);
            }
        }
    }

    public void eliminarPredefinido() {//eliminar un texto predefinido de una categoria seleccionada
        if (idTextoPredef != null && idTextoPredef.length() != 0) {
            txtPredefinidosFacade.remove(txtPredefinidosFacade.find(Integer.parseInt(idTextoPredef)));
            recargarMaestrosTxtPredefinidos();
            RequestContext.getCurrentInstance().update("IdFormRegistroClinico:IdPanelTextosPredefinidos");//se actualiza el editor
            imprimirMensaje("Correcto", "El texto predefinido se ha eliminado", FacesMessage.SEVERITY_INFO);
        } else {
            imprimirMensaje("Error", "No se ha seleccionado ningún texto predefinido", FacesMessage.SEVERITY_ERROR);
        }
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET SET ----------------
    //---------------------------------------------------
    public LazyDataModel<CfgPacientes> getListaPacientes() {
        return listaPacientes;
    }

    public void setListaPacientes(LazyDataModel<CfgPacientes> listaPacientes) {
        this.listaPacientes = listaPacientes;
    }
//    public List<CfgPacientes> getListaPacientes() {
//        return listaPacientes;
//    }
//
//    public void setListaPacientes(List<CfgPacientes> listaPacientes) {
//        this.listaPacientes = listaPacientes;
//    }

    public List<CfgPacientes> getListaPacientesFiltro() {
        return listaPacientesFiltro;
    }

    public void setListaPacientesFiltro(List<CfgPacientes> listaPacientesFiltro) {
        this.listaPacientesFiltro = listaPacientesFiltro;
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

    public String getUrlPagina() {
        return urlPagina;
    }

    public void setUrlPagina(String urlPagina) {
        this.urlPagina = urlPagina;
    }

    public String getTipoRegistroClinico() {
        return tipoRegistroClinico;
    }

    public void setTipoRegistroClinico(String tipoRegistroClinico) {
        this.tipoRegistroClinico = tipoRegistroClinico;
    }

    public boolean isHayPacienteSeleccionado() {
        return hayPacienteSeleccionado;
    }

    public void setHayPacienteSeleccionado(boolean hayPacienteSeleccionado) {
        this.hayPacienteSeleccionado = hayPacienteSeleccionado;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
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

    public String getNombreTipoRegistroClinico() {
        return nombreTipoRegistroClinico;
    }

    public void setNombreTipoRegistroClinico(String nombreTipoRegistroClinico) {
        this.nombreTipoRegistroClinico = nombreTipoRegistroClinico;
    }

    public List<CfgMaestrosTxtPredefinidos> getListaMaestrosTxtPredefinidos() {
        return listaMaestrosTxtPredefinidos;
    }

    public void setListaMaestrosTxtPredefinidos(List<CfgMaestrosTxtPredefinidos> listaMaestrosTxtPredefinidos) {
        this.listaMaestrosTxtPredefinidos = listaMaestrosTxtPredefinidos;
    }

    public List<CfgTxtPredefinidos> getListaTxtPredefinidos() {
        return listaTxtPredefinidos;
    }

    public void setListaTxtPredefinidos(List<CfgTxtPredefinidos> listaTxtPredefinidos) {
        this.listaTxtPredefinidos = listaTxtPredefinidos;
    }

    public String getDetalleTextoPredef() {
        return detalleTextoPredef;
    }

    public void setDetalleTextoPredef(String detalleTextoPredef) {
        this.detalleTextoPredef = detalleTextoPredef;
    }

    public String getIdTextoPredef() {
        return idTextoPredef;
    }

    public void setIdTextoPredef(String idTextoPredef) {
        this.idTextoPredef = idTextoPredef;
    }

    public String getIdMaestroTextoPredef() {
        return idMaestroTextoPredef;
    }

    public void setIdMaestroTextoPredef(String idMaestroTextoPredef) {
        this.idMaestroTextoPredef = idMaestroTextoPredef;
    }

    public String getNombreTextoPredefinido() {
        return nombreTextoPredefinido;
    }

    public void setNombreTextoPredefinido(String nombreTextoPredefinido) {
        this.nombreTextoPredefinido = nombreTextoPredefinido;
    }

    public DatosFormularioHistoria getDatosFormulario() {
        return datosFormulario;
    }

    public void setDatosFormulario(DatosFormularioHistoria datosFormulario) {
        this.datosFormulario = datosFormulario;
    }

    public String getIdEditorSeleccionado() {
        return idEditorSeleccionado;
    }

    public void setIdEditorSeleccionado(String idEditorSeleccionado) {
        this.idEditorSeleccionado = idEditorSeleccionado;
    }

    public String getIdPrestador() {
        return idPrestador;
    }

    public void setIdPrestador(String idPrestador) {
        this.idPrestador = idPrestador;
    }

    public String getEspecialidadPrestador() {
        return especialidadPrestador;
    }

    public void setEspecialidadPrestador(String especialidadPrestador) {
        this.especialidadPrestador = especialidadPrestador;
    }

    public Date getFechaReg() {
        return fechaReg;
    }

    public void setFechaReg(Date fechaReg) {
        this.fechaReg = fechaReg;
    }

    public Date getFechaSis() {
        return fechaSis;
    }

    public void setFechaSis(Date fechaSis) {
        this.fechaSis = fechaSis;
    }

    public String[] getRegCliSelHistorial() {
        return regCliSelHistorial;
    }

    public void setRegCliSelHistorial(String[] regCliSelHistorial) {
        this.regCliSelHistorial = regCliSelHistorial;
    }

    public TreeNode getTreeNodeRaiz() {
        return treeNodeRaiz;
    }

    public void setTreeNodeRaiz(TreeNode treeNodeRaiz) {
        this.treeNodeRaiz = treeNodeRaiz;
    }

    public TreeNode[] getTreeNodesSeleccionados() {
        return treeNodesSeleccionados;
    }

    public void setTreeNodesSeleccionados(TreeNode[] treeNodesSeleccionados) {
        this.treeNodesSeleccionados = treeNodesSeleccionados;
    }

    public boolean isModificandoRegCli() {
        return modificandoRegCli;
    }

    public void setModificandoRegCli(boolean modificandoRegCli) {
        this.modificandoRegCli = modificandoRegCli;
    }

    public LoginMB getLoginMB() {
        return loginMB;
    }

    public void setLoginMB(LoginMB loginMB) {
        this.loginMB = loginMB;
    }

    public boolean isBtnHistorialDisabled() {
        return btnHistorialDisabled;
    }

    public void setBtnHistorialDisabled(boolean btnHistorialDisabled) {
        this.btnHistorialDisabled = btnHistorialDisabled;
    }

    public CfgPacientes getPacienteSeleccionadoTabla() {
        return pacienteSeleccionadoTabla;
    }

    public void setPacienteSeleccionadoTabla(CfgPacientes pacienteSeleccionadoTabla) {
        this.pacienteSeleccionadoTabla = pacienteSeleccionadoTabla;
    }

    public boolean isBtnPdfAgrupadoDisabled() {
        return btnPdfAgrupadoDisabled;
    }

    public void setBtnPdfAgrupadoDisabled(boolean btnPdfAgrupadoDisabled) {
        this.btnPdfAgrupadoDisabled = btnPdfAgrupadoDisabled;
    }

    public String getFiltroAutorizacion() {
        return filtroAutorizacion;
    }

    public void setFiltroAutorizacion(String filtroAutorizacion) {
        this.filtroAutorizacion = filtroAutorizacion;
    }

    public Date getFiltroFechaInicial() {
        return filtroFechaInicial;
    }

    public void setFiltroFechaInicial(Date filtroFechaInicial) {
        this.filtroFechaInicial = filtroFechaInicial;
    }

    public Date getFiltroFechaFinal() {
        return filtroFechaFinal;
    }

    public void setFiltroFechaFinal(Date filtroFechaFinal) {
        this.filtroFechaFinal = filtroFechaFinal;
    }

    public boolean isBtnEditarRendered() {
        return btnEditarRendered;
    }

    public void setBtnEditarRendered(boolean btnEditarRendered) {
        this.btnEditarRendered = btnEditarRendered;
    }

    public List<CfgUsuarios> getListaPrestadores() {
        return listaPrestadores;
    }

    public void setListaPrestadores(List<CfgUsuarios> listaPrestadores) {
        this.listaPrestadores = listaPrestadores;
    }

    public String getTurnoCita() {
        return turnoCita;
    }

    public void setTurnoCita(String turnoCita) {
        this.turnoCita = turnoCita;
    }

    public List<SelectItem> getListaMunicipios() {
        return listaMunicipios;
    }

    public void setListaMunicipios(List<SelectItem> listaMunicipios) {
        this.listaMunicipios = listaMunicipios;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

}
