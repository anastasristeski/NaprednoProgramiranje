package Midterm1.Risk;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Player{
    ArrayList<Integer> rolledDices;
    int survivedSoldiers;
    public Player(String line){
        String [] parts = line.split("\\s+");
        rolledDices = new ArrayList<>();
        for(String s : parts){
            rolledDices.add(Integer.parseInt(s));
        }
        survivedSoldiers = 3;
        rolledDices.sort(Integer::compareTo);
    }
    public String attack(Player attacking){
        int survidedAttacker = 0;
        int survivedDefender = 0;
        for(int i=0;i<rolledDices.size();i++){
            if(this.rolledDices.get(i)<=attacking.rolledDices.get(i)){
                survivedDefender++;
            }
            if(this.rolledDices.get(i)>attacking.rolledDices.get(i)){
                survidedAttacker++;
            }
        }
        return String.format("%d %d",survidedAttacker,survivedDefender);
    }
}
class Risk{
    public Risk() {
    }

    public static void processAttacksData (InputStream is){
        Scanner sc = new Scanner(is);
        Player attacker;
        Player defender;
        while(sc.hasNextLine()){
            String[]players = sc.nextLine().split(";");
            attacker = new Player(players[0]);
            defender = new Player(players[1]);
            System.out.println(attacker.attack(defender));
        }

    }
}
public class RiskTester {
    public static void main(String[] args) {

        Risk risk = new Risk();

        risk.processAttacksData(System.in);

    }
}