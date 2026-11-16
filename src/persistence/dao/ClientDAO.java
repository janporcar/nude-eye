package persistence.dao;

import business.Client;
import persistence.exception.PersistenceException;








public interface ClientDAO {

    





    Client[] findAll() throws PersistenceException;

    





    void save(Client client) throws PersistenceException;

    






    Client findById(int clientId) throws PersistenceException;
}
