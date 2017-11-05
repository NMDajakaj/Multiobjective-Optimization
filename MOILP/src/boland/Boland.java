package boland;

import java.util.ArrayList;
import java.util.Arrays;

import common.*;

public class Boland {
	private MOILP moilp;
	private ArrayList<Solution> solutions;
	private ArrayList<CSObject> P;				// objetos que aún no se han explorado
	private LP basicLP;
	private double epsilon = 1;
	private long timeIni;
	private long timeEnd;
	private int iter;

	public Boland (MOILP m) {
		moilp = m;
		solutions = new ArrayList<Solution> ();
		P = new ArrayList<CSObject> ();
		for (int i = 0; i < moilp.getFunctions ().size (); i++) {
			if (moilp.getFunctions ().get (i).isMax ()) {
				moilp.getFunctions ().set (i, moilp.getFunctions ().get (i).changeSymbol ());
			}
		}
		//System.out.println ("Minimize:");
		//System.out.println(moilp);
		
		moilp.calculateM(false);
		//System.out.println(moilp.getM());
	}
	
	public void initialize () {
		timeIni = System.currentTimeMillis();
		P.add (new CSObject (new ArrayList<int[]> (), null, null));
		
		basicLP = new LP ();
		
		for (int i = 0; i < moilp.getVars ().size (); i++) {
			basicLP.addVariable (moilp.getVars ().get (i));
		}
		
		String func = "- ";
		for (int i = 0; i < moilp.getFunctions ().size () - 1; i++) {
			func += "za" + (i+1) + " - ";
			basicLP.addVariable (new Variable ("za" + (i+1), Integer.MIN_VALUE, Integer.MAX_VALUE));
		}
		func += "za" + moilp.getFunctions ().size ();
		basicLP.addVariable (new Variable ("za" + (moilp.getFunctions ().size ()), Integer.MIN_VALUE, Integer.MAX_VALUE));
		basicLP.setFunction (new ObjFunction (func, "Minimize"));
		
		for (int i = 0; i < moilp.getConstraints ().size (); i++) {
			basicLP.addConstraint (moilp.getConstraints ().get (i));
		}
		
		for (int i = 0; i < moilp.getFunctions ().size (); i++) {
			String cons = "za" + (i+1) + " ";
			for (int j = 0; j < moilp.getFunctions ().get (i).getCoefs ().size (); j++) {
				if (moilp.getFunctions ().get (i).getCoefs ().get (j) < 0) {
					cons += " + " + (- moilp.getFunctions ().get (i).getCoefs ().get (j)) + " " + moilp.getFunctions ().get (i).getVars ().get (j);
				} else {
					cons += " - " + moilp.getFunctions ().get (i).getCoefs ().get (j) + " " + moilp.getFunctions ().get (i).getVars ().get (j);
				}
			}
			cons = cons.substring(0, cons.length ()) + " = 0";
			basicLP.addConstraint (new Constraint (cons));
		}
		
		//System.out.println(basicLP);
	}
	
	public void solve () {
		iter = 1;
		while (!P.isEmpty ()) {
			CSObject aux = P.get (0);
			//System.out.println ("ITERACIÓN " + iter + ": P = " + aux);
			double[] xn = findNDP (aux.getYtilde(), aux.getY());
			
			if (xn != null) {
				int[] objValues = moilp.calculateObjValues (xn);
				solutions.add (new Solution (xn, objValues));
				//System.out.println(solutions.get (solutions.size () - 1));
				
				int[] ytildeL = new int[moilp.getFunctions ().size ()];
				for (int i = 0; i < moilp.getFunctions ().size (); i++) {
					if (aux.getY () == null) {
						if (aux.getYL () == null) {
							ytildeL[i] = solutions.get (solutions.size () - 1).getObjFunVal (i);
						} else {
							ytildeL[i] = Math.max(solutions.get (solutions.size () - 1).getObjFunVal (i), aux.getYL ()[i]);
						}
					} else {
						if (aux.getYL () == null) {
							ytildeL[i] = Math.min(aux.getY ()[i], solutions.get (solutions.size () - 1).getObjFunVal (i));
						} else {
							ytildeL[i] = Math.max(Math.min(aux.getY ()[i], solutions.get (solutions.size () - 1).getObjFunVal (i)), aux.getYL ()[i]);
						}
					}
				}
				
				if (aux.getY () != null) {
					int[] y1 = new int[moilp.getFunctions ().size ()];
					int[] y2 = new int[moilp.getFunctions ().size ()];
					for (int i = 0; i < moilp.getFunctions ().size (); i++) {
						y1[i] = Integer.MIN_VALUE;
						y2[i] = Integer.MIN_VALUE;
						if ((solutions.get (solutions.size () - 1).getObjFunVal (i) > aux.getY ()[i]) && 
								(solutions.get (solutions.size () - 1).getObjFunVal (i) > ytildeL[i])) {
							y1[i] = solutions.get (solutions.size () - 1).getObjFunVal (i);
						}
						if ((solutions.get (solutions.size () - 1).getObjFunVal (i) < aux.getY ()[i]) && (aux.getY ()[i] > ytildeL[i])) {
							y2[i] = aux.getY ()[i];
						}
					}
					
					if ((!allInf (y1)) && (!allInf (y2))) {
						ArrayList<int[]> newYtilde = new ArrayList<int[]> ();
						for (int i = 0; i < aux.getYtilde ().size (); i++) {
							newYtilde.add (aux.getYtilde().get (i));
						}
						newYtilde.add (y1);
						newYtilde.add (y2);
						P.add (new CSObject (newYtilde, null, ytildeL));
					}
				}
				
				int[] ytilde = new int[moilp.getFunctions ().size ()];
				for (int i = 0; i < moilp.getFunctions ().size (); i++) {
					if ((aux.getYL () == null) || (ytildeL[i] > aux.getYL ()[i])) {
						ytilde[i] = ytildeL[i];
					} else {
						ytilde[i] = Integer.MIN_VALUE;
					}
				}
				
				if (!allInf (ytilde)) {
					P.add (new CSObject (aux.getYtilde (), ytilde, aux.getYL ()));
				}
			}
			
			P.remove (0);
			//for (int i = 0; i < P.size(); i++) {
				//System.out.println(P.get(i));
			//}
			iter++;
		}
		
		//System.out.println ("----------------------------------------------");
		//for (int i = 0; i < solutions.size (); i++) {
			//System.out.println (solutions.get (i));
		//}
	}
	
	public double[] findNDP (ArrayList<int[]> Ytilde, int[] y) {
		double[] values = null;
		if (Ytilde.isEmpty() && (y == null)) {
			values = basicLP.solveLP (moilp.getSolver ());
		} else {
			
			LP modLP = new LP ();
			modLP.setFunction (basicLP.getFunction ());
			for (int i = 0; i < basicLP.getVars ().size (); i++) {
				modLP.addVariable (basicLP.getVars ().get (i));
			}
			for (int i = 0; i < basicLP.getConstraints ().size (); i++) {
				modLP.addConstraint (basicLP.getConstraints ().get (i));
			}
			
			if (y != null) {
				String consAdd = "";
				for (int i = 0; i < y.length; i++) {
					double val = y[i] - epsilon - moilp.getM ().get (i);
					consAdd += "b" + (i+1) + " + ";
					String consZ = "za" + (i+1) + " ";
					if (val < 0) {
						consZ += "+ " + (- val);
					} else {
						consZ += "- " + val;
					}
					consZ += " b" + (i+1) + " <= " + moilp.getM ().get (i);
					modLP.addConstraint (new Constraint (consZ));
					Variable b = new Variable ("b" + (i+1), 0, 1);
					b.setInteg (true);
					modLP.addVariable (b);
				}
				consAdd = consAdd.substring (0, consAdd.length () - 2) + "= 1";
				modLP.addConstraint (new Constraint (consAdd));				
				values = modLP.solveLP (moilp.getSolver ());
			}
			
			if (Ytilde.size () != 0) {
				for (int i = 0; i < Ytilde.size (); i++) {
					String consB2 = "";
					for (int j = 0; j < Ytilde.get (i).length; j++) {
						if (Ytilde.get (i)[j] != Integer.MIN_VALUE) {
							consB2 += "+ b" + (i+1) + "" + (j+1) + " ";
							double val2 = Ytilde.get (i)[j] - epsilon - moilp.getM ().get (j);
							String consZ2 = "za" + (j+1) + " ";
							if (val2 < 0) {
								consZ2 += "+ " + (-val2) + " ";
							} else {
								consZ2 += "- " + val2 + " ";
							}
							consZ2 += "b" + (i+1) + "" + (j+1) + " <= " + moilp.getM ().get (j);
							modLP.addConstraint (new Constraint (consZ2));
							Variable b = new Variable ("b" + (i+1) + "" + (j+1), 0, 1);
							b.setInteg (true);
							modLP.addVariable (b);
						}
						
					}
					modLP.addConstraint (new Constraint (consB2 + "= 1"));
				}
				values = modLP.solveLP (moilp.getSolver ());
			}
			//System.out.println (modLP);
		}
		return values;
	}
	
	public Boolean allInf (int[] vector) {
		for (int i = 0; i < vector.length; i++) {
			if (vector[i] != Integer.MIN_VALUE) {
				return false;
			}
		}
		return true;
	}
	
	public void distinctSols () {
		ArrayList<Solution> distinctSols = new ArrayList<Solution> ();
		distinctSols.add (solutions.get (0));
		for (int i = 1; i < solutions.size(); i++) {
			boolean distinct = true;
			for (int j = 0; j < distinctSols.size (); j++) {
				if (Arrays.equals (distinctSols.get(j).getObjFunVals(), solutions.get(i).getObjFunVals())) {
					distinct = false;
					break;
				}
			}
			if (distinct) {
				distinctSols.add(solutions.get(i));
			}
		}
		timeEnd = System.currentTimeMillis();
		System.out.println (distinctSols.size () + " soluciones | " + (timeEnd - timeIni) + " ms | " + iter + " modelos");
	}
}
