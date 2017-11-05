package boland;

import java.util.ArrayList;

public class CSObject {
	private ArrayList<int[]> Ytilde;
	private int[] y;
	private int[] yL;
	
	public CSObject (ArrayList<int[]> YtildeIn, int[] yIn, int[] yLIn) {
		Ytilde = YtildeIn;
		y = yIn;
		yL = yLIn;
	}
	
	public ArrayList<int[]> getYtilde () {
		return Ytilde;
	}
	
	public int[] getY () {
		return y;
	}
	
	public int[] getYL () {
		return yL;
	}
	
	public String toString () {
		String string = "";
		string += "( ";
		
		if (Ytilde.size () == 0) {
			string += "empty , ";
		} else {
			string += "{ ";
			for (int i = 0; i < Ytilde.size (); i++) {
				string += "( ";
				for (int j = 0; j < Ytilde.get (i).length; j++) {
					string += Ytilde.get (i)[j] + " ";
				}
				string += ")";
			}
			string += " }, ";
		}
		
		if (y == null) {
			string += y + " , ";
		} else {
			string += "( ";
			for (int j = 0; j < y.length; j++) {
				string += y[j] + " ";
			}
			string += ") , ";
		}
		
		if (yL == null) {
			string += yL + " )";
		} else {
			string += "( ";
			for (int j = 0; j < yL.length; j++) {
				string += yL[j] + " ";
			}
			string += ") )";
		}
		
		return string;
	}
}
