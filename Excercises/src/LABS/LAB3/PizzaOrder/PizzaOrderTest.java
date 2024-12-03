package LABS.LAB3.PizzaOrder;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
class InvalidPizzaTypeException extends Exception{

}
class EmptyOrder extends Exception{

}
class OrderLockedException extends Exception{

}
class InvalidExtraTypeException extends Exception{

}
class ItemOutOfStockException extends Exception{
    Item item;

    public ItemOutOfStockException(Item item) {
        this.item = item;
    }
    public String getMessage(){

        return item.toString();
    }
}
interface Item{
    int getPrice();
    int singleItemPrice();
    String getType();
    String getItemType();
}

class PizzaItem implements Item{
    String type;
    int count;

    public PizzaItem(String type, int count) throws InvalidPizzaTypeException {
        if(checkType(type))
            throw new InvalidPizzaTypeException();
        this.type = type;
        this.count = count;
    }

    public PizzaItem(String type) throws InvalidPizzaTypeException {
        if(checkType(type))
            throw new InvalidPizzaTypeException();
        this.type = type;
        this.count = 1;
    }
    public boolean checkType(String type){
        return !type.equals("Standard") && !type.equals("Pepperoni") && !type.equals("Vegetarian");
    }
    @Override
    public int getPrice() {
        return singleItemPrice()*count;
    }
    @Override
    public int singleItemPrice() {
        if(type.equals("Standard"))
            return 10;
        if(type.equals("Pepperoni"))
            return 12;
        return 8;
    }
    @Override
    public String getItemType(){
        return "PizzaItem";
    }
    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PizzaItem pizzaItem = (PizzaItem) o;
        return Objects.equals(type, pizzaItem.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return String.format("%-15sx%2d%5d$",type,count,getPrice());
    }
}
class ExtraItem implements Item{
    String type;
    int count;

    public ExtraItem(String type) throws InvalidExtraTypeException {
        if(checkType(type)) {
            throw new InvalidExtraTypeException();
        }
        this.type = type;
        this.count = 1;
    }

    public ExtraItem(String type, int count) throws InvalidExtraTypeException {
        if(checkType(type)) {
            throw new InvalidExtraTypeException();
        }
        this.type = type;
        this.count = count;
    }

    public boolean checkType(String type){
        return !type.equals("Coke") && !type.equals("Ketchup");
    }
    @Override
    public int getPrice() {
        return singleItemPrice()*count;
    }
    @Override
    public String getItemType(){
        return "ExtraItem";
    }
    @Override
    public int singleItemPrice() {
        if(type.equals("Ketchup"))
            return 3;
        return 5;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtraItem extraItem = (ExtraItem) o;
        return Objects.equals(type, extraItem.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return String.format("%-15sx%2d%5d$",type,count,getPrice());
    }
}
class Order{
    ArrayList<Item> items;
    boolean lockOrder = false;
    public Order() {
        this.items = new ArrayList<>();
    }
    public void addItem(Item item, int count) throws ItemOutOfStockException, InvalidPizzaTypeException, InvalidExtraTypeException, OrderLockedException {
       if(lockOrder)
           throw new OrderLockedException();
        if(count>10)
            throw new ItemOutOfStockException(item);
        Item temp = createItem(item,count);
        if(items.contains(item)){
            for(int i=0;i<items.size();i++){
                if(items.get(i).equals(item)){
                        items.set(i,temp);
                }
            }
        }
        else{
            items.add(temp);
        }
    }
    public Item createItem(Item item,int count) throws InvalidExtraTypeException, InvalidPizzaTypeException {
        if(item.getItemType().equals("PizzaItem"))
            return new PizzaItem(item.getType(),count);
        return new ExtraItem(item.getType(),count);
    }

    public int getPrice(){
        return items.stream()
                .mapToInt(Item::getPrice)
                .sum();
    }

    public void displayOrder(){
        StringBuilder sb = new StringBuilder();
        int incrementor = 1;
        for(Item i : items){
            sb.append(String.format("%3d.",incrementor++));
            sb.append(i.toString()).append("\n");
        }
        sb.append(String.format("%-22s%5d$","Total:",getPrice()));
        System.out.println(sb);
    }
    public void removeItem(int idx) throws OrderLockedException {
        if(lockOrder)
            throw new OrderLockedException();
        if(idx<0 || idx>=items.size())
            throw new ArrayIndexOutOfBoundsException();
        items.remove(idx);
    }
    public void lock() throws EmptyOrder {
        if(items.isEmpty())
            throw new EmptyOrder();
        this.lockOrder = true;
    }
}
public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}