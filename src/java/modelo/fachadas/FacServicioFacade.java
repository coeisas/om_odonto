/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.FacServicio;

/**
 *
 * @author santos
 */
@Stateless
public class FacServicioFacade extends AbstractFacade<FacServicio> {

    public FacServicioFacade() {
        super(FacServicio.class);
    }

    public List<FacServicio> consultaNativaServicios(String sql) {
        List<FacServicio> listaServicios = (List<FacServicio>) getEntityManager().createNativeQuery(sql, FacServicio.class).getResultList();
        return listaServicios;
    }

    public int numeroTotalRegistros() {// total de visibles y no visibles
        try {
            return Integer.parseInt(getEntityManager().createNativeQuery("SELECT COUNT(*) FROM fac_servicio").getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public List<FacServicio> buscarActivos() {
        try {
            String hql = "SELECT f FROM FacServicio f WHERE f.visible = true ORDER BY f.codigoServicio";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<FacServicio> findAutorizacionReqAndVisible() {
        try {
            Query query = getEntityManager().createQuery("SELECT f FROM FacServicio f WHERE f.visible = true AND f.autorizacion = true ORDER BY f.codigoServicio");
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<FacServicio> buscarTodosOrdenado() {//busca visibles y no visibles
        try {
            String hql = "SELECT s FROM FacServicio s ORDER BY s.idServicio ASC";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<FacServicio> buscarNoEstanEnManual(Integer idManualTarifario) {//busca los servicios que no se encuentren en un manual tarifario
        try {
            String sql = "SELECT * FROM fac_servicio WHERE visible = true AND id_servicio NOT IN (SELECT id_servicio FROM fac_manual_tarifario_servicio WHERE id_manual_tarifario = " + idManualTarifario.toString() + ") ORDER BY id_servicio";
            List<FacServicio> listaServicios = (List<FacServicio>) getEntityManager().createNativeQuery(sql, FacServicio.class).getResultList();
            return listaServicios;
        } catch (Exception e) {
            return null;
        }
    }

    public List<FacServicio> buscarNoEstanEnPaquete(Integer idPaquete) {//busca los servicios que no se encuentren en un manual tarifario
        try {
            String sql = "SELECT * FROM fac_servicio WHERE visible = true AND id_servicio NOT IN (SELECT id_servicio FROM fac_paquete_servicio WHERE id_paquete = " + idPaquete.toString() + ") ORDER BY id_servicio";
            List<FacServicio> listaServicios = (List<FacServicio>) getEntityManager().createNativeQuery(sql, FacServicio.class).getResultList();
            return listaServicios;
        } catch (Exception e) {
            return null;
        }
    }

    public FacServicio buscarPorIdServicio(Integer idservicio) {
        try {
            Query query = getEntityManager().createQuery("SELECT s FROM FacServicio s WHERE s.idServicio = ?1");
            query.setParameter(1, idservicio);
            return (FacServicio) query.getSingleResult();
        } catch (Exception e) {
            return null;
}
    }

}
