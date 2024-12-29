package LABS.LAB5;

public class test {
    static boolean matching(String in,String text){
        return in.contains(text);
    }
    public static void main(String[] args) {
        System.out.println(matching("ananas","ana"));
    }
}
