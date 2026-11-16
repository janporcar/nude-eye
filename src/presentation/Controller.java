package presentation;

import business.*;
import edu.salle.url.api.ApiHelper;
import edu.salle.url.api.exception.ApiException;
import geolocationAPI.Address;
import geolocationAPI.GeolocationApiManager;
import persistence.dao.ClientDAO;
import persistence.dao.ProductDAO;
import persistence.dao.ProviderDAO;
import persistence.dao.SaleDAO;
import persistence.exception.PersistenceException;
import persistence.impl.ClientAPIDAO;
import persistence.impl.ClientJSONDAO;
import persistence.impl.ProductAPIDAO;
import persistence.impl.ProductJSONDAO;
import persistence.impl.ProviderAPIDAO;
import persistence.impl.ProviderJSONDAO;
import persistence.impl.SaleAPIDAO;
import persistence.impl.SaleCSVDAO;

import java.io.File;

public class Controller {
    private static final String BASE_URL = "https://balandrau.salle.url.edu/dpoo";
    private static final String GROUP_ID = "5";

    private UserInterface ui;
    private ClientManager clientManager;
    private ProductManager productManager;
    private ProviderManager providerManager;
    private SaleManager saleManager;
    private boolean initialized;

    public Controller() {
        this.ui = new UserInterface();
        initialize();
    }

    public Controller(UserInterface ui, ClientManager clientManager, ProductManager productManager, ProviderManager providerManager, SaleManager saleManager) {
        this.ui = ui;
        this.clientManager = clientManager;
        this.productManager = productManager;
        this.providerManager = providerManager;
        this.saleManager = saleManager;
        this.initialized = true;
    }

    private void initialize() {
        ui.showBanner();
        ProductDAO productDAO;
        ProviderDAO providerDAO;
        ClientDAO clientDAO;
        SaleDAO saleDAO;
        ApiHelper apiHelper = createApiHelper();
        ui.showMessage("Checking API status...");
        if (apiHelper != null && apiWorks(apiHelper)) {
            ui.showMessage("API OK.");
            ui.showMessage("Starting program with cloud persistence...");
            productDAO = new ProductAPIDAO(apiHelper);
            providerDAO = new ProviderAPIDAO(apiHelper);
            clientDAO = new ClientAPIDAO(apiHelper, GROUP_ID);
            saleDAO = new SaleAPIDAO(apiHelper, GROUP_ID);
        } else {
            ui.showMessage("Error: The API isn't available.");
            ui.showMessage("Verifying local files...");
            if (!localFilesWork()) {
                ui.showMessage("Shutting down...");
                this.initialized = false;
                return;
            }
            productDAO = new ProductJSONDAO();
            providerDAO = new ProviderJSONDAO();
            clientDAO = new ClientJSONDAO();
            saleDAO = new SaleCSVDAO();
            ui.showMessage("Files OK.");
            ui.showMessage("Starting program with local persistence...");
        }
        this.productManager = new ProductManager(productDAO);
        this.providerManager = new ProviderManager(providerDAO);
        this.clientManager = new ClientManager(clientDAO, saleDAO);
        GeolocationApiManager geolocationApiManager = new GeolocationApiManager();
        this.saleManager = new SaleManager(saleDAO, providerDAO, productManager, geolocationApiManager);
        this.initialized = true;
    }

    private ApiHelper createApiHelper() {
        try {
            return new ApiHelper();
        } catch (ApiException e) {
            return null;
        }
    }

    private boolean apiWorks(ApiHelper apiHelper) {
        try {
            apiHelper.getFromUrl(BASE_URL + "/shared/products");
            return true;
        } catch (ApiException e) {
            return false;
        }
    }

    private boolean localFilesWork() {
        if (!new File("products.json").exists()) {
            ui.showMessage("Error: The products.json file can't be accessed.");
            return false;
        }
        if (!new File("providers.json").exists()) {
            ui.showMessage("Error: The providers.json file can't be accessed.");
            return false;
        }
        try {
            new ProductJSONDAO().findAll();
            new ProviderJSONDAO().findAll();
            new ClientJSONDAO().findAll();
            return true;
        } catch (PersistenceException e) {
            ui.showMessage(e.getMessage());
            return false;
        }
    }

    public void run() {
        if (!initialized) {
            return;
        }
        boolean exit = false;
        while (!exit) {
            ui.showAuthMenu();
            int option = ui.intInput("\nChoose an option: ");
            switch (option) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    handleRegister();
                    break;
                case 0:
                    exit = true;
                    ui.showMessage("We hope to see you again!");
                    break;
                default:
                    ui.showMessage("Invalid option.");
            }
        }
    }

    private void handleLogin() {
        try {
            int clientId = ui.intInput("Enter client ID: ");
            Client client = clientManager.login(clientId);
            handleMainMenu(client);
        } catch (BusinessException e) {
            ui.showMessage(e.getMessage());
        }
    }

    private void handleRegister() {
        try {
            String fullName = ui.stringInput("Enter full name / company name: ");
            PhoneNumber[] phones = askPhoneNumbers();
            ui.showClientTypeMenu();
            int clientType = ui.intInput("Choose client type: ");
            Client client;
            switch (clientType) {
                case 1:
                    client = clientManager.registerInPerson(fullName, phones);
                    break;
                case 2:
                    client = registerOnlineClient(fullName, phones);
                    break;
                case 3:
                    client = registerCorporateClient(fullName, phones);
                    break;
                default:
                    ui.showMessage("Invalid client type.");
                    return;
            }
            handleMainMenu(client);
        } catch (BusinessException e) {
            ui.showMessage(e.getMessage());
        }
    }

    private PhoneNumber[] askPhoneNumbers() {
        int numPhones = ui.intInput("How many phone numbers? ");
        PhoneNumber[] phones = new PhoneNumber[numPhones];
        for (int i = 0; i < numPhones; i++) {
            String countryPrefix = ui.stringInput("Country prefix: ");
            String number = ui.stringInput("Phone number: ");
            phones[i] = new PhoneNumber(countryPrefix, number);
        }
        return phones;
    }

    private Client registerOnlineClient(String fullName, PhoneNumber[] phones) throws BusinessException {
        String email = ui.stringInput("Email: ");
        Address shippingAddress = askAddress("Shipping address");
        return clientManager.registerOnline(fullName, phones, email, shippingAddress);
    }

    private Client registerCorporateClient(String companyName, PhoneNumber[] phones) throws BusinessException {
        String nif = ui.stringInput("NIF/CIF: ");
        String contactPerson = ui.stringInput("Contact person: ");
        Address billingAddress = askAddress("Billing address");
        Address shippingAddress = askAddress("Shipping address");
        return clientManager.registerCorporate(companyName, phones, nif, contactPerson, billingAddress, shippingAddress);
    }

    private Address askAddress(String label) {
        ui.showMessage(label + ":");
        String street = ui.stringInput("Street: ");
        int number = ui.intInput("Number: ");
        String city = ui.stringInput("City: ");
        String country = ui.stringInput("Country: ");
        String postalCode = ui.stringInput("Postal code: ");
        return new Address(street, number, city, country, postalCode);
    }

    private void handleMainMenu(Client client) {
        boolean logout = false;
        while (!logout) {
            ui.showMainMenu(client.getFullName());
            int option = ui.intInput("\nChoose an option: ");
            switch (option) {
                case 1:
                    showUserProfile(client);
                    break;
                case 2:
                    findProductsByName(client);
                    break;
                case 3:
                    findProductsByProvider(client);
                    break;
                case 4:
                    showCart(client);
                    break;
                case 0:
                    clientManager.logout();
                    logout = true;
                    ui.showMessage("Logged out.");
                    break;
                default:
                    ui.showMessage("Invalid option.");
            }
        }
    }

    private void showUserProfile(Client client) {
        try {
            PhoneNumber[] phones = client.getPhoneNumbers();
            String[] phoneLines = new String[phones.length];
            for (int i = 0; i < phones.length; i++) {
                phoneLines[i] = "\t" + phones[i].toString();
            }
            Sale[] history = clientManager.getPurchaseHistory(client.getClientId());
            int limit = Math.min(history.length, 10);
            String[] historyLines = buildHistoryLines(history, limit);
            ui.showUserProfile(client.getClientId(), client.getFullName(), phoneLines, client.getSpecificProfileLines(), historyLines);
        } catch (BusinessException e) {
            ui.showMessage(e.getMessage());
        }
    }

    private String[] buildHistoryLines(Sale[] history, int limit) {
        if (limit == 0) {
            return new String[]{"No purchases yet."};
        }
        String[] historyLines = new String[limit];
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy - HH:mm");
        for (int i = 0; i < limit; i++) {
            Sale sale = history[i];
            String productDescription = sale.getProductId();
            Product product = findProductSafely(sale.getProductId());
            if (product != null) {
                productDescription = formatProductName(product);
            }
            historyLines[i] = "\t" + productDescription + " - € " + String.format("%.2f", sale.getPricePaid()) + " - " + sdf.format(sale.getPurchaseDate());
        }
        return historyLines;
    }

    private void findProductsByName(Client client) {
        try {
            String input = ui.stringInput("Search criteria: ");
            Product[] products = productManager.findByName(input);
            String[] productLines = new String[products.length];
            for (int i = 0; i < products.length; i++) {
                productLines[i] = "\t" + (i + 1) + ") " + products[i].getProductName();
            }
            ui.showProductList(productLines);
            if (products.length == 0) {
                return;
            }
            int option = ui.intInput("Choose an option: ");
            if (option == 0) {
                return;
            }
            if (option < 1 || option > products.length) {
                ui.showMessage("Invalid option.");
                return;
            }
            showProductDetail(products[option - 1], client);
        } catch (BusinessException e) {
            ui.showMessage(e.getMessage());
        }
    }

    private void showProductDetail(Product product, Client client) {
        try {
            Provider[] providers = providerManager.findByProductId(product.getProductId());
            String[][] providerLines = new String[providers.length][3];
            for (int i = 0; i < providers.length; i++) {
                ProductForSale offer = findOfferInProvider(providers[i], product.getProductId());
                providerLines[i][0] = "\t" + (i + 1) + ") " + providers[i].getCompanyName();
                if (offer == null) {
                    providerLines[i][1] = "\t\t- Sale price: unavailable,";
                    providerLines[i][2] = "\t\t- Available stock: unavailable";
                } else {
                    providerLines[i][1] = "\t\t- Sale price: " + offer.getSellingPrice() + "€,";
                    providerLines[i][2] = "\t\t- Available stock: " + offer.getUnitsInStock();
                }
            }
            ui.showProductDetail(product.getProductId(), product.getProductName(), product.getBrand(), product.getModel(), product.getProductType(), product.getSpecificInfoLines(), providerLines);
            if (providers.length > 0) {
                handleProductPurchase(product, providers, client);
            }
        } catch (BusinessException e) {
            ui.showMessage(e.getMessage());
        }
    }

    private void handleProductPurchase(Product product, Provider[] providers, Client client) {
        String answer = ui.stringInput("\nDo you want to add this product to the shopping cart? ");
        if (!"yes".equalsIgnoreCase(answer)) {
            return;
        }
        int providerOption = ui.intInput("Choose a provider: ");
        if (providerOption < 1 || providerOption > providers.length) {
            ui.showMessage("Invalid option.");
            return;
        }
        Provider selectedProvider = providers[providerOption - 1];
        ProductForSale offer = findOfferInProvider(selectedProvider, product.getProductId());
        if (offer == null) {
            ui.showMessage("Selected provider offer is not available.");
            return;
        }
        try {
            saleManager.addItem(offer, client, product);
            ui.showMessage("Product added to shopping cart.");
        } catch (BusinessException e) {
            ui.showMessage(e.getMessage());
        }
    }

    private void findProductsByProvider(Client client) {
        try {
            Provider[] providers = providerManager.getAllProductsForSale();
            String[] providerLines = new String[providers.length];
            for (int i = 0; i < providers.length; i++) {
                providerLines[i] = "\t" + (i + 1) + ") " + providers[i].getCompanyName();
            }
            ui.showProviderList(providerLines);
            if (providers.length == 0) {
                return;
            }
            int option = ui.intInput("\nChoose an option: ");
            if (option == 0) {
                return;
            }
            if (option < 1 || option > providers.length) {
                ui.showMessage("Invalid option.");
                return;
            }
            showProviderDetail(providers[option - 1], client);
        } catch (BusinessException e) {
            ui.showMessage(e.getMessage());
        }
    }

    private void showProviderDetail(Provider provider, Client client) {
        ProductForSale[] offers = provider.getProductsForSale();
        String[][] productLines = new String[offers.length][3];
        Product[] resolvedProducts = new Product[offers.length];
        for (int i = 0; i < offers.length; i++) {
            ProductForSale offer = offers[i];
            resolvedProducts[i] = findProductSafely(offer.getProductId());
            String productDescription = offer.getProductId();
            if (resolvedProducts[i] != null) {
                productDescription = offer.getProductId() + " - " + resolvedProducts[i].getProductName();
            }
            productLines[i][0] = "\t" + (i + 1) + ") " + productDescription;
            productLines[i][1] = "\t\t- Sale price: " + offer.getSellingPrice() + "€,";
            productLines[i][2] = "\t\t- Available stock: " + offer.getUnitsInStock();
        }
        ui.showProviderDetail(provider.getCompanyName(), provider.getCif(), provider.getContactName(), provider.getPhone(), provider.getEmail(), productLines);
        if (offers.length == 0) {
            return;
        }
        int option = ui.intInput("\nChoose an option: ");
        if (option == 0) {
            return;
        }
        if (option < 1 || option > offers.length) {
            ui.showMessage("Invalid option.");
            return;
        }
        ProductForSale selectedOffer = offers[option - 1];
        Product selectedProduct = resolvedProducts[option - 1];
        String answer = ui.stringInput("\nDo you want to add this product to the shopping cart? ");
        if (!"yes".equalsIgnoreCase(answer)) {
            return;
        }
        try {
            saleManager.addItem(selectedOffer, client, selectedProduct);
            ui.showMessage("Product added to shopping cart.");
        } catch (BusinessException e) {
            ui.showMessage(e.getMessage());
        }
    }

    private void showCart(Client client) {
        try {
            boolean backToMenu = false;
            while (!backToMenu) {
                ShoppingCart cart = saleManager.getCart();
                ProductForSale[] items = cart.getItems();
                String[] cartLines = new String[items.length];
                for (int i = 0; i < items.length; i++) {
                    ProductForSale item = items[i];
                    Product product = findProductSafely(item.getProductId());
                    String productDescription = item.getProductId();
                    if (product != null) {
                        productDescription = item.getProductId() + " - " + product.getProductName();
                    }
                    String providerName = resolveProviderName(item.getProviderId());
                    String leftText = "+ " + productDescription + " (" + providerName + ")";
                    cartLines[i] = String.format("\t%-55s %8.2f€", leftText, saleManager.calculateItemPrice(item, client, product));
                }
                float totalPrice = saleManager.calculateTotal(client);
                ui.showCart(cartLines, totalPrice);
                if (cart.isEmpty()) {
                    return;
                }
                ui.showCartMenu();
                int option = ui.intInput("Choose an option: ");
                switch (option) {
                    case 1:
                        removeCartItem(items);
                        break;
                    case 2:
                        saleManager.checkout(client);
                        ui.showMessage("\nThank you for your purchase!");
                        backToMenu = true;
                        break;
                    case 0:
                        backToMenu = true;
                        break;
                    default:
                        ui.showMessage("Invalid option.");
                }
            }
        } catch (BusinessException e) {
            ui.showMessage(e.getMessage());
        }
    }

    private void removeCartItem(ProductForSale[] items) {
        int removeOption = ui.intInput("Choose product number to remove: ");
        if (removeOption < 1 || removeOption > items.length) {
            ui.showMessage("Invalid option.");
            return;
        }
        saleManager.removeFromCart(items[removeOption - 1].getProductId());
        ui.showMessage("Product removed from cart.");
    }

    private String resolveProviderName(int providerId) {
        try {
            Provider provider = providerManager.findById(providerId);
            if (provider != null) {
                return provider.getCompanyName();
            }
        } catch (BusinessException e) {
            return "Provider " + providerId;
        }
        return "Provider " + providerId;
    }

    private ProductForSale findOfferInProvider(Provider provider, String productId) {
        for (ProductForSale offer : provider.getProductsForSale()) {
            if (productId.equals(offer.getProductId())) {
                return offer;
            }
        }
        return null;
    }

    private Product findProductSafely(String productId) {
        try {
            return productManager.findById(productId);
        } catch (BusinessException e) {
            return null;
        }
    }

    private String formatProductName(Product product) {
        if (product.getBrand().isBlank() && product.getModel().isBlank()) {
            return product.getProductName();
        }
        return product.getProductName() + " - " + product.getBrand() + " (" + product.getModel() + ")";
    }
}
