
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
public class
MainLab5{

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
        regexp = structure.getIdent();

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
        operacion.estadoFinal(ident);



/* ***********************************Construccion del automata de Set***********************************************/

        //Creando la cadena que se ingresara para crear el automata de number
        regexp = structure.getSet();
        System.out.println(regexp);


        //Creando cadena Extendida para la generación directa de AFD's y convirtiendola a formato Postfix
        regexpPF = sC.infixToPostfix(regexp);
        cadenaExtendida="("+regexp+")#";
        regexPFestendida=sC.infixToPostfix(cadenaExtendida);

        //Obteniedno la hoja final del arbol sintactico
        n = operacion.generarArbolSintactico(regexPFestendida);

        //Creando el automata
        AutomataDFA set = new AutomataDFA();
        ArrayList<String> alfabetoSet = operacion.generateAlphabet(regexpPF);
        operacion.construccionDirecta(set, n, alfabetoSet);
        operacion.nombrarNodos(set);
        operacion.estadoFinal(set);

        //String c = operacion.descripcionAFDdirecto(cd, alfabeto);

/* ****************************************Construccion del automata de String******************************************/

        //Creando la cadena que se ingresara para crear el automata de number
        regexp = structure.getString();
        System.out.println(regexp);


        //Creando cadena Extendida para la generación directa de AFD's y convirtiendola a formato Postfix
        regexpPF = sC.infixToPostfix(regexp);
        cadenaExtendida="("+regexp+")#";
        regexPFestendida=sC.infixToPostfix(cadenaExtendida);

        //Obteniedno la hoja final del arbol sintactico
        n = operacion.generarArbolSintactico(regexPFestendida);

        //Creando el automata
        AutomataDFA string = new AutomataDFA();
        ArrayList<String> alfabetoString = operacion.generateAlphabet(regexpPF);
        operacion.construccionDirecta(string, n, alfabetoString);
        operacion.nombrarNodos(string);
        operacion.estadoFinal(string);

/* ****************************************Construccion del automata de char************************************/
/*
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

        operacion.Errors(operacion.recorrido(fileContent, ident, set,string));
        //operacion.Errors(operacion.recorrido(fileContent, ident, number, string, charr));


    }
}
