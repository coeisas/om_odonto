/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import modelo.entidades.FacPeriodo;

/**
 *
 * @author santos
 */
@Stateless
public class FacPeriodoFacade extends AbstractFacade<FacPeriodo> {

    public FacPeriodoFacade() {
        super(FacPeriodo.class);
    }

    public List<FacPeriodo> buscarOrdenado() {
        try {
            String hql = "SELECT m FROM FacPeriodo m ORDER By m.idPeriodo";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public FacPeriodo buscarPorNombre(String nombre) {
        String hql = "SELECT c FROM FacPeriodo c WHERE c.nombre = :nombre";
        try {
            return (FacPeriodo) getEntityManager().createQuery(hql).setParameter("nombre", nombre).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
