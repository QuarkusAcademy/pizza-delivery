package academy.quarkus.pizza.auth;

public class UserDetails {
    boolean anonymous = true;
    String username = "nobody";

    public UserDetails(){}

    public UserDetails(boolean anonymous, String username) {
        this.anonymous = anonymous;
        this.username = username;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "[%s %s]".formatted(anonymous, username);
    }
}
