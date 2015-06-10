/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.utilidades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import modelo.entidades.CitTurnos;
import modelo.fachadas.CitTurnosFacade;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Mario
 */
public class LazyTurnosDataModel extends LazyDataModel<CitTurnos> {

    private CitTurnosFacade citTurnosFacade;
    private List<Integer> idsprestadores;
    private Date horaIni;
    private Date horaFin;
    private List diasSemana;
    private Integer sede;

    public LazyTurnosDataModel() {
    }

    public LazyTurnosDataModel(CitTurnosFacade citTurnosFacade, List<Integer> idsprestadores, Date horaIni, Date horaFin, List diasSemana) {
        this.citTurnosFacade = citTurnosFacade;
        this.idsprestadores = idsprestadores;
        this.horaIni = horaIni;
        this.horaFin = horaFin;
        this.diasSemana = diasSemana;
        this.sede = 0;
    }

    public LazyTurnosDataModel(CitTurnosFacade citTurnosFacade, List<Integer> idsprestadores, Date horaIni, Date horaFin, List diasSemana, Integer sede) {
        this.citTurnosFacade = citTurnosFacade;
        this.idsprestadores = idsprestadores;
        this.horaIni = horaIni;
        this.horaFin = horaFin;
        this.diasSemana = diasSemana;
        this.sede = sede;
    }

    @Override
    public Object getRowKey(CitTurnos turno) {
        return turno.getIdTurno();
    }

    @Override
    public List<CitTurnos> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        //filter
        List<CitTurnos> data = loadTurnos(first, pageSize, filters);

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

    private List<CitTurnos> loadTurnos(int offset, int limit, Map<String, Object> filters) {
        List<CitTurnos> data;
        String sql = "";
        SimpleDateFormat ft = new SimpleDateFormat("HH:mm");
        if (horaIni != null) {
            sql += " AND hora_ini >= '" + ft.format(horaIni) + "'";
        }
        if (horaFin != null) {
            sql += " AND hora_fin <= '" + ft.format(horaFin) + "'";
        }
        if (diasSemana != null) {
            if (diasSemana.size() > 0) {
                sql += " AND EXTRACT(DOW FROM fecha) IN (";
                int i = diasSemana.size();
                int index = 1;
                for (Object dia : diasSemana) {
                    sql += dia.toString();
                    if (index < i) {
                        sql += ", ";
                    }
                    index++;
                }
                sql += ")";
            }
        }
        String query;
        String queryCount;
        query = "SELECT * FROM cit_turnos JOIN cfg_consultorios ON cfg_consultorios.id_consultorio = cit_turnos.id_consultorio JOIN cfg_usuarios ON id_prestador = id_usuario WHERE cfg_consultorios.id_sede = " + sede + " AND id_prestador IN (";
        queryCount = "SELECT COUNT(id_turno) FROM cit_turnos JOIN cfg_consultorios ON cfg_consultorios.id_consultorio = cit_turnos.id_consultorio JOIN cfg_usuarios ON id_prestador = id_usuario WHERE cfg_consultorios.id_sede = " + sede + " AND id_prestador IN (";
        int i = idsprestadores.size();
        int indx = 1;
        for (Integer id : idsprestadores) {
            query += id;
            queryCount += id;
            if (indx < i) {
                query += ", ";
                queryCount += ", ";
            }
            indx++;
        }
        query += ") AND estado = 'disponible' AND fecha >= current_date";
        queryCount += ") AND estado = 'disponible' AND fecha >= current_date";

        if (filters.isEmpty()) {
            data = citTurnosFacade.findTurnosDisponiblesByPrestadoresLazyNative(query.concat(sql), idsprestadores, 0, horaIni, horaFin, diasSemana, offset, limit);
            int elementos = citTurnosFacade.totalTurnosByPrestadoresLazyNative(queryCount.concat(sql), idsprestadores, 0, horaIni, horaFin, diasSemana);
            this.setRowCount(elementos);
        } else {
            int especialidad = 0;
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String filterProperty = entry.getKey();
                switch (filterProperty) {
                    case "idPrestador.especialidad.id":
                        especialidad = Integer.parseInt(entry.getValue().toString());
                        sql += " AND cfg_usuarios.especialidad = " + especialidad;
                        break;
                    case "fecha":
                        ft = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            String aux = entry.getValue().toString();
                            Date fecha = ft.parse(aux);
                            ft = new SimpleDateFormat("yyyy-MM-dd");
                            sql += " AND cit_turnos.fecha = '"+ft.format(fecha)+"'";
                        } catch (ParseException pe){
                            sql += "";
                        }
                        break;
                }
            }
            int elementos = citTurnosFacade.totalTurnosByPrestadoresLazyNative(queryCount.concat(sql), idsprestadores, especialidad, horaIni, horaFin, diasSemana);
            this.setRowCount(elementos);
            data = citTurnosFacade.findTurnosDisponiblesByPrestadoresLazyNative(query.concat(sql), idsprestadores, especialidad, horaIni, horaFin, diasSemana, offset, limit);
        }
        return data;
    }
}
