package business;




public class ContactLens extends PhysicalProduct {

    private double prescription;
    private int numberOfLenses;
    private boolean uvFilter;

    










    public ContactLens(String productId, String name, String brand, String model,
                       double prescription, int numberOfLenses, boolean uvFilter) {
        super(productId, name, brand, model);
        this.prescription = prescription;
        this.numberOfLenses = numberOfLenses;
        this.uvFilter = uvFilter;
    }

    
    public double getPrescription() {
        return prescription;
    }

    
    public int getNumberOfLenses() {
        return numberOfLenses;
    }

    
    public boolean hasUvFilter() {
        return uvFilter;
    }

    
    @Override
    public String getProductType() {
        return "Contact lens";
    }

    
    @Override
    public String[] getSpecificInfoLines() {
        String uvFilterText;
        if (uvFilter) {
            uvFilterText = "Yes";
        } else {
            uvFilterText = "No";
        }
        return new String[]{
                "Prescription: " + String.format("%.2f", prescription),
                "Number of lenses: " + numberOfLenses,
                "UV filter: " + uvFilterText
        };
    }

    
    @Override
    public float calculatePrice(float sellingPrice, boolean applyVAT) {
        if (applyVAT) {
            return sellingPrice * PricingConstants.CONTACT_LENS_VAT_MULTIPLIER;
        }
        return sellingPrice;
    }
}
