
import java.text.DecimalFormat;
import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.*;
/*
 * Universdidad del Valle de Guatemala
 * Diseño de lenguajes de programacion
 * Compiladores
 *
 * @author Sebastian Galindo, Carnet: 15452
 */
public class MainLab5{

    public static void main(String[] args) {
        Operaciones operacion = new Operaciones();
        RegExConverter sC = new RegExConverter();
        CocolStructure structure = new CocolStructure();
        String regexp="";
        String regexpPF;
        String cadenaExtendida;
        String regexPFestendida;

/* ***********************************Construccion del automata de ident************************************************/

        //Creando la cadena que se ingresara para crear el automata de ident
        regexp = "("+structure.getLetter()+")("+structure.getLetter()+"|"+structure.getDigit()+")*";
        System.out.println(regexp);

        //Creando cadena Extendida para la generación directa de AFD's y convirtiendola a formato Postfix
        regexpPF = sC.infixToPostfix(regexp);
        cadenaExtendida="("+regexp+")#";
        regexPFestendida=sC.infixToPostfix(cadenaExtendida);

        //Obteniedno la hoja final del arbol sintactico
        Hoja n = operacion.generarArbolSintactico(regexPFestendida);

        //Creando el automata
        AutomataDFA ident = new AutomataDFA();
        ArrayList<String> alfabetoIdent = operacion.generateAlphabet(regexpPF);
        operacion.construccionDirecta(ident, n, alfabetoIdent);
        operacion.nombrarNodos(ident);


/* ***********************************Construccion del automata de number***********************************************/

        //Creando la cadena que se ingresara para crear el automata de number
        String cadena = structure.getDigit()+"("+structure.getDigit()+")*";


        //Creando cadena Extendida para la generación directa de AFD's y convirtiendola a formato Postfix
        cadenaExtendida="("+cadena+")#";
        regexPFestendida=sC.infixToPostfix(cadenaExtendida);

        //Obteniedno la hoja final del arbol sintactico
        n = operacion.generarArbolSintactico(regexPFestendida);

        //Creando el automata
        AutomataDFA number = new AutomataDFA();
        //Obteniendo el alfabeto de la cadena
        ArrayList<String> alfabetoNumber = operacion.generateAlphabet(cadena);
        operacion.construccionDirecta(number, n, alfabetoNumber);
        operacion.nombrarNodos(number);

        //String c = operacion.descripcionAFDdirecto(cd, alfabeto);

/* ****************************************Construccion del automata de String******************************************/

        //Creando la cadena que se ingresara para crear el automata de ident
        cadena = "\""+structure.getAnyButQuote()+"\"";

        //Creando cadena Extendida para la generación directa de AFD's y convirtiendola a formato Postfix
        cadenaExtendida="("+cadena+")#";
        regexPFestendida=sC.infixToPostfix(cadenaExtendida);

        //Obteniedno la hoja final del arbol sintactico
        n = operacion.generarArbolSintactico(regexPFestendida);

        //Creando el automata
        AutomataDFA string = new AutomataDFA();
        //Obteniendo el alfabeto de la cadena
        ArrayList<String> alfabetoString = operacion.generateAlphabet(cadena);
        operacion.construccionDirecta(string, n, alfabetoString);
        operacion.nombrarNodos(string);

/* ****************************************Construccion del automata de char************************************/

        //Creando la cadena que se ingresara para crear el automata de ident
        cadena = "\\"+structure.getAnyButApostrophe()+"\\";

        //Creando cadena Extendida para la generación directa de AFD's y convirtiendola a formato Postfix
        cadenaExtendida="("+cadena+")#";
        regexPFestendida=sC.infixToPostfix(cadenaExtendida);

        //Obteniedno la hoja final del arbol sintactico
        n = operacion.generarArbolSintactico(regexPFestendida);

        //Creando el automata
        AutomataDFA charr = new AutomataDFA();
        //Obteniendo el alfabeto de la cadena
        ArrayList<String> alfabetoCharr = operacion.generateAlphabet(cadena);
        operacion.construccionDirecta(charr, n, alfabetoCharr);
        operacion.nombrarNodos(charr);

/* ******************************************LEYENDO EL ARCHIVO TXT*****************************************************/

        String path = System.getProperty("user.dir") + "\\cocol.txt";
        ArrayList<String> fileContent = operacion.fileReader(path);

/* ******************************************RECORRIENDO EL ARCHIVO*****************************************************/

        operacion.Errors(operacion.recorrido(fileContent, ident, number, string, charr));


    }
}
