/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.Citas;

import beans.utilidades.LazyPacienteDataModel;
import beans.utilidades.LazyTurnosDataModel;
import beans.utilidades.MetodosGenerales;
import beans.utilidades.ObjetTnode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import modelo.entidades.CfgConsultorios;
import modelo.entidades.CfgPacientes;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.CitAutorizaciones;
import modelo.entidades.CitAutorizacionesServicios;
import modelo.entidades.CitAutorizacionesServiciosPK;
import modelo.entidades.CitCitas;
import modelo.entidades.CitTurnos;
import modelo.entidades.FacAdministradora;
import modelo.entidades.FacServicio;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgConsultoriosFacade;
import modelo.fachadas.CfgPacientesFacade;
import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.CitAutorizacionesFacade;
import modelo.fachadas.CitAutorizacionesServiciosFacade;
import modelo.fachadas.CitCitasFacade;
import modelo.fachadas.CitTurnosFacade;
import modelo.fachadas.FacServicioFacade;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Mario
 */
@ManagedBean(name = "citasMasivaMB")
@SessionScoped
public class CitasMasivaMB extends MetodosGenerales implements Serializable {

    //informacion paciente
    private String tipoIdentificacion;
    private String identificacion;
    private CfgPacientes pacienteSeleccionado;
    private String displayPaciente = "none";//muestra u oculta campos del paciente
    private boolean hayPacienteSeleccionado;
    private List<SelectItem> listaServicios;
    private LazyDataModel<CfgPacientes> listaPacientes;

    //patrones de disponibilidad. sirven para filtrar los turnos a mostrar
    private List diassemana;
    private Date horaIni;
    private Date horaFin;

    //listado de especialidades de los prestadores actuales
    private List<SelectItem> listaEspecialidades;
    private CfgUsuarios prestadorSeleccionado;

    //private List<CitTurnos> listaTurnos;
    private LazyDataModel<CitTurnos> listaTurnos;
    private int idServicio;
    private int motivoConsulta;
    private List<CitCitas> listaCitas;
    private boolean renderizarListaTurnos = false;

    private CitCitas citaSeleccionada;
    private CitTurnos turnoSeleccionado;

    //TreeNode
    private TreeNode root;
    private TreeNode[] selectedNodes;
    private List<Integer> idsprestadores;

    //cancelacionCita
    private int motivoCancelacion;
    private String descripcionCancelacion;

    //autorizacion
    private boolean autorizacionvalidada = false;
    private String nombreServicio;
    private String numAutorizacion;
    private int sesionesAutorizadas = 1;
    private CitAutorizaciones autorizacionSeleccionada;
    private CitAutorizacionesServicios autorizacionServicioSeleccionado;
    private int sede;

    @EJB
    CfgPacientesFacade pacientesFachada;

    @EJB
    CfgUsuariosFacade usuariosFachada;

    @EJB
    FacServicioFacade facServicioFacade;

    @EJB
    CitCitasFacade citasFacade;

    @EJB
    CitTurnosFacade turnosFacade;

    @EJB
    CfgClasificacionesFacade clasificacionesFachada;

    @EJB
    CitAutorizacionesFacade autorizacionesFacade;

    @EJB
    CitAutorizacionesServiciosFacade autorizacionesServiciosFacade;

    @EJB
    CfgConsultoriosFacade consultoriosFacade;

    /**
     * Creates a new instance of CitasMasivaMB
     */
    public CitasMasivaMB() {
    }

    @PostConstruct
    public void init() {
        setListaPacientes(new LazyPacienteDataModel(pacientesFachada));
        setListaServicios((List<SelectItem>) new ArrayList());
        crearlistaServicios();
        root = createCheckboxDocuments();

        //setListaPrestadores(prestadoresFachada.findAll());
        setListaTurnos((new LazyTurnosDataModel(turnosFacade, idsPrestadores(), horaIni, horaFin, getDiassemana())));
        cargarEspecialidadesPrestadores();
        setListaCitas((List<CitCitas>) new ArrayList());
        LoginMB loginMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
        sede = loginMB.getCentroDeAtencionactual().getIdSede();
    }

    //-----------------------------------------------------------------------------------
    //-----------------------METODOS DE CARGAR PACIENTE----------------------------------
    //-----------------------------------------------------------------------------------    
    public void findPaciente() {
        listaCitas.clear();
        RequestContext.getCurrentInstance().update("result");
        if (getIdentificacion() != null) {
            if (!identificacion.isEmpty()) {

                //setPacienteSeleccionado(null);
                pacienteSeleccionado = pacientesFachada.buscarPorIdentificacion(getIdentificacion());
                if (pacienteSeleccionado == null) {
                    imprimirMensaje("Error", "No se encontro el paciente", FacesMessage.SEVERITY_ERROR);
                    setHayPacienteSeleccionado(false);
                    setDisplayPaciente("none");
                } else {
                    pacienteSeleccionado.setEdad(calcularEdad(pacienteSeleccionado.getFechaNacimiento()));
                    setDisplayPaciente("block");
                    setPacienteSeleccionado(pacienteSeleccionado);
                    setHayPacienteSeleccionado(true);
                    //descomentar cuando los servicios dependen del genero, edad, zona y administradora del paciente
                    //loadServicios(null);
                }
            } else {
                setPacienteSeleccionado(null);
                setDisplayPaciente("none");
                setHayPacienteSeleccionado(false);
            }
        } else {
            setPacienteSeleccionado(null);
            setDisplayPaciente("none");
            setHayPacienteSeleccionado(false);
        }
        limpiarServicioMotivoConsulta();

    }

    public void actualizarPaciente() {
        listaCitas.clear();
        RequestContext.getCurrentInstance().update("result");
        if (pacienteSeleccionado != null) {
            setHayPacienteSeleccionado(true);
            setIdentificacion(pacienteSeleccionado.getIdentificacion());
            if (pacienteSeleccionado.getFechaNacimiento() != null) {
                pacienteSeleccionado.setEdad(calcularEdad(pacienteSeleccionado.getFechaNacimiento()));
            }
            setDisplayPaciente("block");
            setTipoIdentificacion(String.valueOf(pacienteSeleccionado.getTipoIdentificacion().getId()));
        }

        limpiarServicioMotivoConsulta();
    }

    private void crearlistaServicios() {
        List<FacServicio> servicios = facServicioFacade.buscarActivos();
        for (FacServicio servicio : servicios) {
            getListaServicios().add(new SelectItem(servicio.getIdServicio(), servicio.getNombreServicio()));
        }
    }

    //----------------------------------------------------------------------------------
    //-----------------------METODOS DE CARGAR PRESTADOR--------------------------------
    //----------------------------------------------------------------------------------    
    private void cargarEspecialidadesPrestadores() {
        setListaEspecialidades((List<SelectItem>) new ArrayList());
        List<CfgClasificaciones> lista = usuariosFachada.findEspecialidades();
        for (CfgClasificaciones especialidad : lista) {
            if (especialidad != null) {
                getListaEspecialidades().add(new SelectItem(especialidad.getId(), especialidad.getDescripcion()));
            }
        }
    }

    private List<Integer> idsPrestadores() {
        List<Integer> ids = new ArrayList();
        List<CfgUsuarios> prestadores = usuariosFachada.findPrestadores();
        if (prestadores != null) {
            for (CfgUsuarios cfgUsuario : prestadores) {
                ids.add(cfgUsuario.getIdUsuario());
            }
        }
        return ids;
    }

    //-----------------------------------------------------------------------------------
    //---------------------------METODOS DE GESTION DE CITAS-----------------------------
    //-----------------------------------------------------------------------------------
    public void guardarCita() {
        //System.out.println("Guardando cita ...");
        if (pacienteSeleccionado == null) {
            imprimirMensaje("Error", "Necesita ingresar el paciente", FacesMessage.SEVERITY_ERROR);
            return;
        }

        if (pacienteSeleccionado.getIdAdministradora() == null) {
            imprimirMensaje("Error", "El paciente no tiene asignado una Admnistradora y tampoco esta especificado como Particular", FacesMessage.SEVERITY_ERROR);
            return;
        }

        if (getPrestadorSeleccionado() == null) {
            imprimirMensaje("Error", "Necesita ingresar el prestador", FacesMessage.SEVERITY_ERROR);
            return;
        }

        if (getTurnoSeleccionado() == null) {
            imprimirMensaje("Error", "Necesita elegir el turno de la cita", FacesMessage.SEVERITY_ERROR);
            return;
        }

        //variable que controla si el servicio o tipo de cita seleccionado requiere autorizacion
        boolean ban = false;
        if (getIdServicio() != 0) {
            FacServicio facServicio = facServicioFacade.find(getIdServicio());
            if (facServicio.getAutorizacion()) {
                ban = true;
                validarAutorizacion(0);
                //descomentar si para crear una cita es necesario poseer una autorizacion registrada previamente
                /*
                 if (!autorizacionMB.isAutorizacionvalidada()) {
                 imprimirMensaje("Información", "Para crear la cita se requiere autorizacion", FacesMessage.SEVERITY_ERROR);
                 return;
                 }
                 */
            }

        } else {
            imprimirMensaje("Error", "Es necesario elegir el servicio", FacesMessage.SEVERITY_ERROR);
            return;

        }

//        if (getMotivoConsulta() == 0) {
//            imprimirMensaje("Error", "Especifique el motivo de consulta", FacesMessage.SEVERITY_ERROR);
//            return;
//        }
        if (getTurnoSeleccionado().getEstado().equals("asignado")) {
            imprimirMensaje("Error", "El turno ya se encuentra asignado", FacesMessage.SEVERITY_ERROR);
            return;
        }

        if (pacienteSeleccionado != null) {
            //creando una cita nueva
            CitCitas nuevaCita = new CitCitas();
            nuevaCita.setIdPaciente(pacienteSeleccionado);
            nuevaCita.setIdPrestador(getPrestadorSeleccionado());
            nuevaCita.setIdTurno(getTurnoSeleccionado());
            if (motivoConsulta != 0) {
                CfgClasificaciones clasificaciones;
                clasificaciones = clasificacionesFachada.find(getMotivoConsulta());
                nuevaCita.setTipoCita(clasificaciones);
            }
            nuevaCita.setFechaRegistro(new Date());
            nuevaCita.setIdServicio(new FacServicio(idServicio));
            nuevaCita.setIdPaquete(null);
            nuevaCita.setFacturada(false);
            nuevaCita.setAtendida(false);
            nuevaCita.setCancelada(false);
            nuevaCita.setMultado(false);
            FacAdministradora facAdministradora = pacienteSeleccionado.getIdAdministradora();
            nuevaCita.setIdAdministradora(facAdministradora);
            //si el servicio requiere autorizacion se envia esta a la tabla de citas

            if (ban) {
                if (isAutorizacionvalidada()) {
                    nuevaCita.setIdAutorizacion(getAutorizacionSeleccionada());
                    //se modifica las sesiones pendientes del servicio de la autorizacion implicada
                    CitAutorizaciones autorizacion = getAutorizacionSeleccionada();
                    CitAutorizacionesServicios autorizacionServicio = autorizacionesServiciosFacade.buscarServicioPorAutorizacion(autorizacion.getIdAutorizacion(), idServicio);
                    if (autorizacionServicio != null) {
                        autorizacionServicio.setSesionesPendientes(autorizacionServicio.getSesionesPendientes() - 1);
                        autorizacionesServiciosFacade.edit(autorizacionServicio);
                    }
                } else {
                    nuevaCita.setIdAutorizacion(null);
                }
            } else {
                nuevaCita.setIdAutorizacion(null);
            }
            nuevaCita.setTieneRegAsociado(false);
            citasFacade.create(nuevaCita);

            //modificando la tabla turnos: se incrementa el contador, dependiendo de la situacion el estado del turno puede llegar a ser false
            getTurnoSeleccionado().setContador(getTurnoSeleccionado().getContador() + 1);
            if (getTurnoSeleccionado().getContador() == getTurnoSeleccionado().getConcurrencia()) {
                //turno.setEstado(false);
                getTurnoSeleccionado().setEstado("asignado");
            }
            turnosFacade.edit(getTurnoSeleccionado());

            //liberando Selecciones previas
            imprimirMensaje("Correto", "La cita ha sido creada.", FacesMessage.SEVERITY_INFO);
            setIdServicio(0);
            loadTurnos();
            //carga la lista de citas que sera usada porla tabla que muestra las citas creadas para el prestador elegido
            //cargarCitas();
            //listaCitas.add(citasFacade.findCitasByTurno(getTurnoSeleccionado().getIdTurno()));
            listaCitas.add(nuevaCita);
            //setCitaSelecionada(citasFacade.findCitasByTurno(idTurno));
            RequestContext.getCurrentInstance().update("result");
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlgresult').show();");
        } else {
            imprimirMensaje("Error", "Es necesario seleccionar el paciente", FacesMessage.SEVERITY_ERROR);
        }
        liberarCampos(0);

    }

    //ban == 1 muestra informaciona adcional cuando se valida una autorizacion
    public void validarAutorizacion(int ban) {
        if (idServicio != 0) {
            FacServicio facServicio = facServicioFacade.find(idServicio);
            setNombreServicio(facServicio.getNombreServicio());
            if (facServicio.getAutorizacion()) {
                if (pacienteSeleccionado != null) {
                    if (pacienteSeleccionado.getIdAdministradora() == null) {
                        idServicio = 0;
                        imprimirMensaje("Error", "Este paciente no tiene administradora, debe ingresar a pacientes y asignarle una administradora", FacesMessage.SEVERITY_ERROR);
                        return;
                    }
                    //paciente particular no necesita validar autorizaciones
                    if (pacienteSeleccionado.getIdAdministradora().getCodigoAdministradora().equals("1")) {
                        setAutorizacionSeleccionada(null);
                        autorizacionvalidada = false;
                        RequestContext context = RequestContext.getCurrentInstance();
                        context.update("autorizar");
                        return;
                    }
                    CitAutorizaciones autorizacion = autorizacionesFacade.findAutorizacion(pacienteSeleccionado.getIdPaciente(), idServicio, pacienteSeleccionado.getIdAdministradora().getIdAdministradora());
                    if (autorizacion != null) {
                        autorizacionvalidada = true;
                        setAutorizacionSeleccionada(autorizacion);
                        if (ban == 1) {
                            setAutorizacionServicioSeleccionado(autorizacionesServiciosFacade.buscarServicioPorAutorizacion(autorizacion.getIdAutorizacion(), idServicio));
                            RequestContext context = RequestContext.getCurrentInstance();
                            context.execute("PF('dlgresautorizacion').show();");
                        }
                    } else {
                        autorizacionvalidada = false;
                        setAutorizacionSeleccionada(null);
                        setAutorizacionServicioSeleccionado(null);
                        RequestContext context = RequestContext.getCurrentInstance();
                        context.update("autorizar");
                        if (ban == 1) {
                            imprimirMensaje("Alerta", "El servicio requiere autorizacion y el paciente no posee una autorizacion vigente", FacesMessage.SEVERITY_WARN);
                        }
                    }
                }
            } else {
                setNombreServicio(null);
                setAutorizacionSeleccionada(null);
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("autorizar");
                autorizacionvalidada = false;
            }

        } else {
            autorizacionvalidada = false;
            setNombreServicio(null);
            setAutorizacionSeleccionada(null);
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("autorizar");
        }
    }

    public void crearAutorizacion() {
        System.out.println("creando autorizacion");
        if (pacienteSeleccionado == null) {
            imprimirMensaje("Error", "Ingrese un documento de paciente valido", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (pacienteSeleccionado.getIdAdministradora() != null) {
            if (pacienteSeleccionado.getIdAdministradora().getCodigoAdministradora().equals("1")) {
                imprimirMensaje("Error", "El paciente esta registrado como particular, no necesita autorizaciones", FacesMessage.SEVERITY_ERROR);
                return;
            }
        } else {
            imprimirMensaje("Error", "El paciente no esta registrado como particular y no tiene una administradora asociada", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (idServicio == 0) {
            imprimirMensaje("Error", "No ha seleccionado un servicio", FacesMessage.SEVERITY_ERROR);
            return;

        }
        if (sesionesAutorizadas == 0) {
            imprimirMensaje("Error", "Ingrese el numero de sesiones autorizadas", FacesMessage.SEVERITY_ERROR);
            return;

        }
        if (numAutorizacion.isEmpty()) {
            imprimirMensaje("Error", "Ingrese el numero de autorizacion", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (autorizacionesFacade.findAutorizacionDisponible(pacienteSeleccionado.getIdPaciente(), idServicio, pacienteSeleccionado.getIdAdministradora().getIdAdministradora()) != null) {
            imprimirMensaje("Error", "El paciente ya cuenta con una autorizacion para este servicio", FacesMessage.SEVERITY_ERROR);
            return;
        }
        CitAutorizaciones autorizacion = new CitAutorizaciones();
        autorizacion.setPaciente(pacienteSeleccionado);
        autorizacion.setAdministradora(pacienteSeleccionado.getIdAdministradora());
        LoginMB loginMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
        autorizacion.setIdUsuarioCreador(loginMB.getUsuarioActual());
        autorizacion.setNumAutorizacion(numAutorizacion);
        autorizacion.setCerrada(false);
        autorizacionesFacade.create(autorizacion);
        //ojo
        CitAutorizacionesServicios autorizacionServicio = new CitAutorizacionesServicios();
        autorizacionServicio.setFacServicio(facServicioFacade.find(idServicio));
        autorizacionServicio.setSesionesAutorizadas(sesionesAutorizadas);
        autorizacionServicio.setSesionesRealizadas(0);
        autorizacionServicio.setSesionesPendientes(sesionesAutorizadas);
        autorizacionServicio.setCitAutorizaciones(autorizacion);
        autorizacionServicio.setCitAutorizacionesServiciosPK(new CitAutorizacionesServiciosPK(autorizacion.getIdAutorizacion(), idServicio));
        autorizacionesServiciosFacade.create(autorizacionServicio);
        validarAutorizacion(1);
        imprimirMensaje("Información", "Autorizacion creada correctamente", FacesMessage.SEVERITY_INFO);
    }

    public void limpiarServicioMotivoConsulta() {
        setIdServicio(0);
        setMotivoConsulta(0);
        setAutorizacionSeleccionada(null);
        autorizacionvalidada = false;
        //setListaCitas(null);
        //listaCitas = new ArrayList();
    }

    public void liberarCampos(int ban) {
        if (ban == 1) {
            setPacienteSeleccionado(null);
            setPrestadorSeleccionado(null);
            setMotivoConsulta(0);
            setIdServicio(0);
        }
        //setIdPrestador(0);
        //idPaciente = 0;

        setMotivoCancelacion(0);
        setDescripcionCancelacion(null);
        setTurnoSeleccionado(null);
        setAutorizacionSeleccionada(null);
        setNumAutorizacion(null);
        autorizacionvalidada = false;

    }

    public void seleccionarCita(ActionEvent actionEvent) {
        int id_cita = (int) actionEvent.getComponent().getAttributes().get("id_cita");
        citaSeleccionada = citasFacade.find(id_cita);
    }

    public void cancelarCita(ActionEvent actionEvent) {
        //System.out.println(motivoCancelacion + " - " + descripcionCancelacion);
        if (!citaSeleccionada.getCancelada() && !citaSeleccionada.getAtendida()) {
            citaSeleccionada.setCancelada(true);
            CfgClasificaciones clasificaciones = clasificacionesFachada.find(getMotivoCancelacion());
            citaSeleccionada.setFechaCancelacion(new Date());
            citaSeleccionada.setDescCancelacion(getDescripcionCancelacion());
            citaSeleccionada.setMotivoCancelacion(clasificaciones);
            citasFacade.edit(citaSeleccionada);
            CitTurnos turno = citaSeleccionada.getIdTurno();
            turno.setEstado("disponible");
            turno.setContador(turno.getContador() - 1);
            turnosFacade.edit(turno);
            if (citaSeleccionada.getIdAutorizacion() != null) {
                CitAutorizaciones autorizacion = citaSeleccionada.getIdAutorizacion();
                CitAutorizacionesServicios autorizacionServicio = autorizacionesServiciosFacade.buscarServicioPorAutorizacion(autorizacion.getIdAutorizacion(), idServicio);
                if (autorizacionServicio.getFacServicio() != null) {
                    autorizacionServicio.setSesionesPendientes(autorizacionServicio.getSesionesPendientes() + 1);
                    autorizacionesServiciosFacade.edit(autorizacionServicio);
                }
            }
            imprimirMensaje("Correcto", "Cita " + citaSeleccionada.getIdCita() + " cancelada", FacesMessage.SEVERITY_INFO);
            //actualiza los items de listaCitas
            loadTurnos();
            listaCitas.remove(citaSeleccionada);
            setListaCitas(listaCitas);
            //cargarCitas();
        } else {
            imprimirMensaje("Error", "Cita " + citaSeleccionada.getIdCita() + " ya se encuentra cancelada o atendida", FacesMessage.SEVERITY_ERROR);
        }
        setDescripcionCancelacion(null);

    }

    //-------------------------------------------------------------------
    //----------------------TURNOS DE CITA-------------------------------
    //-------------------------------------------------------------------
    public void loadTurnos() {
        //List<Integer> idsprestadores = selectionViewMB.getIdsprestadores();
        if (getIdsprestadores() != null) {
            if (getIdsprestadores().size() > 0) {
                //turnos sin importar la sede
                //listaTurnos = new LazyTurnosDataModel(turnosFacade, idsprestadores, horaIni, horaFin, getDiassemana());

                //turnos importando la sede por la cual se inicio sesion

                listaTurnos = new LazyTurnosDataModel(turnosFacade, idsprestadores, horaIni, horaFin, getDiassemana(), sede);

                setListaTurnos(listaTurnos);
            } else {
                setListaTurnos(null);
            }
        } else {
            setListaTurnos(null);
        }
        RequestContext.getCurrentInstance().update("tabprincipal:formturnos");
    }

    boolean diadisponible(int dia) {
        boolean ban = false;
        for (Integer i : getDiassemana()) {
            if (dia == i) {
                ban = true;
                break;
            }
        }
        return ban;
    }

    public void guardarDisponibilidad() {
        if (horaFin != null && horaIni != null) {
            if (horaFin.compareTo(horaIni) < 0) {
                imprimirMensaje("Error", "La hora final es anterior a la inicial", FacesMessage.SEVERITY_ERROR);
                return;
            }
        }
        loadTurnos();
        imprimirMensaje("Informacion", "Disponibilidad registrada correctamente", FacesMessage.SEVERITY_INFO);
    }

    public void seleccionarTurno(ActionEvent actionEvent) {
        int id = (int) actionEvent.getComponent().getAttributes().get("id_turno");
        CitTurnos turno = turnosFacade.find(id);
        setTurnoSeleccionado(turno);
        setPrestadorSeleccionado(turno.getIdPrestador());
        RequestContext.getCurrentInstance().update("formCita");
        RequestContext.getCurrentInstance().execute("PF('dlgCita').show();");
        //imprimirMensaje("Informacion", "turno " + turnoSeleccionado.getIdTurno(), FacesMessage.SEVERITY_INFO);
    }

    //--------------------------------------------------------------------------------------------
    //------------------------METODOS COMPONENTE TREENODE-----------------------------------------
    //--------------------------------------------------------------------------------------------
    public TreeNode createCheckboxDocuments() {
        List<CfgClasificaciones> listaespecialidades = usuariosFachada.findEspecialidades();
        root = new CheckboxTreeNode(new ObjetTnode(0, null, "Prestadores", false), null);

        for (CfgClasificaciones ce : listaespecialidades) {
            if (ce != null) {
                List<CfgUsuarios> listaprestadores = usuariosFachada.findPrestadorByEspecialidad(ce.getId());
                if (listaprestadores.size() > 0) {
                    TreeNode especialidad = new CheckboxTreeNode(new ObjetTnode(0, ce.getId().toString(), ce.getDescripcion().toUpperCase(), false), root);

                    for (CfgUsuarios p : listaprestadores) {
                        String fullname = p.getPrimerNombre() + " " + (p.getSegundoNombre() != null ? p.getSegundoNombre() : "") + " " + p.getPrimerApellido() + " " + (p.getSegundoApellido() != null ? p.getSegundoApellido() : "");
                        TreeNode prestadores = new CheckboxTreeNode(new ObjetTnode(p.getIdUsuario(), null, fullname, true), especialidad);
                    }
                }
            }
        }

        return root;
    }

    public void displaySelectedMultiple(NodeSelectEvent event) {
        if (selectedNodes != null && selectedNodes.length > 0) {
            setIdsprestadores((List<Integer>) new ArrayList());
            for (TreeNode node : selectedNodes) {
                if (!node.getData().toString().equals("0")) {
                    getIdsprestadores().add(Integer.parseInt(node.getData().toString()));
                }
            }
            setRenderizarListaTurnos(true);
            loadTurnos();
        } else {
            getIdsprestadores().clear();
            setRenderizarListaTurnos(false);
        }
        RequestContext.getCurrentInstance().update("tabprincipal:formturnos");
    }

    public void displayUnSelectedMultiple(NodeUnselectEvent event) {
        if (selectedNodes != null && selectedNodes.length > 0) {
            setIdsprestadores((List<Integer>) new ArrayList());
            for (TreeNode node : selectedNodes) {
                if (!node.getData().toString().equals("0")) {
                    getIdsprestadores().add(Integer.parseInt(node.getData().toString()));
                }
            }
            setRenderizarListaTurnos(true);
            loadTurnos();
        } else {
            getIdsprestadores().clear();
            setRenderizarListaTurnos(false);
        }
        RequestContext.getCurrentInstance().update("tabprincipal:formturnos");
    }

    //-------------------------------------------------------------------
    //----------------------GETTERS AND SETTERS--------------------------
    //-------------------------------------------------------------------
    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public CfgPacientes getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }

    public void setPacienteSeleccionado(CfgPacientes pacienteSeleccionado) {
        this.pacienteSeleccionado = pacienteSeleccionado;
    }

    public String getDisplayPaciente() {
        return displayPaciente;
    }

    public void setDisplayPaciente(String displayPaciente) {
        this.displayPaciente = displayPaciente;
    }

    public boolean isHayPacienteSeleccionado() {
        return hayPacienteSeleccionado;
    }

    public void setHayPacienteSeleccionado(boolean hayPacienteSeleccionado) {
        this.hayPacienteSeleccionado = hayPacienteSeleccionado;
    }

    public List<SelectItem> getListaServicios() {
        return listaServicios;
    }

    public void setListaServicios(List<SelectItem> listaServicios) {
        this.listaServicios = listaServicios;
    }

    public LazyDataModel<CfgPacientes> getListaPacientes() {
        return listaPacientes;
    }

    public void setListaPacientes(LazyDataModel<CfgPacientes> listaPacientes) {
        this.listaPacientes = listaPacientes;
    }

    public List<SelectItem> getListaEspecialidades() {
        return listaEspecialidades;
    }

    public void setListaEspecialidades(List<SelectItem> listaEspecialidades) {
        this.listaEspecialidades = listaEspecialidades;
    }

    public List<CitCitas> getListaCitas() {
        return listaCitas;
    }

    public void setListaCitas(List<CitCitas> listaCitas) {
        this.listaCitas = listaCitas;
    }

    public CitTurnos getTurnoSeleccionado() {
        return turnoSeleccionado;
    }

    public void setTurnoSeleccionado(CitTurnos turnoSeleccionado) {
        this.turnoSeleccionado = turnoSeleccionado;
    }

    public CfgUsuarios getPrestadorSeleccionado() {
        return prestadorSeleccionado;
    }

    public void setPrestadorSeleccionado(CfgUsuarios prestadorSeleccionado) {
        this.prestadorSeleccionado = prestadorSeleccionado;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public int getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(int motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public CitCitas getCitaSeleccionada() {
        return citaSeleccionada;
    }

    public void setCitaSeleccionada(CitCitas citaSeleccionada) {
        this.citaSeleccionada = citaSeleccionada;
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

    public Date getHoraIni() {
        return horaIni;
    }

    public void setHoraIni(Date horaIni) {
        this.horaIni = horaIni;
    }

    public Date getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public List<Integer> getIdsprestadores() {
        return idsprestadores;
    }

    public void setIdsprestadores(List<Integer> idsprestadores) {
        this.idsprestadores = idsprestadores;
    }

    public LazyDataModel<CitTurnos> getListaTurnos() {
        return listaTurnos;
    }

    public void setListaTurnos(LazyDataModel<CitTurnos> listaTurnos) {
        this.listaTurnos = listaTurnos;
    }

    public boolean isRenderizarListaTurnos() {
        return renderizarListaTurnos;
    }

    public void setRenderizarListaTurnos(boolean renderizarListaTurnos) {
        this.renderizarListaTurnos = renderizarListaTurnos;
    }

    public List<Integer> getDiassemana() {
        return diassemana;
    }

    public void setDiassemana(List<Integer> diassemana) {
        this.diassemana = diassemana;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
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

    public CitAutorizaciones getAutorizacionSeleccionada() {
        return autorizacionSeleccionada;
    }

    public void setAutorizacionSeleccionada(CitAutorizaciones autorizacionSeleccionada) {
        this.autorizacionSeleccionada = autorizacionSeleccionada;
    }

    public boolean isAutorizacionvalidada() {
        return autorizacionvalidada;
    }

    public void setAutorizacionvalidada(boolean autorizacionvalidada) {
        this.autorizacionvalidada = autorizacionvalidada;
    }

    public CitAutorizacionesServicios getAutorizacionServicioSeleccionado() {
        return autorizacionServicioSeleccionado;
    }

    public void setAutorizacionServicioSeleccionado(CitAutorizacionesServicios autorizacionServicioSeleccionado) {
        this.autorizacionServicioSeleccionado = autorizacionServicioSeleccionado;
    }

}
