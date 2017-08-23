package mykidong;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.stream.Collector.Characteristics.CONCURRENT;
import static java.util.stream.Collector.Characteristics.IDENTITY_FINISH;
import static java.util.stream.Collectors.*;

/**
 * Created by mykidong on 2017-06-20.
 */
public class CollectionsMain {

    public static void main(String[] args)
    {
        List<StreamsMain.Dish> menu = Arrays.asList(
                new StreamsMain.Dish("pork", false, 800, StreamsMain.Dish.Type.MEAT),
                new StreamsMain.Dish("beef", false, 700, StreamsMain.Dish.Type.MEAT),
                new StreamsMain.Dish("chicken", false, 400, StreamsMain.Dish.Type.MEAT),
                new StreamsMain.Dish("french fries", true, 530, StreamsMain.Dish.Type.OTHER),
                new StreamsMain.Dish("rice", true, 350, StreamsMain.Dish.Type.OTHER),
                new StreamsMain.Dish("season fruit", true, 120, StreamsMain.Dish.Type.OTHER),
                new StreamsMain.Dish("pizza", true, 550, StreamsMain.Dish.Type.OTHER),
                new StreamsMain.Dish("prawns", false, 300, StreamsMain.Dish.Type.FISH),
                new StreamsMain.Dish("salmon", false, 450, StreamsMain.Dish.Type.FISH) );

        Optional<StreamsMain.Dish> dishOptional = menu.stream()
                .collect(maxBy(Comparator.comparingInt(StreamsMain.Dish::getCalories)));

        dishOptional.ifPresent(d -> System.out.printf("max calories: name - %s, calories: %d\n", d.getName(), d.getCalories()));

        // totoal calories.
        int totalCalories = menu.stream().collect(summingInt(StreamsMain.Dish::getCalories));
        System.out.println("totalCalories: " + totalCalories);

        // joining.
        String menuNames = menu.stream().map(StreamsMain.Dish::getName).collect(joining(", "));
        System.out.println("menu names: " + menuNames);

        // reducing.
        Optional<StreamsMain.Dish> mostCaloriesDish = menu.stream().collect(reducing((d1, d2) -> (d1.getCalories() > d2.getCalories()) ? d1 : d2));
        mostCaloriesDish.ifPresent(dish -> System.out.printf("most calories dish - name: %s, calories: %d\n", dish.getName(), dish.getCalories()));

        Map<CaloricLevel, List<StreamsMain.Dish>> caloricLevelListMap = menu.stream()
                .collect(groupingBy(dish -> {
                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                    else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                    else return CaloricLevel.FAT;
                }));
        System.out.println("calorical level map: " + caloricLevelListMap);

        Map<StreamsMain.Dish.Type, Long> typeCountMap = menu.stream()
                .collect(groupingBy(StreamsMain.Dish::getType, counting()));
        System.out.println("type count: " + typeCountMap);

        Map<StreamsMain.Dish.Type, Optional<StreamsMain.Dish>> typeOptionalMap = menu.stream()
                .collect(groupingBy(StreamsMain.Dish::getType, maxBy(Comparator.comparingInt(StreamsMain.Dish::getCalories))));
        System.out.println("type max calories: " + typeOptionalMap);

        Map<StreamsMain.Dish.Type, StreamsMain.Dish> mostCaloricByType = menu.stream()
                .collect(groupingBy(StreamsMain.Dish::getType, collectingAndThen(maxBy(Comparator.comparingInt(StreamsMain.Dish::getCalories)), Optional::get)));
        System.out.println("mostCaloricByType: " + mostCaloricByType);

        Map<StreamsMain.Dish.Type, Integer> totalCaloriesMap = menu.stream()
                .collect(groupingBy(StreamsMain.Dish::getType, summingInt(StreamsMain.Dish::getCalories)));
        System.out.println("totalCaloriesMap: " + totalCaloriesMap);

        Map<StreamsMain.Dish.Type, Set<CaloricLevel>> caloricLevelByType = menu.stream()
                .collect(groupingBy(StreamsMain.Dish::getType,
                                    mapping(dish -> {
                                        if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                                        else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                                        else return CaloricLevel.FAT; },
                                            toSet())));

        System.out.println("caloricLevelByType: " + caloricLevelByType);

        Map<StreamsMain.Dish.Type, Set<CaloricLevel>> caloricLevelsByType2 =
                menu.stream().collect(
                        groupingBy(StreamsMain.Dish::getType, mapping(
                                dish -> { if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                                else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                                else return CaloricLevel.FAT; },
                                toCollection(HashSet::new) )));
        System.out.println("caloricLevelsByType2: " + caloricLevelsByType2);

        // partitioning.
        Map<Boolean, List<StreamsMain.Dish>> partitionedMenu =
                menu.stream().collect(partitioningBy(StreamsMain.Dish::isVegetarian));
        List<StreamsMain.Dish> vegetarianDishes = partitionedMenu.get(true);

        List<StreamsMain.Dish> vegetarianDishes2 =
                menu.stream().filter(StreamsMain.Dish::isVegetarian).collect(toList());

        Map<Boolean, Map<StreamsMain.Dish.Type, List<StreamsMain.Dish>>> vegetarianDishesByType = menu.stream()
                .collect(partitioningBy(StreamsMain.Dish::isVegetarian, groupingBy(StreamsMain.Dish::getType, toList())));
        System.out.println("vegetarianDishesByType: " + vegetarianDishesByType);

        Map<Boolean, StreamsMain.Dish> mostCaloricPartitionedByVegetarian = menu.stream()
                .collect(partitioningBy(StreamsMain.Dish::isVegetarian,
                                        collectingAndThen(maxBy(Comparator.comparingInt(StreamsMain.Dish::getCalories)),
                                                          Optional::get)));
        System.out.println("mostCaloricPartitionedByVegetarian: " + mostCaloricPartitionedByVegetarian);

        List<StreamsMain.Dish> menuDishes = menu.stream().collect(new ToListCollector<StreamsMain.Dish>());

    }

    public static class ToListCollector<T> implements Collector<T, List<T>, List<T>> {
        @Override
        public Supplier<List<T>> supplier() {
            return ArrayList::new;
        }
        @Override
        public BiConsumer<List<T>, T> accumulator() {
            return List::add;
        }
        @Override
        public Function<List<T>, List<T>> finisher() {
            return Function.identity();
        }
        @Override
        public BinaryOperator<List<T>> combiner() {
            return (list1, list2) -> {
                list1.addAll(list2);
                return list1;
            };
        }
        @Override
        public Set<Characteristics> characteristics() {
            return Collections.unmodifiableSet(EnumSet.of(
                    IDENTITY_FINISH, CONCURRENT));
        }
    }

    public enum CaloricLevel
    {
        DIET, NORMAL, FAT;
    }
}
