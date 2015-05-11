/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import modelo.entidades.FacCaja;

/**
 *
 * @author santos
 */
@Stateless
public class FacCajaFacade extends AbstractFacade<FacCaja> {

    public FacCajaFacade() {
        super(FacCaja.class);
    }

    public List<FacCaja> buscarOrdenado() {
        try {
            String hql = "SELECT u FROM FacCaja u ORDER BY u.nombreCaja";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<FacCaja> buscarPorSede(String sede) {
        try {
            String hql = "SELECT f FROM FacCaja f WHERE f.idSede.idSede = " + sede + " ORDER BY f.idCaja ASC";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
