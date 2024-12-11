package Midterm1.Canvas;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

class Canvas{
    String id;
    ArrayList<Integer>sizes;
    Canvas(String id,ArrayList<Integer>sizes){
        this.id = id;
        this.sizes=sizes;
    }
    int getPerimeter(){
        return sizes.stream().mapToInt(Integer::intValue).sum()*4;
    }

    @Override
    public String toString() {
        return String.format("%s %d %d",id,sizes.size(),getPerimeter());
    }

}
class ShapesApplication{
    ArrayList<Canvas> list;
    ShapesApplication(){
        list = new ArrayList<>();
    }
    int readCanvases (InputStream inputStream){
        int counter=0;
        Scanner sc = new Scanner(inputStream);
        while(sc.hasNextLine()){
            ArrayList<Integer>sizes = new ArrayList<>();
            String string = sc.nextLine();
            String canvasId = string.split("\\s+")[0];
            String [] parts = string.split("\\s+");
            for(int i=1;i< parts.length;i++){
                sizes.add(Integer.parseInt(parts[i]));
            }
            counter += sizes.size();
            list.add(new Canvas(canvasId,sizes));
        }
        return counter;
    }
    void printLargestCanvasTo (OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);
        pw.println(list.stream()
                .max(Comparator.comparing(Canvas::getPerimeter)).orElse(null));
        pw.flush();
    }
}
public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}