package LABS.LAB1_2;


import java.util.Scanner;
import java.util.stream.IntStream;

public class RomanConverterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        IntStream.range(0, n)
                .forEach(x -> System.out.println(RomanConverter.toRoman(scanner.nextInt())));
        scanner.close();
    }
}


class RomanConverter {
    /**
     * Roman to decimal converter
     *
     * @param n number in decimal format
     * @return string representation of the number in Roman numeral
     */
    public static String toRoman(int n) {
        StringBuilder sb = new StringBuilder();
        int[] brojki = {1000,900,500,400,100,90,50,40,10,9,5,4,1};
        String[] romans ={"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};
        for(int i=0;i<13;i++){
            while(n>=brojki[i]){
                sb.append(romans[i]);
                n-= brojki[i];
            }
        }
        return sb.toString();
    }

}
