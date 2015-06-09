/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.Citas;

import beans.utilidades.LazyAgendaModel;
import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.CitAutorizaciones;
import modelo.entidades.CitAutorizacionesServicios;
import modelo.entidades.CitCitas;
import modelo.entidades.CitTurnos;
import modelo.fachadas.CitAutorizacionesFacade;
import modelo.fachadas.CitAutorizacionesServiciosFacade;
import modelo.fachadas.CitCitasFacade;
import modelo.fachadas.CitTurnosFacade;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;

/**
 *
 * @author mario
 */
@ManagedBean(name = "agendaPrestadorMB")
@SessionScoped
public class AgendaPrestadorMB extends MetodosGenerales implements Serializable {

    /**
     * Creates a new instance of AgendaPrestadorMB
     */
    private int sede;
    private LazyAgendaModel evenModel;
    private ScheduleEvent event = new DefaultScheduleEvent();
    private String nombreCompleto;
    private boolean rend = false;
    private boolean haySesionPrestador;
    private boolean rendBtnHistoriClinica = false;
    private boolean rendBtnAtendida = false;
    private boolean rendAgenda;
    private String idTurno;
    private String minTime;
    private String maxTime;

    private CfgUsuarios prestadorActual;
    private CitCitas citCita;
    private CitTurnos citTurnos;
    @EJB
    CitCitasFacade citasFacade;

    @EJB
    CitTurnosFacade turnosfacade;

    @EJB
    CitAutorizacionesFacade autorizacionesFacade;

    @EJB
    CitAutorizacionesServiciosFacade autorizacionesServiciosFacade;

    @ManagedProperty(value = "#{loginMB}")
    private LoginMB loginMB;

    //@ManagedProperty(value = "#{historiasMB}")
    //private HistoriasMB historiasMB;
    public AgendaPrestadorMB() {
    }

    @PostConstruct
    private void inicialize() {
        setRendAgenda(false);
        if (getLoginMB().getUsuarioActual().getTipoUsuario().getCodigo().compareTo("2") == 0) {
            setPrestadorActual(getLoginMB().getUsuarioActual());
            sede = getLoginMB().getCentroDeAtencionactual().getIdSede();
            setHaySesionPrestador(true);
        } else {
            setPrestadorActual(null);
            setHaySesionPrestador(false);
            imprimirMensaje("Error", "No eres prestador para ver el contenido", FacesMessage.SEVERITY_ERROR);
        }
        if (prestadorActual != null) {
            obtenerNombreCompleto();
//            loadEvents();

        } else {
            evenModel = null;
        }
    }

    public void loadEvents() {
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
            evenModel = new LazyAgendaModel(prestadorActual.getIdUsuario(), sede, turnosfacade, citasFacade, "agendaMedico");
            setRendAgenda(true);
        } else {
            evenModel = null;
            setRendAgenda(false);
            if (actualizarDesdeHistorias) {
                imprimirMensaje("Informacion", "No tiene agenda para esta sede", FacesMessage.SEVERITY_WARN);
            }
        }
    }

    private String establerLimitesAgenda(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("ha");
        return format.format(date);
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
                RequestContext.getCurrentInstance().execute("PF('eventDialog').show()");
            }
        }
    }

    private void seleccionarCita(long id) {
        CitCitas cita = citasFacade.findCitasByTurno(id);
        setCitCita(cita);
        if (cita != null) {
            rend = true;
            if (cita.getAtendida()) {
                setRendBtnHistoriClinica(true);
                setRendBtnAtendida(false);
            } else if (cita.getIdTurno().getEstado().equals("en_espera")) {
                setRendBtnAtendida(true);
                setRendBtnHistoriClinica(true);
            } else {
                setRendBtnHistoriClinica(false);
                setRendBtnAtendida(false);
            }
        } else {
            rend = false;
            setRendBtnHistoriClinica(false);
            setRendBtnAtendida(false);
        }
        RequestContext.getCurrentInstance().update("formAgendaPrestador:pdialog");

    }

    public void openHistoriaClinica() {
        RequestContext.getCurrentInstance().execute("window.parent.cargarTab('Historias Clinicas','historias/historias.xhtml','idCita;" + citCita.getIdCita().toString() + "')");
    }

    boolean actualizarDesdeHistorias;

    public void actualizarAutorizaciones(CitCitas cita) {
        actualizarDesdeHistorias = true;
        //si el servicio requiere autorizacion y la administradora es diferente a particular
        if (cita.getIdServicio().getAutorizacion() && !cita.getIdAdministradora().getCodigoAdministradora().equals("1")) {
            if (cita.getIdAutorizacion() == null) {
                //si se muestra este mensaje problemas en recepcion, ya que hay se hace el control definitivo antes de pasar al consultorio
                cita.setAtendida(false);
                citasFacade.edit(cita);
                imprimirMensaje("Error", "la cita requiere autorizacion", FacesMessage.SEVERITY_ERROR);
                return;
            }
            CitAutorizaciones autorizaciones = cita.getIdAutorizacion();
            int aux = 0;
            int cantidadServiciosAutorizacion = autorizaciones.getCitAutorizacionesServiciosList().size();
            for (CitAutorizacionesServicios autorizacionServicio : autorizaciones.getCitAutorizacionesServiciosList()) {
                if (autorizacionServicio.getFacServicio() == cita.getIdServicio()) {
                    autorizacionServicio.setSesionesRealizadas(autorizacionServicio.getSesionesRealizadas() + 1);
                    autorizacionesServiciosFacade.edit(autorizacionServicio);
                }
                if (Objects.equals(autorizacionServicio.getSesionesAutorizadas(), autorizacionServicio.getSesionesRealizadas())) {
                    aux++;
                }
            }
            if (aux == cantidadServiciosAutorizacion) {
                autorizaciones.setCerrada(true);
                autorizacionesFacade.edit(autorizaciones);
            }
        }
        citasFacade.edit(cita);
        CitTurnos ct = cita.getIdTurno();
        ct.setEstado("atendido");
        turnosfacade.edit(ct);
        loadEvents();

        RequestContext.getCurrentInstance().update("formAgendaPrestador:agenda");
        actualizarDesdeHistorias = false;
//            RequestContext.getCurrentInstance().execute("PF('eventDialog').hide()");
//            imprimirMensaje("Correcto", "Cita " + citCita.getIdCita() + " Atendida", FacesMessage.SEVERITY_INFO);
    }

//-----------------------------------------------------------------------------------------------------
//---------------------------------------GETTERS AND SETTERS-------------------------------------------
//-----------------------------------------------------------------------------------------------------        
    public LazyAgendaModel getEvenModel() {
        return evenModel;
    }

    public void setEvenModel(LazyAgendaModel evenModel) {
        this.evenModel = evenModel;
    }

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

    public LoginMB getLoginMB() {
        return loginMB;
    }

    public void setLoginMB(LoginMB loginMB) {
        this.loginMB = loginMB;
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

    //public HistoriasMB getHistoriasMB() {
    //    return historiasMB;
    //}
    //public void setHistoriasMB(HistoriasMB historiasMB) {
    //    this.historiasMB = historiasMB;
    //}
    public boolean isRendBtnAtendida() {
        return rendBtnAtendida;
    }

    public void setRendBtnAtendida(boolean rendBtnAtendida) {
        this.rendBtnAtendida = rendBtnAtendida;
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

    public boolean isRendAgenda() {
        return rendAgenda;
    }

    public void setRendAgenda(boolean rendAgenda) {
        this.rendAgenda = rendAgenda;
    }

    public boolean isRendBtnHistoriClinica() {
        return rendBtnHistoriClinica;
    }

    public void setRendBtnHistoriClinica(boolean rendBtnHistoriClinica) {
        this.rendBtnHistoriClinica = rendBtnHistoriClinica;
    }

    public String getIdTurno() {
        return idTurno;
    }

}
