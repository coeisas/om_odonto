/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import javax.ejb.Stateless;
import modelo.entidades.CfgHorario;

/**
 *
 * @author santos
 */
@Stateless
public class CfgHorarioFacade extends AbstractFacade<CfgHorario> {

    public CfgHorarioFacade() {
        super(CfgHorario.class);
    }

    public CfgHorario buscarPorDescripcion(String descripcion) {
        try {
            return getEntityManager().createNamedQuery("CfgHorario.findByDescripcion", CfgHorario.class).setParameter("descripcion", descripcion).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
