/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import javax.ejb.Stateless;
import modelo.entidades.CfgTxtPredefinidos;

/**
 *
 * @author santos
 */
@Stateless
public class CfgTxtPredefinidosFacade extends AbstractFacade<CfgTxtPredefinidos> {    

    public CfgTxtPredefinidosFacade() {
        super(CfgTxtPredefinidos.class);
    }
    public CfgTxtPredefinidos buscarPorNombre(String nombre) {
        try {
            return getEntityManager().createNamedQuery("CfgTxtPredefinidos.findByNombre", CfgTxtPredefinidos.class).setParameter("nombre", nombre).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
