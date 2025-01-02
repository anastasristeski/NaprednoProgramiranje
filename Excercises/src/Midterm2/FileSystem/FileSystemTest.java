package Midterm2.FileSystem;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
class File implements Comparable<File> {
    String name;
    Integer size;
    LocalDateTime createdAt;

    public File(String name, Integer size, LocalDateTime createdAt) {
        this.name = name;
        this.size = size;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public Integer getSize() {
        return size;
    }

    @Override
    public String toString() {
        return String.format("%-10s %5dB %s",name,size,createdAt.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return name.equals(file.name) && size.equals(file.size) && createdAt.equals(file.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, size, createdAt);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public int getYear(){
        return createdAt.getYear();
    }
    public String getMonthAndDay(){
        return String.format("%s-%s",createdAt.getMonth(),createdAt.getDayOfMonth());
    }
    public boolean isHidden(){
        return name.charAt(0)=='.';
    }
    @Override
    public int compareTo(File o) {
        return Comparator.comparing(File::getCreatedAt)
                .thenComparing(File::getName)
                .thenComparing(File::getSize)
                .compare(this,o);
    }

}
class FileSystem{
    Map<Character,Set<File>>files;
    public FileSystem(){
        files=new HashMap<>();
    }
    public void addFile(char folder, String name, int size, LocalDateTime createdAt){
        files.putIfAbsent(folder,new TreeSet<>());
        files.get(folder).add(new File(name,size,createdAt));
    }
    public List<File> findAllHiddenFilesWithSizeLessThen(int size){
        return files.values().stream()
                .flatMap(Set::stream)
                .filter(x->x.isHidden()&&x.getSize()<size)
                .collect(Collectors.toList());
    }
    public int totalSizeOfFilesFromFolders(List<Character> folders){
        return files.entrySet().stream()
                .filter(x->folders.contains(x.getKey()))
                .mapToInt(x->x.getValue().stream().mapToInt(File::getSize).sum())
                .sum();
    }
    public Map<Integer, Set<File>> byYear(){
        return files.values().stream()
                .flatMap(Set::stream)
                .collect(Collectors
                        .groupingBy(File::getYear,
                        Collectors.toSet()));
    }
    public Map<String, Long> sizeByMonthAndDay(){
        return files.values().stream()
                .flatMap(Set::stream)
                .collect(Collectors.groupingBy(File::getMonthAndDay,Collectors.summingLong(File::getSize)));
    }
}
public class FileSystemTest {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}

// Your code here

