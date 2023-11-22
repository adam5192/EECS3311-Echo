package App;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GraphingGUI extends JFrame implements ActionListener {
	private JButton CEGraph;
	private JButton DNGraph;
	private JLabel title;
	JFrame main = new JFrame();

	public GraphingGUI() {
		super("Graphs");
		title = new JLabel("Graphs");
		JPanel panel = new JPanel();
		JPanel header = new JPanel();
		CEGraph = new JButton("Graph Daily Calory Intake and Exercise");
		DNGraph = new JButton("Graph Daily Nutrient Intake");

		CEGraph.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				main.dispose();
				new CEGraph();
			}// end actionPerformed

		});
		DNGraph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				main.dispose();
				new DNGraph();
			}// end actionPerformed
		});
		header.add(title);
		panel.add(CEGraph);
		panel.add(DNGraph);
		getContentPane().add(header, BorderLayout.NORTH);
		getContentPane().add(panel, BorderLayout.NORTH);
		setExtendedState(JFrame.MAXIMIZED_BOTH);// make fullscreen
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}// end GraphingGUI

	public static void main(String[] args) {
		new GraphingGUI();
	}// end main

	@Override
	public void actionPerformed(ActionEvent e) {
	}// end actionPerformed

}// end class