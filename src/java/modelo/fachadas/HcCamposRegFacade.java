/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import modelo.entidades.HcCamposReg;

/**
 *
 * @author santos
 */
@Stateless
public class HcCamposRegFacade extends AbstractFacade<HcCamposReg> {

    public HcCamposRegFacade() {
        super(HcCamposReg.class);
    }

    public HcCamposReg buscarPorTipoRegistroYPosicion(int idTipoRegistro, int posicion) {
        try {
            String hql = "SELECT h FROM HcCamposReg h WHERE h.idTipoReg.idTipoReg = :idTipoReg AND h.posicion = :posicion";
            return (HcCamposReg) getEntityManager().createQuery(hql).setParameter("idTipoReg", idTipoRegistro).setParameter("posicion", posicion).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<HcCamposReg> buscarPorTipoRegistro(int idTipoRegistro) {
        try {
            String hql = "SELECT h FROM HcCamposReg h WHERE h.idTipoReg.idTipoReg = :idTipoReg";
            return getEntityManager().createQuery(hql).setParameter("idTipoReg", idTipoRegistro).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}
