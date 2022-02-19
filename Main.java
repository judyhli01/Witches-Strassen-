//Judy Li
import java.io.*;
import java.lang.*;
import java.util.Scanner;

class Main {
  public static void main(String[] args) {
    int size = 0;
    try{
      Scanner read = new Scanner(new File("input.txt"));
      while(read.hasNext()) {
        size++;
        String trash = read.nextLine();
      }
    } catch(FileNotFoundException fnf) {
      System.out.println("File was not found.");
    }

    double matrix[][] = new double[size-1][size];

    try{
      Scanner read = new Scanner(new File("input.txt"));
      String[] values = read.nextLine().split(":");
      int [] vals = new int[values.length];
      for (int i=0; i<values.length; i++) {
        vals[i] = Integer.valueOf(values[i]);
      }
      for (int i=0; i<matrix.length;i++) {
        matrix[i][size-1] = vals[i];
      }

      int count = 0;
      while(read.hasNext()) {
        values = read.nextLine().split(":");
        vals = new int[values.length];
        for (int i=0; i<values.length; i++) {
          vals[i] = Integer.valueOf(values[i]);
        }
        for (int i=0; i<matrix.length;i++) {
          matrix[i][count] = vals[i];
        }
        count++;
      }
    } catch(FileNotFoundException fnf) {
      System.out.println("File was not found.");
    }


    //eliminate the bottom triangle and convert to 0
    int q = 0;
    int c = 1;
    boolean done = false;
    while (!done) {
      for (c=1; c<matrix.length-q; c++) {
        double x = -matrix[q+c][q]/matrix[q][q];
        matrix[c+q] = eliminate(matrix[q], matrix[c+q], x);
      }
      if (q == matrix.length-2) {
        done = true;
      }
      q++;
    }

    //find k (multiply with solutions)
    int k = 0;


    // find gcd
    double a = matrix[size-2][size-2];
    double b = matrix[size-2][size-1];
    if (a < 0) 
      a = -1*matrix[size-2][size-2];
    if (b < 0)
      b = -1*matrix[size-2][size-1];
    int gcd = gcdCalculator((int)a, (int)b);
    // test if gcd is 1
    if (gcd == 1) {
      k = 1;
    }
    else if (gcd != 1) {
      // k = divide 28/gcd
      k = (int)(matrix[size-2][size-2]/gcd);
      if (k < 0) {
        k = -1*k;
      }
    }


    // multiply solutions by k
    for (int i=0; i<matrix.length; i++) {
      matrix[i][size-1] = matrix[i][size-1]*k;
    }


    // finish echelon form
    //make middle column all 1 (row echelon form)
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j <= i; j++) {
        if (i == j && (matrix[i][j] != 1)) {
          matrix[i] = divideRow(matrix[i], matrix[i][j]);
        }
      }
    }


    //after gaussian, if bottom is all 0, then no answer
    double max = 0;
    for (int i=0; i<matrix[0].length; i++) {
      if (matrix[matrix.length-1][i] > max) {
        max = matrix[matrix.length-1][i];
      }
    }

    int [] finalVar = new int [matrix.length];
    double []variables = new double[matrix.length];
    // no solution
    if (max == 0) {
      for (int i=0; i<matrix.length; i++) {
        finalVar[i] = -1;
      }
    }
    // find variables
    else {
      variables[variables.length-1] = matrix[matrix.length-1][matrix[0].length-1];
      for (int i=variables.length-2; i>=0; i--) {
        variables[i] = findVariable(matrix[i], variables, i);
      }
      //insert into finalVar
      for (int i=0; i<variables.length; i++) {
        finalVar[i] = (int)Math.round(variables[i]);
      }
    }
    
    
    try{
      PrintWriter writer = new PrintWriter("output.txt");
      for (int i = 0; i < finalVar.length-1; i++) {
        writer.println(finalVar[i]);
      }
      writer.print(finalVar[finalVar.length-1]);
      writer.close();
    } catch(FileNotFoundException fnf){
      System.out.println("File was not found.");
    }
  }


  public static int gcdCalculator(int a, int b) {
    int bigger, smaller;
    int divisor;
    int tempRemainder;
    int remainder = 1;
    //find the bigger number
    if (a == b) {
      return a;
    } else if (a < b) {
      if (b % a == 0) {
        return a;
      }
      bigger = b;
      smaller = a;
    } else {
      if (a % b == 0) {
        return b;
      }
      bigger = a;
      smaller = b;
    }
    
    divisor = bigger/smaller;
    tempRemainder = bigger - (smaller*divisor);

    while (tempRemainder > 0) {
      bigger = smaller;
      smaller = tempRemainder;
      divisor = bigger/smaller;
      if (bigger % smaller == 0) {
        return smaller;
      }
      tempRemainder = bigger - (smaller*divisor);
      if (tempRemainder != 0) {
        remainder = tempRemainder;
      }
    }
    return remainder;  
  }


  public static double findVariable(double row[], double variables[], int count) {
    double sum = row[row.length-1];
    for (int i=count; i<variables.length; i++) {
      sum -= row[i]*variables[i];
    }
    return sum;
  }

  //get 1
  public static double[] divideRow(double row[], double divisor) {
    double result[] = new double[row.length];
    for (int i = 0; i < row.length; i++) {
      result[i] = row[i]/divisor;
    }
    return result;
  }
  //get 0 at that point
  public static double[] eliminate(double row1[], double row2[], double x) {
    double result[] = new double[row1.length];
    for (int i = 0; i<row1.length; i++) {
      result[i] = (row1[i]*x) + row2[i];
    }
    return result;
  }
}