package LABS.LAB2.LAB2_3;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

enum Operator{
    VIP,ONE,TMOBILE
}
abstract class Contact{
    String date;
    public Contact(String date){
        this.date = date;
    }
    public  boolean isNewerThan(Contact c){
        LocalDate dt1 = LocalDate.parse(date);
        LocalDate dt2 = LocalDate.parse(c.date);
        return dt1.isAfter(dt2);
    }
    public String getDate(){
        return date;
    }
    abstract String getType();

    @Override
    abstract public String toString();
}
class EmailContact extends Contact{
    String email;
    public EmailContact(String date,String email) {
        super(date);
        this.email = email;
    }
    String getEmail(){
        return email;
    }

    @Override
    String getType() {
        return "Email";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailContact that = (EmailContact) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return String.format("\"%s\"",email);
    }
}
class PhoneContact extends Contact{
    String phoneNumber;
    Operator operator;
    public PhoneContact(String date,String phoneNumber) {
        super(date);
        this.phoneNumber = phoneNumber;
        this.operator = getOperator();
    }
    public Operator getOperator(){
        String [] parts = phoneNumber.split("/");
        char prefix = parts[0].charAt(2);
        if(prefix == '0'||prefix == '1'||prefix == '2')
            return Operator.TMOBILE;
        if(prefix == '5'||prefix == '6')
            return Operator.ONE;
        else
            return Operator.VIP;
    }
    @Override
    String getType() {
        return "Phone";
    }
    public String getPhone(){
        return phoneNumber;
    }
    @Override
    public String toString() {
        return String.format("\"%s\"",phoneNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneContact that = (PhoneContact) o;
        return Objects.equals(phoneNumber, that.phoneNumber) && operator == that.operator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumber, operator);
    }
}
class Student{
    Contact [] contacts;
    String firstName;
    String lastName;
    String city;
    int age;
    long index;

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        contacts = new Contact[0];
    }
    public void addEmailContact(String date,String email){
        ArrayList<Contact>contactsToArrayList = new ArrayList<>(Arrays.asList(contacts));
        contactsToArrayList.add(new EmailContact(date,email));
        contacts = contactsToArrayList.toArray(contacts);
    }
    public void addPhoneContact(String date, String phone){
        ArrayList<Contact> contactsListToArrayList = new ArrayList<>(Arrays.asList(contacts));
        contactsListToArrayList.add(new PhoneContact(date,phone));
        contacts = contactsListToArrayList.toArray(contacts);
    }
    Contact [] getEmailContacts(){
        return Arrays.stream(contacts)
                .filter(x->x.getType().equals("Email"))
                .toArray(Contact[]::new);
    }
    Contact[] getPhoneContacts(){
        return Arrays.stream(contacts)
                .filter(x->x.getType().equals("Phone"))
                .toArray(Contact[]::new);
    }
    public int getNumberOfContacts(){
        return contacts.length;
    }
    public String getFullName() {
        return String.format("%s %s",firstName,lastName);
    }

    public String getCity() {
        return city;
    }

    public long getIndex() {
        return index;
    }
    public Contact getLatestContact(){
        return Arrays.stream(contacts)
                .max(Comparator.comparing(Contact::getDate))
                .orElse(null);
    }

    @Override
    public String toString() {
        String emailContacts = Arrays.stream(getEmailContacts())
                .map(Contact::toString)
                .collect(Collectors.joining(", ", "[", "]"));
        String phoneContacts = Arrays.stream(getPhoneContacts())
                .map(Contact::toString)
                .collect(Collectors.joining(", ", "[", "]"));
        return String.format("{\"ime\":\"%s\", \"prezime\":\"%s\"," +
                " \"vozrast\":%d," +
                " \"grad\":\"%s\"," +
                " \"indeks\":%d," +
                " \"telefonskiKontakti\":%s," +
                " \"emailKontakti\":%s}",firstName,lastName,age,city,index,phoneContacts,emailContacts);
    }

}
class Faculty{
    String name;
    Student []students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = students;
    }
    public int countStudentsFromCity(String cityName){
        return (int) Arrays.stream(students).filter(s->s.getCity().equals(cityName)).count();
    }
    public Student getStudent(long index){
        return Arrays.stream(students).filter(x->x.getIndex()==index).findFirst().orElse(null);
    }
    public double getAverageNumberOfContacts(){
        return Arrays.stream(students)
                .mapToInt(Student::getNumberOfContacts)
                .average()
                .orElse(0.0);
    }
    public Student getStudentWithMostContacts(){
        return Arrays.stream(students).max(Comparator.comparing(Student::getNumberOfContacts)).orElse(null);
    }
    public String getStudentsInStringFormat(){
        return Arrays.toString(students);
    }
    @Override
    public String toString() {
        return String.format("{\"fakultet\":\"%s\", \"studenti\":%s}",name,getStudentsInStringFormat());
    }
}
public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;



                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
