package managedBeans.seguridad;

import beans.enumeradores.ClasificacionesEnum;
import modelo.entidades.CfgOpcionesMenu;
import modelo.entidades.CfgPerfilesUsuario;
import modelo.entidades.CfgUsuarios;
import modelo.fachadas.CfgOpcionesMenuFacade;
import modelo.fachadas.CfgPerfilesUsuarioFacade;
import modelo.fachadas.CfgUsuariosFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import beans.utilidades.MetodosGenerales;
import javax.faces.context.FacesContext;

@ManagedBean(name = "perfilesMB")
@SessionScoped
public class PerfilesMB extends MetodosGenerales implements Serializable {
    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------

    @EJB
    CfgUsuariosFacade usuariosFachada;
    @EJB
    CfgPerfilesUsuarioFacade perfilesFachada;
    @EJB
    CfgOpcionesMenuFacade opcionesFachada;

    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------
    private List<CfgPerfilesUsuario> listaPerfilesEntidad;
    private CfgPerfilesUsuario perfilEntidadSeleccionado;

    //---------------------------------------------------
    //-----------------VARIABLES -------------------------
    //---------------------------------------------------
    private boolean perfilCargado = false;//determinar si hay o un perfil cargado
    private String nombrePerfilSeleccionado = "";
    private TreeNode nodoRaiz;
    private TreeNode[] nodosSeleccionadosArbol;
    private ArrayList<DefaultTreeNode> nodosDelArbol = new ArrayList<>();
    private String nombreNuevoPerfil = "";
    private AplicacionGeneralMB aplicacionGeneralMB;

    //---------------------------------------------------
    //----------------- FUNCIONES -------------------------
    //---------------------------------------------------    
    public PerfilesMB() {
        aplicacionGeneralMB=FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{aplicacionGeneralMB}", AplicacionGeneralMB.class);
    }

    @PostConstruct
    public void reiniciar() {
        listaPerfilesEntidad = perfilesFachada.findAll();
        perfilEntidadSeleccionado = null;
    }

    public int crearPerfil() {
        if (nombreNuevoPerfil == null || nombreNuevoPerfil.length() == 0) {
            imprimirMensaje("Error", "Debe escribir un nombre", FacesMessage.SEVERITY_ERROR);
            return 0;
        }
        if (perfilesFachada.buscarPorNombrePerfil(nombreNuevoPerfil) != null && !perfilesFachada.buscarPorNombrePerfil(nombreNuevoPerfil).isEmpty()) {
            imprimirMensaje("Error", "El nombre ingresado ya está registrado", FacesMessage.SEVERITY_ERROR);
            return 0;
        }
        CfgPerfilesUsuario nuevoPerfil = new CfgPerfilesUsuario();
        nuevoPerfil.setNombrePerfil(nombreNuevoPerfil);
        perfilesFachada.create(nuevoPerfil);
        aplicacionGeneralMB.cargarClasificacion(ClasificacionesEnum.PerfilesUsuario);
        listaPerfilesEntidad = perfilesFachada.findAll();
        imprimirMensaje("Correcto", "Perfil creado correctamente", FacesMessage.SEVERITY_INFO);
        return 0;
    }

    private boolean determinarPermisoParaOpcion(CfgOpcionesMenu opcion) {
        //saber si el perfil seleccionado contiene el permiso para una determinada opcion del sistema
        List<CfgOpcionesMenu> opcionesPorPerfil = perfilEntidadSeleccionado.getCfgOpcionesMenuList();
        for (CfgOpcionesMenu op : opcionesPorPerfil) {
            if (Objects.equals(op.getIdOpcionMenu(), opcion.getIdOpcionMenu())) {
                return true;
            }
        }
        return false;
    }

    private void cargarArbolDePermisos() {
        //Genera el arbol con todos los controles disponibles
        List<CfgOpcionesMenu> listaDeOpciones = opcionesFachada.buscarOpcionesOrdenado();
        nodoRaiz = new DefaultTreeNode(new CfgOpcionesMenu(), null);
        nodosDelArbol = new ArrayList<>();
        DefaultTreeNode nuevoNodo;
        for (CfgOpcionesMenu opcion : listaDeOpciones) {
            if (opcion.getUrlOpcion() == null || opcion.getUrlOpcion().length() == 0) {
                nuevoNodo = new DefaultTreeNode(opcion, nodoRaiz);
                nuevoNodo.setSelected(determinarPermisoParaOpcion(opcion));
                nodosDelArbol.add(nuevoNodo);
            } else {
                DefaultTreeNode padre = buscarPadre(opcion);
                if (padre != null) {
                    nuevoNodo = new DefaultTreeNode(opcion, padre);
                    nuevoNodo.setSelected(determinarPermisoParaOpcion(opcion));
                    nodosDelArbol.add(nuevoNodo);
                } else if (opcion.getIdOpcionMenu() != 0) {//no es el registro correspondiente a raiz
                    nuevoNodo = new DefaultTreeNode(opcion, nodoRaiz);
                    nuevoNodo.setSelected(determinarPermisoParaOpcion(opcion));
                    nodosDelArbol.add(nuevoNodo);
                }
            }
        }
    }

    private DefaultTreeNode buscarPadre(CfgOpcionesMenu opcionHija) {
        //busca quien es el padre de un nodo en el arbol
        if (opcionHija.getIdOpcionPadre() == null) {
            return null;
        }
        CfgOpcionesMenu a;
        for (DefaultTreeNode nodo : nodosDelArbol) {
            a = (CfgOpcionesMenu) nodo.getData();
            if (Objects.equals(a.getIdOpcionMenu(), opcionHija.getIdOpcionPadre().getIdOpcionMenu())) {
                return nodo;
            }
        }
        return null;
    }

    private boolean buscarIdOpcion(int idOpcion, List<CfgOpcionesMenu> listadoOpciones) {
        //se busca si un identificador esta en un listado
        for (CfgOpcionesMenu op : listadoOpciones) {
            if (idOpcion == op.getIdOpcionMenu()) {
                return true;
            }
        }
        return false;
    }

    public int guardarPermisos() {
        if (!perfilCargado) {
            imprimirMensaje("Error", "No se ha cargado ningún perfil.", FacesMessage.SEVERITY_ERROR);
            return 0;
        }
        if (nodosSeleccionadosArbol == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningun permiso para el perfil: " + nombrePerfilSeleccionado, FacesMessage.SEVERITY_ERROR);
            return 0;
        }
        List<CfgOpcionesMenu> opcionesDelPerfil = new ArrayList<>();
        CfgOpcionesMenu a;
        //AGREGO LOS NODOS QUE ESTAN SELECCIONADOS EN EL ARBOL
        for (TreeNode nodo : nodosSeleccionadosArbol) {
            a = (CfgOpcionesMenu) nodo.getData();
            opcionesDelPerfil.add(a);
        }
        //LOS PADRES DE TODOS LOS NODOS DEBEN ESTAR AGREGADOS(SI NO SE LOS AGREGA)            
        boolean continuar = true;
        while (continuar) {//este cilco se repito por que si se agrega un padre el padre tambien puede tener padre
            continuar = false;
            for (CfgOpcionesMenu opcion : opcionesDelPerfil) {
                if (!buscarIdOpcion(opcion.getIdOpcionPadre().getIdOpcionMenu(), opcionesDelPerfil)) {//se debe agregar el padre
                    opcionesDelPerfil.add(opcionesFachada.find(opcion.getIdOpcionPadre().getIdOpcionMenu()));
                    continuar = true;//se agrego un padre se vuel a revisar que todos tengan padre
                    break;
                }
            }
        }
        perfilEntidadSeleccionado.setCfgOpcionesMenuList(opcionesDelPerfil);//SE AGREGA EL LISTADO DE OPCIONES AL PERFIL SELECCIONADO        
        perfilesFachada.edit(perfilEntidadSeleccionado);//SE PERSISTE EL PERFIL
        aplicacionGeneralMB.cargarClasificacion(ClasificacionesEnum.PerfilesUsuario);
        imprimirMensaje("Correcto", "El perfil: " + nombrePerfilSeleccionado + " se ha actualizado correctamente", FacesMessage.SEVERITY_INFO);
        return 0;
    }

    public void cargarPerfil() {
        //se utiliza esta funcion cuando para cargar el arbol de permisos
        //una ves se haya seleccionado el perfil de la tabla de busqueda de perfil
        if (perfilEntidadSeleccionado != null) {
            nombrePerfilSeleccionado = perfilEntidadSeleccionado.getNombrePerfil();
            perfilCargado = true;
            cargarArbolDePermisos();
        } else {
            nombrePerfilSeleccionado = "";
            perfilCargado = false;
            imprimirMensaje("Error", "Se debe seleccionar un perfil de la tabla", FacesMessage.SEVERITY_ERROR);
        }
    }

    public int eliminarPerfil() {
        //realizar la eliminacion de un perfil cargado
        if (!perfilCargado) {
            imprimirMensaje("Error", "No se ha cargado ningún perfil.", FacesMessage.SEVERITY_ERROR);
            return 0;
        }
        List<CfgUsuarios> b = usuariosFachada.buscarPorPerfil(perfilEntidadSeleccionado.getIdPerfil());
        if (b != null && !b.isEmpty()) {
            imprimirMensaje("Error", "Existen usuarios haciendo uso de este perfil", FacesMessage.SEVERITY_ERROR);
            return 0;
        }

        perfilesFachada.remove(perfilEntidadSeleccionado);
        aplicacionGeneralMB.cargarClasificacion(ClasificacionesEnum.PerfilesUsuario);
        nombrePerfilSeleccionado = "";
        perfilCargado = false;
        perfilEntidadSeleccionado = null;
        listaPerfilesEntidad = perfilesFachada.findAll();
        imprimirMensaje("Correcto", "El perfil " + nombrePerfilSeleccionado + " se ha eliminado", FacesMessage.SEVERITY_INFO);
        return 0;
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------
    public CfgPerfilesUsuario getPerfilEntidadSeleccionado() {
        return perfilEntidadSeleccionado;
    }

    public void setPerfilEntidadSeleccionado(CfgPerfilesUsuario perfilSeleccionado) {
        this.perfilEntidadSeleccionado = perfilSeleccionado;
    }

    public TreeNode getNodoRaiz() {
        return nodoRaiz;
    }

    public void setNodoRaiz(TreeNode nodoRaiz) {
        this.nodoRaiz = nodoRaiz;
    }

    public List<CfgPerfilesUsuario> getListaPerfilesEntidad() {
        return listaPerfilesEntidad;
    }

    public void setListaPerfilesEntidad(List<CfgPerfilesUsuario> listaPerfilesEntidad) {
        this.listaPerfilesEntidad = listaPerfilesEntidad;
    }

    public String getNombrePerfilSeleccionado() {
        return nombrePerfilSeleccionado;
    }

    public void setNombrePerfilSeleccionado(String nombrePerfilSeleccionado) {
        this.nombrePerfilSeleccionado = nombrePerfilSeleccionado;
    }

    public boolean isPerfilCargado() {
        return perfilCargado;
    }

    public void setPerfilCargado(boolean perfilCargado) {
        this.perfilCargado = perfilCargado;
    }

    public TreeNode[] getNodosSeleccionadosArbol() {
        return nodosSeleccionadosArbol;
    }

    public void setNodosSeleccionadosArbol(TreeNode[] nodosSeleccionadosArbol) {
        this.nodosSeleccionadosArbol = nodosSeleccionadosArbol;
    }

    public String getNombreNuevoPerfil() {
        return nombreNuevoPerfil;
    }

    public void setNombreNuevoPerfil(String nombreNuevoPerfil) {
        this.nombreNuevoPerfil = nombreNuevoPerfil;
    }

}
