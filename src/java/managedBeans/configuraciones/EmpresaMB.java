package managedBeans.configuraciones;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import beans.utilidades.MetodosGenerales;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.CfgEmpresa;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgEmpresaFacade;
import modelo.fachadas.CfgImagenesFacade;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "empresaMB")
@SessionScoped
public class EmpresaMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------    
    @EJB
    CfgClasificacionesFacade clasificacionesFacade;
    @EJB
    CfgEmpresaFacade empresaFacade;
    @EJB
    CfgImagenesFacade imagenesFacade;

    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------
    private CfgEmpresa empresa;
    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------
    private List<SelectItem> listaMunicipios;
    private String departamento = "";
    private String municipio = "";
    private String tipoDocumento = "";
    private String numIdentificacion = "";
    private String dv = "";
    private String razonSocial = "";
    private String codigoMinisterio = "";
    private String regimen = "";
    private String tipoDocRepLegal = "";
    private String numDocRepLegal = "";
    private String nomRepLegal = "";
    private String direccion = "";
    private String telefono1 = "";
    private String telefono2 = "";
    private String website = "";
    private String urlLogo = "";
    private final String urlLogoPorDefecto = "../recursos/img/logoDefecto.png";
    private String observaciones = "";
    private UploadedFile archivoLogo;
    private LoginMB loginMB;

    //---------------------------------------------------
    //------------- FUNCIONES EMPRESA--------------------
    //---------------------------------------------------      
    
    public EmpresaMB() {
        urlLogo = urlLogoPorDefecto;
    }

    private void limpiarFormularioEmpresa() {
        listaMunicipios = new ArrayList<>();
        departamento = "";
        municipio = "";
        tipoDocumento = "";
        numIdentificacion = "";
        dv = "";
        razonSocial = "";
        codigoMinisterio = "";
        regimen = "";
        tipoDocRepLegal = "";
        numDocRepLegal = "";
        nomRepLegal = "";
        direccion = "";
        telefono1 = "";
        telefono2 = "";
        website = "";
        urlLogo = "";
        observaciones = "";
        archivoLogo = null;
    }

    public void cargarDatosEmpresa() {
        limpiarFormularioEmpresa();
        empresa = empresaFacade.find(1);//primer y unica empresa
        razonSocial = empresa.getRazonSocial();
        codigoMinisterio = empresa.getCodigoEmpresa();
        regimen = empresa.getRegimen();
        direccion = empresa.getDireccion();
        telefono1 = empresa.getTelefono1();
        telefono2 = empresa.getTelefono2();
        if (empresa.getTipoDoc() != null) {
            tipoDocumento = empresa.getTipoDoc().getId().toString();
        }
        numIdentificacion = empresa.getNumIdentificacion();
        dv = empresa.getDv();
        if (empresa.getTipoDocRepLegal() != null) {
            tipoDocRepLegal = empresa.getTipoDocRepLegal().getId().toString();
        }
        numDocRepLegal = empresa.getNumDocRepLegal();
        nomRepLegal = empresa.getNomRepLegal();
        if (empresa.getCodDepartamento() != null) {
            departamento = empresa.getCodDepartamento().getId().toString();
            cargarMunicipios();
            municipio = empresa.getCodMunicipio().getId().toString();
        }
        website = empresa.getWebsite();
        observaciones = empresa.getObservaciones();
        if (empresa.getLogo() != null) {            
            urlLogo = "../imagenesOpenmedical/" + empresa.getLogo().getUrlImagen();
        } else {
            urlLogo = urlLogoPorDefecto;
        }
    }

    public void guardarEmpresa() {
        if (validacionCampoVacio(razonSocial, "Razón social")) {
            return;
        }
        if (validacionCampoVacio(codigoMinisterio, "Codigo Ministerio")) {
            return;
        }

        if (archivoLogo != null) {//se cargo imagen
            String nombreImagenEnTmp;
            String extension;
            String nombreImagenReal;

            nombreImagenReal = archivoLogo.getFileName();
            extension = nombreImagenReal.substring(nombreImagenReal.lastIndexOf("."), nombreImagenReal.length());
            nombreImagenEnTmp = "logo" + extension;
            if (empresa.getLogo() != null) {//garantizar manualmente primer logo
                moverArchivo(loginMB.getUrltmp() + nombreImagenEnTmp, loginMB.getRutaCarpetaImagenes() + loginMB.getBaseDeDatosActual() + "/logo" + extension);
                empresa.getLogo().setUrlImagen(loginMB.getBaseDeDatosActual() + "/logo" + extension);
                imagenesFacade.edit(empresa.getLogo());
                urlLogo = "../imagenesOpenmedical/" + loginMB.getBaseDeDatosActual() + "/" + nombreImagenEnTmp;
            }
        }
        empresa.setRazonSocial(razonSocial);
        empresa.setCodigoEmpresa(codigoMinisterio);
        empresa.setRegimen(regimen);
        empresa.setDireccion(direccion);
        empresa.setTelefono1(telefono1);
        empresa.setTelefono2(telefono2);
        if (tipoDocumento != null && tipoDocumento.length() != 0) {
            empresa.setTipoDoc(clasificacionesFacade.find(Integer.parseInt(tipoDocumento)));
        }
        empresa.setNumIdentificacion(numIdentificacion);
        empresa.setDv(dv);
        if (tipoDocRepLegal != null && tipoDocRepLegal.length() != 0) {
            empresa.setTipoDocRepLegal(clasificacionesFacade.find(Integer.parseInt(tipoDocRepLegal)));
        }
        empresa.setNumDocRepLegal(numDocRepLegal);
        empresa.setNomRepLegal(nomRepLegal);
        if (departamento != null && departamento.length() != 0) {
            empresa.setCodDepartamento(clasificacionesFacade.find(Integer.parseInt(departamento)));
            empresa.setCodMunicipio(clasificacionesFacade.find(Integer.parseInt(municipio)));
        }
        empresa.setWebsite(website);
        empresa.setObservaciones(observaciones);
        empresaFacade.edit(empresa);
        imprimirMensaje("Correcto", "Información de la empresa actualizada.", FacesMessage.SEVERITY_INFO);
    }

    public void cargarMunicipios() {
        listaMunicipios = new ArrayList<>();
        if (departamento != null && departamento.length() != 0) {
            List<CfgClasificaciones> listaM = clasificacionesFacade.buscarMunicipioPorDepartamento(clasificacionesFacade.find(Integer.parseInt(departamento)).getCodigo());
            for (CfgClasificaciones mun : listaM) {
                listaMunicipios.add(new SelectItem(mun.getId(), mun.getDescripcion()));
            }
        }
    }

    public void uploadFoto(FileUploadEvent event) {
        try {
            archivoLogo = event.getFile();
            String nombreImg = "logo" //es foto de usuario
                    + archivoLogo.getFileName().substring(archivoLogo.getFileName().lastIndexOf("."), archivoLogo.getFileName().length());//colocar extension
            if (uploadArchivo(archivoLogo, loginMB.getUrltmp() + nombreImg)) {
                urlLogo = "../imagenesOpenmedical/" + loginMB.getBaseDeDatosActual() + "/tmp/" + nombreImg;
            } else {
                urlLogo = urlLogoPorDefecto;
                archivoLogo = null;
            }
        } catch (Exception ex) {
            System.out.println("Error 20 en " + this.getClass().getName() + ":" + ex.toString());
        }
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------    
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

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumIdentificacion() {
        return numIdentificacion;
    }

    public void setNumIdentificacion(String numIdentificacion) {
        this.numIdentificacion = numIdentificacion;
    }

    public String getDv() {
        return dv;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public String getCodigoMinisterio() {
        return codigoMinisterio;
    }

    public void setCodigoMinisterio(String codigoMinisterio) {
        this.codigoMinisterio = codigoMinisterio;
    }

    public String getRegimen() {
        return regimen;
    }

    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getTipoDocRepLegal() {
        return tipoDocRepLegal;
    }

    public void setTipoDocRepLegal(String tipoDocRepLegal) {
        this.tipoDocRepLegal = tipoDocRepLegal;
    }

    public String getNumDocRepLegal() {
        return numDocRepLegal;
    }

    public void setNumDocRepLegal(String numDocRepLegal) {
        this.numDocRepLegal = numDocRepLegal;
    }

    public String getNomRepLegal() {
        return nomRepLegal;
    }

    public void setNomRepLegal(String nomRepLegal) {
        this.nomRepLegal = nomRepLegal;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LoginMB getLoginMB() {
        return loginMB;
    }

    public void setLoginMB(LoginMB loginMB) {
        this.loginMB = loginMB;
    }

    public List<SelectItem> getListaMunicipios() {
        return listaMunicipios;
    }

    public void setListaMunicipios(List<SelectItem> listaMunicipios) {
        this.listaMunicipios = listaMunicipios;
    }
}
