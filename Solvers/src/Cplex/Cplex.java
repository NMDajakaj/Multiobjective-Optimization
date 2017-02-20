package Cplex;

import ilog.concert.IloException;
import ilog.concert.IloLPMatrix;
import ilog.cplex.IloCplex;

public class Cplex {

	public static void main(String[] args) {
		try {
			IloCplex cplex = new IloCplex ();
			cplex.importModel(args[0]);
			IloLPMatrix lp = (IloLPMatrix)cplex.LPMatrixIterator().next();
			/* http://perso.ensta-paristech.fr/~diam/ro/online/cplex/examples/cplex/src/java/AdMIPex6.java */
			
			cplex.solve();
			System.out.println ("---- Val Obj ---- " + cplex.getObjValue ());
			
			double[] vals = cplex.getValues(lp.getNumVars());
									
			for (int i = 0; i < vals.length; i++) {
				System.out.println ("   ****  Var " + i + " : " + vals[i]);
			}
			
		} catch (IloException e) {
			e.printStackTrace();
		}
		
	}

}
