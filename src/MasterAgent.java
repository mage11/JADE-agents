import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import obj.ConfigObj;
import obj.DataSet;
import obj.MapSet;
import obj.ReceiverObj;
import sup.ConfigReader;
import sup.OutputMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MasterAgent extends Agent {

    private String filePath = "D:\\config.csv";
    private ArrayList<ReceiverObj>  receivers;
    private ConfigObj configObj = new ConfigObj();
    private Map<Integer, DataSet> resultMap = new HashMap<>();
    private ArrayList<Map<Integer, DataSet>>  listOfMaps = new ArrayList();
    private int countAgents;

    private long startT;
    private long finishT;

    public void setup() {
        startT = System.currentTimeMillis();
        System.out.println("\nMaster Agent " + this.getAID() + " is started.\n");
        SendMessages a = new SendMessages();
        addBehaviour(a);
    }

    class SendMessages extends OneShotBehaviour {
        public void action() {

            configObj = ConfigReader.readFile(filePath);

            receivers = configObj.getReceiverObjArrayList();
            countAgents = ReceiverObj.getCountAgents();

            for (ReceiverObj receiver : receivers){
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                AID receiverAID = new AID(receiver.getName());
                receiverAID.addAddresses(receiver.getAddress());
                msg.addReceiver(receiverAID);
                msg.setContent(receiver.getPath() + "," + configObj.getNumberOfAttributes());
                send(msg);

                System.out.println("Master sent a message to " + receiver.getName());
            }

            ReceiveMsg a = new ReceiveMsg();
            addBehaviour(a);
        }
    }

    class ReceiveMsg extends SimpleBehaviour {

        private boolean finish = false;
        private int countMsg = 0;
        private MapSet tmpMap;



        public void action() {
            ACLMessage msg = myAgent.receive();

            if (msg != null) {
                countMsg++;

                try{
                    tmpMap = (MapSet) msg.getContentObject();
                    System.out.println("Master received a message from " + msg.getSender().getLocalName());

                    listOfMaps.add(tmpMap.getMap());

                } catch (UnreadableException e){ e.printStackTrace();}

                if(countMsg == countAgents) {
                    finish = true;
                    Reduce a = new Reduce();
                    addBehaviour(a);
                }

            } else {
                block();
            }

        }

        public boolean done() {
            return finish;
        }

    }

    class Reduce extends OneShotBehaviour {
        public void action() {

            for(Map<Integer, DataSet> currentMap : listOfMaps){

                for (Map.Entry entry : currentMap.entrySet()){

                    int clss = (int) entry.getKey();
                    if(resultMap.containsKey(clss)){
                        DataSet dataSet = (DataSet) entry.getValue();
                        fillTheResultMap(resultMap.get(clss), dataSet);
                    } else {
                        DataSet dataSet = new DataSet(configObj.getNumberOfAttributes());
                        resultMap.put(clss, dataSet);
                        fillTheResultMap(resultMap.get(clss), dataSet);
                    }

                }

            }
            finalCalculate(resultMap);

            //output
            OutputMap.output(resultMap);

            finishT = System.currentTimeMillis();
            System.out.println("Execution time of jade: " + (finishT - startT));

        }

        private void fillTheResultMap(DataSet mainDataSet, DataSet newDataSet){

            mainDataSet.setDispersion(newDataSet.getDispersion());
            mainDataSet.setAverageValues(newDataSet.getAverageValues());
            mainDataSet.addToCountRows(newDataSet.getCountRows());

        }

        private void finalCalculate(Map<Integer, DataSet> resultMap){

            for (Map.Entry entry : resultMap.entrySet()){
                DataSet dataSet = (DataSet) entry.getValue();
                dataSet.calculateFinalData();
            }

        }

    }
}