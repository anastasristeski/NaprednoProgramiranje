package LABS.LAB6.Anagrams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Anagrams {

    public static void main(String[] args) {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) {
        Map<String, List<String>> map = new LinkedHashMap<>();
        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
        bf.lines().forEach(line->{
            //search for key which is anagram to the current line
            String word = map.keySet().stream()
                    .filter(x->isAnagram(line,x))
                    .findFirst()
                    .orElse(null);
            if(word==null){ //if we didn't find any anagram as the key, this is the first
                List<String> newGroup = new ArrayList<>();
                newGroup.add(line);
                map.put(line,newGroup);
            }else{
                map.get(word).add(line);
            }
        });
      map.values().stream().filter(x->x.size()>4).forEach(line-> System.out.println(String.join(" ",line)));
    }
    public static boolean isAnagram(String word1,String word2){
        char[] first = word1.toCharArray();
        char[] second = word2.toCharArray();
        Arrays.sort(first);
        Arrays.sort(second);
        return Arrays.equals(first, second);
    }
}
