package LABS.LAB7.MP3Player;
import java.util.ArrayList;
import java.util.List;
class Song{
    String title;
    String artist;


    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }


    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    @Override
    public String toString() {
        return "Song{" +
                "title=" + title +
                ", artist=" + artist +
                '}';
    }
}
class MP3Player{
    List<Song> playlist;
    int current;
    boolean playing;
    boolean songsAreStopped;
    public MP3Player(List<Song> playlist) {
        this.playlist = playlist;
        current = 0;
        playing = false;
        songsAreStopped = false;
    }
    public void pressPlay(){
       if(playing) {
            System.out.println("Song is already playing");
        }else{
           System.out.printf("Song %d is playing\n",current);
           playing = true;
       }
       songsAreStopped = false;
    }
    public void printCurrentSong(){
        System.out.println(playlist.get(current));
    }
    public void pressStop(){
        if(songsAreStopped){
            System.out.println("Songs are already stopped");
            return ;
        }
        if(playing) {
            System.out.printf("Song %d is paused\n", current);
            playing = false;
        }
        else{
            System.out.println("Songs are stopped");
            current = 0;
            songsAreStopped = true;
        }
    }
    public void pressFWD(){
        System.out.println("Forward...");
        if(current==playlist.size()-1){
            current = 0;
        }else{
            current++;
        }
        playing = false;
    }
    public void pressREW(){
        System.out.println("Reward...");
        if(current==0){
            current = playlist.size()-1;
        }else{
            current--;
        }
        playing = false;
    }

    @Override
    public String toString() {
        return "MP3Player{" +
                "currentSong = " + current +
                ", songList = " + playlist +
                '}';
    }
}
public class PatternTest {
    public static void main(String args[]) {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);


        System.out.println(player.toString());
        System.out.println("First test");

        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Second test");


        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Third test");


        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
    }
}

//Vasiot kod ovde