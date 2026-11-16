package presentation;

import java.util.Scanner;

public class UserInterface {
    private final Scanner sc;

    public UserInterface() {
        this.sc = new Scanner(System.in);
    }

    public void showBanner() {
        System.out.println("##################################");
        System.out.println("#                                #");
        System.out.println("#     NUDE EYE PROJECT           #");
        System.out.println("#                                #");
        System.out.println("##################################");
        System.out.println();
        System.out.println("Welcome to Nude Eye Project.");
        System.out.println("\"by glass wearers for glass wearers\"");
        System.out.println();
    }

    public void showAuthMenu() {
        System.out.println();
        System.out.println("\t1) Login");
        System.out.println("\t2) Sign up");
        System.out.println();
        System.out.println("\t0) Exit");
    }

    public void showClientTypeMenu() {
        System.out.println("Client type:");
        System.out.println("\t1) In-person client");
        System.out.println("\t2) Online client");
        System.out.println("\t3) Corporate client");
    }

    public void showMainMenu(String fullName) {
        System.out.println("Welcome " + fullName + "!");
        System.out.println();
        System.out.println("\t1) User profile");
        System.out.println("\t2) Find products by name");
        System.out.println("\t3) Find products by provider");
        System.out.println("\t4) Shopping cart");
        System.out.println();
        System.out.println("\t0) Logout");
    }

    public void showUserProfile(int clientId, String fullName, String[] phoneLines, String[] specificLines, String[] historyLines) {
        System.out.println("--| User Profile |--");
        System.out.println();
        System.out.println("Client ID: " + clientId);
        System.out.println("Full name: " + fullName);
        System.out.println("Phone numbers:");
        for (String line : phoneLines) {
            System.out.println(line);
        }
        for (String line : specificLines) {
            System.out.println(line);
        }
        System.out.println("Shopping history:");
        for (String line : historyLines) {
            System.out.println(line);
        }
    }

    public void showUserProfile(int clientId, String fullName, String[] phoneLines, String[] historyLines) {
        showUserProfile(clientId, fullName, phoneLines, new String[0], historyLines);
    }

    public void showProductList(String[] productLines) {
        if (productLines.length == 0) {
            System.out.println("No products found.");
            return;
        }
        for (String line : productLines) {
            System.out.println(line);
        }
        System.out.println();
        System.out.println("\t0) Back");
    }

    public void showProductDetail(String productId, String name, String brand, String model, String productType, String[] specificLines, String[][] providerLines) {
        System.out.println("Product ID: " + productId + ",");
        System.out.println("Name: " + name + ",");
        if (!brand.isBlank()) {
            System.out.println("Brand: " + brand + ",");
        }
        if (!model.isBlank()) {
            System.out.println("Model: " + model + ",");
        }
        System.out.println("Type: " + productType + ",");
        for (String line : specificLines) {
            System.out.println(line + ",");
        }
        System.out.println("Providers:");
        for (String[] lines : providerLines) {
            for (String line : lines) {
                System.out.println(line);
            }
        }
    }

    public void showProductDetail(String productId, String name, String brand, String model, String[][] providerLines) {
        showProductDetail(productId, name, brand, model, "Product", new String[0], providerLines);
    }

    public void showProviderList(String[] providerLines) {
        if (providerLines.length == 0) {
            System.out.println("No providers found.");
            return;
        }
        for (String line : providerLines) {
            System.out.println(line);
        }
        System.out.println();
        System.out.println("\t0) Back");
    }

    public void showProviderDetail(String companyName, String cif, String contactName, String phone, String email, String[][] productLines) {
        System.out.println("Name: " + companyName + ",");
        System.out.println("CIF: " + cif + ",");
        System.out.println("Contact person: " + contactName + ",");
        System.out.println("Phone number: " + phone + ",");
        System.out.println("Contact email: " + email + ",");
        System.out.println("Provider products:");
        System.out.println();
        for (String[] lines : productLines) {
            for (String line : lines) {
                System.out.println(line);
            }
        }
        System.out.println("\t0) Back");
    }

    public void showCart(String[] cartLines, float totalPrice) {
        System.out.println("Shopping cart:");
        if (cartLines.length == 0) {
            System.out.println("Cart is empty.");
            return;
        }
        for (String line : cartLines) {
            System.out.println(line);
        }
        System.out.println();
        System.out.printf("%-50s %8.2f€%n", "TOTAL PRICE:", totalPrice);
    }

    public void showCartMenu() {
        System.out.println();
        System.out.println("\t1) Remove product");
        System.out.println("\t2) Proceed to purchase");
        System.out.println("\t0) Back");
    }

    public String stringInput(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    public int intInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please enter a number.");
            }
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}
