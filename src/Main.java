import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class Main extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JFrame frame = new JFrame("AI learns to go around a course");
	private ArrayList<Wall> walls = new ArrayList<Wall>();
	private ArrayList<Player> players = new ArrayList<Player>();
	private Wall lapCounter = new Wall(300, 300, 300, 10, false);
	private ArrayList<Wall> checkpoints = new ArrayList<Wall>();
	private Timer t = new Timer(15, this);
	private Evolution evo;
	private ArrayList<Updater> updaters = new ArrayList<Updater>();
	private Player lastBest;
	private int generation = 1;
	public Main () {
		setPreferredSize(new Dimension(600, 600));
		frame.add(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		walls.add(new Wall(0, 0, 600, 50, false));
		walls.add(new Wall(0, 0, 50, 600, false));
		walls.add(new Wall(0, 550, 600, 50, false));
		walls.add(new Wall(550, 0, 50, 600, false));
		walls.add(new Wall(200, 200, 200, 200, false));
		
		for (int i = 80; i <= 530; i += 50) {
			if (i > 200) {
				checkpoints.add(new Wall(50, i - 5, 150, 10, true));
				checkpoints.add(new Wall(i - 5, 400, 10, 550, false));
			}
			if (i < 400) {
				checkpoints.add(new Wall(400, i - 5, 550, 10, true));
				checkpoints.add(new Wall(i - 5, 50, 10, 150, false));
			}
		}
		
		frame.setVisible(true);
		evo = new Evolution(players, updaters, walls, checkpoints, lapCounter);
		evo.spawnNewGeneration();
		t.start();
	}
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		/*
		for (Wall c : checkpoints) {
			if (c.horizontal()) {
				g.setColor(Color.ORANGE);
			}
			else {
				g.setColor(Color.YELLOW);
			}
			c.draw(g);
		}
		*/
		g.setColor(Color.GREEN);
		lapCounter.draw(g);
		g.setColor(Color.BLACK);
		for (Wall w : walls) {
			w.draw(g);
		}
		g.setColor(Color.RED);
		boolean done = true;
		try {
			for (Player p : players) {
				if (!p.dead()) {
					done = false;
				}
			}
			if (done) {
				lastBest = evo.getBestPlayer();
				evo.spawnNewGeneration(lastBest);
				generation++;
			}
			for (Player p : players) {
				p.draw(g);
			}
			for (Updater u : updaters) {
				u.run();
			}
		} catch (ConcurrentModificationException e) {}
		g.drawString("Generation: " + generation + ", " + Evolution.playerCount + " players per generation", 350, 20);
		if (generation > 1) {
			g.drawString("Last Best Player: " + lastBest.laps()/2 + " laps, " + lastBest.score()/2 + " score", 20, 20);
		}
	}
	public static void main (String[] args) {
		new Main();
	}
	public void actionPerformed (ActionEvent e) {
		repaint();
	}
}