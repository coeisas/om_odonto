/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import managedBeans.seguridad.LoginMB;

/**
 *
 * @author santos
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    @PersistenceContext(unitName = "OPENMEDICALPU")
    private EntityManager em;   

    @Inject
    private LoginMB loginMB;

    protected EntityManager getEntityManager() {
        //if (loginMB.getBaseDeDatosActual().compareTo("Optica") == 0) {
            return em;
        //} else {//ingresa en base de datos pruebas
        //    return em2;
        //}
    }
    
    public List<Object> consultaNativaArreglo(String sql) {//consulta nativa que retorna una lista de listas
        try {            
            return (List<Object>) getEntityManager().createNativeQuery(sql).getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public int consultaNativaConteo(String sql) {
        try {
            return Integer.parseInt(getEntityManager().createNativeQuery(sql).getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }

//    public List<Object> consultaNativaArreglo(String sql) {//retorna un arreglo con cualquier consulta nativa especificada
//        try {
//            return em.createNativeQuery(sql).getResultList();
//        } catch (Exception e) {
//            return null;
//        }
//    }

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
