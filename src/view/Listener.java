package view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import model.Board;
import model.Player;

public class Listener implements KeyListener{
	private Player _player;
	
	public Listener(Player p, Board b){
		_player = p;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE)
			_player.fire();
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
			_player.moveLeft();
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			_player.moveRight();
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		
	}
	

}
