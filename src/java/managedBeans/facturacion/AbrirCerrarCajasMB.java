package managedBeans.facturacion;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import beans.utilidades.MetodosGenerales;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.FacCaja;
import modelo.entidades.FacMovimientoCaja;
import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.FacCajaFacade;
import modelo.fachadas.FacMovimientoCajaFacade;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "abrirCerrarCajasMB")
@SessionScoped
public class AbrirCerrarCajasMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------            
    @EJB
    FacCajaFacade cajaFacade;
    @EJB
    CfgUsuariosFacade usuariosFacade;
    @EJB
    FacMovimientoCajaFacade movimientoCajaFacade;
    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------    
    private FacCaja cajaSeleccionada;
    private CfgUsuarios usuarioActual;
    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------
    private LoginMB loginMB;
    private String tituloTab = "";
    private String nombreCaja = "";
    private String codigoCaja = "";
    private double valorBaseCaja = 0;
    private boolean cajaAsignada = false;//determinar si el usurio tiene caja asignada
    private boolean cajaCerrada = false;//determinar si la caja esta cerrada=true o abierta=false

    //---------------------------------------------------
    //------------- FUNCIONES INICIALES  ----------------
    //---------------------------------------------------      
    //@PostConstruct
    public void inicializar() {
        //busco si el usuario actual tiene caja asociada
        cajaSeleccionada=null;
        usuarioActual = usuariosFacade.find(loginMB.getUsuarioActual().getIdUsuario());
        tituloTab = "Sin caja asignada";
        if (usuarioActual.getFacCajaList() != null) {
            if (usuarioActual.getFacCajaList().isEmpty()) {//no tiene caja asignada
                cajaAsignada = false;
            } else {//tiene caja asignada
                cajaAsignada = true;
                cajaSeleccionada = cajaFacade.find(usuarioActual.getFacCajaList().get(0).getIdCaja());
                cajaCerrada = cajaSeleccionada.getCerrada();
                codigoCaja = cajaSeleccionada.getCodigoCaja();
                nombreCaja = cajaSeleccionada.getNombreCaja();
                if (cajaCerrada) {
                    tituloTab = "Abrir caja";
                    valorBaseCaja = cajaSeleccionada.getValorBaseDefecto();
                } else {
                    tituloTab = "Cerrar caja";
                    valorBaseCaja = 0;
                }
            }
        }
    }

    public AbrirCerrarCajasMB() {
    }

    //---------------------------------------------------
    //-----------------FUNCIONES CAJAS ------------------
    //---------------------------------------------------      
    public void abrirCaja() {
        if (cajaSeleccionada != null) {
            cajaSeleccionada.setCerrada(false);
            cajaFacade.edit(cajaSeleccionada);
            FacMovimientoCaja movimiento = new FacMovimientoCaja();
            movimiento.setIdCaja(cajaSeleccionada);
            movimiento.setFecha(new Date());
            movimiento.setValor(valorBaseCaja);
            movimiento.setAbrirCaja(true);//se abre caja
            movimientoCajaFacade.create(movimiento);
            inicializar();
            RequestContext.getCurrentInstance().update("IdFormCajas:IdTabView");
            imprimirMensaje("Correcto", "La caja ha sido abierta", FacesMessage.SEVERITY_INFO);
        } else {
            imprimirMensaje("Error", "No se ha seleccionado una caja", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void cerrarCaja() {
        if (cajaSeleccionada != null) {
            cajaSeleccionada.setCerrada(true);
            cajaFacade.edit(cajaSeleccionada);
            FacMovimientoCaja movimiento = new FacMovimientoCaja();
            movimiento.setIdCaja(cajaSeleccionada);
            movimiento.setFecha(new Date());
            movimiento.setValor(valorBaseCaja);
            movimiento.setAbrirCaja(false);//se cierra caja
            movimientoCajaFacade.create(movimiento);
            inicializar();
            RequestContext.getCurrentInstance().update("IdFormCajas:IdTabView");
            imprimirMensaje("Correcto", "La caja ha sido cerrada", FacesMessage.SEVERITY_INFO);
        } else {
            imprimirMensaje("Error", "No se ha seleccionado una caja", FacesMessage.SEVERITY_ERROR);
        }
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //--------------------------------------------------- 
    public LoginMB getLoginMB() {
        return loginMB;
    }

    public void setLoginMB(LoginMB loginMB) {
        this.loginMB = loginMB;
    }

    public FacCaja getCajaSeleccionada() {
        return cajaSeleccionada;
    }

    public void setCajaSeleccionada(FacCaja cajaSeleccionada) {
        this.cajaSeleccionada = cajaSeleccionada;
    }

    public boolean isCajaAsignada() {
        return cajaAsignada;
    }

    public void setCajaAsignada(boolean cajaAsignada) {
        this.cajaAsignada = cajaAsignada;
    }

    public boolean isCajaCerrada() {
        return cajaCerrada;
    }

    public void setCajaCerrada(boolean cajaCerrada) {
        this.cajaCerrada = cajaCerrada;
    }

    public String getNombreCaja() {
        return nombreCaja;
    }

    public void setNombreCaja(String nombreCaja) {
        this.nombreCaja = nombreCaja;
    }

    public String getCodigoCaja() {
        return codigoCaja;
    }

    public void setCodigoCaja(String codigoCaja) {
        this.codigoCaja = codigoCaja;
    }

    public double getValorBaseCaja() {
        return valorBaseCaja;
    }

    public void setValorBaseCaja(double valorBaseCaja) {
        this.valorBaseCaja = valorBaseCaja;
    }

    public String getTituloTab() {
        return tituloTab;
    }

    public void setTituloTab(String tituloTab) {
        this.tituloTab = tituloTab;
    }

}
