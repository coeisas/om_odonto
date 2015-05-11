/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import modelo.entidades.CfgOpcionesMenu;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author santos
 */
@Stateless
public class CfgOpcionesMenuFacade extends AbstractFacade<CfgOpcionesMenu> {

    public CfgOpcionesMenuFacade() {
        super(CfgOpcionesMenu.class);
    }

    public List<CfgOpcionesMenu> buscarOpcionesOrdenado() {
        try {
            String hql = "SELECT o FROM CfgOpcionesMenu o ORDER By o.idOpcionPadre, o.idOpcionMenu";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
