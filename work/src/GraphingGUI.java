import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GraphingGUI extends JFrame implements ActionListener {
	private JButton CEGraph;
	private JButton DNGraph;
	JFrame main = new JFrame();
	private JButton back;

	public GraphingGUI(Front Front) {
		main.setTitle("Graphs");
		JPanel panel = new JPanel();
		CEGraph = new JButton("Graph Daily Calory Intake and Exercise");
		DNGraph = new JButton("Graph Daily Nutrient Intake");
		back = new JButton("Back");
		CEGraph CEGraphInstance = new CEGraph(this,Front.profileGUIInstance.currProfile);
		DNGraph DNGraphInstance = new DNGraph(this,Front.profileGUIInstance.currProfile);
		main.setVisible(false);
		CEGraphInstance.setVisible(false);
		DNGraphInstance.setVisible(false);
		CEGraph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				main.setVisible(false);
				CEGraphInstance.setVisible(true);
			}// end actionPerformed

		});
		DNGraph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				main.setVisible(false);
				DNGraphInstance.setVisible(true);
			}// end actionPerformed
		});

		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				Front.main.setVisible(true);
			}
		});

		// listener for clicking X
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				Front.main.setVisible(true);
			}
		});
		panel.add(CEGraph);
		panel.add(DNGraph);
		panel.add(back);
		getContentPane().add(panel, BorderLayout.NORTH);
		setExtendedState(JFrame.MAXIMIZED_BOTH);// make fullscreen
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}// end GraphingGUI

	public static void main(String[] args) throws SQLException {
		Front Front = new Front();
		new GraphingGUI(Front);
	}// end main

	@Override
	public void actionPerformed(ActionEvent e) {
	}// end actionPerformed

}// end class