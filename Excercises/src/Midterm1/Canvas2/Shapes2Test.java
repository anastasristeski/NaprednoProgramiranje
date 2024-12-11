package Midterm1.Canvas2;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

class IrregularCanvasException extends Exception{
    public IrregularCanvasException(String canvas_id,double maxArea){
        super(String.format("Canvas %s has a shape with area larger than %.2f",canvas_id,maxArea));
    }
}
abstract class Shape{
    String type;
    double size;

    public Shape(String type, double size) {
        this.type = type;
        this.size = size;
    }

    abstract double getArea();
    abstract String getType();
}
class Circle extends Shape{

    public Circle(double size) {
        super("C", size);
    }

    @Override
    double getArea() {
        return Math.PI * size * size;
    }

    @Override
    String getType() {
        return type;
    }
}
class Square extends Shape{

    public Square(double size) {
        super("S", size);
    }

    @Override
    double getArea() {
        return size*size;
    }
    @Override
    String getType() {
        return type;
    }
}
class Canvas{
    String id;
    ArrayList<Shape> shapes;
    public Canvas(){
        shapes = new ArrayList<>();
        this.id ="";
    }
    public Canvas(String id,ArrayList<Shape> shapes){
        this.id=id;
        this.shapes = shapes;
    }
    public double maxArea(){
        return shapes.stream()
                .mapToDouble(Shape::getArea)
                .max()
                .orElse(0.0);
    }
    public double getArea(){
        return shapes.stream()
                .mapToDouble(Shape::getArea)
                .sum();
    }
    @Override
    public String toString() {
        int totalShapes = shapes.size();
        int totalCircles = (int) shapes.stream()
                .filter(x->x.type.equals("C"))
                .count();
        int totalSquares = (int) shapes.stream()
                .filter(x->x.type.equals("S"))
                .count();
        double minArea = shapes.stream()
                .mapToDouble(Shape::getArea)
                .min()
                .orElse(0.0);
        double maxArea = shapes.stream()
                .mapToDouble(Shape::getArea)
                .max()
                .orElse(0.0);
        double averageArea = shapes.stream()
                .mapToDouble(Shape::getArea)
                .average()
                .orElse(0.0);
        return String.format("%s %d %d %d %.2f %.2f %.2f",id,totalShapes,totalCircles,totalSquares,minArea,maxArea,averageArea);
    }
}
class ShapesApplication{
    double maxArea;
    ArrayList<Canvas> canvases;

    public ShapesApplication(double maxArea) {
        this.canvases = new ArrayList<>();
        this.maxArea = maxArea;
    }
    public void addCanvas(Canvas c) throws IrregularCanvasException {
        if(c.maxArea()>maxArea)
            throw new IrregularCanvasException(c.id, maxArea);
        canvases.add(c);
    }
    public void readCanvases(InputStream inputStream) throws IrregularCanvasException {
        Scanner sc = new Scanner(inputStream);
        while(sc.hasNextLine()){
            String [] parts = sc.nextLine().split(" ");
            String canvas_id = parts[0];
            ArrayList<Shape> shapes = new ArrayList<>();
            for(int i=1;i<parts.length;i+=2){
                if(Objects.equals(parts[i], "C"))
                    shapes.add(new Circle(Double.parseDouble(parts[i+1])));
                else
                    shapes.add(new Square(Double.parseDouble(parts[i+1])));
            }
            try{
             addCanvas(new Canvas(canvas_id,shapes));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public void printCanvases(OutputStream os){
        PrintWriter pw = new PrintWriter(os);
        canvases.stream()
                .sorted(Comparator.comparing(Canvas::getArea).reversed())
                        .forEach(pw::println);

        pw.flush();
    }
}
public class Shapes2Test {

    public static void main(String[] args) throws IrregularCanvasException {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");

        shapesApplication.readCanvases(System.in);


        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}