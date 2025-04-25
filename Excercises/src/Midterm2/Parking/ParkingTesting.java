package Midterm2.Parking;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class DateUtil {
    public static long durationBetween(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toMinutes();
    }
}
class Car implements Comparable<Car>{
    String registration;
    String spot;
    LocalDateTime timestamp;

    public Car(String registration, String spot, LocalDateTime timestamp) {
        this.registration = registration;
        this.spot = spot;
        this.timestamp = timestamp;
    }

    public String getRegistration() {
        return registration;
    }

    public String getSpot() {
        return spot;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return registration.equals(car.registration); //&& spot.equals(car.spot) && timestamp.equals(car.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registration, spot, timestamp);
    }

    @Override
    public String toString() {
        return String.format("Registration number: %s Spot: %s Start timestamp: %s", registration,spot,timestamp.toString());

    }

    @Override
    public int compareTo(Car o) {
        return o.timestamp.compareTo(this.timestamp);
    }
}
class ParkingRecord{
    String registration;
    String spot;
    LocalDateTime enter;
    LocalDateTime exit;
    long duration;

    public ParkingRecord(String registration, String spot, LocalDateTime enter, LocalDateTime exit) {
        this.registration = registration;
        this.spot = spot;
        this.enter = enter;
        this.exit = exit;
        this.duration = DateUtil.durationBetween(enter,exit);
    }

    public long getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return String.format("Registration number: %s Spot: %s Start timestamp: %s End timestamp: %s Duration in minutes: %d"
                ,registration,spot,enter.toString(),exit.toString(),duration);
    }
}
class Parking{
    int capacity;
    Map<String,Car> parkedCars;
    List<ParkingRecord>parkingRecords;
    Map<String,Integer> countTimesParked;
    Map<String,Double> spotOccupation;
    public Parking(int capacity){
        this.capacity = capacity;
        parkedCars = new HashMap<>();
        parkingRecords = new ArrayList<>();
        countTimesParked = new TreeMap<>();
        spotOccupation = new TreeMap<>();
    }
    public void update(String registration, String spot, LocalDateTime timestamp, boolean entry){
        if(entry){
            parkedCars.put(registration, new Car(registration,spot,timestamp));
            countTimesParked.put(registration, countTimesParked.getOrDefault(registration,0)+1);
        }else{
            Car exitingCar = parkedCars.remove(registration);
            if(exitingCar == null){
                return;
            }
            ParkingRecord record = new ParkingRecord(registration,spot,exitingCar.getTimestamp(),timestamp);
            parkingRecords.add(record);
            spotOccupation.put(spot, spotOccupation.getOrDefault(spot, 0.0) + record.getDuration());
        }
    }
    public double occupacyPercentage(){
        return ((double)parkedCars.size()/capacity)*100.0;
    }
    public void currentState(){
        System.out.printf("Capacity filled: %.2f%%\n",occupacyPercentage());
        parkedCars.values().stream()
                .sorted(Comparator.comparing(Car::getTimestamp).reversed())
                .forEach(System.out::println);
    }
    public void history(){
        parkingRecords.stream()
                .sorted(Comparator.comparing(ParkingRecord::getDuration).reversed())
                .forEach(System.out::println);
    }
    public Map<String, Integer> carStatistics(){
        return countTimesParked;
    }
    Map<String,Double> spotOccupancy (LocalDateTime start, LocalDateTime end){
        long durationToCount = DateUtil.durationBetween(start,end);

        return spotOccupation.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry->(entry.getValue()/durationToCount)*100.0,
                        (e, x) -> e,
                        TreeMap::new
                ));
    }
}
public class ParkingTesting {

    public static <K, V extends Comparable<V>> void printMapSortedByValue(Map<K, V> map) {
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.println(String.format("%s -> %s", entry.getKey().toString(), entry.getValue().toString())));

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int capacity = Integer.parseInt(sc.nextLine());

        Parking parking = new Parking(capacity);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equals("update")) {
                String registration = parts[1];
                String spot = parts[2];
                LocalDateTime timestamp = LocalDateTime.parse(parts[3]);
                boolean entrance = Boolean.parseBoolean(parts[4]);
                parking.update(registration, spot, timestamp, entrance);
            } else if (parts[0].equals("currentState")) {
                System.out.println("PARKING CURRENT STATE");
                parking.currentState();
            } else if (parts[0].equals("history")) {
                System.out.println("PARKING HISTORY");
                parking.history();
            } else if (parts[0].equals("carStatistics")) {
                System.out.println("CAR STATISTICS");
                printMapSortedByValue(parking.carStatistics());
            } else if (parts[0].equals("spotOccupancy")) {
                LocalDateTime start = LocalDateTime.parse(parts[1]);
                LocalDateTime end = LocalDateTime.parse(parts[2]);
                printMapSortedByValue(parking.spotOccupancy(start, end));
            }
        }
    }
}
