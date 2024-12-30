package LABS.LAB5.TermFrequency;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

class TermFrequency{
    Map<String,Integer> wordsMap;
    public TermFrequency(InputStream inputStream, String[] stopWords) {
       wordsMap = new TreeMap<>();
       Scanner sc = new Scanner(inputStream);
       while(sc.hasNext()){
           String word = sc.next();
           word = word.toLowerCase()
                   .replaceAll("[,.]","")
                   .trim();
          if(!(word.isEmpty() || Arrays.asList(stopWords).contains(word))){
               wordsMap.putIfAbsent(word,0);
               wordsMap.put(word,wordsMap.get(word)+1);
           }
       }
    }

    public int countTotal(){
        return wordsMap.values().stream().mapToInt(Integer::intValue).sum();
    }
    public int countDistinct(){
        return wordsMap.size();
    }
    public List<String> mostOften(int k){
        return wordsMap.keySet()
                .stream()
                .sorted(Comparator.comparing(x->wordsMap.get(x)).reversed())
                .limit(k)
                .collect(Collectors.toList());
    }
}
public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја" };
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
         System.out.println(tf.mostOften(10));
    }
}
// vasiot kod ovde
