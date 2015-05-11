/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import javax.ejb.Stateless;
import modelo.entidades.FacConsumoMedicamento;

/**
 *
 * @author santos
 */
@Stateless
public class FacConsumoMedicamentoFacade extends AbstractFacade<FacConsumoMedicamento> {

    public FacConsumoMedicamentoFacade() {
        super(FacConsumoMedicamento.class);
    }
    
}
