package business;

import java.util.Date;







public class Sale {

    
    private final int clientId;

    
    private final String productId;

    
    private final float pricePaid;

    
    private final Date purchaseDate;

    







    public Sale(int clientId, String productId, float pricePaid, Date purchaseDate) {
        this.clientId = clientId;
        this.productId = productId;
        this.pricePaid = pricePaid;
        this.purchaseDate = purchaseDate;
    }

    




    public int getClientId() {
        return clientId;
    }

    




    public String getProductId() {
        return productId;
    }

    




    public float getPricePaid() {
        return pricePaid;
    }

    




    public Date getPurchaseDate() {
        return purchaseDate;
    }
}
