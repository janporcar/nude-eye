package business;




public class Glasses extends PhysicalProduct {

    







    public Glasses(String productId, String name, String brand, String model) {
        super(productId, name, brand, model);
    }

    
    @Override
    public String getProductType() {
        return "Glasses";
    }
}
