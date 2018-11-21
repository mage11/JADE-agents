import java.io.*;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.*;

public class MasterAgent extends Agent
{
    private int numberOfAgents = 2;
    //names of slaves
    private String receiver1 = "slave1";
    private String receiver2 = "slave2";

    //IP of slaves
    private String IP1 = "@192.168.1.101:1099/JADE";
    private String IP2 = "@192.168.1.101:1099/JADE";

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
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            AID receiverAID = new AID(receiver1.concat(IP1));
            msg.addReceiver(receiverAID);
            msg.setLanguage("my-language");
            msg.setEncoding("text/plain");
            msg.setOntology("Parents");
            msg.setContent("\"test1.cvs \""); //File name
            System.out.println(msg.toString());
            send(msg);

            //send msg to 2nd agent
            ACLMessage msg2 = new ACLMessage(ACLMessage.REQUEST);
            AID receiverAID2 = new AID(receiver2.concat(IP2));
            msg2.addReceiver(receiverAID2);
            msg2.setLanguage("my-language");
            msg2.setEncoding("text/plain");
            msg2.setOntology("Parents");
            msg2.setContent("\"test2.cvs \""); //File name
            System.out.println(msg2.toString());
            send(msg2);

            ReceiveMsg b = new ReceiveMsg();
            addBehaviour(b);

        }
    }
    class ReceiveMsg extends SimpleBehaviour
    {
        /*private Object object1;
        private Object object2;*/

        private boolean finish = false;
        public void action(){
            MessageTemplate mt = MessageTemplate.and(
                    MessageTemplate.MatchPerformative( ACLMessage.REQUEST),
                    MessageTemplate.MatchLanguage("my-language"));

            ACLMessage msg = receive(mt);
            if(msg != null){
                if(msg.getSender().equals(receiver1)){
                    /*try{
                        Object object1 = (Object) msg.getContentObject();
                    } catch (UnreadableException e){}*/
                }
                if(msg.getSender().equals(receiver2)){
                    /*try{
                        Object object2 = (Object) msg.getContentObject();
                    } catch (UnreadableException e){}*/
                    String s = "s";
                    Reduce c = new Reduce();
                    addBehaviour(c);
                    finish = true;
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

    class Reduce extends OneShotBehaviour
    {
        public void action(){


        }
    }
}