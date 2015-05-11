/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.utilidades;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author santos
 */

/*
 * esta clase corresponde a cada uno de los nodos que tendra el arbol que aparece
 * en el formulario de historial de registros clinicos de un paciente
 */
public class NodoArbolHistorial implements Serializable {

    private TipoNodoEnum tipoDeNodo;
    private String nombre = "";//es el nombre que se mira en cada nodo del arbol
    private int idRegistro = -1;//identificador del registro que representa(tabla hc_registro)
    private int idTipoRegistro = -1;//tipo de registro (tabla hc_tipo_reg )(1=consulta, 2=nota, 3=recetario ...)    
    private String strFecha = "";//fecha formateada(se usa para comparar en el arbol)
    private String strHora = "";//fecha formateada(se usa para comparar en el arbol)
    private SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm:ss");

    public NodoArbolHistorial(TipoNodoEnum tipoDeNodo, Date fechaReg, int idRegistro, int idTipoRegistro, String nombreTipoRegistro, String nombreMedico) {
        this.tipoDeNodo = tipoDeNodo;
        this.idRegistro = idRegistro;
        this.idTipoRegistro = idTipoRegistro;

        if (fechaReg != null) {
            strFecha = sdfDate.format(fechaReg);
            strHora = sdfHour.format(fechaReg);
        }
        switch (tipoDeNodo) {
            case RAIZ_HISTORIAL:
                nombre = "raiz";
                break;
            case FECHA_HISTORIAL:
                nombre = strFecha;
                break;
            case REGISTRO_HISTORIAL:
                nombre = strHora + "  (" + nombreTipoRegistro + ") " + nombreMedico;
                break;
            case NOVALUE:
                nombre = nombreMedico;//viene "NO HAY REGISTROS"
                break;
        }
    }

    public TipoNodoEnum getTipoDeNodo() {
        return tipoDeNodo;
    }

    public void setTipoDeNodo(TipoNodoEnum tipoDeNodo) {
        this.tipoDeNodo = tipoDeNodo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
    }

    public int getIdTipoRegistro() {
        return idTipoRegistro;
    }

    public void setIdTipoRegistro(int idTipoRegistro) {
        this.idTipoRegistro = idTipoRegistro;
    }

    public String getStrFecha() {
        return strFecha;
    }

    public void setStrFecha(String strFecha) {
        this.strFecha = strFecha;
    }
}
