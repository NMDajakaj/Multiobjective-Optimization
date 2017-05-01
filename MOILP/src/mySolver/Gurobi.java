package mySolver;

import java.util.ArrayList;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;
import sylva_crema.Constraint;
import sylva_crema.ObjFunction;
import sylva_crema.Variable;

public class Gurobi extends Solver {

	private GRBEnv env;
	private GRBModel model;
	private ArrayList<GRBVar> vars;
	private ArrayList<String> varsName;
	
	public Gurobi () {
		try {
			env = new GRBEnv ();
			env.set (GRB.IntParam.OutputFlag, 0);
			model = new GRBModel (env);
			vars = new ArrayList<GRBVar> ();
			varsName = new ArrayList<String> ();
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

	public Boolean solve() {
		try {
			model.optimize ();
			if (model.get(GRB.IntAttr.SolCount) != 0) {
				return true;
			} else {
				return false;
			}
			
		} catch (GRBException e) {
			e.printStackTrace();
			return false;
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
	
	public void addVars (ArrayList<Variable> v) {
		for (int i = 0; i < v.size (); i++) {
			try {
				if (v.get (i).isInteg ()) {
					vars.add (model.addVar (v.get (i).getLBound (), v.get (i).getUBound (), 0.0, GRB.INTEGER, v.get (i).getName ()));
				} else {
					vars.add (model.addVar (v.get (i).getLBound (), v.get (i).getUBound (), 0.0, GRB.CONTINUOUS, v.get (i).getName ()));
				}
				varsName.add (v.get (i).getName ());
			} catch (GRBException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addFunction (ObjFunction func) {
		GRBLinExpr obj = new GRBLinExpr ();
		for (int i = 0; i < func.getCoefs ().size (); i++) {
			int index = varsName.indexOf (func.getVars ().get (i));
			obj.addTerm(func.getCoefs ().get (i), vars.get (index));
		}
		try {
			model.setObjective (obj, GRB.MAXIMIZE);
		} catch (GRBException e) {
			e.printStackTrace();
		}
	}
	
	public void addConstraint (Constraint cons) {
		GRBLinExpr expr = new GRBLinExpr ();
		for (int i = 0; i < cons.getCoefs ().size (); i++) {
			int index = varsName.indexOf (cons.getVars ().get (i));
			expr.addTerm (cons.getCoefs ().get (i), vars.get (index));
		}
		try {
			switch (cons.getType ()) {
			case "<=":
				model.addConstr(expr, GRB.LESS_EQUAL, cons.getValue (), "");
				break;
			case "=":
				model.addConstr(expr, GRB.EQUAL, cons.getValue (), "");
				break;
			case ">=":
				model.addConstr(expr, GRB.GREATER_EQUAL, cons.getValue (), "");
				break;
			}
		} catch (GRBException e) {
			e.printStackTrace ();
		}
	}
}
