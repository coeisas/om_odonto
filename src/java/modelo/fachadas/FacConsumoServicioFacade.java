/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import javax.ejb.Stateless;
import modelo.entidades.FacConsumoServicio;

/**
 *
 * @author santos
 */
@Stateless
public class FacConsumoServicioFacade extends AbstractFacade<FacConsumoServicio> {

    public FacConsumoServicioFacade() {
        super(FacConsumoServicio.class);
    }
    
}
