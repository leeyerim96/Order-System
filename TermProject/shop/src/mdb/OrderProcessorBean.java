package examples.shop.impl.mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import examples.shop.impl.entity.Order;
import examples.shop.logic.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

@MessageDriven(
    mappedName = "jmsqueue",
    activationConfig = { 
    @ActivationConfigProperty(
            propertyName = "destinationType", 
            propertyValue = "javax.jms.Queue") 
    }) 
public class OrderProcessorBean implements MessageListener {
    
    @PersistenceContext
    private EntityManager manager;
    Mail mail;

    public OrderProcessorBean() {}

    public void onMessage(Message msg) {
        TextMessage tm = (TextMessage) msg;
        try {
            try {
                Context ctx = getInitialContext("t3://localhost:7001", "weblogic", "weblogic123");
                mail = (Mail)ctx.lookup("MailBean");
            } catch (NamingException e) {
            e.printStackTrace();
        }        
        String orderID = tm.getText();
        Order order = manager.find(Order.class,orderID);
        System.out.println("고객 "+order.getCustomer()+"님에게 mail 전송");
        order.setStatus(Order.Status.Approved);
        System.out.println("주문 상태 : "+Order.Status.Approved);

        mail.sendMail(order.getCustomer(),"459722@naver.com", orderID, order.getOrderDate(),order.getSubTotal()+order.getTaxes()); 
             
        } catch (Exception e) {
            e.printStackTrace();
            throw new EJBException(e);
        }
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