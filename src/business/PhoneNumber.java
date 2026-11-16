package business;








public class PhoneNumber {

    
    private final String countryPrefix;

    
    private final String number;

    





    public PhoneNumber(String countryPrefix, String number) {
        this.countryPrefix = countryPrefix;
        this.number = number;
    }

    




    public String getCountryPrefix() {
        return countryPrefix;
    }

    




    public String getNumber() {
        return number;
    }

    




    @Override
    public String toString() {
        return "(" + countryPrefix + ") " + number;
    }
}
