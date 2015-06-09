/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import beans.utilidades.Oportunidad;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import modelo.entidades.CfgPacientes;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.CitCitas;
import modelo.entidades.FacAdministradora;

/**
 *
 * @author mario
 */
@Stateless
public class CitCitasFacade extends AbstractFacade<CitCitas> {

    public CitCitasFacade() {
        super(CitCitas.class);
    }

    public List<CitCitas> buscarFaltanRegistro() {
        //listado de citas que no tienen registro asociado (todos los prestadores)
        try {
            //String hql = "SELECT a FROM CitCitas a WHERE a.tieneRegAsociado = false AND a.cancelada=false AND a.atendida=true";
            String hql = "SELECT a FROM CitCitas a WHERE a.tieneRegAsociado = false AND a.cancelada=false AND a.idTurno.estado like 'en_espera'";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            System.err.println("" + e.toString());
            return null;
        }
    }

    public List<CitCitas> buscarFaltanRegistro(String idprestador) {
        //listado de citas que no tienen registro asociado (solo para un prestador)
        try {
            //String hql = "SELECT a FROM CitCitas a WHERE a.tieneRegAsociado = false AND a.cancelada=false AND a.atendida=true";
            String hql = "SELECT a FROM CitCitas a WHERE a.tieneRegAsociado = false AND a.cancelada=false AND a.idPrestador.idUsuario = " + idprestador +" AND a.idTurno.estado like 'en_espera'";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            System.err.println("" + e.toString());
            return null;
        }
    }

    public CitCitas findCitasByTurno(long id_turno) {
        CitCitas cita;
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM CitCitas c WHERE c.idTurno.idTurno = ?1 AND c.cancelada = false");
            query.setParameter(1, id_turno);
            cita = (CitCitas) query.getSingleResult();
        } catch (Exception e) {
            cita = null;
        }
        return cita;
    }

    public List<CitCitas> findCitasByPrestador(int idprestador) {
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM CitCitas c WHERE c.idPrestador.idUsuario = ?1 AND c.atendida = false ORDER BY c.idCita DESC");
            query.setParameter(1, idprestador);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CitCitas> findCitasByPrestadorVigentes(int idprestador, int offset, int limit) {
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM CitCitas c WHERE c.idPrestador.idUsuario = ?1 AND c.atendida = false AND c.atendida = false AND c.idTurno.fecha >= CURRENT_DATE ORDER BY c.idTurno.fecha, c.idTurno.horaIni");
            query.setParameter(1, idprestador);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CitCitas> findCitasCanceladas() {
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM CitCitas c WHERE c.cancelada = true ORDER BY c.idCita");
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CitCitas> findCitasCanceladasParametrizable(Date fechaInicial, Date fechaFinal, List<CfgPacientes> pacientes, List<CfgUsuarios> prestadores, List<FacAdministradora> administradoras, int ban) {
        String consulta = "SELECT c FROM CitCitas c WHERE c.cancelada = true";
        if (ban == 1) {
            if (fechaInicial != null) {
                consulta = consulta.concat(" AND c.idTurno.fecha >= ?1");
            }
            if (fechaFinal != null) {
                consulta = consulta.concat("  AND c.idTurno.fecha <= ?2");
            }
            if (pacientes.size() > 0) {
                consulta = consulta.concat(" AND c.idPaciente IN ?3");
            }
            if (prestadores.size() > 0) {
                consulta = consulta.concat(" AND c.idPrestador IN ?4");
            }
            if (administradoras.size() > 0) {
                consulta = consulta.concat(" AND c.idAdministradora IN ?5");
            }
        }
        consulta = consulta.concat("  ORDER BY c.idTurno.fecha, c.idTurno.horaIni, c.idCita");
        try {
            Query query = getEntityManager().createQuery(consulta);
            if (ban == 1) {
                if (fechaInicial != null) {
                    query.setParameter(1, fechaInicial);
                }
                if (fechaFinal != null) {
                    query.setParameter(2, fechaFinal);
                }
                if (pacientes.size() > 0) {
                    query.setParameter(3, pacientes);
                }
                if (prestadores.size() > 0) {
                    query.setParameter(4, prestadores);
                }
                if (administradoras.size() > 0) {
                    query.setParameter(5, administradoras);
                }
            }
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CitCitas> findCitasByPaciente(int idpaciente) {
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM CitCitas c WHERE c.idPaciente.idPaciente = ?1 AND c.atendida = false ORDER BY c.idCita DESC");
            query.setParameter(1, idpaciente);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CitCitas> findCitasByPacienteVigentes(int idpaciente, int offset, int limit) {
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM CitCitas c WHERE c.idPaciente.idPaciente = ?1 AND c.atendida = false AND c.idTurno.fecha >= CURRENT_DATE ORDER BY c.idTurno.fecha, c.idTurno.horaIni");
            query.setParameter(1, idpaciente);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public int TotalCitasVigentesByPaciente(int idpaciente) {
        try {
            Query query = getEntityManager().createQuery("SELECT COUNT (c.idCita) FROM CitCitas c WHERE c.idPaciente.idPaciente = ?1 AND c.atendida = false AND c.idTurno.fecha >= CURRENT_DATE");
            query.setParameter(1, idpaciente);
            int total = Integer.parseInt(query.getSingleResult().toString());
            return total;
        } catch (Exception e) {
            return 0;
        }
    }

    public int TotalCitasVigentesByPrestador(int idprestador) {
        try {
            Query query = getEntityManager().createQuery("SELECT COUNT (c.idCita) FROM CitCitas c WHERE c.idPrestador.idUsuario = ?1 AND c.atendida = false AND c.idTurno.fecha >= CURRENT_DATE");
            query.setParameter(1, idprestador);
            int total = Integer.parseInt(query.getSingleResult().toString());
            return total;
        } catch (Exception e) {
            return 0;
        }
    }

    public List<CitCitas> findCitas() {
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM CitCitas c WHERE c.idTurno.fecha >= CURRENT_DATE AND c.atendida = false ORDER BY c.idCita DESC");
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CitCitas> buscarCitas() {
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM CitCitas c ORDER BY c.idTurno.fecha, c.idTurno.horaIni");
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CitCitas> buscarCitasIntervalo(Date fechaInicial, Date fechaFinal) {
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM CitCitas c WHERE c.idTurno.fecha >= ?1 AND c.idTurno.fecha <= ?2 ORDER BY c.idTurno.fecha, c.idTurno.horaIni");
            query.setParameter(1, fechaInicial);
            query.setParameter(2, fechaFinal);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CitCitas> buscarCitasIntervaloParametros(Date fechaInicial, Date fechaFinal, List<CfgPacientes> pacientes, List<CfgUsuarios> prestadores, List<FacAdministradora> administradoras, int ban) {
        String consulta = "SELECT c FROM CitCitas c";
        if (ban == 1) {
            int aux = 0;
            consulta = consulta.concat("  WHERE");
            if (fechaInicial != null) {
                if (aux == 1) {
                    consulta = consulta.concat("  AND");
                }
                consulta = consulta.concat("  c.idTurno.fecha >= ?1");
                aux = 1;

            }
            if (fechaFinal != null) {
                if (aux == 1) {
                    consulta = consulta.concat("  AND");
                }
                aux = 1;
                consulta = consulta.concat("  c.idTurno.fecha <= ?2");
            }
            if (pacientes.size() > 0) {
                if (aux == 1) {
                    consulta = consulta.concat("  AND");
                }
                aux = 1;
                consulta = consulta.concat(" c.idPaciente IN ?3");
            }
            if (prestadores.size() > 0) {
                if (aux == 1) {
                    consulta = consulta.concat("  AND");
                }
                aux = 1;
                consulta = consulta.concat(" c.idPrestador IN ?4");
            }
            if (administradoras.size() > 0) {
                if (aux == 1) {
                    consulta = consulta.concat("  AND");
                }
                aux = 1;
                consulta = consulta.concat(" c.idAdministradora IN ?5");
            }
        }
        consulta = consulta.concat(" ORDER BY c.idTurno.fecha, c.idTurno.horaIni");
        try {
            Query query = getEntityManager().createQuery(consulta);
            if (ban == 1) {
                if (fechaInicial != null) {
                    query.setParameter(1, fechaInicial);
                }
                if (fechaFinal != null) {
                    query.setParameter(2, fechaFinal);
                }
                if (pacientes.size() > 0) {
                    query.setParameter(3, pacientes);
                }
                if (prestadores.size() > 0) {
                    query.setParameter(4, prestadores);
                }
                if (administradoras.size() > 0) {
                    query.setParameter(5, administradoras);
                }
            }
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CitCitas> findCitasById(long id) {
        Query query = getEntityManager().createQuery("SELECT c FROM CitCitas c WHERE c.idCita =?1");
        query.setParameter(1, id);
        return query.getResultList();
    }

    public CitCitas findCitaById(long id) {
        Query query = getEntityManager().createQuery("SELECT c FROM CitCitas c WHERE c.idCita =?1");
        query.setParameter(1, id);
        return (CitCitas) query.getSingleResult();
    }

    public List<CitCitas> findCitaByIdDos(long id) {
        //Query query = getEntityManager().createQuery("SELECT c.idTurno.codConsultorio.idSede.codEmpresa.razonSocial AS empresa, c.idTurno.codConsultorio.idSede.nombre AS sede, c.idTurno.codConsultorio.idSede.direccion AS sedeDir, c.idTurno.codConsultorio.idSede.telefono AS sedeTel, c.idCita AS idCita, c.idPrestador.primernombre AS prestadorPN, c.idPrestador.segundonombre AS prestadorSN, c.idPrestador.primerapellido AS prestadorPA, c.idPrestador.segundoapellido AS prestadorSA, c.idPaciente.primerNombre AS pacientePN, c.idPaciente.segundoNombre AS pacienteSN, c.idPaciente.primerApellido AS pacientePA, c.idPaciente.segundoApellido AS pacienteSA, c.idPaciente.codAdministradora.razonSocial, c.idTurno.codConsultorio.idSede.codEmpresa.observaciones AS observaciones FROM Citas c WHERE c.idCita =?1");
        Query query = getEntityManager().createQuery("SELECT c FROM CitCitas c WHERE c.idCita =?1");
        query.setParameter(1, id);
        return query.getResultList();
    }

    public List<CitCitas> findCitaGroupByAdministradora() {
        //Query query = getEntityManager().createQuery("SELECT c.idTurno.codConsultorio.idSede.codEmpresa.razonSocial AS empresa, c.idTurno.codConsultorio.idSede.nombre AS sede, c.idTurno.codConsultorio.idSede.direccion AS sedeDir, c.idTurno.codConsultorio.idSede.telefono AS sedeTel, c.idCita AS idCita, c.idPrestador.primernombre AS prestadorPN, c.idPrestador.segundonombre AS prestadorSN, c.idPrestador.primerapellido AS prestadorPA, c.idPrestador.segundoapellido AS prestadorSA, c.idPaciente.primerNombre AS pacientePN, c.idPaciente.segundoNombre AS pacienteSN, c.idPaciente.primerApellido AS pacientePA, c.idPaciente.segundoApellido AS pacienteSA, c.idPaciente.codAdministradora.razonSocial, c.idTurno.codConsultorio.idSede.codEmpresa.observaciones AS observaciones FROM Citas c WHERE c.idCita =?1");
        Query query = getEntityManager().createQuery("SELECT c FROM CitCitas c GROUP BY c.idAdministradora, c.idPrestador, c.idCita, c.idTurno ORDER BY c.idAdministradora, c.idPrestador, c.idTurno.fecha, c.idCita");
        return query.getResultList();
    }

    public List<CitCitas> findCitaGroupByAdministradoraIntervalo(Date fechaInicial, Date fechaFinal) {
        //Query query = getEntityManager().createQuery("SELECT c.idTurno.codConsultorio.idSede.codEmpresa.razonSocial AS empresa, c.idTurno.codConsultorio.idSede.nombre AS sede, c.idTurno.codConsultorio.idSede.direccion AS sedeDir, c.idTurno.codConsultorio.idSede.telefono AS sedeTel, c.idCita AS idCita, c.idPrestador.primernombre AS prestadorPN, c.idPrestador.segundonombre AS prestadorSN, c.idPrestador.primerapellido AS prestadorPA, c.idPrestador.segundoapellido AS prestadorSA, c.idPaciente.primerNombre AS pacientePN, c.idPaciente.segundoNombre AS pacienteSN, c.idPaciente.primerApellido AS pacientePA, c.idPaciente.segundoApellido AS pacienteSA, c.idPaciente.codAdministradora.razonSocial, c.idTurno.codConsultorio.idSede.codEmpresa.observaciones AS observaciones FROM Citas c WHERE c.idCita =?1");
        Query query = getEntityManager().createQuery("SELECT c FROM CitCitas c WHERE c.idTurno.fecha >= ?1 AND c.idTurno.fecha <= ?2 GROUP BY c.idAdministradora, c.idPrestador, c.idCita, c.idTurno ORDER BY c.idAdministradora, c.idPrestador, c.idTurno.fecha, c.idCita");
        query.setParameter(1, fechaInicial);
        query.setParameter(2, fechaFinal);
        return query.getResultList();
    }

    public List<CitCitas> findCitaGroupByAdministradoraParametros(Date fechaInicial, Date fechaFinal, List<CfgPacientes> pacientes, List<CfgUsuarios> prestadores, List<FacAdministradora> administradoras, int ban) {
        String consulta = "SELECT c FROM CitCitas c";
        if (ban == 1) {
            int aux = 0;
            consulta = consulta.concat("  WHERE");
            if (fechaInicial != null) {
                if (aux == 1) {
                    consulta = consulta.concat("  AND");
                }
                consulta = consulta.concat("  c.idTurno.fecha >= ?1");
                aux = 1;

            }
            if (fechaFinal != null) {
                if (aux == 1) {
                    consulta = consulta.concat("  AND");
                }
                aux = 1;
                consulta = consulta.concat("  c.idTurno.fecha <= ?2");
            }
            if (pacientes.size() > 0) {
                if (aux == 1) {
                    consulta = consulta.concat("  AND");
                }
                aux = 1;
                consulta = consulta.concat(" c.idPaciente IN ?3");
            }
            if (prestadores.size() > 0) {
                if (aux == 1) {
                    consulta = consulta.concat("  AND");
                }
                aux = 1;
                consulta = consulta.concat(" c.idPrestador IN ?4");
            }
            if (administradoras.size() > 0) {
                if (aux == 1) {
                    consulta = consulta.concat("  AND");
                }
                aux = 1;
                consulta = consulta.concat(" c.idAdministradora IN ?5");
            }
        }
        consulta = consulta.concat(" GROUP BY c.idAdministradora, c.idPrestador, c.idCita, c.idTurno ORDER BY c.idAdministradora, c.idPrestador, c.idTurno.fecha, c.idCita");
        try {
            //Query query = getEntityManager().createQuery("SELECT c FROM CitCitas c WHERE c.idTurno.fecha >= ?1 AND c.idTurno.fecha <= ?2 AND c.idPaciente IN ?3 AND c.idPrestador IN ?4 AND c.idAdministradora IN ?5 GROUP BY c.idAdministradora, c.idPrestador, c.idCita, c.idTurno ORDER BY c.idAdministradora, c.idPrestador, c.idTurno.fecha, c.idCita");
            Query query = getEntityManager().createQuery(consulta);
            if (ban == 1) {
                if (fechaInicial != null) {
                    query.setParameter(1, fechaInicial);
                }
                if (fechaFinal != null) {
                    query.setParameter(2, fechaFinal);
                }
                if (pacientes.size() > 0) {
                    query.setParameter(3, pacientes);
                }
                if (prestadores.size() > 0) {
                    query.setParameter(4, prestadores);
                }
                if (administradoras.size() > 0) {
                    query.setParameter(5, administradoras);
                }
            }
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CitCitas> findCitasByPrestadorParam(int idPrestador, Date fechaInicial, Date horaInicial, Date fechaFinal, Date horaFinal) {
        Query query;
        if (horaInicial == null && horaFinal == null) {
            query = getEntityManager().createQuery("SELECT c FROM CitCitas c WHERE c.idPrestador.idUsuario = ?1 AND c.idTurno.fecha >= ?2 AND c.idTurno.fecha <= ?3 ORDER BY c.idTurno.fecha, c.idTurno.horaIni");
            query.setParameter(1, idPrestador);
            query.setParameter(2, fechaInicial);
            query.setParameter(3, fechaFinal);
        } else {
            query = getEntityManager().createQuery("SELECT c FROM CitCitas c WHERE c.idPrestador.idUsuario = ?1 AND c.idTurno.fecha >= ?2 AND c.idTurno.fecha <= ?3 AND (c.idTurno.horaIni < ?4 OR c.idTurno.horaFin > ?5 OR c.idTurno.horaFin <= ?5) AND c.idTurno.horaFin > ?4 AND c.idTurno.horaIni < ?5 ORDER BY c.idTurno.fecha, c.idTurno.horaIni");
            query.setParameter(1, idPrestador);
            query.setParameter(2, fechaInicial);
            query.setParameter(3, fechaFinal);
            query.setParameter(4, horaInicial);
            query.setParameter(5, horaFinal);
        }
        return query.getResultList();
    }

    public List<CitCitas> findNextCitasByPrestadorParam(int idPrestador, Date fechaInicial, int opc) {
        Query query;
        if (opc == 1) {
            query = getEntityManager().createQuery("SELECT c FROM CitCitas c WHERE c.idPrestador.idUsuario = ?1 AND c.idTurno.fecha > ?2 ORDER BY c.idTurno.fecha, c.idTurno.horaIni");
        } else {
            query = getEntityManager().createQuery("SELECT c FROM CitCitas c WHERE c.idPrestador.idUsuario = ?1 AND c.idTurno.fecha >= ?2 ORDER BY c.idTurno.fecha, c.idTurno.horaIni");
        }
        query.setParameter(1, idPrestador);
        query.setParameter(2, fechaInicial);

        return query.getResultList();
    }

    public List<Object[]> findOportunidad(Date fechaInicial, Date fechaFinal) {
        Query query;
        if (fechaInicial == null || fechaFinal == null) {
            query = getEntityManager().createNativeQuery("SELECT fac_servicio.nombre_servicio, count(cit_citas.id_cita) AS total_consultas, sum(cit_turnos.fecha - cit_citas.fecha_registro) AS total_dias FROM cit_citas, cit_turnos, fac_servicio WHERE cit_citas.id_turno = cit_turnos.id_turno AND cit_citas.id_servicio = fac_servicio.id_servicio AND cit_citas.cancelada = false GROUP BY cit_citas.id_servicio, fac_servicio.nombre_servicio ORDER BY cit_citas.id_servicio");
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            query = getEntityManager().createNativeQuery("SELECT fac_servicio.nombre_servicio AS nombre_servicio, count(cit_citas.id_cita) AS total_consultas, sum(cit_turnos.fecha - cit_citas.fecha_registro) AS total_dias FROM cit_citas, cit_turnos, fac_servicio WHERE cit_citas.id_turno = cit_turnos.id_turno AND cit_citas.id_servicio = fac_servicio.id_servicio AND cit_turnos.fecha >= '" + format.format(fechaInicial) + "' AND cit_turnos.fecha <= '" + format.format(fechaFinal) + "' AND cit_citas.cancelada = false GROUP BY cit_citas.id_servicio, fac_servicio.nombre_servicio ORDER BY cit_citas.id_servicio");
        }
        return query.getResultList();
    }

    public List<CitCitas> findHistoriaClinica(CfgPacientes paciente, Date fechaInicial, Date fechaFinal) {
        try {
            Query query;
            if (fechaInicial == null && fechaFinal == null) {
                query = getEntityManager().createQuery("SELECT c FROM CitCitas c WHERE c.idPaciente = ?1 ORDER BY c.idTurno.fecha, c.idTurno.horaIni");
            } else if (fechaInicial != null && fechaFinal == null) {
                query = getEntityManager().createQuery("SELECT c FROM CitCitas c WHERE c.idPaciente = ?1 AND c.idTurno.fecha >= ?2 ORDER BY c.idTurno.fecha, c.idTurno.horaIni");
                query.setParameter(2, fechaInicial);
            } else if (fechaInicial == null && fechaFinal != null) {
                query = getEntityManager().createQuery("SELECT c FROM CitCitas c WHERE c.idPaciente = ?1 AND c.idTurno.fecha <= ?3 ORDER BY c.idTurno.fecha, c.idTurno.horaIni");
                query.setParameter(3, fechaFinal);
            } else {
                query = getEntityManager().createQuery("SELECT c FROM CitCitas c WHERE c.idPaciente = ?1 AND c.idTurno.fecha >= ?2 AND c.idTurno.fecha <= ?3 ORDER BY c.idTurno.fecha, c.idTurno.horaIni");
                query.setParameter(2, fechaInicial);
                query.setParameter(3, fechaFinal);
            }
            query.setParameter(1, paciente);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}
