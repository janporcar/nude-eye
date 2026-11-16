package business;









public class ProductForSale {

    
    private final String productId;

    
    private final int providerId;

    
    private final float sellingPrice;

    
    private int unitsInStock;

    







    public ProductForSale(String productId, int providerId, float sellingPrice, int unitsInStock) {
        this.productId = productId;
        this.providerId = providerId;
        this.sellingPrice = sellingPrice;
        this.unitsInStock = unitsInStock;
    }

    




    public String getProductId() {
        return productId;
    }

    




    public int getProviderId() {
        return providerId;
    }

    




    public float getSellingPrice() {
        return sellingPrice;
    }

    




    public int getUnitsInStock() {
        return unitsInStock;
    }

    





    public void decreaseStock(int units) {
        this.unitsInStock = Math.max(0, this.unitsInStock - units);
    }
}
