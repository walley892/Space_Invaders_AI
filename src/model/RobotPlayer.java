package model;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;



public class RobotPlayer extends Player {


	private enum ACTION {
		RIGHT, LEFT, FIRE
	};

	private double[] _W;
	private boolean _needsRestoring, _needsSaving;
	private ArrayList<Enemy> _enemies;
	private int _nActions;
	private double _lastScore;
	private int _lastAction;
	private double[] _lastState;
	private float _lastReward;
	private double[] _E;
 	public RobotPlayer(Board b, int yPos, int xPos) {
		super(b, yPos, xPos);
		_enemies = (ArrayList<Enemy>) b.getEnemies().clone();
		_nActions = 3;
		_W = new double[_enemies.size()*2*_nActions];
		
		for(int i = 0; i < _W.length; ++i){
			_W[i] = Math.random();
		}
	}
	private double[] q(double[] state){
		double[] q = new double[_nActions];
		for(int i = 0; i < _nActions; ++i){
			double[] actionState = getActionState(state, i);
			q[i] = dot(actionState, _W);
		}
		return q;
	}
	private void qUpdate(double[] state, int action,double reward, double[] statePrime, double alpha){
		double qOne = q(state)[action];
		double[] qPrime = q(state);
		double qTwo = qPrime[max(qPrime)];
		state = getActionState(state,action);
		for(int i = 0; i < _W.length; ++i){
			_W[i] = _W[i] + alpha*(reward + 0.8*qTwo - qOne)*state[i];
		}
	}
	private void sarsaUpdate(double[] state, int action, double reward, double[] statePrime, int actionPrime, double alpha){
		double qOne = q(state)[action];
		double qTwo = q(statePrime)[actionPrime];
		state = getActionState(state, action);
		for(int i = 0; i < _W.length; ++i){
			_W[i] = _W[i] + alpha*(reward + 0.8*qTwo - qOne)*state[i];
		}
	}
	
	private double dot(double[] a, double[] b){
		if(a.length != b.length){
			throw new IllegalArgumentException();
		}
		double avg = 0;
		
		for(int i = 0; i < a.length; ++i){
			avg += a[i]*b[i];
		}
		return avg;
	}
	private double[] getState(){
		double[] state = new double[_enemies.size()*2];
		ArrayList<Enemy> currentEnemies = _board.getEnemies();
		for(int i = 0; i < _enemies.size()*2; i+=2){
			Enemy e = _enemies.get(i/2);
			if(currentEnemies.contains(e)){
				int[] pos = e._position;
				state[i] = (double)pos[0]/(double)_board.getHeight();
				state[i+1] = (double)pos[1]/(double)_board.getWidth();
			}
		}
		return state;
	}
	private double[] getActionState(double[] state, int action){
		double[] actionState = new double[_enemies.size()*2*_nActions];
		for(int i = 0; i < state.length; ++i){
			int index = i + action*_enemies.size()*2;
			actionState[index] = state[i];			
		}
		return actionState;
	}
	@Override
	public OBJECT_TYPE getType() {
		return GameObject.OBJECT_TYPE.PLAYER;
	}

	@Override
	public void move() {
		if(_needsRestoring){
			restore();
		}
		if(_needsSaving){
			save();
		}
		switch (chooseAction()) {
		case FIRE:
			fire();
			break;
		case LEFT:
			moveLeft();
			break;
		case RIGHT:
			moveRight();
			break;
		default:
			break;
		}
	}
	private int max(double[] q){
		int max = 0;
		for(int i = 0; i < q.length; ++i){
			if(q[max] < q[i]){
				max = i;
			}
		}
		return max;
	}
	public ACTION chooseAction() {
		ACTION action;
		if(_board.getScore() == _lastScore){
			_lastReward = -0.75f;
		}
		if(_board.getScore() > _lastScore){
			_lastReward = 0.5f;
		}
		if(_board.getScore() < _lastScore){
			_lastReward = -1;
		}
		_lastScore = _board.getScore();
		double[] state = getState();
		double[] q = q(state);
		if(Math.random()>(double)10000/(1+(double) (10000+_board.getTurn()))){
			action = ACTION.values()[max(q)];
			if(_board.getTurn() > 0){
				//sarsaUpdate(_lastState, _lastAction, _lastReward, state, action.ordinal(), 0.1);//(double)10/((double)10+(double)_board.getTurn()));
				qUpdate(_lastState, _lastAction, _lastReward, state, 0.1);
			}
		}else{
			action = randomAction();
		}
		_lastAction = action.ordinal();
		_lastState = state;
		System.out.println(Arrays.toString(_W));
		//System.out.println(_board.getTurn());
		return action;
	}

	private ACTION randomAction() {
		double ra = Math.random();
		if (ra < .3)
			return ACTION.LEFT;
		else if (ra < .6)
			return ACTION.RIGHT;
		else
			return ACTION.FIRE;
	}
	public void reset(){
		_enemies = (ArrayList<Enemy>) _board.getEnemies().clone();
	}
	public void save() {
		
	}
	
	public void restore() {
		
	}
	public void makeRestore() {
		_needsRestoring = true;
	}
	public void makeSave(){
		//Don't we all
		_needsSaving = true;
	}

}
