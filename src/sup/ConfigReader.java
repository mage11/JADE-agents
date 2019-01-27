package sup;

import obj.ConfigObj;
import obj.ReceiverObj;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ConfigReader {
    public static ConfigObj readFile(String pathFile){
        String line;
        String[] lines;
        String csvSplitBySymbol = ",";

        ArrayList<ReceiverObj> receiverObjArrayList  = new ArrayList<>();
        ConfigObj configObj = new ConfigObj();

        try(BufferedReader bk = new BufferedReader(new FileReader(pathFile)) )  {

            line = bk.readLine();
            configObj.setNumberOfAttributes(Integer.parseInt(line));

            while ((line = bk.readLine()) != null) {
                lines = line.split(csvSplitBySymbol);

                ReceiverObj receiverObj = new ReceiverObj();
                receiverObj.setName(lines[0]);
                receiverObj.setAddress(lines[1]);
                receiverObj.setPath(lines[2]);

                receiverObjArrayList.add(receiverObj);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        configObj.setReceiverObjArrayList(receiverObjArrayList);
        return configObj;
    }
}
