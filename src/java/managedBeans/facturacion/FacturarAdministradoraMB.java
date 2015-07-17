/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import beans.utilidades.ConversorDeNumerosALetras;
import beans.utilidades.FilaDataTable;
import beans.utilidades.MetodosGenerales;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.FacAdministradora;
import modelo.entidades.FacConsecutivo;
import modelo.entidades.FacContrato;
import modelo.entidades.FacFacturaAdmi;
import modelo.entidades.FacFacturaInsumo;
import modelo.entidades.FacFacturaMedicamento;
import modelo.entidades.FacFacturaPaciente;
import modelo.entidades.FacFacturaPaquete;
import modelo.entidades.FacFacturaServicio;
import modelo.entidades.FacPeriodo;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgInsumoFacade;
import modelo.fachadas.CfgMedicamentoFacade;
import modelo.fachadas.CfgPacientesFacade;
import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.FacAdministradoraFacade;
import modelo.fachadas.FacConsecutivoFacade;
import modelo.fachadas.FacContratoFacade;
import modelo.fachadas.FacFacturaAdmiFacade;
import modelo.fachadas.FacFacturaInsumoFacade;
import modelo.fachadas.FacFacturaMedicamentoFacade;
import modelo.fachadas.FacFacturaPacienteFacade;
import modelo.fachadas.FacFacturaPaqueteFacade;
import modelo.fachadas.FacFacturaServicioFacade;
import modelo.fachadas.FacImpuestosFacade;
import modelo.fachadas.FacPaqueteFacade;
import modelo.fachadas.FacPeriodoFacade;
import modelo.fachadas.FacServicioFacade;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author santos
 */
@ManagedBean(name = "facturarAdministradoraMB")
@SessionScoped
public class FacturarAdministradoraMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    FacFacturaPacienteFacade facturaPacienteFacade;

    @EJB
    FacAdministradoraFacade administradoraFacade;
    @EJB
    FacPeriodoFacade periodoFacade;
    @EJB
    FacContratoFacade contratoFacade;
    @EJB
    FacImpuestosFacade impuestosFacade;
    @EJB
    FacConsecutivoFacade consecutivoFacade;
    @EJB
    CfgClasificacionesFacade clasificacionesFachada;

    @EJB
    CfgUsuariosFacade usuariosFacade;
    @EJB
    CfgPacientesFacade pacientesFacade;
    @EJB
    FacServicioFacade servicioFacade;
    @EJB
    CfgMedicamentoFacade medicamentoFacade;
    @EJB
    CfgInsumoFacade insumoFacade;
    @EJB
    FacPaqueteFacade paqueteFacade;

    @EJB
    FacFacturaAdmiFacade facturaAdmiFacade;

    @EJB
    FacFacturaServicioFacade facturaServicioFacade;
    @EJB
    FacFacturaInsumoFacade facturaInsumoFacade;
    @EJB
    FacFacturaMedicamentoFacade facturaMedicamentoFacade;
    @EJB
    FacFacturaPaqueteFacade facturaPaqueteFacade;
    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------
    private FacAdministradora administradoraActual;
    private List<FacContrato> listaContratos;
    private LazyDataModel<FilaDataTable> listaFacturas;
    private FacFacturaAdmi facturaAdmiSeleccionada;
    private FilaDataTable facturaAdmiSeleccionadaTabla;
    private FacPeriodo periodoSeleccionado;
    private FacContrato contratoActual;
    private FacContrato contratoTodos;//se usa cuando se quiere usar todos los contratos, tiene id=-1
    private FacConsecutivo consecutivoSeleccionado;
    private List<FacFacturaPaciente> listaFacturasDePacientes;//lista de facturas de pacientes para una administradora determinada
    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------

    private boolean disableControlesBuscarFactura = true;

    private final SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    private final SimpleDateFormat formatoFechaSql = new SimpleDateFormat("dd/MM/yyyy", new Locale("ES"));
    private final DecimalFormat formateadorDecimal = new DecimalFormat("0.00");
    private List<EstructuraFacturaAdministradora> listaRegistrosParaPdf;
    private Date fechaInicial = new Date();
    private Date fechaFinal = new Date();

    private String observacionAnulacion = "";

    private final Calendar calendarFechaFinalMasUnDia = Calendar.getInstance();
    private Date fechaElaboracion = new Date();
    private String idAdministradora = "";
    private String idContrato = "";
    private LoginMB loginMB;
    private int facturasSinAutorizacionCerradaInt = 0;
    private String notaFueraRango = "";//me dice si existen facturas sin autorizacion cerrada 
    private String notaNoCerradas = "";//me dice si existen facturas fuera del rango
    private String estiloNotas = "";//me dice si existen facturas fuera del rango
    private int facturasFueraDeRangoInt = 0;
    //private boolean renderedTablaFacturas = false;

    //------- FACTURA --------------    
    private String msjHtmlPeriodo = ""; //mensaje informativo soble estado del periodo: Abierto, Cerrado, Sin Crear
    private String estiloPeriodo = "";

    private String documento = "1822";//factura, cuenta cobro, recibo.
    private String msjHtmlDocumento = ""; //mensaje informativo sobre el tipo Documento 
    private String estiloDocumento = "";

    private String observaciones = "";//observaciones de la factura

    private double acumuladoIva = 0;//sumatoria de los ivas cobrados por cada item
    private double acumuladoCree = 0;//porcentage correspondiente al cree
    private double acumuladoUsuario = 0;//sumatoria de los valores que paga por usuario
    private double acumuladoEmpresa = 0;//sumatoria de los valores que pagara la aempresa
    private double acumuladoParcial = 0;//sumatoria de los valores parciales
    private double acumuladoTotal = 0;
    private double acumuladoCopago = 0;//sumatoria de los copagos(hay algunos con cero si pertenecian a un paquete de citas)
    private double acumuladoCuotaModeradora = 0;//sumatoria de los copagos(hay algunos con cero si pertenecian a un paquete de citas)
    //------- FACTURA --------------    
    private String tituloTabServiciosFactura = "Servicios (0)";
    private String tituloTabInsumosFactura = "Insumos (0)";
    private String tituloTabMedicamentosFactura = "Medicamentos (0)";
    private String tituloTabPaquetesFactura = "Paquetes (0)";
    private List<FilaDataTable> listaServiciosFactura = new ArrayList<>();
    private List<FilaDataTable> listaInsumosFactura = new ArrayList<>();
    private List<FilaDataTable> listaMedicamentosFactura = new ArrayList<>();
    private List<FilaDataTable> listaPaquetesFactura = new ArrayList<>();
    private List<FilaDataTable> listaServiciosFacturaFiltro = new ArrayList<>();
    private List<FilaDataTable> listaInsumosFacturaFiltro = new ArrayList<>();
    private List<FilaDataTable> listaMedicamentosFacturaFiltro = new ArrayList<>();
    private List<FilaDataTable> listaPaquetesFacturaFiltro = new ArrayList<>();

    //---------------------------------------------------
    //------------- FUNCIONES INICIALES  ----------------
    //---------------------------------------------------  
    @PostConstruct
    public void inicializar() {
        contratoTodos = new FacContrato(-1);
        contratoTodos.setDescripcion("TODOS");
        fechaInicial.setDate(1);
        fechaInicial.setMonth(calendarFechaFinalMasUnDia.get(Calendar.MONTH));
        fechaInicial.setYear(calendarFechaFinalMasUnDia.get(Calendar.YEAR) - 1900);

        fechaFinal.setDate(calendarFechaFinalMasUnDia.get(Calendar.DATE));
        fechaFinal.setMonth(calendarFechaFinalMasUnDia.get(Calendar.MONTH));
        fechaFinal.setYear(calendarFechaFinalMasUnDia.get(Calendar.YEAR) - 1900);
        listaFacturas = new LazyFacturasAdmiDataModel(facturaAdmiFacade);//toma solo facturas de adminisatradoras        
    }

    public void recargarDatosFacturacion() {
        //funcion que actualiza la interfaz si se han realizado cambios desde otros modulos
        //documento = clasificacionesFachada.buscarPorMaestroObservacion("TipoFacturacion", "Factura de Venta").get(0).getId().toString();
        cambiaDocumento();//determinar estado de consecutivos
        validarPeriodo();//determinar si existe y esta abierto el periodo
        recalcularValorFactura();
        if (validarNoVacio(idAdministradora)) {
            buscarItems();
        }
    }

    public FacturarAdministradoraMB() {
    }

    //---------------------------------------------------
    //------------- FUNCIONES FACTURACION  --------------
    //---------------------------------------------------  
    public void guardarFactura() {
        if (estiloDocumento.length() != 0) {
            imprimirMensaje("Error", msjHtmlDocumento.replace("<br/>", " "), FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (estiloPeriodo.length() != 0) {
            imprimirMensaje("Error", msjHtmlPeriodo.replace("<br/>", " "), FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (listaServiciosFactura.isEmpty() && listaInsumosFactura.isEmpty() && listaMedicamentosFactura.isEmpty() && listaPaquetesFactura.isEmpty()) {
            imprimirMensaje("Error", "La factura se encuentra vacia", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoConfirmarGuardarFactura').show();");
    }

    public void confirmarGuardarFactura() {
        String numDocStr;
        int numDocInt;
        consecutivoSeleccionado = consecutivoFacade.find(consecutivoSeleccionado.getIdConsecutivo());
        if (Objects.equals(consecutivoSeleccionado.getActual(), consecutivoSeleccionado.getFin())) {//se vuelve a comprobar el consecutivo 
            imprimirMensaje("Error", "Otro usuario utilizó el último consecutivo para el tipo de documento seleccionado", FacesMessage.SEVERITY_ERROR);
            cambiaDocumento();
            RequestContext.getCurrentInstance().update("IdFormFacturacion");
            return;
        } else {
            numDocInt = consecutivoSeleccionado.getActual() + 1;
            numDocStr = cerosIzquierda(numDocInt, 5);//el documento se completa con ceros a 5 cifras
            if (validarNoVacio(consecutivoSeleccionado.getPrefijo())) {
                numDocStr = consecutivoSeleccionado.getPrefijo() + numDocStr;
            }
            consecutivoSeleccionado.setActual(consecutivoSeleccionado.getActual() + 1);
            consecutivoFacade.edit(consecutivoSeleccionado);
        }
        FacFacturaAdmi nuevaFactura = new FacFacturaAdmi();
        nuevaFactura.setTipoDocumento(clasificacionesFachada.find(Integer.parseInt(documento)));
        nuevaFactura.setNumeroDocumento(numDocInt);
        nuevaFactura.setCodigoDocumento(numDocStr);
        nuevaFactura.setFechaElaboracion(fechaElaboracion);
        nuevaFactura.setFechaInicial(fechaInicial);
        nuevaFactura.setFechaFinal(fechaFinal);
        nuevaFactura.setIdPeriodo(periodoSeleccionado);
        if (contratoActual.getIdContrato() != -1) {
            nuevaFactura.setIdContrato(contratoActual);
        }
        //  tipo_ingreso
        //  numero_autorizacion
        //  fecha_autorizacion
        nuevaFactura.setObservacion(observaciones);
        nuevaFactura.setResolucionDian(consecutivoSeleccionado.getResolucionDian());
        nuevaFactura.setIdAdministradora(administradoraActual);
        nuevaFactura.setValoresIva(acumuladoIva);
        nuevaFactura.setValoresCree(acumuladoCree);
        nuevaFactura.setValoresCopago(acumuladoCopago);
        nuevaFactura.setValoresCuotaModeradora(acumuladoCuotaModeradora);
        nuevaFactura.setValorParcial(acumuladoParcial);
        nuevaFactura.setValorEmpresa(acumuladoEmpresa);
        nuevaFactura.setValorTotal(acumuladoTotal);
        nuevaFactura.setAnulada(false);
        nuevaFactura.setFechaSistema(new Date());
        nuevaFactura.setFacFacturaPacienteList(listaFacturasDePacientes);
        facturaAdmiFacade.create(nuevaFactura);

        for (FacFacturaPaciente fac : listaFacturasDePacientes) {//se cambia el estado defacturadaUnAdmi a true
            fac.setFacturadaEnAdmi(true);
            facturaPacienteFacade.edit(fac);
        }

        //ingresar detalles                
        limpiarFormularioFacturacion();
        recargarDatosFacturacion();
        RequestContext.getCurrentInstance().update("IdFormFacturacion");
        imprimirMensaje("Correcto", nuevaFactura.getTipoDocumento().getDescripcion() + " " + numDocStr + " se ha creado.", FacesMessage.SEVERITY_INFO);

    }

    private void buscarItems() {
        notaFueraRango = "";
        notaNoCerradas = "";
        estiloNotas = "";
        facturasSinAutorizacionCerradaInt = 0;
        facturasFueraDeRangoInt = 0;
        //carga los todos los items de las facturas de pacientes en el rango dado
        FilaDataTable nuevaFila;
        listaServiciosFactura = new ArrayList<>();
        listaInsumosFactura = new ArrayList<>();
        listaPaquetesFactura = new ArrayList<>();
        listaMedicamentosFactura = new ArrayList<>();
        listaServiciosFacturaFiltro = new ArrayList<>();
        listaInsumosFacturaFiltro = new ArrayList<>();
        listaPaquetesFacturaFiltro = new ArrayList<>();
        listaMedicamentosFacturaFiltro = new ArrayList<>();
        tituloTabServiciosFactura = "Servicios (0)";
        tituloTabInsumosFactura = "Insumos (0)";
        tituloTabMedicamentosFactura = "Medicamentos (0)";
        tituloTabPaquetesFactura = "Paquetes (0)";
        acumuladoIva = 0;//sumatoria de los ivas cobrados por cada item
        acumuladoCree = 0;//porcentage correspondiente al cree
        acumuladoUsuario = 0;//sumatoria de los valores que paga por usuario
        acumuladoEmpresa = 0;//sumatoria de los valores que pagara la aempresa
        acumuladoParcial = 0;//sumatoria de los valores parciales
        acumuladoTotal = 0;
        acumuladoCopago = 0;//sumatoria de los copagos(hay algunos con cero si pertenecian a un paquete de citas)
        acumuladoCuotaModeradora = 0;//sumatoria de los copagos(hay algunos con cero si pertenecian a un paquete de citas)

        RequestContext.getCurrentInstance().update("IdFormFacturacion");
        if (!validarNoVacio(idAdministradora)) {
            return;
        }
        if (!validarNoVacio(idContrato)) {
            return;
        }
        if (fechaInicial == null || fechaFinal == null) {
            return;
        }
        //a fecha final se le debe aumentar un dia para tomar en cuenta las horas que han transcurrido                
        calendarFechaFinalMasUnDia.setTime(fechaFinal);
        calendarFechaFinalMasUnDia.add(Calendar.DATE, 1);

        administradoraActual = administradoraFacade.find(Integer.parseInt(idAdministradora));
        //determinar que facturas tiene una administradora en un rango de fechas determinado

        String sql = "select * from fac_factura_paciente where id_administradora=" + idAdministradora + " AND ";
        if (idContrato.compareTo("-1") != 0) {//No se requieren todos los contratos
            sql = sql + "id_contrato = " + idContrato + " AND ";
        }
        sql = sql + "facturada_en_admi = false AND "
                + "fecha_elaboracion >= to_date('" + formatoFechaSql.format(fechaInicial) + "','dd/MM/yyyy') AND "
                + "fecha_elaboracion <= to_date('" + formatoFechaSql.format(calendarFechaFinalMasUnDia.getTime()) + "','dd/MM/yyyy')";
        listaFacturasDePacientes = facturaPacienteFacade.consultaNativaFacturas(sql);
        if (listaFacturasDePacientes.isEmpty()) {
            imprimirMensaje("Informacion", "No se encontraron registros en este rango de fechas para la administradora seleccionada", FacesMessage.SEVERITY_WARN);
            return;
        }
        boolean facCerrada;
        //for (FacFacturaPaciente factura : listaFacturasDePacientes) {//se debe agrupar por servicios,insumos,medicamentos,paquetes   
        for (int i = 0; i < listaFacturasDePacientes.size(); i++) {

            FacFacturaPaciente factura = listaFacturasDePacientes.get(i);
            facCerrada = false;
            List<FacFacturaPaciente> listaFacturasPorAutorizacion = new ArrayList<>();
            if (factura.getIdCita() != null) {//si factura no esta cerrada no se agrega
                if (factura.getIdCita().getIdAutorizacion() != null) {
                    if (!factura.getIdCita().getIdAutorizacion().getCerrada()) {
                        facturasSinAutorizacionCerradaInt++;
                        facCerrada = true;
                    } else {
                        listaFacturasPorAutorizacion = facturaPacienteFacade.buscarPorAutorizacion(factura.getIdCita().getIdAutorizacion().getIdAutorizacion().toString());
                    }
                }
            }
            if (!listaFacturasPorAutorizacion.isEmpty()) {
                //se agregan las que no esten agregadas inicialmente
                boolean encontrada;
                for (FacFacturaPaciente fA : listaFacturasPorAutorizacion) {
                    encontrada = false;
                    for (FacFacturaPaciente fP : listaFacturasDePacientes) {
                        if (Objects.equals(fA.getIdFacturaPaciente(), fP.getIdFacturaPaciente())) {
                            encontrada = true;
                            break;
                        }
                    }
                    if (!encontrada) {
                        facturasFueraDeRangoInt++;
                        listaFacturasDePacientes.add(fA);
                    }
                }
            }

            if (!facCerrada) {//si la factura no esta cerrada se agrega 

                acumuladoIva = acumuladoIva + (factura.getValorParcial() * (factura.getIva() / 100));
                acumuladoCree = acumuladoCree + (factura.getValorParcial() * (factura.getCree() / 100));
                acumuladoUsuario = acumuladoUsuario + factura.getValorUsuario();
                acumuladoEmpresa = acumuladoEmpresa + factura.getValorEmpresa();
                acumuladoParcial = acumuladoParcial + factura.getValorParcial();
                acumuladoTotal = acumuladoTotal + factura.getValorTotal();
                acumuladoCopago = acumuladoCopago + factura.getCopago();
                acumuladoCuotaModeradora = acumuladoCuotaModeradora + factura.getCuotaModeradora();
                //servicios---------------------------------------------------------------------------
                for (FacFacturaServicio a : factura.getFacFacturaServicioList()) {
                    nuevaFila = new FilaDataTable();
                    nuevaFila.setColumna1(String.valueOf(listaServiciosFactura.size() + 1));//primer columna es identificador el item
                    nuevaFila.setColumna2(formateadorFecha.format(a.getFechaServicio()));
                    nuevaFila.setColumna3(factura.getIdPaciente().getTipoIdentificacion().getDescripcion() + " - " + factura.getIdPaciente().getIdentificacion());//Paciente
                    nuevaFila.setColumna4(factura.getIdPaciente().nombreCompleto());//Paciente                
                    if (a.getIdServicio().getCodigoCup() != null) {
                        nuevaFila.setColumna5(a.getIdServicio().getCodigoCup() + " - " + a.getIdServicio().getNombreServicio());//nombre servicio                                                    
                    } else {
                        nuevaFila.setColumna5(" - " + a.getIdServicio().getNombreServicio());//nombre servicio                                                    
                    }
                    nuevaFila.setColumna6(a.getCantidadServicio().toString());//quinta cantidad
                    nuevaFila.setColumna7(a.getValorServicio().toString());//valor servicio                

                    nuevaFila.setColumna20(factura.getIdPaciente().getIdPaciente().toString());
                    nuevaFila.setColumna21(a.getIdMedico().getIdUsuario().toString());
                    nuevaFila.setColumna22(a.getIdServicio().getIdServicio().toString());

                    nuevaFila.setColumna30(String.valueOf(a.getFacFacturaServicioPK().getIdDetalle()));//id_detalle (parte llave primaria)
                    nuevaFila.setColumna31(String.valueOf(a.getFacFacturaServicioPK().getIdFactura()));//id_factura (parte llave primaria)

                    listaServiciosFactura.add(nuevaFila);
                }
                //insumos---------------------------------------------------------------------------
                for (FacFacturaInsumo a : factura.getFacFacturaInsumoList()) {//buscar si el insumo ya fue aagregado a la lista de items                
                    nuevaFila = new FilaDataTable();
                    nuevaFila.setColumna1(String.valueOf(listaInsumosFactura.size() + 1));//primer columna es identificador el item
                    nuevaFila.setColumna2(formateadorFecha.format(a.getFechaInsumo()));
                    nuevaFila.setColumna3(factura.getIdPaciente().getTipoIdentificacion().getDescripcion() + " - " + factura.getIdPaciente().getIdentificacion());//Paciente
                    nuevaFila.setColumna4(factura.getIdPaciente().nombreCompleto());//Paciente                
                    nuevaFila.setColumna5(a.getIdInsumo().getCodigoInsumo() + " - " + a.getIdInsumo().getNombreInsumo());//nombre servicio                                                    
                    nuevaFila.setColumna6(a.getCantidadInsumo().toString());//cantidad
                    nuevaFila.setColumna7(a.getValorInsumo().toString());//valor servicio

                    nuevaFila.setColumna20(factura.getIdPaciente().getIdPaciente().toString());
                    nuevaFila.setColumna21(a.getIdMedico().getIdUsuario().toString());
                    nuevaFila.setColumna22(a.getIdInsumo().getIdInsumo().toString());

                    nuevaFila.setColumna30(String.valueOf(a.getFacFacturaInsumoPK().getIdDetalle()));//id_detalle (parte llave primaria)
                    nuevaFila.setColumna31(String.valueOf(a.getFacFacturaInsumoPK().getIdFactura()));//id_factura (parte llave primaria)

                    listaInsumosFactura.add(nuevaFila);
                }
                // medicamentos---------------------------------------------------------------------------
                for (FacFacturaMedicamento a : factura.getFacFacturaMedicamentoList()) {
                    nuevaFila = new FilaDataTable();
                    nuevaFila.setColumna1(String.valueOf(listaMedicamentosFactura.size() + 1));//es identificador el item
                    nuevaFila.setColumna2(formateadorFecha.format(a.getFechaMedicamento()));
                    nuevaFila.setColumna3(factura.getIdPaciente().getTipoIdentificacion().getDescripcion() + " - " + factura.getIdPaciente().getIdentificacion());//Paciente
                    nuevaFila.setColumna4(factura.getIdPaciente().nombreCompleto());//Paciente                
                    nuevaFila.setColumna5(a.getIdMedicamento().getCodigoMedicamento() + " - " + a.getIdMedicamento().getNombreMedicamento());//nombre servicio                                                    
                    nuevaFila.setColumna6(a.getCantidadMedicamento().toString());// cantidad
                    nuevaFila.setColumna7(a.getValorMedicamento().toString());//valor servicio

                    nuevaFila.setColumna20(factura.getIdPaciente().getIdPaciente().toString());
                    nuevaFila.setColumna21(a.getIdMedico().getIdUsuario().toString());
                    nuevaFila.setColumna22(a.getIdMedicamento().getIdMedicamento().toString());

                    nuevaFila.setColumna30(String.valueOf(a.getFacFacturaMedicamentoPK().getIdDetalle()));//id_detalle (parte llave primaria)
                    nuevaFila.setColumna31(String.valueOf(a.getFacFacturaMedicamentoPK().getIdFactura()));//id_factura (parte llave primaria)

                    listaMedicamentosFactura.add(nuevaFila);
                }
                //paquetes---------------------------------------------------------------------------
                for (FacFacturaPaquete a : factura.getFacFacturaPaqueteList()) {
                    nuevaFila = new FilaDataTable();
                    nuevaFila.setColumna1(String.valueOf(listaPaquetesFactura.size() + 1));//es identificador el item
                    nuevaFila.setColumna2(formateadorFecha.format(a.getFechaPaquete()));
                    nuevaFila.setColumna3(factura.getIdPaciente().getTipoIdentificacion().getDescripcion() + " - " + factura.getIdPaciente().getIdentificacion());//Paciente
                    nuevaFila.setColumna4(factura.getIdPaciente().nombreCompleto());//Paciente                
                    nuevaFila.setColumna5(a.getIdPaquete().getCodigoPaquete() + " - " + a.getIdPaquete().getNombrePaquete());//nombre servicio                                                    
                    nuevaFila.setColumna6(a.getCantidadPaquete().toString());//quinta cantidad
                    nuevaFila.setColumna7(a.getValorPaquete().toString());//servicio

                    nuevaFila.setColumna20(factura.getIdPaciente().getIdPaciente().toString());
                    nuevaFila.setColumna21(a.getIdMedico().getIdUsuario().toString());
                    nuevaFila.setColumna22(a.getIdPaquete().getIdPaquete().toString());

                    nuevaFila.setColumna30(String.valueOf(a.getFacFacturaPaquetePK().getIdDetalle()));//id_detalle (parte llave primaria)
                    nuevaFila.setColumna31(String.valueOf(a.getFacFacturaPaquetePK().getIdFactura()));//id_factura (parte llave primaria)

                    listaPaquetesFactura.add(nuevaFila);
                }
            }
        }
        if (facturasFueraDeRangoInt != 0) {
            estiloNotas = "color: blue; border-color: gray; border-width: 2px; border-style: solid; border-radius: 7px 7px 7px 7px;";
            notaFueraRango = "Se agregaron: " + facturasFueraDeRangoInt + " facturas(estaban fuera de rango)";
        }
        if (facturasSinAutorizacionCerradaInt != 0) {
            estiloNotas = "color: blue; border-color: gray; border-width: 2px; border-style: solid; border-radius: 7px 7px 7px 7px;";
            notaNoCerradas = "Existen: " + facturasSinAutorizacionCerradaInt + " facturas sin autorización cerrada";
        }
        listaServiciosFacturaFiltro.addAll(listaServiciosFactura);
        tituloTabServiciosFactura = "Servicios (" + listaServiciosFactura.size() + ")";
        listaInsumosFacturaFiltro.addAll(listaInsumosFactura);
        tituloTabInsumosFactura = "Insumos (" + listaInsumosFactura.size() + ")";
        listaMedicamentosFacturaFiltro.addAll(listaMedicamentosFactura);
        tituloTabMedicamentosFactura = "Medicamentos (" + listaMedicamentosFactura.size() + ")";
        listaPaquetesFacturaFiltro.addAll(listaPaquetesFactura);
        tituloTabPaquetesFactura = "Paquetes (" + listaPaquetesFactura.size() + ")";
        //renderedTablaFacturas = true;
        RequestContext.getCurrentInstance().update("IdFormFacturacion");
    }

    public void cambiaDocumento() {//determinar si existe un consecutivo
        //System.out.println("Cambia documento");
        msjHtmlDocumento = "Se debe configurar <br/>consecutivos para el <br/>tipo de documento";
        estiloDocumento = "border-color: orange; border-width: 2px; border-style: solid; border-radius: 7px 7px 7px 7px;";
        if (validarNoVacio(documento)) {
            List<FacConsecutivo> listaConsecutivos = consecutivoFacade.buscarPorTipoDocumento(Integer.parseInt(documento));
            if (listaConsecutivos != null && !listaConsecutivos.isEmpty()) {
                for (FacConsecutivo c : listaConsecutivos) {
                    if (!Objects.equals(c.getActual(), c.getFin())) {
                        msjHtmlDocumento = "Correcto: <br/>tiene consecutivos";
                        estiloDocumento = "";
                        consecutivoSeleccionado = c;
                        observaciones = c.getTexto();
                    }
                }
            }
        } else {
            msjHtmlDocumento = "Debe seleccionar <br/> un documento";
            estiloDocumento = "border-color: orange; border-width: 2px; border-style: solid; border-radius: 7px 7px 7px 7px;";
            observaciones = "";
        }
    }

    public void validarPeriodo() {//determina existencia y estado de un periodo para la fecha de la factura
        //System.out.println("validarPeriodo");
        msjHtmlPeriodo = "";
        if (fechaFinal != null) {
            List<FacPeriodo> listaPeriodos = periodoFacade.buscarOrdenado();
            for (FacPeriodo p : listaPeriodos) {
                if (fechaDentroDeRangoMas1Dia(p.getFechaInicial(), p.getFechaFinal(), fechaFinal)) {
                    if (p.getCerrado()) {
                        msjHtmlPeriodo = "El periodo esta cerrado";
                        estiloPeriodo = "border-color: orange; border-width: 2px; border-style: solid; border-radius: 7px 7px 7px 7px;";
                    } else {

                        msjHtmlPeriodo = "Correcto: <br/>Periodo abierto";
                        estiloPeriodo = "";
                    }
                    periodoSeleccionado = p;
                    break;
                }
            }
            if (msjHtmlPeriodo.length() == 0) {
                msjHtmlPeriodo = "Se debe configurar <br/>un periodo para usar <br/>la fecha seleccionada";
                estiloPeriodo = "border-color: orange; border-width: 2px; border-style: solid; border-radius: 7px 7px 7px 7px;";
            }
        } else {
            msjHtmlPeriodo = "La fecha <br/>es erronea";
            estiloPeriodo = "border-color: orange; border-width: 2px; border-style: solid; border-radius: 7px 7px 7px 7px;";
        }
    }

    private void recalcularValorFactura() {
        double vlr = 0;
        for (FilaDataTable servicioFactura : listaServiciosFactura) {
            vlr = vlr + Double.parseDouble(servicioFactura.getColumna7());
            //System.out.println("Suma servicioFactura1:"+ servicioFactura.getColumna7()+"("+vlr+")");
        }
        for (FilaDataTable medicamentoFactura : listaMedicamentosFactura) {
            vlr = vlr + Double.parseDouble(medicamentoFactura.getColumna7());
            //System.out.println("Suma medicamentoFactura1:"+ medicamentoFactura.getColumna7()+"("+vlr+")");
        }
        for (FilaDataTable paqueteFactura : listaPaquetesFactura) {
            vlr = vlr + Double.parseDouble(paqueteFactura.getColumna7());
            //System.out.println("Suma paqueteFactura1:"+ paqueteFactura.getColumna7()+"("+vlr+")");
        }
        for (FilaDataTable insumoFactura : listaInsumosFactura) {
            vlr = vlr + Double.parseDouble(insumoFactura.getColumna6());
            //System.out.println("Suma insumoFactura1:"+ insumoFactura.getColumna6()+"("+vlr+")");
        }
        acumuladoParcial = vlr;

        acumuladoUsuario = acumuladoCopago + acumuladoCuotaModeradora;

        acumuladoEmpresa = acumuladoParcial - acumuladoUsuario + (acumuladoParcial * (acumuladoIva / 100)) + (acumuladoParcial * (acumuladoCree / 100));//vlr - acumuladoCopago - acumuladoCuotaModeradora;            

        acumuladoTotal = acumuladoUsuario + acumuladoEmpresa;

    }

    public void limpiarFormularioFacturacion() {
        //System.out.println("limpiarFormularioFacturacion");
        //renderedTablaFacturas = false;
        idAdministradora = "";
        idContrato = "";
        listaContratos = new ArrayList<>();
        periodoSeleccionado = null;
        msjHtmlPeriodo = "No exite periodo <br/>para esta fecha";
        estiloPeriodo = "";
        msjHtmlDocumento = "No hay documento"; //me dice si el periodo esta: Abierto, Cerrado, Sin Crear
        estiloDocumento = "";

        observaciones = "";//observaciones de la factura
        observacionAnulacion = "";
        acumuladoIva = 0;//sumatoria de los ivas cobrados por cada item
        acumuladoCree = 0;//porcentage correspondiente al cree
        acumuladoUsuario = 0;//sumatoria de los valores que paga por usuario
        acumuladoEmpresa = 0;//sumatoria de los valores que pagara la aempresa
        acumuladoParcial = 0;//sumatoria de los valores parciales
        acumuladoTotal = 0;
        acumuladoCopago = 0;//sumatoria de los copagos(hay algunos con cero si pertenecian a un paquete de citas)
        acumuladoCuotaModeradora = 0;//sumatoria de los copagos(hay algunos con cero si pertenecian a un paquete de citas)

        //------- TABVIEW FACTURA --------------    
        tituloTabInsumosFactura = "Insumos (0)";
        tituloTabServiciosFactura = "Servicios (0)";
        tituloTabMedicamentosFactura = "Medicamentos (0)";
        tituloTabPaquetesFactura = "Paquetes (0)";
        listaServiciosFactura = new ArrayList<>();
        listaInsumosFactura = new ArrayList<>();
        listaMedicamentosFactura = new ArrayList<>();
        listaPaquetesFactura = new ArrayList<>();
        listaServiciosFacturaFiltro = new ArrayList<>();
        listaInsumosFacturaFiltro = new ArrayList<>();
        listaMedicamentosFacturaFiltro = new ArrayList<>();
        listaPaquetesFacturaFiltro = new ArrayList<>();

    }

    public void cambiaAdministradora() {
        //se recarga la lista de contratos que contenga
        listaContratos = new ArrayList<>();
        List<FacContrato> listaContratosAux = new ArrayList<>();
        contratoActual = null;
        idContrato = "";
        if (!validarNoVacio(idAdministradora)) {
            imprimirMensaje("Error", "Se debe seleccionar una administradora", FacesMessage.SEVERITY_ERROR);
            buscarItems();
            RequestContext.getCurrentInstance().execute("remoteCommand();");
            return;
        }
        administradoraActual = administradoraFacade.find(Integer.parseInt(idAdministradora));
        listaContratos = administradoraActual.getFacContratoList();
        if (!listaContratos.isEmpty()) {
            listaContratos.add(contratoTodos);//agrega el item TODOS
            contratoActual = listaContratos.get(0);
            idContrato = contratoActual.getIdContrato().toString();
        } else {
            contratoActual = null;
            idContrato = "";
        }
        //recalcularValorFactura();
        buscarItems();
        RequestContext.getCurrentInstance().execute("remoteCommand();");
    }

    public void cambiaContrato() {
        if (validarNoVacio(idContrato)) {
            if (idContrato.compareTo("-1") == 0) {
                contratoActual = contratoTodos;
            } else {
                contratoActual = contratoFacade.find(Integer.parseInt(idContrato));
            }
        } else {
            contratoActual = null;
        }
        buscarItems();
        RequestContext.getCurrentInstance().execute("remoteCommand();");
    }

    private boolean cargarFuenteDeDatosAgrupada() {
        if (facturaAdmiSeleccionadaTabla != null) {
            facturaAdmiSeleccionada = facturaAdmiFacade.find(Integer.parseInt(facturaAdmiSeleccionadaTabla.getColumna1()));
        } else {
            return false;
        }
        //String tipoDocumentoFacturaSeleccionada = facturaAdmiSeleccionada.getTipoDocumento().getDescripcion();//determinar si se imprime media o pagina completa
        List<EstructuraItemsAdministradora> listaItemsFactura = new ArrayList<>();
        listaRegistrosParaPdf = new ArrayList<>();
        EstructuraFacturaAdministradora nuevaFactura = new EstructuraFacturaAdministradora();
        nuevaFactura.setTituloFactura(loginMB.getEmpresaActual().getRazonSocial());
        nuevaFactura.setRegimenEmpresa(loginMB.getEmpresaActual().getRegimen());
        nuevaFactura.setNitEmpresa("NIT. " + loginMB.getEmpresaActual().getNumIdentificacion());
        nuevaFactura.setTipoDocumento(" FACTURA No.");
        nuevaFactura.setCodigoDocumento("" + facturaAdmiSeleccionada.getCodigoDocumento());
        nuevaFactura.setSubtituloFactura(loginMB.getEmpresaActual().getDireccion() + " - Tel1: " + loginMB.getEmpresaActual().getTelefono1() + " - Tel2: " + loginMB.getEmpresaActual().getTelefono2() + " - " + loginMB.getEmpresaActual().getWebsite());
        nuevaFactura.setClienteNombre("<b>NOMBRE: </b>" + facturaAdmiSeleccionada.getIdAdministradora().getRazonSocial());
        nuevaFactura.setClienteDireccion("<b>DIRECCION: </b>" + facturaAdmiSeleccionada.getIdAdministradora().getDireccion());
        nuevaFactura.setClienteIdentificacion("<b>IDENTIFICACION: </b>" + facturaAdmiSeleccionada.getIdAdministradora().getNumeroIdentificacion());
        nuevaFactura.setClienteTelefono("<b>TELEFONO: </b>" + facturaAdmiSeleccionada.getIdAdministradora().getTelefono1() + " <b>CELULAR: </b>" + facturaAdmiSeleccionada.getIdAdministradora().getTelefono2());
        nuevaFactura.setClienteCiudad("<b>CIUDAD: </b>" + "-");
        nuevaFactura.setFechaFactura("<b>FECHA FACTURA: </b>" + formateadorFecha.format(facturaAdmiSeleccionada.getFechaElaboracion()));
        nuevaFactura.setFechaVencimiento("<b>FECHA VENCIMIENTO: </b>" + "-");
        //nuevaFactura.setGravadas("GRAVADA");
        //nuevaFactura.setNoGravadas("NO GRAVADA");
        nuevaFactura.setObservaciones(facturaAdmiSeleccionada.getObservacion());
        nuevaFactura.setSubtotal(formateadorDecimal.format(facturaAdmiSeleccionada.getValorParcial()));
        //nuevaFactura.setIva(formateadorDecimal.format(facturaAdmiSeleccionada.getIva() * facturaAdmiSeleccionada.getValorParcial() / 100));
        nuevaFactura.setTituloIva("IVA: ");
        nuevaFactura.setIva(formateadorDecimal.format(facturaAdmiSeleccionada.getValoresIva()) + "");
        nuevaFactura.setTituloCree("CREE: ");
        nuevaFactura.setCree(formateadorDecimal.format(facturaAdmiSeleccionada.getValoresCree()));

        //nuevaFactura.setCree(formateadorDecimal.format(facturaAdmiSeleccionada.getCree() * facturaAdmiSeleccionada.getValorParcial() / 100));
        nuevaFactura.setMenosCuotaModeradora(formateadorDecimal.format(facturaAdmiSeleccionada.getValoresCuotaModeradora()));
        nuevaFactura.setMenosCopago(formateadorDecimal.format(facturaAdmiSeleccionada.getValoresCopago()));
        String t = formateadorDecimal.format(facturaAdmiSeleccionada.getValorTotal());
        t = t.replace(".", ",");
        if (t.contains(",")) {
            t = t.split(",")[0];
        }
        nuevaFactura.setTotal(formateadorDecimal.format(facturaAdmiSeleccionada.getValorTotal()));
        nuevaFactura.setSon("<b>SON: </b>" + ConversorDeNumerosALetras.convertNumberToLetter(t));
        nuevaFactura.setFirmaAutoriza(loginMB.getRutaCarpetaImagenes() + loginMB.getEmpresaActual().getLogo().getUrlImagen());
        nuevaFactura.setLogoEmpresa(loginMB.getRutaCarpetaImagenes() + loginMB.getEmpresaActual().getLogo().getUrlImagen());
        boolean itemEncontrado;
        //Como el valor unitario puede cambiar de una factura a otra no se usa valor unitario        
        for (FacFacturaPaciente facturaPaciente : facturaAdmiSeleccionada.getFacFacturaPacienteList()) {
            for (FacFacturaServicio servicioFactura : facturaPaciente.getFacFacturaServicioList()) {
                itemEncontrado = false;
                for (EstructuraItemsAdministradora itemFactura : listaItemsFactura) {//agrupar si el servicio ya estaba en la factura
                    if (itemFactura.getTipoItem().compareTo("Servicio") == 0) {
                        if (itemFactura.getIdentificadorItem().compareTo(servicioFactura.getIdServicio().getIdServicio().toString()) == 0) {
                            itemEncontrado = true;
                            itemFactura.setCantidadInt(itemFactura.getCantidadInt() + servicioFactura.getCantidadServicio());
                            itemFactura.setCantidad(String.valueOf(itemFactura.getCantidadInt()));
                            itemFactura.setValorTotalDouble(itemFactura.getValorTotalDouble() + servicioFactura.getValorParcial());
                            itemFactura.setValorTotal(formateadorDecimal.format(itemFactura.getValorTotalDouble()));
                            break;
                        }
                    }
                }
                if (!itemEncontrado) {//agregar si el servicio no estaba en la factura
                    EstructuraItemsAdministradora nuevoItem = new EstructuraItemsAdministradora();
                    nuevoItem.setTipoItem("Servicio");
                    nuevoItem.setCantidad(servicioFactura.getCantidadServicio().toString());
                    nuevoItem.setCantidadInt(servicioFactura.getCantidadServicio());
                    nuevoItem.setDescripcion(servicioFactura.getIdServicio().getNombreServicio());
                    nuevoItem.setValorTotal(formateadorDecimal.format(servicioFactura.getValorParcial()));
                    nuevoItem.setValorTotalDouble(servicioFactura.getValorParcial());
                    nuevoItem.setIdentificadorItem(servicioFactura.getIdServicio().getIdServicio().toString());
                    listaItemsFactura.add(nuevoItem);
                }
            }
            for (FacFacturaMedicamento medicamentoFactura : facturaPaciente.getFacFacturaMedicamentoList()) {
                itemEncontrado = false;
                for (EstructuraItemsAdministradora itemFactura : listaItemsFactura) {//agrupar si el servicio ya estaba en la factura
                    if (itemFactura.getTipoItem().compareTo("Medicamento") == 0) {
                        if (itemFactura.getIdentificadorItem().compareTo(medicamentoFactura.getIdMedicamento().getIdMedicamento().toString()) == 0) {
                            itemEncontrado = true;
                            itemFactura.setCantidadInt(itemFactura.getCantidadInt() + medicamentoFactura.getCantidadMedicamento());
                            itemFactura.setCantidad(String.valueOf(itemFactura.getCantidadInt()));
                            itemFactura.setValorTotalDouble(itemFactura.getValorTotalDouble() + medicamentoFactura.getValorParcial());
                            itemFactura.setValorTotal(formateadorDecimal.format(itemFactura.getValorTotalDouble()));
                            break;
                        }
                    }
                }
                if (!itemEncontrado) {//agregar si el servicio no estaba en la factura
                    EstructuraItemsAdministradora nuevoItem = new EstructuraItemsAdministradora();
                    nuevoItem.setTipoItem("Medicamento");
                    nuevoItem.setCantidad(medicamentoFactura.getCantidadMedicamento().toString());
                    nuevoItem.setCantidadInt(medicamentoFactura.getCantidadMedicamento());
                    nuevoItem.setDescripcion(medicamentoFactura.getIdMedicamento().getNombreMedicamento());
                    nuevoItem.setValorTotal(formateadorDecimal.format(medicamentoFactura.getValorParcial()));
                    nuevoItem.setValorTotalDouble(medicamentoFactura.getValorParcial());
                    nuevoItem.setIdentificadorItem(medicamentoFactura.getIdMedicamento().getIdMedicamento().toString());
                    listaItemsFactura.add(nuevoItem);
                }
            }
            for (FacFacturaInsumo insumoFactura : facturaPaciente.getFacFacturaInsumoList()) {
                itemEncontrado = false;
                for (EstructuraItemsAdministradora itemFactura : listaItemsFactura) {//agrupar si el servicio ya estaba en la factura
                    if (itemFactura.getTipoItem().compareTo("Medicamento") == 0) {
                        if (itemFactura.getIdentificadorItem().compareTo(insumoFactura.getIdInsumo().getIdInsumo().toString()) == 0) {
                            itemEncontrado = true;
                            itemFactura.setCantidadInt(itemFactura.getCantidadInt() + insumoFactura.getCantidadInsumo());
                            itemFactura.setCantidad(String.valueOf(itemFactura.getCantidadInt()));
                            itemFactura.setValorTotalDouble(itemFactura.getValorTotalDouble() + insumoFactura.getValorParcial());
                            itemFactura.setValorTotal(formateadorDecimal.format(itemFactura.getValorTotalDouble()));
                            break;
                        }
                    }
                }
                if (!itemEncontrado) {//agregar si el servicio no estaba en la factura
                    EstructuraItemsAdministradora nuevoItem = new EstructuraItemsAdministradora();
                    nuevoItem.setTipoItem("Insumo");
                    nuevoItem.setCantidad(insumoFactura.getCantidadInsumo().toString());
                    nuevoItem.setCantidadInt(insumoFactura.getCantidadInsumo());
                    nuevoItem.setDescripcion(insumoFactura.getIdInsumo().getNombreInsumo());
                    nuevoItem.setValorTotal(formateadorDecimal.format(insumoFactura.getValorParcial()));
                    nuevoItem.setValorTotalDouble(insumoFactura.getValorParcial());
                    nuevoItem.setIdentificadorItem(insumoFactura.getIdInsumo().getIdInsumo().toString());
                    listaItemsFactura.add(nuevoItem);
                }
            }
            for (FacFacturaPaquete paqueteFactura : facturaPaciente.getFacFacturaPaqueteList()) {
                itemEncontrado = false;
                for (EstructuraItemsAdministradora itemFactura : listaItemsFactura) {//agrupar si el servicio ya estaba en la factura
                    if (itemFactura.getTipoItem().compareTo("Paquete") == 0) {
                        if (itemFactura.getIdentificadorItem().compareTo(paqueteFactura.getIdPaquete().getIdPaquete().toString()) == 0) {
                            itemEncontrado = true;
                            itemFactura.setCantidadInt(itemFactura.getCantidadInt() + paqueteFactura.getCantidadPaquete());
                            itemFactura.setCantidad(String.valueOf(itemFactura.getCantidadInt()));
                            itemFactura.setValorTotalDouble(itemFactura.getValorTotalDouble() + paqueteFactura.getValorParcial());
                            itemFactura.setValorTotal(formateadorDecimal.format(itemFactura.getValorTotalDouble()));
                            break;
                        }
                    }
                }
                if (!itemEncontrado) {//agregar si el servicio no estaba en la factura
                    EstructuraItemsAdministradora nuevoItem = new EstructuraItemsAdministradora();
                    nuevoItem.setTipoItem("Medicamento");
                    nuevoItem.setCantidad(paqueteFactura.getCantidadPaquete().toString());
                    nuevoItem.setCantidadInt(paqueteFactura.getCantidadPaquete());
                    nuevoItem.setDescripcion(paqueteFactura.getIdPaquete().getNombrePaquete());
                    nuevoItem.setValorTotal(formateadorDecimal.format(paqueteFactura.getValorParcial()));
                    nuevoItem.setValorTotalDouble(paqueteFactura.getValorParcial());
                    nuevoItem.setIdentificadorItem(paqueteFactura.getIdPaquete().getIdPaquete().toString());
                    listaItemsFactura.add(nuevoItem);
                }
            }
        }
        nuevaFactura.setListaItemsFactura(listaItemsFactura);
        listaRegistrosParaPdf.add(nuevaFactura);
        return true;
    }

    private boolean cargarFuenteDeDatosDetallada() {
        if (facturaAdmiSeleccionadaTabla != null) {
            facturaAdmiSeleccionada = facturaAdmiFacade.find(Integer.parseInt(facturaAdmiSeleccionadaTabla.getColumna1()));
        } else {
            return false;
        }
        //String tipoDocumentoFacturaSeleccionada = facturaAdmiSeleccionada.getTipoDocumento().getDescripcion();//determinar si se imprime media o pagina completa

        List<EstructuraItemsAdministradora> listaItemsFactura = new ArrayList<>();
        listaRegistrosParaPdf = new ArrayList<>();
        EstructuraFacturaAdministradora nuevaFactura = new EstructuraFacturaAdministradora();
        nuevaFactura.setTituloFactura(loginMB.getEmpresaActual().getRazonSocial());
        nuevaFactura.setRegimenEmpresa(loginMB.getEmpresaActual().getRegimen());
        nuevaFactura.setNitEmpresa("NIT. " + loginMB.getEmpresaActual().getNumIdentificacion());
        nuevaFactura.setTipoDocumento(" FACTURA No.");
        nuevaFactura.setCodigoDocumento("" + facturaAdmiSeleccionada.getCodigoDocumento());
        nuevaFactura.setSubtituloFactura(loginMB.getEmpresaActual().getDireccion() + " - Tel1: " + loginMB.getEmpresaActual().getTelefono1() + " - Tel2: " + loginMB.getEmpresaActual().getTelefono2() + " - " + loginMB.getEmpresaActual().getWebsite());
        nuevaFactura.setClienteNombre("<b>NOMBRE: </b>" + facturaAdmiSeleccionada.getIdAdministradora().getRazonSocial());
        nuevaFactura.setClienteDireccion("<b>DIRECCION: </b>" + facturaAdmiSeleccionada.getIdAdministradora().getDireccion());
        nuevaFactura.setClienteIdentificacion("<b>IDENTIFICACION: </b>" + facturaAdmiSeleccionada.getIdAdministradora().getNumeroIdentificacion());
        nuevaFactura.setClienteTelefono("<b>TELEFONO: </b>" + facturaAdmiSeleccionada.getIdAdministradora().getTelefono1() + " <b>CELULAR: </b>" + facturaAdmiSeleccionada.getIdAdministradora().getTelefono2());
        nuevaFactura.setClienteCiudad("<b>CIUDAD: </b>" + "-");
        nuevaFactura.setFechaFactura("<b>FECHA FACTURA: </b>" + formateadorFecha.format(facturaAdmiSeleccionada.getFechaElaboracion()));
        nuevaFactura.setFechaVencimiento("<b>FECHA VENCIMIENTO: </b>" + "-");
        //nuevaFactura.setGravadas("GRAVADA");
        //nuevaFactura.setNoGravadas("NO GRAVADA");
        nuevaFactura.setObservaciones(facturaAdmiSeleccionada.getObservacion());
        nuevaFactura.setSubtotal(formateadorDecimal.format(facturaAdmiSeleccionada.getValorParcial()));
        //nuevaFactura.setIva(formateadorDecimal.format(facturaAdmiSeleccionada.getIva() * facturaAdmiSeleccionada.getValorParcial() / 100));
        nuevaFactura.setTituloIva("IVA: ");
        nuevaFactura.setIva(formateadorDecimal.format(facturaAdmiSeleccionada.getValoresIva()) + "");
        nuevaFactura.setTituloCree("CREE: ");
        nuevaFactura.setCree(formateadorDecimal.format(facturaAdmiSeleccionada.getValoresCree()));
        //nuevaFactura.setCree(formateadorDecimal.format(facturaAdmiSeleccionada.getCree() * facturaAdmiSeleccionada.getValorParcial() / 100));

        nuevaFactura.setMenosCuotaModeradora(formateadorDecimal.format(facturaAdmiSeleccionada.getValoresCuotaModeradora()));
        nuevaFactura.setMenosCopago(formateadorDecimal.format(facturaAdmiSeleccionada.getValoresCopago()));
        String t = formateadorDecimal.format(facturaAdmiSeleccionada.getValorTotal());
        t = t.replace(".", ",");
        if (t.contains(",")) {
            t = t.split(",")[0];
        }
        nuevaFactura.setTotal(formateadorDecimal.format(facturaAdmiSeleccionada.getValorTotal()));
        nuevaFactura.setSon("<b>SON: </b>" + ConversorDeNumerosALetras.convertNumberToLetter(t));
        nuevaFactura.setFirmaAutoriza(loginMB.getRutaCarpetaImagenes() + loginMB.getEmpresaActual().getLogo().getUrlImagen());
        nuevaFactura.setLogoEmpresa(loginMB.getRutaCarpetaImagenes() + loginMB.getEmpresaActual().getLogo().getUrlImagen());

        for (FacFacturaPaciente facturaPaciente : facturaAdmiSeleccionada.getFacFacturaPacienteList()) {
            for (FacFacturaServicio servicioFactura : facturaPaciente.getFacFacturaServicioList()) {
                EstructuraItemsAdministradora nuevoItem = new EstructuraItemsAdministradora();
                nuevoItem.setCantidad(servicioFactura.getCantidadServicio().toString());
                nuevoItem.setDescripcion(servicioFactura.getIdServicio().getNombreServicio());
                nuevoItem.setValorUnitario(formateadorDecimal.format(servicioFactura.getValorServicio()));
                nuevoItem.setValorTotal(formateadorDecimal.format(servicioFactura.getValorParcial()));
                listaItemsFactura.add(nuevoItem);
            }
            for (FacFacturaMedicamento medicamentoFactura : facturaPaciente.getFacFacturaMedicamentoList()) {
                EstructuraItemsAdministradora nuevoItem = new EstructuraItemsAdministradora();
                nuevoItem.setCantidad(medicamentoFactura.getCantidadMedicamento().toString());
                nuevoItem.setDescripcion(medicamentoFactura.getIdMedicamento().getNombreMedicamento());
                nuevoItem.setValorUnitario(formateadorDecimal.format(medicamentoFactura.getValorMedicamento()));
                nuevoItem.setValorTotal(formateadorDecimal.format(medicamentoFactura.getValorParcial()));
                listaItemsFactura.add(nuevoItem);
            }
            for (FacFacturaInsumo insumoFactura : facturaPaciente.getFacFacturaInsumoList()) {
                EstructuraItemsAdministradora nuevoItem = new EstructuraItemsAdministradora();
                nuevoItem.setCantidad(insumoFactura.getCantidadInsumo().toString());
                nuevoItem.setDescripcion(insumoFactura.getIdInsumo().getNombreInsumo());
                nuevoItem.setValorUnitario(formateadorDecimal.format(insumoFactura.getValorInsumo()));
                nuevoItem.setValorTotal(formateadorDecimal.format(insumoFactura.getValorParcial()));
                listaItemsFactura.add(nuevoItem);
            }
            for (FacFacturaPaquete paqueteFactura : facturaPaciente.getFacFacturaPaqueteList()) {
                EstructuraItemsAdministradora nuevoItem = new EstructuraItemsAdministradora();
                nuevoItem.setCantidad(paqueteFactura.getCantidadPaquete().toString());
                nuevoItem.setDescripcion(paqueteFactura.getIdPaquete().getNombrePaquete());
                nuevoItem.setValorUnitario(formateadorDecimal.format(paqueteFactura.getValorPaquete()));
                nuevoItem.setValorTotal(formateadorDecimal.format(paqueteFactura.getValorParcial()));
                listaItemsFactura.add(nuevoItem);
            }
        }
        nuevaFactura.setListaItemsFactura(listaItemsFactura);
        listaRegistrosParaPdf.add(nuevaFactura);
        return true;
    }

    public void generarPdfAgrupada() throws JRException, IOException {//genera un pdf de una historia seleccionada en el historial        
        if (cargarFuenteDeDatosAgrupada()) {//si se puede cargar datos continuo
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listaRegistrosParaPdf);
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
                httpServletResponse.setContentType("application/pdf");
                ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                JasperPrint jasperPrint = JasperFillManager.fillReport(servletContext.getRealPath("facturacion/reportes/facturaAdministradoraAgrupada.jasper"), new HashMap(), beanCollectionDataSource);
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
                FacesContext.getCurrentInstance().responseComplete();
            }
        }
    }

    public void generarPdfDetallada() throws JRException, IOException {//genera un pdf de una historia seleccionada en el historial        
        if (cargarFuenteDeDatosDetallada()) {//si se puede cargar datos continuo
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listaRegistrosParaPdf);
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
                httpServletResponse.setContentType("application/pdf");
                ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                JasperPrint jasperPrint = JasperFillManager.fillReport(servletContext.getRealPath("facturacion/reportes/facturaAdministradoraDetallada.jasper"), new HashMap(), beanCollectionDataSource);
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
                FacesContext.getCurrentInstance().responseComplete();
            }
        }
    }

    public void seleccionFilaBuscarFactura() {
        disableControlesBuscarFactura = facturaAdmiSeleccionadaTabla == null;
    }

    public void anularFactura() {
        if (facturaAdmiSeleccionadaTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ninguna factura", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoAnularFactura').show();");
    }

    public void confirmarAnularFactura() {
        if (facturaAdmiSeleccionadaTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún factura", FacesMessage.SEVERITY_ERROR);
            return;
        }
        facturaAdmiSeleccionada = facturaAdmiFacade.find(Integer.parseInt(facturaAdmiSeleccionadaTabla.getColumna1()));
        facturaAdmiSeleccionada.setAnulada(true);
        facturaAdmiSeleccionada.setObservacionAnulacion(observacionAnulacion);
        facturaAdmiFacade.edit(facturaAdmiSeleccionada);
        imprimirMensaje("Correcto", "La facrura ha sido anulada", FacesMessage.SEVERITY_INFO);
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------
    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public LoginMB getLoginMB() {
        return loginMB;
    }

    public void setLoginMB(LoginMB loginMB) {
        this.loginMB = loginMB;
    }

    public String getIdAdministradora() {
        return idAdministradora;
    }

    public void setIdAdministradora(String idAdministradora) {
        this.idAdministradora = idAdministradora;
    }

    public List<FacContrato> getListaContratos() {
        return listaContratos;
    }

    public void setListaContratos(List<FacContrato> listaContratos) {
        this.listaContratos = listaContratos;
    }

    public String getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(String idContrato) {
        this.idContrato = idContrato;
    }

    public LazyDataModel<FilaDataTable> getListaFacturas() {
        return listaFacturas;
    }

    public void setListaFacturas(LazyDataModel<FilaDataTable> listaFacturas) {
        this.listaFacturas = listaFacturas;
    }

    public FilaDataTable getFacturaSeleccionadaTabla() {
        return facturaAdmiSeleccionadaTabla;
    }

    public void setFacturaSeleccionadaTabla(FilaDataTable facturaSeleccionadaTabla) {
        this.facturaAdmiSeleccionadaTabla = facturaSeleccionadaTabla;
    }

    public boolean isDisableControlesBuscarFactura() {
        return disableControlesBuscarFactura;
    }

    public void setDisableControlesBuscarFactura(boolean disableControlesBuscarFactura) {
        this.disableControlesBuscarFactura = disableControlesBuscarFactura;
    }

    public String getObservacionAnulacion() {
        return observacionAnulacion;
    }

    public void setObservacionAnulacion(String observacionAnulacion) {
        this.observacionAnulacion = observacionAnulacion;
    }

    public String getMsjHtmlPeriodo() {
        return msjHtmlPeriodo;
    }

    public void setMsjHtmlPeriodo(String msjHtmlPeriodo) {
        this.msjHtmlPeriodo = msjHtmlPeriodo;
    }

    public String getEstiloPeriodo() {
        return estiloPeriodo;
    }

    public void setEstiloPeriodo(String estiloPeriodo) {
        this.estiloPeriodo = estiloPeriodo;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getMsjHtmlDocumento() {
        return msjHtmlDocumento;
    }

    public void setMsjHtmlDocumento(String msjHtmlDocumento) {
        this.msjHtmlDocumento = msjHtmlDocumento;
    }

    public String getEstiloDocumento() {
        return estiloDocumento;
    }

    public void setEstiloDocumento(String estiloDocumento) {
        this.estiloDocumento = estiloDocumento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFechaElaboracion() {
        return fechaElaboracion;
    }

    public void setFechaElaboracion(Date fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }

    public String getTituloTabServiciosFactura() {
        return tituloTabServiciosFactura;
    }

    public void setTituloTabServiciosFactura(String tituloTabServiciosFactura) {
        this.tituloTabServiciosFactura = tituloTabServiciosFactura;
    }

    public String getTituloTabInsumosFactura() {
        return tituloTabInsumosFactura;
    }

    public void setTituloTabInsumosFactura(String tituloTabInsumosFactura) {
        this.tituloTabInsumosFactura = tituloTabInsumosFactura;
    }

    public String getTituloTabMedicamentosFactura() {
        return tituloTabMedicamentosFactura;
    }

    public void setTituloTabMedicamentosFactura(String tituloTabMedicamentosFactura) {
        this.tituloTabMedicamentosFactura = tituloTabMedicamentosFactura;
    }

    public String getTituloTabPaquetesFactura() {
        return tituloTabPaquetesFactura;
    }

    public void setTituloTabPaquetesFactura(String tituloTabPaquetesFactura) {
        this.tituloTabPaquetesFactura = tituloTabPaquetesFactura;
    }

    public List<FilaDataTable> getListaServiciosFactura() {
        return listaServiciosFactura;
    }

    public void setListaServiciosFactura(List<FilaDataTable> listaServiciosFactura) {
        this.listaServiciosFactura = listaServiciosFactura;
    }

    public List<FilaDataTable> getListaInsumosFactura() {
        return listaInsumosFactura;
    }

    public void setListaInsumosFactura(List<FilaDataTable> listaInsumosFactura) {
        this.listaInsumosFactura = listaInsumosFactura;
    }

    public List<FilaDataTable> getListaMedicamentosFactura() {
        return listaMedicamentosFactura;
    }

    public void setListaMedicamentosFactura(List<FilaDataTable> listaMedicamentosFactura) {
        this.listaMedicamentosFactura = listaMedicamentosFactura;
    }

    public List<FilaDataTable> getListaPaquetesFactura() {
        return listaPaquetesFactura;
    }

    public void setListaPaquetesFactura(List<FilaDataTable> listaPaquetesFactura) {
        this.listaPaquetesFactura = listaPaquetesFactura;
    }

    public List<FilaDataTable> getListaServiciosFacturaFiltro() {
        return listaServiciosFacturaFiltro;
    }

    public void setListaServiciosFacturaFiltro(List<FilaDataTable> listaServiciosFacturaFiltro) {
        this.listaServiciosFacturaFiltro = listaServiciosFacturaFiltro;
    }

    public List<FilaDataTable> getListaInsumosFacturaFiltro() {
        return listaInsumosFacturaFiltro;
    }

    public void setListaInsumosFacturaFiltro(List<FilaDataTable> listaInsumosFacturaFiltro) {
        this.listaInsumosFacturaFiltro = listaInsumosFacturaFiltro;
    }

    public List<FilaDataTable> getListaMedicamentosFacturaFiltro() {
        return listaMedicamentosFacturaFiltro;
    }

    public void setListaMedicamentosFacturaFiltro(List<FilaDataTable> listaMedicamentosFacturaFiltro) {
        this.listaMedicamentosFacturaFiltro = listaMedicamentosFacturaFiltro;
    }

    public List<FilaDataTable> getListaPaquetesFacturaFiltro() {
        return listaPaquetesFacturaFiltro;
    }

    public void setListaPaquetesFacturaFiltro(List<FilaDataTable> listaPaquetesFacturaFiltro) {
        this.listaPaquetesFacturaFiltro = listaPaquetesFacturaFiltro;
    }

    public double getAcumuladoIva() {
        return acumuladoIva;
    }

    public void setAcumuladoIva(double acumuladoIva) {
        this.acumuladoIva = acumuladoIva;
    }

    public double getAcumuladoCree() {
        return acumuladoCree;
    }

    public void setAcumuladoCree(double acumuladoCree) {
        this.acumuladoCree = acumuladoCree;
    }

    public double getAcumuladoUsuario() {
        return acumuladoUsuario;
    }

    public void setAcumuladoUsuario(double acumuladoUsuario) {
        this.acumuladoUsuario = acumuladoUsuario;
    }

    public double getAcumuladoEmpresa() {
        return acumuladoEmpresa;
    }

    public void setAcumuladoEmpresa(double acumuladoEmpresa) {
        this.acumuladoEmpresa = acumuladoEmpresa;
    }

    public double getAcumuladoParcial() {
        return acumuladoParcial;
    }

    public void setAcumuladoParcial(double acumuladoParcial) {
        this.acumuladoParcial = acumuladoParcial;
    }

    public double getAcumuladoTotal() {
        return acumuladoTotal;
    }

    public void setAcumuladoTotal(double acumuladoTotal) {
        this.acumuladoTotal = acumuladoTotal;
    }

    public double getAcumuladoCopago() {
        return acumuladoCopago;
    }

    public void setAcumuladoCopago(double acumuladoCopago) {
        this.acumuladoCopago = acumuladoCopago;
    }

    public double getAcumuladoCuotaModeradora() {
        return acumuladoCuotaModeradora;
    }

    public void setAcumuladoCuotaModeradora(double acumuladoCuotaModeradora) {
        this.acumuladoCuotaModeradora = acumuladoCuotaModeradora;
    }

    public String getNotaFueraRango() {
        return notaFueraRango;
    }

    public void setNotaFueraRango(String notaFueraRango) {
        this.notaFueraRango = notaFueraRango;
    }

    public String getNotaNoCerradas() {
        return notaNoCerradas;
    }

    public void setNotaNoCerradas(String notaNoCerradas) {
        this.notaNoCerradas = notaNoCerradas;
    }

    public String getEstiloNotas() {
        return estiloNotas;
    }

    public void setEstiloNotas(String estiloNotas) {
        this.estiloNotas = estiloNotas;
    }

}
