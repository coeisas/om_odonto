package managedBeans.seguridad;


import modelo.entidades.CfgClasificaciones;
import modelo.entidades.CfgOpcionesMenu;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgOpcionesMenuFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import beans.utilidades.MetodosGenerales;

@ManagedBean(name = "opcionesMenuMB")
@SessionScoped
public class OpcionesMenuMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    CfgOpcionesMenuFacade opcionesFachada;
    @EJB
    CfgClasificacionesFacade iconosFacade;

    //---------------------------------------------------
    //-----------------ENTIDADES ------------------------
    //---------------------------------------------------
    private List<CfgOpcionesMenu> listaOpciones;
    CfgOpcionesMenu opcionSeleccionada = null;
    CfgOpcionesMenu nuevaOpcion;
    private List<CfgClasificaciones> listaIconos;
    CfgClasificaciones iconoSeleccionado = null;

    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------
    private final LoginMB loginMB;
    private String id;
    private String idOpcionPadre = "";
    private String nombreOpcion = "";
    private String style = "";
    private String urlOpcion = "";//50
    private String tituloFormulario = "Nueva opción";
    private List<SelectItem> listaOpcionesPadre;

    //---------------------------------------------------
    //----------------- FUNCIONES -----------------------
    //---------------------------------------------------    
    @PostConstruct
    public void inicializar() {
        setTituloFormulario("Nueva opción");
        cargarListaOpcionesPadre();
        listaIconos = iconosFacade.buscarPorMaestro("Icono");
        listaOpciones = opcionesFachada.findAll();
        opcionSeleccionada = null;
    }

    private void cargarListaOpcionesPadre() {
        listaOpcionesPadre = new ArrayList<>();
        List<CfgOpcionesMenu> listaOp = opcionesFachada.findAll();
        for (CfgOpcionesMenu op : listaOp) {
            listaOpcionesPadre.add(new SelectItem(op.getIdOpcionMenu(), op.getNombreOpcion()));
        }
    }

//    private void cargarListaIconos() {
//        listaIconos = new ArrayList<>();
//        List<Iconos> listaIc = iconosFacade.findAll();
//        for (Iconos ic : listaIc) {
//            listaIconos.add(new SelectItem(ic.getIconoId(), ic.getIconoNombre()));
//        }
//    }
    public OpcionesMenuMB() {
        loginMB = (LoginMB) FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
    }

    public void nuevaOpcion() {
        opcionSeleccionada = null;
        setTituloFormulario("Nueva opción");
        id = "";
        idOpcionPadre = "";
        nombreOpcion = "";
        style = "";
        urlOpcion = "";
    }

    public void cargarIcono() {
        if (iconoSeleccionado == null) {
            imprimirMensaje("Error", "Se debe seleccionar un icono de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        style = iconoSeleccionado.getDescripcion();
    }

    public void cargarOpcion() {
        if (opcionSeleccionada == null) {
            imprimirMensaje("Error", "Se debe seleccionar un usuario de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        setTituloFormulario("Datos opción");
        id = opcionSeleccionada.getIdOpcionMenu().toString();
        idOpcionPadre = opcionSeleccionada.getIdOpcionPadre().getIdOpcionMenu().toString();
        nombreOpcion = opcionSeleccionada.getNombreOpcion();
        style = opcionSeleccionada.getStyle();
        urlOpcion = opcionSeleccionada.getUrlOpcion();
    }

    public void guardarOpcion() {
        //VALIDACION DE DATOS OBLIGATORIOS        
        //VALIDACION DE VALORES UNICOS
        if (tituloFormulario.contains("Nueva")) {//guardando nuevo opcion del menu                        
            guardarNuevaOpcion();
        } else {//modificando opcion existente            
            actualizarOpcionExistente();
        }
    }

    private void guardarNuevaOpcion() {
        nuevaOpcion = new CfgOpcionesMenu();
        if (validarNoVacio(idOpcionPadre)) {
            nuevaOpcion.setIdOpcionPadre(opcionesFachada.find(Integer.parseInt(idOpcionPadre)));
        }
        nuevaOpcion.setNombreOpcion(nombreOpcion);
        nuevaOpcion.setStyle(style);
        nuevaOpcion.setUrlOpcion(urlOpcion);
        opcionesFachada.create(nuevaOpcion);
        imprimirMensaje("Correcto", "Nueva opción creada correctamente", FacesMessage.SEVERITY_INFO);
        listaOpciones = opcionesFachada.findAll();
        nuevaOpcion();//limpiar formulario
    }

    private void actualizarOpcionExistente() {

        if (validarNoVacio(idOpcionPadre)) {
            opcionSeleccionada.setIdOpcionPadre(opcionesFachada.find(Integer.parseInt(idOpcionPadre)));
        }

        opcionSeleccionada.setNombreOpcion(nombreOpcion);
        opcionSeleccionada.setStyle(style);
        opcionSeleccionada.setUrlOpcion(urlOpcion);
        opcionesFachada.edit(opcionSeleccionada);
        imprimirMensaje("Correcto", "Opcion actualizada correctamente", FacesMessage.SEVERITY_INFO);
        listaOpciones = opcionesFachada.findAll();
        nuevaOpcion();//limpiar formulario
    }

    public void eliminarOpcion() {
        if (opcionSeleccionada == null) {
            imprimirMensaje("Error", "No se ha cargado ninguna opción", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            opcionesFachada.remove(opcionSeleccionada);
            opcionSeleccionada = null;
            listaOpciones = opcionesFachada.findAll();
            nuevaOpcion();//limpiar formulario
            imprimirMensaje("Correcto", "El registro fue eliminado", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "La opción que se intenta eliminar tiene actividades dentro del sistema; por lo cual no puede ser eliminado.", FacesMessage.SEVERITY_ERROR);
        }
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------
    public String getTituloFormulario() {
        return tituloFormulario;
    }

    public void setTituloFormulario(String tituloFormulario) {
        this.tituloFormulario = tituloFormulario;
    }

    public List<CfgOpcionesMenu> getListaOpciones() {
        return listaOpciones;
    }

    public void setListaOpciones(List<CfgOpcionesMenu> listaOpciones) {
        this.listaOpciones = listaOpciones;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdOpcionPadre() {
        return idOpcionPadre;
    }

    public void setIdOpcionPadre(String idOpcionPadre) {
        this.idOpcionPadre = idOpcionPadre;
    }

    public String getNombreOpcion() {
        return nombreOpcion;
    }

    public void setNombreOpcion(String nombreOpcion) {
        this.nombreOpcion = nombreOpcion;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getUrlOpcion() {
        return urlOpcion;
    }

    public void setUrlOpcion(String urlOpcion) {
        this.urlOpcion = urlOpcion;
    }

    public CfgOpcionesMenu getOpcionSeleccionada() {
        return opcionSeleccionada;
    }

    public void setOpcionSeleccionada(CfgOpcionesMenu opcionSeleccionada) {
        this.opcionSeleccionada = opcionSeleccionada;
    }

    public List<SelectItem> getListaOpcionesPadre() {
        return listaOpcionesPadre;
    }

    public void setListaOpcionesPadre(List<SelectItem> listaOpcionesPadre) {
        this.listaOpcionesPadre = listaOpcionesPadre;
    }

    public List<CfgClasificaciones> getListaIconos() {
        return listaIconos;
    }

    public void setListaIconos(List<CfgClasificaciones> listaIconos) {
        this.listaIconos = listaIconos;
    }

    public CfgClasificaciones getIconoSeleccionado() {
        return iconoSeleccionado;
    }

    public void setIconoSeleccionado(CfgClasificaciones iconoSeleccionado) {
        this.iconoSeleccionado = iconoSeleccionado;
    }

}
