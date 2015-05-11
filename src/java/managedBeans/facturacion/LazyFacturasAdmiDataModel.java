/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import beans.utilidades.FilaDataTable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import modelo.entidades.FacFacturaAdmi;
import modelo.fachadas.FacFacturaAdmiFacade;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author mario
 */
public class LazyFacturasAdmiDataModel extends LazyDataModel<FilaDataTable> {

    private final FacFacturaAdmiFacade facturaAdmiFacade;
    private List<FilaDataTable> dataSource;    
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    public LazyFacturasAdmiDataModel(FacFacturaAdmiFacade facFacade) {
        facturaAdmiFacade = facFacade;
    }

    @PostConstruct
    private void numeroRegistros() {
        this.setRowCount(facturaAdmiFacade.numeroTotalRegistros());
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
    public FilaDataTable getRowData(String rowKey) {
        for (FilaDataTable factura : dataSource) {
            if (factura.getColumna1().equals(rowKey)) {
                return factura;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(FilaDataTable factura) {
        return factura.getColumna1();
    }

    @Override
    public List<FilaDataTable> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        dataSource = new ArrayList<>();
        String where = "";
        if (filters != null && !filters.isEmpty()) {
            for (Map.Entry<String, Object> entrySet : filters.entrySet()) {
                String key = entrySet.getKey();
                String value = entrySet.getValue().toString();
//                if (key.contains("codigo")) {
//                    where = where + " AND numero_documento ilike '%" + value + "%' ";
//                }
            }

        }
        String sql = "SELECT * FROM fac_factura_admi " + where + " ORDER BY fecha_elaboracion DESC LIMIT " + String.valueOf(pageSize) + " OFFSET " + String.valueOf(first);
        List<FacFacturaAdmi> listaFacturas = facturaAdmiFacade.consultaNativaFacturas(sql);
        dataSource = new ArrayList<>();
        FilaDataTable nuevaFila;
        for (FacFacturaAdmi f : listaFacturas) {
            nuevaFila = new FilaDataTable();
            nuevaFila.setColumna1(f.getIdFacturaAdmi().toString());
            nuevaFila.setColumna2(dateFormat.format(f.getFechaElaboracion()));
            nuevaFila.setColumna3(f.getCodigoDocumento());
            if (f.getAnulada()) {
                nuevaFila.setColumna4("ANULADA");
            } else {
                nuevaFila.setColumna4("ACTIVA");
            }            
            nuevaFila.setColumna5(f.getIdAdministradora().getRazonSocial());
            nuevaFila.setColumna6(f.getValorTotal().toString());
            dataSource.add(nuevaFila);
        }
        sql = "SELECT COUNT(*) FROM fac_factura_admi " + where;
        this.setRowCount(facturaAdmiFacade.consultaNativaConteo(sql));
        return dataSource;
    }

    public List<FilaDataTable> getDatasource() {
        return dataSource;
    }

    public void setDatasource(List<FilaDataTable> datasource) {
        this.dataSource = datasource;
    }

}
