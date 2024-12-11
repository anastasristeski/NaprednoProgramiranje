package Midterm1.RaceTest;
import java.io.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
class Racer{
    String id;
    LocalTime start;
    LocalTime end;
    Racer(String line){
        String [] parts = line.split("\\s+");
        this.id = parts[0];
        this.start = LocalTime.parse(parts[1]);
        this.end = LocalTime.parse(parts[2]);
    }

    int getTime(){
        return (int) ChronoUnit.SECONDS.between(start,end);
    }
    LocalTime getTime2(){
        return LocalTime.MIDNIGHT.plusSeconds(getTime());
    }
    @Override
    public String toString() {
        return String.format("%s %s" ,id,getTime2());
    }

}
class TeamRace{
    static ArrayList<Racer> racers;
    TeamRace(){
        racers = new ArrayList<>();
    }
    public static void findBestTeam(InputStream is, OutputStream os){
        BufferedReader bf = new BufferedReader(new InputStreamReader(is));
      racers = bf.lines()
                .map(Racer::new)
              .sorted(Comparator.comparing(Racer::getTime))
              .limit(4)
              .collect(Collectors.toCollection(ArrayList::new));
      PrintWriter pw = new PrintWriter(os);
      for(Racer r:racers){
          pw.println(r);
      }

      pw.flush();
    }
}
public class RaceTest {
    public static void main(String[] args) {
        TeamRace.findBestTeam(System.in, System.out);
    }
}