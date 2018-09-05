//Name: Jubin Patel
//Date: April 1/2012
//Purpose: To create snake and check everything else it is associated with
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Snake
{
	//creating instance variables
	private int x;
	private int y;
	private int moveSpeed = 10;
	private ArrayList tailX;
	private ArrayList tailY;
	private int tailSize = 7;
	private int curKey = -1;
	private int Score = 0, prevScore;
	private final int UP = KeyEvent.VK_UP, DOWN = KeyEvent.VK_DOWN, RIGHT = KeyEvent.VK_RIGHT, LEFT = KeyEvent.VK_LEFT;

	public Snake( int startX, int startY){//constructor

		this.x = startX;
		this.y = startY;
		this.tailX = new ArrayList();
		this.tailY = new ArrayList();
		this.tailX.add(new Integer(x));
		this.tailY.add(new Integer(y));
	}

	public void move( int key ) {//method for moving snake
		//Appending last position into tail
		tailX.add(new Integer(x));
		tailY.add(new Integer(y));
		//If too many positions, remove oldest
		if( tailX.size() > tailSize ){
			tailX.remove(0);
			tailY.remove(0);
		}
		//Make sure movement doesn't collide with self
		if( ( key == UP && curKey != DOWN ) || ( key == DOWN && curKey != UP ) || ( key == RIGHT && curKey != LEFT ) || ( key == LEFT && curKey != RIGHT )  )
		{
			curKey = key;
		}
		//Move in correct direction
		if( curKey == UP ){
			y -= moveSpeed;
		}
		else if( curKey == DOWN ){
			y += moveSpeed;
		}
		else if( curKey == RIGHT ){
			x += moveSpeed;
		}
		else if( curKey == LEFT ){
			x -= moveSpeed;
		}
		//Check collision here
		Collision();
	}
	//Drawing snake and tail
	public void draw( Graphics2D g )   {
		Integer[] tempX = new Integer[tailX.size()];
		Integer[] tempY = new Integer[tailY.size()];
		tailX.toArray(tempX);
		tailY.toArray(tempY);
		for( int i = 0; i < tailX.size(); i++ )
		{
			g.setColor(Color.gray);
			g.fillRect(tempX[i].intValue()-5,tempY[i].intValue()-5,10,10);
		}
		g.setColor(Color.red);
		g.fillRect(x-5,y-5,10,10);
		g.drawString("Score: "+Score , 700,500);
	}



	public void eatFood(Food f)  {//checks to see if the food has be eaten
		Area food = new Area(new Ellipse2D.Double(f.getX()-5,f.getY()-5,10,10));
		Rectangle2D snake = new Rectangle2D.Double(x-5,y-5,10,10);
		if( food.intersects(snake) )//if food has been eaten the snakes grows and score increases and regenerates
		{
			tailSize += 2;
			Score += 10;
			prevScore = Score;
			f.generateFood(this,600);
		}
	}

	public boolean Collision()  {//checking for collision with self and walls
		Integer[] tempX = new Integer[tailX.size()];
		Integer[] tempY = new Integer[tailY.size()];
		tailX.toArray(tempX);
		tailY.toArray(tempY);
		boolean collision = false;
		for( int i = 0; i < tailX.size() && !collision; i++ ){
			collision |= ((tempX[i].intValue() == x && tempY[i].intValue() == y) || ((x == 800 || x == 0) ||(y == 600 ||  y == 0)));
		}
		if( collision ){//if collision has happened everything resets

			x = 350;
			y = 350;
			this.tailX = new ArrayList();
			this.tailY = new ArrayList();
			this.tailX.add(new Integer(x));
			this.tailY.add(new Integer(y));
			tailSize = 7;
			Score = 0;
		}
		return collision;
	}
	//getter methods
	public int getprevScore(){
		return this.prevScore;
	}
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
}
