package sylva_crema;

import java.util.ArrayList;

import mySolver.Cplex;
import mySolver.Gurobi;
import mySolver.Solver;

public class LP {
	private ObjFunction function;
	private ArrayList<Constraint> constraints;
	private ArrayList<Variable> variables;
	
	public LP (){
		constraints = new ArrayList<Constraint> ();
		variables = new ArrayList<Variable> ();
	}
	
	public void setFunction (ObjFunction f) {
		function = f;
	}
	
	public void addConstraint (Constraint c) {
		constraints.add (c);
	}
	
	public void addVariable (Variable v) {
		variables.add (v);
	}
	
	public double[] solveLP (String s) {
		Solver solver;
		if (s.equals("gurobi")) {
			solver = new Gurobi ();
		} else {
			solver = new Cplex ();
		}
		solver.addVars (variables);
		solver.addFunction (function);
		for (int i = 0; i < constraints.size (); i++) {
			solver.addConstraint (constraints.get (i));
		}
		
		double[] results;
		Boolean solved = solver.solve ();
		if (solved) {
			results = solver.getVarsVal ();
		} else {
			results = null;
		}
		return results;
	}
	
	public String toString () {
		String aux = "Maximize\n\t" + function.toString () + "\nSubject To";
		for (int i = 0; i < constraints.size (); i++) {
			aux += "\n\t" + constraints.get (i).toString ();
		}
		for (int i = 0; i < variables.size (); i++) {
			aux += "\n\t" + variables.get (i).getName () + " -> {" + variables.get (i).getLBound () + ", " + variables.get (i).getUBound () + "}";
		}
		return aux;
	}
	
	public ArrayList<Variable> getVars () {
		return variables;
	}
}
