package Midterm1.TImeTable;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
class UnsupportedFormatException extends Exception{
    public UnsupportedFormatException(String message) {
        super(String.format("%s",message));
    }
}
class InvalidTimeException extends Exception{
    public InvalidTimeException(String message) {
        super(String.format("%s",message));
    }
}

public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}
enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}
class TimeTable{
    ArrayList<Time> times;
    public TimeTable(){
        times = new ArrayList<>();
    }
    public boolean checkValidTime(String s){
        int hours = Integer.parseInt(s.split("[.:]")[0]);
        int minutes  = Integer.parseInt(s.split("[.:]")[1]);
        return hours >=0 && hours<24 && minutes >=0 && minutes <60;
    }
    public  void readTimes(InputStream inputStream) throws UnsupportedFormatException, InvalidTimeException {
        Scanner sc = new Scanner(inputStream);
        while(sc.hasNextLine()){
            String [] parts = sc.nextLine().split("\\s+");
            for(int i=0;i<parts.length;i++){
                if(!parts[i].contains(".")&&!parts[i].contains(":")){
                    throw new UnsupportedFormatException(parts[i]);
                }
                if(!checkValidTime(parts[i])){
                    throw new InvalidTimeException(parts[i]);
                }
                else
                    times.add(new Time(parts[i]));
            }
        }
    }
    public void writeTimes(OutputStream outputStream,TimeFormat format){
        PrintWriter pw = new PrintWriter(outputStream);
        times.sort(Comparator.comparing(Time::getTimeToMins));

        if(format.equals(TimeFormat.FORMAT_AMPM)){
            times.forEach(x-> pw.println(x.convertToAmPm()));
        }
        else
            times.forEach(pw::println);
        pw.flush();
    }
}
class Time implements Comparable<Time>{
    int hours;
    int minutes;
    public Time(String s){
        this.hours = Integer.parseInt(s.split("[.:]")[0]);
        this.minutes = Integer.parseInt(s.split("[.:]")[1]);
    }
    public int getTimeToMins(){
        return hours * 60 + minutes;
    }

    @Override
    public int compareTo(Time o) {
        return Integer.compare(this.getTimeToMins(),o.getTimeToMins());
    }
    public String convertToAmPm(){
        String s = "";
        int h = hours;
        if(hours==0) {
            h = hours + 12;
            s = "AM";
        }
        if(hours>0&&hours<12){
            s ="AM";
        }
        if(hours==12){
            s = "PM";
        }
        if(hours>12&&hours<24){
            h = hours-12;
            s = "PM";
        }
        hours = h;
        return String.format("%s %s", this,s);
    }
    @Override
    public String toString() {
        return String.format("%2d:%02d",hours,minutes);
    }
}