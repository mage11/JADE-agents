import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import sup.CSVReader;
import sup.CSVReaderNew;
import sup.MapSet;
import sup.OutputMap;

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
    class Calculate extends OneShotBehaviour
    {
        public void action(){


            map = CSVReaderNew.parseCSV(numberOfAttributes,msg.getContent());
            finishT = System.currentTimeMillis();
            System.out.println("Execution time: " + (finishT - startT));

            System.out.println("Мap:");
            //OutputMap.output(map);

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
            System.out.println(answ.getSender().getLocalName() + " sent a message");
        }
    }
}
