/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import modelo.entidades.CfgInsumo;

/**
 *
 * @author santos
 */
@Stateless
public class CfgInsumoFacade extends AbstractFacade<CfgInsumo> {

    public CfgInsumoFacade() {
        super(CfgInsumo.class);
    }

    public List<CfgInsumo> buscarOrdenado() {
        try {
            String hql = "SELECT i FROM CfgInsumo i ORDER BY i.idInsumo ASC";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CfgInsumo> buscarNoEstanEnManual(Integer idManualTarifario) {//insumo que no se encuetra en un manual tarifario
        try {
            String sql = "SELECT * FROM cfg_insumo WHERE id_insumo NOT IN (SELECT id_insumo FROM fac_manual_tarifario_insumo WHERE id_manual_tarifario = " + idManualTarifario.toString() + ") ORDER BY id_insumo";
            List<CfgInsumo> listaInsumos = (List<CfgInsumo>) getEntityManager().createNativeQuery(sql, CfgInsumo.class).getResultList();
            return listaInsumos;
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<CfgInsumo> buscarNoEstanEnPaquete(Integer idPaquete) {//insumo que no se encuetra en un manual tarifario
        try {
            String sql = "SELECT * FROM cfg_insumo WHERE id_insumo NOT IN (SELECT id_insumo FROM fac_paquete_insumo WHERE id_paquete = " + idPaquete.toString() + ") ORDER BY id_insumo";
            List<CfgInsumo> listaInsumos = (List<CfgInsumo>) getEntityManager().createNativeQuery(sql, CfgInsumo.class).getResultList();
            return listaInsumos;
        } catch (Exception e) {
            return null;
        }
    }
}
