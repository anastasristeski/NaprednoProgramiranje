package Midterm2.Course;

//package mk.ukim.finki.midterm;

import java.util.*;
import java.util.stream.Collectors;
class InvalidPointsException extends Exception{

}
class Student{
    String id;
    String name;
    int mid1;
    int mid2;
    int lab;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        mid1=0;
        mid2=0;
        lab=0;
    }
    public void update(String activity,int points) throws InvalidPointsException {
        if(!checkValidPoints(activity,points))
            throw new InvalidPointsException();
        switch(activity){
            case "midterm1":mid1=points; break;
            case "midterm2":mid2=points; break ;
            case "labs":lab=points; break ;
        }
    }
    public double summaryPoints(){
        return (mid1*0.45)+(mid2 *0.45) + lab;
    }
    public int getGrade(){
        double points = summaryPoints();
        if(points<50){
            return 5;
        }else if(points<60){
            return 6;
        } else if (points<70) {
            return 7;
        } else if (points<80) {
            return 8;
        } else if (points<90) {
            return 9;
        }else return 10;

    }
    public boolean checkValidPoints(String activity,int points){
        if((activity.equals("midterm1")||activity.equals("midterm2"))&&points>100)
            return false;
        return !activity.equals("labs") || points <= 10;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s First midterm: %d Second midterm %d Labs: %d Summary points: %.2f Grade: %d",id,name,mid1,mid2,lab,summaryPoints(),getGrade());
    }
   // ID: 151020 Name: Stefan First midterm: 78 Second midterm 80 Labs: 8 Summary points: 79.10 Grade: 8

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id.equals(student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
class AdvancedProgrammingCourse{
    Map<String,Student> students;

    public AdvancedProgrammingCourse() {
        this.students = new HashMap<>();
    }
    public void addStudent(Student s){
        students.put(s.getId(),s);
    }
    public void updateStudent(String idNumber,String activity,int points) throws InvalidPointsException {
        students.get(idNumber).update(activity,points);
    }
    public List<Student>getFirstNStudents(int n){
        return students.values().stream()
                .sorted(Comparator.comparing(Student::summaryPoints).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }
    public Map<Integer, Integer> getGradeDistribution(){
        Map<Integer,Integer> result = students.values()
                .stream()
                .collect(Collectors.groupingBy(Student::getGrade,
                        TreeMap::new,
                        Collectors.collectingAndThen(Collectors.counting(),Long::intValue)));
        for(int i=5;i<11;i++){
            result.putIfAbsent(i,0);
        }
        return result;
    }
    public void printStatistics(){
        DoubleSummaryStatistics stats = students.values()
                .stream()
                .filter(x->x.getGrade()>5)
                .mapToDouble(Student::summaryPoints)
                .summaryStatistics();
        System.out.printf("Count: %d Min: %.2f Average: %.2f Max: %.2f",stats.getCount(),stats.getMin(),stats.getAverage(),stats.getMax());
        //Count: 1 Min: 79.10 Average: 79.10 Max: 79.10
    }
}
public class CourseTest {

    public static void printStudents(List<Student> students) {
        students.forEach(System.out::println);
    }

    public static void printMap(Map<Integer, Integer> map) {
        map.forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
    }

    public static void main(String[] args) throws InvalidPointsException {
        AdvancedProgrammingCourse advancedProgrammingCourse = new AdvancedProgrammingCourse();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String command = parts[0];

            if (command.equals("addStudent")) {
                String id = parts[1];
                String name = parts[2];
                advancedProgrammingCourse.addStudent(new Student(id, name));
            } else if (command.equals("updateStudent")) {
                String idNumber = parts[1];
                String activity = parts[2];
                int points = Integer.parseInt(parts[3]);
                advancedProgrammingCourse.updateStudent(idNumber, activity, points);
            } else if (command.equals("getFirstNStudents")) {
                int n = Integer.parseInt(parts[1]);
                printStudents(advancedProgrammingCourse.getFirstNStudents(n));
            } else if (command.equals("getGradeDistribution")) {
                printMap(advancedProgrammingCourse.getGradeDistribution());
            } else {
                advancedProgrammingCourse.printStatistics();
            }
        }
    }
}
