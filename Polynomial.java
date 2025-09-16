public class Polynomial {
    private double[] coef;

    Polynomial() {  
        coef = new double[]{0};
    }

    Polynomial(double[] coef) {
        this.coef = coef;
    }

    Polynomial add(Polynomial p) {
        int len = this.coef.length;

        if (p.coef.length > len) 
            len = p.coef.length;
    

        double[] result = new double[len];

        for (int i = 0; i < len; i++) {
            double num1 = 0;
            double num2 = 0;

            if (i < this.coef.length) 
                num1 = this.coef[i];
            
            if (i < p.coef.length) 
                num2 = p.coef[i];
            

            result[i] = num1 + num2;
        }

        return new Polynomial(result);
    }

    public double evaluate(double x) {
        double sum = 0;

        for (int i = 0; i < coef.length; i++) 
            sum += coef[i] * Math.pow(x, i);
        
        return sum;
    }

    public boolean hasRoot(double x) {
        return evaluate(x) == 0;
    }
}