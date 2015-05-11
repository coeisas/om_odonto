package managedBeans.facturacion;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import beans.utilidades.MetodosGenerales;
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
import modelo.entidades.FacFacturaServicio;
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
import modelo.fachadas.HcRegistroFacade;
import modelo.fachadas.RipsAcFacade;
import modelo.fachadas.RipsAfFacade;
import modelo.fachadas.RipsAlmacenadosFacade;
import modelo.fachadas.RipsApFacade;
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
    FacFacturaPacienteFacade facFacturaPacienteFacade;
    @EJB
    FacFacturaAdmiFacade facFacturaAdmiFacade;
    @EJB
    CfgDiagnosticoFacade diagnosticoFacade;
    @EJB
    CfgEmpresaFacade empresaFacade;
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
    private boolean diagnosticos = true;

    private String nombreRIPS = "";
    private Date fechaCreacion = new Date();
    private String idAdministradora = "";
    private String idContrato = "";
    private Date fechaInicial = new Date();
    private Date fechaFinal = new Date();
    private final Calendar calendarFechaFinalMasUnDia = Calendar.getInstance();
    private final SimpleDateFormat formatoFechaSql = new SimpleDateFormat("dd/MM/yyyy", new Locale("ES"));
    private final DecimalFormat formateadorDecimal = new DecimalFormat("0.00");

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
        switch (registroConDiagnostico.getIdTipoReg().getIdTipoReg()) {
            case 1://1;"HISTORIA CLINICA"   (4 diagnosticos)
                for (HcDetalle detalle : registroConDiagnostico.getHcDetalleList()) {
                    try {
                        switch (detalle.getHcDetallePK().getIdCampo()) {
                            case 33://dx principal
                                arregloRetorno.set(0, detalle.getValor().split(" - ")[0]);
                                break;
                            case 34://dx rel_1
                                arregloRetorno.set(1, detalle.getValor().split(" - ")[0]);
                                break;
                            case 35://dx rel_2
                                arregloRetorno.set(2, detalle.getValor().split(" - ")[0]);
                                break;
                            case 36://dx rel_3
                                arregloRetorno.set(3, detalle.getValor().split(" - ")[0]);
                                break;
                        }
                    } catch (Exception e) {
                        System.out.println("Error al sacar codigo de diagnostico de: " + detalle.getValor());
                    }
                }
                break;
            case 2://2;"REFERENCIA - CONTRAREFERENCIA" (1 diagnostico)
                for (HcDetalle detalle : registroConDiagnostico.getHcDetalleList()) {
                    try {
                        switch (detalle.getHcDetallePK().getIdCampo()) {
                            case 44://dx principal
                                arregloRetorno.set(0, detalle.getValor().split(" - ")[0]);
                                break;
                        }
                    } catch (Exception e) {
                        System.out.println("Error al sacar codigo de diagnostico de: " + detalle.getValor());
                    }
                }
                break;
            case 3://3;"NOTA EVOLUCION CONTROL" (0 diagnosticos)

                break;
            case 5://5;"NOTA EVOLUCION CONTROL PSIQUIATRIA" (4 diagnosticos)
                for (HcDetalle detalle : registroConDiagnostico.getHcDetalleList()) {
                    try {
                        switch (detalle.getHcDetallePK().getIdCampo()) {
                            case 59://dx principal
                                arregloRetorno.set(0, detalle.getValor().split(" - ")[0]);
                                break;
                            case 60://dx rel_1
                                arregloRetorno.set(1, detalle.getValor().split(" - ")[0]);
                                break;
                            case 61://dx rel_2
                                arregloRetorno.set(2, detalle.getValor().split(" - ")[0]);
                                break;
                            case 62://dx rel_3
                                arregloRetorno.set(3, detalle.getValor().split(" - ")[0]);
                                break;
                        }
                    } catch (Exception e) {
                        System.out.println("Error al sacar codigo de diagnostico de: " + detalle.getValor());
                    }
                }
                break;
            case 6://6;"RECETARIO"(0 diagnosticos)

                break;
            case 7://7;"HISTORIA CLINICA PSIQUIATRIA"(4 diagnosticos)
                for (HcDetalle detalle : registroConDiagnostico.getHcDetalleList()) {
                    try {
                        switch (detalle.getHcDetallePK().getIdCampo()) {
                            case 107://dx principal
                                arregloRetorno.set(0, detalle.getValor().split(" - ")[0]);
                                break;
                            case 108://dx rel_1
                                arregloRetorno.set(1, detalle.getValor().split(" - ")[0]);
                                break;
                            case 109://dx rel_2
                                arregloRetorno.set(2, detalle.getValor().split(" - ")[0]);
                                break;
                            case 110://dx rel_3
                                arregloRetorno.set(3, detalle.getValor().split(" - ")[0]);
                                break;
                        }
                    } catch (Exception e) {
                        System.out.println("Error al sacar codigo de diagnostico de: " + detalle.getValor());
                    }
                }
                break;
            case 8://8;"HISTORIA CLINICA ADULTO"(4 dignosticos)
                for (HcDetalle detalle : registroConDiagnostico.getHcDetalleList()) {
                    try {
                        switch (detalle.getHcDetallePK().getIdCampo()) {
                            case 137://dx principal
                                arregloRetorno.set(0, detalle.getValor().split(" - ")[0]);
                                break;
                            case 138://dx rel_1
                                arregloRetorno.set(1, detalle.getValor().split(" - ")[0]);
                                break;
                            case 139://dx rel_2
                                arregloRetorno.set(2, detalle.getValor().split(" - ")[0]);
                                break;
                            case 140://dx rel_3
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

    public void generarRIPS() {//RIPS (C/U EN ARCHIVO INDEPENDIENTE) (No lleva Cabecera, solo datos,Delimitador COMA)
        String archivos = "";
        if (!validarNoVacio(nombreRIPS)) {
            imprimirMensaje("Error", "Se debe escribir un nombre para el conjnto de RIPS", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RipsAlmacenados ripAlmacenado = ripsAlmacenadosFacade.buscarPorNombre(nombreRIPS);
        if (ripAlmacenado != null) {
            imprimirMensaje("Error", "Ya existen RIPS generados con este nombre", FacesMessage.SEVERITY_ERROR);
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

        //se buscan las facturas de administradoras 
        ripAlmacenado = new RipsAlmacenados();
        ripAlmacenado.setIdAdministradora(administradoraActual);
        ripAlmacenado.setNombre(nombreRIPS);
        ripAlmacenado.setFecha(new Date());
        ripAlmacenado.setArchivos(archivos);
        ripAlmacenado.setFechaInicial(fechaInicial);
        ripAlmacenado.setFechaFinal(fechaFinal);

        List<FacFacturaAdmi> listaFacturasAdministradora;
        String sql = "";
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
        System.out.println("CONSULTA PARA DETERMINAR RIPS: \n" + sql);
        if (listaFacturasAdministradora == null || listaFacturasAdministradora.isEmpty()) {
            imprimirMensaje("Error", "No existen facturas de administradoras en el rango especificado", FacesMessage.SEVERITY_ERROR);
            return;
        }
        ripsAlmacenadosFacade.create(ripAlmacenado);//se almacena el conjunto de rips a almacenar
        int contadorCT = 0;
        int contadorAF = 0;
        int contadorUS = 0;
        int contadorAP = 0;
        int contadorAC = 0;
        try {
            for (FacFacturaAdmi facturaActual : listaFacturasAdministradora) {
                if (ripAF) {//RIP facturas_y_fac_extramurales
                    contadorAF++;
                    RipsAfPK llave = new RipsAfPK(ripAlmacenado.getIdRipAlmacenado(), contadorAF);
                    RipsAf nuevoregistroRipAF = new RipsAf(llave);
                    nuevoregistroRipAF.setCodPre(empresaActual.getCodigoEmpresa());// codigo de empresa dado por el mimisterio (SOLICITARLO EN SECCION CONFIGURACION > EMPRESA)
                    nuevoregistroRipAF.setRazSoc(empresaActual.getRazonSocial());
                    if (empresaActual.getTipoDoc().getDescripcion().length() > 2) {
                        nuevoregistroRipAF.setTipIde(empresaActual.getTipoDoc().getDescripcion().substring(0, 2));
                    } else {
                        nuevoregistroRipAF.setTipIde(empresaActual.getTipoDoc().getDescripcion());
                    }
                    nuevoregistroRipAF.setNumIde(empresaActual.getNumIdentificacion());
                    nuevoregistroRipAF.setNumFac(facturaActual.getCodigoDocumento());
                    nuevoregistroRipAF.setFecExp(formatoFechaSql.format(facturaActual.getFechaElaboracion()));//cuando se creo la factura
                    nuevoregistroRipAF.setFecInc(formatoFechaSql.format(facturaActual.getFechaInicial()));//fec_ini: desde cuando se esta Facturando
                    nuevoregistroRipAF.setFecFin(formatoFechaSql.format(facturaActual.getFechaFinal()));//fec_fin: hasta cuando se esta facturando
                    nuevoregistroRipAF.setCodEnt(administradoraActual.getCodigoAdministradora());//Código entidad Administradora: codgo rip en administradora < QUEDA PENDIENTE REENVIO DE ARCHIVO- se ingresa en fac_administradora.codigo_rip />
                    if (administradoraActual.getRazonSocial().length() > 29) {
                        nuevoregistroRipAF.setNomEnt(administradoraActual.getRazonSocial().substring(0, 29));
                    } else {
                        nuevoregistroRipAF.setNomEnt(administradoraActual.getRazonSocial());
                    }
                    nuevoregistroRipAF.setNumCon(facturaActual.getIdContrato().getCodigoContrato());
                    nuevoregistroRipAF.setPlanBen(null);//Plan de Beneficios: NO SE MANEJA VA VACIO
                    nuevoregistroRipAF.setNumPoli(facturaActual.getIdContrato().getNumeroPoliza());//Número de la póliza(SALE DE TABLA CONTRATO)
                    nuevoregistroRipAF.setValCopa(facturaActual.getValoresCopago());
                    nuevoregistroRipAF.setValCom(Double.parseDouble("0"));//no aplica valor compartido
                    nuevoregistroRipAF.setValDesc(Double.parseDouble("0"));//no aplica descuentos
                    nuevoregistroRipAF.setValNet(facturaActual.getValorEmpresa());// Valor Neto a Pagar por la entidad Contratante: valor paga
                    ripsAfFacade.create(nuevoregistroRipAF);
                }

                if (ripUS) {//registro de datos para el archivo de usuarios de los servicios de salud
                    for (FacFacturaPaciente facturaPaciente : facturaActual.getFacFacturaPacienteList()) {
                        RipsUs ripUsBuscado = ripsUsFacade.buscarPorIdAlmacenadoYIdentificacion(ripAlmacenado.getIdRipAlmacenado(), facturaPaciente.getIdPaciente().getIdentificacion());
                        if (ripUsBuscado == null) {//no se encuentra en el archivo de usuarios por lo que debe ser agregado
                            contadorUS++;
                            RipsUsPK llave = new RipsUsPK(ripAlmacenado.getIdRipAlmacenado(), contadorUS);
                            RipsUs nuevoregistroRipUS = new RipsUs(llave);
                            nuevoregistroRipUS.setTipIde(facturaPaciente.getIdPaciente().getTipoIdentificacion().getDescripcion());
                            nuevoregistroRipUS.setNumIde(facturaPaciente.getIdPaciente().getIdentificacion());
                            nuevoregistroRipUS.setCodEntAdm(facturaPaciente.getIdPaciente().getIdAdministradora().getCodigoRip());//Código Entidad Administradora:el que se ingresa en fac_administradora > código_rip
                            if (facturaPaciente.getIdPaciente().getRegimen() != null) {
                                nuevoregistroRipUS.setTipUsu(facturaPaciente.getIdPaciente().getRegimen().getCodigo());//Tipo de Usuario: cfg_pacientes > régimen
                            }
                            nuevoregistroRipUS.setPriNom(facturaPaciente.getIdPaciente().getPrimerNombre());
                            nuevoregistroRipUS.setSegNom(facturaPaciente.getIdPaciente().getSegundoNombre());
                            nuevoregistroRipUS.setPriApe(facturaPaciente.getIdPaciente().getPrimerApellido());
                            nuevoregistroRipUS.setSegApe(facturaPaciente.getIdPaciente().getSegundoApellido());

                            nuevoregistroRipUS.setTipoEdad("1");//Edad: se redondea al año, si es menor al año se redondea a mes, si es menor de un mes en días
                            nuevoregistroRipUS.setEdad(calcularEdadInt(facturaPaciente.getIdPaciente().getFechaNacimiento()));//se esta guardando en años pero toca calcularlo en meses y dias

                            if (facturaPaciente.getIdPaciente().getGenero() != null) {
                                nuevoregistroRipUS.setSexo(facturaPaciente.getIdPaciente().getGenero().getObservacion());
                            }
                            if (facturaPaciente.getIdPaciente().getZona() != null) {//Zona de residencia habitual(Si no tiene por defectu U)
                                nuevoregistroRipUS.setZonaRes(facturaPaciente.getIdPaciente().getZona().getObservacion());
                            }
                            if (facturaPaciente.getIdPaciente().getDepartamento() != null) {//Código del departamento de residencia habitual (PENDIENTE: Cuadrar códigos de tabla cfg_clacificaciones con DANE)
                                nuevoregistroRipUS.setCodDepRes(facturaPaciente.getIdPaciente().getDepartamento().getCodigo());//POR EL MOMENTO SE ASIGNAN ESTOS PERO LO DE RESIDENCIA
                            }
                            if (facturaPaciente.getIdPaciente().getMunicipio() != null) {
                                nuevoregistroRipUS.setCodMunRes(facturaPaciente.getIdPaciente().getMunicipio().getObservacion());//SE DEBE CUADRAR LA TABLA CLASIFICACIONES EN EL CAMPO OBSERVACION EL CODIGO DEL MUNICIPIO (NO JUNTO CON EL DEPARTAMENtO COMO ESTA)                        
                            }
                            ripsUsFacade.create(nuevoregistroRipUS);
                        }
                    }
                }
                if (ripAP) {
                    //registro de datos para el archivo de procedimientos(aquí van solo servicios y paquetes)
                    //RIP AP(cod_proc=codigo CUP =>ininican con 8)        
                    for (FacFacturaPaciente facturaPaciente : facturaActual.getFacFacturaPacienteList()) {
                        for (FacFacturaServicio servicioActual : facturaPaciente.getFacFacturaServicioList()) {
                            contadorAP++;
                            RipsApPK llave = new RipsApPK(ripAlmacenado.getIdRipAlmacenado(), contadorAP);
                            RipsAp nuevoregistroRipAP = new RipsAp(llave);
                            nuevoregistroRipAP.setNumFac(facturaActual.getCodigoDocumento());
                            nuevoregistroRipAP.setCodPre(empresaActual.getCodigoEmpresa());//Código del prestador de servicios de salud:el que se ingresa en empresa > código_empresa
                            if (facturaPaciente.getIdPaciente().getTipoIdentificacion() != null) {
                                nuevoregistroRipAP.setTipIde(facturaPaciente.getIdPaciente().getTipoIdentificacion().getDescripcion());
                            }
                            nuevoregistroRipAP.setNumIde(facturaPaciente.getIdPaciente().getIdentificacion());
                            nuevoregistroRipAP.setFecProc(formatoFechaSql.format(servicioActual.getFechaServicio()));
                            nuevoregistroRipAP.setNumAut(facturaPaciente.getNumeroAutorizacion());
                            nuevoregistroRipAP.setCodPro(servicioActual.getIdServicio().getCodigoCup());//Código del procedimiento: fac_servicio > código_cup < PENDIENTE POR CONSEGUIR ESOS CODIGOS />
                            if (servicioActual.getIdServicio().getAmbito() != null) {
                                nuevoregistroRipAP.setAmbPro(servicioActual.getIdServicio().getAmbito().getCodigo());
                            }
                            if (servicioActual.getIdServicio().getFinalidad() != null) {
                                nuevoregistroRipAP.setFinPro(servicioActual.getIdServicio().getFinalidad().getCodigo());
                            }
                            if (servicioActual.getIdMedico().getTipoUsuario() != null) {
                                nuevoregistroRipAP.setPersAti(servicioActual.getIdMedico().getTipoUsuario().getCodigo());
                            }

                            ArrayList<String> diagnosticosDeterminados = determinarDiagnosticos(facturaPaciente);
                            if (diagnosticosDeterminados != null) {//si es diferente de null tiene 4 elementos asi sean cadenas vacias
                                nuevoregistroRipAP.setDxPpal(diagnosticosDeterminados.get(0));
                                nuevoregistroRipAP.setDxRel(diagnosticosDeterminados.get(1));
                            }
                            nuevoregistroRipAP.setComplicacion("");//no se usa para la entidad
                            if (servicioActual.getIdServicio().getActoQuirurgico() != null) {
                                nuevoregistroRipAP.setActQuirur(servicioActual.getIdServicio().getActoQuirurgico().getCodigo());//a todos asignarles 1 y al ingresar nuevo que sea 1
                            }
                            nuevoregistroRipAP.setValor(servicioActual.getValorServicio());//Valor del Procedimiento: valor del servicio – (copago+ cuota_mo) + (iva+cree)	
                            ripsApFacade.create(nuevoregistroRipAP);
                        }
                    }
                }
                if (ripAC) {
                    //REGISTRO DE DATOS PARA EL ARCHIVO DE CONSULTA 
                    //RIP AC(cod proc=CUPS => inician 9 )
                    for (FacFacturaPaciente facturaPaciente : facturaActual.getFacFacturaPacienteList()) {
                        for (FacFacturaServicio servicioActual : facturaPaciente.getFacFacturaServicioList()) {
                            contadorAC++;
                            RipsAcPK llave = new RipsAcPK(ripAlmacenado.getIdRipAlmacenado(), contadorAC);
                            RipsAc nuevoregistroRipAC = new RipsAc(llave);
                            nuevoregistroRipAC.setNumFac(facturaActual.getCodigoDocumento());
                            nuevoregistroRipAC.setCodPre(empresaActual.getCodigoEmpresa());
                            if (facturaPaciente.getIdPaciente().getTipoIdentificacion() != null) {
                                nuevoregistroRipAC.setTipIde(facturaPaciente.getIdPaciente().getTipoIdentificacion().getDescripcion());
                            }
                            nuevoregistroRipAC.setNumIde(facturaPaciente.getIdPaciente().getIdentificacion());
                            nuevoregistroRipAC.setFecCons(formatoFechaSql.format(servicioActual.getFechaServicio()));
                            nuevoregistroRipAC.setNumAut(facturaPaciente.getNumeroAutorizacion());
                            nuevoregistroRipAC.setCodCon(servicioActual.getIdServicio().getCodigoCup());
                            nuevoregistroRipAC.setFinCon("10");//por ahora es estatico pero deberia ser dinamico
                            nuevoregistroRipAC.setCauExt("13");//por ahora es estatico pero deberia ser dinamico

                            ArrayList<String> diagnosticosDeterminados = determinarDiagnosticos(facturaPaciente);
                            if (diagnosticosDeterminados != null) {//si es diferente de null tiene 4 elementos asi sean cadenas vacias
                                nuevoregistroRipAC.setDxPpal(diagnosticosDeterminados.get(0));
                                nuevoregistroRipAC.setDxRel1(diagnosticosDeterminados.get(1));
                                nuevoregistroRipAC.setDxRel2(diagnosticosDeterminados.get(2));
                                nuevoregistroRipAC.setDxRel3(diagnosticosDeterminados.get(3));
                            }
                            nuevoregistroRipAC.setTipoDxPpal("3");//por defecto 3
                            nuevoregistroRipAC.setVlrCons(servicioActual.getValorServicio());
                            nuevoregistroRipAC.setVlrCuoMod(facturaPaciente.getCuotaModeradora());
                            nuevoregistroRipAC.setVlrNeto(servicioActual.getValorEmpresa());
                            ripsAcFacade.create(nuevoregistroRipAC);
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
                    nuevoregistroRipCT.setCodArc("AP" + "002259");
                    nuevoregistroRipCT.setTotalReg(contadorAP);
                    ripsCtFacade.create(nuevoregistroRipCT);
                }
                if (ripUS) {
                    contadorCT++;
                    RipsCtPK llave = new RipsCtPK(ripAlmacenado.getIdRipAlmacenado(), contadorCT);
                    RipsCt nuevoregistroRipCT = new RipsCt(llave);
                    nuevoregistroRipCT.setCodPres(empresaActual.getCodigoEmpresa());
                    nuevoregistroRipCT.setFecRem(new Date());
                    nuevoregistroRipCT.setCodArc("US" + "002259");
                    nuevoregistroRipCT.setTotalReg(contadorUS);
                    ripsCtFacade.create(nuevoregistroRipCT);
                }
                if (ripAP) {
                    contadorCT++;
                    RipsCtPK llave = new RipsCtPK(ripAlmacenado.getIdRipAlmacenado(), contadorCT);
                    RipsCt nuevoregistroRipCT = new RipsCt(llave);
                    nuevoregistroRipCT.setCodPres(empresaActual.getCodigoEmpresa());
                    nuevoregistroRipCT.setFecRem(new Date());
                    nuevoregistroRipCT.setCodArc("AP" + "002259");
                    nuevoregistroRipCT.setTotalReg(contadorAP);
                    ripsCtFacade.create(nuevoregistroRipCT);
                }
                if (ripAC) {//RIP CONTROL
                    contadorCT++;
                    RipsCtPK llave = new RipsCtPK(ripAlmacenado.getIdRipAlmacenado(), contadorCT);
                    RipsCt nuevoregistroRipCT = new RipsCt(llave);
                    nuevoregistroRipCT.setCodPres(empresaActual.getCodigoEmpresa());//Lo da el mimisterio, CONFIGURACION > EMPRESA
                    nuevoregistroRipCT.setFecRem(new Date());//fecha en que se creo cada uno de los archivo
                    nuevoregistroRipCT.setCodArc("AC" + "002259");//< QUEDA PENDIENTE />
                    nuevoregistroRipCT.setTotalReg(contadorAC);//Conteo de los registrso de cada archivo
                    ripsCtFacade.create(nuevoregistroRipCT);
                }
            }

            listaRipsAlmacenados = ripsAlmacenadosFacade.buscarOrdenado();
            imprimirMensaje("Correcto", "Los RIPS han sido generados correctamente", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "No se pudo generar RIPS:" + e.getMessage() + " ------------ " + e.getCause(), FacesMessage.SEVERITY_INFO);
        }
    }

    public void confirmarEliminarRips() {
        if (ripSeleccionado == null) {
            imprimirMensaje("Error", "Se debe seleccionar un registro de la tabla", FacesMessage.SEVERITY_ERROR);
        }
        ripsAlmacenadosFacade.remove(ripSeleccionado);
        ripSeleccionado = null;
        listaRipsAlmacenados = ripsAlmacenadosFacade.buscarOrdenado();
    }

    public StreamedContent generarZip() throws JRException, IOException {//genera un pdf de una historia seleccionada en el historial        

        if (ripSeleccionado == null) {
            imprimirMensaje("Error", "Se debe seleccionar un registro de la tabla", FacesMessage.SEVERITY_ERROR);
        }
        String nameAndPathFile;//System.out.println("RUTA: " + nameAndPathFile);
        FileWriter fichero = null;
        PrintWriter pw;
        String[] archivosGenerados = ripSeleccionado.getArchivos().split(",");
        ArrayList<String> listaArchivos = new ArrayList<>();

        for (Object elem : archivosGenerados) {
            //-------------------DIAGNOSTICOS------------------------------
            if (elem.toString().trim().compareTo("DIAGNOSTICOS") == 0) {
                listaArchivos.add("diagnosticos.txt");
                nameAndPathFile = realPath + "diagnosticos.txt";
                File ficherofile = new File(nameAndPathFile);
                if (ficherofile.exists()) {//Lo Borramos
                    ficherofile.delete();//Lo Borramos
                }
                try {
                    fichero = new FileWriter(nameAndPathFile);
                    pw = new PrintWriter(fichero);
                    List<CfgDiagnostico> listaDiagnosticos = diagnosticoFacade.findAll();
                    pw.println("CODIGO, DESCRIPCION");
                    for (CfgDiagnostico diagnostico : listaDiagnosticos) {
                        pw.println(diagnostico.getCodigoDiagnostico() + "," + diagnostico.getNombreDiagnostico());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {//Asegurarnos que se cierra el fichero.
                        if (null != fichero) {
                            fichero.close();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
            //-------------------ARCHIVO CT------------------------------
            if (elem.toString().trim().compareTo("CT") == 0) {
                listaArchivos.add("ct.txt");
                nameAndPathFile = realPath + "ct.txt";
                File ficherofile = new File(nameAndPathFile);
                if (ficherofile.exists()) {//Lo Borramos
                    ficherofile.delete();//Lo Borramos
                }
                try {
                    fichero = new FileWriter(nameAndPathFile);
                    pw = new PrintWriter(fichero);
                    List<CfgDiagnostico> listaDiagnosticos = diagnosticoFacade.findAll();
                    pw.println("cod_pre,fec_rem,cod_arc,total_reg");
                    for (RipsCt ct : ripSeleccionado.getRipsCtList()) {
                        pw.println(
                                ct.getCodPres() + ","
                                + formatoFechaSql.format(ct.getFecRem()) + ","
                                + ct.getCodArc() + ","
                                + ct.getTotalReg().toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {//Asegurarnos que se cierra el fichero.
                        if (null != fichero) {
                            fichero.close();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
            //-------------------ARCHIVO AF------------------------------
            if (elem.toString().trim().compareTo("AF") == 0) {
                listaArchivos.add("af.txt");
                nameAndPathFile = realPath + "af.txt";
                File ficherofile = new File(nameAndPathFile);
                if (ficherofile.exists()) {//Lo Borramos
                    ficherofile.delete();//Lo Borramos
                }
                try {
                    fichero = new FileWriter(nameAndPathFile);
                    pw = new PrintWriter(fichero);
                    List<CfgDiagnostico> listaDiagnosticos = diagnosticoFacade.findAll();
                    pw.println("cod_pre,raz_soc,tip_ide,num_ide,num_fac,fec_exp,fec_inc,fec_fin,cod_ent,nom_ent,num_con,plan_ben,num_poli,val_copa,val_com,val_desc,val_net");
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
                                + formateadorDecimal.format(af.getValCopa()) + ","
                                + formateadorDecimal.format(af.getValCom()) + ","
                                + formateadorDecimal.format(af.getValDesc()) + ","
                                + formateadorDecimal.format(af.getValNet()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {//Asegurarnos que se cierra el fichero.
                        if (null != fichero) {
                            fichero.close();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
            //-------------------ARCHIVO US------------------------------
            if (elem.toString().trim().compareTo("US") == 0) {
                listaArchivos.add("us.txt");
                nameAndPathFile = realPath + "us.txt";
                File ficherofile = new File(nameAndPathFile);
                if (ficherofile.exists()) {//Lo Borramos
                    ficherofile.delete();//Lo Borramos
                }
                try {
                    fichero = new FileWriter(nameAndPathFile);
                    pw = new PrintWriter(fichero);
                    List<CfgDiagnostico> listaDiagnosticos = diagnosticoFacade.findAll();
                    pw.println("tip_ide,num_ide,cod_ent_adm,tip_usu,apellido_a,apellido_b,nombre_a,nombre_b,edad,unid_med,,cod_dep_res,cod_mun_res,zona_res");
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
                    e.printStackTrace();
                } finally {
                    try {//Asegurarnos que se cierra el fichero.
                        if (null != fichero) {
                            fichero.close();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
            //-------------------ARCHIVO AP------------------------------
            if (elem.toString().trim().compareTo("AP") == 0) {
                listaArchivos.add("ap.txt");
                nameAndPathFile = realPath + "ap.txt";
                File ficherofile = new File(nameAndPathFile);
                if (ficherofile.exists()) {//Lo Borramos
                    ficherofile.delete();//Lo Borramos
                }
                try {
                    fichero = new FileWriter(nameAndPathFile);
                    pw = new PrintWriter(fichero);
                    List<CfgDiagnostico> listaDiagnosticos = diagnosticoFacade.findAll();
                    pw.println("num_fac,cod_pre,tip_ide,num_ide,fec_proc,num_aut,cod_proced,ambito_pro,finali_pro,pers_atiend,diag_pri,diag_relac,complica,act_quirur,val_proced");
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
                                + ap.getActQuirur());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {//Asegurarnos que se cierra el fichero.
                        if (null != fichero) {
                            fichero.close();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
            //-------------------ARCHIVO AC------------------------------
            if (elem.toString().trim().compareTo("AC") == 0) {
                listaArchivos.add("ac.txt");
                nameAndPathFile = realPath + "ac.txt";
                File ficherofile = new File(nameAndPathFile);
                if (ficherofile.exists()) {//Lo Borramos
                    ficherofile.delete();//Lo Borramos
                }
                try {
                    fichero = new FileWriter(nameAndPathFile);
                    pw = new PrintWriter(fichero);
                    List<CfgDiagnostico> listaDiagnosticos = diagnosticoFacade.findAll();
                    pw.println("num_fac,cod_pre,tip_ide,num_ide,fec_cons,num_aut,cod_proced,finali_con,cau_ext,cdiag_pri,cdiag_r1,cdiag_r2,cdiag_r3,tip_diag_pri,val_cons,val_cuo_mod,val_net");
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
                                + formateadorDecimal.format(ac.getVlrCons()) + ","
                                + formateadorDecimal.format(ac.getVlrCuoMod()) + ","
                                + formateadorDecimal.format(ac.getVlrNeto()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {//Asegurarnos que se cierra el fichero.
                        if (null != fichero) {
                            fichero.close();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
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
            ex.printStackTrace();
        }
        StreamedContent download = new DefaultStreamedContent();
        File file = new File(realPath + "comprimido.zip");
        InputStream input = new FileInputStream(file);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        download = new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName());
        //System.out.println("PREP = " + download.getName());
        return download;
    }

    private String crearRipCT() {
        //REGISTRO DE DATOS PARA EL ARCHIVO DE CONTROL
        String nombreReturn = "";
//    Código del Prestador    
//    Fecha de remisión    
//    Código del archivo    
//    Total de Registros

        return nombreReturn;
    }

    private String crearRipAF() {
        //REGISTRO DE DATOS RELACIONADOS CON LA TRANSACCION DE LOS SERVICIOS FACTURADOS
        String nombreReturn = "";
//    Código del Prestador    
//    Razón Social o Apellidos y nombres del prestador    
//    Tipo de Identificación    
//    Número de Identificación    
//    Número de la factura    
//    Fecha de expedición de la factura    
//    Fecha de Inicio    
//    Fecha final    
//    Código entidad Administradora    
//    Nombre entidad administradora    
//    Número del Contrato    
//    Plan de Beneficios    
//    Número de la póliza    
//    Valor total del pago compartido COPAGO    
//    Valor de la comisión    
//    Valor total de Descuentos    
//    Valor Neto a Pagar por la entidad Contratante

        return nombreReturn;
    }

    private String crearRipUS() {
        //REGISTRO DE DATOS PARA EL ARCHIVO DE USUARIOS DE LOS SERVICIOS DE SALUD
        String nombreReturn = "";
//    Tipo de Identificación del Usuario    
//    Número de Identifiación del Usuario en el Sistema    
//    Código Entidad Administradora    
//    Tipo de Usuario    
//    Primer Apellido del usuario    
//    Segundo apellido del usuario    
//    Primer nombre del usuario    
//    Segundo nombre del usuario    
//    Edad    
//    Unidad de medida de la Edad    
//    Sexo    
//    Código del departamento de residencia habitual    
//    Código de municipios de residencia habitual    
//    Zona de residencia habitual

        return nombreReturn;
    }

    private String crearRipAP() {
        //REGISTRO DE DATOS PARA EL ARCHIVO DE PROCEDIMIENTOS
        String nombreReturn = "";
//    Número de la factura    
//    Código del prestador de servicios de salud    
//    Tipo de identificación del usuario    
//    Número de identificación del usuario en el sistema    
//    Fecha del procedimiento    
//    Número de Autorización    
//    Código del procedimiento    
//    Ambito de realización del procedimiento    
//    Finalidad del procedimiento    
//    Personal que atiende    
//    Diagnóstico prinicipal    
//    Código del diagnóstico relacionado    
//    Complicación    
//    Forma de realización del acto quirúrgico    
//    Valor del Procedimiento

        return nombreReturn;
    }

    private String crearRipAC() {
        //REGISTRO DE DATOS PARA EL ARCHIVO DE CONSULTA
        String nombreReturn = "";
//    Número de la factura    
//    Código del prestador de servicios de salud    
//    Tipo de identificación del usuario    
//    Número de identificación del usuario en el sistema    
//    Fecha de la consulta    
//    Número de Autorización    
//    Código de consulta CuPS    
//    Finalidad de la consulta    
//    Causa externa    
//    Código del Diagnóstico principal    
//    Código del diagnóstico relacionado N° 1    
//    Código del diagnóstico relacionado N° 2    
//    Código del diagnóstico relacionado N° 3    
//    Tipo de diagnóstico principal    
//    Valor de la consulta    
//    Valor de la cuota moderadora    
//    Valor Neto a pagar

        return nombreReturn;
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
