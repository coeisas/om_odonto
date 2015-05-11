/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.CfgDiasNoLaborales;

/**
 *
 * @author Mario
 */
@Stateless
public class CfgDiasNoLaboralesFacade extends AbstractFacade<CfgDiasNoLaborales> {

    public CfgDiasNoLaboralesFacade() {
        super(CfgDiasNoLaborales.class);
    }

    public List<CfgDiasNoLaborales> diasNoLaboralesBySede(int id_sede) {
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM CfgDiasNoLaborales c WHERE c.cfgDiasNoLaboralesPK.idSede = ?1 AND c.cfgDiasNoLaboralesPK.fechaNoLaboral >= CURRENT_DATE ORDER BY c.cfgDiasNoLaboralesPK.fechaNoLaboral");
            query.setParameter(1, id_sede);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgDiasNoLaborales FindDiaNoLaboral(List<Integer> id_sedes, Date fecha) {
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM CfgDiasNoLaborales c WHERE c.cfgDiasNoLaboralesPK.idSede IN ?1 AND c.cfgDiasNoLaboralesPK.fechaNoLaboral = ?2");
            query.setParameter(1, id_sedes);
            query.setParameter(2, fecha);
            return (CfgDiasNoLaborales) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgDiasNoLaborales FindDiaNoLaboraleBySede(int id_sede, Date fecha) {
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM CfgDiasNoLaborales c WHERE c.cfgDiasNoLaboralesPK.idSede = ?1 AND c.cfgDiasNoLaboralesPK.fechaNoLaboral = ?2");
            query.setParameter(1, id_sede);
            query.setParameter(2, fecha);
            return (CfgDiasNoLaborales) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
