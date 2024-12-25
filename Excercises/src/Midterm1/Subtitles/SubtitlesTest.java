package Midterm1.Subtitles;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Subtitle{
    int number;
    LocalTime start;
    LocalTime end;
    String sub;
    public Subtitle(int number, LocalTime start, LocalTime end,String sub) {
        this.number = number;
        this.start = start;
        this.end = end;
        this.sub="";
    }
    public Subtitle(String text){
        String [] parts = text.split("\n");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
        StringBuilder subtitleText = new StringBuilder();
        number = Integer.parseInt(parts[0]);
        String[] times = parts[1].split(" --> ");
        start = LocalTime.parse(times[0],formatter);
        end = LocalTime.parse(times[1],formatter);
        for(int i = 2;i<parts.length;i++){
           subtitleText.append(parts[i]).append("\n");
        }
        sub = subtitleText.toString();
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
        String startTime = start.format(formatter);
        String endTime = end.format(formatter);
        return String.format("%d\n%s --> %s\n%s\n",number,startTime,endTime,sub);
    }
    public void shiftTimes(int ms){
        start = start.plus(ms,ChronoUnit.MILLIS);
        end = end.plus(ms,ChronoUnit.MILLIS);
    }
}
class Subtitles{
    ArrayList<Subtitle> subtitles;
    public Subtitles() {
        this.subtitles = new ArrayList<>();
    }
    public void print(){
        for (Subtitle subtitle : subtitles) {
            System.out.print(subtitle);
        }
    }
    public void shift(int shift){
       for(Subtitle sb : subtitles){
           sb.shiftTimes(shift);
       }
    }
    public int loadSubtitles(InputStream inputStream){
        int count=0;
        Scanner sc = new Scanner(inputStream);
        StringBuilder sb = new StringBuilder();
        String line;
        while(sc.hasNextLine()){
            line = sc.nextLine().trim();
            if(line.isEmpty()){
                if(sb.length()>0){
                    subtitles.add(new Subtitle(sb.toString()));
                    count++;
                    sb.setLength(0);
                }
            }
            else{
                sb.append(line).append("\n");
            }
        }
            if(sb.length()>0){
               subtitles.add(new Subtitle(sb.toString()));
               count++;
        }
        return count;
    }
}
public class SubtitlesTest {
    public static void main(String[] args) {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}

// Вашиот код овде
