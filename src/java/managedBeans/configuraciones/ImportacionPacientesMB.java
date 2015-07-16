package managedBeans.configuraciones;

import beans.utilidades.FilaDataTable;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgPacientesFacade;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.FileUploadEvent;
import beans.utilidades.MetodosGenerales;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.CfgConfiguraciones;
import modelo.entidades.CfgPacientes;
import modelo.fachadas.CfgConfiguracionesFacade;
import modelo.fachadas.FacAdministradoraFacade;

import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.context.RequestContext;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

@ManagedBean(name = "importacionPacientesMB")
@SessionScoped
public class ImportacionPacientesMB extends MetodosGenerales implements Serializable {
    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------

    @EJB
    CfgPacientesFacade pacientesFachada;
    @EJB
    CfgClasificacionesFacade clasificacionesFachada;
    @EJB
    FacAdministradoraFacade administradoraFacade;
    @EJB
    CfgConfiguracionesFacade configuracionesFacade;
    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------
    private CfgConfiguraciones configuracionActual;
    //private LazyDataModel<CfgPacientes> listaPacientes;
    //private CfgPacientes pacienteSeleccionado;
    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------    
    private int tuplesProcessed = 0;
    private int numeroDeError = 0;
    private int numeroDeAccion = 0;
    private ArrayList<FilaDataTable> listaErrores = new ArrayList<>(); //SI EL PROCESO FINALIZO CORRECTAMENTE NO HAY ERRORES
    private ArrayList<FilaDataTable> listaAcciones = new ArrayList<>(); //ACCIONES A REALIZAR CUANDO EL ARCHIVO ESTA CORRECTO
    private boolean renderedTablaErrores = false;//ERRORES CUANDO SE VALIDA EL ARCHIVO
    private boolean renderedTablaAcciones = false;//ACCIONES QUE SE LLEVARAN A CABO DESPUES DE LEER Y VALIDAR EL ARCHIVO
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    //---------------------------------------------------
    //----------------- FUNCIONES -------------------------
    //---------------------------------------------------      
    @PostConstruct
    public void inicializar() {
        configuracionActual = configuracionesFacade.findAll().get(0);
    }

    //---------------------------------------------------
    //----------------- EXPORTACION ---------------------
    //---------------------------------------------------      
    public void postProcessXLS(Object document) {
        XSSFWorkbook book = (XSSFWorkbook) document;
        XSSFSheet sheet = book.getSheetAt(0);// Se toma hoja del libro
        XSSFRow row;
        XSSFCellStyle cellStyle = book.createCellStyle();
        XSSFFont font = book.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        cellStyle.setFont(font);
        row = sheet.createRow(0);// Se crea una fila dentro de la hoja        

        createCell(cellStyle, row, 0, "IDENTIFICACION");//identificacion,      
        createCell(cellStyle, row, 1, "TIPO IDENTIFICACION");//tipo odentificacion
        createCell(cellStyle, row, 2, "LUGAR DE EXPEDICION");//lugar_expedicion,
        createCell(cellStyle, row, 3, "FECHA NACIMIENTO");//fecha_nacimiento::date,
        createCell(cellStyle, row, 4, "EDAD");//EDAD   
        createCell(cellStyle, row, 5, "GENERO");//genero,   
        createCell(cellStyle, row, 6, "GRUPO SANGUINEO");//grupo_sanguineo,
        createCell(cellStyle, row, 7, "PRIMER NOMBRE");//primer_nombre,
        createCell(cellStyle, row, 8, "SEGUNDO NOMBRE");//segundo_nombre,
        createCell(cellStyle, row, 9, "PRIMER APELLIDO");//primer_apellido,
        createCell(cellStyle, row, 10, "SEGUNDO APELLIDO");//segundo_apellido,
        createCell(cellStyle, row, 11, "OCUPACION");//ocupacion,
        createCell(cellStyle, row, 12, "ESTADO CIVIL");//estado_civil,
        createCell(cellStyle, row, 13, "TEL RESIDENCIA");//telefono_residencia,
        createCell(cellStyle, row, 14, "TEL OFICINA");//telefono_oficina,
        createCell(cellStyle, row, 15, "CELULAR");//celular,
        createCell(cellStyle, row, 16, "DEPARTAMENTO");//departamento,
        createCell(cellStyle, row, 17, "MUNICIPIO");//municipio, 
        createCell(cellStyle, row, 18, "ZONA");//zona,
        createCell(cellStyle, row, 19, "DIRECCION");//direccion,
        createCell(cellStyle, row, 20, "BARRIO");//barrio, 
        createCell(cellStyle, row, 21, "EMAIL");//email,
        createCell(cellStyle, row, 22, "ACTIVO");//activo, 
        createCell(cellStyle, row, 23, "ADMINISTRADORA");//administradora,
        createCell(cellStyle, row, 24, "TIPO AFILIADO");//tipo_afiliado,
        createCell(cellStyle, row, 25, "REGIMEN");//regimen,
        createCell(cellStyle, row, 26, "CATEGORIA PACIENTE");//categoria_paciente, 
        createCell(cellStyle, row, 27, "NIVEL");//nivel,
        createCell(cellStyle, row, 28, "ETNIA");//etnia,
        createCell(cellStyle, row, 29, "ESCOLARIDAD");//escolaridad,
        createCell(cellStyle, row, 30, "RESPONSABLE");//responsable,
        createCell(cellStyle, row, 31, "TEL RESPONSABLE");//telefono_responsable,
        createCell(cellStyle, row, 32, "PARENTESCO");//parentesco,
        createCell(cellStyle, row, 33, "ACOMPAÑANTE");//acompanante,
        createCell(cellStyle, row, 34, "TEL ACOMPAÑANTE");//telefono_acompanante,  
        createCell(cellStyle, row, 35, "FECHA AFILIACION");//fecha_afiliacion,
        createCell(cellStyle, row, 36, "FECHA SISBEN");//fecha_sisben,
        createCell(cellStyle, row, 37, "CARNET");//carnet,  
        createCell(cellStyle, row, 38, "FECHA VENCE CARNET");//fecha_vence_carnet,
        createCell(cellStyle, row, 39, "OBSERVACIONES");//observaciones
        List<Object> listaDatosPacientes = pacientesFachada.exportacionPacientes();
        for (int i = 0; i < listaDatosPacientes.size(); i++) {
            row = sheet.createRow(i + 1);
            Object[] datosUnPaciente = (Object[]) listaDatosPacientes.get(i);
            for (int j = 0; j < datosUnPaciente.length; j++) {
                if (datosUnPaciente[j] != null) {
                    createCell(row, j, datosUnPaciente[j].toString());//CODIGO  
                }
            }
        }
        //RequestContext.getCurrentInstance().execute("pageActive()");
        //System.out.println("Acabo");
    }

    //---------------------------------------------------
    //----------------- IMPORTACION ---------------------
    //---------------------------------------------------      
    public void importarPacientes(FileUploadEvent event) {
        try {
            copyFile("importado.xlsx", event.getFile().getInputstream());
            comprobarInformacion();
            if (listaErrores != null && !listaErrores.isEmpty()) {
                imprimirMensaje("Error", "El archivo tiene errores que deben ser corregidos", FacesMessage.SEVERITY_ERROR);
                renderedTablaAcciones = false;
                renderedTablaErrores = true;
            } else {
                imprimirMensaje("Correcto", "Archivo validado, use el botón 'realizar importación' para realizar las acciones.", FacesMessage.SEVERITY_INFO);
                renderedTablaAcciones = true;
                renderedTablaErrores = false;
            }
            RequestContext.getCurrentInstance().update("IdFormPrincipal:IdTabView");
        } catch (Exception ex) {
            System.out.println("Error 001 en " + this.getClass().getName() + ":" + ex.toString());
            imprimirMensaje("Error", "Error al cargar archivo" + ex.toString(), FacesMessage.SEVERITY_ERROR);
        }
    }

    private void copyFile(String fileName, InputStream in) {//copea el archivo en la raiz del servidor
        try {
            File importFile = new java.io.File(configuracionActual.getRutaImagenes() + "" + fileName);
            if (importFile.exists()) {
                importFile.delete();//elimino el archivo
            }
            try (OutputStream out = new FileOutputStream(new File(configuracionActual.getRutaImagenes() + fileName))) {
                int read;
                byte[] bytes = new byte[1024];
                while ((read = in.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                in.close();
                out.flush();
            }
            //System.out.println("El nuevo fichero fue creado con éxito! " + fileName);
        } catch (IOException e) {
            System.out.println("Error 002 en " + this.getClass().getName() + ":" + e.toString());
        }
    }

    public void comprobarInformacion() {
        try {
            File file2 = new File(configuracionActual.getRutaImagenes() + "importado.xlsx");
            tuplesProcessed = 0;
            numeroDeError = 0;
            numeroDeAccion = 0;
            listaAcciones = new ArrayList<>();
            listaErrores = new ArrayList<>(); //si el proceso finalizo correctamente no hay errores
            try {//procesar el archivo
                OPCPackage container;
                container = OPCPackage.open(file2.getAbsolutePath());
                ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(container);
                XSSFReader xssfReader = new XSSFReader(container);
                StylesTable styles = xssfReader.getStylesTable();
                XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
                while (iter.hasNext()) {
                    InputStream stream = iter.next();
                    InputSource sheetSource = new InputSource(stream);
                    SAXParserFactory saxFactory = SAXParserFactory.newInstance();
                    try {
                        SAXParser saxParser = saxFactory.newSAXParser();
                        XMLReader sheetParser = saxParser.getXMLReader();
                        ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, new XSSFSheetXMLHandler.SheetContentsHandler() {
                            ArrayList<String> rowFileData = new ArrayList<>();
                            ArrayList<String> headerFileNames = new ArrayList<>();
                            boolean continueProcces = true;

                            @Override
                            public void startRow(int rowNum) {//System.out.println(" INICIA " + String.valueOf(rowNum) + "-----------------------");
                                rowFileData = new ArrayList<>();
                            }

                            @Override
                            public void endRow() {
                                if (continueProcces) {
                                    int empyColumns = headerFileNames.size() - rowFileData.size();//COMPLETAR SI HAY CASILLAS VACIAS
                                    for (int i = 0; i < empyColumns; i++) {
                                        rowFileData.add("");
                                    }
                                    if (tuplesProcessed == 0) {//ES LA CABECERA
                                        if (rowFileData.size() != 40) {
                                            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "-", "LA CABECERA DEBE TENER 40 COLUMNAS", "SE DEBE UTILIZAR COMO BASE EL ARCHIVO DE EXPORTACION DE PACIENTES"));
                                            continueProcces = false;
                                        }
                                        comprobarCabecera(rowFileData);
                                        if (listaErrores.isEmpty()) {
                                            headerFileNames = rowFileData;
                                        } else {
                                            continueProcces = false;
                                        }
                                    } else {//ES UNA FILA(DISTINTA DE LA CABECERA)
                                        comprobarFila(rowFileData, tuplesProcessed);//System.out.println("fila " + tuplesProcessed + " " + rowFileData.toString());
                                    }
                                    tuplesProcessed++;
                                }
                            }

                            @Override
                            public void cell(String cellReference, String formattedValue) {
                                if (continueProcces) {
                                    CellReference a = new CellReference(cellReference);
                                    int empyColumns = a.getCol() - rowFileData.size();//COMPLETAR SI HAY CASILLAS VACIAS
                                    for (int i = 0; i < empyColumns; i++) {
                                        rowFileData.add("");
                                    }
                                    rowFileData.add(formattedValue.replace("\n", " ").replace((char) 13, ' '));//AGREGAR VALOR A LA FILA
                                }
                            }

                            @Override
                            public void headerFooter(String text, boolean isHeader, String tagName) {
                            }
                        }, false);//means result instead of formula                                
                        sheetParser.setContentHandler(handler);
                        sheetParser.parse(sheetSource);
                    } catch (ParserConfigurationException e) {
                        System.out.println("Error 12 en " + this.getClass().getName() + ":" + e.toString());
                        //throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
                    }
                    stream.close();
                    break;
                }
            } catch (InvalidFormatException e) {
                System.out.println("Error 13 en " + this.getClass().getName() + ":" + e.toString());
            } catch (SAXException e) {
                System.out.println("Error 14 en " + this.getClass().getName() + ":" + e.toString());
            } catch (OpenXML4JException e) {
                System.out.println("Error 15 en " + this.getClass().getName() + ":" + e.toString());
            }
        } catch (Exception e) {
            System.out.println("Error 16 en " + this.getClass().getName() + ":" + e.toString());
        }
    }

    private void comprobarCabecera(ArrayList<String> rowFileData) {
        //System.out.println(rowFileData.toString());
        if (rowFileData.get(0).compareTo("IDENTIFICACION") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 1 " + rowFileData.get(1), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 1 SE DEBE LLAMAR 'IDENTIFICACION'"));
        }
        if (rowFileData.get(1).compareTo("TIPO IDENTIFICACION") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 2 " + rowFileData.get(0), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 2 SE DEBE LLAMAR 'TIPO IDENTIFICACION'"));
        }

        if (rowFileData.get(2).compareTo("LUGAR DE EXPEDICION") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 3 " + rowFileData.get(2), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 3 SE DEBE LLAMAR 'LUGAR DE EXPEDICION'"));
        }
        if (rowFileData.get(3).compareTo("FECHA NACIMIENTO") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 4 " + rowFileData.get(3), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 4 SE DEBE LLAMAR 'FECHA NACIMIENTO'"));
        }
        if (rowFileData.get(4).compareTo("EDAD") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 5 " + rowFileData.get(4), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 5 SE DEBE LLAMAR 'EDAD'"));
        }
        if (rowFileData.get(5).compareTo("GENERO") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 6 " + rowFileData.get(5), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 6 SE DEBE LLAMAR 'GENERO'"));
        }
        if (rowFileData.get(6).compareTo("GRUPO SANGUINEO") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 7 " + rowFileData.get(6), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 7 SE DEBE LLAMAR 'GRUPO SANGUINEO'"));
        }
        if (rowFileData.get(7).compareTo("PRIMER NOMBRE") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 8 " + rowFileData.get(7), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 8 SE DEBE LLAMAR 'PRIMER NOMBRE'"));
        }
        if (rowFileData.get(8).compareTo("SEGUNDO NOMBRE") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 9 " + rowFileData.get(8), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 9 SE DEBE LLAMAR 'SEGUNDO NOMBRE'"));
        }
        if (rowFileData.get(9).compareTo("PRIMER APELLIDO") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 10 " + rowFileData.get(9), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 10 SE DEBE LLAMAR 'PRIMER APELLIDO'"));
        }
        if (rowFileData.get(10).compareTo("SEGUNDO APELLIDO") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 11 " + rowFileData.get(10), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 11 SE DEBE LLAMAR 'SEGUNDO APELLIDO'"));
        }
        if (rowFileData.get(11).compareTo("OCUPACION") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 12 " + rowFileData.get(11), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 12 SE DEBE LLAMAR 'OCUPACION'"));
        }
        if (rowFileData.get(12).compareTo("ESTADO CIVIL") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 13 " + rowFileData.get(12), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 13 SE DEBE LLAMAR 'ESTADO CIVIL'"));
        }
        if (rowFileData.get(13).compareTo("TEL RESIDENCIA") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 14 " + rowFileData.get(13), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 14 SE DEBE LLAMAR 'TEL RESIDENCIA'"));
        }
        if (rowFileData.get(14).compareTo("TEL OFICINA") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 15 " + rowFileData.get(14), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 15 SE DEBE LLAMAR 'TEL OFICINA'"));
        }
        if (rowFileData.get(15).compareTo("CELULAR") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 16 " + rowFileData.get(15), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 16 SE DEBE LLAMAR 'CELULAR'"));
        }
        if (rowFileData.get(16).compareTo("DEPARTAMENTO") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 17 " + rowFileData.get(16), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 17 SE DEBE LLAMAR 'DEPARTAMENTO'"));
        }
        if (rowFileData.get(17).compareTo("MUNICIPIO") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 18 " + rowFileData.get(17), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 18 SE DEBE LLAMAR 'MUNICIPIO'"));
        }
        if (rowFileData.get(18).compareTo("ZONA") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 19 " + rowFileData.get(18), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 19 SE DEBE LLAMAR 'ZONA'"));
        }
        if (rowFileData.get(19).compareTo("DIRECCION") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 20 " + rowFileData.get(19), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 20 SE DEBE LLAMAR 'DIRECCION'"));
        }
        if (rowFileData.get(20).compareTo("BARRIO") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 21 " + rowFileData.get(20), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 21 SE DEBE LLAMAR 'BARRIO'"));
        }
        if (rowFileData.get(21).compareTo("EMAIL") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 22 " + rowFileData.get(21), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 22 SE DEBE LLAMAR 'EMAIL'"));
        }
        if (rowFileData.get(22).compareTo("ACTIVO") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 23 " + rowFileData.get(22), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 23 SE DEBE LLAMAR 'ACTIVO'"));
        }
        if (rowFileData.get(23).compareTo("ADMINISTRADORA") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 24 " + rowFileData.get(23), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 24 SE DEBE LLAMAR 'ADMINISTRADORA'"));
        }
        if (rowFileData.get(24).compareTo("TIPO AFILIADO") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 25 " + rowFileData.get(24), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 25 SE DEBE LLAMAR 'TIPO AFILIADO'"));
        }
        if (rowFileData.get(25).compareTo("REGIMEN") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 26 " + rowFileData.get(25), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 26 SE DEBE LLAMAR 'REGIMEN'"));
        }
        if (rowFileData.get(26).compareTo("CATEGORIA PACIENTE") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 27 " + rowFileData.get(26), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 27 SE DEBE LLAMAR 'CATEGORIA PACIENTE'"));
        }
        if (rowFileData.get(27).compareTo("NIVEL") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 28 " + rowFileData.get(27), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 28 SE DEBE LLAMAR 'NIVEL'"));
        }
        if (rowFileData.get(28).compareTo("ETNIA") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 29 " + rowFileData.get(28), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 29 SE DEBE LLAMAR 'ETNIA'"));
        }
        if (rowFileData.get(29).compareTo("ESCOLARIDAD") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 30 " + rowFileData.get(29), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 30 SE DEBE LLAMAR 'ESCOLARIDAD'"));
        }
        if (rowFileData.get(30).compareTo("RESPONSABLE") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 31 " + rowFileData.get(30), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 31 SE DEBE LLAMAR 'RESPONSABLE'"));
        }
        if (rowFileData.get(31).compareTo("TEL RESPONSABLE") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 32 " + rowFileData.get(31), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 32 SE DEBE LLAMAR 'TEL RESPONSABLE'"));
        }
        if (rowFileData.get(32).compareTo("PARENTESCO") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 33 " + rowFileData.get(32), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 33 SE DEBE LLAMAR 'PARENTESCO'"));
        }
        if (rowFileData.get(33).compareTo("ACOMPAÑANTE") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 34 " + rowFileData.get(33), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 34 SE DEBE LLAMAR 'ACOMPAÑANTE'"));
        }
        if (rowFileData.get(34).compareTo("TEL ACOMPAÑANTE") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 35 " + rowFileData.get(34), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 35 SE DEBE LLAMAR 'TEL ACOMPAÑANTE'"));
        }
        if (rowFileData.get(35).compareTo("FECHA AFILIACION") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 36 " + rowFileData.get(35), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 36 SE DEBE LLAMAR 'FECHA AFILIACION'"));
        }
        if (rowFileData.get(36).compareTo("FECHA SISBEN") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 37 " + rowFileData.get(36), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 37 SE DEBE LLAMAR 'FECHA SISBEN'"));
        }
        if (rowFileData.get(37).compareTo("CARNET") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 38 " + rowFileData.get(37), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 38 SE DEBE LLAMAR 'CARNET'"));
        }
        if (rowFileData.get(38).compareTo("FECHA VENCE CARNET") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 39 " + rowFileData.get(38), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 39 SE DEBE LLAMAR 'FECHA VENCE CARNET'"));
        }
        if (rowFileData.get(39).compareTo("OBSERVACIONES") != 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), "0", "COLUMNA 40 " + rowFileData.get(39), "NOMBRE COLUMNA INCORRECTO", "LA COLUMNA 40 SE DEBE LLAMAR 'OBSERVACIONES'"));
        }
    }

    private CfgClasificaciones estaEnClasificacion(String numFila, String nombreColumna, String valor, String clasificacion) {
        CfgClasificaciones clasificacionBuscada;
        clasificacionBuscada = clasificacionesFachada.buscarPorMaestroDescripcion(clasificacion, valor);
        if (clasificacionBuscada == null) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), String.valueOf(numFila), nombreColumna, valor, "El valor no se encuentra dentro de la clasificación " + clasificacion));
            return null;
        }
        return clasificacionBuscada;
    }

    private String determinarCodigoDescripcion(CfgPacientes p, String var, String codigoDescripcion) {
        if (p == null) {
            return "";
        }
        switch (var) {
            case "TIPO IDENTIFICACION"://--------------------------------------------
                if (p.getTipoIdentificacion() != null) {
                    if (codigoDescripcion.compareTo("codigo") == 0) {
                        return p.getTipoIdentificacion().getCodigo();
                    } else {
                        return p.getTipoIdentificacion().getDescripcion();
                    }
                } else {
                    return "";
                }
            case "LUGAR DE EXPEDICION"://-------------------------------------------
                if (p.getLugarExpedicion() != null) {
                    return p.getLugarExpedicion();
                } else {
                    return "";
                }
            case "FECHA NACIMIENTO"://-------------------------------------------
                if (p.getFechaNacimiento() != null) {
                    return dateFormat.format(p.getFechaNacimiento());
                } else {
                    return "";
                }
            case "EDAD"://-------------------------------------------
                return p.getEdad();
            case "GENERO"://-------------------------------------------
                if (p.getGenero() != null) {
                    if (codigoDescripcion.compareTo("codigo") == 0) {
                        return p.getGenero().getCodigo();
                    } else {
                        return p.getGenero().getDescripcion();
                    }
                } else {
                    return "";
                }
            case "GRUPO SANGUINEO"://-------------------------------------------
                if (p.getGrupoSanguineo() != null) {
                    if (codigoDescripcion.compareTo("codigo") == 0) {
                        return p.getGrupoSanguineo().getCodigo();
                    } else {
                        return p.getGrupoSanguineo().getDescripcion();
                    }
                } else {
                    return "";
                }
            case "PRIMER NOMBRE"://-------------------------------------------
                if (p.getPrimerNombre() != null) {
                    return p.getPrimerNombre();
                } else {
                    return "";
                }
            case "SEGUNDO NOMBRE"://-------------------------------------------
                if (p.getSegundoNombre() != null) {
                    return p.getSegundoNombre();
                } else {
                    return "";
                }
            case "PRIMER APELLIDO"://-------------------------------------------
                if (p.getPrimerApellido() != null) {
                    return p.getPrimerApellido();
                } else {
                    return "";
                }
            case "SEGUNDO APELLIDO"://-------------------------------------------
                if (p.getSegundoApellido() != null) {
                    return p.getSegundoApellido();
                } else {
                    return "";
                }
            case "OCUPACION"://-------------------------------------------
                if (p.getOcupacion() != null) {
                    if (codigoDescripcion.compareTo("codigo") == 0) {
                        return p.getOcupacion().getCodigo();
                    } else {
                        return p.getOcupacion().getDescripcion();
                    }
                } else {
                    return "";
                }
            case "ESTADO CIVIL"://-------------------------------------------
                if (p.getEstadoCivil() != null) {
                    if (codigoDescripcion.compareTo("codigo") == 0) {
                        return p.getEstadoCivil().getCodigo();
                    } else {
                        return p.getEstadoCivil().getDescripcion();
                    }
                } else {
                    return "";
                }
            case "TEL RESIDENCIA"://-------------------------------------------
                if (p.getTelefonoResidencia() != null) {
                    return p.getTelefonoResidencia();
                } else {
                    return "";
                }
            case "TEL OFICINA"://-------------------------------------------
                if (p.getTelefonoOficina() != null) {
                    return p.getTelefonoOficina();
                } else {
                    return "";
                }
            case "CELULAR"://-------------------------------------------
                if (p.getCelular() != null) {
                    return p.getCelular();
                } else {
                    return "";
                }
            case "DEPARTAMENTO"://-------------------------------------------
                if (p.getDepartamento() != null) {
                    if (codigoDescripcion.compareTo("codigo") == 0) {
                        return p.getDepartamento().getCodigo();
                    } else {
                        return p.getDepartamento().getDescripcion();
                    }
                } else {
                    return "";
                }
            case "MUNICIPIO"://-------------------------------------------
                if (p.getMunicipio() != null) {
                    if (codigoDescripcion.compareTo("codigo") == 0) {
                        return p.getMunicipio().getCodigo();
                    } else {
                        return p.getMunicipio().getDescripcion();
                    }
                } else {
                    return "";
                }
            case "ZONA"://-------------------------------------------
                if (p.getZona() != null) {
                    if (codigoDescripcion.compareTo("codigo") == 0) {
                        return p.getZona().getCodigo();
                    } else {
                        return p.getZona().getDescripcion();
                    }
                } else {
                    return "";
                }
            case "DIRECCION"://-------------------------------------------
                if (p.getDireccion() != null) {
                    return p.getDireccion();
                } else {
                    return "";
                }
            case "BARRIO"://-------------------------------------------
                if (p.getBarrio() != null) {
                    return p.getBarrio();
                } else {
                    return "";
                }
            case "EMAIL"://-------------------------------------------
                if (p.getEmail() != null) {
                    return p.getEmail();
                } else {
                    return "";
                }
            case "ACTIVO"://-------------------------------------------
                if (p.getActivo() != null) {
                    if (p.getActivo()) {
                        return "ACTIVO";
                    } else {
                        return "INACTIVO";
                    }
                } else {
                    return "";
                }
            case "ADMINISTRADORA"://-------------------------------------------
                if (p.getIdAdministradora() != null) {
                    if (codigoDescripcion.compareTo("codigo") == 0) {
                        return p.getIdAdministradora().getCodigoAdministradora();
                    } else {
                        return p.getIdAdministradora().getRazonSocial();
                    }
                } else {
                    return "";
                }
            case "TIPO AFILIADO"://-------------------------------------------
                if (p.getTipoAfiliado() != null) {
                    if (codigoDescripcion.compareTo("codigo") == 0) {
                        return p.getTipoAfiliado().getCodigo();
                    } else {
                        return p.getTipoAfiliado().getDescripcion();
                    }
                } else {
                    return "";
                }
            case "REGIMEN"://-------------------------------------------
                if (p.getRegimen() != null) {
                    if (codigoDescripcion.compareTo("codigo") == 0) {
                        return p.getRegimen().getCodigo();
                    } else {
                        return p.getRegimen().getDescripcion();
                    }
                } else {
                    return "";
                }
            case "CATEGORIA PACIENTE"://-------------------------------------------
                if (p.getCategoriaPaciente() != null) {
                    if (codigoDescripcion.compareTo("codigo") == 0) {
                        return p.getCategoriaPaciente().getCodigo();
                    } else {
                        return p.getCategoriaPaciente().getDescripcion();
                    }
                } else {
                    return "";
                }
            case "NIVEL"://-------------------------------------------
                if (p.getNivel() != null) {
                    if (codigoDescripcion.compareTo("codigo") == 0) {
                        return p.getNivel().getCodigo();
                    } else {
                        return p.getNivel().getDescripcion();
                    }
                } else {
                    return "";
                }
            case "ETNIA"://-------------------------------------------
                if (p.getEtnia() != null) {
                    if (codigoDescripcion.compareTo("codigo") == 0) {
                        return p.getEtnia().getCodigo();
                    } else {
                        return p.getEtnia().getDescripcion();
                    }
                } else {
                    return "";
                }
            case "ESCOLARIDAD"://-------------------------------------------
                if (p.getEscolaridad() != null) {
                    if (codigoDescripcion.compareTo("codigo") == 0) {
                        return p.getEscolaridad().getCodigo();
                    } else {
                        return p.getEscolaridad().getDescripcion();
                    }
                } else {
                    return "";
                }
            case "RESPONSABLE"://-------------------------------------------
                if (p.getResponsable() != null) {
                    return p.getResponsable();
                } else {
                    return "";
                }
            case "TEL RESPONSABLE"://-------------------------------------------
                if (p.getTelefonoResponsable() != null) {
                    return p.getTelefonoResponsable();
                } else {
                    return "";
                }
            case "PARENTESCO"://-------------------------------------------
                if (p.getParentesco() != null) {
                    if (codigoDescripcion.compareTo("codigo") == 0) {
                        return p.getParentesco().getCodigo();
                    } else {
                        return p.getParentesco().getDescripcion();
                    }
                } else {
                    return "";
                }
            case "ACOMPAÑANTE"://-------------------------------------------
                if (p.getAcompanante() != null) {
                    return p.getAcompanante();
                } else {
                    return "";
                }
            case "TEL ACOMPAÑANTE"://-------------------------------------------
                if (p.getTelefonoAcompanante() != null) {
                    return p.getTelefonoAcompanante();
                } else {
                    return "";
                }
            case "FECHA AFILIACION"://-------------------------------------------
                if (p.getFechaAfiliacion() != null) {
                    return dateFormat.format(p.getFechaAfiliacion());
                } else {
                    return "";
                }
            case "FECHA SISBEN"://-------------------------------------------
                if (p.getFechaSisben() != null) {
                    return dateFormat.format(p.getFechaSisben());
                } else {
                    return "";
                }
            case "CARNET"://-------------------------------------------
                if (p.getCarnet() != null) {
                    return p.getCarnet();
                } else {
                    return "";
                }
            case "FECHA VENCE CARNET"://-------------------------------------------
                if (p.getFechaVenceCarnet() != null) {
                    return dateFormat.format(p.getFechaVenceCarnet());
                } else {
                    return "";
                }
            case "OBSERVACIONES"://-------------------------------------------
                if (p.getObservaciones() != null) {
                    return p.getObservaciones();
                } else {
                    return "";
                }
        }
        return "ERROR: valor indeterminado";
    }

    private void comprobarFila(ArrayList<String> rowFileData, int numFila) {//DETERMINA SI HAY ERRORES AL LEER UNA FILA
        //listaAcciones = numero_accion,tipo,identificacion,variable,codigo_antes,valor_antes,codigo_nuevo,valor_nuevo,descripcion
        //listaErrores = numero_error,fila,columna,valor,descripcion
        String tipoRegistro = "existente";
        CfgPacientes paciente;
        CfgClasificaciones clasificacionBuscada;
        String identificacion;

        //case 0: //"IDENTIFICACION"--------------------------------------------
        if (rowFileData.get(0).length() == 0) {
            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), String.valueOf(numFila), "IDENTIFICACION", "", "El campo identificación no puede estar vacio"));
        }
        if (listaErrores.isEmpty()) {
            identificacion = rowFileData.get(0);
            paciente = pacientesFachada.buscarPorIdentificacion(rowFileData.get(0));
            if (paciente == null) {
                tipoRegistro = "nuevo";
            }
            listaAcciones.add(new FilaDataTable(String.valueOf(numeroDeAccion++), tipoRegistro, identificacion, "IDENTIFICACION", "", identificacion, "", identificacion));//identificacion

            for (int i = 1; i < rowFileData.size(); i++) {
                switch (i) {
                    case 1: //"TIPO IDENTIFICACION"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            clasificacionBuscada = estaEnClasificacion(String.valueOf(numFila), "TIPO IDENTIFICACION", rowFileData.get(i), "TipoIdentificacion");
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "TIPO IDENTIFICACION", //columna 
                                        determinarCodigoDescripcion(paciente, "TIPO IDENTIFICACION", "codigo"),//codigo antiguo(si es existente)
                                        determinarCodigoDescripcion(paciente, "TIPO IDENTIFICACION", "descripcion"),//valor antiguo(si es existente)
                                        clasificacionBuscada.getCodigo(),//codigo nuevo
                                        clasificacionBuscada.getDescripcion()));//valor nuevo
                            }
                        }
                        break;
                    case 2: //"LUGAR DE EXPEDICION"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "LUGAR DE EXPEDICION", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "LUGAR DE EXPEDICION", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                    case 3: //"FECHA NACIMIENTO"--------------------------------------------
                        if (paciente == null) {//es un nuevo paciente
                            if (rowFileData.get(i) == null || rowFileData.get(i).length() == 0) {
                                listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), String.valueOf(numFila), "FECHA NACIMIENTO", rowFileData.get(i), "La fecha de nacimiento es obligatoria"));

                            } else if (esFecha(rowFileData.get(i), "yyyy-MM-dd") == null) {
                                listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), String.valueOf(numFila), "FECHA NACIMIENTO", rowFileData.get(i), "No se puede convertir al formato yyyy-MM-dd"));
                            }
                        }
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "FECHA NACIMIENTO", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "FECHA NACIMIENTO", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                    case 4: //"EDAD"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "EDAD", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "EDAD", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                    case 5: //"GENERO"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            clasificacionBuscada = estaEnClasificacion(String.valueOf(numFila), "GENERO", rowFileData.get(i), "Genero");
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "GENERO", //columna 
                                        determinarCodigoDescripcion(paciente, "GENERO", "codigo"),//codigo antiguo(si es existente)
                                        determinarCodigoDescripcion(paciente, "GENERO", "descripcion"),//valor antiguo(si es existente)
                                        clasificacionBuscada.getCodigo(),//codigo nuevo
                                        clasificacionBuscada.getDescripcion()));//valor nuevo
                            }
                        }
                        break;
                    case 6: //"GRUPO SANGUINEO"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            clasificacionBuscada = estaEnClasificacion(String.valueOf(numFila), "GRUPO SANGUINEO", rowFileData.get(i), "GrupoSanguineo");
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "GRUPO SANGUINEO", //columna 
                                        determinarCodigoDescripcion(paciente, "GRUPO SANGUINEO", "codigo"),//codigo antiguo(si es existente)
                                        determinarCodigoDescripcion(paciente, "GRUPO SANGUINEO", "descripcion"),//valor antiguo(si es existente)
                                        clasificacionBuscada.getCodigo(),//codigo nuevo
                                        clasificacionBuscada.getDescripcion()));//valor nuevo
                            }
                        }
                        break;
                    case 7: //"PRIMER NOMBRE"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "PRIMER NOMBRE", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "PRIMER NOMBRE", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                    case 8: //"SEGUNDO NOMBRE"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "SEGUNDO NOMBRE", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "SEGUNDO NOMBRE", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                    case 9: //"PRIMER APELLIDO"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "PRIMER APELLIDO", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "PRIMER APELLIDO", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                    case 10: //"SEGUNDO APELLIDO"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "SEGUNDO APELLIDO", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "SEGUNDO APELLIDO", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                    case 11: //"OCUPACION"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            clasificacionBuscada = estaEnClasificacion(String.valueOf(numFila), "OCUPACION", rowFileData.get(i), "Ocupacion");
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "OCUPACION", //columna 
                                        determinarCodigoDescripcion(paciente, "OCUPACION", "codigo"),//codigo antiguo(si es existente)
                                        determinarCodigoDescripcion(paciente, "OCUPACION", "descripcion"),//valor antiguo(si es existente)
                                        clasificacionBuscada.getCodigo(),//codigo nuevo
                                        clasificacionBuscada.getDescripcion()));//valor nuevo
                            }
                        }
                        break;
                    case 12: //"ESTADO CIVIL"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            clasificacionBuscada = estaEnClasificacion(String.valueOf(numFila), "ESTADO CIVIL", rowFileData.get(i), "EstadoCivil");
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "ESTADO CIVIL", //columna 
                                        determinarCodigoDescripcion(paciente, "ESTADO CIVIL", "codigo"),//codigo antiguo(si es existente)
                                        determinarCodigoDescripcion(paciente, "ESTADO CIVIL", "descripcion"),//valor antiguo(si es existente)
                                        clasificacionBuscada.getCodigo(),//codigo nuevo
                                        clasificacionBuscada.getDescripcion()));//valor nuevo
                            }
                        }
                        break;
                    case 13: //"TEL RESIDENCIA"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "TEL RESIDENCIA", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "TEL RESIDENCIA", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                    case 14: //"TEL OFICINA"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "TEL OFICINA", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "TEL OFICINA", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                    case 15: //"CELULAR"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "CELULAR", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "CELULAR", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                    case 16: //"DEPARTAMENTO"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            clasificacionBuscada = estaEnClasificacion(String.valueOf(numFila), "DEPARTAMENTO", rowFileData.get(i), "DPTO");
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "DEPARTAMENTO", //columna 
                                        determinarCodigoDescripcion(paciente, "DEPARTAMENTO", "codigo"),//codigo antiguo(si es existente)
                                        determinarCodigoDescripcion(paciente, "DEPARTAMENTO", "descripcion"),//valor antiguo(si es existente)
                                        clasificacionBuscada.getCodigo(),//codigo nuevo
                                        clasificacionBuscada.getDescripcion()));//valor nuevo
                            }
                        }
                        break;
                    case 17: //"MUNICIPIO"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            clasificacionBuscada = estaEnClasificacion(String.valueOf(numFila), "MUNICIPIO", rowFileData.get(i), "Municipios");
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "MUNICIPIO", //columna 
                                        determinarCodigoDescripcion(paciente, "MUNICIPIO", "codigo"),//codigo antiguo(si es existente)
                                        determinarCodigoDescripcion(paciente, "MUNICIPIO", "descripcion"),//valor antiguo(si es existente)
                                        clasificacionBuscada.getCodigo(),//codigo nuevo
                                        clasificacionBuscada.getDescripcion()));//valor nuevo
                            }
                        }
                        break;
                    case 18: //"ZONA"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            clasificacionBuscada = estaEnClasificacion(String.valueOf(numFila), "ZONA", rowFileData.get(i), "Zona");
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "ZONA", //columna 
                                        determinarCodigoDescripcion(paciente, "ZONA", "codigo"),//codigo antiguo(si es existente)
                                        determinarCodigoDescripcion(paciente, "ZONA", "descripcion"),//valor antiguo(si es existente)
                                        clasificacionBuscada.getCodigo(),//codigo nuevo
                                        clasificacionBuscada.getDescripcion()));//valor nuevo
                            }
                        }
                        break;
                    case 19: //"DIRECCION"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "DIRECCION", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "DIRECCION", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                    case 20: //"BARRIO"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "BARRIO", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "BARRIO", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                    case 21: //"EMAIL"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "EMAIL", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "EMAIL", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                    case 22: //"ACTIVO"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "ACTIVO", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "ACTIVO", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                    case 23: //"ADMINISTRADORA"--------------------------------------------
                        if (paciente == null) {//es un nuevo paciente
                            if (rowFileData.get(i) == null || rowFileData.get(i).length() == 0) {
                                listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), String.valueOf(numFila), "ADMINISTRADORA", rowFileData.get(i), "El campo administradora no puede estar vacio"));
                            } else {
                                if (administradoraFacade.buscarPorNombre(rowFileData.get(i)) == null) {
                                    listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), String.valueOf(numFila), "ADMINISTRADORA", rowFileData.get(i), "La administradora no se encuentra registrada en el sistema"));
                                }
                            }
                        }
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "ADMINISTRADORA", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "ADMINISTRADORA", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                    case 24: //"TIPO AFILIADO"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            clasificacionBuscada = estaEnClasificacion(String.valueOf(numFila), "TIPO AFILIADO", rowFileData.get(i), "TipoAfiliado");
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "TIPO AFILIADO", //columna 
                                        determinarCodigoDescripcion(paciente, "TIPO AFILIADO", "codigo"),//codigo antiguo(si es existente)
                                        determinarCodigoDescripcion(paciente, "TIPO AFILIADO", "descripcion"),//valor antiguo(si es existente)
                                        clasificacionBuscada.getCodigo(),//codigo nuevo
                                        clasificacionBuscada.getDescripcion()));//valor nuevo
                            }
                        }
                        break;
                    case 25: //"REGIMEN"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            clasificacionBuscada = estaEnClasificacion(String.valueOf(numFila), "REGIMEN", rowFileData.get(i), "Regimen");
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "REGIMEN", //columna 
                                        determinarCodigoDescripcion(paciente, "REGIMEN", "codigo"),//codigo antiguo(si es existente)
                                        determinarCodigoDescripcion(paciente, "REGIMEN", "descripcion"),//valor antiguo(si es existente)
                                        clasificacionBuscada.getCodigo(),//codigo nuevo
                                        clasificacionBuscada.getDescripcion()));//valor nuevo
                            }
                        }
                        break;
                    case 26: //"CATEGORIA PACIENTE"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            clasificacionBuscada = estaEnClasificacion(String.valueOf(numFila), "CATEGORIA PACIENTE", rowFileData.get(i), "CategoriaPaciente");
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "CATEGORIA PACIENTE", //columna 
                                        determinarCodigoDescripcion(paciente, "CATEGORIA PACIENTE", "codigo"),//codigo antiguo(si es existente)
                                        determinarCodigoDescripcion(paciente, "CATEGORIA PACIENTE", "descripcion"),//valor antiguo(si es existente)
                                        clasificacionBuscada.getCodigo(),//codigo nuevo
                                        clasificacionBuscada.getDescripcion()));//valor nuevo
                            }
                        }

                        break;
                    case 27: //"NIVEL"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            clasificacionBuscada = estaEnClasificacion(String.valueOf(numFila), "NIVEL", rowFileData.get(i), "Nivel");
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "NIVEL", //columna 
                                        determinarCodigoDescripcion(paciente, "NIVEL", "codigo"),//codigo antiguo(si es existente)
                                        determinarCodigoDescripcion(paciente, "NIVEL", "descripcion"),//valor antiguo(si es existente)
                                        clasificacionBuscada.getCodigo(),//codigo nuevo
                                        clasificacionBuscada.getDescripcion()));//valor nuevo
                            }
                        }
                        break;
                    case 28: //"ETNIA"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            clasificacionBuscada = estaEnClasificacion(String.valueOf(numFila), "ETNIA", rowFileData.get(i), "Etnia");
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "ETNIA", //columna 
                                        determinarCodigoDescripcion(paciente, "ETNIA", "codigo"),//codigo antiguo(si es existente)
                                        determinarCodigoDescripcion(paciente, "ETNIA", "descripcion"),//valor antiguo(si es existente)
                                        clasificacionBuscada.getCodigo(),//codigo nuevo
                                        clasificacionBuscada.getDescripcion()));//valor nuevo
                            }
                        }
                        break;
                    case 29: //"ESCOLARIDAD"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            clasificacionBuscada = estaEnClasificacion(String.valueOf(numFila), "ESCOLARIDAD", rowFileData.get(i), "Escolaridad");
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "ESCOLARIDAD", //columna 
                                        determinarCodigoDescripcion(paciente, "ESCOLARIDAD", "codigo"),//codigo antiguo(si es existente)
                                        determinarCodigoDescripcion(paciente, "ESCOLARIDAD", "descripcion"),//valor antiguo(si es existente)
                                        clasificacionBuscada.getCodigo(),//codigo nuevo
                                        clasificacionBuscada.getDescripcion()));//valor nuevo
                            }
                        }
                        break;
                    case 30: //"RESPONSABLE"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "RESPONSABLE", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "RESPONSABLE", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                    case 31: //"TEL RESPONSABLE"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "TEL RESPONSABLE", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "TEL RESPONSABLE", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                    case 32: //"PARENTESCO"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            clasificacionBuscada = estaEnClasificacion(String.valueOf(numFila), "PARENTESCO", rowFileData.get(i), "Parentesco");
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "PARENTESCO", //columna 
                                        determinarCodigoDescripcion(paciente, "PARENTESCO", "codigo"),//codigo antiguo(si es existente)
                                        determinarCodigoDescripcion(paciente, "PARENTESCO", "descripcion"),//valor antiguo(si es existente)
                                        clasificacionBuscada.getCodigo(),//codigo nuevo
                                        clasificacionBuscada.getDescripcion()));//valor nuevo
                            }
                        }
                        break;
                    case 33: //"ACOMPAÑANTE"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "ACOMPAÑANTE", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "ACOMPAÑANTE", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                    case 34: //"TEL ACOMPAÑANTE"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "TEL ACOMPAÑANTE", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "TEL ACOMPAÑANTE", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                    case 35: //"FECHA AFILIACION"--------------------------------------------
                        if (esFecha(rowFileData.get(i), "yyyy-MM-dd") == null) {
                            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), String.valueOf(numFila), "FECHA AFILIACION", rowFileData.get(i), "No se puede convertir al formato yyyy-MM-dd"));
                        }
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "FECHA AFILIACION", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "FECHA AFILIACION", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                    case 36: //"FECHA SISBEN"--------------------------------------------
                        if (esFecha(rowFileData.get(i), "yyyy-MM-dd") == null) {
                            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), String.valueOf(numFila), "FECHA SISBEN", rowFileData.get(i), "No se puede convertir al formato yyyy-MM-dd"));
                        }
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "FECHA SISBEN", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "FECHA SISBEN", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                    case 37: //"CARNET"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "CARNET", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "CARNET", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                    case 38: //"FECHA VENCE CARNET"--------------------------------------------
                        if (esFecha(rowFileData.get(i), "yyyy-MM-dd") == null) {
                            listaErrores.add(new FilaDataTable(String.valueOf(numeroDeError++), String.valueOf(numFila), "FECHA VENCE CARNET", rowFileData.get(i), "No se puede convertir al formato yyyy-MM-dd"));
                        }
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "FECHA VENCE CARNET", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "FECHA VENCE CARNET", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                    case 39: //"OBSERVACIONES"--------------------------------------------
                        if (rowFileData.get(i).length() != 0) {
                            if (listaErrores.isEmpty()) {//SI CLASIFICACION BUSCADA=NULL ES POR QUE HAY ERORES, SI NO HAY ERRORES SE DETERMINA LAS ACCIONES                            
                                listaAcciones.add(new FilaDataTable(
                                        String.valueOf(numeroDeAccion++),//numero de accion
                                        tipoRegistro, //existente o nuevo
                                        identificacion, //identificacion del paciente
                                        "OBSERVACIONES", //columna 
                                        "",//codigo antiguo(si es existente)//no tiene por que es texto
                                        determinarCodigoDescripcion(paciente, "OBSERVACIONES", "descripcion"),//valor antiguo(si es existente)
                                        "",//codigo nuevo//no tiene por que es texto
                                        rowFileData.get(i)));//valor nuevo
                            }
                        }
                        break;
                }
            }
        }
    }

    public void realizarImportacion() {//EFECTUAR LA IMPORTACION CUANDO EL ARCHIVO HA SIDO VALIDADO
        renderedTablaAcciones = false;
        renderedTablaErrores = false;
        if (listaAcciones == null || listaAcciones.isEmpty()) {
            imprimirMensaje("Advertencia", "No se encontraron acciones a realizar", FacesMessage.SEVERITY_WARN);
            return;
        }
        CfgPacientes pacienteImportar = new CfgPacientes();
        String estado = "inicio";//puede ser inicio(se espera identificacion) y cargando(se estan cargando los datos)
        String tipo = "";//puede ser nuevo o existente
        for (FilaDataTable accion : listaAcciones) {
            //numero_accion,tipo,identificacion,variable,codigo_antes,valor_antes,codigo_nuevo,valor_nuevo
            switch (accion.getColumna4()) {
                case "IDENTIFICACION"://-----------------------------------
                    if (estado.compareTo("inicio") == 0) {
                        tipo = accion.getColumna2();
                        estado = "cargando";
                    } else {//INDICA QUE SE ESTABA CARGANDO UN PACIENTE Y LLEGA UN NUEVO
                        if (tipo.compareTo("nuevo") == 0) {
                            pacientesFachada.create(pacienteImportar);
                        } else {
                            pacientesFachada.edit(pacienteImportar);
                        }
                        tipo = accion.getColumna2();
                    }
                    if (accion.getColumna2().compareTo("nuevo") == 0) {
                        pacienteImportar = new CfgPacientes();
                        pacienteImportar.setIdentificacion(accion.getColumna8());
                    } else {
                        pacienteImportar = pacientesFachada.buscarPorIdentificacion(accion.getColumna8());
                    }
                    break;
                case "TIPO IDENTIFICACION"://-----------------------------------
                    pacienteImportar.setTipoIdentificacion(clasificacionesFachada.buscarPorMaestroDescripcion("TipoIdentificacion", accion.getColumna8()));
                    break;
                case "LUGAR DE EXPEDICION"://-----------------------------------
                    pacienteImportar.setLugarExpedicion(accion.getColumna8());
                    break;
                case "FECHA NACIMIENTO": //--------------------------------------
                    try {
                        pacienteImportar.setFechaNacimiento(dateFormat.parse(accion.getColumna8()));
                    } catch (ParseException ex) {
                    }
                    break;
                case "EDAD"://--------------------------------------------------
                    break;
                case "GENERO"://------------------------------------------------
                    pacienteImportar.setGenero(clasificacionesFachada.buscarPorMaestroDescripcion("Genero", accion.getColumna8()));
                    break;
                case "GRUPO SANGUINEO"://---------------------------------------
                    pacienteImportar.setGrupoSanguineo(clasificacionesFachada.buscarPorMaestroDescripcion("GrupoSanguineo", accion.getColumna8()));
                    break;
                case "PRIMER NOMBRE"://-----------------------------------------
                    pacienteImportar.setPrimerNombre(accion.getColumna8());
                    break;
                case "SEGUNDO NOMBRE"://----------------------------------------
                    pacienteImportar.setSegundoNombre(accion.getColumna8());
                    break;
                case "PRIMER APELLIDO"://---------------------------------------
                    pacienteImportar.setPrimerApellido(accion.getColumna8());
                    break;
                case "SEGUNDO APELLIDO"://--------------------------------------
                    pacienteImportar.setSegundoApellido(accion.getColumna8());
                    break;
                case "OCUPACION"://---------------------------------------------
                    pacienteImportar.setOcupacion(clasificacionesFachada.buscarPorMaestroDescripcion("Ocupacion", accion.getColumna8()));
                    break;
                case "ESTADO CIVIL"://------------------------------------------
                    pacienteImportar.setEstadoCivil(clasificacionesFachada.buscarPorMaestroDescripcion("EstadoCivil", accion.getColumna8()));
                    break;
                case "TEL RESIDENCIA"://----------------------------------------
                    pacienteImportar.setTelefonoResidencia(accion.getColumna8());
                    break;
                case "TEL OFICINA"://-------------------------------------------
                    pacienteImportar.setTelefonoOficina(accion.getColumna8());
                    break;
                case "CELULAR"://-----------------------------------------------
                    pacienteImportar.setCelular(accion.getColumna8());
                    break;
                case "DEPARTAMENTO"://------------------------------------------
                    pacienteImportar.setDepartamento(clasificacionesFachada.buscarPorMaestroDescripcion("DPTO", accion.getColumna8()));
                    break;
                case "MUNICIPIO"://---------------------------------------------
                    pacienteImportar.setMunicipio(clasificacionesFachada.buscarPorMaestroDescripcion("Municipios", accion.getColumna8()));
                    break;
                case "ZONA"://--------------------------------------------------
                    pacienteImportar.setZona(clasificacionesFachada.buscarPorMaestroDescripcion("Zona", accion.getColumna8()));
                    break;
                case "DIRECCION"://---------------------------------------------
                    pacienteImportar.setDireccion(accion.getColumna8());
                    break;
                case "BARRIO"://------------------------------------------------
                    pacienteImportar.setBarrio(accion.getColumna8());
                    break;
                case "EMAIL"://-------------------------------------------------
                    pacienteImportar.setEmail(accion.getColumna8());
                    break;
                case "ACTIVO"://------------------------------------------------
                    pacienteImportar.setActivo(accion.getColumna8().compareTo("ACTIVO") == 0);
                    break;
                case "ADMINISTRADORA"://----------------------------------------
                    pacienteImportar.setIdAdministradora(administradoraFacade.buscarPorNombre(accion.getColumna8()));
                    break;
                case "TIPO AFILIADO"://-----------------------------------------
                    pacienteImportar.setTipoAfiliado(clasificacionesFachada.buscarPorMaestroDescripcion("TipoAfiliado", accion.getColumna8()));
                    break;
                case "REGIMEN"://-----------------------------------------------
                    pacienteImportar.setRegimen(clasificacionesFachada.buscarPorMaestroDescripcion("Regimen", accion.getColumna8()));
                    break;
                case "CATEGORIA PACIENTE"://------------------------------------
                    pacienteImportar.setCategoriaPaciente(clasificacionesFachada.buscarPorMaestroDescripcion("CategoriaPaciente", accion.getColumna8()));
                    break;
                case "NIVEL"://-------------------------------------------------
                    pacienteImportar.setNivel(clasificacionesFachada.buscarPorMaestroDescripcion("Estrato", accion.getColumna8()));
                    break;
                case "ETNIA"://-------------------------------------------------
                    pacienteImportar.setEtnia(clasificacionesFachada.buscarPorMaestroDescripcion("Etnia", accion.getColumna8()));
                    break;
                case "ESCOLARIDAD"://-------------------------------------------
                    pacienteImportar.setEscolaridad(clasificacionesFachada.buscarPorMaestroDescripcion("Escolaridad", accion.getColumna8()));
                    break;
                case "RESPONSABLE"://-------------------------------------------                    
                    pacienteImportar.setResponsable(accion.getColumna8());
                    break;
                case "TEL RESPONSABLE"://---------------------------------------
                    pacienteImportar.setTelefonoResponsable(accion.getColumna8());
                    break;
                case "PARENTESCO"://--------------------------------------------
                    pacienteImportar.setParentesco(clasificacionesFachada.buscarPorMaestroDescripcion("Parentesco", accion.getColumna8()));
                    break;
                case "ACOMPAÑANTE"://-------------------------------------------
                    pacienteImportar.setAcompanante(accion.getColumna8());
                    break;
                case "TEL ACOMPAÑANTE"://---------------------------------------
                    pacienteImportar.setTelefonoAcompanante(accion.getColumna8());
                    break;
                case "FECHA AFILIACION"://--------------------------------------
                    try {
                        pacienteImportar.setFechaAfiliacion(dateFormat.parse(accion.getColumna8()));
                    } catch (ParseException ex) {
                    }
                    break;
                case "FECHA SISBEN"://------------------------------------------
                    try {
                        pacienteImportar.setFechaSisben(dateFormat.parse(accion.getColumna8()));
                    } catch (ParseException ex) {
                    }
                    break;
                case "CARNET"://------------------------------------------------
                    pacienteImportar.setCarnet(accion.getColumna8());
                    break;
                case "FECHA VENCE CARNET"://------------------------------------
                    try {
                        pacienteImportar.setFechaVenceCarnet(dateFormat.parse(accion.getColumna8()));
                    } catch (ParseException ex) {
                    }
                    break;
                case "OBSERVACIONES"://-----------------------------------------
                    pacienteImportar.setObservaciones(accion.getColumna8());
                    break;
            }
        }
        if (tipo.compareTo("nuevo") == 0) {
            pacientesFachada.create(pacienteImportar);
        } else {
            pacientesFachada.edit(pacienteImportar);
        }
        imprimirMensaje("Correcto", "Importación realizada", FacesMessage.SEVERITY_INFO);
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------
    public ArrayList<FilaDataTable> getListaErrores() {
        return listaErrores;
    }

    public void setListaErrores(ArrayList<FilaDataTable> listaErrores) {
        this.listaErrores = listaErrores;
    }

    public boolean isRenderedTablaErrores() {
        return renderedTablaErrores;
    }

    public void setRenderedTablaErrores(boolean renderedTablaErrores) {
        this.renderedTablaErrores = renderedTablaErrores;
    }

    public boolean isRenderedTablaAcciones() {
        return renderedTablaAcciones;
    }

    public void setRenderedTablaAcciones(boolean renderedTablaAcciones) {
        this.renderedTablaAcciones = renderedTablaAcciones;
    }

    public ArrayList<FilaDataTable> getListaAcciones() {
        return listaAcciones;
    }

    public void setListaAcciones(ArrayList<FilaDataTable> listaAcciones) {
        this.listaAcciones = listaAcciones;
    }

}
