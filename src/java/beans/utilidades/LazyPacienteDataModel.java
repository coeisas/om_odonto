/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.utilidades;

import modelo.entidades.CfgPacientes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import modelo.fachadas.CfgPacientesFacade;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author mario
 */
public class LazyPacienteDataModel extends LazyDataModel<CfgPacientes> {

    private CfgPacientesFacade pacientesFacade;
    private List<CfgPacientes> dataSource;

    public LazyPacienteDataModel(CfgPacientesFacade pacFacade) {
        pacientesFacade = pacFacade;
    }

    @PostConstruct
    private void numeroRegistros() {
        this.setRowCount(pacientesFacade.numeroTotalRegistros());
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
    public CfgPacientes getRowData(String rowKey) {
        for (CfgPacientes paciente : dataSource) {
            if (paciente.getIdPaciente().toString().equals(rowKey)) {
                return paciente;
            }
        }    
        return null;
    }

    @Override
    public Object getRowKey(CfgPacientes paciente) {
        return paciente.getIdPaciente();
    }

    @Override
    public List<CfgPacientes> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        dataSource = new ArrayList<>();
        String where = "";
        if (filters != null && !filters.isEmpty()) {
            for (Map.Entry<String, Object> entrySet : filters.entrySet()) {
                String key = entrySet.getKey();
                String value = entrySet.getValue().toString();
                if (key.contains("identificacion")) {
                    where = where + " identificacion ilike '%" + value + "%' AND ";
                } else {
                    where = where + " replace(array_to_string(array [primer_nombre || ' ', segundo_nombre || ' ', primer_apellido || ' ', segundo_apellido],'',''),'  ',' ') ilike '%" + value + "%' AND ";
                }
            }
            if (where.length() != 0) {
                where = "WHERE " + where.substring(0, where.length() - 4);
            }
        }
        String sql = "SELECT * FROM cfg_pacientes " + where + " ORDER BY primer_nombre LIMIT " + String.valueOf(pageSize) + " OFFSET " + String.valueOf(first);
        dataSource = pacientesFacade.consultaNativaPacientes(sql);
        sql = "SELECT COUNT(*) FROM cfg_pacientes " + where;
        this.setRowCount(pacientesFacade.consultaNativaConteo(sql));
        return dataSource;
    }

    public List<CfgPacientes> getDatasource() {
        return dataSource;
    }

    public void setDatasource(List<CfgPacientes> datasource) {
        this.dataSource = datasource;
    }

}
