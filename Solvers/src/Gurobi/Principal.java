package Gurobi;
import gurobi.*;

public class Principal {

	public static void main(String[] args) throws GRBException {
		GRBEnv env = new GRBEnv ();
		GRBModel model = new GRBModel (env, args[0]);
		
		model.optimize ();
		
		double objval = model.get(GRB.DoubleAttr.ObjVal);
		System.out.println ("---- Gurobi: Valor objetivo: " + objval + "----");
		
		GRBVar[] vars = model.getVars ();
		String[] vnames = model.get (GRB.StringAttr.VarName, vars);
		double[] x = model.get (GRB.DoubleAttr.X, vars);
		
		for (int i = 0; i < vars.length; i++) {
			System.out.println ("    **** " + i + "  : "+ vnames[i] + " - " + x[i]);
		}
	}

}
