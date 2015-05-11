/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.utilidades;

import java.util.Date;

/**
 *
 * @author mario
 */
public class CitaU {

    private String empresa;
    private String sede;
    private String sedeDir;
    private String sedeTel;
    private int idCita;
    private Date fecha;
    private Date hora;
    private String consultorio;
    private int idPrestador;
    private String prestadorPN;
    private String prestadorSN;
    private String prestadorPA;
    private String prestadorSA;
    private String prestadorEspecialidad;
    private int idPaciente;
    private String pacientePN;
    private String pacienteSN;
    private String pacientePA;
    private String pacienteSA;
    private String pacienteTipoDoc;
    private String pacienteNumDoc;
    private String administradora;
    private String empresaTelefono;
    private String empresaDireccion;
    private String servicio;


    private String codAdministradora;
    private String observaciones;
    private String motivoConsulta;
    private String motivoCancelacion;
    private Date fechaRegistro;
    private Date fechaCancelacion;
    private boolean atendida;
    private boolean cancelada;

    public CitaU() {
    }

    /**
     * @return the empresa
     */
    public String getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa the empresa to set
     */
    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    /**
     * @return the sede
     */
    public String getSede() {
        return sede;
    }

    /**
     * @param sede the sede to set
     */
    public void setSede(String sede) {
        this.sede = sede;
    }

    /**
     * @return the sedeDir
     */
    public String getSedeDir() {
        return sedeDir;
    }

    /**
     * @param sedeDir the sedeDir to set
     */
    public void setSedeDir(String sedeDir) {
        this.sedeDir = sedeDir;
    }

    /**
     * @return the sedeTel
     */
    public String getSedeTel() {
        return sedeTel;
    }

    /**
     * @param sedeTel the sedeTel to set
     */
    public void setSedeTel(String sedeTel) {
        this.sedeTel = sedeTel;
    }

    /**
     * @return the citaId
     */
    public int getIdCita() {
        return idCita;
    }

    /**
     * @param citaId the citaId to set
     */
    public void setIdCita(int citaId) {
        this.idCita = citaId;
    }

    /**
     * @return the prestadorPN
     */
    public String getPrestadorPN() {
        return prestadorPN;
    }

    /**
     * @param prestadorPN the prestadorPN to set
     */
    public void setPrestadorPN(String prestadorPN) {
        this.prestadorPN = prestadorPN;
    }

    /**
     * @return the prestadorSN
     */
    public String getPrestadorSN() {
        return prestadorSN;
    }

    /**
     * @param prestadorSN the prestadorSN to set
     */
    public void setPrestadorSN(String prestadorSN) {
        this.prestadorSN = prestadorSN;
    }

    /**
     * @return the prestadorPA
     */
    public String getPrestadorPA() {
        return prestadorPA;
    }

    /**
     * @param prestadorPA the prestadorPA to set
     */
    public void setPrestadorPA(String prestadorPA) {
        this.prestadorPA = prestadorPA;
    }

    /**
     * @return the prestadorSA
     */
    public String getPrestadorSA() {
        return prestadorSA;
    }

    /**
     * @param prestadorSA the prestadorSA to set
     */
    public void setPrestadorSA(String prestadorSA) {
        this.prestadorSA = prestadorSA;
    }

    /**
     * @return the pacientePN
     */
    public String getPacientePN() {
        return pacientePN;
    }

    /**
     * @param pacientePN the pacientePN to set
     */
    public void setPacientePN(String pacientePN) {
        this.pacientePN = pacientePN;
    }

    /**
     * @return the pacienteSN
     */
    public String getPacienteSN() {
        return pacienteSN;
    }

    /**
     * @param pacienteSN the pacienteSN to set
     */
    public void setPacienteSN(String pacienteSN) {
        this.pacienteSN = pacienteSN;
    }

    /**
     * @return the pacientePA
     */
    public String getPacientePA() {
        return pacientePA;
    }

    /**
     * @param pacientePA the pacientePA to set
     */
    public void setPacientePA(String pacientePA) {
        this.pacientePA = pacientePA;
    }

    /**
     * @return the pacienteSA
     */
    public String getPacienteSA() {
        return pacienteSA;
    }

    /**
     * @param pacienteSA the pacienteSA to set
     */
    public void setPacienteSA(String pacienteSA) {
        this.pacienteSA = pacienteSA;
    }

    /**
     * @return the Administradora
     */
    public String getAdministradora() {
        return administradora;
    }

    /**
     * @param Administradora the Administradora to set
     */
    public void setAdministradora(String Administradora) {
        this.administradora = Administradora;
    }

    /**
     * @return the observaciones
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * @param observaciones the observaciones to set
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the Hora
     */
    public Date getHora() {
        return hora;
    }

    /**
     * @param Hora the Hora to set
     */
    public void setHora(Date Hora) {
        this.hora = Hora;
    }

    /**
     * @return the consultorio
     */
    public String getConsultorio() {
        return consultorio;
    }

    /**
     * @param consultorio the consultorio to set
     */
    public void setConsultorio(String consultorio) {
        this.consultorio = consultorio;
    }

    /**
     * @return the prestadorEspecialidad
     */
    public String getPrestadorEspecialidad() {
        return prestadorEspecialidad;
    }

    /**
     * @param prestadorEspecialidad the prestadorEspecialidad to set
     */
    public void setPrestadorEspecialidad(String prestadorEspecialidad) {
        this.prestadorEspecialidad = prestadorEspecialidad;
    }

    /**
     * @return the pacienteTipoDoc
     */
    public String getPacienteTipoDoc() {
        return pacienteTipoDoc;
    }

    /**
     * @param pacienteTipoDoc the pacienteTipoDoc to set
     */
    public void setPacienteTipoDoc(String pacienteTipoDoc) {
        this.pacienteTipoDoc = pacienteTipoDoc;
    }

    /**
     * @return the pacienteNumDoc
     */
    public String getPacienteNumDoc() {
        return pacienteNumDoc;
    }

    /**
     * @param pacienteNumDoc the pacienteNumDoc to set
     */
    public void setPacienteNumDoc(String pacienteNumDoc) {
        this.pacienteNumDoc = pacienteNumDoc;
    }

    /**
     * @return the motivoConsulta
     */
    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    /**
     * @param motivoConsulta the motivoConsulta to set
     */
    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    /**
     * @return the motivoCancelacion
     */
    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }

    /**
     * @param motivoCancelacion the motivoCancelacion to set
     */
    public void setMotivoCancelacion(String motivoCancelacion) {
        this.motivoCancelacion = motivoCancelacion;
    }

    /**
     * @return the idPrestador
     */
    public int getIdPrestador() {
        return idPrestador;
    }

    /**
     * @param idPrestador the idPrestador to set
     */
    public void setIdPrestador(int idPrestador) {
        this.idPrestador = idPrestador;
    }

    /**
     * @return the idPaciente
     */
    public int getIdPaciente() {
        return idPaciente;
    }

    /**
     * @param idPaciente the idPaciente to set
     */
    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    /**
     * @return the codAdministradora
     */
    public String getCodAdministradora() {
        return codAdministradora;
    }

    /**
     * @param codAdministradora the codAdministradora to set
     */
    public void setCodAdministradora(String codAdministradora) {
        this.codAdministradora = codAdministradora;
    }

    public String getEmpresaDireccion() {
        return empresaDireccion;
    }

    public void setEmpresaDireccion(String empresaDireccion) {
        this.empresaDireccion = empresaDireccion;
    }

    public String getEmpresaTelefono() {
        return empresaTelefono;
    }

    public void setEmpresaTelefono(String empresaTelefono) {
        this.empresaTelefono = empresaTelefono;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(Date fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }

    public boolean isAtendida() {
        return atendida;
    }

    public void setAtendida(boolean atendida) {
        this.atendida = atendida;
    }

    public boolean isCancelada() {
        return cancelada;
    }

    public void setCancelada(boolean cancelada) {
        this.cancelada = cancelada;
    }

}
