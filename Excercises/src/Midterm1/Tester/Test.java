package Midterm1.Tester;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

public class Test {
    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
        LocalTime start = LocalTime.parse("00:01:53,468",formatter);
        String formatted =  start.format(formatter);
        System.out.println(formatted);
    }
}
