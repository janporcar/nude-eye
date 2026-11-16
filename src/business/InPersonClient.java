package business;




public class InPersonClient extends Client {

    






    public InPersonClient(int clientId, String fullName, PhoneNumber[] phoneNumbers) {
        super(clientId, fullName, phoneNumbers);
    }

    
    @Override
    public String getClientType() {
        return "in_person";
    }
}
