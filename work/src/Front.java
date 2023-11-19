
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
//This class represents the main front-end interface of the application and implements the ActionListener interface.
public class Front implements ActionListener {

 // Buttons for different functionalities.
 JButton Meal = new JButton("Meal Log");
 JButton Exercise = new JButton("Exercise log");
 JButton Graphing = new JButton("Graphing");
 JButton Profile = new JButton("Profile");

 // Frame to hold the main interface components.
 MyFrame main = new MyFrame();

 // Constructor for the Front class.
 public Front() {

     // Set the title of the main frame.
     main.setTitle("Echo");

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
 }

 // ActionListener method for button clicks.
 @Override
 public void actionPerformed(ActionEvent e) {
     // Check which button was clicked.

     if (e.getSource() == Meal) {
         // Open a new window for Meal log GUI.
    	 main.dispose();
    	 MealGui mealgui = new MealGui;
     } else if (e.getSource() == Exercise) {
         // Open a new window for Exercise log GUI.
    	 main.dispose();
    	 
     } else if (e.getSource() == Graphing) {
         // Open a new window for Graphing GUI.
    	 main.dispose();
    	 
     } else if (e.getSource() == Profile) {
         // Open a new window for Profile GUI.
    	 main.dispose();
    	 ProfileGui profilegui = new ProfileGui();
     }
 }
}
