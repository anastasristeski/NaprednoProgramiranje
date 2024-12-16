package Midterm1.Archive;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
class NonExistingItemException extends Exception{
    public NonExistingItemException(int id) {
        super(String.format("Item with id %d doesn't exist.",id));
    }
}
abstract class Archive{
    int id;
    LocalDate dateArchived;

    public Archive(int id ) {
        this.id = id;
        this.dateArchived = LocalDate.now();
    }

    public void setDateArchived(LocalDate dateArchived) {
        this.dateArchived = dateArchived;
    }
   abstract public String open(LocalDate date);

}
class LockedArchive extends Archive{
    LocalDate dateToOpen;
    public LockedArchive(int id, LocalDate dateToOpen) {
        super(id);
        this.dateToOpen = dateToOpen;
    }

    @Override
    public String open(LocalDate date) {
        if(date.isBefore(dateToOpen)){
            return String.format("Item %d cannot be opened before %s\n",id,dateToOpen);
        }
        return String.format("Item %d opened at %s\n",id,date);
    }
}
class SpecialArchive extends Archive{
    int maxOpen;
    int timesOpened;
    public SpecialArchive(int id,int maxOpen) {
        super(id);
        this.maxOpen = maxOpen;
        this.timesOpened = 0;
    }


    @Override
    public String open(LocalDate date){
        if(timesOpened == maxOpen){
            return String.format("Item %d cannot be opened more than %d times.\n",id,maxOpen);
        }
        timesOpened++;
        return String.format("Item %d opened at %s\n",id,date);
    }
}
class ArchiveStore{
    ArrayList<Archive> list;
    StringBuilder log;

    public ArchiveStore() {
        this.list = new ArrayList<>();
        log = new StringBuilder();
    }
    public void archiveItem(Archive item,LocalDate date){
        item.setDateArchived(date);
        list.add(item);
        log.append("Item ").append(item.id).append(" archived at ").append(date).append("\n");
    }
    public void openItem(int id,LocalDate date) throws NonExistingItemException {
        Optional<Archive> optional = getItemWithID(id);
        if(optional.isEmpty()){
            throw new NonExistingItemException(id);
        }
        else{
            log.append(optional.get().open(date));
        }
    }
    public String getLog(){
        return log.toString();
    }
    public Optional<Archive> getItemWithID(int id){
        Optional<Archive> optional = list.stream()
                .filter(x->x.id==id)
                .findAny();
        return optional;
    }
}
public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}