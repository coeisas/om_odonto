/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.Citas;

import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.CitPaqDetalle;
import modelo.entidades.CitPaqMaestro;
import modelo.entidades.FacServicio;
import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.CitPaqDetalleFacade;
import modelo.fachadas.CitPaqMaestroFacade;
import modelo.fachadas.FacServicioFacade;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Mario
 */
@ManagedBean(name = "paqCitasMB")
@SessionScoped
public class PaqCitasMB extends MetodosGenerales implements Serializable {

    private List<SelectItem> listaPrestadores;
    private int idPrestador;
    private List<SelectItem> listaServicios;
    private List<SelectItem> ItemsDias;
    private CitPaqMaestro paqMaestroSeleccionado;
    private int idServicio;
    private int idPaqueteAuxiliar;
    private String codPaquete;
    private String nomPaquete;
    private String metodo;
    private Integer[] diasSemana;
    private List<CitPaqMaestro> listaPaquetesMaestro;//listado de los paquetes creados
    private List<CitPaqDetalle> listaDetallePaquete;//detalles del paquete
    private boolean rendBotones;//renderizar los botonoes: insertar items y el de guardar o actulizar paquete
    private boolean rendBotonEliminarPaquete;
    private List<CitPaqDetalle> listaAuxiliarDetallePaqueteEliminar;//lista detalle del paquete usado para eliminar -> solo en actualizacion 
    private List<CitPaqDetalle> listaAuxiliarDetallePaqueteInsertar;//lista detalle del paquete usado para insertar -> solo en actualizacion

    @EJB
    CfgUsuariosFacade prestadorFacade;

    @EJB
    FacServicioFacade servicioFacade;

    @EJB
    CitPaqMaestroFacade paqMaestroFacade;

    @EJB
    CitPaqDetalleFacade paqDetalleFacade;

    @PostConstruct
    private void init() {
        listaAuxiliarDetallePaqueteEliminar = new ArrayList();
        listaAuxiliarDetallePaqueteInsertar = new ArrayList();
        List<CfgUsuarios> prestadores = prestadorFacade.findAll();
        crearItemsPrestadores(prestadores);
        List<FacServicio> servicios = servicioFacade.buscarActivos();
        crearItemsServicios(servicios);
        setRendBotonEliminarPaquete(false);
        setListaDetallePaquete((List<CitPaqDetalle>) new ArrayList());
        diasSemana = null;
        cargarTablaPaquetes();
        setItemsDias((List<SelectItem>) new ArrayList());
        crearItemsDias();
        setRendBotones(false);
        metodo = "vacio";
    }

    public PaqCitasMB() {

    }

    //--------------------------------------------------------------------------------
    //--------------------------------METHODS-----------------------------------------
    //--------------------------------------------------------------------------------
    private void crearItemsDias() {
        getItemsDias().add(new SelectItem(1, "Lunes"));
        getItemsDias().add(new SelectItem(2, "Martes"));
        getItemsDias().add(new SelectItem(3, "Miercoles"));
        getItemsDias().add(new SelectItem(4, "Jueves"));
        getItemsDias().add(new SelectItem(5, "Viernes"));
        getItemsDias().add(new SelectItem(6, "Sabado"));
        getItemsDias().add(new SelectItem(0, "Domingo"));
    }

    private void cargarTablaPaquetes() {
        listaPaquetesMaestro = paqMaestroFacade.findAll();
    }

    private void crearItemsPrestadores(List<CfgUsuarios> prestadores) {
        setListaPrestadores((List<SelectItem>) new ArrayList());
        for (CfgUsuarios prestador : prestadores) {
            getListaPrestadores().add(new SelectItem(prestador.getIdUsuario(), prestador.nombreCompleto()));
        }

    }

    public void eventoTabcodigoPaquete() {

        if (!codPaquete.isEmpty()) {
            setRendBotones(true);
            setIdServicio(0);
            setIdPrestador(0);
            listaDetallePaquete.clear();
            setDiasSemana(null);
            paqMaestroSeleccionado = paqMaestroFacade.buscarPorCodigo(codPaquete);
            if (paqMaestroSeleccionado != null) {
                metodo = "actualizar";
                cargarPaquete();
            } else {
                idPaqueteAuxiliar = 1;
                setRendBotonEliminarPaquete(false);
                setNomPaquete(null);
                metodo = "crear";
                listaAuxiliarDetallePaqueteEliminar.clear();

            }
        } else {
            liberarVariables();
        }
    }

    //inserta un elemento a la tabla
    public void insertarDetallePaquete() {
        if (idPrestador != 0 && idServicio != 0) {
            //en la tabla detalle no se debe ingresar el prestador y el servicio mas de una vez
            if (validarListaDetalle()) {
                CitPaqDetalle paqDetalle = new CitPaqDetalle();
                paqDetalle.setIdPrestador(prestadorFacade.find(idPrestador));
                paqDetalle.setIdServicio(servicioFacade.find(idServicio));
                paqDetalle.setIdPaqDetalle(idPaqueteAuxiliar);
                if (paqMaestroSeleccionado != null) {//comprueba si el item a insertar es para un paquete creado (se va modificar)
                    paqDetalle.setIdPaqMaestro(paqMaestroSeleccionado);
                    listaAuxiliarDetallePaqueteInsertar.add(paqDetalle);//nuevo item para el paquete ya creado
                }
                idPaqueteAuxiliar++;
                getListaDetallePaquete().add(paqDetalle);

                RequestContext.getCurrentInstance().update("idFormPaquetesCitas:idTableDetalle");
            } else {
                imprimirMensaje("Error", "El servicio asociado a este prestador no debe variar", FacesMessage.SEVERITY_ERROR);
            }
        } else {
            imprimirMensaje("Error", "Falta elegir el Servicio o el Prestador", FacesMessage.SEVERITY_ERROR);
        }

    }

    //elimina un elemento en listaDetallePaquete y tambien en citPaqDetalleList del paqMaestroSeleccionado
    public void eliminarDetallePaquete(ActionEvent event) {
        CitPaqDetalle detalle = (CitPaqDetalle) event.getComponent().getAttributes().get("detalle");
        listaDetallePaquete.remove(detalle);
        if (paqMaestroSeleccionado != null) {//si se esta modificando los items del paquete, no se esta creando un nuevo paquete
            if (!listaAuxiliarDetallePaqueteInsertar.contains(detalle)) {//comprueba que el item a elminiar no sea un nuevo item del paquete
                listaAuxiliarDetallePaqueteEliminar.add(detalle);
                paqMaestroSeleccionado.getCitPaqDetalleList().remove(detalle);
            } else {
                listaAuxiliarDetallePaqueteInsertar.remove(detalle);
            }
        }
        RequestContext.getCurrentInstance().update("idFormPaquetesCitas:idTableDetalle");
    }

    //busca si el servicio o el prestador se ha insertado en la listaDetallePaquete: no deben haber repetidos
    private boolean validarListaDetalle() {
        boolean ban = true;
        if (!listaDetallePaquete.isEmpty()) {
            for (CitPaqDetalle detalle : listaDetallePaquete) {
//                si el prestador se debe incluir una sola vez en el paquete: comentar o desmomentar la siguiente sentencia IF segun el caso
//                if (detalle.getCfgUsuarios().getIdUsuario() == idPrestador) {
//                    ban = false;
//                    break;
//                }
//                si el servicio se debe incluir una unica vez con el mismo prestador en el paquete: comente o descomente la siguiente sentencia IF dado el caso
//                if (detalle.getFacServicio().getIdServicio() == idServicio) {
//                    if (detalle.getCfgUsuarios().getIdUsuario() == idPrestador) {
//                        ban = false;
//                        break;
//                    }
//                }
//                en un paquete la veces que se inserte un prestador su servicio asociado debe ser el mismo
                if (detalle.getIdPrestador().getIdUsuario() == idPrestador) {
                    if (detalle.getIdServicio().getIdServicio() != idServicio) {
                        ban = false;
                    }
                    break;
                }
            }
        }
        return ban;
    }

    private void crearItemsServicios(List<FacServicio> servicios) {
        setListaServicios((List<SelectItem>) new ArrayList());
        for (FacServicio servicio : servicios) {
            getListaServicios().add(new SelectItem(servicio.getIdServicio(), servicio.getNombreServicio()));
        }
    }

    public void cargarPaquete() {
        if (paqMaestroSeleccionado != null) {
            listaAuxiliarDetallePaqueteEliminar.clear();
            listaAuxiliarDetallePaqueteInsertar.clear();
            setRendBotones(true);
            setRendBotonEliminarPaquete(true);
            this.metodo = "actualizar";
            setCodPaquete(paqMaestroSeleccionado.getCodPaquete());
            setNomPaquete(paqMaestroSeleccionado.getNomPaquete());
            char[] aux = paqMaestroSeleccionado.getDias().toCharArray();
            diasSemana = new Integer[aux.length];
            int i = 0;
            for (char dia : aux) {
                diasSemana[i] = Integer.parseInt(String.valueOf(dia));
                i++;
            }
//            listaDetallePaquete = paqMaestroSeleccionado.getCitPaqDetalleList();
            listaDetallePaquete = paqDetalleFacade.buscarPorMaestro(paqMaestroSeleccionado.getIdPaqMaestro());
            idPaqueteAuxiliar = listaDetallePaquete.get(listaDetallePaquete.size() - 1).getIdPaqDetalle() + 1;
        } else {
            metodo = "vacio";
            setRendBotones(false);
            setRendBotonEliminarPaquete(false);
        }
    }

    public void controladorMetodoAccion() {
        switch (metodo) {
            case "actualizar":
                actualizarPaquete();
                break;
            case "crear":
                crearPaquete();
                break;
            default:
                imprimirMensaje("Error", "No se puede realizar ninguna accion", FacesMessage.SEVERITY_ERROR);
                break;
        }
    }

    private void crearPaquete() {
        if (codPaquete.isEmpty()) {
            imprimirMensaje("Error", "Codgio de paquete requerido", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (nomPaquete.isEmpty()) {
            imprimirMensaje("Error", "Nombre de paquete requerido", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (diasSemana.length == 0) {
            imprimirMensaje("Error", "Debe elegir al menos un dia de semana", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (listaDetallePaquete.isEmpty()) {
            imprimirMensaje("Error", "No se encontraron items para el Paquete", FacesMessage.SEVERITY_ERROR);
            return;
        }
        CitPaqMaestro paqMaestro = new CitPaqMaestro();
        paqMaestro.setCodPaquete(codPaquete);
        paqMaestro.setNomPaquete(nomPaquete);
        String dias = "";
        for (Integer dia : diasSemana) {
            dias = dias.concat(dia.toString());
        }
        paqMaestro.setDias(dias);
        paqMaestro.setNoPaqAplicado(0);
        paqMaestroFacade.create(paqMaestro);
        listaPaquetesMaestro.add(paqMaestro);
        for (CitPaqDetalle paqDetalle : listaDetallePaquete) {
            paqDetalle.setIdPaqMaestro(paqMaestro);
            paqDetalle.setIdPaqDetalle(null);
            paqDetalleFacade.create(paqDetalle);
        }
        liberarVariables();
        actualizarPaquetesEnCitasMasivas();
        imprimirMensaje("Correcto", "Paquete creado", FacesMessage.SEVERITY_INFO);

    }

    private void actualizarPaquete() {
        if (nomPaquete.isEmpty()) {
            imprimirMensaje("Error", "Nombre de paquete requerido", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (diasSemana.length == 0) {
            imprimirMensaje("Error", "Debe elegir al menos un dia de semana", FacesMessage.SEVERITY_ERROR);
            return;
        }
//        paqMaestroSeleccionado.setCitPaqDetalleList(listaDetallePaquete);
        if (paqMaestroSeleccionado.getCitPaqDetalleList().isEmpty() && listaAuxiliarDetallePaqueteInsertar.isEmpty()) {
            listaDetallePaquete = paqDetalleFacade.buscarPorMaestro(paqMaestroSeleccionado.getIdPaqMaestro());
            listaAuxiliarDetallePaqueteEliminar.clear();
            paqMaestroSeleccionado.setCitPaqDetalleList(listaDetallePaquete);
            imprimirMensaje("Error", "No habian items para el Paquete. Se reestablecieron los Items", FacesMessage.SEVERITY_ERROR);
            return;
        }
//        elimina definitivamente los items escogidos para esa accion
        for (CitPaqDetalle cpd : listaAuxiliarDetallePaqueteEliminar) {
            paqDetalleFacade.remove(cpd);
        }
//        creando los nuevos items
        for (CitPaqDetalle cpd : listaAuxiliarDetallePaqueteInsertar) {
            cpd.setIdPaqDetalle(null);
            paqDetalleFacade.create(cpd);
        }
        paqMaestroSeleccionado.setNomPaquete(nomPaquete);
        String dias = "";
        for (Integer dia : diasSemana) {
            dias = dias.concat(dia.toString());
        }
        paqMaestroSeleccionado.setCodPaquete(paqMaestroSeleccionado.getCodPaquete());
        paqMaestroSeleccionado.setIdPaqMaestro(paqMaestroSeleccionado.getIdPaqMaestro());
        paqMaestroSeleccionado.setDias(dias);
        paqMaestroSeleccionado.setCitPaqDetalleList(paqDetalleFacade.buscarPorMaestro(paqMaestroSeleccionado.getIdPaqMaestro()));
        paqMaestroFacade.edit(paqMaestroSeleccionado);
        liberarVariables();
        actualizarPaquetesEnCitasMasivas();
        imprimirMensaje("Correcto", "Paquete actualizado", FacesMessage.SEVERITY_INFO);
    }

    public void eliminarPaquete() {
        try {
            paqDetalleFacade.eliminarPorMaestro(paqMaestroSeleccionado.getIdPaqMaestro());
            paqMaestroFacade.remove(paqMaestroSeleccionado);
            listaPaquetesMaestro.remove(paqMaestroSeleccionado);
            liberarVariables();
            actualizarPaquetesEnCitasMasivas();
            imprimirMensaje("Correcto", "Paquete Eliminado", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "No se puede elminar. Paquete usado", FacesMessage.SEVERITY_ERROR);
        }

    }

    private void liberarVariables() {
        cargarTablaPaquetes();
        RequestContext.getCurrentInstance().update("idFormCargarPaqMaestro");
        setRendBotones(false);
        setRendBotonEliminarPaquete(false);
        listaDetallePaquete = new ArrayList();
        listaAuxiliarDetallePaqueteEliminar.clear();
        listaAuxiliarDetallePaqueteInsertar.clear();
        diasSemana = null;
        setCodPaquete(null);
        setNomPaquete(null);
        setIdServicio(0);
        setIdPrestador(0);
        metodo = "vacio";
    }

    private void actualizarPaquetesEnCitasMasivas() {
        CitasMasivasV3MB citasMasivasV3MB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{citasMasivasV3MB}", CitasMasivasV3MB.class);
        citasMasivasV3MB.cargarPaquetes();
    }

    //----------------------------------------------------------------------------------------
    //-------------------------------GETTERS AND SETTERS--------------------------------------
    //---------------------------------------------------------------------------------------- 
    public List<SelectItem> getListaPrestadores() {
        return listaPrestadores;
    }

    public void setListaPrestadores(List<SelectItem> listaPrestadores) {
        this.listaPrestadores = listaPrestadores;
    }

    public List<SelectItem> getListaServicios() {
        return listaServicios;
    }

    public void setListaServicios(List<SelectItem> listaServicios) {
        this.listaServicios = listaServicios;
    }

    public Integer[] getDiasSemana() {
        return diasSemana;
    }

    public void setDiasSemana(Integer[] diasSemana) {
        this.diasSemana = diasSemana;
    }

    public int getIdPrestador() {
        return idPrestador;
    }

    public void setIdPrestador(int idPrestador) {
        this.idPrestador = idPrestador;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public List<CitPaqDetalle> getListaDetallePaquete() {
        return listaDetallePaquete;
    }

    public void setListaDetallePaquete(List<CitPaqDetalle> listaDetallePaquete) {
        this.listaDetallePaquete = listaDetallePaquete;
    }

    public String getCodPaquete() {
        return codPaquete;
    }

    public void setCodPaquete(String codPaquete) {
        this.codPaquete = codPaquete;
    }

    public String getNomPaquete() {
        return nomPaquete;
    }

    public void setNomPaquete(String nomPaquete) {
        this.nomPaquete = nomPaquete;
    }

    public List<CitPaqMaestro> getListaPaquetesMaestro() {
        return listaPaquetesMaestro;
    }

    public void setListaPaquetesMaestro(List<CitPaqMaestro> listaPaquetesMaestro) {
        this.listaPaquetesMaestro = listaPaquetesMaestro;
    }

    public CitPaqMaestro getPaqMaestroSeleccionado() {
        return paqMaestroSeleccionado;
    }

    public void setPaqMaestroSeleccionado(CitPaqMaestro paqMaestroSeleccionado) {
        this.paqMaestroSeleccionado = paqMaestroSeleccionado;
    }

    public boolean isRendBotones() {
        return rendBotones;
    }

    public void setRendBotones(boolean rendBotones) {
        this.rendBotones = rendBotones;
    }

    public List<SelectItem> getItemsDias() {
        return ItemsDias;
    }

    public void setItemsDias(List<SelectItem> ItemsDias) {
        this.ItemsDias = ItemsDias;
    }

    public boolean isRendBotonEliminarPaquete() {
        return rendBotonEliminarPaquete;
    }

    public void setRendBotonEliminarPaquete(boolean rendBotonEliminarPaquete) {
        this.rendBotonEliminarPaquete = rendBotonEliminarPaquete;
    }

}
