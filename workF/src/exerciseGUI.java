
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.List;

public class exerciseGUI extends JFrame {
    // GUI componenets declaration
    public JPanel MainPanel;
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
    JButton back = new JButton("Back");

    public exerciseGUI(Front front) {
        setContentPane(MainPanel);
        setTitle("Exercise Log");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setVisible(false);

        // Year, month, day JComboBox
        JComboBox<Integer> yearComboBox = new JComboBox<>();
        JComboBox<String> monthComboBox = new JComboBox<>();
        JComboBox<Integer> dayComboBox = new JComboBox<>();
        DateGUI.setupComboDateBoxes(yearComboBox, dayComboBox, monthComboBox);

        // listener for back button
        back.setBounds(0, 0, 20, 20);
        MainPanel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                front.main.setVisible(true);
            }
        });

        // listener for clicking X
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                setVisible(false);
                front.main.setVisible(true);
            }
        });

        // Listener for logButton
        logButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Exercise exercise = new Exercise();
                try {
                    // Get corresponding details from user input
                    int duration = Integer.parseInt(timeField.getText());
                    // Input for duration must be positive number
                    if (duration > 0) {
                        String intensity = (String) intensityComboBox.getSelectedItem();
                        String type = (String) exerciseComboBox.getSelectedItem();
                        String time = (String) hourComboBox.getSelectedItem() + (String) minutesComboBox.getSelectedItem();
                        exercise.setIntensity(intensity);
                        exercise.setType(type);
                        exercise.setTime(time);
                        exercise.setDuration(duration);
                        DateGUI.logThis(yearComboBox, dayComboBox, monthComboBox, "Exercise", front, new Meal(), exercise, new ArrayList<>());

                        exerciseLogger.logExercise(exercise);
                        timeField.setText("");
                        // add current exercise to profile log
                        exercise.setTime(time);
                        front.profileGUIInstance.currProfile.addLog(exercise);
                        DBQuery.storeLog(exercise, front.profileGUIInstance.currProfile.getUserID());
                    } else {
                        // Number is not positive, show error message
                        JOptionPane.showMessageDialog(MainPanel, "Please enter valid number");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainPanel, "Please enter valid number");
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }
        });
        // Listener for viewLogButton
        viewLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // panel for displaying exercise log
                JPanel exerciseLogPanel = new JPanel();
                exerciseLogPanel.setLayout(new BoxLayout(exerciseLogPanel, BoxLayout.Y_AXIS));
                // get bmr from current user info
                double userBMR = front.profileGUIInstance.currProfile.getBMR();
                // loop through exercise log
                for (Exercise exercise : exerciseLogger.getExercises()) {
                    exercise.setCaloriesBurned(exercise.calculateCaloriesBurnt(userBMR));

                    JPanel exercisePanel = new JPanel();
                    exercisePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                    // button to remove an exercise
                    JButton removeButton = new JButton("Remove");
                    removeButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // remove from log and gui
                            exerciseLogger.removeExercise(exercise);
                            exerciseLogPanel.remove(exercisePanel);
                            exerciseLogPanel.revalidate();
                            exerciseLogPanel.repaint();
                        }
                    });
                    // display exercise info
                    JLabel exerciseLabel = new JLabel(exercise.getDate() + " : " + exercise.getType() +
                            " for " + exercise.getDuration() + " minutes, Calories Burned: " +
                            exercise.getCaloriesBurned());

                    exercisePanel.add(exerciseLabel);
                    exercisePanel.add(removeButton);
                    exerciseLogPanel.add(exercisePanel);
                }

                JOptionPane.showMessageDialog(MainPanel, exerciseLogPanel, "Daily Exercise Log", JOptionPane.INFORMATION_MESSAGE);
            }
        });

    }

    public static void main(String[] args) throws SQLException {
        new exerciseGUI(new Front());
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
        defaultComboBoxModel1.addElement("Weight Lifting");
        defaultComboBoxModel1.addElement("Jump Roping");
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
