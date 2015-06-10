/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.utilidades;

import java.util.Date;
import java.util.List;
import modelo.entidades.CitCitas;
import modelo.entidades.CitTurnos;
import modelo.fachadas.CitCitasFacade;
import modelo.fachadas.CitTurnosFacade;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;

public class LazyAgendaModel extends LazyScheduleModel {

    private final int idPrestador;
    private final int idSede;
    private final CitTurnosFacade turnosFacade;
    private final CitCitasFacade citasFacade;
    private List<CitTurnos> listaTurnos;
    private final String opcion;//citaUnica, otrasAgendas, generarTurnos

    public LazyAgendaModel(int idPrestador, int idSede, CitTurnosFacade turnosFacade, CitCitasFacade citasFacade, String opcion) {
        this.idPrestador = idPrestador;
        this.idSede = idSede;
        this.turnosFacade = turnosFacade;
        this.citasFacade = citasFacade;
        this.opcion = opcion;
    }

    @Override
    public void loadEvents(Date start, Date end) {
//        System.out.println("start->" + start);
//        System.out.println("end->" + end);
        clear();
        listaTurnos = turnosFacade.obtenerTurnosLazy(idPrestador, idSede, start, end);
        if (listaTurnos != null) {
            if (!listaTurnos.isEmpty()) {
//                System.out.println(listaTurnos.size());
                switch (opcion) {
                    case "citaUnica":
                        funcionalidadAgendaCitaUnica();
                        break;
                    case "generarTurnos":
                        funcionalidadAgendaCrearTurnos();
                        break;
                    default:
                        funcionalidadOtrasAgendas();
                }
            }
        }
    }

    private void funcionalidadAgendaCitaUnica() {
        for (CitTurnos turno : listaTurnos) {
            Date inicial = new Date(turno.getFecha().getTime());
            inicial.setHours(turno.getHoraIni().getHours());
            inicial.setMinutes(turno.getHoraIni().getMinutes());
            Date finaliza = new Date(turno.getFecha().getTime());
            String estilo;
            String title = turno.getIdTurno().toString();
            if (turno.getEstado().equals("disponible")) {
                estilo = "disponible";
            } else if (turno.getEstado().equals("asignado") || turno.getEstado().equals("en_espera") || turno.getEstado().equals("atendido")) {
                estilo = "asignado";
            } else if (turno.getEstado().equals("no_disponible")) {
                estilo = "no_disponible";
            } else {
                estilo = "reservado";
            }
            CitCitas c = citasFacade.findCitasByTurno(turno.getIdTurno());
            if (c != null) {
                title = title.concat(" - " + c.getIdPaciente().nombreCompleto());
            }
            finaliza.setHours(turno.getHoraFin().getHours());
            finaliza.setMinutes(turno.getHoraFin().getMinutes());
            addEvent(new DefaultScheduleEvent(title, inicial, finaliza, estilo));
        }
    }

    private void funcionalidadAgendaCrearTurnos() {
        String title;
        for (CitTurnos turno : listaTurnos) {
            Date inicial = new Date(turno.getFecha().getTime());
            inicial.setHours(turno.getHoraIni().getHours());
            inicial.setMinutes(turno.getHoraIni().getMinutes());
            Date finaliza = new Date(turno.getFecha().getTime());
            String estilo;
            if (turno.getEstado().equals("no_disponible")) {
                estilo = "no_disponible";
            } else {
                estilo = "disponible";
            }
            finaliza.setHours(turno.getHoraFin().getHours());
            finaliza.setMinutes(turno.getHoraFin().getMinutes());
            title = turno.getIdTurno().toString();
            if (turno.getIdHorario() != null) {
                title = title.concat(" - " + turno.getIdHorario().getDescripcion());
            }
            addEvent(new DefaultScheduleEvent(title, inicial, finaliza, estilo));
        }
    }

    //carga de  eventos para las agendas: recepcion de citas y agenda medico
    private void funcionalidadOtrasAgendas() {
        for (CitTurnos turno : listaTurnos) {
            Date inicial = new Date(turno.getFecha().getTime());
            inicial.setHours(turno.getHoraIni().getHours());
            inicial.setMinutes(turno.getHoraIni().getMinutes());
            Date finaliza = new Date(turno.getFecha().getTime());
            String estilo;
            String title;
            CitCitas cita = citasFacade.findCitasByTurno(turno.getIdTurno());
            if (cita != null) {
                title = turno.getIdTurno().toString().concat(" - " + cita.getIdPaciente().nombreCompleto());
                if (turno.getEstado().equals("asignado") && !cita.getAtendida()) {
                    estilo = "no_atendido";
                } else if (turno.getEstado().equals("atendido") || cita.getAtendida()) {
                    estilo = "atendido";
                } else if (turno.getEstado().equals("en_espera")) {
                    estilo = "en_espera";
                } else {
                    estilo = "libre";
                }
            } else {
                estilo = "libre";
                title = turno.getIdTurno().toString();
            }
            finaliza.setHours(turno.getHoraFin().getHours());
            finaliza.setMinutes(turno.getHoraFin().getMinutes());
            addEvent(new DefaultScheduleEvent(title, inicial, finaliza, estilo));
        }
    }

    public List<CitTurnos> getListaTurnos() {
        return listaTurnos;
    }
}
