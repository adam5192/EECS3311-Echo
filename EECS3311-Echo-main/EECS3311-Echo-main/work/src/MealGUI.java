

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class MealGUI extends JFrame {
    // GUI componenets declaration
    public JPanel MainPanel;
    private JTextField ingredientField;
    private JButton addIngredientBtn;
    private JTextField servingField;
    private JButton logMealButton;
    private JButton viewMealLogButton;
    private JComboBox ingredientComboBox;
    private JLabel servingLabel;
    private MealLogger mealLogger = new MealLogger();
    private ArrayList<Ingredient> ingredientList = new ArrayList<>();
    JButton back = new JButton("Back");

    public MealGUI(Front front) throws SQLException {
        setContentPane(MainPanel);
        setTitle("Meal Log");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        back.setBounds(0, 0, 20, 20);
        setVisible(false);
        MainPanel.add(back);
        ingredientField.setForeground(Color.gray);
        ingredientField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextField textField = (JTextField) e.getSource();
                if ( textField.getText().equals("Search") ) {
                    textField.setText("");
                    textField.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                JTextField textField = (JTextField) e.getSource();
                if ( textField.getText().equals("") ) {
                    textField.setText("Search");
                    textField.setForeground(Color.gray);
                }
            }
        });
        // listener for back button
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                front.main.setVisible(true);
            }
        });

        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                setVisible(false);
                front.main.setVisible(true);
            }
        });

        // Year JComboBox
        JComboBox<Integer> yearComboBox = new JComboBox<>();
        for (int year = 2020; year <= 2025; year++) {
            yearComboBox.addItem(year);
        }

        //Array of ingredients
        String[] ops = DBQuery.getIngredientNames();
        
        // Add ingredients to combo box
        for (int i = 0; i < ops.length; i++) {
            ingredientComboBox.addItem(ops[i]);
        }

        // Month JComboBox setup
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
        JComboBox<String> monthComboBox = new JComboBox<>(months);

        // Day combo box setup
        JComboBox<Integer> dayComboBox = new JComboBox<>();
        for (int day = 1; day <= 31; day++) {
            dayComboBox.addItem(day);
        }

        // Key listener for ingredientField to implement suggestions as user types ingredient
        ingredientField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchText = ingredientField.getText();
                List<String> suggestions = getMatchingIngredients(searchText);
                updateSuggestionList(suggestions);
            }

            // Method to get matching ingredients based on user input
            private List<String> getMatchingIngredients(String searchText) {
                List<String> matchedIngredients = new ArrayList<>();
                String searchTextLower = searchText.toLowerCase(); // Set properties to lowercase for case insensitivity
                for (String ingredient : ops) {
                    if (ingredient.toLowerCase().contains(searchTextLower)) {
                        matchedIngredients.add(ingredient);
                    }
                }
                return matchedIngredients;
            }

            // Method to update combo box with matching ingredients
            private void updateSuggestionList(List<String> suggestions) {
                ingredientComboBox.removeAllItems();
                for (String s : suggestions) {
                    ingredientComboBox.addItem(s);
                }
            }

        });

        // Listener for addIngredientBtn to add selected ingredient
        addIngredientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Get currently selected ingredient and amount of servings
                    int servings = Integer.parseInt(servingField.getText());

                    // Servings must be positive
                    if (servings > 0) {
                        String name = ingredientComboBox.getSelectedItem().toString();
                        Ingredient ingredient = new Ingredient();
                        ingredient.setServing(servings);

                        // Get nutritional information from database
                        ingredient.setCalories(servings * DBQuery.getNutrientVal(name, "KCAL"));
                        ingredient.setProtein(servings * DBQuery.getNutrientVal(name, "PROT"));
                        ingredient.setFat(servings * DBQuery.getNutrientVal(name, "FAT"));
                        ingredient.setCarbs(servings * DBQuery.getNutrientVal(name, "CARB"));
                        System.out.println(name);
                        ingredientList.add(ingredient);
                        ingredientField.setText("");
                        servingField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(MainPanel, "Please enter valid number");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainPanel, "Please enter valid number");
                } catch (NullPointerException exception) {
                    JOptionPane.showMessageDialog(MainPanel, "Please select an ingredient");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Listener for logMealButton to log meal
        logMealButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Meal meal = new Meal();
                // List of meal types
                List<String> optionsList = new ArrayList<>(Arrays.asList("Breakfast", "Lunch", "Dinner", "Snacks"));

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
                        String monthName = (String) monthComboBox.getSelectedItem();
                        int day = (int) dayComboBox.getSelectedItem(); // validate this input

                        // Convert month name to month number (1-12)
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
                        int month = Arrays.asList(months).indexOf(monthName) + 1;

                        // Check if the date is valid
                        if (isValidDate(year, month, day)) {
                            String formattedDate = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
                            System.out.println("Selected Date: " + formattedDate);
                            meal.setDate(formattedDate);
                            validDateSelected = true;

                            // Remove all logged meal types except snacks
                            mealLogger.getMeals().stream()
                                    .filter(m -> m.getDate().equals(formattedDate))
                                    .map(Meal::getMealType)
                                    .distinct()
                                    .forEach(mealType -> {
                                        if (!"Snacks".equals(mealType)) {
                                            optionsList.remove(mealType);
                                        }
                                    });

                        } else {
                            System.out.println("Invalid Date");
                            JOptionPane.showMessageDialog(MainPanel, "Please enter valid date");
                        }
                    }
                }

                 // Convert list to array for option pane
                String[] options = optionsList.toArray(new String[0]);

                // If at least one ingredient is selected, show message dialog to select meal type
                if (!ingredientList.isEmpty() && options.length > 0) {
                    int choice = JOptionPane.showOptionDialog(MainPanel,
                            "Select meal type", "Choose Meal Type",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                            null, options, options[0]);
                    // Set meal type based on selected option
                    if (choice != -1) {
                        String selectedMeal = options[choice];
                        meal.setType(selectedMeal);

                        // Add all ingredients to meal
                        for (Ingredient ingredient : ingredientList) {
                            meal.addIngredient(ingredient);
                        }

                        // Give feedback to user that meal has been logged
                        mealLogger.logMeal(meal);
                        JOptionPane.showMessageDialog(MainPanel, "Meal logged as: " + selectedMeal, "Meal logged", JOptionPane.INFORMATION_MESSAGE);

                        ingredientList.clear(); // Clears the ingredient list for next entry
                        ingredientField.setText(""); // Clears the ingredient input field
                        servingField.setText(""); // Clears the grams input field
                    }
                } else {
                    // If no ingredients have been logged, show error dialog
                    if (ingredientList.isEmpty()) {
                        JOptionPane.showMessageDialog(MainPanel,
                                "No ingredients added to the meal.",
                                "Meal Logging Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        // Listener for viewMealLogButton
        viewMealLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder mealLogInfo = new StringBuilder();
                int totalCalories = 0, totalFat = 0, totalProtein = 0, totalCarbs = 0;
                // Calculate nutritional information for all meals in meal log
                for (Meal meal : mealLogger.getMeals()) {
                    int calories = meal.calculateCalories();
                    int fat = meal.calculateFat();
                    int protein = meal.calculateProtein();
                    int carbs = meal.calculateCarbs();

                    totalCalories += calories;
                    totalFat += fat;
                    totalProtein += protein;
                    totalCarbs += carbs;

                    // Display each meal and its nutrients
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
                JOptionPane.showMessageDialog(MainPanel, mealLogInfo.toString(), "Daily Meal Log", JOptionPane.INFORMATION_MESSAGE);
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

    public static void main(String[] args) throws SQLException {
        new MealGUI(new Front());
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
        ingredientField = new JTextField();
        ingredientField.setText("Search");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 2.0;
        gbc.weighty = 1.0;
        gbc.ipadx = 70;
        MainPanel.add(ingredientField, gbc);
        addIngredientBtn = new JButton();
        addIngredientBtn.setText("Add Ingredient");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        MainPanel.add(addIngredientBtn, gbc);
        servingField = new JTextField();
        servingField.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.ipadx = 30;
        MainPanel.add(servingField, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Ingedient:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTH;
        MainPanel.add(label1, gbc);
        servingLabel = new JLabel();
        servingLabel.setText("Servings");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTH;
        MainPanel.add(servingLabel, gbc);
        logMealButton = new JButton();
        logMealButton.setText("Log Meal");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        MainPanel.add(logMealButton, gbc);
        viewMealLogButton = new JButton();
        viewMealLogButton.setText("View Meal Log");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        MainPanel.add(viewMealLogButton, gbc);
        ingredientComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        ingredientComboBox.setModel(defaultComboBoxModel1);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.ipadx = 50;
        MainPanel.add(ingredientComboBox, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return MainPanel;
    }
}
