import java.io.File;

public class Driver {
	public static void main(String [] args) {
		double[] cz = {};
		int[] ez = {};
		Polynomial p = new Polynomial(cz, ez);
		System.out.println("p(3) = " + p.evaluate(3));

		double [] c1 = {6, 0, 0, 5};
		int [] e1 = {0, 1, 2, 3};
		Polynomial p1 = new Polynomial(c1, e1);

		double [] c2 = {0, -2, 0, 0, -9};
		int [] e2 = {0, 1, 2, 3, 4};
		Polynomial p2 = new Polynomial(c2, e2);

		Polynomial s = p1.add(p2);
		System.out.println("s(0.1) = " + s.evaluate(0.1));
		if (s.hasRoot(1))
			System.out.println("1 is a root of s");
		else
			System.out.println("1 is not a root of s");

		Polynomial m = p1.multiply(p2);
		System.out.println("m(1) = " + m.evaluate(1));
		System.out.println("m(2) = " + m.evaluate(2));

		String fname = "poly.txt";
		s.saveToFile(fname);
		try {
			Polynomial fromFile = new Polynomial(new File(fname));
			System.out.println("fromFile(0.1) = " + fromFile.evaluate(0.1));
		} catch (Exception ex) {
			System.out.println("File load failed: " + ex.getMessage());
		}
	}		
}