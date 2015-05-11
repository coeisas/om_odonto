/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TemporalType;
import modelo.entidades.CfgMaestrosClasificaciones;
import modelo.entidades.CfgUsuarios;

/**
 *
 * @author santos
 */
@Stateless
public class CfgMaestrosClasificacionesFacade extends AbstractFacade<CfgMaestrosClasificaciones> {

    public CfgMaestrosClasificacionesFacade() {
        super(CfgMaestrosClasificaciones.class);
    }

    public List<CfgMaestrosClasificaciones> buscarOrdenado() {
        try {
            String hql = "SELECT u FROM CfgMaestrosClasificaciones u ORDER BY u.maestro ASC";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgMaestrosClasificaciones buscarPorNombre(String maestro) {
        try {
            return (CfgMaestrosClasificaciones) getEntityManager().createNativeQuery("select * from cfg_maestros_clasificaciones where maestro ilike '" + maestro + "' limit 1", CfgMaestrosClasificaciones.class).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
