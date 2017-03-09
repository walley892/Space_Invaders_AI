package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import model.RobotPlayer;
import model.Main;
public class RobotParameters implements ActionListener{
	protected enum PARAMETER { epsilon, learningRate, speed, rewardAttribution};
	
	private PARAMETER _parameter;
	private RobotPlayer _robot;
	private double _val;
	private JTextField _field;
	public RobotParameters(PARAMETER p, RobotPlayer r, JTextField f){
		_parameter = p;
		_robot = r;
		_field = f;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try{
			_val = Double.valueOf(_field.getText());
			if(_val < 0){
				_field.setText("Paramter cannot be negative");
				return;
			}
		} catch(Exception ex){
			_field.setText("Parameter must be a double or an int value");
			return;
		}
		switch(_parameter){
		case epsilon:
			_robot.setEpsilon(_val);
			break;
		case learningRate:
			_robot.setLearningRate(_val);
			break;
		case rewardAttribution:
			_robot.setAttribution((int) _val);
			break;
		case speed:
			Main.setCooldown((long)_val);
			break;
		default:
			break;
		
		
		}
		_field.setText("Enter"+ _parameter +"value");
	}
}