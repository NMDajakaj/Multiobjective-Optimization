package lokman;

import java.util.ArrayList;
import common.*;

public class LokmanAlg1 {
	private MOILP moilp;
	private final double EPSILON = 0.01;
	private ArrayList<Solution> solutions;
	private LP p0;
	private long timeIni;
	private long timeEnd;
	
	public LokmanAlg1 (MOILP m) {
		moilp = m;
		solutions = new ArrayList<Solution> ();
		for (int i = 0; i < moilp.getFunctions ().size (); i++) {
			if (!moilp.getFunctions ().get (i).isMax ()) {
				moilp.getFunctions ().set (i, moilp.getFunctions ().get (i).changeSymbol ());
			}
		}
		moilp.calculateM (true);
	}
	
	public void solve () {	
		timeIni = System.currentTimeMillis();
		int iter = 1;
		Boolean solved = solveP0 ();
		while (solved) {
			//System.out.println ("ITERACION " + iter);
			solved = solveP (iter, p0);
			iter++;
		}
		
		timeEnd = System.currentTimeMillis();
		//for (int i = 0; i < solutions.size (); i++) {
			//System.out.println (solutions.get (i));
		//}
		
		System.out.println(solutions.size() + " soluciones | " + (timeEnd - timeIni) + " ms");
	}
	
	public Boolean solveP0 () {
		p0 = new LP ();
		String function = "";
		ArrayList<Variable> vars = moilp.getVars ();
		for (int i = 0; i < vars.size (); i++) {
			double ac = 0;
			int index2 = moilp.getFunctions ().get (moilp.getFunctions ().size () - 1).getVars ().indexOf (moilp.getVars ().get (i).getName ());
			if (index2 != -1) {
				ac = moilp.getFunctions ().get (moilp.getFunctions ().size () - 1).getCoefs ().get (index2);
			}
			for (int j = 0; j < moilp.getFunctions ().size () - 1; j++) {
				int index = moilp.getFunctions ().get (j).getVars ().indexOf (vars.get (i).getName ());
				if (index != -1) {
					ac += EPSILON * moilp.getFunctions ().get (j).getCoefs ().get (index);
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
		
		//System.out.println (p0);
		
		double[] varValues = p0.solveLP(moilp.getSolver ());
		if (varValues != null) {
			int[] objValues = moilp.calculateObjValues (varValues);
			solutions.add (new Solution (varValues, objValues));
			//System.out.println ("P0" + ": " + solutions.get (0));
			return true;
		} else {
			//System.out.println ("No hay más soluciones");
			return false;
		}
	}
	
	public Boolean solveP (int iter, LP lp) {
		String consV = "";
		for (int i = 0; i < moilp.getFunctions ().size () - 1; i++) {
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
		//System.out.println (lp);
		
		double[] varValues = lp.solveLP(moilp.getSolver ());
		if (varValues != null) {
			int[] objValues = moilp.calculateObjValues (varValues);
			solutions.add (new Solution (varValues, objValues));
			//System.out.println ("P" + iter + ": " + solutions.get (iter));
			return true;
		} else {
			//System.out.println ("No hay más soluciones");
			return false;
		}
	}

}
