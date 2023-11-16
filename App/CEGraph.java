/**
 * Team Echo
 * EECS 3311
 * Use case 4 that visualizes the daily calorie intakes
 * and daily exercise within an inputed time period
 */
package ECHO.UseCase4Test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class CEGraph extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static CEGraph instance;
	private String startDate;
	private String endDate;
	private JLabel inputDate;
	private JTextField start;
	private JLabel to;
	private JTextField end;
	private JLabel example;
	private JButton graph;
	private MealLogger myMealLogger;
	private ExerciseLogger myExerciseLogger;
	private double userBMR;

	public static CEGraph getInstance() {
		if (instance == null)
			instance = new CEGraph();

		return instance;
	}// end getInstance

	private CEGraph() {
		// Set window title
		super("Daily Calory Intake & Daily Exercise");
		// Set charts region
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 0));

		// Create top bar which takes time period from the user
		inputDate = new JLabel("Input Date");
		start = new JTextField(10);
		to = new JLabel("To");
		end = new JTextField(10);
		example = new JLabel("dd/mm/yyyy");
		graph = new JButton("Graph");
		// programs button to create the chart
		graph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startDate = start.getText();
				endDate = end.getText();
				try {
					createTimeSeries(panel, startDate, endDate);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}// end actionPerformeds
		});// end actionPerformed

		JPanel header = new JPanel();
		header.add(inputDate);
		header.add(start);
		header.add(to);
		header.add(end);
		header.add(example);
		header.add(graph);

		getContentPane().add(header, BorderLayout.NORTH);
		getContentPane().add(panel, BorderLayout.WEST);
	}// end UC5test

	public void createTimeSeries(JPanel panel, String startDate, String endDate) throws ParseException {
		panel.removeAll();// removes previous chart that was made
		panel.revalidate();
		panel.repaint();
		TimeSeries caloryIntake = new TimeSeries("Daily Calory Intake");
		TimeSeries amountExercise = new TimeSeries("Daily Amount of Exercise");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		int day, month, year;
		// turns date inputed from the user into a format
		// that allows comparison of dates
		Date start = simpleDateFormat.parse(startDate);
		Date end = simpleDateFormat.parse(endDate);
		// will receive the user bmr from profile
		// will receive myMealLogger, log of the users meals, from mealLog
		// will receive myExerciseLogger, log of the users
		// exercise info, from exerciseLog
		if (start.after(end)) {
			JOptionPane.showMessageDialog(null, "Incorrect Date Info");
		} else {
			// for loop that adds info to the chart that fits
			// into the inputed time period
			for (int i = 0; i < myMealLogger.getMeals().size(); i++) {
				Date test = simpleDateFormat.parse(myMealLogger.getMeals().get(i).getDate());
				if (test.equals(start) || test.equals(end) || test.after(start) && test.before(end)) {
					day = Integer.valueOf(myMealLogger.getMeals().get(i).getDate().substring(0, 2));
					month = Integer.valueOf(myMealLogger.getMeals().get(i).getDate().substring(3, 5));
					year = Integer.valueOf(myMealLogger.getMeals().get(i).getDate().substring(6, 10));
					caloryIntake.add(new Day(day, month, year), myMealLogger.getMeals().get(i).calculateCalories());
				} // end if statement
			} // end for loop

			for (int i = 0; i < myExerciseLogger.getExercises().size(); i++) {
				Date test = simpleDateFormat.parse(myExerciseLogger.getExercises().get(i).getDate());
				if (test.equals(start) || test.equals(end) || test.after(start) && test.before(end)) {
					day = Integer.valueOf(myExerciseLogger.getExercises().get(i).getDate().substring(0, 2));
					month = Integer.valueOf(myExerciseLogger.getExercises().get(i).getDate().substring(3, 5));
					year = Integer.valueOf(myExerciseLogger.getExercises().get(i).getDate().substring(6, 10));
					amountExercise.add(new Day(day, month, year),
							myExerciseLogger.getExercises().get(i).calculateCaloriesBurnt(userBMR));
				} // end if statement
			} // end for loop
				// creates the chart and adds the data
				// adds the different axis'
			TimeSeriesCollection dataset2 = new TimeSeriesCollection();
			dataset2.addSeries(amountExercise);

			TimeSeriesCollection dataset = new TimeSeriesCollection();
			dataset.addSeries(caloryIntake);

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

	public static void main(String[] args) {

		JFrame frame = CEGraph.getInstance();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}// end main

	@Override
	public void actionPerformed(ActionEvent e) {
	}// end actionPerformed
}// end class