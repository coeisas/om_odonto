package managedBeans.configuraciones;

import beans.enumeradores.ClasificacionesEnum;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import beans.utilidades.MetodosGenerales;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import managedBeans.seguridad.AplicacionGeneralMB;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.CfgSede;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgSedeFacade;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "sedesMB")
@SessionScoped
public class SedesMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------    
    @EJB
    CfgClasificacionesFacade clasificacionesFacade;
    @EJB
    CfgSedeFacade sedeFacade;
    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------
    private List<CfgSede> listaSedes;
    private CfgSede sedeSeleccionada;
    private CfgSede sedeSeleccionadaTabla;
    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------
    private List<SelectItem> listaMunicipiosSede;
    private String tituloTabSede = "Datos Nueva Sede";
    private String nombreSede = "";
    private String codigoSede = "";
    private String departamentoSede = "";
    private String municipioSede = "";
    private String encargadoSede = "";
    private String direccionSede = "";
    private String telefono1Sede = "";
    private String telefono2Sede = "";
    private AplicacionGeneralMB aplicacionGeneralMB;

    //---------------------------------------------------
    //------------- FUNCIONES INICIALES -----------------
    //---------------------------------------------------      
    @PostConstruct
    public void inicializar() {
        aplicacionGeneralMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{aplicacionGeneralMB}", AplicacionGeneralMB.class);
        listaSedes = sedeFacade.buscarOrdenado();
    }

    public SedesMB() {
    }

    //---------------------------------------------------
    //-----------------FUNCIONES SEDES ------------------
    //---------------------------------------------------    
    public void limpiarSede() {
        listaSedes = sedeFacade.buscarOrdenado();
        tituloTabSede = "Datos Nueva Sede";
        sedeSeleccionada = null;
        listaMunicipiosSede = new ArrayList<>();
        codigoSede = "";
        nombreSede = "";
        departamentoSede = "";
        municipioSede = "";
        encargadoSede = "";
        direccionSede = "";
        telefono1Sede = "";
        telefono2Sede = "";
    }

    public void buscarSede() {
        listaSedes = sedeFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().execute("PF('wvTablaSedes').clearFilters(); PF('wvTablaSedes').getPaginator().setPage(0); PF('dialogoBuscarSede').show();");
    }

    public void cargarSede() {
        if (sedeSeleccionadaTabla == null) {
            imprimirMensaje("Error", "Se debe seleccionar una sede de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        limpiarSede();
        sedeSeleccionada = sedeFacade.find(sedeSeleccionadaTabla.getIdSede());
        codigoSede = sedeSeleccionada.getCodigoSede();
        nombreSede = sedeSeleccionada.getNombreSede();
        tituloTabSede = "Datos Sede: " + nombreSede;
        if (sedeSeleccionada.getDepartamento() != null) {
            departamentoSede = sedeSeleccionada.getDepartamento().getId().toString();
            cargarMunicipiosSede();
        }
        if (sedeSeleccionada.getMunicipio() != null) {
            municipioSede = sedeSeleccionada.getMunicipio().getId().toString();
        }
        encargadoSede = sedeSeleccionada.getEncargado();
        direccionSede = sedeSeleccionada.getDireccion();
        telefono1Sede = sedeSeleccionada.getTelefono1();
        telefono2Sede = sedeSeleccionada.getTelefono2();
        RequestContext.getCurrentInstance().execute("PF('dialogoBuscarSede').hide(); PF('wvTablaSedes').clearFilters(); PF('wvTablaSedes').getPaginator().setPage(0);");
    }

    public void eliminarSede() {
        if (sedeSeleccionada == null) {
            imprimirMensaje("Error", "No se ha cargado ninguna sede", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoEliminarSede').show();");

    }

    public void confirmarEliminarSede() {
        if (sedeSeleccionada == null) {
            imprimirMensaje("Error", "No se ha cargado ninguna sede", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            sedeFacade.remove(sedeSeleccionada);
            limpiarSede();
            RequestContext.getCurrentInstance().update("IdFormSedes");
            aplicacionGeneralMB.cargarClasificacion(ClasificacionesEnum.Sedes);
            imprimirMensaje("Correcto", "La sede ha sido eliminada", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "la sede que se intenta eliminar tiene actividades dentro del sistema; por lo cual no puede ser eliminada.", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void guardarSede() {
        if (validacionCampoVacio(codigoSede, "Código sede")) {
            return;
        }
        if (validacionCampoVacio(nombreSede, "Nombre sede")) {
            return;
        }
        if (sedeSeleccionada == null) {
            guardarNuevaSede();
        } else {
            actualizarSedeExistente();
        }
    }

    private void guardarNuevaSede() {
        CfgSede nuevaSede = new CfgSede();
        nuevaSede.setCodigoSede(codigoSede);
        nuevaSede.setNombreSede(nombreSede);
        if (validarNoVacio(departamentoSede)) {
            nuevaSede.setDepartamento(clasificacionesFacade.find(Integer.parseInt(departamentoSede)));
        }
        if (validarNoVacio(municipioSede)) {
            nuevaSede.setMunicipio(clasificacionesFacade.find(Integer.parseInt(municipioSede)));
        }
        nuevaSede.setEncargado(encargadoSede);
        nuevaSede.setDireccion(direccionSede);
        nuevaSede.setTelefono1(telefono1Sede);
        nuevaSede.setTelefono2(telefono2Sede);
        sedeFacade.create(nuevaSede);
        limpiarSede();
        aplicacionGeneralMB.cargarClasificacion(ClasificacionesEnum.Sedes);
        RequestContext.getCurrentInstance().update("IdFormSedes");
        imprimirMensaje("Correcto", "La sede se ha creado.", FacesMessage.SEVERITY_INFO);
    }

    private void actualizarSedeExistente() {
        sedeSeleccionada.setCodigoSede(codigoSede);
        sedeSeleccionada.setNombreSede(nombreSede);
        if (validarNoVacio(departamentoSede)) {
            sedeSeleccionada.setDepartamento(clasificacionesFacade.find(Integer.parseInt(departamentoSede)));
        }
        if (validarNoVacio(municipioSede)) {
            sedeSeleccionada.setMunicipio(clasificacionesFacade.find(Integer.parseInt(municipioSede)));
        }
        sedeSeleccionada.setEncargado(encargadoSede);
        sedeSeleccionada.setDireccion(direccionSede);
        sedeSeleccionada.setTelefono1(telefono1Sede);
        sedeSeleccionada.setTelefono2(telefono2Sede);
        sedeFacade.edit(sedeSeleccionada);
        limpiarSede();
        aplicacionGeneralMB.cargarClasificacion(ClasificacionesEnum.Sedes);
        RequestContext.getCurrentInstance().update("IdFormSedes");
        imprimirMensaje("Correcto", "La sede ha sido actualizada.", FacesMessage.SEVERITY_INFO);
    }

    public void cargarMunicipiosSede() {
        listaMunicipiosSede = new ArrayList<>();
        if (departamentoSede != null && departamentoSede.length() != 0) {
            List<CfgClasificaciones> listaM = clasificacionesFacade.buscarMunicipioPorDepartamento(clasificacionesFacade.find(Integer.parseInt(departamentoSede)).getCodigo());
            for (CfgClasificaciones mun : listaM) {
                listaMunicipiosSede.add(new SelectItem(mun.getId(), mun.getDescripcion()));
            }
        }
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

    public List<CfgSede> getListaSedes() {
        return listaSedes;
    }

    public void setListaSedes(List<CfgSede> listaSedes) {
        this.listaSedes = listaSedes;
    }

    public String getNombreSede() {
        return nombreSede;
    }

    public void setNombreSede(String nombreSede) {
        this.nombreSede = nombreSede;
    }

    public String getMunicipioSede() {
        return municipioSede;
    }

    public void setMunicipioSede(String municipioSede) {
        this.municipioSede = municipioSede;
    }

    public String getEncargadoSede() {
        return encargadoSede;
    }

    public void setEncargadoSede(String encargadoSede) {
        this.encargadoSede = encargadoSede;
    }

    public String getDireccionSede() {
        return direccionSede;
    }

    public void setDireccionSede(String direccionSede) {
        this.direccionSede = direccionSede;
    }

    public String getTelefono1Sede() {
        return telefono1Sede;
    }

    public void setTelefono1Sede(String telefono1Sede) {
        this.telefono1Sede = telefono1Sede;
    }

    public String getTelefono2Sede() {
        return telefono2Sede;
    }

    public void setTelefono2Sede(String telefono2Sede) {
        this.telefono2Sede = telefono2Sede;
    }

    public String getDepartamentoSede() {
        return departamentoSede;
    }

    public void setDepartamentoSede(String departamentoSede) {
        this.departamentoSede = departamentoSede;
    }

    public List<SelectItem> getListaMunicipiosSede() {
        return listaMunicipiosSede;
    }

    public void setListaMunicipiosSede(List<SelectItem> listaMunicipiosSede) {
        this.listaMunicipiosSede = listaMunicipiosSede;
    }

    public String getTituloTabSede() {
        return tituloTabSede;
    }

    public void setTituloTabSede(String tituloTabSede) {
        this.tituloTabSede = tituloTabSede;
    }

    public CfgSede getSedeSeleccionadaTabla() {
        return sedeSeleccionadaTabla;
    }

    public void setSedeSeleccionadaTabla(CfgSede sedeSeleccionadaTabla) {
        this.sedeSeleccionadaTabla = sedeSeleccionadaTabla;
    }

    public String getCodigoSede() {
        return codigoSede;
    }

    public void setCodigoSede(String codigoSede) {
        this.codigoSede = codigoSede;
    }

}
