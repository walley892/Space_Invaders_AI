package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import model.RobotPlayer;

public class SaveListener implements ActionListener{
	public enum PARAMETER {save, restore};
	private RobotPlayer _robot;
	private PARAMETER _p;
	private JTextField _field;
	public SaveListener(RobotPlayer pl, PARAMETER p, JTextField f){
		_robot = pl;
		_p = p;
		_field = f;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(_p){
		case restore:
			_robot.setRestorePath(_field.getText());
			_robot.restore();
			break;
		case save:
			_robot.setSavePath(_field.getText());
			_robot.save();
			break;
		default:
			break;
		
		}
	}

}
