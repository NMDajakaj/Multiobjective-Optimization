package mySolver;

import ilog.concert.IloException;
import ilog.concert.IloLPMatrix;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.UnknownObjectException;

public class Cplex extends Solver {

	private IloCplex cplex;
	private IloLPMatrix lp;
	
	public Cplex () {
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
			lp = (IloLPMatrix)cplex.LPMatrixIterator().next();
		} catch (IloException e) {
			e.printStackTrace ();
		}
	}

	public void solve () {
		try {
			cplex.solve ();
		} catch (IloException e) {
			e.printStackTrace ();
		}
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
			varsVal = cplex.getValues(lp.getNumVars());
		} catch (UnknownObjectException e) {
			e.printStackTrace();
		} catch (IloException e) {
			e.printStackTrace();
		}
		return varsVal;
	}

}
