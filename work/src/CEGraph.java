/**
 * Team Echo
 * EECS 3311
 * Use case 4 that visualizes the daily calorie intakes
 * and daily exercise within an inputed time period
 */
package App;

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

	public CEGraph(MealLogger myMealLogger, ExerciseLogger myExerciseLogger) {
		// Set window title
		super("Daily Calory Intake & Daily Exercise");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Profile user = DBQuery.getCurrentProfile();
		double userBMR = Calculator.calculateBMR(user.getBirth(), user.getWeight(), user.getSex(), user.getCalcMethod(),
				user.getFatLvl());
		// Set charts region
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 0));

		inputDate = new JLabel("Input Date");
		start = new JTextField(10);
		to = new JLabel("To");
		end = new JTextField(10);
		example = new JLabel("dd/mm/yyyy");
		graph = new JButton("Graph");

		JPanel header = new JPanel();
		header.add(inputDate);
		header.add(start);
		header.add(to);
		header.add(end);
		header.add(example);
		header.add(graph);

		// programs button to create the chart
		graph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startDate = start.getText();
				endDate = end.getText();
				try {
					createTimeSeries(panel, simpleDateFormat.parse(startDate), simpleDateFormat.parse(endDate),
							Calculator.calculateBMR(user), myMealLogger, myExerciseLogger);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}// end actionPerformeds
		});// end actionPerformed
		getContentPane().add(header, BorderLayout.NORTH);
		getContentPane().add(panel, BorderLayout.WEST);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}// end UC5test

	public void createTimeSeries(JPanel panel, Date startDate, Date endDate, double userBMR, MealLogger myMealLogger,
			ExerciseLogger myExerciseLogger) throws ParseException {
		panel.removeAll();// removes previous chart that was made
		panel.revalidate();
		panel.repaint();
		TimeSeries caloryIntake = new TimeSeries("Daily Calory Intake");
		TimeSeries amountOfExercise = new TimeSeries("Daily Amount of Exercise");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

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
			for (int i = 0; i < myMealLogger.getMeals().size(); i++) {
				Double calories = amountOfCalsPerDate
						.get((simpleDateFormat.parse(myMealLogger.getMeals().get(i).getDate())));
				Double amount = calories == null ? 0 : calories;
				amountOfCalsPerDate.put(simpleDateFormat.parse(myMealLogger.getMeals().get(i).getDate()),
						amount + myMealLogger.getMeals().get(i).calculateCalories());
			} // end for loop

			for (Entry<Date, Double> e : amountOfCalsPerDate.entrySet()) {
				caloryIntake.add(new Day(e.getKey()), e.getValue());
			} // end for loop

			Map<Date, Double> amountOfExPerDate = new HashMap<>();
			for (int i = 0; i < myExerciseLogger.getExercises().size(); i++) {
				Double exercise = amountOfExPerDate
						.get((simpleDateFormat.parse(myExerciseLogger.getExercises().get(i).getDate())));
				Double amount = exercise == null ? 0 : exercise;
				amountOfExPerDate.put(simpleDateFormat.parse(myExerciseLogger.getExercises().get(i).getDate()),
						amount + myExerciseLogger.getExercises().get(i).calculateCaloriesBurnt(userBMR));
			} // end for loop

			for (Entry<Date, Double> e : amountOfExPerDate.entrySet()) {
				if (e.getKey().after(startDate) && e.getKey().before(endDate) || e.getKey().equals(startDate)
						|| e.getKey().equals(endDate))
					amountOfExercise.add(new Day(e.getKey()), e.getValue());
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

	public static void main(String[] args) {
		MealLogger myMealLogger = new MealLogger();
		ExerciseLogger myExerciseLogger = new ExerciseLogger();
		new CEGraph(myMealLogger, myExerciseLogger);
	}// end main

	@Override
	public void actionPerformed(ActionEvent e) {
	}// end actionPerformed
}// end class