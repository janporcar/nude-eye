package business;

import geolocationAPI.Address;
import geolocationAPI.Geo;
import geolocationAPI.GeolocationApiManager;




public class OnlineClient extends Client {

    private String email;
    private Address shippingAddress;

    








    public OnlineClient(int clientId, String fullName, PhoneNumber[] phoneNumbers,
                        String email, Address shippingAddress) {
        super(clientId, fullName, phoneNumbers);
        this.email = email;
        this.shippingAddress = shippingAddress;
    }

    
    public String getEmail() {
        return email;
    }

    
    public Address getShippingAddress() {
        return shippingAddress;
    }

    
    @Override
    public String getClientType() {
        return "online";
    }

    
    @Override
    public String[] getSpecificProfileLines() {
        return new String[]{
                "Client type: Online client",
                "Email: " + email,
                "Shipping address: " + formatAddress(shippingAddress)
        };
    }

    
    @Override
    public boolean canBuy(Product product) {
        return !(product instanceof Service);
    }

    
    @Override
    public float applyConstraintRules(float total, GeolocationApiManager geoManager) throws BusinessException {
        try {
            Geo origin = new Geo(PricingConstants.LASALLE_LATITUDE, PricingConstants.LASALLE_LONGITUDE);
            Geo destination = geoManager.getGeolocationFromAddress(shippingAddress);
            double distanceKm = geoManager.calculateDistance(origin, destination);
            double shippingCost = PricingConstants.SHIPPING_BASE_COST
                    + PricingConstants.SHIPPING_COST_PER_KM * distanceKm;
            return (float) (total + shippingCost);
        } catch (Exception e) {
            throw new BusinessException("Error calculating shipping cost.", e);
        }
    }
}
