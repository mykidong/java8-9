package mykidong;

import java.util.function.Function;

/**
 * Created by mykidong on 2017-06-15.
 */
public class CompositionMain {

    public static void main(String[] args)
    {
        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;

        Function<Integer, Integer> h = f.andThen(g);

        int result = h.apply(3);
        System.out.printf("result: %d\n", result);

        Function<Integer, Integer> h2 = f.compose(g);
        int result2 = h2.apply(3);
        System.out.printf("using compose = result: %d\n", result2);

        Function<String, String> addHeader = Letter::addHeader;
        Function<String, String> transformPipeline = addHeader.andThen(Letter::checkSpelling).andThen(Letter::addFooter);

        String transformedText = transformPipeline.apply("Rock and Roll!, labda");
        System.out.printf("transformed text: %s\n", transformedText);
    }

    public static class Letter{
        public static String addHeader(String text){
            return "From Raoul, Mario and Alan: " + text;
        }
        public static String addFooter(String text){
            return text + " Kind regards";
        }
        public static String checkSpelling(String text){
            return text.replaceAll("labda", "lambda");
        }
    }
}
