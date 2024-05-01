package academy.quarkus.pizza.model;

public class Location {
    public static Location current() {
        return new Location();
    }
}
