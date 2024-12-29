package LABS.LAB5.Sets;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class DuplicateStudentException extends Exception{
    public DuplicateStudentException(String id) {
        super(String.format("Student with ID %s already exists",id));
    }
}
class Student{
    String id;
    List<Integer>grades;

    public Student(String id, List<Integer> grades) {
        this.id = id;
        this.grades = grades;
    }

    public String getId() {
        return id;
    }
    public void addGrade(int grade){
        grades.add(grade);
    }
    public double averageGrade(){
        return grades.stream().mapToDouble(Double::valueOf).average().orElse(0.0);
    }
    public int getPassedExams(){
        return grades.size();
    }

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

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", grades=" + grades +
                '}';
    }
}
class Faculty{
    Set<Student> students;
    Map<String,Student> studentsMap;
    public Faculty() {
        this.students = new HashSet<>();
        this.studentsMap = new HashMap<>();
    }
    public void addStudent(String id,List<Integer>grades) throws DuplicateStudentException {
        if(studentsMap.containsKey(id))
            throw new DuplicateStudentException(id);
        else {
            Student student = new Student(id,grades);
            students.add(student);
            studentsMap.put(student.getId(), student);
        }
    }
    public void addGrade(String id, int grade){
        studentsMap.get(id).addGrade(grade);
    }
    Set<Student> getStudentsSortedByAverageGrade(){
        Comparator<Student> comparatorByAverageGrade = Comparator.comparing(Student::averageGrade)
                .thenComparing(Student::getPassedExams)
                .thenComparing(Student::getId).reversed();
        return studentsMap.values().stream()
                .sorted(comparatorByAverageGrade)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
    Set<Student> getStudentsSortedByCoursesPassed(){
        Comparator<Student> comparatorByCoursesPassed = Comparator
                .comparing(Student::getPassedExams,Comparator.reverseOrder())
                .thenComparing(Student::averageGrade,Comparator.reverseOrder())
                .thenComparing(Student::getId,Comparator.reverseOrder());
        return studentsMap.values().stream()
                .sorted(comparatorByCoursesPassed)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

}
public class SetsTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Faculty faculty = new Faculty();

        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            String[] tokens = input.split("\\s+");
            String command = tokens[0];

            switch (command) {
                case "addStudent":
                    String id = tokens[1];
                    List<Integer> grades = new ArrayList<>();
                    for (int i = 2; i < tokens.length; i++) {
                        grades.add(Integer.parseInt(tokens[i]));
                    }
                    try {
                        faculty.addStudent(id, grades);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "addGrade":
                    String studentId = tokens[1];
                    int grade = Integer.parseInt(tokens[2]);
                    faculty.addGrade(studentId, grade);
                    break;

                case "getStudentsSortedByAverageGrade":
                    System.out.println("Sorting students by average grade");
                    Set<Student> sortedByAverage = faculty.getStudentsSortedByAverageGrade();
                    for (Student student : sortedByAverage) {
                        System.out.println(student);
                    }
                    break;

                case "getStudentsSortedByCoursesPassed":
                    System.out.println("Sorting students by courses passed");
                    Set<Student> sortedByCourses = faculty.getStudentsSortedByCoursesPassed();
                    for (Student student : sortedByCourses) {
                        System.out.println(student);
                    }
                    break;

                default:
                    break;
            }
        }

        scanner.close();
    }
}
