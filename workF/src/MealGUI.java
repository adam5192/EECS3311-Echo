import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.ParseException;
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
    MealLogger mealLogger = new MealLogger();
    private ArrayList<Ingredient> ingredientList = new ArrayList<>();
    JButton back = new JButton("Back");

    public MealGUI(Front front) {
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
                front.profileGUIInstance.currProfile.setMealHistory(mealLogger.getMeals());
            }
        });

        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                setVisible(false);
                front.main.setVisible(true);
            }
        });

        // Year, month, day JComboBox
        JComboBox<Integer> yearComboBox = new JComboBox<>();
        JComboBox<String> monthComboBox = new JComboBox<>();
        JComboBox<Integer> dayComboBox = new JComboBox<>();
        String[] ops = DBQuery.getIngredientNames();
        // Add ingredients to combo box
        for (int i = 0; i < ops.length; i++) {
            ingredientComboBox.addItem(ops[i]);
        }
        DateGUI.setupComboDateBoxes(yearComboBox, dayComboBox, monthComboBox);

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
                try {
                    DateGUI.logThis(yearComboBox, dayComboBox, monthComboBox, "Meal", front, meal, new Exercise(), optionsList);
                } catch (ParseException ex) {
                    ex.printStackTrace();
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

                        // add current meal to profile log
                        front.profileGUIInstance.currProfile.addLog(meal);
                        DBQuery.storeLog(meal, front.profileGUIInstance.currProfile.getUserID());

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
                // new panel for displaying meal log
                JPanel mealLogPanel = new JPanel();
                mealLogPanel.setLayout(new BoxLayout(mealLogPanel, BoxLayout.Y_AXIS));

                for (Meal meal : mealLogger.getMeals()) {
                    JPanel mealPanel = new JPanel();
                    mealPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                    // button to remove meal
                    JButton removeButton = new JButton("Remove");
                    removeButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            mealLogger.removeMeal(meal);
                            mealLogPanel.remove(mealPanel);
                            mealLogPanel.revalidate();
                            mealLogPanel.repaint();
                        }
                    });
                    // display meals info
                    JLabel mealLabel = new JLabel(meal.getMealType() + " " + meal.getDate() +
                            " Calories " + meal.calculateCalories() +
                            ", Fat " + meal.calculateFat() + "g, Protein " +
                            meal.calculateProtein() + "g, Carbs " +
                            meal.calculateCarbs() + "g");

                    mealPanel.add(mealLabel);
                    mealPanel.add(removeButton);
                    mealLogPanel.add(mealPanel);
                }

                JOptionPane.showMessageDialog(MainPanel, mealLogPanel, "Daily Meal Log", JOptionPane.INFORMATION_MESSAGE);
            }
        });

    }



    public static void main(String[] args) {
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
