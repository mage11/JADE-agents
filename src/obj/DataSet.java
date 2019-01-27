package obj;

import java.io.Serializable;

public class DataSet implements Serializable {
    private static final long serialVersionUID = -8082739947519804587L;
    private double[] averageValues;
    private double[] dispersion;
    private int countRows = 0;

    public DataSet(int n) {
        averageValues = new double[n-1];
        dispersion = new double[n-1];
    }

    public void countRowsIncrease() {countRows++;}

    public void addToCountRows(int countRows) {this.countRows += countRows;}

    public void setAverageValues(double[] averageValues) {
        for (int i = 0; i < averageValues.length; i++) {
            this.averageValues[i] += averageValues[i];
        }
    }

    public void setDispersion(double[] dispersion) {
        for (int i = 0; i < dispersion.length; i++) {
            this.dispersion[i] += dispersion[i];
        }
    }

    public void calculateFinalData(){
        for (int i = 0; i < averageValues.length; i++) {
            averageValues[i] = averageValues[i]/countRows;
            dispersion[i] = dispersion[i]/countRows - averageValues[i]*averageValues[i];
        }
    }

    public double[] getAverageValues() {
        return averageValues;
    }

    public double[] getDispersion() {
        return dispersion;
    }

    public int getCountRows() {return countRows;}
}
