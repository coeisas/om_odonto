/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.seguridad;

import modelo.fachadas.CfgUsuariosFacade;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import beans.utilidades.MetodosGenerales;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import managedBeans.facturacion.EstructuraDatosGraficoFacturacion;
import managedBeans.facturacion.EstructuraDatosGraficoRegistros;
import modelo.entidades.CitCitas;
import modelo.entidades.FacFacturaPaciente;
import modelo.fachadas.CitCitasFacade;
import modelo.fachadas.FacFacturaPacienteFacade;
import org.primefaces.context.RequestContext;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Administrador
 */
@Named("paginaInformativaMB")
@SessionScoped
public class PaginaInformativaMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    CfgUsuariosFacade usuariosFachada;
    @EJB
    CitCitasFacade citasFacade;
    @EJB
    FacFacturaPacienteFacade facturaPacienteFacade;

    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------
    private List<CitCitas> listaCitasSinRegistro = new ArrayList<>();
    private CitCitas citaSeleccionadaTabla;

    //---------------------------------------------------
    //-----------------VARIABLES -------------------------
    //---------------------------------------------------
    private PieChartModel pastelRegistros;
    private PieChartModel pastelFacturacion;

    private LoginMB loginMB;
    private boolean mostrarTabFacturacion = false;
    private boolean mostrarTabRegistrosPendientes = false;
    private ArrayList<EstructuraDatosGraficoFacturacion> datosGraficoFacturacion;
    private ArrayList<EstructuraDatosGraficoRegistros> datosGraficoRegistros;
    private final DecimalFormat formateadorDecimal = new DecimalFormat("0.00");

    //---------------------------------------------------
    //----------------- FUNCIONES -------------------------
    //---------------------------------------------------    
    public PaginaInformativaMB() {

    }

    public void openHistoriaClinica() { //abrir interfaz historias desde pagina informativa
        if (citaSeleccionadaTabla == null) {
            imprimirMensaje("Error", "Se debe seleccionar un registro de la tabla: ", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("window.parent.cargarTab('Historias Clinicas','historias/historias.xhtml','idCita;" + citaSeleccionadaTabla.getIdCita().toString() + "')");        
    }

    private void crearGraficoFacturacion() {
        List<FacFacturaPaciente> listaFacturasFaltanCobrar = facturaPacienteFacade.buscarFaltanFacturar();
        boolean encontrado;
        double total = 0;
        datosGraficoFacturacion = new ArrayList<>();
        for (FacFacturaPaciente factura : listaFacturasFaltanCobrar) {
            encontrado = false;
            for (EstructuraDatosGraficoFacturacion datoGraficoFacturacion : datosGraficoFacturacion) {
                if (datoGraficoFacturacion.getIdAdministradora() == factura.getIdAdministradora().getIdAdministradora()) {
                    encontrado = true;
                    datoGraficoFacturacion.setValorFactura(datoGraficoFacturacion.getValorFactura() + factura.getValorEmpresa());
                    datoGraficoFacturacion.setNombreAdministradora(factura.getIdAdministradora().getRazonSocial() + "  " + datoGraficoFacturacion.getValorFactura());
                    //System.out.println("Ya estaba: "+factura.getIdAdministradora().getRazonSocial()+" se suma "+factura.getValorEmpresa()+" queda "+datoGraficoFacturacion.getValorFactura());
                    break;
                }
            }
            if (!encontrado) {//la administradora no esta agregada
                datosGraficoFacturacion.add(new EstructuraDatosGraficoFacturacion(
                        factura.getIdAdministradora().getRazonSocial() + "  " + formateadorDecimal.format(factura.getValorEmpresa()),
                        factura.getIdAdministradora().getIdAdministradora(),
                        factura.getValorEmpresa()));
                //System.out.println("Se agrega: "+factura.getIdAdministradora().getRazonSocial()+" inicia con "+factura.getValorEmpresa());
            }
            total = total + factura.getValorEmpresa();
        }
        pastelFacturacion = null;//new PieChartModel();
        if (datosGraficoFacturacion != null && !datosGraficoFacturacion.isEmpty()) {
            pastelFacturacion = new PieChartModel();
            for (EstructuraDatosGraficoFacturacion datoGraficoFacturacion : datosGraficoFacturacion) {
                pastelFacturacion.set(datoGraficoFacturacion.getNombreAdministradora(), datoGraficoFacturacion.getValorFactura());
                //System.out.println("Se saca en el pastel : "+datoGraficoFacturacion.getNombreAdministradora()+ " - "+datoGraficoFacturacion.getValorFactura());
            }
            configurarGrafico(pastelFacturacion, "Total pendiente de cobro a administradoras: " + formateadorDecimal.format(total), "e", true, true, 330);
        }
    }

    private void crearGraficoRegistrosFaltantes() {
        listaCitasSinRegistro = citasFacade.buscarFaltanRegistro();
        boolean encontrado;
        int total = 0;
        datosGraficoRegistros = new ArrayList<>();
        for (CitCitas cita : listaCitasSinRegistro) {
            encontrado = false;
            for (EstructuraDatosGraficoRegistros datoGraficoRegistros : datosGraficoRegistros) {
                if (datoGraficoRegistros.getIdAdministradora() == cita.getIdAdministradora().getIdAdministradora()) {
                    encontrado = true;
                    datoGraficoRegistros.setCantidad(datoGraficoRegistros.getCantidad() + 1);
                    break;
                }
            }
            if (!encontrado) {//la administradora no esta agregada
                datosGraficoRegistros.add(new EstructuraDatosGraficoRegistros(
                        cita.getIdAdministradora().getRazonSocial(),
                        cita.getIdAdministradora().getIdAdministradora(),
                        1));
            }
            total++;
        }
        pastelRegistros = null;
        if (datosGraficoRegistros != null && !datosGraficoRegistros.isEmpty()) {
            pastelRegistros = new PieChartModel();
            for (EstructuraDatosGraficoRegistros datoGraficoRegistros : datosGraficoRegistros) {
                pastelRegistros.set(datoGraficoRegistros.getNombreAdministradora() + ": " + datoGraficoRegistros.getCantidad(), datoGraficoRegistros.getCantidad());
            }
            configurarGrafico(pastelRegistros, "Turnos sin registro asociado: " + String.valueOf(total), "s", true, true, 200);
        }
    }

    public void crearGraficoRegistrosFaltantesPrestador() {
        listaCitasSinRegistro = citasFacade.buscarFaltanRegistro(loginMB.getUsuarioActual().getIdUsuario().toString());
        //System.out.println("El prestador es: "+loginMB.getUsuarioActual().nombreCompleto());
        boolean encontrado;
        int total = 0;
        datosGraficoRegistros = new ArrayList<>();
        for (CitCitas cita : listaCitasSinRegistro) {
            //System.out.println("Tiene las citas : "+cita.getIdCita().toString());
            encontrado = false;
            for (EstructuraDatosGraficoRegistros datoGraficoRegistros : datosGraficoRegistros) {
                if (datoGraficoRegistros.getIdAdministradora() == cita.getIdAdministradora().getIdAdministradora()) {
                    encontrado = true;
                    datoGraficoRegistros.setCantidad(datoGraficoRegistros.getCantidad() + 1);
                    break;
                }
            }
            if (!encontrado) {//la administradora no esta agregada
                datosGraficoRegistros.add(new EstructuraDatosGraficoRegistros(
                        cita.getIdAdministradora().getRazonSocial(),
                        cita.getIdAdministradora().getIdAdministradora(),
                        1));
            }
            total++;
        }
        pastelRegistros = null;
        if (datosGraficoRegistros != null && !datosGraficoRegistros.isEmpty()) {
            pastelRegistros = new PieChartModel();
            for (EstructuraDatosGraficoRegistros datoGraficoRegistros : datosGraficoRegistros) {
                pastelRegistros.set(datoGraficoRegistros.getNombreAdministradora() + ": " + datoGraficoRegistros.getCantidad(), datoGraficoRegistros.getCantidad());
            }
            configurarGrafico(pastelRegistros, "Cantidad citas sin registro asociado: " + String.valueOf(total), "s", true, true, 200);
        }
    }

    public void recargarControles() {
        citaSeleccionadaTabla = null;
        datosGraficoFacturacion = new ArrayList<>();
        String perfilUsuario = loginMB.getUsuarioActual().getIdPerfil().getNombrePerfil();
        if (perfilUsuario.compareTo("SuperUsuario") == 0 || perfilUsuario.compareTo("Administrador") == 0) {
            //System.out.println("ES ADMINISTRADOR O SUPERUSUARIO");
            mostrarTabFacturacion = true;
            mostrarTabRegistrosPendientes = true;
            crearGraficoFacturacion();
            crearGraficoRegistrosFaltantes();

        } else if (perfilUsuario.compareTo("Prestador") == 0) {
            //System.out.println("ES PRESTADOR");
            mostrarTabFacturacion = false;
            mostrarTabRegistrosPendientes = true;
            crearGraficoRegistrosFaltantesPrestador();
        }
    }

    @PostConstruct
    public void inicializar() {
    }

    private void configurarGrafico(PieChartModel grafico, String titulo, String posicion, boolean relleno, boolean mostrarEtiquetas, int diametro) {
        grafico.setTitle(titulo);
        grafico.setLegendPosition(posicion);
        grafico.setFill(relleno);
        grafico.setShowDataLabels(mostrarEtiquetas);
        grafico.setDiameter(diametro);
    }

    public PieChartModel getPastelFacturacion() {
        return pastelFacturacion;
    }

    public void setPastelFacturacion(PieChartModel pastelFacturacion) {
        this.pastelFacturacion = pastelFacturacion;
    }

    public PieChartModel getPastelRegistros() {
        return pastelRegistros;
    }

    public void setPastelRegistros(PieChartModel pastelRegistros) {
        this.pastelRegistros = pastelRegistros;
    }

    public LoginMB getLoginMB() {
        return loginMB;
    }

    public void setLoginMB(LoginMB loginMB) {
        this.loginMB = loginMB;
    }

    public boolean isMostrarTabFacturacion() {
        return mostrarTabFacturacion;
    }

    public void setMostrarTabFacturacion(boolean mostrarTabFacturacion) {
        this.mostrarTabFacturacion = mostrarTabFacturacion;
    }

    public boolean isMostrarTabRegistrosPendientes() {
        return mostrarTabRegistrosPendientes;
    }

    public void setMostrarTabRegistrosPendientes(boolean mostrarTabRegistrosPendientes) {
        this.mostrarTabRegistrosPendientes = mostrarTabRegistrosPendientes;
    }

    public List<CitCitas> getListaCitasSinRegistro() {
        return listaCitasSinRegistro;
    }

    public void setListaCitasSinRegistro(List<CitCitas> listaCitasSinRegistro) {
        this.listaCitasSinRegistro = listaCitasSinRegistro;
    }

    public CitCitas getCitaSeleccionadaTabla() {
        return citaSeleccionadaTabla;
    }

    public void setCitaSeleccionadaTabla(CitCitas citaSeleccionadaTabla) {
        this.citaSeleccionadaTabla = citaSeleccionadaTabla;
    }

}
