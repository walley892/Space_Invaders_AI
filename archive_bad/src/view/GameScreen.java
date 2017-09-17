package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import model.*;

@SuppressWarnings("serial")
public class GameScreen extends JPanel{
	private Board _board;
	private Player _player;
	public GameScreen(Board b){
		_board = b;
		_player = b.getPlayer();
		setPreferredSize(new Dimension(_board.getWidth(), _board.getHeight()));
	}
	
	protected void paintComponent(Graphics g) {
		GameObject[][] board = _board.getBoard();
        super.paintComponent(g);
        for(int i = 0; i < _board.getHeight(); ++i){
        	for(int j = 0; j < _board.getWidth(); ++j){
        		if(board[i][j] == null){
        			g.setColor(Color.BLACK);
        			g.drawRect(j, i, 1, 1);
        		}
        	}
        }
        for(int i = 0; i < _board.getHeight(); ++i){
        	for(int j = 0; j < _board.getWidth(); ++j){
        		if(board[i][j] == null){
        		}else if(board[i][j].getType() == GameObject.OBJECT_TYPE.ENEMY){
        			g.setColor(Color.GREEN);
        			g.drawRect(j, i, 5, 5);
        		}else if(board[i][j].getType() == GameObject.OBJECT_TYPE.LASER){
        			g.setColor(Color.GREEN);
        			g.drawRect(j, i, 1, 1);
        		}
        	}
        }
        
        g.setColor(Color.RED);
        g.drawRect(_player.getPosition()[1], _player.getPosition()[0], 5, 5);
        
        g.setColor(Color.green);
        g.drawString("Score: " + _board.getScore(), 10, 10);
    }  
}
