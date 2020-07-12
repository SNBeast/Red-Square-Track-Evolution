import java.awt.Graphics;
public class Wall {
	public final int x;
	public final int y;
	public final int width;
	public final int depth;
	public final boolean horizontal;
	public Wall (int x, int y, int width, int depth, boolean horizontal) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.depth = depth;
		this.horizontal = horizontal;
	}
	public boolean collision (double centerX, double centerY, double radius) {
		return horizontalCollision(centerX, radius) && verticalCollision(centerY, radius);
	}
	private boolean horizontalCollision (double centerX, double radius) {
		return (centerX - radius < x + width && centerX - radius > x) || (centerX + radius > x && centerX + radius < x + width);
	}
	private boolean verticalCollision (double centerY, double radius) {
		return (centerY - radius < y + depth && centerY - radius > y) || (centerY + radius > y && centerY + radius < y + depth);
	}
	public void draw (Graphics g) {
		g.fillRect(x, y, width, depth);
	}
}