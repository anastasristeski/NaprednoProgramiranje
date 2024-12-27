package Midterm1.Quiz;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

class InvalidOperationException extends Exception{
    public InvalidOperationException(String answer) {
        super(String.format("%s is not allowed option for this question",answer));
    }

    public InvalidOperationException() {
        super("Answers and questions must be of same length!");
    }
}
abstract class Question{
    String type;
    String text;
    int points;
    public Question(String type, String text, int points) {
        this.type = type;
        this.text = text;
        this.points = points;
    }
    public String getType() {
        return type;
    }
    public String getText() {
        return text;
    }
    public int getPoints() {
        return points;
    }
    abstract double answerQuestion(String ans);
}
class MCQuestion extends Question{
    String answer;

    public MCQuestion(String type, String text, int points,String answer) throws InvalidOperationException {
        super(type, text, points);
        if(!isQuestionValid(answer)){
            throw new InvalidOperationException(answer);
        }
        this.answer = answer;
    }
    public boolean isQuestionValid(String answer){
        List<String> validAnswers = Arrays.asList("A","B","C","D","E");
        return validAnswers.contains(answer);
    }

    @Override
    public String toString() {
        return String.format("Multiple Choice Question: %s Points %d Answer: %s",text,points,answer);
    }
    @Override
    public double answerQuestion(String ans){
        if(answer.equals(ans)){
            return points*1.0;
        }
        return -(points*0.2);
    }
}
class TFQuestion extends Question{
    String answer;

    public TFQuestion(String type, String text, int points,String answer)  {
        super(type, text, points);
        this.answer = answer;
    }
    @Override
    public String toString() {
        return String.format("True/False Question: %s Points: %d Answer: %s",text,points,answer);
    }
    @Override
    public double answerQuestion(String ans){
        if(answer.equals(ans)){
            return points*1.0;
        }
        return 0.0;
    }
}
class Quiz{
    List<Question> questions;

    public Quiz() {
        this.questions = new ArrayList<>();
    }
    public void addQuestion(String questionData) throws InvalidOperationException {
        String[]parts = questionData.split(";");
        String type = parts[0];
        String text = parts[1];
        int points = Integer.parseInt(parts[2]);
        String answer = parts[3];
        if(type.equals("TF")) {
            questions.add(new TFQuestion(type,text,points,answer));
        }
        else{
            questions.add(new MCQuestion(type,text,points,answer));
        }
    }
    public void printQuiz(OutputStream os){
        PrintWriter pw = new PrintWriter(os);
        List<Question>sortedQuestions = questions
                .stream()
                .sorted(Comparator.comparing(Question::getPoints)
                        .reversed())
                .collect(Collectors.toList());
        for (Question q:sortedQuestions)
            pw.println(q);
        pw.flush();
    }
    public void answerQuiz(List<String> answers,OutputStream os) throws InvalidOperationException {
        if(questions.size()!=answers.size()){
            throw new InvalidOperationException();
        }
            double totalPoints= 0.0;
            double currQuestionPoints =0.0;
            PrintWriter pw = new PrintWriter(os);
            for(int i=0;i<questions.size();i++){
                currQuestionPoints=questions.get(i).answerQuestion(answers.get(i));
                pw.printf("%d. %.2f\n",i+1,currQuestionPoints);
                totalPoints+=currQuestionPoints;
            }
            pw.printf("Total points: %.2f",totalPoints);
            pw.flush();
    }
}
public class QuizTest {
    public static void main(String[] args) throws InvalidOperationException {

        Scanner sc = new Scanner(System.in);

        Quiz quiz = new Quiz();

        int questions = Integer.parseInt(sc.nextLine());

        for (int i=0;i<questions;i++) {
           try{
                quiz.addQuestion(sc.nextLine());
            }catch(InvalidOperationException e){
               System.out.println(e.getMessage());
           }
        }

        List<String> answers = new ArrayList<>();

        int answersCount =  Integer.parseInt(sc.nextLine());

        for (int i=0;i<answersCount;i++) {
            answers.add(sc.nextLine());
        }

        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase==1) {
            quiz.printQuiz(System.out);
        } else if (testCase==2) {
            try{
                quiz.answerQuiz(answers, System.out);
            }catch(InvalidOperationException e){
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}
