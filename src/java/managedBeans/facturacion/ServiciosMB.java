/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import beans.enumeradores.ClasificacionesEnum;
import beans.utilidades.MetodosGenerales;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import managedBeans.seguridad.AplicacionGeneralMB;
import modelo.entidades.CfgCentroCosto;
import modelo.entidades.FacServicio;
import modelo.fachadas.CfgCentroCostoFacade;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgDiagnosticoFacade;
import modelo.fachadas.FacServicioFacade;
import org.primefaces.context.RequestContext;

/**
 *
 * @author santos
 */
@Named(value = "serviciosMB")
@SessionScoped
public class ServiciosMB extends MetodosGenerales implements Serializable {
//---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------

    @EJB
    FacServicioFacade servicioFacade;
    @EJB
    CfgClasificacionesFacade clasificacionesFacade;
    @EJB
    CfgCentroCostoFacade centroCostoFacade;
    @EJB
    CfgDiagnosticoFacade diagnosticoFacade;
    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------
    FacServicio servicioSeleccionado;
    FacServicio servicioSeleccionadoTabla;
    private List<FacServicio> listaServicios;
    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------
    private String tituloTabServicios = "Nuevo servicio";
    private String codigoServicio = "";

    private String codigoCup = "";
    private String codigoCums = "";
    private String codigoSoat = "";
    private String codigoIss = "";
    private String nombreServicio = "";
    private String grupoServicio = "";
    private String especialidad = "";
    private String centroCosto = "";
    private String tipoSevicio = "";
    private double factorISS = 0;
    private double factorSOAT = 0;
    private double valorParticular = 0;
    private boolean tipoPos = false;
    private double porcentajeHonorarios = 0;
    private String tipoServicioFurip = "";
    private String sexo = "";
    private String zona = "";
    private int edadInicial = 0;
    private String unidadEdadInicial = "";
    private int edadFinal = 0;
    private String unidadEdadFinal = "";
    private String ripAplica = "";
    private String finalidad = "";
    private String codigoAmbito = "";
    private String codigoActQtx = "";
    private String codigoDiagnostico = "";
    private boolean autorizacion = false;
    private boolean visible = true;
    private AplicacionGeneralMB aplicacionGeneralMB;

    //---------------------------------------------------
    //------------- FUNCIONES INICIALES  ----------------
    //---------------------------------------------------
    @PostConstruct
    private void inicializar() {
    }

    public ServiciosMB() {
        aplicacionGeneralMB=FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{aplicacionGeneralMB}", AplicacionGeneralMB.class);
    }

    //---------------------------------------------------
    //----------------- FUNCIONES SERVICIOS -------------
    //---------------------------------------------------      
    public void limpiarFormularioServicios() {
        tituloTabServicios = "Nuevo servicio";
        servicioSeleccionado = null;
        codigoServicio = "";
        codigoCup = "";
        codigoCums = "";
        codigoSoat = "";
        codigoIss = "";
        nombreServicio = "";
        grupoServicio = "";
        especialidad = "";
        centroCosto = "";
        tipoSevicio = "";
        factorISS = 0;
        factorSOAT = 0;
        tipoPos = false;
        porcentajeHonorarios = 0;
        tipoServicioFurip = "";
        sexo = "";
        zona = "";
        edadInicial = 0;
        unidadEdadInicial = "";
        edadFinal = 0;
        unidadEdadFinal = "";
        ripAplica = "";
        finalidad = "";
        codigoAmbito = "";
        codigoActQtx = "";
        codigoDiagnostico = "";
        autorizacion = false;
        visible = true;
    }

    public void buscarServicio() {
        listaServicios = servicioFacade.buscarTodosOrdenado();
        servicioSeleccionadoTabla = null;
        RequestContext.getCurrentInstance().execute("PF('wvTablaServicios').clearFilters(); PF('wvTablaServicios').getPaginator().setPage(0); PF('dialogoBuscarServicios').show();");
    }

    public void cargarServicio() {
        if (servicioSeleccionadoTabla == null) {
            imprimirMensaje("Error", "Se debe seleccionar un servicio de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        limpiarFormularioServicios();
        servicioSeleccionado = servicioFacade.find(servicioSeleccionadoTabla.getIdServicio());
        tituloTabServicios = "Servicio: " + servicioSeleccionado.getCodigoServicio();
        codigoServicio = servicioSeleccionado.getCodigoServicio();
        codigoCup = servicioSeleccionado.getCodigoCup();
        codigoCums = servicioSeleccionado.getCodigoCums();
        codigoSoat = servicioSeleccionado.getCodigoSoat();
        codigoIss = servicioSeleccionado.getCodigoIss();
        nombreServicio = servicioSeleccionado.getNombreServicio();
        if (servicioSeleccionado.getGrupoServicio() != null) {
            grupoServicio = servicioSeleccionado.getGrupoServicio().getId().toString();
        }
        if (servicioSeleccionado.getEspecialidad() != null) {
            especialidad = servicioSeleccionado.getEspecialidad().getId().toString();
        }
        if (servicioSeleccionado.getCentroCosto() != null) {
            centroCosto = servicioSeleccionado.getCentroCosto().getIdCentroCosto().toString();
        }
        if (servicioSeleccionado.getTipoSevicio() != null) {
            tipoSevicio = servicioSeleccionado.getTipoSevicio().getId().toString();
        }

        if (servicioSeleccionado.getFactorIss() != null) {
            factorISS = servicioSeleccionado.getFactorIss();
        }
        if (servicioSeleccionado.getFactorSoat() != null) {
            factorSOAT = servicioSeleccionado.getFactorSoat();
        }
        if (servicioSeleccionado.getValorParticular()!= null) {
            valorParticular = servicioSeleccionado.getValorParticular();
        }
        if (servicioSeleccionado.getTipoPos() != null) {
            tipoPos = servicioSeleccionado.getTipoPos();
        }
        if (servicioSeleccionado.getPorcentajeHonorarios() != null) {
            porcentajeHonorarios = servicioSeleccionado.getPorcentajeHonorarios();
        }
        tipoServicioFurip = servicioSeleccionado.getTipoServicioFurip();
        if (servicioSeleccionado.getSexo() != null) {
            sexo = servicioSeleccionado.getSexo().getId().toString();
        }
        if (servicioSeleccionado.getZona() != null) {
            zona = servicioSeleccionado.getZona().getId().toString();
        }
        if (servicioSeleccionado.getEdadInicial() != null) {
            edadInicial = servicioSeleccionado.getEdadInicial();
        }
        if (servicioSeleccionado.getUnidadEdadInicial() != null) {
            unidadEdadInicial = servicioSeleccionado.getUnidadEdadInicial().getId().toString();
        }
        if (servicioSeleccionado.getEdadFinal() != null) {
            edadFinal = servicioSeleccionado.getEdadFinal();
        }
        if (servicioSeleccionado.getUnidadEdadFinal() != null) {
            unidadEdadFinal = servicioSeleccionado.getUnidadEdadFinal().getId().toString();
        }
        ripAplica = servicioSeleccionado.getRipAplica();
        if (servicioSeleccionado.getFinalidad() != null) {
            finalidad = servicioSeleccionado.getFinalidad().getId().toString();
        }
        if (servicioSeleccionado.getAmbito() != null) {
            codigoAmbito = servicioSeleccionado.getAmbito().getId().toString();
        }
        if (servicioSeleccionado.getActoQuirurgico() != null) {
            codigoActQtx = servicioSeleccionado.getActoQuirurgico().getId().toString();
        }
        if (servicioSeleccionado.getCodigoDiagnostico() != null) {
            codigoDiagnostico = servicioSeleccionado.getCodigoDiagnostico().getNombreDiagnostico();
        }
        if (servicioSeleccionado.getAutorizacion() != null) {
            autorizacion = servicioSeleccionado.getAutorizacion();
        }
        if (servicioSeleccionado.getVisible() != null) {
            visible = servicioSeleccionado.getVisible();
        }
        tituloTabServicios = "Servicio: " + nombreServicio;
        RequestContext.getCurrentInstance().execute("PF('dialogoBuscarServicios').hide(); PF('wvTablaServicios').clearFilters(); PF('wvTablaServicios').getPaginator().setPage(0);");
    }

    public void eliminarServicio() {
        if (servicioSeleccionado == null) {
            imprimirMensaje("Error", "No se ha cargado ningún servicio", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoEliminarServicio').show();");
    }

    public void confirmarEliminarServicio() {
        if (servicioSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ninguna caja", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            servicioFacade.remove(servicioSeleccionado);
            limpiarFormularioServicios();
            aplicacionGeneralMB.cargarClasificacion(ClasificacionesEnum.Servicios);
            RequestContext.getCurrentInstance().update("IdFormServicios");
            imprimirMensaje("Correcto", "El servicio ha sido eliminado", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El servicio que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void guardarServicio() {
        if (validacionCampoVacio(codigoServicio, "Código Interno")) {
            return;
        }
        if (validacionCampoVacio(nombreServicio, "Nombre servicio")) {
            return;
        }
        if (servicioSeleccionado == null) {
            guardarNuevoServicio();
        } else {
            actualizarServicioExistente();
        }
        aplicacionGeneralMB.cargarClasificacion(ClasificacionesEnum.Servicios);

    }

    private void guardarNuevoServicio() {
        FacServicio nuevoServicio = new FacServicio();
        nuevoServicio.setCodigoServicio(codigoServicio);
        nuevoServicio.setCodigoCup(codigoCup);
        nuevoServicio.setCodigoCums(codigoCums);
        nuevoServicio.setCodigoSoat(codigoSoat);
        nuevoServicio.setCodigoIss(codigoIss);
        nuevoServicio.setNombreServicio(nombreServicio);
        if (validarNoVacio(grupoServicio)) {
            nuevoServicio.setGrupoServicio(clasificacionesFacade.find(Integer.parseInt(grupoServicio)));
        }
        if (validarNoVacio(especialidad)) {
            nuevoServicio.setEspecialidad(clasificacionesFacade.find(Integer.parseInt(especialidad)));
        }
        if (validarNoVacio(centroCosto)) {
            nuevoServicio.setCentroCosto(centroCostoFacade.find(Integer.parseInt(centroCosto)));
        }
        if (validarNoVacio(tipoSevicio)) {
            nuevoServicio.setTipoSevicio(clasificacionesFacade.find(Integer.parseInt(tipoSevicio)));
        }
        nuevoServicio.setFactorIss(factorISS);
        nuevoServicio.setFactorSoat(factorSOAT);
        nuevoServicio.setValorParticular(valorParticular);
        
        nuevoServicio.setTipoPos(tipoPos);
        nuevoServicio.setPorcentajeHonorarios(porcentajeHonorarios);
        nuevoServicio.setTipoServicioFurip(tipoServicioFurip);
        if (validarNoVacio(sexo)) {
            nuevoServicio.setSexo(clasificacionesFacade.find(Integer.parseInt(sexo)));
        }
        if (validarNoVacio(zona)) {
            nuevoServicio.setZona(clasificacionesFacade.find(Integer.parseInt(zona)));
        }
        nuevoServicio.setEdadInicial(edadInicial);
        nuevoServicio.setEdadFinal(edadFinal);
        if (validarNoVacio(unidadEdadInicial)) {
            nuevoServicio.setUnidadEdadInicial(clasificacionesFacade.find(Integer.parseInt(unidadEdadInicial)));
        }
        if (validarNoVacio(unidadEdadFinal)) {
            nuevoServicio.setUnidadEdadFinal(clasificacionesFacade.find(Integer.parseInt(unidadEdadFinal)));
        }
        nuevoServicio.setRipAplica(ripAplica);
        if (validarNoVacio(finalidad)) {
            nuevoServicio.setFinalidad(clasificacionesFacade.find(Integer.parseInt(finalidad)));
        }
        if (validarNoVacio(codigoAmbito)) {
            nuevoServicio.setAmbito(clasificacionesFacade.find(Integer.parseInt(codigoAmbito)));
        }
        if (validarNoVacio(codigoActQtx)) {
            nuevoServicio.setActoQuirurgico(clasificacionesFacade.find(Integer.parseInt(codigoActQtx)));
        }        
        if (validarNoVacio(codigoDiagnostico)) {
            nuevoServicio.setCodigoDiagnostico(diagnosticoFacade.find(codigoDiagnostico));
        }
        nuevoServicio.setAutorizacion(autorizacion);
        nuevoServicio.setVisible(visible);
        tituloTabServicios = "Nuevo Servicio";
        servicioFacade.create(nuevoServicio);
        limpiarFormularioServicios();
        RequestContext.getCurrentInstance().update("IdFormServicios");
        imprimirMensaje("Correcto", "El servicio ha sido creado.", FacesMessage.SEVERITY_INFO);
    }

    private void actualizarServicioExistente() {//realiza la actualizacion del consultorio        

        servicioSeleccionado.setCodigoServicio(codigoServicio);
        servicioSeleccionado.setCodigoCup(codigoCup);
        servicioSeleccionado.setCodigoCums(codigoCums);
        servicioSeleccionado.setCodigoSoat(codigoSoat);
        servicioSeleccionado.setCodigoIss(codigoIss);
        servicioSeleccionado.setNombreServicio(nombreServicio);
        if (validarNoVacio(grupoServicio)) {
            servicioSeleccionado.setGrupoServicio(clasificacionesFacade.find(Integer.parseInt(grupoServicio)));
        }
        if (validarNoVacio(especialidad)) {
            servicioSeleccionado.setEspecialidad(clasificacionesFacade.find(Integer.parseInt(especialidad)));
        }
        if (validarNoVacio(centroCosto)) {
            servicioSeleccionado.setCentroCosto(centroCostoFacade.find(Integer.parseInt(centroCosto)));
        }
        if (validarNoVacio(tipoSevicio)) {
            servicioSeleccionado.setTipoSevicio(clasificacionesFacade.find(Integer.parseInt(tipoSevicio)));
        }
        servicioSeleccionado.setFactorIss(factorISS);
        servicioSeleccionado.setFactorSoat(factorSOAT);
        servicioSeleccionado.setValorParticular(valorParticular);
        servicioSeleccionado.setTipoPos(tipoPos);
        servicioSeleccionado.setPorcentajeHonorarios(porcentajeHonorarios);
        servicioSeleccionado.setTipoServicioFurip(tipoServicioFurip);
        if (validarNoVacio(sexo)) {
            servicioSeleccionado.setSexo(clasificacionesFacade.find(Integer.parseInt(sexo)));
        }
        if (validarNoVacio(zona)) {
            servicioSeleccionado.setZona(clasificacionesFacade.find(Integer.parseInt(zona)));
        }
        servicioSeleccionado.setEdadInicial(edadInicial);
        servicioSeleccionado.setEdadFinal(edadFinal);
        if (validarNoVacio(unidadEdadInicial)) {
            servicioSeleccionado.setUnidadEdadInicial(clasificacionesFacade.find(Integer.parseInt(unidadEdadInicial)));
        }
        if (validarNoVacio(unidadEdadFinal)) {
            servicioSeleccionado.setUnidadEdadFinal(clasificacionesFacade.find(Integer.parseInt(unidadEdadFinal)));
        }
        servicioSeleccionado.setRipAplica(ripAplica);
        
        if (validarNoVacio(finalidad)) {
            servicioSeleccionado.setFinalidad(clasificacionesFacade.find(Integer.parseInt(finalidad)));
        }
        if (validarNoVacio(codigoAmbito)) {
            servicioSeleccionado.setAmbito(clasificacionesFacade.find(Integer.parseInt(codigoAmbito)));
        }
        if (validarNoVacio(codigoActQtx)) {
            servicioSeleccionado.setActoQuirurgico(clasificacionesFacade.find(Integer.parseInt(codigoActQtx)));
        }
        if (validarNoVacio(codigoDiagnostico)) {
            servicioSeleccionado.setCodigoDiagnostico(diagnosticoFacade.find(codigoDiagnostico));
        }
        servicioSeleccionado.setAutorizacion(autorizacion);
        servicioSeleccionado.setVisible(visible);
        servicioFacade.edit(servicioSeleccionado);
        tituloTabServicios = "Nuevo Servicio";
        limpiarFormularioServicios();
        RequestContext.getCurrentInstance().update("IdFormServicios");
        imprimirMensaje("Correcto", "El servicio ha sido actualizado.", FacesMessage.SEVERITY_INFO);
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------
    public String getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(String codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public String getCodigoCup() {
        return codigoCup;
    }

    public void setCodigoCup(String codigoCup) {
        this.codigoCup = codigoCup;
    }

    public String getCodigoCums() {
        return codigoCums;
    }

    public void setCodigoCums(String codigoCums) {
        this.codigoCums = codigoCums;
    }

    public String getCodigoSoat() {
        return codigoSoat;
    }

    public void setCodigoSoat(String codigoSoat) {
        this.codigoSoat = codigoSoat;
    }

    public String getCodigoIss() {
        return codigoIss;
    }

    public void setCodigoIss(String codigoIss) {
        this.codigoIss = codigoIss;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public String getGrupoServicio() {
        return grupoServicio;
    }

    public void setGrupoServicio(String grupoServicio) {
        this.grupoServicio = grupoServicio;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getCentroCosto() {
        return centroCosto;
    }

    public void setCentroCosto(String centroCosto) {
        this.centroCosto = centroCosto;
    }

    public String getTipoSevicio() {
        return tipoSevicio;
    }

    public void setTipoSevicio(String tipoSevicio) {
        this.tipoSevicio = tipoSevicio;
    }

    public int getEdadInicial() {
        return edadInicial;
    }

    public void setEdadInicial(int edadInicial) {
        this.edadInicial = edadInicial;
    }

    public String getUnidadEdadInicial() {
        return unidadEdadInicial;
    }

    public void setUnidadEdadInicial(String unidadEdadInicial) {
        this.unidadEdadInicial = unidadEdadInicial;
    }

    public int getEdadFinal() {
        return edadFinal;
    }

    public void setEdadFinal(int edadFinal) {
        this.edadFinal = edadFinal;
    }

    public String getUnidadEdadFinal() {
        return unidadEdadFinal;
    }

    public void setUnidadEdadFinal(String unidadEdadFinal) {
        this.unidadEdadFinal = unidadEdadFinal;
    }

    public double getFactorISS() {
        return factorISS;
    }

    public void setFactorISS(double factorISS) {
        this.factorISS = factorISS;
    }

    public double getFactorSOAT() {
        return factorSOAT;
    }

    public void setFactorSOAT(double factorSOAT) {
        this.factorSOAT = factorSOAT;
    }

    public boolean getTipoPos() {
        return tipoPos;
    }

    public void setTipoPos(boolean tipoPos) {
        this.tipoPos = tipoPos;
    }

    public String getTipoServicioFurip() {
        return tipoServicioFurip;
    }

    public void setTipoServicioFurip(String tipoServicioFurip) {
        this.tipoServicioFurip = tipoServicioFurip;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getRipAplica() {
        return ripAplica;
    }

    public void setRipAplica(String ripAplica) {
        this.ripAplica = ripAplica;
    }

    public String getFinalidad() {
        return finalidad;
    }

    public void setFinalidad(String finalidad) {
        this.finalidad = finalidad;
    }

    public String getCodigoAmbito() {
        return codigoAmbito;
    }

    public void setCodigoAmbito(String codigoAmbito) {
        this.codigoAmbito = codigoAmbito;
    }

    public String getCodigoActQtx() {
        return codigoActQtx;
    }

    public void setCodigoActQtx(String codigoActQtx) {
        this.codigoActQtx = codigoActQtx;
    }

    public String getCodigoDiagnostico() {
        return codigoDiagnostico;
    }

    public void setCodigoDiagnostico(String codigoDiagnostico) {
        this.codigoDiagnostico = codigoDiagnostico;
    }

    public FacServicio getServicioSeleccionado() {
        return servicioSeleccionado;
    }

    public void setServicioSeleccionado(FacServicio servicioSeleccionado) {
        this.servicioSeleccionado = servicioSeleccionado;
    }

    public FacServicio getServicioSeleccionadoTabla() {
        return servicioSeleccionadoTabla;
    }

    public void setServicioSeleccionadoTabla(FacServicio servicioSeleccionadoTabla) {
        this.servicioSeleccionadoTabla = servicioSeleccionadoTabla;
    }

    public List<FacServicio> getListaServicios() {
        return listaServicios;
    }

    public void setListaServicios(List<FacServicio> listaServicios) {
        this.listaServicios = listaServicios;
    }

    public String getTituloTabServicios() {
        return tituloTabServicios;
    }

    public void setTituloTabServicios(String tituloTabServicios) {
        this.tituloTabServicios = tituloTabServicios;
    }

    public double getPorcentajeHonorarios() {
        return porcentajeHonorarios;
    }

    public void setPorcentajeHonorarios(double porcentajeHonorarios) {
        this.porcentajeHonorarios = porcentajeHonorarios;
    }

    public void setEdadInicial(Short edadInicial) {
        this.edadInicial = edadInicial;
    }

    public void setEdadFinal(Short edadFinal) {
        this.edadFinal = edadFinal;
    }

    public boolean isAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(boolean autorizacion) {
        this.autorizacion = autorizacion;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public double getValorParticular() {
        return valorParticular;
    }

    public void setValorParticular(double valorParticular) {
        this.valorParticular = valorParticular;
    }

}
