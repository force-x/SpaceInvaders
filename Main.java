package Game;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

public class Main {
	
	public static File tmpDir;
	
	
	public static void main(String[] args) {
		

		String libName = "opencv_java453.dll"; // The name of the file in resources/ dir
		URL url = Main.class.getResource("/" + libName);
		try {
			tmpDir = Files.createTempDirectory("my-native-lib").toFile();
			tmpDir.deleteOnExit();
			File nativeLibTmpFile = new File(tmpDir, libName);
			nativeLibTmpFile.deleteOnExit();
			try (InputStream in = url.openStream()) {
			    Files.copy(in, nativeLibTmpFile.toPath());
			}
			System.load(nativeLibTmpFile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		
		Player player=new Player();
		player.playGame(true);
	}
}