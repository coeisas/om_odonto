/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.utilidades;

import java.util.List;
import java.util.Map;
import modelo.entidades.CitAutorizaciones;
import modelo.fachadas.CitAutorizacionesFacade;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author mario
 */
public class LazyAutorizacionesDataModel extends LazyDataModel<CitAutorizaciones> {

    private CitAutorizacionesFacade autorizacionesFacade;
    private int idPaciente;
    private List<CitAutorizaciones> data;

    public LazyAutorizacionesDataModel(CitAutorizacionesFacade autorizacionesFacade, int idPaciente) {
        this.autorizacionesFacade = autorizacionesFacade;
        this.idPaciente = idPaciente;
    }

    @Override
    public Object getRowKey(CitAutorizaciones autorizacion) {
        return autorizacion.getIdAutorizacion();
    }

    @Override
    public List<CitAutorizaciones> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        //paginacion(first, pageSize);
        int rows;
        if (idPaciente != 0) {
            data = autorizacionesFacade.findAutorizacionesNoCerradas(idPaciente, first, pageSize);
            rows = autorizacionesFacade.totalAutorizacionesNoCerradasByPaciente(idPaciente);
        } else {
            data = autorizacionesFacade.findAutorizacionesNoCerradas(first, pageSize);
            rows = autorizacionesFacade.totalAutorizacionesNoCerradas();
        }
        //rowCount
        this.setRowCount(rows);
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
}
