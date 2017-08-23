package mykidong;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by mykidong on 2017-06-13.
 */
public class FunctionalInterfaceMain {

    public static void main(String[] args) throws Exception
    {
        Runnable r1 = () -> System.out.println("hello runnable r1");

        process(r1);

        process(() -> System.out.println("hello runnable r2"));

        String oneLine = processFile((BufferedReader br) -> br.readLine());
        System.out.printf("one line: %s\n", oneLine);

        String twoLine = processFile((BufferedReader br) -> br.readLine() + br.readLine());
        System.out.printf("two lines: %s\n", twoLine);

        // Consumer.
        foreach(Arrays.asList(1, 2, 3, 4, 5), (Integer i) -> System.out.println("consumer: " + i));

        // Function.
        List<Integer> outputs = map(Arrays.asList("hello", "this is", "kidong lee"), (String s) -> s.length());
        outputs.stream().forEach((Integer i) -> System.out.printf("map: %d, ", i));
    }

    public static <T, R> List<R> map(List<T> inputs, Function<T, R> f)
    {
        List<R> results = new ArrayList<>();

        for(T t : inputs)
        {
            results.add(f.apply(t));
        }

        return results;
    }

    public static <T> void foreach(List<T> list, Consumer<T> c)
    {
        for(T t : list)
        {
            c.accept(t);
        }
    }

    public static void process(Runnable r)
    {
        r.run();
    }

    @FunctionalInterface
    public interface BufferedReaderProcessor {
        String process(BufferedReader br) throws IOException;
    }

    public static String processFile(BufferedReaderProcessor p) throws IOException {
        try (BufferedReader br =
                     new BufferedReader(new FileReader("data.txt"))) {
            return p.process(br);
        }
    }
}
