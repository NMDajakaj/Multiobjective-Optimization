package problems;

import java.io.IOException;

public class Principal {
	public static void main (String[] args) throws IOException {
		for (int i = 0; i < 10; i ++) {
			Problem p = new Problem ();
			p.write (i);
		}
	}
}
