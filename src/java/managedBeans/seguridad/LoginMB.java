/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.seguridad;

import modelo.entidades.CfgOpcionesMenu;
import modelo.entidades.CfgSede;
import modelo.entidades.CfgUsuarios;
import modelo.fachadas.CfgOpcionesMenuFacade;
import modelo.fachadas.CfgSedeFacade;
import modelo.fachadas.CfgUsuariosFacade;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabCloseEvent;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultSubMenu;
import beans.utilidades.MetodosGenerales;
import beans.utilidades.StringEncryption;
import beans.utilidades.Tab;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import managedBeans.Citas.AutorizacionMB;
import managedBeans.configuraciones.EmpresaMB;
import managedBeans.configuraciones.PacientesMB;
import managedBeans.facturacion.AbrirCerrarCajasMB;
import managedBeans.facturacion.ContratosMB;
import managedBeans.facturacion.FacturarAdministradoraMB;
import managedBeans.facturacion.FacturarPacienteMB;
import managedBeans.facturacion.ProgramasMB;
import managedBeans.historias.HistoriasMB;
import modelo.entidades.CfgEmpresa;
import modelo.fachadas.CfgEmpresaFacade;

/**
 *
 * @author Administrador
 */
@Named("loginMB")
@SessionScoped
public class LoginMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    CfgUsuariosFacade usuariosFachada;
    @EJB
    CfgOpcionesMenuFacade opcionesFacade;
    @EJB
    CfgSedeFacade sedesFacade;
    @EJB
    CfgEmpresaFacade empresaFacade;

    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------
    private CfgUsuarios usuarioActual;
    private CfgEmpresa empresaActual;

    //---------------------------------------------------
    //-----------------VARIABLES -------------------------
    //---------------------------------------------------
    private String clave = "";
    private String login = "";
    private boolean autenticado = false;
    private boolean superUsuario = false;//determinar si quien se loguea es super usuario
    private final StringEncryption stringEncryption = new StringEncryption();
    private List<Tab> listadoDeTabs = new ArrayList<>();
    private String indexTabActiva = "0";
    private List<CfgOpcionesMenu> opcionesParaUsuarioSegunPerfil;
    private List<SelectItem> listaCentrosDeAtencion = new ArrayList<>();
    private DefaultMenuModel simpleMenuModel = new DefaultMenuModel();//3155377874
    private String baseDeDatosActual = "Produccion";
    private String centroDeAtencion;//identificador del centro de atencion
    private CfgSede centroDeAtencionactual;
    private String urlFotos = "";//url del directorio para fotos 
    private String urlFirmas = "";//url del directorio para firma
    private String urlTmp = "";//url del directorio para fotos temporales
    private String rutaServidor = "";
    private String imagenUsuario = "";//imagen del usuario que se encuentre logeado
    private String idSession = "";//identificador de la session
    private AplicacionGeneralMB aplicacionGeneralMB;
    private PacientesMB pacientesMB;
    private HistoriasMB historiasMB;
    private ContratosMB contratosMB;
    private ProgramasMB programasMB;
    private AutorizacionMB autorizacionMB;
    private FacturarPacienteMB facturarPacienteMB;
    private FacturarAdministradoraMB facturarAdministradoraMB;
    private AbrirCerrarCajasMB abrirCerrarCajasMB;
    private PaginaInformativaMB paginaInformativaMB;
    private EmpresaMB empresaMB;
    private String rutaCarpetaImagenes;//ruta de la carpeta externa imagenes(se usa asi para poder usar en windows 'C:/imgOpenmedical/' y linux 'home/imgOpenmedical/')
    private String urlTabActual = "";

    //---------------------------------------------------
    //----------------- FUNCIONES -------------------------
    //---------------------------------------------------    
    public LoginMB() {
        aplicacionGeneralMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{aplicacionGeneralMB}", AplicacionGeneralMB.class);

        rutaCarpetaImagenes = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("uploadDirectory");
        if (rutaCarpetaImagenes.compareTo("windows") == 0) {
            rutaCarpetaImagenes = "C:/imagenesOpenmedical/";
        } else {
            rutaCarpetaImagenes = "/home/imagenesOpenmedical/";
        }
    }

    public void inicializar() {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        rutaServidor = (String) servletContext.getRealPath("/");
    }

    public void cargarSedes() {
        listaCentrosDeAtencion = new ArrayList<>();
        if (baseDeDatosActual != null && baseDeDatosActual.length() != 0) {
            List<CfgSede> lista = sedesFacade.buscarOrdenado();
            for (CfgSede sede : lista) {
                listaCentrosDeAtencion.add(new SelectItem(sede.getIdSede(), sede.getNombreSede()));
            }
        }
    }

    @PreDestroy
    public void procesoAntesDeSalir() {
        aplicacionGeneralMB.removeSession(idSession);
    }

    public void cerrarSesion() {
        //fin de sesion dada por el usuario: botón "cerrar cesion"
        try {
            aplicacionGeneralMB.removeSession(idSession);
            ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
            String ctxPath = ((ServletContext) ctx.getContext()).getContextPath();
            ((HttpSession) ctx.getSession(false)).invalidate();
            ctx.redirect(ctxPath + "/index.html");
        } catch (IOException ex) {
            imprimirMensaje("Error", "Excepcion cuando usuario cierra sesion: " + ex.toString(), FacesMessage.SEVERITY_ERROR);
        }
    }

    public String verificarUsuario() {
        //determinar si el usuario puede acceder al sistema //determinando si exite login, clave y la cuenta esta activa
        String claveEncriptada = stringEncryption.getStringMessageDigest(clave, "SHA-1");
        if (baseDeDatosActual == null || baseDeDatosActual.length() == 0) {
            imprimirMensaje("Error", "Seleccione la base de datos", FacesMessage.SEVERITY_ERROR);
            return "";//no se redirecciona, se mantiene en la misma pagina
        }
        if (clave.trim().length() == 0 || login.trim().length() == 0) {//se buscara en la base de datos
            imprimirMensaje("Error", "Debe ingresar usuario y clave", FacesMessage.SEVERITY_ERROR);
            return "";//no se redirecciona, se mantiene en la misma pagina
        }
        usuarioActual = usuariosFachada.buscarPorLoginClave(login, clave);
        empresaActual = empresaFacade.find(1);
        if (usuarioActual == null) {
            imprimirMensaje("Error", "Verifique nombre de usuario y clave", FacesMessage.SEVERITY_ERROR);
            clave = "";
            return "";//no se redirecciona, se mantiene en la misma pagina
        } else if (!usuarioActual.getEstadoCuenta()) {
            imprimirMensaje("Error", "La cuenta de este usuario esta inactiva", FacesMessage.SEVERITY_ERROR);
            clave = "";
            return "";//no se redirecciona, se mantiene en la misma pagina
        } else if (aplicacionGeneralMB.estaLogueado(usuarioActual.getIdUsuario())) {
            RequestContext.getCurrentInstance().execute("PF('closeSessionDialog').show();");//ya esta ingresado en otro equipo
            return "";//no se redirecciona, se mantiene en la misma pagina
        } else {
            superUsuario = usuarioActual.getIdPerfil().getNombrePerfil().compareTo("SuperUsuario") == 0;
            cargarMenu();
            realizarConfiguraciones();
            abrirTab("Inicio", "paginaInformativa.xhtml");
            autenticado = true;
            return "home";//se redirecciona por reglas de navegacion
        }
    }

    public String cerrarSesionAbrirNueva() {
        /*
         * terminar una session iniciada en otra terminal y continuar abriendo una nueva;
         * se usa cuando un mismo usuario intenta loguearse desde dos terminales distintas, 
         */
        aplicacionGeneralMB.removeSession(usuarioActual.getIdUsuario());
        cargarMenu();
        realizarConfiguraciones();
        autenticado = true;
        return "home";//se redirecciona por reglas de navegacion
    }

    private void realizarConfiguraciones() {

        //agregar variables de sesion ('username' se usa en defaultFaseListener)
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", usuarioActual.getLoginUsuario());
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        idSession = session.getId();
        aplicacionGeneralMB.addSession(usuarioActual.getIdUsuario(), idSession);

        //cada DB debe tener propios directorios para imagenes de no existir se crean        
        urlFotos = rutaCarpetaImagenes + getBaseDeDatosActual() + "/fotos/";
        File directorio = new File(urlFotos);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
        urlTmp = rutaCarpetaImagenes + getBaseDeDatosActual() + "/tmp/";
        directorio = new File(urlTmp);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
        urlFirmas = rutaCarpetaImagenes + getBaseDeDatosActual() + "/firmas/";
        directorio = new File(urlFirmas);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
        //se carga la imagen del usuario
        if (usuarioActual.getFoto() != null) {
            imagenUsuario = rutaCarpetaImagenes + usuarioActual.getFoto().getUrlImagen();
        } else {
            imagenUsuario = "../recursos/img/img_usuario.png";
        }
        //se carga carga el centro de atencion actual
        centroDeAtencionactual = sedesFacade.find(Integer.parseInt(centroDeAtencion));
        pacientesMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{pacientesMB}", PacientesMB.class);

        contratosMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{contratosMB}", ContratosMB.class);
        programasMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{programasMB}", ProgramasMB.class);

        abrirCerrarCajasMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{abrirCerrarCajasMB}", AbrirCerrarCajasMB.class);
        abrirCerrarCajasMB.setLoginMB(this);
        
        paginaInformativaMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{paginaInformativaMB}", PaginaInformativaMB.class);
        paginaInformativaMB.setLoginMB(this);

        facturarPacienteMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{facturarPacienteMB}", FacturarPacienteMB.class);
        facturarPacienteMB.setLoginMB(this);

        facturarAdministradoraMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{facturarAdministradoraMB}", FacturarAdministradoraMB.class);
        facturarAdministradoraMB.setLoginMB(this);

        empresaMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{empresaMB}", EmpresaMB.class);
        empresaMB.setLoginMB(this);

        historiasMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{historiasMB}", HistoriasMB.class);
        historiasMB.setLoginMB(this);
        historiasMB.setBtnEditarRendered(superUsuario);
    }

    private void cargarMenu() {//crea las opciones del menu principal
        simpleMenuModel = new DefaultMenuModel();
        if (usuarioActual != null) {
            opcionesParaUsuarioSegunPerfil = usuarioActual.getIdPerfil().getCfgOpcionesMenuList();
            Collections.sort(opcionesParaUsuarioSegunPerfil, new Comparator() {//uso de collections.sort para realizar el ordenamiento de esta lista
                @Override
                public int compare(Object o1, Object o2) {
                    CfgOpcionesMenu a = (CfgOpcionesMenu) o1;
                    CfgOpcionesMenu b = (CfgOpcionesMenu) o2;
                    return a.getIdOpcionMenu().compareTo(b.getIdOpcionMenu());
                }
            });

            for (CfgOpcionesMenu opcion : opcionesParaUsuarioSegunPerfil) {
                if (opcion.getIdOpcionMenu() != 0 && opcion.getIdOpcionPadre().getIdOpcionMenu() == 0) {//no es raiz; pero padre si es raiz
                    if (opcion.getUrlOpcion() == null || opcion.getUrlOpcion().length() == 0) {//tiene hijos sera submenu
                        DefaultSubMenu sm = new DefaultSubMenu();
                        sm.setLabel(opcion.getNombreOpcion());
                        if (opcion.getStyle() != null) {//se usa style para saber icono tipo jquery-ui
                            sm.setIcon(opcion.getStyle());
                        }
                        for (CfgOpcionesMenu op : opcion.getCfgOpcionesMenuList()) {
                            agregarSubOpciones(op, sm);
                        }
                        simpleMenuModel.addElement(sm);
                    } else {//no tiene hijos es menu item
                        DefaultMenuItem mi = new DefaultMenuItem();
                        mi.setTitle(opcion.getNombreOpcion());
                        mi.setValue(opcion.getNombreOpcion());
                        mi.setUpdate(":IdFormCenter");
                        if (opcion.getStyle() != null) {//se usa style para saber icono tipo jquery-ui
                            mi.setIcon(opcion.getStyle());
                        }
                        mi.setCommand("#{loginMB.abrirTab('" + opcion.getNombreOpcion() + "','" + opcion.getUrlOpcion() + "')}");
                        simpleMenuModel.addElement(mi);
                    }
                }
            }
        }
    }

    private void agregarSubOpciones(CfgOpcionesMenu opcion, DefaultSubMenu submenu) {
        if (buscarOpcionSegunPerfil(opcion)) {//determinar si tiene permisos para esta opcion
            if (opcion.getUrlOpcion() == null || opcion.getUrlOpcion().length() == 0) {//debe tener hijos es un submenu
                DefaultSubMenu sm = new DefaultSubMenu();
                sm.setLabel(opcion.getNombreOpcion());
                if (opcion.getStyle() != null) {//se usa style para saber icono tipo jquery-ui
                    sm.setIcon(opcion.getStyle());
                }
                for (CfgOpcionesMenu op : opcion.getCfgOpcionesMenuList()) {
                    agregarSubOpciones(op, sm);
                }
                submenu.getElements().add(sm);
            } else {//no tiene hijos es item
                DefaultMenuItem mi = new DefaultMenuItem();
                mi.setTitle(opcion.getNombreOpcion());
                mi.setValue(opcion.getNombreOpcion());
                mi.setUpdate(":IdFormCenter");
                if (opcion.getStyle() != null) {//se usa style para saber icono tipo jquery-ui
                    mi.setIcon(opcion.getStyle());
                }
                mi.setCommand("#{loginMB.abrirTab('" + opcion.getNombreOpcion() + "','" + opcion.getUrlOpcion() + "')}");
                submenu.getElements().add(mi);
            }
        }
    }

    private boolean buscarOpcionSegunPerfil(CfgOpcionesMenu opcion) {//determinar si una opcion del menu debe mostrarce segun el perfil del ususario
        for (CfgOpcionesMenu op : opcionesParaUsuarioSegunPerfil) {
            if (Objects.equals(op.getIdOpcionMenu(), opcion.getIdOpcionMenu())) {
                return true;
            }
        }
        return false;
    }

    public void cargarPacienteDesdeHistorias() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String titulo = params.get("titulo");
        String url = params.get("url");
        String idPaciente = params.get("idPaciente");
        pacientesMB.cargarPacienteDesdeHistorias(idPaciente);
        abrirTab(titulo, url);
    }

    public void cargarTab() {
        //permite cargar un Tab de menu principal invocandolo desde otro tab del menu principal y llevando parametros        
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String titulo = params.get("titulo");//me dice que managedBean debo usar
        String url = params.get("url");
        String id = params.get("id");//id puede contener varios identificacors se estan separados por '|' 
        switch (titulo) {
            case "Pacientes":
                pacientesMB.cargarPacienteDesdeHistorias(id);
                break;
            case "Contratos":
                contratosMB.cargarDesdeTab(id);
                break;
            case "Programas":
                programasMB.cargarDesdeTab(id);
                break;
            case "Facturar Paciente":
                facturarPacienteMB.cargarDesdeTab(id);
                break;
            case "Historias Clinicas"://Cargar historia clinica desde agenda del prestador o desde home
                historiasMB.cargarDesdeTab(id);
                break;
            case "Autorizaciones":
//                System.out.println("pacienteId->"+id);
                autorizacionMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{autorizacionMB}", AutorizacionMB.class);
                autorizacionMB.cargarDesdeTab(id);
                break;

        }
        abrirTab(titulo, url);
    }

    public void abrirTab(String titulo, String url) {//se quiere agregar un tab al tabView central
        try {
            boolean tabYaEstaAgregada = false;
            int posicion = -1;
            for (Tab tabActual : listadoDeTabs) {
                posicion++;
                if (tabActual.getTitulo().equals(titulo) && tabActual.getUrl().equals(url)) {
                    tabYaEstaAgregada = true;
                    break;
                }
            }
            if (tabYaEstaAgregada) {
                indexTabActiva = String.valueOf(posicion);
                urlTabActual = listadoDeTabs.get(posicion).getUrl();
            } else {
                listadoDeTabs.add(new Tab(titulo, url));
                urlTabActual = listadoDeTabs.get(posicion + 1).getUrl();
                indexTabActiva = String.valueOf(posicion + 1);
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Setear Pagina: " + e.toString(), ""));
        }
    }

    public void cambiaTab() {//se selecciona una tab del tabView central
        urlTabActual = listadoDeTabs.get(Integer.parseInt(indexTabActiva)).getUrl();
    }

    public int cerrarTab(TabCloseEvent event) {//se quiere cerrar una pagina agregada en las tabulaciones
        try {
            int posicionTabEliminar = -1;
            int posicionTabActiva = Integer.parseInt(indexTabActiva);
            boolean esLaActiva = false;
            boolean esLaPrimera = false;
            boolean esLaUltima = false;
            boolean hayMasDeUna = false;
            boolean esMayorActivaQueEliminar = false;

            if (event.getTab() == null) {//System.err.println("ES NULL");
                //urlTabActual = "paginaInformativa.xhtml";
                return 0;
            }
            String titulo = event.getTab().getTitle();
            for (Tab tabActual : listadoDeTabs) {
                posicionTabEliminar++;
                if (tabActual.getTitulo().equals(titulo)) {
                    if (posicionTabActiva == posicionTabEliminar) {//se esta cerrando la pagina activa
                        esLaActiva = true;
                    } else {
                        if (posicionTabActiva > posicionTabEliminar) {
                            esMayorActivaQueEliminar = true;
                        }
                    }
                    if (listadoDeTabs.size() > 1) {
                        hayMasDeUna = true;
                    }
                    if (posicionTabEliminar == 0) {
                        esLaPrimera = true;
                    }
                    if (posicionTabEliminar == (listadoDeTabs.size() - 1)) {
                        esLaUltima = true;
                    }
                    if (esLaPrimera) {
                        if (hayMasDeUna) {
                            if (esLaActiva) {
                                indexTabActiva = "0";
                                listadoDeTabs.remove(tabActual);
                                urlTabActual = listadoDeTabs.get(0).getUrl();
                                return 0;
                            } else {
                                indexTabActiva = String.valueOf(posicionTabActiva - 1);
                                listadoDeTabs.remove(tabActual);
                                urlTabActual = listadoDeTabs.get(posicionTabActiva - 1).getUrl();
                                return 0;
                            }
                        } else {
                            urlTabActual = "";
                        }
                    }
                    if (esLaUltima) {//ultima posicion                        
                        if (hayMasDeUna) {
                            if (esLaActiva) {
                                indexTabActiva = String.valueOf(listadoDeTabs.size() - 2);
                                listadoDeTabs.remove(tabActual);
                                urlTabActual = listadoDeTabs.get(listadoDeTabs.size() - 1).getUrl();
                                return 0;
                            }
                        }
                    } else {//posicion en medio
                        if (esLaActiva) {
                            indexTabActiva = String.valueOf(posicionTabActiva - 1);
                            listadoDeTabs.remove(tabActual);
                            urlTabActual = listadoDeTabs.get(posicionTabActiva - 1).getUrl();
                            return 0;
                        } else {
                            if (esMayorActivaQueEliminar) {
                                indexTabActiva = String.valueOf(posicionTabActiva - 1);
                                listadoDeTabs.remove(tabActual);
                                urlTabActual = listadoDeTabs.get(posicionTabActiva - 1).getUrl();
                                return 0;
                            }
                        }
                    }
                    listadoDeTabs.remove(posicionTabEliminar);
                    return 0;
                }
            }
        } catch (NumberFormatException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cerrar Tab: " + e.toString(), ""));
        }
        return 0;
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------
    public List<Tab> getListadoDeTabs() {
        return listadoDeTabs;
    }

    public void setListadoDeTabs(List<Tab> listadoDePaginas) {
        this.listadoDeTabs = listadoDePaginas;
    }

    public String getIndexTabActiva() {
        return indexTabActiva;
    }

    public void setIndexTabActiva(String indexTabActiva) {
        this.indexTabActiva = indexTabActiva;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isAutenticado() {
        return autenticado;
    }

    public void setAutenticado(boolean autenticado) {
        this.autenticado = autenticado;
    }

    public CfgUsuarios getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(CfgUsuarios usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public DefaultMenuModel getSimpleMenuModel() {
        return simpleMenuModel;
    }

    public void setSimpleMenuModel(DefaultMenuModel simpleMenuModel) {
        this.simpleMenuModel = simpleMenuModel;
    }

    public String getBaseDeDatosActual() {
        return baseDeDatosActual;
    }

    public void setBaseDeDatosActual(String baseDeDatosActual) {
        this.baseDeDatosActual = baseDeDatosActual;
    }

    public List<SelectItem> getListaCentrosDeAtencion() {
        return listaCentrosDeAtencion;
    }

    public void setListaCentrosDeAtencion(List<SelectItem> listaCentrosDeAtencion) {
        this.listaCentrosDeAtencion = listaCentrosDeAtencion;
    }

    public String getCentroDeAtencion() {
        return centroDeAtencion;
    }

    public void setCentroDeAtencion(String centroDeAtencion) {
        this.centroDeAtencion = centroDeAtencion;
    }

    public String getRutaServidor() {
        return rutaServidor;
    }

    public void setRutaServidor(String rutaServidor) {
        this.rutaServidor = rutaServidor;
    }

    public String getUrlFotos() {
        return urlFotos;
    }

    public void setUrlFotos(String urlFotos) {
        this.urlFotos = urlFotos;
    }

    public String getUrlFirmas() {
        return urlFirmas;
    }

    public void setUrlFirmas(String urlFirmas) {
        this.urlFirmas = urlFirmas;
    }

    public String getUrltmp() {
        return urlTmp;
    }

    public void setUrltmp(String urltmp) {
        this.urlTmp = urltmp;
    }

    public String getImagenUsuario() {
        return imagenUsuario;
    }

    public void setImagenUsuario(String imagenUsuario) {
        this.imagenUsuario = imagenUsuario;
    }

    public String getIdSession() {
        return idSession;
    }

    public void setIdSession(String idSession) {
        this.idSession = idSession;
    }

    public AplicacionGeneralMB getAplicacionGeneralMB() {
        return aplicacionGeneralMB;
    }

    public void setAplicacionGeneralMB(AplicacionGeneralMB aplicacionGeneralMB) {
        this.aplicacionGeneralMB = aplicacionGeneralMB;
    }

    public CfgSede getCentroDeAtencionactual() {
        return centroDeAtencionactual;
    }

    public void setCentroDeAtencionactual(CfgSede centroDeAtencionactual) {
        this.centroDeAtencionactual = centroDeAtencionactual;
    }

    public String getRutaCarpetaImagenes() {
        return rutaCarpetaImagenes;
    }

    public void setRutaCarpetaImagenes(String rutaCarpetaImagenes) {
        this.rutaCarpetaImagenes = rutaCarpetaImagenes;
    }

    public String getUrlTabActual() {
        return urlTabActual;
    }

    public void setUrlTabActual(String urlTabActual) {
        this.urlTabActual = urlTabActual;
    }

    public CfgEmpresa getEmpresaActual() {
        return empresaActual;
    }

    public void setEmpresaActual(CfgEmpresa empresaActual) {
        this.empresaActual = empresaActual;
    }

    public boolean isSuperUsuario() {
        return superUsuario;
    }

    public void setSuperUsuario(boolean superUsuario) {
        this.superUsuario = superUsuario;
    }

}
