/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.CfgItemsHorario;

/**
 *
 * @author santos
 */
@Stateless
public class CfgItemsHorarioFacade extends AbstractFacade<CfgItemsHorario> {

    public CfgItemsHorarioFacade() {
        super(CfgItemsHorario.class);
    }

    public List<CfgItemsHorario> findHorarioByIdCfgTurno(int id_cfgturno) {
        List<CfgItemsHorario> cfgHorarios;
        try {
            //Query query = getEntityManager().createQuery("SELECT c FROM  CfgItemsHorario c WHERE c.codCfgturno.codigo = ?1");
            Query query = getEntityManager().createQuery("SELECT c FROM CfgItemsHorario c WHERE c.idHorario.idHorario = ?1 ");
            query.setParameter(1, id_cfgturno);
            cfgHorarios = query.getResultList();
        } catch (Exception e) {
            cfgHorarios = null;
        }
        return cfgHorarios;
    }

    public List<Short> findSelectedDays(int id_horario) {
        List<Short> dias;
        try {
            Query query = getEntityManager().createQuery("SELECT  DISTINCT c.dia FROM  CfgItemsHorario c WHERE c.idHorario.idHorario = ?1");
            query.setParameter(1, id_horario);
            dias = query.getResultList();
        } catch (Exception e) {
            dias = null;
        }
        return dias;
    }

    public List<CfgItemsHorario> findDateByDay(int id, Short dia) {
        List<CfgItemsHorario> horario;
        try {
            Query query = getEntityManager().createQuery("SELECT  c FROM  CfgItemsHorario c WHERE c.idHorario.idHorario = ?1 AND c.dia =?2");
            query.setParameter(1, id);
            query.setParameter(2, dia);
            horario = query.getResultList();
        } catch (Exception e) {
            horario = null;
        }
        return horario;
    }

    public int eliminarByIdHorario(int idHorario) {
        try {
            Query query = getEntityManager().createQuery("DELETE FROM CfgItemsHorario c WHERE c.idHorario.idHorario = ?1");
            query.setParameter(1, idHorario);
            return query.executeUpdate();
        } catch (Exception e) {
            return 0;
        }
    }
}
