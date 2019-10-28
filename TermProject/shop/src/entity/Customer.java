package examples.shop.impl.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import javax.persistence.CascadeType;
import javax.persistence.Table;

/**
 * This entity represents customer details.
 */
@Entity
@Table(name = "CUSTOMERS")

public class Customer implements java.io.Serializable {

    private String customerID;
    private String name;
    private String password;
    private String address;
    private double discount;

    @Id
    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerId) {
        this.customerID = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDiscount() {
        return discount;
    }
    
    public void setDiscount(double discount) {
        this.discount = discount;
    }
    
    public Customer() {
    }

    public void init(String id, String name, String password, String address) {
        setCustomerID(id);
        setName(name);
        setAddress(address);
        setPassword(password);
    }

}