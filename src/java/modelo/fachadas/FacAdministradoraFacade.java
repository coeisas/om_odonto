/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.FacAdministradora;

/**
 *
 * @author santos
 */
@Stateless
public class FacAdministradoraFacade extends AbstractFacade<FacAdministradora> {

    public FacAdministradoraFacade() {
        super(FacAdministradora.class);
    }

    public List<FacAdministradora> buscarOrdenado() {
        try {
            String hql = "SELECT o FROM FacAdministradora o ORDER By o.idAdministradora";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }

    }

    public FacAdministradora buscarPorCodigo(String codigo) {
        try {
            Query query = getEntityManager().createQuery("SELECT a FROM FacAdministradora a WHERE a.codigoAdministradora = ?1").setParameter(1, codigo);
            return (FacAdministradora) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    public FacAdministradora buscarPorNombre(String nombre) {
        try {
            Query query = getEntityManager().createQuery("SELECT a FROM FacAdministradora a WHERE a.razonSocial = ?1").setParameter(1, nombre);
            return (FacAdministradora) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<FacAdministradora> buscarAdmiFaltanFacturar(){
        try {
            String hql = "SELECT a FROM FacAdministradora a WHERE a.anulada = FALSE and facturadaEnAmi = FALSE AND a.idAdministradora != 1";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}
