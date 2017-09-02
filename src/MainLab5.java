
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

/* ****************************************Construccion directa del AFD**************************************/

        long time_sta, time_e;
        time_sta = System.nanoTime();
        //Obteniendo la hoja final del arbol sintactico
        Hoja n = operacion.generarArbolSintactico(regexPFestendida);

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


/* ******************************************GENERACION DE ARCHIVOS***************************************************************/

        BufferedWriter bw1 = null;
        FileWriter fw1 = null;

        try {

            PrintWriter writer = new PrintWriter("Descripcion AFD directo.txt");
            writer.println(c);
            writer.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

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
