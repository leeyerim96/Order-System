package examples.shop.impl.entity;

import java.sql.Timestamp;
import java.sql.*;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ORDERS")
public class Order {

    public enum Status {
        Submitted, Unverified, Approved, Shipping, Delivered, Returned
    };

    private String orderId;
    // private List<LineItem> lineItems;
    private String customer;
    private Date orderDate;
    private Status status;
    private double taxes;
    private double subTotal;

    @Id
    public String getOrderID() {
        return orderId;
    }

    public void setOrderID(String id) {
        orderId = id;
    }

    // @OneToMany(cascade = CascadeType.PERSIST)
    // public List<LineItem> getLineItems() {
    //     return lineItems;
    // }

    // public void setLineItems(List<LineItem> lineItems) {
    //     this.lineItems = lineItems;
    // }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getTaxes() {
        return taxes;
    }

    public void setTaxes(double taxes) {
        this.taxes = taxes;
    }

    // public double totalPrice() {
    //     double totalPrice = 0;
    //     for (Iterator<LineItem> iter = getLineItems().iterator(); iter.hasNext();) {
    //         LineItem item = iter.next();
    //         totalPrice += item.getProduct().basePrice;
    //     }
    //     return totalPrice;
    // }

    public void init(String orderID, Customer customer, Status status, double subTotal, double taxes) {
        System.out.println("Order.init(" + orderID + ") called");
        setOrderID(orderID);
        setOrderDate(new java.sql.Date(System.currentTimeMillis()));
        setStatus(status);
        setCustomer(customer.getCustomerID());
        setSubTotal(subTotal);
        setTaxes(taxes);
    }

}
