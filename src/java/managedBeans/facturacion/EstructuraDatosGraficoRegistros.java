/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

/**
 *
 * @author santos
 */
public class EstructuraDatosGraficoRegistros {
    /*
     Esturctura usada cuando se quiere generar un grafico de como esta el estado de facturacion de la empresa
     */

    private String nombreAdministradora = "";
    private int idAdministradora = 0;
    private int cantidad = 0;

    public EstructuraDatosGraficoRegistros(String nomAdm, int idAdmi, int cant) {
        nombreAdministradora = nomAdm;
        idAdministradora = idAdmi;
        cantidad = cant;
    }

    public String getNombreAdministradora() {
        return nombreAdministradora;
    }

    public void setNombreAdministradora(String nombreAdministradora) {
        this.nombreAdministradora = nombreAdministradora;
    }

    public int getIdAdministradora() {
        return idAdministradora;
    }

    public void setIdAdministradora(int idAdministradora) {
        this.idAdministradora = idAdministradora;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

}
