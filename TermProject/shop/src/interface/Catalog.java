package examples.shop.logic;

import java.util.*;

import examples.shop.impl.entity.Product;
import java.io.Serializable;

/**
 * This is the Catalog business interface.
 */

public interface Catalog  {
    
	/**
     * @return a vector of Products
     */
    public List<Product> getProductList();
    
    /**
     * @return a Product for the given product id.
     */
    public Product getProduct(String productId);
    
    /**
     * Add a new product to the catalog
     * @param product
     */
    public void addProduct(Product product);
        
}


