/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import modelo.entidades.FacAdministradora;
import modelo.entidades.FacContrato;
import modelo.entidades.FacManualTarifario;
import modelo.entidades.FacPrograma;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgDiagnosticoFacade;
import modelo.fachadas.FacAdministradoraFacade;
import modelo.fachadas.FacContratoFacade;
import modelo.fachadas.FacManualTarifarioFacade;
import modelo.fachadas.FacProgramaFacade;
import org.primefaces.context.RequestContext;

/**
 *
 * @author santos
 */
@ManagedBean(name = "programasMB")
@SessionScoped
public class ProgramasMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    FacProgramaFacade programaFacade;
    @EJB
    FacContratoFacade contratoFacade;
    @EJB
    FacAdministradoraFacade administradoraFacade;
    @EJB
    CfgClasificacionesFacade clasificacionesFachada;
    @EJB
    CfgDiagnosticoFacade diagnosticoFacade;
    @EJB
    FacManualTarifarioFacade manualTarifarioFacade;
    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------

    private FacPrograma programaSeleccionado;
    private FacPrograma programaSeleccionadoTabla;
    private List<FacPrograma> listaProgramas;
    private List<FacContrato> listaContratos;
    private List<FacManualTarifario> listaManuales;

    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------
    private boolean cargandoDesdeTab = false;
    private String tituloTabPrograma = "Nuevo Programa";
    private String codigoPrograma = "";
    private String idContrato = "";
    private String idManualTarifario = "";
    private String idAdministradora = "";
    private String nombrePrograma = "";
    //private String sexo = "";
    //private int edadInicial = 0;
    //private String unidadEdadInical = "";
    //private int edadFinal = 0;
    //private String unidadEdadFinal = "";
    //private String zona = "";
    private boolean activo = true;
    private double cm1 = 0;
    private double cm2 = 0;
    private double cm3 = 0;
    private double cm4 = 0;
    private double cm5 = 0;
    private boolean cmc = false;
    private boolean cmb = false;
    private double cp1 = 0;
    private double cp2 = 0;
    private double cp3 = 0;
    private double cp4 = 0;
    private double cp5 = 0;
    private boolean cpc = false;
    private boolean cpb = false;
    private double medicamentoValor1 = 0;
    private double medicamentoValor2 = 0;
    private double medicamentoValor3 = 0;
    private double insumosPorcentaje1 = 0;
    private double insumosPorcentaje2 = 0;
    private double insumosPorcentaje3 = 0;
    private String finalidadConsulta = "";
    private String causaExterna = "";
    private String finalidadProcedimineto = "";
    private String codigoDiagnostico = "";

    //---------------------------------------------------
    //------------- FUNCIONES INICIALES  ----------------
    //---------------------------------------------------      
    public ProgramasMB() {
    }

    @PostConstruct
    public void inicializar() {
        limpiarFormularioProgramas();//carge listas
    }

    //---------------------------------------------------
    //-----------------FUNCIONES PROGRAMAS --------
    //--------------------------------------------------- 
    public void limpiarFormularioProgramas() {
        //listaProgramas = programaFacade.buscarOrdenado();
        programaSeleccionado=null;
        listaManuales = manualTarifarioFacade.buscarOrdenado();
        listaContratos = new ArrayList<>();
        tituloTabPrograma = "Nuevo Programa";
        codigoPrograma = "";
        idContrato = "";
        idManualTarifario = "";
        idAdministradora = "";
        nombrePrograma = "";
        activo = true;
        cm1 = 0;
        cm2 = 0;
        cm3 = 0;
        cm4 = 0;
        cm5 = 0;
        cmc = false;
        cmb = false;
        cp1 = 0;
        cp2 = 0;
        cp3 = 0;
        cp4 = 0;
        cp5 = 0;
        cpc = false;
        cpb = false;
        medicamentoValor1 = 0;
        medicamentoValor2 = 0;
        medicamentoValor3 = 0;
        insumosPorcentaje1 = 0;
        insumosPorcentaje2 = 0;
        insumosPorcentaje3 = 0;
        finalidadConsulta = "";
        causaExterna = "";
        finalidadProcedimineto = "";
        codigoDiagnostico = "";
    }

    public void buscarProgramas() {
        listaProgramas = programaFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().execute("PF('wvTablaProgramas').clearFilters(); PF('wvTablaProgramas').getPaginator().setPage(0); PF('dialogoBuscarProgramas').show();");
    }

    public void cargarDesdeTab(String id) {//cargar tab programas desde el tab externo(contrato)
        cargandoDesdeTab = true;
        String[] splitId = id.split(";");
        if (splitId[0].compareTo("idContrato") == 0) {//Nuevo programa por que se recibe idContrato            
            limpiarFormularioProgramas();
            idAdministradora = contratoFacade.find(Integer.parseInt(splitId[1])).getIdAdministradora().getIdAdministradora().toString();
            cambiaAdministradora();
            idContrato = contratoFacade.find(Integer.parseInt(splitId[1])).getIdContrato().toString();
        } else {//editar programa existene por que se recibe IdPrograma
            programaSeleccionadoTabla = programaFacade.find(Integer.parseInt(splitId[1]));
            cargarPrograma();
        }
        RequestContext.getCurrentInstance().update("IdFormProgramas");
        cargandoDesdeTab = false;
    }

    public void cargarPrograma() {//click cobre editar caja (carga los datos de la adminstradora)
        if (programaSeleccionadoTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún programa de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        limpiarFormularioProgramas();
        programaSeleccionado = programaFacade.find(programaSeleccionadoTabla.getIdPrograma());
        codigoPrograma = programaSeleccionado.getCodigoPrograma();
        if (programaSeleccionado.getIdContrato() != null) {
            idContrato = programaSeleccionado.getIdContrato().getIdContrato().toString();
            idAdministradora = programaSeleccionado.getIdContrato().getIdAdministradora().getIdAdministradora().toString();
            listaContratos = programaSeleccionado.getIdContrato().getIdAdministradora().getFacContratoList();
        }
        if (programaSeleccionado.getIdManualTarifario() != null) {
            idManualTarifario = programaSeleccionado.getIdManualTarifario().getIdManualTarifario().toString();
        }

        nombrePrograma = programaSeleccionado.getNombrePrograma();
        activo = programaSeleccionado.getActivo();
        cm1 = programaSeleccionado.getCm1();
        cm2 = programaSeleccionado.getCm2();
        cm3 = programaSeleccionado.getCm3();
        cm4 = programaSeleccionado.getCm4();
        cm5 = programaSeleccionado.getCm5();
        cmc = programaSeleccionado.getCmc();
        cmb = programaSeleccionado.getCmb();
        cp1 = programaSeleccionado.getCp1();
        cp2 = programaSeleccionado.getCp2();
        cp3 = programaSeleccionado.getCp3();
        cp4 = programaSeleccionado.getCp4();
        cp5 = programaSeleccionado.getCp5();
        cpc = programaSeleccionado.getCpc();
        cpb = programaSeleccionado.getCpb();
        medicamentoValor1 = programaSeleccionado.getMedicamentoValor1();
        medicamentoValor2 = programaSeleccionado.getMedicamentoValor2();
        medicamentoValor3 = programaSeleccionado.getMedicamentoValor3();
        insumosPorcentaje1 = programaSeleccionado.getInsumosPorcentaje1();
        insumosPorcentaje2 = programaSeleccionado.getInsumosPorcentaje2();
        insumosPorcentaje3 = programaSeleccionado.getInsumosPorcentaje3();
        if (programaSeleccionado.getFinalidadConsulta() != null) {
            finalidadConsulta = programaSeleccionado.getFinalidadConsulta().getId().toString();
        }
        if (programaSeleccionado.getCausaExterna() != null) {
            causaExterna = programaSeleccionado.getCausaExterna().getId().toString();
        }
        if (programaSeleccionado.getFinalidadProcedimineto() != null) {
            finalidadProcedimineto = programaSeleccionado.getFinalidadProcedimineto().getId().toString();
        }
        if (programaSeleccionado.getCodigoDiagnostico() != null) {
            codigoDiagnostico = programaSeleccionado.getCodigoDiagnostico().getCodigoDiagnostico();
        }
        tituloTabPrograma = "Datos Programa: " + nombrePrograma;
        if (!cargandoDesdeTab) {//si se esta cargando desde tab esta funcion no aplica (provocaria error javaScript)
            RequestContext.getCurrentInstance().execute("PF('dialogoBuscarProgramas').hide(); PF('wvTablaProgramas').clearFilters();");
        }
    }

    public void eliminarPrograma() {
        if (programaSeleccionado == null) {
            imprimirMensaje("Error", "No se ha cargado ningún programa", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoEliminarPrograma').show();");
    }

    public void confirmarEliminarPrograma() {
        if (programaSeleccionado == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún programa", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            programaFacade.remove(programaSeleccionado);
            //listaProgramas = programaFacade.buscarOrdenado();
            limpiarFormularioProgramas();
            RequestContext.getCurrentInstance().update("IdFormProgramas");
            imprimirMensaje("Correcto", "El programa ha sido eliminado", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El programa que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void guardarPrograma() {
        if (validacionCampoVacio(codigoPrograma, "Código programa")) {
            return;
        }
        if (validacionCampoVacio(nombrePrograma, "Nombre programa")) {
            return;
        }
        if (validacionCampoVacio(idAdministradora, "Administradora")) {
            return;
        }
        if (validacionCampoVacio(idContrato, "Contrato")) {
            return;
        }
        if (validacionCampoVacio(idManualTarifario, "Manual Tarifario")) {
            return;
        }
        if (programaSeleccionado == null) {
            guardarNuevoPrograma();
        } else {
            actualizarProgramaExistente();
        }
    }

    private void guardarNuevoPrograma() {
        FacPrograma nuevoPrograma = new FacPrograma();
        nuevoPrograma.setCodigoPrograma(codigoPrograma);
        if (validarNoVacio(idContrato)) {
            nuevoPrograma.setIdContrato(contratoFacade.find(Integer.parseInt(idContrato)));
        }
        if (validarNoVacio(idManualTarifario)) {
            nuevoPrograma.setIdManualTarifario(manualTarifarioFacade.find(Integer.parseInt(idManualTarifario)));
        }
        nuevoPrograma.setNombrePrograma(nombrePrograma);

        nuevoPrograma.setActivo(activo);
        nuevoPrograma.setCm1(cm1);
        nuevoPrograma.setCm2(cm2);
        nuevoPrograma.setCm3(cm3);
        nuevoPrograma.setCm4(cm4);
        nuevoPrograma.setCm5(cm5);
        nuevoPrograma.setCmc(cmc);
        nuevoPrograma.setCmb(cmb);
        nuevoPrograma.setCp1(cp1);
        nuevoPrograma.setCp2(cp2);
        nuevoPrograma.setCp3(cp3);
        nuevoPrograma.setCp4(cp4);
        nuevoPrograma.setCp5(cp5);
        nuevoPrograma.setCpc(cpc);
        nuevoPrograma.setCpb(cpb);
        nuevoPrograma.setMedicamentoValor1(medicamentoValor1);
        nuevoPrograma.setMedicamentoValor2(medicamentoValor2);
        nuevoPrograma.setMedicamentoValor3(medicamentoValor3);
        nuevoPrograma.setInsumosPorcentaje1(insumosPorcentaje1);
        nuevoPrograma.setInsumosPorcentaje2(insumosPorcentaje2);
        nuevoPrograma.setInsumosPorcentaje3(insumosPorcentaje3);
        if (validarNoVacio(finalidadConsulta)) {
            nuevoPrograma.setFinalidadConsulta(clasificacionesFachada.find(Integer.parseInt(finalidadConsulta)));
        }
        if (validarNoVacio(causaExterna)) {
            nuevoPrograma.setCausaExterna(clasificacionesFachada.find(Integer.parseInt(causaExterna)));
        }
        if (validarNoVacio(finalidadProcedimineto)) {
            nuevoPrograma.setFinalidadProcedimineto(clasificacionesFachada.find(Integer.parseInt(finalidadProcedimineto)));
        }
        if (validarNoVacio(codigoDiagnostico)) {
            nuevoPrograma.setCodigoDiagnostico(diagnosticoFacade.find(codigoDiagnostico));
        }
        programaFacade.create(nuevoPrograma);
        limpiarFormularioProgramas();
        //listaProgramas = programaFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().update("IdFormProgramas");
        imprimirMensaje("Correcto", "El programa ha sido creado.", FacesMessage.SEVERITY_INFO);
    }

    private void actualizarProgramaExistente() {//realiza la actualizacion del consultorio        
        programaSeleccionado.setCodigoPrograma(codigoPrograma);
        if (validarNoVacio(idContrato)) {
            programaSeleccionado.setIdContrato(contratoFacade.find(Integer.parseInt(idContrato)));
        }
        if (validarNoVacio(idManualTarifario)) {
            programaSeleccionado.setIdManualTarifario(manualTarifarioFacade.find(Integer.parseInt(idManualTarifario)));
        }
        programaSeleccionado.setNombrePrograma(nombrePrograma);
        programaSeleccionado.setActivo(activo);
        programaSeleccionado.setCm1(cm1);
        programaSeleccionado.setCm2(cm2);
        programaSeleccionado.setCm3(cm3);
        programaSeleccionado.setCm4(cm4);
        programaSeleccionado.setCm5(cm5);
        programaSeleccionado.setCmc(cmc);
        programaSeleccionado.setCmb(cmb);
        programaSeleccionado.setCp1(cp1);
        programaSeleccionado.setCp2(cp2);
        programaSeleccionado.setCp3(cp3);
        programaSeleccionado.setCp4(cp4);
        programaSeleccionado.setCp5(cp5);
        programaSeleccionado.setCpc(cpc);
        programaSeleccionado.setCpb(cpb);
        programaSeleccionado.setMedicamentoValor1(medicamentoValor1);
        programaSeleccionado.setMedicamentoValor2(medicamentoValor2);
        programaSeleccionado.setMedicamentoValor3(medicamentoValor3);
        programaSeleccionado.setInsumosPorcentaje1(insumosPorcentaje1);
        programaSeleccionado.setInsumosPorcentaje2(insumosPorcentaje2);
        programaSeleccionado.setInsumosPorcentaje3(insumosPorcentaje3);
        if (validarNoVacio(finalidadConsulta)) {
            programaSeleccionado.setFinalidadConsulta(clasificacionesFachada.find(Integer.parseInt(finalidadConsulta)));
        }
        if (validarNoVacio(causaExterna)) {
            programaSeleccionado.setCausaExterna(clasificacionesFachada.find(Integer.parseInt(causaExterna)));
        }
        if (validarNoVacio(finalidadProcedimineto)) {
            programaSeleccionado.setFinalidadProcedimineto(clasificacionesFachada.find(Integer.parseInt(finalidadProcedimineto)));
        }
        if (validarNoVacio(codigoDiagnostico)) {
            programaSeleccionado.setCodigoDiagnostico(diagnosticoFacade.find(codigoDiagnostico));
        }
        programaFacade.edit(programaSeleccionado);
        limpiarFormularioProgramas();
        //listaProgramas = programaFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().update("IdFormProgramas");
        imprimirMensaje("Correcto", "El programa ha sido actualizado.", FacesMessage.SEVERITY_INFO);
    }

    public void cambiaAdministradora() {//cambia la administradora, se carga el combo de contrato
        if (validarNoVacio(idAdministradora)) {
            FacAdministradora administradoraSeleccionada = administradoraFacade.find(Integer.parseInt(idAdministradora));
            if (administradoraSeleccionada != null) {
                listaContratos = administradoraSeleccionada.getFacContratoList();
            } else {
                listaContratos = new ArrayList<>();
            }
        } else {
            listaContratos = new ArrayList<>();
        }
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------
    public String getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(String idContrato) {
        this.idContrato = idContrato;
    }

    public String getIdManualTarifario() {
        return idManualTarifario;
    }

    public void setIdManualTarifario(String idManualTarifario) {
        this.idManualTarifario = idManualTarifario;
    }

    public String getIdAdministradora() {
        return idAdministradora;
    }

    public void setIdAdministradora(String idAdministradora) {
        this.idAdministradora = idAdministradora;
    }

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public double getCm1() {
        return cm1;
    }

    public void setCm1(double cm1) {
        this.cm1 = cm1;
    }

    public double getCm2() {
        return cm2;
    }

    public void setCm2(double cm2) {
        this.cm2 = cm2;
    }

    public double getCm3() {
        return cm3;
    }

    public void setCm3(double cm3) {
        this.cm3 = cm3;
    }

    public double getCm4() {
        return cm4;
    }

    public void setCm4(double cm4) {
        this.cm4 = cm4;
    }

    public double getCm5() {
        return cm5;
    }

    public void setCm5(double cm5) {
        this.cm5 = cm5;
    }

    public double getCp1() {
        return cp1;
    }

    public void setCp1(double cp1) {
        this.cp1 = cp1;
    }

    public double getCp2() {
        return cp2;
    }

    public void setCp2(double cp2) {
        this.cp2 = cp2;
    }

    public double getCp3() {
        return cp3;
    }

    public void setCp3(double cp3) {
        this.cp3 = cp3;
    }

    public double getCp4() {
        return cp4;
    }

    public void setCp4(double cp4) {
        this.cp4 = cp4;
    }

    public double getCp5() {
        return cp5;
    }

    public void setCp5(double cp5) {
        this.cp5 = cp5;
    }

    public boolean isCpc() {
        return cpc;
    }

    public void setCpc(boolean cpc) {
        this.cpc = cpc;
    }

    public boolean isCpb() {
        return cpb;
    }

    public void setCpb(boolean cpb) {
        this.cpb = cpb;
    }

    public FacPrograma getProgramaSeleccionado() {
        return programaSeleccionado;
    }

    public void setProgramaSeleccionado(FacPrograma programaSeleccionado) {
        this.programaSeleccionado = programaSeleccionado;
    }

    public FacPrograma getProgramaSeleccionadoTabla() {
        return programaSeleccionadoTabla;
    }

    public void setProgramaSeleccionadoTabla(FacPrograma programaSeleccionadoTabla) {
        this.programaSeleccionadoTabla = programaSeleccionadoTabla;
    }

    public List<FacPrograma> getListaProgramas() {
        return listaProgramas;
    }

    public void setListaProgramas(List<FacPrograma> listaProgramas) {
        this.listaProgramas = listaProgramas;
    }

    public String getTituloTabPrograma() {
        return tituloTabPrograma;
    }

    public void setTituloTabPrograma(String tituloTabPrograma) {
        this.tituloTabPrograma = tituloTabPrograma;
    }

    public String getCodigoPrograma() {
        return codigoPrograma;
    }

    public void setCodigoPrograma(String codigoPrograma) {
        this.codigoPrograma = codigoPrograma;
    }

    public double getMedicamentoValor1() {
        return medicamentoValor1;
    }

    public void setMedicamentoValor1(double medicamentoValor1) {
        this.medicamentoValor1 = medicamentoValor1;
    }

    public double getMedicamentoValor2() {
        return medicamentoValor2;
    }

    public void setMedicamentoValor2(double medicamentoValor2) {
        this.medicamentoValor2 = medicamentoValor2;
    }

    public double getMedicamentoValor3() {
        return medicamentoValor3;
    }

    public void setMedicamentoValor3(double medicamentoValor3) {
        this.medicamentoValor3 = medicamentoValor3;
    }

    public double getInsumosPorcentaje1() {
        return insumosPorcentaje1;
    }

    public void setInsumosPorcentaje1(double insumosPorcentaje1) {
        this.insumosPorcentaje1 = insumosPorcentaje1;
    }

    public double getInsumosPorcentaje2() {
        return insumosPorcentaje2;
    }

    public void setInsumosPorcentaje2(double insumosPorcentaje2) {
        this.insumosPorcentaje2 = insumosPorcentaje2;
    }

    public double getInsumosPorcentaje3() {
        return insumosPorcentaje3;
    }

    public void setInsumosPorcentaje3(double insumosPorcentaje3) {
        this.insumosPorcentaje3 = insumosPorcentaje3;
    }

    public String getFinalidadConsulta() {
        return finalidadConsulta;
    }

    public void setFinalidadConsulta(String finalidadConsulta) {
        this.finalidadConsulta = finalidadConsulta;
    }

    public String getFinalidadProcedimineto() {
        return finalidadProcedimineto;
    }

    public void setFinalidadProcedimineto(String finalidadProcedimineto) {
        this.finalidadProcedimineto = finalidadProcedimineto;
    }

    public String getCodigoDiagnostico() {
        return codigoDiagnostico;
    }

    public void setCodigoDiagnostico(String codigoDiagnostico) {
        this.codigoDiagnostico = codigoDiagnostico;
    }

    public boolean isCmc() {
        return cmc;
    }

    public void setCmc(boolean cmc) {
        this.cmc = cmc;
    }

    public boolean isCmb() {
        return cmb;
    }

    public void setCmb(boolean cmb) {
        this.cmb = cmb;
    }

    public String getCausaExterna() {
        return causaExterna;
    }

    public void setCausaExterna(String causaExterna) {
        this.causaExterna = causaExterna;
    }

    public List<FacContrato> getListaContratos() {
        return listaContratos;
    }

    public void setListaContratos(List<FacContrato> listaContratos) {
        this.listaContratos = listaContratos;
    }

    public List<FacManualTarifario> getListaManuales() {
        return listaManuales;
    }

    public void setListaManuales(List<FacManualTarifario> listaManuales) {
        this.listaManuales = listaManuales;
    }

}
