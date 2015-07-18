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
        List<Object> listaDatosFacturas;
        dataSource = new ArrayList<>();
        FilaDataTable nuevaFila;
        String where = "";
        String sql = "";
        String sqlResult = "select * from\n";
        String sqlCount = "select count(*) from\n";

        if (filters != null && !filters.isEmpty()) {
            for (Map.Entry<String, Object> entrySet : filters.entrySet()) {
                String key = entrySet.getKey();
                String value = entrySet.getValue().toString();
                //System.out.println("key:"+key+" value:"+value);
                if (key.contains("columna2")) {//numero
                    where = where + " fecha ilike '%" + value + "%' AND ";
                }
                if (key.contains("columna3")) {//numero
                    where = where + " codigo ilike '%" + value + "%' AND ";
                }
                if (key.contains("columna4")) {//estado
                    where = where + " estado ilike '%" + value + "%' AND ";
                }
                if (key.contains("columna5")) {//paciente
                    where = where + " administradora ilike '%" + value + "%' AND ";
                }
                if (key.contains("columna6")) {//valor
                    where = where + " valor ilike '%" + value + "%' AND ";
                }
            }
            if (where.length() != 0) {
                where = "WHERE " + where.substring(0, where.length() - 4);
            }
        }

        sql = sql + ""
                + "( \n"
                + "     select \n"
                + "         id_factura_admi::text as id, \n"
                + "         ------------------------------------- \n"
                + "         fecha_elaboracion::text as fecha,\n"
                + "         --------------------------------------\n"
                + "         codigo_documento as codigo,\n"
                + "         --------------------------------------\n"
                + "         CASE \n"
                + "            WHEN anulada = 't' \n"
                + "            THEN 'ANULADA' \n"
                + "            ELSE 'ACTIVA' \n"
                + "         END AS estado,\n"
                + "         --------------------------------------\n"
                //                + "         ( select \n"
                //                + "     	   replace(array_to_string(array [\n"
                //                + "     	   primer_nombre || ' ', \n"
                //                + "     	   segundo_nombre || ' ', \n"
                //                + "     	   primer_apellido || ' ', \n"
                //                + "     	   segundo_apellido],'',''),'  ',' ') \n"
                //                + "            from cfg_pacientes \n"
                //                + "            where id_paciente = fac_factura_paciente.id_paciente\n"
                //                + "         ) as nombres,\n"
                + "         ( select razon_social \n"
                + "            from fac_administradora \n"
                + "            where id_administradora = fac_factura_admi.id_administradora\n"
                + "         ) as administradora,\n"
                + "         --------------------------------------\n"
                + "         valor_total::text as valor\n"
                + "         ------------------------------------- \n"
                + "     from fac_factura_admi \n"
                + ")as consulta " + where;

        sqlResult = sqlResult + sql + " ORDER BY fecha DESC LIMIT " + String.valueOf(pageSize) + " OFFSET " + String.valueOf(first);
        sqlCount = sqlCount + sql;
        //System.out.println(sqlResult + "---");
        //System.out.println(sqlCount + "---");

        listaDatosFacturas = facturaAdmiFacade.consultaNativaArreglo(sqlResult);
        for (Object listaDatosFactura : listaDatosFacturas) {
            Object[] datosUnaFactura = (Object[]) listaDatosFactura;
            nuevaFila = new FilaDataTable(
                    datosUnaFactura[0].toString(),
                    datosUnaFactura[1].toString(),
                    datosUnaFactura[2].toString(),
                    datosUnaFactura[3].toString(),
                    datosUnaFactura[4].toString(),
                    datosUnaFactura[5].toString());
            dataSource.add(nuevaFila);
        }
        this.setRowCount(facturaAdmiFacade.consultaNativaConteo(sqlCount));
        return dataSource;
    }

    public List<FilaDataTable> getDatasource() {
        return dataSource;
    }

    public void setDatasource(List<FilaDataTable> datasource) {
        this.dataSource = datasource;
    }

}
