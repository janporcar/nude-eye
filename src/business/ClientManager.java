package business;

import geolocationAPI.Address;
import persistence.dao.ClientDAO;
import persistence.dao.SaleDAO;
import persistence.exception.PersistenceException;




public class ClientManager {

    private final ClientDAO clientDAO;
    private final SaleDAO saleDAO;
    private Client currentClient;

    





    public ClientManager(ClientDAO clientDAO, SaleDAO saleDAO) {
        this.clientDAO = clientDAO;
        this.saleDAO = saleDAO;
    }

    






    public Client login(int clientId) throws BusinessException {
        try {
            Client client = clientDAO.findById(clientId);
            if (client == null) {
                throw new ClientNotFoundException("Client not found.");
            }
            currentClient = client;
            return client;
        } catch (PersistenceException e) {
            throw new BusinessException("Error accessing client data.", e);
        }
    }

    







    public Client registerInPerson(String fullName, PhoneNumber[] phones) throws BusinessException {
        try {
            Client client = new InPersonClient(generateNextClientId(), fullName, phones);
            clientDAO.save(client);
            currentClient = client;
            return client;
        } catch (PersistenceException e) {
            throw new BusinessException("Error saving new client.", e);
        }
    }

    









    public Client registerOnline(String fullName, PhoneNumber[] phones, String email, Address shippingAddress)
            throws BusinessException {
        try {
            Client client = new OnlineClient(generateNextClientId(), fullName, phones, email, shippingAddress);
            clientDAO.save(client);
            currentClient = client;
            return client;
        } catch (PersistenceException e) {
            throw new BusinessException("Error saving new client.", e);
        }
    }

    











    public Client registerCorporate(String companyName, PhoneNumber[] phones, String nif,
                                    String contactPersonName, Address billingAddress,
                                    Address shippingAddress) throws BusinessException {
        try {
            Client client = new CorporateClient(generateNextClientId(), companyName, phones,
                    nif, contactPersonName, billingAddress, shippingAddress);
            clientDAO.save(client);
            currentClient = client;
            return client;
        } catch (PersistenceException e) {
            throw new BusinessException("Error saving new client.", e);
        }
    }

    







    public Client register(String fullName, PhoneNumber[] phones) throws BusinessException {
        return registerInPerson(fullName, phones);
    }

    
    public Client getCurrentClient() {
        return currentClient;
    }

    






    public Sale[] getPurchaseHistory(int clientId) throws BusinessException {
        try {
            return saleDAO.findByClientId(clientId);
        } catch (PersistenceException e) {
            throw new BusinessException("Error reading purchase history.", e);
        }
    }

    
    public void logout() {
        currentClient = null;
    }

    private int generateNextClientId() throws PersistenceException {
        Client[] clients = clientDAO.findAll();
        int maxId = 0;
        for (Client client : clients) {
            if (client.getClientId() > maxId) {
                maxId = client.getClientId();
            }
        }
        return maxId + 1;
    }
}
