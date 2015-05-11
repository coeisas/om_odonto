/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.utilidades;

import java.io.Serializable;

/**
 *
 * @author santos
 */

/*
 * esta clase corresponde a cada uno de los nodos que tendra el arbol que aparece
 * en el formulario de perfiles
 */
public class NodoArbolPerfiles implements Serializable {

    private TipoNodoEnum tipoDeNodo;
    private String nombre = "";
    private int identificador = 0;
    private int identificadorPadre = 0;
    private boolean activado = false;

    public NodoArbolPerfiles(TipoNodoEnum tipoDeNodo) {
        this.tipoDeNodo = tipoDeNodo;
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

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public int getIdentificadorPadre() {
        return identificadorPadre;
    }

    public void setIdentificadorPadre(int identificadorPadre) {
        this.identificadorPadre = identificadorPadre;
    }

    public boolean isActivado() {
        return activado;
    }

    public void setActivado(boolean activado) {
        this.activado = activado;
    }
}