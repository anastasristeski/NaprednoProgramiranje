package FilterAndSort;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
class EmptyResultException extends Exception{
    public EmptyResultException(String message) {
        super(message);
    }
}
class FilterAndSort{
public static <T extends Comparable<T>> List<T> execute(List<T> list, Predicate<T> predicate) throws EmptyResultException {
    List<T> result = new ArrayList<>();
    for(T item : list){
        if(predicate.test(item)){
            result.add(item);
        }
    }
    if(result.isEmpty()){
        throw new EmptyResultException("No element met the criteria");
    }
    result.sort(Comparator.reverseOrder());
    return result;
}
}

class Student implements Comparable<Student> {
    String id;
    List<Integer> grades;

    public Student(String id, List<Integer> grades) {
        this.id = id;
        this.grades = grades;
    }

    public double average() {
        return grades.stream().mapToDouble(i -> i).average().getAsDouble();
    }

    public int getYear() {
        return (24 - Integer.parseInt(id.substring(0, 2)));
    }

    public int totalCourses() {
        return Math.min(getYear() * 10, 40);
    }

    public double labAssistantPoints() {
        return average() * ((double) grades.size() / totalCourses()) * (0.8 + ((getYear()-1)*0.2)/3.0);
    }
    public boolean studentPassedMoreThan90Percent(){
        return (double)grades.size()/totalCourses()>0.9;
    }

    @Override
    public int compareTo(Student o) {
        return Comparator.comparing(Student::labAssistantPoints)
                .thenComparing(Student::average)
                .compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("Student %s (%d year) - %d/%d passed exam, average grade %.2f.\nLab assistant points: %.2f", id, getYear(), grades.size(), totalCourses(), average(), labAssistantPoints());
    }
}


public class FilterAndSortTest {
    public static void main(String[] args) throws EmptyResultException {
        Scanner sc = new Scanner(System.in);
        int testCase = Integer.parseInt(sc.nextLine());
        int n = Integer.parseInt(sc.nextLine());

        if (testCase == 1) { // students
            int studentScenario = Integer.parseInt(sc.nextLine());
            List<Student> students = new ArrayList<>();
            while (n > 0) {

                String line = sc.nextLine();
                String[] parts = line.split("\\s+");
                String id = parts[0];
                List<Integer> grades = Arrays.stream(parts).skip(1).map(Integer::parseInt).collect(Collectors.toList());
                students.add(new Student(id, grades));
                --n;
            }

            if (studentScenario == 1) {
                    Predicate<Student> predicate = x->x.labAssistantPoints()>=8.0 & x.getYear()>=3;
                    List<Student>test;
               try {
                    test = FilterAndSort.execute(students, predicate);
                    for(Student s : test){
                        System.out.println(s);
                    }
                }catch(EmptyResultException e){
                   System.out.println( e.getMessage());
               }
                //TODO filter and sort all students who have at least 8.0 points and are at least 3rd year student

            } else {
                Predicate<Student> predicate = x->x.studentPassedMoreThan90Percent() && x.average()>=9.0;
                List<Student> test2;
              try  {
                    test2 = FilterAndSort.execute(students, predicate);
                    for(Student s:test2){
                        System.out.println(s);
                    }
                }catch(EmptyResultException e){
                  System.out.println(e.getMessage());
              }
                //TODO filter and sort all students who have passed at least 90% of their total courses with an average grade of at least 9.0

            }
        } else { //integers
            List<Integer> integers = new ArrayList<>();
            while (n > 0) {
                integers.add(Integer.parseInt(sc.nextLine()));
                --n;
            }
            Predicate<Integer> predicate = x->x%2==0 && x%15==0;
            List<Integer>test3;
            try{
                test3 = FilterAndSort.execute(integers, predicate);
                for(Integer i : test3){
                    System.out.println(i);
                }
            }catch(EmptyResultException e){
                System.out.println(e.getMessage());
            }
            //TODO filter and sort all even numbers divisible with 15

        }

    }
}
