/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.seguridad;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import modelo.entidades.CfgConfiguraciones;
import modelo.entidades.CfgCopiasSeguridad;
import modelo.fachadas.CfgConfiguracionesFacade;
import modelo.fachadas.CfgCopiasSeguridadFacade;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 * This class allows you to manage everything related to the backs of all the
 * information contained SIGEODEP also has functions that allow to have backups
 * in case of loss or any kind of error.
 *
 * @author santos
 */
@ManagedBean(name = "copiasSeguridadMB")
@SessionScoped
public class CopiasSeguridadMB {

    @EJB
    CfgConfiguracionesFacade configuracionesFacade;
    @EJB
    CfgCopiasSeguridadFacade copiasSeguridadFacade;

    private List<CfgCopiasSeguridad> listaCopiasSeguridad;
    private CfgCopiasSeguridad copiaSeguridadSeleccionada;
    private CfgConfiguraciones configuracionActual;
    private String nombreCopiaSeguridad = "";//Nombre del la copia de seguridad.    
    private AplicacionGeneralMB aplicacionGeneralMB;
    private StreamedContent fileBackup;
    private String nombreCopiaDescargar = "";

    @PostConstruct
    public void inicializar() {
        configuracionActual = configuracionesFacade.findAll().get(0);
        eliminarCopiasNoEncontradas();
        listaCopiasSeguridad = copiasSeguridadFacade.buscarOrdenado();
        copiaSeguridadSeleccionada = null;
        nombreCopiaSeguridad = "";
    }

    public CopiasSeguridadMB() {
        aplicacionGeneralMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{aplicacionGeneralMB}", AplicacionGeneralMB.class);
    }

    public void clickBtnDescargarCopia() {
        if (copiaSeguridadSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Se debe seleccionar una copia de seguridad para realizar la descarga."));
            return;
        }
        nombreCopiaDescargar = copiaSeguridadSeleccionada.getNombre();
        RequestContext.getCurrentInstance().execute("PF('dialogDownload').show();");
    }

    public StreamedContent getFileBackup() {//DESCARGA DE ARCHIVO DE COPIA DE SEGURIDAD
        InputStream input;
        try {
            File file = new File(copiaSeguridadSeleccionada.getRuta());
            input = new FileInputStream(file);
            fileBackup = new DefaultStreamedContent(input, "application/binary", file.getName());
            return fileBackup;
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR 001: " + ex.toString());
        }
        return null;
    }

    /**
     * is responsible to clear the fields for creating a new Backup.
     */
    public void reset() {
        eliminarCopiasNoEncontradas();
        listaCopiasSeguridad = copiasSeguridadFacade.buscarOrdenado();
        copiaSeguridadSeleccionada = null;
        nombreCopiaSeguridad = "";
    }

    /**
     * Delete backups that do not have the file stored in the server folder.
     */
    public void eliminarCopiasNoEncontradas() {// elimina copias que no tengan archivo almacenado en la carpeta del servidor
        ArrayList<CfgCopiasSeguridad> listaCopiasAEliminar = new ArrayList<>();
        listaCopiasSeguridad = copiasSeguridadFacade.buscarOrdenado();
        for (CfgCopiasSeguridad copia : listaCopiasSeguridad) {
            File archivoDeBackup = new File(copia.getRuta());
            if (!archivoDeBackup.exists()) {//si no existe se elimina de tabla backups
                listaCopiasAEliminar.add(copia);
            }
        }
        for (CfgCopiasSeguridad copia : listaCopiasAEliminar) {
            copiasSeguridadFacade.remove(copia);
        }
    }

    /**
     * Create the backup od (Sigeodep) also determines whether you've entered a
     * name and if it exists and stores the information to create the copy.
     */
    public void crearCopiaDeSeguridad() {// click sobre crear backup de od(sigeodep)
        boolean continueProcess;
        ResultSet rs;

        if (nombreCopiaSeguridad != null && nombreCopiaSeguridad.trim().length() != 0) {//determinar si se ingreso nombre
            continueProcess = true;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Se debe escribir un nombre para la copia de seguridad"));
            continueProcess = false;
        }

        if (continueProcess) {//determinar si el nombre ya esta ingresado                
            nombreCopiaSeguridad = nombreCopiaSeguridad + ".backup";
            if (copiasSeguridadFacade.buscarPorNombre(nombreCopiaSeguridad) != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ya existe una copia de seguridad con el nombre ingresado"));
                continueProcess = false;
            }
        }

        if (continueProcess) {//almaceno la informacion de la copia de seguridad a crear
            try {
                if (new java.io.File(configuracionActual.getRutaCopiasSeguridad()).exists()) {//verificar que el directorio exista                    
                    CfgCopiasSeguridad nuevaCopiasSeguridad = new CfgCopiasSeguridad();
                    nuevaCopiasSeguridad.setFecha(new Date());
                    nuevaCopiasSeguridad.setNombre(nombreCopiaSeguridad);
                    nuevaCopiasSeguridad.setTipo("MANUAL");
                    nuevaCopiasSeguridad.setRuta(configuracionActual.getRutaCopiasSeguridad() + nombreCopiaSeguridad);
                    copiasSeguridadFacade.create(nuevaCopiasSeguridad);
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Directorio '" + configuracionActual.getRutaCopiasSeguridad() + "' no existe en el servidor"));
                    continueProcess = false;
                }
            } catch (Exception x) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", x.getMessage()));
            }
        }
        if (continueProcess) {//genero los archivos de copia de seguridad
            try {
                Process p;
                ProcessBuilder pb;
                String backupFilePath = configuracionActual.getRutaCopiasSeguridad() + nombreCopiaSeguridad;
                File fiRcherofile = new java.io.File(backupFilePath);//si archivo od existe Lo eliminamos 
                if (fiRcherofile.exists()) {
                    fiRcherofile.delete();
                }
                //ejecutamos proceso de copia de seguridad
                pb = new ProcessBuilder(configuracionActual.getRutaBinPostgres() + "pg_dump", "-i", "-h", configuracionActual.getServidor(), "-p", "5432", "-U", configuracionActual.getUsuario(), "-F", "c", "-b", "-v", "-f", backupFilePath, configuracionActual.getNombreDb());
                pb.environment().put("PGPASSWORD", configuracionActual.getClave());
                pb.redirectErrorStream(true);
                p = pb.start();
                //imprimirSalidaDeProceso(p, " crear copia de seguridad: " + backupFilePath + "_od.backup");
                nombreCopiaSeguridad = "";
                listaCopiasSeguridad = copiasSeguridadFacade.buscarOrdenado();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "La copia de seguridad ha sido creada correctamente"));
            } catch (IOException x) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", x.getMessage()));
            }
        }
    }

    /**
     * Show console the progress of an invoked external process.
     *
     * @param p
     * @param description
     */
    private void imprimirSalidaDeProceso(Process p, String description) {// mostrar por consola el progreso de un proceso externo invocado         
        try {//CODIGO PARA MOSTRAR EL PROGESO DE LA GENERACION DEL ARCHIVO
            InputStream is = p.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String ll;
            while ((ll = br.readLine()) != null) {
                System.out.println(ll);
            }
        } catch (IOException e) {
            System.out.println("Error 99 " + e.getMessage());
        }//System.out.println("Termina proceso " + description + " /////////////////////////////////////////");
    }

    public void clickBtnRestaurarCopia() {
        if (copiaSeguridadSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Se debe seleccionar una copia de seguridad para realizar la restauración"));
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogEdit').show();");
    }

    public void restaurarCopiaDeSeguridad() {
        /*
         * click sobre restaurar una copia de seguridad de od(sigeodep)
         */
        boolean continueProcess;

        if (copiaSeguridadSeleccionada != null) {
            continueProcess = true;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Se debe seleccionar una copia de seguridad para realizar la restauración"));
            continueProcess = false;
        }
        if (continueProcess) {//valido que el archivo exista        
            if (!new java.io.File(copiaSeguridadSeleccionada.getRuta()).exists()) {//Probamos a ver si existe ese ultimo dato                    
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontro el archivo: " + copiaSeguridadSeleccionada.getRuta() + " en el servidor"));
                continueProcess = false;
            }
        }
        if (continueProcess) {//realizo la restauracion de la copia de seguridad            
            try {
                Process p;
                ProcessBuilder pb;
                pb = new ProcessBuilder(configuracionActual.getRutaBinPostgres() + "pg_restore", "-i", "-h", configuracionActual.getServidor(), "-p", "5432", "-U", configuracionActual.getUsuario(), "-d", configuracionActual.getNombreDb(), "-v", "-F", "c", "-c", copiaSeguridadSeleccionada.getRuta());
                pb.environment().put("PGPASSWORD", configuracionActual.getClave());
                pb.redirectErrorStream(true);
                p = pb.start();
                //imprimirSalidaDeProceso(p, " restauracion copia seguridad: " + copiaSeguridadSeleccionada.getRuta() + copiaSeguridadSeleccionada.getNombre());
            } catch (IOException x) {
                System.err.println("Caught: " + x.getMessage());
                continueProcess = false;
            }
        }
        if (continueProcess) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "La copia de seguridad se ha sido restaurada, por favor cierre la sesión."));
            try {//me redirijo a la pagina inicial
                aplicacionGeneralMB.cerraTodasLasSesiones();
                ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
                String ctxPath = ((ServletContext) ctx.getContext()).getContextPath();
                ((HttpSession) ctx.getSession(false)).invalidate();
                //ctx.redirect(ctxPath + "/index.html");
            } catch (Exception ex) {
                System.out.println("Excepcion cuando se cierra sesions por restauración de copia de seguridad" + ex.toString());
            }
        }
    }

    public void clickBtnEliminarCopia() {
        if (copiaSeguridadSeleccionada == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Se debe seleccionar una copia de seguridad para realizar la eliminación."));
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogDelete').show();");
    }

    public void eliminarCopiaDeSeguridad() {
        /*
         * click sobre eliminar un backup de od(sigeodep)
         */
        File backupFile;
        if (copiaSeguridadSeleccionada != null) {
            backupFile = new java.io.File(copiaSeguridadSeleccionada.getRuta());
            if (backupFile.exists()) {
                backupFile.delete();//elimino el archivo
            }
            copiasSeguridadFacade.remove(copiasSeguridadFacade.buscarPorNombre(copiaSeguridadSeleccionada.getNombre()));
            listaCopiasSeguridad = copiasSeguridadFacade.buscarOrdenado();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "La copia de seguridad se ha eliminado correctamente"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Se debe seleccionar una copia de seguridad para realizar la eliminación"));
        }
    }
    // --------------------------    
    // -- METODOS GET Y SET -----
    // --------------------------    

    public List<CfgCopiasSeguridad> getListaCopiasSeguridad() {
        return listaCopiasSeguridad;
    }

    public void setListaCopiasSeguridad(List<CfgCopiasSeguridad> listaCopiasSeguridad) {
        this.listaCopiasSeguridad = listaCopiasSeguridad;
    }

    public CfgCopiasSeguridad getCopiaSeguridadSeleccionada() {
        return copiaSeguridadSeleccionada;
    }

    public void setCopiaSeguridadSeleccionada(CfgCopiasSeguridad copiaSeguridadSeleccionada) {
        this.copiaSeguridadSeleccionada = copiaSeguridadSeleccionada;
    }

    public String getNombreCopiaSeguridad() {
        return nombreCopiaSeguridad;
    }

    public void setNombreCopiaSeguridad(String nombreCopiaSeguridad) {
        this.nombreCopiaSeguridad = nombreCopiaSeguridad;
    }

    public String getNombreCopiaDescargar() {
        return nombreCopiaDescargar;
    }

    public void setNombreCopiaDescargar(String nombreCopiaDescargar) {
        this.nombreCopiaDescargar = nombreCopiaDescargar;
    }

}
