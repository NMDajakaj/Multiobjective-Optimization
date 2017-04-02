package sylva_crema;

import java.io.IOException;

public class Principal {

	public static void main(String[] args) throws IOException {
		MOILP moilp = new MOILP ("src/files/prueba.txt");
		SylvaCrema sc = new SylvaCrema (moilp);
		sc.solve ();
	}

}
