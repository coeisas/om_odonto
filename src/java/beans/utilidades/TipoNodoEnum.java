/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.utilidades;

/**
 *
 * @author SANTOS
 */
public enum TipoNodoEnum {

    
    //usados para perfiles
    
    OPCION_CON_URL,//opcion de pagina principal QUE ABRE UNA PAGINA
    OPCION_SIN_URL,//opcion de pagina principal QUE NO ABRE UNA PAGINA
    MENU_CON_URL,//opcion de un menu QUE ABRE UNA PAGINA
    MENU_SIN_URL,//opcion de un menu QUE NO ABRE UNA PAGINA
    
    //usados para Historial de registros clinicos
    RAIZ_HISTORIAL,//es la raiz
    FECHA_HISTORIAL,//es un nodo que contiene solo fecha
    REGISTRO_HISTORIAL,//es un nodo que representa un registro en el sistema
    
    NOVALUE;

    public static TipoNodoEnum convert(String str) {
        try {
            return valueOf(str);
        } catch (IllegalArgumentException ex) {
            return NOVALUE;
        }
    }
}
