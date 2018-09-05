//Name: Jubin Patel
//Date: April 1/2012
//Purpose: Controls everything
import java.awt.*;
import java.awt.image.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;


public class ControlPanel extends JPanel implements Runnable,KeyListener{
	private MainPanel main;
	private final static int maxWidth = 800, maxHeight = 600; //width & height of JPanel
	private final static int menu = 0;
	private final static int Alive =0,Dead=1;
	private final int UP = KeyEvent.VK_UP, DOWN = KeyEvent.VK_DOWN, RIGHT = KeyEvent.VK_RIGHT, LEFT = KeyEvent.VK_LEFT, ESC = KeyEvent.VK_ESCAPE;
	private int curKey = -1;
	private boolean running; //keeps track of state of the program
	private BufferedImage title,outro;
	private Thread thread;
	private Graphics2D graphics;
	private Image image; //used for double buffering
	private BufferedImage bgImage;
	private Snake snake;
	private Food fruit;
	private int key,menuState, gameState;

	public ControlPanel(MainPanel main) {
		this.setDoubleBuffered(false); //we'll use our own double buffering method
		this.setBackground(Color.black);
		this.setPreferredSize(new Dimension(maxWidth, maxHeight));
		this.setFocusable(true);
		this.requestFocus();            
		addKeyListener(this);
		this.main = main;
	}

	public void keyPressed(KeyEvent e){
		key = e.getKeyCode();
		if (curKey != ESC && key == ESC)
			curKey = ESC;
		if ((curKey != LEFT ||curKey != RIGHT || curKey != DOWN || curKey != UP) && (key == LEFT || key == RIGHT || key == UP || key == DOWN))
			gameState = Alive;
	}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}

	public static void main(String[] args) {
		new MainPanel();
	}

	public void addNotify() {
		super.addNotify();
		startGame();
	}

	public void stopGame() {
		running = false;
	}

	//Creates a thread and starts it
	public void startGame() {
		if (thread == null || !running) {
			thread = new Thread(this);
		}
		thread.start(); //calls run()
	}
	public static void drawBackground(Graphics2D g){
		g.setColor(Color.black);
		g.fillRect(0,0,700,700);
	}

	public void run() {
		running = true;
		init();
		while (running && curKey != ESC ) {
			createImage(); //creates image for double buffering 
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			} //Sleeps as level progresses reduce sleep time to increase speed
			draw();
			update();
			//////////////////////////////////////////////////////
			drawImage(); //draws on the JPanel
		}
		createImage();
		graphics.drawImage(outro,0,0,null);
		drawImage();
		try{ 
			thread.sleep(2000);
		}catch(Exception e){}
		System.exit(0);
	}

	//Use this to create or initialize any variables or objects you have
	public void init() {
		snake = new Snake(350,650);
		fruit = new Food(snake,600);
		try {
			title = ImageIO.read(new File("title.jpg"));//reading in a image
			outro = ImageIO.read(new File("outro.jpg"));
			

		} catch (IOException e) {
			e.printStackTrace();
		}
		menuState = menu;
		gameState = Dead;
	}

	//Use this to update anything you need to update
	public void update() {//updating for movement and collision detection
		fruit.autoGenerate(snake);
		snake.eatFood(fruit);
		snake.move(key);
			
	}

	//Use this to draw anything you need to draw
	public void draw() {
		drawBackground(graphics);
		if(gameState == Dead)//draws menu if game isn't running
			menu();
		if (gameState == Alive ){// runs game 
			snake.draw(graphics);
			fruit.drawFood(graphics);
			}
		if (snake.Collision() == true){//checks if collision is true and draws menu
			gameState = Dead;
			menu();}
	
	}
	public void menu(){//creating menu
		if (menuState == menu){
			graphics.drawImage(title,0,0,null);
			graphics.setColor(Color.black);
			RoundRectangle2D Play = new RoundRectangle2D.Double(340, 420, 170, 50, 20, 17);
			graphics.fill(Play);
			graphics.draw(Play);
			graphics.setColor(Color.white);
			graphics.drawString("Press Any ArrowKey to Begin", 345,450);
			graphics.setColor(Color.black);
			RoundRectangle2D Quit = new RoundRectangle2D.Double(340, 500, 170, 50, 20, 17);
			graphics.fill(Quit);
			graphics.draw(Quit);
			graphics.setColor(Color.white);
			graphics.drawString("Press Esc To Exit", 375,529);
			graphics.setColor(Color.white);
			graphics.drawString("YOUR PREVIOUS SCORE WAS: "+snake.getprevScore(), 50, 500);}
	}
	
	
	//creates an image for double buffering
	public void createImage() {
		if (image == null) {
			image = createImage(maxWidth, maxHeight);

			if (image == null) {
				System.out.println("Cannot create buffer");
				return;
			}
			else
				graphics = (Graphics2D)image.getGraphics(); //get graphics object from Image
		}

		if (bgImage == null) {
			graphics.setColor(Color.black);
			graphics.fillRect(0, 0, maxWidth, maxHeight);
			//System.out.println("No background image");
		}
		else {
			//draws a background image on the Image
			graphics.drawImage(bgImage, 0, 0, null);
		}
	}

	//outputs everything to the JPanel
	public void drawImage() {
		Graphics g;
		try {
			g = this.getGraphics(); //a new image is created for each frame, this gets the graphics for that image so we can draw on it
			if (g != null && image != null) {
				g.drawImage(image, 0, 0, null);
				g.dispose(); //not associated with swing, so we have to free memory ourselves (not done by the JVM)
			}
			image = null;
		}catch(Exception e) {System.out.println("Graphics objects error");}
	}

	



}
