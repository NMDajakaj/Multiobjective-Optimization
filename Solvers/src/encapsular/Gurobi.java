package encapsular;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBModel;
import gurobi.GRBVar;

public class Gurobi extends Solver {

	private GRBEnv env;
	private GRBModel model;
	
	public Gurobi () {
		try {
			env = new GRBEnv ();
		} catch (GRBException e) {
			e.printStackTrace();
		}
	}

	public void read(String filename) {
		try {
			model = new GRBModel (env, filename);
		} catch (GRBException e) {
			e.printStackTrace();
		}
	}

	public void solve() {
		try {
			model.optimize ();
		} catch (GRBException e) {
			e.printStackTrace();
		}
	}

	public Double getObjVal() {
		Double objVal = null;
		try {
			objVal = model.get(GRB.DoubleAttr.ObjVal);
		} catch (GRBException e) {
			e.printStackTrace();
		}
		return objVal;
	}

	public double[] getVarsVal() {
		GRBVar[] vars = model.getVars ();
		double[] vals = null;
		try {
			vals = model.get (GRB.DoubleAttr.X, vars);
		} catch (GRBException e) {
			e.printStackTrace();
		}
		return vals;
	}
}
