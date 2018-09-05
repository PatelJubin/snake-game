//Name: Jubin Patel
//Date: April 1/2012
//Purpose: to create food for the snake to eat
import java.awt.Color;
import java.awt.Graphics2D;


public class Food
{
	//instance variables
	private int x,y;
	private final static double THRESHOLD_DISTANCE = 450;//make sure the food doesn't appear on the snake
	private long lastFoodTime;
	public Food(Snake s, int size)//constructor
	{
		generateFood(s,size);
	}

	public void generateFood(Snake s, int size){//generates food for the snake to eat in random places
		//Do not generate more than 5 times
		int calcTimes = 0;
		do{
			calcTimes++;
			x = 100+(int)(Math.random()*1000)%400;
			y = 100+(int)(Math.random()*1000)%400;
		}while( Math.sqrt( Math.pow(x-s.getX(),2)+ Math.pow(y -s.getY(), 2)) < THRESHOLD_DISTANCE  && calcTimes < 5);
		lastFoodTime = System.currentTimeMillis();
				
		
	}

	public void autoGenerate(Snake s){//the food will appear somewhere else if it hasn't been eaten in certain time
		if( (System.currentTimeMillis() - lastFoodTime) > 12000 )
		{
			this.generateFood(s,600);
			lastFoodTime = System.currentTimeMillis();
		}
	}

	public void drawFood(Graphics2D g){//drawing the food
		g.setColor(Color.yellow);
		g.fillOval(x-5, y-5, 10, 10);
	}
	//getters methods
	public int getX(){
		return this.x;
	}
	public int getY(){
		return this.y;
	}

}
