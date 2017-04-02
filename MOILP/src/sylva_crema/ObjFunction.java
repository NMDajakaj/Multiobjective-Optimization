package sylva_crema;

import java.util.ArrayList;

public class ObjFunction {
	private ArrayList<String> vars;			// Variables que intervienen en la función objetivo, nombre de las variables
	private ArrayList<Integer> coefs;		// Coeficientes de las variables guardadas en "vars" para esta función objetivo
	
	/*
	 * El constructor recibe la función objetivo como una cadena de caracteres y la descompone para 
	 * guardar las variables y sus coeficientes
	 */
	public ObjFunction (String function) {
		vars = new ArrayList<String> ();
		coefs = new ArrayList<Integer> ();
		
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
			if (tokens[i].matches("[0-9]*")) {
				aux += tokens[i];
			} else {
				aux += "1";
				i--;
			}
			
			// guardamos el nombre de la variable
			vars.add (tokens[++i]);
			coefs.add (new Integer (aux));
		}		
	}
	
	public String toString () {
		String string = "";
		for (int i = 0; i < vars.size (); i++) {
			string += coefs.get (i) + " " + vars.get (i) + " ";
		}
		return string;
	}
}
