import java.awt.geom.*;
public class Coin{
	private int x, y, w, h, startX, startY;
	private boolean pickedUp, debug;
	public Coin(int x, int y, int w, int h){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		startX = x;
		startY = y;
		pickedUp = false;
		debug = false;
	}
	public boolean debug(){
		return debug;
	}
	public void debug(boolean b){
		debug = b;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public boolean pickedUp(){
		return pickedUp;
	}
	public void setPickedUp(boolean b){
		pickedUp = b;
	}
	public void update(int changeX){
		if(!pickedUp)
			x+=changeX;
	}
	public void reset(){
		pickedUp = false;
		x = startX;
		y = startY;
	}
	public Rectangle2D getCollisionBox(){
		return new Rectangle2D.Double(x, y, w, h);
	}

}