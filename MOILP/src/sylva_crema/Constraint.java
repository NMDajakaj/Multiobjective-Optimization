package sylva_crema;

import java.util.ArrayList;

public class Constraint {
	private ArrayList<String> vars;
	private ArrayList<Integer> coefs;
	private String type;
	private int value;
	
	public Constraint (String constraint) {
		vars = new ArrayList<String> ();
		coefs = new ArrayList<Integer> ();
		
		String[] tokens = constraint.split ("\\s");
		for (int i = 0; i < tokens.length; i++) {
			while (tokens[i].length() == 0) {
				i++;
			}
			if (tokens[i].matches("<|<=|=|>=|>")) {
				type = tokens[i];
				value = new Integer (tokens[++i]);
			} else {
			
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
	}
	
	public String toString () {
		String aux = "";
		for (int i = 0; i < vars.size (); i++) {
			aux += coefs.get (i) + " " + vars.get (i) + " ";
		}
		aux += type + " " + value;
		return aux;
	}
}
