package Midterm1.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
class Component{
    String color;
    int weight;
    int position;
    ArrayList<Component>components;

    public Component(String color, int weight) {
        this.color = color;
        this.weight = weight;
        position = -1;
        components = new ArrayList<>();
    }
    public void addComponent(Component component){
        components.add(component);
        components.sort(Comparator.comparing(Component::getWeight).thenComparing(Component::getColor));
    }
    public String getColor(){
        return color;
    }
    public int getWeight(){
        return weight;
    }
    public void setColor(int weight,String color){
        if(weight>this.weight)
            this.color = color;
        components.forEach(c->c.setColor(weight,color));
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return strInd(0);
    }
    public String strInd(int indentLevel){
        StringBuilder sb = new StringBuilder();
        String indent = "-".repeat(indentLevel*3);
        sb.append(indent);
        if(indentLevel==0)
            sb.append(position).append(":");
        sb.append(weight).append(":").append(color).append("\n");
        for(Component c : components)
            sb.append(c.strInd(indentLevel+1));
        return sb.toString();
    }

}
class Window{
    String name;
    ArrayList<Component> list;

    public Window(String name) {
        this.name = name;
        this.list=new ArrayList<>();
    }
    public void addComponent(int position,Component component) throws InvalidPositionException {
        if(!checkPosition(position)){
            throw new InvalidPositionException(position);
        }
        else{
            list.add(component);
            component.setPosition(position);
            list.sort(Comparator.comparing(Component::getPosition));
        }
    }
    public boolean checkPosition(int position){
        for(Component c : list){
            if(c.getPosition()==position){
                return false;
            }
        }
        return true;
    }
    public void changeColor(int weight,String color){
        list.forEach(c->c.setColor(weight,color));
    }
    public void swichComponents(int pos1,int pos2){
       Component comp1=null;
       Component comp2=null;
       for(Component c:list){
           if(c.getPosition()==pos1)
               comp1=c;
           if(c.getPosition()==pos2)
               comp2=c;
       }
       if(comp1!=null&&comp2!=null){
           int temp = comp1.getPosition();
           comp1.setPosition(pos2);
           comp2.setPosition(pos1);
           list.sort(Comparator.comparing(Component::getPosition));
       }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WINDOW ").append(name).append("\n");
        for(Component c: list)
            sb.append(c);
        return sb.toString();
    }
}
class InvalidPositionException extends Exception{
    public InvalidPositionException(int pos) {
        super(String.format("Invalid position %d, alredy taken!",pos));
    }
}
public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.swichComponents(pos1, pos2);
        System.out.println(window);
    }
}

// вашиот код овде