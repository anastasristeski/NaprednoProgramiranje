package Midterm2.Disconuts;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

class Product implements Comparable<Product>{
    int discountedPrice;
    int originalPrice;

    public Product(String line){
        String[]parts = line.split(":");
        discountedPrice=Integer.parseInt(parts[0]);
        originalPrice=Integer.parseInt(parts[1]);
    }
    public int percentDiscount(){
        return (int)(100-((float) discountedPrice/originalPrice*100));
    }
    public int totalDiscount(){
        return originalPrice-discountedPrice;
    }

    @Override
    public String toString() {
        return String.format("%2d%% %d/%d",percentDiscount(),discountedPrice,originalPrice);
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }

    @Override
    public int compareTo(Product o) {
        return Comparator.comparing(Product::percentDiscount)
                .thenComparing(Product::totalDiscount)
                .thenComparing(Product::getDiscountedPrice)
                .compare(o,this);
    }
}
class Store{
    String name;
    TreeSet<Product> products;
    public Store(String line){
        String[]parts = line.split("\\s+");
        name = parts[0];
        products = Arrays.stream(parts)
                .skip(1)
                .map(Product::new)
                .collect(Collectors.toCollection(TreeSet::new));
    }
    public float averageDiscount(){
        return (float)products.stream().mapToInt(Product::percentDiscount).average().orElse(0);
    }
    public int totalDiscount(){
        return products.stream().mapToInt(Product::totalDiscount).sum();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s\nAverage discount: %.1f%%\nTotal discount: %d\n",name,averageDiscount(),totalDiscount()));
        String str = products.stream().map(Product::toString).collect(Collectors.joining("\n"));
        sb.append(str);
        //products.forEach(x->sb.append(x.toString()));
        return sb.toString();
    }
}
class Discounts{
    Set<Store> stores;

    public Discounts() {
        this.stores = new HashSet<>();
    }
    public int readStores(InputStream inputStream){
        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
        stores = bf.lines().map(Store::new).collect(Collectors.toSet());
        return stores.size();
    }
    public List<Store> byAverageDiscount(){
        return stores.stream()
                .sorted(Comparator.comparing(Store::averageDiscount).reversed().thenComparing(Store::getName))
                .limit(3)
                .collect(Collectors.toList());
    }
    public List<Store> byTotalDiscount(){
        return stores.stream()
                .sorted(Comparator.comparing(Store::totalDiscount))
                .limit(3)
                .collect(Collectors.toList());
    }

}
public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}