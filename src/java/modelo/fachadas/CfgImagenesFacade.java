/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import modelo.entidades.CfgImagenes;
import javax.ejb.Stateless;

/**
 *
 * @author santos
 */
@Stateless
public class CfgImagenesFacade extends AbstractFacade<CfgImagenes> {

    public CfgImagenesFacade() {
        super(CfgImagenes.class);
    }

}
