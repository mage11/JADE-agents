package obj;

import java.util.ArrayList;

public class ConfigObj {
    private int numberOfAttributes;
    private ArrayList<ReceiverObj> receiverObjArrayList = new ArrayList<>();

    public ConfigObj() {
    }

    public void setNumberOfAttributes(int numberOfAttributes) {
        this.numberOfAttributes = numberOfAttributes;
    }

    public void setReceiverObjArrayList(ArrayList<ReceiverObj> receiverObjArrayList) {
        this.receiverObjArrayList = receiverObjArrayList;
    }

    public int getNumberOfAttributes() {
        return numberOfAttributes;
    }

    public ArrayList<ReceiverObj> getReceiverObjArrayList() {
        return receiverObjArrayList;
    }
}
