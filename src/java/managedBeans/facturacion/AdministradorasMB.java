/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.FacAdministradora;
import modelo.entidades.FacContrato;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.FacAdministradoraFacade;
import modelo.fachadas.FacContratoFacade;
import org.primefaces.context.RequestContext;

/**
 *
 * @author santos
 */
@ManagedBean(name = "administradorasMB")
@SessionScoped
public class AdministradorasMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    FacAdministradoraFacade administradoraFacade;
    @EJB
    FacContratoFacade ContratoFacade;
    @EJB
    CfgClasificacionesFacade clasificacionesFachada;
    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------
    private FacAdministradora administradoraSeleccionada;
    private FacAdministradora administradoraSeleccionadaTabla;
    private List<FacAdministradora> listaAdministradoras;

    private FacContrato contratoSeleccionado;//contrato seleccionado de los asociados a una administradora    
    private List<FacContrato> listaContratos;//lista de contratos asociados a una administradora cargada

    private FacAdministradora administradoraCopiaSeleccionada;
    private List<FacContrato> contratosCopiaSeleccionados;//contrato seleccionado cuando se quiere copiar contratos    
    private List<FacContrato> listaContratosCopia;//lista de contratos de una administradora cuando se quiere copiar contratos
    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------

    private String idAdministradoraACopiar = "";//identificador de la administradora a ala cual se le copearan los contratos

    private String activeIndex = "0";
    private String tituloTabAdministradora = "Nueva Administradora";
    private String tituloTabContratos = "Contratos (0)";
    private String codigoAdminstradora = "";
    private String razonSocial = "";
    private String tipoAdministradora = "";
    private String tipoDocumento = "";
    private String numeroIdentificacion = "";
    private String direccion = "";
    private String telefono1 = "";
    private String telefono2 = "";
    private String departamento = "";
    private String municipio = "";
    private List<SelectItem> listaMunicipios;
    private String tipoDocumentoRepresentante = "";
    private String numeroDocumentoRepresentante = "";
    private String nombreRepresentante = "";
    private String codigoRip = "";
    private String codigoIngreso = "";
    private String codigoRubro = "";
    private String codigoConcepto = "";

    //---------------------------------------------------
    //------------- FUNCIONES INICIALES  ----------------
    //---------------------------------------------------      
    public AdministradorasMB() {
    }

    //---------------------------------------------------
    //-----------------FUNCIONES ADMINISTRADORAS --------
    //--------------------------------------------------- 
    public void limpiarFormularioAdministradoras() {

        listaContratos = new ArrayList<>();
        contratoSeleccionado = null;
        idAdministradoraACopiar = "";

        listaAdministradoras = administradoraFacade.buscarOrdenado();
        tituloTabAdministradora = "Nueva Administradora";
        tituloTabContratos = "Contratos (0)";
        administradoraSeleccionada = null;
        codigoAdminstradora = "";
        razonSocial = "";
        tipoAdministradora = "";
        tipoDocumento = "";
        numeroIdentificacion = "";
        direccion = "";
        telefono1 = "";
        telefono2 = "";
        departamento = "";
        municipio = "";
        listaMunicipios = new ArrayList<>();
        tipoDocumentoRepresentante = "";
        numeroDocumentoRepresentante = "";
        nombreRepresentante = "";
        codigoRip = "";
        codigoIngreso = "";
        codigoRubro = "";
        codigoConcepto = "";
    }
    public void recargarSiHaySeleccionado(){
        //se usa esta funcion cuando se crea un manua tarifario y al volver a contratos esten cargados los datos
        if(administradoraSeleccionada!=null){
            administradoraSeleccionadaTabla=administradoraSeleccionada;
            cargarAdministradora();
        }        
    }

    public void buscarAdministradoras() {
        listaAdministradoras = administradoraFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().execute("PF('wvTablaAdministradoras').clearFilters(); PF('wvTablaAdministradoras').getPaginator().setPage(0); PF('dialogoBuscarAdministradoras').show();");
    }

    public void cargarAdministradora() {
        if (administradoraSeleccionadaTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ninguna administradora de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        limpiarFormularioAdministradoras();
        administradoraSeleccionada = administradoraFacade.find(administradoraSeleccionadaTabla.getIdAdministradora());
        codigoAdminstradora = administradoraSeleccionada.getCodigoAdministradora();
        razonSocial = administradoraSeleccionada.getRazonSocial();
        if (administradoraSeleccionada.getTipoAdministradora() != null) {
            tipoAdministradora = administradoraSeleccionada.getTipoAdministradora().getId().toString();
        }
        if (administradoraSeleccionada.getTipoDocumento() != null) {
            tipoDocumento = administradoraSeleccionada.getTipoDocumento().getId().toString();
        }
        numeroIdentificacion = administradoraSeleccionada.getNumeroIdentificacion();
        direccion = administradoraSeleccionada.getDireccion();
        telefono1 = administradoraSeleccionada.getTelefono1();
        telefono2 = administradoraSeleccionada.getTelefono2();
        if (administradoraSeleccionada.getCodigoDepartamento() != null) {
            departamento = administradoraSeleccionada.getCodigoDepartamento().getId().toString();
            cargarMunicipios();
            municipio = administradoraSeleccionada.getCodigoMunicipio().getId().toString();
        }
        if (administradoraSeleccionada.getTipoDocumentoRepresentante() != null) {
            tipoDocumentoRepresentante = administradoraSeleccionada.getTipoDocumentoRepresentante().getId().toString();
        }
        numeroDocumentoRepresentante = administradoraSeleccionada.getNumeroDocumentoRepresentante();
        nombreRepresentante = administradoraSeleccionada.getNombreRepresentante();
        codigoRip = administradoraSeleccionada.getCodigoRip();
        codigoIngreso = administradoraSeleccionada.getCodigoIngreso();
        codigoRubro = administradoraSeleccionada.getCodigoRubro();
        codigoConcepto = administradoraSeleccionada.getCodigoConc();
        listaContratos = administradoraSeleccionada.getFacContratoList();
        if (listaContratos != null) {
            tituloTabContratos = "Contratos (" + listaContratos.size() + ")";
        }
        tituloTabAdministradora = "Datos Administradora: " + razonSocial;
        RequestContext.getCurrentInstance().execute("PF('dialogoBuscarAdministradoras').hide(); PF('wvTablaAdministradoras').clearFilters();");

    }

    public void eliminarAdministradora() {
        if (administradoraSeleccionada == null) {
            imprimirMensaje("Error", "No se ha cargado ninguna administradora", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoEliminarAdministradora').show();");
    }

    public void confirmarEliminarAdministradora() {
        if (administradoraSeleccionada == null) {
            imprimirMensaje("Error", "No se ha seleccionado ninguna administradora", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            administradoraFacade.remove(administradoraSeleccionada);
            listaAdministradoras = administradoraFacade.buscarOrdenado();
            limpiarFormularioAdministradoras();
            RequestContext.getCurrentInstance().update("IdFormAdministradoras");
            imprimirMensaje("Correcto", "La administradora ha sido eliminada", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "La administradora que intenta eliminar esta siendo usada por el sistema", FacesMessage.SEVERITY_ERROR);
        }

    }

    public void guardarAdministradora() {
        if (validacionCampoVacio(razonSocial, "Razón social")) {
            return;
        }
        if (validacionCampoVacio(codigoAdminstradora, "Código administradora")) {
            return;
        }
        if (validacionCampoVacio(tipoDocumento, "Tipo Identificación")) {
            return;
        }
        if (validacionCampoVacio(numeroIdentificacion, "Número Identificación")) {
            return;
        }
        if (administradoraSeleccionada == null) {
            guardarNuevaAdminstradora();
        } else {
            actualizarAdministradoraExistente();
        }
    }

    private void guardarNuevaAdminstradora() {
        FacAdministradora nuevaAdministradora = new FacAdministradora();
        nuevaAdministradora.setCodigoAdministradora(codigoAdminstradora);
        nuevaAdministradora.setRazonSocial(razonSocial);
        if (validarNoVacio(tipoAdministradora)) {
            nuevaAdministradora.setTipoAdministradora(clasificacionesFachada.find(Integer.parseInt(tipoAdministradora)));
        }
        if (validarNoVacio(tipoDocumento)) {
            nuevaAdministradora.setTipoDocumento(clasificacionesFachada.find(Integer.parseInt(tipoDocumento)));
        }
        nuevaAdministradora.setNumeroIdentificacion(numeroIdentificacion);
        nuevaAdministradora.setDireccion(direccion);
        nuevaAdministradora.setTelefono1(telefono1);
        nuevaAdministradora.setTelefono2(telefono2);
        if (validarNoVacio(departamento)) {
            nuevaAdministradora.setCodigoDepartamento(clasificacionesFachada.find(Integer.parseInt(departamento)));
            nuevaAdministradora.setCodigoMunicipio(clasificacionesFachada.find(Integer.parseInt(municipio)));
        }
        if (validarNoVacio(tipoDocumentoRepresentante)) {
            nuevaAdministradora.setTipoDocumentoRepresentante(clasificacionesFachada.find(Integer.parseInt(tipoDocumentoRepresentante)));
        }
        nuevaAdministradora.setNumeroDocumentoRepresentante(numeroDocumentoRepresentante);
        nuevaAdministradora.setNombreRepresentante(nombreRepresentante);
        nuevaAdministradora.setCodigoRip(codigoRip);
        nuevaAdministradora.setCodigoIngreso(codigoIngreso);
        nuevaAdministradora.setCodigoRubro(codigoRubro);
        nuevaAdministradora.setCodigoConc(codigoConcepto);
        administradoraFacade.create(nuevaAdministradora);
        limpiarFormularioAdministradoras();
        listaAdministradoras = administradoraFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().update("IdFormAdministradoras");
        imprimirMensaje("Correcto", "La administradora ha sido creada.", FacesMessage.SEVERITY_INFO);
    }

    private void actualizarAdministradoraExistente() {//realiza la actualizacion del consultorio        
        administradoraSeleccionada.setCodigoAdministradora(codigoAdminstradora);
        administradoraSeleccionada.setRazonSocial(razonSocial);
        if (validarNoVacio(tipoAdministradora)) {
            administradoraSeleccionada.setTipoAdministradora(clasificacionesFachada.find(Integer.parseInt(tipoAdministradora)));
        }
        if (validarNoVacio(tipoDocumento)) {
            administradoraSeleccionada.setTipoDocumento(clasificacionesFachada.find(Integer.parseInt(tipoDocumento)));
        }
        administradoraSeleccionada.setNumeroIdentificacion(numeroIdentificacion);
        administradoraSeleccionada.setDireccion(direccion);
        administradoraSeleccionada.setTelefono1(telefono1);
        administradoraSeleccionada.setTelefono2(telefono2);
        if (validarNoVacio(departamento)) {
            administradoraSeleccionada.setCodigoDepartamento(clasificacionesFachada.find(Integer.parseInt(departamento)));
            administradoraSeleccionada.setCodigoMunicipio(clasificacionesFachada.find(Integer.parseInt(municipio)));
        }
        if (validarNoVacio(tipoDocumentoRepresentante)) {
            administradoraSeleccionada.setTipoDocumentoRepresentante(clasificacionesFachada.find(Integer.parseInt(tipoDocumentoRepresentante)));
        }
        administradoraSeleccionada.setNumeroDocumentoRepresentante(numeroDocumentoRepresentante);
        administradoraSeleccionada.setNombreRepresentante(nombreRepresentante);
        administradoraSeleccionada.setCodigoRip(codigoRip);
        administradoraSeleccionada.setCodigoIngreso(codigoIngreso);
        administradoraSeleccionada.setCodigoRubro(codigoRubro);
        administradoraSeleccionada.setCodigoConc(codigoConcepto);
        administradoraFacade.edit(administradoraSeleccionada);
        limpiarFormularioAdministradoras();
        listaAdministradoras = administradoraFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().update("IdFormAdministradoras");
        imprimirMensaje("Correcto", "La Administradora ha sido actualizada.", FacesMessage.SEVERITY_INFO);
    }

    public void cargarMunicipios() {
        listaMunicipios = new ArrayList<>();
        if (departamento != null && departamento.length() != 0) {
            List<CfgClasificaciones> listaM = clasificacionesFachada.buscarMunicipioPorDepartamento(clasificacionesFachada.find(Integer.parseInt(departamento)).getCodigo());
            for (CfgClasificaciones mun : listaM) {
                listaMunicipios.add(new SelectItem(mun.getId(), mun.getDescripcion()));
            }
        }
    }

    //---------------------------------------------------
    //-----------------FUNCIONES CONTRATOS --------------
    //---------------------------------------------------
    public void nuevoContrato() {//se invoca funcion javaScript(cargarTab -> home.xhtml) que carga la pestaña de contratos y selecciona la administradora especificada
        if (administradoraSeleccionada != null) {
            RequestContext.getCurrentInstance().execute("window.parent.cargarTab('Contratos','facturacion/contratos.xhtml','idAdministradora;" + administradoraSeleccionada.getIdAdministradora() + "')");
        } else {
            imprimirMensaje("Error", "Se debe seleccionar una administradora", FacesMessage.SEVERITY_ERROR);
        }

    }
    public void editarContrato() {//se invoca funcion javaScript(cargarTab -> home.xhtml) que carga la pestaña de contratos y selecciona la administradora especificada
        if (contratoSeleccionado != null) {
            RequestContext.getCurrentInstance().execute("window.parent.cargarTab('Contratos','facturacion/contratos.xhtml','idContrato;" + contratoSeleccionado.getIdContrato() + "')");
        } else {
            imprimirMensaje("Error", "Se debe seleccionar un contrato de la tabla", FacesMessage.SEVERITY_ERROR);
        }

    }
    public void cambiaAdministradoraCopia() {//cambia la administradora de la cual se van a copiar los contratos
        contratosCopiaSeleccionados = null;
        if (validarNoVacio(idAdministradoraACopiar)) {
            administradoraCopiaSeleccionada = administradoraFacade.find(Integer.parseInt(idAdministradoraACopiar));
            if (administradoraCopiaSeleccionada != null) {
                listaContratosCopia = administradoraCopiaSeleccionada.getFacContratoList();
            } else {
                listaContratosCopia = new ArrayList<>();
            }
        }
    }

    public void copiarContratos() {

    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------
    public FacAdministradora getAdministradoraSeleccionada() {
        return administradoraSeleccionada;
    }

    public void setAdministradoraSeleccionada(FacAdministradora administradoraSeleccionada) {
        this.administradoraSeleccionada = administradoraSeleccionada;
    }

    public FacAdministradora getAdministradoraSeleccionadaTabla() {
        return administradoraSeleccionadaTabla;
    }

    public void setAdministradoraSeleccionadaTabla(FacAdministradora administradoraSeleccionadaTabla) {
        this.administradoraSeleccionadaTabla = administradoraSeleccionadaTabla;
    }

    public List<FacAdministradora> getListaAdministradoras() {
        return listaAdministradoras;
    }

    public void setListaAdministradoras(List<FacAdministradora> listaAdministradoras) {
        this.listaAdministradoras = listaAdministradoras;
    }

    public String getTituloTabAdministradora() {
        return tituloTabAdministradora;
    }

    public void setTituloTabAdministradora(String tituloTabAdministradora) {
        this.tituloTabAdministradora = tituloTabAdministradora;
    }

    public String getCodigoAdminstradora() {
        return codigoAdminstradora;
    }

    public void setCodigoAdminstradora(String codigoAdminstradora) {
        this.codigoAdminstradora = codigoAdminstradora;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipoAdministradora() {
        return tipoAdministradora;
    }

    public void setTipoAdministradora(String tipoAdministradora) {
        this.tipoAdministradora = tipoAdministradora;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public List<SelectItem> getListaMunicipios() {
        return listaMunicipios;
    }

    public void setListaMunicipios(List<SelectItem> listaMunicipios) {
        this.listaMunicipios = listaMunicipios;
    }

    public String getTipoDocumentoRepresentante() {
        return tipoDocumentoRepresentante;
    }

    public void setTipoDocumentoRepresentante(String tipoDocumentoRepresentante) {
        this.tipoDocumentoRepresentante = tipoDocumentoRepresentante;
    }

    public String getNumeroDocumentoRepresentante() {
        return numeroDocumentoRepresentante;
    }

    public void setNumeroDocumentoRepresentante(String numeroDocumentoRepresentante) {
        this.numeroDocumentoRepresentante = numeroDocumentoRepresentante;
    }

    public String getNombreRepresentante() {
        return nombreRepresentante;
    }

    public void setNombreRepresentante(String nombreRepresentante) {
        this.nombreRepresentante = nombreRepresentante;
    }

    public String getCodigoRip() {
        return codigoRip;
    }

    public void setCodigoRip(String codigoRip) {
        this.codigoRip = codigoRip;
    }

    public String getCodigoIngreso() {
        return codigoIngreso;
    }

    public void setCodigoIngreso(String codigoIngreso) {
        this.codigoIngreso = codigoIngreso;
    }

    public String getCodigoRubro() {
        return codigoRubro;
    }

    public void setCodigoRubro(String codigoRubro) {
        this.codigoRubro = codigoRubro;
    }

    public String getCodigoConcepto() {
        return codigoConcepto;
    }

    public void setCodigoConcepto(String codigoConcepto) {
        this.codigoConcepto = codigoConcepto;
    }

    public String getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(String activeIndex) {
        this.activeIndex = activeIndex;
    }

    public FacContrato getContratoSeleccionado() {
        return contratoSeleccionado;
    }

    public void setContratoSeleccionado(FacContrato contratoSeleccionado) {
        this.contratoSeleccionado = contratoSeleccionado;
    }

    public List<FacContrato> getListaContratos() {
        return listaContratos;
    }

    public void setListaContratos(List<FacContrato> listaContratos) {
        this.listaContratos = listaContratos;
    }

    public FacAdministradora getAdministradoraCopiaSeleccionada() {
        return administradoraCopiaSeleccionada;
    }

    public void setAdministradoraCopiaSeleccionada(FacAdministradora administradoraCopiaSeleccionada) {
        this.administradoraCopiaSeleccionada = administradoraCopiaSeleccionada;
    }

    public List<FacContrato> getContratosCopiaSeleccionados() {
        return contratosCopiaSeleccionados;
    }

    public void setContratosCopiaSeleccionados(List<FacContrato> contratosCopiaSeleccionados) {
        this.contratosCopiaSeleccionados = contratosCopiaSeleccionados;
    }

    public List<FacContrato> getListaContratosCopia() {
        return listaContratosCopia;
    }

    public void setListaContratosCopia(List<FacContrato> listaContratosCopia) {
        this.listaContratosCopia = listaContratosCopia;
    }

    public String getIdAdministradoraACopiar() {
        return idAdministradoraACopiar;
    }

    public void setIdAdministradoraACopiar(String idAdministradoraACopiar) {
        this.idAdministradoraACopiar = idAdministradoraACopiar;
    }

    public String getTituloTabContratos() {
        return tituloTabContratos;
    }

    public void setTituloTabContratos(String tituloTabContratos) {
        this.tituloTabContratos = tituloTabContratos;
    }

}
