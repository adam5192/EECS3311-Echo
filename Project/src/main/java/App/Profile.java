package App;

import java.util.*;
import javax.naming.directory.InvalidAttributesException;

@SuppressWarnings("deprecation")
//Date values of:
//Year as (year - 1900) (automatically corrected aside from default )
//Month from 0-11 (corrected in log.toString() but be mindful of month in)
//Date is as normal.

public class Profile {
	private static int nextId = 0;

	// Data
	private int userId; // Keeps track of which profile is being opened
	private String name;
	private boolean sex; // true = male, false = female
	private Date birth;
	private double height; // In centimeters
	private double weight; // In kg
	private double fatLvl; // For Karth-McArdle's BMR Calc, range: 0-100
	private List<Log> history;
	private double bmrVal;

	// Settings
	public boolean isMetric; // True = Metric, False = Imperial
	private int bmrSetting; // 0 = Miffin St Jeor, 1 = Revised Harris-Benedict, 2 = Katch McArdle

	// Basic constructors
	public Profile(boolean sex, Date birth, double height, double weight, int bmrSetting) {
		// Data
		this.sex = sex;
		this.birth = birth;
		this.height = height;
		this.weight = weight;
		this.fatLvl = 0;

		// Log Update
		Date logDate = new Date();
		logDate.setYear(logDate.getYear() + 1900);
		logDate.setMonth(logDate.getMonth());
		history = new LinkedList<Log>();
		history.add(new DataLog(this.height, this.weight, logDate, this.userId));

		// Settings
		userId = nextId++;
		isMetric = true; // Default in Metric
		this.bmrSetting = bmrSetting;

		// Calculate BMR value
		bmrVal = CalculateBMR.calculateBMR(birth, weight, height, sex, bmrSetting, fatLvl);
	}

	// Recreating profile saved in the database
	public Profile(String name, boolean sex, Date birth, double height, double weight, int bmrSetting, int userID) {
		// Settings
		this.userId = userID;
		isMetric = true; // Default in Metric
		bmrSetting = 0;

		// Data
		this.name = name;
		this.sex = sex;
		this.birth = birth;
		this.setHeight(height);
		this.setWeight(weight);

		// Calculate BMR Value
		bmrVal = CalculateBMR.calculateBMR(birth, weight, height, sex, bmrSetting, fatLvl);
	}

	// Setters
	public void setName(String name) {
		this.name = name;
	}

	public void setBMR(int bmr) {
		this.bmrVal = bmr;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	// Two options: Use whatever more convenient (or just ask for a specific one)
	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public void setBirth(int year, int month, int day) {
		this.birth = new Date(year, month, day);
	}

	// Conversion rate: 1m = 3.281ft
	public void setHeight(double height) {
		try {
			Date logDate = new Date();
			// Adjustments to date values for the correct display
			logDate.setYear(logDate.getYear() + 1900);

			if (isMetric)
				this.height = height;
			else {
				this.height = (height / 3.281) / 100.0; // Convert feet to centimeters
			}
			history.add(new DataLog(this.height, this.weight, logDate, this.userId));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Conversion rate: 1kg = 2.204bs
	public void setWeight(double weight) {
		try {
			Date logDate = new Date();
			// Adjustments to date values for the correct display
			logDate.setYear(logDate.getYear() + 1900);

			if (isMetric)
				this.weight = weight;
			else
				this.weight = weight / 2.204; // Convert pounds input to centimeters
			history.add(new DataLog(this.height, this.weight, logDate, this.userId));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setFatLvl(double fatLvl) {
		try {
			if (fatLvl < 0.0 || fatLvl > 100.0)
				throw new InvalidAttributesException("Invalid fatLvl value, value must be between 0.0 to 100.0");
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.fatLvl = fatLvl;
	}

	public void setUnit(boolean isMetric) {
		this.isMetric = isMetric;
	}

	public void setCalcMethod(int bmrSetting) {
		try {
			if (bmrSetting < 0 || bmrSetting > 2)
				throw new InvalidAttributesException("Invalid bmrSetting value, value must be between 1 to 3.");
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.bmrSetting = bmrSetting;
	}

	// Sets the history log of a profile to a pre-generated history
	public void setHistory(List<Log> history) {
		this.history = history;
	}

	// Getters
	public int getUserID() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public boolean getSex() {
		return sex;
	}

	public Date getBirth() {
		return birth;
	}

	public double getHeight() {
		return height;
	}

	public double getWeight() {
		return weight;
	}

	public double getFatLvl() {
		return fatLvl;
	}

	public double getBMR() {
		return bmrVal;
	}

	public boolean getIsMetric() {
		return isMetric;
	}

	// Not sure if calc method should just return the name or the value
	public int getCalcMethod() {
		return bmrSetting;
	}

	public List<Log> getHistory() {
		return history;
	}

	public String getHistoryString() {
		String out = "";
		for (int i = 0; i < history.size(); i++)
			out += history.get(i).toString() + "\n";
		return out;
	}

	// Log creation and management
	/**
	 * Overloaded method for adding a new log to the profile.
	 */
	public void addLog(double height, double weight, Date logDate) {
		history.add(new DataLog(height, weight, logDate, this.userId));
	}

	public void addLog(String mealName, Ingredient[] ingredients, String mealType, Date logDate) {
		MealLog meal = new MealLog(mealName, mealType, logDate, this.userId);
		for (Ingredient i : ingredients) {
			if (i != null)
				meal.addIngredient(i);
		}
		history.add(meal);
	}

	public void addLog(String exerciseName, int caloBurnt, double time, Date logDate) {
		history.add(new ExerciseLog(exerciseName, caloBurnt, time, logDate, this.userId));
	}

	/**
	 * Remove the first log that matched (based on specified date and type) from
	 * history and returns it. Returns null if none matches.
	 * 
	 * @param type 0 for any log, 1 for DataLog, 2 for MealLog, 3 for ExerciseLog
	 */
	public Log removeLog(Date logDate, int type) {
		// TODO: Implement better search algorithm (Quicksearch)
		Log removedLog = null;

		// type: 0 = all, 1 = data, 2 = meals, 3 = exercise, default: do nothing
		for (int i = 0; i < history.size(); i++) {
			if (history.get(i).getLogType() == type) {
				removedLog = history.remove(i);
				break;
			}
		}

		return removedLog;
	}

	// Temp test method
	public static void main(String args[]) {
		// Creation
		// Profile user0 = new Profile(); //Default
		Profile user1 = new Profile(false, new Date(1974, 06, 10), 155.0, 50.0, 1);

		// Getters
		// if (user0.getSex() && user0.getBirth() == null && user0.getHeight() == 0.0 &&
		// user0.getWeight() == 0.0)
		System.out.println("Correct default values.");

		if (!user1.getSex() && user1.getBirth().equals(new Date(1974, 06, 10))
				&& user1.getHeight() == (155.0 * 3.281 / 100) && user1.getWeight() == 50.0 * 2.2046)
			System.out.println("Correct data assignments.");

		if (!user1.isMetric && user1.getFatLvl() == 0)
			System.out.println("Correct default settings.");

		// Setters
		user1.setBirth(2002, 03, 24);
		if (user1.getBirth().equals(new Date(2002, 03, 24)))
			System.out.println("Correct setBirth(int, int, int).");

		user1.setBirth(new Date(2023, 10, 06));
		if (user1.getBirth().equals(new Date(2023, 10, 06)))
			System.out.println("Correct setBirth(Date).");

		user1.setFatLvl(20);
		if (user1.getFatLvl() == 20)
			System.out.println("Correct setFatLvl.");

		user1.isMetric = true;
		user1.setHeight(123.0);
		user1.setWeight(75.0);
		if (user1.getHeight() == 123.0)
			System.out.println("Correct setHeight().");
		if (user1.getWeight() == 75.0)
			System.out.println("Correct setWeight().");

		user1.isMetric = false;
		if (user1.getHeight() == 123.0 * 3.281 / 100)
			System.out.println("Correct setHeight().");
		if (user1.getWeight() == 75.0 * 2.2046)
			System.out.println("Correct setWeight().");

		// Add, remove logs
		Date today = new Date();
		user1.addLog(170.0, 120.0, today);
		String[] list = new String[5];
		list[0] = "milk";
		Ingredient tomato = new Ingredient("Tomato", 20, 0, 1, 6, 0, 1);
		Ingredient bread = new Ingredient("Bread", 255, 3, 8, 51, 0, 1);
		Ingredient egg = new Ingredient("Egg", 145, 11, 14, 2, 0, 1);
		Ingredient[] set = new Ingredient[3];
		set[0] = tomato;
		set[1] = bread;
		set[2] = egg;
		user1.addLog("Running", 400, 30, today);
		user1.addLog("meal", set, "Lunch", today);
		user1.addLog("Walking", 400, 30, today);
		user1.addLog("meal", set, "Breakfast", today);
		user1.addLog("meal", set, "Dinner", today);
		user1.addLog("meal", set, "Lunch", today);
		user1.addLog(200, 20.0, today);
		System.out.println(user1.getHistory());
		for (int i = 0; i < user1.getHistory().size(); i++) {
			System.out.println(i + " " + user1.getHistory().get(i));
			System.out.println(user1.getHistory().get(i).getDate());
		} // end for loop
			// 2 == meal
		for (int i = 0; i < user1.getHistory().size(); i++) {
			if (user1.getHistory().get(i).getLogType() == 2) {
				System.out.println(user1.getHistory().get(i).getDate());
				System.out.println(((MealLog) user1.getHistory().get(i)).calculateCalories());
			} // end if statement
		} // end for loop
			// 3 == exercise
		for (int i = 0; i < user1.getHistory().size(); i++) {
			if (user1.getHistory().get(i).getLogType() == 3) {
				System.out.println(user1.getHistory().get(i).getDate());
				System.out.println(((ExerciseLog) user1.getHistory().get(i)).getCaloBurnt());
			}
		} // end for loop
//		user1.removeLog(today, 1);
//		System.out.println(user1.getHistory());

		System.out.println("End of test.");
	}
}