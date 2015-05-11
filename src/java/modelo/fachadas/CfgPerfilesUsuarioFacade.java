/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import modelo.entidades.CfgPerfilesUsuario;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author santos
 */
@Stateless
public class CfgPerfilesUsuarioFacade extends AbstractFacade<CfgPerfilesUsuario> {

    public CfgPerfilesUsuarioFacade() {
        super(CfgPerfilesUsuario.class);
    }
    
    public List<CfgPerfilesUsuario> buscarPorNombrePerfil(String nombrePerfil) {
        try {
            return getEntityManager().createNamedQuery("CfgPerfilesUsuario.findByNombrePerfil", CfgPerfilesUsuario.class).setParameter("nombrePerfil", nombrePerfil).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}
