/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import modelo.entidades.FacPaquete;

/**
 *
 * @author santos
 */
@Stateless
public class FacPaqueteFacade extends AbstractFacade<FacPaquete> {

    public FacPaqueteFacade() {
        super(FacPaquete.class);
    }

    public List<FacPaquete> buscarOrdenado() {
        try {
            String hql = "SELECT p FROM FacPaquete p ORDER BY p.idPaquete ASC";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<FacPaquete> buscarNoEstanEnManual(Integer idManualTarifario) {
        try {
            String sql = "SELECT * FROM fac_paquete WHERE id_paquete NOT IN (SELECT id_paquete FROM fac_manual_tarifario_paquete WHERE id_manual_tarifario = " + idManualTarifario.toString() + ") ORDER BY id_paquete";
            List<FacPaquete> listaPaquetes = (List<FacPaquete>) getEntityManager().createNativeQuery(sql, FacPaquete.class).getResultList();
            return listaPaquetes;
        } catch (Exception e) {
            return null;
        }
    }
}
