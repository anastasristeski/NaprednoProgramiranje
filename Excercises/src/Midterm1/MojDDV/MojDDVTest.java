package Midterm1.MojDDV;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.Scanner;
import java.util.stream.Collectors;

class AmountNotAllowedException extends Exception{
    public AmountNotAllowedException(int suma) {
        super(String.format("Receipt with amount %d is not allowed to be scanned",suma));
    }
}
enum Type{
    A,B,V
}
class Item{
    int price;
    Type type;

    public Item(int price, Type type) {
        this.price = price;
        this.type = type;
    }

    public double getTax(){
        if(type.equals(Type.A)){
            return price*0.18;
        }
        if(type.equals(Type.B)){
            return price*0.05;
        }
        if(type.equals(Type.V)){
            return 0.0;
        }
        else return -1;
    }
    public double povrat(){
        return getTax()*0.15;
    }
    public int getPrice() {
        return price;
    }
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Item{" +
                "price=" + price +
                ", type=" + type +
                '}';
    }
}
class Smetka{
    String id;
    ArrayList<Item> items;

    public Smetka(String id, ArrayList<Item> items) throws AmountNotAllowedException {
        int suma = items.stream()
                .mapToInt(Item::getPrice)
                .sum();
        if(suma>30000){
            throw new AmountNotAllowedException(suma);
        }
        this.id = id;
        this.items = items;
    }
    public int getAmount(){
        return items.stream()
                .mapToInt(Item::getPrice)
                .sum();
    }
    public double taxReturns(){
        return items.stream()
                .mapToDouble(Item::getTax)
                .sum()*0.15;
    }

    @Override
    public String toString() {
        return String.format("%10s\t%10d\t%10.5f",id,getAmount(),taxReturns());
    }
}
class MojDDV{
    ArrayList<Smetka> smetki;
    public MojDDV() {
        smetki = new ArrayList<>();
    }
    public void readRecords(InputStream inputStream) {
        Scanner sc = new Scanner(inputStream);
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            String[]parts = line.split("\\s+");
            String id = parts[0];
            ArrayList<Item> items = new ArrayList<>();
            for(int i=1;i<parts.length;i+=2){
                items.add(new Item(Integer.parseInt(parts[i]),Type.valueOf(parts[i+1])));
            }
            try{
                smetki.add(new Smetka(id, items));
            }catch(AmountNotAllowedException e){
                System.out.println(e.getMessage());
            }
        }
    }
    public void printTaxReturns(OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);
        for (Smetka smetka : smetki) {
           pw.println(smetka);
        }
        pw.flush();
    }
    public void printStatistics(OutputStream outputStream){
        DoubleSummaryStatistics stats = smetki.stream().mapToDouble(Smetka::taxReturns).summaryStatistics();
        String s = String.format("min:\t%.3f\nmax:\t%.3f\nsum:\t%.3f\ncount:\t%d\navg:\t%.3f",stats.getMin(),stats.getMax(),stats.getSum(),stats.getCount(),stats.getAverage());
        PrintWriter pw = new PrintWriter(outputStream);
        pw.println(s);
        pw.flush();
    }
}
public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
            mojDDV.readRecords(System.in);



        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);
        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);

    }
}