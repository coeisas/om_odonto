/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.CitTurnos;

/**
 *
 * @author mario
 */
@Stateless
public class CitTurnosFacade extends AbstractFacade<CitTurnos> {

    public CitTurnosFacade() {
        super(CitTurnos.class);
    }

    public CitTurnos findById(long id_turno) {
        Query query = getEntityManager().createQuery("SELECT t FROM CitTurnos t WHERE t.idTurno = ?1");
        query.setParameter(1, id_turno);
        return (CitTurnos) query.getSingleResult();
    }

    public List<CitTurnos> buscarPorPrestador(int id_prestador) {
        Query query = getEntityManager().createQuery("SELECT t FROM CitTurnos t WHERE t.idPrestador.idUsuario = ?1");
        query.setParameter(1, id_prestador);
        return query.getResultList();
    }

    public List<CitTurnos> buscarPorPrestadorParam(int id_prestador, Date fecha, int cantidad, int opc) {
        Query query;
        if (cantidad == 0) {
            List<CitTurnos> lista = new ArrayList();
            return lista;
        }
        if (opc == 1) {
            query = getEntityManager().createQuery("SELECT t FROM CitTurnos t WHERE t.idPrestador.idUsuario = ?1 AND t.fecha > ?2 AND t.estado = 'disponible'");
        } else {
            query = getEntityManager().createQuery("SELECT t FROM CitTurnos t  WHERE t.idPrestador.idUsuario = ?1 AND t.fecha >= ?2 AND t.estado = 'disponible'");
        }

        query.setParameter(1, id_prestador);
        query.setParameter(2, fecha);
        query.setMaxResults(cantidad);

        return query.getResultList();
    }

    public List<CitTurnos> findTurnosDisponiblesByPrestador(int id_prestador) {
        Query query = getEntityManager().createQuery("SELECT t FROM CitTurnos t  WHERE t.idPrestador.idUsuario = ?1 AND t.estado = 'disponible' AND t.fecha >= CURRENT_DATE ORDER BY t.fecha, t.horaIni");
        query.setParameter(1, id_prestador);
        return query.getResultList();
    }

    public List<CitTurnos> findTurnosDisponiblesByPrestadores(List id_prestadores) {
        Query query = getEntityManager().createQuery("SELECT t FROM CitTurnos t  WHERE t.idPrestador.idUsuario IN ?1 AND t.estado = 'disponible' AND t.fecha >= CURRENT_DATE ORDER BY t.fecha, t.horaIni");
        query.setParameter(1, id_prestadores);
        return query.getResultList();
    }

    public List<CitTurnos> findTurnosDisponiblesByPrestadoresAndHoraIni(List id_prestadores, Date horaini) {
        Query query = getEntityManager().createQuery("SELECT t FROM CitTurnos t  WHERE t.idPrestador.idUsuario IN ?1 AND t.estado='disponible' AND t.fecha >= CURRENT_DATE AND t.horaIni >= ?2 ORDER BY t.fecha, t.horaIni");
        query.setParameter(1, id_prestadores);
        query.setParameter(2, horaini);
        return query.getResultList();
    }

    public List<CitTurnos> findTurnosDisponiblesByPrestadoresAndHoraFin(List id_prestadores, Date horaFin) {
        Query query = getEntityManager().createQuery("SELECT t FROM CitTurnos t  WHERE t.idPrestador.idUsuario IN ?1 AND t.estado='disponible' AND t.fecha >= CURRENT_DATE AND t.horaFin <= ?2 ORDER BY t.fecha, t.horaIni");
        query.setParameter(1, id_prestadores);
        query.setParameter(2, horaFin);
        return query.getResultList();
    }

    public List<CitTurnos> findTurnosDisponiblesByPrestadoresAndIntervalo(List id_prestadores, Date horaIni, Date horaFin) {
        Query query = getEntityManager().createQuery("SELECT t FROM CitTurnos t  WHERE t.idPrestador.idUsuario IN ?1 AND t.estado='disponible' AND t.fecha >= CURRENT_DATE AND t.horaIni >= ?2 AND t.horaFin <= ?3 ORDER BY t.fecha, t.horaIni");
        query.setParameter(1, id_prestadores);
        query.setParameter(2, horaIni);
        query.setParameter(3, horaFin);
        return query.getResultList();
    }

    public List<CitTurnos> findTurnosDisponiblesByPrestadoresLazyNative(String consulta, List id_prestadores, int especialidad, Date horaIni, Date horaFin, List<Integer> daysofweek, int offset, int limit) {
        try {
            Query query = getEntityManager().createNativeQuery(consulta.concat(" order by fecha, hora_ini"), CitTurnos.class);
//            System.out.println(query);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return (List<CitTurnos>) query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public int totalTurnosByPrestadoresLazyNative(String consulta, List id_prestadores, int especialidad, Date horaIni, Date horaFin, List<Integer> daysofweek) {
        try {
            Query query = getEntityManager().createNativeQuery(consulta);
            return Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public List<CitTurnos> buscarPorPrestadorDisponibles(int id_prestador) {
        try {
            return getEntityManager().createNamedQuery("CitTurnos.findByIdPrestadorDisponible", CitTurnos.class
            ).setParameter("idPrestador", id_prestador).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CitTurnos> buscarTurnosDisponibles() {
        try {
            return getEntityManager().createNamedQuery("CitTurnos.findAllDisponible", CitTurnos.class
            ).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public CitTurnos
            buscarPorId(long id_turno) {
        return getEntityManager().createNamedQuery("CitTurnos.findByIdTurno", CitTurnos.class
        ).setParameter("idTurno", id_turno).getSingleResult();

    }

    public List<CitTurnos> getTurnosByIdPrestador(int idPrestador) {
        Query query = getEntityManager().createQuery("SELECT t FROM CitTurnos t WHERE  t.idPrestador.idUsuario = ?1 AND t.fecha >= CURRENT_DATE ORDER BY t.fecha, t.horaIni");
        query.setParameter(1, idPrestador);
        return query.getResultList();
    }

    public List<CitTurnos> findByPrestadorAndSede(int idPrestador, int idSede) {
        Query query = getEntityManager().createQuery("SELECT t FROM CitTurnos t WHERE  t.idPrestador.idUsuario = ?1 AND t.idConsultorio.idSede.idSede =?2 AND t.fecha >= CURRENT_DATE ORDER BY t.fecha, t.horaIni");
        query.setParameter(1, idPrestador);
        query.setParameter(2, idSede);
        return query.getResultList();
    }

    public List<CitTurnos> getTurnosByIdPrestadorAndSedeOneDay(int idPrestador, int idSede) {
        Query query = getEntityManager().createQuery("SELECT t FROM CitTurnos t WHERE  t.idPrestador.idUsuario = ?1 AND t.idConsultorio.idSede.idSede = ?2 AND t.fecha = CURRENT_DATE ORDER BY t.fecha, t.horaIni");
        query.setParameter(1, idPrestador);
        query.setParameter(2, idSede);
        return query.getResultList();
    }

    public List<CitTurnos> getTurnosByIdPrestadorAndSede(int idPrestador, int idSede) {
        Query query = getEntityManager().createQuery("SELECT t FROM CitTurnos t WHERE  t.idPrestador.idUsuario = ?1 AND t.idConsultorio.idSede.idSede = ?2 AND t.fecha >= CURRENT_DATE ORDER BY t.fecha, t.horaIni");
        query.setParameter(1, idPrestador);
        query.setParameter(2, idSede);
        return query.getResultList();
    }

    public List<CitTurnos> getTurnos() {
        Query query = getEntityManager().createQuery("SELECT t FROM CitTurnos t WHERE t.fecha >= CURRENT_DATE ORDER BY t.fecha, t.horaIni");
        return query.getResultList();
    }

    public List<CitTurnos> getNewTurnosByIdPrestador(int idPrestador) {
        Query query = getEntityManager().createQuery("SELECT t FROM CitTurnos t WHERE  t.idPrestador.idUsuario = ?1 AND t.fechaCreacion = CURRENT_DATE ORDER BY t.fecha, t.horaIni");
        query.setParameter(1, idPrestador);
        return query.getResultList();
    }

    public List<CitTurnos> findTurnosNoDisponiblesByPrestador(int id_prestador) {
        Query query = getEntityManager().createQuery("SELECT t FROM CitTurnos t  WHERE t.idPrestador.idUsuario = ?1 AND t.estado = 'no_disponible' AND t.fecha >= CURRENT_DATE ORDER BY t.fecha, t.horaIni");
        query.setParameter(1, id_prestador);
        return query.getResultList();
    }

    public List<CitTurnos> findTurnosNoDisponiblesByPrestadorParam(int id_prestador, Date fechaInicial, Date horaInicial, Date fechaFinal, Date horaFinal) {
        Query query;
        if (horaInicial == null && horaFinal == null) {
            query = getEntityManager().createQuery("SELECT t FROM CitTurnos t  WHERE t.idPrestador.idUsuario = ?1 AND t.estado = 'no_disponible' AND t.fecha >= ?2 AND t.fecha <= ?3 ORDER BY t.fecha, t.horaIni");
            query.setParameter(1, id_prestador);
            query.setParameter(2, fechaInicial);
            query.setParameter(3, fechaFinal);
        } else {
            query = getEntityManager().createQuery("SELECT t FROM CitTurnos t  WHERE t.idPrestador.idUsuario = ?1 AND t.estado = 'no_disponible' AND t.fecha >= ?2 AND t.fecha <= ?3 AND t.horaIni >= ?4 AND t.horaIni <= ?5 ORDER BY t.fecha, t.horaIni");
            query.setParameter(1, id_prestador);
            query.setParameter(2, fechaInicial);
            query.setParameter(3, fechaFinal);
            query.setParameter(4, horaInicial);
            query.setParameter(5, horaFinal);
        }
        return query.getResultList();
    }

    public int EliminarAgenda(int idPrestador, int idSede, Date fechaInicial, Date fechaFinal, int id_horario, List estados) {
        Query query;
        try {
            if (id_horario != 0) {
                query = getEntityManager().createQuery("DELETE FROM CitTurnos t WHERE t.idPrestador.idUsuario = ?1 AND t.fecha >= ?2 AND t.fecha <= ?3 AND t.idHorario.idHorario = ?4 AND t.estado NOT IN ?5 AND t.idConsultorio.idSede.idSede = ?6 AND t.idTurno NOT IN (SELECT c.idTurno.idTurno FROM CitCitas c WHERE c.idPrestador.idUsuario = ?1 AND c.idTurno.fecha >= ?2 AND c.idTurno.fecha <= ?3 AND c.idTurno.idHorario.idHorario = ?4)");
                query.setParameter(4, id_horario);
            } else {
                query = getEntityManager().createQuery("DELETE FROM CitTurnos t WHERE t.idPrestador.idUsuario = ?1 AND t.fecha >= ?2 AND t.fecha <= ?3 AND t.idHorario IS NULL AND t.estado NOT IN ?5 AND t.idConsultorio.idSede.idSede = ?6  AND t.idTurno NOT IN (SELECT c.idTurno.idTurno FROM CitCitas c WHERE c.idPrestador.idUsuario = ?1 AND c.idTurno.fecha >= ?2 AND c.idTurno.fecha <= ?3 AND c.idTurno.idHorario IS NULL)");
            }
            query.setParameter(1, idPrestador);
            query.setParameter(2, fechaInicial);
            query.setParameter(3, fechaFinal);
            query.setParameter(5, estados);
            query.setParameter(6, idSede);
            return query.executeUpdate();
        } catch (Exception e) {
            return 0;
        }
    }

    public int ValidarEliminarAgenda(int idPrestador, int idSede, Date fechaInicial, Date fechaFinal, int id_horario) {
        Query query;
        try {
            if (id_horario != 0) {
                query = getEntityManager().createQuery("SELECT COUNT(t.idTurno) FROM CitTurnos t WHERE t.idPrestador.idUsuario = ?1 AND t.fecha >= ?2 AND t.fecha <= ?3 AND t.idHorario.idHorario = ?4 AND t.idConsultorio.idSede.idSede = ?5");
                query.setParameter(4, id_horario);
            } else {
                query = getEntityManager().createQuery("SELECT COUNT(t.idTurno) FROM CitTurnos t WHERE t.idPrestador.idUsuario = ?1 AND t.fecha >= ?2 AND t.fecha <= ?3 AND t.idHorario IS NULL AND t.idConsultorio.idSede.idSede = ?5");
            }
            query.setParameter(1, idPrestador);
            query.setParameter(2, fechaInicial);
            query.setParameter(3, fechaFinal);
            query.setParameter(5, idSede);
            int totalTurnos = Integer.parseInt(query.getSingleResult().toString());
            return totalTurnos;
        } catch (Exception e) {
            return 0;
        }
    }

    public int actualizarTurno(int idPrestador, int idSede, Date fechaInicial, Date fechaFinal, int id_horario) {
        Query query;
        try {
            if (id_horario != 0) {
                query = getEntityManager().createQuery("UPDATE CitTurnos t SET t.idHorario = null, t.estado = 'no_disponible' WHERE t.idTurno IN (SELECT c.idTurno.idTurno FROM CitCitas c WHERE c.idPrestador.idUsuario = ?1 AND c.idTurno.estado = 'disponible' AND c.idTurno.fecha >= ?2 AND c.idTurno.fecha <= ?3 AND c.idTurno.idHorario.idHorario = ?4 AND c.idTurno.idConsultorio.idSede.idSede = ?5)");
                query.setParameter(4, id_horario);
            } else {
                query = getEntityManager().createQuery("UPDATE CitTurnos t SET t.estado = 'no_disponible' WHERE t.idTurno IN (SELECT c.idTurno.idTurno FROM CitCitas c WHERE c.idPrestador.idUsuario = ?1 AND c.idTurno.estado = 'disponible' AND c.idTurno.fecha >= ?2 AND c.idTurno.fecha <= ?3 AND c.idTurno.idHorario IS NULL AND c.idTurno.idConsultorio.idSede.idSede = ?5)");
            }
            query.setParameter(1, idPrestador);
            query.setParameter(2, fechaInicial);
            query.setParameter(3, fechaFinal);
            query.setParameter(5, idSede);
            return query.executeUpdate();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean ValidarEliminarHorario(int id_horario) {
        try {
            Query query = getEntityManager().createQuery("SELECT t FROM CitTurnos t WHERE t.idHorario.idHorario = ?1");
            query.setParameter(1, id_horario);
            query.setMaxResults(1);
            CitTurnos turno = (CitTurnos) query.getSingleResult();
            return turno == null;
        } catch (Exception e) {
            return true;
        }
    }

    public List<CitTurnos> buscarTurnosParametrizado(int id_prestador, Date fechaInicial, Date fechaFinal, int id_consultorio, int id_horario) {
        try {
            Query query = getEntityManager().createQuery("SELECT t FROM CitTurnos t WHERE t.idPrestador.idUsuario = ?1 AND t.fecha >= ?2 AND t.fecha <= ?3 AND t.idConsultorio.idConsultorio = ?4 AND t.idHorario.idHorario = ?5");
            query.setParameter(1, id_prestador);
            query.setParameter(2, fechaInicial);
            query.setParameter(3, fechaFinal);
            query.setParameter(4, id_consultorio);
            query.setParameter(5, id_horario);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CitTurnos> buscarDisponibilidadFechas(int idPrestador, List<Date> fechas, Date hora, int idsede) {
        try {
            //Query query = getEntityManager().createQuery("SELECT DISTINCT c.idTurno, c.fecha FROM CitTurnos c WHERE c.idPrestador.idUsuario = ?1 AND c.fecha IN ?2 AND c.horaIni = ?3 AND c.idConsultorio.idSede.idSede = ?4 ORDER BY c.fecha, c.horaIni");
            Query query = getEntityManager().createQuery("SELECT c FROM CitTurnos c WHERE c.idTurno IN (SELECT DISTINCT t.idTurno FROM CitTurnos t WHERE t.idPrestador.idUsuario = ?1  AND t.fecha IN ?2 AND t.estado = 'disponible' AND t.horaIni = ?5 AND t.idPrestador.idUsuario = ?1 AND t.idConsultorio.idSede.idSede = ?4) ORDER BY c.fecha, c.horaIni");
            query.setParameter(1, idPrestador);
            query.setParameter(2, fechas);
            query.setParameter(4, idsede);
            query.setParameter(5, hora);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public CitTurnos buscarTurnoDisponiblePrestadorFecha(int idPrestador, Date fecha, Date hora, int idsede) {
        try {
            //Query query = getEntityManager().createQuery("SELECT DISTINCT c.idTurno, c.fecha FROM CitTurnos c WHERE c.idPrestador.idUsuario = ?1 AND c.fecha IN ?2 AND c.horaIni = ?3 AND c.idConsultorio.idSede.idSede = ?4 ORDER BY c.fecha, c.horaIni");
            Query query = getEntityManager().createQuery("SELECT c FROM CitTurnos c WHERE c.idPrestador.idUsuario = ?1 AND c.fecha = ?2 AND c.estado = 'disponible' AND c.idConsultorio.idSede.idSede = ?3 AND c.horaIni >= ?4 ORDER BY c.horaIni");
            query.setParameter(1, idPrestador);
            query.setParameter(2, fecha);
            query.setParameter(3, idsede);
            query.setParameter(4, hora);
            query.setMaxResults(1);
            return (CitTurnos) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Object[] determinarHorainicial(int idPrestador, Date fechaInicial, int sede) {
        try {
            Query query = getEntityManager().createQuery("SELECT p.horaIni, p.horaFin FROM CitTurnos p WHERE p.fecha = ?2 AND p.estado = 'disponible' AND p.idPrestador.idUsuario = ?1 AND p.idConsultorio.idSede.idSede = ?3 ORDER BY p.horaIni", Object[].class);
            query.setParameter(1, idPrestador);
            query.setParameter(2, fechaInicial);
            query.setParameter(3, sede);
            query.setMaxResults(1);
            Object o = query.getSingleResult();
            //return (Date) query.getSingleResult();
            return (Object[]) o;
        } catch (Exception e) {
            return null;
//            System.out.println("no problem");
        }
    }

    public CitTurnos determinarTurnoSiguiente(int idPrestador, Date horaFinA, Date fecha, int sede) {
        try {
            Query query;
            query = getEntityManager().createQuery("SELECT c FROM CitTurnos c WHERE c.idPrestador.idUsuario = ?1 AND c.idConsultorio.idSede.idSede = ?2 AND c.estado = 'disponible' AND c.fecha = ?3 AND c.horaIni >= ?5 ORDER BY c.horaIni");
            query.setParameter(1, idPrestador);
            query.setParameter(2, sede);
            query.setParameter(3, fecha);
            query.setParameter(5, horaFinA);
            query.setMaxResults(1);
            return (CitTurnos) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

//    select * from cit_turnos where id_turno in (select distinct id_turno from cit_turnos where fecha IN (select distinct fecha from cit_turnos WHERE fecha >= current_date AND id_prestador = 12 and estado = 'disponible') and hora_ini = (select hora_ini from cit_turnos WHERE fecha >= current_date AND estado = 'disponible' and id_prestador = 12 limit 1) and id_prestador = 12) order by fecha, hora_ini;
    public List<CitTurnos> obtenerTurnosLazy(int idPrestador, int idSede, Date start, Date end) {
        try {
            Query query = getEntityManager().createQuery("SELECT t FROM CitTurnos t WHERE  t.idPrestador.idUsuario = ?1 AND t.idConsultorio.idSede.idSede = ?2 AND t.fecha >= ?3 AND t.fecha < ?4  ORDER BY t.fecha, t.horaIni");
            query.setParameter(1, idPrestador);
            query.setParameter(2, idSede);
            query.setParameter(3, start);
            query.setParameter(4, end);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }

    }

    public Object[] MinDateMaxDate(int idPrestador, int idSede) {
        try {
            Query query = getEntityManager().createQuery("SELECT MIN(t.horaIni), MAX(t.horaFin) FROM CitTurnos t WHERE t.idPrestador.idUsuario = ?1 AND t.idConsultorio.idSede.idSede = ?2", Object[].class);
            query.setParameter(1, idPrestador);
            query.setParameter(2, idSede);
            Object[] o = (Object[]) query.getSingleResult();
            return o;
        } catch (Exception e) {
            return null;
        }
    }

    public int totalTurnosDisponibles(List<Integer> idTurnos) {
        try {
            Query query = getEntityManager().createQuery("SELECT COUNT(t.idTurno) FROM CitTurnos t WHERE t.idTurno IN ?1 AND t.estado = 'disponible'");
            query.setParameter(1, idTurnos);
            return Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public int totalTurnosDisponiblesApartirFecha(int idPrestador, int idSede, Date fecha) {
        try {
            Query query = getEntityManager().createQuery("SELECT COUNT(t.idTurno) FROM CitTurnos t WHERE t.idPrestador.idUsuario = ?1 AND t.idConsultorio.idSede.idSede = ?2 AND t.fecha >= ?3 AND t.estado = 'disponible'");
            query.setParameter(1, idPrestador);
            query.setParameter(2, idSede);
            query.setParameter(3, fecha);
            return Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }
}
