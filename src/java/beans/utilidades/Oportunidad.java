/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.utilidades;

public class Oportunidad {

    private String nombre_servicio;
    private long total_citas;
    private long total_dias;
    private float promedio;

    public Oportunidad(String nombre_servicio, Long total_citas, Long total_dias) {
        this.nombre_servicio = nombre_servicio;
        this.total_citas = total_citas;
        this.total_dias = total_dias;
        this.promedio = (float) total_dias / (float) total_citas;
    }

    public String getNombre_servicio() {
        return nombre_servicio;
    }

    public void setNombre_servicio(String nombre_servicio) {
        this.nombre_servicio = nombre_servicio;
    }

    public long getTotal_citas() {
        return total_citas;
    }

    public void setTotal_citas(long total_citas) {
        this.total_citas = total_citas;
    }

    public long getTotal_dias() {
        return total_dias;
    }

    public void setTotal_dias(long total_dias) {
        this.total_dias = total_dias;
    }

    public float getPromedio() {
        return promedio;
    }

    public void setPromedio(float promedio) {
        this.promedio = promedio;
    }
}
