package problems;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Problem {
	private int nVars;
	private int nFuns;
	private int nCons;
	private int dFuns;
	private int dCons;
	private int[][] funcs;
	private int[][] cons;
	private int[] values;
	private int lValue;
	private int uValue;
	private int lRange;
	private int uRange;
	private int greater;
	private int equal;
	private int lower;
	private int lBound;
	private int uBound;
	
	public Problem () throws IOException {		
		nFuns = 4;	/****/
		dFuns = 30;
		nVars = 4;	/****/
		funcs = new int[nFuns][nVars];
		dCons = 30;
		greater = 0;
		equal = 0;
		lower = 1;	/****/
		nCons = greater + equal + lower;
		cons = new int[nCons][nVars];
		lValue = 2;
		uValue = 10;
		lRange = -4;
		uRange = 6;
		lBound = 0;
		uBound = 4;
				
		values = new int[nCons];
		setValues ();
		fillMatrix (funcs, dFuns);
		fillMatrix (cons, dCons);
	}
	
	public void setValues () {
		for (int i = 0; i < nCons; i++) {
			values[i] = (int) Math.floor (Math.random () * (uValue - lValue + 1) + lValue);
		}
	}
	
	public void fillMatrix (int[][] matrix, int disp) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] = 1;
			}
		}
		
		ArrayList<Integer> nums = new ArrayList<Integer> ();
		for (int i = 0; i < matrix.length * matrix[0].length * disp / 100; i++) {
			Boolean next = true;
			while (next) {
				int aux = (int) Math.floor (Math.random () * matrix.length * matrix[0].length);
				if (!nums.contains (aux)) {
					nums.add (aux);
					next = false;
					matrix[aux / matrix[0].length][aux % matrix[0].length] = 0;
				}
			}
		}
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j] != 0) {
					Boolean next = true;
					while (next) {
						int aux = (int) Math.floor (Math.random () * (uRange - lRange + 1) + lRange);
						if (aux != 0) {
							matrix[i][j] = aux;
							next = false;
						}
					}
				}
			}
		}
	}
	
	public void write (int ind) throws IOException {
		FileWriter file = null;
		file = new FileWriter ("src/output/" + nFuns + "x" + nVars + "x" + nCons + "_" + ind + "_problem.molp");
		for (int i = 0; i < nFuns; i++) {
			file.write ("Maximize\n\t");
			for (int j = 0; j < nVars; j++) {
				if (funcs[i][j] > 0) {
					file.write ("+ " + funcs[i][j] + " x" + (j + 1) + " ");
				} else {
					if (funcs[i][j] < 0) {
						file.write ("- " + (-funcs[i][j]) + " x" + (j + 1) + " ");
					}
				}
			}
			file.write ("\n");
		}
		file.write ("Subject To\n");
		for (int i = 0; i < greater; i++) {
			file.write ("\t");
			for (int j = 0; j < nVars; j++) {
				if (cons[i][j] > 0) {
					file.write ("+ " + cons[i][j] + " x" + (j + 1) + " ");
				} else {
					if (cons[i][j] < 0) {
						file.write ("- " + (-cons[i][j]) + " x" + (j + 1) + " ");
					}
				}
			}
			file.write (">= " + values[i] + "\n");
		}
		for (int i = greater; i < greater + lower; i++) {
			file.write ("\t");
			for (int j = 0; j < nVars; j++) {
				if (cons[i][j] > 0) {
					file.write ("+ " + cons[i][j] + " x" + (j + 1) + " ");
				} else {
					if (cons[i][j] < 0) {
						file.write ("- " + (-cons[i][j]) + " x" + (j + 1) + " ");
					}
				}
			}
			file.write ("<= " + values[i] + "\n");
		}
		for (int i = greater + lower; i < nCons; i++) {
			file.write ("\t");
			for (int j = 0; j < nVars; j++) {
				if (cons[i][j] > 0) {
					file.write ("+ " + cons[i][j] + " x" + (j + 1) + " ");
				} else {
					if (cons[i][j] < 0) {
						file.write ("- " + (-cons[i][j]) + " x" + (j + 1) + " ");
					}
				}
			}
			file.write ("= " + values[i] + "\n");
		}
		file.write ("Bounds\n");
		for (int i = 0; i < nVars; i++) {
			file.write ("\t" + lBound + " <= x" + (i+1) + " <= " + uBound + "\n");
		}
		file.write ("Integers\n\t");
		for (int i = 0; i < nVars; i++) {
			file.write("x" + (i + 1) + " ");
		}
		file.write ("\nEnd");
		file.close ();
	}
}
