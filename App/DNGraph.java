package ECHO.UseCase6Test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

@SuppressWarnings("serial")
public class DNGraph extends JFrame implements ActionListener {
	private static DNGraph instance;
	private String startDate;
	private String endDate;
	private JLabel inputDate;
	private JTextField start;
	private JLabel to;
	private JTextField end;
	private JLabel example;
	private JButton graph;

	public static DNGraph getInstance() {
		if (instance == null)
			instance = new DNGraph();

		return instance;
	}// end getInstance

	private DNGraph() {
		// Set window title
		super("Daily Nutrients Intake");

		inputDate = new JLabel("Input Date");
		start = new JTextField(10);
		to = new JLabel("To");
		end = new JTextField(10);
		example = new JLabel("dd/mm/yyyy");
		graph = new JButton("Graph");
		JPanel west = new JPanel();
		west.setLayout(new GridLayout(2, 0));
		JPanel north = new JPanel();
		graph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startDate = start.getText();
				endDate = end.getText();
				createPie(west, startDate, endDate);
			}// end actionPerformeds
		});// end actionPerformed
		north.add(inputDate);
		north.add(start);
		north.add(to);
		north.add(end);
		north.add(example);
		north.add(graph);

		getContentPane().add(north, BorderLayout.NORTH);
		getContentPane().add(west, BorderLayout.WEST);
	}// end DNGraph

	private void createPie(JPanel west, String startDate, String endDate) {
		// example data set
		DefaultPieDataset result = new DefaultPieDataset();

		result.setValue("Sodium", 20);
		result.setValue("Fat", 15);
		result.setValue("Protein", 25);
		result.setValue("Carbs", 30);
		result.setValue("Sugars", 10);

		result.setValue("Sodium", 30);
		result.setValue("Fat", 20);
		result.setValue("Protein", 10);
		result.setValue("Carbs", 15);
		result.setValue("Sugars", 25);

		JFreeChart chart = ChartFactory.createPieChart("Daily Nutrient Intake", result, true, true, false);

		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(900, 600));
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chartPanel.setBackground(Color.white);
		west.add(chartPanel);
	}// end createPie

	public static void main(String[] args) {

		JFrame frame = DNGraph.getInstance();
		frame.setSize(900, 600);
		frame.pack();
		frame.setVisible(true);
	}// end main

	@Override
	public void actionPerformed(ActionEvent e) {
	}// end actionPerformed
}// end class