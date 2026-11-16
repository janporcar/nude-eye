package business;

import geolocationAPI.Address;
import geolocationAPI.GeolocationApiManager;




public abstract class Client {

    protected int clientId;
    protected String fullName;
    protected PhoneNumber[] phoneNumbers;

    






    public Client(int clientId, String fullName, PhoneNumber[] phoneNumbers) {
        this.clientId = clientId;
        this.fullName = fullName;
        this.phoneNumbers = phoneNumbers;
    }

    
    public int getClientId() {
        return clientId;
    }

    
    public String getFullName() {
        return fullName;
    }

    
    public String getName() {
        return fullName;
    }

    
    public PhoneNumber[] getPhoneNumbers() {
        return phoneNumbers;
    }

    




    public abstract String getClientType();

    




    public String[] getSpecificProfileLines() {
        return new String[0];
    }

    





    public boolean canBuy(Product product) {
        return true;
    }

    




    public boolean appliesVAT() {
        return true;
    }

    







    public float applyConstraintRules(float total, GeolocationApiManager geoManager) throws BusinessException {
        return total;
    }

    





    protected String formatAddress(Address address) {
        if (address == null) {
            return "";
        }
        return address.getStreet() + " " + address.getNumber() + ", "
                + address.getPostalCode() + " " + address.getCity() + ", "
                + address.getCountry();
    }
}
