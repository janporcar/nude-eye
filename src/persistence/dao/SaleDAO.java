package persistence.dao;

import business.Sale;
import persistence.exception.PersistenceException;








public interface SaleDAO {

    





    void save(Sale sale) throws PersistenceException;

    







    Sale[] findByClientId(int clientId) throws PersistenceException;
}
