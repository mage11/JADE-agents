package sup;

import obj.DataSet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataReader {

    public Map<Integer, DataSet> readData(int numberOfAttributes, String filePath){
        String line;
        String[] lines;
        String csvSplitBy = ",";

        double[] attributes = new double[numberOfAttributes-1];
        Map<Integer, DataSet> map = new HashMap<>();

        try(BufferedReader bk = new BufferedReader(new FileReader(filePath)) )  {
            bk.readLine();
            while ((line = bk.readLine()) != null) {
                lines = line.split(csvSplitBy);
                if(lines.length != numberOfAttributes)
                    continue;

                int clss = Integer.parseInt(lines[numberOfAttributes-1]);
                if(clss == 0)
                    continue;

                if(map.containsKey(clss)){
                    DataSet dataSet = map.get(clss);
                    fillTheData(dataSet, attributes, lines, numberOfAttributes);

                } else {
                    DataSet dataSet = new DataSet(numberOfAttributes);
                    fillTheData(dataSet, attributes, lines, numberOfAttributes);
                    map.put(clss, dataSet);
                }
            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private void fillTheData(DataSet dataSet, double[] attributes,String[] lines, int numberOfAttributes){

        double[] avg = new double[numberOfAttributes-1];
        double[] disp = new double[numberOfAttributes-1];

        for (int i = 0; i < numberOfAttributes-1; i++) {
            attributes[i] = Double.parseDouble(lines[i]);

            avg[i] += attributes[i];
            disp[i] += attributes[i]*attributes[i];
        }

        dataSet.countRowsIncrease();
        dataSet.setAverageValues(avg);
        dataSet.setDispersion(disp);
    }
}
