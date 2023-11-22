/**
 * Team Echo
 * EECS 3311
 * Use case 5 that visualizes the daily
 * nutrient intakes
 */
package App;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

@SuppressWarnings("serial")
public class DNGraph extends JFrame implements ActionListener {
	private String startDate;
	private String endDate;
	private JLabel inputDate;
	private JTextField start;
	private JLabel to;
	private JTextField end;
	private JLabel example;
	// private JButton graphTen;
	private JButton graphFive;

	public DNGraph()  {
		// Set window title
		super("Daily Nutrients Intake");
		@SuppressWarnings("deprecation")
		Profile user = new Profile(false, new Date(1974, 06, 10), 155.0, 50.0, 1);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		DBQuery.getCurrentProfile();
		inputDate = new JLabel("Input Date");
		start = new JTextField(10);
		to = new JLabel("To");
		end = new JTextField(10);
		example = new JLabel("dd/mm/yyyy");
		// graphTen = new JButton("Graph Top 10");
		graphFive = new JButton("Graph Top 5");
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 0));
		JPanel header = new JPanel();
		graphFive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startDate = start.getText();
				endDate = end.getText();
				try {
					createChartTopFive(panel, simpleDateFormat.parse(startDate), simpleDateFormat.parse(endDate), user);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}// end actionPerformed

		});// end actionPerformed
		header.add(inputDate);
		header.add(start);
		header.add(to);
		header.add(end);
		header.add(example);
		// header.add(graphTen);
		header.add(graphFive);

		getContentPane().add(header, BorderLayout.NORTH);
		getContentPane().add(panel, BorderLayout.WEST);
		setExtendedState(JFrame.MAXIMIZED_BOTH);// make fullscreen
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}// end DNGraph

	private void createChartTopFive(JPanel panel, Date startDate, Date endDate, Profile user) throws ParseException {
		panel.removeAll();// removes any previous charts that where made
		panel.revalidate();
		panel.repaint();
		// data set
		DefaultPieDataset result = new DefaultPieDataset();
		int totalCalories = 0;
		int totalFat = 0;
		int totalProtein = 0;
		int totalCarbs = 0;
		int other = 0;

		for (int i = 0; i < user.getHistory().size(); i++) {
			if (user.getHistory().get(i).getLogType() == 2 && user.getHistory().get(i).getDate().after(startDate)
					&& user.getHistory().get(i).getDate().before(endDate)
					|| (user.getHistory().get(i).getLogType() == 2
							&& user.getHistory().get(i).getDate().equals(startDate))
					|| (user.getHistory().get(i).getLogType() == 2
							&& user.getHistory().get(i).getDate().equals(endDate))) {
				totalCalories += ((MealLog) user.getHistory().get(i)).calculateCalories();
				totalFat += ((MealLog) user.getHistory().get(i)).calculateFat();
				totalProtein += ((MealLog) user.getHistory().get(i)).calculateProtein();
				totalCarbs += ((MealLog) user.getHistory().get(i)).calculateCarbs();
				other += ((MealLog) user.getHistory().get(i)).calculateOthers();
			} // end if statement
		} // end for loop
			// adds nutrients to the data set
		result.setValue("Carbs", totalCarbs);
		result.setValue("Protein", totalProtein);
		result.setValue("Fat", totalFat);
		result.setValue("Calories", totalCalories);
		result.setValue("Other", other);
		// catch when the user inputs a end date that is earlier than the start
		if (startDate.after(endDate)) {
			JOptionPane.showMessageDialog(null, "Incorrect Date Info");
		} else {
			JFreeChart chart = ChartFactory.createPieChart("Daily Nutrient Intake", result, true, true, false);

			ChartPanel chartPanel = new ChartPanel(chart);
			chartPanel.setPreferredSize(new Dimension(900, 600));
			chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
			chartPanel.setBackground(Color.white);
			panel.add(chartPanel);
			panel.revalidate();
			panel.repaint();
		} // end if statement
	}// end createChartTopFive

	public static void main(String[] args) {
		new DNGraph();
	}// end main

	@Override
	public void actionPerformed(ActionEvent e) {
	}// end actionPerformed
}// end class