/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.seguridad;

import beans.enumeradores.ClasificacionesEnum;
import modelo.entidades.CfgClasificaciones;
import modelo.fachadas.CfgClasificacionesFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import beans.utilidades.Sesion;
import modelo.entidades.CfgInsumo;
import modelo.entidades.CfgMedicamento;
import modelo.entidades.CfgPerfilesUsuario;
import modelo.entidades.CfgSede;
import modelo.entidades.HcTipoReg;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.FacAdministradora;
import modelo.entidades.FacPaquete;
import modelo.entidades.FacServicio;
import modelo.fachadas.CfgInsumoFacade;
import modelo.fachadas.CfgMedicamentoFacade;
import modelo.fachadas.CfgPerfilesUsuarioFacade;
import modelo.fachadas.CfgSedeFacade;
import modelo.fachadas.HcTipoRegFacade;
import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.FacAdministradoraFacade;
import modelo.fachadas.FacPaqueteFacade;
import modelo.fachadas.FacServicioFacade;

/**
 *
 * @author santos
 */
@ManagedBean(name = "aplicacionGeneralMB")
@ApplicationScoped
public class AplicacionGeneralMB {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    CfgClasificacionesFacade clasificacionesFacade;
    @EJB
    CfgPerfilesUsuarioFacade perfilesUsuarioFacade;
    @EJB
    CfgUsuariosFacade usuariosFacade;
    @EJB
    HcTipoRegFacade tipoRegCliFacade;
    @EJB
    FacServicioFacade servicioFacade;
    @EJB
    CfgSedeFacade cfgSedeFacade;
    @EJB
    FacAdministradoraFacade administradoraFacade;
    @EJB
    CfgInsumoFacade insumoFacade;
    @EJB
    CfgMedicamentoFacade medicamentoFacade;
    @EJB
    FacPaqueteFacade paqueteFacade;

    //---------------------------------------------------
    //-----------------ENTIDADES ------------------------
    //---------------------------------------------------
    private List<SelectItem> listaTipoIdentificacion;
    private List<SelectItem> listaGenero;
    private List<SelectItem> listaTipoEdad;
    private List<SelectItem> listaZona;
    private List<SelectItem> listaRegimen;
    private List<SelectItem> listaEstrato;
    private List<SelectItem> listaEstadoCivil;
    private List<SelectItem> listaGrupoSanguineo;
    private List<SelectItem> listaEtnia;
    private List<SelectItem> listaEscolaridad;
    private List<SelectItem> listaTipoAfiliado;
    private List<SelectItem> listaDepartamentos;
    private List<SelectItem> listaEspecialidades;
    private List<SelectItem> listaOcupaciones;
    private List<SelectItem> listaTipoTarifa;
    private List<SelectItem> listaTipoUsuario;
    private List<SelectItem> listaTipoConsumo;
    //private List<SelectItem> listaFinalidad;
    private List<SelectItem> listaFinalidadConsulta;
    private List<SelectItem> listaFinalidadProcedimiento;
    private List<SelectItem> listaAmbito;
    private List<SelectItem> listaActoQuirurgico;
    private List<SelectItem> listaPersonalAtiende;
    private List<SelectItem> listaCausaExterna;
    private List<SelectItem> listaClasificacionEvento;
    private List<SelectItem> listaMotivoConsulta;
    private List<SelectItem> listaMotivoCancelacionCitas;
    private List<CfgPerfilesUsuario> listaPerfilesUsuario;
    private List<SelectItem> listaTipoDiagnosticoConsulta;
    private List<SelectItem> listaGenero3;
    private List<SelectItem> listaTipoConsulta;
    private List<SelectItem> listaTipoPago;
    private List<SelectItem> listaTipoFacturacion;
    private List<SelectItem> listaTipoContrato;
    private List<HcTipoReg> listaTipoRegistroClinico;

    private List<FacServicio> listaServicios;
    private List<CfgInsumo> listaInsumos;
    private List<CfgMedicamento> listaMedicamentos;
    private List<FacPaquete> listaPaquetes;
    private List<CfgUsuarios> listaPrestadores;
    private List<CfgUsuarios> listaUsuarios;
    private List<SelectItem> listaSedes;
    private List<SelectItem> listaParentesco;
    private List<SelectItem> listaCategoriaPaciente;
    private List<FacAdministradora> listaAdministradoras;
    private List<SelectItem> listaTipoAdministradora;

    //---------------------------------------------------
    //-----------------VARIABLES -------------------------
    //---------------------------------------------------
    private ArrayList<Sesion> sesionesActuales = new ArrayList<>();
    private List<SelectItem> listaMeses;

    //---------------------------------------------------
    //----------------- FUNCIONES -----------------------
    //---------------------------------------------------
    public AplicacionGeneralMB() {
    }

    public void inicializar() {
        if (listaGenero == null) {//no se han cargaron las listas
            cargarClasificacion(ClasificacionesEnum.ActoQuirurgico);
            cargarClasificacion(ClasificacionesEnum.Administradoras);
            cargarClasificacion(ClasificacionesEnum.Ambito);
            cargarClasificacion(ClasificacionesEnum.CategoriaPaciente);
            cargarClasificacion(ClasificacionesEnum.CausaExterna);
            cargarClasificacion(ClasificacionesEnum.ClasificacionEvento);
            cargarClasificacion(ClasificacionesEnum.DPTO);
            cargarClasificacion(ClasificacionesEnum.Escolaridad);
            cargarClasificacion(ClasificacionesEnum.Etnia);
            cargarClasificacion(ClasificacionesEnum.EstadoCivil);
            cargarClasificacion(ClasificacionesEnum.Especialidad);
            //cargarClasificacion(ClasificacionesEnum.Finalidad);
            cargarClasificacion(ClasificacionesEnum.FinalidadConsulta);
            cargarClasificacion(ClasificacionesEnum.FinalidadProcedimiento);
            cargarClasificacion(ClasificacionesEnum.GrupoSanguineo);
            cargarClasificacion(ClasificacionesEnum.Genero);
            cargarClasificacion(ClasificacionesEnum.Genero3);
            cargarClasificacion(ClasificacionesEnum.Insumos);
            cargarClasificacion(ClasificacionesEnum.Medicamentos);
            cargarClasificacion(ClasificacionesEnum.Meses);
            cargarClasificacion(ClasificacionesEnum.MotiCancCitas);
            cargarClasificacion(ClasificacionesEnum.MotivoConsulta);
            cargarClasificacion(ClasificacionesEnum.Estrato);
            cargarClasificacion(ClasificacionesEnum.Ocupacion);
            cargarClasificacion(ClasificacionesEnum.Paquetes);
            cargarClasificacion(ClasificacionesEnum.Parentesco);
            cargarClasificacion(ClasificacionesEnum.PerfilesUsuario);
            cargarClasificacion(ClasificacionesEnum.PersonalAtiende);
            cargarClasificacion(ClasificacionesEnum.Prestadores);
            cargarClasificacion(ClasificacionesEnum.Regimen);
            cargarClasificacion(ClasificacionesEnum.Sedes);
            cargarClasificacion(ClasificacionesEnum.Servicios);
            cargarClasificacion(ClasificacionesEnum.TipoAfiliado);
            cargarClasificacion(ClasificacionesEnum.TipoAdministradora);
            cargarClasificacion(ClasificacionesEnum.TipoConsulta);
            cargarClasificacion(ClasificacionesEnum.TipoContrato);
            cargarClasificacion(ClasificacionesEnum.TipoConsumo);
            cargarClasificacion(ClasificacionesEnum.TipoDiagnosticoConsulta);
            cargarClasificacion(ClasificacionesEnum.TipoEdad);
            cargarClasificacion(ClasificacionesEnum.TipoFacturacion);
            cargarClasificacion(ClasificacionesEnum.TipoIdentificacion);
            cargarClasificacion(ClasificacionesEnum.TipoPago);
            cargarClasificacion(ClasificacionesEnum.TipoRegistroClinico);
            cargarClasificacion(ClasificacionesEnum.TipoTarifa);
            cargarClasificacion(ClasificacionesEnum.TipoUsuario);
            cargarClasificacion(ClasificacionesEnum.Usuarios);
            cargarClasificacion(ClasificacionesEnum.Zona);
        }
    }

    public void cargarClasificacion(ClasificacionesEnum maestro) {

        switch (maestro) {

            case ActoQuirurgico:
                listaActoQuirurgico = cargarClasificacion(maestro.toString());
                break;
            case Administradoras:
                listaAdministradoras = administradoraFacade.buscarOrdenado();
                break;
            case Ambito:
                listaAmbito = cargarClasificacion(maestro.toString());
                break;
            case CategoriaPaciente:
                listaCategoriaPaciente = cargarClasificacion(maestro.toString());
                break;
            case CausaExterna:
                listaCausaExterna = cargarClasificacion(maestro.toString());
                break;
            case ClasificacionEvento:
                listaClasificacionEvento = cargarClasificacion(maestro.toString());
                break;
            case DPTO:
                listaDepartamentos = cargarClasificacion(maestro.toString());
                break;
            case Especialidad:
                listaEspecialidades = cargarClasificacion(maestro.toString());
                break;
            case Estrato:
                listaEstrato = cargarClasificacion(maestro.toString());
                break;
            case EstadoCivil:
                listaEstadoCivil = cargarClasificacion(maestro.toString());
                break;
            case Etnia:
                listaEtnia = cargarClasificacion(maestro.toString());
                break;
            case Escolaridad:
                listaEscolaridad = cargarClasificacion(maestro.toString());
                break;
//            case Finalidad:
//                listaFinalidad = cargarClasificacion(maestro.toString());
//                break;
            case FinalidadConsulta:
                listaFinalidadConsulta = cargarClasificacion(maestro.toString());
                break;
            case FinalidadProcedimiento:
                listaFinalidadProcedimiento = cargarClasificacion(maestro.toString());
                break;
            case GrupoSanguineo:
                listaGrupoSanguineo = cargarClasificacion(maestro.toString());
                break;
            case Genero:
                listaGenero = cargarClasificacion(maestro.toString());
                break;
            case Genero3:
                listaGenero3 = cargarClasificacion(maestro.toString());
                break;
            case Insumos:
                listaInsumos = insumoFacade.buscarOrdenado();
                break;
            case Medicamentos:
                listaMedicamentos = medicamentoFacade.buscarOrdenado();
                break;
            case Meses:
                cargarMeses();
                break;
            case MotiCancCitas:
                listaMotivoCancelacionCitas = cargarClasificacion(maestro.toString());
                break;
            case MotivoConsulta:
                listaMotivoConsulta = cargarClasificacion(maestro.toString());
                break;
            case Ocupacion:
                listaOcupaciones = cargarClasificacion(maestro.toString());
                break;
            case Paquetes:
                listaPaquetes = paqueteFacade.buscarOrdenado();
                break;
            case Parentesco:
                listaParentesco = cargarClasificacion(maestro.toString());
                break;
            case Prestadores:
                listaPrestadores = usuariosFacade.findPrestadores();
                break;
            case PerfilesUsuario:
                listaPerfilesUsuario = perfilesUsuarioFacade.findAll();
                break;
            case PersonalAtiende:
                listaPersonalAtiende = cargarClasificacion(maestro.toString());
                break;
            case Regimen:
                listaRegimen = cargarClasificacion(maestro.toString());
                break;
            case Servicios:
                listaServicios = servicioFacade.buscarActivos();
                break;
            case Sedes:
                listaSedes = generarSelectItem(cfgSedeFacade.findAll());
                break;
            case TipoRegistroClinico:
                listaTipoRegistroClinico = tipoRegCliFacade.buscarTiposRegstroActivos();
                break;
            case TipoAfiliado:
                listaTipoAfiliado = cargarClasificacion(maestro.toString());
                break;
            case TipoIdentificacion:
                listaTipoIdentificacion = cargarClasificacion(maestro.toString());
                break;
            case TipoEdad:
                listaTipoEdad = cargarClasificacion(maestro.toString());
                break;
            case TipoTarifa:
                listaTipoTarifa = cargarClasificacion(maestro.toString());
                break;
            case TipoUsuario:
                listaTipoUsuario = cargarClasificacion(maestro.toString());
                break;
            case TipoAdministradora:
                listaTipoAdministradora = cargarClasificacion(maestro.toString());
                break;
            case TipoDiagnosticoConsulta:
                listaTipoDiagnosticoConsulta = cargarClasificacion(maestro.toString());
                break;
            case TipoPago:
                listaTipoPago = cargarClasificacion(maestro.toString());
                break;
            case TipoContrato:
                listaTipoContrato = cargarClasificacion(maestro.toString());
                break;
            case TipoConsumo:
                listaTipoConsumo = cargarClasificacion(maestro.toString());
                break;
            case TipoFacturacion:
                listaTipoFacturacion = cargarClasificacion(maestro.toString());
                break;
            case TipoConsulta:
                listaTipoConsulta = cargarClasificacion(maestro.toString(), "TRUE");//solo cargar los que esten activos
                break;
            case Usuarios:
                listaUsuarios = usuariosFacade.buscarOrdenado();
                break;
            case Zona:
                listaZona = cargarClasificacion(maestro.toString());
                break;
        }
    }

    private List<SelectItem> generarSelectItem(List<CfgSede> lista) {
        List<SelectItem> listaRetorno = new ArrayList<>();
        for (CfgSede cfgSede : lista) {
            listaRetorno.add(new SelectItem(cfgSede.getIdSede(), cfgSede.getNombreSede()));
        }
        return listaRetorno;

    }

    private List<SelectItem> cargarClasificacion(String maestro) {
        List<SelectItem> listaRetorno = new ArrayList<>();
        List<CfgClasificaciones> listaClasificaciones = clasificacionesFacade.buscarPorMaestro(maestro);
        for (CfgClasificaciones clasificacion : listaClasificaciones) {
            listaRetorno.add(new SelectItem(clasificacion.getId(), clasificacion.getCodigo() + " - " + clasificacion.getDescripcion()));
        }
        return listaRetorno;
    }

    private List<SelectItem> cargarClasificacion(String maestro, String observacion) {
        List<SelectItem> listaRetorno = new ArrayList<>();
        List<CfgClasificaciones> listaClasificaciones = clasificacionesFacade.buscarPorMaestroObservacion(maestro, observacion);
        for (CfgClasificaciones clasificacion : listaClasificaciones) {
            listaRetorno.add(new SelectItem(clasificacion.getId(), clasificacion.getCodigo() + " - " + clasificacion.getDescripcion()));
        }
        return listaRetorno;
    }

    public void cargarMeses() {
        listaMeses = new ArrayList<>();
        listaMeses.add(new SelectItem("0", "Enero"));
        listaMeses.add(new SelectItem("1", "Febrero"));
        listaMeses.add(new SelectItem("2", "Marzo"));
        listaMeses.add(new SelectItem("3", "Abril"));
        listaMeses.add(new SelectItem("4", "Mayo"));
        listaMeses.add(new SelectItem("5", "Junio"));
        listaMeses.add(new SelectItem("6", "Julio"));
        listaMeses.add(new SelectItem("7", "Agosto"));
        listaMeses.add(new SelectItem("8", "Septiembre"));
        listaMeses.add(new SelectItem("9", "Octubre"));
        listaMeses.add(new SelectItem("10", "Noviembre"));
        listaMeses.add(new SelectItem("11", "Diciembre"));
    }

    public boolean estaLogueado(int idUsuario) {
        //determina si un usario tiene una sesion iniciada //idUser= identificador del usuario en la base de datos
        for (Sesion sesion : sesionesActuales) {
            if (sesion.getIdUsuario() == idUsuario) {
                return true;
            }
        }
        return false;
    }

    public void addSession(int idUser, String idSession) {
        //adicionar a la lista de sesiones activas //
        sesionesActuales.add(new Sesion(idSession, idUser));
    }

    public void removeSession(int idUsuario) {
        //eliminar de la lista de sesiones activas dependiento del id del usuario
        for (Sesion sesion : sesionesActuales) {
            if (sesion.getIdUsuario() == idUsuario) {
                sesionesActuales.remove(sesion);//toca ver si funciona sino usar un index
                //sesionesActuales.remove(i);
                return;
            }
        }
    }

    public void removeSession(String idSesion) {
        //eliminar de la lista de sesiones actuales dependiento del id de la sesion
        for (Sesion sesion : sesionesActuales) {
            if (sesion.getIdSesion().equals(idSesion)) {
                sesionesActuales.remove(sesion);//toca ver si funciona sino usar un index
                //sesionesActuales.remove(i);
                return;
            }
        }
    }

    public boolean buscarPorIdSesion(String idSesion) {
        //buscar una session segun su id
        for (Sesion sesion : sesionesActuales) {
            if (sesion.getIdSesion().equals(idSesion)) {
                return true;
            }
        }
        return false;
    }

    public void cerraTodasLasSesiones() {
        //eliminar todas las sessiones activas (se usa cuando se realiza una restauracion de la copia de seguridad)
        sesionesActuales = new ArrayList<>();
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------
    public List<SelectItem> getListaTipoIdentificacion() {
        return listaTipoIdentificacion;
    }

    public void setListaTipoIdentificacion(List<SelectItem> listaTipoIdentificacion) {
        this.listaTipoIdentificacion = listaTipoIdentificacion;
    }

    public List<SelectItem> getListaGenero() {
        return listaGenero;
    }

    public void setListaGenero(List<SelectItem> listaGenero) {
        this.listaGenero = listaGenero;
    }

    public List<SelectItem> getListaTipoEdad() {
        return listaTipoEdad;
    }

    public void setListaTipoEdad(List<SelectItem> listaTipoEdad) {
        this.listaTipoEdad = listaTipoEdad;
    }

    public List<SelectItem> getListaZona() {
        return listaZona;
    }

    public void setListaZona(List<SelectItem> listaZona) {
        this.listaZona = listaZona;
    }

    public List<SelectItem> getListaRegimen() {
        return listaRegimen;
    }

    public void setListaRegimen(List<SelectItem> listaRegimen) {
        this.listaRegimen = listaRegimen;
    }

    public List<SelectItem> getListaEstrato() {
        return listaEstrato;
    }

    public void setListaEstrato(List<SelectItem> listaEstrato) {
        this.listaEstrato = listaEstrato;
    }

    public List<SelectItem> getListaEstadoCivil() {
        return listaEstadoCivil;
    }

    public void setListaEstadoCivil(List<SelectItem> listaEstadoCivil) {
        this.listaEstadoCivil = listaEstadoCivil;
    }

    public List<SelectItem> getListaGrupoSanguineo() {
        return listaGrupoSanguineo;
    }

    public void setListaGrupoSanguineo(List<SelectItem> listaGrupoSanguineo) {
        this.listaGrupoSanguineo = listaGrupoSanguineo;
    }

    public List<SelectItem> getListaEtnia() {
        return listaEtnia;
    }

    public void setListaEtnia(List<SelectItem> listaEtnia) {
        this.listaEtnia = listaEtnia;
    }

    public List<SelectItem> getListaEscolaridad() {
        return listaEscolaridad;
    }

    public void setListaEscolaridad(List<SelectItem> listaEscolaridad) {
        this.listaEscolaridad = listaEscolaridad;
    }

    public List<SelectItem> getListaTipoAfiliado() {
        return listaTipoAfiliado;
    }

    public void setListaTipoAfiliado(List<SelectItem> listaTipoAfiliado) {
        this.listaTipoAfiliado = listaTipoAfiliado;
    }

    public List<SelectItem> getListaDepartamentos() {
        return listaDepartamentos;
    }

    public void setListaDepartamentos(List<SelectItem> listaDepartamentos) {
        this.listaDepartamentos = listaDepartamentos;
    }

    public List<SelectItem> getListaEspecialidades() {
        return listaEspecialidades;
    }

    public void setListaEspecialidades(List<SelectItem> listaEspecialidades) {
        this.listaEspecialidades = listaEspecialidades;
    }

    public List<SelectItem> getListaOcupaciones() {
        return listaOcupaciones;
    }

    public void setListaOcupaciones(List<SelectItem> listaOcupaciones) {
        this.listaOcupaciones = listaOcupaciones;
    }

    public List<SelectItem> getListaTipoUsuario() {
        return listaTipoUsuario;
    }

    public void setListaTipoUsuario(List<SelectItem> listaTipoUsuario) {
        this.listaTipoUsuario = listaTipoUsuario;
    }

    public List<CfgPerfilesUsuario> getListaPerfilesUsuario() {
        return listaPerfilesUsuario;
    }

    public void setListaPerfilesUsuario(List<CfgPerfilesUsuario> listaPerfilesUsuario) {
        this.listaPerfilesUsuario = listaPerfilesUsuario;
    }

    public List<CfgUsuarios> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<CfgUsuarios> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public List<SelectItem> getListaGenero3() {
        return listaGenero3;
    }

    public void setListaGenero3(List<SelectItem> listaGenero3) {
        this.listaGenero3 = listaGenero3;
    }

    public List<HcTipoReg> getListaTipoRegistroClinico() {
        return listaTipoRegistroClinico;
    }

    public void setListaTipoRegistroClinico(List<HcTipoReg> listaTipoRegistroClinico) {
        this.listaTipoRegistroClinico = listaTipoRegistroClinico;
    }

//    public List<SelectItem> getListaFinalidad() {
//        return listaFinalidad;
//    }
//
//    public void setListaFinalidad(List<SelectItem> listaFinalidad) {
//        this.listaFinalidad = listaFinalidad;
//    }
    public List<SelectItem> getListaMotivoConsulta() {
        return listaMotivoConsulta;
    }

    public void setListaMotivoConsulta(List<SelectItem> listaMotivoConsulta) {
        this.listaMotivoConsulta = listaMotivoConsulta;
    }

    public List<SelectItem> getListaTipoDiagnosticoConsulta() {
        return listaTipoDiagnosticoConsulta;
    }

    public void setListaTipoDiagnosticoConsulta(List<SelectItem> listaTipoDiagnosticoConsulta) {
        this.listaTipoDiagnosticoConsulta = listaTipoDiagnosticoConsulta;
    }

    public List<FacServicio> getListaServicios() {
        return listaServicios;
    }

    public void setListaServicios(List<FacServicio> listaServicios) {
        this.listaServicios = listaServicios;
    }

    public List<SelectItem> getListaTipoConsulta() {
        return listaTipoConsulta;
    }

    public void setListaTipoConsulta(List<SelectItem> listaTipoConsulta) {
        this.listaTipoConsulta = listaTipoConsulta;
    }

    public List<CfgUsuarios> getListaPrestadores() {
        return listaPrestadores;
    }

    public void setListaPrestadores(List<CfgUsuarios> listaPrestadores) {
        this.listaPrestadores = listaPrestadores;
    }

    public List<SelectItem> getListaMotivoCancelacionCitas() {
        return listaMotivoCancelacionCitas;
    }

    public void setListaMotivoCancelacionCitas(List<SelectItem> listaMotivoCancelacion) {
        this.listaMotivoCancelacionCitas = listaMotivoCancelacion;
    }

    public List<SelectItem> getListaSedes() {
        return listaSedes;
    }

    public void setListaSedes(List<SelectItem> listaSedes) {
        this.listaSedes = listaSedes;
    }

    public List<SelectItem> getListaParentesco() {
        return listaParentesco;
    }

    public void setListaParentesco(List<SelectItem> listaParentesco) {
        this.listaParentesco = listaParentesco;
    }

    public List<FacAdministradora> getListaAdministradoras() {
        return listaAdministradoras;
    }

    public void setListaAdministradoras(List<FacAdministradora> listaAdministradoras) {
        this.listaAdministradoras = listaAdministradoras;
    }

    public List<SelectItem> getListaCategoriaPaciente() {
        return listaCategoriaPaciente;
    }

    public void setListaCategoriaPaciente(List<SelectItem> listaCategoriaPaciente) {
        this.listaCategoriaPaciente = listaCategoriaPaciente;
    }

    public List<SelectItem> getListaTipoAdministradora() {
        return listaTipoAdministradora;
    }

    public void setListaTipoAdministradora(List<SelectItem> listaTipoAdministradora) {
        this.listaTipoAdministradora = listaTipoAdministradora;
    }

    public List<SelectItem> getListaTipoPago() {
        return listaTipoPago;
    }

    public void setListaTipoPago(List<SelectItem> listaTipoPago) {
        this.listaTipoPago = listaTipoPago;
    }

    public List<SelectItem> getListaTipoFacturacion() {
        return listaTipoFacturacion;
    }

    public void setListaTipoFacturacion(List<SelectItem> listaTipoFacturacion) {
        this.listaTipoFacturacion = listaTipoFacturacion;
    }

    public List<SelectItem> getListaTipoContrato() {
        return listaTipoContrato;
    }

    public void setListaTipoContrato(List<SelectItem> listaTipoContrato) {
        this.listaTipoContrato = listaTipoContrato;
    }

    public List<CfgInsumo> getListaInsumos() {
        return listaInsumos;
    }

    public void setListaInsumos(List<CfgInsumo> listaInsumos) {
        this.listaInsumos = listaInsumos;
    }

    public List<CfgMedicamento> getListaMedicamentos() {
        return listaMedicamentos;
    }

    public void setListaMedicamentos(List<CfgMedicamento> listaMedicamentos) {
        this.listaMedicamentos = listaMedicamentos;
    }

    public List<FacPaquete> getListaPaquetes() {
        return listaPaquetes;
    }

    public void setListaPaquetes(List<FacPaquete> listaPaquetes) {
        this.listaPaquetes = listaPaquetes;
    }

    public List<SelectItem> getListaMeses() {
        return listaMeses;
    }

    public void setListaMeses(List<SelectItem> listaMeses) {
        this.listaMeses = listaMeses;
    }

    public List<SelectItem> getListaTipoConsumo() {
        return listaTipoConsumo;
    }

    public void setListaTipoConsumo(List<SelectItem> listaTipoConsumo) {
        this.listaTipoConsumo = listaTipoConsumo;
    }

    public List<SelectItem> getListaTipoTarifa() {
        return listaTipoTarifa;
    }

    public void setListaTipoTarifa(List<SelectItem> listaTipoTarifa) {
        this.listaTipoTarifa = listaTipoTarifa;
    }

    public List<SelectItem> getListaFinalidadConsulta() {
        return listaFinalidadConsulta;
    }

    public void setListaFinalidadConsulta(List<SelectItem> listaFinalidadConsulta) {
        this.listaFinalidadConsulta = listaFinalidadConsulta;
    }

    public List<SelectItem> getListaFinalidadProcedimiento() {
        return listaFinalidadProcedimiento;
    }

    public void setListaFinalidadProcedimiento(List<SelectItem> listaFinalidadProcedimiento) {
        this.listaFinalidadProcedimiento = listaFinalidadProcedimiento;
    }

    public List<SelectItem> getListaAmbito() {
        return listaAmbito;
    }

    public void setListaAmbito(List<SelectItem> listaAmbito) {
        this.listaAmbito = listaAmbito;
    }

    public List<SelectItem> getListaActoQuirurgico() {
        return listaActoQuirurgico;
    }

    public void setListaActoQuirurgico(List<SelectItem> listaActoQuirurgico) {
        this.listaActoQuirurgico = listaActoQuirurgico;
    }

    public List<SelectItem> getListaPersonalAtiende() {
        return listaPersonalAtiende;
    }

    public void setListaPersonalAtiende(List<SelectItem> listaPersonalAtiende) {
        this.listaPersonalAtiende = listaPersonalAtiende;
    }

    public List<SelectItem> getListaCausaExterna() {
        return listaCausaExterna;
    }

    public void setListaCausaExterna(List<SelectItem> listaCausaExterna) {
        this.listaCausaExterna = listaCausaExterna;
    }

    public List<SelectItem> getListaClasificacionEvento() {
        return listaClasificacionEvento;
}

    public void setListaClasificacionEvento(List<SelectItem> listaClasificacionEvento) {
        this.listaClasificacionEvento = listaClasificacionEvento;
    }

}
