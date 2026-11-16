package business;

import geolocationAPI.GeolocationApiManager;
import persistence.dao.ProviderDAO;
import persistence.dao.SaleDAO;
import persistence.exception.PersistenceException;

import java.util.Date;




public class SaleManager {

    private final SaleDAO saleDAO;
    private final ProviderDAO providerDAO;
    private final ProductManager productManager;
    private final GeolocationApiManager geolocationApiManager;
    private final ShoppingCart cart;

    







    public SaleManager(SaleDAO saleDAO,
                       ProviderDAO providerDAO,
                       ProductManager productManager,
                       GeolocationApiManager geolocationApiManager) {
        this.saleDAO = saleDAO;
        this.providerDAO = providerDAO;
        this.productManager = productManager;
        this.geolocationApiManager = geolocationApiManager;
        this.cart = new ShoppingCart();
    }

    
    public ShoppingCart getCart() {
        return cart;
    }

    






    public void addItem(ProductForSale item, Client client) throws BusinessException {
        Product product = productManager.findById(item.getProductId());
        addItem(item, client, product);
    }

    







    public void addItem(ProductForSale item, Client client, Product product) throws BusinessException {
        if (item == null) {
            throw new BusinessException("Selected provider offer is not available.");
        }
        if (item.getUnitsInStock() <= 0) {
            throw new OutOfStockException("This product is out of stock.");
        }
        if (product != null && !client.canBuy(product)) {
            throw new BusinessException("This client cannot buy this product.");
        }
        cart.addItem(item);
    }

    
    public void removeFromCart(String productId) {
        cart.remove(productId);
    }

    






    public float calculateTotal(Client client) throws BusinessException {
        float total = 0;
        for (ProductForSale item : cart.getItems()) {
            Product product = findProductOrNull(item.getProductId());
            total += calculateItemPrice(item, client, product);
        }
        return client.applyConstraintRules(total, geolocationApiManager);
    }

    







    public float calculateItemPrice(ProductForSale item, Client client, Product product) {
        Product resolvedProduct = product;
        if (resolvedProduct == null) {
            resolvedProduct = findProductOrNull(item.getProductId());
        }
        if (resolvedProduct != null) {
            return resolvedProduct.calculatePrice(item.getSellingPrice(), client.appliesVAT());
        }
        if (client.appliesVAT()) {
            return item.getSellingPrice() * PricingConstants.STANDARD_VAT_MULTIPLIER;
        }
        return item.getSellingPrice();
    }

    





    public void checkout(Client client) throws BusinessException {
        try {
            for (ProductForSale item : cart.getItems()) {
                Product product = findProductOrNull(item.getProductId());
                if (product != null && !client.canBuy(product)) {
                    throw new BusinessException("This client cannot buy this product.");
                }
                Sale sale = createSale(client, item, product);
                saleDAO.save(sale);
                providerDAO.updateStock(item.getProviderId(), item.getProductId(), 1);
            }
            cart.clear();
        } catch (PersistenceException e) {
            throw new BusinessException("Error during checkout.", e);
        }
    }

    private Sale createSale(Client client, ProductForSale item, Product product) {
        float finalPrice = calculateItemPrice(item, client, product);
        return new Sale(client.getClientId(), item.getProductId(), finalPrice, new Date());
    }

    private Product findProductOrNull(String productId) {
        try {
            return productManager.findById(productId);
        } catch (BusinessException e) {
            return null;
        }
    }
}
