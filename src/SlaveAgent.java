import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import obj.DataSet;
import obj.MapSet;
import sup.DataReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SlaveAgent extends Agent
{
    private int numberOfAttributes;
    private String filePath;
    private String agentName;

    private Map<Integer, DataSet> map = new HashMap<>();
    private ArrayList<Map<Integer, DataSet>> dataSet = new ArrayList<>();


    private ACLMessage answ;
    private ACLMessage msg;

    long startT;
    long finishT;

    public void setup()
    {
        agentName = this.getAID().toString();
        System.out.println("\nAgent " + agentName + " is started.");
        ReceiveMessage a = new ReceiveMessage();
        addBehaviour(a);
    }

    class ReceiveMessage extends SimpleBehaviour {
        private boolean finish = false;

        public void action(){
            msg = myAgent.receive();

            if(msg != null){
                answ = msg.createReply();

                startT = System.currentTimeMillis();

                String line = msg.getContent();
                String[] values = line.split(",");
                filePath = values[0];
                numberOfAttributes = Integer.parseInt(values[1]);


                System.out.println("\nAgent " + agentName + " received message.");
                finish = true;
                Calculate b = new Calculate();
                addBehaviour(b);
            }
            else{
                block();
            }
        }
        public boolean done(){
            return finish;
        }

    }
    class Calculate extends OneShotBehaviour {
        public void action(){

            DataReader reader = new DataReader();
            map = reader.readData(numberOfAttributes,filePath);

            finishT = System.currentTimeMillis();
            System.out.println("Execution time slave: " + (finishT - startT));

            //System.out.println("Мap:");
            //OutputMap.output(map);

            SendMsg c = new SendMsg();
            addBehaviour(c);
        }

    }

    class SendMsg extends OneShotBehaviour
    {
        public void action(){
            try{
                answ.setContentObject(new MapSet(map)); //serializable
            } catch (IOException e){e.printStackTrace();}

            send(answ);
            System.out.println(answ.getSender().getLocalName() + " sent a message");
        }
    }
}
