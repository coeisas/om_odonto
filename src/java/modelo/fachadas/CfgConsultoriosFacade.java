/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.CfgConsultorios;

/**
 *
 * @author mario
 */
@Stateless
public class CfgConsultoriosFacade extends AbstractFacade<CfgConsultorios> {

    public CfgConsultoriosFacade() {
        super(CfgConsultorios.class);
    }
    public List<CfgConsultorios> buscarOrdenado() {
        try {
            String hql = "SELECT c FROM CfgConsultorios c ORDER BY c.idConsultorio ASC";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<CfgConsultorios> getConsultoriosBySedeAndEspecialidad(int id_sede, int id_especialidad){
        Query query = getEntityManager().createQuery("SELECT c FROM CfgConsultorios c WHERE c.idSede.idSede = ?1 AND c.codEspecialidad.id = ?2");
        query.setParameter(1, id_sede);
        query.setParameter(2, id_especialidad);
        return query.getResultList();
    }
    
        public List<CfgConsultorios> getBySede(int id_sede){
        Query query = getEntityManager().createQuery("SELECT c FROM CfgConsultorios c WHERE c.idSede.idSede = ?1");
        query.setParameter(1, id_sede);
        return query.getResultList();
    }
    
    public List<CfgConsultorios> getConsultoriosByEspecialidad(int id_especialidad){
        Query query = getEntityManager().createQuery("SELECT c FROM CfgConsultorios c WHERE c.codEspecialidad.id = ?1");
        query.setParameter(1, id_especialidad);
        return query.getResultList();
    }       
        
}
