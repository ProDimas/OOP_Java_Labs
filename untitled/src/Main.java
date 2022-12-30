
public class Main {
    public static void main(String[] args) {
        Class<? extends Apple> a = Apple.class;
        System.out.println(a.getName());
    }
}

interface Peelable {
    default void f() {};
}

interface Growable {
    default void g() {};
}

class Apple implements Peelable, Growable {}