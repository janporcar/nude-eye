package persistence.dao;

import business.Provider;
import persistence.exception.PersistenceException;







public interface ProviderDAO {

    





    Provider[] findAll() throws PersistenceException;

    






    Provider findById(int providerId) throws PersistenceException;

    






    Provider[] findByProductId(String productId) throws PersistenceException;

    









    void updateStock(int providerId, String productId, int units) throws PersistenceException;
}
