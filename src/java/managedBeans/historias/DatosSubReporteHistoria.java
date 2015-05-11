/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.historias;

/**
 *
 * @author santos
 */
public class DatosSubReporteHistoria {

    private String dato0 = "";
    private String dato1 = "";
    private String dato2 = "";
    private String dato3 = "";
    private String dato4 = "";
    private String dato5 = "";
    private String dato6 = "";
    private String dato7 = "";
    private String dato8 = "";
    private String dato9 = "";
    private String dato10 = "";

    public DatosSubReporteHistoria(String dato0, String dato1) {
        this.dato0 = dato0;
        this.dato1 = dato1;
    }

    public void setValor(int pos, String valor) {
        switch (pos) {
            case 0:
                dato0 = valor;
                break;
            case 1:
                dato1 = valor;
                break;
            case 2:
                dato2 = valor;
                break;
            case 3:
                dato3 = valor;
                break;
            case 4:
                dato4 = valor;
                break;
            case 5:
                dato5 = valor;
                break;
            case 6:
                dato6 = valor;
                break;
            case 7:
                dato7 = valor;
                break;
            case 8:
                dato8 = valor;
                break;
            case 9:
                dato9 = valor;
                break;
            case 10:
                dato10 = valor;
                break;
        }
    }

    public String getValor(int pos) {
        switch (pos) {
            case 0:
                return dato0;
            case 1:
                return dato1;
            case 2:
                return dato2;
            case 3:
                return dato3;
            case 4:
                return dato4;
            case 5:
                return dato5;
            case 6:
                return dato6;
            case 7:
                return dato7;
            case 8:
                return dato8;
            case 9:
                return dato9;
            case 10:
                return dato10;

        }
        return null;
    }

    public void limpiar() {
        dato0 = "";
        dato1 = "";
        dato2 = "";
        dato3 = "";
        dato4 = "";
        dato5 = "";
        dato6 = "";
        dato7 = "";
        dato8 = "";
        dato9 = "";
        dato10 = "";
    }

    public String getDato0() {
        return dato0;
    }

    public void setDato0(String dato0) {
        this.dato0 = dato0;
    }

    public String getDato1() {
        return dato1;
    }

    public void setDato1(String dato1) {
        this.dato1 = dato1;
    }

    public String getDato2() {
        return dato2;
    }

    public void setDato2(String dato2) {
        this.dato2 = dato2;
    }

    public String getDato3() {
        return dato3;
    }

    public void setDato3(String dato3) {
        this.dato3 = dato3;
    }

    public String getDato4() {
        return dato4;
    }

    public void setDato4(String dato4) {
        this.dato4 = dato4;
    }

    public String getDato5() {
        return dato5;
    }

    public void setDato5(String dato5) {
        this.dato5 = dato5;
    }

    public String getDato6() {
        return dato6;
    }

    public void setDato6(String dato6) {
        this.dato6 = dato6;
    }

    public String getDato7() {
        return dato7;
    }

    public void setDato7(String dato7) {
        this.dato7 = dato7;
    }

    public String getDato8() {
        return dato8;
    }

    public void setDato8(String dato8) {
        this.dato8 = dato8;
    }

    public String getDato9() {
        return dato9;
    }

    public void setDato9(String dato9) {
        this.dato9 = dato9;
    }

    public String getDato10() {
        return dato10;
    }

    public void setDato10(String dato10) {
        this.dato10 = dato10;
    }

}
