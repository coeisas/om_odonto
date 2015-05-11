package managedBeans.migracion;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import beans.utilidades.MetodosGenerales;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PreDestroy;

@ManagedBean(name = "migracionMB")
@SessionScoped
public class MigracionMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------    
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------
    Connection conn;
    private String password = "1234";
    private String server = "localhost";
    private String url = "";
    private String user = "postgres";
    private Statement st;
    private ResultSet rs;
    ArrayList<String> arrayHcConsulta = new ArrayList<>();
    ArrayList<String> arrayHcPsicologica = new ArrayList<>();//se le resta -67 para igualar
    ArrayList<String> listaTagsValidos = new ArrayList<>();//tags validos

    //---------------------------------------------------
    //----------------- FUNCIONES -----------------------
    //---------------------------------------------------    
    public MigracionMB() {
        //ACTIVAR ESTE CODIGO SI SE REUQIERE MIGRAR(Y SABER BIEN LO QUE SE REQUIERE)
        try {
            url = "jdbc:postgresql://localhost:5432/openmedical";
            conn = DriverManager.getConnection(url, user, password);//conectarse a bodega de datos                
            if (conn != null && !conn.isClosed()) {
                System.out.println(" Conexión a base de datos Correcta.");
            } else {
                System.out.println("Conexión a base de datos Fallida.");
            }
        } catch (Exception e) {
            System.out.println("Conexión a base de datos Fallida.");
        }
    }

    public void realizarMigracion() {
        //correccionHTML();
        //migracion_inical();
        //estandarizar_CIE_codigo_nombre();       
        //correccionAntecedentesPrenatales();
    }

    private int buscarEnTagValidos(String tagEncontrado) {
        //retorna -1 si se encuentra en el arreglo
        //retorna -100 si no esta ni starwhith
        //retorna >= 0 si empieza por: starwhith
        for (String tagValido : listaTagsValidos) {
            if (tagEncontrado.compareTo(tagValido) == 0) {
                return -1;
            }
        }
        for (int i = 0; i < listaTagsValidos.size(); i++) {
            if (tagEncontrado.startsWith(listaTagsValidos.get(i).substring(0, listaTagsValidos.get(i).length() - 1))) {
                return i;
            }
        }
        return -100;
    }

    private void cargarTagsValidos() {
        listaTagsValidos = new ArrayList<>();
        listaTagsValidos.add("<strong>");
        listaTagsValidos.add("</strong>");
        listaTagsValidos.add("<p>");
        listaTagsValidos.add("</p>");
        listaTagsValidos.add("<b>");
        listaTagsValidos.add("</b>");
        listaTagsValidos.add("<ol>");
        listaTagsValidos.add("</ol>");
        listaTagsValidos.add("<li>");
        listaTagsValidos.add("</li>");
        listaTagsValidos.add("<div>");
        listaTagsValidos.add("</div>");
        listaTagsValidos.add("<br>");
        listaTagsValidos.add("<div style=\"text-align: justify;\">");
    }

    private void correccionHTML() {
        cargarTagsValidos();
        try {
            //ResultSet rsA=consult("");
            ResultSet rsA = consult("select * from hc_detalle where id_campo in(select id_campo from hc_campos_reg where tipo like 'text')");            
            int contador = 0;
            String valorActual;
            String valorFinal;
            String tagActual = "";
            String caso = "esperandoMenorQue";
            ArrayList<String> listaTags;//almacenara todos los tags que tenga valor

            while (rsA.next()) {
                valorActual = rsA.getString("valor");
                valorFinal = valorActual;
                listaTags = new ArrayList<>();

                for (int i = 0; i < valorActual.length(); i++) {
                    switch (caso) {
                        case "esperandoMenorQue":
                            if (valorActual.charAt(i) == '<') {
                                tagActual = "<";
                                caso = "esperandoMayorQue";
                            }
                            break;
                        case "esperandoMayorQue":
                            if (valorActual.charAt(i) == '>') {
                                tagActual = tagActual + ">";
                                listaTags.add(tagActual);
                                caso = "esperandoMenorQue";
                            } else {
                                tagActual = tagActual + valorActual.charAt(i);
                            }
                            break;
                    }
                }
                //--------------------------------
                int posicion = 0;
                if (!listaTags.isEmpty()) {
                    for (String tagEncontrado : listaTags) {
                        posicion = buscarEnTagValidos(tagEncontrado);
                        switch (posicion) {
                            case -1://es tag valido
                                break;
                            case -100://no es tag valido
                                if (tagEncontrado.startsWith("</")) {
                                    valorFinal = valorFinal.replace(tagEncontrado, "</div>");
                                } else {
                                    valorFinal = valorFinal.replace(tagEncontrado, "<div style=\"text-align: justify;\">");
                                }
                                break;
                            default://es tag que empieza por
                                valorFinal = valorFinal.replace(tagEncontrado, listaTagsValidos.get(posicion));
                                break;
                        }
                    }
                    //System.out.println("------------------------------------------------");
                    //System.out.println("Actual: " + valorActual);
                    //System.out.println("Final: " + valorFinal);
                    //System.out.println("------------------------------------------------");
                    non_query("UPDATE hc_detalle set valor = '" + valorFinal.replace("'", "") + "' WHERE id_registro=" + rsA.getString("id_registro") + " AND id_campo=" + rsA.getString("id_campo"));
                }
                contador++;
                if ((contador % 1000) == 0) {
                    System.out.println(contador);
                }
            }
            System.out.println("FINALIZO");
        } catch (Exception e) {
            System.err.println("ERROR AL PROCESAR: " + e.getCause().toString());
            System.err.println("ERROR AL PROCESAR: " + e.getMessage());
        }

    }

    private void estandarizar_CIE_codigo_nombre() {
        //---------------------------------------------------------------------------------------------------------
        //----------------- ESTANDARIZAR DIAGNOSTISCOS A: (CODIGO - NOMBRE) ---------------------------------------
        //---------------------------------------------------------------------------------------------------------
        try {
            ResultSet rsA = consult("select * from hc_detalle where id_campo in (33,34,35,36,44,59,60,61,62,107,108,109,110)");
            ResultSet rsB;
            int contador = 0;
            while (rsA.next()) {

                rsB = consult("select * from cfg_diagnostico where codigo_diagnostico like '" + rsA.getString("valor") + "'");
                if (rsB.next()) {//buscamos si el codigo esta disponible
                    non_query("UPDATE hc_detalle set valor='" + rsB.getString("codigo_diagnostico") + " - " + rsB.getString("nombre_diagnostico") + "' where id_registro=" + rsA.getString("id_registro") + " and id_campo=" + rsA.getString("id_campo"));
                } else {
                    rsB = consult("select * from cfg_diagnostico where nombre_diagnostico like '" + rsA.getString("valor") + "'");
                    if (rsB.next()) {//bucamos si el nombre esta disponible        
                        non_query("UPDATE hc_detalle set valor='" + rsB.getString("codigo_diagnostico") + " - " + rsB.getString("nombre_diagnostico") + "' where id_registro=" + rsA.getString("id_registro") + " and id_campo=" + rsA.getString("id_campo"));
                    }
                    //llegado a este punto se deja como estaba
                }
                System.out.println(contador++);
            }
            System.err.println("FINALIZO");
        } catch (Exception e) {
            System.err.println("ERROR AL PROCESAR: " + e.getCause().toString());
            System.err.println("ERROR AL PROCESAR: " + e.getMessage());
        }
    }

    private void migracion_inical() {

        //teniendo el listado inicio con la migracion
        Calendar c = Calendar.getInstance();

        Date fechSis = new Date();
        fechSis.setDate(31);
        fechSis.setMonth(11);
        fechSis.setYear(c.get(Calendar.YEAR) - 1901);
        //SimpleDateFormat sdfFechaHora = new SimpleDateFormat("yyyy-dd-MM HH:mm");
        //System.out.println(sdfFechaHora.format(initialDate));
        //return;

        try {
            String numHistoria = "";
            String tipoRegistroClinico = "";
            String medico = "";
            int contadorHcDetalle = 0;
            int contadorHcRegistro = 0;
            int idPaciente = 0;
            SimpleDateFormat sdfFechaHora2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            SimpleDateFormat sdfFechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String codCabecera;
            ResultSet rs1, rs2, rs0;
            System.out.println("INICIA EL PROCESAMIENTO");
            //non_query("DELETE FROM hc_detalle");
            //non_query("DELETE FROM hc_registro");
            rs0 = consult("SELECT * FROM migracion_reg_cli order by cod_cabecera");
            codCabecera = "-1";
            Date fechaRegistro;
            boolean continuar = false;
            System.out.println("PRIMER RESULT SET CARGADO");
            while (rs0.next()) {
                if (codCabecera.compareTo(rs0.getString("cod_cabecera")) != 0) {
                    numHistoria = rs0.getString("num_historia");//DETERMINO NUMERO HISTORIA  
                    codCabecera = rs0.getString("cod_cabecera");//DETERMINO NUMUMERO DE REGISTRO                    
                    medico = rs0.getString("nom_medico");//BUSCO EL MEDICO QUE CORRESPONDIENTE A ESTA HISTORIA
                    tipoRegistroClinico = rs0.getString("nom_formato");//DETERMINO TIPO DE REGISTRO CLINICO
                    fechaRegistro = sdfFechaHora2.parse(rs0.getString("fecha"));//09/06/2013 15:18:21
                    //  "06/25/2014 14:40:00" MF
                    //  "2013-11-28 14:00:00" OM
                    rs1 = consult(" SELECT * FROM cfg_pacientes WHERE identificacion like '" + numHistoria + "'");
                    if (rs1.next()) {//BUSCO EL PACIENTE QUE CORRESPONDIENTE A ESTA HISTORIA
                        idPaciente = rs1.getInt("id_paciente");
                        contadorHcRegistro++;
                        if (contadorHcRegistro == 79964) {
                            contadorHcRegistro = 83285;
                        }
                        continuar = true;
                        //REGISTRO EL DATO EN LA TABLA hc_registro
                        non_query("insert into hc_registro values ("
                                + contadorHcRegistro + ","
                                + tipoRegistroClinico + ","
                                + idPaciente + ","
                                + medico + ","
                                + "'" + sdfFechaHora.format(fechaRegistro) + "',"
                                + "'" + sdfFechaHora.format(fechSis) + "'"
                                + ")");
//                        + "to_date('" + sdfFechaHora2.format(fechaRegistro) + "','MM/dd/yyyy HH24:MI:ss'),"
//                                + "to_date('" + sdfFechaHora2.format(fechaRegistro) + "','MM/dd/yyyy HH24:MI:ss')"
//                                + ")");
                    } else {
                        System.out.println("NO SE ENCONTRO PACIENTE PARA LA HISTORIA" + numHistoria);
                        return;
                    }
                }
                if (continuar) {
                    rs1 = consult(" SELECT * FROM migracion_reg_cli WHERE cod_cabecera like '" + codCabecera + "'");
                    while (rs1.next()) {
                        insert("hc_detalle", "id_registro,id_campo,valor", contadorHcRegistro + "," + rs1.getString("cod_campo") + ",'" + rs1.getString("texto").replaceAll("'", "") + "'");
                        contadorHcDetalle++;
                        if ((contadorHcDetalle % 1000) == 0) {
                            System.out.println("Registros hc_registro: " + contadorHcRegistro + " Registros hc_detalle: " + contadorHcDetalle);
                        }
                    }
                    continuar = false;
                }
            }
            System.out.println("MIGRACION FINALIZADA CORRECTAMENTE: " + contadorHcRegistro + " Registros hc_detalle: " + contadorHcDetalle);
        } catch (SQLException e) {
            System.err.println("ERROR 1 AL PROCESAR: " + e.getCause().toString());
            System.err.println("ERROR 1 AL PROCESAR: " + e.getMessage());
        } catch (ParseException ex) {
            System.err.println("ERROR 2 AL PROCESAR: " + ex.getCause().toString());
            System.err.println("ERROR 2 AL PROCESAR: " + ex.getMessage());
        }

    }

    private void cargarArrays() {

        arrayHcConsulta.add("num_historia");
        arrayHcConsulta.add("tipo_consulta");
        arrayHcConsulta.add("finalidad");
        arrayHcConsulta.add("mot_consulta");
        arrayHcConsulta.add("num_autorizacion");
        arrayHcConsulta.add("colegio");
        arrayHcConsulta.add("escolaridad");
        arrayHcConsulta.add("nombre_padre");
        arrayHcConsulta.add("nombre_madre");
        arrayHcConsulta.add("acudiente");
        arrayHcConsulta.add("lateridad");
        arrayHcConsulta.add("motivo_consulta");
        arrayHcConsulta.add("enf_actual");
        arrayHcConsulta.add("his_ant_fliares");
        arrayHcConsulta.add("des_prenatal");
        arrayHcConsulta.add("des_pos_motor");
        arrayHcConsulta.add("des_pos_lenguaje");
        arrayHcConsulta.add("esc_ini_adap");
        arrayHcConsulta.add("esc_situa_actual");
        arrayHcConsulta.add("esc_cond_soc_emoc");
        arrayHcConsulta.add("ant_medicos");
        arrayHcConsulta.add("ant_quirurjico");
        arrayHcConsulta.add("ant_transfuncionales");
        arrayHcConsulta.add("ant_inmunologico");
        arrayHcConsulta.add("ant_alergicos");
        arrayHcConsulta.add("ant_traumaticos");
        arrayHcConsulta.add("ant_psicologico");
        arrayHcConsulta.add("ant_farmacologico");
        arrayHcConsulta.add("ant_familiares");
        arrayHcConsulta.add("ant_toxicos");
        arrayHcConsulta.add("ant_otros");
        arrayHcConsulta.add("revision_sistema");

        arrayHcConsulta.add("analisis");
        arrayHcConsulta.add("imp_diag_ppal");
        arrayHcConsulta.add("imp_diag_1");
        arrayHcConsulta.add("imp_diag_2");
        arrayHcConsulta.add("imp_diag_3");
        arrayHcConsulta.add("tipo_diag_consulta");
        arrayHcConsulta.add("familiograma");
        arrayHcConsulta.add("obj_terap_area");
        arrayHcConsulta.add("plan_trat");

        arrayHcPsicologica.add("num_historia");
        arrayHcPsicologica.add("tipo_consulta");
        arrayHcPsicologica.add("finalidad");
        arrayHcPsicologica.add("mot_consulta");
        arrayHcPsicologica.add("num_autorizacion");
        arrayHcPsicologica.add("colegio");
        arrayHcPsicologica.add("escolaridad");
        arrayHcPsicologica.add("nombre_padre");
        arrayHcPsicologica.add("nombre_madre");
        arrayHcPsicologica.add("acudiente");
        arrayHcPsicologica.add("lateridad");
        arrayHcPsicologica.add("motivo_consulta");
        arrayHcPsicologica.add("enf_actual");
        arrayHcPsicologica.add("his_ant_fliares");
        arrayHcPsicologica.add("des_prenatal");
        arrayHcPsicologica.add("des_pos_motor");
        arrayHcPsicologica.add("des_pos_lenguaje");
        arrayHcPsicologica.add("esc_ini_adap");
        arrayHcPsicologica.add("esc_situa_actual");
        arrayHcPsicologica.add("esc_cond_soc_emoc");
        arrayHcPsicologica.add("ant_medicos");
        arrayHcPsicologica.add("ant_quirurjico");
        arrayHcPsicologica.add("ant_transfuncionales");
        arrayHcPsicologica.add("ant_inmunologico");
        arrayHcPsicologica.add("ant_alergicos");
        arrayHcPsicologica.add("ant_traumaticos");
        arrayHcPsicologica.add("ant_psicologico");
        arrayHcPsicologica.add("ant_farmacologico");
        arrayHcPsicologica.add("ant_familiares");
        arrayHcPsicologica.add("ant_toxicos");
        arrayHcPsicologica.add("ant_otros");
        arrayHcPsicologica.add("revision_sistema");
        arrayHcPsicologica.add("exa_fis_ta");
        arrayHcPsicologica.add("exa_fis_fc");
        arrayHcPsicologica.add("exa_fis_fr");
        arrayHcPsicologica.add("exa_fis_talla");
        arrayHcPsicologica.add("exa_fis_peso");
        arrayHcPsicologica.add("exa_fis_estado_general");
        arrayHcPsicologica.add("examen_menta");
        arrayHcPsicologica.add("analisis");
        arrayHcPsicologica.add("imp_diag_ppal");
        arrayHcPsicologica.add("imp_diag_1");
        arrayHcPsicologica.add("imp_diag_2");
        arrayHcPsicologica.add("imp_diag_3");
        arrayHcPsicologica.add("tipo_diag_consulta");
        arrayHcPsicologica.add("familiograma");
        arrayHcPsicologica.add("obj_terap_area");
        arrayHcPsicologica.add("plan_trat");
    }

    private void correccionAntecedentesPrenatales() {

        cargarArrays();
        int contador = 0;
        FileWriter fichero = null;
        PrintWriter pw;
        String txtSalida;

        try {
            fichero = new FileWriter("d:/prueba.txt");
            pw = new PrintWriter(fichero);

            //inicialmente se tomo cod_campo=19 pero era 164 pero al final queda 14// HISTORIA CLINICA CONSULTA
            ResultSet rsA = consult("select * from migracion_reg_cli where cod_campo like '14' order by num_historia::float, cod_cabecera::float");
            ResultSet rsB;
            ResultSet rsC;
            ArrayList<String> arrayEnBlanco = new ArrayList<>();
            //arrayEnBlanco = arr
            txtSalida = "";
            for (String txt : arrayHcConsulta) {
                txtSalida = txtSalida + "\"" + txt + "\";";
                arrayEnBlanco.add("-");
            }
            pw.println(txtSalida);
            String id_paciente;
            while (rsA.next()) {
                id_paciente = "";
                rsB = consult("select * from cfg_pacientes where identificacion like '" + rsA.getString("num_historia") + "'");
                if (rsB.next()) {
                    id_paciente = rsB.getString("id_paciente");
                }
                pw.println("\"{{{{{{{{{{\";\"num_historia(mf)\";\"" + rsA.getString("num_historia") + "\";\"{{{{{{{{{{\";\"id_paciente(om)\";\"" + id_paciente + "\";\"{{{{{{{{{{\";\"{{{{{{{{{{\";");
                rsB = consult("select * from migracion_reg_cli where cod_cabecera like '" + rsA.getString("cod_cabecera") + "'");
                //LIMPIO EL ARRAY EN BLANCO
                for (int i = 0; i < arrayEnBlanco.size(); i++) {
                    arrayEnBlanco.set(i, "-");
                }
                //CARGO DATOS AL ARRAY EN BLANCO
                arrayEnBlanco.set(0, "cod_cabecera(mf) " + rsA.getString("cod_cabecera"));
                while (rsB.next()) {
                    arrayEnBlanco.set(rsB.getInt("cod_campo"), rsB.getString("texto"));
                }
                //GUARDO EN EL ARCHIVO EL ARRAY EN BLANCO(REGISTRO ENCONTRADO EN MEDIFOLIOS)
                txtSalida = "";
                for (String str : arrayEnBlanco) {
                    txtSalida = txtSalida + "\"" + str + "\";";
                }
                pw.println(txtSalida);

                //DETERMINO LOS REGISTROS EN OPENMEDICAL
                rsB = consult("select * from hc_registro where id_tipo_reg=1 and id_paciente = (select id_paciente from cfg_pacientes where identificacion like '" + rsA.getString("num_historia") + "')");
                while (rsB.next()) {
                    rsC = consult("select * from hc_detalle where id_registro = " + rsB.getString("id_registro"));
                    //LIMPIO EL ARRAY EN BLANCO
                    for (int i = 0; i < arrayEnBlanco.size(); i++) {
                        arrayEnBlanco.set(i, "-");
                    }
                    //CARGO DATOS AL ARRAY EN BLANCO
                    arrayEnBlanco.set(0, "id_registro(om) " + rsB.getString("id_registro"));
                    while (rsC.next()) {
                        arrayEnBlanco.set(rsC.getInt("id_campo"), rsC.getString("valor"));
                    }
                    //GUARDO EN EL ARCHIVO EL ARRAY EN BLANCO(REGISTRO ENCONTRADO EN MEDIFOLIOS)
                    txtSalida = "";
                    for (String str : arrayEnBlanco) {
                        txtSalida = txtSalida + "\"" + str + "\";";
                    }
                    pw.println(txtSalida);
                }
                System.out.println(contador++);
            }
            System.out.println("FINALIZACION CORRECTA");
            /*
             //inicialmente se tomo cod_campo=262 pero era 344 pero al final queda 81//HISTORIA CLINICA PSICOLOGIA
             ResultSet rs = consult("select * from migracion_reg_cli where cod_campo like '81'");//--536 registros
             ResultSet rsB;
             while (rs.next()&&contador<10) {
             //se arma un vector con la cantidad de registros encontrados
             rsB = consult("select count(*) from migracion_reg_cli where cod_cabecera like '" + rs.getString("cod_cabecera") + "'");
             if (rsB.next()) {//me entrega la cantidad

             }
             contador++;
             }*/
            fichero.close();
        } catch (IOException | SQLException e) {
            System.err.println("001-----------------001   " + e.getMessage());
        } finally {
            try {
                // Nuevamente aprovechamos el finally para 
                // asegurarnos que se cierra el fichero.
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public ResultSet consult(String query) {

        try {
            if (conn != null) {
                st = conn.createStatement();
                rs = st.executeQuery(query);
                //System.out.println("---- CONSULTA: " + query);
                return rs;
            } else {
                System.out.println("There don't exist connection");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error1: " + e.toString() + " - Clase: " + this.getClass().getName());
            System.out.println(query);
            return null;
        }
    }

    public int non_query(String query) {
        int reg;
        reg = 0;
        try {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    reg = stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error:2" + e.toString() + " -- Clase: " + this.getClass().getName() + " -  " + query);
            System.out.println(query);
        }
        return reg;
    }

    public String insert(String Tabla, String elementos, String valores) {
        int reg = 1;
        String success;
        try {
            if (conn != null) {
                st = conn.createStatement();
                st.execute("INSERT INTO " + Tabla + " (" + elementos + ") VALUES (" + valores + ")");
                if (reg > 0) {
                    success = "true";
                } else {
                    success = "false";
                }
                st.close();
            } else {
                success = "false";
            }
        } catch (SQLException e) {
            System.out.println("Error: 3 " + e.toString());
            System.out.println("INSERT INTO " + Tabla + " (" + elementos + ") VALUES (" + valores + ")");
            success = e.getMessage();
        }
        return success;
    }

    public void remove(String Tabla, String condicion) {
        int reg;
        try {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + Tabla + " WHERE " + condicion)) {
                    reg = stmt.executeUpdate();
                    if (reg > 0) {
                    } else {
                    }
                }
            } else {
                System.out.println("ERROR: There don't exist connection");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.toString() + " ---- Clase: " + this.getClass().getName());
        }
    }

    public void update(String Tabla, String campos, String donde) {
        int reg;
        try {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement("UPDATE " + Tabla + " SET " + campos + " WHERE " + donde)) {
                    reg = stmt.executeUpdate();
                    if (reg > 0) {
                    } else {
                    }
                }
            } else {
                System.out.println("ERROR: There don't exist connection");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.toString() + " ----- Clase: " + this.getClass().getName());
        }
    }

    @PreDestroy
    private void cerrarConexion() {
        try {
            if (!conn.isClosed()) {
                conn.close();
                System.out.println("Cerrada conexion a base de datos " + url + " ... OK  " + this.getClass().getName());
            }
        } catch (Exception e) {
            System.out.println("Error al cerrar conexion a base de datos " + url + " ... " + e.toString());
        }
    }

}
