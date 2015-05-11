/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.Citas;

import beans.utilidades.LazyAutorizacionesDataModel;
import beans.utilidades.LazyPacienteDataModel;
import modelo.entidades.CitAutorizaciones;
import modelo.entidades.CfgPacientes;
import modelo.fachadas.CitAutorizacionesFacade;
import modelo.fachadas.CfgPacientesFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import beans.utilidades.MetodosGenerales;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CitAutorizacionesServicios;
import modelo.entidades.CitAutorizacionesServiciosPK;
import modelo.entidades.CitCitas;
import modelo.entidades.FacServicio;
import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.CitAutorizacionesServiciosFacade;
import modelo.fachadas.CitCitasFacade;
import modelo.fachadas.FacManualTarifarioServicioFacade;
import modelo.fachadas.FacServicioFacade;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author mario
 */
@ManagedBean(name = "autorizacionMB")
@SessionScoped
public class AutorizacionMB extends MetodosGenerales implements Serializable {

    private String numAutorizacion;
    private int idPaciente;
    private String numDocumento;
    private String codigoPrograma;
    private CfgPacientes paciente;
    private int sesionesAutorizadas = 1;
    private Date fechaAutorizacion;
    private int idServicio;
    private int idAdministradora;
    private boolean cerrada = false;
    private boolean deshabilitarComboBox;
    private boolean renderizar = false;
    private boolean renderizarBtnAdicionarServicio = false;
    private boolean renderizarBtnRemoverServicios = false;
    private boolean crearAutorizacionRecepcion = false;//si la autorizacion ha crear viene desde la recepcion. se aplicara a la cita una vez creada
    private String opcion;
    private boolean autorizacionvalidada = false;
    private CitAutorizaciones nuevaAutorizacion;
    private CitAutorizaciones autorizacionSeleccionada;
    private LazyDataModel<CfgPacientes> listaPacientes;
    private LazyDataModel<CitAutorizaciones> listaAutorizaciones;
    private List<CitAutorizacionesServicios> listaAutorizacionesServicios;
    private List<SelectItem> listaProgramas = null;
    private List<SelectItem> listaServicios;
    //private List<SelectItem> listaTipoCitas = null;

    private CitCitas citaSeleccionada;//tiene valor, cuando la autorizacion se crea desde recepcion de citas

    @EJB
    CitAutorizacionesFacade autorizacionesFacade;

    @EJB
    CfgPacientesFacade pacientesFachada;

    @EJB
    FacServicioFacade facServicioFacade;

    @EJB
    CitAutorizacionesServiciosFacade autorizacionesServiciosFacade;

    @EJB
    FacManualTarifarioServicioFacade facManualTarifarioServicioFacade;

    @EJB
    CfgUsuariosFacade usuariosFachada;

    @EJB
    CitCitasFacade citasFacade;

    /**
     * Creates a new instance of AutorizacionMB
     */
    public AutorizacionMB() {
    }

    //-----------------------------------------------------------------
    //---------------------METHODS-------------------------------------
    //-----------------------------------------------------------------
    @PostConstruct
    public void init() {
        //comentar cuando la listaServicios se calcule con loadServicios
        opcion = "default";
        setListaPacientes(new LazyPacienteDataModel(pacientesFachada));
        setListaAutorizaciones(new LazyAutorizacionesDataModel(autorizacionesFacade, 0));
        fillServicios();
        listaAutorizacionesServicios = new ArrayList();
    }

    private void fillServicios() {
        List<FacServicio> lista = facServicioFacade.findAutorizacionReqAndVisible();
        listaServicios = new ArrayList();
        for (FacServicio servicio : lista) {
            listaServicios.add(new SelectItem(servicio.getIdServicio(), servicio.getNombreServicio()));

        }
        setListaServicios(listaServicios);

    }

    public void loadProgramas() {
        //si el contrato al que pertenece el paciente necesita autorizacion para crear una cita sera true o viceversa
    /*    
         if (paciente != null) {
         crearItems(pacienteFacProgramaFacade.findProgramaByPaciente(paciente.getId()));
         } else {
         listaProgramas = null;
         }
         */
    }

    /*    
     private void crearItems(List<FacRelPacientesProgramas> lista) {
     listaProgramas = new ArrayList<>();
     if (!lista.isEmpty()) {
     for (FacRelPacientesProgramas facPrograma : lista) {
     listaProgramas.add(new SelectItem(facPrograma.getPrograma().getCodigoPrograma(), facPrograma.getPrograma().getNombrePrograma()));
     }
     }
     }
     */
    public void loadServicios() {
        //crearItemsServicios(facProgramaServicioFacade.findByPrograma(codigoPrograma));
    }

    /*
     private void crearItemsServicios(List<FacProgramaServicio> lista) {
     listaTipoCitas = new ArrayList<>();
     if (!lista.isEmpty()) {
     for (FacProgramaServicio facProgramaServicio : lista) {
     getListaTipoCitas().add(new SelectItem(facProgramaServicio.getCodigoServicio().getCodigoServicio(), facProgramaServicio.getCodigoServicio().getNombreServicio()));
     }
     } else {
     setListaTipoCitas(null);
     }
     }
     */
    public boolean validarAutorizacion() {
        CitAutorizaciones autorizacion = autorizacionesFacade.findAutorizacion(idPaciente, idServicio, idAdministradora);
        setAutorizacionSeleccionada(autorizacion);
        if (autorizacion == null) {
            setRenderizar(false);
            setAutorizacionvalidada(false);
            //imprimirMensaje("Información", "No se encontro aquella autorizacion", FacesMessage.SEVERITY_ERROR);
            return false;
        } else {
            setAutorizacionvalidada(true);
            setRenderizar(true);
            return true;
        }
    }

    //carga unicamente los servicios que requieren autorizacion 
//    public void loadServicios(CfgPacientes paciente) {
//        if (paciente != null) {
//            listaServicios = new ArrayList();
//            List<FacContrato> listacontratos = paciente.getIdAdministradora().getFacContratoList();
//            if (listacontratos != null) {
//
//                List<Integer> contratos = new ArrayList();
//                for (FacContrato contrato : listacontratos) {
//                    contratos.add(contrato.getIdContrato());
//                }
//                //la lista se llena con lo programas a los que un paciente aplica - se tiene en cuenta la administradora y sus contratos
//                List<FacPrograma> listaprogramas = facProgramaFacade.findProgramasByContratoAndPaciente(contratos, paciente);
//                if (listaprogramas != null && listaprogramas.size() > 0) {
//                    List<Integer> programas = new ArrayList();
//                    for (FacPrograma programa : listaprogramas) {
//                        programas.add(programa.getIdManualTarifario().getIdManualTarifario());
//                    }
//                    List<FacManualTarifarioServicio> listamanualtarifario = facManualTarifarioServicioFacade.findManualTarifarioServicioAutorizacionRequiredByProgramas(programas);
//                    for (FacManualTarifarioServicio manualTarifarioServicio : listamanualtarifario) {
//                        FacServicio servicio = manualTarifarioServicio.getFacServicio();
//                        listaServicios.add(new SelectItem(servicio.getIdServicio(), servicio.getNombreServicio()));
//                    }
//                } else {
//                    listaServicios = new ArrayList();
//                }
//            } else {
//                setListaServicios(null);
//            }
//        }
//    }
    public void buscarPaciente() {
        //setListaServicios(null);
        limpiarVaribles(1);
        paciente = pacientesFachada.buscarPorIdentificacion(numDocumento);
        RequestContext.getCurrentInstance().update("autorizar");
        if (paciente == null) {
            opcion = "default";
            setListaAutorizaciones(new LazyAutorizacionesDataModel(autorizacionesFacade, 0));
            RequestContext.getCurrentInstance().update("formListaAutoriaciones");
            if (!numDocumento.isEmpty()) {
                imprimirMensaje("Información", "No se encuentra registrado aquel paciente", FacesMessage.SEVERITY_ERROR);
            }

        } else {
            if (paciente.getIdAdministradora() != null) {
                setListaAutorizaciones(new LazyAutorizacionesDataModel(autorizacionesFacade, paciente.getIdPaciente()));
                RequestContext.getCurrentInstance().update("formListaAutoriaciones");
                if (paciente.getIdAdministradora().getCodigoAdministradora().equals("1")) {
                    imprimirMensaje("Información", "El paciente esta registrado como particular, no necesita autorizaciones", FacesMessage.SEVERITY_ERROR);
                }
            } else {
                imprimirMensaje("Información", "El paciente no esta registrado como particular y no tiene una administradora asociada", FacesMessage.SEVERITY_ERROR);
            }
        }

        //descomentar cuando los servicios son exclusivos para el paciente
        //loadServicios(paciente);
    }

    public void cargarDesdeTab(String params) {
        String[] parametros = params.split(";");
        if (parametros[0].equals("idCita")) {
            citaSeleccionada = citasFacade.find(Integer.parseInt(parametros[1]));
            paciente = citaSeleccionada.getIdPaciente();
            crearAutorizacionRecepcion = true;
        } else {
            paciente = pacientesFachada.find(Integer.parseInt(parametros[1]));
            crearAutorizacionRecepcion = false;
        }
        setIdServicio(Integer.parseInt(parametros[3]));
        setNumDocumento(paciente.getIdentificacion());
    }

    public void actualizarPaciente() {
        if (paciente != null) {
            setNumDocumento(paciente.getIdentificacion());
            setListaAutorizaciones(new LazyAutorizacionesDataModel(autorizacionesFacade, paciente.getIdPaciente()));
            if(paciente.getIdAdministradora().getCodigoAdministradora().equals("1")){
                imprimirMensaje("Error", "El paciente es particular, no necesita autorizacion", FacesMessage.SEVERITY_ERROR);
            }
            RequestContext.getCurrentInstance().update("formListaAutoriaciones");
        }
        listaAutorizacionesServicios.clear();
        setIdServicio(0);
        setSesionesAutorizadas(Short.parseShort("1"));
        setNumAutorizacion(null);
        setAutorizacionSeleccionada(null);

    }

    private void limpiarVaribles(int ban) {
        if (ban == 1) {
            paciente = null;
            //setNumDocumento(null);
            listaAutorizacionesServicios.clear();
            crearAutorizacionRecepcion = false;
            citaSeleccionada = null;
            setNumAutorizacion(null);
            setAutorizacionSeleccionada(null);
            setRenderizarBtnAdicionarServicio(false);
            setRenderizarBtnRemoverServicios(false);
            setFechaAutorizacion(null);
        }
        setIdServicio(0);
        setListaProgramas(null);
        //descomentar cuando los servicios son exclusivos para el paciente
        //setListaServicios(null);
        setSesionesAutorizadas(1);

    }

    public void adicionarAutorizacionAlaLista() {
        if (paciente == null) {
            imprimirMensaje("Información", "Ingrese un documento valido del paciente", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (paciente.getIdAdministradora() != null) {
            if (paciente.getIdAdministradora().getCodigoAdministradora().equals("1")) {
                imprimirMensaje("Error", "El paciente esta registrado como particular, no necesita autorizaciones", FacesMessage.SEVERITY_ERROR);
                return;
            }
        } else {
            imprimirMensaje("Error", "El paciente no esta registrado como particular y no tiene una administradora asociada", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (idServicio == 0) {
            imprimirMensaje("Error", "No ha seleccionado un servicio", FacesMessage.SEVERITY_ERROR);
            return;

        }
        if (sesionesAutorizadas == 0) {
            imprimirMensaje("Error", "Ingrese el numero de sesiones autorizadas", FacesMessage.SEVERITY_ERROR);
            return;

        }
        if (numAutorizacion.isEmpty()) {
            imprimirMensaje("Error", "Ingrese el numero de autorizacion", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (fechaAutorizacion == null) {
            imprimirMensaje("Error", "Ingrese la fecha de autorizacion", FacesMessage.SEVERITY_ERROR);
            return;
        }
//        OJO 19 MARZO 2015
        if (autorizacionesFacade.findAutorizacionDisponible(paciente.getIdPaciente(), idServicio, paciente.getIdAdministradora().getIdAdministradora()) != null) {
            imprimirMensaje("Error", "El paciente ya cuenta con una autorizacion vigente para este servicio", FacesMessage.SEVERITY_ERROR);
            return;
        }
        boolean ban = false;
//        valida que los servicios de la autorizacion no esten repetidos
        for (CitAutorizacionesServicios autorizacionServicio : listaAutorizacionesServicios) {
            if (autorizacionServicio.getFacServicio().getIdServicio() == idServicio) {
                ban = true;
                break;
            }
        }
        if (ban) {
            imprimirMensaje("Error", "Ya se ha insertado una autorizacion para este servicio", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (nuevaAutorizacion == null) {
            nuevaAutorizacion = new CitAutorizaciones();
            nuevaAutorizacion.setNumAutorizacion(numAutorizacion);
            nuevaAutorizacion.setPaciente(paciente);
            nuevaAutorizacion.setAdministradora(paciente.getIdAdministradora());
            nuevaAutorizacion.setCerrada(false);
            nuevaAutorizacion.setFechaAutorizacion(fechaAutorizacion);
            nuevaAutorizacion.setFechaSistema(new Date());
            LoginMB loginMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
            nuevaAutorizacion.setIdUsuarioCreador(loginMB.getUsuarioActual());
        }
        //ojo
        CitAutorizacionesServicios autorizacionServicio = new CitAutorizacionesServicios();
        autorizacionServicio.setCitAutorizaciones(nuevaAutorizacion);
        autorizacionServicio.setFacServicio(facServicioFacade.find(idServicio));
        autorizacionServicio.setSesionesAutorizadas(sesionesAutorizadas);
        autorizacionServicio.setSesionesRealizadas(0);
        autorizacionServicio.setSesionesPendientes(sesionesAutorizadas);
        listaAutorizacionesServicios.add(autorizacionServicio);
        RequestContext.getCurrentInstance().update("autorizar:idTable");
    }

    public void borrarListaAutorizaciones() {
        listaAutorizacionesServicios.clear();
        RequestContext.getCurrentInstance().update("autorizar:idTable");
    }

    public void controladorAccion() {
        switch (opcion) {
            case "crear":
                crearAutorizacion();
                break;
            case "actualizar":
                actualizarAutorizacion();
                break;
            default:
                imprimirMensaje("Error", "El formulario no esta diligenciado completamente", FacesMessage.SEVERITY_ERROR);
        }
    }

    private void crearAutorizacion() {
        if (paciente == null) {
            imprimirMensaje("Error", "No ha seleccionado el paciente", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (listaAutorizacionesServicios.isEmpty()) {
            imprimirMensaje("Error", "No se ha incluido servicios a la autorizacion", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (nuevaAutorizacion != null) {
            if (listaAutorizacionesServicios.size() > 0) {
                autorizacionesFacade.create(nuevaAutorizacion);
                for (CitAutorizacionesServicios autorizacionServicio : listaAutorizacionesServicios) {
                    //si la creacion de la autorizacion viene desde recepcion de citas y el servicio de la cita corresponde a algun servicio autorizado se aplicara a la cita y se modifica la parte de sesiones autorizadas
                    if (crearAutorizacionRecepcion && autorizacionServicio.getFacServicio().equals(citaSeleccionada.getIdServicio())) {
                        autorizacionServicio.setSesionesPendientes(autorizacionServicio.getSesionesAutorizadas() - 1);
                        citaSeleccionada.setIdAutorizacion(nuevaAutorizacion);
                        citasFacade.edit(citaSeleccionada);
                    }
                    autorizacionServicio.setCitAutorizacionesServiciosPK(new CitAutorizacionesServiciosPK(nuevaAutorizacion.getIdAutorizacion(), autorizacionServicio.getFacServicio().getIdServicio()));
                    autorizacionesServiciosFacade.create(autorizacionServicio);
                }
                setListaAutorizaciones(new LazyAutorizacionesDataModel(autorizacionesFacade, 0));
                RequestContext.getCurrentInstance().update("formListaAutoriaciones");
                imprimirMensaje("Correcto", "Autorizacion creada", FacesMessage.SEVERITY_INFO);
                numDocumento = null;
                limpiarVaribles(1);
                nuevaAutorizacion = null;
            } else {
                imprimirMensaje("Informacion", "No hay servicios autorizados", FacesMessage.SEVERITY_WARN);
            }
        } else {
            imprimirMensaje("Informacion", "Faltan datos para crear la autorizacion", FacesMessage.SEVERITY_WARN);
        }
    }

    private void actualizarAutorizacion() {
        if (autorizacionSeleccionada != null) {
            autorizacionSeleccionada.setCerrada(cerrada);
            autorizacionesFacade.edit(autorizacionSeleccionada);
            setListaAutorizaciones(new LazyAutorizacionesDataModel(autorizacionesFacade, 0));
            RequestContext.getCurrentInstance().update("formListaAutoriaciones");
            imprimirMensaje("Correcto", "Autorizacion cerrada", FacesMessage.SEVERITY_INFO);
            setDeshabilitarComboBox(true);
            setRenderizarBtnAdicionarServicio(false);
            setRenderizarBtnRemoverServicios(false);
            setNumAutorizacion(null);
            setCerrada(false);
            setAutorizacionSeleccionada(null);
            setPaciente(null);
            setNumDocumento(null);
            listaAutorizacionesServicios.clear();
            RequestContext.getCurrentInstance().update("autorizar");
        }
    }

    public void buscarAutorizacion() {
        listaAutorizacionesServicios.clear();
        if (paciente != null && !numAutorizacion.isEmpty()) {
//            imprimirMensaje("Informacion", "Buscando autorizacion", FacesMessage.SEVERITY_INFO);
            CitAutorizaciones autorizacion = autorizacionesFacade.findAutorizacionNoCerrada(paciente, numAutorizacion);
            if (autorizacion != null) {
                setDeshabilitarComboBox(false);
                setRenderizarBtnAdicionarServicio(false);
                setRenderizarBtnRemoverServicios(false);
                setAutorizacionSeleccionada(autorizacion);
                setListaAutorizacionesServicios(autorizacion.getCitAutorizacionesServiciosList());
                opcion = "actualizar";
//                imprimirMensaje("Correcto", "Autorizacion encontrada", FacesMessage.SEVERITY_INFO);
            } else {
                setDeshabilitarComboBox(true);
                setRenderizarBtnAdicionarServicio(true);
                setRenderizarBtnRemoverServicios(true);
                setAutorizacionSeleccionada(null);
                opcion = "crear";
//                imprimirMensaje("Error", "Autorizacion no encontrada", FacesMessage.SEVERITY_ERROR);
            }

        } else {
            setDeshabilitarComboBox(true);
            setRenderizarBtnAdicionarServicio(false);
            setRenderizarBtnRemoverServicios(false);
            setAutorizacionSeleccionada(null);
            listaAutorizacionesServicios.clear();
        }
        RequestContext.getCurrentInstance().update("autorizar");
    }

    public void cargarAutorizacion() {
        paciente = autorizacionSeleccionada.getPaciente();
        setNumDocumento(paciente.getIdentificacion());
        setNumAutorizacion(autorizacionSeleccionada.getNumAutorizacion());
        setListaAutorizaciones(new LazyAutorizacionesDataModel(autorizacionesFacade, paciente.getIdPaciente()));
        setDeshabilitarComboBox(false);
        setRenderizarBtnAdicionarServicio(false);
        setRenderizarBtnRemoverServicios(false);
        setAutorizacionSeleccionada(autorizacionSeleccionada);
        setListaAutorizacionesServicios(autorizacionSeleccionada.getCitAutorizacionesServiciosList());
        opcion = "actualizar";
        RequestContext.getCurrentInstance().update("autorizar");
        RequestContext.getCurrentInstance().update("formListaAutoriaciones");
    }
    //-----------------------------------------------------------------------
    //--------------------GETTERS AND SETTERS--------------------------------
    //-----------------------------------------------------------------------

    /**
     * @return the numAutorizacion
     */
    public String getNumAutorizacion() {
        return numAutorizacion;
    }

    /**
     * @param numAutorizacion the numAutorizacion to set
     */
    public void setNumAutorizacion(String numAutorizacion) {
        this.numAutorizacion = numAutorizacion;
    }

    /**
     * @return the sesionesAutorizadas
     */
    public int getSesionesAutorizadas() {
        return sesionesAutorizadas;
    }

    /**
     * @param sesionesAutorizadas the sesionesAutorizadas to set
     */
    public void setSesionesAutorizadas(int sesionesAutorizadas) {
        this.sesionesAutorizadas = sesionesAutorizadas;
    }

    /**
     * @return the autorizacionSeleccionada
     */
    public CitAutorizaciones getAutorizacionSeleccionada() {
        return autorizacionSeleccionada;
    }

    /**
     * @param autorizacionSeleccionada the autorizacionSeleccionada to set
     */
    public void setAutorizacionSeleccionada(CitAutorizaciones autorizacionSeleccionada) {
        this.autorizacionSeleccionada = autorizacionSeleccionada;
    }

    /**
     * @return the renderizar
     */
    public boolean isRenderizar() {
        return renderizar;
    }

    /**
     * @param renderizar the renderizar to set
     */
    public void setRenderizar(boolean renderizar) {
        this.renderizar = renderizar;
    }

    /**
     * @return the idPaciente
     */
    public int getIdPaciente() {
        return idPaciente;
    }

    /**
     * @param idPaciente the idPaciente to set
     */
    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    /**
     * @return the numDocumento
     */
    public String getNumDocumento() {
        return numDocumento;
    }

    /**
     * @param numDocumento the numDocumento to set
     */
    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    /**
     * @return the paciente
     */
    public CfgPacientes getPaciente() {
        return paciente;
    }

    /**
     * @param paciente the paciente to set
     */
    public void setPaciente(CfgPacientes paciente) {
        this.paciente = paciente;
    }

    /**
     * @return the listaProgramas
     */
    public List<SelectItem> getListaProgramas() {
        return listaProgramas;
    }

    /**
     * @param listaProgramas the listaProgramas to set
     */
    public void setListaProgramas(List<SelectItem> listaProgramas) {
        this.listaProgramas = listaProgramas;
    }

    /**
     * @return the codigoPrograma
     */
    public String getCodigoPrograma() {
        return codigoPrograma;
    }

    /**
     * @param codigoPrograma the codigoPrograma to set
     */
    public void setCodigoPrograma(String codigoPrograma) {
        this.codigoPrograma = codigoPrograma;
    }

    /**
     * @return the autorizacionvalidada
     */
    public boolean isAutorizacionvalidada() {
        return autorizacionvalidada;
    }

    /**
     * @param autorizacionvalidada the autorizacionvalidada to set
     */
    public void setAutorizacionvalidada(boolean autorizacionvalidada) {
        this.autorizacionvalidada = autorizacionvalidada;
    }

    /**
     * @return the idServicio
     */
    public int getIdServicio() {
        return idServicio;
    }

    /**
     * @param idServicio the idServicio to set
     */
    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    /**
     * @return the idAdministradora
     */
    public int getIdAdministradora() {
        return idAdministradora;
    }

    /**
     * @param idAdministradora the idAdministradora to set
     */
    public void setIdAdministradora(int idAdministradora) {
        this.idAdministradora = idAdministradora;
    }

    /**
     * @return the listaServicios
     */
    public List<SelectItem> getListaServicios() {
        return listaServicios;
    }

    /**
     * @param listaServicios the listaServicios to set
     */
    public void setListaServicios(List<SelectItem> listaServicios) {
        this.listaServicios = listaServicios;
    }

    public LazyDataModel<CfgPacientes> getListaPacientes() {
        return listaPacientes;
    }

    public void setListaPacientes(LazyDataModel<CfgPacientes> listaPacientes) {
        this.listaPacientes = listaPacientes;
    }

    public List<CitAutorizacionesServicios> getListaAutorizacionesServicios() {
        return listaAutorizacionesServicios;
    }

    public void setListaAutorizacionesServicios(List<CitAutorizacionesServicios> listaAutorizacionesServicios) {
        this.listaAutorizacionesServicios = listaAutorizacionesServicios;
    }

    public boolean isCerrada() {
        return cerrada;
    }

    public void setCerrada(boolean cerrada) {
        this.cerrada = cerrada;
    }

    public boolean isDeshabilitarComboBox() {
        return deshabilitarComboBox;
    }

    public void setDeshabilitarComboBox(boolean deshabilitarComboBox) {
        this.deshabilitarComboBox = deshabilitarComboBox;
    }

    public boolean isRenderizarBtnAdicionarServicio() {
        return renderizarBtnAdicionarServicio;
    }

    public void setRenderizarBtnAdicionarServicio(boolean renderizarBtnAdicionarServicio) {
        this.renderizarBtnAdicionarServicio = renderizarBtnAdicionarServicio;
    }

    public boolean isRenderizarBtnRemoverServicios() {
        return renderizarBtnRemoverServicios;
    }

    public void setRenderizarBtnRemoverServicios(boolean renderizarBtnRemoverServicios) {
        this.renderizarBtnRemoverServicios = renderizarBtnRemoverServicios;
    }

    public Date getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(Date fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public LazyDataModel<CitAutorizaciones> getListaAutorizaciones() {
        return listaAutorizaciones;
    }

    public void setListaAutorizaciones(LazyDataModel<CitAutorizaciones> listaAutorizaciones) {
        this.listaAutorizaciones = listaAutorizaciones;
    }

}
