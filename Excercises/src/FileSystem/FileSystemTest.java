package FileSystem;

import java.util.ArrayList;
import java.util.Scanner;
interface IFile extends Comparable<IFile>{
    String getFileName();
    long getFileSize();
    String getFileInfo(int indent);
    void sortBySize();
    long findLargestFile();

    @Override
    default int compareTo(IFile o){
        return Long.compare(getFileSize(),o.getFileSize());
    }
}
class File implements IFile{
String name;
long size;

    public File(String name, long size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        return size;
    }

    @Override
    public String getFileInfo(int indent) {
       StringBuilder sb = new StringBuilder();
       for(int i=0;i<indent;i++){
           sb.append("\t");
       }
       sb.append(String.format("File name: %10s File size: %10d\n",getFileName(),getFileSize()));
        return sb.toString();
    }

    @Override
    public void sortBySize() {

    }

    @Override
    public long findLargestFile() {
        return size;
    }
}
class FileNameExistsException extends Exception{
    public FileNameExistsException(String file, String folder) {
        super(String.format("There is already a file named %s in the folder %s",file,folder));
    }
}
class Folder implements IFile{
    String name;
    ArrayList<IFile> list;

    public Folder(String name) {
        this.name = name;
        list = new ArrayList<>();
    }
    private boolean duplicateCheck(IFile file){
        for(IFile f: list){
            if(f.getFileName().equals(file.getFileName()))
                return true;
        }
        return false;
    }
    public void addFile(IFile file) throws FileNameExistsException {
        if(duplicateCheck(file)){
            throw new FileNameExistsException(file.getFileName(),this.name);
        }
        list.add(file);
    }
    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        return list.stream().mapToLong(IFile::getFileSize).sum();
    }

    @Override
    public String getFileInfo(int indent) {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<indent;i++){
            sb.append("\t");
        }
        sb.append(String.format("Folder name: %10s Folder size: %10d\n",getFileName(),getFileSize()));
        for(IFile f : list){
            sb.append(f.getFileInfo(indent+1));
        }
        return sb.toString();
    }

    @Override
    public void sortBySize() {
        list.sort(IFile::compareTo);
        for(IFile f : list){
            f.sortBySize();
        }
    }

    @Override
    public long findLargestFile() {
        return list.stream().mapToLong(IFile::findLargestFile).max().orElse(0);
    }
}
class FileSystem{
    private final Folder root;
    public FileSystem(){
        root=new Folder("root");
    }
    public void addFile(IFile file) throws FileNameExistsException {
        root.addFile(file);
    }
    public long findLargestFile(){
        return root.findLargestFile();
    }
    public void sortBySize(){
        root.sortBySize();
    }

    @Override
    public String toString() {
        return root.getFileInfo(0);
    }
}
public class FileSystemTest {

    public static Folder readFolder (Scanner sc)  {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i=0;i<totalFiles;i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String [] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args)  {

        //file reading from input

        Scanner sc = new Scanner (System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());




    }
}