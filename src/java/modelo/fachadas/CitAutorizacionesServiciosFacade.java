/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.CfgPacientes;
import modelo.entidades.CitAutorizacionesServicios;
import modelo.entidades.FacAdministradora;

/**
 *
 * @author mario
 */
@Stateless
public class CitAutorizacionesServiciosFacade extends AbstractFacade<CitAutorizacionesServicios> {

    public CitAutorizacionesServiciosFacade() {
        super(CitAutorizacionesServicios.class);
    }

    public CitAutorizacionesServicios buscarServicioPorAutorizacion(int idAutorizacion, int idServicio) {
        try {
            Query query = getEntityManager().createQuery("SELECT sa FROM CitAutorizacionesServicios sa WHERE sa.citAutorizaciones.idAutorizacion = ?1 AND sa.facServicio.idServicio = ?2");
            query.setParameter(1, idAutorizacion);
            query.setParameter(2, idServicio);
            return (CitAutorizacionesServicios) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public CitAutorizacionesServicios buscarServicioPorAutorizacionDisponible(int idAutorizacion, int idServicio) {
        try {
            Query query = getEntityManager().createQuery("SELECT sa FROM CitAutorizacionesServicios sa WHERE sa.citAutorizaciones.idAutorizacion = ?1 AND sa.facServicio.idServicio = ?2 AND sa.sesionesPendientes > 0");
            query.setParameter(1, idAutorizacion);
            query.setParameter(2, idServicio);
            return (CitAutorizacionesServicios) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
