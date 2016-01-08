/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import javax.ejb.Stateless;
import modelo.entidades.HcDetalle;
import java.util.List;
import javax.persistence.Query;
import modelo.entidades.HcRegistro;

/**
 *
 * @author mario
 */
@Stateless
public class HcDetalleFacade extends AbstractFacade<HcDetalle> {

    public HcDetalleFacade() {
        super(HcDetalle.class);
    }

    public List<HcDetalle> buscarPorRegistroAndCampo(HcRegistro registro) {
        try {
            Query query = getEntityManager().createQuery("SELECT h FROM HcDetalle h WHERE h.hcRegistro = ?1 AND h.hcDetallePK.idCampo IN (35,1,2,37,72,57,61,70)");
            query.setParameter(1, registro);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}
