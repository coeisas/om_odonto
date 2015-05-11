/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import modelo.entidades.CfgUsuarios;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import modelo.entidades.CfgClasificaciones;

/**
 *
 * @author santos
 */
@Stateless
public class CfgUsuariosFacade extends AbstractFacade<CfgUsuarios> {

    public CfgUsuariosFacade() {
        super(CfgUsuarios.class);
    }
    
    public List<CfgUsuarios> buscarOrdenado() {
        try {
            String hql = "SELECT u FROM CfgUsuarios u ORDER BY u.primerNombre, u.segundoNombre , u.primerApellido, u.segundoApellido";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgUsuarios buscarPorLoginClave(String login, String clave) {
        try {
            String hql = "SELECT u FROM CfgUsuarios u WHERE u.loginUsuario = :loginUsuario AND u.clave = :clave";
            return (CfgUsuarios) getEntityManager().createQuery(hql).setParameter("loginUsuario", login).setParameter("clave", clave).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgUsuarios buscarPorLogin(String login) {
        try {
            return getEntityManager().createNamedQuery("CfgUsuarios.findByLoginUsuario", CfgUsuarios.class).setParameter("loginUsuario", login).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public EntityManager sacarEntityManager() {
        return getEntityManager();
    }

    public List<CfgUsuarios> buscarPorPerfil(Integer idPerfil) {
        try {
            String hql = "SELECT u FROM CfgUsuarios u WHERE u.idPerfil.idPerfil = :idPerfil";
            return getEntityManager().createQuery(hql).setParameter("idPerfil", idPerfil).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgUsuarios buscarPorIdentificacion(String identificacion) {
        try {
            return getEntityManager().createNamedQuery("CfgUsuarios.findByIdentificacion", CfgUsuarios.class).setParameter("identificacion", identificacion).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    //el tipoUsuario = 1 . corresponde al prestador
    public List<CfgUsuarios> findPrestadorByEspecialidad(int especialidad) {
        try {
            Query query = getEntityManager().createQuery("SELECT p FROM CfgUsuarios p WHERE p.especialidad.id = ?1 AND p.tipoUsuario.codigo=2 AND p.estadoCuenta = true");
            query.setParameter(1, especialidad);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CfgClasificaciones> findEspecialidades() {
        try {
            Query query = getEntityManager().createQuery("SELECT DISTINCT p.especialidad FROM CfgUsuarios p WHERE p.tipoUsuario.codigo=2 AND p.estadoCuenta = true");
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<CfgUsuarios> buscarUsuariosParaHistorias() {
        try {
            Query query = getEntityManager().createQuery("SELECT p FROM CfgUsuarios p WHERE p.tipoUsuario.codigo = 2 AND p.mostrarEnHistorias = true");
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CfgUsuarios> findPrestadores() {
        try {
            Query query = getEntityManager().createQuery("SELECT p FROM CfgUsuarios p WHERE p.tipoUsuario.codigo = 2 AND p.visible = true");
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CfgUsuarios> findPrestadoresLazy(String sql, int limit, int offset, int especialidad, String prestador) {
        try {
            Query query = getEntityManager().createQuery(sql);
            if (especialidad != 0) {
                query.setParameter(1, especialidad);
            }
            if (!prestador.isEmpty()) {
                query.setParameter(3, prestador + '%');
            }
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public EntityManager obtenerEntityManager() {
        return getEntityManager();
    }

    public int totalPrestadoresLazy(String consulta, int especialidad, String prestador) {
        try {
            Query query = getEntityManager().createQuery(consulta);
            if (especialidad != 0) {
                query.setParameter(1, especialidad);
            }
            if (!prestador.isEmpty()) {
                query.setParameter(3, prestador + '%');

            }
            return Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }
}
