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
	private int[] weight = {4, 3};					// vector de pesos
	private ArrayList<Solution> solutions;		// vector de soluciones (valores de las variables y valores de que toman las funciones objetivo)
	private LP p0;
	
	public SylvaCrema (MOILP m) {
		solutions = new ArrayList<Solution> ();
		moilp = m;
		for (int i = 0; i < moilp.getFunctions ().size (); i++) {
			if (!moilp.getFunctions ().get (i).isMax ()) {
				moilp.getFunctions ().set (i, moilp.getFunctions ().get (i).changeSymbol ());
			}
		}
		moilp.calculateM ();
	}
	
	public void solve () {
		solveP0 ();
		
		int iter = 1;
		Boolean solved = true;
		while (solved) {
			System.out.println ("ITERACION " + iter);
			solved = solveP (iter, p0);
			iter++;
		}
	}
	
	public void solveP0 () {
		p0 = new LP ();
		String function = "";
		ArrayList<Variable> vars = moilp.getVars ();
		for (int i = 0; i < vars.size (); i++) {
			int ac = 0;
			for (int j = 0; j < moilp.getFunctions ().size (); j++) {
				int index = moilp.getFunctions ().get (j).getVars ().indexOf (vars.get (i).getName ());
				if (index != -1) {
					ac += weight[j] * moilp.getFunctions ().get (j).getCoefs ().get (index);
				}
			}
			if (ac < 0) {
				function += "- " + (- ac) + " " + vars.get (i).getName () + " ";
			} else {
				function += "+ " + ac + " " + vars.get (i).getName () + " ";
			}
		}

		p0.setFunction (new ObjFunction (function, "Maximize"));
		ArrayList<Constraint> cons = moilp.getConstraints ();
		for (int i = 0; i < cons.size (); i++) {
			p0.addConstraint (cons.get (i));
		}
		
		for (int i = 0; i < vars.size (); i++) {
			p0.addVariable (vars.get (i));
		}
		
		System.out.println (p0);
		
		double[] varValues = p0.solveLP(moilp.getSolver ());
		int[] objValues = moilp.calculateObjValues (varValues);
		solutions.add (new Solution (varValues, objValues));
		System.out.println ("P0 " + solutions.get (0));
	}
	
	public Boolean solveP (int iter, LP lp) {
		String consV = "";
		for (int i = 0; i < moilp.getFunctions ().size (); i++) {
			String varName = "y" + iter + "-" + (i + 1);
			Variable varAux = new Variable (varName, 0, 1);
			varAux.setInteg (true);
			lp.addVariable(varAux);
			consV += varName + " + ";
			String consAux = moilp.getFunctions ().get (i).toString ();
			
			int val1 = solutions.get (iter - 1).getObjFunVal (i) + 1;
			int val2 = moilp.getM ().get (i);
			int val = val1 + val2;
			if (val > 0) {
				consAux += " - " + val;
			} else {
				consAux += " + " + (- val);
			}
			consAux += " " + varName + " >= ";
						
			if (moilp.getM ().get (i) < 0) {
				consAux += (- moilp.getM ().get (i));
			} else {
				consAux +="-" + moilp.getM ().get (i);
			}
			lp.addConstraint (new Constraint (consAux));
		}
		lp.addConstraint (new Constraint (consV.substring (0, consV.length () - 3) + " >= 1"));
		System.out.println (lp);
		
		double[] varValues = lp.solveLP(moilp.getSolver ());
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
