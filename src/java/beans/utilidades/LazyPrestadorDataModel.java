/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.utilidades;

import org.primefaces.model.SortOrder;
import java.util.Map;
import modelo.entidades.CfgUsuarios;
import java.util.List;
import modelo.fachadas.CfgUsuariosFacade;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author mario
 */
public class LazyPrestadorDataModel extends LazyDataModel<CfgUsuarios> {

    private final CfgUsuariosFacade cfgUsuariosFacade;


    public LazyPrestadorDataModel(CfgUsuariosFacade cfgUsuariosFacade) {
        this.cfgUsuariosFacade = cfgUsuariosFacade;
    }

    @Override
    public Object getRowKey(CfgUsuarios prestador) {
        return prestador.getIdUsuario();
    }

    @Override
    public List<CfgUsuarios> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        //filter
        List<CfgUsuarios> data = loadPrestadores(first, pageSize, filters);

        //rowCount
        int dataSize = data.size();

        //paginate
        if (dataSize > pageSize) {
            try {
                return data.subList(first, first + pageSize);
            } catch (IndexOutOfBoundsException e) {
                return data.subList(first, first + (dataSize % pageSize));
            }
        } else {
            return data;
        }
    }

    private List<CfgUsuarios> loadPrestadores(int offset, int limit, Map<String, Object> filters) {
        List<CfgUsuarios> data;
        String sql = "";
        if (filters.isEmpty()) {
            String query = "SELECT p FROM CfgUsuarios p WHERE p.tipoUsuario.codigo = 2 AND p.estadoCuenta = true";
            String queryCount = "SELECT COUNT (p) FROM CfgUsuarios p WHERE p.tipoUsuario.codigo = 2 AND p.estadoCuenta = true";
            this.setRowCount(cfgUsuariosFacade.totalPrestadoresLazy(queryCount, 0, ""));
            data = cfgUsuariosFacade.findPrestadoresLazy(query, limit, offset, 0, "");
        } else {
            String prestador = "";
            int especialidad = 0;
            int i = 0;
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String filterProperty = entry.getKey();
                if (i > 0) {
                    sql += " AND";
                }
                switch (filterProperty) {
                    case "especialidad.id":
                        sql += " p.especialidad.id = ?1";
                        especialidad = Integer.parseInt(entry.getValue().toString());
                        break;
                    default:
                        sql += " (LOWER(p.primerNombre) LIKE ?3 OR LOWER(p.segundoNombre) LIKE ?3 OR LOWER(p.primerApellido) LIKE ?3 OR LOWER(p.segundoApellido) LIKE ?3 OR CONCAT(LOWER(p.primerNombre), ' ', LOWER(p.segundoNombre)) LIKE ?3 OR CONCAT(LOWER(p.primerApellido), ' ', LOWER(p.segundoApellido)) LIKE ?3 OR CONCAT(LOWER(p.primerNombre), ' ', LOWER(p.segundoNombre), ' ', LOWER(p.primerApellido), ' ', LOWER(p.segundoApellido)) LIKE ?3 OR CONCAT(LOWER(p.primerNombre), ' ', LOWER(p.segundoNombre), ' ', LOWER(p.primerApellido)) LIKE ?3 )";
                        //sql += " (p.primerNombre LIKE ?3 OR p.segundoNombre LIKE ?3 OR p.primerApellido LIKE ?3 OR p.segundoApellido LIKE ?3)";
                        prestador = entry.getValue().toString().toLowerCase();
                        break;
                }
                i++;

            }
            String queryCount = "SELECT COUNT (p) FROM CfgUsuarios p WHERE p.tipoUsuario.codigo = 2 AND p.estadoCuenta = true AND".concat(sql);
            this.setRowCount(cfgUsuariosFacade.totalPrestadoresLazy(queryCount, especialidad, prestador));
            String query = "SELECT p FROM CfgUsuarios p WHERE p.tipoUsuario.codigo = 2 AND p.estadoCuenta = true AND".concat(sql);
            data = cfgUsuariosFacade.findPrestadoresLazy(query, limit, offset, especialidad, prestador);
        }
        return data;
    }
}
