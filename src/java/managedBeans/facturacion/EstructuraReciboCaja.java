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
public class EstructuraReciboCaja {

    //clase que contiene los datos principales para un registro clinico
    private String tituloFactura = "";//NombreEmpresa,Nit,Regimen <=> Html    
    private String nitEmpresa = "";
    private String regimenEmpresa = "";
    private String piePagina = "";
    private String subtituloFactura = "";//direccion-telefonos-email
    private String codigoDocumento = "";
    private String clienteCiudad = "";
    private String fechaFactura = "";
    private String clienteAdministradora = "";
    private String clienteNombre = "";
    private String clienteIdentificacion = "";//Nit o C.C.
    private String copago = "";
    private String cuotaModeradora = "";
    private String bono = "";
    private String particular = "";
    private String observaciones = "";
    private String total = "";
    private String son = "";//total en palabras	
    private String firmaAutoriza = "";
    private String logoEmpresa = "";

    public String getTituloFactura() {
        return tituloFactura;
    }

    public void setTituloFactura(String tituloFactura) {
        this.tituloFactura = tituloFactura;
    }

    public String getNitEmpresa() {
        return nitEmpresa;
    }

    public void setNitEmpresa(String nitEmpresa) {
        this.nitEmpresa = nitEmpresa;
    }

    public String getRegimenEmpresa() {
        return regimenEmpresa;
    }

    public void setRegimenEmpresa(String regimenEmpresa) {
        this.regimenEmpresa = regimenEmpresa;
    }

    public String getSubtituloFactura() {
        return subtituloFactura;
    }

    public void setSubtituloFactura(String subtituloFactura) {
        this.subtituloFactura = subtituloFactura;
    }

    public String getCodigoDocumento() {
        return codigoDocumento;
    }

    public void setCodigoDocumento(String codigoDocumento) {
        this.codigoDocumento = codigoDocumento;
    }

    public String getClienteCiudad() {
        return clienteCiudad;
    }

    public void setClienteCiudad(String clienteCiudad) {
        this.clienteCiudad = clienteCiudad;
    }

    public String getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(String fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public String getClienteAdministradora() {
        return clienteAdministradora;
    }

    public void setClienteAdministradora(String clienteAdministradora) {
        this.clienteAdministradora = clienteAdministradora;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getClienteIdentificacion() {
        return clienteIdentificacion;
    }

    public void setClienteIdentificacion(String clienteIdentificacion) {
        this.clienteIdentificacion = clienteIdentificacion;
    }

    public String getCopago() {
        return copago;
    }

    public void setCopago(String copago) {
        this.copago = copago;
    }

    public String getCuotaModeradora() {
        return cuotaModeradora;
    }

    public void setCuotaModeradora(String cuotaModeradora) {
        this.cuotaModeradora = cuotaModeradora;
    }

    public String getBono() {
        return bono;
    }

    public void setBono(String bono) {
        this.bono = bono;
    }

    public String getParticular() {
        return particular;
    }

    public void setParticular(String particular) {
        this.particular = particular;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSon() {
        return son;
    }

    public void setSon(String son) {
        this.son = son;
    }

    public String getFirmaAutoriza() {
        return firmaAutoriza;
    }

    public void setFirmaAutoriza(String firmaAutoriza) {
        this.firmaAutoriza = firmaAutoriza;
    }

    public String getLogoEmpresa() {
        return logoEmpresa;
    }

    public void setLogoEmpresa(String logoEmpresa) {
        this.logoEmpresa = logoEmpresa;
    }

    public String getPiePagina() {
        return piePagina;
    }

    public void setPiePagina(String piePagina) {
        this.piePagina = piePagina;
    }

}
