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
import modelo.entidades.CitCitas;
import modelo.entidades.HcDetalle;
import modelo.entidades.HcItems;
import modelo.entidades.HcRegistro;

/**
 *
 * @author santos
 */
@Stateless
public class HcRegistroFacade extends AbstractFacade<HcRegistro> {

    public HcRegistroFacade() {
        super(HcRegistro.class);
    }

    public List<HcRegistro> buscarOrdenadoPorFecha(int idPaciente) {//buscar todos los registros clinicos de un paciente
        try {
            String hql = "SELECT u FROM HcRegistro u WHERE u.idPaciente.idPaciente = :idPaciente ORDER BY u.fechaReg DESC";
            return getEntityManager().createQuery(hql).setParameter("idPaciente", idPaciente).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<HcRegistro> buscarOrdenado(int idPaciente, Date fechaInicial, Date fechaFinal) {//buscar registros clinicos filtrando por fecha inicial y final
        try {
            Query query = getEntityManager().createQuery("SELECT u FROM HcRegistro u WHERE u.idPaciente.idPaciente = ?1 AND u.fechaReg >= ?2 AND u.fechaReg <= ?3 ORDER BY u.fechaReg DESC");
            query.setParameter(1, idPaciente);
            query.setParameter(2, fechaInicial);
            query.setParameter(3, fechaFinal);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<HcRegistro> buscarFiltradoPorNumeroAutorizacion(int idPaciente, String numAutorizacion) {//buscar registros clinicos filtrando por Numero Autorizacion
        try {
            List<HcRegistro> listaResultado;
            List<HcRegistro> listaResultadoAux;
            Query query = getEntityManager().createQuery("SELECT u FROM HcRegistro u WHERE u.idPaciente.idPaciente = ?1 ORDER BY u.fechaReg DESC");
            query.setParameter(1, idPaciente);
            listaResultadoAux = query.getResultList();
            listaResultado = new ArrayList<>();
            boolean agregar;
            for (HcRegistro resultado : listaResultadoAux) {
                List<HcDetalle> hcDetalleList = resultado.getHcDetalleList();
                agregar = false;
                for (HcDetalle detalle : hcDetalleList) {
                    if (detalle.getHcCamposReg().getNombre().contains("autorizacion")) {
                        if (detalle.getValor().compareTo(numAutorizacion) == 0) {
                            agregar = true;
                            break;
                        }
                    }
                }
                if (agregar) {
                    listaResultado.add(resultado);
                }
            }
            return listaResultado;
        } catch (Exception e) {
            return null;
        }
    }

    public List<HcRegistro> buscarFiltradoPorNumeroAutorizacionYFecha(int idPaciente, Date fechaInicial, Date fechaFinal, String numAutorizacion) {//buscar registros clinicos filtrando por fecha inicial, fecha final y Numero Autorizacion
        try {
            List<HcRegistro> listaResultado;
            List<HcRegistro> listaResultadoAux;
            Query query = getEntityManager().createQuery("SELECT u FROM HcRegistro u WHERE u.idPaciente.idPaciente = ?1 AND u.fechaReg >= ?2 AND u.fechaReg <= ?3 ORDER BY u.fechaReg DESC");
            query.setParameter(1, idPaciente);
            query.setParameter(2, fechaInicial);
            query.setParameter(3, fechaFinal);
            listaResultadoAux = query.getResultList();
            listaResultado = new ArrayList<>();
            boolean agregar;
            for (HcRegistro resultado : listaResultadoAux) {
                List<HcDetalle> hcDetalleList = resultado.getHcDetalleList();
                agregar = false;
                for (HcDetalle detalle : hcDetalleList) {
                    if (detalle.getHcCamposReg().getNombre().contains("autorizacion")) {
                        if (detalle.getValor().compareTo(numAutorizacion) == 0) {
                            agregar = true;
                            break;
                        }
                    }
                }
                if (agregar) {
                    listaResultado.add(resultado);
                }
            }
            return listaResultado;
        } catch (Exception e) {
            return null;
        }
    }

    public HcRegistro buscarUltimo(Integer idPaciente, Integer idTipoReg) {//usado para cargar la ultima historia ingresada, de un tipo de registro especifico
        try {
            String hql = "SELECT u FROM HcRegistro u WHERE u.idPaciente.idPaciente = :idPaciente AND u.idTipoReg.idTipoReg = :idTipoReg ORDER BY u.fechaReg DESC";
            List<HcRegistro> listaResultado = getEntityManager().createQuery(hql).setMaxResults(1).setParameter("idPaciente", idPaciente).setParameter("idTipoReg", idTipoReg).getResultList();
            if (listaResultado.isEmpty()) {
                return null;
            } else {
                return listaResultado.get(0);
            }

        } catch (Exception e) {
            return null;
        }
    }

    public HcRegistro buscarRegistroConDiagnosticoSegunCita(String idCita) {
        try {
            return (HcRegistro) getEntityManager().createNativeQuery(""
                    + " select * \n"
                    + " from hc_registro  \n"
                    + " where \n"
                    + " id_cita = " + idCita + " AND \n"
                    + " id_tipo_reg in (1,2,5,7,8)  \n"
                    + " ORDER BY fecha_reg DESC\n"
                    + " LIMIT 1", HcRegistro.class).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public HcRegistro buscarRegistroConDiagnosticoSegunPaciente(String idPaciente) {
        try {
            return (HcRegistro) getEntityManager().createNativeQuery(""
                    + " select * \n"
                    + " from hc_registro  \n"
                    + " where \n"
                    + " id_paciente = " + idPaciente + " AND \n"
                    + " id_tipo_reg in (1,2,5,7,8)  \n"
                    + " ORDER BY fecha_reg DESC\n"
                    + " LIMIT 1", HcRegistro.class).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public int buscarMaximoFolio(Integer idPaciente) {
        try {            
            return Integer.parseInt(getEntityManager().createNativeQuery("SELECT MAX(folio) FROM hc_registro WHERE id_paciente = " + idPaciente).getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }

}
