package mySolver;

public interface MySolver {

	public void read (String filename);
	
	public void solve ();
	
	public Double getObjVal ();
	
	public double[] getVarsVal ();
	
}
