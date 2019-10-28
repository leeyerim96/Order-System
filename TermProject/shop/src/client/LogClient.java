package client;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.Context;
import java.util.Hashtable;
import javax.naming.NamingException;


public class LogClient {

    public static void main(String[] args) throws Exception {
        // Initialize
        Context ctx = getInitialContext("t3://localhost:7001", "weblogic", "weblogic123");

        // 1: Lookup connection factory        
        TopicConnectionFactory factory = 
            (TopicConnectionFactory) ctx.lookup("weblogic.jms.ConnectionFactory");
        
        QueueConnectionFactory factory2 =
            (QueueConnectionFactory) ctx.lookup("weblogic.jms.ConnectionFactory"); //ok

        // 2: Use connection factory to create JMS connection
        TopicConnection connection = factory.createTopicConnection();
        QueueConnection connection2 = factory2.createQueueConnection(); //ok
        // 3: Use connection to create a session
        TopicSession session = 
            connection.createTopicSession(false,Session.AUTO_ACKNOWLEDGE);
        
        QueueSession session2 =
            connection2.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);

        // 4: Lookup destination 
        Queue topic = (Queue)ctx.lookup("./jmsqueue");
        /*
        참고 레퍼런스
        http://docs.oracle.com/javaee/6/api/javax/jms/package-summary.html
        */
        
        // 5: Create a message publisher 
        QueueSender sender = session2.createSender(topic);   //error

        // 6: Create and publish a message
        TextMessage msg = session2.createTextMessage();
        msg.setText("330814231010172"); //여기에 주문번호 넣기
        sender.send(msg);
        
        // finish
        //publisher.close();
        System.out.println("Message published. Please check application server's console to see the response from MDB.");
    }

    private static Context getInitialContext(String url, String username, String password)
        throws NamingException
        {
            Hashtable<String, String> env = new Hashtable<String, String>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
            env.put(Context.PROVIDER_URL, url);
            env.put(Context.SECURITY_PRINCIPAL, username);
            env.put(Context.SECURITY_CREDENTIALS, password);

            return new InitialContext(env);
        }

}