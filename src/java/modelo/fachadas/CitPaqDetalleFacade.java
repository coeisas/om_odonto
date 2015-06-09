/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.CitPaqDetalle;

/**
 *
 * @author Mario
 */
@Stateless
public class CitPaqDetalleFacade extends AbstractFacade<CitPaqDetalle> {

    public CitPaqDetalleFacade() {
        super(CitPaqDetalle.class);
    }
    
    public List<CitPaqDetalle> buscarPorMaestro(int id_maestro){
        Query query = getEntityManager().createQuery("SELECT p FROM CitPaqDetalle p WHERE p.idPaqMaestro.idPaqMaestro = ?1 ORDER BY p.idPaqDetalle");
        query.setParameter(1, id_maestro);
        return query.getResultList();
    }
    
    public void eliminarPorMaestro(int id_maestro){
        Query query = getEntityManager().createQuery("DELETE FROM CitPaqDetalle p WHERE p.idPaqMaestro.idPaqMaestro = ?1");
        query.setParameter(1, id_maestro);
        query.executeUpdate();        
    }
    
}
