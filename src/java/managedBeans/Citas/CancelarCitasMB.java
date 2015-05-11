/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.Citas;

import beans.utilidades.LazyCitasDataModel;
import beans.utilidades.LazyPacienteDataModel;
import beans.utilidades.LazyPrestadorDataModel;
import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.CfgPacientes;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.CitAutorizaciones;
import modelo.entidades.CitAutorizacionesServicios;
import modelo.entidades.CitCitas;
import modelo.entidades.CitTurnos;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgPacientesFacade;
import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.CitAutorizacionesFacade;
import modelo.fachadas.CitAutorizacionesServiciosFacade;
import modelo.fachadas.CitCitasFacade;
import modelo.fachadas.CitTurnosFacade;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Mario
 */
@ManagedBean(name = "cancelarCitasMB")
@SessionScoped
public class CancelarCitasMB extends MetodosGenerales implements Serializable {

    /**
     * Creates a new instance of CancelarCitasMB
     */
    private String nombreCompleto;
    private String opc;
    private int motivoCancelacion;
    private String descripcionCancelacion;
    private LazyDataModel<CitCitas> listaCitas;

    private CfgUsuarios prestadorSeleccionado;
    private CfgPacientes pacienteSeleccionado;
    private CitCitas citaSeleccionada;

    private LazyDataModel<CfgUsuarios> listaPrestadores;
    private LazyDataModel<CfgPacientes> listaPacientes;
    private List<SelectItem> listaEspecialidades;

    @EJB
    CitCitasFacade citasFacade;

    @EJB
    CfgPacientesFacade pacientesFachada;

    @EJB
    CfgUsuariosFacade usuariosFachada;

    @EJB
    CitTurnosFacade turnosFacade;

    @EJB
    CitAutorizacionesFacade autorizacionesFacade;

    @EJB
    CitAutorizacionesServiciosFacade autorizacionesServiciosFacade;

    @EJB
    CfgClasificacionesFacade clasificacionesFachada;

    public CancelarCitasMB() {
    }

    @PostConstruct
    private void inicializar() {
        //-----------
        setListaPacientes(new LazyPacienteDataModel(pacientesFachada));
        setListaPrestadores(new LazyPrestadorDataModel(usuariosFachada));
        //setListaPrestadores(prestadoresFachada.findAll());
        cargarEspecialidadesPrestadores();
        setListaCitas(new LazyCitasDataModel(citasFacade, "nulo", 0));
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

    private String obtenerNombreCompleto() {
        setNombreCompleto("");
        if (getPrestadorSeleccionado().getPrimerNombre() != null) {
            setNombreCompleto(getNombreCompleto() + getPrestadorSeleccionado().getPrimerNombre());
        }
        if (getPrestadorSeleccionado().getSegundoNombre() != null) {
            setNombreCompleto(getNombreCompleto() + " " + getPrestadorSeleccionado().getSegundoNombre());
        }
        if (getPrestadorSeleccionado().getPrimerApellido() != null) {
            setNombreCompleto(getNombreCompleto() + " " + getPrestadorSeleccionado().getPrimerApellido());
        }
        if (getPrestadorSeleccionado().getSegundoApellido() != null) {
            setNombreCompleto(getNombreCompleto() + " " + getPrestadorSeleccionado().getSegundoApellido());
        }
        return getNombreCompleto();
    }

    ///candelacion de citas
    public void buscarCita(ActionEvent actionEvent) {
        int id = (int) actionEvent.getComponent().getAttributes().get("id_cita");
        citaSeleccionada = citasFacade.findCitaById(id);
    }

    public void opcionCancelacion(ActionEvent actionEvent) {
        opc = (String) actionEvent.getComponent().getAttributes().get("opcion");
    }

    public void cargarListaCitas() {
        switch (getOpc()) {
            case "paciente":
                setPrestadorSeleccionado(null);
                LazyDataModel<CitCitas>  lista = new LazyCitasDataModel(citasFacade, opc, getPacienteSeleccionado().getIdPaciente());
                setListaCitas(lista);
                break;
            case "prestador":
                setPacienteSeleccionado(null);
                setListaCitas(new LazyCitasDataModel(citasFacade, opc, getPrestadorSeleccionado().getIdUsuario()));
                break;
        }
        RequestContext.getCurrentInstance().update("formTablaCitas");
    }

    public void cancelarCita(ActionEvent actionEvent) {
        //System.out.println(motivoCancelacion + " - " + descripcionCancelacion);
        if (!citaSeleccionada.getCancelada() && !citaSeleccionada.getAtendida()) {
            citaSeleccionada.setCancelada(true);
            CfgClasificaciones clasificaciones = clasificacionesFachada.find(motivoCancelacion);
            citaSeleccionada.setDescCancelacion(descripcionCancelacion);
            citaSeleccionada.setMotivoCancelacion(clasificaciones);
            citaSeleccionada.setFechaCancelacion(new Date());
            citasFacade.edit(citaSeleccionada);
            CitTurnos turno = citaSeleccionada.getIdTurno();
            turno.setEstado("disponible");
            turno.setContador(turno.getContador() - 1);
            turnosFacade.edit(turno);
            if (citaSeleccionada.getIdAutorizacion() != null) {
                CitAutorizaciones autorizacion = citaSeleccionada.getIdAutorizacion();
                CitAutorizacionesServicios cas = autorizacionesServiciosFacade.buscarServicioPorAutorizacion(autorizacion.getIdAutorizacion(), citaSeleccionada.getIdServicio().getIdServicio());
                if (cas != null) {
                    cas.setSesionesPendientes(cas.getSesionesPendientes() + 1);
                    autorizacionesServiciosFacade.edit(cas);
                }
            }
            imprimirMensaje("Correcto", "Cita " + citaSeleccionada.getIdCita() + " cancelada", FacesMessage.SEVERITY_INFO);
            //actualiza los items de listaCitas
//            listaCitas.remove(citaSeleccionada);
            setListaCitas(listaCitas);
            //cargarCitas();
        } else {
            imprimirMensaje("Error", "Cita " + citaSeleccionada.getIdCita() + " ya se encuentra atendida o cancelada", FacesMessage.SEVERITY_ERROR);
        }
        descripcionCancelacion = null;

    }

//GETTERS AND SETTERS
    public String getOpc() {
        return opc;
    }

    public void setOpc(String opc) {
        this.opc = opc;
    }

    public LazyDataModel<CitCitas> getListaCitas() {
        return listaCitas;
    }

    public void setListaCitas(LazyDataModel<CitCitas> listaCitas) {
        this.listaCitas = listaCitas;
    }

    public CfgPacientes getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }

    public void setPacienteSeleccionado(CfgPacientes pacienteSeleccionado) {
        this.pacienteSeleccionado = pacienteSeleccionado;
    }

    public CfgUsuarios getPrestadorSeleccionado() {
        return prestadorSeleccionado;
    }

    public void setPrestadorSeleccionado(CfgUsuarios prestadorSeleccionado) {
        this.prestadorSeleccionado = prestadorSeleccionado;
    }

    public LazyDataModel<CfgUsuarios> getListaPrestadores() {
        return listaPrestadores;
    }

    public void setListaPrestadores(LazyDataModel<CfgUsuarios> listaPrestadores) {
        this.listaPrestadores = listaPrestadores;
    }

    public LazyDataModel<CfgPacientes> getListaPacientes() {
        return listaPacientes;
    }

    public void setListaPacientes(LazyDataModel<CfgPacientes> listaPacientes) {
        this.listaPacientes = listaPacientes;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public List<SelectItem> getListaEspecialidades() {
        return listaEspecialidades;
    }

    public void setListaEspecialidades(List<SelectItem> listaEspecialidades) {
        this.listaEspecialidades = listaEspecialidades;
    }

    public int getMotivoCancelacion() {
        return motivoCancelacion;
    }

    public void setMotivoCancelacion(int motivoCancelacion) {
        this.motivoCancelacion = motivoCancelacion;
    }

    public CitCitas getCitaSeleccionada() {
        return citaSeleccionada;
    }

    public void setCitaSeleccionada(CitCitas citaSeleccionada) {
        this.citaSeleccionada = citaSeleccionada;
    }

    public String getDescripcionCancelacion() {
        return descripcionCancelacion;
    }

    public void setDescripcionCancelacion(String descripcionCancelacion) {
        this.descripcionCancelacion = descripcionCancelacion;
    }

}
