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
public class EstructuraItemsAdministradora {

    private String cantidad = "";
    private String descripcion = "";
    private String valorUnitario = "";
    private String valorTotal = "";
    
    private int cantidadInt=0;//usado para caluculos cuando se necesita una factura agrupada
    private double valorTotalDouble=0;//usado para calculos cuando se necetita una factura agrupada agrupar
    private String tipoItem=""; //Servicio, Medicamento, Insumo, Paquete
    private String identificadorItem=""; //Identificador del Servicio, Medicamento, Insumo, Paquete (Para Poder agruparlo)

    public EstructuraItemsAdministradora() {
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(String valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public String getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(String valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(String tipoItem) {
        this.tipoItem = tipoItem;
    }

    public String getIdentificadorItem() {
        return identificadorItem;
    }

    public void setIdentificadorItem(String identificadorItem) {
        this.identificadorItem = identificadorItem;
    }

    public int getCantidadInt() {
        return cantidadInt;
    }

    public void setCantidadInt(int cantidadInt) {
        this.cantidadInt = cantidadInt;
    }

    public double getValorTotalDouble() {
        return valorTotalDouble;
    }

    public void setValorTotalDouble(double valorTotalDouble) {
        this.valorTotalDouble = valorTotalDouble;
    }
    
    

}
