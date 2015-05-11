/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import javax.ejb.Stateless;
import modelo.entidades.CfgMaestrosTxtPredefinidos;

/**
 *
 * @author santos
 */
@Stateless
public class CfgMaestrosTxtPredefinidosFacade extends AbstractFacade<CfgMaestrosTxtPredefinidos> {
    
    public CfgMaestrosTxtPredefinidosFacade() {
        super(CfgMaestrosTxtPredefinidos.class);
    }
    
    public CfgMaestrosTxtPredefinidos buscarPorNombre(String nombre) {
        try {
            return getEntityManager().createNamedQuery("CfgMaestrosTxtPredefinidos.findByNombre", CfgMaestrosTxtPredefinidos.class).setParameter("nombre", nombre).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }  
}
