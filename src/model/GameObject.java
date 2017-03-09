package model;

public abstract class GameObject {
	public enum DIRECTION {UP, DOWN, LEFT, RIGHT};
	public enum OBJECT_TYPE {ENEMY, PLAYER, LASER};
	protected int[] _position;
	protected Board _board;
	public abstract OBJECT_TYPE getType();
	
	
	protected void setPosition(int x, int y) {
		_position = new int[2];
		_position[0] = x;
		_position[1] = y;
	}
	
	public int[] getPosition(){
		return _position;
	}
	public void move(){
		
	}

}
