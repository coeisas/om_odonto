/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import modelo.entidades.FacPeriodo;
import modelo.entidades.FacUnidadValor;

/**
 *
 * @author santos
 */
@Stateless
public class FacUnidadValorFacade extends AbstractFacade<FacUnidadValor> {

    public FacUnidadValorFacade() {
        super(FacUnidadValor.class);
    }

    public List<FacUnidadValor> buscarOrdenado() {
        try {
            String hql = "SELECT c FROM FacUnidadValor c ORDER BY c.anio ASC";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public FacUnidadValor buscarPorAnio(Integer anio) {
        String hql = "SELECT c FROM FacUnidadValor c WHERE c.anio = :anio";
        try {
            return (FacUnidadValor) getEntityManager().createQuery(hql).setParameter("anio", anio).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
