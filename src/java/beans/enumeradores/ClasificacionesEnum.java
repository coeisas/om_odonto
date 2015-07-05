/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.enumeradores;

/**
 * This class handles enumerators related with variables
 *
 * @author SANTOS
 */
public enum ClasificacionesEnum {
    //---------------------------------------------
    //categorias contenidas en cfg_clasificaciones
    //---------------------------------------------
    ActoQuirurgico,//Acto quirurgico correspondiente a un servicio"
    Ambito,//Ambito de realización del procedimiento"
    CategoriaPaciente,//Ingresos en SMLMV
    CausaExterna,//Identificador de la causa externa que origina el servicio de salud"
    ClasificacionEvento,//clasificacion evento(formulario referencia)
    DPTO,//Departamentos
    Escolaridad,//Nivel de escolaridad
    Especialidad,//Especialidades de médicos
    EstadoCivil,//Tipo de estado Civil
    Estrato,//Estrato
    Etnia,//Etnia    
    //Finalidad,//Finalidad del registro clinico"
    FinalidadConsulta,//Finalidad de una consulta"
    FinalidadProcedimiento,//Finalidad de un servicio"
    FormaDePago,//Forma de Pago
    Genero,//Masculino, Femenino
    Genero3,//Ambos, Masculino, Femenino
    GrupoSanguineo,//Grupo Sanguíneo
    GrupoServicio,//Grupo de servicio
    Icono,//Iconos de la aplicación
    MotiCancCitas,//Motivo cancelacion citas
    MotivoConsulta,//Mtivo de consulta
    Municipios,//Municipios
    Ocupacion,//Ocupaciones
    Parentesco,//Parentezco familiar
    PersonalAtiende,//Identifica el tipo de prestador"
    Regimen,//Tipos de régimenes
    TipoAdministradora,//Tipos de administradoras
    TipoAfiliado,//Tipos de afiliados
    TipoConsulta,//Tipos de consultas
    TipoConsumo,//Tipo de consumo: Insumo, Medicamento, Servicio
    //TipoContrato,//Tipo de contrato
    TipoDiagnosticoConsulta,//Tipos de diagnosticos en consultas
    TipoEdad,//Tipo de edad
    TipoFacturacion,//Tipo de facturacion en contratos
    TipoIdentificacion,//Tipos de identificacion
    TipoPago,//Tipo de Pago en contratos
    TipoTarifa,//Tipo de servicio
    TipoUsuario,//Tipos de usuarios,prestador,administrador..
    Zona,//Zonas urbana,rural,

    //-----------------------------------------------------
    //no contenidas en cfg_clasificaciones (son tablas independientes)
    //-----------------------------------------------------
    PerfilesUsuario, //                    permisos de un determinado usuario
    Administradoras,
    Prestadores, //                        solo prestadores    
    Usuarios, //                          usuarios y prestadores
    TipoRegistroClinico,//                  formatos de historas de la entidad
    Insumos,
    Meses,
    Medicamentos,
    Paquetes,
    Servicios,
    Sedes, //                           sedes que tiene la empresa
    NOVALUE;

    public static ClasificacionesEnum convert(String str) {
        try {
            return valueOf(str);
        } catch (IllegalArgumentException ex) {
            return NOVALUE;
        }
    }
}
