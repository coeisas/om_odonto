/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import modelo.entidades.RipsAlmacenados;

/**
 *
 * @author santos
 */
@Stateless
public class RipsAlmacenadosFacade extends AbstractFacade<RipsAlmacenados> {

    public RipsAlmacenadosFacade() {
        super(RipsAlmacenados.class);
    }

    public void resetearSecuencia() {
        this.consultaNativaConteo(""
                + " SELECT "
                + "   setval"
                + "   ('public.rips_almacenados_id_rip_almacenado_seq', "
                + "      (SELECT MAX(id_rip_almacenado) FROM rips_almacenados), true);");
    }

    public RipsAlmacenados buscarPorNombre(String nombre) {
        try {
            return getEntityManager().createNamedQuery("RipsAlmacenados.findByNombre", RipsAlmacenados.class).setParameter("nombre", nombre).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<RipsAlmacenados> buscarOrdenado() {
        try {
            String hql = "SELECT c FROM RipsAlmacenados c ORDER By c.fecha DESC";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}
