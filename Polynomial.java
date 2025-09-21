public class Polynomial {
  double[] coeff;

  public Polynomial(){
    this.coeff = new double[] {0};
  }
  public Polynomial(double[] coeffArray){
    this.coeff = coeffArray;
  }
  public Polynomial add(Polynomial other){
    int maxlen = Math.max(this.coeff.length, other.coeff.length);
    double[] result = new double[maxlen];
    double a;
    double b;
    for (int i=0; i<maxlen; i++){
      if (i<this.coeff.length){
        a = this.coeff[i];
      }
      else{
        a = 0;
      }
      if (i<other.coeff.length){
        b = other.coeff[i];
      }
      else{
        b = 0;
      }
      result[i] = a+b;
      }
    Polynomial sum = new Polynomial(result);
    return sum;
  }

  public double evaluate(double x){
    double result = 0;
    for (int i=0; i<this.coeff.length; i++){
      result += this.coeff[i] * (Math.pow(x,i));
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
}