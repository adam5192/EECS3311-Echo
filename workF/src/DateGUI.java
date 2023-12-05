import javax.swing.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DateGUI {
    // This method checks if the inputted date by the user is a real date
    public static boolean isValidDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1); // Calendar month is 0-based
        cal.set(Calendar.DAY_OF_MONTH, 1);

        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return day >= 1 && day <= maxDay;
    }

    public static void setupComboDateBoxes(JComboBox yearComboBox, JComboBox dayComboBox, JComboBox monthComboBox){
        String[] months = {
                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"
        };
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        // Year JComboBox
        for (int year = currentYear-2; year <= currentYear; year++) {
            yearComboBox.addItem(year);
        }
        yearComboBox.setSelectedItem(currentYear);

        // Month JComboBox setup
        for (int i = 0; i < months.length; i++) {
            monthComboBox.addItem(months[i]);
        }
        monthComboBox.setSelectedIndex(currentMonth);

        // Day combo box setup
        for (int day = 1; day <= 31; day++) {
            dayComboBox.addItem(day);
        }
        dayComboBox.setSelectedItem(currentDay);
    }

    public static void logThis(JComboBox yearComboBox, JComboBox dayComboBox, JComboBox monthComboBox, String objType, Front front, Meal meal, Exercise exercise, List<String> optionsList) throws ParseException {
        boolean validDateSelected = false;
        while (!validDateSelected) {
            // Add elements to panel for date selection
            JPanel panel = new JPanel();
            panel.add(new JLabel("Year:"));
            panel.add(yearComboBox);
            panel.add(Box.createHorizontalStrut(15)); // Spacer
            panel.add(new JLabel("Month:"));
            panel.add(monthComboBox);
            panel.add(Box.createHorizontalStrut(15)); // Spacer
            panel.add(new JLabel("Day:"));
            panel.add(dayComboBox);
            // Show the dialog for date selection

            int result = JOptionPane.showOptionDialog(
                    null,
                    panel,
                    "Enter the Date",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    new Object[]{
                            "OK"
                    }, // Array of options. Only "OK" button here
                    null // Default button, null for no default
            );

            // Process the result
            if (result == JOptionPane.OK_OPTION) {
                int year = (int) yearComboBox.getSelectedItem();
                int day = (int) dayComboBox.getSelectedItem(); // validate this input
                int month = monthComboBox.getSelectedIndex()+1;

                // Check if the date is valid
                if (isValidDate(year, month, day)) {
                    String formattedDate = year + "/" + String.format("%02d", month) + "/" + String.format("%02d", day);
                    System.out.println("Selected Date: " + formattedDate);
                    validDateSelected = true;

                    if(objType.equals("Meal")){
                        meal.setDate(formattedDate);
                        System.out.println(front.mealGUIInstance.mealLogger.getMeals());
                        // Remove all logged meal types except snacks
                        front.mealGUIInstance.mealLogger.getMeals().stream()
                                .filter(m -> m.getDate().equals(formattedDate))
                                .map(Meal::getMealType)
                                .distinct()
                                .forEach(mealType -> {
                                    if (!"Snacks".equals(mealType)) {
                                        optionsList.remove(mealType);
                                    }
                                });
                    } else if(objType.equals("Exercise")){
                        exercise.setDate(formattedDate);
                    }
                } else {
                    System.out.println("Invalid Date");
                    JOptionPane.showMessageDialog(panel, "Please enter valid date");
                }
            }
        }
    }
}
