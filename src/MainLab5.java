
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
        Scanner scanner = new Scanner(System.in);
        Operaciones operacion = new Operaciones();
        RegExConverter sC = new RegExConverter();
        CocolStructure structure = new CocolStructure();
        String regexp;
        String regexpPF;
        String cadenaExtendida;
        String regexPFestendida;
        String cadenaSimulacion;
        Stack<Automata> miStack = new Stack<>();
        ArrayList<String> alfabeto = new ArrayList<>();

        //Solicitando al usuario que ingrese la expresion regular
        System.out.println("Ingrese la expresion regular que desee: ");
        regexp=scanner.nextLine();

        //Conversion de la cadena a notacion Postfix 
        regexpPF = sC.infixToPostfix(regexp);

        //Creando cadena Extendida para la generación directa de AFD's
        cadenaExtendida="("+regexp+")#";
        regexPFestendida=sC.infixToPostfix(cadenaExtendida);

/* ***********************************Construccion del automata de ident************************************************/

        //Creando la cadena que se ingresara para crear el automata de ident
        String cadena = structure.getLetter()+"("+structure.getLetter()+"|"+structure.getDigit()+")*";

        //Creando cadena Extendida para la generación directa de AFD's y convirtiendola a formato Postfix
        cadenaExtendida="("+cadena+")#";
        regexPFestendida=sC.infixToPostfix(cadenaExtendida);

        //Obteniedno la hoja final del arbol sintactico
        Hoja n = operacion.generarArbolSintactico(regexPFestendida);

        //Creando el automata
        AutomataDFA ident = new AutomataDFA();
        ArrayList<String> alfabetoIdent = operacion.generateAlphabet(cadena);
        operacion.construccionDirecta(ident, n, alfabetoIdent);
        operacion.nombrarNodos(ident);

        //String c = operacion.descripcionAFDdirecto(cd, alfabeto);

/* ***********************************Construccion del automata de number***********************************************/

        //Creando la cadena que se ingresara para crear el automata de number
        cadena = structure.getDigit()+"("+structure.getDigit()+")*";

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

        //String c = operacion.descripcionAFDdirecto(cd, alfabeto);


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

        //String c = operacion.descripcionAFDdirecto(cd, alfabeto);

/* ******************************************LEYENDO EL ARCHIVO TXT*****************************************************/

        String path = System.getProperty("user.dir") + "\\cocol.txt";
        ArrayList<String> fileContent = operacion.fileReader(path);
        //System.out.println("Yo soy el contenido del archivo de texto: \n"+fileContent);

/* ******************************************RECORRIENDO EL ARCHIVO*****************************************************/

        operacion.Errors(operacion.recorrido(fileContent, ident, number, string, charr));

/* ****************************************Construccion directa del AFD**************************************/

        long time_sta, time_e;
        time_sta = System.nanoTime();
        //Obteniendo la hoja final del arbol sintactico
        n = operacion.generarArbolSintactico(regexPFestendida);

        AutomataDFA cd = new AutomataDFA();
        ArrayList<String> alfabetoDirecto = operacion.generateAlphabet(regexpPF);
        operacion.construccionDirecta(cd, n, alfabetoDirecto);

        time_e = System.nanoTime();
        double delta1;
        delta1 = time_e - time_sta;
        delta1 = delta1 / 1000000;
        System.out.println("El AFD directo fue creado en: " + delta1 + " milisegundos.");
        operacion.nombrarNodos(cd);
        String c = operacion.descripcionAFDdirecto(cd, alfabeto);



/* ******************************************GENERACION DE ARCHIVOS*****************************************************/
/*
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {

            PrintWriter writer = new PrintWriter("Descripcion AFD directo.txt");
            writer.println(c);
            writer.close();

        } catch (Exception e) {

            e.printStackTrace();

        }*/

        System.out.println("\nIngrese la cadena que desea simular en el AFD directo: ");
        cadenaSimulacion=scanner.nextLine();
        long time_ini, time_fin;
        time_ini=System.nanoTime();
        operacion.simulacionAFDdirecto(cd, cadenaSimulacion);
        time_fin=System.nanoTime();
        delta1 = time_fin - time_ini;
        delta1 = delta1 / 1000000;
        System.out.println("La simulación duró: " + delta1 + " milisegundos.");


    }
}
