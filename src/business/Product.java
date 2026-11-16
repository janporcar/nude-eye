package business;








public abstract class Product {

    protected String productId;
    protected String name;

    





    public Product(String productId, String name) {
        this.productId = productId;
        this.name = name;
    }

    




    public String getProductId() {
        return productId;
    }

    




    public String getProductName() {
        return name;
    }

    




    public String getName() {
        return name;
    }

    




    public String getBrand() {
        return "";
    }

    




    public String getModel() {
        return "";
    }

    




    public String getProductType() {
        return "Product";
    }

    




    public String[] getSpecificInfoLines() {
        return new String[0];
    }

    






    public abstract float calculatePrice(float sellingPrice, boolean applyVAT);
}
