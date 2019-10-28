package examples.shop.impl.session;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.*;
import javax.jms.*;
import javax.naming.*;
import javax.persistence.*;

import examples.shop.impl.entity.Customer;
import examples.shop.impl.entity.LineItem;
import examples.shop.impl.entity.Order;
import examples.shop.impl.entity.Product;
import examples.shop.logic.Cart;

import java.util.Hashtable;

import weblogic.javaee.JNDIName;

/**
 * This stateful session bean represents a cart which a customer is
 * working on.  A cart is a set of products that the customer is
 * interested in, but has not committed to buying yet.  
 *
 * A cart consists of a series of line items.  Each line item
 * represents one particular product the customer wants, as well as a
 * quantity and discount for that product.
 *
 * The distinction between a cart and an order is that a quote is only
 * temporary and in-memory (hence a stateful session bean), whereas an
 * order is a persistent record (hence an entity).  The cart bean 
 * knows how to transform itself into an order (via the purchase() 
 * method).
 */
@JNDIName("CartBean")
@Stateful
@Remote(Cart.class)
public class CartBean implements Cart {

    /**
     * The shopping cart owner
     */
    private String owner;

    /**
     * The shopping cart contents - a vector of LineItems
     */
    private List<LineItem> contents = new ArrayList();

    /**
     * The subtotal (price before taxes) of the goods
     */
    private double subTotal;

    /**
     * The taxes on the goods
     */
    private double taxes;

    @PersistenceContext
    private EntityManager manager;

    public CartBean() {
    }

    /**
     * Get method for the shopping cart owner's name
     * 
     * @return shopping cart owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Set method for the shopping cart owner's name
     * 
     * @param shopping
     *            cart owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Adds an item to the shopping cart
     * 
     * @param shopping
     *            cart lineitem
     */
    public void add(LineItem lineItem) {
        /*
         * Search for an exiting line item in the cart that match 
         * the productID passed in. 
         */
        for (Iterator<LineItem> iter = contents.iterator(); iter.hasNext();) {
            LineItem item = iter.next();
            Product product = item.getProduct();

            if (product != null && product.getProductID().equals(lineItem.getProduct().getProductID())) {
                // found one, modify that line item's quantity and return
                item.setQuantity(item.getQuantity() + lineItem.getQuantity());
                return;
            }
        }

        /*
         * Did not find a match, so add the line item as new.
         */
        contents.add(lineItem);
    }

    /**
     * Changes the quantities of contents in the shopping cart
     * 
     * @param product
     *            Id
     * @param number
     *            of items.
     */
    public void modify(String productID, int quantity) throws Exception {
        System.out.println("CartBean.modify()");

        /*
         * Search for a lineitem in the cart that match the productID passed in.
         * If found, modify that lineitem.
         */
        for (Iterator<LineItem> iter = contents.iterator(); iter.hasNext();) {
            LineItem item = iter.next();
            Product product = item.getProduct();
            if (product != null && product.getProductID().equals(productID)) {
                item.setQuantity(quantity);
                if (quantity == 0) {
                    iter.remove();
                }
                return;
            }
        }
        throw new Exception("CartBean.modify() error: Could not find product " + productID);
    }

    /**
     * Returns all the shopping cart line items
     * 
     * @return A collection of Cart LineItems
     */
    public List<LineItem> getAll() {
        return contents;
    }

    /**
     * @return  the subtotal price which has been previously set by
     * setSubtotal().
     */
    public double getSubtotal() {
        return subTotal;
    }

    /**
     * Sets the subtotal price.
     * @param subTotal the subtotal price
     */
    public void setSubtotal(double subTotal) {
        this.subTotal = subTotal;
    }

    /**
     * @return the taxes computed for this Cart.
     */
    public double getTaxes() {
        return taxes;
    }

    /**
     * Sets the taxes for this Cart.
     */
    public void setTaxes(double taxes) {
        System.out.println("CartBean.setTaxes(): " + taxes);
        this.taxes = taxes;
    }

    /**
     * Get the total price. The total Price is computed from:
     * <ol> 
     * <li>Subtotal price
     * <li>Tax
     * </ol>
     * @return the total price. 
     */
    public double getTotalPrice() {
        return subTotal + taxes;
    }

    /**
     * Empties the shopping cart
     */
    public void clear() {
        contents.clear();
    }

    /**
     * Purchases this cart. The cart will create an order in the database 
     * and return the order ID as a confirmation number.
     * 
     * @return The Order confirmation number
     */
    public String purchase() {
        System.out.println("CartBean.purchase()");
        Customer customer = manager.find(Customer.class, owner);
        /*
         * Create the order entity
         */
        Order order = new Order();
        String orderID = makeUniqueID();
        order.init(orderID, customer, Order.Status.Unverified, subTotal, taxes);
        prepareContents(orderID);
        manager.persist(order);

        /*
         * Sends the JMS message to the topic
         */
        try {
            sendMessage(orderID);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /*
         * Return the order ID
         */
        return orderID;
    }

    /**
     * Sends a JMS message to a destination (Queue or Topic)
     * 
     * @param a
     *            message string, in this case orderId
     */
    private void sendMessage(String text) throws Exception {
            Context ctx = getInitialContext("t3://localhost:7001","weblogic","weblogic123");
            QueueConnectionFactory factory = (QueueConnectionFactory)ctx.lookup("weblogic.jms.ConnectionFactory");
            QueueConnection connection = factory.createQueueConnection();
            QueueSession session = connection.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);
            Queue queue = (Queue)ctx.lookup("./jmsqueue");
            QueueSender sender = session.createSender(queue);
            TextMessage msg = session.createTextMessage();
            msg.setText(text);
            sender.send(msg);
            sender.close();
}
    private void prepareContents(String id) {
        for (Iterator<LineItem> iter = contents.iterator(); iter.hasNext();) {
            LineItem lineItem = iter.next();
            Product p = manager.find(Product.class, lineItem.getProduct().getProductID());
            lineItem.setProduct(p);
            lineItem.setId(makeUniqueID());
            lineItem.setOrders_orderid(id);
            manager.persist(lineItem);
        }
    }

    /**
     * Returns a unique Id based on current time. Can be removed
     * when AUTO key generation works in Glassfish      * 
     */
    private String makeUniqueID() {
        String id = new Long(System.nanoTime()).toString();
        System.out.println("makeUniqueID: " + id);
        return id;
    }

    private static Context getInitialContext(String url, String username, String password) throws NamingException {
       Hashtable<String, String> env = new Hashtable<String, String>();
       env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
       env.put(Context.PROVIDER_URL, url);
       env.put(Context.SECURITY_PRINCIPAL, username);
       env.put(Context.SECURITY_CREDENTIALS, password);

       return new InitialContext(env);
      }
}