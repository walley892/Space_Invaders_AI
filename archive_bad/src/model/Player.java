package model;



//import model.GameObject.OBJECT_TYPE;

public class Player extends GameObject{
	
	public Player(Board b, int yPos, int xPos){
		setPosition(yPos, xPos);
		_board = b;
	}
	
	public OBJECT_TYPE getType() {
		return OBJECT_TYPE.PLAYER;
	}

	public void moveLeft(){
		if(_position[1] > 2)
		_position[1] -= 1;
	}
	public void moveRight(){
		if(_position[1] < _board.getWidth() -2)
		_position[1] += 1;
	}
	
	public void fire(){
		_board.fire(new Laser(DIRECTION.UP, this));
	}
}
