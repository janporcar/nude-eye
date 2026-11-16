package business;




public class PhysicalProduct extends Product {

    private String brand;
    private String model;

    







    public PhysicalProduct(String productId, String name, String brand, String model) {
        super(productId, name);
        this.brand = brand;
        this.model = model;
    }

    
    @Override
    public String getBrand() {
        return brand;
    }

    
    @Override
    public String getModel() {
        return model;
    }

    
    @Override
    public String getProductType() {
        return "Physical product";
    }

    
    @Override
    public float calculatePrice(float sellingPrice, boolean applyVAT) {
        if (applyVAT) {
            return sellingPrice * PricingConstants.STANDARD_VAT_MULTIPLIER;
        }
        return sellingPrice;
    }
}
