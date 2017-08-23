package mykidong;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Created by mykidong on 2017-06-19.
 */
public class StreamsMain {

    public static void main(String[] args)
    {
        List<Dish> dishes = new ArrayList<>();
        dishes.add(new Dish("dish1", 1500));
        dishes.add(new Dish("dish2", 1800));
        dishes.add(new Dish("dish3", 600));
        dishes.add(new Dish("dish4", 2000));

        List<String> filteredDishes = dishes.parallelStream()
                .filter(d -> d.getCalories() < 1600)
                .sorted(Comparator.comparing(Dish::getCalories))
                .map(Dish::getName)
                .collect(toList());

        System.out.println("filtered dished: " + filteredDishes);

        List<Dish> menu = Arrays.asList(
                new Dish("pork", false, 800, Dish.Type.MEAT),
                new Dish("beef", false, 700, Dish.Type.MEAT),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french fries", true, 530, Dish.Type.OTHER),
                new Dish("rice", true, 350, Dish.Type.OTHER),
                new Dish("season fruit", true, 120, Dish.Type.OTHER),
                new Dish("pizza", true, 550, Dish.Type.OTHER),
                new Dish("prawns", false, 300, Dish.Type.FISH),
                new Dish("salmon", false, 450, Dish.Type.FISH) );

        Map<Dish.Type, List<Dish>> menuMap = menu.parallelStream()
                .collect(groupingBy(Dish::getType));
        System.out.println("menuMap: " + menuMap);

        List<Integer> numbers = Arrays.asList(1, 2, 2, 3, 5, 8, 6, 7, 8);
        numbers.stream()
                .filter(i -> i % 2 == 0)
                .distinct()
                .forEach(System.out::println);

        List<Dish> specialMenu = Arrays.asList(
                new Dish("season fruit", true, 120, Dish.Type.OTHER),
                new Dish("prawns", false, 300, Dish.Type.FISH),
                new Dish("rice", true, 350, Dish.Type.OTHER),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french fries", true, 530, Dish.Type.OTHER));

        List<Dish> filteredMenu
                = specialMenu.stream()
                .filter(dish -> dish.getCalories() > 320)
                .collect(toList());

        List<String> words = Arrays.asList("Java8", "Lambdas", "In", "Action");
        List<Integer> wordLengths = words.stream()
                .map(String::length)
                .collect(toList());

        List<Integer> dishNameLengths = menu.stream()
                .map(Dish::getName)
                .map(String::length)
                .collect(toList());

        dishNameLengths.forEach(i -> System.out.println("dish name length: " + i));


        String[] helloWorld = {"Hello", "World"};

        List<String> filteredStr = Arrays.stream(helloWorld)
                .map(w -> w.split(""))
                .flatMap(wa -> Arrays.stream(wa)) // .flatMap(Arrays::stream)
                .distinct()
                .collect(toList());
        System.out.println("filtered str: " + filteredStr);

        if(menu.stream().anyMatch(Dish::isVegetarian)) {
            System.out.println("The menu is (somewhat) vegetarian friendly!!");
        }

        boolean isHealthy = menu.stream()
                .allMatch(dish -> dish.getCalories() < 1000);

        isHealthy = menu.stream()
                .noneMatch(d -> d.getCalories() >= 1000);

        Optional<Dish> dish =
                menu.stream()
                        .filter(Dish::isVegetarian)
                        .findAny();

        if(dish.isPresent()) {
            System.out.println("vegetarian found: " + dish.get().getName());
        }

        menu.stream()
                .filter(Dish::isVegetarian)
                .findAny()
                .ifPresent(d -> System.out.println("vegetarian found 2: " + d.getName()));

        List<Integer> intList = Arrays.asList(4, 5, 8, 3);
        intList.stream().reduce(Integer::max).ifPresent(integer -> System.out.println("max: " + integer));

        // numerics.
        int caloriesSum = menu.stream().mapToInt(Dish::getCalories).sum();

        // boxing.
        IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
        Stream<Integer> integerStream = intStream.boxed();

        OptionalInt optionalInt = menu.stream().mapToInt(Dish::getCalories).max();
        int maxResult = optionalInt.orElse(1);

        IntStream evenNumbers = IntStream.rangeClosed(1, 100)
                .filter(i -> i % 2 == 0);
        long evenCount = evenNumbers.count();

        Stream<int[]> pythagoreanTriples =
                IntStream.rangeClosed(1, 100)
                .boxed()
                .flatMap(a -> IntStream.rangeClosed(1, 100)
                    .filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
                    .mapToObj(b -> new int[]{a, b, (int)Math.sqrt(a * a + b* b)}));

        pythagoreanTriples.limit(5)
                .forEach(p -> System.out.printf("pythagorean triples - a: %d, b: %d, c: %d\n", p[0], p[1], p[2]));

        Stream<String> stringStream = Stream.of("Java", "Hello", "Streams API");
        stringStream.map(String::toUpperCase).forEach(System.out::println);

        // arrays.
        int[] ints = {1, 3, 5, 3, 7, 7};
        int intSum = Arrays.stream(ints).sum();

        long uniqueWords = 0;
        try(Stream<String> lines = Files.lines(Paths.get("data.txt"), Charset.defaultCharset())) {
            uniqueWords = lines.flatMap(line -> Arrays.stream(line.split(" ")))
                    .distinct()
                    .count();
            System.out.printf("unique words: %d\n", uniqueWords);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static class Dish
    {
        private String name;
        private int calories;
        private boolean vegetarian = false;
        private Type type = Type.FISH;


        public Dish(String name, int calories)
        {
            this.name = name;
            this.calories = calories;
        }

        public Dish(String name, int calories, Type type)
        {
            this.name = name;
            this.calories = calories;
            this.type = type;
        }

        public Dish(String name, boolean vegetarian, int calories, Type type)
        {
            this.name = name;
            this.vegetarian = vegetarian;
            this.calories = calories;
            this.type = type;
        }

        public boolean isVegetarian() {
            return vegetarian;
        }

        public void setVegetarian(boolean vegetarian) {
            this.vegetarian = vegetarian;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCalories() {
            return calories;
        }

        public void setCalories(int calories) {
            this.calories = calories;
        }

        @Override
        public String toString()
        {
            return this.name;
        }

        public enum Type
        {
            FISH, OTHER, MEAT;
        }
    }
}
