/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import javax.ejb.Stateless;
import modelo.entidades.CfgEmpresa;

/**
 *
 * @author mario
 */
@Stateless
public class CfgEmpresaFacade extends AbstractFacade<CfgEmpresa> {

    public CfgEmpresaFacade() {
        super(CfgEmpresa.class);
    }
    
}
