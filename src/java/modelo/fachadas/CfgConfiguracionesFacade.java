/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import javax.ejb.Stateless;
import modelo.entidades.CfgConfiguraciones;

/**
 *
 * @author santos
 */
@Stateless
public class CfgConfiguracionesFacade extends AbstractFacade<CfgConfiguraciones> {

    public CfgConfiguracionesFacade() {
        super(CfgConfiguraciones.class);
    }

}
