/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import modelo.entidades.FacServicio;
import modelo.fachadas.FacServicioFacade;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author mario
 */
public class LazyServiciosDataModel extends LazyDataModel<FacServicio> {

    private FacServicioFacade servicioFacade;
    private List<FacServicio> dataSource;

    public LazyServiciosDataModel(FacServicioFacade servFacade) {
        servicioFacade = servFacade;
    }

    @PostConstruct
    private void numeroRegistros() {
        this.setRowCount(servicioFacade.numeroTotalRegistros());
    }

    @Override
    public void setRowIndex(int rowIndex) {
        if (rowIndex == -1 || getPageSize() == 0) {
            super.setRowIndex(-1);
        } else {
            super.setRowIndex(rowIndex % getPageSize());
        }
    }

    @Override
    public FacServicio getRowData(String rowKey) {
        for (FacServicio servicio : dataSource) {
            if (servicio.getIdServicio().toString().equals(rowKey)) {
                return servicio;
            }
        }    
        return null;
    }

    @Override
    public Object getRowKey(FacServicio servicio) {
        return servicio.getIdServicio();
    }

    @Override
    public List<FacServicio> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        dataSource = new ArrayList<>();
        String where = "";
        if (filters != null && !filters.isEmpty()) {
            for (Map.Entry<String, Object> entrySet : filters.entrySet()) {
                String key = entrySet.getKey();
                String value = entrySet.getValue().toString();
                if (key.contains("codigo")) {
                    where = where + " codigo_servicio ilike '%" + value + "%' AND ";
                } else {
                    where = where + " nombre_servicio ilike '%" + value + "%' AND ";
                }
            }
            if (where.length() != 0) {
                where = "WHERE " + where.substring(0, where.length() - 4);
            }
        }
        String sql = "SELECT * FROM fac_servicio " + where + " ORDER BY id_servicio LIMIT " + String.valueOf(pageSize) + " OFFSET " + String.valueOf(first);
        dataSource = servicioFacade.consultaNativaServicios(sql);
        sql = "SELECT COUNT(*) FROM fac_servicio " + where;
        this.setRowCount(servicioFacade.consultaNativaConteo(sql));
        return dataSource;
    }

    public List<FacServicio> getDatasource() {
        return dataSource;
    }

    public void setDatasource(List<FacServicio> datasource) {
        this.dataSource = datasource;
    }

}
