package LABS.LAB3.ResizableArray;
import java.util.Arrays;
import java.util.Scanner;
import java.util.LinkedList;
class ResizableArray<T>{
    private T [] elements;
    @SuppressWarnings("unchecked")
    ResizableArray(){
        elements = (T[]) new Object[0];
    }
    public void addElement(T element){
        T [] newElements = Arrays.copyOf(elements,elements.length+1);
        newElements[newElements.length-1]=element;
        elements = newElements;
    }
    @SuppressWarnings("unchecked")
    public boolean removeElement(T element){
        int index = findElementIndex(element);
        if(index == -1)
            return false;
        elements[index] = elements[count()-1];
        T [] newElements = (T[]) new Object[elements.length-1];
        newElements = Arrays.copyOf(elements,elements.length-1);
        elements = newElements;
        return true;
    }
    public int count(){
        return elements.length;
    }
    public int findElementIndex(T element){
        for(int i=0;i<elements.length;i++){
            if(element.equals(elements[i]))
                return i;
        }
        return -1;
    }
    public boolean contains(T element){
        for(T e : elements){
            if (element.equals(e)){
                return true;
            }
        }
        return false;
    }
    public Object [] toArray(){
        return Arrays.stream(elements).toArray();
    }
    public boolean isEmpty(){
        return elements.length==0;
    }
    public T elementAt(int index){
        if(index<0||index >=elements.length){
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return elements[index];
    }
    @SuppressWarnings("unchecked")
    public static <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src){
        T [] array = (T[]) Arrays.copyOf(src.toArray(),src.count());
        for(T element : array){
            dest.addElement(element);
        }
    }
}
class IntegerArray extends ResizableArray<Integer>{
    public double sum(){
        double s =0.0;
        for(int i=0;i<count();i++){
            s+=elementAt(i);
        }
        return s;
    }
    public double mean(){
        return sum()/count();
    }
    public int countNonZero(){
        int counter = 0;
        for(int i=0;i<count();i++){
            if(elementAt(i)!=0)
                counter++;
        }
        return counter;
    }
    public IntegerArray distinct(){
        IntegerArray array = new IntegerArray();
        for(int i=0;i<count();i++){
            if(!array.contains(elementAt(i))){
                array.addElement(elementAt(i));
            }
        }
        return array;
    }
    public IntegerArray increment(int offset){
        IntegerArray arr = new IntegerArray();
        for(int i=0;i<count();i++){
            arr.addElement(elementAt(i)+offset);
        }
        return arr;
    }
}
public class ResizableArrayTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if ( test == 0 ) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while ( jin.hasNextInt() ) {
                last = jin.nextInt();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
        }
        if ( test == 1 ) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for ( int i = 0 ; i < 4 ; ++i ) {
                last = jin.next();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            ResizableArray<String> b = new ResizableArray<String>();
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));

            System.out.println(a.removeElement(first));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
        }
        if ( test == 2 ) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while ( jin.hasNextInt() ) {
                a.addElement(jin.nextInt());
            }
            jin.next();
            System.out.println(a.sum());
            System.out.println(a.mean());
            System.out.println(a.countNonZero());
            System.out.println(a.count());
            IntegerArray b = a.distinct();
            System.out.println(b.sum());
            IntegerArray c = a.increment(5);
            System.out.println(c.sum());
            if ( a.sum() > 100 )
                ResizableArray.copyAll(a, a);
            else
                ResizableArray.copyAll(a, b);
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.contains(jin.nextInt()));
            System.out.println(a.contains(jin.nextInt()));
        }
        if ( test == 3 ) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for ( int w = 0 ; w < 500 ; ++w ) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k =  2000;
                int t =  1000;
                for ( int i = 0 ; i < k ; ++i ) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for ( int i = 0 ; i < t ; ++i ) {
                    a.removeElement(k-i-1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
    }

}
