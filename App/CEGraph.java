package ECHO.UseCase5Test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

	private static CEGraph instance;
	private String startDate;
	private String endDate;
	private JLabel inputDate;
	private JTextField start;
	private JLabel to;
	private JTextField end;
	private JLabel example;
	private JButton graph;

	public static CEGraph getInstance() {
		if (instance == null)
			instance = new CEGraph();

		return instance;
	}// end getInstance

	private CEGraph() {
		// Set window title
		super("Daily Calory Intake & Daily Exercise");
		// Set charts region
		JPanel west = new JPanel();
		west.setLayout(new GridLayout(2, 0));

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
					createTimeSeries(west, startDate, endDate);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} // end
			}// end actionPerformeds
		});// end actionPerformed

		JPanel north = new JPanel();
		north.add(inputDate);
		north.add(start);
		north.add(to);
		north.add(end);
		north.add(example);
		north.add(graph);

		getContentPane().add(north, BorderLayout.NORTH);
		getContentPane().add(west, BorderLayout.WEST);
	}// end UC5test

	private void createTimeSeries(JPanel west, String startDate, String endDate) throws ParseException {
		TimeSeries series1 = new TimeSeries("Daily Calory Intake");
		TimeSeries series2 = new TimeSeries("Daily Amount of Exercise");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		// sample info for testing
		ArrayList<String> dates = new ArrayList<String>();
		ArrayList<Double> totalCalories = new ArrayList<Double>();
		ArrayList<Double> totalExercises = new ArrayList<Double>();
		dates.add("09/12/2023");
		dates.add("10/12/2023");
		dates.add("11/12/2023");
		dates.add("12/12/2023");
		dates.add("13/12/2023");
		dates.add("14/12/2023");
		dates.add("15/12/2023");
		dates.add("16/12/2023");
		dates.add("17/12/2023");
		dates.add("18/12/2023");
		dates.add("19/12/2023");
		dates.add("20/12/2023");
		totalCalories.add(2000.0);
		totalCalories.add(2023.0);
		totalCalories.add(2346.0);
		totalCalories.add(2332.0);
		totalCalories.add(2454.0);
		totalCalories.add(1903.0);
		totalCalories.add(2100.0);
		totalCalories.add(2223.0);
		totalCalories.add(2346.0);
		totalCalories.add(1932.0);
		totalCalories.add(2454.0);
		totalCalories.add(1903.0);
		totalExercises.add(4.0);
		totalExercises.add(2.0);
		totalExercises.add(6.0);
		totalExercises.add(5.0);
		totalExercises.add(3.0);
		totalExercises.add(4.0);
		totalExercises.add(4.0);
		totalExercises.add(2.0);
		totalExercises.add(4.0);
		totalExercises.add(5.0);
		totalExercises.add(3.0);
		totalExercises.add(4.0);
		User Dan = new User(dates, totalCalories, totalExercises);
		// turns date inputed from the user into a format
		// that allows comparison of dates
		Date start = simpleDateFormat.parse(startDate);
		Date end = simpleDateFormat.parse(endDate);
		// for loop that adds info to the chart that fits
		// into the inputed time period
		for (int i = 0; i < Dan.size(); i++) {
			Date test = simpleDateFormat.parse(dates.get(i));
			if (test.equals(start) || test.equals(end) || test.after(start) && test.before(end)) {
				int day = Integer.valueOf(Dan.getDate(i).substring(0, 2));
				int month = Integer.valueOf(Dan.getDate(i).substring(3, 5));
				int year = Integer.valueOf(Dan.getDate(i).substring(6, 10));
				series1.add(new Day(day, month, year), Dan.gettotalCalories(i));
				series2.add(new Day(day, month, year), Dan.gettotalExercises(i));
			} // end if statement
		} // end for loop

		// creates the chart and adds the data
		// adds the different axis'
		TimeSeriesCollection dataset2 = new TimeSeriesCollection();
		dataset2.addSeries(series2);

		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(series1);

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
		plot.setRangeAxis(1, new NumberAxis("Exercise(hrs)"));

		plot.mapDatasetToRangeAxis(0, 0);
		plot.mapDatasetToRangeAxis(1, 1);

		JFreeChart chart = new JFreeChart("Daily Calory Intake & Daily Amount of Exercise",
				new Font("Serif", java.awt.Font.BOLD, 18), plot, true);

		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(900, 600));
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chartPanel.setBackground(Color.white);
		west.add(chartPanel);

	}// end createTimeSeries

	public static void main(String[] args) {

		JFrame frame = CEGraph.getInstance();
		frame.setSize(900, 600);
		frame.pack();
		frame.setVisible(true);
	}// end main

	@Override
	public void actionPerformed(ActionEvent e) {
	}// end actionPerformed

	class User {
		ArrayList<String> dates = new ArrayList<String>();
		ArrayList<Double> totalCalories = new ArrayList<Double>();
		ArrayList<Double> totalExercises = new ArrayList<Double>();

		User(ArrayList<String> dates, ArrayList<Double> totalCalories, ArrayList<Double> totalExercises) {
			for (int i = 0; i < dates.size(); i++) {
				this.dates.add(dates.get(i));
				this.totalCalories.add(totalCalories.get(i));
				this.totalExercises.add(totalExercises.get(i));
			} // end for loop
		}// end User

		public void setDate(String date) {
			this.dates.add(date);
		}// end setDate

		public void setTotalCalories(Double totalCalories) {
			this.totalCalories.add(totalCalories);
		}// end setTotalCalories

		public void setTotalExercises(Double totalExercises) {
			this.totalExercises.add(totalExercises);
		}// end setTotalExercises

		public String getDate(int index) {
			return this.dates.get(index);
		}// end getDate

		public Double gettotalCalories(int index) {
			return this.totalCalories.get(index);
		}// end gettotalCalories

		public Double gettotalExercises(int index) {
			return this.totalExercises.get(index);
		}// end gettotalExercises

		public int size() {
			// TODO Auto-generated method stub
			return this.dates.size();
		}// end size
	}// end User;
}// end class