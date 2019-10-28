package examples.shop.impl.session;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.io.Serializable;


import examples.shop.impl.entity.Product;
import examples.shop.logic.Catalog;
import javax.ejb.Stateless;
import javax.ejb.Remote;
import javax.persistence.*;

import weblogic.javaee.JNDIName;

/**
 * This catalog Stateless Session Bean retrieves a list of productitems.
 * ProductItem has product Id, name of the product and description
 */
@JNDIName("CatalogBean")
@Stateless
@Remote(Catalog.class)
public class CatalogBean implements Catalog, Serializable {

    @PersistenceContext
    private EntityManager manager;
    
    public CatalogBean() {
    }

    public Product getProduct(String productId) {
        return manager.find(Product.class, productId);
    }

    public List<Product> getProductList() {
        /* find all products */
        return manager.createQuery("SELECT p FROM Product p")
                .getResultList();
    }

    public void addProduct(Product product) {
        manager.persist(product);
    }
    
}
