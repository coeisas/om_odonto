/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import modelo.entidades.CfgCups;

/**
 *
 * @author santos
 */
@Stateless
public class CfgCupsFacade extends AbstractFacade<CfgCups> {    

    public CfgCupsFacade() {
        super(CfgCups.class);
    }
    
}
