package App;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GraphingGUI extends JFrame implements ActionListener {
	private JButton CEGraph;
	private JButton DNGraph;

	public GraphingGUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 0));
		CEGraph = new JButton("Graph Daily Calory Intake and Exercise");
		DNGraph = new JButton("Graph Daily Nutrient Intake");

		CEGraph.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MealLogger myMealLogger = new MealLogger();
				ExerciseLogger myExerciseLogger = new ExerciseLogger();
				CEGraph UC4 = new CEGraph(myMealLogger, myExerciseLogger);
			}// end actionPerformed

		});
		DNGraph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MealLogger myMealLogger = new MealLogger();
				CEGraph UC5 = new CEGraph(myMealLogger);
			}// end actionPerformed
		});

		panel.add(CEGraph);
		panel.add(DNGraph);
		getContentPane().add(panel, BorderLayout.NORTH);
		setExtendedState(JFrame.MAXIMIZED_BOTH);// make fullscreen
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}// end GraphingGUI

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}// end main

	@Override
	public void actionPerformed(ActionEvent e) {
	}// end actionPerformed

}// end class