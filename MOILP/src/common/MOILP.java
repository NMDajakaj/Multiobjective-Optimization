package common;

import java.io.IOException;
import java.util.ArrayList;

public class MOILP {
	private ArrayList<ObjFunction> functions;
	private ArrayList<Constraint> constraints;
	private ArrayList<Variable> variables;
	private ArrayList<Integer> M;
	private String SOLVER;
	
	/*
	 * El constructor recibe el nombre del fichero que contiene el MOILP, para poder leerlo y 
	 * guardar las funciones, variables y restricciones
	 */
	
	public MOILP (String file, String solver) throws IOException {
		SOLVER = solver;
		Reader r = new Reader (file);
		functions = r.getFMOILP();
		constraints = r.getCMOILP();
		variables = r.getVMOILP();
		M = new ArrayList<Integer> ();
	}
	
	public void calculateM (Boolean max) {
		LP lp = new LP ();
		for (int i = 0; i < variables.size (); i++) {
			lp.addVariable (variables.get (i));
		}
		
		for (int i = 0; i < functions.size (); i++) {
			if (max) {
				lp.setFunction (functions.get (i).changeSymbol ());
			} else {
				lp.setFunction (functions.get (i));
			}
			double[] aux = lp.solveLP (SOLVER);
			if (max) {
				M.add (- functions.get (i).calculateObjValue (aux, variables));
			} else {
				M.add (functions.get (i).calculateObjValue (aux, variables));
			}
		}
	}
	
	public int[] calculateObjValues (double[] varValues) {
		int[] objValues = new int[functions.size ()];
		for (int i = 0; i < functions.size (); i++) {
			objValues[i] = functions.get (i).calculateObjValue(varValues, variables);
		}
		return objValues;
	}
	
	public Variable getVar (String name) {
		for (int i = 0; i < variables.size (); i++) {
			if (variables.get (i).getName ().equals (name)) {
				return variables.get (i);
			}
		}
		return null;
	}
	
	public ArrayList<Variable> getVars () {
		return variables;
	}
	
	public ArrayList<ObjFunction> getFunctions () {
		return functions;
	}
	
	public ArrayList<Constraint> getConstraints () {
		return constraints;
	}
	
	public ArrayList<Integer> getM () {
		return M;
	}
	
	public String getSolver () {
		return SOLVER;
	}
	
	public String toString () {
		String string = "";
		for (int i = 0; i < functions.size (); i++) {
			string += "\t" + functions.get (i).toString () + "\n";
		}
		
		string += "Subject To:\n";
		
		for (int i = 0; i < constraints.size (); i++) {
			string += "\t" + constraints.get (i).toString () + "\n";
		}
		
		string += "Variables:\n";
		
		for (int i = 0; i < variables.size (); i++) {
			string += "\t" + variables.get (i).toString () + "\n";
		}
		
		return string;
	}
}
