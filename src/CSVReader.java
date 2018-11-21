import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVReader {
    public static ArrayList<double[]> parseCSV(int numberOfAttributes, String csvFile){
        String line;
        String[] lines;
        double[] attributes;
        ArrayList<double[]> dataSet = new ArrayList<>();
        String csvSplitBy = ",";

        try(
                BufferedReader bk = new BufferedReader(new FileReader(csvFile)) )  {
                //bk.readLine(); // If first string is useless, don't touch
                while ((line = bk.readLine()) != null) {
                attributes = new double[numberOfAttributes];
                lines = line.split(csvSplitBy);
                for(int i = 0; i < numberOfAttributes; i++){
                    attributes[i] = Double.parseDouble(lines[i]);
                }
                dataSet.add(attributes);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataSet;
    }
}
