/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import modelo.entidades.CfgDiagnostico;

/**
 *
 * @author santos
 */
@Stateless
public class CfgDiagnosticoFacade extends AbstractFacade<CfgDiagnostico> {

    public CfgDiagnosticoFacade() {
        super(CfgDiagnostico.class);
    }

    public List<String> autocompletarDiagnostico(String txt) {
        try {
            return getEntityManager().createNativeQuery("select codigo_diagnostico||' - '||nombre_diagnostico from cfg_diagnostico where codigo_diagnostico||' - '||nombre_diagnostico ilike '%" + txt + "%' limit 10").getResultList();
        } catch (Exception e) {                          
            return null;
        }
    }
    
    public CfgDiagnostico buscarPorNombre(String nombreDiagnostico) {
        String hql = "SELECT c FROM CfgDiagnostico c WHERE c.nombreDiagnostico = :nombreDiagnostico";
        try {
            return (CfgDiagnostico) getEntityManager().createQuery(hql).setParameter("nombreDiagnostico", nombreDiagnostico).getSingleResult();
        } catch (Exception e) {
            System.out.println(e.toString() + "----------------------------------------------------");
            return null;
        }
    }
    

}
