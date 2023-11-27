package work.src;

/**
 * Team Echo
 * EECS 3311
 * Use case 4 that visualizes the daily calorie intakes
 * and daily exercise within an inputed time period
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

@SuppressWarnings("serial")
public class CEGraph extends JFrame implements ActionListener {
	private String startDate;
	private String endDate;
	private JLabel inputDate;
	private JTextField start;
	private JLabel to;
	private JTextField end;
	private JLabel example;
	private JButton graph;
	private JButton back;

	public CEGraph(GraphingGUI previous, Profile profile) {
		// Set window title
		super("Daily Calory Intake & Daily Exercise");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD");
		Profile user = profile;
		// Set charts region
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 0));
		inputDate = new JLabel("Input Date");
		start = new JTextField(10);
		to = new JLabel("To");
		end = new JTextField(10);
		example = new JLabel("YYYY-MM-DD");
		graph = new JButton("Graph");
		back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				previous.main.setVisible(true);
			}
		});

		// listener for clicking X
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				previous.main.setVisible(true);
			}
		});

		JPanel header = new JPanel();
		header.add(inputDate);
		header.add(start);
		header.add(to);
		header.add(end);
		header.add(example);
		header.add(graph);
		header.add(back);

		// programs button to create the chart
		graph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startDate = start.getText();
				endDate = end.getText();
				try {
					createTimeSeries(panel, simpleDateFormat.parse(startDate), simpleDateFormat.parse(endDate), user);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}// end actionPerformeds
		});// end actionPerformed
		getContentPane().add(header, BorderLayout.NORTH);
		getContentPane().add(panel, BorderLayout.WEST);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}// end UC5test

	public void createTimeSeries(JPanel panel, Date startDate, Date endDate, Profile user) throws ParseException {
		panel.removeAll();// removes previous chart that was made
		panel.revalidate();
		panel.repaint();
		TimeSeries caloryIntake = new TimeSeries("Daily Calory Intake");
		TimeSeries amountOfExercise = new TimeSeries("Daily Amount of Exercise");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD");
		// turns date inputed from the user into a format
		// that allows comparison of dates
		// will receive the user BMR from profile
		// will receive myMealLogger, log of the users meals, from mealLog
		// will receive myExerciseLogger, log of the users
		// exercise info, from exerciseLog
		// catch when the user inputs an end date that is
		// earlier than the start date
		if (startDate.after(endDate)) {
			JOptionPane.showMessageDialog(null, "Incorrect Date Info");
			System.out.println("test");
		} else {
			// for loop that adds info to the chart that fits
			// into the inputed time period
			Map<Date, Double> amountOfCalsPerDate = new HashMap<>();
			for (int i = 0; i < user.getMealHistory().size(); i++) {
				Double calories = amountOfCalsPerDate
						.get(simpleDateFormat.parse(user.getMealHistory().get(i).getDate()));
				Double amountOfCals = calories == null ? 0 : calories;
				amountOfCalsPerDate.put(simpleDateFormat.parse(user.getMealHistory().get(i).getDate()),
						amountOfCals + (user.getMealHistory().get(i)).calculateCalories());
			} // end for loop

			for (Entry<Date, Double> e : amountOfCalsPerDate.entrySet()) {
				if (e.getKey().after(startDate) && e.getKey().before(endDate) || e.getKey().equals(startDate)
						|| e.getKey().equals(endDate)) {
					caloryIntake.add(new Day(e.getKey()), e.getValue());
				} // end if statement
			} // end for loop

			Map<Date, Double> amountOfExPerDate = new HashMap<>();
			for (int i = 0; i < user.getExerciseHistory().size(); i++) {
				Double exercise = amountOfExPerDate
						.get(simpleDateFormat.parse(user.getExerciseHistory().get(i).getDate()));
				Double amountOfEx = exercise == null ? 0 : exercise;
				amountOfExPerDate.put(simpleDateFormat.parse(user.getExerciseHistory().get(i).getDate()),
						amountOfEx + (user.getExerciseHistory().get(i)).getCaloriesBurned());
			} // end for loop

			for (Entry<Date, Double> e : amountOfExPerDate.entrySet()) {
				if (e.getKey().after(startDate) && e.getKey().before(endDate) || e.getKey().equals(startDate)
						|| e.getKey().equals(endDate)) {
					amountOfExercise.add(new Day(e.getKey()), e.getValue());
				} // end if statement
			} // end for loop

			// creates the chart and adds the data
			// adds the different axis'
			TimeSeriesCollection dataset = new TimeSeriesCollection();
			dataset.addSeries(caloryIntake);

			TimeSeriesCollection dataset2 = new TimeSeriesCollection();
			dataset2.addSeries(amountOfExercise);

			XYPlot plot = new XYPlot();
			XYSplineRenderer splinerenderer1 = new XYSplineRenderer();
			XYSplineRenderer splinerenderer2 = new XYSplineRenderer();

			plot.setDataset(0, dataset);
			plot.setRenderer(0, splinerenderer1);
			DateAxis domainAxis = new DateAxis("Days");
			domainAxis.setAutoTickUnitSelection(false);
			plot.setDomainAxis(domainAxis);
			domainAxis.setDateFormatOverride(new SimpleDateFormat("dd/MM/yy"));
			plot.setRangeAxis(new NumberAxis("Calories(cal)"));

			plot.setDataset(1, dataset2);
			plot.setRenderer(1, splinerenderer2);
			plot.setRangeAxis(1, new NumberAxis("Exercise"));

			plot.mapDatasetToRangeAxis(0, 0);
			plot.mapDatasetToRangeAxis(1, 1);

			JFreeChart chart = new JFreeChart("Daily Calory Intake & Daily Amount of Exercise",
					new Font("Serif", java.awt.Font.BOLD, 18), plot, true);

			ChartPanel chartPanel = new ChartPanel(chart);
			chartPanel.setPreferredSize(new Dimension(900, 600));
			chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
			chartPanel.setBackground(Color.white);
			panel.add(chartPanel);
			panel.revalidate();
			panel.repaint();
		} // end if statement
	}// end createTimeSeries

	public static void main(String[] args) throws SQLException {
		GraphingGUI back = new GraphingGUI(new Front());
		new CEGraph(back, new Profile(false, null, 0, 0, 0));
	}// end main

	@Override
	public void actionPerformed(ActionEvent e) {
	}// end actionPerformed
}// end class
