/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.CfgPacientes;
import modelo.entidades.CitAutorizaciones;
import modelo.entidades.FacAdministradora;

/**
 *
 * @author mario
 */
@Stateless
public class CitAutorizacionesFacade extends AbstractFacade<CitAutorizaciones> {

    public CitAutorizacionesFacade() {
        super(CitAutorizaciones.class);
    }

    //obtiene la autorizacion de un paciente que incluya determinado servicio con sesiones disponibles
    public CitAutorizaciones findAutorizacion(int idPaciente, int idServicio, int idAdministradora) {
        try {
            Query query = getEntityManager().createQuery("SELECT a FROM CitAutorizaciones a WHERE a.paciente.idPaciente = ?1 AND a.administradora.idAdministradora = ?3 AND EXISTS(SELECT sa FROM a.citAutorizacionesServiciosList sa WHERE sa.facServicio.idServicio = ?2 AND sa.sesionesPendientes > 0) AND a.cerrada = false");
            query.setParameter(1, idPaciente);
            query.setParameter(2, idServicio);
            query.setParameter(3, idAdministradora);
            return (CitAutorizaciones) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

//    obtiene la autorizacion donde no hay sesiones pendientes
    public CitAutorizaciones findAutorizacionDos(int idPaciente, int idServicio, int idAdministradora) {
        try {
            Query query = getEntityManager().createQuery("SELECT a FROM CitAutorizaciones a WHERE a.paciente.idPaciente = ?1 AND a.administradora.idAdministradora = ?3 AND EXISTS(SELECT sa FROM a.citAutorizacionesServiciosList sa WHERE sa.facServicio.idServicio = ?2 AND sa.sesionesPendientes = 0) AND a.cerrada = false");
            query.setParameter(1, idPaciente);
            query.setParameter(2, idServicio);
            query.setParameter(3, idAdministradora);
            return (CitAutorizaciones) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }    

    public CitAutorizaciones findAutorizacionNoCerrada(CfgPacientes paciente, String numAutorizacion) {
        try {
            Query query = getEntityManager().createQuery("SELECT a FROM CitAutorizaciones a WHERE a.paciente = ?1 AND a.cerrada = false AND a.numAutorizacion = ?2");
            query.setParameter(1, paciente);
            query.setParameter(2, numAutorizacion);
            return (CitAutorizaciones) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<CitAutorizaciones> findAutorizacionesNoCerradas(int offset, int limit) {
        try {
            Query query = getEntityManager().createQuery("SELECT a FROM CitAutorizaciones a WHERE a.cerrada = false ORDER BY a.fechaSistema, a.idAutorizacion");
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public int totalAutorizacionesNoCerradas() {
        try {
            Query query = getEntityManager().createQuery("SELECT COUNT(a.idAutorizacion) FROM CitAutorizaciones a WHERE a.cerrada = false");
            return Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }    
    
    public List<CitAutorizaciones> findAutorizacionesNoCerradas(int idPaciente, int offset, int limit) {
        try {
            Query query = getEntityManager().createQuery("SELECT a FROM CitAutorizaciones a WHERE a.paciente.idPaciente = ?1 AND a.cerrada = false");
            query.setParameter(1, idPaciente);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public int totalAutorizacionesNoCerradasByPaciente(int idPaciente) {
        try {
            Query query = getEntityManager().createQuery("SELECT COUNT(a.idAutorizacion) FROM CitAutorizaciones a WHERE  a.paciente.idPaciente = ?1 AND a.cerrada = false");
            query.setParameter(1, idPaciente);
            return Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }     
    
    public CitAutorizaciones findAutorizacionDisponible(int idPaciente, int idServicio, int idAdministradora) {
        try {
            Query query = getEntityManager().createQuery("SELECT a FROM CitAutorizaciones a WHERE a.paciente.idPaciente = ?1 AND EXISTS(SELECT sa FROM a.citAutorizacionesServiciosList sa WHERE sa.facServicio.idServicio = ?2 AND (sa.sesionesPendientes > 0 OR sa.sesionesRealizadas < sa.sesionesAutorizadas)) AND a.administradora.idAdministradora = ?3 AND a.cerrada = false");
            query.setParameter(1, idPaciente);
            query.setParameter(2, idServicio);
            query.setParameter(3, idAdministradora);
            return (CitAutorizaciones) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CitAutorizaciones> buscarAutorizacionReporte(int i, boolean cerrada, List<FacAdministradora> administradoras, List<CfgPacientes> pacientes, String numAutorizacion) {
        boolean ban = false;//bandera que controla si se ha insertado un WHERE, en caso negativo se concatena a la query y en caso afirmativo se concatena un AND
        try {
//            String sql = "SELECT sa FROM CitAutorizacionesServicios sa WHERE sa.citAutorizaciones.cerrada = ?1 AND sa.citAutorizaciones.administradora.idAdministradora IN ?2 AND sa.citAutorizaciones.paciente.idPaciente IN ?3 AND sa.citAutorizaciones.numAutorizacion = ?4 GROUP BY sa.citAutorizaciones.administradora, sa.citAutorizaciones.paciente, sa.facServicio, sa.citAutorizaciones ORDER BY sa.citAutorizaciones DESC";
//            Query query = getEntityManager().createQuery("SELECT a FROM CitAutorizaciones a GROUP BY a.administradora, a.paciente, a.idAutorizacion ORDER BY a.idAutorizacion DESC");
            String sql = "SELECT a FROM CitAutorizaciones a ";
            if (i != 0) {// si no se ha especificado cerradas o no cerradas
                if (!ban) {
                    sql = sql.concat(" WHERE ");
                }
                sql = sql.concat(" a.cerrada = ?1 ");
                ban = true;
            }
            if (!administradoras.isEmpty()) {//si se ha especificado alguna administradora
                if (ban) {
                    sql = sql.concat(" AND ");
                } else {
                    sql = sql.concat(" WHERE ");
                }
                sql = sql.concat(" a.administradora IN ?2 ");
                ban = true;
            }
            if (!pacientes.isEmpty()) {//si se ha especificado algun paciente
                if (ban) {
                    sql = sql.concat(" AND ");
                } else {
                    sql = sql.concat(" WHERE ");
                }
                sql = sql.concat(" a.paciente IN ?3 ");
                ban = true;
            }
            if (numAutorizacion != null) {
                if (!numAutorizacion.isEmpty()) {
                    if (ban) {
                        sql = sql.concat(" AND ");
                    } else {
                        sql = sql.concat(" WHERE ");
                    }
                    sql = sql.concat(" a.numAutorizacion = ?4 ");
//                ban = true;
                }
            }
            sql = sql.concat(" GROUP BY a.administradora, a.paciente, a.idAutorizacion ORDER BY a.administradora, a.paciente, a.idAutorizacion DESC");
//            System.out.println(sql);
            Query query = getEntityManager().createQuery(sql);
//            incluyendo parametros
            if (ban) {
                if (i != 0) {
                    query.setParameter(1, cerrada);
                }
                if (!administradoras.isEmpty()) {
                    query.setParameter(2, administradoras);
                }
                if (!pacientes.isEmpty()) {
                    query.setParameter(3, pacientes);
                }
                if (numAutorizacion != null) {
                    if (!numAutorizacion.isEmpty()) {
                        query.setParameter(4, numAutorizacion);
                    }
                }
            }
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
