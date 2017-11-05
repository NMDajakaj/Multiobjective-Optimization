package common;

import java.util.ArrayList;

public class ObjFunction {
	private ArrayList<String> vars;			// Variables que intervienen en la función objetivo, nombre de las variables
	private ArrayList<Double> coefs;		// Coeficientes de las variables guardadas en "vars" para esta función objetivo
	private Boolean max;					// indica si la función es de maximizar o minimizar
	
	/*
	 * El constructor recibe la función objetivo como una cadena de caracteres y la descompone para 
	 * guardar las variables y sus coeficientes
	 */
	public ObjFunction (String function, String type) {
		max = type.equals("Maximize");
		vars = new ArrayList<String> ();
		coefs = new ArrayList<Double> ();
		
		String[] tokens = function.split ("\\s");
		for (int i = 0; i < tokens.length; i++) {
			while (tokens[i].length() == 0) {
				i++;
			}
			
			// guardamos el signo de la variable
			String aux = "";
			if (tokens[i].matches("[+-]")) {
				aux += tokens[i];
				i++;
			}
			
			// añadimos el coeficiente de la variable, si no hay nada pone un 1
			if (tokens[i].matches("[0-9]*(\\.[0-9E]*)?")) {
				aux += tokens[i];
			} else {
				aux += "1";
				i--;
			}
			
			// guardamos el nombre de la variable
			vars.add (tokens[++i]);
			coefs.add (new Double (aux));
		}		
	}
	
	public ObjFunction (ArrayList<String> v, ArrayList<Double> c) {
		vars = v;
		coefs = c;
	}
	
	public int calculateObjValue (double[] values, ArrayList<Variable> v) {
		int result = 0;
		ArrayList<String> varNames = new ArrayList<String> ();
		for (int i = 0; i < v.size (); i++) {
			varNames.add (v.get (i).getName ());
		}
		for (int i = 0; i < vars.size (); i++) {
			int index = varNames.indexOf (vars.get (i));
			result += coefs.get (i) * values[index];
		}
		return result;
	}
	
	public ArrayList<String> getVars () {
		return vars;
	}
	
	public ArrayList<Double> getCoefs () {
		return coefs;
	}
	
	public String toString () {
		String string = "";
		for (int i = 0; i < vars.size (); i++) {
			if (coefs.get (i) < 0) {
				string += "- " + (- coefs.get (i));
			} else {
				string += "+ " + coefs.get (i);
			}
			string += " " + vars.get (i) + " ";
		}
		return string;
	}
	
	public ObjFunction changeSymbol () {
		ArrayList<Double> changedCoefs = new ArrayList<Double> ();
		for (int i = 0; i < coefs.size (); i++) {
			changedCoefs.add (- coefs.get (i));
		}
		
		return new ObjFunction (vars, changedCoefs);
	}
	
	public Boolean isMax () {
		return max;
	}
}
