package App;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Calendar;

public class exerciseGUI extends JFrame {

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
        setContentPane(MainPanel);
        setTitle("Exercise Log");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);

        // Year JComboBox
        JComboBox<Integer> yearComboBox = new JComboBox<>();
        for(int year = 2020; year <= 2025; year++) {
            yearComboBox.addItem(year);
        }

        // Month JComboBox
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        JComboBox<String> monthComboBox = new JComboBox<>(months);

        JComboBox<Integer> dayComboBox = new JComboBox<>();
        for (int day = 1; day <= 31; day++) {
            dayComboBox.addItem(day);
        }

        logButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int duration = Integer.parseInt(timeField.getText());
                    String intensity = (String) intensityComboBox.getSelectedItem();
                    String type = (String) exerciseComboBox.getSelectedItem();
                    String time = (String) hourComboBox.getSelectedItem() + (String) minutesComboBox.getSelectedItem();

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
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainPanel, "Please enter valid number");
                }
            }
        });
        viewLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder exerciseLogInfo = new StringBuilder();
                int totalCaloriesBurned = 0;
                for (Exercise exercise : exerciseLogger.getExercises()){
                    int caloriesBurned = exercise.calculateCaloriesBurnt(1200);

                    totalCaloriesBurned += caloriesBurned;

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

//                exerciseLogInfo.append("\nTotal calories burned today: " + totalCaloriesBurned);

                JOptionPane.showMessageDialog(MainPanel, exerciseLogInfo.toString(), "Daily Exercise Log", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    // this method checks if the inputted date by the user is a real date
    public boolean isValidDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1); // Calendar month is 0-based
        cal.set(Calendar.DAY_OF_MONTH, 1);

        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return day >= 1 && day <= maxDay;
    }

    public static void main(String[] args) {
        new exerciseGUI();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
