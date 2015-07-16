package managedBeans.configuraciones;

import beans.utilidades.LazyPacienteDataModel;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.CfgImagenes;
import modelo.entidades.CfgPacientes;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgImagenesFacade;
import modelo.fachadas.CfgPacientesFacade;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.imageio.stream.FileImageOutputStream;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import beans.utilidades.MetodosGenerales;
import modelo.fachadas.FacAdministradoraFacade;
import org.apache.poi.xssf.usermodel.*;
import org.primefaces.model.LazyDataModel;

@ManagedBean(name = "pacientesMB")
@SessionScoped
public class PacientesMB extends MetodosGenerales implements Serializable {
    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------

    @EJB
    CfgPacientesFacade pacientesFachada;
    @EJB
    CfgClasificacionesFacade clasificacionesFachada;
    @EJB
    CfgImagenesFacade imagenesFacade;
    @EJB
    FacAdministradoraFacade administradoraFacade;

    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------
    private LazyDataModel<CfgPacientes> listaPacientes;
    private CfgPacientes pacienteSeleccionado;
    private CfgPacientes pacienteSeleccionadoTabla;
    private CfgPacientes nuevoPaciente;
    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------
    private UploadedFile archivoFirma;
    private UploadedFile archivoFoto;
    private final LoginMB loginMB;
    private final String firmaPorDefecto = "../recursos/img/firma.png";
    private final String fotoPorDefecto = "../recursos/img/img_usuario.png";
    private String urlFirma = firmaPorDefecto;
    private String urlFoto = fotoPorDefecto;
    private List<SelectItem> listaMunicipios;
    private List<SelectItem> listaServicios;
    //private List<CfgClasificaciones> listaCompletaOcupaciones;
    private boolean fotoTomadaWebCam = false;//saber si la foto se tomo de la webcam
    private boolean nuevoRegistro = true;
    private String tabActivaPacientes = "0";//cual tab esa activa(0=datos presonales 1=datos adicionales)
    //DATOS PRINCIPALES  
    private String tituloTabPacientes = "Datos nuevo paciente";
    private String tipoIdentificacion = "";
    private String identificacion = "";
    private String lugarExpedicion = "";
    private Date fechaNacimiento = null;
    private String fechNacimiConvetEdad = "-";//fecha de nacimiento convertida en edad
    private String genero = "";
    private String grupoSanguineo = "";
    private String primerNombre = "";
    private String segundoNombre = "";
    private String primerApellido = "";
    private String segundoApellido = "";
    private String estadoCivil = "";
    private String ocupacion = "";
    private String telefonoResidencia = "";
    private String telefonoOficina = "";
    private String celular = "";
    private String departamento = "";
    private String municipio = "";
    private String zona = "";
    private String barrio = "";
    private String direccion = "";
    private String email = "";
    private Boolean activo = true;
    private String administradora = "";
    private String tipoAfiliado = "";
    private String regimen = "";
    private String categoriaPaciente;
    private String estrato = "";
    private String etnia = "";
    private String escolaridad = "";
    private String numeroAutorizacion = "";
    private String responsable;
    private String telefonoResponsable;
    private String parentesco;
    private String acompanante;
    private String telefonoAcompanante;
    private Date fechaAfiliacion = null;
    private Date fechaSisben = null;
    private String carnet = "";
    private Date fechaVenceCarnet = null;
    private String observaciones = "";

    //---------------------------------------------------
    //----------------- FUNCIONES -------------------------
    //---------------------------------------------------      
    @PostConstruct
    public void inicializar() {
        nuevoRegistro = true;
        listaPacientes = new LazyPacienteDataModel(pacientesFachada);
        //listaCompletaOcupaciones = clasificacionesFachada.buscarPorMaestro("ocupaciones");
    }

    public void cambiaFechaNacimiento() {
        if (fechaNacimiento != null) {
            fechNacimiConvetEdad = calcularEdad(fechaNacimiento);
        } else {
            fechNacimiConvetEdad = "-";
        }
    }

    public PacientesMB() {
        loginMB = (LoginMB) FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
    }

    public void cargarMunicipios() {
        listaMunicipios = new ArrayList<>();
        if (departamento != null && departamento.length() != 0) {
            List<CfgClasificaciones> listaM = clasificacionesFachada.buscarMunicipioPorDepartamento(clasificacionesFachada.find(Integer.parseInt(departamento)).getCodigo());
            for (CfgClasificaciones mun : listaM) {
                listaMunicipios.add(new SelectItem(mun.getId(), mun.getDescripcion()));
            }
        }
    }

    public void limpiarFormulario() {
        tituloTabPacientes = "Datos nuevo paciente";
        pacienteSeleccionado = null;
        archivoFirma = null;
        archivoFoto = null;
        fotoTomadaWebCam = false;
        urlFirma = firmaPorDefecto;
        urlFoto = fotoPorDefecto;
        tipoIdentificacion = "";
        identificacion = "";
        lugarExpedicion = "";
        fechaNacimiento = null;
        fechNacimiConvetEdad = "-";
        genero = "";
        grupoSanguineo = "";
        primerNombre = "";
        segundoNombre = "";
        primerApellido = "";
        segundoApellido = "";
        estadoCivil = "";
        ocupacion = "";
        telefonoResidencia = "";
        telefonoOficina = "";
        celular = "";
        departamento = "";
        municipio = "";
        zona = "";
        barrio = "";
        direccion = "";
        email = "";
        activo = true;
        administradora = "";
        tipoAfiliado = "";
        regimen = "";
        categoriaPaciente = "";
        estrato = "";
        etnia = "";
        escolaridad = "";
        numeroAutorizacion = "";
        responsable = "";
        telefonoResponsable = "";
        parentesco = "";
        acompanante = "";
        telefonoAcompanante = "";
        fechaAfiliacion = null;
        fechaSisben = null;
        carnet = "";
        fechaVenceCarnet = null;
        observaciones = "";
        cargarMunicipios();

    }

    public void cargarPacienteDesdeHistorias(String id) {//se llama a esta funcion desde historias para que cargue un paciente
        pacienteSeleccionadoTabla = pacientesFachada.find(Integer.parseInt(id));
        cargarPaciente();

    }

    public void cargarPaciente() {
        if (pacienteSeleccionadoTabla == null) {
            imprimirMensaje("Error", "Se debe seleccionar un paciente de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        limpiarFormulario();        
        pacienteSeleccionado = pacientesFachada.find(pacienteSeleccionadoTabla.getIdPaciente());
        tituloTabPacientes = "Datos paciente: " + pacienteSeleccionado.nombreCompleto();
        archivoFirma = null;
        archivoFoto = null;
        fotoTomadaWebCam = false;

        if (pacienteSeleccionado.getFirma() != null) {
            urlFirma = "../imagenesOpenmedical/" + pacienteSeleccionado.getFirma().getUrlImagen();
        } else {
            urlFirma = firmaPorDefecto;
        }
        if (pacienteSeleccionado.getFoto() != null) {
            urlFoto = "../imagenesOpenmedical/" + pacienteSeleccionado.getFoto().getUrlImagen();
        } else {
            urlFoto = fotoPorDefecto;
        }
        identificacion = pacienteSeleccionado.getIdentificacion();
        if (pacienteSeleccionado.getTipoIdentificacion() != null) {
            tipoIdentificacion = pacienteSeleccionado.getTipoIdentificacion().getId().toString();
        } else {
            tipoIdentificacion = "";
        }
        lugarExpedicion = pacienteSeleccionado.getLugarExpedicion();
        if (pacienteSeleccionado.getFechaNacimiento() != null) {
            fechaNacimiento = pacienteSeleccionado.getFechaNacimiento();
            fechNacimiConvetEdad = calcularEdad(fechaNacimiento);
        } else {
            fechaNacimiento = null;
            fechNacimiConvetEdad = "-";
        }
        if (pacienteSeleccionado.getGenero() != null) {
            genero = pacienteSeleccionado.getGenero().getId().toString();
        } else {
            genero = "";
        }
        if (pacienteSeleccionado.getGrupoSanguineo() != null) {
            grupoSanguineo = pacienteSeleccionado.getGrupoSanguineo().getId().toString();
        } else {
            grupoSanguineo = "";
        }
        primerNombre = pacienteSeleccionado.getPrimerNombre();
        segundoNombre = pacienteSeleccionado.getSegundoNombre();
        primerApellido = pacienteSeleccionado.getPrimerApellido();
        segundoApellido = pacienteSeleccionado.getSegundoApellido();
        if (pacienteSeleccionado.getEstadoCivil() != null) {
            estadoCivil = pacienteSeleccionado.getEstadoCivil().getId().toString();
        } else {
            estadoCivil = "";
        }
        if (pacienteSeleccionado.getOcupacion() != null) {
            ocupacion = pacienteSeleccionado.getOcupacion().getId().toString();
        } else {
            ocupacion = "";
        }
        telefonoResidencia = pacienteSeleccionado.getTelefonoResidencia();
        telefonoOficina = pacienteSeleccionado.getTelefonoOficina();
        celular = pacienteSeleccionado.getCelular();
        if (pacienteSeleccionado.getDepartamento() != null) {
            departamento = pacienteSeleccionado.getDepartamento().getId().toString();
            cargarMunicipios();
            municipio = pacienteSeleccionado.getMunicipio().getId().toString();
        } else {
            departamento = "";
            municipio = "";
        }
        if (pacienteSeleccionado.getZona() != null) {
            zona = pacienteSeleccionado.getZona().getId().toString();
        } else {
            zona = "";
        }
        barrio = pacienteSeleccionado.getBarrio();
        direccion = pacienteSeleccionado.getDireccion();
        email = pacienteSeleccionado.getEmail();
        activo = pacienteSeleccionado.getActivo();
        if (pacienteSeleccionado.getIdAdministradora() != null) {
            administradora = pacienteSeleccionado.getIdAdministradora().getIdAdministradora().toString();
        } else {
            administradora = "";
        }
        if (pacienteSeleccionado.getTipoAfiliado() != null) {
            tipoAfiliado = pacienteSeleccionado.getTipoAfiliado().getId().toString();
        } else {
            tipoAfiliado = "";
        }
        if (pacienteSeleccionado.getRegimen() != null) {
            regimen = pacienteSeleccionado.getRegimen().getId().toString();
        } else {
            regimen = "";
        }
        if (pacienteSeleccionado.getCategoriaPaciente() != null) {
            categoriaPaciente = pacienteSeleccionado.getCategoriaPaciente().getId().toString();
        } else {
            categoriaPaciente = "";
        }
        if (pacienteSeleccionado.getNivel() != null) {
            estrato = pacienteSeleccionado.getNivel().getId().toString();
        } else {
            estrato = "";
        }
        if (pacienteSeleccionado.getEtnia() != null) {
            etnia = pacienteSeleccionado.getEtnia().getId().toString();
        } else {
            etnia = "";
        }
        if (pacienteSeleccionado.getEscolaridad() != null) {
            escolaridad = pacienteSeleccionado.getEscolaridad().getId().toString();
        } else {
            escolaridad = "";
        }
        numeroAutorizacion = pacienteSeleccionado.getNumeroAutorizacion();
        responsable = pacienteSeleccionado.getResponsable();
        telefonoResponsable = pacienteSeleccionado.getTelefonoResponsable();
        if (pacienteSeleccionado.getParentesco() != null) {
            parentesco = pacienteSeleccionado.getParentesco().getId().toString();
        } else {
            parentesco = null;
        }
        acompanante = pacienteSeleccionado.getAcompanante();
        telefonoAcompanante = pacienteSeleccionado.getTelefonoAcompanante();

        if (pacienteSeleccionado.getFechaAfiliacion() != null) {
            fechaAfiliacion = pacienteSeleccionado.getFechaAfiliacion();
        } else {
            fechaAfiliacion = null;
        }
        if (pacienteSeleccionado.getFechaSisben() != null) {
            fechaSisben = pacienteSeleccionado.getFechaSisben();
        } else {
            fechaSisben = null;
        }
        carnet = pacienteSeleccionado.getCarnet();
        if (pacienteSeleccionado.getFechaVenceCarnet() != null) {
            fechaVenceCarnet = pacienteSeleccionado.getFechaVenceCarnet();
        } else {
            fechaVenceCarnet = null;
        }
        observaciones = pacienteSeleccionado.getObservaciones();
        tabActivaPacientes = "0";
    }

    public void guardarPaciente() {
        CfgPacientes pacienteTmp;
        //VALIDACION DE DATOS OBLIGATORIOS
        if (validacionCampoVacio(identificacion, "Identificación")) {
            return;
        }
        if (validacionCampoVacio(tipoIdentificacion, "Tipo de identificación")) {
            return;
        }
        if (fechaNacimiento == null) {
            imprimirMensaje("Error", "La fecha de nacimiento es obligatoria", FacesMessage.SEVERITY_ERROR);
            return;
        }
        //VALIDACION DE VALORES UNICOS
        if (pacienteSeleccionado == null) {//guardando nuevo paciente                        
            if (pacientesFachada.buscarPorIdentificacion(identificacion) != null) {
                imprimirMensaje("Error", "Ya existe un paciente con esta identificación", FacesMessage.SEVERITY_ERROR);
                return;
            }
            guardarNuevoPaciente();
        } else {//modificando paciente existente            
            pacienteTmp = pacientesFachada.buscarPorIdentificacion(identificacion);
            if (pacienteTmp != null && !pacienteSeleccionado.getIdentificacion().equals(pacienteTmp.getIdentificacion())) {
                imprimirMensaje("Error", "Existe un paciente diferente con esta identificación", FacesMessage.SEVERITY_ERROR);
                return;
            }
            actualizarPacienteExistente();
        }
        tabActivaPacientes = "0";
    }

    private void guardarNuevoPaciente() {
        nuevoPaciente = new CfgPacientes();
        String nombreImagenEnTmp;
        String extension;
        String nombreImagenReal;
        if (archivoFirma != null) {//se cargo firma         
            nombreImagenReal = archivoFirma.getFileName();
            extension = nombreImagenReal.substring(nombreImagenReal.lastIndexOf("."), nombreImagenReal.length());
            CfgImagenes nuevaImagen = new CfgImagenes();
            imagenesFacade.create(nuevaImagen);//crearlo para que me autogenere el ID            
            nombreImagenEnTmp = "firmaPaciente" + loginMB.getUsuarioActual().getIdUsuario() + extension;
            moverArchivo(loginMB.getUrltmp() + nombreImagenEnTmp, loginMB.getUrlFirmas() + nuevaImagen.getId().toString() + extension);
            nuevaImagen.setNombre(nombreImagenReal);
            nuevaImagen.setNombreEnServidor(nuevaImagen.getId().toString() + extension);
            nuevaImagen.setUrlImagen(loginMB.getBaseDeDatosActual() + "/firmas/" + nuevaImagen.getId().toString() + extension);
            imagenesFacade.edit(nuevaImagen);
            nuevoPaciente.setFirma(nuevaImagen);
        }
        if (archivoFoto != null || fotoTomadaWebCam) {//se cargo foto
            if (fotoTomadaWebCam) {//es por webCam
                nombreImagenReal = "fotoWebCam.png";
                extension = ".png";
            } else {//es por carga de archivo
                nombreImagenReal = archivoFoto.getFileName();
                extension = nombreImagenReal.substring(nombreImagenReal.lastIndexOf("."), nombreImagenReal.length());
            }

            CfgImagenes nuevaImagen = new CfgImagenes();
            imagenesFacade.create(nuevaImagen);//crearlo para que me autogenere el ID            
            nombreImagenEnTmp = "fotoPaciente" + loginMB.getUsuarioActual().getIdUsuario() + extension;
            moverArchivo(loginMB.getUrltmp() + nombreImagenEnTmp, loginMB.getUrlFotos() + nuevaImagen.getId().toString() + extension);
            nuevaImagen.setNombre(nombreImagenReal);
            nuevaImagen.setNombreEnServidor(nuevaImagen.getId().toString() + extension);
            nuevaImagen.setUrlImagen(loginMB.getBaseDeDatosActual() + "/fotos/" + nuevaImagen.getId().toString() + extension);
            imagenesFacade.edit(nuevaImagen);
            nuevoPaciente.setFoto(nuevaImagen);
        }
        nuevoPaciente.setIdentificacion(identificacion);
        if (validarNoVacio(tipoIdentificacion)) {
            nuevoPaciente.setTipoIdentificacion(clasificacionesFachada.find(Integer.parseInt(tipoIdentificacion)));
        }
        nuevoPaciente.setLugarExpedicion(lugarExpedicion);
        if (fechaNacimiento != null) {
            nuevoPaciente.setFechaNacimiento(fechaNacimiento);
        }
        if (validarNoVacio(genero)) {
            nuevoPaciente.setGenero(clasificacionesFachada.find(Integer.parseInt(genero)));
        }
        if (validarNoVacio(grupoSanguineo)) {
            nuevoPaciente.setGrupoSanguineo(clasificacionesFachada.find(Integer.parseInt(grupoSanguineo)));
        }
        nuevoPaciente.setPrimerNombre(primerNombre);
        nuevoPaciente.setSegundoNombre(segundoNombre);
        nuevoPaciente.setPrimerApellido(primerApellido);
        nuevoPaciente.setSegundoApellido(segundoApellido);
        if (validarNoVacio(estadoCivil)) {
            nuevoPaciente.setEstadoCivil(clasificacionesFachada.find(Integer.parseInt(estadoCivil)));
        }
        if (validarNoVacio(ocupacion)) {
            nuevoPaciente.setOcupacion(clasificacionesFachada.find(Integer.parseInt(ocupacion)));
        }
        nuevoPaciente.setTelefonoResidencia(telefonoResidencia);
        nuevoPaciente.setTelefonoOficina(telefonoOficina);
        nuevoPaciente.setCelular(celular);
        if (departamento != null && departamento.length() != 0) {
            nuevoPaciente.setDepartamento(clasificacionesFachada.find(Integer.parseInt(departamento)));
            nuevoPaciente.setMunicipio(clasificacionesFachada.find(Integer.parseInt(municipio)));
        }
        if (validarNoVacio(zona)) {
            nuevoPaciente.setZona(clasificacionesFachada.find(Integer.parseInt(zona)));
        }
        nuevoPaciente.setBarrio(barrio);
        nuevoPaciente.setDireccion(direccion);
        nuevoPaciente.setEmail(email);
        nuevoPaciente.setActivo(activo);
        if (validarNoVacio(administradora)) {
            nuevoPaciente.setIdAdministradora(administradoraFacade.find(Integer.parseInt(administradora)));
        }
        if (validarNoVacio(tipoAfiliado)) {
            nuevoPaciente.setTipoAfiliado(clasificacionesFachada.find(Integer.parseInt(tipoAfiliado)));
        }
        if (validarNoVacio(regimen)) {
            nuevoPaciente.setRegimen(clasificacionesFachada.find(Integer.parseInt(regimen)));
        }
        if (validarNoVacio(categoriaPaciente)) {
            nuevoPaciente.setCategoriaPaciente(clasificacionesFachada.find(Integer.parseInt(categoriaPaciente)));
        }
        if (validarNoVacio(estrato)) {
            nuevoPaciente.setNivel(clasificacionesFachada.find(Integer.parseInt(estrato)));
        }
        if (validarNoVacio(etnia)) {
            nuevoPaciente.setEtnia(clasificacionesFachada.find(Integer.parseInt(etnia)));
        }
        if (validarNoVacio(escolaridad)) {
            nuevoPaciente.setEscolaridad(clasificacionesFachada.find(Integer.parseInt(escolaridad)));
        }
        nuevoPaciente.setNumeroAutorizacion(numeroAutorizacion);
        nuevoPaciente.setResponsable(responsable);
        nuevoPaciente.setTelefonoResponsable(telefonoResponsable);
        if (validarNoVacio(parentesco)) {
            nuevoPaciente.setParentesco(clasificacionesFachada.find(Integer.parseInt(parentesco)));
        }
        nuevoPaciente.setAcompanante(acompanante);
        nuevoPaciente.setTelefonoAcompanante(telefonoAcompanante);

        if (fechaAfiliacion != null) {
            nuevoPaciente.setFechaAfiliacion(fechaAfiliacion);
        }
        if (fechaSisben != null) {
            nuevoPaciente.setFechaSisben(fechaSisben);
        }
        nuevoPaciente.setCarnet(carnet);
        if (fechaVenceCarnet != null) {
            nuevoPaciente.setFechaVenceCarnet(fechaVenceCarnet);
        }
        nuevoPaciente.setObservaciones(observaciones);
        pacientesFachada.create(nuevoPaciente);
        imprimirMensaje("Correcto", "Nuevo paciente creado correctamente", FacesMessage.SEVERITY_INFO);
        listaPacientes = new LazyPacienteDataModel(pacientesFachada);
        limpiarFormulario();//limpiar formulario
    }

    private void actualizarPacienteExistente() {
        String nombreImagenEnTmp;
        String extension;
        String nombreImagenReal;
        if (archivoFirma != null) {//se cargo imagen
            nombreImagenReal = archivoFirma.getFileName();
            extension = nombreImagenReal.substring(nombreImagenReal.lastIndexOf("."), nombreImagenReal.length());
            nombreImagenEnTmp = "firmaPaciente" + loginMB.getUsuarioActual().getIdUsuario() + extension;
            if (pacienteSeleccionado.getFirma() != null) {//existe firma
                moverArchivo(loginMB.getUrltmp() + nombreImagenEnTmp, loginMB.getUrlFirmas() + pacienteSeleccionado.getFirma().getId() + extension);
            } else {//no existe firma
                CfgImagenes nuevaImagen = new CfgImagenes();
                imagenesFacade.create(nuevaImagen);//crearlo para que me autogenere el ID            
                nombreImagenEnTmp = "firmaPaciente" + loginMB.getUsuarioActual().getIdUsuario() + extension;
                moverArchivo(loginMB.getUrltmp() + nombreImagenEnTmp, loginMB.getUrlFirmas() + nuevaImagen.getId().toString() + extension);
                nuevaImagen.setNombre(nombreImagenReal);
                nuevaImagen.setNombreEnServidor(nuevaImagen.getId().toString() + extension);
                nuevaImagen.setUrlImagen(loginMB.getBaseDeDatosActual() + "/firmas/" + nuevaImagen.getId().toString() + extension);
                imagenesFacade.edit(nuevaImagen);
                pacienteSeleccionado.setFirma(nuevaImagen);
            }
        }
        if (archivoFoto != null || fotoTomadaWebCam) {//se cargo foto
            if (fotoTomadaWebCam) {//es por webCam
                nombreImagenReal = "fotoWebCam.png";
                extension = ".png";
            } else {//es por carga de archivo
                nombreImagenReal = archivoFoto.getFileName();
                extension = nombreImagenReal.substring(nombreImagenReal.lastIndexOf("."), nombreImagenReal.length());
            }
            nombreImagenEnTmp = "fotoPaciente" + loginMB.getUsuarioActual().getIdUsuario() + extension;
            if (pacienteSeleccionado.getFoto() != null) {//existe foto
                moverArchivo(loginMB.getUrltmp() + nombreImagenEnTmp, loginMB.getUrlFotos() + pacienteSeleccionado.getFoto().getId() + extension);
                pacienteSeleccionado.getFoto().setNombreEnServidor(pacienteSeleccionado.getFoto().getId() + extension);
                pacienteSeleccionado.getFoto().setUrlImagen(loginMB.getBaseDeDatosActual() + "/fotos/" + pacienteSeleccionado.getFoto().getId() + extension);
                imagenesFacade.edit(pacienteSeleccionado.getFoto());
            } else {//no existe foto
                CfgImagenes nuevaImagen = new CfgImagenes();
                imagenesFacade.create(nuevaImagen);//crearlo para que me autogenere el ID            
                nombreImagenEnTmp = "fotoPaciente"
                        + loginMB.getUsuarioActual().getIdUsuario() + extension;
                moverArchivo(loginMB.getUrltmp() + nombreImagenEnTmp, loginMB.getUrlFotos() + nuevaImagen.getId().toString() + extension);
                nuevaImagen.setNombre(nombreImagenReal);
                nuevaImagen.setNombreEnServidor(nuevaImagen.getId().toString() + ".png");
                nuevaImagen.setUrlImagen(loginMB.getBaseDeDatosActual() + "/fotos/" + nuevaImagen.getId().toString() + extension);
                imagenesFacade.edit(nuevaImagen);
                pacienteSeleccionado.setFoto(nuevaImagen);
            }
        }

        pacienteSeleccionado.setIdentificacion(identificacion);
        if (validarNoVacio(tipoIdentificacion)) {
            pacienteSeleccionado.setTipoIdentificacion(clasificacionesFachada.find(Integer.parseInt(tipoIdentificacion)));
        }
        pacienteSeleccionado.setLugarExpedicion(lugarExpedicion);
        pacienteSeleccionado.setFechaNacimiento(fechaNacimiento);
        if (validarNoVacio(genero)) {
            pacienteSeleccionado.setGenero(clasificacionesFachada.find(Integer.parseInt(genero)));
        }
        if (validarNoVacio(grupoSanguineo)) {
            pacienteSeleccionado.setGrupoSanguineo(clasificacionesFachada.find(Integer.parseInt(grupoSanguineo)));
        }
        pacienteSeleccionado.setPrimerNombre(primerNombre);
        pacienteSeleccionado.setSegundoNombre(segundoNombre);
        pacienteSeleccionado.setPrimerApellido(primerApellido);
        pacienteSeleccionado.setSegundoApellido(segundoApellido);
        if (validarNoVacio(estadoCivil)) {
            pacienteSeleccionado.setEstadoCivil(clasificacionesFachada.find(Integer.parseInt(estadoCivil)));
        }
        if (validarNoVacio(ocupacion)) {
            pacienteSeleccionado.setOcupacion(clasificacionesFachada.find(Integer.parseInt(ocupacion)));
        }
        pacienteSeleccionado.setTelefonoResidencia(telefonoResidencia);
        pacienteSeleccionado.setTelefonoOficina(telefonoOficina);
        pacienteSeleccionado.setCelular(celular);
        if (departamento != null && departamento.length() != 0) {
            pacienteSeleccionado.setDepartamento(clasificacionesFachada.find(Integer.parseInt(departamento)));
            pacienteSeleccionado.setMunicipio(clasificacionesFachada.find(Integer.parseInt(municipio)));
        }
        if (validarNoVacio(zona)) {
            pacienteSeleccionado.setZona(clasificacionesFachada.find(Integer.parseInt(zona)));
        }
        pacienteSeleccionado.setBarrio(barrio);
        pacienteSeleccionado.setDireccion(direccion);
        pacienteSeleccionado.setEmail(email);
        pacienteSeleccionado.setActivo(activo);
        if (validarNoVacio(administradora)) {
            pacienteSeleccionado.setIdAdministradora(administradoraFacade.find(Integer.parseInt(administradora)));
        }
        if (validarNoVacio(tipoAfiliado)) {
            pacienteSeleccionado.setTipoAfiliado(clasificacionesFachada.find(Integer.parseInt(tipoAfiliado)));
        }
        if (validarNoVacio(regimen)) {
            pacienteSeleccionado.setRegimen(clasificacionesFachada.find(Integer.parseInt(regimen)));
        }
        if (validarNoVacio(categoriaPaciente)) {
            pacienteSeleccionado.setCategoriaPaciente(clasificacionesFachada.find(Integer.parseInt(categoriaPaciente)));
        }
        if (validarNoVacio(estrato)) {
            pacienteSeleccionado.setNivel(clasificacionesFachada.find(Integer.parseInt(estrato)));
        }
        if (validarNoVacio(etnia)) {
            pacienteSeleccionado.setEtnia(clasificacionesFachada.find(Integer.parseInt(etnia)));
        }
        if (validarNoVacio(escolaridad)) {
            pacienteSeleccionado.setEscolaridad(clasificacionesFachada.find(Integer.parseInt(escolaridad)));
        }
        pacienteSeleccionado.setNumeroAutorizacion(numeroAutorizacion);
        pacienteSeleccionado.setResponsable(responsable);
        pacienteSeleccionado.setTelefonoResponsable(telefonoResponsable);
        if (validarNoVacio(parentesco)) {
            pacienteSeleccionado.setParentesco(clasificacionesFachada.find(Integer.parseInt(parentesco)));
        }
        pacienteSeleccionado.setAcompanante(acompanante);
        pacienteSeleccionado.setTelefonoAcompanante(telefonoAcompanante);

        if (fechaAfiliacion != null) {
            pacienteSeleccionado.setFechaAfiliacion(fechaAfiliacion);
        }
        if (fechaSisben != null) {
            pacienteSeleccionado.setFechaSisben(fechaSisben);
        }
        pacienteSeleccionado.setCarnet(carnet);
        if (fechaVenceCarnet != null) {
            pacienteSeleccionado.setFechaVenceCarnet(fechaVenceCarnet);
        }
        pacienteSeleccionado.setObservaciones(observaciones);

        pacientesFachada.edit(pacienteSeleccionado);
        imprimirMensaje("Correcto", "Paciente actualizado correctamente", FacesMessage.SEVERITY_INFO);
        listaPacientes = new LazyPacienteDataModel(pacientesFachada);
        limpiarFormulario();//limpiar formulario
    }

    private UploadedFile file;
    private String newFileName = "";

    public void importarPacientes(FileUploadEvent event) {
        try {
            file = event.getFile();
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            newFileName = file.getFileName();
        } catch (Exception ex) {
            System.out.println("Error 20 en " + this.getClass().getName() + ":" + ex.toString());
            FacesMessage msg = new FacesMessage("Error:", "error al realizar la carga del archivo" + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    private void copyFile(String fileName, InputStream in) {
        try {
            try (OutputStream out = new FileOutputStream(new File(fileName))) {
                int read;
                byte[] bytes = new byte[1024];
                while ((read = in.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                in.close();
                out.flush();
            }
            System.out.println("El nuevo fichero fue creado con éxito!");
        } catch (IOException e) {
            System.out.println("Error 4 en " + this.getClass().getName() + ":" + e.toString());
        }
    }

    public void eliminarPaciente() {
        if (pacienteSeleccionado == null) {
            imprimirMensaje("Error", "No se ha cargado ningún paciente", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            pacientesFachada.remove(pacienteSeleccionado);
            pacienteSeleccionado = null;
            //listaPacientes=new LazyPacienteDataModel(pacientesFachada.numeroTotalRegistros(),pacientesFachada.consultaNativaPacientes("SELECT * FROM cfg_pacientes LIMIT 10"),pacientesFachada);
            listaPacientes = new LazyPacienteDataModel(pacientesFachada);
            limpiarFormulario();//limpiar formulario
            imprimirMensaje("Correcto", "El registro fue eliminado", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El paciente que se intenta eliminar tiene actividades dentro del sistema; por lo cual no puede ser eliminado.", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void uploadFirma(FileUploadEvent event) {
        try {
            archivoFirma = event.getFile();
            String nombreImg = "firmaPaciente" //es firma de usuario
                    + loginMB.getUsuarioActual().getIdUsuario() //diferenciar el usuario actual
                    + archivoFirma.getFileName().substring(archivoFirma.getFileName().lastIndexOf("."), archivoFirma.getFileName().length());//colocar extension
            if (uploadArchivo(archivoFirma, loginMB.getUrltmp() + nombreImg)) {
                urlFirma = "../imagenesOpenmedical/" + loginMB.getBaseDeDatosActual() + "/tmp/" + nombreImg;
            } else {
                urlFirma = firmaPorDefecto;
                archivoFirma = null;
            }
        } catch (Exception ex) {
            System.out.println("Error 20 en " + this.getClass().getName() + ":" + ex.toString());
        }
    }

    public void uploadFoto(FileUploadEvent event) {
        try {
            archivoFoto = event.getFile();
            String nombreImg = "fotoPaciente" //es foto de usuario
                    + loginMB.getUsuarioActual().getIdUsuario() //diferenciar el usuario actual
                    + archivoFoto.getFileName().substring(archivoFoto.getFileName().lastIndexOf("."), archivoFoto.getFileName().length());//colocar extension
            if (uploadArchivo(archivoFoto, loginMB.getUrltmp() + nombreImg)) {
                urlFoto = "../imagenesOpenmedical/" + loginMB.getBaseDeDatosActual() + "/tmp/" + nombreImg;
                fotoTomadaWebCam = false;
            } else {
                urlFoto = fotoPorDefecto;
                archivoFoto = null;
                fotoTomadaWebCam = false;
            }
        } catch (Exception ex) {
            System.out.println("Error 20 en " + this.getClass().getName() + ":" + ex.toString());
        }
    }

    public void tomarFoto(CaptureEvent captureEvent) {
        byte[] data = captureEvent.getData();
        FileImageOutputStream imageOutput;
        archivoFoto = null;
        try {
            File imagen = new File(loginMB.getUrltmp() + "fotoPaciente" + loginMB.getUsuarioActual().getIdUsuario() + ".png");
            if (imagen.exists()) {
                imagen.delete();
                imagen = new File(loginMB.getUrltmp() + "fotoPaciente" + loginMB.getUsuarioActual().getIdUsuario() + ".png");
            }
            imageOutput = new FileImageOutputStream(imagen);
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            fotoTomadaWebCam = true;
            urlFoto = "../imagenesOpenmedical/" + loginMB.getBaseDeDatosActual() + "/tmp/fotoPaciente" + loginMB.getUsuarioActual().getIdUsuario() + ".png";
        } catch (IOException e) {
            urlFoto = fotoPorDefecto;
            fotoTomadaWebCam = false;
            System.out.println("Error 02: " + e.getMessage());//imprimirMensaje("Error 02", e.getMessage(), FacesMessage.SEVERITY_ERROR);            
        }
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------
    public LazyDataModel<CfgPacientes> getListaPacientes() {
        return listaPacientes;
    }

    public void setListaPacientes(LazyDataModel<CfgPacientes> listaPacientes) {
        this.listaPacientes = listaPacientes;
    }

    public CfgPacientes getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }

    public void setPacienteSeleccionado(CfgPacientes pacienteSeleccionado) {
        this.pacienteSeleccionado = pacienteSeleccionado;
    }

    public CfgPacientes getPacienteSeleccionadoTabla() {
        return pacienteSeleccionadoTabla;
    }

    public void setPacienteSeleccionadoTabla(CfgPacientes pacienteSeleccionadoTabla) {
        this.pacienteSeleccionadoTabla = pacienteSeleccionadoTabla;
    }

    public CfgPacientes getNuevoPaciente() {
        return nuevoPaciente;
    }

    public void setNuevoPaciente(CfgPacientes nuevoPaciente) {
        this.nuevoPaciente = nuevoPaciente;
    }

    public CfgPacientesFacade getPacientesFachada() {
        return pacientesFachada;
    }

    public void setPacientesFachada(CfgPacientesFacade pacientesFachada) {
        this.pacientesFachada = pacientesFachada;
    }

    public CfgClasificacionesFacade getClasificacionesFachada() {
        return clasificacionesFachada;
    }

    public void setClasificacionesFachada(CfgClasificacionesFacade clasificacionesFachada) {
        this.clasificacionesFachada = clasificacionesFachada;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getLugarExpedicion() {
        return lugarExpedicion;
    }

    public void setLugarExpedicion(String lugarExpedicion) {
        this.lugarExpedicion = lugarExpedicion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getRegimen() {
        return regimen;
    }

    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }

    public String getEstrato() {
        return estrato;
    }

    public void setEstrato(String estrato) {
        this.estrato = estrato;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getGrupoSanguineo() {
        return grupoSanguineo;
    }

    public void setGrupoSanguineo(String grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }

    public String getEtnia() {
        return etnia;
    }

    public void setEtnia(String etnia) {
        this.etnia = etnia;
    }

    public String getEscolaridad() {
        return escolaridad;
    }

    public void setEscolaridad(String escolaridad) {
        this.escolaridad = escolaridad;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefonoResidencia() {
        return telefonoResidencia;
    }

    public void setTelefonoResidencia(String telefonoResidencia) {
        this.telefonoResidencia = telefonoResidencia;
    }

    public String getTelefonoOficina() {
        return telefonoOficina;
    }

    public void setTelefonoOficina(String telefonoOficina) {
        this.telefonoOficina = telefonoOficina;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getTipoAfiliado() {
        return tipoAfiliado;
    }

    public void setTipoAfiliado(String tipoAfiliado) {
        this.tipoAfiliado = tipoAfiliado;
    }

    public Date getFechaAfiliacion() {
        return fechaAfiliacion;
    }

    public void setFechaAfiliacion(Date fechaAfiliacion) {
        this.fechaAfiliacion = fechaAfiliacion;
    }

    public Date getFechaSisben() {
        return fechaSisben;
    }

    public void setFechaSisben(Date fechaSisben) {
        this.fechaSisben = fechaSisben;
    }

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getUrlFirma() {
        return urlFirma;
    }

    public void setUrlFirma(String urlFirma) {
        this.urlFirma = urlFirma;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public List<SelectItem> getListaMunicipios() {
        return listaMunicipios;
    }

    public void setListaMunicipios(List<SelectItem> listaMunicipios) {
        this.listaMunicipios = listaMunicipios;
    }

    public List<SelectItem> getListaServicios() {
        return listaServicios;
    }

    public void setListaServicios(List<SelectItem> listaServicios) {
        this.listaServicios = listaServicios;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public String getAdministradora() {
        return administradora;
    }

    public void setAdministradora(String administradora) {
        this.administradora = administradora;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getTelefonoResponsable() {
        return telefonoResponsable;
    }

    public void setTelefonoResponsable(String telefonoResponsable) {
        this.telefonoResponsable = telefonoResponsable;
    }

    public String getAcompanante() {
        return acompanante;
    }

    public void setAcompanante(String acompanante) {
        this.acompanante = acompanante;
    }

    public String getTelefonoAcompanante() {
        return telefonoAcompanante;
    }

    public void setTelefonoAcompanante(String telefonoAcompanante) {
        this.telefonoAcompanante = telefonoAcompanante;
    }

    public String getCategoriaPaciente() {
        return categoriaPaciente;
    }

    public void setCategoriaPaciente(String categoriaPaciente) {
        this.categoriaPaciente = categoriaPaciente;
    }

    public UploadedFile getArchivoFoto() {
        return archivoFoto;
    }

    public void setArchivoFoto(UploadedFile archivoFoto) {
        this.archivoFoto = archivoFoto;
    }

    public String getFechNacimiConvetEdad() {
        return fechNacimiConvetEdad;
    }

    public void setFechNacimiConvetEdad(String fechNacimiConvetEdad) {
        this.fechNacimiConvetEdad = fechNacimiConvetEdad;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public Date getFechaVenceCarnet() {
        return fechaVenceCarnet;
    }

    public void setFechaVenceCarnet(Date fechaVenceCarnet) {
        this.fechaVenceCarnet = fechaVenceCarnet;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getTabActivaPacientes() {
        return tabActivaPacientes;
    }

    public void setTabActivaPacientes(String tabActivaPacientes) {
        this.tabActivaPacientes = tabActivaPacientes;
    }

    public String getTituloTabPacientes() {
        return tituloTabPacientes;
    }

    public void setTituloTabPacientes(String tituloTabPacientes) {
        this.tituloTabPacientes = tituloTabPacientes;
    }

}
