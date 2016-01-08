package managedBeans.facturacion;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import beans.utilidades.MetodosGenerales;
import com.google.common.base.Strings;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import modelo.entidades.CfgDiagnostico;
import modelo.entidades.CfgEmpresa;
import modelo.entidades.FacAdministradora;
import modelo.entidades.FacContrato;
import modelo.entidades.FacFacturaPaciente;
import modelo.entidades.FacFacturaAdmi;
import modelo.entidades.FacFacturaInsumo;
import modelo.entidades.FacFacturaServicio;
import modelo.entidades.FacManualTarifarioServicio;
import modelo.entidades.HcDetalle;
import modelo.entidades.HcRegistro;
//import modelo.entidades.FacFacturaAdmiServicio;
import modelo.entidades.RipsAc;
import modelo.entidades.RipsAcPK;
import modelo.entidades.RipsAf;
import modelo.entidades.RipsAfPK;
import modelo.entidades.RipsAlmacenados;
import modelo.entidades.RipsAp;
import modelo.entidades.RipsApPK;
import modelo.entidades.RipsAt;
import modelo.entidades.RipsAtPK;
import modelo.entidades.RipsCt;
import modelo.entidades.RipsCtPK;
import modelo.entidades.RipsUs;
import modelo.entidades.RipsUsPK;
import modelo.fachadas.CfgDiagnosticoFacade;
import modelo.fachadas.CfgEmpresaFacade;
import modelo.fachadas.FacAdministradoraFacade;
import modelo.fachadas.FacContratoFacade;
import modelo.fachadas.FacFacturaAdmiFacade;
import modelo.fachadas.FacFacturaPacienteFacade;
import modelo.fachadas.HcDetalleFacade;
import modelo.fachadas.HcRegistroFacade;
import modelo.fachadas.RipsAcFacade;
import modelo.fachadas.RipsAfFacade;
import modelo.fachadas.RipsAlmacenadosFacade;
import modelo.fachadas.RipsApFacade;
import modelo.fachadas.RipsAtFacade;
import modelo.fachadas.RipsCtFacade;
import modelo.fachadas.RipsUsFacade;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "ripsMB")
@SessionScoped
public class RipsMB extends MetodosGenerales implements Serializable {

    //https://www.youtube.com/watch?v=Qwj_L9gnZvo    EXPLICACION RIP
    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    FacAdministradoraFacade administradoraFacade;
    @EJB
    FacContratoFacade contratoFacade;
    @EJB
    RipsAlmacenadosFacade ripsAlmacenadosFacade;
    @EJB
    RipsAcFacade ripsAcFacade;
    @EJB
    RipsAfFacade ripsAfFacade;
    @EJB
    HcRegistroFacade registroFacade;
    @EJB
    RipsApFacade ripsApFacade;
    @EJB
    RipsCtFacade ripsCtFacade;
    @EJB
    RipsUsFacade ripsUsFacade;
    @EJB
    RipsAtFacade ripsAtFacade;
    @EJB
    FacFacturaPacienteFacade facFacturaPacienteFacade;
    @EJB
    FacFacturaAdmiFacade facFacturaAdmiFacade;
    @EJB
    CfgDiagnosticoFacade diagnosticoFacade;
    @EJB
    CfgEmpresaFacade empresaFacade;
    @EJB
    FacFacturaAdmiFacade facturaAdmiFacade;
    @EJB
    HcDetalleFacade hcDetalleFacade;
    //---------------------------------------------------
    //-----------------ENTIDADES ------------------------
    //---------------------------------------------------
    private FacContrato contratoActual;
    private CfgEmpresa empresaActual;
    private FacAdministradora administradoraActual;
    private List<FacContrato> listaContratos;
    private List<RipsAlmacenados> listaRipsAlmacenados;
    private RipsAlmacenados ripSeleccionado;
    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------
    private String realPath = "";
    private boolean ripCT = true;
    private boolean ripAF = true;
    private boolean ripUS = true;
    private boolean ripAP = true;
    private boolean ripAC = true;
    private boolean ripAT = true;
    private boolean diagnosticos = true;

    private String nombreRIPS = "";
    private Date fechaCreacion = new Date();
    private String idAdministradora = "";
    private String idContrato = "";
    private Date fechaInicial = new Date();
    private Date fechaFinal = new Date();
    private final Calendar calendarFechaFinalMasUnDia = Calendar.getInstance();
    private final SimpleDateFormat formatoFechaSql = new SimpleDateFormat("dd/MM/yyyy", new Locale("ES"));
//    private final DecimalFormat formateadorDecimal = new DecimalFormat("0.00");//santos
    private final DecimalFormat formateadorDecimal = new DecimalFormat("#########");//santos
    private FacturarAdministradoraMB facturarAdministradoraMB;
    private boolean generarRipsDesdeAdministradora = false;
    private List<FacFacturaAdmi> listaFacturasAdministradora;
    private RipsAlmacenados ripAlmacenado;//si se esta creando rips desde administradora al final se elimina

    private FacFacturaAdmi facturaAdmiSeleccionada;

    //---------------------------------------------------
    //------------- FUNCIONES INICIALES  ----------------
    //---------------------------------------------------      
    @PostConstruct
    public void inicializar() {

        fechaInicial.setDate(1);
        fechaInicial.setMonth(calendarFechaFinalMasUnDia.get(Calendar.MONTH));
        fechaInicial.setYear(calendarFechaFinalMasUnDia.get(Calendar.YEAR) - 1900);

        fechaFinal.setDate(calendarFechaFinalMasUnDia.get(Calendar.DATE));
        fechaFinal.setMonth(calendarFechaFinalMasUnDia.get(Calendar.MONTH));
        fechaFinal.setYear(calendarFechaFinalMasUnDia.get(Calendar.YEAR) - 1900);
        empresaActual = empresaFacade.find(1);
        listaRipsAlmacenados = ripsAlmacenadosFacade.buscarOrdenado();
        facturarAdministradoraMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{facturarAdministradoraMB}", FacturarAdministradoraMB.class);
        nombreRIPS = "";
        cambiaAdministradora();
        ripCT = true;
        ripAF = true;
        ripUS = true;
        ripAP = true;
        ripAC = true;
        ripAT = true;
        diagnosticos = true;
        nombreRIPS = "";
        fechaCreacion = new Date();
    }

    public RipsMB() {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        realPath = (String) servletContext.getRealPath("/");
    }

    //---------------------------------------------------
    //-----------------FUNCIONES ------------------
    //---------------------------------------------------        
    public void cambiaNombreRip() {
        if (nombreRIPS != null && nombreRIPS.length() != 0 && ripsAlmacenadosFacade.buscarPorNombre(nombreRIPS) != null) {
            imprimirMensaje("Error", "Existen RIPS almacenados con este mismo nombre", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void cambiaAdministradora() {
        //se recarga la lista de contratos que contenga
        if (!validarNoVacio(idAdministradora)) {
            imprimirMensaje("Error", "Se debe seleccionar una administradora", FacesMessage.SEVERITY_ERROR);
            listaContratos = new ArrayList<>();
            return;
        }
        administradoraActual = administradoraFacade.find(Integer.parseInt(idAdministradora));
        listaContratos = administradoraActual.getFacContratoList();
        if (!listaContratos.isEmpty()) {
            contratoActual = listaContratos.get(0);
            idContrato = contratoActual.getIdContrato().toString();
        } else {
            contratoActual = null;
            idContrato = "";
        }
    }

    public void cambiaContrato() {
        if (validarNoVacio(idContrato)) {
            contratoActual = contratoFacade.find(Integer.parseInt(idContrato));
        } else {
            contratoActual = null;
        }
        //buscarItems();
    }

    private ArrayList<String> determinarDiagnosticos(FacFacturaPaciente facturaPaciente) {
        //se determinan los diagnosticos a partir de la factura o el ultimo registro que tenga el paciente

        //Para diagnósticos: busco la ultima historia clinica del paciente y saco los diagnosticos        
        ArrayList<String> arregloRetorno = new ArrayList<>();
        HcRegistro registroConDiagnostico = null;

        if (facturaPaciente.getIdCita() != null) {//se busca si la cita tiene registro asociado
            registroConDiagnostico = registroFacade.buscarRegistroConDiagnosticoSegunCita(facturaPaciente.getIdCita().getIdCita().toString());
        }
        if (registroConDiagnostico == null) {//se busca el ultimo registro del paciente
            registroConDiagnostico = registroFacade.buscarRegistroConDiagnosticoSegunPaciente(facturaPaciente.getIdPaciente().getIdPaciente().toString());
        }
        if (registroConDiagnostico == null) {//no se encontro un registro con diagnostico
            return null;
        }
        //si se encontro un registro saco los diagnostico en un arreglo
        arregloRetorno.add("");
        arregloRetorno.add("");
        arregloRetorno.add("");
        arregloRetorno.add("");
        List<HcDetalle> listaDetalleHC = hcDetalleFacade.buscarPorRegistroAndCampo(registroConDiagnostico);
        switch (registroConDiagnostico.getIdTipoReg().getIdTipoReg()) {
            case 1://1;"HISTORIA CLINICA DE OPTOMETRIA"   (4 diagnosticos)
                for (HcDetalle detalle : listaDetalleHC) {
                    try {
                        switch (detalle.getHcDetallePK().getIdCampo()) {
                            case 35://dx principal
                                arregloRetorno.set(0, detalle.getValor().split(" - ")[0]);
                                break;
                            case 1://dx rel_1
                                arregloRetorno.set(1, detalle.getValor().split(" - ")[0]);
                                break;
                            case 2://dx rel_2
                                arregloRetorno.set(2, detalle.getValor().split(" - ")[0]);
                                break;
                            case 37://dx rel_3
                                arregloRetorno.set(3, detalle.getValor().split(" - ")[0]);
                                break;
                        }
                    } catch (Exception e) {
                        System.out.println("Error al sacar codigo de diagnostico de: " + detalle.getValor());
                    }
                }
                break;
            case 2://2;"HISTORIA CLINICA ORTOPTICA " (4 diagnosticos)
                for (HcDetalle detalle : listaDetalleHC) {
                    try {
                        switch (detalle.getHcDetallePK().getIdCampo()) {
                            case 72://dx principal
                                arregloRetorno.set(0, detalle.getValor().split(" - ")[0]);
                                break;
                            case 57://dx rel_1
                                arregloRetorno.set(1, detalle.getValor().split(" - ")[0]);
                                break;
                            case 61://dx rel_2
                                arregloRetorno.set(2, detalle.getValor().split(" - ")[0]);
                                break;
                            case 70://dx rel_3
                                arregloRetorno.set(3, detalle.getValor().split(" - ")[0]);
                                break;
                        }
                    } catch (Exception e) {
                        System.out.println("Error al sacar codigo de diagnostico de: " + detalle.getValor());
                    }
                }
                break;
        }
        return arregloRetorno;
    }

    public void confirmarEliminarRips() {
        if (ripSeleccionado == null) {
            imprimirMensaje("Error", "Se debe seleccionar un registro de la tabla", FacesMessage.SEVERITY_ERROR);
        }
        for (RipsAc rAC : ripSeleccionado.getRipsAcList()) {
            ripsAcFacade.remove(rAC);
        }
        for (RipsAf rAF : ripSeleccionado.getRipsAfList()) {
            ripsAfFacade.remove(rAF);
        }
        for (RipsAp rAP : ripSeleccionado.getRipsApList()) {
            ripsApFacade.remove(rAP);
        }
        for (RipsCt rCT : ripSeleccionado.getRipsCtList()) {
            ripsCtFacade.remove(rCT);
        }
        for (RipsUs rUS : ripSeleccionado.getRipsUsList()) {
            ripsUsFacade.remove(rUS);
        }
        for (RipsAt rAT : ripSeleccionado.getRipsAtList()) {
            ripsAtFacade.remove(rAT);
        }
        ripsAlmacenadosFacade.remove(ripSeleccionado);
        ripSeleccionado = null;
        listaRipsAlmacenados = ripsAlmacenadosFacade.buscarOrdenado();
    }

    public void clickBtnGenerarRIPS() {
        ripAlmacenado = null;
        generarRipsDesdeAdministradora = false;
        generarRIPS();
    }

    private void generarRIPS() {//RIPS (C/U EN ARCHIVO INDEPENDIENTE) (No lleva Cabecera, solo datos,Delimitador COMA)
        String archivos = "";
        if (generarRipsDesdeAdministradora) {
            ripCT = true;
            ripAF = true;
            ripUS = true;
            ripAP = true;
            ripAC = true;
            ripAT = true;
            int idClasificacion = facturaAdmiSeleccionada.getClasificacion();
            if (idClasificacion == 1) {
                ripAT = false;
                archivos = "CT,AF,US,AP,AC,DIAGNOSTICOS";
            } else if (idClasificacion == 2) {
                ripAP = false;
                ripAC = false;
                archivos = "CT,AF,US,AT,DIAGNOSTICOS";
            } else {
                archivos = "CT,AF,US,AP,AC,AT,DIAGNOSTICOS";
            }
        } else {
            if (!validarNoVacio(nombreRIPS)) {
                imprimirMensaje("Error", "Se debe escribir el código del archivo", FacesMessage.SEVERITY_ERROR);
                return;
            }
            ripAlmacenado = ripsAlmacenadosFacade.buscarPorNombre(nombreRIPS);
            if (ripAlmacenado != null) {
                imprimirMensaje("Error", "Ya existen RIPS generados con este código de archivo", FacesMessage.SEVERITY_ERROR);
                return;
            }

            if (!validarNoVacio(idAdministradora)) {
                imprimirMensaje("Error", "Se debe seleccionar una administradora", FacesMessage.SEVERITY_ERROR);
                return;
            }
            if (!validarNoVacio(idContrato)) {
                imprimirMensaje("Error", "Se debe seleccionar un contrato", FacesMessage.SEVERITY_ERROR);
                return;
            }
            if (fechaInicial == null) {
                imprimirMensaje("Error", "La fecha inicial es obligatoria", FacesMessage.SEVERITY_ERROR);
                return;
            }
            if (fechaFinal == null) {
                imprimirMensaje("Error", "La fecha final es obligatoria", FacesMessage.SEVERITY_ERROR);
                return;
            }
            //determinar si se escogio como minimo un tipo de archivo para los rips
            if (ripCT) {
                archivos = archivos + " CT,";
            }
            if (ripAF) {
                archivos = archivos + " AF,";
            }
            if (ripUS) {
                archivos = archivos + " US,";
            }
            if (ripAP) {
                archivos = archivos + " AP,";
            }
            if (ripAC) {
                archivos = archivos + " AC,";
            }

            if (diagnosticos) {
                archivos = archivos + " DIAGNOSTICOS,";
            }
            if (archivos.length() == 0) {
                imprimirMensaje("Error", "No se ha seleccionado ningún archivo a generar", FacesMessage.SEVERITY_ERROR);
                return;
            } else {
                archivos = archivos.substring(0, archivos.length() - 1);
            }
        }

        //se buscan las facturas de administradoras 
        ripAlmacenado = new RipsAlmacenados();
        ripAlmacenado.setIdAdministradora(administradoraActual);
        ripAlmacenado.setNombre(nombreRIPS);
        ripAlmacenado.setFecha(new Date());
        ripAlmacenado.setArchivos(archivos);
        ripAlmacenado.setFechaInicial(fechaInicial);
        ripAlmacenado.setFechaFinal(fechaFinal);

        if (!generarRipsDesdeAdministradora) {
            String sql;
            if (idContrato.compareTo("-1") == 0) {//se busca en todos los contratos de la administradora
                sql = " SELECT * \n"
                        + " FROM fac_factura_admi \n"
                        + " WHERE \n"
                        + "   id_administradora = " + idAdministradora + " AND \n"
                        + "   anulada = false AND \n"
                        + "   fecha_inicial >= to_date('" + formatoFechaSql.format(fechaInicial) + "','dd/MM/yyyy') AND \n"
                        + "   fecha_inicial <= to_date('" + formatoFechaSql.format(fechaFinal) + "','dd/MM/yyyy') AND \n"
                        + "   fecha_final >= to_date('" + formatoFechaSql.format(fechaInicial) + "','dd/MM/yyyy') AND \n"
                        + "   fecha_final <= to_date('" + formatoFechaSql.format(fechaFinal) + "','dd/MM/yyyy')";
                listaFacturasAdministradora = facFacturaAdmiFacade.consultaNativaFacturas(sql);
            } else {//se busca solo en el contrato especificado por el usuario
                sql = " SELECT * \n"
                        + " FROM fac_factura_admi \n"
                        + " WHERE \n"
                        + "   id_administradora = " + idAdministradora + " AND \n"
                        + "   id_contrato = " + idContrato + " AND \n"
                        + "   anulada = false AND \n"
                        + "   fecha_inicial >= to_date('" + formatoFechaSql.format(fechaInicial) + "','dd/MM/yyyy') AND \n"
                        + "   fecha_inicial <= to_date('" + formatoFechaSql.format(fechaFinal) + "','dd/MM/yyyy') AND \n"
                        + "   fecha_final >= to_date('" + formatoFechaSql.format(fechaInicial) + "','dd/MM/yyyy') AND \n"
                        + "   fecha_final <= to_date('" + formatoFechaSql.format(fechaFinal) + "','dd/MM/yyyy')";
                listaFacturasAdministradora = facFacturaAdmiFacade.consultaNativaFacturas(sql);
                ripAlmacenado.setIdContrato(contratoFacade.find(Integer.parseInt(idContrato)));
            }
            //System.out.println("CONSULTA PARA DETERMINAR RIPS: \n" + sql);
            if (listaFacturasAdministradora == null || listaFacturasAdministradora.isEmpty()) {
                imprimirMensaje("Error", "No existen facturas de administradoras en el rango especificado", FacesMessage.SEVERITY_ERROR);
                return;
            }
        } else {
            //a listaFacturasAdministradora ya se cargo la necesaria para el rip desde cacturarAdministradora.xhtml
        }
        ripsAlmacenadosFacade.create(ripAlmacenado);//se almacena el conjunto de rips a almacenar
        int contadorCT = 0;
        int contadorAF = 0;
        int contadorUS = 0;
        int contadorAP = 0;
        int contadorAC = 0;
        int contadorAT = 0;
        boolean cuotaModeradoraCobrada;
        try {
            for (FacFacturaAdmi facturaActual : listaFacturasAdministradora) {
                for (FacFacturaPaciente facturaPaciente : facturaActual.getFacFacturaPacienteList()) {
                    if (ripAF) {//RIP facturas_y_fac_extramurales
                        contadorAF++;
                        RipsAfPK llave = new RipsAfPK(ripAlmacenado.getIdRipAlmacenado(), contadorAF);
                        RipsAf nuevoregistroRipAF = new RipsAf(llave);
                        nuevoregistroRipAF.setCodPre(empresaActual.getCodigoEmpresa());// codigo de empresa dado por el mimisterio (SOLICITARLO EN SECCION CONFIGURACION > EMPRESA)
                        nuevoregistroRipAF.setRazSoc(empresaActual.getRazonRip());
                        if (empresaActual.getTipoDoc().getDescripcion().length() > 2) {
                            nuevoregistroRipAF.setTipIde(empresaActual.getTipoDoc().getDescripcion().substring(0, 2));
                        } else {
                            nuevoregistroRipAF.setTipIde(empresaActual.getTipoDoc().getDescripcion());
                        }
                        nuevoregistroRipAF.setNumIde(empresaActual.identificacionMasDigitoVerificacion());
                        nuevoregistroRipAF.setNumFac(facturaPaciente.getCodigoDocumento());
                        nuevoregistroRipAF.setFecExp(formatoFechaSql.format(facturaActual.getFechaElaboracion()));//cuando se creo la factura
                        nuevoregistroRipAF.setFecInc(formatoFechaSql.format(facturaActual.getFechaInicial()));//fec_ini: desde cuando se esta Facturando
                        nuevoregistroRipAF.setFecFin(formatoFechaSql.format(facturaActual.getFechaFinal()));//fec_fin: hasta cuando se esta facturando
                        if (!facturaPaciente.getIdPaciente().getIdAdministradora().getCodigoRip().isEmpty()) {
                            nuevoregistroRipAF.setCodEnt(facturaPaciente.getIdPaciente().getIdAdministradora().getCodigoRip().substring(0, 6));//Código Entidad Administradora:el que se ingresa en fac_administradora > código_rip                
                        } else {
                            if (facturaPaciente.getIdPaciente().getIdAdministradora().getCodigoAdministradora().length() > 6) {
                                nuevoregistroRipAF.setCodEnt(administradoraActual.getCodigoAdministradora().substring(0, 6));
                            } else {
                                nuevoregistroRipAF.setCodEnt(administradoraActual.getCodigoAdministradora());
                            }
                        }

                        if (administradoraActual.getRazonSocial().length() > 29) {
                            nuevoregistroRipAF.setNomEnt(administradoraActual.getRazonSocial().substring(0, 29));
                        } else {
                            nuevoregistroRipAF.setNomEnt(administradoraActual.getRazonSocial());
                        }
                        nuevoregistroRipAF.setNumCon("");
                        nuevoregistroRipAF.setPlanBen("");//Plan de Beneficios: NO SE MANEJA VA VACIO
                        nuevoregistroRipAF.setNumPoli("");//Número de la póliza(SALE DE TABLA CONTRATO)
                        nuevoregistroRipAF.setValCopa(facturaPaciente.getCopago());
                        nuevoregistroRipAF.setValCom(Double.parseDouble("0"));//no aplica valor compartido
                        nuevoregistroRipAF.setValDesc(Double.parseDouble("0"));//no aplica descuentos
                        nuevoregistroRipAF.setValNet(facturaPaciente.getValorTotal());// Valor Neto a Pagar por la entidad Contratante: valor paga
                        ripsAfFacade.create(nuevoregistroRipAF);
                    }

                    if (ripAT) {//registro de datos para el archivo de otros servicios (insumos optica)
                        boolean continuar;
//                        for (FacFacturaPaciente facturaPaciente : facturaActual.getFacFacturaPacienteList()) {
                        for (FacFacturaInsumo insumoActual : facturaPaciente.getFacFacturaInsumoList()) {
                            continuar = true;
                            if (insumoActual.getIdInsumo().getCodigoInsumo() == null) {
                                continuar = false;
                                System.err.println("El insumo: " + insumoActual.getIdInsumo().getCodigoInsumo() + " - " + insumoActual.getIdInsumo().getNombreInsumo() + "no tiene codigo CUPS");
                            }
                            if (continuar && insumoActual.getIdInsumo().getCodigoInsumo().startsWith("8")) {
                                continuar = false;
                                if (!continuar && !insumoActual.getIdInsumo().getCodigoInsumo().startsWith("9")) {
                                    continuar = true;
                                } else {
                                    System.err.println("El codigo CUP(" + insumoActual.getIdInsumo().getCodigoInsumo() + ") no inicia ni con 9 ni con 8");
                                }
                            }
                            if (continuar) {
                                contadorAT++;
                                RipsAtPK llave = new RipsAtPK(ripAlmacenado.getIdRipAlmacenado(), contadorAT);
                                RipsAt nuevoRegistroRipAt = new RipsAt(llave);
                                nuevoRegistroRipAt.setNumFac(facturaPaciente.getCodigoDocumento());
                                nuevoRegistroRipAt.setCodPre(empresaActual.getCodigoEmpresa());
                                if (facturaPaciente.getIdPaciente().getTipoIdentificacion() != null) {
                                    nuevoRegistroRipAt.setTipIde(facturaPaciente.getIdPaciente().getTipoIdentificacion().getDescripcion());
                                } else {
                                    nuevoRegistroRipAt.setTipIde("");
                                }
                                nuevoRegistroRipAt.setNumIde(facturaPaciente.getIdPaciente().getIdentificacion());
                                if (facturaPaciente.getIdCita() != null && facturaPaciente.getIdCita().getNumAutorizacion() != null) {
                                    nuevoRegistroRipAt.setNumAut(facturaPaciente.getIdCita().getNumAutorizacion());
                                } else {
                                    nuevoRegistroRipAt.setNumAut("");
                                }
                                nuevoRegistroRipAt.setTipSer("1");
                                nuevoRegistroRipAt.setCodSer(insumoActual.getIdInsumo().getCodigoInsumo());
                                nuevoRegistroRipAt.setNomSer(insumoActual.getIdInsumo().getNombreInsumo());
                                nuevoRegistroRipAt.setCantidad(String.valueOf(insumoActual.getCantidadInsumo()));
                                nuevoRegistroRipAt.setVrUni(String.valueOf(formateadorDecimal.format(insumoActual.getValorInsumo()).replace(",", ".")));
                                nuevoRegistroRipAt.setVrTot(String.valueOf(formateadorDecimal.format(insumoActual.getValorParcial()).replace(",", ".")));
                                nuevoRegistroRipAt.setRipsAlmacenados(ripAlmacenado);
                                ripsAtFacade.create(nuevoRegistroRipAt);
                            }
                        }
                    }

                    if (ripUS) {//registro de datos para el archivo de usuarios de los servicios de salud
                        RipsUs ripUsBuscado = ripsUsFacade.buscarPorIdAlmacenadoYIdentificacion(ripAlmacenado.getIdRipAlmacenado(), facturaPaciente.getIdPaciente().getIdentificacion());
                        if (ripUsBuscado == null) {//no se encuentra en el archivo de usuarios por lo que debe ser agregado
                            contadorUS++;
                            RipsUsPK llave = new RipsUsPK(ripAlmacenado.getIdRipAlmacenado(), contadorUS);
                            RipsUs nuevoregistroRipUS = new RipsUs(llave);
                            nuevoregistroRipUS.setTipIde(facturaPaciente.getIdPaciente().getTipoIdentificacion().getDescripcion());
                            nuevoregistroRipUS.setNumIde(facturaPaciente.getIdPaciente().getIdentificacion());
                            if (!facturaPaciente.getIdPaciente().getIdAdministradora().getCodigoRip().isEmpty()) {
                                nuevoregistroRipUS.setCodEntAdm(facturaPaciente.getIdPaciente().getIdAdministradora().getCodigoRip().substring(0, 6));//Código Entidad Administradora:el que se ingresa en fac_administradora > código_rip                
                            } else {
                                if (facturaPaciente.getIdPaciente().getIdAdministradora().getCodigoAdministradora().length() > 6) {
                                    nuevoregistroRipUS.setCodEntAdm(administradoraActual.getCodigoAdministradora().substring(0, 6));
                                } else {
                                    nuevoregistroRipUS.setCodEntAdm(administradoraActual.getCodigoAdministradora());
                                }
                            }
                            if (facturaPaciente.getIdPaciente().getRegimen() != null) {
                                nuevoregistroRipUS.setTipUsu(facturaPaciente.getIdPaciente().getRegimen().getCodigo());//Tipo de Usuario: cfg_pacientes > régimen
                            } else {
                                nuevoregistroRipUS.setTipUsu("");
                            }
                            nuevoregistroRipUS.setPriNom(facturaPaciente.getIdPaciente().getPrimerNombre());
                            nuevoregistroRipUS.setSegNom(facturaPaciente.getIdPaciente().getSegundoNombre());
                            nuevoregistroRipUS.setPriApe(facturaPaciente.getIdPaciente().getPrimerApellido());
                            nuevoregistroRipUS.setSegApe(facturaPaciente.getIdPaciente().getSegundoApellido());

                            nuevoregistroRipUS.setTipoEdad("1");//Edad: se redondea al año, si es menor al año se redondea a mes, si es menor de un mes en días
                            nuevoregistroRipUS.setEdad(calcularEdadInt(facturaPaciente.getIdPaciente().getFechaNacimiento()));//se esta guardando en años pero toca calcularlo en meses y dias

                            if (facturaPaciente.getIdPaciente().getGenero() != null) {
                                nuevoregistroRipUS.setSexo(facturaPaciente.getIdPaciente().getGenero().getObservacion());
                            } else {
                                nuevoregistroRipUS.setSexo("");
                            }
                            if (facturaPaciente.getIdPaciente().getZona() != null) {//Zona de residencia habitual(Si no tiene por defectu U)
                                nuevoregistroRipUS.setZonaRes(facturaPaciente.getIdPaciente().getZona().getObservacion());
                            } else {
                                nuevoregistroRipUS.setZonaRes("");
                            }
                            if (facturaPaciente.getIdPaciente().getDepartamento() != null) {//Código del departamento de residencia habitual (PENDIENTE: Cuadrar códigos de tabla cfg_clacificaciones con DANE)
                                nuevoregistroRipUS.setCodDepRes(facturaPaciente.getIdPaciente().getDepartamento().getCodigo());//POR EL MOMENTO SE ASIGNAN ESTOS PERO LO DE RESIDENCIA
                            } else {
                                nuevoregistroRipUS.setCodDepRes("");
                            }
                            if (facturaPaciente.getIdPaciente().getMunicipio() != null) {
                                String aux = facturaPaciente.getIdPaciente().getMunicipio().getCodigo();
                                aux = aux.substring(aux.length() - 3);
                                nuevoregistroRipUS.setCodMunRes(aux);//SE DEBE CUADRAR LA TABLA CLASIFICACIONES EN EL CAMPO OBSERVACION EL CODIGO DEL MUNICIPIO (NO JUNTO CON EL DEPARTAMENtO COMO ESTA)                        
                            } else {
                                nuevoregistroRipUS.setCodMunRes("");
                            }
                            nuevoregistroRipUS.setRipsAlmacenados(ripAlmacenado);
                            ripsUsFacade.create(nuevoregistroRipUS);
                        }
                    }
                    if (ripAP) {
                        //registro de datos para el archivo de procedimientos(aquí van solo servicios y paquetes)
                        //RIP AP(cod_proc=codigo CUP =>inician con 8)  
                        boolean continuar;
//                        for (FacFacturaPaciente facturaPaciente : facturaActual.getFacFacturaPacienteList()) {
                        for (FacFacturaServicio servicioActual : facturaPaciente.getFacFacturaServicioList()) {
                            continuar = true;
                            if (servicioActual.getIdServicio().getCodigoCup() == null) {
                                continuar = false;
                                System.err.println("El servicio: " + servicioActual.getIdServicio().getCodigoServicio() + " - " + servicioActual.getIdServicio().getNombreServicio() + "no tiene codigo CUPS");
                            }
                            if (continuar && !servicioActual.getIdServicio().getCodigoCup().startsWith("8")) {
                                continuar = false;
                                if (!continuar && servicioActual.getIdServicio().getCodigoCup().startsWith("9")) {
                                    continuar = true;
                                } else {
                                    System.err.println("El codigo CUP(" + servicioActual.getIdServicio().getCodigoCup() + ") del servicio: " + servicioActual.getIdServicio().getCodigoServicio() + " - " + servicioActual.getIdServicio().getNombreServicio() + " no inicia ni con 9 ni con 8");
                                }
                            }
                            //SOLUCION NO OPTIMA. BUSCA EL CODIGO INTERNO DEL SERVICIO QUE SE ESTIMA COMO PROCEDIMIENTO
                            if (servicioActual.getIdServicio().getCodigoServicio().equals("953501")) {
                                continuar = true;
                            } else {
                                continuar = false;
                            }
                            if (continuar) {
                                contadorAP++;
                                RipsApPK llave = new RipsApPK(ripAlmacenado.getIdRipAlmacenado(), contadorAP);
                                RipsAp nuevoregistroRipAP = new RipsAp(llave);
                                nuevoregistroRipAP.setNumFac(facturaPaciente.getCodigoDocumento());
                                nuevoregistroRipAP.setCodPre(empresaActual.getCodigoEmpresa());//Código del prestador de servicios de salud:el que se ingresa en empresa > código_empresa
                                if (facturaPaciente.getIdPaciente().getTipoIdentificacion() != null) {
                                    nuevoregistroRipAP.setTipIde(facturaPaciente.getIdPaciente().getTipoIdentificacion().getDescripcion());
                                } else {
                                    nuevoregistroRipAP.setTipIde("");
                                }
                                nuevoregistroRipAP.setNumIde(facturaPaciente.getIdPaciente().getIdentificacion());
                                nuevoregistroRipAP.setFecProc(formatoFechaSql.format(servicioActual.getFechaServicio()));
                                if (facturaPaciente.getIdCita() != null && facturaPaciente.getIdCita().getNumAutorizacion() != null) {
                                    nuevoregistroRipAP.setNumAut(facturaPaciente.getIdCita().getNumAutorizacion());
                                } else {
                                    nuevoregistroRipAP.setNumAut("");
                                }
                                if (servicioActual.getIdServicio().getCodigoCup() != null) {
                                    nuevoregistroRipAP.setCodPro(servicioActual.getIdServicio().getCodigoCup());//Código del procedimiento: fac_servicio > código_cup < PENDIENTE POR CONSEGUIR ESOS CODIGOS />
                                } else {
                                    nuevoregistroRipAP.setCodPro("");
                                }
                                if (servicioActual.getIdServicio().getAmbito() != null) {
                                    nuevoregistroRipAP.setAmbPro(servicioActual.getIdServicio().getAmbito().getCodigo());
                                } else {
                                    nuevoregistroRipAP.setAmbPro("");
                                }
//                                    if (servicioActual.getIdServicio().getFinalidad() != null) {
//                                        nuevoregistroRipAP.setFinPro(servicioActual.getIdServicio().getFinalidad().getCodigo());
//                                    } else {
                                nuevoregistroRipAP.setFinPro("2");
//                                    }
//                                    if (servicioActual.getIdMedico().getTipoUsuario() != null) {
//                                        nuevoregistroRipAP.setPersAti(servicioActual.getIdMedico().getTipoUsuario().getCodigo());
//                                    } else {
                                nuevoregistroRipAP.setPersAti("");
//                                    }

                                ArrayList<String> diagnosticosDeterminados = determinarDiagnosticos(facturaPaciente);

                                if (diagnosticosDeterminados != null) {//si es diferente de null tiene 4 elementos asi sean cadenas vacias
                                    nuevoregistroRipAP.setDxPpal(diagnosticosDeterminados.get(0));
                                    nuevoregistroRipAP.setDxRel(diagnosticosDeterminados.get(1));
                                } else {
                                    nuevoregistroRipAP.setDxPpal("");
                                    nuevoregistroRipAP.setDxRel("");
                                }
                                nuevoregistroRipAP.setComplicacion("");//no se usa para la entidad
//                                    if (servicioActual.getIdServicio().getActoQuirurgico() != null) {
//                                        nuevoregistroRipAP.setActQuirur(servicioActual.getIdServicio().getActoQuirurgico().getCodigo());//a todos asignarles 1 y al ingresar nuevo que sea 1
//                                    } else {
                                nuevoregistroRipAP.setActQuirur("");
//                                    }
                                nuevoregistroRipAP.setValor(servicioActual.getValorParcial());//Valor del Procedimiento: valor del servicio – (copago+ cuota_mo) + (iva+cree)	
                                nuevoregistroRipAP.setRipsAlmacenados(ripAlmacenado);
                                ripsApFacade.create(nuevoregistroRipAP);
                            }
                        }
                    }
                    if (ripAC) {
                        //REGISTRO DE DATOS PARA EL ARCHIVO DE CONSULTA 
                        //RIP AC(cod proc=CUPS => inician 9 )
                        boolean continuar;
                        cuotaModeradoraCobrada = false;
                        for (FacFacturaServicio servicioActual : facturaPaciente.getFacFacturaServicioList()) {
                            continuar = true;
                            if (servicioActual.getIdServicio().getCodigoCup() == null) {
                                continuar = false;
                                System.err.println("El servicio: " + servicioActual.getIdServicio().getCodigoServicio() + " - " + servicioActual.getIdServicio().getNombreServicio() + "no tiene codigo CUPS");
                            }
                            if (continuar && !servicioActual.getIdServicio().getCodigoCup().startsWith("9")) {
                                continuar = false;
                                if (!continuar && servicioActual.getIdServicio().getCodigoCup().startsWith("8")) {
                                    continuar = true;
                                } else {
                                    System.err.println("El codigo CUP(" + servicioActual.getIdServicio().getCodigoCup() + ") del servicio: " + servicioActual.getIdServicio().getCodigoServicio() + " - " + servicioActual.getIdServicio().getNombreServicio() + " no inicia ni con 9 ni con 8");
                                }
                            }
                            if (servicioActual.getIdServicio().getCodigoServicio().equals("953501")) {
                                continuar = false;
                            } else {
                                continuar = true;
                            }
                            if (continuar) {
                                contadorAC++;
                                RipsAcPK llave = new RipsAcPK(ripAlmacenado.getIdRipAlmacenado(), contadorAC);
                                RipsAc nuevoregistroRipAC = new RipsAc(llave);
                                nuevoregistroRipAC.setNumFac(facturaPaciente.getCodigoDocumento());
                                nuevoregistroRipAC.setCodPre(empresaActual.getCodigoEmpresa());
                                if (facturaPaciente.getIdPaciente().getTipoIdentificacion() != null) {
                                    nuevoregistroRipAC.setTipIde(facturaPaciente.getIdPaciente().getTipoIdentificacion().getDescripcion());
                                } else {
                                    nuevoregistroRipAC.setTipIde("");
                                }
                                nuevoregistroRipAC.setNumIde(facturaPaciente.getIdPaciente().getIdentificacion());
                                nuevoregistroRipAC.setFecCons(formatoFechaSql.format(servicioActual.getFechaServicio()));
                                if (facturaPaciente.getIdCita() != null && facturaPaciente.getIdCita().getNumAutorizacion() != null) {
                                    nuevoregistroRipAC.setNumAut(facturaPaciente.getIdCita().getNumAutorizacion());
                                } else {
                                    nuevoregistroRipAC.setNumAut("");
                                }

                                if (servicioActual.getIdServicio().getCodigoCup() != null) {
                                    nuevoregistroRipAC.setCodCon(servicioActual.getIdServicio().getCodigoCup());
                                } else {
                                    nuevoregistroRipAC.setCodCon("");
                                }
                                nuevoregistroRipAC.setFinCon("10");//por ahora es estatico pero deberia ser dinamico
                                nuevoregistroRipAC.setCauExt("13");//por ahora es estatico pero deberia ser dinamico

                                ArrayList<String> diagnosticosDeterminados = determinarDiagnosticos(facturaPaciente);
                                if (diagnosticosDeterminados != null) {//si es diferente de null tiene 4 elementos asi sean cadenas vacias
                                    nuevoregistroRipAC.setDxPpal(diagnosticosDeterminados.get(0));
//                                    nuevoregistroRipAC.setDxRel1(diagnosticosDeterminados.get(1));
//                                    nuevoregistroRipAC.setDxRel2(diagnosticosDeterminados.get(2));
//                                    nuevoregistroRipAC.setDxRel3(diagnosticosDeterminados.get(3));
                                    nuevoregistroRipAC.setDxRel1("");
                                    nuevoregistroRipAC.setDxRel2("");
                                    nuevoregistroRipAC.setDxRel3("");
                                } else {
                                    nuevoregistroRipAC.setDxPpal("H522");
                                    nuevoregistroRipAC.setDxRel1("");
                                    nuevoregistroRipAC.setDxRel2("");
                                    nuevoregistroRipAC.setDxRel3("");
                                }
                                nuevoregistroRipAC.setTipoDxPpal("1");//por defecto 3
                                nuevoregistroRipAC.setVlrCons(servicioActual.getValorServicio());//Valor del Procedimiento: valor del servicio – (copago+ cuota_mo) + (iva+cree)	                                                                     
                                if (!cuotaModeradoraCobrada && facturaPaciente.getCuotaModeradora() != 0) {
                                    nuevoregistroRipAC.setVlrCuoMod(facturaPaciente.getCuotaModeradora());
                                    cuotaModeradoraCobrada = true;
                                } else {
                                    nuevoregistroRipAC.setVlrCuoMod(Double.parseDouble("0"));
                                }
                                nuevoregistroRipAC.setVlrNeto(servicioActual.getValorParcial());
                                nuevoregistroRipAC.setRipsAlmacenados(ripAlmacenado);
                                ripsAcFacade.create(nuevoregistroRipAC);
                            }
                        }
                    }
                }
            }
            if (ripCT) {
                if (ripAF) {
                    contadorCT++;
                    RipsCtPK llave = new RipsCtPK(ripAlmacenado.getIdRipAlmacenado(), contadorCT);
                    RipsCt nuevoregistroRipCT = new RipsCt(llave);
                    nuevoregistroRipCT.setCodPres(empresaActual.getCodigoEmpresa());
                    nuevoregistroRipCT.setFecRem(new Date());
                    nuevoregistroRipCT.setCodArc("AF" + nombreRIPS);
                    nuevoregistroRipCT.setTotalReg(contadorAF);
                    ripsCtFacade.create(nuevoregistroRipCT);
                }
                if (ripUS) {
                    contadorCT++;
                    RipsCtPK llave = new RipsCtPK(ripAlmacenado.getIdRipAlmacenado(), contadorCT);
                    RipsCt nuevoregistroRipCT = new RipsCt(llave);
                    nuevoregistroRipCT.setCodPres(empresaActual.getCodigoEmpresa());
                    nuevoregistroRipCT.setFecRem(new Date());
                    nuevoregistroRipCT.setCodArc("US" + nombreRIPS);
                    nuevoregistroRipCT.setTotalReg(contadorUS);
                    ripsCtFacade.create(nuevoregistroRipCT);
                }
                if (ripAP) {
                    contadorCT++;
                    RipsCtPK llave = new RipsCtPK(ripAlmacenado.getIdRipAlmacenado(), contadorCT);
                    RipsCt nuevoregistroRipCT = new RipsCt(llave);
                    nuevoregistroRipCT.setCodPres(empresaActual.getCodigoEmpresa());
                    nuevoregistroRipCT.setFecRem(new Date());
                    nuevoregistroRipCT.setCodArc("AP" + nombreRIPS);
                    nuevoregistroRipCT.setTotalReg(contadorAP);
                    ripsCtFacade.create(nuevoregistroRipCT);
                }
                if (ripAC) {//RIP CONSULTA
                    contadorCT++;
                    RipsCtPK llave = new RipsCtPK(ripAlmacenado.getIdRipAlmacenado(), contadorCT);
                    RipsCt nuevoregistroRipCT = new RipsCt(llave);
                    nuevoregistroRipCT.setCodPres(empresaActual.getCodigoEmpresa());//Lo da el mimisterio, CONFIGURACION > EMPRESA
                    nuevoregistroRipCT.setFecRem(new Date());//fecha en que se creo cada uno de los archivo
                    nuevoregistroRipCT.setCodArc("AC" + nombreRIPS);//< QUEDA PENDIENTE />
                    nuevoregistroRipCT.setTotalReg(contadorAC);//Conteo de los registrso de cada archivo
                    ripsCtFacade.create(nuevoregistroRipCT);
                }
                if (ripAT) {//RIP OTRO SERVICIOS (INSUMOS SUMINISTROS)
                    contadorCT++;
                    RipsCtPK llave = new RipsCtPK(ripAlmacenado.getIdRipAlmacenado(), contadorCT);
                    RipsCt nuevoregistroRipCT = new RipsCt(llave);
                    nuevoregistroRipCT.setCodPres(empresaActual.getCodigoEmpresa());
                    nuevoregistroRipCT.setFecRem(new Date());
                    nuevoregistroRipCT.setCodArc("AT" + nombreRIPS);
                    nuevoregistroRipCT.setTotalReg(contadorAT);
                    ripsCtFacade.create(nuevoregistroRipCT);
                }
            }
            listaRipsAlmacenados = ripsAlmacenadosFacade.buscarOrdenado();
            imprimirMensaje("Correcto", "Los RIPS han sido generados correctamente", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "No se pudo generar RIPS:" + e.getMessage(), FacesMessage.SEVERITY_INFO);
        }
    }

    public StreamedContent generarZipfacturaAdministradora() throws JRException, IOException {
        //genearcion de RIPS desde facturarAdministradora.xhtml
        generarRipsDesdeAdministradora = true;
        if (facturarAdministradoraMB.getFacturaSeleccionadaTabla() != null) {
            //imprimirMensaje("Correcto", "Se selecciono", FacesMessage.SEVERITY_INFO);
            facturaAdmiSeleccionada = facturaAdmiFacade.find(Integer.parseInt(facturarAdministradoraMB.getFacturaSeleccionadaTabla().getColumna1()));
            listaFacturasAdministradora = new ArrayList<>();
            listaFacturasAdministradora.add(facturaAdmiSeleccionada);
            String aux = String.valueOf(facturaAdmiSeleccionada.getNumeroDocumento());
            aux = Strings.padStart(aux, 4, '0');
            nombreRIPS = aux;
            administradoraActual = facturaAdmiSeleccionada.getIdAdministradora();
            generarRIPS();
            ripSeleccionado = ripsAlmacenadosFacade.find(ripAlmacenado.getIdRipAlmacenado());
            StreamedContent a = generarZip();
            confirmarEliminarRips();
            ripsAlmacenadosFacade.resetearSecuencia();//resetear la secuencia de rips
            inicializar();
            facturaAdmiSeleccionada = null;
            return a;
        } else {
            System.out.println("No hay seleccion");
            imprimirMensaje("Error", "Se debe seleccionar una factura de la tabla", FacesMessage.SEVERITY_ERROR);
            return null;
        }
    }

    public StreamedContent generarZip() throws JRException, IOException {//genera un pdf de una historia seleccionada en el historial        

        if (ripSeleccionado == null) {
            imprimirMensaje("Error", "Se debe seleccionar un registro de la tabla", FacesMessage.SEVERITY_ERROR);
            return null;
        }
        String nameAndPathFile;//System.out.println("RUTA: " + nameAndPathFile);
        FileWriter fichero = null;
        PrintWriter pw;
        String[] archivosGenerados = ripSeleccionado.getArchivos().split(",");
        ArrayList<String> listaArchivos = new ArrayList<>();
        boolean imprimirCabeceras = false;

        for (Object elem : archivosGenerados) {

            //-------------------DIAGNOSTICOS------------------------------
//            if (elem.toString().trim().compareTo("DIAGNOSTICOS") == 0) {
//                listaArchivos.add("diagnosticos.txt");
//                nameAndPathFile = realPath + "diagnosticos.txt";
//                File ficherofile = new File(nameAndPathFile);
//                if (ficherofile.exists()) {//Lo Borramos
//                    ficherofile.delete();//Lo Borramos
//                }
//                try {
//                    fichero = new FileWriter(nameAndPathFile);
//                    pw = new PrintWriter(fichero);
//                    List<CfgDiagnostico> listaDiagnosticos = diagnosticoFacade.findAll();
//                    if (imprimirCabeceras) {
//                        pw.println("CODIGO, DESCRIPCION");
//                    }
//                    for (CfgDiagnostico diagnostico : listaDiagnosticos) {
//                        pw.println(diagnostico.getCodigoDiagnostico() + "," + diagnostico.getNombreDiagnostico());
//                    }
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                } finally {
//                    try {//Asegurarnos que se cierra el fichero.
//                        if (null != fichero) {
//                            fichero.close();
//                        }
//                    } catch (Exception e2) {
//                        System.out.println(e2.getMessage());
//                    }
//                }
//            }
            //-------------------ARCHIVO CT------------------------------
            if (elem.toString().trim().compareTo("CT") == 0) {
                if (facturaAdmiSeleccionada != null) {
                    String aux = String.valueOf(facturaAdmiSeleccionada.getNumeroDocumento());
                    listaArchivos.add("CT" + Strings.padStart(aux, 4, '0') + ".txt");
                    nameAndPathFile = realPath + "CT" + Strings.padStart(aux, 4, '0') + ".txt";
                } else {
                    listaArchivos.add("CT" + ripSeleccionado.getNombre() + ".txt");
                    nameAndPathFile = realPath + "CT" + ripSeleccionado.getNombre() + ".txt";
                }
                File ficherofile = new File(nameAndPathFile);
                if (ficherofile.exists()) {//Lo Borramos
                    ficherofile.delete();//Lo Borramos
                }
                try {
                    fichero = new FileWriter(nameAndPathFile);
                    pw = new PrintWriter(fichero);
                    //List<CfgDiagnostico> listaDiagnosticos = diagnosticoFacade.findAll();
                    if (imprimirCabeceras) {
                        pw.println("cod_pre,fec_rem,cod_arc,total_reg");
                    }
                    for (RipsCt ct : ripSeleccionado.getRipsCtList()) {
                        pw.println(
                                ct.getCodPres() + ","
                                + formatoFechaSql.format(ct.getFecRem()) + ","
                                + ct.getCodArc() + ","
                                + ct.getTotalReg().toString());
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    try {//Asegurarnos que se cierra el fichero.
                        if (null != fichero) {
                            fichero.close();
                        }
                    } catch (Exception e2) {
                        System.out.println(e2.getMessage());
                    }
                }
            }
            //-------------------ARCHIVO AF------------------------------
            if (elem.toString().trim().compareTo("AF") == 0) {
                if (facturaAdmiSeleccionada != null) {
                    String aux = String.valueOf(facturaAdmiSeleccionada.getNumeroDocumento());
                    listaArchivos.add("AF" + Strings.padStart(aux, 4, '0') + ".txt");
                    nameAndPathFile = realPath + "AF" + Strings.padStart(aux, 4, '0') + ".txt";
                } else {
                    listaArchivos.add("AF" + ripSeleccionado.getNombre() + ".txt");
                    nameAndPathFile = realPath + "AF" + ripSeleccionado.getNombre() + ".txt";
                }
                File ficherofile = new File(nameAndPathFile);
                if (ficherofile.exists()) {//Lo Borramos
                    ficherofile.delete();//Lo Borramos
                }
                try {
                    fichero = new FileWriter(nameAndPathFile);
                    pw = new PrintWriter(fichero);
                    List<CfgDiagnostico> listaDiagnosticos = diagnosticoFacade.findAll();
                    if (imprimirCabeceras) {
                        pw.println("cod_pre,raz_soc,tip_ide,num_ide,num_fac,fec_exp,fec_inc,fec_fin,cod_ent,nom_ent,num_con,plan_ben,num_poli,val_copa,val_com,val_desc,val_net");
                    }
                    for (RipsAf af : ripSeleccionado.getRipsAfList()) {
                        pw.println(
                                af.getCodPre() + ","
                                + af.getRazSoc() + ","
                                + af.getTipIde() + ","
                                + af.getNumIde() + ","
                                + af.getNumFac() + ","
                                + af.getFecExp() + ","
                                + af.getFecInc() + ","
                                + af.getFecFin() + ","
                                + af.getCodEnt() + ","
                                + af.getNomEnt() + ","
                                + af.getNumCon() + ","
                                + af.getPlanBen() + ","
                                + af.getNumPoli() + ","
                                + formateadorDecimal.format(af.getValCopa()).replace(",", ".") + ","
                                + formateadorDecimal.format(af.getValCom()).replace(",", ".") + ","
                                + formateadorDecimal.format(af.getValDesc()).replace(",", ".") + ","
                                + (af.getValNet() != 0 ? formateadorDecimal.format(af.getValNet()).replace(",", ".") : "")
                        );
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    try {//Asegurarnos que se cierra el fichero.
                        if (null != fichero) {
                            fichero.close();
                        }
                    } catch (Exception e2) {
                        System.out.println(e2.getMessage());
                    }
                }
            }
            //-------------------ARCHIVO US------------------------------
            if (elem.toString().trim().compareTo("US") == 0) {
                if (facturaAdmiSeleccionada != null) {
                    String aux = String.valueOf(facturaAdmiSeleccionada.getNumeroDocumento());
                    listaArchivos.add("US" + Strings.padStart(aux, 4, '0') + ".txt");
                    nameAndPathFile = realPath + "US" + Strings.padStart(aux, 4, '0') + ".txt";
                } else {
                    listaArchivos.add("US" + ripSeleccionado.getNombre() + ".txt");
                    nameAndPathFile = realPath + "US" + ripSeleccionado.getNombre() + ".txt";
                }
                File ficherofile = new File(nameAndPathFile);
                if (ficherofile.exists()) {//Lo Borramos
                    ficherofile.delete();//Lo Borramos
                }
                try {
                    fichero = new FileWriter(nameAndPathFile);
                    pw = new PrintWriter(fichero);
                    List<CfgDiagnostico> listaDiagnosticos = diagnosticoFacade.findAll();
                    if (imprimirCabeceras) {
                        pw.println("tip_ide,num_ide,cod_ent_adm,tip_usu,apellido_a,apellido_b,nombre_a,nombre_b,edad,unid_med,,cod_dep_res,cod_mun_res,zona_res");
                    }
                    for (RipsUs us : ripSeleccionado.getRipsUsList()) {
                        pw.println(
                                us.getTipIde() + ","
                                + us.getNumIde() + ","
                                + us.getCodEntAdm() + ","
                                + us.getTipUsu() + ","
                                + us.getPriApe() + ","
                                + us.getSegApe() + ","
                                + us.getPriNom() + ","
                                + us.getSegNom() + ","
                                + us.getEdad() + ","
                                + us.getTipoEdad() + ","
                                + us.getSexo() + ","
                                + us.getCodDepRes() + ","
                                + us.getCodMunRes() + ","
                                + us.getZonaRes());
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    try {//Asegurarnos que se cierra el fichero.
                        if (null != fichero) {
                            fichero.close();
                        }
                    } catch (Exception e2) {
                        System.out.println(e2.getMessage());
                    }
                }
            }
            //-------------------ARCHIVO AP------------------------------
            if (elem.toString().trim().compareTo("AP") == 0) {
                if (facturaAdmiSeleccionada != null) {
                    String aux = String.valueOf(facturaAdmiSeleccionada.getNumeroDocumento());
                    listaArchivos.add("AP" + Strings.padStart(aux, 4, '0') + ".txt");
                    nameAndPathFile = realPath + "AP" + Strings.padStart(aux, 4, '0') + ".txt";
                } else {
                    listaArchivos.add("AP" + ripSeleccionado.getNombre() + ".txt");
                    nameAndPathFile = realPath + "AP" + ripSeleccionado.getNombre() + ".txt";
                }
                File ficherofile = new File(nameAndPathFile);
                if (ficherofile.exists()) {//Lo Borramos
                    ficherofile.delete();//Lo Borramos
                }
                try {
                    fichero = new FileWriter(nameAndPathFile);
                    pw = new PrintWriter(fichero);
                    if (imprimirCabeceras) {
                        pw.println("num_fac,cod_pre,tip_ide,num_ide,fec_proc,num_aut,cod_proced,ambito_pro,finali_pro,pers_atiend,diag_pri,diag_relac,complica,act_quirur,val_proced");
                    }
                    for (RipsAp ap : ripSeleccionado.getRipsApList()) {
                        pw.println(
                                ap.getNumFac() + ","
                                + ap.getCodPre() + ","
                                + ap.getTipIde() + ","
                                + ap.getNumIde() + ","
                                + ap.getFecProc() + ","
                                + ap.getNumAut() + ","
                                + ap.getCodPro() + ","
                                + ap.getAmbPro() + ","
                                + ap.getFinPro() + ","
                                + ap.getPersAti() + ","
                                + ap.getDxPpal() + ","
                                + ap.getDxRel() + ","
                                + ap.getComplicacion() + ","
                                + ap.getActQuirur() + ","
                                + formateadorDecimal.format(ap.getValor()).replace(",", ".")
                        );
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    try {//Asegurarnos que se cierra el fichero.
                        if (null != fichero) {
                            fichero.close();
                        }
                    } catch (Exception e2) {
                        System.out.println(e2.getMessage());
                    }
                }
            }
            //-------------------ARCHIVO AC------------------------------
            if (elem.toString().trim().compareTo("AC") == 0) {
                if (facturaAdmiSeleccionada != null) {
                    String aux = String.valueOf(facturaAdmiSeleccionada.getNumeroDocumento());
                    listaArchivos.add("AC" + Strings.padStart(aux, 4, '0') + ".txt");
                    nameAndPathFile = realPath + "AC" + Strings.padStart(aux, 4, '0') + ".txt";
                } else {
                    listaArchivos.add("AC" + ripSeleccionado.getNombre() + ".txt");
                    nameAndPathFile = realPath + "AC" + ripSeleccionado.getNombre() + ".txt";
                }
                File ficherofile = new File(nameAndPathFile);
                if (ficherofile.exists()) {//Lo Borramos
                    ficherofile.delete();//Lo Borramos
                }
                try {
                    fichero = new FileWriter(nameAndPathFile);
                    pw = new PrintWriter(fichero);

                    if (imprimirCabeceras) {
                        pw.println("num_fac,cod_pre,tip_ide,num_ide,fec_cons,num_aut,cod_proced,finali_con,cau_ext,cdiag_pri,cdiag_r1,cdiag_r2,cdiag_r3,tip_diag_pri,val_cons,val_cuo_mod,val_net");
                    }
                    for (RipsAc ac : ripSeleccionado.getRipsAcList()) {
                        pw.println(
                                ac.getNumFac() + ","
                                + ac.getCodPre() + ","
                                + ac.getTipIde() + ","
                                + ac.getNumIde() + ","
                                + ac.getFecCons() + ","
                                + ac.getNumAut() + ","
                                + ac.getCodCon() + ","
                                + ac.getFinCon() + ","
                                + ac.getCauExt() + ","
                                + ac.getDxPpal() + ","
                                + ac.getDxRel1() + ","
                                + ac.getDxRel2() + ","
                                + ac.getDxRel3() + ","
                                + ac.getTipoDxPpal() + ","
                                + (ac.getVlrCons() != 0 ? formateadorDecimal.format(ac.getVlrCons()).replace(",", ".") + "," : ",")
                                + formateadorDecimal.format(ac.getVlrCuoMod()).replace(",", ".") + ","
                                + (ac.getVlrNeto() != 0 ? formateadorDecimal.format(ac.getVlrNeto()).replace(",", ".") : "")
                        );
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    try {//Asegurarnos que se cierra el fichero.
                        if (null != fichero) {
                            fichero.close();
                        }
                    } catch (Exception e2) {
                        System.out.println(e2.getMessage());
                    }
                }
            }
            //---------------------ARCHIVO AT-------------------
            if (elem.toString().trim().compareTo("AT") == 0) {
                if (facturaAdmiSeleccionada != null) {
                    String aux = String.valueOf(facturaAdmiSeleccionada.getNumeroDocumento());
                    listaArchivos.add("AT" + Strings.padStart(aux, 4, '0') + ".txt");
                    nameAndPathFile = realPath + "AT" + Strings.padStart(aux, 4, '0') + ".txt";
                } else {
                    listaArchivos.add("AT" + ripSeleccionado.getNombre() + ".txt");
                    nameAndPathFile = realPath + "AT" + ripSeleccionado.getNombre() + ".txt";
                }
                File ficherofile = new File(nameAndPathFile);
                if (ficherofile.exists()) {//Lo Borramos
                    ficherofile.delete();//Lo Borramos
                }
                try {
                    fichero = new FileWriter(nameAndPathFile);
                    pw = new PrintWriter(fichero);

                    if (imprimirCabeceras) {
                        pw.println("num_fac,cod_pre,tip_ide,num_ide,num_aut,tip_ser,cod_ser,nom_ser,cantidad,vr_uni,vr_tot");
                    }
                    for (RipsAt at : ripSeleccionado.getRipsAtList()) {
                        pw.println(
                                at.getNumFac() + ","
                                + at.getCodPre() + ","
                                + at.getTipIde() + ","
                                + at.getNumIde() + ","
                                + at.getNumAut() + ","
                                + at.getTipSer() + ","
                                + at.getCodSer() + ","
                                + at.getNomSer() + ","
                                + at.getCantidad() + ","
                                + at.getVrUni() + ","
                                + at.getVrTot()
                        );
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    try {//Asegurarnos que se cierra el fichero.
                        if (null != fichero) {
                            fichero.close();
                        }
                    } catch (Exception e2) {
                        System.out.println(e2.getMessage());
                    }
                }
            }
        }

        //---------COPRESION A ZIP--------------
        byte[] buffer = new byte[1024];
        try {
            FileOutputStream fos = new FileOutputStream(realPath + "comprimido.zip");
            ZipOutputStream zos = new ZipOutputStream(fos);
            for (String archivo : listaArchivos) {
                ZipEntry ze = new ZipEntry(archivo);
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream(realPath + archivo);
                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                in.close();
            }
            zos.closeEntry();
            zos.close();//remember close it
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }

        File file = new File(realPath + "comprimido.zip");
        InputStream input = new FileInputStream(file);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        StreamedContent download = new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName());
        //System.out.println("PREP = " + download.getName());
        return download;
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------    
    public boolean isRipCT() {
        return ripCT;
    }

    public void setRipCT(boolean ripCT) {
        this.ripCT = ripCT;
    }

    public boolean isRipAF() {
        return ripAF;
    }

    public void setRipAF(boolean ripAF) {
        this.ripAF = ripAF;
    }

    public boolean isRipUS() {
        return ripUS;
    }

    public void setRipUS(boolean ripUS) {
        this.ripUS = ripUS;
    }

    public boolean isRipAP() {
        return ripAP;
    }

    public void setRipAP(boolean ripAP) {
        this.ripAP = ripAP;
    }

    public boolean isRipAC() {
        return ripAC;
    }

    public void setRipAC(boolean ripAC) {
        this.ripAC = ripAC;
    }

    public List<FacContrato> getListaContratos() {
        return listaContratos;
    }

    public void setListaContratos(List<FacContrato> listaContratos) {
        this.listaContratos = listaContratos;
    }

    public String getIdAdministradora() {
        return idAdministradora;
    }

    public void setIdAdministradora(String idAdministradora) {
        this.idAdministradora = idAdministradora;
    }

    public String getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(String idContrato) {
        this.idContrato = idContrato;
    }

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

    public String getNombreRIPS() {
        return nombreRIPS;
    }

    public void setNombreRIPS(String nombreRIPS) {
        this.nombreRIPS = nombreRIPS;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isDiagnosticos() {
        return diagnosticos;
    }

    public void setDiagnosticos(boolean diagnosticos) {
        this.diagnosticos = diagnosticos;
    }

    public List<RipsAlmacenados> getListaRipsAlmacenados() {
        return listaRipsAlmacenados;
    }

    public void setListaRipsAlmacenados(List<RipsAlmacenados> listaRipsAlmacenados) {
        this.listaRipsAlmacenados = listaRipsAlmacenados;
    }

    public RipsAlmacenados getRipSeleccionado() {
        return ripSeleccionado;
    }

    public void setRipSeleccionado(RipsAlmacenados ripSeleccionado) {
        this.ripSeleccionado = ripSeleccionado;
    }

}
