import java.util.*;

public class Evolution {
	private ArrayList<Player> players;
	private ArrayList<Updater> updaters;
	private ArrayList<Wall> walls;
	private ArrayList<Wall> checkpoints;
	private Wall lapCounter;
	public static final int playerCount = 2000;
	public static final int playersPerUpdater = 100;
	public static final double initialRange = 2;
	public static final double mutationRange = 0.5;
	public Evolution (ArrayList<Player> players, ArrayList<Updater> updaters, ArrayList<Wall> walls, ArrayList<Wall> checkpoints, Wall lapCounter) {
		this.players = players;
		this.updaters = updaters;
		this.walls = walls;
		this.checkpoints = checkpoints;
		this.lapCounter = lapCounter;
	}
	public Player addToUpdaters (Player p) {
		Updater currentUpdater = updaters.get(updaters.size() - 1);
		if (currentUpdater.size() == playersPerUpdater) {
			updaters.add(currentUpdater = new Updater(new ArrayList<Player>()));
		}
		currentUpdater.addPlayer(p);
		return p;
	}
	public void spawnNewPlayer () {
		ArrayList<Double> doubles = new ArrayList<Double>();
		for (int i = 0; i < Player.nodeCount; i++) {
			doubles.add(Math.random() * initialRange - initialRange/2);
		}
		players.add(addToUpdaters(new Player(475, 300, 10, walls, checkpoints, lapCounter, doubles.toArray(new Double[]{}))));
	}
	public void spawnNewPlayer (Player p) {
		Double[] doubles = p.strengths().clone();
		for (int i = 0; i < Player.nodeCount; i++) {
			doubles[i] += Math.random() * mutationRange - mutationRange/2;
		}
		players.add(addToUpdaters(new Player(475, 300, 10, walls, checkpoints, lapCounter, doubles)));
	}
	public void spawnBest (Player best) {
		players.add(addToUpdaters(new Player(475, 300, 10, walls, checkpoints, lapCounter, best.strengths().clone())));
	}
	public void spawnNewGeneration () {
		updaters.add(new Updater(new ArrayList<Player>()));
		for (int i = 0; i < playerCount; i++) {
			spawnNewPlayer();
		}
	}
	public void spawnNewGeneration (Player p) {
		while (players.size() > 0) {
			players.remove(0);
		}
		while (updaters.size() > 0) {
			updaters.remove(0);
		}
		updaters.add(new Updater(new ArrayList<Player>()));
		spawnBest(p); //keep in the best player to conserve changes
		for (int i = 0; i < playerCount - 1; i++) {
			spawnNewPlayer(p);
		}
	}
	public Player getBestPlayer () {
		Player bestPlayer = players.get(0);
		for (Player p : players) {
			if (p.score() > bestPlayer.score()) {
				bestPlayer = p;
			}
		}
		return bestPlayer;
	}
}