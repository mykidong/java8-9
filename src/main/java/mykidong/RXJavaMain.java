package mykidong;


import rx.Observable;

/**
 * Created by mykidong on 2017-06-22.
 */
public class RXJavaMain {

    public static void main(String[] args)
    {
        hello("Ben", "George");
    }

    public static void hello(String... names)
    {
        Observable.from(names).subscribe(s -> System.out.printf("Hello %s!\n", s));
    }
}
