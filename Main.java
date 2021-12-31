import org.opencv.core.*;

public class Main {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	/**Enter argument true if you would like to display what the computer "sees" **/
	public static void main(String[] args) {
		
		Player player=new Player();
		
		if(args.length!=0 && args[0].equals("true")) {
			player.playGame(true);
		}
		else {
			player.playGame(false);
		}
		
		
	}
}