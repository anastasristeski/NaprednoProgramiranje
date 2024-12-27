package Midterm1.Risk;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Player{
    ArrayList<Integer> rolledDices;
    public Player(String line){
        String [] parts = line.split("\\s+");
        rolledDices = new ArrayList<>();
        for(String s : parts){
            rolledDices.add(Integer.parseInt(s));
        }
        rolledDices.sort(Integer::compareTo);
    }
    public int attack(Player attacking){
        for(int i=0;i<rolledDices.size();i++){
            if(this.rolledDices.get(i)<=attacking.rolledDices.get(i)){
                return 0;
            }
        }
        return 1;
    }
}
class Risk{
    public Risk() {
    }

    public static int processAttacksData (InputStream is){
        Scanner sc = new Scanner(is);
        Player attacker;
        Player defender;
        int succAttacks = 0;
        while(sc.hasNextLine()){
            String[]players = sc.nextLine().split(";");
            attacker = new Player(players[0]);
            defender = new Player(players[1]);
            succAttacks+=attacker.attack(defender);
        }
        return succAttacks;
    }
}
public class RiskTester {
    public static void main(String[] args) {

        Risk risk = new Risk();

        System.out.println(risk.processAttacksData(System.in));

    }
}