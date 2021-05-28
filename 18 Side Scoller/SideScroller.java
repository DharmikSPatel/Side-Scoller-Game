import javax.swing.JPanel;

import java.awt.*;

import javax.swing.JFrame;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.event.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.geom.*;


public class SideScroller extends JPanel implements KeyListener, Runnable{
    JFrame frame;
    BufferedImage aladdinSheet, smallCity, bigCity, clouds, sky, blockImage, arrowImage, coinImage;
    BufferedImage[] aladdin = new BufferedImage[13];
    BufferedImage[] jumping = new BufferedImage[12];
    int count = 0, sCount = 0, bCount = 0, cCount = 0, scale = 2, score = 0;
    boolean right = false;
    Thread timer;
	Hero hero;
	HashMap<Integer, ArrayList<Block>> map = new HashMap<Integer, ArrayList<Block>>();
	HashMap<Integer, ArrayList<Coin>> coins = new HashMap<Integer, ArrayList<Coin>>();
	GraphicsEnvironment ge;
	Font aFont;

	ArrayList<Arrow> arrows = new ArrayList<Arrow>();
    public SideScroller(){
        frame = new JFrame("Aladdin");
        frame.add(this);
        frame.addKeyListener(this);
        try {
			ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			aFont = Font.createFont(Font.TRUETYPE_FONT, new File("aladdin.ttf"));
			ge.registerFont(aFont);
            aladdinSheet = ImageIO.read(new File("Aladdin.png"));
            smallCity = ImageIO.read(new File("smallCity.png"));
            bigCity = ImageIO.read(new File("bigCity.png"));
            clouds = ImageIO.read(new File("clouds.png"));
            sky = ImageIO.read(new File("sunset.png"));
            blockImage = ImageIO.read(new File("box.png"));
			arrowImage = ImageIO.read(new File("arrow.png"));
			coinImage = ImageIO.read(new File("coin.png"));
			blockImage = resize(blockImage, blockImage.getWidth(), blockImage.getHeight());
            File file = new File("map2.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            int height = 0; //height
            while((line = br.readLine()) != null){
				String[] parts = line.split("");
				for(int i = 0; i < parts.length; i++){
					if(parts[i].equals("b")){
						if(!map.containsKey(i))
							map.put(i, new ArrayList<Block>());
						map.get(i).add(new Block(50*i, 50*height+(height != 11 ? -7 : 0), 50, 50));
					}
					else if(parts[i].equals("c")){
						if(!coins.containsKey(i))
							coins.put(i, new ArrayList<Coin>());
						coins.get(i).add(new Coin(50*i+10, 50*height+(height != 11 ? -7 : 0), 30, 30));
					}
				}
				height++;
			}
        } catch (IOException|FontFormatException e) {
            System.out.println("ERROR");
        }
        int[][] locsAndDims = new int[][]{
            {337, 3,23,53},
            {  4,64,31,53},
            { 34,64,31,51},
            { 62,64,31,51},
            { 92,64,31,51},
            {127,64,37,51},
            {166,64,31,51},
            {205,64,31,51},
            {233,64,30,51},
            {263,61,30,56},
            {292,61,34,56},
            {325,60,41,56},
            {365,60,36,56}
        };
        int[][] jumpLocs = new int[][]{
			{  4,294,31,59},//jumping
			{ 35,300,29,58},
			{ 62,301,38,56},
			{100,301,36,56},
			{140,303,41,50},
			{183,304,49,47},
			{230,303,42,50},//falling
			{278,302,37,54},
			{321,303,33,56},
			{  4,363,35,64},
			{ 42,365,36,63},
			{168,361,25,55}
		};

        for(int x = 0; x < 13; x++){
            aladdin[x] = aladdinSheet.getSubimage(locsAndDims[x][0], locsAndDims[x][1], locsAndDims[x][2], locsAndDims[x][3]);
        	locsAndDims[x][2]*=scale; //scale = 2
        	locsAndDims[x][3]*=scale;
        	aladdin[x] = resize(aladdin[x], locsAndDims[x][2], locsAndDims[x][3]);
        }
        for(int x = 0; x < 12; x++){
            jumping[x] = aladdinSheet.getSubimage(jumpLocs[x][0], jumpLocs[x][1], jumpLocs[x][2], jumpLocs[x][3]);
			jumpLocs[x][2]*=scale;
        	jumpLocs[x][3]*=scale;
        	jumping[x] = resize(jumping[x], jumpLocs[x][2], jumpLocs[x][3]);
        }
		hero = new Hero(100, 300, locsAndDims, jumpLocs);
		arrows.add(new Arrow(hero, arrowImage.getWidth(), arrowImage.getHeight()));
        frame.setSize(900, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        timer = new Thread(this);
        timer.start();
    }
    public BufferedImage resize(BufferedImage b, int w, int h){
		Image temp = b.getScaledInstance(w, h, Image.SCALE_SMOOTH);
		BufferedImage scaledV = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = scaledV.createGraphics();
		g.drawImage(temp, 0, 0, null);
		g.dispose();
		return scaledV;
	}
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g.drawImage(sky, 0, 0, this);
        g.drawImage(clouds, cCount, 30, this);
        g.drawImage(clouds, cCount+1920, 30, this);
        g.drawImage(bigCity, bCount, 90, this);
        g.drawImage(bigCity, bCount+1920, 90, this);
        g.drawImage(smallCity, sCount, -40, this);
        g.drawImage(smallCity, sCount+1920, -40, this);
		map.forEach((col, blocks) -> {
				for(Block b : blocks)
					if(b.getX() > -50 && b.getX() < 1000)
						g.drawImage(blockImage, b.getX(), b.getY(), this);
		});
		coins.forEach((col, coinsz) -> {
				for(Coin c : coinsz)
					if(c.getX() > -50 && c.getX() < 1000 && !c.pickedUp())
						g.drawImage(coinImage, c.getX(), c.getY(), this);
		});
		arrows.forEach(a -> {
			if(a.isMoving())
				g.drawImage(arrowImage, a.getX(), a.getY(), this);
		});

        if(hero.isJumping() || hero.isFalling())
        	g.drawImage(jumping[hero.getJumpCount()], hero.getX(), hero.getY(), this);
        else
        	g.drawImage(aladdin[hero.getAladdinCount()], hero.getX(), hero.getY(), this);
		g2.setColor(Color.WHITE);
		g2.setFont(aFont.deriveFont(40f));
		g.drawImage(coinImage, 30, 34, this);
		g2.drawString(score+"", 72, 57);
		//g2.draw(hero.getSmallCollisionBox());
    }
    public static void main(String[] args) {
        SideScroller app = new SideScroller();
    }
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == 39){
            right = true;
        }
        if(e.getKeyCode() == 32){
			if(!hero.isJumping() && !hero.isFalling()){
				hero.setJump(true);
				hero.setOnBox(false);
			}
		}
		if(e.getKeyCode() == 82){  //R
			reset();
		}
    }
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == 39){
            right = false;
            hero.setAladdinCount(0);
        }
    }
    public void keyTyped(KeyEvent e) {

    }
    public boolean hitBellow(){
		int col = (int)(count)/50+2;
		for(Block b : map.getOrDefault(col, new ArrayList<Block>())){
			if(hero.getCollisionBellow().intersects(b.getCollisionBox()))
				return true;
		}
		for(Block b : map.getOrDefault(col+1, new ArrayList<Block>())){
			int heroW = hero.getX()+hero.getWidth();
			int boxX = b.getX();
			if(hero.getX()+45 > boxX){
				if(hero.getCollisionBellow().intersects(b.getCollisionBox())){
					return true;
				}
			}
		}
		return false;
	}
	public boolean hitAbove(){
		int col = (int)(count)/50+2;
		for(Block b : map.getOrDefault(col, new ArrayList<Block>())){
			if(hero.getCollisionAbove().intersects(b.getCollisionBox())){
				return true;
			}
		}
		for(Block b : map.getOrDefault(col+1, new ArrayList<Block>())){
			int heroW = hero.getX()+hero.getWidth();
			int boxX = b.getX();
			if(hero.getX()+45 > boxX){
				if(hero.getCollisionAbove().intersects(b.getCollisionBox())){
					return true;
				}
			}
		}
		return false;
	}
    public boolean hit(){
		int col = (int)(count)/50+2;
		for(int i = 0; i < 2; i++)
			for(Block b : map.getOrDefault(col+i, new ArrayList<Block>())){
				if(hero.getCollisionBox().intersects(b.getCollisionBox()))
					return true;
			}
		return false;
	}
	public boolean hitArrow(){
		for(Arrow a : arrows)
			if(hero.getSmallCollisionBox().intersects(a.getCollisionBox()))
				return true;
		return false;
	}
    public boolean hitCoin(){
		int col = (int)(count)/50+2;
		for(int i = 0; i < 2; i++)
			for(Coin c : coins.getOrDefault(col+i, new ArrayList<Coin>())){
				if(hero.getCollisionBox().intersects(c.getCollisionBox()) && !c.pickedUp()){
					score++;
					c.setPickedUp(true);
					return true;
				}
			}
		return false;
	}
	public void reset(){
		hero.reset();
		arrows.forEach((a)->{a.reset(true); a.setMoving(false);});
		map.forEach((k,v)->v.forEach((b)->b.reset()));
		coins.forEach((k,v)->v.forEach((c)->c.reset()));
		count = 0;
		sCount = 0;
		bCount = 0;
		cCount = 0;
		scale = 2;
		score = 0;
    	right = false;
	}
    public void run() {
        while(true){
			if(hitCoin())
				arrows.forEach(a -> a.reset(false));
			if(hero.isJumping()){
				boolean hitAbove = hitAbove();
				if(!hitAbove){
					hero.updateJumping();
				}
				else{
					hero.setFall(true);
					hero.setJump(false);
				}
			}
			else if(hero.isFalling()){
				boolean hitBellow = hitBellow();
				if(!hitBellow){
					hero.updateFalling();
					if(hero.getY() > 700){
						reset();
					}
				}
				else{
					hero.setFall(false);
					if(hitBellow)
						hero.setOnBox(true);
					hero.zeroJC();
				}
			}
			else if(hero.isOnBox()){
				boolean hitBlock = hitBellow();
				if(!hitBlock){
					hero.setFall(true);
					hero.setOnBox(false);
				}

			}
            if(right){
				if(!hit()){
					sCount--;
					if(sCount == -1925)
						sCount = 0;
					count++;
					if(count%15 == 0)
						hero.setAladdinCount(hero.getAladdinCount()+1);
					if(count%2==0)
						bCount--;
					if(bCount == -1925)
						bCount = 0;
					if(count%10==0)
						cCount--;
					if(cCount == -1925)
						cCount = 0;
					map.forEach((k,v)->v.forEach((b)->b.updateX(-1)));
					coins.forEach((k,v)->v.forEach((c)->c.update(-1)));
				}
				arrows.forEach(a -> a.update(-1));
            }
			arrows.forEach(a -> a.update(-2));
			if(hitArrow()){
			   	try{
					timer.sleep(500);
				}catch(InterruptedException e){
					System.out.println("Timer Error");
				}
				reset();
			}
            try{
                timer.sleep(3);
            }catch(InterruptedException e){
                System.out.println("Timer Error");
            }
            repaint();
        }
    }
}