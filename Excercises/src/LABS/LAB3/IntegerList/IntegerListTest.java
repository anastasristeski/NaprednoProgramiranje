package LABS.LAB3.IntegerList;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

class IntegerList{
    ArrayList<Integer> list;
    IntegerList(){
        list = new ArrayList<>();
    }
    IntegerList(Integer... numbers){
        list = new ArrayList<>(Arrays.asList(numbers));
    }

    public IntegerList(ArrayList<Integer> newList) {
        this.list = newList;
    }

    public void add(int el,int idx){
        if(idx>list.size()){
            for(int i=list.size();i<=idx;i++){
                if(i==idx){
                    list.add(el);
                }
                else{
                    list.add(0);
                }
            }
        }
       else
          list.add(idx,el);
    }
    public int remove(int idx){
        if(!isIndexValid(idx)){
            throw new ArrayIndexOutOfBoundsException();
        }
        return list.remove(idx);
    }
    public void set(int el,int idx){
        if(!isIndexValid(idx)){
            throw new ArrayIndexOutOfBoundsException();
        }
        list.set(idx,el);
    }
    public int get(int idx){
        if(!isIndexValid(idx)){
            throw new ArrayIndexOutOfBoundsException();
        }
        return list.get(idx);
    }
    public int size(){
        return list.size();
    }
    public int count(int el){
        return (int) list.stream()
                .filter(x->x.equals(el))
                .count();
    }
    public void removeDuplicates(){ // won't work on the test case because of the order
       list = (ArrayList<Integer>) list.stream()
               .distinct()
               .collect(Collectors.toList());
    }
    public int sumFirst(int k){
        return list.stream()
                .limit(k)
                .mapToInt(Integer::intValue)
                .sum();
    }
    public int sumLast(int k){
        return list.stream()
                .skip(size()-k)
                .mapToInt(Integer::intValue)
                .sum();
    }
    public void shiftRight(int idx,int k){
        if(!isIndexValid(idx)){
            throw new ArrayIndexOutOfBoundsException();
        }
        int newIndex = (idx + k) % list.size();
        Integer element = list.remove(idx);
        list.add(newIndex,element);
    }
    public void shiftLeft(int idx,int k){
        if(!isIndexValid(idx)){
            throw new ArrayIndexOutOfBoundsException();
        }
        int newIndex = (idx)-k % list.size();
        if(newIndex<0) newIndex +=list.size();
        Integer element = list.remove(idx);
        list.add(newIndex,element);
    }
    public IntegerList addValue(int value){
        ArrayList<Integer> newList = new ArrayList<>();
        for(Integer i : list)
            newList.add(i+value);
        return new IntegerList(newList);

    }
    public boolean isIndexValid(int idx){
        return idx>0||idx<list.size();
    }

}

public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) { //test standard methods
            int subtest = jin.nextInt();
            if ( subtest == 0 ) {
                IntegerList list = new IntegerList();
                while ( true ) {
                    int num = jin.nextInt();
                    if ( num == 0 ) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if ( num == 1 ) {
                        list.remove(jin.nextInt());
                    }
                    if ( num == 2 ) {
                        print(list);
                    }
                    if ( num == 3 ) {
                        break;
                    }
                }
            }
            if ( subtest == 1 ) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for ( int i = 0 ; i < n ; ++i ) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if ( k == 1 ) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if ( num == 1 ) {
                    list.removeDuplicates();
                }
                if ( num == 2 ) {
                    print(list.addValue(jin.nextInt()));
                }
                if ( num == 3 ) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
        if ( k == 2 ) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if ( num == 1 ) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if ( num == 2 ) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if ( num == 3 ) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if ( il.size() == 0 ) System.out.print("EMPTY");
        for ( int i = 0 ; i < il.size() ; ++i ) {
            if ( i > 0 ) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}