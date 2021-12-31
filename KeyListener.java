import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class KeyListener implements NativeKeyListener {

    public void nativeKeyPressed(NativeKeyEvent nke) {

    	if(nke.getKeyCode()==nke.VC_S) {
    		Player.keepPlaying=false;
    		System.out.println("Stopping Playing");
    		try {
				GlobalScreen.unregisterNativeHook();
			} catch (NativeHookException e) {
				e.printStackTrace();
			}
    	}
    } 
	
}
