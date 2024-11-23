package Canvas2;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

enum TYPE{
    Square,Circle
}
 abstract class Shape{
        TYPE type;
  abstract double area();

}
class Square extends Shape{
    ArrayList<Integer> sizes;

    public Square() {
        sizes = new ArrayList<>();
    }

    @Override
    double area() {
        return 0;
    }
}
class Circle extends Shape{
    ArrayList<Integer> sizes;

    public Circle() {
        sizes = new ArrayList<>();
    }

    @Override
    double area() {
        return 0;
    }
}
class ShapesApplication{
double maxArea;
ArrayList<Shape> shapes;

    public ShapesApplication(double maxArea) {
        this.maxArea = maxArea;
        this.shapes = new ArrayList<>();
    }
   public void readCanvases (InputStream inputStream){
        Scanner sc = new Scanner(inputStream);
    }
}
public class Shapes2Test {

    public static void main(String[] args) {

      ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
       shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        //shapesApplication.printCanvases(System.out);


    }
}