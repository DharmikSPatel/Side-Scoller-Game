import java.awt.geom.*;
public class Hero{
	private int x, y, ac, count, jc, ground;
	private int[][] locs, jumpLocs;
	private boolean jumping = false, falling = false, onBox= false;
	private int startX, startY;

	public Hero(int x, int y, int[][] locs, int[][] jumpLocs){
		this.x = x;
		this.y = y;
		ground = y;
		this.locs = locs;
		this.jumpLocs = jumpLocs;
		ac = 0;
		onBox = true;
		startX = x;
		startY = y;

	}
	public void reset(){
		x = startX;
		y = startY;
		ground = y;
		ac = 0;
		jc = 0;
		onBox = true;
		jumping = false;
		falling = false;
		count = 0;

	}
	public int getCol(){
		return 0;
	}
	public int getWidth(){
		return jumpLocs[0][2];
	}
	public int getGround(){
		return ground;
	}
	public boolean isJumping(){
		return jumping;
	}
	public boolean isFalling(){
		return falling;
	}
	public void setJump(boolean b){
		jumping = b;
	}
	public void setFall(boolean b){
		falling = b;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getAladdinCount(){
		return ac;
	}
	public void setAladdinCount(int newac){
		this.ac = newac;
		if(ac == 13)
			ac=1;
	}
	public Rectangle2D getCollisionBellow(){
		return new Rectangle2D.Double(x, y+1, locs[jc][2], locs[0][3]);
	}
	public Rectangle2D getCollisionAbove(){
		return new Rectangle2D.Double(x, y-1, locs[jc][2], locs[0][3]);
	}
	public Rectangle2D getCollisionBox(){
		return new Rectangle2D.Double(x, y, locs[ac][2], locs[0][3]);
	}
	public Rectangle2D getSmallCollisionBox(){
		if(falling || jumping)
			return new Rectangle2D.Double(x+20, y+20, 30, 60);
		return new Rectangle2D.Double(x+20, y+10, 10, 80);
	}
	public void zeroJC(){
		jc = 0;
	}
	public int getJumpCount(){
		return jc;
	}
	public void setOnBox(boolean b){
		onBox = b;
	}
	public boolean isOnBox(){
		return onBox;
	}
	public void updateJumping(){
		y--;
		count++;
		if(count%25 == 0){
			jc++;
			if(jc ==6){
				setJump(false);
				setFall(true);
			}
		}
	}
	public void updateFalling(){
		y++;
		count++;
		if(count%25 == 0){
			jc++;
			if(jc ==12){
				jc=0;
			}
		}
	}

}