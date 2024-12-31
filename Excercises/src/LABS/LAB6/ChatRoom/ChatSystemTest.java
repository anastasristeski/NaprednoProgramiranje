package LABS.LAB6.ChatRoom;
import com.sun.source.tree.Tree;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;
class NoSuchRoomException extends Exception{
    public NoSuchRoomException(String roomName) {
        super(String.format("NoSuchRoomException %s\n",roomName));
    }
}
class NoSuchUserException extends Exception{
    public NoSuchUserException(String message) {
        super(String.format("NoSuchUserException %s\n",message));
    }
}
class ChatRoom{
    String name;
    Set<String> users;

    public ChatRoom(String name) {
        this.name = name;
        users = new TreeSet<>();
    }
    public void addUser(String username){
        users.add(username);
    }
    public void removeUser(String username){
        users.remove(username);
    }
    public boolean hasUser(String username){
        return users.contains(username);
    }
    public int numUsers(){
        return users.size();
    }
    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        if(users.isEmpty()){
            sb.append("EMPTY\n");
            return sb.toString();
        }
        String str = users.stream().map(String::toString).collect(Collectors.joining("\n"));
        sb.append(str).append("\n");
        return sb.toString();
    }
}
class ChatSystem{
    Map<String,ChatRoom> chatRooms;
    Set<String> allUsers;

    public ChatSystem() {
        chatRooms = new TreeMap<>();
        allUsers = new HashSet<>();
    }
    public void addRoom(String roomName){
        chatRooms.put(roomName,new ChatRoom(roomName));
    }
    public void removeRoom(String roomName){
        chatRooms.remove(roomName);
    }
    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        checkRoom(roomName);
        return chatRooms.get(roomName);
    }
    public void register(String userName) {
        ChatRoom temp = chatRooms.values()
                .stream()
                .min(Comparator.comparing(ChatRoom::numUsers).thenComparing(ChatRoom::getName))
                .orElse(null);
        allUsers.add(userName);
        if(temp != null) {
            temp.addUser(userName);
         }
    }
    public void registerAndJoin(String userName, String roomName){
        allUsers.add(userName);
        chatRooms.get(roomName).addUser(userName);
    }
    public void joinRoom(String userName, String roomName) throws NoSuchUserException, NoSuchRoomException {
        checkRoom(roomName);
        checkUser(userName);
        chatRooms.get(roomName).addUser(userName);
    }
    public void leaveRoom(String userName,String roomName) throws NoSuchRoomException {
        checkRoom(roomName);
        chatRooms.get(roomName).removeUser(userName);
    }
    public void followFriend(String username,String friend_username) throws NoSuchUserException {
        checkUser(username);
        checkUser(friend_username);
        chatRooms.values().stream()
                .filter(x->x.users.contains(friend_username))
                .forEach(x->x.addUser(username));
    }
    public void checkRoom(String roomName) throws NoSuchRoomException {
        if(!chatRooms.containsKey(roomName)){
            throw new NoSuchRoomException(roomName);
        }
    }
    public void checkUser(String userName) throws NoSuchUserException {
        if(!allUsers.contains(userName)){
            throw new NoSuchUserException(userName);
        }
    }
}
public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr.addUser(jin.next());
                if ( k == 1 ) cr.removeUser(jin.next());
                if ( k == 2 ) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if ( n == 0 ) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr2.addUser(jin.next());
                if ( k == 1 ) cr2.removeUser(jin.next());
                if ( k == 2 ) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if ( k == 1 ) {
            ChatSystem cs = new ChatSystem();
            Method[] mts = cs.getClass().getMethods();
            while ( true ) {
                String cmd = jin.next();
                if ( cmd.equals("stop") ) break;
                if ( cmd.equals("print") ) {
                    System.out.println(cs.getRoom(jin.next())+"\n");continue;
                }
                for ( Method m : mts ) {
                    if ( m.getName().equals(cmd) ) {
                        try{
                            String[] params = new String[m.getParameterTypes().length];
                            for (int i = 0; i < params.length; ++i) params[i] = jin.next();
                            m.invoke(cs, (Object[]) params);
                        }catch(InvocationTargetException ignored){

                        }
                    }
                }
            }
        }
    }

}
