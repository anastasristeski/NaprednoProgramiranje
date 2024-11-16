package Canvas2;

import java.util.ArrayList;

enum TYPE{
    Square,Circle
}
 abstract class Shape{
        TYPE type;
        int size;

  abstract double area();

}
class Square extends Shape{
    String id;
    ArrayList<Integer> sizes;

    public Square() {
        id = "";
        sizes = new ArrayList<>();
    }

    @Override
    double area() {
        return 0;
    }
}
class Circle extends Shape{
    String id;
    ArrayList<Integer> sizes;

    public Circle() {
        id = "";
        sizes = new ArrayList<>();
    }

    @Override
    double area() {
        return 0;
    }
}
class ShapesApplication{
double maxArea;
}
public class Shapes2Test {

    public static void main(String[] args) {

      ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
       shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}