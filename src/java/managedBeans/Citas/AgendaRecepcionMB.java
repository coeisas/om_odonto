/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.Citas;

import beans.utilidades.LazyAgendaModel;
import beans.utilidades.LazyPrestadorDataModel;
import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.CfgPacientes;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.CitAutorizaciones;
import modelo.entidades.CitAutorizacionesServicios;
import modelo.entidades.CitAutorizacionesServiciosPK;
import modelo.entidades.CitCitas;
import modelo.entidades.CitTurnos;
import modelo.entidades.FacAdministradora;
import modelo.entidades.FacConsumoServicio;
import modelo.entidades.FacContrato;
import modelo.entidades.FacManualTarifario;
import modelo.entidades.FacManualTarifarioServicio;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.CitAutorizacionesFacade;
import modelo.fachadas.CitAutorizacionesServiciosFacade;
import modelo.fachadas.CitCitasFacade;
import modelo.fachadas.CitTurnosFacade;
import modelo.fachadas.FacAdministradoraFacade;
import modelo.fachadas.FacConsumoServicioFacade;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.ScheduleEvent;

/**
 *
 * @author Mario
 */
@ManagedBean(name = "agendaRecepcionMB")
@SessionScoped
public class AgendaRecepcionMB extends MetodosGenerales implements Serializable {

    @EJB
    FacConsumoServicioFacade consumoServicioFacade;

    /**
     * Creates a new instance of AgendaRecepcionMB
     */
    public AgendaRecepcionMB() {
    }
    private int sede;
    private LazyAgendaModel evenModel;
    private ScheduleEvent event = new DefaultScheduleEvent();
    private String nombreCompleto;
    private boolean rend;
    private boolean rendBtnCancelar;
    private boolean rendBtnAutorizar;
    private boolean rendBtnParticular;
    private boolean rendBtnEnEspera;
    private boolean renderAgenda;
    private boolean haySesionPrestador;
    private String identificacion;
    private LazyDataModel<CfgUsuarios> listaPrestadores;
    private String display = "none";
    private List<SelectItem> listaEspecialidades;
    private boolean hayPrestadorSeleccionado;
    private boolean renderedBtnFacturar;
    private String idTurno;
    private CfgUsuarios prestadorActual;
    private CitCitas citCita;
    private CitTurnos citTurnos;

    private int motivoCancelacion;
    private String descripcionCancelacion;

    private int sesionesAutorizadas = 1;
    private String numAutorizacion;

    private String minTime;
    private String maxTime;

    @EJB
    CitCitasFacade citasFacade;

    @EJB
    CitTurnosFacade turnosfacade;

    @EJB
    CfgUsuariosFacade usuariosFachada;

    @EJB
    FacAdministradoraFacade administradoraFacade;

    @EJB
    CfgClasificacionesFacade clasificacionesFachada;

    @EJB
    CitAutorizacionesFacade autorizacionesFacade;

    @EJB
    CitAutorizacionesServiciosFacade autorizacionesServiciosFacade;

    @PostConstruct
    private void inicialize() {
        setRenderAgenda(false);
        LoginMB loginMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
        sede = loginMB.getCentroDeAtencionactual().getIdSede();
        if (getPrestadorActual() != null) {
            loadEvents();

        } else {
            setEvenModel(null);
        }
        listaPrestadores = new LazyPrestadorDataModel(usuariosFachada);
        //setListaPrestadores(prestadoresFachada.findAll());
        cargarEspecialidadesPrestadores();
    }

    public void facturarPaciente() {
        if (citCita != null) {
            RequestContext.getCurrentInstance().execute("window.parent.cargarTab('Facturar Paciente','facturacion/facturarPaciente.xhtml','idCita;" + citCita.getIdCita() + "')");
        } else {
            imprimirMensaje("Error", "Se debe seleccionar una cita", FacesMessage.SEVERITY_ERROR);
        }
    }

    //-------------------------------------------------------------------------------
    //--------------------------METODOS DE CARGA DE PRESTADOR------------------------
    //-------------------------------------------------------------------------------
    private void cargarEspecialidadesPrestadores() {
        listaEspecialidades = new ArrayList();
        List<CfgClasificaciones> lista = usuariosFachada.findEspecialidades();
        for (CfgClasificaciones especialidad : lista) {
            if (especialidad != null) {
                listaEspecialidades.add(new SelectItem(especialidad.getId(), especialidad.getDescripcion()));
            }
        }
    }

    public void functionDisplay() {
        if (prestadorActual != null) {
            setDisplay("block");
        } else {
            setDisplay("none");
        }
    }

    public void validarIdentificacion() {//verifica si existe la identificacion de lo contrario abre un dialogo para seleccionar el prestador de una tabla
        setRenderAgenda(false);
        CfgUsuarios prestadorTmp = usuariosFachada.buscarPorIdentificacion(identificacion);
        if (prestadorTmp != null) {
            prestadorActual = prestadorTmp;
            identificacion = prestadorActual.getIdentificacion();
            setRenderAgenda(true);
            loadEvents();
            setHayPrestadorSeleccionado(true);
            setNombreCompleto(obtenerNombreCompleto());
        } else {
            setHayPrestadorSeleccionado(false);
            setNombreCompleto(null);
            setDisplay("none");
            if (!identificacion.isEmpty()) {
                imprimirMensaje("Error", "No se encontro prestador", FacesMessage.SEVERITY_ERROR);
            }
            setPrestadorActual(null);
            setIdentificacion(null);
        }
    }

    public void findprestador() {
        setRenderAgenda(false);
        if (!identificacion.isEmpty()) {
            try {
                prestadorActual = usuariosFachada.buscarPorIdentificacion(identificacion);
                if (prestadorActual == null) {
                    setHayPrestadorSeleccionado(false);
                    imprimirMensaje("Error", "No se encontro el prestador", FacesMessage.SEVERITY_ERROR);
                    setNombreCompleto(null);
                } else {
                    setPrestadorActual(prestadorActual);
                    setRenderAgenda(true);
                    loadEvents();
                    setHayPrestadorSeleccionado(true);
                    setNombreCompleto(obtenerNombreCompleto());
                }
                functionDisplay();
            } catch (Exception e) {
                imprimirMensaje("Error", "Ingrese un codigo de prestador valido", FacesMessage.SEVERITY_ERROR);
                setHayPrestadorSeleccionado(false);
                setNombreCompleto(null);
                setDisplay("none");
            }
        } else {
            setPrestadorActual(null);
            functionDisplay();
            setHayPrestadorSeleccionado(false);
            setNombreCompleto(null);
        }
    }

    public void actualizarPrestador() {
        if (prestadorActual != null) {
            setIdentificacion(prestadorActual.getIdentificacion());
            setHayPrestadorSeleccionado(true);
            obtenerNombreCompleto();
        }
        functionDisplay();
    }

    //-------------------------------------------------------------------------------
    //--------------------------METODOS DE LA AGENDA---------------------------------
    //-------------------------------------------------------------------------------
    private String establerLimitesAgenda(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("ha");
        return format.format(date);
    }

    public void loadEvents() {
        setRenderAgenda(false);
        if (prestadorActual != null) {
            Object[] horas = turnosfacade.MinDateMaxDate(prestadorActual.getIdUsuario(), sede);
            if (horas[0] != null) {
                setMinTime(establerLimitesAgenda((Date) horas[0]));
                Date aux = (Date) horas[1];
                if (aux.getMinutes() > 0) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(aux);
                    calendar.add(Calendar.HOUR_OF_DAY, 1);
                    aux = calendar.getTime();
                }
                setMaxTime(establerLimitesAgenda(aux));
                setRenderAgenda(true);
                setEvenModel(new LazyAgendaModel(getPrestadorActual().getIdUsuario(), sede, turnosfacade, citasFacade, "recepcionCitas"));
            } else {
                imprimirMensaje("Información", "El prestador no tiene agenda", FacesMessage.SEVERITY_WARN);
            }
        }
    }

    private String obtenerNombreCompleto() {
        setNombreCompleto("");
        if (prestadorActual.getPrimerNombre() != null) {
            setNombreCompleto(getNombreCompleto() + prestadorActual.getPrimerNombre());
        }
        if (prestadorActual.getSegundoNombre() != null) {
            setNombreCompleto(getNombreCompleto() + " " + prestadorActual.getSegundoNombre());
        }
        if (prestadorActual.getPrimerApellido() != null) {
            setNombreCompleto(getNombreCompleto() + " " + prestadorActual.getPrimerApellido());
        }
        if (prestadorActual.getSegundoApellido() != null) {
            setNombreCompleto(getNombreCompleto() + " " + prestadorActual.getSegundoApellido());
        }
        return getNombreCompleto();
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
        if (event.getTitle() != null) {
            String[] vector = event.getTitle().split(" - ");
            idTurno = vector[0];
            seleccionarCita(Long.parseLong(idTurno));
            if (citCita != null) {
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("formRecepcion:pdialog");
                context.execute("PF('eventDialog').show()");
            }
        }
    }

    private void seleccionarCita(long id) {
        CitCitas cita = citasFacade.findCitasByTurno(id);
        setCitCita(cita);
        if (cita != null) {
            rend = true;
            renderedBtnFacturar = !cita.getFacturada();
            //si la cita corresponde a un particular
            if (citCita.getIdAdministradora().getCodigoAdministradora().equals("1")) {//pacientes particulares no requieren autorizaciones
                //un paciente particular no necesita autorizacion

                setRendBtnParticular(false);
                if (cita.getCancelada() || citCita.getAtendida()) {
                    setRendBtnCancelar(false);
                } else {
                    setRendBtnCancelar(true);
                }

                setRendBtnAutorizar(false);

                if (cita.getIdTurno().getEstado().equals("en_espera") || citCita.getAtendida()) {
                    setRendBtnEnEspera(false);
                    setRendBtnCancelar(false);
                } else {
                    setRendBtnEnEspera(true);
                }
            } else { // si el paciente no es particular
                //si la cita requiere autorizacion y la tiene asociada
                if (citCita.getIdServicio().getAutorizacion() && citCita.getIdAutorizacion() != null) {//comprueba si la cita incluye una autorizacion
                    setRendBtnParticular(false);
                    setRendBtnAutorizar(false);
                    if (cita.getCancelada() || citCita.getAtendida()) {
                        setRendBtnCancelar(false);
                    } else {
                        setRendBtnCancelar(true);
                    }
                    if (cita.getIdTurno().getEstado().equals("en_espera") || citCita.getAtendida()) {
                        setRendBtnCancelar(false);
                        setRendBtnEnEspera(false);
                    } else {
                        setRendBtnEnEspera(true);
                    }
                } else if (citCita.getIdServicio().getAutorizacion() && citCita.getIdAutorizacion() == null) {//si la cita necesita autorizacion pero esta no la tiene incluida. Primero se buscara dentro de la tabla autorizaciones  si hay alguna disponible y se la asociara a la cita
                    CitAutorizaciones autorizacion = autorizacionesFacade.findAutorizacion(citCita.getIdPaciente().getIdPaciente(), citCita.getIdServicio().getIdServicio(), citCita.getIdAdministradora().getIdAdministradora());
                    if (autorizacion != null) {//se encontro una autorizacion vigente para el servicio de la cita
                        for (CitAutorizacionesServicios cas : autorizacion.getCitAutorizacionesServiciosList()) {
                            if (cas.getFacServicio() == citCita.getIdServicio()) {
                                cas.setSesionesPendientes(cas.getSesionesPendientes() - 1);
                                autorizacionesServiciosFacade.edit(cas);
                                break;
                            }
                        }
                        autorizacionesFacade.edit(autorizacion);
                        citCita.setIdAutorizacion(autorizacion);
                        citasFacade.edit(citCita);
                        setRendBtnCancelar(true);
                        setRendBtnAutorizar(false);
                        setRendBtnParticular(false);
                        setRendBtnEnEspera(true);
                    } else {
                        setRenderedBtnFacturar(false);
                        setRendBtnCancelar(true);
                        setRendBtnAutorizar(true);
                        setRendBtnParticular(true);
                        setRendBtnEnEspera(false);
                    }
                } else if (!citCita.getIdServicio().getAutorizacion()) {//si el servicio asociado a la cita no requiere autorizacion
                    if (citCita.getCancelada() || citCita.getAtendida() || citCita.getIdTurno().getEstado().equals("en_espera")) {
                        setRendBtnCancelar(false);
                    } else {
                        setRendBtnCancelar(true);
                    }
                    setRendBtnAutorizar(false);
                    setRendBtnParticular(false);
                    setRendBtnEnEspera(true);
                }
            }
            //rendBtnElegir = false;
        } else {
            rend = false;
            setRendBtnAutorizar(false);
            setRendBtnCancelar(false);
            setRendBtnParticular(false);
            setRendBtnEnEspera(false);
            setRenderedBtnFacturar(false);
            //rendBtnElegir = true;
        }
    }

    public void cambiarParticular() {
        //la administradora con codigo 1 es PARTICULAR
        FacAdministradora administradora = administradoraFacade.buscarPorCodigo("1");
        citCita.setIdAdministradora(administradora);
        citasFacade.edit(citCita);
        setRendBtnParticular(false);
        setRendBtnAutorizar(false);
        setRendBtnEnEspera(true);
        setRenderedBtnFacturar(true);
        RequestContext.getCurrentInstance().update("formRecepcion:eventDetails");
        imprimirMensaje("Correcto", "Esta cita ha cambiado de administradora a Particular", FacesMessage.SEVERITY_INFO);
    }

    public void cancelarCita(ActionEvent actionEvent) {
        if (!citCita.getCancelada() && !citCita.getAtendida()) {
            citCita.setCancelada(true);
            CfgClasificaciones clasificaciones = clasificacionesFachada.find(getMotivoCancelacion());
            citCita.setDescCancelacion(getDescripcionCancelacion());
            citCita.setMotivoCancelacion(clasificaciones);
            citCita.setFechaCancelacion(new Date());
            citasFacade.edit(citCita);
            CitTurnos turno = citCita.getIdTurno();
            turno.setEstado("disponible");
            turno.setContador(turno.getContador() - 1);
            turnosfacade.edit(turno);
            if (citCita.getIdAutorizacion() != null) {
                CitAutorizaciones autorizacion = citCita.getIdAutorizacion();
                CitAutorizacionesServicios cas = autorizacionesServiciosFacade.buscarServicioPorAutorizacion(autorizacion.getIdAutorizacion(), citCita.getIdServicio().getIdServicio());
                if (cas != null) {
                    cas.setSesionesPendientes(cas.getSesionesPendientes() + 1);
                    autorizacionesServiciosFacade.edit(cas);
                }
            }
            setRendBtnAutorizar(false);
            setRendBtnCancelar(false);
            setRendBtnParticular(false);
            RequestContext.getCurrentInstance().update("formRecepcion:eventDetails");
            RequestContext.getCurrentInstance().execute("PF('dlgcancelar').hide()");
            RequestContext.getCurrentInstance().execute("PF('eventDialog').hide()");
            loadEvents();
            RequestContext.getCurrentInstance().update("formRecepcion");
            imprimirMensaje("Correcto", "Cita " + citCita.getIdCita() + " cancelada", FacesMessage.SEVERITY_INFO);
            //actualiza los items de listaCitas
            //cargarCitas();
        } else {
            imprimirMensaje("Error", "Cita " + citCita.getIdCita() + " ya se encuentra cancelada o atendida", FacesMessage.SEVERITY_ERROR);
        }
        setDescripcionCancelacion(null);
    }

    public void abrirTabAutorizaciones() {
        RequestContext.getCurrentInstance().execute("window.parent.cargarTab('Autorizaciones','citas/autorizaciones.xhtml','idCita;" + citCita.getIdCita().toString() + ";idServicio;" + citCita.getIdServicio().getIdServicio().toString() + "')");
    }

    public void crearAutorizacion() {
        if (numAutorizacion.isEmpty()) {
            imprimirMensaje("Error", "Ingrese el numero de autorizacion", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (autorizacionesFacade.findAutorizacionDisponible(citCita.getIdPaciente().getIdPaciente(), citCita.getIdServicio().getIdServicio(), citCita.getIdAdministradora().getIdAdministradora()) != null) {
            numAutorizacion = null;
            imprimirMensaje("Alerta", "El paciente ya cuenta con una autorizacion vigente para este servicio", FacesMessage.SEVERITY_WARN);
            return;
        }
        CitAutorizaciones autorizacion = new CitAutorizaciones();
        autorizacion.setPaciente(citCita.getIdPaciente());
        autorizacion.setAdministradora(citCita.getIdAdministradora());
        autorizacion.setNumAutorizacion(numAutorizacion);
        LoginMB loginMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
        autorizacion.setIdUsuarioCreador(loginMB.getUsuarioActual());
        autorizacion.setCerrada(false);
        autorizacionesFacade.create(autorizacion);
        CitAutorizacionesServicios autorizacionServicio = new CitAutorizacionesServicios();
        autorizacionServicio.setFacServicio(citCita.getIdServicio());
        autorizacionServicio.setSesionesAutorizadas(getSesionesAutorizadas());
        autorizacionServicio.setSesionesRealizadas(0);
        autorizacionServicio.setSesionesPendientes(getSesionesAutorizadas() - 1);
        autorizacionServicio.setCitAutorizaciones(autorizacion);
        autorizacionServicio.setCitAutorizacionesServiciosPK(new CitAutorizacionesServiciosPK(autorizacion.getIdAutorizacion(), citCita.getIdServicio().getIdServicio()));
        autorizacionesServiciosFacade.create(autorizacionServicio);
        citCita.setIdAutorizacion(autorizacion);
        citasFacade.edit(citCita);
        setRendBtnAutorizar(false);
        setRendBtnParticular(false);
        setRendBtnCancelar(true);
        setRendBtnEnEspera(true);
        setNumAutorizacion(null);
        setSesionesAutorizadas(1);
        RequestContext.getCurrentInstance().update("formRecepcion:eventDetails");
        imprimirMensaje("Información", "Autorizacion creada correctamente", FacesMessage.SEVERITY_INFO);
    }

    public void citaEnEspera() {
        if (agregarServicioPorCambioAEspera()) {
            CitTurnos turno = citCita.getIdTurno();
            turno.setEstado("en_espera");
            turnosfacade.edit(turno);
            loadEvents();
            RequestContext.getCurrentInstance().update("formRecepcion");
            imprimirMensaje("Correcto", "Cita No " + citCita.getIdCita() + " en espera", FacesMessage.SEVERITY_INFO);
        }
    }

    private boolean agregarServicioPorCambioAEspera() {//al cambiar de estado a 'espera' => servicio debe pasar a consumos

        CfgPacientes pacienteSeleccionado = citCita.getIdPaciente();
        FacContrato contratoSeleccionado = null;
        FacManualTarifario manualTarifarioPaciente = null;
        String mensajeConfiguracion = null;

        if (pacienteSeleccionado.getIdAdministradora() != null) {//SE VALIDA QUE SE PUEDA OBTENER EL MANUAL TARIFARIO
            if (pacienteSeleccionado.getRegimen() != null) {
                FacAdministradora ad = pacienteSeleccionado.getIdAdministradora();
                if (ad.getFacContratoList() != null && !ad.getFacContratoList().isEmpty()) {
                    for (FacContrato contrato : ad.getFacContratoList()) {//BUSCO UN MANUAL QUE CORRESPONDA AL MISMO REGIMEN DEL PACIENTE
                        if (Objects.equals(pacienteSeleccionado.getRegimen().getId(), contrato.getTipoContrato().getId())) {
                            contratoSeleccionado = contrato;
                        }
                    }
                    if (contratoSeleccionado == null) {
                        mensajeConfiguracion = "No se puede agregar el servicio a consumos por que ningún contrato es del tipo: " + pacienteSeleccionado.getRegimen().getDescripcion();
                    } else {
                        if (contratoSeleccionado.getIdManualTarifario() != null) {//DETERMINAR SI CONTRATO SELECCIONADO TIENE MANUAL TARIFARIO
                            manualTarifarioPaciente = contratoSeleccionado.getIdManualTarifario();
                        } else {
                            mensajeConfiguracion = "No se puede agregar el servicio a consumos por que el contrato '" + contratoSeleccionado.getDescripcion() + "' no tiene manual tarifario";
                        }
                    }
                } else {
                    mensajeConfiguracion = "No se puede agregar el servicio a consumos por que la adminstradora no tiene ningún contrato";
                }
            } else {
                mensajeConfiguracion = "No se puede agregar el servicio a consumos por paciente no tiene régimen";
            }
        } else {
            mensajeConfiguracion = "No se puede agregar el servicio a consumos por que el paciente no tiene administradora.";
        }
        if (mensajeConfiguracion != null) {//APARECIO ERROR AL CARGAR MANUA TARIFARIO
            imprimirMensaje("Alerta", mensajeConfiguracion, FacesMessage.SEVERITY_WARN);
            return false;
        }
        //busco el servicio en el manual tarifario del paciente
        FacManualTarifarioServicio servicioEnManualTarifario = null;
        for (FacManualTarifarioServicio servicioManual : manualTarifarioPaciente.getFacManualTarifarioServicioList()) {
            if (Objects.equals(servicioManual.getFacServicio().getIdServicio(), citCita.getIdServicio().getIdServicio())) {
                servicioEnManualTarifario = servicioManual;
                break;
            }
        }

        if (servicioEnManualTarifario == null) {//servicio no se encuentra en el manual tarifario                
            imprimirMensaje("Alerta", "El servicio " + citCita.getIdServicio().getNombreServicio() + " no se encuentra en el manual tarifario " + manualTarifarioPaciente.getNombreManualTarifario(), FacesMessage.SEVERITY_WARN);
            return false;
        } else {//servicio si esta en el manual tarifario
            FacConsumoServicio nuevoConsumoServicio = new FacConsumoServicio();
            nuevoConsumoServicio.setIdPaciente(pacienteSeleccionado);
            nuevoConsumoServicio.setIdPrestador(citCita.getIdPrestador());
            nuevoConsumoServicio.setIdServicio(citCita.getIdServicio());
            nuevoConsumoServicio.setFecha(new Date());
            nuevoConsumoServicio.setCantidad(1);
            nuevoConsumoServicio.setTipoTarifa(servicioEnManualTarifario.getTipoTarifa());
            nuevoConsumoServicio.setValorUnitario(servicioEnManualTarifario.getValorFinal());
            nuevoConsumoServicio.setValorFinal(servicioEnManualTarifario.getValorFinal());
            consumoServicioFacade.create(nuevoConsumoServicio);
            return true;
        }
    }

//-----------------------------------------------------------------------------------------------------
//---------------------------------------GETTERS AND SETTERS-------------------------------------------
//-----------------------------------------------------------------------------------------------------        
    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public CfgUsuarios getPrestadorActual() {
        return prestadorActual;
    }

    public void setPrestadorActual(CfgUsuarios prestadorActual) {
        this.prestadorActual = prestadorActual;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public boolean isRend() {
        return rend;
    }

    public void setRend(boolean rend) {
        this.rend = rend;
    }

    public CitCitas getCitCita() {
        return citCita;
    }

    public void setCitCita(CitCitas citCita) {
        this.citCita = citCita;
    }

    public CitTurnos getCitTurnos() {
        return citTurnos;
    }

    public void setCitTurnos(CitTurnos citTurnos) {
        this.citTurnos = citTurnos;
    }

    public boolean isHaySesionPrestador() {
        return haySesionPrestador;
    }

    public void setHaySesionPrestador(boolean haySesionPrestador) {
        this.haySesionPrestador = haySesionPrestador;
    }

    public boolean isRendBtnCancelar() {
        return rendBtnCancelar;
    }

    public void setRendBtnCancelar(boolean rendBtnCancelar) {
        this.rendBtnCancelar = rendBtnCancelar;
    }

    public boolean isRendBtnAutorizar() {
        return rendBtnAutorizar;
    }

    public void setRendBtnAutorizar(boolean rendBtnAutorizar) {
        this.rendBtnAutorizar = rendBtnAutorizar;
    }

    public LazyDataModel<CfgUsuarios> getListaPrestadores() {
        return listaPrestadores;
    }

    public void setListaPrestadores(LazyDataModel<CfgUsuarios> listaPrestadores) {
        this.listaPrestadores = listaPrestadores;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public List<SelectItem> getListaEspecialidades() {
        return listaEspecialidades;
    }

    public void setListaEspecialidades(List<SelectItem> listaEspecialidades) {
        this.listaEspecialidades = listaEspecialidades;
    }

    public boolean isHayPrestadorSeleccionado() {
        return hayPrestadorSeleccionado;
    }

    public void setHayPrestadorSeleccionado(boolean hayPrestadorSeleccionado) {
        this.hayPrestadorSeleccionado = hayPrestadorSeleccionado;
    }

    public boolean isRendBtnParticular() {
        return rendBtnParticular;
    }

    public void setRendBtnParticular(boolean rendBtnParticular) {
        this.rendBtnParticular = rendBtnParticular;
    }

    public int getMotivoCancelacion() {
        return motivoCancelacion;
    }

    public void setMotivoCancelacion(int motivoCancelacion) {
        this.motivoCancelacion = motivoCancelacion;
    }

    public String getDescripcionCancelacion() {
        return descripcionCancelacion;
    }

    public void setDescripcionCancelacion(String descripcionCancelacion) {
        this.descripcionCancelacion = descripcionCancelacion;
    }

    public String getNumAutorizacion() {
        return numAutorizacion;
    }

    public void setNumAutorizacion(String numAutorizacion) {
        this.numAutorizacion = numAutorizacion;
    }

    public int getSesionesAutorizadas() {
        return sesionesAutorizadas;
    }

    public void setSesionesAutorizadas(int sesionesAutorizadas) {
        this.sesionesAutorizadas = sesionesAutorizadas;
    }

    public boolean isRendBtnEnEspera() {
        return rendBtnEnEspera;
    }

    public void setRendBtnEnEspera(boolean rendBtnEnEspera) {
        this.rendBtnEnEspera = rendBtnEnEspera;
    }

    public String getMinTime() {
        return minTime;
    }

    public void setMinTime(String minTime) {
        this.minTime = minTime;
    }

    public String getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(String maxTime) {
        this.maxTime = maxTime;
    }

    public boolean isRenderAgenda() {
        return renderAgenda;
    }

    public void setRenderAgenda(boolean renderAgenda) {
        this.renderAgenda = renderAgenda;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public boolean isRenderedBtnFacturar() {
        return renderedBtnFacturar;
    }

    public void setRenderedBtnFacturar(boolean renderedBtnFacturar) {
        this.renderedBtnFacturar = renderedBtnFacturar;
    }

    public String getIdTurno() {
        return idTurno;
    }

    public LazyAgendaModel getEvenModel() {
        return evenModel;
    }

    public void setEvenModel(LazyAgendaModel evenModel) {
        this.evenModel = evenModel;
    }

}
