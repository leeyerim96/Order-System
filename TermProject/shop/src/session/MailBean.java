package examples.shop.impl.session;

import java.util.Iterator;
import java.util.List;
import java.util.*;
import examples.shop.logic.*;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.jws.WebService;

import examples.shop.impl.entity.Customer;
import examples.shop.impl.entity.LineItem;
import examples.shop.logic.Pricer;
import examples.shop.logic.UserManager;
import weblogic.javaee.JNDIName;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Stateless Session Bean which computes prices based
 * upon a set of pricing rules.  The pricing rules are
 * deployed with the bean as environment properties.
 */
@Stateless
@JNDIName("MailBean")
@Remote(Mail.class)

//implements Pricer
public class MailBean  implements Mail{

 @PersistenceContext
    private EntityManager manager;
    

    public void sendMail(String name,String address, String order, Date date,Double totalPrice){
      String to = address;

      String from = "yelim9596@gmail.com";
      final String username = "yelim9596@gmail.com";
      final String password = "ehdanf12";

      String host = "smtp.gmail.com";

      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.host", host);
      props.put("mail.smtp.port", "25");

   String items="" ; 
        for (Iterator<LineItem> i = manager.createQuery("SELECT p FROM LineItem p where p.orders_orderid='"+order+"'").getResultList().iterator(); i
                    .hasNext();) {
                items+=i.next().getDescription()+"\n";
            }

             System.out.println(items);
      Session session = Session.getInstance(props,
         new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(username, password);
       }
         });

      try {
       Message message = new MimeMessage(session);
    
       message.setFrom(new InternetAddress(from));
    
       message.setRecipients(Message.RecipientType.TO,
               InternetAddress.parse(to));
    
       message.setSubject("EJB 상점 주문 알림 메일 입니다.");
    
       message.setText("주문 내역 정보입니다.\n고객 ID: "+name+"\n주문날짜: "+date+"\n주문번호: "+order+"\n\n주문내역\n "+items+"\n총가격: "+totalPrice);

       Transport.send(message);

       System.out.println("Sent message successfully....");

      } catch (MessagingException e) {
         throw new RuntimeException(e);
      }
   
    }
}
