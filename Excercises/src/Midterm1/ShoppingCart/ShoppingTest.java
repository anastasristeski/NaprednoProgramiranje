package Midterm1.ShoppingCart;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class InvalidOperationException extends Exception{
    public InvalidOperationException(String id) {
        super(String.format("The quantity of the product with id %s can not be 0.",id));
    }
    public InvalidOperationException(){
        super("There are no products with discount.");
    }
}
class Product{
    String type;
    String id;
    String name;
    int price;

    public Product(String type, String id, String name, int price) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.price = price;
    }
    public double getFullPrice(){
        return price;
    }
    public Integer getId(){
        return Integer.parseInt(id);
    }
    public String getReportForDiscounted(){
        return String.format("%s - %.2f",id,getFullPrice()-(getFullPrice()*0.9));
    }
}
class WSProduct extends Product{
    int quantity;
    public WSProduct(String type, String id, String name, int price,int quantity) throws InvalidOperationException {
        super(type, id, name, price);
        if(quantity==0){
            throw new InvalidOperationException(id);
        }
        this.quantity = quantity;
    }
    @Override
    public double getFullPrice(){
        return price * quantity;
    }

    @Override
    public String toString() {
        return String.format("%s - %.2f",id,getFullPrice());
    }

}
class PSProduct extends Product{
    double quantity;
    public PSProduct(String type, String id, String name, int price,double quantity) throws InvalidOperationException {
        super(type, id, name, price);
        if(quantity==0.0){
            throw new InvalidOperationException(id);
        }
        this.quantity = quantity;
    }
    @Override
    public double getFullPrice(){
        return price * (quantity/1000);
    }
    @Override
    public String toString() {
        return String.format("%s - %.2f",id,getFullPrice());
    }
}
class ShoppingCart{
    List<Product> products;
    public ShoppingCart() {
        this.products = new ArrayList<>();
    }
    public void addItem(String itemData) throws InvalidOperationException {
        String [] parts = itemData.split(";");
        String type = parts[0];
        String productId = parts[1];
        String productName = parts[2];
        int productPrice = Integer.parseInt(parts[3]);
        if(type.equals("WS")){
            int quantity = Integer.parseInt(parts[4]);
            products.add(new WSProduct(type,productId,productName,productPrice,quantity));
        }
        else{
            double quantity = Double.parseDouble(parts[4]);
            products.add(new PSProduct(type,productId,productName,productPrice,quantity));
        }
    }
    public void printShoppingCart(OutputStream os){
        PrintWriter pw = new PrintWriter(os);
        products.sort(Comparator.comparing(Product::getFullPrice).reversed());
        for(Product p : products){
            pw.println(p);
        }
        pw.flush();
    }
    public void blackFridayOffer(List<Integer>discountItems,OutputStream os) throws InvalidOperationException {
        PrintWriter pw = new PrintWriter(os);
        List<Product> discounted = products.stream()
                .filter(x->discountItems.contains(x.getId()))
                .collect(Collectors.toList());
        if(discountItems.isEmpty()){
            throw new InvalidOperationException();
        }
        for(Product p : discounted){
            pw.println(p.getReportForDiscounted());
        }
        pw.flush();
    }
}
public class ShoppingTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ShoppingCart cart = new ShoppingCart();

        int items = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < items; i++) {
            try {
                cart.addItem(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        List<Integer> discountItems = new ArrayList<>();
        int discountItemsCount = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < discountItemsCount; i++) {
            discountItems.add(Integer.parseInt(sc.nextLine()));
        }

        int testCase = Integer.parseInt(sc.nextLine());
        if (testCase == 1) {
            cart.printShoppingCart(System.out);
        } else if (testCase == 2) {
            try {
                cart.blackFridayOffer(discountItems, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}