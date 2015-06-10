/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.Citas;

import beans.utilidades.AutorizacionReport;
import modelo.entidades.CitCitas;
import modelo.fachadas.CitCitasFacade;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import beans.utilidades.CitaU;
import beans.utilidades.LazyPacienteDataModel;
import beans.utilidades.LazyPrestadorDataModel;
import beans.utilidades.MetodosGenerales;
import beans.utilidades.Oportunidad;
import java.text.SimpleDateFormat;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgEmpresa;
import modelo.entidades.CfgPacientes;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.CitAutorizaciones;
import modelo.entidades.CitAutorizacionesServicios;
import modelo.entidades.FacAdministradora;
import modelo.fachadas.CfgEmpresaFacade;
import modelo.fachadas.CfgPacientesFacade;
import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.CitAutorizacionesFacade;
import modelo.fachadas.CitAutorizacionesServiciosFacade;
import modelo.fachadas.FacServicioFacade;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Mario
 */
@ManagedBean(name = "reporteCitasMB")
@ViewScoped
public class ReporteCitasMB extends MetodosGenerales implements Serializable {

    /**
     * Creates a new instance of RecordatorioMB
     */
    private String tipoReporte;
    private String identificacionPaciente;
    private Date fechaInicial;
    private Date fechaFinal;
    private CfgPacientes pacienteSeleccionado;
    private CfgPacientes pacienteHC;
    private CfgUsuarios prestadorSeleccionado;
    private FacAdministradora administradoraSeleccionada;
    private List<CfgUsuarios> listaPrestadores;//para filtrar
    private List<CfgPacientes> listaPacientes;//para filtrar
    private List<FacAdministradora> listaAdministradoras;
    private List<SelectItem> listaReportes;
    private List<SelectItem> listaEstadoAutorizacion;
    private LazyDataModel<CfgUsuarios> prestadores;
    private LazyDataModel<CfgPacientes> pacientes;
    private boolean renBtnFiltrar;//renderiza el boton de filtrado
    private boolean renBtnReporte;//renderiza el boton de generear reporte
    private String displayBusquedaPaciente = "none";
    private LazyDataModel<CfgPacientes> listaPacientesBusqueda;
    private int estadoAutorizacion;// 0 -> todas, 1->cerradas, 2->no cerradas
    private String numAutorizacion;

    private int sede;

    @EJB
    FacServicioFacade servicioFacade;

    @EJB
    CfgPacientesFacade pacientesFachada;

    @EJB
    CfgUsuariosFacade usuariosFachada;

    @EJB
    CitAutorizacionesServiciosFacade autorizacionesServiciosFacade;

    @EJB
    CitAutorizacionesFacade autorizacionesFacade;

    public ReporteCitasMB() {
    }

    @PostConstruct
    public void init() {
        listaAdministradoras = new ArrayList();
        listaPacientes = new ArrayList();
        listaPrestadores = new ArrayList();
        setListaPacientesBusqueda(new LazyPacienteDataModel(pacientesFachada));
        listaReportes = new ArrayList();
        listaEstadoAutorizacion = new ArrayList();
        listaEstadoAutorizacion.add(new SelectItem(0, "Todas"));
        listaEstadoAutorizacion.add(new SelectItem(1, "Cerradas"));
        listaEstadoAutorizacion.add(new SelectItem(2, "Abiertas"));
        setRenBtnFiltrar(true);
        setRenBtnReporte(true);
        crearMenuReportes();
        setPacientes(new LazyPacienteDataModel(pacientesFachada));
        setPrestadores(new LazyPrestadorDataModel(usuariosFachada));
        LoginMB loginMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
        sede = loginMB.getCentroDeAtencionactual().getIdSede();
    }

    @EJB
    CitCitasFacade citasFacade;

    @EJB
    CfgEmpresaFacade cfgEmpresaFacade;

    //--------------------------------------------------------------------------------
    //--------------------------------METHODS-----------------------------------------
    //--------------------------------------------------------------------------------
    private void crearMenuReportes() {

        listaReportes.add(new SelectItem(1, "Citas Asignadas"));
        listaReportes.add(new SelectItem(2, "Citas - Admnistradora"));
        listaReportes.add(new SelectItem(3, "Citas Canceladas"));
        listaReportes.add(new SelectItem(4, "Oportunidad"));
        listaReportes.add(new SelectItem(5, "Historial Citas"));
        listaReportes.add(new SelectItem(6, "Autorizaciones"));
    }

    public void habilitarFuncionalidadReporte() {
        setIdentificacionPaciente(null);
        setPacienteHC(null);
        if (tipoReporte.equals("6")) {
            setRenBtnFiltrar(false);
            setRenBtnReporte(false);
            setDisplayBusquedaPaciente("none");
            RequestContext.getCurrentInstance().execute("PF('dlgfiltrarAutorizaciones').show()");

        } else {
            if (tipoReporte.equals("4") || tipoReporte.equals("5")) {
                setRenBtnFiltrar(false);

                if (tipoReporte.equals("5")) {
                    setRenBtnReporte(false);
                    setDisplayBusquedaPaciente("block");
                } else {
                    setRenBtnReporte(true);
                    setDisplayBusquedaPaciente("none");
                }
            } else {
                setRenBtnFiltrar(true);
                setRenBtnReporte(true);
                setDisplayBusquedaPaciente("none");
            }
        }
    }

    public void findPaciente() {
        if (!identificacionPaciente.isEmpty()) {
            pacienteHC = pacientesFachada.buscarPorIdentificacion(getIdentificacionPaciente());
            if (pacienteHC == null) {
                setRenBtnReporte(false);
                imprimirMensaje("Error", "No se encontro el paciente", FacesMessage.SEVERITY_ERROR);
            } else {
                setRenBtnReporte(true);
            }
        } else {
            setPacienteSeleccionado(null);
            setRenBtnReporte(false);
        }
    }

    public void actualizarPaciente() {
        if (pacienteHC != null) {
            setIdentificacionPaciente(pacienteHC.getIdentificacion());
            setRenBtnReporte(true);
        }
    }

    private List<Oportunidad> generarListaOportunidad(List<Object[]> rows) {
        List<Oportunidad> listaoportunidad = new ArrayList();
        for (Object[] row : rows) {
            String servicio = (String) row[0];
            long totalcitas = row[1] == null ? 0 : (long) row[1];
            long totaldias = row[2] == null ? 0 : (long) row[2];
            listaoportunidad.add(new Oportunidad(servicio, totalcitas, totaldias));
        }
        return listaoportunidad;
    }

    private List<AutorizacionReport> generarListaAutorizaciones(List<CitAutorizaciones> autorizaciones) {
        List<AutorizacionReport> listaAutorizacionReport = new ArrayList();
        if (autorizaciones != null) {
            if (!autorizaciones.isEmpty()) {
                for (CitAutorizaciones autorizacion : autorizaciones) {
                    for (CitAutorizacionesServicios autorizacionServicio : autorizacion.getCitAutorizacionesServiciosList()) {
                        AutorizacionReport autorizacionReport = new AutorizacionReport(autorizacion.getIdAutorizacion(), autorizacion.getAdministradora().getRazonSocial(), autorizacion.getPaciente().nombreCompleto(), autorizacion.getNumAutorizacion(), autorizacion.getCerrada(), autorizacionServicio.getFacServicio().getNombreServicio(), autorizacionServicio.getSesionesAutorizadas(), autorizacionServicio.getSesionesRealizadas(), autorizacionServicio.getSesionesPendientes());
                        listaAutorizacionReport.add(autorizacionReport);
                    }
                }
            }
        }
        return listaAutorizacionReport;
    }

    private List<CitaU> generarCitasAuxiliar(List<CitCitas> citas) {
        List<CitaU> listaCitasU = new ArrayList();
        if (citas != null) {
            for (CitCitas cita : citas) {
                CitaU citaU = new CitaU();
                List<CfgEmpresa> listaempresa = cfgEmpresaFacade.findAll();
                //solo existira una empresa => intranet
                citaU.setEmpresa(listaempresa.get(0).getRazonSocial());
                citaU.setEmpresaDireccion(listaempresa.get(0).getDireccion());
                citaU.setEmpresaTelefono(listaempresa.get(0).getTelefono1());
                citaU.setObservaciones(listaempresa.get(0).getObservaciones());
                citaU.setSede(cita.getIdTurno().getIdConsultorio().getIdSede().getNombreSede());
                citaU.setSedeDir(cita.getIdTurno().getIdConsultorio().getIdSede().getDireccion());
                citaU.setSedeTel(cita.getIdTurno().getIdConsultorio().getIdSede().getTelefono1());
                citaU.setIdCita(cita.getIdCita());
                citaU.setFecha(cita.getIdTurno().getFecha());
                citaU.setHora(cita.getIdTurno().getHoraIni());
                citaU.setConsultorio(cita.getIdTurno().getIdConsultorio().getNomConsultorio());
                citaU.setIdPaciente(cita.getIdPaciente().getIdPaciente());
                citaU.setPrestadorPN(cita.getIdPrestador().nombreCompleto());
//                citaU.setPrestadorSN(cita.getIdPrestador().getSegundoNombre());
//                citaU.setPrestadorPA(cita.getIdPrestador().getPrimerApellido());
//                citaU.setPrestadorSA(cita.getIdPrestador().getSegundoApellido());
                citaU.setPrestadorEspecialidad(cita.getIdPrestador().getEspecialidad().getDescripcion());
                citaU.setIdPrestador(cita.getIdPrestador().getIdUsuario());
                citaU.setPacientePN(cita.getIdPaciente().nombreCompleto());
//                citaU.setPacienteSN(cita.getIdPaciente().getSegundoNombre());
//                citaU.setPacientePA(cita.getIdPaciente().getPrimerApellido());
//                citaU.setPacienteSA(cita.getIdPaciente().getSegundoApellido());
                citaU.setCodAdministradora(String.valueOf(cita.getIdAdministradora().getIdAdministradora()));
                citaU.setAdministradora(cita.getIdAdministradora().getRazonSocial());
                citaU.setFechaRegistro(cita.getFechaRegistro());
                citaU.setPacienteTipoDoc(cita.getIdPaciente().getTipoIdentificacion().getDescripcion());
                citaU.setPacienteNumDoc(cita.getIdPaciente().getIdentificacion());
                if (cita.getTipoCita() != null) {
                    citaU.setMotivoConsulta(cita.getTipoCita().getDescripcion());
                }
                int idServicio = cita.getIdServicio().getIdServicio();
                citaU.setServicio(servicioFacade.find(idServicio).getNombreServicio());
                if (cita.getCancelada()) {
                    citaU.setCancelada(true);
                    citaU.setFechaCancelacion(cita.getFechaCancelacion());
                    citaU.setMotivoCancelacion(cita.getMotivoCancelacion().getDescripcion());
                } else {
                    citaU.setCancelada(false);
                    citaU.setFechaCancelacion(null);
                    citaU.setMotivoCancelacion("");
                }
                citaU.setAtendida(cita.getAtendida());
                listaCitasU.add(citaU);

            }
        }
        return listaCitasU;
    }

    public void generarRecordatorio(ActionEvent actionEvent) throws JRException, IOException, SQLException, ClassNotFoundException {
        int id = (int) actionEvent.getComponent().getAttributes().get("id_cita");
        String user = (String) actionEvent.getComponent().getAttributes().get("user");
        String logoEmpresa = (String) actionEvent.getComponent().getAttributes().get("logo_empresa");
        //System.out.println("creando recordatorio para la cita->" + id + " creado por->" + user);
        List<CitCitas> citas = citasFacade.findCitaByIdDos(id);
        List<CitaU> objeto = generarCitasAuxiliar(citas);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(objeto);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath("/citas/reportes/recordatorio.jasper");
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("user", user);
            parametros.put("logoEmpresa", logoEmpresa);
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public void generarRecordatorioMasivo(ActionEvent actionEvent) throws IOException, JRException {
        String logoEmpresa = (String) actionEvent.getComponent().getAttributes().get("logo_empresa");
        //System.out.println("creando recordatorio para la cita->" + id + " creado por->" + user);
        CitasMasivaMB citasMasivaMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{citasMasivaMB}", CitasMasivaMB.class);
        List<CitaU> citas = generarCitasAuxiliar(citasMasivaMB.getListaCitas());
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(citas);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath("/citas/reportes/recordatorioMasivo.jasper");
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("logoEmpresa", logoEmpresa);
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public void generarRecordatorioMasivoV2(ActionEvent actionEvent) throws IOException, JRException {
        String logoEmpresa = (String) actionEvent.getComponent().getAttributes().get("logo_empresa");
        //System.out.println("creando recordatorio para la cita->" + id + " creado por->" + user);
        CitasMasivasV2MB citasMasivasV2MB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{citasMasivasV2MB}", CitasMasivasV2MB.class);
        List<CitaU> citas = generarCitasAuxiliar(citasMasivasV2MB.getListaCitas());
        CfgPacientes paciente = pacientesFachada.find(citas.get(0).getIdPaciente());
        String edad = calcularEdad(paciente.getFechaNacimiento());
        int sesiones = citas.size();
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(citas);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath("/citas/reportes/recordatorioMasivoV2.jasper");
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("logoEmpresa", logoEmpresa);
            parametros.put("sesiones", sesiones);
            parametros.put("edad", edad);
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public void generarRecordatorioMasivoV3(ActionEvent actionEvent) throws IOException, JRException {
        String logoEmpresa = (String) actionEvent.getComponent().getAttributes().get("logo_empresa");
        //System.out.println("creando recordatorio para la cita->" + id + " creado por->" + user);
        CitasMasivasV3MB citasMasivasV3MB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{citasMasivasV3MB}", CitasMasivasV3MB.class);
        String nombrePaquete = citasMasivasV3MB.getNombrePaquete();
        List<CitaU> citas = generarCitasAuxiliar(citasMasivasV3MB.getListaCitas());
        CfgPacientes paciente = pacientesFachada.find(citas.get(0).getIdPaciente());
        String edad = calcularEdad(paciente.getFechaNacimiento());
        int totalCitas = citas.size();
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(citas);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath("/citas/reportes/recordatorioMasivoV3.jasper");
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("logoEmpresa", logoEmpresa);
            parametros.put("citas", totalCitas);
            parametros.put("edad", edad);
            parametros.put("paquete", nombrePaquete.toUpperCase());
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public void generarReporte(ActionEvent actionEvent) throws IOException, JRException {
        String logoEmpresa = (String) actionEvent.getComponent().getAttributes().get("logo_empresa");
        //System.out.println(tipoReporte + " - " + fechaInicial + " - " + fechaFinal);
        Map<String, Object> parametros = new HashMap<>();
//        NO FUNCIONA LA RUTA DEL LOGO
        parametros.put("logoEmpresa", logoEmpresa);
        List<CitCitas> lista;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        switch (tipoReporte) {
            case "1":
                parametros.put("title", "INFORME CITAS ASIGNADAS");
                if (fechaInicial != null || fechaFinal != null || listaAdministradoras.size() > 0 || listaPacientes.size() > 0 || listaPrestadores.size() > 0) {
                    lista = citasFacade.buscarCitasIntervaloParametros(fechaInicial, fechaFinal, listaPacientes, listaPrestadores, listaAdministradoras, 1);
                } else {
                    lista = citasFacade.buscarCitasIntervaloParametros(fechaInicial, fechaFinal, listaPacientes, listaPrestadores, listaAdministradoras, 0);
                }
                citasReport(lista, parametros, "/citas/reportes/citasAsignadas.jasper");
                break;
            case "2":
                parametros.put("title", "INFORME CITAS POR ADMINISTRADORA");
                if (fechaInicial != null || fechaFinal != null || listaAdministradoras.size() > 0 || listaPacientes.size() > 0 || listaPrestadores.size() > 0) {
                    lista = citasFacade.findCitaGroupByAdministradoraParametros(fechaInicial, fechaFinal, listaPacientes, listaPrestadores, listaAdministradoras, 1);
                } else {
                    lista = citasFacade.findCitaGroupByAdministradoraParametros(fechaInicial, fechaFinal, listaPacientes, listaPrestadores, listaAdministradoras, 0);
                }
                citasReport(lista, parametros, "/citas/reportes/citasAdministradora.jasper");
                break;
            case "3":
                parametros.put("title", "INFORME CITAS CANCELADAS");
                if (fechaInicial != null || fechaFinal != null || listaAdministradoras.size() > 0 || listaPacientes.size() > 0 || listaPrestadores.size() > 0) {
                    lista = citasFacade.findCitasCanceladasParametrizable(fechaInicial, fechaFinal, listaPacientes, listaPrestadores, listaAdministradoras, 1);
                } else {
                    lista = citasFacade.findCitasCanceladasParametrizable(fechaInicial, fechaFinal, listaPacientes, listaPrestadores, listaAdministradoras, 0);
                }
                citasReport(lista, parametros, "/citas/reportes/citasCanceladas.jasper");
                break;
            case "4":
                parametros.put("title", "INFORME DE OPORTUNIDAD");
                if (fechaInicial == null || fechaFinal == null) {
                    fechaFinal = null;
                    fechaInicial = null;
                }
                String p = fechaInicial == null ? "TODAS" : dateFormat.format(fechaInicial) + " - " + dateFormat.format(fechaFinal);
                parametros.put("periodo", p);
                List<Object[]> rows = citasFacade.findOportunidad(fechaInicial, fechaFinal);
                oportunidadReport(rows, parametros, "/citas/reportes/oportunidad.jasper");
                break;
            case "5":
                parametros.put("title", "HISTORIAL DE CITAS");

                String periodo;
                if (fechaInicial == null && fechaFinal == null) {
                    periodo = "TODAS";
                } else if (fechaInicial != null && fechaFinal == null) {
                    periodo = "Desde " + dateFormat.format(fechaInicial);
                } else if (fechaInicial == null && fechaFinal != null) {
                    periodo = "Hasta " + dateFormat.format(fechaFinal);
                } else {
                    periodo = dateFormat.format(fechaInicial) + " - " + dateFormat.format(fechaFinal);
                }
                lista = citasFacade.findHistoriaClinica(pacienteHC, fechaInicial, fechaFinal);
                parametros.put("periodo", periodo);
                if (!lista.isEmpty()) {
                    String paciente = lista.get(0).getIdPaciente().getPrimerNombre() + " " + lista.get(0).getIdPaciente().getSegundoNombre() + " " + lista.get(0).getIdPaciente().getPrimerApellido() + " " + lista.get(0).getIdPaciente().getSegundoApellido();
                    parametros.put("paciente", paciente);
                }
                citasReport(lista, parametros, "/citas/reportes/historialcitas.jasper");
                break;
            case "6":
                parametros.put("title", "INFORME DE AUTORIZACIONES");
                boolean cerrada;
                cerrada = estadoAutorizacion == 1;
                List<CitAutorizaciones> listaAutorizaciones = autorizacionesFacade.buscarAutorizacionReporte(estadoAutorizacion, cerrada, listaAdministradoras, listaPacientes, numAutorizacion);
                parametros.put("fecha", new Date());
                autorizacionReporte(listaAutorizaciones, parametros, "/citas/reportes/autorizaciones.jasper");
                break;
        }

    }

    public void eventoChange() {
//        imprimirMensaje("Informacion", String.valueOf(estadoAutorizacion), FacesMessage.SEVERITY_INFO);
        setEstadoAutorizacion(estadoAutorizacion);
    }

    private void citasReport(List<CitCitas> citas, Map<String, Object> parametros, String path) throws IOException, JRException {
        List<CitaU> citasU = generarCitasAuxiliar(citas);

        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(citasU);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath(path);
            //Map<String, Object> parametros = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    private void oportunidadReport(List<Object[]> rows, Map<String, Object> parametros, String path) throws IOException, JRException {
        List<Oportunidad> listaOportunidad = generarListaOportunidad(rows);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listaOportunidad);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath(path);
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    private void autorizacionReporte(List<CitAutorizaciones> autorizaciones, Map<String, Object> parametros, String path) throws IOException, JRException {
        List<AutorizacionReport> listaReporte = generarListaAutorizaciones(autorizaciones);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listaReporte);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath(path);
            //Map<String, Object> parametros = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public void addPrestadorFiltro() {
        listaPrestadores.add(prestadorSeleccionado);
    }

    public void addPacienteFiltro() {
        listaPacientes.add(pacienteSeleccionado);
        if (tipoReporte.equals("6")) {
            RequestContext.getCurrentInstance().update("formfiltradoAutorizaciones");
        } else {
            RequestContext.getCurrentInstance().update("formfiltrado");
        }
    }

    public void addAdministradoraFiltro() {
        listaAdministradoras.add(administradoraSeleccionada);
        if (tipoReporte.equals("6")) {
            RequestContext.getCurrentInstance().update("formfiltradoAutorizaciones");
        } else {
            RequestContext.getCurrentInstance().update("formfiltrado");
        }
    }

    public void limpiarFiltros() {
        listaAdministradoras.clear();
        listaPacientes.clear();
        listaPrestadores.clear();
    }

    public void limpiarFiltrosDos() {
        listaAdministradoras.clear();
        listaPacientes.clear();
        setNumAutorizacion(null);
        setEstadoAutorizacion(0);
    }

//---------------------------------------------------------------------
//------------------------GETTERS AND SETTERS -------------------------
//---------------------------------------------------------------------
    /**
     * @return the tipoReporte
     */
    public String getTipoReporte() {
        return tipoReporte;
    }

    /**
     * @param tipoReporte the tipoReporte to set
     */
    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    /**
     * @return the fechaInicial
     */
    public Date getFechaInicial() {
        return fechaInicial;
    }

    /**
     * @param fechaInicial the fechaInicial to set
     */
    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    /**
     * @return the fechaFinal
     */
    public Date getFechaFinal() {
        return fechaFinal;
    }

    /**
     * @param fechaFinal the fechaFinal to set
     */
    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public List<CfgUsuarios> getListaPrestadores() {
        return listaPrestadores;
    }

    public void setListaPrestadores(List<CfgUsuarios> listaPrestadores) {
        this.listaPrestadores = listaPrestadores;
    }

    public List<CfgPacientes> getListaPacientes() {
        return listaPacientes;
    }

    public void setListaPacientes(List<CfgPacientes> listaPacientes) {
        this.listaPacientes = listaPacientes;
    }

    public List<FacAdministradora> getListaAdministradoras() {
        return listaAdministradoras;
    }

    public void setListaAdministradoras(List<FacAdministradora> listaAdministradoras) {
        this.listaAdministradoras = listaAdministradoras;
    }

    public CfgPacientes getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }

    public void setPacienteSeleccionado(CfgPacientes pacienteSeleccionado) {
        this.pacienteSeleccionado = pacienteSeleccionado;
    }

    public CfgUsuarios getPrestadorSeleccionado() {
        return prestadorSeleccionado;
    }

    public void setPrestadorSeleccionado(CfgUsuarios prestadorSeleccionado) {
        this.prestadorSeleccionado = prestadorSeleccionado;
    }

    public FacAdministradora getAdministradoraSeleccionada() {
        return administradoraSeleccionada;
    }

    public void setAdministradoraSeleccionada(FacAdministradora administradoraSeleccionada) {
        this.administradoraSeleccionada = administradoraSeleccionada;
    }

    public LazyDataModel<CfgUsuarios> getPrestadores() {
        return prestadores;
    }

    public void setPrestadores(LazyDataModel<CfgUsuarios> prestadores) {
        this.prestadores = prestadores;
    }

    public LazyDataModel<CfgPacientes> getPacientes() {
        return pacientes;
    }

    public void setPacientes(LazyDataModel<CfgPacientes> pacientes) {
        this.pacientes = pacientes;
    }

    public List<SelectItem> getListaReportes() {
        return listaReportes;
    }

    public void setListaReportes(List<SelectItem> listaReportes) {
        this.listaReportes = listaReportes;
    }

    public boolean isRenBtnFiltrar() {
        return renBtnFiltrar;
    }

    public void setRenBtnFiltrar(boolean renBtnFiltrar) {
        this.renBtnFiltrar = renBtnFiltrar;
    }

    public String getDisplayBusquedaPaciente() {
        return displayBusquedaPaciente;
    }

    public void setDisplayBusquedaPaciente(String displayBusquedaPaciente) {
        this.displayBusquedaPaciente = displayBusquedaPaciente;
    }

    public String getIdentificacionPaciente() {
        return identificacionPaciente;
    }

    public void setIdentificacionPaciente(String identificacionPaciente) {
        this.identificacionPaciente = identificacionPaciente;
    }

    public boolean isRenBtnReporte() {
        return renBtnReporte;
    }

    public void setRenBtnReporte(boolean renBtnReporte) {
        this.renBtnReporte = renBtnReporte;
    }

    public CfgPacientes getPacienteHC() {
        return pacienteHC;
    }

    public void setPacienteHC(CfgPacientes pacienteHC) {
        this.pacienteHC = pacienteHC;
    }

    public LazyDataModel<CfgPacientes> getListaPacientesBusqueda() {
        return listaPacientesBusqueda;
    }

    public void setListaPacientesBusqueda(LazyDataModel<CfgPacientes> listaPacientesBusqueda) {
        this.listaPacientesBusqueda = listaPacientesBusqueda;
    }

    public int getEstadoAutorizacion() {
        return estadoAutorizacion;
    }

    public void setEstadoAutorizacion(int estadoAutorizacion) {
        this.estadoAutorizacion = estadoAutorizacion;
    }

    public String getNumAutorizacion() {
        return numAutorizacion;
    }

    public void setNumAutorizacion(String numAutorizacion) {
        this.numAutorizacion = numAutorizacion;
    }

    public List<SelectItem> getListaEstadoAutorizacion() {
        return listaEstadoAutorizacion;
    }

    public void setListaEstadoAutorizacion(List<SelectItem> listaEstadoAutorizacion) {
        this.listaEstadoAutorizacion = listaEstadoAutorizacion;
    }
}
