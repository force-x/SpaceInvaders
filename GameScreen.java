package Game;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Point;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class GameScreen extends JFrame {
	
	
	BufferedImage screenshot;
	BottomPanel bottomPanel;
	double playerLocation;
	Point bulletLocation;
	
	public GameScreen(BufferedImage img, double player_x) {
		screenshot=img;
		playerLocation=player_x;
		this.setSize(screenshot.getWidth(),screenshot.getHeight()+30);
		bottomPanel=new BottomPanel();
		this.add(bottomPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setLocation(800,0);
		this.setVisible(true);
	}
	
	
	public void updateGameState(BufferedImage img, double player_x, Point bullet) {
		screenshot=img;
		playerLocation=player_x;
		bulletLocation=bullet;
		bottomPanel.paintComponent(bottomPanel.getGraphics());
	}
	
	
	private class BottomPanel extends JPanel{
		
		public void paintComponent(Graphics g) {
			g.drawImage(screenshot,0,0,null);
			g.setColor(Color.BLUE);
			g.drawRect((int)playerLocation,(int)350,30,20);
			
			if(bulletLocation!=null) {
				g.setColor(Color.RED);
				g.drawRect((int)(bulletLocation.x),(int)bulletLocation.y+55,19,27);
			}
		}	
	}

}
