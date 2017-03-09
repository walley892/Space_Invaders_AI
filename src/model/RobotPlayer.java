package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;



public class RobotPlayer extends Player {

	private class StateActionPair {
		int[] state;
		ACTION action;

		StateActionPair(int[] s, ACTION a) {
			state = s;
			action = a;
		}

	}

	private enum ACTION {
		LEFT, RIGHT, FIRE
	};

	private double[][] _Q;
	private boolean _needsRestoring, _needsSaving;
	private double _epsilon;
	private int _scorePrev;
	private int _turns;
	private double _learningRate;
	private ArrayList<StateActionPair> _lastActions;
	private int _attribution;
	private String _restorePath;
	private String _savePath;

	public RobotPlayer(Board b, int yPos, int xPos) {
		super(b, yPos, xPos);
		_needsSaving = false;
		_restorePath = "src\\model\\Q_Saved.q";
		_savePath = "Q_Saved.q";
		_attribution = 10;
		_needsRestoring = true;
		_lastActions = new ArrayList<StateActionPair>();
		_turns = 0;
		_scorePrev = 0;
		_learningRate = 0.7;
		_Q = new double[b.getWidth()][b.getWidth()];
		for (int i = 0; i < _Q.length; ++i) {
			for (int j = 0; j < _Q.length; ++j) {
				if (i > 10)
					_Q[i][j] = 1000;
				else {
					_Q[i][j] = -100000;
				}
			}
		}
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
		if (_turns % _attribution == 0)
			updateQ();
	}

	private int[] nextState(int[] state, ACTION a) {
		switch (a) {
		case FIRE:
			break;
		case LEFT:
			state[0] -= 1;
			break;
		case RIGHT:
			state[0] += 1;
			break;
		default:
			break;
		}
		return state;
	}

	private void updateQ() {
		int reward = 0;
		if (_board.getScore() == _scorePrev) {
			reward = -100;
		} else {
			reward = _board.getScore() - _scorePrev;
		}
		for (StateActionPair a : _lastActions) {
			try {
				int[] stateNext = nextState(a.state, a.action);
				double futureReward = reward(stateNext, maxAction(stateNext));
				_Q[a.state[0]][a.state[1]] = _Q[a.state[0]][a.state[1]]
						+ _learningRate * (reward + 0.8 * futureReward - _Q[a.state[0]][a.state[1]]);
			} catch (Exception e) {
			}
		}
		_lastActions.clear();
		_scorePrev = _board.getScore();
	}

	private ACTION maxAction(int[] state) {
		ACTION[] acts = { ACTION.LEFT, ACTION.RIGHT, ACTION.FIRE };
		ACTION best = acts[1];
		for (ACTION a : acts) {
			if (reward(state, a) > reward(state, best)) {
				best = a;
			}
		}
		return best;
	}

	public ACTION chooseAction() {
		ACTION act = qAction();
		int[] state = { _position[1], averageEnemyPosition() };
		_lastActions.add(new StateActionPair(state, act));
		return act;
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

	private ACTION qAction() {
		double ran = Math.random();
		if (ran < 1 - _epsilon) {
			int[] state = { _position[1], averageEnemyPosition() };
			ACTION[] acts = { ACTION.LEFT, ACTION.RIGHT, ACTION.FIRE };
			double[] rewards = new double[3];
			for (int i = 0; i < 3; ++i) {
				rewards[i] = reward(state, acts[i]);
			}
			double m = Math.random();
			int max = 0;
			if (m > 0.33) {
				max = 1;
			}
			if (m > 0.66) {
				max = 2;
			}
			for (int i = 0; i < 3; ++i) {
				if (rewards[i] > rewards[max]) {
					max = i;
				}
			}
			return acts[max];
		} else {
			return randomAction();
		}
	}

	private int averageEnemyPosition() {
		ArrayList<Enemy> enemies = _board.getEnemies();
		int xsum = 0;
		for (Enemy e : enemies) {
			xsum += e.getPosition()[1];
		}
		xsum = xsum / enemies.size();
		return xsum;
	}

	private double reward(int[] state, ACTION action) {
		try {
			switch (action) {
			case LEFT:
				return _Q[state[0] - 1][state[1]];
			case RIGHT:
				return _Q[state[0] + 1][state[1]];
			default:
				return _Q[state[0]][state[1]];

			}
		} catch (IndexOutOfBoundsException e) {
			return -10000;
		}
	}

	public void setLearningRate(double d) {
		_learningRate = d;
	}

	public void save() {
		java.io.FileWriter f;
		try {
			f = new java.io.FileWriter(_savePath);
			for (double[] line : _Q) {
				f.write(Arrays.toString(line));
				f.write("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Save Complete");
		_needsSaving = false;
	}

	public void restore() {
		java.util.Scanner s;
		try {
			File f = new File(_restorePath);

			s = new java.util.Scanner(f);
			char c = 0;
			String build = "";
			double[] line = new double[_board.getWidth()];
			int col = -1;
			int row = -1;
			while (s.hasNext()) {
				String st = s.next();
				for (int i = 0; i < st.length(); ++i) {
					c = st.charAt(i);
					switch (c) {
					case '[':
						break;
					case ',':
						line[++col] = Double.valueOf(build);
						build = "";
						break;
					case ']':
						line[++col] = Double.valueOf(build);
						build = "";
						_Q[++row] = line;
						line = new double[_board.getWidth()];
						col = -1;
						break;
					case '\n':
						break;
					default:
						build = build + c;
						break;
					}
				}
			}
		} catch (IOException e) {
			System.out.println("No restore file found");
		}
		System.out.println("Restore Complete");
		_needsRestoring = false;
	}
	public void makeRestore() {
		_needsRestoring = true;
	}
	public void makeSave(){
		//Don't we all
		_needsSaving = true;
	}
	public void setEpsilon(double val) {
		_epsilon = val;
	}

	public void setAttribution(int val) {
		_attribution = val;
	}
	public void setSavePath(String p){
		_savePath = p;
	}
	public void setRestorePath(String p){
		_restorePath = p;
	}
}
