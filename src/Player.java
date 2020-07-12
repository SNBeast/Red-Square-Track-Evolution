import java.awt.Graphics;
import java.util.ArrayList;
public class Player {
	public static final int maxLife = 1000;
	public static final int nodeCount = 20;
	public static final int wsr = 20; //"Wall Search Resolution"
	private double x;
	private double y;
	private double radius;
	private ArrayList<Wall> walls;
	private ArrayList<Wall> checkpoints;
	private Wall lapCounter;
	private boolean dead = false;
	private boolean collidingWithCheckpoint = false;
	private boolean collidingWithLapCounter = false;
	private double dx = 0;
	private double dy = 0;
	private int laps;
	private int score;
	private boolean wrongDirection;
	private boolean touchingHorizontalWall;
	private double[] distances = new double[8];
	private int life = 0;

	private Node ddx = new Node(10);
	private Node ddy = new Node(10);
	private Double[] strengths;
	public Player (double x, double y, double radius, ArrayList<Wall> walls, ArrayList<Wall> checkpoints, Wall lapCounter, Double[] strengths) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.walls = walls;
		this.checkpoints = checkpoints;
		this.lapCounter = lapCounter;
		this.strengths = strengths;
	}
	public boolean dead () {
		return dead;
	}
	public int laps () {
		return laps;
	}
	public int score () {
		return score;
	}
	public Double[] strengths () {
		return strengths;
	}
	public double[] distance (double deltaX, double deltaY) {
		double xDist = x;
		double yDist = y;
		search:
		while (true) {
			for (Wall w : walls) {
				if (w.collision(xDist, yDist, 0.1)) {
					break search;
				}
			}
			xDist += deltaX;
			yDist += deltaY;
		}
		return new double[]{xDist, yDist, Math.sqrt(Math.pow(x - xDist, 2) + Math.pow(y - yDist, 2))};
	}
	public void update () {
		if (!dead) {
			double[] d;
			brain();
			x += dx;
			y += dy;
			int k = 0;
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (!(i == 0 && j == 0)) {
						d = distance(i * wsr, j * wsr);
						distances[k] = d[2];
						k++;
					}
				}
			}
			life++;
			if (life > maxLife) dead = true;
			for (Wall w : walls) {
				if (w.collision(x, y, radius)) {
					dead = true;
				}
			}
			if (lapCounter.collision(x, y, radius)) {
				if (!collidingWithLapCounter) {
					if (dy < 0) {
						laps--;
						wrongDirection = true;
					}
					else {
						laps++;
						wrongDirection = false;
					}
					collidingWithLapCounter = true;
				}
			}
			else {
				if (collidingWithLapCounter) {
					if (dy < 0 && !wrongDirection) {
						laps--;
						wrongDirection = true;
					}
					if (dy > 0 && wrongDirection) {
						laps++;
						wrongDirection = false;
					}
					collidingWithLapCounter = false;
				}
			}

			boolean triggering = false;
			boolean horizontal = false;
			for (Wall c : checkpoints) {
				if (c.collision(x, y, radius)) {
					triggering = true;
					horizontal = c.horizontal;
				}
			}
			if (triggering) {
				if (!collidingWithCheckpoint) {
					collidingWithCheckpoint = true;
					if (horizontal) {
						touchingHorizontalWall = true;
						if (x < 300) {
							if (dy <= 0) {
								score++;
								wrongDirection = false;
							}
							else {
								score--;
								wrongDirection = true;
							}
						}
						else {
							if (dy < 0) {
								score--;
								wrongDirection = true;
							}
							else {
								score++;
								wrongDirection = false;
							}
						}
					}
					else {
						touchingHorizontalWall = false;
						if (y > 300) {
							if (dx <= 0) {
								score++;
								wrongDirection = false;
							}
							else {
								score--;
								wrongDirection = true;
							}
						}
						else {
							if (dx < 0) {
								score--;
								wrongDirection = true;
							}
							else {
								score++;
								wrongDirection = false;
							}
						}
					}
				}
			}
			else {
				if (collidingWithCheckpoint) {
					collidingWithCheckpoint = false;
					if (touchingHorizontalWall) {
						if (x < 300) {
							if (dy >= 0 && !wrongDirection) {
								score--;
								wrongDirection = true;
							}
							if (dy < 0 && wrongDirection) {
								score++;
								wrongDirection = false;
							}
						}
						else {
							if (dy <= 0 && !wrongDirection) {
								score--;
								wrongDirection = true;
							}
							if (dy > 0 && wrongDirection) {
								score++;
								wrongDirection = false;
							}
						}
					}
					else {
						if (y > 300) {
							if (dx >= 0 && !wrongDirection) {
								score--;
								wrongDirection = true;
							}
							if (dx < 0 && wrongDirection) {
								score++;
								wrongDirection = false;
							}
						}
						else {
							if (dx <= 0 && !wrongDirection) {
								score--;
								wrongDirection = true;
							}
							if (dx > 0 && wrongDirection) {
								score++;
								wrongDirection = false;
							}
						}
					}
					touchingHorizontalWall = false;
				}
			}
		}
	}
	public void draw (Graphics g) {
		if (!dead) {
			g.fillRect((int)(x - radius), (int)(y - radius), (int)(radius * 2), (int)(radius * 2));
		}
	}
	public void brain () {
		//inputs: dx, dy, distances[0] (up-left), distances[1] (up),
		//distances[2] (up-right), distances[3] (left), distances[4] (right)
		//distances[5] (down-left), distances[6] (down), distances[7] (down-right)
		ddx.setInput(0, dx * strengths[0]);
		ddx.setInput(1, dy * strengths[1]);
		ddx.setInput(2, distances[0] * strengths[2]);
		ddx.setInput(3, distances[1] * strengths[3]);
		ddx.setInput(4, distances[2] * strengths[4]);
		ddx.setInput(5, distances[3] * strengths[5]);
		ddx.setInput(6, distances[4] * strengths[6]);
		ddx.setInput(7, distances[5] * strengths[7]);
		ddx.setInput(8, distances[6] * strengths[8]);
		ddx.setInput(9, distances[7] * strengths[9]);
		ddy.setInput(0, dx * strengths[10]);
		ddy.setInput(1, dy * strengths[11]);
		ddy.setInput(2, distances[0] * strengths[12]);
		ddy.setInput(3, distances[1] * strengths[13]);
		ddy.setInput(4, distances[2] * strengths[14]);
		ddy.setInput(5, distances[3] * strengths[15]);
		ddy.setInput(6, distances[4] * strengths[16]);
		ddy.setInput(7, distances[5] * strengths[17]);
		ddy.setInput(8, distances[6] * strengths[18]);
		ddy.setInput(9, distances[7] * strengths[19]);
		ddx.updateOutputs();
		ddy.updateOutputs();
		dx += ddx.getOutput()/5;
		dy += ddy.getOutput()/5;
		//outputs: ddx, ddy
	}
}