package Midterm2.QuizProcessor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.*;
import java.util.stream.Collectors;
class WrongNumberOfInputsException extends Exception{
    public WrongNumberOfInputsException() {
        super("A quiz must have same number of correct and selected answers");
    }
}
class Student{
    String id;
    List<String>correct;
    List<String>actual;


    public Student(String id, List<String> correct, List<String> actual) throws WrongNumberOfInputsException {
        if(correct.size()!=actual.size()){
            throw new WrongNumberOfInputsException();
        }
        this.id = id;
        this.correct = correct;
        this.actual = actual;

    }

    public String getId() {
        return id;
    }
    public  double totalPoints(){
        int countCorrect=0;
        int countIncorrect=0;
        for(int i=0;i<correct.size();i++){
            if(correct.get(i).equals(actual.get(i))){
                countCorrect++;
            }else{
                countIncorrect++;
            }
        }
        return (double)(countCorrect)-(countIncorrect*0.25);
    }

}
class QuizProcessor{
    static List<Student>students;

    public QuizProcessor() {
        students = new ArrayList<>();
    }

    public static Map<String, Double> processAnswers(InputStream is)  {
        Map<String,Double> result= new TreeMap<>();
        Scanner sc = new Scanner(is);
        String id;

        while(sc.hasNextLine()){
            List<String> correct= new ArrayList<>();
            List<String> actual = new ArrayList<>();
            String line = sc.nextLine();
            String [] parts = line.split(";");
            id = parts[0];
            String[]correctParts = parts[1].split(",");
            String[]actualParts = parts[2].split(",");
            correct.addAll(Arrays.asList(correctParts));
            actual.addAll(Arrays.asList(actualParts));
            try{
                Student s = new Student(id, correct, actual);
                result.put(s.getId(),s.totalPoints());
            }catch(WrongNumberOfInputsException e){
                System.out.println(e.getMessage());
            }

        }
        return result;
    }
}
public class QuizProcessorTest {
    public static void main(String[] args) throws WrongNumberOfInputsException {
            QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));

    }
}