package examples.shop.logic;

import java.util.List;
import java.util.*;
import examples.shop.impl.entity.LineItem;

/**
 * The Cart interface.
 */
public interface Mail {

    public void sendMail(String name,String address, String order, Date date,Double totalPrice );
   

}
