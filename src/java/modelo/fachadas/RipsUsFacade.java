/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import modelo.entidades.RipsUs;

/**
 *
 * @author santos
 */
@Stateless
public class RipsUsFacade extends AbstractFacade<RipsUs> {

    public RipsUsFacade() {
        super(RipsUs.class);
    }

    public RipsUs buscarPorIdAlmacenadoYIdentificacion(int idRipAlmacenado, String numIde) {
        try {
            String hql = "SELECT c FROM RipsUs c WHERE c.ripsUsPK.idRipAlmacenado = :idRipAlmacenado AND c.numIde = :numIde";
            return (RipsUs) getEntityManager().createQuery(hql).setParameter("idRipAlmacenado", idRipAlmacenado).setParameter("numIde", numIde).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
