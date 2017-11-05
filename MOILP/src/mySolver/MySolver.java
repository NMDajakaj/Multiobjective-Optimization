package mySolver;

import java.util.ArrayList;

import common.Constraint;
import common.ObjFunction;
import common.Variable;

public interface MySolver {

	public void read (String filename);
	
	public Boolean solve ();
	
	public Double getObjVal ();
	
	public double[] getVarsVal ();
	
	public void addVars (ArrayList<Variable> vars);
	
	public void addFunction (ObjFunction func);
	
	public void addConstraint (Constraint cons);
}
