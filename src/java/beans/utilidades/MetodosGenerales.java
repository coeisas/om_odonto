/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.utilidades;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import modelo.entidades.CfgClasificaciones;
import modelo.fachadas.CfgClasificacionesFacade;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.primefaces.model.UploadedFile;
import org.apache.poi.xssf.usermodel.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Administrador
 */
public class MetodosGenerales {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    CfgClasificacionesFacade clasFacade;

    
    //---------------------------------------------------
    //-----------------VARIABLES -------------------------
    //---------------------------------------------------
    
    
    List<String> listaTagsValidos = Arrays.asList("<strong>", "</strong>", "<span style=\"font-weight: bold;\">", "<span>", "</span>", "<p>", "</p>", "<b>", "</b>", "<ol>", "</ol>",
            "<li>", "</li>", "<div>", "</div>", "<br>", "<div style=\"text-align: justify;\">");

    
    //---------------------------------------------------
    //-----------------FUNCIONES -------------------------
    //---------------------------------------------------    

    //CREA UNA CELDA PARA UN DOCUEMNTO DE TIPO XLS
    public void createCell(XSSFCellStyle cellStyle, XSSFRow fila, int position, String value) {
        XSSFCell cell;
        cell = fila.createCell((short) position);// Se crea una cell dentro de la fila                        
        cell.setCellValue(new XSSFRichTextString(value));
        cell.setCellStyle(cellStyle);
    }

    //CREA UNA CELDA PARA UN DOCUMENTO TIPO EXCEL
    public void createCell(XSSFRow fila, int position, String value) {
        XSSFCell cell;
        cell = fila.createCell((short) position);// Se crea una cell dentro de la fila                        
        cell.setCellValue(new XSSFRichTextString(value));
    }

    public String corregirHtml(String in) {
        //corrige el html que viede de un editor primefaces y en ocasiones ingresa textos errorneos
        String valorActual = in;
        String valorFinal = in;
        String tagActual = "";
        String caso = "esperandoMenorQue";
        ArrayList<String> listaTags = new ArrayList<>();//almacenara todos los tags que tenga valor
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
        int posicion;
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
//            System.out.println("------------------------------------------------");
//            System.out.println("Acual: " + valorActual);
//            System.out.println("Final: " + valorFinal);
//            System.out.println("------------------------------------------------");
        }
        return valorFinal;
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

    public String obtenerCadenaNoNula(String txt1) {
        //se entrega una cadena que no contenga null
        String strReturn = "";
        if (txt1 != null && txt1.trim().length() != 0) {
            strReturn = strReturn + txt1;
        }
        return strReturn;
    }

    public String obtenerCadenaNoNula(String txt1, String txt2) {
        //se entrega una cadena que no contenga null
        String strReturn = "";
        if (txt1 != null && txt1.trim().length() != 0) {
            strReturn = strReturn + txt1;
        }
        if (txt2 != null && txt2.trim().length() != 0) {
            strReturn = strReturn + " " + txt2;
        }

        return strReturn;
    }

    public String obtenerCadenaNoNula(String txt1, String txt2, String txt3) {
        //se entrega una cadena que no contenga null
        String strReturn = "";
        if (txt1 != null && txt1.trim().length() != 0) {
            strReturn = strReturn + txt1;
        }
        if (txt2 != null && txt2.trim().length() != 0) {
            strReturn = strReturn + " " + txt2;
        }
        if (txt3 != null && txt3.trim().length() != 0) {
            strReturn = strReturn + " " + txt3;
        }
        return strReturn;
    }

    public String obtenerCadenaNoNula(String txt1, String txt2, String txt3, String txt4) {
        //se entrega una cadena que no contenga null
        String strReturn = "";
        if (txt1 != null && txt1.trim().length() != 0) {
            strReturn = strReturn + txt1;
        }
        if (txt2 != null && txt2.trim().length() != 0) {
            strReturn = strReturn + " " + txt2;
        }
        if (txt3 != null && txt3.trim().length() != 0) {
            strReturn = strReturn + " " + txt3;
        }
        if (txt4 != null && txt4.trim().length() != 0) {
            strReturn = strReturn + " " + txt4;
        }

        return strReturn;
    }

    public String esFecha(String f, String format) {
        /*
         *  null=invalido ""=aceptado pero vacio "valor"=aceptado (valor para db)
         */
        if (f.trim().length() == 0) {
            return "";
        }
        try {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTimeFormatter fmt2 = DateTimeFormat.forPattern(format);
            DateTime the_date = DateTime.parse(f, fmt2);//trata de convertir al formato "format"(me llega por parametro)
            return fmt.print(the_date);//lo imprime en el formato "yyyy-MM-dd"
        } catch (Throwable ex) {
            return null;//invalida
        }
    }

    public boolean fechaDentroDeRangoMas1Dia(Date inicioRango, Date finRango, Date fecBuscada) {
        //determinar si una fecha esta dentro de un rango; true = fecha en el rango
        //se suma 1 dia a la fecha final para que incluya el ultimo dia 
        Interval interval = new Interval(new DateTime(inicioRango), new DateTime(finRango).plusDays(1));
        return interval.contains(new DateTime(fecBuscada));
    }

    private boolean fechaDentroDeRango(Date inicioRango, Date finRango, Date fecBuscada) {
        //determinar si una fecha esta dentro de un rango; true = fecha en el rango
        //no es necesario aumentar el dia final por que se evaluan ambos limites de ambos rangos
        Interval interval = new Interval(new DateTime(inicioRango), new DateTime(finRango));
        return interval.contains(new DateTime(fecBuscada));
    }

    public boolean rangoDentroDeRango(Date inicioRango1, Date finRango1, Date inicioRango2, Date finRango2) {
        //determinar si un rango de fechas esta dentro de otro rango de fechas; true = se interceptan
        return fechaDentroDeRango(inicioRango1, finRango1, inicioRango2)
                || fechaDentroDeRango(inicioRango1, finRango1, finRango2)
                || fechaDentroDeRango(inicioRango2, finRango2, finRango1)
                || fechaDentroDeRango(inicioRango2, finRango2, finRango1);
    }

    public String calcularEdad(Date fechaNacimiento) {//calcular la edad a partir de la fecha de nacimiento    
        Period periodo = new Period(new DateTime(fechaNacimiento), new DateTime(new Date()));
        return String.valueOf(periodo.getYears()) + "A " + String.valueOf(periodo.getMonths()) + "M ";
    }

    public int calcularEdadInt(Date fechaNacimiento) {//calcular la edad en años
        Period periodo = new Period(new DateTime(fechaNacimiento), new DateTime(new Date()));
        if (periodo.getYears() == 0) {
            return 1;
        } else {
            return periodo.getYears();
        }
    }

    public String cerosIzquierda(int valor, int numCifras) {
        //aumenta ceros a la izquierda segun numCifas
        String v = String.valueOf(valor);
        int ceros = numCifras - v.length();
        if (v.length() < numCifras) {
            for (int i = 0; i < ceros; i++) {
                v = "0" + v;
            }
        }
        return v;
    }

    public static String getErrorExcepcion(Throwable e, int profundidad) {
        /*ESTE METODO PERMITE SACAR EN PROFUNDIDAD UN EXCEPCION CAPTURADA DESDE
         OTRAS CLASES O CLASES LOCALES*/
        int profundidadVo = profundidad;
        try {
            if (e != null) {
                if (e.getCause() != null && profundidadVo < 6) {
                    profundidadVo++;
                    return getErrorExcepcion(e.getCause(), profundidadVo);
                } else {
                    String error = e.toString() == null ? null : e.toString();
                    if (error != null) {
                        if (error.length() > 300) {
                            error = error.substring(0, 299);
                        }
                    }
                    return error;
                }
            } else {
                return null;
            }
        } catch (Exception ex) {
            return "No fue posible obtener el error en la excepción debido a: " + ex.getMessage();
        }
    }

    public boolean esNumero(String valor) {
        //validar si una cadena es numerica
        try {
            Integer.parseInt(valor);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validarNoVacio(String valor) {
        //validas si una cadena no es vacia //true= no vacio //false=vacio
        if (valor == null) {
            return false;
        }
        return valor.trim().length() != 0;
    }

    public boolean validacionCampoVacio(String valor, String nombre) {
        /*false = lleno, true = vacio pero ademas imprime el mensaje de error
         */
        if (valor == null || valor.trim().length() == 0) {
            imprimirMensaje("Error", "El campo " + nombre + " es obligatorio", FacesMessage.SEVERITY_ERROR);
            return true;
        }
        return false;
    }

    public boolean validacionFechaVacia(Date f, String nombre) {
        /*false = lleno, true = vacio pero ademas imprime el mensaje de error
         */
        if (f == null) {
            imprimirMensaje("Error", "El campo " + nombre + " es obligatorio", FacesMessage.SEVERITY_ERROR);
            return true;
        }
        return false;
    }

    public void moverArchivo(String origen, String destino) {
        File fichero = new File(origen);
        if (!fichero.exists()) {
            System.out.println("Error. No existe un fichero " + origen);
            return;
        }
        File fichero2 = new File(destino);
        if (fichero2.exists()) {
            //System.out.println("Error. Ya existe un fichero " + destino);
            fichero2.delete();
            fichero2 = new File(destino);
        }
        boolean success = fichero.renameTo(fichero2);
        if (!success) {
            System.out.println("Error intentando mover archivo ");
        }
    }

    public void imprimirMensaje(String titulo, String mensaje, Severity tipo) {
        if (tipo == FacesMessage.SEVERITY_INFO) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, titulo, mensaje));
        } else if (tipo == FacesMessage.SEVERITY_WARN) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, titulo, mensaje));
        } else if (tipo == FacesMessage.SEVERITY_ERROR) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, titulo, mensaje));
            //System.out.println("ERROR: ############### " + titulo + " ---- " + mensaje);
        } else if (tipo == FacesMessage.SEVERITY_FATAL) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, titulo, mensaje));
        }
    }

    public boolean uploadArchivo(UploadedFile archivoCargado, String rutaDestino) {
        File fichero = new java.io.File(rutaDestino);
        if (fichero.exists()) {//si existe se borra
            fichero.delete();
        }
        fichero = new java.io.File(rutaDestino);
        try (FileOutputStream fileOutput = new FileOutputStream(fichero)) {
            InputStream inputStream = archivoCargado.getInputstream();
            byte[] buffer = new byte[1024];
            int bufferLength;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
            }
        } catch (Exception e) {
            System.out.println("Error 01: " + e.getMessage());
            imprimirMensaje("Error 01", e.getMessage(), FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    public List<SelectItem> cargarClasificacion(String maestro) {
        List<SelectItem> listaRetorno = new ArrayList<>();
        List<CfgClasificaciones> listaClasificaciones = clasFacade.buscarPorMaestro(maestro);
        for (CfgClasificaciones clasificacion : listaClasificaciones) {
            listaRetorno.add(new SelectItem(clasificacion.getId(), clasificacion.getDescripcion()));
        }
        return listaRetorno;
    }

}
