package lokman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import common.*;

public class Lokman {
	private MOILP moilp;
	private final double EPSILON = 0.01;
	private ArrayList<Solution> solutions;
	private LP lp;
	private HashMap<int[], Solution> boundsSols;
	private long timeIni;
	private long timeEnd;
	private int models;
	
	public Lokman (MOILP m) {
		moilp = m;
		models = 0;
		solutions = new ArrayList<Solution> ();
		boundsSols = new HashMap<int[], Solution> ();
		for (int i = 0; i < moilp.getFunctions ().size (); i++) {
			if (!moilp.getFunctions ().get (i).isMax ()) {
				moilp.getFunctions ().set (i, moilp.getFunctions ().get (i).changeSymbol ());
			}
		}
		moilp.calculateM (true);
	}
	
	public void initialize () {
		timeIni = System.currentTimeMillis();
		lp = new LP ();
		
		String func = "";
		for (int i = 0; i < moilp.getVars ().size (); i++) {
			double ac = 0;
			int index = moilp.getFunctions ().get (moilp.getFunctions ().size () - 1).getVars ().indexOf (moilp.getVars ().get (i).getName ());
			if (index != -1) {
				ac = moilp.getFunctions ().get (moilp.getFunctions ().size () - 1).getCoefs ().get (index);
			}
			for (int j = 0; j < moilp.getFunctions ().size () - 1; j++) {
				int index2 = moilp.getFunctions ().get (j).getVars ().indexOf (moilp.getVars ().get (i).getName ());
				if (index2 != -1) {
					ac += EPSILON * moilp.getFunctions ().get (j).getCoefs ().get (index2);
				}
			}
			if (ac > 0) {
				func += "+ " + ac;
			} else {
				func += "- " + (-ac);
			}
			func += " " + moilp.getVars ().get (i).getName () + " ";
		}
		
		lp.setFunction (new ObjFunction (func, "Maximize"));
		
		for (int i = 0; i < moilp.getConstraints ().size (); i++) {
			lp.addConstraint (moilp.getConstraints ().get (i));
		}
		
		for (int i = 0; i < moilp.getVars ().size (); i++) {
			lp.addVariable (moilp.getVars ().get (i));
		}
		
	}
	
	public void calculateK (int n, int size, String string, ArrayList<int[]> K) {
		String[] k = new String[n + 1];
		for (int i = 0; i <= n; i++) {
			k[i] = string + i + " ";
			if (size > 1) {
				calculateK (n, size - 1, k[i], K);
			} else {
				String[] tokens = k[i].split (" ");
				int[] aux = new int[k[i].length () / 2];
				for (int j = 0; j < tokens.length; j++) {
					aux[j] = new Integer (tokens[j]);
				}
				K.add (aux);
			}
		}
	}
	
	public void solve () {
		int iter = 0;
		Boolean solved = true;
		//System.out.println (lp);
		
		while (solved) {
			//System.out.println ("---------- ITERACION " + iter + " ----------");
			solved = false;
			ArrayList<Solution> solTemp = new ArrayList<Solution> ();
			ArrayList<int[]> K = new ArrayList<int[]> ();
			calculateK (iter, moilp.getFunctions ().size () - 2, "", K);
			for (int i = 0; i < K.size (); i++) {
				//System.out.println (Arrays.toString (K.get (i)));
				int[] bounds = calculateBounds (iter, K.get (i));
				//System.out.print ("COTAS: " + Arrays.toString (bounds));
				//System.out.println ();
				solveP (bounds, solTemp);
			}
			if (solTemp.size () != 0) {
				solved = true;
				int max = solTemp.get (0).getObjFunVal (moilp.getFunctions ().size () - 1);
				int indMax = 0;
				//for (int i = 0; i < solTemp.size(); i++) {
					//System.out.println(solTemp.get(i));
				//}
				for (int i = 1; i < solTemp.size (); i++) {
					if (solTemp.get (i).getObjFunVal (moilp.getFunctions ().size () - 1) > max) {
						indMax = i;
					}
				}
				//System.out.println("ELEGIDA:   " + solTemp.get(indMax));
				solutions.add(solTemp.get (indMax));
			}
			iter++;
		}
		
		timeEnd = System.currentTimeMillis();
		//System.out.println ("---------- SOLUCIONES ----------");
		//for (int i = 0; i < solutions.size (); i++) {
			//System.out.println (solutions.get (i));
		//}
		
		System.out.println(solutions.size() + " soluciones | " + (timeEnd - timeIni) + " ms | " + models + " modelos");
	}
	
	public int[] calculateBounds (int n, int[] k) {
		int[] bounds = new int[moilp.getFunctions ().size () - 1];
		
		if (moilp.getFunctions ().size () == 2) {
			if (solutions.size () == 0) {
				bounds[0] = - moilp.getM ().get (0);
			} else {
				bounds[0] = solutions.get (solutions.size () - 1).getObjFunVal (0) + 1;
			}
		} else {
			for (int i = 0; i < moilp.getFunctions ().size () - 2; i++) {
				if (k[i] == 0) {
					bounds[i] = - moilp.getM ().get (i);
				} else {
					bounds[i] = solutions.get (k[i] - 1).getObjFunVal (i) + 1;
				}
			}
			if (n == 0) {
				bounds[bounds.length - 1] = - moilp.getM ().get (bounds.length - 1);
			} else {
				// calcular Skn
				ArrayList<Integer> Skn = new ArrayList<Integer> ();
				for (int i = 0; i < n; i++) {
					//--------------------
					boolean addSkn = true;
					for (int j = 0; j < moilp.getFunctions ().size () - 2; j++) {
						if (solutions.get (i).getObjFunVal (j) < bounds[j]) {
							addSkn = false;
						}
					}
					if (addSkn) {
						Skn.add (solutions.get (i).getObjFunVal (bounds.length - 1));
					}
				}
				if (Skn.size () == 0) {
					bounds[bounds.length - 1] = moilp.getM ().get (bounds.length - 1);
				} else {
					bounds[bounds.length - 1] = Collections.max (Skn) + 1;
				}
			}
			
		}
		
		return bounds;
	}
	
	public void solveP (int[] bounds, ArrayList<Solution> solTemp) {
		boolean containsB = false;
		for (int i = 0; i < boundsSols.keySet ().size (); i++) {
			if (Arrays.equals ((int[]) boundsSols.keySet ().toArray ()[i], bounds)) {
				solTemp.add (boundsSols.get (boundsSols.keySet ().toArray ()[i]));
				//System.out.println ("*** No hace falta resolver ***");
				containsB = true;
			}
		}
		if (!containsB) {
			models++;
			LP lpAux = new LP ();
			
			lpAux.setFunction (lp.getFunction ());
			for (int i = 0; i < lp.getVars ().size (); i++) {
				lpAux.addVariable (lp.getVars ().get (i));
			}
			for (int i = 0; i < lp.getConstraints ().size (); i++) {
				lpAux.addConstraint (lp.getConstraints ().get (i));
			}
			
			for (int i = 0; i < moilp.getFunctions ().size () - 1; i++) {
				String cons = "";
				for (int j = 0; j < moilp.getFunctions ().get (i).getCoefs ().size (); j++) {
					if (moilp.getFunctions ().get (i).getCoefs ().get (j) < 0) {
						cons += " - " + (- moilp.getFunctions ().get (i).getCoefs ().get (j)) + " " + moilp.getFunctions ().get (i).getVars ().get (j);
					} else {
						cons += " + " + moilp.getFunctions ().get (i).getCoefs ().get (j) + " " + moilp.getFunctions ().get (i).getVars ().get (j);
					}
				}
				cons = cons.substring(0, cons.length ()) + " >= " + bounds[i];
				lpAux.addConstraint (new Constraint (cons));
			}
		
		//System.out.println (lpAux);
		
			double[] values = lpAux.solveLP (moilp.getSolver ());
			if (values != null) {
				int[] objValues = moilp.calculateObjValues (values);
				Solution solAux = new Solution (values, objValues);
				solTemp.add (solAux);
				boundsSols.put(bounds, solAux);
			} else {
				//System.out.println(">>>No sol<<<");
			}
		}	
		
	}
}
