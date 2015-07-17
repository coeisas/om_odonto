/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import modelo.entidades.FacContrato;

/**
 *
 * @author santos
 */
@Stateless
public class FacContratoFacade extends AbstractFacade<FacContrato> {

    public FacContratoFacade() {
        super(FacContrato.class);
    }

    public List<FacContrato> buscarOrdenado() {
        try {
            String hql = "SELECT c FROM FacContrato c ORDER By c.codigoContrato";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<FacContrato> buscarPorCodigo(String codigoContrato) {
        try {
            String hql = "SELECT c FROM FacContrato c WHERE c.codigoContrato LIKE '" + codigoContrato + "'";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public FacContrato buscarPorNombre(String nombreContrato) {
        try {
            String hql = "SELECT c FROM FacContrato c WHERE c.descripcion LIKE '" + nombreContrato + "'";
            return (FacContrato) getEntityManager().createQuery(hql).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
