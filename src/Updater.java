import java.util.*;

public class Updater extends Thread {
    private ArrayList<Player> players;
    public Updater (ArrayList<Player> players) {
        this.players = players;
    }
    public void addPlayer (Player p) {
        players.add(p);
    }
    public int size () {
        return players.size();
    }
    public void run () {
        for (Player p : players) {
            p.update();
        }
    }
}