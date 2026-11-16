package business;

import java.time.LocalDate;




public class Consumable extends PhysicalProduct {

    private double volumeMl;
    private LocalDate expirationDate;
    private boolean containsPreservatives;

    










    public Consumable(String productId, String name, String brand, String model,
                      double volumeMl, LocalDate expirationDate, boolean containsPreservatives) {
        super(productId, name, brand, model);
        this.volumeMl = volumeMl;
        this.expirationDate = expirationDate;
        this.containsPreservatives = containsPreservatives;
    }

    
    public double getVolumeMl() {
        return volumeMl;
    }

    
    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    
    public boolean containsPreservatives() {
        return containsPreservatives;
    }

    




    private boolean expiresInLessThanThreeMonths() {
        return expirationDate != null && expirationDate.isBefore(LocalDate.now().plusMonths(3));
    }

    
    @Override
    public String getProductType() {
        return "Consumable";
    }

    
    @Override
    public String[] getSpecificInfoLines() {
        String preservativesText;
        if (containsPreservatives) {
            preservativesText = "Yes";
        } else {
            preservativesText = "No";
        }
        return new String[]{
                "Volume: " + String.format("%.2f", volumeMl) + " ml",
                "Expiration date: " + expirationDate,
                "Contains preservatives: " + preservativesText
        };
    }

    
    @Override
    public float calculatePrice(float sellingPrice, boolean applyVAT) {
        float price;
        if (applyVAT) {
            price = sellingPrice * PricingConstants.STANDARD_VAT_MULTIPLIER;
        } else {
            price = sellingPrice;
        }

        if (expiresInLessThanThreeMonths()) {
            price = price * PricingConstants.EXPIRING_CONSUMABLE_DISCOUNT_MULTIPLIER;
        }
        return price;
    }
}
