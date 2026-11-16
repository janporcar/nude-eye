package business;




public class Service extends Product {

    private double completionTimeHours;

    






    public Service(String productId, String name, double completionTimeHours) {
        super(productId, name);
        this.completionTimeHours = completionTimeHours;
    }

    
    public double getCompletionTimeHours() {
        return completionTimeHours;
    }

    
    @Override
    public String getProductType() {
        return "Service";
    }

    
    @Override
    public String[] getSpecificInfoLines() {
        return new String[]{
                "Completion time: " + String.format("%.2f", completionTimeHours) + " hours"
        };
    }

    
    @Override
    public float calculatePrice(float sellingPrice, boolean applyVAT) {
        float price = (float) (sellingPrice * completionTimeHours);
        if (applyVAT) {
            price = price * PricingConstants.STANDARD_VAT_MULTIPLIER;
        }
        return price;
    }
}
