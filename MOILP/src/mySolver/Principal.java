package mySolver;

public class Principal {

	public static void main(String[] args) {
		
		Solver solver;
		if (args[0].equals("cplex")) {
			solver = new Cplex ();
		} else {
			solver = new Gurobi ();
		}
		
		solver.read ("src/Entrada/coins.lp");
		solver.solve ();
		System.out.println ("--------------------------\n\t" + args[0].toUpperCase () + "\t\n--------------------------");
		System.out.println ("\nRESULTADOS\n");
		System.out.println ("Val Obj: " + solver.getObjVal());
		double[] v = solver.getVarsVal();
		for (int i = 0; i < v.length; i++) {
			System.out.println ("  Variable " + i + " = " + v[i]);
		}
		
		// CPLEX
		/*Cplex c = new Cplex ();
		c.read ("src/Entrada/prueba.lp");
		c.solve ();
		System.out.println ("--------------------------\n\tCPLEX\t\n--------------------------");
		System.out.println ("\nRESULTADOS\n");
		System.out.println ("Val Obj: " + c.getObjVal());
		double[] v = c.getVarsVal();
		for (int i = 0; i < v.length; i++) {
			System.out.println ("  Variable " + i + " = " + v[i]);
		}
		
		// GUROBI
		Gurobi g = new Gurobi ();
		g.read("src/Entrada/prueba.lp");
		g.solve ();
		System.out.println ("--------------------------\n\tGUROBI\t\n--------------------------");
		System.out.println ("\nRESULTADOS\n");
		System.out.println ("Val Obj: " + g.getObjVal());
		double[] v2 = g.getVarsVal();
		for (int i = 0; i < v2.length; i++) {
			System.out.println ("  Variable " + i + " = " + v2[i]);
		}*/
		
	}

}
