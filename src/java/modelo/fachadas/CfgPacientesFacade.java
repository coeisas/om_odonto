/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import modelo.entidades.CfgPacientes;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author santos
 */
@Stateless
public class CfgPacientesFacade extends AbstractFacade<CfgPacientes> {

    public List<CfgPacientes> consultaNativaPacientes(String sql) {
        List<CfgPacientes> listaPacientes = (List<CfgPacientes>) getEntityManager().createNativeQuery(sql, CfgPacientes.class).getResultList();
        return listaPacientes;
    }   
    
    public List<CfgPacientes> buscarOrdenado() {
        try {
            String hql = "SELECT c FROM CfgPacientes c ORDER BY c.primerNombre ASC";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public int numeroTotalRegistros() {
        try {            
            return Integer.parseInt(getEntityManager().createNativeQuery("SELECT COUNT(*) FROM cfg_pacientes").getSingleResult().toString());            
        } catch (Exception e) {
            return 0;
        }
    }

    public CfgPacientesFacade() {
        super(CfgPacientes.class);
    }

    public CfgPacientes buscarPorIdentificacion(String identificacion) {
        try {
            return getEntityManager().createNamedQuery("CfgPacientes.findByIdentificacion", CfgPacientes.class).setParameter("identificacion", identificacion).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgPacientes findPacienteByTipIden(int id, String identificacion) {
        CfgPacientes paciente;
        try {
            Query query = getEntityManager().createQuery("SELECT p FROM CfgPacientes p WHERE p.identificacion=?1 AND p.tipoIdentificacion.id=?2");
            query.setParameter(1, identificacion);
            query.setParameter(2, id);
            paciente = (CfgPacientes) query.getSingleResult();
        } catch (Exception e) {
            paciente = null;
        }
        return paciente;
    }

    public List<CfgPacientes> findLazyPaciente(int limit, int offset) {
        CfgPacientes paciente;
        try {
            Query query = getEntityManager().createQuery("SELECT p FROM CfgPacientes p WHERE p.identificacion=?1 AND p.tipoIdentificacion.id=?2");
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public EntityManager obtenerEntityManager() {
        return getEntityManager();
    }

}
