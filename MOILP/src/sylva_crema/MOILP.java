package sylva_crema;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MOILP {
	private ArrayList<ObjFunction> functions;
	private ArrayList<Constraint> constraints;
	private ArrayList<String> variables;
	
	/*
	 * El constructor recibe el nombre del fichero que contiene el MOILP, para poder leerlo y 
	 * guardar las funciones, variables y restricciones
	 */
	
	public MOILP (String file) throws IOException {
		functions = new ArrayList<ObjFunction> ();
		constraints = new ArrayList<Constraint> ();
		variables = new ArrayList<String> ();
		
		FileReader f = new FileReader (file);
		BufferedReader b = new BufferedReader (f);
		
		b.readLine ();				// eliminamos la primera fila del fichero: "Maximize"
		
		// guardamos las funciones objetivo
		boolean nextF = true;
		String aux;
		while (nextF) {
			aux = b.readLine ();
			if (!aux.equals ("Subject To")) {
				functions.add (new ObjFunction (aux));
			} else {				// cuando no hay más funciones se para el bucle y no se pasa la línea Subject To
				nextF = false;		// al constructor de las funciones objetivas
			}
		}
		
		// guardamos las restricciones
		boolean nextC = true;
		while (nextC) {
			aux = b.readLine ();
			if (!aux.equals ("Integers")) {
				constraints.add (new Constraint (aux));
			} else {
				nextC = false;
			}
		}
		
		aux = b.readLine ();
		String[] tokens = aux.split ("\\s");
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].length () != 0) {
				variables.add(tokens[i]);
			}
		}
		
		b.close ();
	}
	
	public ArrayList<String> getVars () {
		return variables;
	}
	
	public ArrayList<Constraint> getConstraints () {
		return constraints;
	}
}
