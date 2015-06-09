/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import modelo.entidades.FacFacturaPaciente;

/**
 *
 * @author santos
 */
@Stateless
public class FacFacturaPacienteFacade extends AbstractFacade<FacFacturaPaciente> {

    public FacFacturaPacienteFacade() {
        super(FacFacturaPaciente.class);
    }

    public List<FacFacturaPaciente> buscarOrdenado() {
        try {
            String hql = "SELECT i FROM FacFactura i ORDER BY i.idFactura ASC";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<FacFacturaPaciente> buscarPorAministradora(String administradora) {
        try {
            String hql = "SELECT f FROM FacFactura f WHERE f.idAdministradora.idAdministradora = " + administradora + " ORDER BY f.fecha DESC";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<FacFacturaPaciente> buscarPorAutorizacion(String idAutorizacion) {
        try {
            String sql = ""
                    + " select * from fac_factura_paciente where id_cita in "
                    + " (select id_cita from cit_citas where id_autorizacion="+idAutorizacion+")";
            return (List<FacFacturaPaciente>) getEntityManager().createNativeQuery(sql, FacFacturaPaciente.class).getResultList();
            
        } catch (Exception e) {
            return null;
        }
    }

    public int numeroTotalRegistros() {
        try {
            return Integer.parseInt(getEntityManager().createNativeQuery("SELECT COUNT(*) FROM fac_factura_paciente").getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public List<FacFacturaPaciente> consultaNativaFacturas(String sql) {
        List<FacFacturaPaciente> listaFacturas = (List<FacFacturaPaciente>) getEntityManager().createNativeQuery(sql, FacFacturaPaciente.class).getResultList();
        return listaFacturas;
    }
    
    public List<FacFacturaPaciente> buscarFaltanFacturar() {
        try {
            String hql = "SELECT a FROM FacFacturaPaciente a WHERE a.anulada = false and a.facturadaEnAdmi = false AND a.idAdministradora.idAdministradora != 1";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}
