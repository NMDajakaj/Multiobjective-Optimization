package common;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Reader {
	private ArrayList<String> funcs;
	private ArrayList<String> types;
	private ArrayList<String> cons;
	private HashMap<String, Variable> vars;		// nombre de la variable mas si es entera o no
	private ArrayList<ObjFunction> fMOILP;
	private ArrayList<Constraint> cMOILP;
	private ArrayList<Variable> vMOILP;
	
	public Reader (String file) {
		FileReader f;
		funcs = new ArrayList<String> ();
		types = new ArrayList<String> ();
		cons = new ArrayList<String> ();
		vars = new HashMap<String, Variable> ();
				
		try {
			f = new FileReader (file);
			BufferedReader b = new BufferedReader (f);
			Boolean moreF = true;
			String type = "";
			while (moreF) {
				type = b.readLine ();
				if (type.equals("Maximize") || type.equals ("Minimize")) {
					String F = b.readLine ();
					types.add (type);
					funcs.add (F);
				} else {
					moreF = false;
				}
			}
			
			functions ();
			
			Boolean moreC = true;
			String c = "";
			while (moreC) {
				c = b.readLine ();
				if (c.equals ("Bounds") || (c.equals ("Integers"))) {
					moreC = false;
				} else {
					cons.add (c);
				}
			}
			
			constraints ();
			
			if (c.equals ("Bounds")) {
				Boolean moreB = true;
				String bound = "";
				while (moreB) {
					bound = b.readLine ();
					if (bound.equals ("Integers") || bound.equals ("End")) {
						moreB = false;
					} else {
						String[] tokens = bound.split ("\\s");
						int i = 0;
						while (tokens[i].length () == 0) {
							i++;
						}
						if (tokens[i].equals ("-infinity")) {
							vars.get (tokens[i + 2]).setLBound (Integer.MIN_VALUE);
						} else {
							vars.get (tokens[i + 2]).setLBound(new Integer (tokens[i]));
						}
						if (tokens[i + 4].equals ("infinity")) {
							vars.get (tokens[i + 2]).setUBound(Integer.MAX_VALUE);
						} else {
							vars.get (tokens[i + 2]).setUBound(new Integer (tokens[i + 4]));
						}
					}
				}
				c = bound;
			}
			if (c.equals ("Integers")) {
				String tokens[] = b.readLine ().split ("\\s");
				for (int i = 0; i < tokens.length; i++) {
					while (tokens[i].length () == 0) {
						i++;
					}
					vars.get (tokens[i]).setInteg (true);
				}
			}
			variables ();
			
			b.close ();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void functions () {
		ArrayList<ObjFunction> objF = new ArrayList<ObjFunction> ();
		
		for (int i = 0; i < funcs.size (); i++) {
			ObjFunction auxF = new ObjFunction (funcs.get (i), types.get (i));
			objF.add (auxF);
			ArrayList<String> v = auxF.getVars ();
			for (int j = 0; j < v.size (); j++) {
				if (!vars.containsKey (v.get (j))) {
					vars.put (v.get (j), new Variable (v.get (j)));
				}
			}
		}
		
		fMOILP = objF;
	}
	
	public void constraints () {
		ArrayList<Constraint> constraint = new ArrayList<Constraint> ();
		
		for (int i = 0; i < cons.size (); i++) {
			Constraint auxC = new Constraint (cons.get (i));
			constraint.add (auxC);
			ArrayList<String> v = auxC.getVars ();
			for (int j = 0; j < v.size (); j++) {
				if (!vars.containsKey (v.get (j))) {
					vars.put (v.get (j), new Variable (v.get (j)));
				}
			}
		}
		
		cMOILP = constraint;
	}
	
	public void variables () {
		ArrayList<Variable> v = new ArrayList<Variable> ();
		
		for (int i = 0; i < vars.keySet ().size (); i++) {
			v.add (vars.get(vars.keySet ().toArray ()[i]));
		}
		
		vMOILP = v;
	}
	
	public ArrayList<ObjFunction> getFMOILP () {
		return fMOILP;
	}
	
	public ArrayList<Constraint> getCMOILP () {
		return cMOILP;
	}
	
	public ArrayList<Variable> getVMOILP () {
		return vMOILP;
	}
}
