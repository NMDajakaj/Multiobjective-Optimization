package sylva_crema;

import java.util.ArrayList;

/* 
 * Algoritmo para generar la región eficiente de un problema de optimización multiobjetivo lineal entero (MOILP)
 * Algoritmo para generar los puntos de un MOILP de Sylva y Crema
 * Tendrá los métodos necesarios para implementar el algoritmo y almacenará el MOILP y los resultados
 * Natalie Dajakaj, Trabajo Final de Grado (TFG), Cuarto del Grado de Ingeniería Informática, curso 2016-2017
 * */

public class SylvaCrema {
	private MOILP moilp;
	//private int[] weight;		// vector de pesos
	
	public SylvaCrema (MOILP m) {
		moilp = m;
	}
	
	public void solve () {
		solveP0 ();
	}
	
	public void solveP0 () {
		LP p0 = new LP ();
		String function = "";
		ArrayList<String> vars = moilp.getVars ();
		for (int i = 0; i < vars.size () - 1; i++) {
			function += vars.get (i) + " + ";
		}
		function += vars.get (vars.size () - 1);
		
		p0.setFunction (new ObjFunction (function));
		ArrayList<Constraint> cons = moilp.getConstraints ();
		for (int i = 0; i < cons.size (); i++) {
			p0.addConstraint (cons.get (i));
		}
		
		System.out.println (p0);
	}
}
