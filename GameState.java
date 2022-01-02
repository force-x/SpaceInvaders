package Game;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

public class GameState {

	/**Takes a screenshot of the screen of the area designated by rect **/
	public static Mat takeScreenshot(Rectangle rect) throws AWTException {
				//Takes grayscale screenshot of specified rectangle
				Robot robot;
				Mat mat=new Mat((int)(rect.getHeight()),(int)(rect.getWidth()),0);
				
				try {
					robot = new Robot();
					BufferedImage img=robot.createScreenCapture(rect);
					int[] data=((DataBufferInt)img.getRaster().getDataBuffer()).getData();
					byte[] array=new byte[1];
					int row=0;
					
					//puts in the first entry
					int bit_32=data[0];
					int alpha=(bit_32)&(255);
					int blue=(bit_32>>8)&(255);
					int green=(bit_32>>16)&(255);
					int red=(bit_32>>24)&(255);
					array[0]=(byte) Math.round((0.21*red+0.72*green+0.07*blue));
					mat.put(row,0,array);
					
					
					//puts in the rest of the entries
					for(int j=1; j<data.length;j++) {
						bit_32=data[j];
						blue=(bit_32)&(255);
						green=(bit_32>>8)&(255);
						red=(bit_32>>16)&(255);
						alpha=(bit_32>>24)&(255);
						array[0]=(byte) Math.round((0.21*red+0.72*green+0.07*blue));
						
						if(j%rect.getWidth()==0) {
							row++;
						}
						
						mat.put(row,j%(int)(rect.getWidth()),array);
					}
					
					
				} catch (AWTException e) {
				e.printStackTrace();
				}
				
				return mat;		
	}
	
	/**Returns information associated with the best instance of a template 
	 * in an image (i.e. maxVal,maxLoc, etc.) **/
	private static MinMaxLocResult findBestInstance(Mat img, Mat template) {
		
		//Matches template to image
		Mat result= new Mat();
		Imgproc.matchTemplate(img, template, result,5);
			
		MinMaxLocResult max_min=Core.minMaxLoc(result);
			
		//Prints out max value
		//System.out.println(max_min.maxVal);
			
		return max_min;
	}
	
	/**Finds instances of a template in the given img.
	 * Higher threshold means only better matches are accepted.
	 * Draws them to the screen if draw is set to true **/
	private static void findInstances(Mat img, Mat template, double threshold, boolean draw) {
				
		//Matches template to image
		Mat result= new Mat();
		Imgproc.matchTemplate(img, template, result,5);
		
		//Cycles through results to find places with highest values
		//value of highest match=1
		ArrayList<Point> targetPoints=new ArrayList<Point>();
		for (int i=0;i<result.cols();i++) {
			for (int k=0; k<result.rows();k++) {
				if (result.get(k,i)[0]>=threshold) {
					targetPoints.add(new Point(i,k));
				}
			}
		}
		
		if(draw) {
			//draws all the points
			Scalar color=new Scalar(255,255,255);
			for (Point point: targetPoints) {
				Point oppositeCorner=new Point(point.x+template.cols(),point.y+template.rows());
				Imgproc.rectangle(img,point,oppositeCorner,color,1);
			}
			
			HighGui.imshow("Template Match", img);
			HighGui.waitKey();
			System.exit(0);
		}
		
	}
	
	/**finds the optimal template size of a template that will produce 
	 * the best match by repeatedly shrinking it
	 * Threshold denotes the level of match you wish to achieve
	 * minSize denotes the minimum dimension of the image you wish to have **/
	private static void findCorrectTemplateSize(Mat img, Mat template, double threshold,double scalingFactor, int minSize) {
		
		
		Mat result= new Mat();
		double matchValue;
		double bestValue;
		int bestRows;
		int bestCols;
		Mat intermediate;
		
		Imgproc.matchTemplate(img, template, result,5);
		MinMaxLocResult max_min=Core.minMaxLoc(result);
		matchValue=max_min.maxVal;
		bestValue=matchValue;
		bestRows=template.rows();
		bestCols=template.cols();
		
		while(bestValue<=threshold && Math.min(template.rows(),template.cols())>=minSize){
			
			//Create new mat that is 95% smaller
			intermediate=new Mat((int)(0.95*template.rows()),(int)(0.95*template.cols()),0);
			Imgproc.resize(template,intermediate,intermediate.size());
			//Imgproc.resize(template,intermediate, new Size(),0.95,0.95,Imgproc.INTER_AREA); //Old line
			template=intermediate;
			
			//Match this new resized image against screenshot
			Imgproc.matchTemplate(img, template, result,5);
			max_min=Core.minMaxLoc(result);
			matchValue=max_min.maxVal;
			
			if (matchValue>bestValue) {
				bestValue=matchValue;
				bestRows=template.rows();
				bestCols=template.cols();
			}
			
		}

		System.out.println("Best rows: "+bestRows);
		System.out.println("Best cols: "+bestCols);
		System.out.println("Best value: "+bestValue);

		findInstances(img,template,threshold,true);
	}
		
	/**Return point of bullet. Null if there is no bullet. **/
	public static Point checkForBullets(Mat img, Mat[] bulletTemplates, double location, boolean movingRight) {
		int leftPadding=0;
		int rightPadding=0;
		Mat bullet_img=null;
		
		if(movingRight) {
			//find area to take submatrix of screenshot
			leftPadding=5;
			rightPadding=105;
			bullet_img=img.submat(55,390,(int)Math.max(location-leftPadding,0),(int)Math.min(location+rightPadding,img.cols()-1));				
		}
		else {
			//find area to take submatrix of screenshot
			leftPadding=75;
			rightPadding=35;
			bullet_img=img.submat(55,390,(int)Math.max(location-leftPadding,0),(int)Math.min(location+rightPadding,img.cols()-1));
		}
	
		 
		MinMaxLocResult max_min_nail=findBestInstance(bullet_img,bulletTemplates[0]);		
		MinMaxLocResult max_min_squiggly=findBestInstance(bullet_img,bulletTemplates[1]);		
		MinMaxLocResult max_min_squiggly_reverse=findBestInstance(bullet_img,bulletTemplates[2]);
		MinMaxLocResult max_min_fast=findBestInstance(bullet_img,bulletTemplates[3]);

		if(max_min_nail.maxVal>=0.9) {
			max_min_nail.maxLoc.x=Math.max(location-leftPadding,0)+max_min_nail.maxLoc.x;
			return max_min_nail.maxLoc;
		}
		else if(max_min_squiggly.maxVal>=0.7) {
			max_min_squiggly.maxLoc.x=Math.max(location-leftPadding,0)+max_min_squiggly.maxLoc.x;
			return max_min_squiggly.maxLoc;
		}
		else if(max_min_squiggly_reverse.maxVal>=0.7) {
			max_min_squiggly_reverse.maxLoc.x=Math.max(location-leftPadding,0)+max_min_squiggly_reverse.maxLoc.x;
			return max_min_squiggly_reverse.maxLoc;
		}
		else if(max_min_fast.maxVal>=0.85) {
			max_min_fast.maxLoc.x=Math.max(location-leftPadding,0)+max_min_fast.maxLoc.x;
			return max_min_fast.maxLoc;
		}
		else {
			return null;
		}
	}
	
	/**Returns the point of the player location **/
	public static Point findPlayerLocation(Mat img, Mat player) {
				
				//Look only in region where player is located
				Mat player_img=img.submat(350,390,0,img.cols()-1);
				
				//find the location of the player
				Point point=findBestInstance(player_img,player).maxLoc;
				
				//adjust y-coordinate for screen
				point.y=point.y+350;
				
				return point;
	}
}
