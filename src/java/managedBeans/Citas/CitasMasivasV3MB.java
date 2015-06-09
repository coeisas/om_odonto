/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.Citas;

import beans.utilidades.AutorizacionReport;
import beans.utilidades.CtrlSesionesAutorizadas;
import beans.utilidades.IntervaloHoras;
import beans.utilidades.LazyPacienteDataModel;
import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.faces.model.SelectItem;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgPacientes;
import modelo.entidades.CitAutorizaciones;
import modelo.entidades.CitAutorizacionesServicios;
import modelo.entidades.CitCitas;
import modelo.entidades.CitPaqDetalle;
import modelo.entidades.CitPaqMaestro;
import modelo.entidades.CitTurnos;
import modelo.entidades.FacServicio;
import modelo.fachadas.CfgDiasNoLaboralesFacade;
import modelo.fachadas.CfgPacientesFacade;
import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.CitAutorizacionesFacade;
import modelo.fachadas.CitAutorizacionesServiciosFacade;
import modelo.fachadas.CitCitasFacade;
import modelo.fachadas.CitPaqDetalleFacade;
import modelo.fachadas.CitPaqMaestroFacade;
import modelo.fachadas.CitTurnosFacade;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Mario
 */
@ManagedBean(name = "citasMasivasV3MB")
@SessionScoped
public class CitasMasivasV3MB extends MetodosGenerales implements Serializable {

    //informacion paciente
    private String tipoIdentificacion;
    private String identificacion;
    private CfgPacientes pacienteSeleccionado;
    private String displayPaciente = "none";//muestra u oculta campos del paciente
    private boolean hayPacienteSeleccionado;
//    private List<SelectItem> listaServicios;
    private LazyDataModel<CfgPacientes> listaPacientes;
    private String nombrePaquete;
    
    private Date fechaInicial;
    private Date fechaFinal;
    private Date horaInicial;
    
    private List<SelectItem> listaPaquetes;
    private int idPaquete;
    
    private List<CitTurnos> listaTurnos;
    private List<CitCitas> listaCitas;
    private List<IntervaloHoras> listaItervaloOcupado;//lista que almacena el intervalo de tiempo en un dia que el paciente estara ocupado por razon de cita
    private List<AutorizacionReport> listadoAutorizado;
    private List<Integer> id_sedes;//lista utilizada para determinar los dias no laborales exclusivos para la actual sede, como para los comunes entre las sedes.
    private List<CtrlSesionesAutorizadas> listaCtrlSesionesAutorizadas;
    
    private int sede;
    private boolean rendComponente; //renderizar la tabla y el boton crear citas
    private boolean rendTablaResultado;
    private boolean permitidoCrearCitas;
    private boolean autorizacionRequerida;
    
    @EJB
    CfgPacientesFacade pacientesFacade;
    
    @EJB
    CitPaqMaestroFacade maestroFacade;
    
    @EJB
    CitPaqDetalleFacade detalleFacade;
    
    @EJB
    CitTurnosFacade turnosFacade;
    
    @EJB
    CfgUsuariosFacade prestadoresFacade;
    
    @EJB
    CitAutorizacionesFacade autorizacionesFacade;
    
    @EJB
    CitAutorizacionesServiciosFacade autorizacionesServiciosFacade;
    
    @EJB
    CitCitasFacade citasFacade;
    
    @EJB
    CfgDiasNoLaboralesFacade diasNoLaboralesFacade;
    
    public CitasMasivasV3MB() {
    }
    
    @PostConstruct
    private void init() {
        setListaPaquetes((List<SelectItem>) new ArrayList());
        setListaPacientes(new LazyPacienteDataModel(pacientesFacade));
        LoginMB loginMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
        sede = loginMB.getCentroDeAtencionactual().getIdSede();
        setRendComponente(false);
        listaTurnos = new ArrayList();
        listaItervaloOcupado = new ArrayList();
        listaCitas = new ArrayList();
        setRendTablaResultado(false);
        cargarPaquetes();
        id_sedes = new ArrayList();
        id_sedes.add(sede);
        id_sedes.add(0);//el valor 0 representa el id para todas las sedes
        //inicializando el listadoAutorizado
        listadoAutorizado = new ArrayList();
        listaCtrlSesionesAutorizadas = new ArrayList();
    }
//--------------------------------------------------------------------------------------    
//---------------------------------------METHODS----------------------------------------
//--------------------------------------------------------------------------------------

    public void cargarPaquetes() {
        listaPaquetes.clear();
        List<CitPaqMaestro> paqMaestros = maestroFacade.findAll();
        for (CitPaqMaestro paqMaestro : paqMaestros) {
            listaPaquetes.add(new SelectItem(paqMaestro.getIdPaqMaestro(), paqMaestro.getNomPaquete()));
        }
    }
    
    public void findPaciente() {
        setRendComponente(false);
        setRendTablaResultado(false);
        setIdPaquete(0);
        listaTurnos.clear();
        listaCitas.clear();
        if (getIdentificacion() != null) {
            if (!identificacion.isEmpty()) {
                pacienteSeleccionado = pacientesFacade.buscarPorIdentificacion(getIdentificacion());
                if (pacienteSeleccionado == null) {
                    imprimirMensaje("Error", "No se encontro el paciente", FacesMessage.SEVERITY_ERROR);
                    setHayPacienteSeleccionado(false);
                    setDisplayPaciente("none");
                } else {
                    pacienteSeleccionado.setEdad(calcularEdad(pacienteSeleccionado.getFechaNacimiento()));
                    setDisplayPaciente("block");
                    setPacienteSeleccionado(pacienteSeleccionado);
                    setHayPacienteSeleccionado(true);
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
    }
    
    public void actualizarPaciente() {
        listaTurnos.clear();
        listaCitas.clear();
        setIdPaquete(0);
        setRendComponente(false);
        setRendTablaResultado(false);
        if (pacienteSeleccionado != null) {
            setHayPacienteSeleccionado(true);
            setIdentificacion(pacienteSeleccionado.getIdentificacion());
            if (pacienteSeleccionado.getFechaNacimiento() != null) {
                pacienteSeleccionado.setEdad(calcularEdad(pacienteSeleccionado.getFechaNacimiento()));
            }
            setDisplayPaciente("block");
            setTipoIdentificacion(String.valueOf(pacienteSeleccionado.getTipoIdentificacion().getId()));
        } else {
            setHayPacienteSeleccionado(false);
            setDisplayPaciente("none");
        }
    }
    
    public void buscarDiponibilidad() throws ParseException {
        listaTurnos.clear();
        determinarAutorizacionParaPaquete(false);
        permitidoCrearCitas = false;
//        restablecerContadoresListaCtrl();
        if (pacienteSeleccionado.getIdAdministradora() == null) {
            RequestContext.getCurrentInstance().update("idFormDetallesCita");
            imprimirMensaje("Error", "El paciente no tiene asignado una Admnistradora y tampoco esta especificado como Particular", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (idPaquete == 0) {
            RequestContext.getCurrentInstance().update("idFormDetallesCita");
            imprimirMensaje("Error", "Elija algun paquete", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (!pacienteSeleccionado.getIdAdministradora().getCodigoAdministradora().equals("1")) {//la administradora con codigo 1 corresponde a particular por tanto no requiere autorizacion                                       
            determinarAutorizacionRequerida();
        } else {
            autorizacionRequerida = false;
        }
        if (autorizacionRequerida) {
            RequestContext.getCurrentInstance().update("idFormDetallesCita");
//            imprimirMensaje("Error", "Alguno de los servicios incluidos en el paquete requiren de autorizacion", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (fechaInicial == null) {
            RequestContext.getCurrentInstance().update("idFormDetallesCita");
            imprimirMensaje("Error", "Elija fecha inicial", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (pacienteSeleccionado.getIdAdministradora().getCodigoAdministradora().equals("1")) {//para paciente particular se limita las citas del paquete mediante la fecha final
            if (fechaFinal == null) {
                imprimirMensaje("Error", "Elija fecha final", FacesMessage.SEVERITY_ERROR);
                return;
            } else if (fechaFinal.before(fechaInicial)) {
                imprimirMensaje("Error", "verifique fecha final", FacesMessage.SEVERITY_ERROR);
                return;
            }
        }
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = new Date();
        Date fechaActual = formatter.parse(formatter.format(fecha));
        if (fechaInicial.before(fechaActual)) {
            imprimirMensaje("Error", "La fecha inicial es previa a la actual", FacesMessage.SEVERITY_ERROR);
            return;
        }
        boolean completo = false;//variable para salir del ciclo
        boolean ban = true;//bandera que es false cuando no es posible seleccionar turnos para todos los servicios del paquete
        boolean ban2 = true;//bandera para controlar que los turnos seleccionados esta dentro de las sesiones autorizadas
        List<CitPaqDetalle> citPaqDetalles = detalleFacade.buscarPorMaestro(idPaquete);
//        List<Date> fechas = interseccionDiasPaqueteConAgendaPrestador();
        CitPaqMaestro paqMaestroSeleccionado = maestroFacade.find(idPaquete);
        char[] aux = paqMaestroSeleccionado.getDias().toCharArray();
        List<Integer> diasSemana = new ArrayList();
        for (char dia : aux) {
            Integer d = Integer.parseInt(String.valueOf(dia));
            diasSemana.add(d);
        }
//        listaItervaloOcupado.clear();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaInicial);
        int totalServicios = listaCtrlSesionesAutorizadas.size();
        int contador = 0;
        while (completo == false) {//bucle que hace el recorrido entre fechas
            if (diasNoLaboralesFacade.FindDiaNoLaboral(id_sedes, calendar.getTime()) == null && diasSemana.contains(calendar.get(Calendar.DAY_OF_WEEK) - 1)) {//valida que la fecha sea un dia laboral para la sede y que el dia actual del recorrido este seleccionado en el paquete
                listaItervaloOcupado.clear();
                for (CitPaqDetalle detalle : citPaqDetalles) {//recorre entre los servicios que contiene el paquete elegido
                    CitTurnos turno;
                    Date horaCita;
                    if (listaItervaloOcupado.isEmpty()) {//si es la primera cita en el dia
                        if (horaInicial == null) {//si no se ha elegido la hora inicial, se elige el primer turno disponible que posea el prestador
                            Object[] hora = turnosFacade.determinarHorainicial(detalle.getIdPrestador().getIdUsuario(), calendar.getTime(), sede);
                            if (hora != null) {//se encontro hora para la primera cita
                                listaItervaloOcupado.add(new IntervaloHoras((Date) hora[0], (Date) hora[1]));
                                horaCita = (Date) hora[0];
                                turno = turnosFacade.buscarTurnoDisponiblePrestadorFecha(detalle.getIdPrestador().getIdUsuario(), calendar.getTime(), horaCita, sede);
                            } else {
                                turno = null;
                            }
                        } else {//se ha especificado la hora de las citas
                            turno = turnosFacade.buscarTurnoDisponiblePrestadorFecha(detalle.getIdPrestador().getIdUsuario(), calendar.getTime(), horaInicial, sede);
                            if (turno != null) {
                                listaItervaloOcupado.add(new IntervaloHoras(turno.getHoraIni(), turno.getHoraFin()));
                            }
                        }
                    } else {//no es la primera cita en el dia
                        turno = determinarCitaSiguiente(detalle.getIdPrestador().getIdUsuario(), calendar.getTime());
                    }
                    if (turno == null) {//no se econtro turno disponible
                        eliminarTurnosPorFecha(calendar.getTime());//elimina los turnos seleccionados  previamente donde la fecha corresponda a la del recorrido ya que debe poderse seleccionar turnos para todos los servicios del paquete.
                        if (turnosFacade.totalTurnosDisponiblesApartirFecha(detalle.getIdPrestador().getIdUsuario(), sede, calendar.getTime()) == 0) {//si el prestador actual no tiene turnos disponibles sale del ciclo
                            ban = false;
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", detalle.getIdPrestador().nombreCompleto().toUpperCase() + " no tiene agenda a partir de la fecha " + sdf.format(calendar.getTime())));
                        }
                        break;
                    } else {
//                    condicional que valida que los turnos seleccionados correspondan a las sesiones autorizadas si el paciente no es particular
                        if (pacienteSeleccionado.getIdAdministradora().getCodigoAdministradora().equals("1")) {
                            listaTurnos.add(turno);
                        } else {
                            if (validarAutorizacionTurnos(detalle.getIdServicio().getIdServicio(), calendar.getTime())) {
                                listaTurnos.add(turno);
                            } else {
                                ban = !listaTurnos.isEmpty();
                                ban2 = false;
                                break;
                            }
                        }
                    }
                }
                if (!ban) {
                    break;
                }
                if (!ban2) {
                    break;
                }
            }
//            recorre la lista de autorizaciones para cada servicio del paquete y verifica que el contador de sesiones sea igual a las sesiones autorizadas
            for (CtrlSesionesAutorizadas ctrlSesionAutorizada : listaCtrlSesionesAutorizadas) {
                if (ctrlSesionAutorizada.getContadorSesiones() == ctrlSesionAutorizada.getSesionesAutorizadas()) {
                    contador++;
                }
            }
//            si las sesiones autorizadas de los servicios se cumplen completamente. Se saldra del ciclo while
            if (!pacienteSeleccionado.getIdAdministradora().getCodigoAdministradora().equals("1")) {
                if (contador == totalServicios) {
                    completo = true;
                }
            }
            calendar.add(Calendar.DATE, 1);
            if (fechaFinal != null && calendar.getTime().after(fechaFinal)) {
                completo = true;
            }
        }
        if (listaTurnos.isEmpty()) {
            setRendComponente(false);
            
            listaTurnos.clear();
//            imprimirMensaje("Error", "Algun prestador no tiene agenda dentro ese periodo o no la cumple totalmente", FacesMessage.SEVERITY_ERROR);
        } else {
            permitidoCrearCitas = true;
            setRendComponente(true);
            RequestContext.getCurrentInstance().update("idFormDetallesCita");
        }
        
    }

//    valida que el total de turnos escogidos este dentro de las sesiones autorizadas (si el servicio requiere autorizacion) para cada servicio del paquete
    private boolean validarAutorizacionTurnos(int servicio, Date fecha) {
        boolean ban = false;
        //controla que el servicio que requiera autorizacion el paciente la tenga. Ademas controla que el numero total de turnos seleccionados para aquel servicio no supere a la sesiones autorizadas
        for (CtrlSesionesAutorizadas ctrlSesionAutorizada : listaCtrlSesionesAutorizadas) {
            if (ctrlSesionAutorizada.isAutorizacionRequerida()) {
                if (servicio == ctrlSesionAutorizada.getIdServicio()) {
                    if ((ctrlSesionAutorizada.getContadorSesiones() < ctrlSesionAutorizada.getSesionesAutorizadas())) {
                        ban = true;
                        ctrlSesionAutorizada.setContadorSesiones(ctrlSesionAutorizada.getContadorSesiones() + 1);
                    }
                    break;
                }
            } else {
                ban = true;
                break;
            }
        }
//        si la eleccion de un turno para un servicio supera a las sesiones autorizadas se eliminan los turnos para aquella fecha
        if (!ban) {
            eliminarTurnosPorFecha(fecha);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", "El total de sesiones autorizadas no permiten cumplir completamente con los servicios del paquete a partir de la fecha " + sdf.format(fecha)));
        }
        return ban;
    }
    
    private void eliminarTurnosPorFecha(Date fecha) {
        List<CitTurnos> listaAuxiliar = new ArrayList();
        for (CitTurnos turno : listaTurnos) {
            if (turno.getFecha().equals(fecha)) {
                listaAuxiliar.add(turno);
            }
        }
//        si se econtraron turnos se debe descontar el contador de sesiones en la listaCtrlSesionesAutorizadas de los servicios afectados
        if (!listaAuxiliar.isEmpty()) {
            int contador = 0;
            List<CitPaqDetalle> detallesPaquete = detalleFacade.buscarPorMaestro(idPaquete);
            for (CitPaqDetalle detalle : detallesPaquete) {
                contador++;
                for (CtrlSesionesAutorizadas csa : listaCtrlSesionesAutorizadas) {
                    if (csa.getIdServicio() == detalle.getIdServicio().getIdServicio()) {
                        csa.setContadorSesiones(csa.getContadorSesiones() - 1);
                        break;
                    }
                }
                if (contador == listaAuxiliar.size()) {
                    break;
                }
            }
        }
        listaTurnos.removeAll(listaAuxiliar);
    }

//    private void ordenarListaTurnos() {
//        Collections.sort(listaTurnos, new Comparator<CitTurnos>() {
//            @Override
//            public int compare(CitTurnos o1, CitTurnos o2) {
//                long time = o1.getFecha().getTime() + o1.getHoraIni().getTime();
//                Date aux = new Date(time);
//                time = o2.getFecha().getTime() + o2.getHoraIni().getTime();
//                Date aux2 = new Date(time);
//                return aux.compareTo(aux2);
//            }
//        });
//    }
//llena una lista con la informacion de los servicios del paquete y dado el caso de que el servicio requiera autorizacion y el paciente posea alguna se incluira a dicha lista
    private void llenarListaCtrlSesionesAutorizadas() {
        listaCtrlSesionesAutorizadas.clear();
        for (AutorizacionReport autorizacion : listadoAutorizado) {
            CtrlSesionesAutorizadas ctrlSesionesAutorizadas = new CtrlSesionesAutorizadas();
            ctrlSesionesAutorizadas.setIdServicio(autorizacion.getIdServicio());
            if (autorizacion.isRequiereAutorizacion()) {
                ctrlSesionesAutorizadas.setAutorizacionRequerida(true);
                ctrlSesionesAutorizadas.setSesionesAutorizadas(autorizacion.getSesionesAutorizadas());
                ctrlSesionesAutorizadas.setContadorSesiones(0);
            } else {
                ctrlSesionesAutorizadas.setAutorizacionRequerida(false);
            }
            listaCtrlSesionesAutorizadas.add(ctrlSesionesAutorizadas);
        }
    }

//determina la hora para la proxima cita. Respetando las citas previas
    private CitTurnos determinarCitaSiguiente(int idPrestador, Date fecha) {//determina cual sera la cita siguiente evitanto crear citas a la misma hora que las anteriores
        int itemsIntervalo = listaItervaloOcupado.size();
        Date hora = listaItervaloOcupado.get(itemsIntervalo - 1).getHoraFinal();
        CitTurnos turno = turnosFacade.determinarTurnoSiguiente(idPrestador, hora, fecha, sede);
        if (turno != null) {
            listaItervaloOcupado.add(new IntervaloHoras(turno.getHoraIni(), turno.getHoraFin()));
//                ordenarListaIntervaloOcupado();
        }
        return turno;
    }

//    private void ordenarListaIntervaloOcupado() {
//        Collections.sort(listaItervaloOcupado, new Comparator<IntervaloHoras>() {
//            @Override
//            public int compare(IntervaloHoras o1, IntervaloHoras o2) {
//                return o1.getHoraInicial().compareTo(o2.getHoraInicial());
//            }
//        });
//    }
    public void crearCitas() {
        listaCitas.clear();
        if (!pacienteSeleccionado.getIdAdministradora().getCodigoAdministradora().equals("1")) {//la administradora con codigo 1 corresponde a particular por tanto no requiere autorizacion                                       
            determinarAutorizacionRequerida();
        } else {
            autorizacionRequerida = false;
        }
        if (autorizacionRequerida) {
            imprimirMensaje("Error", "Alguno de los servicios incluidos en el paquete requiren de autorizacion", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (listaTurnos.isEmpty()) {
            imprimirMensaje("Error", "No hay Turnos Seleccionados", FacesMessage.SEVERITY_ERROR);
            return;
        } else {
            int totalTurnosSeleccionados = listaTurnos.size();
            List<Integer> idTurnos = new ArrayList();
            for (CitTurnos t : listaTurnos) {
                idTurnos.add(t.getIdTurno());
            }
            int totalTurnosDisponibles = turnosFacade.totalTurnosDisponibles(idTurnos);
            if (totalTurnosDisponibles != totalTurnosSeleccionados) {
                listaTurnos.clear();
                imprimirMensaje("Error", "Almenos un turno de los seleccionados ya no esta disponible. Seleccione nuevamente los Turnos", FacesMessage.SEVERITY_ERROR);
                return;
            }
        }
        boolean error = false;
        if (permitidoCrearCitas) {
            CitPaqMaestro paquete = maestroFacade.find(idPaquete);
//            descomentar si se maneja las veces que se aplica el paquete
//            if (paquete.getNoPaqAplicado() == null) {
//                paquete.setNoPaqAplicado(0);
//            }
//            paquete.setNoPaqAplicado(paquete.getNoPaqAplicado() + 1);
//            maestroFacade.edit(paquete);
//            Date fechaAnterior = listaTurnos.get(0).getFecha();//fecha anterior del recorrido
//            Date fechaActual;//fecha actual del recorrido            
            for (CitTurnos ct : listaTurnos) {//listaTurnos posee todos los turnos escogidos para crear las citas -> esta ordenada cronologicameente
                error = false;
                if (ct.getEstado().equals("disponible")) {
                    CitCitas cita = new CitCitas();
                    cita.setIdTurno(ct);
                    cita.setAtendida(false);
                    cita.setCancelada(false);
                    cita.setFacturada(false);
                    cita.setMultado(false);
                    cita.setTipoCita(null);
                    cita.setIdPaquete(paquete);
                    //aqui se incrementa el no_paq_aplicado en cit_paq_maestro: si la cita corresponde a una nueva fecha dentro de los turnos seleccionados
//                    fechaActual = ct.getFecha();
//                    if (fechaActual.after(fechaAnterior)) {
//                        paquete.setNoPaqAplicado(paquete.getNoPaqAplicado() + 1);
//                        maestroFacade.edit(paquete);
//                    }
//                    cita.setNoPaqAplicado(paquete.getNoPaqAplicado());
                    cita.setFechaRegistro(new Date());
                    cita.setIdPaciente(pacienteSeleccionado);
                    cita.setIdPrestador(ct.getIdPrestador());
                    cita.setIdAdministradora(pacienteSeleccionado.getIdAdministradora());
                    ct.setContador(ct.getContador() + 1);
                    if (ct.getConcurrencia() == ct.getContador()) {
                        ct.setEstado("asignado");
                    }
//                    turnosFacade.edit(ct);
                    FacServicio servicio = determinarServicio(ct.getIdPrestador().getIdUsuario());
                    cita.setIdServicio(servicio);
                    CitAutorizacionesServicios autorizacionServicio = null;
                    if (pacienteSeleccionado.getIdAdministradora().getCodigoAdministradora().equals("1")) {//el paciente particular no requiere autorizaciones
                        cita.setIdAutorizacion(null);
                    } else {
                        CitAutorizaciones autorizacion = determinarAutorizacionServicio(servicio.getIdServicio());//busca si existe autorizacion para el servicio
                        if (autorizacion != null) {//si se encontro autorizacion actualizar el estado del servicio afectado : decrementar sesiones pendiente
                            autorizacionServicio = autorizacionesServiciosFacade.buscarServicioPorAutorizacionDisponible(autorizacion.getIdAutorizacion(), servicio.getIdServicio());
                            if (autorizacionServicio != null) {
                                autorizacionServicio.setSesionesPendientes(autorizacionServicio.getSesionesPendientes() - 1);
//                                autorizacionesServiciosFacade.edit(autorizacionServicio);
                            }
                        } else {
                            error = true;
                            break;
                        }
                        cita.setIdAutorizacion(autorizacion);
                    }
//                    descomentar si se maneja las veces que aplica el paquete
//                    fechaAnterior = ct.getFecha();
                    if (!error) {
                        turnosFacade.edit(ct);
                        if (autorizacionServicio != null) {
                            autorizacionesServiciosFacade.edit(autorizacionServicio);
                        }
                        citasFacade.create(cita);
                        listaCitas.add(cita);
                    }
                }
            }
            listaTurnos.clear();
            listaCtrlSesionesAutorizadas.clear();
            setIdPaquete(0);
            setFechaFinal(null);
            setFechaInicial(null);
            setRendComponente(false);
            setRendTablaResultado(true);
            RequestContext.getCurrentInstance().update("idFormDetallesCita");
            RequestContext.getCurrentInstance().update("formResultCitas");
            RequestContext.getCurrentInstance().execute("PF('dlgCitas').show();");
            if (!error) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Citas Creadas"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informacion", "No tadas las citas se han Creado. Las sesiones autorizadas son insuficientes o nulas"));
            }
        }
    }
    
    private FacServicio determinarServicio(int idPrestador) {
        FacServicio servicio = null;
        List<CitPaqDetalle> citPaqDetalles = detalleFacade.buscarPorMaestro(idPaquete);
        for (CitPaqDetalle paqDetalle : citPaqDetalles) {
            if (paqDetalle.getIdPrestador().getIdUsuario() == idPrestador) {
                servicio = paqDetalle.getIdServicio();
                break;
            }
        }
        return servicio;
    }
    
    private CitAutorizaciones determinarAutorizacionServicio(int idServicio) {//busca si exite alguna autorizacion para el servicio enviado como parametro
        //lista de las autorizaciones no cerradas de un paciente
        return autorizacionesFacade.findAutorizacion(pacienteSeleccionado.getIdPaciente(), idServicio, pacienteSeleccionado.getIdAdministradora().getIdAdministradora());
    }
    
    public void determinarAutorizacionParaPaquete(boolean mostrarModal) {//busca si el paciente seleccionado requiere autorizaciones para los servicios del paquete

        listaCtrlSesionesAutorizadas.clear();
        if (mostrarModal) {//mostrarMmodal es true cuando se cambia de paquete desde la lista. Se eligiran nuevos turnos
            listaTurnos.clear();
            rendComponente = false;
        }
        listadoAutorizado.clear();
        RequestContext.getCurrentInstance().update("idTablaAutorizacion");
        RequestContext.getCurrentInstance().update("idFormDetallesCita");
        if (idPaquete != 0) {
            List<CitPaqDetalle> citPaqDetalles = detalleFacade.buscarPorMaestro(idPaquete);
            nombrePaquete = citPaqDetalles.get(0).getIdPaqMaestro().getNomPaquete();
            if (pacienteSeleccionado.getIdAdministradora() != null) {//el paciente debe pertener a alguna administradora
                if (!pacienteSeleccionado.getIdAdministradora().getCodigoAdministradora().equals("1")) {//la administradora con codigo 1 corresponde a particular por tanto no requiere autorizacion                                       
//                    llenarListaCtrlSesionesAutorizadas();
                    determinarAutorizacionRequerida();
                } else {
                    autorizacionRequerida = false;
                }
            } else {
                autorizacionRequerida = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El paciente no tiene administradora asociada"));
//            imprimirMensaje("Error", "El paciente no tiene administradora asociada", FacesMessage.SEVERITY_ERROR);
            }
            if (autorizacionRequerida) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Algunos servicios del paquete requieren autorizacion y el paciente no posee una"));
//            imprimirMensaje("Alerta", "Algunos servicios del paquete requieren autorizacion", FacesMessage.SEVERITY_WARN);
            } else {
                if (!listadoAutorizado.isEmpty()) {//si el listadoAutorizado no esta vacio, se mostrara el dialogo de autorizacion
                    llenarListaCtrlSesionesAutorizadas();
                    RequestContext.getCurrentInstance().update("idTablaAutorizacion");
                    if (mostrarModal) {
                        RequestContext.getCurrentInstance().execute("PF('dlgEstadoAutorizacion').show()");
                    }
                }
            }
        }
    }
    
    private void determinarAutorizacionRequerida() {
        listadoAutorizado.clear();
        List<CitPaqDetalle> citPaqDetalles = detalleFacade.buscarPorMaestro(idPaquete);
        for (CitPaqDetalle detalle : citPaqDetalles) {
            //la variable autorizacionRequerida sera true cuando el servicio requiere de una autorzacion y no posee una, de caso contrario sera false
            autorizacionRequerida = detalle.getIdServicio().getAutorizacion();
            if (detalle.getIdServicio().getAutorizacion()) {//se tiene en cuenta unicamente los servicios que requieren autorizacion
                //se busca si el paciente tiene autorizacion para el servicio actual del paquete
                AutorizacionReport autorizacionReport = new AutorizacionReport();
                CitAutorizaciones autorizacion = autorizacionesFacade.findAutorizacionDisponible(pacienteSeleccionado.getIdPaciente(), detalle.getIdServicio().getIdServicio(), pacienteSeleccionado.getIdAdministradora().getIdAdministradora());
                if (autorizacion != null) {
                    //la variable cambia a false ya que se encontro una autorizacion
                    autorizacionRequerida = false;
                    for (CitAutorizacionesServicios cas : autorizacion.getCitAutorizacionesServiciosList()) {
                        if (cas.getFacServicio().equals(detalle.getIdServicio())) {
                            autorizacionReport.setNumAutorizacion(autorizacion.getNumAutorizacion());
                            autorizacionReport.setServicio(cas.getFacServicio().getNombreServicio());
                            autorizacionReport.setIdServicio(cas.getFacServicio().getIdServicio());
                            autorizacionReport.setRequiereAutorizacion(cas.getFacServicio().getAutorizacion());
                            autorizacionReport.setSesionesAutorizadas(cas.getSesionesAutorizadas());
                            autorizacionReport.setSesionesPendientes(cas.getSesionesPendientes());
                            autorizacionReport.setSesionesRealizadas(cas.getSesionesRealizadas());
                            if (!validarListadoAutorizado(autorizacionReport.getServicio())) {
                                listadoAutorizado.add(autorizacionReport);
                            }
                            break;
                        }
                    }
//                                break;
                } else {
                    break;
                }
            }
            if (autorizacionRequerida) {
                break;
            }
        }
        RequestContext.getCurrentInstance().update("idTablaAutorizacion");
    }
    
    private boolean validarListadoAutorizado(String servicio) {
        boolean ban = false;
        for (AutorizacionReport autorizacionReport : listadoAutorizado) {
            if (autorizacionReport.getServicio().equals(servicio)) {
                ban = true;
                break;
            }
        }
        return ban;
        
    }
    
    public void verAutorizacion() {
        if (idPaquete != 0) {
            determinarAutorizacionParaPaquete(false);
            if (!listadoAutorizado.isEmpty()) {
                RequestContext.getCurrentInstance().execute("PF('dlgEstadoAutorizacion').show()");
            }
        } else {
            imprimirMensaje("Informacion", "Es necesario elegir un paquete", FacesMessage.SEVERITY_WARN);
        }
    }

//--------------------------------------------------------------------------------------------------------
//------------------------------------GETTERS AND SETTERS-------------------------------------------------
//--------------------------------------------------------------------------------------------------------    
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
    
    public LazyDataModel<CfgPacientes> getListaPacientes() {
        return listaPacientes;
    }
    
    public void setListaPacientes(LazyDataModel<CfgPacientes> listaPacientes) {
        this.listaPacientes = listaPacientes;
    }
    
    public boolean isHayPacienteSeleccionado() {
        return hayPacienteSeleccionado;
    }
    
    public void setHayPacienteSeleccionado(boolean hayPacienteSeleccionado) {
        this.hayPacienteSeleccionado = hayPacienteSeleccionado;
    }
    
    public String getDisplayPaciente() {
        return displayPaciente;
    }
    
    public void setDisplayPaciente(String displayPaciente) {
        this.displayPaciente = displayPaciente;
    }
    
    public Date getFechaInicial() {
        return fechaInicial;
    }
    
    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }
    
    public Date getFechaFinal() {
        return fechaFinal;
    }
    
    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
    
    public List<SelectItem> getListaPaquetes() {
        return listaPaquetes;
    }
    
    public void setListaPaquetes(List<SelectItem> listaPaquetes) {
        this.listaPaquetes = listaPaquetes;
    }
    
    public int getIdPaquete() {
        return idPaquete;
    }
    
    public void setIdPaquete(int idPaquete) {
        this.idPaquete = idPaquete;
    }
    
    public List<CitTurnos> getListaTurnos() {
        return listaTurnos;
    }
    
    public void setListaTurnos(List<CitTurnos> listaTurnos) {
        this.listaTurnos = listaTurnos;
    }
    
    public boolean isRendComponente() {
        return rendComponente;
    }
    
    public void setRendComponente(boolean rendComponente) {
        this.rendComponente = rendComponente;
    }
    
    public List<CitCitas> getListaCitas() {
        return listaCitas;
    }
    
    public void setListaCitas(List<CitCitas> listaCitas) {
        this.listaCitas = listaCitas;
    }
    
    public boolean isRendTablaResultado() {
        return rendTablaResultado;
    }
    
    public void setRendTablaResultado(boolean rendTablaResultado) {
        this.rendTablaResultado = rendTablaResultado;
    }
    
    public String getNombrePaquete() {
        return nombrePaquete;
    }
    
    public Date getHoraInicial() {
        return horaInicial;
    }
    
    public void setHoraInicial(Date horaInicial) {
        this.horaInicial = horaInicial;
    }
    
    public List<AutorizacionReport> getListadoAutorizado() {
        return listadoAutorizado;
    }
    
    public void setListadoAutorizado(List<AutorizacionReport> listadoAutorizado) {
        this.listadoAutorizado = listadoAutorizado;
    }
    
}
