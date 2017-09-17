package model;
import view.GUI;
import java.time.Instant;

public class Main {
	private static long COOLDOWN = 10;
	private static int gameMode;

	public static void main(String[] args) throws InterruptedException {
		try{
			gameMode = Integer.valueOf(args[1]);
		} catch(IndexOutOfBoundsException e) {
			gameMode = 1;	
		}
		GUI gui = new GUI();
		gui.generateUI(gameMode);
		Instant nextUpdate = Instant.now();
		while(true){
			if(Instant.now().isAfter(nextUpdate)){
				gui.update();
				nextUpdate = Instant.now().plusMillis(COOLDOWN);
			}
		}
	}
	public static void setCooldown(long cooldown){
		COOLDOWN = cooldown;
	}
}