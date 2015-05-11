package managedBeans.seguridad;

import beans.enumeradores.ClasificacionesEnum;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.CfgImagenes;
import modelo.entidades.CfgUsuarios;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgImagenesFacade;
import modelo.fachadas.CfgPerfilesUsuarioFacade;
import modelo.fachadas.CfgUsuariosFacade;
import java.io.File;
import java.io.IOException;
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
import org.primefaces.context.RequestContext;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import beans.utilidades.MetodosGenerales;
import java.math.BigDecimal;
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "usuariosMB")
@SessionScoped
public class UsuariosMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    CfgUsuariosFacade usuariosFachada;
    @EJB
    CfgPerfilesUsuarioFacade perfilesFachada;
    @EJB
    CfgClasificacionesFacade clasificacionesFachada;
    @EJB
    CfgImagenesFacade imagenesFacade;

    //---------------------------------------------------
    //-----------------ENTIDADES ------------------------
    //---------------------------------------------------    
    CfgUsuarios usuarioSeleccionado;
    CfgUsuarios usuarioSeleccionadoTabla;

    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------
    private boolean nuevoRegistro = true;
    private UploadedFile archivoFirma;
    private UploadedFile archivoFoto;
    private final LoginMB loginMB;
    private final String firmaPorDefecto = "../recursos/img/firma.png";
    private final String fotoPorDefecto = "../recursos/img/img_usuario.png";
    private String urlFirma = firmaPorDefecto;
    private String urlFoto = fotoPorDefecto;
    private String identificacion = "";
    private String tipoIdentificacion = "";
    private String clave = "";//50  
    private String confirmacionClave = "";//50 
    private String login = "";//
    private String genero = "1";//
    private String observacion = "";
    private String primerApellido = "";
    private String segundoApellido = "";
    private String primerNombre = "";
    private String segundoNombre = "";
    private String telefonoResidencia = "";//50
    private String telefonoOficina = "";//50
    private String celular = "";//50
    private String email = "";  //100
    private String direccion = "";//150
    private String tipoUsuario = "";
    private String departamento = "";
    private String municipio = "";
    private String cargoActual = "";//100
    private String registroProfesional = "";
    private String especialidad = "";
    private String personalAtiende = "";
    private String unidadFuncional = "";
    private String tituloTabUsuarios = "Datos nuevo usuario";
    private double porcentajeHonorario = 0;
    private List<SelectItem> listaMunicipios;
    private boolean cuentaActiva = true;
    private boolean usuarioVisible = true;
    private boolean mostrarEnHistorias = true;
    private boolean fotoTomadaWebCam = false;//saber si la foto se tomo de la webcam
    private Date fechaCreacion = new Date();
    private String perfilAcceso;
    private String mostrarSeccionPrestador = "none";
    private String nombreImagenEnTmp;//como quedara almacenado en la carpeta de pemporales
    private String extension;//que estension tiene
    private String nombreImagenReal;//nombre con que se cargo el archivo

    //---------------------------------------------------
    //----------------- FUNCIONES -----------------------
    //---------------------------------------------------    
    @PostConstruct
    public void inicializar() {
        nuevoRegistro = true;
        usuarioSeleccionado = null;
    }

    public UsuariosMB() {
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

    public void cambiaTipoUsuario() {
        if (tipoUsuario != null && tipoUsuario.equals("1774")) {//es prestador
            mostrarSeccionPrestador = "block";
        } else {
            mostrarSeccionPrestador = "none";//no es prestador
        }
    }

    public void limpiarFormulario() {
        tituloTabUsuarios = "Datos nuevo usuario";
        archivoFirma = null;
        archivoFoto = null;
        fotoTomadaWebCam = false;
        urlFirma = firmaPorDefecto;
        urlFoto = fotoPorDefecto;
        tipoIdentificacion = "";
        identificacion = "";
        primerApellido = "";
        segundoApellido = "";
        primerNombre = "";
        segundoNombre = "";
        genero = "";
        departamento = "";
        municipio = "";
        cargarMunicipios();
        direccion = "";
        telefonoResidencia = "";
        telefonoOficina = "";
        celular = "";
        email = "";
        tipoUsuario = "";
        registroProfesional = "";
        especialidad = "";
        personalAtiende = "";
        unidadFuncional = "";
        porcentajeHonorario = 0;
        login = "";
        clave = "";
        confirmacionClave = "";
        perfilAcceso = "";
        cuentaActiva = true;
        cargoActual = "";
        observacion = "";
        usuarioVisible = true;
        mostrarEnHistorias=true;
        fechaCreacion = new Date();
    }

    public int cargarUsuario() {
        limpiarFormulario();
        if (usuarioSeleccionadoTabla == null) {
            imprimirMensaje("Error", "Se debe seleccionar un usuario de la tabla", FacesMessage.SEVERITY_ERROR);
            return 0;
        }
        usuarioSeleccionado = usuariosFachada.find(usuarioSeleccionadoTabla.getIdUsuario());
        tituloTabUsuarios = "Datos usuario: " + usuarioSeleccionado.nombreCompleto();
        archivoFirma = null;
        archivoFoto = null;
        fotoTomadaWebCam = false;
        nuevoRegistro = false;
        if (usuarioSeleccionado.getFirma() != null) {
            urlFirma = "../imagenesOpenmedical/" + usuarioSeleccionado.getFirma().getUrlImagen();
        }
        if (usuarioSeleccionado.getFoto() != null) {
            urlFoto = "../imagenesOpenmedical/" + usuarioSeleccionado.getFoto().getUrlImagen();
        }
        identificacion = usuarioSeleccionado.getIdentificacion();
        if (usuarioSeleccionado.getTipoIdentificacion() != null) {
            tipoIdentificacion = usuarioSeleccionado.getTipoIdentificacion().getId().toString();
        }
        primerApellido = usuarioSeleccionado.getPrimerApellido();
        segundoApellido = usuarioSeleccionado.getSegundoApellido();
        primerNombre = usuarioSeleccionado.getPrimerNombre();
        segundoNombre = usuarioSeleccionado.getSegundoNombre();
        if (usuarioSeleccionado.getGenero() != null) {
            genero = usuarioSeleccionado.getGenero().getId().toString();
        }
        if (usuarioSeleccionado.getDepartamento() != null) {
            departamento = usuarioSeleccionado.getDepartamento().getId().toString();
            cargarMunicipios();
            municipio = usuarioSeleccionado.getMunicipio().getId().toString();
        }
        direccion = usuarioSeleccionado.getDireccion();
        telefonoOficina = usuarioSeleccionado.getTelefonoOficina();
        telefonoResidencia = usuarioSeleccionado.getTelefonoResidencia();
        celular = usuarioSeleccionado.getCelular();
        email = usuarioSeleccionado.getEmail();
        if (usuarioSeleccionado.getTipoUsuario() != null) {
            tipoUsuario = usuarioSeleccionado.getTipoUsuario().getId().toString();
        }
        registroProfesional = usuarioSeleccionado.getRegistroProfesional();
        if (usuarioSeleccionado.getEspecialidad() != null) {
            especialidad = usuarioSeleccionado.getEspecialidad().getId().toString();
        }

        if (usuarioSeleccionado.getPersonalAtiende() != null) {
            personalAtiende = usuarioSeleccionado.getPersonalAtiende().getId().toString();
        }
        unidadFuncional = usuarioSeleccionado.getUnidadFuncional();
        if (usuarioSeleccionado.getPorcentajeHonorario() != null) {
            porcentajeHonorario = usuarioSeleccionado.getPorcentajeHonorario();
        }
        login = usuarioSeleccionado.getLoginUsuario();
        clave = "";
        confirmacionClave = "";
        if (usuarioSeleccionado.getIdPerfil() != null) {
            perfilAcceso = usuarioSeleccionado.getIdPerfil().getIdPerfil().toString();
        }
        cuentaActiva = usuarioSeleccionado.getEstadoCuenta();
        cargoActual = usuarioSeleccionado.getCargoActual();
        observacion = usuarioSeleccionado.getObservacion();
        usuarioVisible = usuarioSeleccionado.getVisible();
        mostrarEnHistorias=usuarioSeleccionado.getMostrarEnHistorias();
        fechaCreacion = usuarioSeleccionado.getFechaCreacion();
        cambiaTipoUsuario();
        return 0;
    }

    public void guardarUsuario() {
        CfgUsuarios usuarioTmp;
        //VALIDACION DE DATOS OBLIGATORIOS
        if (validacionCampoVacio(identificacion, "Identificación")) {
            return;
        }
        if (validacionCampoVacio(tipoIdentificacion, "Tipo de identificación")) {
            return;
        }
        if (validacionCampoVacio(login, "login")) {
            return;
        }
        //VALIDACION DE VALORES UNICOS
        if (nuevoRegistro) {//guardando nuevo usuario            
            if (validacionCampoVacio(clave, "Clave")) {
                return;
            }
            if (validacionCampoVacio(confirmacionClave, "Confirmación clave")) {
                return;
            }
            if (clave.compareTo(confirmacionClave) != 0) {
                imprimirMensaje("Error", "Clave y Confirmación clave no son iguales", FacesMessage.SEVERITY_ERROR);
                return;
            }
            if (usuariosFachada.buscarPorIdentificacion(identificacion) != null) {
                imprimirMensaje("Error", "Ya existe un usuario con esta identificación", FacesMessage.SEVERITY_ERROR);
                return;
            }
            if (usuariosFachada.buscarPorLogin(login) != null) {
                imprimirMensaje("Error", "Ya existe un usuario con este Login", FacesMessage.SEVERITY_ERROR);
                return;
            }
            guardarNuevoUsuario();
        } else {//modificando usuario existente
            if (clave.length() != 0 || confirmacionClave.length() != 0) {
                if (clave.compareTo(confirmacionClave) != 0) {
                    imprimirMensaje("Error", "Clave y Confirmación clave no son iguales", FacesMessage.SEVERITY_ERROR);
                    return;
                }
            }
            usuarioTmp = usuariosFachada.buscarPorIdentificacion(identificacion);
            if (usuarioTmp != null && !usuarioSeleccionado.getIdentificacion().equals(usuarioTmp.getIdentificacion())) {
                imprimirMensaje("Error", "Existe un usuario diferente con esta identificación", FacesMessage.SEVERITY_ERROR);
                return;
            }
            usuarioTmp = usuariosFachada.buscarPorLogin(login);
            if (usuarioTmp != null && !usuarioSeleccionado.getLoginUsuario().equals(usuarioTmp.getLoginUsuario())) {
                imprimirMensaje("Error", "Existe un usuario diferente con el mismo login", FacesMessage.SEVERITY_ERROR);
                return;
            }
            actualizarUsuarioExistente();
        }
    }

    private void guardarNuevoUsuario() {
        CfgUsuarios nuevoUsuario = new CfgUsuarios();

        if (archivoFirma != null) {//se cargo firma         
            nombreImagenReal = archivoFirma.getFileName();
            extension = nombreImagenReal.substring(nombreImagenReal.lastIndexOf("."), nombreImagenReal.length());
            CfgImagenes nuevaImagen = new CfgImagenes();
            imagenesFacade.create(nuevaImagen);//crearlo para que me autogenere el ID            
            nombreImagenEnTmp = "firmaUsuario" + loginMB.getUsuarioActual().getIdUsuario() + extension;
            moverArchivo(loginMB.getUrltmp() + nombreImagenEnTmp, loginMB.getUrlFirmas() + nuevaImagen.getId().toString() + extension);
            nuevaImagen.setNombre(nombreImagenReal);
            nuevaImagen.setNombreEnServidor(nuevaImagen.getId().toString() + extension);
            nuevaImagen.setUrlImagen(loginMB.getBaseDeDatosActual() + "/firmas/" + nuevaImagen.getId().toString() + extension);
            imagenesFacade.edit(nuevaImagen);
            nuevoUsuario.setFirma(nuevaImagen);
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
            nombreImagenEnTmp = "fotoUsuario" + loginMB.getUsuarioActual().getIdUsuario() + extension;
            moverArchivo(loginMB.getUrltmp() + nombreImagenEnTmp, loginMB.getUrlFotos() + nuevaImagen.getId().toString() + extension);
            nuevaImagen.setNombre(nombreImagenReal);
            nuevaImagen.setNombreEnServidor(nuevaImagen.getId().toString() + extension);
            nuevaImagen.setUrlImagen(loginMB.getBaseDeDatosActual() + "/fotos/" + nuevaImagen.getId().toString() + extension);
            imagenesFacade.edit(nuevaImagen);
            nuevoUsuario.setFoto(nuevaImagen);
        }
        nuevoUsuario.setTipoIdentificacion(clasificacionesFachada.find(Integer.parseInt(tipoIdentificacion)));
        nuevoUsuario.setIdentificacion(identificacion);
        nuevoUsuario.setPrimerApellido(primerApellido);
        nuevoUsuario.setSegundoApellido(segundoApellido);
        nuevoUsuario.setPrimerNombre(primerNombre);
        nuevoUsuario.setSegundoNombre(segundoNombre);
        nuevoUsuario.setGenero(clasificacionesFachada.find(Integer.parseInt(genero)));
        if (departamento != null && departamento.length() != 0) {
            nuevoUsuario.setDepartamento(clasificacionesFachada.find(Integer.parseInt(departamento)));
            nuevoUsuario.setMunicipio(clasificacionesFachada.find(Integer.parseInt(municipio)));
        }
        nuevoUsuario.setDireccion(direccion);
        nuevoUsuario.setTelefonoOficina(telefonoOficina);
        nuevoUsuario.setTelefonoResidencia(telefonoResidencia);
        nuevoUsuario.setCelular(celular);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setTipoUsuario(clasificacionesFachada.find(Integer.parseInt(tipoUsuario)));
        nuevoUsuario.setRegistroProfesional(registroProfesional);
        if (especialidad != null && especialidad.length() != 0) {
            nuevoUsuario.setEspecialidad(clasificacionesFachada.find(Integer.parseInt(especialidad)));
        }
        if (personalAtiende != null && personalAtiende.length() != 0) {
            nuevoUsuario.setPersonalAtiende(clasificacionesFachada.find(Integer.parseInt(personalAtiende)));
        }
        nuevoUsuario.setUnidadFuncional(unidadFuncional);
        nuevoUsuario.setPorcentajeHonorario(porcentajeHonorario);
        nuevoUsuario.setLoginUsuario(login);
        nuevoUsuario.setClave(clave);
        if (perfilAcceso != null && perfilAcceso.length() != 0) {
            nuevoUsuario.setIdPerfil(perfilesFachada.find(Integer.parseInt(perfilAcceso)));
        }
        nuevoUsuario.setEstadoCuenta(cuentaActiva);
        nuevoUsuario.setCargoActual(cargoActual);
        nuevoUsuario.setObservacion(observacion);
        nuevoUsuario.setVisible(usuarioVisible);
        nuevoUsuario.setMostrarEnHistorias(mostrarEnHistorias);
        nuevoUsuario.setFechaCreacion(fechaCreacion);
        usuariosFachada.create(nuevoUsuario);
        imprimirMensaje("Correcto", "Nuevo usuario creado correctamente", FacesMessage.SEVERITY_INFO);
        loginMB.getAplicacionGeneralMB().cargarClasificacion(ClasificacionesEnum.Usuarios);
        loginMB.getAplicacionGeneralMB().cargarClasificacion(ClasificacionesEnum.Prestadores);
        usuarioSeleccionado = null;
        nuevoRegistro = true;
        limpiarFormulario();//limpiar formulario
    }

    private void actualizarUsuarioExistente() {
        if (archivoFirma != null) {//se cargo imagen
            nombreImagenReal = archivoFirma.getFileName();
            extension = nombreImagenReal.substring(nombreImagenReal.lastIndexOf("."), nombreImagenReal.length());
            nombreImagenEnTmp = "firmaUsuario" + loginMB.getUsuarioActual().getIdUsuario() + extension;
            if (usuarioSeleccionado.getFirma() != null) {//existe firma
                moverArchivo(loginMB.getUrltmp() + nombreImagenEnTmp, loginMB.getUrlFirmas() + usuarioSeleccionado.getFirma().getId() + extension);
            } else {//no existe firma
                CfgImagenes nuevaImagen = new CfgImagenes();
                imagenesFacade.create(nuevaImagen);//crearlo para que me autogenere el ID            
                nombreImagenEnTmp = "firmaUsuario" + loginMB.getUsuarioActual().getIdUsuario() + extension;
                moverArchivo(loginMB.getUrltmp() + nombreImagenEnTmp, loginMB.getUrlFirmas() + nuevaImagen.getId().toString() + extension);
                nuevaImagen.setNombre(nombreImagenReal);
                nuevaImagen.setNombreEnServidor(nuevaImagen.getId().toString() + extension);
                nuevaImagen.setUrlImagen(loginMB.getBaseDeDatosActual() + "/firmas/" + nuevaImagen.getId().toString() + extension);
                imagenesFacade.edit(nuevaImagen);
                usuarioSeleccionado.setFirma(nuevaImagen);
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
            //nombreImagenReal = sacarNombre(archivoFoto);
            //extension = "." + nombreImagenReal.substring(nombreImagenReal.lastIndexOf("."), nombreImagenReal.length());
            nombreImagenEnTmp = "fotoUsuario" + loginMB.getUsuarioActual().getIdUsuario() + extension;
            if (usuarioSeleccionado.getFoto() != null) {//existe foto
                moverArchivo(loginMB.getUrltmp() + nombreImagenEnTmp, loginMB.getUrlFotos() + usuarioSeleccionado.getFoto().getId() + extension);
                usuarioSeleccionado.getFoto().setNombreEnServidor(usuarioSeleccionado.getFoto().getId() + extension);
                usuarioSeleccionado.getFoto().setUrlImagen(loginMB.getBaseDeDatosActual() + "/fotos/" + usuarioSeleccionado.getFoto().getId() + extension);
                imagenesFacade.edit(usuarioSeleccionado.getFoto());
            } else {//no existe foto
                CfgImagenes nuevaImagen = new CfgImagenes();
                imagenesFacade.create(nuevaImagen);//crearlo para que me autogenere el ID            
                nombreImagenEnTmp = "fotoUsuario" + loginMB.getUsuarioActual().getIdUsuario() + extension;
                moverArchivo(loginMB.getUrltmp() + nombreImagenEnTmp, loginMB.getUrlFotos() + nuevaImagen.getId().toString() + extension);
                nuevaImagen.setNombre(nombreImagenReal);
                nuevaImagen.setNombreEnServidor(nuevaImagen.getId().toString() + ".png");
                nuevaImagen.setUrlImagen(loginMB.getBaseDeDatosActual() + "/fotos/" + nuevaImagen.getId().toString() + extension);
                imagenesFacade.edit(nuevaImagen);
                usuarioSeleccionado.setFoto(nuevaImagen);
            }
        }
        usuarioSeleccionado.setTipoIdentificacion(clasificacionesFachada.find(Integer.parseInt(tipoIdentificacion)));
        if (clave.length() != 0) {//SI SE DIGITO CLAVE SE REGISTRA SINO SE DEJA LA ANTIGUA
            usuarioSeleccionado.setClave(clave);
        }
        usuarioSeleccionado.setIdentificacion(identificacion);
        usuarioSeleccionado.setPrimerApellido(primerApellido);
        usuarioSeleccionado.setSegundoApellido(segundoApellido);
        usuarioSeleccionado.setPrimerNombre(primerNombre);
        usuarioSeleccionado.setSegundoNombre(segundoNombre);
        usuarioSeleccionado.setGenero(clasificacionesFachada.find(Integer.parseInt(genero)));
        if (departamento != null && departamento.length() != 0) {
            usuarioSeleccionado.setDepartamento(clasificacionesFachada.find(Integer.parseInt(departamento)));
            usuarioSeleccionado.setMunicipio(clasificacionesFachada.find(Integer.parseInt(municipio)));
        }
        usuarioSeleccionado.setDireccion(direccion);
        usuarioSeleccionado.setTelefonoResidencia(telefonoResidencia);
        usuarioSeleccionado.setTelefonoOficina(telefonoOficina);
        usuarioSeleccionado.setCelular(celular);
        usuarioSeleccionado.setEmail(email);
        usuarioSeleccionado.setTipoUsuario(clasificacionesFachada.find(Integer.parseInt(tipoUsuario)));
        usuarioSeleccionado.setRegistroProfesional(registroProfesional);
        if (especialidad != null && especialidad.length() != 0) {
            usuarioSeleccionado.setEspecialidad(clasificacionesFachada.find(Integer.parseInt(especialidad)));
        }
        if (personalAtiende != null && personalAtiende.length() != 0) {
            usuarioSeleccionado.setPersonalAtiende(clasificacionesFachada.find(Integer.parseInt(personalAtiende)));
        }
        usuarioSeleccionado.setUnidadFuncional(unidadFuncional);
        usuarioSeleccionado.setPorcentajeHonorario(porcentajeHonorario);
        usuarioSeleccionado.setLoginUsuario(login);
        if (perfilAcceso != null && perfilAcceso.length() != 0) {
            usuarioSeleccionado.setIdPerfil(perfilesFachada.find(Integer.parseInt(perfilAcceso)));
        }
        usuarioSeleccionado.setEstadoCuenta(cuentaActiva);
        usuarioSeleccionado.setCargoActual(cargoActual);
        usuarioSeleccionado.setObservacion(observacion);
        usuarioSeleccionado.setVisible(usuarioVisible);
        usuarioSeleccionado.setMostrarEnHistorias(mostrarEnHistorias);
        usuarioSeleccionado.setFechaCreacion(fechaCreacion);
        usuariosFachada.edit(usuarioSeleccionado);
        imprimirMensaje("Correcto", "Usuario actualizado correctamente", FacesMessage.SEVERITY_INFO);
        loginMB.getAplicacionGeneralMB().cargarClasificacion(ClasificacionesEnum.Usuarios);
        loginMB.getAplicacionGeneralMB().cargarClasificacion(ClasificacionesEnum.Prestadores);
        usuarioSeleccionado = null;
        nuevoRegistro = true;
        limpiarFormulario();
    }

    public void eliminarUsuario() {
        if (usuarioSeleccionado == null) {
            imprimirMensaje("Error", "No se ha cargado un usuario", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            usuariosFachada.remove(usuarioSeleccionado);
            usuarioSeleccionado = null;
            loginMB.getAplicacionGeneralMB().cargarClasificacion(ClasificacionesEnum.Usuarios);
            loginMB.getAplicacionGeneralMB().cargarClasificacion(ClasificacionesEnum.Prestadores);
            usuarioSeleccionado = null;
            nuevoRegistro = true;
            limpiarFormulario();//limpiar formulario
            imprimirMensaje("Correcto", "El registro fue eliminado", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El usuario que se intenta eliminar tiene actividades dentro del sistema; por lo cual no puede ser eliminado.", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void uploadFirma(FileUploadEvent event) {
        try {
            archivoFirma = event.getFile();
            String nombreImg = "firmaUsuario" //es firma de usuario
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
            String nombreImg = "fotoUsuario" //es foto de usuario
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
            File imagen = new File(loginMB.getUrltmp() + "fotoUsuario" + loginMB.getUsuarioActual().getIdUsuario() + ".png");
            if (imagen.exists()) {
                imagen.delete();
                imagen = new File(loginMB.getUrltmp() + "fotoUsuario" + loginMB.getUsuarioActual().getIdUsuario() + ".png");
            }
            imageOutput = new FileImageOutputStream(imagen);
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            fotoTomadaWebCam = true;
            urlFoto = "../imagenesOpenmedical/" + loginMB.getBaseDeDatosActual() + "/tmp/fotoUsuario" + loginMB.getUsuarioActual().getIdUsuario() + ".png";
            RequestContext.getCurrentInstance().update("IdFormFoto");
        } catch (IOException e) {
            urlFoto = fotoPorDefecto;
            fotoTomadaWebCam = false;
            System.out.println("Error 02: " + e.getMessage());//imprimirMensaje("Error 02", e.getMessage(), FacesMessage.SEVERITY_ERROR);            
        }
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------
    public CfgUsuarios getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(CfgUsuarios usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public CfgUsuarios getUsuarioSeleccionadoTabla() {
        return usuarioSeleccionadoTabla;
    }

    public void setUsuarioSeleccionadoTabla(CfgUsuarios usuarioSeleccionadoTabla) {
        this.usuarioSeleccionadoTabla = usuarioSeleccionadoTabla;
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

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isCuentaActiva() {
        return cuentaActiva;
    }

    public void setCuentaActiva(boolean cuentaActiva) {
        this.cuentaActiva = cuentaActiva;
    }

    public String getPerfilAcceso() {
        return perfilAcceso;
    }

    public void setPerfilAcceso(String perfilAcceso) {
        this.perfilAcceso = perfilAcceso;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getCargoActual() {
        return cargoActual;
    }

    public void setCargoActual(String cargoActual) {
        this.cargoActual = cargoActual;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fecha_creacion) {
        this.fechaCreacion = fecha_creacion;
    }

    public String getConfirmacionClave() {
        return confirmacionClave;
    }

    public void setConfirmacionClave(String confirmacionClave) {
        this.confirmacionClave = confirmacionClave;
    }

    public List<SelectItem> getListaMunicipios() {
        return listaMunicipios;
    }

    public void setListaMunicipios(List<SelectItem> listaMunicipios) {
        this.listaMunicipios = listaMunicipios;
    }

//    public String getTituloFormulario() {
//        return tituloFormulario;
//    }
//
//    public void setTituloFormulario(String tituloFormulario) {
//        this.tituloFormulario = tituloFormulario;
//    }
    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getMostrarSeccionPrestador() {
        return mostrarSeccionPrestador;
    }

    public void setMostrarSeccionPrestador(String mostrarSeccionPrestador) {
        this.mostrarSeccionPrestador = mostrarSeccionPrestador;
    }

    public String getRegistroProfesional() {
        return registroProfesional;
    }

    public void setRegistroProfesional(String registroProfesional) {
        this.registroProfesional = registroProfesional;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getPersonalAtiende() {
        return personalAtiende;
    }

    public void setPersonalAtiende(String personalAtiende) {
        this.personalAtiende = personalAtiende;
    }

    public String getUnidadFuncional() {
        return unidadFuncional;
    }

    public void setUnidadFuncional(String unidadFuncional) {
        this.unidadFuncional = unidadFuncional;
    }

    public double getPorcentajeHonorario() {
        return porcentajeHonorario;
    }

    public void setPorcentajeHonorario(double porcentajeHonorario) {
        this.porcentajeHonorario = porcentajeHonorario;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public boolean isUsuarioVisible() {
        return usuarioVisible;
    }

    public void setUsuarioVisible(boolean usuarioVisible) {
        this.usuarioVisible = usuarioVisible;
    }

    public boolean isMostrarEnHistorias() {
        return mostrarEnHistorias;
    }

    public void setMostrarEnHistorias(boolean mostrarEnHistorias) {
        this.mostrarEnHistorias = mostrarEnHistorias;
    }    

    public String getTituloTabUsuarios() {
        return tituloTabUsuarios;
    }

    public void setTituloTabUsuarios(String tituloTabUsuarios) {
        this.tituloTabUsuarios = tituloTabUsuarios;
    }

}
