import java.awt.geom.*;
public class Block{
	private int x, y, w, h;
	private int startX, startY;
	private boolean debug;
	public Block(int x, int y, int w, int h){
		this.x = x;
		this.y = y;
		startX = x;
		startY = y;
		this.w = w;
		this.h = h;
		debug = false;

	}
	public void reset(){
		x = startX;
		y = startY;
		debug = false;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public void updateX(int n){
		x+=n;
	}
	public Rectangle2D getCollisionBox(){
		return new Rectangle2D.Double(x, y, w, h);
	}
	public void setDebug(boolean b){
		debug = b;
	}
	public boolean debug(){
		return debug;
	}
	public String toString(){
		return x + " " + y + " " + w + " " + h;
	}
}