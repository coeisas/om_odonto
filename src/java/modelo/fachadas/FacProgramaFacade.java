/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.CfgPacientes;
import modelo.entidades.FacPrograma;

/**
 *
 * @author mario
 */
@Stateless
public class FacProgramaFacade extends AbstractFacade<FacPrograma> {

    public FacProgramaFacade() {
        super(FacPrograma.class);
    }
    
    public List<FacPrograma> buscarOrdenado() {
        try {
            String hql = "SELECT p FROM FacPrograma p ORDER By p.idPrograma";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    //busca los programas por idcontrato y a los que aplica un determinado paciente
    public List<FacPrograma> findProgramasByContratoAndPaciente(List<Integer> contratos, CfgPacientes paciente){
        Query query;
        try{
            //serciorarse de como se guarda el genero y la zona en la base de datos
            query = getEntityManager().createQuery("SELECT p FROM FacPrograma p WHERE p.idContrato.idContrato IN ?1 AND (p.sexo = ?2 OR p.sexo = ?6) AND p.edadInicial <= ?3 AND p.edadFinal >= ?4 AND (p.zona = ?5 OR p.zona = ?6)");
            //query = getEntityManager().createQuery("SELECT p FROM FacPrograma p WHERE p.idContrato.idContrato IN ?1");
            query.setParameter(1, contratos);
            
            Date fechaActual = new Date();
            int edad = fechaActual.getYear() - paciente.getFechaNacimiento().getYear();
            short ambos = 0;
            query.setParameter(2, Short.parseShort(paciente.getGenero().getId().toString()));
            query.setParameter(3, Short.parseShort(String.valueOf(edad)));
            query.setParameter(4, Short.parseShort(String.valueOf(edad)));
            query.setParameter(5, Short.parseShort(paciente.getZona().getId().toString()));
            query.setParameter(6, ambos);
                   
            //query.setParameter(6, fechaActual);
            return query.getResultList();
        }catch(Exception e){
            return null;
        }
        
    }
    
}
