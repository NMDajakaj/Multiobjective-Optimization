package common;

public class Variable {
	private String name;
	private int lBound;
	private int uBound;
	private Boolean integ;
	
	public Variable (String n, int l, int u) {
		name = n;
		lBound = l;
		uBound = u;
		integ = false;
	}
	
	/**
	 * Puede que una variable no tenga cotas, por lo que se podrá crear solo con el nombre,
	 * si tiene solo una cota primero crearemos la variable con el nombre y después añadiremos esa cota
	 * @param n
	 */
	public Variable (String n) {
		name = n;
		uBound = Integer.MAX_VALUE;
	}
	
	public void setName (String n) {
		name = n;
	}
	
	public String getName () {
		return name;
	}
	
	public void setLBound (int l) {
		lBound = l;
	}
	
	public int getLBound () {
		return lBound;
	}
	
	public void setUBound (int u) {
		uBound = u;
	}
	
	public int getUBound () {
		return uBound;
	}
	
	public String toString () {
		return "" + name + " -> {" + lBound + ", " + uBound + "} -> " + integ;
	}
	
	public void setInteg (Boolean i) {
		integ = i;
	}
	
	public Boolean isInteg () {
		return integ;
	}
}
