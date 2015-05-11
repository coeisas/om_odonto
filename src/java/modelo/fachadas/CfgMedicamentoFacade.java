/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import modelo.entidades.CfgMedicamento;

/**
 *
 * @author santos
 */
@Stateless
public class CfgMedicamentoFacade extends AbstractFacade<CfgMedicamento> {

    public CfgMedicamentoFacade() {
        super(CfgMedicamento.class);
    }

    public List<CfgMedicamento> buscarOrdenado() {
        try {
            String hql = "SELECT m FROM CfgMedicamento m ORDER BY m.idMedicamento ASC";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CfgMedicamento> buscarNoEstanEnManual(Integer idManualTarifario) {//medicamento que no se encuentra en un amnual tarifario
        try {
            String sql = "SELECT * FROM cfg_medicamento WHERE id_medicamento NOT IN (SELECT id_medicamento FROM fac_manual_tarifario_medicamento WHERE id_manual_tarifario = " + idManualTarifario.toString() + ") ORDER BY id_medicamento";
            List<CfgMedicamento> listaMedicamentos = (List<CfgMedicamento>) getEntityManager().createNativeQuery(sql, CfgMedicamento.class).getResultList();
            return listaMedicamentos;
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<CfgMedicamento> buscarNoEstanEnPaquete(Integer idPaquete) {//medicamentos que no se encuentran en un paquete
        try {
            String sql = "SELECT * FROM cfg_medicamento WHERE id_medicamento NOT IN (SELECT id_medicamento FROM fac_paquete_medicamento WHERE id_paquete = " + idPaquete.toString() + ") ORDER BY id_medicamento";
            List<CfgMedicamento> listaMedicamentos = (List<CfgMedicamento>) getEntityManager().createNativeQuery(sql, CfgMedicamento.class).getResultList();
            return listaMedicamentos;
        } catch (Exception e) {
            return null;
        }
    }
}
