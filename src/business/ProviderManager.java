package business;

import persistence.dao.ProviderDAO;
import persistence.exception.PersistenceException;








public class ProviderManager {

    
    private final ProviderDAO providerDAO;

    




    public ProviderManager(ProviderDAO providerDAO) {
        this.providerDAO = providerDAO;
    }

    





    public Provider[] getAllProductsForSale() throws BusinessException {
        try {
            return providerDAO.findAll();
        } catch (PersistenceException e) {
            throw new BusinessException("Error reading providers.", e);
        }
    }

    






    public Provider findById(int providerId) throws BusinessException {
        try {
            return providerDAO.findById(providerId);
        } catch (PersistenceException e) {
            throw new BusinessException("Error accessing provider data.", e);
        }
    }

    






    public Provider[] findByProductId(String productId) throws BusinessException {
        try {
            return providerDAO.findByProductId(productId);
        } catch (PersistenceException e) {
            throw new BusinessException("Error searching providers by product.", e);
        }
    }

    







    public ProductForSale findOffer(int providerId, String productId) throws BusinessException {
        try {
            Provider provider = providerDAO.findById(providerId);
            if (provider == null) {
                return null;
            }
            for (ProductForSale offer : provider.getProductsForSale()) {
                if (offer.getProductId().equals(productId)) {
                    return offer;
                }
            }
            return null;
        } catch (PersistenceException e) {
            throw new BusinessException("Error finding offer.", e);
        }
    }

    






    public ProductForSale[] getProductsForProvider(int providerId) throws BusinessException {
        try {
            Provider provider = providerDAO.findById(providerId);
            if (provider == null) {
                return new ProductForSale[0];
            }
            return provider.getProductsForSale();
        } catch (PersistenceException e) {
            throw new BusinessException("Error reading provider products.", e);
        }
    }
}