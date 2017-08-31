
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
public class MainLab2{

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
        //System.out.println("Cadena extendida postfix:  " + regexPFestendida);

        long time_start, time_end;
        time_start = System.nanoTime();

        for (int x=0;x<regexpPF.length();x++){
            String caracter = String.valueOf(regexpPF.charAt(x));
            if(caracter.equals("*") || caracter.equals("|") || caracter.equals("?") || caracter.equals(".") || caracter.equals("+")){
                if(caracter.equals(".")){
                    //Hacer aqui la concatenacion
                    if(miStack.size()>=2){
                        Automata automataB=miStack.pop();
                        Automata automataA=miStack.pop();
                        Automata automataAB = operacion.concatenacion(automataA, automataB);
                        miStack.push(automataAB);
                    }else{
                        System.out.println("La cadena ingresada no es una regex.");
                        System.exit(0);
                    }
                }
                if(caracter.equals("|")){
                    //OR
                    if(miStack.size()>=2){
                        Automata automataB=miStack.pop();
                        Automata automataA=miStack.pop();
                        Automata automataAorB=operacion.or(automataA, automataB);
                        miStack.push(automataAorB);
                    }else{
                        System.out.println("La cadena ingresada no es una regex.");
                        System.exit(0);
                    }       
                }
                if(caracter.equals("*")){
                    //Kleene
                    if(miStack.size()>=1) {
                        Automata automataA = miStack.pop();
                        Automata automataK = operacion.kleene(automataA);
                        miStack.push(automataK);
                    }else{
                        System.out.println("La cadena ingresada no es una regex.");
                        System.exit(0);
                    }
                }
                if(caracter.equals("?")){
                    //Abreviatura ?
                    if(miStack.size()>=1) {
                        Automata X = miStack.pop();
                        Automata e = new Automata("$");
                        Automata automataOrEpsilon = operacion.or(X, e);
                        miStack.push(automataOrEpsilon);
                    }else{
                        System.out.println("La cadena ingresada no es una regex.");
                        System.exit(0);
                    }

                }
                if(caracter.equals("+")){
                    //Cerradura Positiva
                    if(miStack.size()>=1) {
                        Automata a = miStack.pop();
                        Automata automataCerradura = operacion.kleenemas(a);
                        miStack.push(automataCerradura);
                    }else{
                        System.out.println("La cadena ingresada no es una regex.");
                        System.exit(0);
                    }
                }
            }else{
                //Ciclo if que verifica si el ArrayList del alfabeto ya contiene el caractér
                if(!alfabeto.contains(caracter) && !caracter.equals("$") && !caracter.equals("")){
                    alfabeto.add(caracter);
                }
                //Creando el automata básico
                Automata elAutomata = new Automata(caracter);
                miStack.push(elAutomata);
            }
        }

        time_end = System.nanoTime();
        double delta1 = time_end - time_start;
        delta1 = delta1 / 1000000;
        System.out.println("\nEl AFN fue creado en: "+ delta1 +" milisegundos'");

        Automata elAutomatota = miStack.pop();

        //Creando un ArrayList que contiene todos
        operacion.getArrayNodos(elAutomatota.getNodoInicial());

        //Nombrando a los nodos
        operacion.nombrarNodos();
        elAutomatota.getNodoFinal().setEsFinal(true);


/* *************************************** Conversion de AFN a AFD ****************************************************/

        long time_star, time_en;
        time_star = System.nanoTime();

        AutomataDFA DFA  = new AutomataDFA();
        operacion.subsetConstruction(elAutomatota.getNodoInicial(), alfabeto, DFA);

        time_en = System.nanoTime();
        delta1 = time_en - time_star;
        delta1 = delta1 / 1000000;
        System.out.println("El AFD fue creado en: "+ delta1 +" milisegundos'");

        //Nombrando los nodos del AFD
        operacion.nombrarNodosDFA(DFA);


/* ****************************************Construccion directa del AFD**************************************/

        long time_sta, time_e;
        time_sta = System.nanoTime();
        //Obteniendo la hoja final del arbol sintactico
        Hoja n = operacion.generarArbolSintactico(regexPFestendida);

        AutomataDFA cd = new AutomataDFA();
        operacion.construccionDirecta(cd, n, alfabeto);

        time_e = System.nanoTime();
        delta1 = time_e - time_sta;
        delta1 = delta1 / 1000000;
        System.out.println("El AFD directo fue creado en: " + delta1 + " milisegundos.");
        operacion.nombrarNodos(cd);
        String c = operacion.descripcionAFDdirecto(cd, alfabeto);


/* ******************************************GENERACION DE ARCHIVOS***************************************************************/
        BufferedWriter bw = null;
        FileWriter fw = null;


        try {

            PrintWriter writer = new PrintWriter("Descripcion AFN.txt");
            writer.println("AFN:\nLista de nodos = "+operacion.listadoDeNodos()
                    + "\nAlfabeto = "+ operacion.alfabeto(alfabeto)+"\nInicio = "
                    + elAutomatota.getNodoInicial().getNumeroEstado() + "\nAceptacion = " + elAutomatota.getNodoFinal().getNumeroEstado()
                    + "\nTransiciones: " + operacion.transiciones());
            writer.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        BufferedWriter bw1 = null;
        FileWriter fw1 = null;


        try {

            PrintWriter writer = new PrintWriter("Descripcion AFD directo.txt");
            writer.println(c);
            writer.close();

        } catch (Exception e) {

            e.printStackTrace();

        }


        BufferedWriter bw2 = null;
        FileWriter fw2 = null;


        try {

            PrintWriter writer = new PrintWriter("Descripcion AFN-AFD.txt");
            writer.println("AFD:\n"+operacion.descripcionAFD(DFA, alfabeto));
            writer.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        System.out.println("\nIngrese la cadena que desea simular en el AFN: ");
        cadenaSimulacion=scanner.nextLine();
        long time_i, time_f;
        time_i=System.nanoTime();
        operacion.simulacionAFN(elAutomatota, cadenaSimulacion);
        time_f=System.nanoTime();
        delta1 = time_f - time_i;
        delta1 = delta1 / 1000000;
        System.out.println("La simulación duró: " + delta1 + " milisegundos.");

        System.out.println("\nIngrese la cadena que desea simular en el AFD: ");
        cadenaSimulacion=scanner.nextLine();
        long time_in, time_fi;
        time_in=System.nanoTime();
        operacion.simulacionAFD(DFA, cadenaSimulacion);
        time_fi=System.nanoTime();
        delta1 = time_fi - time_in;
        delta1 = delta1 / 1000000;
        System.out.println("La simulación duró: " + delta1 + " milisegundos.");

        System.out.println("\nIngrese la cadena que desea simular en el AFD directo: ");
        cadenaSimulacion=scanner.nextLine();
        long time_ini, time_fin;
        time_ini=System.nanoTime();
        operacion.simulacionAFDdirecto(cd, cadenaSimulacion);
        time_fin=System.nanoTime();
        delta1 = time_fin - time_ini;
        delta1 = delta1 / 1000000;
        System.out.println("La simulación duró: " + delta1 + " milisegundos.");

        operacion.minimizar(DFA, alfabeto);

    }
}
