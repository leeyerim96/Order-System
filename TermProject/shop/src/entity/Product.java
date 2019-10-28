package examples.shop.impl.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import javax.persistence.CascadeType;
import javax.persistence.Table;

@Entity
@Table(name = "PRODUCTS")

public class Product implements java.io.Serializable {

    public String name;
    public String description;
    public double basePrice;
    public String productID;
    
    public Product() {}

    @Id
    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double price) {
        this.basePrice = price;
    }

    public Product init(String productID, String name, String description,
            double basePrice) {
        this.productID = productID;
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        return this;
    }

}