/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import beans.enumeradores.ClasificacionesEnum;
import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import managedBeans.seguridad.AplicacionGeneralMB;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.FacAdministradora;
import modelo.entidades.FacContrato;
import modelo.entidades.FacManualTarifario;
import modelo.entidades.FacManualTarifarioInsumo;
import modelo.entidades.FacManualTarifarioInsumoPK;
import modelo.entidades.FacManualTarifarioMedicamentoPK;
import modelo.entidades.FacManualTarifarioPaquetePK;
import modelo.entidades.FacManualTarifarioServicioPK;
import modelo.entidades.FacManualTarifarioMedicamento;
import modelo.entidades.FacManualTarifarioPaquete;
import modelo.entidades.FacManualTarifarioServicio;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.FacAdministradoraFacade;
import modelo.fachadas.FacContratoFacade;
import modelo.fachadas.FacManualTarifarioFacade;
import modelo.fachadas.FacManualTarifarioInsumoFacade;
import modelo.fachadas.FacManualTarifarioMedicamentoFacade;
import modelo.fachadas.FacManualTarifarioPaqueteFacade;
import modelo.fachadas.FacManualTarifarioServicioFacade;
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
    FacContratoFacade contratoFacade;
    @EJB
    FacManualTarifarioFacade manualTarifarioFacade;
    @EJB
    CfgClasificacionesFacade clasificacionesFachada;
    @EJB
    FacManualTarifarioServicioFacade manualTarifarioServicioFacade;
    @EJB
    FacManualTarifarioInsumoFacade manualTarifarioInsumoFacade;
    @EJB
    FacManualTarifarioMedicamentoFacade manualTarifarioMedicamentoFacade;
    @EJB
    FacManualTarifarioPaqueteFacade manualTarifarioPaqueteFacade;
    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------
    private FacAdministradora administradoraSeleccionada;
    private FacAdministradora administradoraSeleccionadaTabla;
    private List<FacAdministradora> listaAdministradoras;

    private FacContrato contratoSeleccionado;//contrato seleccionado de los asociados a una administradora    
    private List<FacContrato> listaContratos;//lista de contratos asociados a una administradora cargada

    private FacAdministradora administradoraCopiaSeleccionada;
    private List<FacContrato> listaContratosCopia;//lista de contratos de una administradora cuando se quiere copiar contratos
    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------

    private String idAdministradoraACopiar = "";//identificador de la administradora a ala cual se le copiaran los contratos
    private String prefijo = "";
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
    private AplicacionGeneralMB aplicacionGeneralMB;

    //---------------------------------------------------
    //------------- FUNCIONES INICIALES  ----------------
    //---------------------------------------------------      
    public AdministradorasMB() {
        aplicacionGeneralMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{aplicacionGeneralMB}", AplicacionGeneralMB.class);
    }

    //---------------------------------------------------
    //-----------------FUNCIONES ADMINISTRADORAS --------
    //--------------------------------------------------- 
    public void limpiarFormularioAdministradoras() {

        listaContratos = new ArrayList<>();
        contratoSeleccionado = null;
        idAdministradoraACopiar = "";
        prefijo="";
        listaContratosCopia= new ArrayList<>();

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

    public void recargarSiHaySeleccionado() {
        //se usa esta funcion cuando se crea un manua tarifario y al volver a contratos esten cargados los datos
        if (administradoraSeleccionada != null) {
            administradoraSeleccionadaTabla = administradoraSeleccionada;
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

    public void clickBtnCopiarContratos() {
        if (administradoraSeleccionada == null) {
            imprimirMensaje("Error", "No se ha cargado ninguna administradora", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoCopiarContratos').show();");
    }

    public void copiarContratos() {
        if (validacionCampoVacio(prefijo, "Prefijo")) {
            return;
        }
        if (validacionCampoVacio(idAdministradoraACopiar, "Administradora")) {
            return;
        }
        if (listaContratosCopia == null || listaContratosCopia.isEmpty()) {
            imprimirMensaje("Error", "La administradora seleccionada no tiene contratos", FacesMessage.SEVERITY_ERROR);
            return;
        }
        FacContrato contratoAux;
        FacManualTarifario manualAux;
        FacManualTarifarioInsumo nuevoManualTarifarioInsumo;
        FacManualTarifarioMedicamento nuevoManualTarifarioMedicamento;
        FacManualTarifarioPaquete nuevoManualTarifarioPaquete;
        FacManualTarifarioServicio nuevoManualTarifarioServicio;

        //validar que no existan contratos ni manuales con igual nombre
        for (FacContrato contrato : listaContratosCopia) {
            contratoAux = contratoFacade.buscarPorNombre(prefijo + contrato.getDescripcion());
            if (contratoAux != null) {
                imprimirMensaje("Error", "Ya existe un contrato con el igual nombre, debe escoger otro prefijo", FacesMessage.SEVERITY_ERROR);
                return;
            }
            if (contrato.getIdManualTarifario() != null) {
                manualAux = manualTarifarioFacade.buscarPorNombre(prefijo + contrato.getIdManualTarifario().getNombreManualTarifario());
                if (manualAux != null) {
                    imprimirMensaje("Error", "Ya existe un manual tarifario con el igual nombre, debe escoger otro prefijo", FacesMessage.SEVERITY_ERROR);
                    return;
                }
            }
        }
        //realizar la copia de contratos y manuales
        for (FacContrato contrato : listaContratosCopia) {
            manualAux = null;
            if (contrato.getIdManualTarifario() != null) {

                manualAux = new FacManualTarifario();
                manualAux.setCodigoManualTarifario(prefijo + contrato.getIdManualTarifario().getCodigoManualTarifario());
                manualAux.setNombreManualTarifario(prefijo + contrato.getIdManualTarifario().getNombreManualTarifario());
                manualTarifarioFacade.create(manualAux);
                manualAux = manualTarifarioFacade.find(manualAux.getIdManualTarifario());

                for (FacManualTarifarioInsumo manualTarifarioInsumo : contrato.getIdManualTarifario().getFacManualTarifarioInsumoList()) {
                    nuevoManualTarifarioInsumo = new FacManualTarifarioInsumo();
                    FacManualTarifarioInsumoPK llavePK = new FacManualTarifarioInsumoPK();
                    llavePK.setIdManualTarifario(manualAux.getIdManualTarifario());
                    llavePK.setIdInsumo(manualTarifarioInsumo.getFacManualTarifarioInsumoPK().getIdInsumo());
                    nuevoManualTarifarioInsumo.setFacManualTarifarioInsumoPK(llavePK);
                    nuevoManualTarifarioInsumo.setActivo(manualTarifarioInsumo.getActivo());
                    nuevoManualTarifarioInsumo.setDescuento(manualTarifarioInsumo.getDescuento());
                    nuevoManualTarifarioInsumo.setObservacion(manualTarifarioInsumo.getObservacion());
                    nuevoManualTarifarioInsumo.setValorInicial(manualTarifarioInsumo.getValorInicial());
                    nuevoManualTarifarioInsumo.setValorFinal(manualTarifarioInsumo.getValorFinal());
                    manualTarifarioInsumoFacade.create(nuevoManualTarifarioInsumo);
                }

                for (FacManualTarifarioMedicamento manualTarifarioMedicamento : contrato.getIdManualTarifario().getFacManualTarifarioMedicamentoList()) {
                    nuevoManualTarifarioMedicamento = new FacManualTarifarioMedicamento();
                    FacManualTarifarioMedicamentoPK llavePK = new FacManualTarifarioMedicamentoPK();
                    llavePK.setIdManualTarifario(manualAux.getIdManualTarifario());
                    llavePK.setIdMedicamento(manualTarifarioMedicamento.getFacManualTarifarioMedicamentoPK().getIdMedicamento());
                    nuevoManualTarifarioMedicamento.setFacManualTarifarioMedicamentoPK(llavePK);
                    nuevoManualTarifarioMedicamento.setActivo(manualTarifarioMedicamento.getActivo());
                    nuevoManualTarifarioMedicamento.setDescuento(manualTarifarioMedicamento.getDescuento());
                    nuevoManualTarifarioMedicamento.setObservacion(manualTarifarioMedicamento.getObservacion());
                    nuevoManualTarifarioMedicamento.setValorInicial(manualTarifarioMedicamento.getValorInicial());
                    nuevoManualTarifarioMedicamento.setValorFinal(manualTarifarioMedicamento.getValorFinal());
                    manualTarifarioMedicamentoFacade.create(nuevoManualTarifarioMedicamento);
                }

                for (FacManualTarifarioPaquete manualTarifarioPaquete : contrato.getIdManualTarifario().getFacManualTarifarioPaqueteList()) {
                    nuevoManualTarifarioPaquete = new FacManualTarifarioPaquete();
                    FacManualTarifarioPaquetePK llavePK = new FacManualTarifarioPaquetePK();
                    llavePK.setIdManualTarifario(manualAux.getIdManualTarifario());
                    llavePK.setIdPaquete(manualTarifarioPaquete.getFacManualTarifarioPaquetePK().getIdPaquete());
                    nuevoManualTarifarioPaquete.setFacManualTarifarioPaquetePK(llavePK);
                    nuevoManualTarifarioPaquete.setActivo(manualTarifarioPaquete.getActivo());
                    nuevoManualTarifarioPaquete.setDescuento(manualTarifarioPaquete.getDescuento());
                    nuevoManualTarifarioPaquete.setObservacion(manualTarifarioPaquete.getObservacion());
                    nuevoManualTarifarioPaquete.setValorInicial(manualTarifarioPaquete.getValorInicial());
                    nuevoManualTarifarioPaquete.setValorFinal(manualTarifarioPaquete.getValorFinal());                    
                    manualTarifarioPaqueteFacade.create(nuevoManualTarifarioPaquete);
                }

                for (FacManualTarifarioServicio manualTarifarioServicio : contrato.getIdManualTarifario().getFacManualTarifarioServicioList()) {
                    nuevoManualTarifarioServicio = new FacManualTarifarioServicio();
                    FacManualTarifarioServicioPK llavePK = new FacManualTarifarioServicioPK();
                    llavePK.setIdManualTarifario(manualAux.getIdManualTarifario());
                    llavePK.setIdServicio(manualTarifarioServicio.getFacManualTarifarioServicioPK().getIdServicio());
                    nuevoManualTarifarioServicio.setFacManualTarifarioServicioPK(llavePK);
                    nuevoManualTarifarioServicio.setActivo(manualTarifarioServicio.getActivo());
                    nuevoManualTarifarioServicio.setDescuento(manualTarifarioServicio.getDescuento());
                    nuevoManualTarifarioServicio.setObservacion(manualTarifarioServicio.getObservacion());
                    nuevoManualTarifarioServicio.setValorInicial(manualTarifarioServicio.getValorInicial());
                    nuevoManualTarifarioServicio.setValorFinal(manualTarifarioServicio.getValorFinal());
                    nuevoManualTarifarioServicio.setMetaCumplimiento(manualTarifarioServicio.getMetaCumplimiento());
                    nuevoManualTarifarioServicio.setPeriodicidad(manualTarifarioServicio.getPeriodicidad());
                    nuevoManualTarifarioServicio.setHonorarioMedico(manualTarifarioServicio.getHonorarioMedico());
                    nuevoManualTarifarioServicio.setTipoTarifa(manualTarifarioServicio.getTipoTarifa());
                    nuevoManualTarifarioServicio.setAnioUnidadValor(manualTarifarioServicio.getAnioUnidadValor());
                    manualTarifarioServicioFacade.create(nuevoManualTarifarioServicio);
                }
            }
            contratoAux = new FacContrato();
            contratoAux.setIdManualTarifario(manualAux);
            contratoAux.setAplicarCree(contrato.getAplicarCree());
            contratoAux.setAplicarIva(contrato.getAplicarIva());
            contratoAux.setC1(contrato.getC1());
            contratoAux.setCm1(contrato.getCm1());
            contratoAux.setCm2(contrato.getCm2());
            contratoAux.setCm3(contrato.getCm3());
            contratoAux.setCm4(contrato.getCm4());
            contratoAux.setCm5(contrato.getCm5());
            contratoAux.setCmb(contrato.getCmb());
            contratoAux.setCmc(contrato.getCmc());
            contratoAux.setCodigoConcepto(contrato.getCodigoConcepto());
            contratoAux.setCodigoConceptoDescuento(contrato.getCodigoConceptoDescuento());
            contratoAux.setCodigoContrato(prefijo + contrato.getCodigoContrato());
            contratoAux.setCodigoTarifaNopos(contrato.getCodigoTarifaNopos());
            contratoAux.setCodigoTarifaPos(contrato.getCodigoTarifaPos());
            contratoAux.setCp1(contrato.getCp1());
            contratoAux.setCp2(contrato.getCp2());
            contratoAux.setCp3(contrato.getCp3());
            contratoAux.setCp4(contrato.getCp4());
            contratoAux.setCp5(contrato.getCp5());
            contratoAux.setCpb(contrato.getCpb());
            contratoAux.setCpc(contrato.getCpc());
            contratoAux.setCuentaCobrar(contrato.getCuentaCobrar());
            contratoAux.setCuentaCopago(contrato.getCuentaCopago());
            contratoAux.setDescripcion(prefijo + contrato.getDescripcion());
            contratoAux.setFechaFinal(contrato.getFechaFinal());
            contratoAux.setFechaInicio(contrato.getFechaInicio());
            contratoAux.setIdAdministradora(administradoraSeleccionada);
            contratoAux.setInsumosPorcentaje1(contrato.getInsumosPorcentaje1());
            contratoAux.setInsumosPorcentaje2(contrato.getInsumosPorcentaje2());
            contratoAux.setInsumosPorcentaje3(contrato.getInsumosPorcentaje3());
            contratoAux.setMedicamentoValor1(contrato.getMedicamentoValor1());
            contratoAux.setMedicamentoValor2(contrato.getMedicamentoValor2());
            contratoAux.setMedicamentoValor3(contrato.getMedicamentoValor3());
            contratoAux.setNumeroAfiliados(contrato.getNumeroAfiliados());
            contratoAux.setNumeroPoliza(contrato.getNumeroPoliza());
            contratoAux.setNumeroRipContrato(contrato.getNumeroRipContrato());
            contratoAux.setObservacionContrato(contrato.getObservacionContrato());
            contratoAux.setObservacionFacturacion(contrato.getObservacionFacturacion());
            contratoAux.setPorcentajeDescuentoServicios(contrato.getPorcentajeDescuentoServicios());
            contratoAux.setPorcentejeDescuentoEntidad(contrato.getPorcentejeDescuentoEntidad());
            contratoAux.setRip(contrato.getRip());
            contratoAux.setTipoContrato(contrato.getTipoContrato());
            contratoAux.setTipoFacturacion(contrato.getTipoFacturacion());
            contratoAux.setTipoPago(contrato.getTipoPago());
            contratoAux.setUpc(contrato.getUpc());
            contratoAux.setValorAlarma(contrato.getValorAlarma());
            contratoAux.setValorContrato(contrato.getValorContrato());
            contratoAux.setValorMensual(contrato.getValorMensual());
            contratoAux.setValorValidacionMensual(contrato.getValorValidacionMensual());
            contratoAux.setVigencia(contrato.getVigencia());
            contratoFacade.create(contratoAux);
        }
        imprimirMensaje("Correcto", "La copia de contratos y manuales tarifarios se ha realizado", FacesMessage.SEVERITY_ERROR);
        RequestContext.getCurrentInstance().update("IdFormAdministradoras");
        RequestContext.getCurrentInstance().update("IdMensajeAdministradoras");
        RequestContext.getCurrentInstance().execute("PF('dialogoCopiarContratos').hide()");
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
            aplicacionGeneralMB.cargarClasificacion(ClasificacionesEnum.Administradoras);
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
        aplicacionGeneralMB.cargarClasificacion(ClasificacionesEnum.Administradoras);
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
        listaContratosCopia = new ArrayList<>();
        if (validarNoVacio(idAdministradoraACopiar)) {
            administradoraCopiaSeleccionada = administradoraFacade.find(Integer.parseInt(idAdministradoraACopiar));
            if (administradoraCopiaSeleccionada != null) {
                listaContratosCopia = administradoraCopiaSeleccionada.getFacContratoList();
            }
        }
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

    public String getPrefijo() {
        return prefijo;
}

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

}
