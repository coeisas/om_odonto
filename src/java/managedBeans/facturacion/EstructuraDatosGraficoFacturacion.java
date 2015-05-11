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
public class EstructuraDatosGraficoFacturacion {
    /*
     Esturctura usada cuando se quiere generar un grafico de como esta el estado de facturacion de la empresa
     */

    private String nombreAdministradora = "";
    private int idAdministradora = 0;
    private double valorFactura = 0;

    public EstructuraDatosGraficoFacturacion(String nomAdm, int idAdmi, double valor) {
        nombreAdministradora = nomAdm;
        idAdministradora = idAdmi;
        valorFactura = valor;
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

    public double getValorFactura() {
        return valorFactura;
    }

    public void setValorFactura(double valorFactura) {
        this.valorFactura = valorFactura;
    }

}
