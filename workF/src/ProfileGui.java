import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Date;

import javax.swing.*;

public class ProfileGui implements ActionListener, FocusListener {

	MyFrame frame = new MyFrame();
	JButton Create = new JButton("Create profile");
	JButton New_profile = new JButton("New profile");
	JButton Edit_Profile = new JButton("Edit profile");
	JButton back = new JButton("Back");
	JButton confirm = new JButton("Confirm");
	JTextField Height = new JTextField("Height");
	JTextField Weight = new JTextField("Weight");
	JTextField BodyFat = new JTextField("BodyFat%");
	JTextField date = new JTextField("yyyy/mm/dd");
	JRadioButton Male = new JRadioButton("Male");
	JRadioButton Female = new JRadioButton("Female");
	JRadioButton Metric = new JRadioButton("Metric");
	JRadioButton Imperial = new JRadioButton("Imperial");

	JRadioButton setting1 = new JRadioButton("Miffin St Jeor");
	JRadioButton setting2 = new JRadioButton("Revised Harris-Benedict");
	JRadioButton setting3 = new JRadioButton("Katch McArdle");

	JLabel title = new JLabel("Profile");
	JLabel GenderButtonsLabel = new JLabel("Gender");
	JLabel UnitButtonsLabel = new JLabel("Unit");
	JLabel BMRSettingLabel = new JLabel("BMR setting");

	ButtonGroup group = new ButtonGroup();
	ButtonGroup groupSettings = new ButtonGroup();
	ButtonGroup UnitGroups = new ButtonGroup();


	Front front;

	public static Profile currProfile;
	double height = -1;
	double weight = -1;
	double fatlevel = -1;
	int bmrSetting = -1;
	boolean unitSetting;
	boolean gender;
	Date birth;
	String Birth;

	Front temp;

	public ProfileGui() {
		frame.setTitle("Profile");

		// frame.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 20));
		back.setBounds(350, 20, 70, 30);

		frame.setVisible(false);

		title.setBounds(230, 0, 100, 70);
		title.setForeground(Color.BLACK);

		Height.setBounds(200, 50, 100, 30);
		Weight.setBounds(200, 85, 100, 30);
		BodyFat.setBounds(200, 120, 100, 30);
		date.setBounds(200, 155, 100, 30);

		GenderButtonsLabel.setBounds(65, 175, 100, 70);
		Male.setBounds(20, 220, 65, 30);
		Female.setBounds(85, 220, 75, 30);

		BMRSettingLabel.setBounds(200, 250, 100, 70);
		setting1.setBounds(100, 300, 100, 30);
		setting2.setBounds(250, 300, 200, 30);
		setting3.setBounds(180, 340, 200, 30);

		UnitButtonsLabel.setBounds(350, 175, 100, 70);
		Metric.setBounds(280, 225, 100, 30);
		Imperial.setBounds(380, 225, 100, 30);

		group.add(Male);
		group.add(Female);

		groupSettings.add(setting1);
		groupSettings.add(setting2);
		groupSettings.add(setting3);

		UnitGroups.add(Metric);
		UnitGroups.add(Imperial);

		frame.add(back);
		frame.add(title);
		frame.add(Height);
		frame.add(Weight);
		frame.add(BodyFat);
		frame.add(Male);
		frame.add(Female);
		frame.add(date);
		frame.add(setting1);
		frame.add(setting2);
		frame.add(setting3);
		frame.add(Metric);
		frame.add(Imperial);
		frame.add(GenderButtonsLabel);
		frame.add(UnitButtonsLabel);
		frame.add(BMRSettingLabel);

		Weight.addFocusListener(this);
		Height.addFocusListener(this);
		BodyFat.addFocusListener(this);
		date.addFocusListener(this);
		Male.addActionListener(this);
		Female.addActionListener(this);
		setting1.addActionListener(this);
		setting2.addActionListener(this);
		setting3.addActionListener(this);
		Metric.addActionListener(this);
		Imperial.addActionListener(this);

		back.addActionListener(this);

		frame.setLayout(null);
		frame.setLocationRelativeTo(null);
	}

	// Profile Creation window
	public ProfileGui(Front front) {
		this();

		this.front = front;

		Create.setBounds(180, 400, 120, 40);
		frame.add(Create);
		Create.addActionListener(this);

//		frame.setLayout(null);
//		frame.setLocationRelativeTo(null);
	}

	// Loaded Profile edit window
	public ProfileGui(Front front, Profile user) {
		this();

		this.front = front;

		confirm.setBounds(180, 400, 120, 40);
		frame.add(confirm);
		confirm.addActionListener(this);

//		frame.setLayout(null);
//		frame.setLocationRelativeTo(null);
	}


	// Loaded Profile display
	public ProfileGui(Front front, int user) {

		this.front = front;
		this.temp=front;

		back.setBounds(350, 20, 70, 30);
		New_profile.setBounds(280, 370, 120, 30);
		Edit_Profile.setBounds(100, 370, 120, 30);

		title.setBounds(230, 20, 100, 70);
		title.setForeground(Color.BLACK);

		currProfile = DBQuery.getProfile(user);

		JLabel Userid = new JLabel("UserId:  " + currProfile.getUserID());
		JLabel HeightTitle = new JLabel("Height:  "+ currProfile.getHeight() +"m");
		JLabel WeightTitle = new JLabel("Weight:  "+ currProfile.getWeight()+"kg");
		JLabel BodyFatTitle = new JLabel("BodyFat%:  " + currProfile.getFatLvl());
		System.out.println(currProfile.getFatLvl());

		if(currProfile.getSex())
		{
			JLabel GenderTitle = new JLabel("Gender: Male");
			GenderTitle.setBounds(120, 230, 100, 30);
			frame.add(GenderTitle);
		}else{
			JLabel GenderTitle = new JLabel("Gender: Female");
			GenderTitle.setBounds(120, 230, 100, 30);
			frame.add(GenderTitle);
		}

		JLabel dateTitle = new JLabel("Birth day:  " + currProfile.getBirth().getYear()+"/"+ (currProfile.getBirth().getMonth()+1)+"/"+ currProfile.getBirth().getDate());
		JLabel BMRTitle = new JLabel("BMR:  " + currProfile.getBMR());


		Userid.setBounds(120,70,100,30);
		HeightTitle.setBounds(120, 110, 100, 30);
		WeightTitle.setBounds(120, 150, 100, 30);
		BodyFatTitle.setBounds(120, 190, 100, 30);
		dateTitle.setBounds(120, 270, 200, 30);
		BMRTitle.setBounds(120, 310, 100, 30);



		frame.add(title);
		frame.add(Userid);
		frame.add(HeightTitle);
		frame.add(WeightTitle);
		frame.add(BodyFatTitle);
		frame.add(dateTitle);
		frame.add(BMRTitle);
		frame.add(back);
		frame.add(New_profile);
		frame.add(Edit_Profile);

		back.addActionListener(this);
		New_profile.addActionListener(this);
		Edit_Profile.addActionListener(this);
		frame.setLayout(null);
		frame.setLocationRelativeTo(null);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==back)
		{
			frame.setVisible(false);
			front.main.setVisible(true);
		}
		else if(e.getSource()==Male)
		{
			gender=true;
		}
		else if(e.getSource()==Female)
		{
			gender=false;
		}
		else if(e.getSource()==setting1)
		{
			bmrSetting=0;
		}
		else if(e.getSource()==setting2)
		{
			bmrSetting=1;
		}
		else if(e.getSource()==setting3)
		{
			bmrSetting=2;
		}
		else if(e.getSource()==Metric)
		{
			unitSetting=true;
		}
		else if(e.getSource()==Imperial)
		{
			unitSetting=false;
		}
		else if(e.getSource()==Create || e.getSource()==confirm)
		{
			try {
				height =  Double.parseDouble(Height.getText());
				weight =  Double.parseDouble(Weight.getText());
				fatlevel =  Double.parseDouble(BodyFat.getText());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(frame, "Please enter valid numbers");
			}
			Birth = date.getText();
			try {
				birth = new Date(Integer.parseInt(Birth.substring(0,4)),Integer.parseInt(Birth.substring(5,7))-1,Integer.parseInt(Birth.substring(8,10)));
			} catch (StringIndexOutOfBoundsException ex) {
				JOptionPane.showMessageDialog(frame, "Incorrect date format");
			} catch (NumberFormatException exc) {
				JOptionPane.showMessageDialog(frame, "Please enter valid date");
			}

			try {
				if (height <= 0 || weight <= 0 || fatlevel <= 0) {
					JOptionPane.showMessageDialog(frame, "Please enter valid numbers");
				} else if (bmrSetting < 0) {
					JOptionPane.showMessageDialog(frame, "Please select a bmr setting");
				}
				else {
					if (e.getSource()==Create) {
						currProfile = new Profile(gender, birth, height, weight, bmrSetting);
						currProfile.setFatLvl(fatlevel);
						currProfile.setUnit(unitSetting);
						JOptionPane.showMessageDialog(frame, "Profile Created");
					}
					else {
						currProfile.setSex(gender);
						currProfile.setBirth(birth);
						currProfile.setHeight(height);
						currProfile.setWeight(weight);
						currProfile.setCalcMethod(bmrSetting);
						currProfile.setFatLvl(fatlevel);
						currProfile.setUnit(unitSetting);
						JOptionPane.showMessageDialog(frame, "Profile Changed");
					}
					DBQuery.storeProfile(currProfile);
					System.out.println(DBQuery.getUsers());
				}
			} catch (NullPointerException exception) {
				JOptionPane.showMessageDialog(frame, "Please fill in all fields correctly");
			}
			//front.list.add(currProfile.getUserID());
		}
		else if(e.getSource()==New_profile)
		{
			frame.setVisible(false);
			ProfileGui profileGUIInstance = new ProfileGui(front);
			profileGUIInstance.frame.setVisible(true);
		}
		else if(e.getSource()==Edit_Profile)
		{
			frame.setVisible(false);
			ProfileGui profileGUIInstance = new ProfileGui(front, currProfile);
			profileGUIInstance.frame.setVisible(true);
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub

		JTextField textField = (JTextField) e.getSource();
		if ( textField.getText().equals("Weight") || textField.getText().equals("Height") || textField.getText().equals("BodyFat%")
				|| textField.getText().equals("yyyy/mm/dd")) {
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
				textField.setText("yyyy/mm/dd");
			}
		}
	}}
