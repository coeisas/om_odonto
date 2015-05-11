/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import javax.ejb.Stateless;
import modelo.entidades.RipsAp;

/**
 *
 * @author santos
 */
@Stateless
public class RipsApFacade extends AbstractFacade<RipsAp> {

    public RipsApFacade() {
        super(RipsAp.class);
    }    

}
