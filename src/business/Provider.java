package business;








public class Provider {

    
    private final int providerId;

    
    private final String companyName;

    
    private final String cif;

    
    private final String contactName;

    
    private final String phone;

    
    private final String email;

    
    private final ProductForSale[] productsForSale;

    










    public Provider(int providerId, String companyName, String cif, String contactName, String phone, String email, ProductForSale[] productsForSale) {
        this.providerId = providerId;
        this.companyName = companyName;
        this.cif = cif;
        this.contactName = contactName;
        this.phone = phone;
        this.email = email;
        this.productsForSale = productsForSale;
    }

    




    public int getProviderId() {
        return providerId;
    }

    




    public String getCompanyName() {
        return companyName;
    }

    




    public String getCif() {
        return cif;
    }

    




    public String getContactName() {
        return contactName;
    }

    




    public String getPhone() {
        return phone;
    }

    




    public String getEmail() {
        return email;
    }

    




    public ProductForSale[] getProductsForSale() {
        return productsForSale;
    }
}