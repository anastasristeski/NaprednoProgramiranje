package LABS.LAB2.LAB2_2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
class MovableObjectNotFittableException extends Exception{
    Movable m;
    public MovableObjectNotFittableException(Movable m) {
        this.m = m;
    }
    public void message(){
        if(m.getType()==TYPE.POINT){
            System.out.printf("Point (%d,%d) is out of bounds\n",m.getCurrentXPosition(),m.getCurrentYPosition());
        }else{
            System.out.printf("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection\n",m.getCurrentXPosition(),m.getCurrentYPosition(),((MovableCircle)m).getRadius());
        }
    }
}
class ObjectCanNotBeMovedException extends Exception{
    Movable m;

    public ObjectCanNotBeMovedException(Movable m) {
        this.m = m;
    }
   public void message(){
        System.out.printf("Point (%d,%d) is out of bounds\n",m.getCurrentXPosition(),m.getCurrentYPosition());
    }
}
enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}
interface Movable{
    void moveUp() throws ObjectCanNotBeMovedException;
    void moveLeft() throws ObjectCanNotBeMovedException;
    void moveRight() throws ObjectCanNotBeMovedException;
    void moveDown() throws ObjectCanNotBeMovedException;
    TYPE getType();
    int getCurrentXPosition();
    int getCurrentYPosition();
  //  void moveInDirection(DIRECTION d) throws ObjectCanNotBeMovedException;
}
class MovablePoint implements Movable{
 private int x;
 private int y;
 private int xSpeed;
 private int ySpeed;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        moveObject(DIRECTION.UP);
    }
    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        moveObject(DIRECTION.DOWN);
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        moveObject(DIRECTION.RIGHT);
    }
    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        moveObject(DIRECTION.LEFT);
    }


    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovablePoint that = (MovablePoint) o;
        return x == that.x && y == that.y && xSpeed == that.xSpeed && ySpeed == that.ySpeed;
    }
    public TYPE getType(){
        return TYPE.POINT;
    }
    @Override
    public int hashCode() {
        return Objects.hash(x, y, xSpeed, ySpeed);
    }

    @Override
    public String toString() {
        return String.format("Movable point with coordinates (%d,%d)\n",x,y);
    }
    public void moveObject(DIRECTION d) throws ObjectCanNotBeMovedException {
            if(d.equals(DIRECTION.RIGHT)){
                if(x+xSpeed>MovablesCollection.getxMax()) throw new ObjectCanNotBeMovedException(new MovablePoint(x+xSpeed,y,0,0));
                else{
                    x += xSpeed;
                }
            }
            if(d.equals(DIRECTION.LEFT)){
                if(x-xSpeed<0)throw new ObjectCanNotBeMovedException(new MovablePoint(x-xSpeed,y,0,0));
                else {
                    x -= xSpeed;
                }
            }
            if(d.equals(DIRECTION.UP)){
                if(y+ySpeed>MovablesCollection.getyMax())throw new ObjectCanNotBeMovedException(new MovablePoint(x,y+ySpeed,0,0));
             else {
                    y += ySpeed;
                }
            }
            if(d.equals(DIRECTION.DOWN)){
                if(y-ySpeed<0)throw new ObjectCanNotBeMovedException(new MovablePoint(x,y-ySpeed,0,0));
              else  {
                    y -= ySpeed;
                }
            }
    }

}
class MovableCircle implements Movable{
     int radius;
     MovablePoint center;

    public MovableCircle(int radius, MovablePoint center){
        this.radius = radius;
        this.center = center;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        center.moveUp();
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        center.moveLeft();
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        center.moveRight();
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        center.moveDown();
    }

    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }

//    @Override
//    public void moveInDirection(DIRECTION d) throws ObjectCanNotBeMovedException {
//        center.moveInDirection(d);
//    }

    public TYPE getType(){
        return TYPE.CIRCLE;
    }
    public int getRadius(){
        return radius;
    }
    @Override
    public String toString() {
        return String.format("Movable circle with center coordinates (%d,%d) and radius %d\n",center.getCurrentXPosition(),center.getCurrentYPosition(),radius);
    }

}
class MovablesCollection{
    static int X_MAX = 0;
    static int Y_MAX = 0;
    ArrayList<Movable> movables;
    MovablesCollection(int x_max,int y_max){
        X_MAX = x_max;
        Y_MAX = y_max;
        movables = new ArrayList<>();
    }
    public void addMovableObject(Movable m) throws MovableObjectNotFittableException {
        if(!checkBoundsOfObject(m)) {
            throw new MovableObjectNotFittableException(m);
        }
        movables.add(m);
    }
    public boolean checkBoundsOfObject(Movable m){
        if(m.getCurrentXPosition()<0||m.getCurrentXPosition()>X_MAX)
            if(m.getCurrentYPosition()<0||m.getCurrentYPosition()>Y_MAX)
                    return false;
        if(m.getType()==TYPE.CIRCLE){
            int radius = ((MovableCircle)m).getRadius();
            int rightX = m.getCurrentXPosition()+radius;
            int leftX = m.getCurrentXPosition() - radius;
            int upY = m.getCurrentYPosition() + radius;
            int downY = m.getCurrentYPosition() - radius;
            if(rightX>X_MAX||leftX<0||upY>Y_MAX||downY<0)
                    return false;
        }
       return true;
    }

    public static int getxMax() {
        return X_MAX;
    }

    public static int getyMax() {
        return Y_MAX;
    }

    public static void setxMax(int xMax) {
        X_MAX = xMax;
    }

    public static void setyMax(int yMax) {
        Y_MAX = yMax;
    }
    public void moveObjectsFromTypeWithDirection (TYPE type, DIRECTION direction) throws ObjectCanNotBeMovedException {
        for(Movable m: movables){
            if(m.getType().equals(type)){
               try {
                    if (direction.equals(DIRECTION.UP)) {
                        m.moveUp();
                    }
                    if (direction.equals(DIRECTION.DOWN)) {
                        m.moveDown();
                    }
                    if (direction.equals(DIRECTION.RIGHT)) {
                        m.moveRight();
                    }
                    if (direction.equals(DIRECTION.LEFT)) {
                        m.moveLeft();
                    }
                }catch(ObjectCanNotBeMovedException o){
                   o.message();
               }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Collection of movable objects with size ").append(movables.size()).append(":").append("\n");
        for(Movable m : movables)
            sb.append(m.toString());
        return sb.toString();
    }
}
public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                try{
                    collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
                }catch (MovableObjectNotFittableException mv){
                    mv.message();
                }
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                try{
                    collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
                }catch(MovableObjectNotFittableException mv){
                    mv.message();
                }
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        try{
            collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        }catch(ObjectCanNotBeMovedException o){
            o.message();
        }
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
       try {
            collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        }catch(ObjectCanNotBeMovedException o){
           o.message();
         }
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        try{
            collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        }catch(ObjectCanNotBeMovedException o){
           o.message();
        }
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        try{
            collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        }catch(ObjectCanNotBeMovedException o){
            o.message();
        }
        System.out.println(collection.toString());


    }


}
