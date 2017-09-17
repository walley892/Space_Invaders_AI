package model;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class Board {
	public static final int _width = 300;
	private static final int _height = 200;
	private static final int ENEMIES_PER_ROW = 20;
	private static final int ENEMY_ROWS = 4;
	private static final int COLLIDE_DISTANCE = 10;
	private int _score;
	private int _round;
	private ArrayList<Enemy> _enemies;
	private Player _player;
	private ArrayList<GameObject> _nonPlayerObjects;
	private ArrayList<Laser> _lasersToAdd;
	private GameObject[][] _board;
	private int _gameMode;
	
	public Board(int gameMode){
		_enemies = new ArrayList<Enemy>();
		_gameMode = gameMode;
		_score = 0;
		_round = 1;
		_nonPlayerObjects = new ArrayList<GameObject>();
		_lasersToAdd = new ArrayList<Laser>();
		if(gameMode ==0)
			_player = new Player(this, _width -20, _height/2);
		else
			_player = new RobotPlayer(this, _height -20, _width/2);
		placeEnemies();
		update();
	}
	
	public void update(){
		_board = new GameObject[_height][_width];
		_board[_player.getPosition()[0]][_player.getPosition()[1]] = _player;
		if(_gameMode != 0){
			_player.move();
		}
		ArrayList<GameObject> removed = new ArrayList<GameObject>();
		for(GameObject o: _nonPlayerObjects){
			try{
				o.move();
				if(_board[o.getPosition()[0]][o.getPosition()[1]] == null)
					_board[o.getPosition()[0]][o.getPosition()[1]] = o;
				if(o.getType() == GameObject.OBJECT_TYPE.LASER){
					Laser l = (Laser) o;
					if(detectCollide(l) != null){
						removed.add(detectCollide(l));
						removed.add(l);
						if(detectCollide(l) == _player){
							changeScore(-200);
							removed.addAll(_nonPlayerObjects);
							_enemies.clear();
						}else{
							changeScore(20);
							_enemies.remove(detectCollide(l));
						}
					}
				}
			} catch(ArrayIndexOutOfBoundsException e){
				removed.add(o);
			} catch (ConcurrentModificationException e){}
		}
		_nonPlayerObjects.removeAll(removed);
		_enemies.removeAll(removed);
		_nonPlayerObjects.addAll(_lasersToAdd);
		_lasersToAdd.clear();
		if(_enemies.size() == 0){
			//++_round;
			_player.setPosition(_height-20, _width/2);
			placeEnemies();
		}
		
	}
	
	private void changeScore(int i) {
		_score += i;
		
	}

	public int getScore(){
		return _score;
	}

	public ArrayList<GameObject> getNonPlayerObjects(){
		return _nonPlayerObjects;
	}
	private GameObject detectCollide(GameObject o) {
		int yPos  = o.getPosition()[0];
		int xPos = o.getPosition()[1];
		
		for(int i = - COLLIDE_DISTANCE/2; i < COLLIDE_DISTANCE/2; ++i){
			for(int j = - COLLIDE_DISTANCE/2; j < COLLIDE_DISTANCE/2; ++j){
				try{
					if(_board[yPos+i][xPos+j] != null){
						if(_board[yPos+i][xPos+j].getType() != o.getType()){
							Laser l = (Laser) o;
							if(l.getSource().getType() != _board[yPos+i][xPos+j].getType()){
								return _board[yPos+i][xPos+j];
							}
						}
					}
				}catch(IndexOutOfBoundsException e){
					
				}
			}
		}
		
		
		return null;
	}

	public void placeEnemies(){
		for(int j = 0; j < ENEMY_ROWS; ++j){
		for(int i = 0; i < ENEMIES_PER_ROW; ++i){
				float x = i;
				
				//x = x * (WIDTH/3) / (ENEMIES_PER_ROW /ENEMY_ROWS);
				//if(x %2 == 0) x = x/2;
				//System.out.println(x);
				x = map(x, 0, ENEMIES_PER_ROW/ENEMY_ROWS, 30, _height - 30);
				int y = (int) map(j, 0, ENEMY_ROWS, 20, 50);
				if(j%2==0){
					x = (map(i+1, 0, ENEMIES_PER_ROW/ENEMY_ROWS, 30, _height - 30) + x) /2; 
				}
				
				if (x < Board._height){
				Enemy e = new Enemy(this, y,(int) x, _round);
				_nonPlayerObjects.add(e);
				_enemies.add(e);
				}
		}
		}
	}
	
	public void printBoard(){
		for(GameObject[] row: _board){
			for(GameObject element: row){
				if (element == null){
					System.out.print(" , ");
				}
				else{
					System.out.print(" " + element.getType() + ", ");
				}
			}
			System.out.println();
		}
	}
	public void fire(Laser l){
		_lasersToAdd.add(l);
	}
	public Player getPlayer(){
		return _player;
	}
	
	public GameObject[][] getBoard(){
		return _board;
	}
	
	private float map(float x, float a, float b, float c, float d){
		return (x-a)/(b-a) * (d-c) + c;
	}
	
	public ArrayList<Enemy> getEnemies(){
		return _enemies;
	}

	public int getWidth() {
		return _width;
	}
	public int getHeight(){
		return _height;
	}
	
}
