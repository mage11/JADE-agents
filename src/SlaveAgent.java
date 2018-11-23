import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import sup.CSVReader;
import sup.MapSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SlaveAgent extends Agent
{
    private int numberOfAttributes = 5; // Number of attributes
    private int numberOfClasses = 2; //Number of classes

    private double[] attributes;
    private ArrayList<double[]> dataSet = new ArrayList<>();

    private double[][] avg = new double[numberOfClasses][numberOfAttributes];
    private double[][] gaus = new double[numberOfClasses][numberOfAttributes];

    private Map<String,ArrayList<double[]>> map = new HashMap<>();

    ACLMessage answ;

    public void setup()
    {
        System.out.println("\nAgent "+this.getAID()+" is started.");
        OpenFile a = new OpenFile();
        addBehaviour(a);
    }
    class OpenFile extends OneShotBehaviour
    {
        public void action()
        {
            MessageTemplate mt = MessageTemplate.and(
                    MessageTemplate.MatchPerformative( ACLMessage.REQUEST ),
                    MessageTemplate.MatchLanguage("my-language"));

            ACLMessage msg = receive(mt);
            answ = new ACLMessage(ACLMessage.INFORM);
            answ.addReceiver(msg.getSender());

            dataSet = CSVReader.parseCSV(numberOfAttributes,msg.getContent());


            AvgCalculate b = new AvgCalculate();
            addBehaviour(b);
        }
    }
    class AvgCalculate extends OneShotBehaviour
    {
        public void action(){
            //initialize
            avg[0][numberOfAttributes-1] = 1; //first class;
            avg[1][numberOfAttributes-1] = 2; //second class;
            gaus[0][numberOfAttributes-1] = 1;
            gaus[1][numberOfAttributes-1] = 2;
            int cnt1 = 0;
            int cnt2 = 0;

            //average of each attribute
            for (int i = 0; i < numberOfAttributes-1; i++) {
                for(double[] tmp : dataSet) {
                    if(tmp[numberOfAttributes-1] == avg[0][numberOfAttributes-1] ){
                        avg[0][i] += tmp[i];
                        if(i == 0){
                            cnt1++;
                        }
                    }
                    else{
                        avg[1][i] += tmp[i];
                        if(i == 0){
                            cnt2++;
                        }
                    }
                }
                avg[0][i] = avg[0][i]/cnt1;
                avg[1][i] = avg[1][i]/cnt2;
            }

            //gauss distribution of each attribute
            for (int i = 0; i < numberOfAttributes-1; i++) {
                for(double[] tmp : dataSet){
                    if(tmp[numberOfAttributes-1] == gaus[0][numberOfAttributes-1] ){
                        gaus[0][i] += Math.pow((tmp[i] - avg[0][i]),2);
                    }
                    else{
                        gaus[1][i] += Math.pow((tmp[i] - avg[1][i]),2);
                    }
                }
                gaus[0][i] = gaus[0][i]/cnt1;
                gaus[1][i] = gaus[1][i]/cnt2;
            }

            ArrayList<double[]> resultSet = new ArrayList<>();
            resultSet.add(avg[0]);
            resultSet.add(gaus[0]);
            map.put("0", resultSet);

            ArrayList<double[]> resultSet2 = new ArrayList<>();
            resultSet2.add(avg[1]);
            resultSet2.add(gaus[1]);
            map.put("1", resultSet2);

            SenderMsg c = new SenderMsg();
            addBehaviour(c);
        }
    }

    class SenderMsg extends OneShotBehaviour
    {
        public void action(){
            try{
                answ.setContentObject(new MapSet(map)); //serializable
            } catch (IOException e){}

            answ.setLanguage("human-language");
            send(answ);
        }
    }
}
