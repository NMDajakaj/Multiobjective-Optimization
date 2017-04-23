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
	private ArrayList<Solution> solutions;		// vector de soluciones (valores de las variables y valores de que toman las funciones objetivo)
	private ArrayList<Integer> M;				// cotas inferiores de cada una de las funciones objetivo
	private final String SOLVER = "gurobi";
	
	public SylvaCrema (MOILP m) {
		solutions = new ArrayList<Solution> ();
		M = new ArrayList<Integer> ();
		moilp = m;
		for (int i = 0; i < m.getFunctions ().size (); i++) {
			M.add (calculateM (m.getFunctions ().get (i)));
		}
	}
	
	public int calculateM (ObjFunction f) {
		LP lp = new LP ();
		lp.setFunction (f.changeSymbol ());
		for (int i = 0; i < moilp.getVars ().size (); i++) {
			lp.addVariable (moilp.getVars ().get (i));
		}
		double[] aux = lp.solveLP (SOLVER);
		
		return - f.calculateObjValue (aux, moilp.getVars ());
	}
	
	public void solve () {
		solveP0 ();
	}
	
	public void solveP0 () {
		LP p0 = new LP ();
		String function = "";
		ArrayList<Variable> vars = moilp.getVars ();
		for (int i = 0; i < vars.size () - 1; i++) {
			function += vars.get (i).getName () + " + ";
		}
		function += vars.get (vars.size () - 1).getName ();
		
		p0.setFunction (new ObjFunction (function));
		ArrayList<Constraint> cons = moilp.getConstraints ();
		for (int i = 0; i < cons.size (); i++) {
			p0.addConstraint (cons.get (i));
		}
		
		for (int i = 0; i < vars.size (); i++) {
			p0.addVariable (vars.get (i));
		}
		
		System.out.println (p0);
		
		double[] varValues = p0.solveLP(SOLVER);
		int[] objValues = moilp.calculateObjValues (varValues);
		solutions.add (new Solution (varValues, objValues));
		System.out.println ("P0 " + solutions.get (0));
		int iter = 1;
		Boolean solved = true;
		while (solved) {
			System.out.println ("ITERACION " + iter);
			solved = solveP (iter, p0);
			iter++;
		}
	}
	
	public Boolean solveP (int iter, LP lp) {
		String consV = "";
		for (int i = 0; i < moilp.getFunctions ().size (); i++) {
			String varName = "y" + iter + "-" + (i + 1);
			lp.addVariable(new Variable (varName, 0, 1));
			consV += varName + " + ";
			String consAux = moilp.getFunctions ().get (i).toString ();
			
			int val1 = solutions.get (iter - 1).getObjFunVal (i) + 1;
			int val2 = M.get (i);
			int val = val1 + val2;
			if (val > 0) {
				consAux += " - " + val;
			} else {
				consAux += " + " + (- val);
			}
			consAux += " " + varName + " >= ";
						
			if (M.get (i) < 0) {
				consAux += (- M.get (i));
			} else {
				consAux +="-" + M.get (i);
			}
			lp.addConstraint (new Constraint (consAux));
		}
		lp.addConstraint (new Constraint (consV.substring (0, consV.length () - 3) + " >= 1"));
		System.out.println (lp);
		
		double[] varValues = lp.solveLP(SOLVER);
		if (varValues != null) {
			int[] objValues = moilp.calculateObjValues (varValues);
			solutions.add (new Solution (varValues, objValues));
			System.out.println ("P" + iter + ": " + solutions.get (iter));
			return true;
		} else {
			System.out.println ("No hay más soluciones");
			return false;
		}
	}
}
