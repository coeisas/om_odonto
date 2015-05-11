/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.utilidades;

/**
 *
 * @author Administrador
 */
public class Tab {

    private String titulo;
    private String url;

    public Tab(String titulo, String url) {
        this.titulo = titulo;
        this.url = url;
    }    

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }    
}
