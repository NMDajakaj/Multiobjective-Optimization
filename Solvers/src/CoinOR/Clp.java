package CoinOR;

import com.quantego.clp.CLP;
import com.quantego.clp.CLPVariable;

public class Clp {
	public static void main (String[] args) {
		CLP solver = new com.quantego.clp.CLP().buffer(2).maximization();
		
		/*CLPVariable x1 = solver.addVariable().ub(40).lb(0.0).obj(1);
		CLPVariable x2 = solver.addVariable().obj(2);
		CLPVariable x3 = solver.addVariable().obj(3);
		CLPVariable x4 = solver.addVariable().ub(3).lb(2).obj(1);
		
		solver.createExpression().add(-1, x1).add(1, x2).add(1, x3).add(10, x4).leq(20);
		solver.createExpression().add(1, x1).add(-3, x2).add(1, x3).leq(30);
		solver.createExpression().add(1, x2).add(-3.5, x4).eq(0);*/
		
		CLPVariable x0 = solver.addVariable().ub(1.0);
	    CLPVariable x1 = solver.addVariable().ub(0.3).obj(2.655523).name("var_1");
	    CLPVariable x2 = solver.addVariable().ub(0.3).obj(-2.70917);
	    CLPVariable x3 = solver.addVariable().free().obj(1);
	    solver.createExpression().add(-2,x0).add(-1.484345,x0).add(x3).leq(0.302499);
	    solver.createExpression().add(-3.074807,x0).add(x3).leq(0.507194);
	    solver.createExpression().add(x0).add(1.01,x1).add(-.99,x2).eq(0.594).name("eq_ctr");
		
		System.out.println(solver.toString());
		
		solver.solve();
		System.out.println (" -->  Valor objetivo: " + solver.getObjectiveValue());
		
		System.out.println (" Variable " + 1 + " : " + x0.getSolution());
		System.out.println (" Variable " + 2 + " : " + x1.getSolution());
		System.out.println (" Variable " + 3 + " : " + x2.getSolution());
		System.out.println (" Variable " + 4 + " : " + x3.getSolution());
	}
}
