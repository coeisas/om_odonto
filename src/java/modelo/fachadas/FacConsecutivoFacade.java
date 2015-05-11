/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import modelo.entidades.CfgMedicamento;
import modelo.entidades.FacConsecutivo;

/**
 *
 * @author santos
 */
@Stateless
public class FacConsecutivoFacade extends AbstractFacade<FacConsecutivo> {
    
    public FacConsecutivoFacade() {
        super(FacConsecutivo.class);
    }
    
    public List<FacConsecutivo> buscarOrdenado() {
        try {
            String hql = "SELECT c FROM FacConsecutivo c ORDER BY c.idConsecutivo ASC";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    public List<FacConsecutivo> buscarPorTipoDocumento(Integer tipo) {
        try {
            String hql = "SELECT c FROM FacConsecutivo c WHERE c.tipoDocumento.id = :tipo ORDER BY c.idConsecutivo";
            return getEntityManager().createQuery(hql).setParameter("tipo", tipo).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
}
