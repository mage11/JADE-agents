package sup;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CSVReaderNew {
    public static Map<String,ArrayList<double[]>> parseCSV(int numberOfAttributes, String csvFile){
        String line;
        String[] lines;
        double[] attributes = new double[numberOfAttributes];
        double[][] avg = new double[2][numberOfAttributes];
        double[][] disp = new double[2][numberOfAttributes];

        int counter_1 = 0;
        int counter_2 = 0;

        Map<String,ArrayList<double[]>> map = new HashMap<>();
        String csvSplitBy = ",";

        avg[0][numberOfAttributes-1] = 1; //first class;
        avg[1][numberOfAttributes-1] = 2; //second class;
        disp[0][numberOfAttributes-1] = 1;
        disp[1][numberOfAttributes-1] = 2;

        try(
                BufferedReader bk = new BufferedReader(new FileReader(csvFile)) )  {
            bk.readLine();
            while ((line = bk.readLine()) != null) {
                lines = line.split(csvSplitBy);
                double clss = Double.parseDouble(lines[numberOfAttributes-1]);
                if(clss == avg[0][numberOfAttributes-1]){
                    counter_1++;
                }
                else{
                    counter_2++;
                }

                for(int i = 0; i < numberOfAttributes-1; i++){
                    attributes[i] = Double.parseDouble(lines[i]);

                    if(clss == avg[0][numberOfAttributes-1]){
                        avg[0][i] += attributes[i];
                        disp[0][i] += attributes[i]*attributes[i];
                    }
                    else{
                        avg[1][i] += attributes[i];
                        disp[1][i] += attributes[i]*attributes[i];
                    }
                }
            }

            for (int i = 0; i < numberOfAttributes-1 ; i++) {
                avg[0][i] = avg[0][i]/counter_1;
                avg[1][i] = avg[1][i]/counter_2;

                disp[0][i] = disp[0][i]/counter_1 - avg[0][i]*avg[0][i];
                disp[1][i] = disp[1][i]/counter_2 - avg[1][i]*avg[1][i];
            }

            ArrayList<double[]> dataSet1 = new ArrayList<>();
            ArrayList<double[]> dataSet2 = new ArrayList<>();

            dataSet1.add(avg[0]);
            dataSet1.add(disp[0]);
            map.put("0", dataSet1);

            dataSet2.add(avg[1]);
            dataSet2.add(disp[1]);
            map.put("1", dataSet2);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}
