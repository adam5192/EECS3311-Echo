package ECHO.UseCase6Test;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.GradientPaintTransformType;
import org.jfree.chart.ui.StandardGradientPaintTransformer;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

@SuppressWarnings("serial")
public class DNGraph extends ApplicationFrame {

	public DNGraph(String title) {
		super("Daily Nutrient Intake");
		final CategoryDataset dataset = createDataset();
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 350));
		setContentPane(chartPanel);
	}// end UC6test

	private CategoryDataset createDataset() {
		// example data set
		DefaultCategoryDataset result = new DefaultCategoryDataset();

		result.addValue(20, "Sodium", "10/12/2023");
		result.addValue(15, "Fat", "10/12/2023");
		result.addValue(25, "Protein", "10/12/2023");
		result.addValue(30, "Carbs", "10/12/2023");
		result.addValue(10, "Sugars", "10/12/2023");

		result.addValue(10, "Sodium", "11/12/2023");
		result.addValue(30, "Fat", "11/12/2023");
		result.addValue(20, "Protein", "11/12/2023");
		result.addValue(20, "Carbs", "11/12/2023");
		result.addValue(20, "Sugars", "11/12/2023");

		result.addValue(10, "Sodium", "12/12/2023");
		result.addValue(10, "Fat", "12/12/2023");
		result.addValue(10, "Protein", "12/12/2023");
		result.addValue(60, "Carbs", "12/12/2023");
		result.addValue(10, "Sugars", "12/12/2023");

		result.addValue(15, "Sodium", "13/12/2023");
		result.addValue(15, "Fat", "13/12/2023");
		result.addValue(15, "Protein", "13/12/2023");
		result.addValue(50, "Carbs", "13/12/2023");
		result.addValue(5, "Sugars", "13/12/2023");

		result.addValue(15, "Sodium", "14/12/2023");
		result.addValue(15, "Fat", "14/12/2023");
		result.addValue(50, "Protein", "14/12/2023");
		result.addValue(15, "Carbs", "14/12/2023");
		result.addValue(5, "Sugars", "14/12/2023");

		return result;
	}// end CategoryDataset

	private JFreeChart createChart(final CategoryDataset dataset) {

		final JFreeChart chart = ChartFactory.createStackedBarChart("Daily Nutrient Intake", "Category", "Percentage %",
				dataset, PlotOrientation.VERTICAL, true, true, false);

		GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
		KeyToGroupMap map = new KeyToGroupMap("G1");
		map.mapKeyToGroup("Sodium", "G1");
		map.mapKeyToGroup("Fat", "G1");
		map.mapKeyToGroup("Protein", "G1");
		map.mapKeyToGroup("Carbs", "G1");
		map.mapKeyToGroup("Sugars", "G1");

		renderer.setItemMargin(0.0);
		Paint p1 = new GradientPaint(0.0f, 0.0f, new Color(0x22, 0x22, 0xFF), 0.0f, 0.0f, new Color(0x88, 0x88, 0xFF));
		renderer.setSeriesPaint(0, p1);
		renderer.setSeriesPaint(4, p1);
		renderer.setSeriesPaint(8, p1);

		Paint p2 = new GradientPaint(0.0f, 0.0f, new Color(0x22, 0xFF, 0x22), 0.0f, 0.0f, new Color(0x88, 0xFF, 0x88));
		renderer.setSeriesPaint(1, p2);
		renderer.setSeriesPaint(5, p2);
		renderer.setSeriesPaint(9, p2);

		Paint p3 = new GradientPaint(0.0f, 0.0f, new Color(0xFF, 0x22, 0x22), 0.0f, 0.0f, new Color(0xFF, 0x88, 0x88));
		renderer.setSeriesPaint(2, p3);
		renderer.setSeriesPaint(6, p3);
		renderer.setSeriesPaint(10, p3);

		Paint p4 = new GradientPaint(0.0f, 0.0f, new Color(0xFF, 0xFF, 0x22), 0.0f, 0.0f, new Color(0xFF, 0xFF, 0x88));
		renderer.setSeriesPaint(3, p4);
		renderer.setSeriesPaint(7, p4);
		renderer.setSeriesPaint(11, p4);
		renderer.setGradientPaintTransformer(
				new StandardGradientPaintTransformer(GradientPaintTransformType.HORIZONTAL));

		SubCategoryAxis domainAxis = new SubCategoryAxis("Nutrients");
		domainAxis.setCategoryMargin(0.05);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setDomainAxis(domainAxis);
		// plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
		plot.setRenderer(renderer);
		// plot.setFixedLegendItems(createLegendItems());
		return chart;
	}// end createChart

	public static void main(final String[] args) {

		final DNGraph demo = new DNGraph("Stacked Bar Chart");
		demo.pack();
//		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	}// end main
}// end class