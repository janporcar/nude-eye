package persistence.dao;

import business.Product;
import persistence.exception.PersistenceException;








public interface ProductDAO {

    





    Product[] findAll() throws PersistenceException;

    






    Product findById(String productId) throws PersistenceException;

    







    Product[] findByName(String name) throws PersistenceException;
}
