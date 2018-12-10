package sup;

import java.util.ArrayList;
import java.util.Map;

public class OutputMap {
    public static void output( Map<String, ArrayList<double[]>> map){

        System.out.println("-----------------------------------");
        for (Map.Entry entry : map.entrySet()){
            ArrayList<double[]> tmp = (ArrayList<double[]>) entry.getValue();

            for (double[] d : tmp){
                for (int i = 0; i < d.length; i++) {
                    System.out.print(d[i] + " ");
                }
                System.out.println();
            }
            System.out.println("\n");
        }

        System.out.println("----------------------------------------\n ");
    }
}
