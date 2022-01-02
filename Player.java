package Game;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

public class Player {
	
	/**True when the program is actively shooting**/
	private volatile boolean keepRunning=true;
		
	/**True when the program is playing**/
	public static boolean keepPlaying=true;
		
	/**Plays the game by dodging bullets and shooting on its own. 
	 * Press 's' to terminate the program
	 * set display to true if you wish to "see" what is happening**/
	public void playGame(boolean display) {
				
		//Get all the templates
				
		//Best player template
		URL url1 = Main.class.getResource("/" + "Player.PNG");
		File playerFile=null;
		try {
			playerFile= new File(Main.tmpDir, "Player.PNG");
			playerFile.deleteOnExit();
			try (InputStream in = url1.openStream()) {
			    Files.copy(in, playerFile.toPath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
		Mat player=Imgcodecs.imread(playerFile.getAbsolutePath(),0);
		Mat best_player=new Mat(20,30,0);
		Imgproc.resize(player,best_player, best_player.size());
				
		//Best nail bullet
		URL url2 = Main.class.getResource("/" + "Bullet1.PNG");
		File Bullet1File=null;
		try {
			Bullet1File= new File(Main.tmpDir, "Bullet1.PNG");
			Bullet1File.deleteOnExit();
			try (InputStream in = url2.openStream()) {
			    Files.copy(in,Bullet1File.toPath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
		Mat nailBullet=Imgcodecs.imread(Bullet1File.getAbsolutePath(),0);
		Mat best_nailBullet=new Mat(26,18,0);
		Imgproc.resize(nailBullet,best_nailBullet, best_nailBullet.size());
		best_nailBullet=best_nailBullet.submat(4,20,5,14);
				
		//best squiggly bullet and flipped squiggly bullet	
		URL url3= Main.class.getResource("/" + "Bullet2.PNG");
		File Bullet2File=null;
		try {
			Bullet2File= new File(Main.tmpDir, "Bullet2.PNG");
			Bullet2File.deleteOnExit();
			try (InputStream in = url3.openStream()) {
			    Files.copy(in, Bullet2File.toPath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
		Mat squigglyBullet=Imgcodecs.imread(Bullet2File.getAbsolutePath(),0);
		Mat best_squigglyBullet=new Mat(28,19,0);
		Imgproc.resize(squigglyBullet,best_squigglyBullet, best_squigglyBullet.size());
		best_squigglyBullet=best_squigglyBullet.submat(4,27,5,17);	
		Mat reverse_squigglyBullet=new Mat(best_squigglyBullet.rows(),best_squigglyBullet.cols(),0);
		Core.flip(best_squigglyBullet,reverse_squigglyBullet,1);

		//best fast bullet
		URL url4 = Main.class.getResource("/" + "Bullet3.PNG");
		File Bullet3File=null;
		try {
			Bullet3File= new File(Main.tmpDir, "Bullet3.PNG");
			Bullet3File.deleteOnExit();
			try (InputStream in = url4.openStream()) {
			    Files.copy(in, Bullet3File.toPath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Mat fastBullet=Imgcodecs.imread(Bullet3File.getAbsolutePath(),0);
		Mat best_fastBullet=new Mat(27,18,0);
		Imgproc.resize(fastBullet,best_fastBullet, best_fastBullet.size());
		best_fastBullet=best_fastBullet.submat(4,21,2,13);

				
		Mat[] bulletTemplates= {best_nailBullet,best_squigglyBullet,reverse_squigglyBullet,best_fastBullet};		
		
		//Wait for 5 seconds
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
				
				
		try {
			Robot robot=new Robot();
			boolean movingRight=true;
			double playerLocation;
			Point bulletLocation;
			Image screenshotImg;
			GameScreen screen=null;
			boolean inDanger=false;
			boolean standingStill=false;
			boolean onLeftEdge=false;
			boolean onRightEdge=false;
			
			/*
			//this allows you to stop playing by pressing 's'
			try {
	            GlobalScreen.registerNativeHook();
	        } catch (NativeHookException ex) {
	            System.err.println("There was a problem registering the native hook.");
	            System.err.println(ex.getMessage());

	            System.exit(1);
	        }

	        GlobalScreen.addNativeKeyListener(new KeyListener());
			*/
			
			//sole purpose of this thread is to shoot
			Thread shootingThread=new Thread() {
				public void run() {
					Robot robot;
					
					System.out.println("start shooting");
					
					while(keepRunning) {
						try {
							robot = new Robot();
							robot.keyPress(88);
							Thread.sleep(30);
							robot.keyRelease(88);
							
							Thread.sleep(75);
						}
						catch(InterruptedException e) {
						e.printStackTrace();
						}
						catch (AWTException a) {
							a.printStackTrace();
						}
					}
					
					System.out.println("shooting stopped");
					
				}
			};
					
			shootingThread.start();
			
					
			//take screenshot of game
			Rectangle rect=new Rectangle(325,225,500,400); //game size: width-500, height-400, x-point-325, y-point-225
			Mat img=GameState.takeScreenshot(rect);
					
			//Find location of player
			playerLocation=GameState.findPlayerLocation(img,best_player).x;
			robot.keyPress(39);
			
			if(display) {
				//display information
				screenshotImg=HighGui.toBufferedImage(img);
				screen=new GameScreen((BufferedImage)screenshotImg,playerLocation);
			}
					
			//sleep for fraction of a second
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
							
			while(keepPlaying){
											
				//take screenshot of game
				img=GameState.takeScreenshot(rect);
						
				//Find location of player, check for bullets, and check for enemies
				playerLocation=GameState.findPlayerLocation(img,best_player).x;
				bulletLocation=GameState.checkForBullets(img,bulletTemplates,playerLocation,movingRight);
				
				
				if(display) {
					//display to screen
					screenshotImg=HighGui.toBufferedImage(img);
					screen.updateGameState((BufferedImage)screenshotImg, playerLocation,bulletLocation);
				}
				
				//see if bullet is present
				if(bulletLocation!=null) {
					inDanger=true;
				}
				else {
					inDanger=false;
				}
			
				//sees if on any edges
				if(playerLocation<=70) {
					onLeftEdge=true;
					onRightEdge=false;
				}
				else if(playerLocation>=400) {
					onLeftEdge=false;
					onRightEdge=true;
				}
				else {
					onLeftEdge=false;
					onRightEdge=false;
				}
				
				
				if(onLeftEdge) {
					//6
					//if on the left edge and there is a bullet over you or no bullet at all
					//then move right otherwise don't move
					if(!inDanger || bulletLocation.x<=playerLocation+35) {
						Player.moveRight(robot);
						movingRight=true;
						//System.out.println(6);
					}
				}
				else if(onRightEdge) {
					//5
					//if on the right edge and there is a bullet over you or no bullet at all
					//then move left otherwise don't move
					if(!inDanger || bulletLocation.x>=playerLocation-5) {
						Player.moveLeft(robot);
						movingRight=false;
						//System.out.println(5);
					}
				}
				else {
					if(!standingStill) {
						//4
						//if you are not on edge and moving right into a bullet then stop moving
						if(movingRight && inDanger && bulletLocation.x>=playerLocation+35) {
							Player.stopMoving(robot);
							standingStill=true;
							//System.out.println(4);
						}
						//3
						//if you are not on edge and moving left into a bullet then stop moving
						else if(!movingRight && inDanger && bulletLocation.x<=playerLocation-5) {
							Player.stopMoving(robot);
							standingStill=true;
							//System.out.println(3);
						}
					}
					else {
						if(movingRight) {
							//2
							//if not on edge, standing still, prev. dir. was right,
							//and no bullet/bullet over you then move right
							if(!inDanger || bulletLocation.x<=playerLocation+35) {
								Player.moveRight(robot);
								standingStill=false;
								//System.out.println(2);
							}
						}
						else {
							//1
							//if not on edge,standing still, prev. dir. was left,
							//and no bullet/bullet over you then move left
							if(!inDanger || bulletLocation.x>=playerLocation-5) {
								Player.moveLeft(robot);
								standingStill=false;
								//System.out.println(1);
							}
						}
					}
					
				}
	
				//sleep for fraction of second
				try {
				Thread.sleep(50);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			
			keepRunning=false;
			robot.keyRelease(39);
			robot.keyRelease(37);
		}
		catch(AWTException awt) {
			awt.printStackTrace();	
		}
				
		
	}
	
	/**Moves right **/
	private static void moveRight(Robot robot) {
		robot.keyRelease(37);
		robot.keyPress(39);
	}
	
	/**Moves left **/
	private static void moveLeft(Robot robot) {
		robot.keyRelease(39);
		robot.keyPress(37);
	}
	
	/**Stops moving **/
	private static void stopMoving(Robot robot) {
		robot.keyRelease(39);
		robot.keyRelease(37);
	}
	
	
}
