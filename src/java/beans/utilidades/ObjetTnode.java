/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.utilidades;

import java.io.Serializable;

/**
 *
 * @author mario
 */
public class ObjetTnode implements Serializable {

    //identifica al nodo paciente
    private int id;

    //identifica al nodo especialidad
    private String codigo;

    //corresponde al nombre del prestador o de la especialidad
    private String name;

    //true si corresponde a prestador, false a especialidad
    private boolean tag;

    public ObjetTnode(int id, String codigo, String name, boolean tag) {
        this.id = id;
        this.codigo = codigo;
        this.name = name;
        this.tag = tag;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the tag
     */
    public boolean isTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(boolean tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

}
