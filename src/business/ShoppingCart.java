package business;

import java.util.ArrayList;
import java.util.List;








public class ShoppingCart {

    
    private final List<ProductForSale> items;

    


    public ShoppingCart() {
        this.items = new ArrayList<>();
    }

    




    public void addItem(ProductForSale item) {
        items.add(item);
    }

    




    public void remove(String productId) {
        items.removeIf(item -> item.getProductId().equals(productId));
    }

    




    public ProductForSale[] getItems() {
        return items.toArray(new ProductForSale[0]);
    }

    




    public boolean isEmpty() {
        return items.isEmpty();
    }

    


    public void clear() {
        items.clear();
    }
}
