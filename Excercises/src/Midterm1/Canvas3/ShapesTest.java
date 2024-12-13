package Midterm1.Canvas3;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
enum Color {
    RED, GREEN, BLUE
}
interface Scalable{
    public void scale(float scaleFactor);

}
interface Stackable{
    public float weight();
}
abstract class Shape implements Scalable,Stackable{
    String id;
    Color color;
    public Shape(String id,Color color){
        this.id = id;
        this.color= color;
    }
    @Override
    abstract public void scale(float scaleFactor);

    @Override
    abstract public float weight();
}
class Circle extends Shape{
    float radius;

    public Circle(String id, Color color,float radius) {
        super(id, color);
        this.radius = radius;
    }

    @Override
    public void scale(float scaleFactor) {
        radius +=scaleFactor;
    }

    @Override
    public float weight() {
        return (float) (Math.PI * radius * radius);
    }

    @Override
    public String toString() {
        return String.format("C: %-5s%10s%10.2f\n",id,color.toString(),weight());
    }
}
class Rectangle extends Shape{
    float width;
    float height;
    public Rectangle(String id, Color color,float width,float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }
    @Override
    public void scale(float scaleFactor) {
        width += scaleFactor;
        height += scaleFactor;
    }

    @Override
    public float weight() {
        return width * height;
    }
    @Override
    public String toString() {
        return String.format("R: %-5s%10s%10.2f\n",id,color.toString(),weight());
    }
}
class Canvas {
    ArrayList<Shape> shapes;
    public Canvas(){
        shapes = new ArrayList<>();
    }
    public void add(String id,Color color,float radius){
        Circle c = new Circle(id,color,radius);
        int i=0;
        while(i<shapes.size() && shapes.get(i).weight()>=c.weight())
            i++;
        shapes.add(i,c);
    }
    public void add(String id,Color color,float width,float height){
        Rectangle r = new Rectangle(id,color,width,height);
        int i=0;
        while(i<shapes.size() && shapes.get(i).weight()>=r.weight())
            i++;
        shapes.add(i,r);
    }
    public void scale(String id,float scaleFactor){
        for (Shape shape : shapes) {
            if (shape.id.equals(id))
                shape.scale(scaleFactor);
        }
        shapes.sort(Comparator.comparing(Shape::weight).reversed());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        shapes.forEach(sb::append);
        return sb.toString();
    }
}
public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}

