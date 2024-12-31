package Midterm2.PhoneBook;

import java.util.*;
import java.util.stream.Collectors;

class DuplicateNumberException extends Exception {
    public DuplicateNumberException(String message) {
        super(message);
    }
}

class Contact implements Comparable<Contact> {
    String name;
    String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public int compareTo(Contact o) {
        int res = this.name.compareTo(o.name);

        if (res==0){
            res = this.number.compareTo(o.number);
        }

        return res;


//        return Comparator.comparing(Contact::getName)
//                .thenComparing(Contact::getNumber)
//                .compare(this, o);
    }

    @Override
    public String toString() {
        return name + " " + number;
    }
}


class PhoneBook {

    Set<String> allNumbers = new HashSet<>();

    Map<String, TreeSet<Contact>> contacts = new HashMap<>();

    public void addContact(String name, String number) throws DuplicateNumberException {
        if (allNumbers.contains(number)){
            throw new DuplicateNumberException(String.format("Duplicate number: %s", number));
        }

        contacts.putIfAbsent(name, new TreeSet<>());

        contacts.get(name).add(new Contact(name, number));
        allNumbers.add(number);
    }
    public void contactsByNumber(String number){
           List<Contact> contactsByNum = contacts.values()
                    .stream()
                    .flatMap(Set::stream)
                    .filter(contact->contact.getNumber().contains(number))
                    .sorted()
                   .collect(Collectors.toList());
           if(contactsByNum.isEmpty())
               System.out.println("NOT FOUND");
           else contactsByNum.forEach(System.out::println);
    }
    public void contactsByName(String name) {
        if (!contacts.containsKey(name)){
            System.out.println("NOT FOUND");
            return ;
        }
        contacts.get(name).forEach(System.out::println);
    }
}

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}

// Вашиот код овде

