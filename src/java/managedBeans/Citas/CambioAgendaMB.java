/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.Citas;

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
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.CitCitas;
import modelo.entidades.CitTurnos;

import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.CitCitasFacade;
import modelo.fachadas.CitTurnosFacade;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author mario
 */
@ManagedBean(name = "cambioAgendaMB")
@SessionScoped
public class CambioAgendaMB extends MetodosGenerales implements Serializable {

    private String id_prestadorOrigen;
    private String id_prestadorDestino;

    private LazyDataModel<CfgUsuarios> listaPrestadores;
    private CfgUsuarios prestadorOrigenSeleccionado;
    private CfgUsuarios prestadorDestinoSeleccionado;

    private Date fechaInicial;
    private Date horaInicial;
    private Date fechaFinal;
    private Date horaFinal;

    private boolean validado;

    private List<CitCitas> listaCitasOrigen;
    private List<CitCitas> listaCitasDestino;
    private List<CitTurnos> listaTurnos;
    private List<SelectItem> listaEspecialidades;

    private int sede;

    @EJB
    CfgUsuariosFacade usuariosFachada;

    @EJB
    CitTurnosFacade citTurnosFacade;

    @EJB
    CitCitasFacade citCitasFacade;

    /**
     * Creates a new instance of CambioAgendaMB
     */
    public CambioAgendaMB() {
    }

    @PostConstruct
    public void init() {

        setListaPrestadores(new LazyPrestadorDataModel(usuariosFachada));
        //setListaPrestadores(prestadoresFachada.findAll());
        cargarEspecialidadesPrestadores();
        LoginMB loginMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
        sede = loginMB.getCentroDeAtencionactual().getIdSede();
    }

    private void cargarEspecialidadesPrestadores() {
        setListaEspecialidades((List<SelectItem>) new ArrayList());
        List<CfgClasificaciones> lista = usuariosFachada.findEspecialidades();
        for (CfgClasificaciones especialidad : lista) {
            if (especialidad != null) {
                getListaEspecialidades().add(new SelectItem(especialidad.getId(), especialidad.getDescripcion()));
            }
        }
    }

    public void findprestador(int ban) {
        CfgUsuarios prestadorSeleccionado;
        String id_prestador;
        if (ban == 0) {
            id_prestador = getId_prestadorOrigen();
        } else {
            id_prestador = getId_prestadorDestino();
        }
        if (!id_prestador.isEmpty()) {
            try {
                prestadorSeleccionado = usuariosFachada.buscarPorIdentificacion(id_prestador);
                if (prestadorSeleccionado == null) {
                    if (ban == 0) {
                        setPrestadorOrigenSeleccionado(null);
                    } else {
                        setPrestadorDestinoSeleccionado(null);
                    }
                    imprimirMensaje("Error", "No se encontro el prestador", FacesMessage.SEVERITY_ERROR);
                } else {
                    if (ban == 0) {
                        setPrestadorOrigenSeleccionado(prestadorSeleccionado);
                    } else {
                        setPrestadorDestinoSeleccionado(prestadorSeleccionado);
                    }
                }
            } catch (Exception e) {
                imprimirMensaje("Error", "Ingrese un codigo de prestador valido", FacesMessage.SEVERITY_ERROR);
            }
        } else {
            if (ban == 0) {
                setPrestadorOrigenSeleccionado(null);
            } else {
                setPrestadorDestinoSeleccionado(null);
            }
        }
    }

    public void actualizarPrestador(int ban) {
        CfgUsuarios prestadorSeleccionado;
        if (ban == 0) {
            prestadorSeleccionado = getPrestadorOrigenSeleccionado();
        } else {
            prestadorSeleccionado = getPrestadorDestinoSeleccionado();
        }
        if (prestadorSeleccionado != null) {
            if (ban == 0) {
                setId_prestadorOrigen(String.valueOf(prestadorSeleccionado.getIdentificacion()));
            } else {
                setId_prestadorDestino(String.valueOf(prestadorSeleccionado.getIdentificacion()));
            }
        }
    }

    //----------------------------------------------------------------------------------------
    //--------------------------------------METODOS CAMBIAR AGENDA----------------------------
    //----------------------------------------------------------------------------------------
    public void validar(ActionEvent actionEvent) {
        if (getPrestadorOrigenSeleccionado() == null) {
            validado = false;
            imprimirMensaje("Error", "Es necesario que seleccione almenos el prestador de origen", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (fechaInicial == null || fechaFinal == null) {
            validado = false;
            imprimirMensaje("Error", "Es necesario que especifique la fecha inicial y final", FacesMessage.SEVERITY_ERROR);
            return;
        }
        //horaInicial y horaFinal no son obligatorias, pero cuando se entren, deben estar los dos
        if (horaInicial == null && horaFinal != null) {
            validado = false;
            imprimirMensaje("Error", "Es necesario que entre la hora inicial", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (horaInicial != null && horaFinal == null) {
            validado = false;
            imprimirMensaje("Error", "Es necesario que entre la hora final", FacesMessage.SEVERITY_ERROR);
            return;
        }
        validado = true;
    }

    public void cargarCitas() {
        if (validado) {
            setListaCitasOrigen(citCitasFacade.findCitasByPrestadorParam(getPrestadorOrigenSeleccionado().getIdUsuario(), fechaInicial, horaInicial, fechaFinal, horaFinal));
            if (getPrestadorDestinoSeleccionado() != null) {
                listaCitasDestino = citCitasFacade.findNextCitasByPrestadorParam(getPrestadorDestinoSeleccionado().getIdUsuario(), fechaInicial, 2);
                listaTurnos = citTurnosFacade.buscarPorPrestadorParam(getPrestadorDestinoSeleccionado().getIdUsuario(), fechaInicial, listaCitasOrigen.size(), 2);
            } else {
                listaCitasDestino = citCitasFacade.findNextCitasByPrestadorParam(getPrestadorOrigenSeleccionado().getIdUsuario(), fechaFinal, 1);
                listaTurnos = citTurnosFacade.buscarPorPrestadorParam(getPrestadorOrigenSeleccionado().getIdUsuario(), fechaFinal, listaCitasOrigen.size(), 1);
            }

        } else {
            setListaCitasOrigen(null);
            setListaCitasDestino(null);
        }

    }

    public void cambiarAgenda() {

        if (listaCitasOrigen != null && listaTurnos != null) {
            if (listaCitasOrigen.isEmpty()) {
                imprimirMensaje("Error", "No hay elementos para reasignar", FacesMessage.SEVERITY_ERROR);
                return;
            }
            if (listaTurnos.size() < listaCitasOrigen.size()) {
                imprimirMensaje("Error", "La agenda destino no puede recibir todos los elementos de origen", FacesMessage.SEVERITY_ERROR);
                return;
            }
            int indx = 0;
            for (CitCitas citCitas : listaCitasOrigen) {

                CitTurnos citTurnoNuevo = listaTurnos.get(indx);
                if (citTurnoNuevo.getEstado().equals("disponible")) {

                    //haciendo cambios en el turnoAnterior
                    CitTurnos citTurnoAnterior = citCitas.getIdTurno();
                    citTurnoAnterior.setEstado("no_disponible");
                    citTurnoAnterior.setContador(citTurnoAnterior.getContador() - 1);
                    citTurnosFacade.edit(citTurnoAnterior);

                    //actualizando la informacion de la cita
                    citCitas.setIdPrestador(listaTurnos.get(indx).getIdPrestador());
                    citCitas.setIdTurno(citTurnoNuevo);
                    citCitasFacade.edit(citCitas);

                    //actualizando al turnoNuevo
                    citTurnoNuevo.setContador(citTurnoNuevo.getContador() + 1);
                    citTurnoNuevo.setEstado("no_disponible");
                    citTurnosFacade.edit(citTurnoNuevo);

                }
                indx++;
            }
            cargarCitas();
            if (listaCitasDestino.size() > 0) {
                imprimirMensaje("Informacion", "No es posible rehubicar algunos turnos", FacesMessage.SEVERITY_INFO);
            } else {
                imprimirMensaje("Informacion", "Se ha cambiado la agenda correctamente", FacesMessage.SEVERITY_INFO);
            }
        } else {
            imprimirMensaje("Error", "No hay elementos para hacer el cambio de agenda", FacesMessage.SEVERITY_ERROR);
        }
    }

    //----------------------------------------------------------------------------------------
    //-----------------------------GETTERS AND SETTERS----------------------------------------
    //----------------------------------------------------------------------------------------
    /**
     * @return the fechaInicial
     */
    public Date getFechaInicial() {
        return fechaInicial;
    }

    /**
     * @param fechaInicial the fechaInicial to set
     */
    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    /**
     * @return the horaInicial
     */
    public Date getHoraInicial() {
        return horaInicial;
    }

    /**
     * @param horaInicial the horaInicial to set
     */
    public void setHoraInicial(Date horaInicial) {
        this.horaInicial = horaInicial;
    }

    /**
     * @return the fechaFinal
     */
    public Date getFechaFinal() {
        return fechaFinal;
    }

    /**
     * @param fechaFinal the fechaFinal to set
     */
    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    /**
     * @return the horaFinal
     */
    public Date getHoraFinal() {
        return horaFinal;
    }

    /**
     * @param horaFinal the horaFinal to set
     */
    public void setHoraFinal(Date horaFinal) {
        this.horaFinal = horaFinal;
    }

    /**
     * @return the listaCitasOrigen
     */
    public List<CitCitas> getListaCitasOrigen() {
        return listaCitasOrigen;
    }

    /**
     * @param listaCitasOrigen the listaTurnosOrigen to set
     */
    public void setListaCitasOrigen(List<CitCitas> listaCitasOrigen) {
        this.listaCitasOrigen = listaCitasOrigen;
    }

    /**
     * @return the listaTurnosDestino
     */
    public List<CitCitas> getListaCitasDestino() {
        return listaCitasDestino;
    }

    /**
     * @param listaCitasDestino the listaCitasDestino to set
     */
    public void setListaCitasDestino(List<CitCitas> listaCitasDestino) {
        this.listaCitasDestino = listaCitasDestino;
    }

    public String getId_prestadorOrigen() {
        return id_prestadorOrigen;
    }

    public void setId_prestadorOrigen(String id_prestadorOrigen) {
        this.id_prestadorOrigen = id_prestadorOrigen;
    }

    public String getId_prestadorDestino() {
        return id_prestadorDestino;
    }

    public void setId_prestadorDestino(String id_prestadorDestino) {
        this.id_prestadorDestino = id_prestadorDestino;
    }

    public LazyDataModel<CfgUsuarios> getListaPrestadores() {
        return listaPrestadores;
    }

    public void setListaPrestadores(LazyDataModel<CfgUsuarios> listaPrestadores) {
        this.listaPrestadores = listaPrestadores;
    }

    public CfgUsuarios getPrestadorOrigenSeleccionado() {
        return prestadorOrigenSeleccionado;
    }

    public void setPrestadorOrigenSeleccionado(CfgUsuarios prestadorOrigenSeleccionado) {
        this.prestadorOrigenSeleccionado = prestadorOrigenSeleccionado;
    }

    public CfgUsuarios getPrestadorDestinoSeleccionado() {
        return prestadorDestinoSeleccionado;
    }

    public void setPrestadorDestinoSeleccionado(CfgUsuarios prestadorDestinoSeleccionado) {
        this.prestadorDestinoSeleccionado = prestadorDestinoSeleccionado;
    }

    public List<SelectItem> getListaEspecialidades() {
        return listaEspecialidades;
    }

    public void setListaEspecialidades(List<SelectItem> listaEspecialidades) {
        this.listaEspecialidades = listaEspecialidades;
    }

}
