package common;

public class Solution {
	private double[] varValues;
	private int[] objFunValues;
	
	public Solution (double[] var, int[] obj) {
		varValues = var;
		objFunValues = obj;
	}
	
	public String toString () {
		String string = "SOLUTION: (";
		for (int i = 0; i < varValues.length - 1; i++) {
			string += varValues[i] + ", ";
		}
		string += varValues[varValues.length - 1] + ") - (";
		for (int i = 0; i < objFunValues.length - 1; i++) {
			string += objFunValues[i] + ", ";
		}
		string += objFunValues[objFunValues.length - 1] + ")";
		return string;
	}
	
	public double getVarValue (int i) {
		return varValues[i];
	}
	
	public int getObjFunVal (int i) {
		return objFunValues[i];
	}
	
	public int[] getObjFunVals () {
		return objFunValues;
	}
}
