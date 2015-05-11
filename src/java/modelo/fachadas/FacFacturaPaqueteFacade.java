/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import javax.ejb.Stateless;
import modelo.entidades.FacFacturaPaquete;

/**
 *
 * @author santos
 */
@Stateless
public class FacFacturaPaqueteFacade extends AbstractFacade<FacFacturaPaquete> {

    public FacFacturaPaqueteFacade() {
        super(FacFacturaPaquete.class);
    }

}
