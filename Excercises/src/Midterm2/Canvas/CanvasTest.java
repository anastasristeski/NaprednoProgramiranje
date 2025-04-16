package Midterm2.Canvas;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
class InvalidIDException extends Exception{
    public InvalidIDException() {
        super("InvalidIDException");
    }
}
class InvalidDimensionException extends Exception{
    public InvalidDimensionException() {
        super("Dimension 0 is not allowed!");
    }
}
abstract class Shape{
    String id;
    public Shape(String id) throws InvalidIDException {
        if(!checkId(id)){
            throw new InvalidIDException();
        }
        this.id = id;
    }
    abstract void scaleShapes(double coef);
    abstract double perimeter();
    abstract double area();
    public boolean checkId(String id){
        if (id.length()!=6)
            return false;
        return id.matches("[a-zA-Z0-9]+");

    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shape shape = (Shape) o;
        return id.equals(shape.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    abstract public String toString();
}
class Circle extends Shape{
    double radius;
    public Circle(String id,double radius) throws InvalidIDException {
        super(id);
        this.radius=radius;
    }
    @Override
    void scaleShapes(double coef) {
        radius*=coef;
    }
    @Override
    double perimeter() {
        return 2*Math.PI*radius;
    }
    @Override
    double area() {
        return Math.PI*radius*radius;
    }

    @Override
    public String toString() {
        return String.format("Circle -> Radius: %.2f Area: %.2f Perimeter: %.2f",radius,area(),perimeter());
    }

}
class Square extends Shape{
    double side;
    public Square(String id,double side) throws InvalidIDException {
        super(id);
        this.side = side;
    }

    @Override
    void scaleShapes(double coef) {
        side*=coef;
    }

    @Override
    double perimeter() {
        return 4*side;
    }

    @Override
    double area() {
        return side*side;
    }
    @Override
    public String toString() {
        return String.format("Square: -> Side: %.2f Area: %.2f Perimeter: %.2f",side,area(),perimeter());
    }
}
class Rectangle extends Shape{
    double a;
    double b;
    public Rectangle(String id,double a,double b) throws InvalidIDException {
        super(id);
        this.a=a;
        this.b=b;
    }

    @Override
    void scaleShapes(double coef) {
        a*=coef;
        b*=coef;
    }

    @Override
    double perimeter() {
        return 2*(a+b);
    }

    @Override
    double area() {
        return a*b;
    }
    public String toString() {
        return String.format("Rectangle: -> Sides: %.2f, %.2f Area: %.2f Perimeter: %.2f",a,b,area(),perimeter());
    }

}

class Canvas{
    Set<Shape>shapes;
    Comparator<Shape> sortedByArea = Comparator.comparing(Shape::area);

    public Canvas(){
        shapes = new TreeSet<>(sortedByArea);

    }
    public void readShapes(InputStream is) throws InvalidDimensionException, InvalidIDException {
        Scanner sc = new Scanner(is);
        while(sc.hasNextLine()){
            String [] parts = sc.nextLine().split("\\s+");
            if(Double.parseDouble(parts[2])==0.0){
                throw new InvalidDimensionException();
            }

               if(parts[0].equals("1")){
                   shapes.add(new Circle(parts[1], Double.parseDouble(parts[2])));
               }
                if(parts[0].equals("2")){
                    shapes.add(new Square(parts[1], Double.parseDouble(parts[2])));
                }
                if(parts[0].equals("3")){
                    if(Double.parseDouble(parts[3])==0.0)
                        throw new InvalidDimensionException();
                    shapes.add(new Rectangle(parts[1], Double.parseDouble(parts[2]),Double.parseDouble(parts[3])));
                }

            }
        }
    public void printAllShapes (OutputStream os){
        PrintWriter pw = new PrintWriter(os);
        shapes.forEach(pw::println);
        pw.flush();
    }
    public void scaleShapes (String userID, double coef){
        shapes.stream().filter(x->x.getId().equals(userID))
                .forEach(x->x.scaleShapes(coef));
    }
    public void printByUserId (OutputStream os){
        PrintWriter pw = new PrintWriter(os);
        Map<String,List<Shape>> groupByUser = shapes.stream()
                        .collect(Collectors.groupingBy(Shape::getId));
        List<Map.Entry<String,List<Shape>>>sortedUsers = groupByUser.entrySet().stream()
                .sorted(Comparator.<Map
                                .Entry<String,List<Shape>>>comparingInt
                                (entry->entry.getValue().size())
                        .reversed().thenComparingDouble(entry->entry.getValue().stream().mapToDouble(Shape::area).sum()).reversed())
                        .collect(Collectors.toList());
        pw.flush();
    }
    public void statistics(OutputStream os){
        PrintWriter pw = new PrintWriter(os);
        DoubleSummaryStatistics ds = shapes.stream().mapToDouble(Shape::area).summaryStatistics();
        pw.printf("count: %d\nsum: %.2f\nmin: %.2f\naverage: %.2f\nmax: %.2f",
                ds.getCount(),ds.getSum(),ds.getMin(),ds.getAverage(),ds.getMax());
        pw.flush();
    }
}
public class CanvasTest {

    public static void main(String[] args) throws InvalidDimensionException, InvalidIDException {
        Canvas canvas = new Canvas();

        System.out.println("READ SHAPES AND EXCEPTIONS TESTING");
        try{
            canvas.readShapes(System.in);
        }catch(InvalidDimensionException | InvalidIDException e){
            System.out.println(e.getMessage());
        }

        System.out.println("BEFORE SCALING");
        canvas.printAllShapes(System.out);
        canvas.scaleShapes("123456", 1.5);
        System.out.println("AFTER SCALING");
        canvas.printAllShapes(System.out);

        System.out.println("PRINT BY USER ID TESTING");
        canvas.printByUserId(System.out);

        System.out.println("PRINT STATISTICS");
        canvas.statistics(System.out);
    }
}