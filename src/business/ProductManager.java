package business;

import persistence.dao.ProductDAO;
import persistence.exception.PersistenceException;








public class ProductManager {

    
    private final ProductDAO productDAO;

    




    public ProductManager(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    







    public Product[] findByName(String productName) throws BusinessException {
        try {
            return productDAO.findByName(productName);
        } catch (PersistenceException e) {
            throw new BusinessException("Error searching products.", e);
        }
    }

    







    public Product findById(String productId) throws BusinessException {
        try {
            Product product = productDAO.findById(productId);
            if (product == null) {
                throw new ProductNotFoundException("Product not found.");
            }
            return product;
        } catch (PersistenceException e) {
            throw new BusinessException("Error accessing product data.", e);
        }
    }

    





    public Product[] getAllProducts() throws BusinessException {
        try {
            return productDAO.findAll();
        } catch (PersistenceException e) {
            throw new BusinessException("Error reading product catalogue.", e);
        }
    }
}
