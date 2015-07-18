/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.FacManualTarifarioServicio;

/**
 *
 * @author mario
 */
@Stateless
public class FacManualTarifarioServicioFacade extends AbstractFacade<FacManualTarifarioServicio> {

    public FacManualTarifarioServicioFacade() {
        super(FacManualTarifarioServicio.class);
    }
    
    public List<FacManualTarifarioServicio> findManualTarifarioServicioByProgramas(List<Integer> programas){
        Query query;
        try{
            query = getEntityManager().createQuery("SELECT m FROM FacManualTarifarioServicio m WHERE m.facManualTarifario.idManualTarifario IN ?1");
            query.setParameter(1, programas);
            return query.getResultList();
        }catch(Exception e){
            return null;
        }
        
    }
    
    public List<FacManualTarifarioServicio> findManualTarifarioServicioAutorizacionRequiredByProgramas(List<Integer> programas){
        Query query;
        try{
            query = getEntityManager().createQuery("SELECT m FROM FacManualTarifarioServicio m WHERE m.facManualTarifario.idManualTarifario IN ?1 AND m.facServicio.autorizacion = true");
            query.setParameter(1, programas);
            return query.getResultList();
        }catch(Exception e){
            return null;
        }
        
    }    
    
    public List<FacManualTarifarioServicio> buscarPorManualTarifario(Integer idManual){
        Query query;
        try{
            query = getEntityManager().createQuery("SELECT m FROM FacManualTarifarioServicio m WHERE m.facManualTarifario.idManualTarifario = :idManual ORDER BY m.facManualTarifarioServicioPK.idServicio").setParameter("idManual", idManual);            
            return query.getResultList();
        }catch(Exception e){
            return null;
}
    }    
    
}
