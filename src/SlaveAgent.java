import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.*;

public class SlaveAgent extends Agent
{
    public void setup()
    {
        System.out.println("\nAgent "+this.getAID()+" is started.");
        myBehaviour b=new myBehaviour();
        System.out.println("\nNow agent "+getAID()+" is receiving a message...");
        addBehaviour(b);
        if(b.finished)
            removeBehaviour(b);
    }
    class myBehaviour extends CyclicBehaviour
    {
        private boolean finished = false;
        public void action()
        {
            MessageTemplate mt = MessageTemplate.and(
                    MessageTemplate.MatchPerformative( ACLMessage.QUERY_IF ),
                    MessageTemplate.MatchLanguage("my-language"));

            ACLMessage msg = receive(mt);
            if (msg != null)
            {
                System.out.println("Message: " + msg.getContent() + " was sended by " + msg.getSender());

                ACLMessage answ = new ACLMessage(ACLMessage.INFORM);
                answ.addReceiver(msg.getSender());
                answ.setContent("Hello Master. I am agent "+ answ.getSender());
                answ.setLanguage("human-language");
                answ.setOntology("Parents");
                send(answ);
            }
            else
            {
                block();
            }
        }
    }
}
