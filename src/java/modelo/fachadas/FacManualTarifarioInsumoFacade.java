/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.FacManualTarifarioInsumo;

/**
 *
 * @author santos
 */
@Stateless
public class FacManualTarifarioInsumoFacade extends AbstractFacade<FacManualTarifarioInsumo> {
    
    public FacManualTarifarioInsumoFacade() {
        super(FacManualTarifarioInsumo.class);
    }
    
    public List<FacManualTarifarioInsumo> buscarPorManualTarifario(Integer idManual) {
        Query query;
        try {
            query = getEntityManager().createQuery("SELECT m FROM FacManualTarifarioInsumo m WHERE m.facManualTarifario.idManualTarifario = :idManual ORDER BY m.facManualTarifarioInsumoPK.idInsumo").setParameter("idManual", idManual);
            return query.getResultList();
        } catch (Exception e) {
            return null;
}
    }
}
