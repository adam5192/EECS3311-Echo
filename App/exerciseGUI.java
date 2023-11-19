package App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Calendar;

public class exerciseGUI extends JFrame {
    // GUI componenets declaration
    private JPanel MainPanel;
    private JComboBox exerciseComboBox;
    private JLabel exerciseLabel;
    private JTextField timeField;
    private JButton logButton;
    private JComboBox intensityComboBox;
    private JButton viewLogButton;
    private JComboBox minutesComboBox;
    private JComboBox hourComboBox;
    private JLabel timeOfDayLabel;
    private JLabel timeLabel;
    private ExerciseLogger exerciseLogger = new ExerciseLogger();

    public exerciseGUI() {
        Profile user = DBQuery.getCurrentProfile();
        double userBMR = Calculator.calculateBMR(user);
        setContentPane(MainPanel);
        setTitle("Exercise Log");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);

        // Year JComboBox
        JComboBox<Integer> yearComboBox = new JComboBox<>();
        for (int year = 2020; year <= 2025; year++) {
            yearComboBox.addItem(year);
        }

        // Month JComboBox
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        JComboBox<String> monthComboBox = new JComboBox<>(months);

        // Day combo box
        JComboBox<Integer> dayComboBox = new JComboBox<>();
        for (int day = 1; day <= 31; day++) {
            dayComboBox.addItem(day);
        }

        // Listener for logButton
        logButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Get corresponding details from user input
                    int duration = Integer.parseInt(timeField.getText());

                    // Input for duration must be positive number
                    if (duration > 0) {
                        String intensity = (String) intensityComboBox.getSelectedItem();
                        String type = (String) exerciseComboBox.getSelectedItem();
                        String time = (String) hourComboBox.getSelectedItem() + (String) minutesComboBox.getSelectedItem();

                        // Create panel to ask user for date of logged exercise
                        JPanel panel = new JPanel();
                        panel.add(new JLabel("Year:"));
                        panel.add(yearComboBox);
                        panel.add(Box.createHorizontalStrut(15)); // Spacer
                        panel.add(new JLabel("Month:"));
                        panel.add(monthComboBox);
                        panel.add(Box.createHorizontalStrut(15)); // Spacer
                        panel.add(new JLabel("Day:"));
                        panel.add(dayComboBox);

                        boolean validDateSelected = false;
                        while (!validDateSelected) {
                            // Show the dialog
                            int result = JOptionPane.showOptionDialog(
                                    null,
                                    panel,
                                    "Enter the Date",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.PLAIN_MESSAGE,
                                    null,
                                    new Object[]{"OK"},  // Array of options. Only "OK" button here
                                    null // Default button, null for no default
                            );

                            // Process the result
                            if (result == JOptionPane.OK_OPTION) {
                                int year = (int) yearComboBox.getSelectedItem();
                                String monthName = (String) monthComboBox.getSelectedItem();
                                int day = (int) dayComboBox.getSelectedItem(); // validate this input

                                // Convert month name to month number (1-12)
                                String[] months = {"January", "February", "March", "April", "May", "June",
                                        "July", "August", "September", "October", "November", "December"};
                                int month = Arrays.asList(months).indexOf(monthName) + 1;

                                // Check if the date is valid
                                if (isValidDate(year, month, day)) {
                                    String formattedDate = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
                                    System.out.println("Selected Date: " + formattedDate);
                                    Exercise exercise = new Exercise(formattedDate, time, type, duration, intensity);
                                    exerciseLogger.logExercise(exercise);
                                    timeField.setText("");
                                    validDateSelected = true;
                                    // Process the date as needed
                                } else {
                                    System.out.println("Invalid Date");
                                    JOptionPane.showMessageDialog(MainPanel, "Please enter valid date");
                                }
                            }
                        }
                    } else {
                        // Number is not positive, show error message
                        JOptionPane.showMessageDialog(MainPanel, "Please enter valid number");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainPanel, "Please enter valid number");
                }
            }
        });
        // Listener for viewLogButton
        viewLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder exerciseLogInfo = new StringBuilder();
                int totalCaloriesBurned = 0;
                for (Exercise exercise : exerciseLogger.getExercises()) {
                    int caloriesBurned = exercise.calculateCaloriesBurnt(userBMR);

                    totalCaloriesBurned += caloriesBurned;

                    // Display all corresponding info for each exercise in log
                    exerciseLogInfo.append(exercise.getDate())
                            .append(" : ")
                            .append(exercise.getType())
                            .append(" for ")
                            .append(exercise.getDuration())
                            .append(" minutes,")
                            .append(" Calories Burned: ")
                            .append(caloriesBurned)
                            .append("\n");
                }
                JOptionPane.showMessageDialog(MainPanel, exerciseLogInfo.toString(), "Daily Exercise Log", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    // This method checks if the inputted date by the user is a real date
    public boolean isValidDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1); // Calendar month is 0-based
        cal.set(Calendar.DAY_OF_MONTH, 1);

        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return day >= 1 && day <= maxDay;
    }

    public static void main(String[] args) {
//        Profile user = new Profile();
        new exerciseGUI();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        MainPanel = new JPanel();
        MainPanel.setLayout(new GridBagLayout());
        exerciseLabel = new JLabel();
        exerciseLabel.setText("Exercise:");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTH;
        MainPanel.add(exerciseLabel, gbc);
        exerciseComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Running");
        defaultComboBoxModel1.addElement("Biking");
        defaultComboBoxModel1.addElement("Swimming");
        defaultComboBoxModel1.addElement("Boxing");
        exerciseComboBox.setModel(defaultComboBoxModel1);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        MainPanel.add(exerciseComboBox, gbc);
        timeLabel = new JLabel();
        timeLabel.setText("Time (minutes)");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTH;
        MainPanel.add(timeLabel, gbc);
        timeField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.ipadx = 30;
        MainPanel.add(timeField, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Intensity:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTH;
        MainPanel.add(label1, gbc);
        intensityComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("low");
        defaultComboBoxModel2.addElement("medium");
        defaultComboBoxModel2.addElement("high");
        defaultComboBoxModel2.addElement("very high");
        intensityComboBox.setModel(defaultComboBoxModel2);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        MainPanel.add(intensityComboBox, gbc);
        viewLogButton = new JButton();
        viewLogButton.setText("View Log");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 8;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        MainPanel.add(viewLogButton, gbc);
        logButton = new JButton();
        logButton.setText("Log Exercise");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        MainPanel.add(logButton, gbc);
        timeOfDayLabel = new JLabel();
        timeOfDayLabel.setText("Time of Day");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTH;
        MainPanel.add(timeOfDayLabel, gbc);
        minutesComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel3 = new DefaultComboBoxModel();
        defaultComboBoxModel3.addElement("00");
        defaultComboBoxModel3.addElement("01");
        defaultComboBoxModel3.addElement("02");
        defaultComboBoxModel3.addElement("03");
        defaultComboBoxModel3.addElement("04");
        defaultComboBoxModel3.addElement("05");
        defaultComboBoxModel3.addElement("06");
        defaultComboBoxModel3.addElement("07");
        defaultComboBoxModel3.addElement("08");
        defaultComboBoxModel3.addElement("09");
        defaultComboBoxModel3.addElement("10");
        defaultComboBoxModel3.addElement("11");
        defaultComboBoxModel3.addElement("12");
        defaultComboBoxModel3.addElement("13");
        defaultComboBoxModel3.addElement("14");
        defaultComboBoxModel3.addElement("15");
        defaultComboBoxModel3.addElement("16");
        defaultComboBoxModel3.addElement("17");
        defaultComboBoxModel3.addElement("18");
        defaultComboBoxModel3.addElement("19");
        defaultComboBoxModel3.addElement("20");
        defaultComboBoxModel3.addElement("21");
        defaultComboBoxModel3.addElement("22");
        defaultComboBoxModel3.addElement("23");
        defaultComboBoxModel3.addElement("24");
        defaultComboBoxModel3.addElement("25");
        defaultComboBoxModel3.addElement("26");
        defaultComboBoxModel3.addElement("27");
        defaultComboBoxModel3.addElement("28");
        defaultComboBoxModel3.addElement("29");
        defaultComboBoxModel3.addElement("30");
        defaultComboBoxModel3.addElement("31");
        defaultComboBoxModel3.addElement("32");
        defaultComboBoxModel3.addElement("33");
        defaultComboBoxModel3.addElement("34");
        defaultComboBoxModel3.addElement("35");
        defaultComboBoxModel3.addElement("36");
        defaultComboBoxModel3.addElement("37");
        defaultComboBoxModel3.addElement("38");
        defaultComboBoxModel3.addElement("39");
        defaultComboBoxModel3.addElement("40");
        defaultComboBoxModel3.addElement("41");
        defaultComboBoxModel3.addElement("42");
        defaultComboBoxModel3.addElement("43");
        defaultComboBoxModel3.addElement("44");
        defaultComboBoxModel3.addElement("45");
        defaultComboBoxModel3.addElement("46");
        defaultComboBoxModel3.addElement("47");
        defaultComboBoxModel3.addElement("48");
        defaultComboBoxModel3.addElement("49");
        defaultComboBoxModel3.addElement("50");
        defaultComboBoxModel3.addElement("51");
        defaultComboBoxModel3.addElement("52");
        defaultComboBoxModel3.addElement("53");
        defaultComboBoxModel3.addElement("54");
        defaultComboBoxModel3.addElement("55");
        defaultComboBoxModel3.addElement("56");
        defaultComboBoxModel3.addElement("57");
        defaultComboBoxModel3.addElement("58");
        defaultComboBoxModel3.addElement("59");
        minutesComboBox.setModel(defaultComboBoxModel3);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.weighty = 1.0;
        MainPanel.add(minutesComboBox, gbc);
        hourComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel4 = new DefaultComboBoxModel();
        defaultComboBoxModel4.addElement("00:");
        defaultComboBoxModel4.addElement("01:");
        defaultComboBoxModel4.addElement("02:");
        defaultComboBoxModel4.addElement("03:");
        defaultComboBoxModel4.addElement("04:");
        defaultComboBoxModel4.addElement("05:");
        defaultComboBoxModel4.addElement("06:");
        defaultComboBoxModel4.addElement("07:");
        defaultComboBoxModel4.addElement("08:");
        defaultComboBoxModel4.addElement("09:");
        defaultComboBoxModel4.addElement("10:");
        defaultComboBoxModel4.addElement("11:");
        defaultComboBoxModel4.addElement("12:");
        defaultComboBoxModel4.addElement("13:");
        defaultComboBoxModel4.addElement("14:");
        defaultComboBoxModel4.addElement("15:");
        defaultComboBoxModel4.addElement("16:");
        defaultComboBoxModel4.addElement("17:");
        defaultComboBoxModel4.addElement("18:");
        defaultComboBoxModel4.addElement("19:");
        defaultComboBoxModel4.addElement("20:");
        defaultComboBoxModel4.addElement("21:");
        defaultComboBoxModel4.addElement("22:");
        defaultComboBoxModel4.addElement("23:");
        hourComboBox.setModel(defaultComboBoxModel4);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weighty = 1.0;
        MainPanel.add(hourComboBox, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return MainPanel;
    }
}
