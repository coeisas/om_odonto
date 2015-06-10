/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.Citas;

import beans.utilidades.CtrlSesionesAutorizadas;
import beans.utilidades.LazyPacienteDataModel;
import beans.utilidades.LazyTurnosDataModel;
import beans.utilidades.MetodosGenerales;
import beans.utilidades.ObjetTnode;
import beans.utilidades.TurnoServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Mario
 */
@ManagedBean(name = "citasMasivasV2MB")
@SessionScoped
public class CitasMasivasV2MB extends MetodosGenerales implements Serializable {

    //informacion paciente
    private String tipoIdentificacion;
    private String identificacion;
    private CfgPacientes pacienteSeleccionado;
    private String displayPaciente = "none";//muestra u oculta campos del paciente
    private String displayServicio = "block";//muestra u oculta campos para determinar el servicio para todos los turnos que se seleccionen
    private boolean hayPacienteSeleccionado;
    private boolean rendBtnAutorizacion = false;
    private List<SelectItem> listaServicios;
    private List<FacServicio> listaServiciosSeleccionados;//listado de los diferentes servicios que se han asignado a los turnos 
    private List<CtrlSesionesAutorizadas> listaControlSesionesAutorizadas;//controla las sesiones autorizadas para cada servicio
    private List<TurnoServicio> listaTurnoServicio;
    private LazyDataModel<CfgPacientes> listaPacientes;

    //patrones de disponibilidad. sirven para filtrar los turnos a mostrar
    private List diassemana;
    private Date horaIni;
    private Date horaFin;
    private boolean diferenteServicio = false;
    CitTurnos turnoSeleccionado = null;//turno que se selecciona en la tabla turnos cuando el servicio cambia entre turnos 

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
    private List<CitTurnos> listaTurnosSeleccionado;
    private List<CitTurnos> listaTurnosSeleccionadosRespaldo;

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
    private boolean autorizacionrequerida;
    int totalTurnosSeleccionables;//total turnos para un unico servicio

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

    public CitasMasivasV2MB() {
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
        listaTurnosSeleccionado = new ArrayList();
        setListaTurnosSeleccionadosRespaldo((List<CitTurnos>) new ArrayList());
        LoginMB loginMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
        sede = loginMB.getCentroDeAtencionactual().getIdSede();
        //setIdsprestadores((List<Integer>) new ArrayList());
        listaServiciosSeleccionados = new ArrayList();
        listaControlSesionesAutorizadas = new ArrayList();
        listaTurnoServicio = new ArrayList();
        idsprestadores = new ArrayList();
    }

    //-----------------------------------------------------------------------------------
    //-----------------------METODOS DE CARGAR PACIENTE----------------------------------
    //-----------------------------------------------------------------------------------    
    public void findPaciente() {
        listaCitas.clear();
        listaTurnosSeleccionadosRespaldo.clear();
        listaControlSesionesAutorizadas.clear();
        listaTurnoServicio.clear();
        listaTurnosSeleccionado.clear();
        listaTurnosSeleccionado.clear();
        getListaTurnosSeleccionadosRespaldo().clear();
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
        listaTurnosSeleccionadosRespaldo.clear();
        listaControlSesionesAutorizadas.clear();
        listaTurnoServicio.clear();
        listaTurnosSeleccionado.clear();
        listaCitas.clear();
        listaTurnosSeleccionado.clear();
        getListaTurnosSeleccionadosRespaldo().clear();
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

    public void switchDiferenteServicio() {
        listaTurnosSeleccionado.clear();
        listaServiciosSeleccionados.clear();
        listaTurnosSeleccionadosRespaldo.clear();
        listaControlSesionesAutorizadas.clear();
        listaTurnoServicio.clear();
        displayServicio = diferenteServicio ? "none" : "block";
        setIdServicio(0);
        setAutorizacionSeleccionada(null);
        setNumAutorizacion(null);
        RequestContext.getCurrentInstance().update("idFormTurnosSeleccionados");
        RequestContext.getCurrentInstance().update("tabprincipal:formCita");
    }

    public void abrirTabAutorizaciones() {
        RequestContext.getCurrentInstance().execute("window.parent.cargarTab('Autorizaciones','citas/autorizaciones.xhtml','idPaciente;" + pacienteSeleccionado.getIdPaciente().toString() + ";idServicio;" + String.valueOf(idServicio) + "')");
    }

    //----------------------------------------------------------------------------------
    //----------------------SELECTEVENT TABLA TURNOS DISPONIBLES-----------------------
    //----------------------------------------------------------------------------------
    public void onRowSelect(SelectEvent event) {
        CitTurnos turno = (CitTurnos) event.getObject();
        if (listaTurnosSeleccionadosRespaldo.contains(turno)) {
            imprimirMensaje("Informacion", "El turno ya se habia adicionado", FacesMessage.SEVERITY_WARN);
            return;
        }
        if (getListaTurnosSeleccionadosRespaldo().size() > 0) {
            listaTurnosSeleccionado.clear();
            for (CitTurnos ct : getListaTurnosSeleccionadosRespaldo()) {
                listaTurnosSeleccionado.add(ct);
            }
        }
//        si cada turno seleccionado admitira un servicio diferente. Se abre un modal para elegir el servicio 
        if (isDiferenteServicio()) {
            setIdServicio(0);
            turnoSeleccionado = turno;
            RequestContext.getCurrentInstance().update("formservicio");
            RequestContext.getCurrentInstance().execute("PF('dlgServicio').show()");
            return;
//        cada turno seleccionado admite un unico servicio
        } else {
            turnoSeleccionado = null;
            if (idServicio == 0) {
                imprimirMensaje("Erro", "Necesita especifacar el servicio", FacesMessage.SEVERITY_ERROR);
                return;
            }
            if (pacienteSeleccionado.getIdAdministradora() != null) {
                //si el paciente no es particular. Se controla la cantidad de turnos seleccionables.
                if (!pacienteSeleccionado.getIdAdministradora().getCodigoAdministradora().equals("1")) {
                    if (facServicioFacade.buscarPorIdServicio(idServicio).getAutorizacion()) {//si el servicio requiere autorizacion
                        if (totalTurnosSeleccionables > 0) {
                            totalTurnosSeleccionables -= 1;
                        } else {
                            if (autorizacionrequerida && autorizacionSeleccionada == null) {
                                imprimirMensaje("Error", "Se necesita autorizacion", FacesMessage.SEVERITY_ERROR);
                                return;
                            } else {
                                if (totalTurnosSeleccionables <= 0) {
                                    imprimirMensaje("Error", "La autorizacion no permite adicionar otro turno", FacesMessage.SEVERITY_ERROR);
                                    return;
                                }
                            }
                        }
                    }
                }
            } else {
                imprimirMensaje("Error", "El paciente no tiene definida la administradora", FacesMessage.SEVERITY_ERROR);
                return;
            }
        }
        listaTurnosSeleccionado.add(turno);
        getListaTurnosSeleccionadosRespaldo().add(turno);
        ordenarListaTurnos();
//        getListaTurnosSeleccionadosRespaldo().sort(new Comparator<CitTurnos>() {
//            @Override
//            public int compare(CitTurnos o1, CitTurnos o2) {
//                long time = o1.getFecha().getTime() + o1.getHoraIni().getTime();
//                Date aux = new Date(time);
//                time = o2.getFecha().getTime() + o2.getHoraIni().getTime();
//                Date aux2 = new Date(time);
//                return aux.compareTo(aux2);
//            }
//        });        
//        FacesMessage msg = new FacesMessage("Turno Selected", turno.getIdTurno().toString());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    private void ordenarListaTurnos() {
        Collections.sort(listaTurnosSeleccionadosRespaldo, new Comparator<CitTurnos>() {
            @Override
            public int compare(CitTurnos o1, CitTurnos o2) {
                long time = o1.getFecha().getTime() + o1.getHoraIni().getTime();
                Date aux = new Date(time);
                time = o2.getFecha().getTime() + o2.getHoraIni().getTime();
                Date aux2 = new Date(time);
                return aux.compareTo(aux2);
            }
        });
        RequestContext.getCurrentInstance().update("idFormTurnosSeleccionados");
    }

    public void onRowUnselect(UnselectEvent event) {
        CitTurnos turno = (CitTurnos) event.getObject();
        if (getListaTurnosSeleccionadosRespaldo().size() > 0) {
            listaTurnosSeleccionado.clear();
            for (CitTurnos ct : getListaTurnosSeleccionadosRespaldo()) {
                listaTurnosSeleccionado.add(ct);
            }
        } else {
            return;
        }
        listaTurnosSeleccionado.remove(turno);
        getListaTurnosSeleccionadosRespaldo().remove(turno);
        if (!isDiferenteServicio()) {
            if (pacienteSeleccionado.getIdAdministradora() != null) {
                //si el paciente no es particular. Se controla la cantidad de turnos seleccionables.
                if (!pacienteSeleccionado.getIdAdministradora().getCodigoAdministradora().equals("1")) {
                    FacServicio servicio = facServicioFacade.buscarPorIdServicio(idServicio);
                    if (servicio == null) {
                        return;
                    }
                    if (servicio.getAutorizacion()) {//si el servicio requiere autorizacion
                        if (autorizacionrequerida && autorizacionSeleccionada != null) {
                            int sesionesPendientes = autorizacionesServiciosFacade.buscarServicioPorAutorizacion(autorizacionSeleccionada.getIdAutorizacion(), idServicio).getSesionesPendientes();
                            if (sesionesPendientes > listaTurnosSeleccionadosRespaldo.size()) {
                                totalTurnosSeleccionables += 1;
                            }
                        }
                    }
                }
            } else {
                imprimirMensaje("Error", "El paciente no tiene definida la administradora", FacesMessage.SEVERITY_ERROR);
                return;
            }
        } else {//si el servicio es diferente para cada turno
            TurnoServicio turnoServicio = buscarTurnoServicio(turno.getIdTurno());
            if (turnoServicio != null) {
                CtrlSesionesAutorizadas csa = determinarSesionesPendiente(facServicioFacade.buscarPorIdServicio(turnoServicio.getServicio()));
                if (csa.isAutorizacionRequerida()) {
                    csa.setContadorSesiones(csa.getContadorSesiones() - 1);
                }
            }
        }

        RequestContext.getCurrentInstance().update("idFormTurnosSeleccionados");
//        FacesMessage msg = new FacesMessage("Turno Unselected", turno.getIdTurno().toString());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void insertarServicio() {
        if (pacienteSeleccionado.getIdAdministradora() != null) {
            FacServicio servicio = facServicioFacade.buscarPorIdServicio(idServicio);
            if (servicio == null) {
                imprimirMensaje("Error", "Falta elegir el servicio", FacesMessage.SEVERITY_ERROR);
                return;
            }
            if (!listaServiciosSeleccionados.contains(servicio)) {//si el servicio no se ha elegido anteriormente se registra el control de las sesiones autorizadas para el servicio
                listaServiciosSeleccionados.add(servicio);
                CtrlSesionesAutorizadas ctrlSesionesAutorizadas;
                if (!pacienteSeleccionado.getIdAdministradora().getCodigoAdministradora().equals("1")) {
                    if (servicio.getAutorizacion() && autorizacionSeleccionada == null) {
                        imprimirMensaje("Error", "No tiene autorizacion para este servicio", FacesMessage.SEVERITY_ERROR);
                        return;
                    } else if (servicio.getAutorizacion() && autorizacionSeleccionada != null) {
                        if (totalTurnosSeleccionables == 0) {
                            imprimirMensaje("Error", "La autorizacion no permite crear otra cita para este servicio", FacesMessage.SEVERITY_ERROR);
                            return;
                        }
                    }
                    ctrlSesionesAutorizadas = new CtrlSesionesAutorizadas(idServicio, servicio.getAutorizacion(), totalTurnosSeleccionables);
                    ctrlSesionesAutorizadas.setContadorSesiones(1);
                } else {//los servicios de un paciente particular no requiere autorizacion. No se hace control de la cantidad de turnos seleccionables
                    ctrlSesionesAutorizadas = new CtrlSesionesAutorizadas(idServicio, false, -1);
                }
                listaControlSesionesAutorizadas.add(ctrlSesionesAutorizadas);
            } else {//el servicio seleccionada ya se ha insertado anteriormente. Se hace uso del control de sesiones autorizadas
                CtrlSesionesAutorizadas csa = determinarSesionesPendiente(servicio);
                if (csa.isAutorizacionRequerida()) {
                    if (csa.getContadorSesiones() < csa.getSesionesAutorizadas()) {
                        csa.setContadorSesiones(csa.getContadorSesiones() + 1);
                    } else {
                        imprimirMensaje("Error", "La autorizacion no permite crear otra cita para este servicio", FacesMessage.SEVERITY_ERROR);
                        return;
                    }
                }
            }
            turnoSeleccionado.setServicio(servicio.getNombreServicio());
            listaTurnoServicio.add(new TurnoServicio(idServicio, turnoSeleccionado.getIdTurno()));
            listaTurnosSeleccionado.add(turnoSeleccionado);
            listaTurnosSeleccionadosRespaldo.add(turnoSeleccionado);
            ordenarListaTurnos();
        } else {
            imprimirMensaje("Error", "El paciente no tiene asociada una administradora", FacesMessage.SEVERITY_ERROR);
        }
        RequestContext.getCurrentInstance().execute("PF('dlgServicio').hide()");
        RequestContext.getCurrentInstance().update("idFormTurnosSeleccionados");
    }

    public void quitarTurno(ActionEvent event) {
        CitTurnos t = (CitTurnos) event.getComponent().getAttributes().get("turno");
        listaTurnosSeleccionado.remove(t);
        getListaTurnosSeleccionadosRespaldo().remove(t);
        if (!isDiferenteServicio()) {
            if (pacienteSeleccionado.getIdAdministradora() != null) {
                //si el paciente no es particular. Se controla la cantidad de turnos seleccionables.
                if (!pacienteSeleccionado.getIdAdministradora().getCodigoAdministradora().equals("1")) {
                    if (facServicioFacade.buscarPorIdServicio(idServicio).getAutorizacion()) {//si el servicio requiere autorizacion
                        if (autorizacionrequerida && autorizacionSeleccionada != null) {
                            int sesionesPendientes = autorizacionesServiciosFacade.buscarServicioPorAutorizacion(autorizacionSeleccionada.getIdAutorizacion(), idServicio).getSesionesPendientes();
                            if (sesionesPendientes > listaTurnosSeleccionadosRespaldo.size()) {
                                totalTurnosSeleccionables += 1;
                            }
                        }
                    }
                }
            } else {
                imprimirMensaje("Error", "El paciente no tiene definida la administradora", FacesMessage.SEVERITY_ERROR);
                return;
            }
        } else {//si el servicio es diferente para cada turno
            TurnoServicio turnoServicio = buscarTurnoServicio(t.getIdTurno());
            if (turnoServicio != null) {
                CtrlSesionesAutorizadas csa = determinarSesionesPendiente(facServicioFacade.buscarPorIdServicio(turnoServicio.getServicio()));
                if (csa.isAutorizacionRequerida()) {
                    csa.setContadorSesiones(csa.getContadorSesiones() - 1);
                }
            }
        }
        RequestContext.getCurrentInstance().update("idFormTurnosSeleccionados");
    }

    private CtrlSesionesAutorizadas determinarSesionesPendiente(FacServicio servicio) {
        CtrlSesionesAutorizadas ctrlSesionesAutorizadas = null;
        for (CtrlSesionesAutorizadas csa : listaControlSesionesAutorizadas) {
            if (csa.getIdServicio() == servicio.getIdServicio()) {
                ctrlSesionesAutorizadas = csa;
                break;
            }
        }
        return ctrlSesionesAutorizadas;
    }

    private TurnoServicio buscarTurnoServicio(int idTurno) {
        for (TurnoServicio ts : listaTurnoServicio) {
            if (idTurno == ts.getTurno()) {
                return ts;
            }
        }
        return null;
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

//        if (getPrestadorSeleccionado() == null) {
//            imprimirMensaje("Error", "Necesita ingresar el prestador", FacesMessage.SEVERITY_ERROR);
//            return;
//        }
        if (listaTurnosSeleccionadosRespaldo.isEmpty()) {
            imprimirMensaje("Error", "Necesita elegir al menos un turno", FacesMessage.SEVERITY_ERROR);
            return;
        }
        int totalTurnosSeleccionados = listaTurnosSeleccionadosRespaldo.size();
        List<Integer> idTurnos = new ArrayList();
        for (CitTurnos t : listaTurnosSeleccionadosRespaldo) {
            idTurnos.add(t.getIdTurno());
        }
        int totalTurnosDisponibles = turnosFacade.totalTurnosDisponibles(idTurnos);
        if (totalTurnosDisponibles != totalTurnosSeleccionados) {
            listaTurnosSeleccionadosRespaldo.clear();
            listaControlSesionesAutorizadas.clear();
            listaTurnoServicio.clear();
            listaTurnosSeleccionado.clear();
            RequestContext.getCurrentInstance().update("idFormTurnosSeleccionados");
            imprimirMensaje("Error", "Almenos un turno de los seleccionados ya no esta disponible. Seleccione nuevamente los Turnos", FacesMessage.SEVERITY_ERROR);
            return;
        }

        //creando una cita nueva
        CitCitas nuevaCita;
        for (CitTurnos ct : listaTurnosSeleccionadosRespaldo) {
            if (ct.getEstado().equals("disponible")) {
                nuevaCita = new CitCitas();
                nuevaCita.setIdPaciente(pacienteSeleccionado);
                nuevaCita.setIdPrestador(ct.getIdPrestador());
                nuevaCita.setIdTurno(ct);
                if (motivoConsulta != 0) {
                    CfgClasificaciones clasificaciones;
                    clasificaciones = clasificacionesFachada.find(getMotivoConsulta());
                    nuevaCita.setTipoCita(clasificaciones);
                }
                nuevaCita.setFechaRegistro(new Date());
                Integer id_servicio;
                if (isDiferenteServicio()) {
                    TurnoServicio turnoServicio = buscarTurnoServicio(ct.getIdTurno());
                    id_servicio = turnoServicio.getServicio();
                    nuevaCita.setIdServicio(facServicioFacade.buscarPorIdServicio(id_servicio));
                    listaTurnoServicio.remove(turnoServicio);
                } else {
                    id_servicio = idServicio;
                    nuevaCita.setIdServicio(facServicioFacade.buscarPorIdServicio(id_servicio));
                }
                nuevaCita.setFacturada(false);
                nuevaCita.setAtendida(false);
                nuevaCita.setCancelada(false);
                nuevaCita.setMultado(false);
                nuevaCita.setIdPaquete(null);
                FacAdministradora facAdministradora = pacienteSeleccionado.getIdAdministradora();
                nuevaCita.setIdAdministradora(facAdministradora);

                if (!pacienteSeleccionado.getIdAdministradora().getCodigoAdministradora().equals("1")) {
                    CitAutorizaciones autorizacion = autorizacionesFacade.findAutorizacion(pacienteSeleccionado.getIdPaciente(), id_servicio, pacienteSeleccionado.getIdAdministradora().getIdAdministradora());
                    if (autorizacion != null) {
                        nuevaCita.setIdAutorizacion(autorizacion);
                        //se modifica las sesiones pendientes del servicio de la autorizacion implicada
                        CitAutorizacionesServicios autorizacionServicio = autorizacionesServiciosFacade.buscarServicioPorAutorizacion(autorizacion.getIdAutorizacion(), id_servicio);
                        if (autorizacionServicio != null) {//se actualizara los valores de la autorizacion.
                            autorizacionServicio.setSesionesPendientes(autorizacionServicio.getSesionesPendientes() - 1);
                            autorizacionesServiciosFacade.edit(autorizacionServicio);
                            nuevaCita.setIdAutorizacion(autorizacion);
                        }
                    } else {//el servicio no requiere autorizacion. La validacion se hace desde la vista: citasMasivasV2.xhtml
                        nuevaCita.setIdAutorizacion(null);
                    }
                } else {
                    nuevaCita.setIdAutorizacion(null);
                }
                nuevaCita.setTieneRegAsociado(false);
                citasFacade.create(nuevaCita);

                //modificando la tabla turnos: se incrementa el contador, dependiendo de la situacion el estado del turno puede llegar a ser false
                ct.setContador(ct.getContador() + 1);
                if (ct.getContador() == ct.getConcurrencia()) {
                    ct.setEstado("asignado");
                }
                turnosFacade.edit(ct);
                listaCitas.add(nuevaCita);
            }
        }
        //liberando Selecciones previas
        imprimirMensaje("Correto", "Las citas han sido creadas.", FacesMessage.SEVERITY_INFO);
        RequestContext.getCurrentInstance().execute("PF('dlgturnosselect').hide();");
        liberarCampos(1);
        loadTurnos();
        RequestContext.getCurrentInstance().update("result");
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('dlgresult').show();");
    }

//ban == 1 muestra informaciona adcional cuando se valida una autorizacion. Solo se usa cuando el servicio es igual para todos los servicios
    public void validarAutorizacion(int ban) {
        setRendBtnAutorizacion(false);
        totalTurnosSeleccionables = 0;
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
                        autorizacionrequerida = false;
                        autorizacionvalidada = false;
                        setRendBtnAutorizacion(false);
                        setAutorizacionSeleccionada(null);
                        return;
                    }
                    autorizacionrequerida = true;
                    CitAutorizaciones autorizacion = autorizacionesFacade.findAutorizacion(pacienteSeleccionado.getIdPaciente(), idServicio, pacienteSeleccionado.getIdAdministradora().getIdAdministradora());
                    if (autorizacion != null) {
                        autorizacionvalidada = true;
                        totalTurnosSeleccionables = autorizacionesServiciosFacade.buscarServicioPorAutorizacion(autorizacion.getIdAutorizacion(), idServicio).getSesionesPendientes();
                        setAutorizacionSeleccionada(autorizacion);
                        if (ban == 1) {
                            setAutorizacionServicioSeleccionado(autorizacionesServiciosFacade.buscarServicioPorAutorizacion(autorizacion.getIdAutorizacion(), idServicio));
                            RequestContext context = RequestContext.getCurrentInstance();
                            context.execute("PF('dlgresautorizacion').show();");
                        }
                    } else {
                        autorizacionvalidada = false;
                        autorizacion = autorizacionesFacade.findAutorizacionDos(pacienteSeleccionado.getIdPaciente(), idServicio, pacienteSeleccionado.getIdAdministradora().getIdAdministradora());
                        setAutorizacionSeleccionada(null);
                        setAutorizacionServicioSeleccionado(null);
//                        RequestContext context = RequestContext.getCurrentInstance();
//                        context.update("autorizar");
                        if (ban == 1 && autorizacion == null) {
                            setRendBtnAutorizacion(true);
                            imprimirMensaje("Error", "El servicio requiere autorizacion y el paciente no posee una autorizacion vigente", FacesMessage.SEVERITY_ERROR);
                        }
                        if (ban == 1 && autorizacion != null) {
                            setAutorizacionSeleccionada(autorizacion);
                            imprimirMensaje("Error", "La autorizacion asociada al servicio no admite crear otra cita", FacesMessage.SEVERITY_ERROR);
                        }
                    }
                }
            } else {
                autorizacionrequerida = false;
                autorizacionvalidada = false;
                setNombreServicio(null);
                setAutorizacionSeleccionada(null);
//                RequestContext context = RequestContext.getCurrentInstance();
//                context.update("autorizar");
            }

        } else {
            autorizacionvalidada = false;
            autorizacionrequerida = false;
            setNombreServicio(null);
            setAutorizacionSeleccionada(null);
//            RequestContext context = RequestContext.getCurrentInstance();
//            context.update("autorizar");
        }
        RequestContext.getCurrentInstance().update("formCita");
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
        autorizacion.setNumAutorizacion(numAutorizacion);
        LoginMB loginMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
        autorizacion.setIdUsuarioCreador(loginMB.getUsuarioActual());
        autorizacion.setCerrada(false);
        autorizacionesFacade.create(autorizacion);

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
            listaTurnosSeleccionado.clear();
            listaTurnosSeleccionadosRespaldo.clear();
            RequestContext.getCurrentInstance().update("idFormTurnosSeleccionados");
            setPacienteSeleccionado(null);
            setPrestadorSeleccionado(null);
            setIdentificacion("");
            setHayPacienteSeleccionado(false);
            setDisplayPaciente("none");
            setMotivoConsulta(0);
            setIdServicio(0);
        }
        //setIdPrestador(0);
        //idPaciente = 0;
        setNumAutorizacion("");
        setMotivoCancelacion(0);
        setDescripcionCancelacion(null);
        setAutorizacionSeleccionada(null);
        autorizacionvalidada = false;
        RequestContext.getCurrentInstance().update("tabprincipal:formDatosPaciente");
        RequestContext.getCurrentInstance().update("tabprincipal:formdisponibilidad");
        RequestContext.getCurrentInstance().update("tabprincipal:formCita");

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
            citaSeleccionada.setDescCancelacion(getDescripcionCancelacion());
            citaSeleccionada.setMotivoCancelacion(clasificaciones);
            citaSeleccionada.setFechaCancelacion(new Date());
            citasFacade.edit(citaSeleccionada);
            CitTurnos turno = citaSeleccionada.getIdTurno();
            turno.setEstado("disponible");
            turno.setContador(turno.getContador() - 1);
            turnosFacade.edit(turno);
            if (citaSeleccionada.getIdAutorizacion() != null) {
                CitAutorizaciones autorizacion = citaSeleccionada.getIdAutorizacion();
                CitAutorizacionesServicios autorizacionServicio = autorizacionesServiciosFacade.buscarServicioPorAutorizacion(autorizacion.getIdAutorizacion(), idServicio);
                if (autorizacionServicio != null) {
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

                //lista de turnos sin importar la sede
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
        imprimirMensaje("Correcto", "Disponibilidad registrada", FacesMessage.SEVERITY_INFO);
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

    public void onTabChange(TabChangeEvent event) {
        if (event.getTab().getTitle().equals("Prestadores")) {
            if (pacienteSeleccionado == null) {
                listaTurnosSeleccionado.clear();
                listaTurnosSeleccionadosRespaldo.clear();
                setRenderizarListaTurnos(false);
            } else {
                if (selectedNodes != null && selectedNodes.length > 0) {
                    loadTurnos();
                    setRenderizarListaTurnos(true);
                } else {
                    setRenderizarListaTurnos(false);
                }
            }
            RequestContext.getCurrentInstance().update("tabprincipal:formprestadores");
        }
    }

    public void displaySelectedMultiple(NodeSelectEvent event) {
        if (pacienteSeleccionado != null) {
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
        } else {
            imprimirMensaje("Error", "Elija el paciente", FacesMessage.SEVERITY_ERROR);
            setRenderizarListaTurnos(false);
        }
        RequestContext.getCurrentInstance().update("tabprincipal:formprestadores");
    }

    public void displayUnSelectedMultiple(NodeUnselectEvent event) {
        if (selectedNodes != null && selectedNodes.length > 0) {
            idsprestadores.clear();
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
        RequestContext.getCurrentInstance().update("tabprincipal:formprestadores");
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

    public List<CitTurnos> getListaTurnosSeleccionado() {
        return listaTurnosSeleccionado;
    }

    public void setListaTurnosSeleccionado(List<CitTurnos> listaTurnosSeleccionado) {
        this.listaTurnosSeleccionado = listaTurnosSeleccionado;
    }

    public List<CitTurnos> getListaTurnosSeleccionadosRespaldo() {
        return listaTurnosSeleccionadosRespaldo;
    }

    public void setListaTurnosSeleccionadosRespaldo(List<CitTurnos> listaTurnosSeleccionadosRespaldo) {
        this.listaTurnosSeleccionadosRespaldo = listaTurnosSeleccionadosRespaldo;
    }

    public CitAutorizacionesServicios getAutorizacionServicioSeleccionado() {
        return autorizacionServicioSeleccionado;
    }

    public void setAutorizacionServicioSeleccionado(CitAutorizacionesServicios autorizacionServicioSeleccionado) {
        this.autorizacionServicioSeleccionado = autorizacionServicioSeleccionado;
    }

    /**
     * @return the diferenteServicio
     */
    public boolean isDiferenteServicio() {
        return diferenteServicio;
    }

    /**
     * @param diferenteServicio the diferenteServicio to set
     */
    public void setDiferenteServicio(boolean diferenteServicio) {
        this.diferenteServicio = diferenteServicio;
    }

    /**
     * @return the displayServicio
     */
    public String getDisplayServicio() {
        return displayServicio;
    }

    /**
     * @param displayServicio the displayServicio to set
     */
    public void setDisplayServicio(String displayServicio) {
        this.displayServicio = displayServicio;
    }

    /**
     * @return the rendBtnAutorizacion
     */
    public boolean isRendBtnAutorizacion() {
        return rendBtnAutorizacion;
    }

    /**
     * @param rendBtnAutorizacion the rendBtnAutorizacion to set
     */
    public void setRendBtnAutorizacion(boolean rendBtnAutorizacion) {
        this.rendBtnAutorizacion = rendBtnAutorizacion;
    }

}
