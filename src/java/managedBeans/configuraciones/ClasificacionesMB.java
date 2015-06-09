package managedBeans.configuraciones;

import beans.enumeradores.ClasificacionesEnum;
import modelo.entidades.CfgClasificaciones;
import modelo.fachadas.CfgClasificacionesFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import beans.utilidades.MetodosGenerales;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import managedBeans.Citas.AutorizacionMB;
import managedBeans.seguridad.AplicacionGeneralMB;
import modelo.entidades.CfgMaestrosClasificaciones;
import modelo.fachadas.CfgMaestrosClasificacionesFacade;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "clasificacionesMB")
@SessionScoped
public class ClasificacionesMB extends MetodosGenerales implements Serializable {
    
    
    
    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    CfgMaestrosClasificacionesFacade clasificacionesFacade;
    @EJB
    CfgClasificacionesFacade categoriasFacade;
    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------
    private List<CfgMaestrosClasificaciones> listaClasificaciones;
    private List<CfgClasificaciones> listaCategorias;
    private String nombreMaestroSeleccionado = "";
    private CfgClasificaciones categoriaSeleccionada;
    private CfgMaestrosClasificaciones clasificacionSeleccionada;

    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------
    private String nombreNuevaClasificacion = "";
    private String nombreClasificacionSeleccionada = "";
    private String nombreCategoriaSeleccionada = "";
    private boolean renderedTabClasificacion = false;
    private boolean btnDisabled = true;

    private String codigoCategoria = "";
    private String descripcionCategoria = "";
    private String observacionCategoria = "";
    private AplicacionGeneralMB aplicacionGeneralMB;

    //---------------------------------------------------
    //----------------- FUNCIONES -------------------------
    //---------------------------------------------------      
    public ClasificacionesMB() {
        //Un ejemplo de clasificacion es GENERO <=>  sus categorias son FEMENINO,MASCULINO
        aplicacionGeneralMB=FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{aplicacionGeneralMB}", AplicacionGeneralMB.class);
    }

    @PostConstruct
    public void inicializar() {
        listaClasificaciones = clasificacionesFacade.buscarOrdenado();
    }

    public void cargarClasificacion() {
        if (clasificacionSeleccionada == null) {
            imprimirMensaje("Error", "Se debe seleccionar una clasificación de la tabla", FacesMessage.SEVERITY_ERROR);
            renderedTabClasificacion = false;
            nombreClasificacionSeleccionada = "";
            listaCategorias = new ArrayList<>();
            return;
        }
        btnDisabled = true;
        categoriaSeleccionada = null;
        RequestContext.getCurrentInstance().execute("PF('dialogoBuscarClasificacion').hide(); PF('wvTablaClasificaciones').clearFilters(); PF('wvTablaClasificaciones').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("IdFormClasificaciones");
        renderedTabClasificacion = true;
        nombreClasificacionSeleccionada = clasificacionSeleccionada.getMaestro();
        listaCategorias = categoriasFacade.buscarPorMaestro(nombreClasificacionSeleccionada);
    }

    public void crearClasificaion() {
        if (nombreNuevaClasificacion == null || nombreNuevaClasificacion.trim().length() == 0) {
            imprimirMensaje("Error", "El campo nombre es obligatorio", FacesMessage.SEVERITY_ERROR);
            return;
        }
        CfgMaestrosClasificaciones buscado = clasificacionesFacade.buscarPorNombre(nombreNuevaClasificacion);
        if (buscado == null) {
            clasificacionesFacade.create(new CfgMaestrosClasificaciones(nombreNuevaClasificacion));
            inicializar();//
            clasificacionSeleccionada = clasificacionesFacade.find(nombreNuevaClasificacion);
            cargarClasificacion();//recargue categorias
            nombreNuevaClasificacion = "";
            RequestContext.getCurrentInstance().execute("PF('dialogoNuevaClasificacion').hide();");
            imprimirMensaje("Correcto", "La clasificación se ha creado.", FacesMessage.SEVERITY_INFO);
        } else {
            imprimirMensaje("Error", "Ya existe una clasificación con este nombre", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void cambiaClasificacion() {
        categoriaSeleccionada = null;
        btnDisabled = true;
        listaCategorias = new ArrayList<>();
        //listaCategoriasFiltro = new ArrayList<>();
        if (nombreMaestroSeleccionado.length() != 0) {
            listaCategorias = categoriasFacade.buscarPorMaestro(nombreMaestroSeleccionado);
            //listaCategoriasFiltro.addAll(listaCategorias);        
        }
    }

    public void seleccionaCategoria() {
        btnDisabled = false;
    }

    public void eliminarClasificacion() {
        if (clasificacionSeleccionada == null) {
            imprimirMensaje("Error", "No se ha cargado ningúna clasificación", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoEliminarClasificacion').show()");

    }

    public void confirmarEliminarClasificacion() {
        for (CfgClasificaciones categoria : clasificacionSeleccionada.getCfgClasificacionesList()) {
            try {
                categoriasFacade.remove(categoria);
            } catch (Exception e) {
                imprimirMensaje("Error", "La clasificación no pudo ser eliminada, por que una o mas de sus categorias estan siendo usadas ", FacesMessage.SEVERITY_ERROR);
                return;
            }
        }
        clasificacionSeleccionada = clasificacionesFacade.find(clasificacionSeleccionada.getMaestro());
        clasificacionesFacade.remove(clasificacionSeleccionada);
        inicializar();
        clasificacionSeleccionada = null;
        categoriaSeleccionada = null;
        renderedTabClasificacion = false;
        nombreClasificacionSeleccionada = "";
        listaCategorias = new ArrayList<>();
        RequestContext.getCurrentInstance().execute("PF('dialogoEliminarClasificacion').hide()");
        //RequestContext.getCurrentInstance().update("IdFormClasificaciones:IdTabView");
        imprimirMensaje("Correcto", "La clasificación se ha eliminado", FacesMessage.SEVERITY_ERROR);
    }

    public void confirmaEliminarClasificacion() {
        try {
            clasificacionesFacade.remove(clasificacionSeleccionada);
            clasificacionSeleccionada = null;
            listaClasificaciones = clasificacionesFacade.buscarOrdenado();
            //limpiarFormularioClasificacion();//limpiar formulario
            imprimirMensaje("Correcto", "La clasificación ha sido eliminada", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "La clasificación que se intenta eliminar está siendo utilizada dentro del sistema; por lo cual no se puede eliminar.", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void nuevaCategoria() {//clic en boton nuevo(solo se limpia formulario)
        limpiarCategoria();
        RequestContext.getCurrentInstance().update("IdFormClasificaciones:IdTabView");
        RequestContext.getCurrentInstance().update("IdFormDialogoNuevaCategoria:IdPanelNuevaCategoria");
        RequestContext.getCurrentInstance().update("IdFormDialogoEditarCategoria:IdPanelEditarCategoria");
        RequestContext.getCurrentInstance().execute("PF('dialogoNuevaCategoria').show();");
    }

    public void crearCategoria() {//clicl en boton crar, se crea la categoria
        if (validacionCampoVacio(codigoCategoria, "Código")) {
            return;
        }
        if (validacionCampoVacio(descripcionCategoria, "Descipción")) {
            return;
        }
        CfgClasificaciones nuevaClasificacion = new CfgClasificaciones();
        nuevaClasificacion.setCodigo(codigoCategoria);
        nuevaClasificacion.setDescripcion(descripcionCategoria);
        nuevaClasificacion.setObservacion(observacionCategoria);
        nuevaClasificacion.setMaestro(clasificacionSeleccionada);
        categoriasFacade.create(nuevaClasificacion);
        if (ClasificacionesEnum.convert(clasificacionSeleccionada.getMaestro()) != ClasificacionesEnum.NOVALUE) {//se recarga la categoria en aplicacionGeneralMB
            aplicacionGeneralMB.cargarClasificacion(ClasificacionesEnum.convert(clasificacionSeleccionada.getMaestro()));
        }
        cargarListaCategorias();
        limpiarCategoria();
        RequestContext.getCurrentInstance().update("IdFormClasificaciones:IdTabView");
        RequestContext.getCurrentInstance().update("IdFormDialogoNuevaCategoria:IdPanelNuevaCategoria");
        RequestContext.getCurrentInstance().update("IdFormDialogoEditarCategoria:IdPanelEditarCategoria");
        RequestContext.getCurrentInstance().execute("PF('dialogoNuevaCategoria').hide();");
        imprimirMensaje("Correcto", "La categoría ha sido creada", FacesMessage.SEVERITY_INFO);
    }

    public void actualizarCategoria() {//clicl en actualiza de dialogo, se realiza la actualizacion de la categoria
        if (categoriaSeleccionada == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningúna categoría", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validacionCampoVacio(codigoCategoria, "Código")) {
            return;
        }
        if (validacionCampoVacio(descripcionCategoria, "Descipción")) {
            return;
        }
        categoriaSeleccionada.setCodigo(codigoCategoria);
        categoriaSeleccionada.setDescripcion(descripcionCategoria);
        categoriaSeleccionada.setObservacion(observacionCategoria);
        categoriaSeleccionada.setMaestro(clasificacionSeleccionada);
        categoriasFacade.edit(categoriaSeleccionada);
        if (ClasificacionesEnum.convert(clasificacionSeleccionada.getMaestro()) != ClasificacionesEnum.NOVALUE) {//se recarga la categoria en aplicacionGeneralMB
            aplicacionGeneralMB.cargarClasificacion(ClasificacionesEnum.convert(clasificacionSeleccionada.getMaestro()));
        }
        cargarListaCategorias();
        limpiarCategoria();

        RequestContext.getCurrentInstance().update("IdFormClasificaciones:IdTabView");
        RequestContext.getCurrentInstance().update("IdFormDialogoNuevaCategoria:IdPanelNuevaCategoria");
        RequestContext.getCurrentInstance().update("IdFormDialogoEditarCategoria:IdPanelEditarCategoria");
        RequestContext.getCurrentInstance().execute("PF('dialogoEditarCategoria').hide();");
        imprimirMensaje("Correcto", "La categoría ha sido actualizada.", FacesMessage.SEVERITY_INFO);
    }

    public void editarCategoria() {//clic en boton editar(se cargan valores)  
        if (categoriaSeleccionada == null) {
            imprimirMensaje("Error", "No se ha seleccionado ninguna categoría de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        codigoCategoria = categoriaSeleccionada.getCodigo();
        descripcionCategoria = categoriaSeleccionada.getDescripcion();
        observacionCategoria = categoriaSeleccionada.getObservacion();
        if (ClasificacionesEnum.convert(clasificacionSeleccionada.getMaestro()) != ClasificacionesEnum.NOVALUE) {//se recarga la categoria en aplicacionGeneralMB
            aplicacionGeneralMB.cargarClasificacion(ClasificacionesEnum.convert(clasificacionSeleccionada.getMaestro()));
        }
        RequestContext.getCurrentInstance().update("IdFormDialogoEditarCategoria:IdPanelEditarCategoria");
        RequestContext.getCurrentInstance().execute("PF('dialogoEditarCategoria').show();");
    }

    private void cargarListaCategorias() {
        if (clasificacionSeleccionada == null) {
            imprimirMensaje("Error", "No se ha seleccionado una categoría", FacesMessage.SEVERITY_ERROR);
            listaCategorias = new ArrayList<>();
            return;
        }
        clasificacionSeleccionada = clasificacionesFacade.find(clasificacionSeleccionada.getMaestro());
        listaCategorias = clasificacionSeleccionada.getCfgClasificacionesList();
    }

    public void limpiarCategoria() {
        codigoCategoria = "";
        descripcionCategoria = "";
        observacionCategoria = "";
        categoriaSeleccionada = null;
        btnDisabled = true;
    }

    public void eliminarCategoria() {
        if (categoriaSeleccionada == null) {
            imprimirMensaje("Error", "No se ha seleccionado una categoría", FacesMessage.SEVERITY_INFO);
            return;
        }
        categoriasFacade.remove(categoriaSeleccionada);
        if (ClasificacionesEnum.convert(clasificacionSeleccionada.getMaestro()) != ClasificacionesEnum.NOVALUE) {//se recarga la categoria en aplicacionGeneralMB
            aplicacionGeneralMB.cargarClasificacion(ClasificacionesEnum.convert(clasificacionSeleccionada.getMaestro()));
        }
        cargarListaCategorias();
        limpiarCategoria();
        imprimirMensaje("Correcto", "La categoría ha sido eliminada", FacesMessage.SEVERITY_INFO);
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------
    public List<CfgMaestrosClasificaciones> getListaClasificaciones() {
        return listaClasificaciones;
    }

    public void setListaClasificaciones(List<CfgMaestrosClasificaciones> listaClasificaciones) {
        this.listaClasificaciones = listaClasificaciones;
    }

    public CfgMaestrosClasificaciones getClasificacionSeleccionada() {
        return clasificacionSeleccionada;
    }

    public void setClasificacionSeleccionada(CfgMaestrosClasificaciones clasificacionSeleccionada) {
        this.clasificacionSeleccionada = clasificacionSeleccionada;
    }

    public String getNombreMaestroSeleccionado() {
        return nombreMaestroSeleccionado;
    }

    public void setNombreMaestroSeleccionado(String nombreMaestroSeleccionado) {
        this.nombreMaestroSeleccionado = nombreMaestroSeleccionado;
    }

    public List<CfgClasificaciones> getListaCategorias() {
        return listaCategorias;
    }

    public void setListaCategorias(List<CfgClasificaciones> listaCategorias) {
        this.listaCategorias = listaCategorias;
    }

    public CfgClasificaciones getCategoriaSeleccionada() {
        return categoriaSeleccionada;
    }

    public void setCategoriaSeleccionada(CfgClasificaciones categoriaSeleccionada) {
        this.categoriaSeleccionada = categoriaSeleccionada;
    }

    public boolean isBtnDisabled() {
        return btnDisabled;
    }

    public void setBtnDisabled(boolean btnDisabled) {
        this.btnDisabled = btnDisabled;
    }

    public boolean isRenderedTabClasificacion() {
        return renderedTabClasificacion;
    }

    public void setRenderedTabClasificacion(boolean renderedTabClasificacion) {
        this.renderedTabClasificacion = renderedTabClasificacion;
    }

    public String getNombreNuevaClasificacion() {
        return nombreNuevaClasificacion;
    }

    public void setNombreNuevaClasificacion(String nombreNuevaClasificacion) {
        this.nombreNuevaClasificacion = nombreNuevaClasificacion;
    }

    public String getNombreClasificacionSeleccionada() {
        return nombreClasificacionSeleccionada;
    }

    public void setNombreClasificacionSeleccionada(String nombreClasificacionSeleccionada) {
        this.nombreClasificacionSeleccionada = nombreClasificacionSeleccionada;
    }

    public String getCodigoCategoria() {
        return codigoCategoria;
    }

    public void setCodigoCategoria(String codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }

    public String getDescripcionCategoria() {
        return descripcionCategoria;
    }

    public void setDescripcionCategoria(String descripcionCategoria) {
        this.descripcionCategoria = descripcionCategoria;
    }

    public String getObservacionCategoria() {
        return observacionCategoria;
    }

    public void setObservacionCategoria(String observacionCategoria) {
        this.observacionCategoria = observacionCategoria;
    }

    public String getNombreCategoriaSeleccionada() {
        return nombreCategoriaSeleccionada;
    }

    public void setNombreCategoriaSeleccionada(String nombreCategoriaSeleccionada) {
        this.nombreCategoriaSeleccionada = nombreCategoriaSeleccionada;
    }

}
