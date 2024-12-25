package Midterm1.WeatherStation;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

class Measurment{
    double temperature;
    double wind;
    double humidity;
    double visibility;
    Date date;

    public Measurment(double temperature, double wind, double humidity, double visibility, Date date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }


    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("E MMM d HH:mm:ss 'GMT' yyyy");
        String formattedDate = formatter.format(this.date);
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s",temperature,wind,humidity,visibility,formattedDate);
        //24.6 80.2 km/h 28.7% 51.7 km Tue Dec 17 23:40:15 CET 2013
    }
    public Date getDate(){
        return date;
    }
    public double getTemperature(){
        return temperature;
    }
}
class WeatherStation{
    ArrayList<Measurment> measurments;
    int days;
    public WeatherStation(int x){
        this.days=x;
        measurments = new ArrayList<>();
    }
    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date){
        Measurment newMeasurment = new Measurment(temperature,wind,humidity,visibility,date);
        measurments.removeIf(m->{
           long differenceInDays = ChronoUnit.DAYS.between(m.getDate().toInstant(),newMeasurment.getDate().toInstant());
           return differenceInDays >= days;
        });
        for(Measurment m : measurments){
            long differenceInMillis = Math.abs(date.getTime()-m.getDate().getTime());
            if(differenceInMillis <= 150000){
                return ;
            }
        }
        measurments.add(newMeasurment);
    }
    public void status(Date from, Date to){
     List<Measurment> newList = measurments.stream()
             .filter(x -> !x.getDate().before(from) && !x.getDate().after(to))
             .sorted(Comparator.comparing(Measurment::getDate))
             .collect(Collectors.toList());
    if(newList.isEmpty()) {
        throw new RuntimeException();
    }
    Double averageTemp = newList.stream()
                .mapToDouble(Measurment::getTemperature)
                .average()
                .orElse(0.0);
        newList.forEach(System.out::println);
        System.out.printf("Average temperature: %.2f\n", averageTemp);

    }
    public int total(){
        return measurments.size();
    }
}
public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

// vashiot kod ovde