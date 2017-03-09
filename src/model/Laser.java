package model;

public class Laser extends GameObject{
	private DIRECTION _direction;
	private GameObject _source;
	
	public Laser(DIRECTION d, GameObject source){
		_direction = d;
		setPosition(source.getPosition()[0], source.getPosition()[1]);
		_source = source;
		
	}
	@Override
	public void move(){
		
		switch(_direction){
		case DOWN:
			_position[0] += 10;
		case LEFT:
			break;
		case RIGHT:
			break;
		case UP:
			_position[0] -= 10;
			break;
		default:
			break;
		
		}
	}
	
	@Override
	public OBJECT_TYPE getType() {
		return OBJECT_TYPE.LASER;
	}
	
	public GameObject getSource(){
		return _source;
	}
	

}
