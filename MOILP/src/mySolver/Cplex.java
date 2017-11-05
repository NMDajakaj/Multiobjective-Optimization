package mySolver;

import java.util.ArrayList;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.UnknownObjectException;
import common.Constraint;
import common.ObjFunction;
import common.Variable;

public class Cplex extends Solver {
	private ArrayList<IloNumVar> vars;
	private ArrayList<String> varsName;
	private IloCplex cplex;
	// private IloLPMatrix lp;
	
	public Cplex () {
		vars = new ArrayList<IloNumVar> ();
		varsName = new ArrayList<String> ();
		try {
			cplex = new IloCplex ();
			cplex.setOut (null);
		} catch (IloException e) {
			e.printStackTrace();
		}
	}
	
	public void read (String filename) {
		try {
			cplex.importModel (filename);
			// lp = (IloLPMatrix)cplex.LPMatrixIterator().next();
		} catch (IloException e) {
			e.printStackTrace ();
		}
	}

	public Boolean solve () {
		Boolean solved = false;
		try {
			solved = cplex.solve ();
		} catch (IloException e) {
			e.printStackTrace ();
		}
		return solved;
	}

	public Double getObjVal () {
		Double objVal = null;
		try {
			objVal = cplex.getObjValue ();
		} catch (IloException e) {
			e.printStackTrace ();
			
		}
		return objVal;
	}

	public double[] getVarsVal() {
		double[] varsVal = null;
		try {
			IloNumVar[] v = new IloNumVar[vars.size ()];
			for (int i = 0; i < vars.size (); i++) {
				v[i] = vars.get (i);
			}
			varsVal = cplex.getValues(v);
		} catch (UnknownObjectException e) {
			e.printStackTrace();
		} catch (IloException e) {
			e.printStackTrace();
		}
		return varsVal;
	}
	
	public void addVars (ArrayList<Variable> v) {
		for (int i = 0; i < v.size (); i++) {
			try {
				if (v.get (i).isInteg ()) {
					vars.add (cplex.intVar(v.get (i).getLBound (), v.get (i).getUBound ()));
				} else {
					vars.add (cplex.numVar(v.get (i).getLBound (), v.get (i).getUBound ()));
				}
				varsName.add (v.get (i).getName ());
			} catch (IloException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addFunction (ObjFunction func) {
		try {
			IloLinearNumExpr expr = cplex.linearNumExpr();
			for (int i = 0; i < func.getVars ().size (); i++) {
				int index = varsName.indexOf(func.getVars ().get (i));
				expr.addTerm(func.getCoefs ().get (i), vars.get (index));
			}
			cplex.addMaximize (expr);
		} catch (IloException e) {
			e.printStackTrace();
		}
	}
	
	public void addConstraint (Constraint cons) {
		IloLinearNumExpr expr;
		try {
			expr = cplex.linearNumExpr();
			for (int i = 0; i < cons.getVars ().size (); i++) {
				int index = varsName.indexOf(cons.getVars ().get (i));
				expr.addTerm(cons.getCoefs ().get (i), vars.get (index));
			}
			
			switch (cons.getType ()) {
			case "<=":
				cplex.addLe (expr, cons.getValue ());
				break;
			case "=":
				cplex.addEq (expr, cons.getValue ());
				break;
			case ">=":
				cplex.addGe (expr, cons.getValue ());
				break;
			}
		} catch (IloException e) {
			e.printStackTrace();
		}
	}
	
	public String toString () {
		return cplex.toString ();
	}

}
