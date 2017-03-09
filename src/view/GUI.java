package view;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

import model.Board;
import model.RobotPlayer;


public class GUI {
	private GameScreen _screen;
	private Board _board;
	private JFrame _frame;
	public void generateUI(int gameMode) {
		
		gameMode = 1;
		_board = new Board(gameMode);
		_frame = new JFrame();
		_screen = new GameScreen(_board);
		
		
		_frame.setLayout(new GridBagLayout());
		
		_frame.add(_screen);
		generateMenu();
		_frame.setVisible(true);
		_frame.pack();
		if (gameMode == 0) {
			_frame.addKeyListener(new Listener(_board.getPlayer(), _board));
		}
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void update(){
		_board.update();
		_screen.repaint();
	}
	protected void generateMenu(){
		JMenuBar bar = new JMenuBar();
		bar.setLayout(new GridLayout(0,1));
		for(int i = 0; i < 4; ++i){
			JTextField f = new JTextField();
			JMenu m = new JMenu("Set " + RobotParameters.PARAMETER.values()[i]);
			f.setText("Enter"+ RobotParameters.PARAMETER.values()[i] +"value");
			f.addActionListener(new RobotParameters(RobotParameters.PARAMETER.values()[i], (RobotPlayer) _board.getPlayer(), f));
			f.setEnabled(true);
			m.add(f);
			bar.add(new JMenuItem());
			bar.add(m);
		}
		for(int i = 0; i < 2; ++i){
			JTextField f = new JTextField();
			JMenu m = new JMenu("" + SaveListener.PARAMETER.values()[i]);
			f.setText("Specify "+SaveListener.PARAMETER.values()[i]+" path");
			f.addActionListener(new SaveListener((RobotPlayer) _board.getPlayer(),SaveListener.PARAMETER.values()[i], f));
			f.setEnabled(true);
			m.add(f);
			bar.add(new JMenuItem());
			bar.add(m);
		}
		bar.setBackground(Color.WHITE);
		_frame.add(bar);
	}
	
}
