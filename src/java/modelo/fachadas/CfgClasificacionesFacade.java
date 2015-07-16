/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import modelo.entidades.CfgClasificaciones;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author santos
 */
@Stateless
public class CfgClasificacionesFacade extends AbstractFacade<CfgClasificaciones> {

    public CfgClasificacionesFacade() {
        super(CfgClasificaciones.class);
    }

    public List<CfgClasificaciones> buscarPorMaestro(String maestro) {
        try {
            String hql = "SELECT c FROM CfgClasificaciones c WHERE c.maestro.maestro = :maestro order by c.codigo ASC";
            return getEntityManager().createQuery(hql).setParameter("maestro", maestro).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CfgClasificaciones> buscarPorMaestroObservacion(String maestro, String observacion) {
        try {
            String hql = "SELECT c FROM CfgClasificaciones c WHERE c.maestro.maestro = :maestro AND c.observacion = :observacion order by c.codigo";
            return getEntityManager().createQuery(hql).setParameter("maestro", maestro).setParameter("observacion", observacion).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgClasificaciones buscarPorMaestroDescripcion(String maestro, String descripcion) {
        try {
            String hql = "SELECT c FROM CfgClasificaciones c WHERE c.maestro.maestro = :maestro AND c.descripcion = :descripcion";
            return (CfgClasificaciones) getEntityManager().createQuery(hql).setParameter("maestro", maestro).setParameter("descripcion", descripcion).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Object> listaClasificaciones() {
        try {
            String hql = "SELECT DISTINCT c.maestro FROM CfgClasificaciones c ORDER BY c.maestro ASC";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CfgClasificaciones> buscarMunicipioPorDepartamento(String departamento) {
        try {
            String hql = "SELECT c FROM CfgClasificaciones c WHERE c.maestro.maestro LIKE 'Municipios' AND c.codigo LIKE '" + departamento + "%' ORDER BY c.codigo";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
