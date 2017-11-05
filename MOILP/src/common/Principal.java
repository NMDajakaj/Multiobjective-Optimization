package common;

import java.io.IOException;

import sylva_crema.SylvaCrema;

public class Principal {
	private static final String SOLVER = "gurobi";

	public static void main(String[] args) throws IOException {
		for (int i = 0; i < 10; i++) {
			if (i==0||i==2||i==4){
				System.out.println();
			} else{
				MOILP moilpSC = new MOILP ("src/output/4x4x1_" + i +"_problem.molp", SOLVER);
				SylvaCrema sc = new SylvaCrema (moilpSC);
				sc.solve ();
			}
		}
		
//		for (int i = 0; i < 10; i++) {
//			if (i==-1){
//				System.out.println();
//			}else{
//				MOILP moilpB = new MOILP ("src/output/3x6x5_" + i + "_problem.molp", SOLVER);
//				Boland b = new Boland (moilpB);
//				b.initialize ();
//				b.solve ();
//				b.distinctSols ();
//			}
//		}
		
//		for (int i = 0; i < 10; i++) {
//			if (i==3 || i==6 || i==9) {
//				System.out.println();
//			} else {
//				MOILP moilpL = new MOILP ("src/output/3x6x5_" + i + "_problem.molp", SOLVER);
//				Lokman l = new Lokman (moilpL);
//				l.initialize ();
//				l.solve ();
//			}
//		}
		
//		for (int i = 0; i < 10; i++) {
//			MOILP moilpL = new MOILP ("src/output/3x6x5_" + i + "_problem.molp", SOLVER);
//			LokmanAlg1 l = new LokmanAlg1 (moilpL);
//			l.solve ();
//		}
	}

}
