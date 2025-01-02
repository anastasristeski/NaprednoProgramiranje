package Midterm2.PayrollSystem;

import com.sun.source.tree.Tree;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class Employee implements Comparable<Employee>{
    String type;
    String id;
    String level;

    public Employee(String type, String id, String level) {
        this.type = type;
        this.id = id;
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getLevel() {
        return level;
    }
    public double getTotalHours(){
        return 0;
    }
    public void setSalary(double rate){

    }
    public double getSalary(){
        return 0.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return type.equals(employee.type) && id.equals(employee.id) && level.equals(employee.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id, level);
    }

    @Override
    public int compareTo(Employee o) {
        return Comparator.comparing(Employee::getSalary)
                .reversed()
                .thenComparing(Employee::getLevel)
                .compare(this,o);    }
}
class HourlyEmployee extends Employee{
    double hours;
    double salary=0.0;

    public HourlyEmployee(String type, String id, String level,double hours) {
        super(type,id,level);
        this.hours = hours;
    }
    @Override
    public void setSalary(double rate) {
        if(getTotalHours()>40){
            salary = 40*rate + (getTotalHours()-40)*rate*1.5;
        }else{
            salary = getTotalHours()*rate;
        }
    }
    @Override
    public double getSalary() {
        return salary;
    }
    @Override
    public double getTotalHours(){
        return hours;
    }
    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Regular hours: %.2f Overtime hours: %.2f"
                ,id,level,salary, Math.min(getTotalHours(), 40),getTotalHours()<40? 0:getTotalHours()-40);
    }

}
class FreelanceEmployee extends Employee{
    List<Integer>ticketPoints;
    double salary =0.0;
    public FreelanceEmployee(String type, String id, String level,List<Integer>ticketPoints) {
        super(type, id, level);
        this.ticketPoints=ticketPoints;
    }

    public int getTotalTicketPoints(){
        return ticketPoints.stream().mapToInt(Integer::intValue).sum();
    }
    @Override
    public double getSalary(){
        return salary;
    }
    @Override
    public void setSalary(double rate) {
       salary = getTotalTicketPoints()*rate;
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Tickets count: %d Tickets points: %d"
                ,id,level,salary,ticketPoints.size(),getTotalTicketPoints());
    }

}
class PayrollSystem{
    Map<String,Double> hourlyRateByLevel;
    Map<String,Double> ticketRateByLevel;
    Set<Employee>employees;

    public PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
        employees = new TreeSet<>();
    }
    void readEmployees(InputStream inputStream){
        Scanner sc = new Scanner(inputStream);
        while(sc.hasNextLine()){
            String [] parts = sc.nextLine().split(";");
            String type = parts[0];
            String id = parts[1];
            String level = parts[2];
            if(type.equals("H")){
                double hours = Double.parseDouble(parts[3]);
                Employee employee = new HourlyEmployee(type,id,level,hours);
                double rate = hourlyRateByLevel.get(employee.getLevel());
                employee.setSalary(rate);
                employees.add(employee);
            }else{
                ArrayList<Integer> pointsForType = Arrays.stream(parts)
                        .skip(3)
                        .map(Integer::valueOf)
                        .collect(Collectors.toCollection(ArrayList::new));
                Employee employee = new FreelanceEmployee(type,id,level,pointsForType);
                double rate = ticketRateByLevel.get(employee.getLevel());
                employee.setSalary(rate);
                employees.add(employee);
            }

        }
    }
    Map<String, Set<Employee>> printEmployeesByLevels (OutputStream os, Set<String> levels){

        return employees.
                stream()
                .filter(x->levels.contains(x.getLevel()))
                .collect(Collectors.groupingBy(Employee::getLevel,LinkedHashMap::new,Collectors.toCollection(TreeSet::new)));
    }


}
public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i=5;i<=10;i++) {
            levels.add("level"+i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: "+ level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
            System.out.println("------------");
        });


    }
}