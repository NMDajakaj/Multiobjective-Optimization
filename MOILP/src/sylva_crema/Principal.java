package sylva_crema;

import java.io.IOException;

public class Principal {
	private static final String SOLVER = "gurobi";

	public static void main(String[] args) throws IOException {
		MOILP moilp = new MOILP ("src/files/prueba.txt", SOLVER);
		SylvaCrema sc = new SylvaCrema (moilp);
		sc.solve ();		
	}

}
