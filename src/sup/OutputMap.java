package sup;

import obj.DataSet;

import java.util.ArrayList;
import java.util.Map;

public class OutputMap {
    public static void output( Map<Integer, DataSet> map){

        System.out.println("\n----------------------------------------");
        for (Map.Entry entry : map.entrySet()){
            DataSet dataSet = (DataSet) entry.getValue();
            double[] avg = dataSet.getAverageValues();
            double[] disp = dataSet.getDispersion();

            System.out.println("Class value = " + entry.getKey());

            System.out.print("Average values: ");
            for (int i = 0; i < avg.length; i++) {
                System.out.print(avg[i] + " ");
            }
            System.out.println();

            System.out.print("Dispersion values: ");
            for (int i = 0; i < disp.length; i++) {
                System.out.print(disp[i] + " ");
            }
            System.out.println("\n");
        }

        System.out.println("\n----------------------------------------\n ");
    }
}