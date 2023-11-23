import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JPanel;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import javax.swing.JButton;

//This class represents the main front-end interface of the application and implements the ActionListener interface.
public class Front implements ActionListener {

    // Buttons for different functionalities.
    JButton Meal = new JButton("Meal Log");
    JButton Exercise = new JButton("Exercise log");
    JButton Graphing = new JButton("Graphing");
    JButton Profile = new JButton("Profile");

    ArrayList<Integer> list = new ArrayList<Integer>();
    JPanel panel = new JPanel();

    MealGUI mealGUIInstance;
    exerciseGUI exGUIInstance;
    ProfileGui profileGUIInstance;
//    private GraphingGUI graphingGUIInstance;

    // Frame to hold the main interface components.
    MyFrame main = new MyFrame();

    // Constructor for the Front class.
    public Front() throws SQLException {

        // Set the title of the main frame.
        main.setTitle("Echo");
        // When closed, save all logs to database
        main.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                // Save logs
            }
        });

        // Set the positions and sizes of the buttons.
        Meal.setBounds(50, 160, 120, 40);
        Exercise.setBounds(190, 160, 120, 40);
        Graphing.setBounds(330, 160, 120, 40);
        Profile.setBounds(350, 20, 100, 40);

        // Make buttons not focusable.
        Meal.setFocusable(false);
        Exercise.setFocusable(false);
        Graphing.setFocusable(false);
        Profile.setFocusable(false);

        // Add ActionListener to each button.
        Meal.addActionListener(this);
        Exercise.addActionListener(this);
        Graphing.addActionListener(this);
        Profile.addActionListener(this);

        // Set layout to null for custom component placement.
        main.setLayout(null);

        // Add buttons to the main frame.
        main.add(Meal);
        main.add(Exercise);
        main.add(Graphing);
        main.add(Profile);

        // Create instances of each gui component, they will default to invisible, and
        // then become visibile when corresponding button is pressed
        mealGUIInstance = new MealGUI(this);
        exGUIInstance = new exerciseGUI(this);
//        graphingGUIInstance = new GraphingGUI(this);

        // TODO: implement above logic for ProfileGUI
       profileGUIInstance = new ProfileGui(this);
    }

    // ActionListener method for button clicks.
    @Override
    public void actionPerformed(ActionEvent e) {
        // Check which button was clicked.

        if (e.getSource() == Meal) {
            // Open a new window for Meal log GUI.
            main.setVisible(false);
            mealGUIInstance.setVisible(true);
        } else if (e.getSource() == Exercise) {
            // Open a new window for Exercise log GUI.
            main.setVisible(false);
            exGUIInstance.setVisible(true);

        } else if (e.getSource() == Graphing) {
            // Open a new window for Graphing GUI.
            main.setVisible(false);
//            graphingGUIInstance.setVisible(true);

        } else if (e.getSource() == Profile) {
             //TODO: implement same logic from Meal and Exercise for profile

	        	/* if a user created their userid is added to the list and based on whoever the user pick the profile of that userid is called 
	        	JComboBox<Integer> users = new JComboBox<>(list.toArray(new Integer[0]));
		    	panel.add(users);
	        	JOptionPane.showConfirmDialog(main, panel, "Select a Number", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	        	  Integer selectedNumber = (Integer) ((JComboBox<?>) panel.getComponent(0)).getSelectedItem();
	        	  main.setVisible(false);
	        	  profileGUIInstance = new ProfileGui(this,selectedNumber);
	        	  profileGUIInstance.frame.setVisible(true);
	        	  
	        	  /* else if no is create profile then you do  
	        	   * main.setVisible(false); 
	        	   * profileGUIInstance = new ProfileGui(this);
	        	   * profileGUIInstance.frame.setVisible(true);
	        	   */
        }
    }
}
