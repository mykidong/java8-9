package mykidong;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by mykidong on 2017-06-14.
 */
public class MethodConstructorReferenceMain {

    public static void main(String[] args)
    {
        List<String> str = Arrays.asList("a","b","A","B");

        str.sort(String::compareToIgnoreCase);

        System.out.println(str);


        Supplier<ApplePredicate.Apple> s1 = ApplePredicate.Apple::new;

        ApplePredicate.Apple apple1 = s1.get();

        System.out.printf("color: %s, weight: %d\n", apple1.getColor(), apple1.getWeight());

        Function<Integer, ApplePredicate.Apple> f1 = ApplePredicate.Apple::new;

        ApplePredicate.Apple apple2 = f1.apply(150);

        System.out.printf("color: %s, weight: %d\n", apple2.getColor(), apple2.getWeight());

        List<Integer> list = Arrays.asList(50, 40, 70, 80);
        List<ApplePredicate.Apple> mapList = map(list, ApplePredicate.Apple::new);
        mapList.stream().forEach((apple -> System.out.printf("mapped - color: %s, weight: %d\n", apple.getColor(), apple.getWeight())));

        BiFunction<ApplePredicate.Color, Integer, ApplePredicate.Apple> biFunction = ApplePredicate.Apple::new;
        ApplePredicate.Apple apple3 = biFunction.apply(ApplePredicate.Color.RED, 200);
        System.out.printf("color: %s, weight: %d\n", apple3.getColor(), apple3.getWeight());
    }

    public static List<ApplePredicate.Apple> map(List<Integer> list, Function<Integer, ApplePredicate.Apple> f)
    {
        List<ApplePredicate.Apple> resultList = new ArrayList<>();

        for(Integer i : list)
        {
            resultList.add(f.apply(i));
        }

        return resultList;
    }
}
