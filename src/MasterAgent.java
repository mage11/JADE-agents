import java.io.*;
import java.util.ArrayList;
import java.util.Map;

import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import sup.CSVReader;
import sup.MapSet;

public class MasterAgent extends Agent
{
    private int numberOfAgents = 2;
    private int numberOfAttributes = 5;
    private int numberOfClasses = 2;
    //names of slaves
    private String receiver1 = "slave1";
    private String receiver2 = "slave2";

    //IP of slaves
    private String IP1 = "@192.168.1.101:1099/JADE";
    private String IP2 = "@192.168.1.101:1099/JADE";

    //Files
    private String content1 = "\"test1.csv \"";
    private String content2 = "\"test2.csv \"";
    private String vectorPath = "test.csv";

    private MapSet dataSet1;
    private MapSet dataSet2;
    private Map<String, ArrayList<double[]>> resultSet;
    private double[] vector;
    private double answer;



    public void setup()
    {
        System.out.println("\nMaster Agent "+this.getAID()+" is started.");
        PushMsg a = new PushMsg();
        addBehaviour(a);
    }
    class PushMsg extends OneShotBehaviour
    {
        public void action()
        {

            ACLMessage msg = setconfig(receiver1, IP1, content1);
            send(msg);

            //send msg to 2nd agent
            ACLMessage msg2 = setconfig(receiver2, IP2, content2);
            send(msg2);

            ReceiveMsg b = new ReceiveMsg();
            addBehaviour(b);

        }

        private ACLMessage setconfig(String receiver, String ip, String content) {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            AID receiverAID = new AID(receiver.concat(ip));
            msg.addReceiver(receiverAID);
            msg.setLanguage("my-language");
            msg.setEncoding("text/plain");
            msg.setOntology("Parents");
            msg.setContent(content);
            return msg;
        }
    }
    class ReceiveMsg extends SimpleBehaviour
    {

        private boolean finish = false;
        public void action(){
            MessageTemplate mt = MessageTemplate.and(
                    MessageTemplate.MatchPerformative( ACLMessage.REQUEST),
                    MessageTemplate.MatchLanguage("my-language"));

            ACLMessage msg = receive(mt);
            if(msg != null){
                if(msg.getSender().equals(receiver1)){
                    try{
                        dataSet1 = (MapSet) msg.getContentObject();
                    } catch (UnreadableException e){}
                }
                if(msg.getSender().equals(receiver2)){
                    try{
                        dataSet2 = (MapSet) msg.getContentObject();
                    } catch (UnreadableException e){}

                    finish = true;
                    Reduce c = new Reduce();
                    addBehaviour(c);
                }
            }
            else{
                block();
            }

        }
        public boolean done(){
            return finish;
        }
    }

    class Reduce extends OneShotBehaviour {
        public void action() {
            resultSet.put("0", sum(dataSet1.map.get("0"), dataSet2.map.get("0")));
            resultSet.put("1", sum(dataSet1.map.get("1"), dataSet2.map.get("1")));

            vector = CSVReader.parseCSV(numberOfAttributes,vectorPath).get(0); //read vector X

            CalculateProb d = new CalculateProb();
            addBehaviour(d);

        }


        private ArrayList<double[]> sum(ArrayList <double[]> lista, ArrayList <double[]> listb){
            ArrayList<double[]> results = new ArrayList<>();
            for (int i = 0; i < 2; i++) { // 2 -> avg and gaus values
                double[] tmp = new double[numberOfAttributes-1];
                for (int j = 0; j < numberOfAttributes-1; j++) {
                    tmp[j] = (lista.get(i)[j] + listb.get(i)[j]) / numberOfAgents;
                }
                results.add(tmp);
            }

            return results;
        }
    }

    class CalculateProb extends OneShotBehaviour{
        public void action(){
            double[][] prob = new double[numberOfClasses][numberOfAttributes];
            int cnt = 0;

            for(Map.Entry entry : resultSet.entrySet()){
                ArrayList<double[]> tmp = (ArrayList<double[]>) entry.getValue();
                double[] u = tmp.get(0);
                double[] g = tmp.get(1);
                for (int i = 0; i < numberOfAttributes-1; i++) {
                    prob[cnt][i] = (1/Math.sqrt(2*Math.PI*g[i]))*Math.exp(-1*(Math.pow((vector[i]-u[i]),2)/(g[i]*2)));
                }
                cnt++;
            }


            double [][] resultProb = new double[numberOfClasses][1];
            for (int i = 0; i < numberOfClasses; i++) {
                double tmp = 1;
                for (int j = 0; j < numberOfAttributes-1 ; i++) {
                    tmp *= prob[cnt][j];
                }
                resultProb[cnt][1] = tmp;
                cnt++;
            }


            System.out.println("1st class: " + resultProb[0][0]);
            System.out.println("2nd class: " + resultProb[1][0]);
        }
    }
}