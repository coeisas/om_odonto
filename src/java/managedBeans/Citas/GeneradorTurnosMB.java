/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.Citas;

import beans.utilidades.LazyAgendaModel;
import beans.utilidades.LazyPrestadorDataModel;
import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.CfgConsultorios;
import modelo.entidades.CfgHorario;
import modelo.entidades.CfgItemsHorario;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.CitTurnos;
import modelo.fachadas.CfgConsultoriosFacade;
import modelo.fachadas.CfgDiasNoLaboralesFacade;
import modelo.fachadas.CfgHorarioFacade;
import modelo.fachadas.CfgItemsHorarioFacade;
import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.CitTurnosFacade;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author mario
 */
@ManagedBean(name = "generadorTurnosMB")
@SessionScoped
public class GeneradorTurnosMB extends MetodosGenerales implements Serializable {

    @ManagedProperty(value = "#{horarioMB}")
    private HorarioMB horarioMB;

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    CfgItemsHorarioFacade cfgItemsHorarioFacade;
    @EJB
    CitTurnosFacade citTurnosFacade;
    @EJB
    CfgUsuariosFacade usuariosFachada;
    @EJB
    CfgConsultoriosFacade consultorioFacade;
    @EJB
    CfgHorarioFacade cfgHorarioFacade;
    @EJB
    CfgDiasNoLaboralesFacade diasNoLaboralesFacade;

    //---------------------------------------------------
    //-----------------ENTIDADES ------------------------
    //---------------------------------------------------
    private CfgUsuarios prestadorSeleccionado;
    private CfgUsuarios prestadorTmp;
    private List<CfgItemsHorario> lunes;
    private List<CfgItemsHorario> martes;
    private List<CfgItemsHorario> miercoles;
    private List<CfgItemsHorario> jueves;
    private List<CfgItemsHorario> viernes;
    private List<CfgItemsHorario> sabado;
    private List<CfgItemsHorario> domingo;

    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------        
    private int id_sede;//sedes de la empresa

    //DATOS CONSULTORIOS-------------------------------
    private List<SelectItem> listaConsultorios;
    private int id_consultorio;
    private int concurrencia = 1;
    private String identificacionPrestador = "";
    private Date fechaIni;
    private Date fechaFin;
    private int duracion;
    private Date horaInicial;
    //PRESTADOR-----------------------------------------
    private LazyDataModel<CfgUsuarios> listaPrestadores;
    private String id_prestador;
    private String nombreCompleto;
    private String display = "none";
    private String displayBtnEliminarAgenda = "none";
    private String displayHoraInicial = "block";
    private String accion = "simple"; //simple -> crea turno(s) sencillo sin seguir agenda; agenda -> crea agenda basandose en los elementos del horario seleccionado
    private List listaComponentesActualizar;
    private List<SelectItem> listaEspecialidades;
    private boolean hayPrestadorSeleccionado;
//    private boolean renderBtnBorrarAgenda;
    private List<Short> diasLaborales;
    //HORARIO------------------------------
    private int id_horario;
    private List<SelectItem> listahorarios;
    private boolean rendBtnEliminarHorario = false;
    //AGENDA-------------------------------
    private LazyAgendaModel evenModel;
//    private ScheduleEvent event = new DefaultScheduleEvent();

    public GeneradorTurnosMB() {
    }

    @PostConstruct
    private void inicializador() {
        setDuracion(20);
        setListaPrestadores(new LazyPrestadorDataModel(usuariosFachada));
        //setListaPrestadores(prestadoresFachada.findAll());
        cargarEspecialidadesPrestadores();
        //carga los horarios
        cargarItemsHorario();
        String aux = "formCrearAgenda:";
        listaComponentesActualizar = new ArrayList();
        listaComponentesActualizar.add(aux.concat("schTurnos"));
//        listaComponentesActualizar.add(aux.concat("idCBeliminarAgenda"));
    }

    public void validarIdentificacion() {//verifica si existe la identificacion de lo contrario abre un dialogo para seleccionar el prestador de una tabla
//        setRenderBtnBorrarAgenda(false);
        displayBtnEliminarAgenda = "none";
        setFechaIni(null);
        setFechaFin(null);
        setId_horario(0);
        setDuracion(20);
        setConcurrencia(1);
        prestadorTmp = usuariosFachada.buscarPorIdentificacion(identificacionPrestador);
        if (prestadorTmp != null) {
            prestadorSeleccionado = prestadorTmp;
            identificacionPrestador = prestadorSeleccionado.getIdentificacion();
            loadEvents();

            RequestContext.getCurrentInstance().update("formAsignarPrestador");
//            RequestContext.getCurrentInstance().update("formCrearAgenda");
        } else {
            prestadorSeleccionado = null;
            RequestContext.getCurrentInstance().update("formAsignarPrestador");
            RequestContext.getCurrentInstance().execute("PF('dlgfindPrestador').show();");
        }
        functionDisplay();
    }

    //--------------------------------------------------------------------------------------------------------
    //-------------------------------------METODOS PRESTADORES------------------------------------------------
    //--------------------------------------------------------------------------------------------------------
    private void cargarEspecialidadesPrestadores() {
        setListaEspecialidades((List<SelectItem>) new ArrayList());
        List<CfgClasificaciones> lista = usuariosFachada.findEspecialidades();
        for (CfgClasificaciones especialidad : lista) {
            if (especialidad != null) {
                getListaEspecialidades().add(new SelectItem(especialidad.getId(), especialidad.getDescripcion()));
            }
        }
    }

    public void functionDisplay() {
//        setRenderBtnBorrarAgenda(false);
        displayBtnEliminarAgenda = "none";
        setFechaIni(null);
        setFechaFin(null);
        setId_horario(0);
        setDuracion(20);
        setConcurrencia(1);
        if (getPrestadorSeleccionado() != null) {
            setIdentificacionPrestador(prestadorSeleccionado.getIdentificacion());
            setDisplay("block");
        } else {
            setDisplay("none");
        }
        if (listaConsultorios != null) {
            listaConsultorios.clear();
        }
        setId_sede(0);
        RequestContext.getCurrentInstance().update("formCrearAgenda");
    }

    public void findprestador() {
        if (!id_prestador.isEmpty()) {
            try {
                setPrestadorSeleccionado(usuariosFachada.find(Integer.parseInt(getId_prestador())));
                if (getPrestadorSeleccionado() == null) {
                    setHayPrestadorSeleccionado(false);
                    imprimirMensaje("Error", "No se encontro el prestador", FacesMessage.SEVERITY_ERROR);
                    setNombreCompleto(null);
                } else {
                    //List<CfgConsultorios> listaconsultorios = consultorioFacade.getConsultoriosBySedeAndEspecialidad(getId_sede(), getPrestadorSeleccionado().getEspecialidad().getId());
                    //construirArrayList(listaconsultorios);
                    loadEvents();
                    setPrestadorSeleccionado(getPrestadorSeleccionado());

                    setHayPrestadorSeleccionado(true);
                    setNombreCompleto(obtenerNombreCompleto());
                }
                functionDisplay();
            } catch (Exception e) {
                imprimirMensaje("Error", "Ingrese un codigo de prestador valido", FacesMessage.SEVERITY_ERROR);
                setHayPrestadorSeleccionado(false);
                setNombreCompleto(null);
                setDisplay("none");
            }
        } else {
            setPrestadorSeleccionado(null);
            functionDisplay();
            setHayPrestadorSeleccionado(false);
            setNombreCompleto(null);
        }
    }

    private String obtenerNombreCompleto() {
        setNombreCompleto("");
        if (getPrestadorSeleccionado().getPrimerNombre() != null) {
            setNombreCompleto(getNombreCompleto() + getPrestadorSeleccionado().getPrimerNombre());
        }
        if (getPrestadorSeleccionado().getSegundoNombre() != null) {
            setNombreCompleto(getNombreCompleto() + " " + getPrestadorSeleccionado().getSegundoNombre());
        }
        if (getPrestadorSeleccionado().getPrimerApellido() != null) {
            setNombreCompleto(getNombreCompleto() + " " + getPrestadorSeleccionado().getPrimerApellido());
        }
        if (getPrestadorSeleccionado().getSegundoApellido() != null) {
            setNombreCompleto(getNombreCompleto() + " " + getPrestadorSeleccionado().getSegundoApellido());
        }
        return getNombreCompleto();
    }

    //-------------------------------------------------------------------------------------------------------- 
    //-------------------------METODOS PARA CONSULTORIOS------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------
    private void construirArrayList(List<CfgConsultorios> listaconsultorios) {
        listaConsultorios = new ArrayList<>();
        for (CfgConsultorios consultorio : listaconsultorios) {
            listaConsultorios.add(new SelectItem(consultorio.getIdConsultorio(), consultorio.getNomConsultorio()));
        }
    }

    public void cargarListaConsultorios() {
        //System.out.println("cargando los consultorios");
        loadEvents();
        if (getId_sede() != 0 && getPrestadorSeleccionado() != null) {
            List<CfgConsultorios> listaconsultorios = consultorioFacade.getBySede(getId_sede());
            construirArrayList(listaconsultorios);
            if (listaconsultorios.isEmpty()) {
                imprimirMensaje("Alerta", "La sede seleccionada no cuenta con consultorios de la especialidad del prestador", FacesMessage.SEVERITY_WARN);
            }
        } else {
            listaConsultorios.clear();
        }
    }

    //--------------------------------------------------------------------------------------------------------
    //------------------HORARIO DE LA AGENDA DEL PRESTADOR----------------------------------------------------
    //--------------------------------------------------------------------------------------------------------
    public void cargarItemsHorario() {
        List<CfgHorario> cfgHorarios = cfgHorarioFacade.findAll();
        listahorarios = new ArrayList();
        for (CfgHorario cfgHorario : cfgHorarios) {
            listahorarios.add(new SelectItem(cfgHorario.getIdHorario(), cfgHorario.getDescripcion()));
        }
        setListahorarios(listahorarios);
//        RequestContext.getCurrentInstance().update("formCrearAgenda:idHorario");
    }

    //--------------------------------------------------------------------------------------------------------
    //----------------------------METODOS PARA GENERAR TURNOS O AGENDA DEL PRESTADOR -------------------------
    //--------------------------------------------------------------------------------------------------------
    //crea una agenda completa    
    public void generarTurnos() throws ParseException {
        //System.out.println("Entro al generador de turnos");
        if (getPrestadorSeleccionado() == null) {
            imprimirMensaje("Error", "Es necesario escoger algun prestador", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (getId_consultorio() == 0) {
            imprimirMensaje("Error", "Falta elegir consultorio", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (duracion < 0) {
            duracion = 20;
            imprimirMensaje("Error", "El valor de la duracion de la cita no es valido", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (concurrencia < 1) {
            concurrencia = 1;
            imprimirMensaje("Error", "El valor mimino para la concurrencia es 1", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (fechaIni == null) {
            imprimirMensaje("Error", "Falta elegir fecha inicial", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (fechaFin == null) {
            fechaFin = fechaIni;
        }

        if (getId_horario() == 0) {
            imprimirMensaje("Error", "Falta elegir el horario", FacesMessage.SEVERITY_ERROR);
            return;

        }

        //comprueban si existen turnos del prestador creados dentro del intervalo de tiempo, consultorio y horario
        if (!citTurnosFacade.buscarTurnosParametrizado(prestadorSeleccionado.getIdUsuario(), fechaIni, fechaFin, id_consultorio, id_horario).isEmpty()) {
            imprimirMensaje("Error", "Se encontro una agenda dentro de esas especificaciones", FacesMessage.SEVERITY_ERROR);
            return;
        }
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = new Date();
        Date fechaActual = formatter.parse(formatter.format(fecha));
        if (fechaIni.before(fechaActual) || fechaFin.before(fechaActual)) {
            imprimirMensaje("Error", "Las fechas: incial y final no deben corresponder a una fecha anterior a la actual", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (fechaFin.before(fechaIni)) {
            imprimirMensaje("Error", "La fecha final corresponde a una fecha anterior a la inicial", FacesMessage.SEVERITY_ERROR);
            return;
        }
//        if(fechaIni.before(new Date())){
//            imprimirMensaje("Error", "La fecha inicial corresponde a una fecha anterior a la actual", FacesMessage.SEVERITY_ERROR);
//            return;            
//        }
//        if (fechaFin.before(new Date())) {
//            imprimirMensaje("Error", "La fecha final corresponde a una fecha anterior a la actual", FacesMessage.SEVERITY_ERROR);
//            return;
//        }
        boolean bandera = false;
        diasLaborales = cfgItemsHorarioFacade.findSelectedDays(getId_horario());
        crearHorario();
        long oneDayMilSec = 86400000;
        long startHour = 0;
        long endHour = 0;
        long timeCita = duracion * 60000;
        long startDateMilSec = fechaIni.getTime();
        long endDateMilSec = fechaFin.getTime();
        Date aux;
        Date aux2;

        //lista que almacena el id de la sede elegida y el 0  usado para identificar una fecha no laboral comun para todas las sedes
        List<Integer> id_sedes = new ArrayList();
        id_sedes.add(id_sede);
        id_sedes.add(0);

        //BUCLE ENCARGADO DE TRANSLADARSE DIARIAMENTE ENTRE LAS FECHAS SELECCIONADAS
        for (long d = startDateMilSec; d <= endDateMilSec; d = d + oneDayMilSec) {
            aux = new Date(d);
            //valida que la fecha actual del recorrido no se encuentre en la tabla cfg_dias_no_laborales
            if (diasNoLaboralesFacade.FindDiaNoLaboral(id_sedes, aux) == null) {
                if (esdialaboral(aux.getDay())) {
                    Date aux3 = aux;
                    String dia = String.valueOf(aux3.getDay());
                    List<CfgItemsHorario> auxiliar = findHorario(Short.parseShort(dia));

                    //CICLO ENCARGADO DE CONSULTAR EL HORARIO PARA EL DIA ACTUAL DEL RECORRIDO
                    for (CfgItemsHorario h : auxiliar) {
                        //HORA DE INICIO Y FNALIZACION DEL HORARIO, PUEDEN SER VARIOS EN UN MISMO DIA
                        startHour = generarMilisegundos(aux3, h.getHoraInicio());
                        endHour = generarMilisegundos(aux3, h.getHoraFinal());
                        //ciclo encargado de iterar con base a la duracion de la cita
                        for (long e = startHour; e < endHour; e = e + timeCita) {

                            //CONTROLA QUE LA HORA FINAL DE LA CITA NO SUPERE LA HORA FINAL DEL HORARIO DE TRABAJO DEL PRESTADOR
                            if (e + timeCita > endHour) {
                                break;
                            }

                            //SE ENCARGA DE CREAR VARIOS CITTURNOS DE ACUERDO A LA CONCURRENCIA ESPECIFICADA
                            for (int j = 0; j < concurrencia; j++) {

                                aux2 = new Date(e);
                            //System.out.println("\t" + aux2);

                                //persistiendo el turno
                                CitTurnos turno = new CitTurnos();
                                //para concurrencia se envia 1, ya que se cambio la tabla por la agenda (SULCHEDULE) que maneja de forma grafica la concurrencia
                                turno.setConcurrencia(1);
                                turno.setFecha(aux);
                                turno.setHoraIni(new Date(e));
                                turno.setHoraFin(new Date(e + timeCita));
                                turno.setIdPrestador(prestadorSeleccionado);
                                CfgConsultorios consultorio = new CfgConsultorios();
                                consultorio.setIdConsultorio(getId_consultorio());
                                turno.setIdConsultorio(consultorio);
                                CfgHorario horario = new CfgHorario(id_horario);
                                turno.setIdHorario(horario);
                                turno.setFechaCreacion(new Date());
                                //turno.setEstado(true);
                                turno.setEstado("disponible");
                                citTurnosFacade.create(turno);

                            }
                        }
                    }
                    bandera = true;
                }

            }
        }
//        setRenderBtnBorrarAgenda(true);
        if (bandera) {
            displayBtnEliminarAgenda = "block";
            loadEvents();
//        schTurnos
            RequestContext.getCurrentInstance().update(listaComponentesActualizar);
            //liberar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "La agenda fue creada"));
//        imprimirMensaje("Correcto", "La agenda fue creada", FacesMessage.SEVERITY_INFO);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La agenda no fue creada. Probablemente el periodo elegido corresponde a un dia no laboral para el horario o la sede"));
        }
    }

//    crea turnos adicionales o sin tener en cuenta un horario
    public void crearTurno() throws ParseException {
        if (id_consultorio == 0) {
            imprimirMensaje("Error", "Falta elegir consultorio", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (duracion < 0) {
            duracion = 20;
            imprimirMensaje("Error", "El valor de la duracion de la cita no es valido", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (concurrencia < 1) {
            concurrencia = 1;
            imprimirMensaje("Error", "El valor mimino para la concurrencia es 1", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (horaInicial == null) {
            imprimirMensaje("Error", "Ingrese la hora inicial de la cita", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (fechaIni == null) {
            imprimirMensaje("Error", "Falta elegir fecha inicial", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (fechaFin == null && fechaIni != null) {
            fechaFin = fechaIni;
        }
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = new Date();
        Date fechaActual = formatter.parse(formatter.format(fecha));
        if (fechaIni.before(fechaActual) || fechaFin.before(fechaActual)) {
            imprimirMensaje("Error", "Las fechas: incial y final no deben corresponder a una fecha anterior a la actual", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (fechaFin.before(fechaIni)) {
            imprimirMensaje("Error", "La fecha final corresponde a una fecha anterior a la inicial", FacesMessage.SEVERITY_ERROR);
            return;
        }
        Calendar f_inicial = Calendar.getInstance();
        f_inicial.setTime(fechaIni);
        Calendar f_final = Calendar.getInstance();
        f_final.setTime(fechaFin);
        long timeCita = duracion * 60000;
        List<Integer> id_sedes = new ArrayList();
        id_sedes.add(id_sede);
        id_sedes.add(0);
        boolean bandera = false;
        while (!f_inicial.after(f_final)) {
//            System.out.println(f_inicial.getTime());
            //valida que la fecha actual del recorrido no se encuentre en la tabla cfg_dias_no_laborales
            if (diasNoLaboralesFacade.FindDiaNoLaboral(id_sedes, f_inicial.getTime()) == null) {
                Date aux = f_inicial.getTime();
                String dia = String.valueOf(aux.getDay());
                //SE ENCARGA DE CREAR VARIOS CITTURNOS DE ACUERDO A LA CONCURRENCIA ESPECIFICADA
                for (int j = 0; j < concurrencia; j++) {
                    //persistiendo el turno
                    CitTurnos turno = new CitTurnos();
                    //para concurrencia se envia 1, ya que se cambio la tabla por la agenda (SULCHEDULE) que maneja de forma grafica la concurrencia
                    turno.setConcurrencia(1);
                    turno.setFecha(aux);
                    turno.setHoraIni(horaInicial);
                    turno.setHoraFin(new Date(horaInicial.getTime() + timeCita));
//                    System.out.println(turno.getHoraIni() + " - " + turno.getHoraFin());
                    turno.setIdPrestador(prestadorSeleccionado);
                    CfgConsultorios consultorio = consultorioFacade.find(id_consultorio);
//                    consultorio.setIdConsultorio(getId_consultorio());
                    turno.setIdConsultorio(consultorio);
                    turno.setFechaCreacion(new Date());
                    turno.setEstado("disponible");
                    citTurnosFacade.create(turno);
                }
                bandera = true;
            }
            f_inicial.add(Calendar.DATE, 1);
        }
        if (bandera) {
            loadEvents();
            liberar();
            List<String> componentes = new ArrayList();
            componentes.add("formCrearAgenda:schTurnos");
//            componentes.add("idFormCrearTurnoDlg");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "La agenda fue creada"));
            RequestContext.getCurrentInstance().update(componentes);
//            RequestContext.getCurrentInstance().execute("PF('dlgCrearTurno').hide()");

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se crearon los turnos es posible que el rango de fecha corresponda a dias no laborales"));
        }
        //liberar();
    }

    public void accionControlador() throws ParseException {
        switch (accion) {
            case "simple":
                crearTurno();
                break;
            case "agenda":
                generarTurnos();
                break;
        }

    }

    private List<CfgItemsHorario> findHorario(Short dia) {
        List<CfgItemsHorario> h = null;
        switch (dia) {
            case 0:
                h = domingo;
                break;
            case 1:
                h = lunes;
                break;
            case 2:
                h = martes;
                break;
            case 3:
                h = miercoles;
                break;
            case 4:
                h = jueves;
                break;
            case 5:
                h = viernes;
                break;
            case 6:
                h = sabado;
                break;
        }
        return h;
    }

    private void crearHorario() {
        for (Short d : diasLaborales) {
            switch (d) {
                case 0:
                    domingo = cfgItemsHorarioFacade.findDateByDay(getId_horario(), d);
                    break;
                case 1:
                    lunes = cfgItemsHorarioFacade.findDateByDay(getId_horario(), d);
                    break;
                case 2:
                    martes = cfgItemsHorarioFacade.findDateByDay(getId_horario(), d);
                    break;
                case 3:
                    miercoles = cfgItemsHorarioFacade.findDateByDay(getId_horario(), d);
                    break;
                case 4:
                    jueves = cfgItemsHorarioFacade.findDateByDay(getId_horario(), d);
                    break;
                case 5:
                    viernes = cfgItemsHorarioFacade.findDateByDay(getId_horario(), d);
                    break;
                case 6:
                    sabado = cfgItemsHorarioFacade.findDateByDay(getId_horario(), d);
                    break;
            }
        }

    }

    private long generarMilisegundos(Date fecha, Date hora) {
        fecha.setHours(hora.getHours());
        fecha.setMinutes(hora.getMinutes());
        Long milisegundos = fecha.getTime();
        return milisegundos;
    }

    private void liberar() {
        this.concurrencia = 1;
//        setDuracion(new Date(1200000));
        setDuracion(20);
        this.horaInicial = null;
        setId_sede(0);
        this.fechaFin = null;
        this.fechaIni = null;

//        citTurnosFacade = null;
        //prestadorMB.setPrestadorSeleccionado(null);
        setId_consultorio(0);
        setId_horario(0);
    }

    //BASANDODE EN LOS DIAS SELECCIONADOS EN EL FORMULARIO SE CONTENMPLARA SI ES UN DIA LABORAL 
    private boolean esdialaboral(int dia) {
        boolean retorno = false;
        for (Short d : diasLaborales) {
            if (d == dia) {
                retorno = true;
                break;
            }
        }

        return retorno;
    }

    public void eliminarAgenda() throws ParseException {
//        setRenderBtnBorrarAgenda(false);
//        displayBtnEliminarAgenda = "none";
        if (id_sede == 0) {
            imprimirMensaje("Error", "Especifique la sede de donde se hara la eliminacion", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (fechaIni == null) {
            imprimirMensaje("Error", "Se necesita la fecha inicial", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (fechaFin == null) {
            imprimirMensaje("Error", "Se necesita la fecha final", FacesMessage.SEVERITY_ERROR);
            return;
        }
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = new Date();
        Date fechaActual = formatter.parse(formatter.format(fecha));
        if (fechaIni.before(fechaActual) || fechaFin.before(fechaActual)) {
            imprimirMensaje("Error", "Las fechas: incial y final no deben corresponder a una fecha anterior a la actual", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (fechaFin.before(fechaIni)) {
            imprimirMensaje("Error", "La fecha final corresponde a una fecha anterior a la inicial", FacesMessage.SEVERITY_ERROR);
            return;
        }
        List estados = new ArrayList();//listado de los estados de los turnos de no eliminacion
        estados.add("asignado");
        estados.add("en_espera");
        estados.add("atendido");
        try {
            int totalTurnos = citTurnosFacade.ValidarEliminarAgenda(prestadorSeleccionado.getIdUsuario(), id_sede, fechaIni, fechaFin, id_horario);
            if (totalTurnos > 0) {
                int result = citTurnosFacade.EliminarAgenda(prestadorSeleccionado.getIdUsuario(), id_sede, fechaIni, fechaFin, id_horario, estados);
                if (result == totalTurnos) {
                    RequestContext.getCurrentInstance().update(listaComponentesActualizar);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Agenda eliminada"));
//                    imprimirMensaje("Correcto", "Turnos eliminados", FacesMessage.SEVERITY_INFO);
                } else if (result > 0) {
//                    RequestContext.getCurrentInstance().update("formCrearAgenda:idCBeliminarAgenda");
                    citTurnosFacade.actualizarTurno(prestadorSeleccionado.getIdUsuario(), id_sede, fechaIni, fechaFin, id_horario);
                    imprimirMensaje("Informacion", "Se eliminaron unicamente los turnos que nunca han sido asignados a una cita", FacesMessage.SEVERITY_WARN);
                } else {
                    result = citTurnosFacade.actualizarTurno(prestadorSeleccionado.getIdUsuario(), id_sede, fechaIni, fechaFin, id_horario);
                    if (result == 0) {
                        imprimirMensaje("Error", "No encontraron turnos a elminar", FacesMessage.SEVERITY_ERROR);
                        return;
                    }
                    imprimirMensaje("Informacion", "Los turnos disponibles relacionados con una cita cancelada no lo estaran", FacesMessage.SEVERITY_INFO);
                }
                loadEvents();
            } else {
//                RequestContext.getCurrentInstance().update("formCrearAgenda:idCBeliminarAgenda");
                imprimirMensaje("Error", "No se econtraron turnos a eliminiar", FacesMessage.SEVERITY_ERROR);
            }
        } catch (Exception e) {
            imprimirMensaje("Error", "Agenda no eliminada", FacesMessage.SEVERITY_ERROR);
        }

    }

    //-----------------------------------------------------------------------------------
    //----------------METODOS CREAR HORARIO----------------------------------------------
    //-----------------------------------------------------------------------------------
    public void seleccionarHorario() {
        //imprimirMensaje("Informacion", "Ha elegido el horario " + id_horario, FacesMessage.SEVERITY_INFO);
        if (id_horario == 0) {
            displayHoraInicial = "block";
            accion = "simple";
            horarioMB.validarHorario(null);
            setRendBtnEliminarHorario(false);
        } else {
            displayHoraInicial = "none";
            accion = "agenda";
            horarioMB.validarHorario(cfgHorarioFacade.find(id_horario));
            setRendBtnEliminarHorario(true);
        }
    }

    public void saveHorarioAndItems() {
        horarioMB.guardarHorario();
        horarioMB.getItemsHorarioMB().guardarItems();
        cargarItemsHorario();
        RequestContext.getCurrentInstance().update("formCrearAgenda:idHorario");
    }

    public void limpiarFormHorario() {
        if (id_horario == 0) {
            horarioMB.setDesc(null);
            horarioMB.getItemsHorarioMB().setHorarioseleccionado(null);
            horarioMB.getItemsHorarioMB().setHoraFin(null);
            horarioMB.getItemsHorarioMB().setHoraIni(null);
            horarioMB.getItemsHorarioMB().setListaItemsHorario(new ArrayList());
            horarioMB.getItemsHorarioMB().setIndex(0);
            horarioMB.validarHorario(null);
        }
    }

    public void eliminarHorario() {
        try {
            if (citTurnosFacade.ValidarEliminarHorario(id_horario)) {
                CfgHorario horario = cfgHorarioFacade.find(id_horario);
                int totalItems = horario.getCfgItemsHorarioList().size();
                int elementosEliminados = cfgItemsHorarioFacade.eliminarByIdHorario(id_horario);
                RequestContext.getCurrentInstance().execute("PF('dlghorario').hide()");
                if (totalItems == 0 || totalItems == elementosEliminados) {
                    horario = cfgHorarioFacade.find(id_horario);
                    cfgHorarioFacade.remove(horario);
                    cargarItemsHorario();
//                    RequestContext.getCurrentInstance().update("message");
                    imprimirMensaje("Correcto", "Horario eliminado", FacesMessage.SEVERITY_INFO);
                    RequestContext.getCurrentInstance().update("formCrearAgenda:idHorario");
                } else {
                    imprimirMensaje("Error", "El horario no fue eliminado completamente", FacesMessage.SEVERITY_ERROR);
                }
                setId_horario(0);

            } else {
                imprimirMensaje("Error", "El horario esta siendo usado", FacesMessage.SEVERITY_ERROR);
            }
        } catch (Exception e) {
            imprimirMensaje("Error", "El horario esta siendo usado", FacesMessage.SEVERITY_ERROR);
        }
    }

    //------------------------------------------------------------------------------------
    //---------------------------METODOS AGENDA--------------------------------------
    //------------------------------------------------------------------------------------
    public void loadEvents() {
        evenModel = new LazyAgendaModel(prestadorSeleccionado.getIdUsuario(), id_sede, citTurnosFacade, null, "generarTurnos");
        RequestContext.getCurrentInstance().update("formCrearAgenda:schTurnos");
    }

    //------------------------------------------------------------------------------------
    //---------------------------GETTERS AND SETTERS--------------------------------------
    //------------------------------------------------------------------------------------
    /**
     * @return the fechaIni
     */
    public Date getFechaIni() {
        return fechaIni;
    }

    /**
     * @param fechaIni the fechaIni to set
     */
    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    /**
     * @return the fechaFin
     */
    public Date getFechaFin() {
        return fechaFin;
    }

    /**
     * @param fechaFin the fechaFin to set
     */
    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * @return the concurrencia
     */
    public int getConcurrencia() {
        return concurrencia;
    }

    /**
     * @param concurrencia the concurrencia to set
     */
    public void setConcurrencia(int concurrencia) {
        this.concurrencia = concurrencia;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
//        duracion.setHours(0);
        this.duracion = duracion;
    }

    public LazyDataModel<CfgUsuarios> getListaPrestadores() {
        return listaPrestadores;
    }

    public void setListaPrestadores(LazyDataModel<CfgUsuarios> listaPrestadores) {
        this.listaPrestadores = listaPrestadores;
    }

    public String getId_prestador() {
        return id_prestador;
    }

    public void setId_prestador(String id_prestador) {
        this.id_prestador = id_prestador;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public List<SelectItem> getListaEspecialidades() {
        return listaEspecialidades;
    }

    public void setListaEspecialidades(List<SelectItem> listaEspecialidades) {
        this.listaEspecialidades = listaEspecialidades;
    }

    public boolean isHayPrestadorSeleccionado() {
        return hayPrestadorSeleccionado;
    }

    public void setHayPrestadorSeleccionado(boolean hayPrestadorSeleccionado) {
        this.hayPrestadorSeleccionado = hayPrestadorSeleccionado;
    }

    public CfgUsuarios getPrestadorSeleccionado() {
        return prestadorSeleccionado;
    }

    public void setPrestadorSeleccionado(CfgUsuarios prestadorSeleccionado) {
        this.prestadorSeleccionado = prestadorSeleccionado;
    }

    public List<SelectItem> getListaConsultorios() {
        return listaConsultorios;
    }

    public void setListaConsultorios(List<SelectItem> listaConsultorios) {
        this.listaConsultorios = listaConsultorios;
    }

    public int getId_consultorio() {
        return id_consultorio;
    }

    public void setId_consultorio(int id_consultorio) {
        this.id_consultorio = id_consultorio;
    }

    public int getId_sede() {
        return id_sede;
    }

    public void setId_sede(int id_sede) {
        this.id_sede = id_sede;
    }

    public int getId_horario() {
        return id_horario;
    }

    public void setId_horario(int id_horario) {
        this.id_horario = id_horario;
    }

    public List<SelectItem> getListahorarios() {
        return listahorarios;
    }

    public void setListahorarios(List<SelectItem> listahorarios) {
        this.listahorarios = listahorarios;
    }

    public LazyAgendaModel getEvenModel() {
        return evenModel;
    }

    public void setEvenModel(LazyAgendaModel evenModel) {
        this.evenModel = evenModel;
    }

    public HorarioMB getHorarioMB() {
        return horarioMB;
    }

    public void setHorarioMB(HorarioMB horarioMB) {
        this.horarioMB = horarioMB;
    }

    public String getIdentificacionPrestador() {
        return identificacionPrestador;
    }

    public void setIdentificacionPrestador(String identificacionPrestador) {
        this.identificacionPrestador = identificacionPrestador;
    }

//    public boolean isRenderBtnBorrarAgenda() {
//        return renderBtnBorrarAgenda;
//    }
//
//    public void setRenderBtnBorrarAgenda(boolean renderBtnBorrarAgenda) {
//        this.renderBtnBorrarAgenda = renderBtnBorrarAgenda;
//    }
    public String getDisplayBtnEliminarAgenda() {
        return displayBtnEliminarAgenda;
    }

    public Date getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(Date horaInicial) {
        this.horaInicial = horaInicial;
    }

    public boolean isRendBtnEliminarHorario() {
        return rendBtnEliminarHorario;
    }

    public void setRendBtnEliminarHorario(boolean rendBtnEliminarHorario) {
        this.rendBtnEliminarHorario = rendBtnEliminarHorario;
    }

    public String getDisplayHoraInicial() {
        return displayHoraInicial;
    }

}
