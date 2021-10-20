package game;

public class Console implements Observer {
    public void update(String message) {
        System.out.println(message);
    }

}
