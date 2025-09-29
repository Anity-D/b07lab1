import java.io.File;
import java.util.Scanner;
import java.io.FileWriter;

public class Polynomial {
  double[] coeff;
  int [] expo;

  public Polynomial(){
    this.coeff = new double[] {1};
    this.expo = new int[] {0};
  }
  public Polynomial(double[] coeffArray, int[] expoArray){
    if (coeffArray.length != expoArray.length){
      throw new IllegalArgumentException("Coefficients and exponents arrays must have the same length.");
    }
    for (int i=0; i<coeffArray.length; i++){
      if (coeffArray[i] == 0){
        throw new IllegalArgumentException("Coefficients must be non-zero.");
      }
    }
    this.coeff = coeffArray;
    this.expo = expoArray;
  }

  public Polynomial(File polyf) throws Exception{
    String poly;
    try (Scanner f = new Scanner(polyf)){
      if (f.hasNextLine()){
        poly = f.nextLine();
      }
      else{
        throw new Exception("Invalid input: File is empty");
      }
    }

    poly = poly.replace("-", "+-");
    String[] terms = poly.split("\\+");
    double[] resultCoeff = new double[terms.length];
    int[] resultExpo = new int[terms.length];
    for (int i=0; i<terms.length; i++){
      if (!terms[i].isEmpty()){
        double coeff;
        int expo;
        if (terms[i].contains("x")){
          String[] parts = terms[i].split("x");
          if (parts[0].equals("-")){
            coeff = -1;
          }
          else if (parts[0].equals("")){
            coeff = 1;
          }
          else{
            coeff = Double.parseDouble(parts[0]);
          }

          if (parts[1].equals("")){
            expo = 1;
          }
          else{
            expo = Integer.parseInt(parts[1]);
          }
        }
        else{
          coeff = Double.parseDouble(terms[i]);
          expo = 0;
        }
        resultCoeff[i] = coeff;
        resultExpo[i] = expo;
      }
    }
    this.coeff = resultCoeff;
    this.expo = resultExpo;
  }

  public Polynomial add(Polynomial other){
    int i = 0, j = 0, k = 0;
    int n = this.coeff.length;
    int m = other.coeff.length;
    double[] tempCoeff = new double[n + m];
    int[] tempExpo = new int[n + m];

    while (i < n && j < m) {
      if (this.expo[i] == other.expo[j]) {
        tempCoeff[k] = this.coeff[i] + other.coeff[j];
        tempExpo[k] = this.expo[i];
        i++; 
        j++;
        k++;
      }
      else if (this.expo[i] < other.expo[j]) {
        tempCoeff[k] = this.coeff[i];
        tempExpo[k] = this.expo[i];
        i++; 
        k++;
      } 
      else {
        tempCoeff[k] = other.coeff[j];
        tempExpo[k] = other.expo[j];
        j++; 
        k++;
      }
    }
    while (i < n) {
      tempCoeff[k] = this.coeff[i];
      tempExpo[k] = this.expo[i];
      i++; 
      k++;
    }
    while (j < m) {
      tempCoeff[k] = other.coeff[j];
      tempExpo[k] = other.expo[j];
      j++; 
      k++;
    }

    int count = 0;
    for (int t = 0; t < k; t++) {
      if (tempCoeff[t] != 0) count++;
    }
    double[] resultCoeff = new double[count];
    int[] resultExpo = new int[count];
    int idx = 0;
    for (int t = 0; t < k; t++) {
      if (tempCoeff[t] != 0) {
        resultCoeff[idx] = tempCoeff[t];
        resultExpo[idx] = tempExpo[t];
        idx++;
      }
    }
    return new Polynomial(resultCoeff, resultExpo);
  }

  public double evaluate(double x){
    double result = 0;
    for (int i=0; i<this.coeff.length; i++){
      result += this.coeff[i] * (Math.pow(x,expo[i]));
    }
    return result;
  }

  public boolean hasRoot(double x){
    double result = this.evaluate(x);
    if (result == 0){
      return true;
    }
    else {
      return false;
    }
  }

  public Polynomial multiply(Polynomial other){
    int n = this.coeff.length;
    int m = other.coeff.length;;
    double[] tempCoeff = new double[n * m];
    int[] tempExpo = new int[n * m];

    int k = 0;
    for (int i = 0; i < n; i++){
      for (int j = 0; j < m; j++){
        tempCoeff[k] = this.coeff[i] * other.coeff[j];
        tempExpo[k] = this.expo[i] + other.expo[j];
        k++;
      }
    }

    for (int i = 0; i < k; i++){
      for (int j = i+1; j < k; j++){
        if (tempExpo[i] == tempExpo[j]){
          tempCoeff[i] += tempCoeff[j];
          tempCoeff[j] = 0;
        }
      }
    }

    int count = 0;
    for (int t = 0; t < k; t++) {
      if (tempCoeff[t] != 0) count++;
    }
    double[] resultCoeff = new double[count];
    int[] resultExpo = new int[count];
    int idx = 0;
    for (int t = 0; t < k; t++) {
      if (tempCoeff[t] != 0) {
        resultCoeff[idx] = tempCoeff[t];
        resultExpo[idx] = tempExpo[t];
        idx++;
      }
    }

    return new Polynomial(resultCoeff, resultExpo);
  }

  public void saveToFile(String filename) throws Exception{
    try (FileWriter fw = new FileWriter(filename)){
      String poly = "";
      for (int i = 0; i<this.coeff.length; i++){
        double c = this.coeff[i];
        int e = this.expo[i];
        if (i == 0){
          if (c<0){
            poly += "-";
          }
        }
        else{
          if (c<0){
            poly += "-";
          }
          else{
            poly += "+";
          }
        }

      if (Math.abs(c) != 1 || e == 0) {
        poly += String.valueOf(Math.abs(c));
        } 
      else if (Math.abs(c) == 1 && e == 0) {
        poly += "1";
        }
      if (e != 0) {
        poly += "x";
        if (e != 1) {
          poly += String.valueOf(e);
          }
        }
      }
      fw.write(poly);
    }
  }
  
}

