package managedBeans.configuraciones;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import beans.utilidades.MetodosGenerales;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import modelo.entidades.CfgConsultorios;
import modelo.entidades.CfgSede;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgConsultoriosFacade;
import modelo.fachadas.CfgSedeFacade;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "consultoriosMB")
@SessionScoped
public class ConsultoriosMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------    
    @EJB
    CfgClasificacionesFacade clasificacionesFacade;
    @EJB
    CfgSedeFacade sedeFacade;
    @EJB
    CfgConsultoriosFacade consultoriosFacade;

    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------    
    private CfgSede sedeSeleccionada;
    private List<CfgConsultorios> listaConsultorios;
    private CfgConsultorios consultorioSeleccionado;
    private CfgConsultorios consultorioSeleccionadoTabla;

    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------
    private String idSedeSeleccionada;
    private String tituloTabConsultorio = "Datos Nuevo Consultorio";
    private String codigoConsultorio = "";
    private String nombreConsultorio = "";
    private String pisoConsultorio = "1";
    private String especialidadConsultorio = "";
    private boolean mostrarControlesConsultorios = false;//ocultar controles si no se tiene escogida la sede

    //---------------------------------------------------
    //------------- FUNCIONES INICIALES -----------------
    //---------------------------------------------------      
    public ConsultoriosMB() {
    }

    //---------------------------------------------------
    //-----------------FUNCIONES CONSULTORIOS -----------
    //---------------------------------------------------    
    public void limpiarFormularioConsultorio() {
        listaConsultorios = consultoriosFacade.buscarOrdenado();
        tituloTabConsultorio = "Datos Nuevo Consultorio";
        consultorioSeleccionado = null;
        codigoConsultorio = "";
        pisoConsultorio = "1";
        nombreConsultorio = "";
        especialidadConsultorio = "";
    }

    public void buscarConsultorio() {
        listaConsultorios = consultoriosFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().execute("PF('wvTablaConsultorios').clearFilters(); PF('wvTablaConsultorios').getPaginator().setPage(0); PF('dialogoBuscarConsultorios').show();");
    }

    public void cargarConsultorio() {
        if (consultorioSeleccionadoTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún consultorio de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        limpiarFormularioConsultorio();
        consultorioSeleccionado = consultoriosFacade.find(consultorioSeleccionadoTabla.getIdConsultorio());
        codigoConsultorio = consultorioSeleccionado.getCodConsultorio();
        pisoConsultorio = consultorioSeleccionado.getPisoConsultorio().toString();
        nombreConsultorio = consultorioSeleccionado.getNomConsultorio();
        especialidadConsultorio = consultorioSeleccionado.getCodEspecialidad().getId().toString();
        tituloTabConsultorio = "Datos Consultorio: " + nombreConsultorio;
        RequestContext.getCurrentInstance().execute("PF('dialogoBuscarConsultorios').hide(); PF('wvTablaConsultorios').clearFilters(); PF('wvTablaConsultorios').getPaginator().setPage(0);");
    }

    public void eliminarConsultorio() {
        if (consultorioSeleccionado == null) {
            imprimirMensaje("Error", "No se ha cargado ningún consultorio", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoEliminarConsultorio').show();");
    }

    public void confirmarEliminarConsultorio() {
        if (consultorioSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún consultorio", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            consultoriosFacade.remove(consultorioSeleccionado);
            recargarConsultorios();
            limpiarFormularioConsultorio();
            RequestContext.getCurrentInstance().update("IdFormConsultorios");
            imprimirMensaje("Correcto", "El consultorio ha sido eliminado", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El consultorio que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void guardarConsultorio() {
        if (sedeSeleccionada == null) {
            imprimirMensaje("Error", "No se ha seleccionado una sede.", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validacionCampoVacio(codigoConsultorio, "Código consultorio")) {
            return;
        }
        if (validacionCampoVacio(nombreConsultorio, "Nombre consultorio")) {
            return;
        }
        if (validacionCampoVacio(pisoConsultorio, "Piso consultorio")) {
            return;
        }
        if (validacionCampoVacio(especialidadConsultorio, "Especialidad consultorio")) {
            return;
        }
        if (consultorioSeleccionado == null) {
            guardarNuevoConsultorio();
        } else {
            actualizarConsultorioExistente();
        }
    }

    private void guardarNuevoConsultorio() {
        CfgConsultorios nuevoConsultorio = new CfgConsultorios();
        nuevoConsultorio.setCodConsultorio(codigoConsultorio);
        nuevoConsultorio.setPisoConsultorio(Integer.parseInt(pisoConsultorio));
        nuevoConsultorio.setNomConsultorio(nombreConsultorio);
        nuevoConsultorio.setCodEspecialidad(clasificacionesFacade.find(Integer.parseInt(especialidadConsultorio)));
        nuevoConsultorio.setIdSede(sedeSeleccionada);
        consultoriosFacade.create(nuevoConsultorio);
        limpiarFormularioConsultorio();
        recargarConsultorios();
        RequestContext.getCurrentInstance().update("IdFormConsultorios");
        imprimirMensaje("Correcto", "El consultorio ha sido creado.", FacesMessage.SEVERITY_INFO);
    }

    public void actualizarConsultorioExistente() {//realiza la actualizacion del consultorio        
        consultorioSeleccionado.setCodConsultorio(codigoConsultorio);
        consultorioSeleccionado.setPisoConsultorio(Integer.parseInt(pisoConsultorio));
        consultorioSeleccionado.setNomConsultorio(nombreConsultorio);
        consultorioSeleccionado.setCodEspecialidad(clasificacionesFacade.find(Integer.parseInt(especialidadConsultorio)));
        consultoriosFacade.edit(consultorioSeleccionado);
        limpiarFormularioConsultorio();
        recargarConsultorios();
        RequestContext.getCurrentInstance().update("IdFormConsultorios");
        imprimirMensaje("Correcto", "El consultorio ha sido actualizado.", FacesMessage.SEVERITY_INFO);
    }

    private void recargarConsultorios() {
        if (sedeSeleccionada != null) {
            sedeSeleccionada = sedeFacade.find(sedeSeleccionada.getIdSede());//se actulice
            listaConsultorios = sedeSeleccionada.getCfgConsultoriosList();
        }
    }

    public void cambiaSede() {
        limpiarFormularioConsultorio();
        if (validarNoVacio(idSedeSeleccionada)) {
            sedeSeleccionada = sedeFacade.find(Integer.parseInt(idSedeSeleccionada));
            if (sedeSeleccionada != null) {
                mostrarControlesConsultorios = true;
                listaConsultorios = sedeSeleccionada.getCfgConsultoriosList();
            } else {
                mostrarControlesConsultorios = false;
                listaConsultorios = new ArrayList<>();
            }
            return;
        }
        sedeSeleccionada = null;
        mostrarControlesConsultorios = false;
        listaConsultorios = new ArrayList<>();
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------    
    public CfgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(CfgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public List<CfgConsultorios> getListaConsultorios() {
        return listaConsultorios;
    }

    public void setListaConsultorios(List<CfgConsultorios> listaConsultorios) {
        this.listaConsultorios = listaConsultorios;
    }

    public CfgConsultorios getConsultorioSeleccionado() {
        return consultorioSeleccionado;
    }

    public void setConsultorioSeleccionado(CfgConsultorios consultorioSeleccionado) {
        this.consultorioSeleccionado = consultorioSeleccionado;
    }

    public String getNombreConsultorio() {
        return nombreConsultorio;
    }

    public void setNombreConsultorio(String nombreConsultorio) {
        this.nombreConsultorio = nombreConsultorio;
    }

    public String getPisoConsultorio() {
        return pisoConsultorio;
    }

    public void setPisoConsultorio(String pisoConsultorio) {
        this.pisoConsultorio = pisoConsultorio;
    }

    public String getEspecialidadConsultorio() {
        return especialidadConsultorio;
    }

    public void setEspecialidadConsultorio(String especialidadConsultorio) {
        this.especialidadConsultorio = especialidadConsultorio;
    }

    public String getCodigoConsultorio() {
        return codigoConsultorio;
    }

    public void setCodigoConsultorio(String codigoConsultorio) {
        this.codigoConsultorio = codigoConsultorio;
    }

    public String getTituloTabConsultorio() {
        return tituloTabConsultorio;
    }

    public void setTituloTabConsultorio(String tituloTabConsultorio) {
        this.tituloTabConsultorio = tituloTabConsultorio;
    }

    public CfgConsultorios getConsultorioSeleccionadoTabla() {
        return consultorioSeleccionadoTabla;
    }

    public void setConsultorioSeleccionadoTabla(CfgConsultorios consultorioSeleccionadoTabla) {
        this.consultorioSeleccionadoTabla = consultorioSeleccionadoTabla;
    }

    public String getIdSedeSeleccionada() {
        return idSedeSeleccionada;
    }

    public void setIdSedeSeleccionada(String idSedeSeleccionada) {
        this.idSedeSeleccionada = idSedeSeleccionada;
    }

    public boolean isMostrarControlesConsultorios() {
        return mostrarControlesConsultorios;
    }

    public void setMostrarControlesConsultorios(boolean mostrarControlesConsultorios) {
        this.mostrarControlesConsultorios = mostrarControlesConsultorios;
    }

}
