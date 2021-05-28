import java.awt.geom.*;
public class Arrow{
	private int x,y,w,h;
	private Hero hero;
	private boolean moving;
	public Arrow(Hero hero, int w, int h){
		this.x = 950;
		this.w = w;
		this.h = h;
		this.hero = hero;
		moving = false;
		this.y = getRandY();


	}
	public boolean isMoving(){
		return moving;
	}
	public void setMoving(boolean b){
		moving = b;
	}
	public Rectangle2D getCollisionBox(){
		return new Rectangle2D.Double(x, y, w, h);
	}
	public void update(int changeX){
		if(moving)
			x+=changeX;
			if(x < -50){
				reset(false);
				moving = false;
			}
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getRandY(){
		int max = hero.getY()+150 < 600 ? hero.getY()+150 : 600;
		int min = hero.getY()-100 > 50 ? hero.getY()-100 : 0;
		int range = max - min + 1;
		return (int)(Math.random() * range) + min;
	}
	public void reset(boolean forcedReset){
		if(forcedReset || !moving){
			x = 950;
			y = getRandY();
			moving = true;
		}


	}

}