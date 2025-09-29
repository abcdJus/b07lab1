import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Polynomial {
    private double[] coef;
    private int[] exp;

    Polynomial() {
    }

    Polynomial(double[] coef, int[] exp) {
        this.coef = coef;
        this.exp = exp;
    }

    Polynomial(File file) {
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            line = br.readLine();
        } catch (Exception num2) {
            this.coef = new double[0];
            this.exp = new int[0];
            return;
        }

        if (line == null || line.trim().length() == 0) {
            this.coef = new double[0];
            this.exp = new int[0];
            return;
        }

        String str = line.trim();

        StringBuilder normalized = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char num = str.charAt(i);
            if (i > 0 && num == '-') normalized.append("+-");
            else normalized.append(num);
        }

        String[] terms = normalized.toString().split("\\+");

        double[] tmpCoef = new double[terms.length];
        int[] tmpExp = new int[terms.length];
        int size = 0;

        for (String term : terms) {
            if (term == null || term.trim().isEmpty()) continue;
            term = term.trim();

            double coeffValue;
            int exponentValue;

            int xIndex = term.indexOf('x');
            if (xIndex >= 0) {
                String coeffStr = term.substring(0, xIndex);
                String expStr = term.substring(xIndex + 1);

                if (coeffStr.length() == 0 || "+".equals(coeffStr)) coeffValue = 1.0;
                else if ("-".equals(coeffStr)) coeffValue = -1.0;
                else coeffValue = Double.parseDouble(coeffStr);

                exponentValue = (expStr.length() == 0) ? 1 : Integer.parseInt(expStr);
            } else {
                coeffValue = Double.parseDouble(term);
                exponentValue = 0;
            }

            boolean found = false;
            for (int i = 0; i < size; i++) {
                if (tmpExp[i] == exponentValue) {
                    tmpCoef[i] += coeffValue;
                    found = true;
                    break;
                }
            }

            if (!found) {
                tmpCoef[size] = coeffValue;
                tmpExp[size] = exponentValue;
                size++;
            }
        }

        int count = 0;
        for (int i = 0; i < size; i++) {
            if (tmpCoef[i] != 0.0) count++;
        }

        if (count == 0) {
            this.coef = new double[0];
            this.exp = new int[0];
            return;
        }

        this.coef = new double[count];
        this.exp = new int[count];
        int k = 0;
        for (int i = 0; i < size; i++) {
            if (tmpCoef[i] != 0.0) {
                this.coef[k] = tmpCoef[i];
                this.exp[k] = tmpExp[i];
                k++;
            }
        }
    }   

    void saveToFile(String filename) {
        StringBuilder sb = new StringBuilder();

        if (this.coef == null || this.coef.length == 0) {
            sb.append("0");
        } else {
            boolean yes = true;
            for (int i = 0; i < this.coef.length; i++) {
                double num = this.coef[i];
                int num2 = this.exp[i];
                
                if (Math.abs(num) == 0) continue;

                if (yes) {
                    if (num < 0) sb.append("-");
                } else {
                    if (num < 0) sb.append("-");
                    else sb.append("+");
                }

                double absCoef = Math.abs(num);
                
                if (num2 == 0) {
                    if (absCoef == (long)absCoef) {
                        sb.append((long)absCoef);
                    } else {
                        sb.append(absCoef);
                    }
                } else {
                    if (Math.abs(absCoef - 1.0) != 0) {
                        if (absCoef == (long)absCoef) {
                            sb.append((long)absCoef);
                        } else {
                            sb.append(absCoef);
                        }
                    }
                    
                    sb.append("x");
                    if (num2 != 1) {
                        sb.append(num2);
                    }
                }

                yes = false;
            }

            if (sb.length() == 0) sb.append("0");
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write(sb.toString());
        } catch (Exception num2) {
        }
    }

    Polynomial add(Polynomial p) {
        if (p == null) return new Polynomial(this.coef, this.exp);

        int maxLength = this.coef.length + p.coef.length;
        double[] tempCoefs = new double[maxLength];
        int[] tempexp = new int[maxLength];
        int currentSize = 0; 

        for (int i = 0; i < this.coef.length; i++) {
            tempCoefs[currentSize] = this.coef[i];
            tempexp[currentSize] = this.exp[i];
            currentSize++;
        }

        for (int i = 0; i < p.coef.length; i++) {
            int pExp = p.exp[i];
            double pCoef = p.coef[i];
            boolean foundMatch = false;

            for (int j = 0; j < currentSize; j++) {
                if (tempexp[j] == pExp) {
                    tempCoefs[j] += pCoef;
                    foundMatch = true;
                    break;
                }
            }

            if (!foundMatch) {
                tempCoefs[currentSize] = pCoef;
                tempexp[currentSize] = pExp;
                currentSize++;
            }
        }

        int finalSize = 0;
        for (int i = 0; i < currentSize; i++) {
            if (Math.abs(tempCoefs[i]) != 0) finalSize++;
        }

        if (finalSize == 0) {
            return new Polynomial();
        }

        double[] resultcoef = new double[finalSize];
        int[] resultexp = new int[finalSize];
        int destIndex = 0;
        for (int i = 0; i < currentSize; i++) {
            if (Math.abs(tempCoefs[i]) != 0) {
                resultcoef[destIndex] = tempCoefs[i];
                resultexp[destIndex] = tempexp[i];
                destIndex++;
            }
        }

        return new Polynomial(resultcoef, resultexp);
    }

    public Polynomial multiply(Polynomial p) {
        if (p == null) return new Polynomial();
        
        if (this.coef.length == 0 || (this.coef.length == 1 && this.coef[0] == 0) || p.coef.length == 0 || (p.coef.length == 1 && p.coef[0] == 0)) return new Polynomial();

        int maxProductTerms = this.coef.length * p.coef.length;
        double[] tempCoefs = new double[maxProductTerms];
        int[] tempexp = new int[maxProductTerms];
        int currentSize = 0;

        for (int i = 0; i < this.coef.length; i++) {
            for (int j = 0; j < p.coef.length; j++) {
                double productCoef = this.coef[i] * p.coef[j];
                int productExp = this.exp[i] + p.exp[j];

                if (Math.abs(productCoef) == 0) continue;

                boolean foundMatch = false;
                for (int k = 0; k < currentSize; k++) {
                    if (tempexp[k] == productExp) {
                        tempCoefs[k] += productCoef;
                        foundMatch = true;
                        break;
                    }
                }

                if (!foundMatch) {
                    tempCoefs[currentSize] = productCoef;
                    tempexp[currentSize] = productExp;
                    currentSize++;
                }
            }
        }

        int finalSize = 0;
        for (int i = 0; i < currentSize; i++) {
            if (Math.abs(tempCoefs[i]) > 0) finalSize++;
        }

        if (finalSize == 0) return new Polynomial();

        double[] resultcoef = new double[finalSize];
        int[] resultexp = new int[finalSize];
        int destIndex = 0;
        for (int i = 0; i < currentSize; i++) {
            if (Math.abs(tempCoefs[i]) > 0) {
                resultcoef[destIndex] = tempCoefs[i];
                resultexp[destIndex] = tempexp[i];
                destIndex++;
            }
        }

        return new Polynomial(resultcoef, resultexp);
    }

    public double evaluate(double x) {
        double sum = 0;
        for (int i = 0; i < coef.length; i++) {
            sum += coef[i] * Math.pow(x, exp[i]);
        }
        return sum;
    }

    public boolean hasRoot(double x) {
        return evaluate(x) == 0;
    }
}


