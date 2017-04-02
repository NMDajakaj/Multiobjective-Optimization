package sylva_crema;

import java.util.ArrayList;

import mySolver.Cplex;
import mySolver.Gurobi;
import mySolver.Solver;

public class LP {
	private ObjFunction function;
	private ArrayList<Constraint> constraints;
	private ArrayList<String> variables;
	
	public LP (){
		constraints = new ArrayList<Constraint> ();
	}
	
	public void setFunction (ObjFunction f) {
		function = f;
	}
	
	public void addConstraint (Constraint c) {
		constraints.add (c);
	}
	
	public void addVariable (String v) {
		variables.add (v);
	}
	
	public void solveLP (String s) {
		Solver solver;
		if (s.equals("gurobi")) {
			solver = new Gurobi ();
		} else {
			solver = new Cplex ();
		}
	}
	
	public String toString () {
		String aux = "Maximize\n\t" + function.toString () + "\nSubject To";
		for (int i = 0; i < constraints.size (); i++) {
			aux += "\n\t" + constraints.get (i).toString ();
		}
		return aux;
	}
}
