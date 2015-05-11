/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import modelo.entidades.CfgSede;
import javax.ejb.Stateless;

/**
 *
 * @author santos
 */
@Stateless
public class CfgSedeFacade extends AbstractFacade<CfgSede> {
    
    public CfgSedeFacade() {
        super(CfgSede.class);
    }
    public List<CfgSede> buscarOrdenado() {
        try {
            String hql = "SELECT u FROM CfgSede u ORDER BY u.idSede ASC";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
}
