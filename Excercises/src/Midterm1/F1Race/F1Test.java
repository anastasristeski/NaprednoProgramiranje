package Midterm1.F1Race;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}
class F1Race{
    ArrayList<Racer> racers;

    public F1Race() {
        this.racers = new ArrayList<>();
    }
    public void readResults(InputStream inputStream){
        Scanner sc = new Scanner(inputStream);
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            racers.add(new Racer(line));
        }
    }
    public void printSorted(OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);
        int i=1;
        racers.sort(Comparator.comparing(Racer::getBestLap));
        for(Racer r : racers){
            pw.println(String.format("%d. %s",i++,r.toString()));
        }
        pw.flush();
    }
}
class Racer{
    String name;
    ArrayList<Lap> laps;
    Lap bestLap;
    public Racer(String line) {
      laps = new ArrayList<>();
      String [] parts = line.split("\\s+");
      name = parts[0];
      for(int i=1;i<parts.length;i++){
          laps.add(new Lap(parts[i]));
      }
      bestLap=getBestLap();
    }
    public  Lap getBestLap(){
        return laps.stream().min(Lap::compareTo).orElse(null);
    }
    public String bestLap(){
        return Objects.requireNonNull(laps.stream().min(Lap::compareTo).orElse(null)).toString();
    }
    @Override
    public String toString() {
        return String.format("%-10s%10s",name,bestLap());
    }
}
class Lap implements Comparable<Lap>{
    int mm;
    int ss;
    int nnn;

    public Lap(String line) {
        String [] parts = line.split(":");
        mm = Integer.parseInt(parts[0]);
        ss = Integer.parseInt(parts[1]);
        nnn = Integer.parseInt(parts[2]);
    }
    @Override
    public String toString() {
        return String.format("%d:%02d:%03d",mm,ss,nnn);
    }
    @Override
    public int compareTo(Lap o) {
        if(this.mm!=o.mm){
            return Integer.compare(this.mm,o.mm);
        }
        if(this.ss!=o.ss){
            return Integer.compare(this.ss,o.ss);
        }
        return Integer.compare(this.nnn,o.nnn);
    }
}
