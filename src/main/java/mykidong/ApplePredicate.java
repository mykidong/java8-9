package mykidong;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by mykidong on 2017-06-13.
 */
public class ApplePredicate {

    public static void main(String[] args)
    {
        List<Apple> inventory = new ArrayList<>();
        inventory.add(new Apple(Color.GREEN, 200, "USA"));
        inventory.add(new Apple(Color.RED, 200, "Germany"));
        inventory.add(new Apple(Color.GREEN, 200, "France"));
        inventory.add(new Apple(Color.RED, 80, "South Korea"));
        inventory.add(new Apple(Color.GREEN, 90, "USA"));

        // filter.
        List<Apple> greenApples = filter(inventory, (Apple p) -> {
            return p.getColor().equals(Color.GREEN);
        });

        greenApples.stream().forEach(apple -> System.out.printf("color: %s, weight: %d\n", apple.getColor().name(), apple.getWeight()));

        // sort.
        inventory.sort((Apple apple1, Apple apple2) -> apple1.getWeight() - apple2.getWeight());
        inventory.stream().forEach(apple -> System.out.printf("sorted by weight ASC - color: %s, weight: %d\n", apple.getColor().name(), apple.getWeight()));

        // sort by weight desc, country.
        inventory.sort(Comparator.comparing(Apple::getWeight).reversed().thenComparing(Apple::getCountry));
        inventory.stream().forEach(apple -> System.out.printf("sort by weight desc, country - color: %s, weight: %d, country: %s\n", apple.getColor().name(), apple.getWeight(), apple.getCountry()));

        // composition.
        Predicate<Apple> redApple = apple -> apple.getColor().equals(Color.RED);
        Predicate<Apple> moreFilters = redApple.and(apple -> apple.getWeight() > 150).or(apple -> apple.getColor().equals(Color.GREEN));
        List<Apple> moreFilterApples = filter(inventory, moreFilters);
        moreFilterApples.stream().forEach(apple -> System.out.printf("more filters - color: %s, weight: %d, country: %s\n", apple.getColor().name(), apple.getWeight(), apple.getCountry()));
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> p) {
        List<T> result = new ArrayList<>();
        for(T e: list) {
            if(p.test(e)) {
                result.add(e);
            }
        }
        return result;
    }


    public enum Color
    {
        RED, GREEN;
    }

    public static class Apple
    {
        private Color color = Color.GREEN;

        private int weight = 100;

        private String country = "South Korea";

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public Apple()
        {}

        public Apple(int weight)
        {
            this.weight = weight;
        }

        public Apple(Color color, int weight)
        {
            this.color = color;
            this.weight = weight;
        }

        public Apple(Color color, int weight, String country)
        {
            this.color = color;
            this.weight = weight;
            this.country = country;
        }


        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }
}
