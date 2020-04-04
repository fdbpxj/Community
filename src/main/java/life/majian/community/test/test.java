package life.majian.community.test;


import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class test{

    public static void main(String[] args) {
        List<String> list= Arrays.asList("a","b","c","d","c");

        Set<String> collec=list.stream().map(String::toUpperCase).collect(Collectors.toSet());
        List<String> collect=list.stream().map(n->n+"s").collect(Collectors.toList());
        System.out.println(collec);
        System.out.println(collect);
    }



}
