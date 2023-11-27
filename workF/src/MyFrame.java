
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

//This class extends JFrame and represents the main frame of the application.
public class MyFrame extends JFrame {

 // Constructor for the Frame class.
 MyFrame() {
     // Set the default close operation to exit the application when the frame is closed.
     this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

     // Set the layout manager with horizontal and vertical gaps.
     this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 120));

     // Set the size of the frame.
     this.setSize(500, 500);

     // Create an ImageIcon for the frame's icon.
     ImageIcon image = new ImageIcon("Cal-icon.png");

     // Set the frame's icon using the created ImageIcon.
     this.setIconImage(image.getImage());

     // Set the frame to appear in the center of the screen.
     this.setLocationRelativeTo(null);

     // Make the frame visible.
     this.setVisible(true);
 }
}

