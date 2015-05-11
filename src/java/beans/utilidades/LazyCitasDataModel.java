/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import modelo.entidades.CitCitas;
import modelo.fachadas.CitCitasFacade;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author mario
 */
public class LazyCitasDataModel extends LazyDataModel<CitCitas> {

    private CitCitasFacade citasFacade;
    private String opcion;//prestador o paciente
    private int identificador;
    private List<CitCitas> data;  
    
    public LazyCitasDataModel(CitCitasFacade citasFacade, String opcion, int identificador) {
        this.citasFacade = citasFacade;
        this.opcion = opcion;
        this.identificador = identificador;
    }

//    @PostConstruct
//    private void totalregistros() {
//        if (identificador != 0) {
//            
//            switch (opcion) {
//                case "paciente":
//                    rows = citasFacade.TotalCitasVigentesByPaciente(identificador);
//                    break;
//                case "prestador":
////                    rows = citasFacade.findCitasByPrestadorVigentes(identificador);
//                    break;
//            }
//            this.setRowCount(rows);
//        } else {
//            this.setRowCount(0);
//        }
//    }

//    @Override
//    public void setRowIndex(int rowIndex) {
//        if (rowIndex == -1 || getPageSize() == 0) {
//            super.setRowIndex(-1);
//        } else {
//            super.setRowIndex(rowIndex % getPageSize());
//        }
//    }
//
//    @Override
//    public CitCitas getRowData(String rowkey) {
//        for (CitCitas cita : data) {
//            if (cita.getIdCita().toString().equals(rowkey)) {
//                return cita;
//            }
//        }
//        return null;
//    }

    @Override
    public Object getRowKey(CitCitas cita) {
        return cita.getIdCita();
    }

    @Override
    public List<CitCitas> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        //paginacion(first, pageSize);
        int rows = 0;
        switch (opcion) {
            case "paciente":
                data = citasFacade.findCitasByPacienteVigentes(identificador, first, pageSize);
                rows = citasFacade.TotalCitasVigentesByPaciente(identificador);
                break;
            case "prestador":
                data = citasFacade.findCitasByPrestadorVigentes(identificador, first, pageSize);
                rows = citasFacade.TotalCitasVigentesByPrestador(identificador);
                break;
            default:
                data = new ArrayList();
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
