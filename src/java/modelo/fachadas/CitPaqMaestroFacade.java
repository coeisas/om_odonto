/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.CitPaqMaestro;

/**
 *
 * @author Mario
 */
@Stateless
public class CitPaqMaestroFacade extends AbstractFacade<CitPaqMaestro> {

    public CitPaqMaestroFacade() {
        super(CitPaqMaestro.class);
    }

    public CitPaqMaestro buscarPorCodigo(String codigo) {
        try {
            Query query = getEntityManager().createQuery("SELECT p FROM CitPaqMaestro p WHERE p.codPaquete = ?1");
            query.setParameter(1, codigo);
            return (CitPaqMaestro) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
