import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class ProfileGui implements ActionListener, FocusListener {
	
	MyFrame frame = new MyFrame();
	JButton Create = new JButton("Create profile");
	JButton back = new JButton("Back");
	JTextField Height = new JTextField("Height");
	JTextField Weight = new JTextField("Weight");
	JTextField BodyFat = new JTextField("BodyFat%");
	JTextField date = new JTextField("yy/mm/dd");
	JRadioButton Male = new JRadioButton("Male");
	JRadioButton Female = new JRadioButton("Female");
	JLabel title = new JLabel("Profile");
	ButtonGroup group = new ButtonGroup();
	
	
	double height;
	double weight;
	double fatlevel;
	double bmr;
	int bmrSetting;
	boolean gender;
	Date birth;
	String Birth;

	

	 public ProfileGui() {
		 
		 frame.setTitle("Profile");
		 
		// frame.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 20));
		 back.setBounds(350, 20, 70, 30);
		 
		 title.setBounds(230, 90, 100, 70);
		 title.setForeground(Color.BLACK);

		 Height.setBounds(200, 150, 100, 30);
		 Weight.setBounds(200, 200, 100, 30);
		 BodyFat.setBounds(200, 250, 100, 30);
		 date.setBounds(200, 300, 100, 30);
		 
		 Male.setBounds(170, 350, 65, 30);
		 Female.setBounds(255, 350, 75, 30);
		 
		 Create.setBounds(190, 400, 120, 40); 
		 
		 group.add(Male);
		 group.add(Female);
		 
		 frame.add(back);
		 frame.add(title);
		 frame.add(Height);
		 frame.add(Weight);
		 frame.add(BodyFat);
		 frame.add(Male);
		 frame.add(Female);
		 frame.add(date);
		 frame.add(Create);
		 
		 Weight.addFocusListener(this);
		 Height.addFocusListener(this);
		 BodyFat.addFocusListener(this);
		 date.addFocusListener(this);
		 
		 back.addActionListener(this);
		 Create.addActionListener(this);
		 
		 frame.setLayout(null);
		 frame.setLocationRelativeTo(null);
		 frame.setVisible(true); //makes frame visible
	 }


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==back) 
		{
			frame.dispose();
	    	Front cal = new Front();
		}
		else if(e.getSource()==Male) 
		{
			gender=true;
		}
		else if(e.getSource()==Female) 
		{
			gender=false;
		}
		else if(e.getSource()==Create) 
		{
			height =  Double.parseDouble(Height.getText());
			weight =  Double.parseDouble(Weight.getText());
			fatlevel =  Double.parseDouble(BodyFat.getText());
			Birth = date.getText();
			
			birth = new Date(Integer.parseInt(Birth.substring(0,4)),Integer.parseInt(Birth.substring(5,7))-1,Integer.parseInt(Birth.substring(8,10)));
			
			bmr=CalculateBMR.calculateBMR(gender,weight,height,bmrSetting,fatlevel); 
			Profile user = new Profile(gender,birth,height,weight);
			
		}
		
	}
	
	  @Override
	  public void focusGained(FocusEvent e) {
	    // TODO Auto-generated method stub

	    JTextField textField = (JTextField) e.getSource();
	    if ( textField.getText().equals("Weight") || textField.getText().equals("Height") || textField.getText().equals("BodyFat%")
	    		|| textField.getText().equals("yy/mm/dd")) {
	      textField.setText("");
	    } 

	  }

	  @Override
	  public void focusLost(FocusEvent e) {
	    // TODO Auto-generated method stub
	    JTextField textField = (JTextField) e.getSource();
	    if (textField.getText().isEmpty()) {
	       if (textField == Weight) {
	        textField.setText("Weight");
	      } else if (textField == Height) {
	        textField.setText("Height");
	      } else if (textField == BodyFat) {
	        textField.setText("BodyFat%");
	      } else if (textField == date) {
		        textField.setText("yy/mm/dd");
		      }
	    }
	  }
}
