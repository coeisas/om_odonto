/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.FacManualTarifarioPaquete;

/**
 *
 * @author santos
 */
@Stateless
public class FacManualTarifarioPaqueteFacade extends AbstractFacade<FacManualTarifarioPaquete> {
    
    public FacManualTarifarioPaqueteFacade() {
        super(FacManualTarifarioPaquete.class);
    }
    
    public List<FacManualTarifarioPaquete> buscarPorManualTarifario(Integer idManual) {
        Query query;
        try {
            query = getEntityManager().createQuery("SELECT m FROM FacManualTarifarioPaquete m WHERE m.facManualTarifario.idManualTarifario = :idManual ORDER BY m.facManualTarifarioPaquetePK.idPaquete").setParameter("idManual", idManual);
            return query.getResultList();
        } catch (Exception e) {
            return null;
}
    }
}
