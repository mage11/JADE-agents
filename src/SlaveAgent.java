import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import sup.CSVReader;
import sup.CSVReaderNew;
import sup.MapSet;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SlaveAgent extends Agent
{
    private int numberOfAttributes = 68; // Number of attributes
    private int numberOfClasses = 2; //Number of classes

    private ArrayList<double[]> dataSet = new ArrayList<>();

    private double[][] avg = new double[numberOfClasses][numberOfAttributes];
    private double[][] gaus = new double[numberOfClasses][numberOfAttributes];

    private Map<String,ArrayList<double[]>> map = new HashMap<>();
    private Map<String,ArrayList<double[]>> testMap = new HashMap<>();


    ACLMessage answ;
    ACLMessage msg;

    long startT;
    long finishT;

    public void setup()
    {
        System.out.println("\nAgent "+this.getAID()+" is started.");
        OpenFile a = new OpenFile();
        addBehaviour(a);
    }

    class OpenFile extends  SimpleBehaviour{
        private boolean finish = false;
        public void action(){
            MessageTemplate mt = MessageTemplate.and(
                    MessageTemplate.MatchPerformative( ACLMessage.REQUEST ),
                    MessageTemplate.MatchLanguage("my-language"));
            msg = receive(mt);

            if(msg != null){
                answ = new ACLMessage(ACLMessage.INFORM);
                answ.addReceiver(msg.getSender());

                 startT = System.currentTimeMillis();

                //dataSet = CSVReader.parseCSV(numberOfAttributes,msg.getContent());

                System.out.println(msg.getContent());
                finish = true;
                AvgCalculate b = new AvgCalculate();
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
    class AvgCalculate extends OneShotBehaviour
    {
        public void action(){
            //initialize
            /*avg[0][numberOfAttributes-1] = 1; //first class;
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

            System.out.println("Cnt1: " + cnt1);
            System.out.println("Cnt2: " + cnt2);

            ArrayList<double[]> resultSet = new ArrayList<>();
            resultSet.add(avg[0]);
            resultSet.add(gaus[0]);
            map.put("0", resultSet);

            ArrayList<double[]> resultSet2 = new ArrayList<>();
            resultSet2.add(avg[1]);
            resultSet2.add(gaus[1]);
            map.put("1", resultSet2);*/

            map = CSVReaderNew.parseCSV(numberOfAttributes,msg.getContent());
            finishT = System.currentTimeMillis();
            System.out.println("Execution time: " + (finishT - startT));

            /*System.out.println("Old map");
            output(map);*/
            System.out.println("Test map:");
            output(map);

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

            answ.setLanguage("my-language");
            send(answ);
        }
    }

    public void output( Map<String,ArrayList<double[]>> map){

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
