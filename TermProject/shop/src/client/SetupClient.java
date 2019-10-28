package client;

import java.util.Iterator;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;


import javax.naming.NamingException;
import examples.shop.impl.entity.Product;
import examples.shop.logic.Catalog;
import examples.shop.logic.UserManager;

public class SetupClient {
    public static void main(String[] args) throws Exception {
        try {
             Context ctx = getInitialContext("t3://localhost:7001", "weblogic", "weblogic123");
             Catalog catalog = (Catalog) ctx.lookup("CatalogBean");

            catalog.addProduct(new Product().init("123-456-7890", "P4-1.8",
                    "1.8 GHz Pentium 4", 200));
            catalog.addProduct(new Product().init("123-456-7891", "P4-3",
                    "3 GHz Pentium 4", 300));
            catalog.addProduct(new Product().init("123-456-7892", "P4-4",
                    "4 GHz Pentium", 400));
            catalog.addProduct(new Product().init("123-456-7893", "SD-256",
                    "256 MB SDRAM", 50));
            catalog.addProduct(new Product().init("123-456-7894", "SD-512",
                    "512 MB SDRAM", 100));
            catalog.addProduct(new Product().init("123-456-7895", "DD-1000",
                    "1GB MB DDRAM", 200));            
            catalog.addProduct(new Product().init("123-456-7896", "MP3-x",
                    "MP3 Player", 200));

            for (Iterator<Product> i = catalog.getProductList().iterator(); i.hasNext();) {
                System.out.println(i.next().getDescription());
            }
            
            UserManager userManager = 
                (UserManager) ctx.lookup("UserManagerBean");            
                userManager.createUser("admin", "lyr", "1234", "Kor");            
        } catch (Exception e) {
            e.printStackTrace();
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