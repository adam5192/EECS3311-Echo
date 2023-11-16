package App;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MealGUI extends JFrame {
    private JPanel MainPanel;
    private JTextField ingredientField;
    private JButton addIngredientBtn;
    private JTextField gramsField;
    private JButton logMealButton;
    private JButton viewMealLogButton;
    private MealLogger mealLogger = new MealLogger();
    private ArrayList<Ingredient> ingredientList = new ArrayList<>();
    public MealGUI () {
        setContentPane(MainPanel);
        setTitle("Meal Log");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 400);
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


        addIngredientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = ingredientField.getText();
                    int grams = Integer.parseInt(gramsField.getText());
                    Ingredient ingredient = new Ingredient(name, 0, 0, 0, 0);
                    ingredientList.add(ingredient);
                    ingredientField.setText("");
                    gramsField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainPanel, "Please enter valid number");
                }
            }
        });
        logMealButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // keep track of which meal types have been added
                List<String> optionsList = new ArrayList<>(Arrays.asList("Breakfast", "Lunch", "Dinner", "Snacks"));

                // remove all logged meal types except snacks
                mealLogger.getMeals().stream()
                        .map(Meal::getMealType)
                        .distinct()
                        .forEach(mealType -> {
                            if (!"Snacks".equals(mealType)) {
                                optionsList.remove(mealType);
                            }
                        });

                // convert list to array for option pane
                String[] options = optionsList.toArray(new String[0]);

                if (!ingredientList.isEmpty() && options.length > 0) {
                    int choice = JOptionPane.showOptionDialog(MainPanel,
                            "Select meal type", "Choose Meal Type",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                            null, options, options[0]);

                    if (choice != -1) {
                        String selectedMeal = options[choice];

                        Meal meal = new Meal();
                        meal.setType(selectedMeal);

                        for (Ingredient ingredient : ingredientList) {
                            meal.addIngredient(ingredient);
                        }

                        mealLogger.logMeal(meal);
                        JOptionPane.showMessageDialog(MainPanel, "Meal logged as: " + selectedMeal, "Meal logged", JOptionPane.INFORMATION_MESSAGE);

                        ingredientList.clear(); // Clears the ingredient list for next entry
                        ingredientField.setText(""); // Clears the ingredient input field
                        gramsField.setText(""); // Clears the grams input field

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
                                    meal.setDate(formattedDate);
                                    validDateSelected = true;
                                    // Process the date as needed
                                } else {
                                    System.out.println("Invalid Date");
                                    JOptionPane.showMessageDialog(MainPanel, "Please enter valid date");
                                }
                            }
                        }
                    }
                } else {
                    if (ingredientList.isEmpty()) {
                        JOptionPane.showMessageDialog(MainPanel,
                                "No ingredients added to the meal.",
                                "Meal Logging Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        viewMealLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder mealLogInfo = new StringBuilder();
                int totalCalories = 0, totalFat = 0, totalProtein = 0, totalCarbs = 0;

                for (Meal meal : mealLogger.getMeals()) {
                    int calories = meal.calculateCalories();
                    int fat = meal.calculateFat();
                    int protein = meal.calculateProtein();
                    int carbs = meal.calculateCarbs();

                    totalCalories += calories;
                    totalFat += fat;
                    totalProtein += protein;
                    totalCarbs += carbs;

                    mealLogInfo.append(meal.getMealType())
                            .append(" ")
                            .append(meal.getDate())
                            .append(" Calories ")
                            .append(calories)
                            .append(", Fat ")
                            .append(fat)
                            .append("g, Protein ")
                            .append(protein)
                            .append("g, Carbs ")
                            .append(carbs)
                            .append("g\n");
                }

                mealLogInfo.append("\nTotal for the day: Calories ")
                        .append(totalCalories)
                        .append(", Fat ")
                        .append(totalFat)
                        .append("g, Protein ")
                        .append(totalProtein)
                        .append("g, Carbs ")
                        .append(totalCarbs)
                        .append("g");

                JOptionPane.showMessageDialog(MainPanel, mealLogInfo.toString(), "Daily Meal Log", JOptionPane.INFORMATION_MESSAGE);
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
        new MealGUI();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
