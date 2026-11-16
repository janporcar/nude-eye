package business;

import geolocationAPI.Address;




public class CorporateClient extends Client {

    private String nif;
    private String contactPersonName;
    private Address billingAddress;
    private Address shippingAddress;

    










    public CorporateClient(int clientId, String fullName, PhoneNumber[] phoneNumbers,
                           String nif, String contactPersonName,
                           Address billingAddress, Address shippingAddress) {
        super(clientId, fullName, phoneNumbers);
        this.nif = nif;
        this.contactPersonName = contactPersonName;
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
    }

    
    public String getNif() {
        return nif;
    }

    
    public String getContactPersonName() {
        return contactPersonName;
    }

    
    public Address getBillingAddress() {
        return billingAddress;
    }

    
    public Address getShippingAddress() {
        return shippingAddress;
    }

    
    @Override
    public String getClientType() {
        return "corporate";
    }

    
    @Override
    public String[] getSpecificProfileLines() {
        return new String[]{
                "Client type: Corporate client",
                "NIF: " + nif,
                "Contact person: " + contactPersonName,
                "Billing address: " + formatAddress(billingAddress),
                "Shipping address: " + formatAddress(shippingAddress)
        };
    }

    
    @Override
    public boolean canBuy(Product product) {
        return !(product instanceof Service);
    }

    
    @Override
    public boolean appliesVAT() {
        return billingAddress != null && "Spain".equalsIgnoreCase(billingAddress.getCountry());
    }
}
